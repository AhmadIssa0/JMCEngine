public class XboardParser{
	boolean turn = true;
	int time=120000;
	SearchAgent sa = new SearchAgent();
	Move tmp[] = new Move[100];
	OpeningBook ob = new OpeningBook();
	String moveSequence = "";
	int moveNumber = 0;
	int order[] = new int[100];
    boolean out_of_book = false;
	
	public XboardParser()
	{
		for (int i=0; i<100; i++)
			tmp[i] = new Move();
	}
	
	static final String m2str[] = {
		"a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
		"a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",	
		"a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",	
		"a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
		"a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
		"a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
		"a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
		"a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1",
	};
	
    // returns false if we got checkmated or game ended
	public boolean parse(String str, Board board)
	{
		if (str.startsWith("protover"))
		{
			System.out.println("feature myname=\"JMCEngine\"");
			System.out.println("feature usermove=1");
			System.out.println("feature done=1");
		} else if (str.startsWith("accept"))
		{
			
		} else if (str.startsWith("result")) 
		{
			turn = true;
		} else if (str.startsWith("reject"))
		{
			
		} else if (str.startsWith("quit"))
		{
			System.exit(1);
		} else if (str.startsWith("force"))
		{
			go(board);
			moveNumber++;
		} else if (str.startsWith("go"))
		{
			go(board);
			moveNumber++;
		} else if (str.startsWith("level"))
		{
			
		} else if (str.startsWith("st")) 
		{
			
		} else if (str.startsWith("sd"))
		{
			
		} else if (str.startsWith("time"))
		{
			time = Integer.parseInt(str.substring(5))*10; // reported in hundredths of a second, we use milliseconds
		} else if (str.startsWith("otim"))
		{
			
		} else if (str.startsWith("hard"))
		{
		
		} else if (str.startsWith("random"))
		{	
		} else if (str.startsWith("draw"))
        {
        
        } else if (str.startsWith("computer"))
		{
			
		} else if (apparentMove(str)) {
            System.out.println("w_castle: " + board.w_castle_rights + " b_castle: " + board.b_castle_rights);
            board.makeMove(strToMove(str.substring(0,4), board));
			moveSequence += str.substring(0,4);
			turn = !turn;
            //System.out.println("hash: " + board.board_hashes[board.move_no-1]);
            //System.out.println("threeMoveRep: " + board.threeMoveRep());
			move(board);
			moveNumber++;
            board.printBoard();
            if (!((turn && board.whiteHasValidMove()) || (!turn && board.blackHasValidMove()))) {
                return false;
            } else {
                return true;
            }
        } else if (str.startsWith("usermove")) { // its a move
			board.makeMove(strToMove(str.substring(9,13), board));
			moveSequence += str.substring(9,13);
			turn = !turn;
			move(board);
			moveNumber++;
			moveNumber++;
            board.printBoard();
            /*
            if (!((turn && board.whiteHasValidMove()) || (!turn && board.blackHasValidMove()))) {
                return false;
            } else {
                return true;
            }*/
		}
        return true;
	}
    
    public boolean apparentMove(String str) {
        if (str.length() < 4 || str.length() > 5) 
            return false;
        if (Character.digit(str.charAt(1), 10) < 0 || Character.digit(str.charAt(3), 10) < 0)
            return false;
        return true;
    }
	
	public void go(Board board)
	{
        Move move = strToMove(ob.start(""), board);
        // play e2e4?
		//Move move = new Move();
		//move.from = 52;
		//move.to = 36;
		board.makeMove(move);
		System.out.println("move " + moveToStr(move));
		moveSequence += moveToStr(move);
		turn = !turn;
	}
	
	public void move(Board board)
	{
        if (!((turn && board.whiteHasValidMove()) || (!turn && board.blackHasValidMove())))
            return;
		String seq = "";
		System.out.println(moveSequence);
		if (!out_of_book && ((seq = ob.start(moveSequence)) != null))
		{
			board.makeMove(strToMove(seq, board));
			moveSequence += seq;
			System.out.println("move " + seq);
            Logger.log("move " + seq);
			turn = !turn;
            moveNumber++;
			return;
		}
        out_of_book = true;
		Move move = new Move();
		try{
		move = sa.searchPos(turn, board, time);
        if (move == null)
            return;
		} catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
		board.makeMove(move);
		System.out.println("move " + moveToStr(move));	
        Logger.log("move " + moveToStr(move));
        Logger.log(board.toString());
		moveSequence += moveToStr(move);
		turn = !turn;	
        moveNumber++;
	}
	
	public static String moveToStr(Move move)
	{
		return m2str[move.from] + m2str[move.to];
	}
	
	public Move strToMove(String str, Board board)
	{
		Move move = new Move();
		
		for (int i=0; i<64; i++)
		{
			if (m2str[i].equals(str.substring(0, 2)))
			{
				move.from = i;
			}
		}
		
		for (int i=0; i<64; i++)
		{
			if (m2str[i].equals(str.substring(2, 4)))
			{
				move.to = i;
			}
		}
		
		MoveGenerator mg = new MoveGenerator();
		int size=0;
		if (turn)
		{
			size = mg.genWhite(board, tmp);
		} else {
			size = mg.genBlack(board, tmp);
		}
		
		for (int i=0; i<size; i++)
		{
			if ((tmp[i].from == move.from) &&
				(tmp[i].to == move.to))
				{
					System.out.println("Validated");
					System.out.println(tmp[i].type);
					return tmp[i];
				}
		}
		
		return move;
	}
}