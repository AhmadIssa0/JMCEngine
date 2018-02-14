
public final class Board {
	

	int board[] = new int[64];
    int wPieces[] = new int[16];
    int bPieces[] = new int[16];
    int nWhitePieces = 16;
    int nBlackPieces = 16;
    
    int w_castle_rights = 0;
    int b_castle_rights = 0;
    static final int ALL_CASTLE = 3;
    static final int Q_CASTLE = 2;
    static final int K_CASTLE = 1;
    static final int NO_CASTLE = 0;
    
	int wpawns[] = new int[20];
	int wknights[] = new int[20];
	int wbishops[] = new int[20];	
	int wrooks[] = new int[20];
	int wqueens[] = new int[20];
	int wking = 0;
	
	int bpawns[] = new int[20];
	int bknights[] = new int[20];
	int bbishops[] = new int[20];
	int brooks[] = new int[20];
	int bqueens[] = new int[20];
	int bking = 0;
	
	static final int EM = 0;
	static final int WP = 1;
	static final int WN = 2;
	static final int WB = 3;
	static final int WR = 4;
	static final int WQ = 5;
	static final int WK = 6;
	static final int BP = -1;
	static final int BN = -2;
	static final int BB = -3;
	static final int BR = -4;
	static final int BQ = -5;
	static final int BK = -6;
	
	BoardState state = new BoardState();
	int EP = -1;
	Move mvs[] = new Move[100]; // used in isWhiteChecked()
    Move mvs2[] = new Move[100]; // used in whiteHasValidMove()
    
    long board_hashes[] = new long[1000]; // used to detect 3-move repetition
    long hash=0;
    int move_no = 0;
    int last_irrev_move_no = 0;
    
    TranspositionTable t = TranspositionTable.getTable();
	
	static final String strPiece[] = {
		" -- ", "P ", "N ", "B ", "R ", "Q ", "K "
	};
	
	static final int column[] = {
		0, 1, 2, 3, 4, 5, 6, 7,
		0, 1, 2, 3, 4, 5, 6, 7,
		0, 1, 2, 3, 4, 5, 6, 7,
		0, 1, 2, 3, 4, 5, 6, 7,
		0, 1, 2, 3, 4, 5, 6, 7,
		0, 1, 2, 3, 4, 5, 6, 7,
		0, 1, 2, 3, 4, 5, 6, 7,
		0, 1, 2, 3, 4, 5, 6, 7		
	};
	
	static final int row[] = {
		7, 7, 7, 7, 7, 7, 7, 7,
		6, 6, 6, 6, 6, 6, 6, 6,
		5, 5, 5, 5, 5, 5, 5, 5,
		4, 4, 4, 4, 4, 4, 4, 4,
		3, 3, 3, 3, 3, 3, 3, 3,
		2, 2, 2, 2, 2, 2, 2, 2,
		1, 1, 1, 1, 1, 1, 1, 1,
		0, 0, 0, 0, 0, 0, 0, 0
	};
	
	static final int initPos[] = {
		BR, BN, BB, BQ, BK, BB, BN, BR,
		BP, BP, BP, BP, BP, BP, BP, BP,
		EM, EM, EM, EM, EM, EM, EM, EM,
		EM, EM, EM, EM, EM, EM, EM, EM,
		EM, EM, EM, EM, EM, EM, EM, EM,
		EM, EM, EM, EM, EM, EM, EM, EM,
		WP, WP, WP, WP, WP, WP, WP, WP,		
		WR, WN, WB, WQ, WK, WB, WN, WR						
	};
	
	public Board()
	{
		for (int i=0; i<100; i++) {
			mvs[i] = new Move();
            mvs2[i] = new Move();
        }
	}
    
    public void setPosition(int pos[]) {
        for (int i=0; i<64; i++)
            this.board[i] = pos[i];
    }
	
	public void setToInit()
	{
		for (int i=0; i<64; i++) {
			board[i] = initPos[i];
		}
        nWhitePieces = 16;
        nBlackPieces = 16;
        for (int i=0; i<16; i++) {
            bPieces[i] = i;
            wPieces[i] = 63-i;
        }
        wking = 60;
        bking = 4;
        w_castle_rights = ALL_CASTLE;
        b_castle_rights = ALL_CASTLE;
        state.w_castle_rights = ALL_CASTLE;
        state.b_castle_rights = ALL_CASTLE;
        hash = t.getHash(this, true);
	}
    
    public String toString() {
        String s = "";
		for (int i=0; i<64; i++) {
			if (i%8==0)
				s += "\r\n";
			if (board[i] == 0) {
				s += strPiece[0];
				continue;
			}
			
			if (board[i] > 0) {
				s += " W"+ strPiece[board[i]];
			} else if (board[i] < 0) {
				s += " B"+ strPiece[-board[i]];		
			}
		}
		s += "\n";
        return s;
    }
	
	public void printBoard()
	{
		for (int i=0; i<64; i++)
		{
			if (i%8==0)
				System.out.println("\n");
			if (board[i] == 0)
			{
				System.out.print(strPiece[0]);
				continue;
			}
			
			if (board[i] > 0)
			{
				System.out.print(" W"+ strPiece[board[i]]);
			} else if (board[i] < 0) {
				System.out.print(" B"+ strPiece[-board[i]]);		
			}
		}
		System.out.println();
	}
	
	public boolean isWhiteChecked()
	{
        return MoveGenerator.isWhiteChecked(this);
	}
    
    public boolean whiteHasValidMove(Move moves[], int size) {
        // try all moves and see if king is still in check in any of them
		for (int i=0; i<size; i++) {
            if (makeMove(moves[i])) {
                if (!isWhiteChecked()) {
                    unmakeMove(state);
                    return true;
                }
                unmakeMove(state);
            }
		}
		return false;
    }
    
    public boolean whiteHasValidMove() {
        // try all moves and see if king is still in check in any of them
		MoveGenerator mg = new MoveGenerator();
		int size = mg.genWhite(this, mvs2);
		for (int i=0; i<size; i++) {
            if (makeMove(mvs2[i])) {
                if (!isWhiteChecked()) {
                    unmakeMove(state);
                    return true;
                }
                unmakeMove(state);
            }
		}
		return false;
    }
    
    public boolean threeMoveRep() {
        int count=0;
        for (int i=0; i<move_no; i++) {
            if (board_hashes[i] == board_hashes[move_no-1])
                count++;
        }
        if (count >= 2) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean blackHasValidMove(Move moves[], int size) {
        // try all moves and see if king is still in check in any of them
		for (int i=0; i<size; i++) {
            if (makeMove(moves[i])) {
                if (!isBlackChecked()) {
                    unmakeMove(state);
                    return true;
                }
                unmakeMove(state);
            }
		}
		return false;
    }

    public boolean blackHasValidMove() {
        // try all moves and see if king is still in check in any of them
		MoveGenerator mg = new MoveGenerator();
		int size = mg.genBlack(this, mvs2);
		for (int i=0; i<size; i++) {
            if (makeMove(mvs2[i])) {
                if (!isBlackChecked()) {
                    unmakeMove(state);
                    return true;
                }
                unmakeMove(state);
            }
		}
		return false;
    }
	
	public boolean isBlackChecked()
	{
        return MoveGenerator.isBlackChecked(this);
	}
    
    public boolean fiftyMoveRule() {
        if (move_no - last_irrev_move_no >= 80)
            return true;
        return false;
    }
    
    // needs to be called before pieces are actually moved
    public void updatePieces(int from, int to) {
        //System.out.println("updating piece: " + from + " to " + to);
        if (board[from] == WK) {
            wking = to;
        } else if (board[from] == BK) {
            bking = to;
        }
        if (board[from] > 0) {
            for (int i=0; i<nWhitePieces; i++) {
                if (wPieces[i] == from) {
                    wPieces[i] = to;
                    break;
                }
            }
            hash ^= t.zobrist[board[from]][0][from];
            hash ^= t.zobrist[board[from]][0][to];
        } else if (board[from] < 0) {
            for (int i=0; i<nBlackPieces; i++) {
                if (bPieces[i] == from) {
                    bPieces[i] = to;
                    break;
                }
            }
            hash ^= t.zobrist[-board[from]][1][from];
            hash ^= t.zobrist[-board[from]][1][to];
        }
        
        // remove captured piece, if any
        if (board[to] > 0) {
            for (int i=0; i<nWhitePieces; i++) {
                if (wPieces[i] == to) {
                    for (int j=i; j<nWhitePieces-1; j++) {
                        wPieces[j] = wPieces[j+1];
                    }
                    break;
                }
            }
            nWhitePieces--;
            hash ^= t.zobrist[board[to]][0][to];
        } else if (board[to] < 0) {
            for (int i=0; i<nBlackPieces; i++) {
                if (bPieces[i] == to) {
                    for (int j=i; j<nBlackPieces-1; j++) {
                        bPieces[j] = bPieces[j+1];
                    }
                    break;
                }
            }
            nBlackPieces--;
            hash ^= t.zobrist[-board[to]][1][to];
        }
    }
    
    // updates wPieces, bPieces
    public void placePiece(int sq, int piece) {
        //System.out.println("placing piece " + piece + " on " + sq);
        if (piece > 0) {
            wPieces[nWhitePieces++] = sq;
            hash ^= t.zobrist[piece][0][sq];
        } else {
            bPieces[nBlackPieces++] = sq;
            hash ^= t.zobrist[-piece][1][sq];
        }
    }
    
    public void removePiece(int sq, int piece) {
        //System.out.println("removing: " + piece + " from "  + sq);
        if (piece > 0) {
            for (int i=0; i<nWhitePieces; i++) {
                if (wPieces[i] == sq) {
                    for (int j=i; j<nWhitePieces-1; j++) {
                        wPieces[j] = wPieces[j+1];
                    }
                    break;
                }
            }
            nWhitePieces--;
            hash ^= t.zobrist[piece][0][sq];
        } else if (piece < 0) {
            for (int i=0; i<nBlackPieces; i++) {
                if (bPieces[i] == sq) {
                    for (int j=i; j<nBlackPieces-1; j++) {
                        bPieces[j] = bPieces[j+1];
                    }
                    break;
                }
            }
            nBlackPieces--;
            hash ^= t.zobrist[-piece][1][sq];
        }
    }
    
    public void printPieces() {
        System.out.print(nWhitePieces + " white pieces: ");
        for (int k=0; k<wPieces.length; k++) {
            System.out.print(wPieces[k] + " ");
        }
        System.out.println();
        System.out.print(nBlackPieces + " black pieces: ");
        for (int k=0; k<bPieces.length; k++) {
            System.out.print(bPieces[k] + " ");
        }
        System.out.println();
    }
	
	public boolean makeMove(Move move)
	{
        boolean isWhite = board[move.from] > 0 ? false : true; // at the end of the move, not start!
        state.PREV_EP = state.EP;

		if (move.promote != 0)
		{
            state.last_irrev_move_no = last_irrev_move_no;
            last_irrev_move_no = move_no;
			state.from = move.from;
			state.to = move.to;
			state.fromPiece = board[move.from];
			state.toPiece = board[move.to];
			state.type = move.type;		
			state.promote = true;
            if (state.EP > 0) {
                hash ^= t.EN_PASSANT[state.EP];
                //System.out.println("undoing EP: " + state.EP);
            }
			state.EP = -1;
            removePiece(move.from, board[move.from]);
            if (board[move.to] != 0)
                removePiece(move.to, board[move.to]);
            //updatePieces(move.from, move.to);
            placePiece(move.to, move.promote);
			board[move.to] = move.promote;
			board[move.from] = EM;
            long my_hash = this.hash;
            if (isWhite) {
                my_hash ^= t.BLACKS_TURN;
            }
            board_hashes[move_no++] = my_hash;
			//hash ^= t.BLACKS_TURN;
			return true;
		}
		
		
		state.promote = false;
		state.from = move.from;
		state.to = move.to;
		state.fromPiece = board[move.from];
		state.toPiece = board[move.to];
		state.type = move.type;
		
		if (move.type == Move.KCASTLE)
		{
			if (board[move.from] > 0) // white
			{
                if ((w_castle_rights & K_CASTLE) == 0)
                    return false;
				if (isWhiteChecked())
				{
					state.type = Move.NORMAL;
					return false;
				}
                // check that castling squares aren't attacked
                 updatePieces(60, 61);
                board[60] = Board.EM;
                board[61] = Board.WK;
                if (isWhiteChecked()) {
                    updatePieces(61, 60);
                    board[60] = Board.WK;
                    board[61] = Board.EM;
                    return false;
                }
                 updatePieces(61, 62);    
                board[61] = Board.EM;
                board[62] = Board.WK;
                if (isWhiteChecked()) {
                     updatePieces(62, 60);
                    board[60] = Board.WK;
                    board[62] = Board.EM;
                    return false;
                }
                updatePieces(63, 61);
				board[60] = Board.EM;
				board[62] = Board.WK;
				board[63] = Board.EM;
				board[61] = Board.WR;
			} else {
                if ((b_castle_rights & K_CASTLE) == 0)
                    return false;
				if (isBlackChecked())
				{
					state.type = Move.NORMAL;
					return false;
				}
                // check that castling squares aren't attacked
                updatePieces(4, 5);
                board[4] = Board.EM;
                board[5] = Board.BK;
                if (isBlackChecked()) {
                     updatePieces(5, 4);
                    board[4] = Board.BK;
                    board[5] = Board.EM;
                    return false;
                }
                updatePieces(5, 6);
                board[5] = Board.EM;
                board[6] = Board.BK;
                if (isBlackChecked()) {
                    updatePieces(6, 4);
                    board[6] = Board.EM;
                    board[4] = Board.BK;
                    return false;
                }
                updatePieces(7, 5);
				board[4] = Board.EM;
				board[6] = Board.BK;
				board[7] = Board.EM;
				board[5] = Board.BR;
			}
            if (state.EP > 0) { // unset EP
                hash ^= t.EN_PASSANT[state.EP];
            }
			state.EP = -1;
            long my_hash = this.hash;
            if (isWhite) {
                my_hash ^= t.BLACKS_TURN;
            }
            board_hashes[move_no++] = my_hash;
            state.w_castle_rights = w_castle_rights;
            state.b_castle_rights = b_castle_rights;
            if (board[move.to] > 0) {
                w_castle_rights = NO_CASTLE;
            } else {
                b_castle_rights = NO_CASTLE;
            }
			//hash ^= t.BLACKS_TURN;
			return true;
		} else if (move.type == Move.QCASTLE)
		{
			if (board[move.from] > 0) // white
			{
                if ((w_castle_rights & Q_CASTLE) == 0)
                    return false;
				if (isWhiteChecked())
				{
					state.type = Move.NORMAL;
					return false;
				}
                // check that castling squares aren't attacked
                updatePieces(60, 59);
                board[60] = Board.EM;
                board[59] = Board.WK;
                if (isWhiteChecked()) {
                    updatePieces(59, 60);
                    board[60] = Board.WK;
                    board[59] = Board.EM;
                    return false;
                }
                updatePieces(59, 58);
                board[59] = Board.EM;
                board[58] = Board.WK;
                if (isWhiteChecked()) {
                    updatePieces(58, 60);
                    board[60] = Board.WK;
                    board[58] = Board.EM;
                    return false;
                }
                updatePieces(56, 59);
				board[60] = Board.EM;
				board[58] = Board.WK;
				board[56] = Board.EM;
				board[57] = Board.EM;
				board[59] = Board.WR;
			} else {
                if ((b_castle_rights & Q_CASTLE) == 0)
                    return false;
				if (isBlackChecked())
				{
					state.type = Move.NORMAL;
					return false;
				}
                // check that castling squares aren't attacked
                updatePieces(4, 3);
                board[4] = Board.EM;
                board[3] = Board.BK;
                if (isBlackChecked()) {
                    updatePieces(3, 4);
                    board[4] = Board.BK;
                    board[3] = Board.EM;
                    return false;
                }
                updatePieces(3, 2);
                board[3] = Board.EM;
                board[2] = Board.BK;
                if (isBlackChecked()) {
                    updatePieces(2, 4);
                    board[4] = Board.BK;
                    board[2] = Board.EM;
                    return false;
                }
                updatePieces(0, 3);
				board[4] = Board.EM;
				board[2] = Board.BK;
				board[0] = Board.EM;
				board[1] = Board.EM;
				board[3] = Board.BR;
			}
            if (state.EP > 0)
                hash ^= t.EN_PASSANT[state.EP];
			state.EP = -1;
            long my_hash = this.hash;
            if (isWhite) {
                my_hash ^= t.BLACKS_TURN;
            }
            board_hashes[move_no++] = my_hash;
            state.w_castle_rights = w_castle_rights;
            state.b_castle_rights = b_castle_rights;
            if (board[move.to] > 0) {
                w_castle_rights = NO_CASTLE;
            } else {
                b_castle_rights = NO_CASTLE;
            }
			//hash ^= t.BLACKS_TURN;
			return true;
		}
        state.w_castle_rights = w_castle_rights;
        state.b_castle_rights = b_castle_rights;
        switch (move.from) {
            case 0: b_castle_rights &= ~Q_CASTLE; // no longer allow queen castle
                    break;
            case 7: b_castle_rights &= ~K_CASTLE; // no longer allow king castle
                    break;
            case 63: w_castle_rights &= ~K_CASTLE;
                     break;
            case 56: w_castle_rights &= ~Q_CASTLE;
                     break;
            case 4: b_castle_rights = NO_CASTLE;
                    break;
            case 60: w_castle_rights = NO_CASTLE;
                    break;
        }
        
        if (state.EP > 0) { // undo ep square
                hash ^= t.EN_PASSANT[state.EP];
                //System.out.println("undoing EP: " + state.EP);
        }

		updatePieces(move.from, move.to);
		board[move.to] = board[move.from];
		board[move.from] = EM;
		
        if (move.type == Move.EN_PASSANT || move.type == Move.CAPTURE || move.type == Move.DOUBLE_PUSH) {
            state.last_irrev_move_no = last_irrev_move_no;
            last_irrev_move_no = move_no;
        }
                        
		if (move.type == Move.EN_PASSANT)
		{
			if (board[state.to] > 0) // white, order here important, assume we already made the move
			{
				state.type = Move.EN_PASSANT;
                removePiece(state.to+8, BP);
				board[state.to+8] = EM;
			} else if (board[state.to] < 0) {
                removePiece(state.to-8, WP);
				board[state.to-8] = EM;
				state.type = Move.EN_PASSANT;
			}
		}
		
		state.EP = -1;
		
		if (move.type == Move.DOUBLE_PUSH)
		{
			if (board[move.to] > 0) // white
			{
				state.EP = move.from-8;
			} else { // black
				state.EP = move.from+8;
			}
		}

		//hash ^= t.BLACKS_TURN;
        if (state.EP > 0) { // set new EP square
            hash ^= t.EN_PASSANT[state.EP];
            //System.out.println("New EP: " + state.EP);
        }
        
        long my_hash = this.hash;
        if (isWhite) {
            my_hash ^= t.BLACKS_TURN;
        }
        board_hashes[move_no++] = my_hash;
		return true;
	}
	
	public void unmakeMove(BoardState state)
	{
        //System.out.println("entering unmake with hash: " + hash);
    	//hash ^= t.BLACKS_TURN;
        //System.out.println("changed turn: " + hash);
        
        if (state.EP > 0) {
            //System.out.println("Unsetting EP square: " + state.EP);
            hash ^= t.EN_PASSANT[state.EP];
        }
        if (state.PREV_EP > 0) { // set new EP square
            //System.out.println("Setting EP square: " + state.PREV_EP);
            hash ^= t.EN_PASSANT[state.PREV_EP];
        }
            
        //System.out.println("changed en passant: " + hash);
        
        move_no--;
        last_irrev_move_no = state.last_irrev_move_no;
        BoardState move = state;
        this.state.EP = move.PREV_EP;
		if (move.promote)
		{
            //System.out.println("unmaking PROMOTE");
            //updatePieces(move.to, move.from);
			if (board[move.to] > 0) // if white is the one who promoted
			{
                removePiece(move.to, WQ);
                placePiece(move.from, WP);
				board[move.from] = WP;
			} else {
                removePiece(move.to, BQ);
                placePiece(move.from, BP);
				board[move.from] = BP;
			}
            if (move.toPiece != 0)
                placePiece(move.to, move.toPiece);
			board[move.to] = move.toPiece;
			this.state.promote = false;
			this.state.EP = -1;
            this.state.PREV_EP = -1;
			return;
		}
		
		
		if (move.type == Move.KCASTLE)
		{
			if (board[move.to] > 0) // white
			{
                updatePieces(61, 63);
                updatePieces(62, 60);
				board[60] = Board.WK;
				board[62] = Board.EM;
				board[63] = Board.WR;
				board[61] = Board.EM;
			} else {
                updatePieces(6, 4);
                updatePieces(5, 7);
				board[4] = Board.BK;
				board[6] = Board.EM;
				board[7] = Board.BR;
				board[5] = Board.EM;
			}
            this.state.PREV_EP = -1;
            w_castle_rights = state.w_castle_rights;
            b_castle_rights = state.b_castle_rights;
			return;
		} else if (move.type == Move.QCASTLE)
		{
			if (board[move.to] > 0) // white
			{
                updatePieces(58, 60);
                updatePieces(59, 56);
				board[60] = Board.WK;
				board[58] = Board.EM;
				board[56] = Board.WR;
				board[59] = Board.EM;
				board[57] = Board.EM;
			} else {
                updatePieces(2, 4);
                updatePieces(3, 0);
				board[4] = Board.BK;
				board[2] = Board.EM;
				board[0] = Board.BR;
				board[3] = Board.EM;
				board[1] = Board.EM;
			}
            this.state.PREV_EP = -1;
            w_castle_rights = state.w_castle_rights;
            b_castle_rights = state.b_castle_rights;
			return;
		}
		
		if (state.type == Move.EN_PASSANT)
		{
			if (state.fromPiece > 0) // if white captured with en passant
			{
                placePiece(state.to+8, BP);
				board[state.to+8] = BP;
			} else if (state.fromPiece < 0) // if black captured with en passant
			{
                placePiece(state.to-8, WP);
				board[state.to-8] = WP;
			}
		}
        updatePieces(state.to, state.from);
        if (state.toPiece != 0) {
            placePiece(state.to, state.toPiece);
            //System.out.println("placing: " + state.toPiece + " on " + state.to);
        }
		board[state.to] = state.toPiece;
		board[state.from] = state.fromPiece;
        this.state.PREV_EP = -1;
        w_castle_rights = state.w_castle_rights;
        b_castle_rights = state.b_castle_rights;
		state.type = -1;	
        //System.out.println("EXITING WITH HASH: " + hash);
	}
	
}