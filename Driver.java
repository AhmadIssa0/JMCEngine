
import java.io.*;

public class Driver {
		FileOutputStream fos;
		BufferedWriter bw;
	
	public Driver()
	{
		Board board = new Board();
		board.setToInit();
		MoveGenerator mg = new MoveGenerator();
        /*
        Move moves[] = new Move[100];
        for (int i=0; i<100; i++) moves[i] = new Move();
        int size = mg.genWhite(board, moves);
        for (int j=0; j<size; j++) {
        board.makeMove(moves[j]);
                if (TranspositionTable.getHash(board, false) != board.hash)
                        System.out.println("different hashes");
        board.printBoard();
        board.unmakeMove(board.state);
        }
        
        mg.genBlack(board, moves);
        BoardState bs = board.state;
        board.makeMove(moves[5]);
                if (TranspositionTable.getHash(board, true) != board.hash)
                        System.out.println("different hashes");
        board.printBoard();
        board.unmakeMove(board.state);
                        if (TranspositionTable.getHash(board, false) != board.hash)
                        System.out.println("different hashes");
        board.printBoard();
        */
        
		XboardParser xboard = new XboardParser();
        
        
		try{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
 		fos = new FileOutputStream("errn.txt", false);
 		bw = new BufferedWriter(new OutputStreamWriter(fos));
        System.setErr(new TeePrintStream(new FileOutputStream("out.txt"), System.err));
		String str="";

		while ((str = br.readLine()) != null)
		{
            Logger.log(str);
            if (str.startsWith("unmake")) // unmakemove
            {
            System.out.println("from: " + board.state.from + " to: " + board.state.to);
			board.unmakeMove(board.state);
            System.out.println("unmaking");
            xboard.turn = !xboard.turn;
            board.printBoard();
            }
            
            if (str.startsWith("usermove"))
                Logger.log(board.toString());
			if (str.startsWith("ne") || str.startsWith("post") || str.startsWith("force"))
			{
                Logger.log("New game");
				board = new Board();
				board.setToInit();
                xboard = new XboardParser();
				continue;
			}
			
				bw.write(str);
				bw.newLine();
				bw.flush();				

			if (!xboard.parse(str, board)) {
                Logger.log("Game ended creating new board");
				board = new Board();
				board.setToInit();
                xboard = new XboardParser();
				continue;
            }
		}
		} catch (Exception e) {
			e.printStackTrace();
			try{
			StackTraceElement[] ste = e.getStackTrace();
				bw.write(e.getMessage());
				bw.newLine();
				bw.flush();
			for (int i=0; i<ste.length; i++)
			{
				bw.write(ste[i].getMethodName() + " at line " + ste[i].getLineNumber());
				bw.newLine();
				bw.flush();
			}
			} catch (Exception z) {}
			board.printBoard();
		}
		
		System.out.println("ending program");
		
		
		/*
		Move moves[] = new Move[100];
		for (int i=0; i<100; i++)
			moves[i] = new Move();
			
		BoardState bs = new BoardState();
		
		int i=0;
		long prev = System.currentTimeMillis();
		while ((System.currentTimeMillis() - prev) < 10000)
		{
			i++;
			mg.genWhite(board, moves);
		}
		
		System.out.println(i/10);
		
		board.printBoard();
		*/
	}
	
	public static void main(String[] args)
	{
		new Driver();
	}
}
