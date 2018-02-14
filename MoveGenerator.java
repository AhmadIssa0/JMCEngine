
public class MoveGenerator {
	static final int CAP_VAL = 10;
    static final int KILLER_VAL = 30;
    
    public static boolean isWhiteChecked(Board board) {
        int i = board.wking;
                for (int k=0; k<KnightMove.KnightMove[i].length; k++) {
                    if (board.board[KnightMove.KnightMove[i][k]] == Board.BN) 
                        return true;
                }
                
                for (int l=0; l<4; l++) {
                    for (int k=0; k<BishopMove.BishopMove[i][l].length; k++) {
                        int piece = board.board[BishopMove.BishopMove[i][l][k]];
                        if (piece == board.BB || piece == board.BQ)
                            return true;
                        if (piece != 0)
                            break;
                    }
                }
                
                for (int l=0; l<4; l++) {
                    for (int k=0; k<RookMove.RookMove[i][l].length; k++) {
                        int piece = board.board[RookMove.RookMove[i][l][k]];
                        if (piece == board.BR || piece == board.BQ)
                            return true;
                        if (piece != 0)
                            break;
                    }
				}
                
                for (int k=0; k<KingMove.KingMove[i].length; k++) {
                    if (board.board[KingMove.KingMove[i][k]] == board.BK)
                        return true;
                }
                
                if (board.column[i] < 7 && i-7 >= 0 && board.board[i-7] == board.BP)
                    return true;
                    
                if (board.column[i] > 0 && i-9 >= 0 && board.board[i-9] == board.BP)
                    return true;

        return false;
    }
    
    public static boolean isBlackChecked(Board board) {
        int i = board.bking;
                for (int k=0; k<KnightMove.KnightMove[i].length; k++) {
                    if (board.board[KnightMove.KnightMove[i][k]] == Board.WN) 
                        return true;
                }
                
                for (int l=0; l<4; l++) {
                    for (int k=0; k<BishopMove.BishopMove[i][l].length; k++) {
                        int piece = board.board[BishopMove.BishopMove[i][l][k]];
                        if (piece == board.WB || piece == board.WQ) 
                            return true;
                        if (piece != 0)
                            break;
                    }
                }
                
                for (int l=0; l<4; l++) {
                    for (int k=0; k<RookMove.RookMove[i][l].length; k++) {
                        int piece = board.board[RookMove.RookMove[i][l][k]];
                        if (piece == board.WR || piece == board.WQ)
                            return true;
                        if (piece != 0)
                            break;
                    }
				}
                
                for (int k=0; k<KingMove.KingMove[i].length; k++) {
                    if (board.board[KingMove.KingMove[i][k]] == board.WK)
                        return true;
                }
                
                if (board.column[i] < 7 && i+9 < 64 && board.board[i+9] == board.WP)
                    return true;
                    
                if (board.column[i] > 0 && i+7 < 64 && board.board[i+7] == board.WP)
                    return true;
                    
        return false;
    }
    
    // generates captures and promotions
    public static int genWhiteCaptures(Board board, Move moves[]) {
		int counter = 0;
		/////////////////////////// pawns //////////////////////////////////////
		for (int j=0; j<board.nWhitePieces; j++)
		{
            int i = board.wPieces[j];
			switch (board.board[i])
			{
				case Board.WP:
				{
			if (board.board[i-8] == Board.EM && board.row[i] == 6) {
				moves[counter].from = i;
				moves[counter].to = i-8;
				moves[counter].type = Move.NORMAL;
				moves[counter].value = 	Evaluator.WP_MAX[moves[counter].to] - 
										Evaluator.WP_MAX[moves[counter].from];
			    moves[counter].promote = Board.WQ;
                moves[counter].value += Evaluator.W_QUEEN_VAL - Evaluator.W_PAWN_VAL;
                counter++;
			}
			
			if (board.column[i] > 0) // can pawn capture in this direction
			{
				if (board.board[i-9] < Board.EM)
				{
					moves[counter].promote = 0;
					moves[counter].from = i;
					moves[counter].to = i-9;
                    int toPiece = Math.abs(board.board[moves[counter].to]);
					moves[counter].value = Evaluator.P_VALUE[toPiece] - Evaluator.B_PAWN_VAL + CAP_VAL;
					moves[counter].type = Move.CAPTURE;
                    if (board.row[i] == 6) {// promotion
                        moves[counter].promote = Board.WQ;
                        moves[counter].value += Evaluator.W_QUEEN_VAL;
                    }
					counter++;
				} else if (board.row[i] == 4 && board.state.EP == i-9) {
                    moves[counter].promote = 0;
                    moves[counter].from = i;
                    moves[counter].to = i-9;
                    moves[counter].value = CAP_VAL;
                    moves[counter].type = Move.EN_PASSANT;
                    counter++;
                }
			}
			
			
			if (board.column[i] < 7)
			{
				if (board.board[i-7] < Board.EM)
				{
					moves[counter].promote = 0;
					moves[counter].from = i;
					moves[counter].to = i-7;
                    int toPiece = Math.abs(board.board[moves[counter].to]);
					moves[counter].value = Evaluator.P_VALUE[toPiece] - Evaluator.B_PAWN_VAL + CAP_VAL;
					moves[counter].type = Move.CAPTURE;
                    if (board.row[i] == 6) { // promotion
                        moves[counter].promote = Board.WQ;
                        moves[counter].value += Evaluator.W_QUEEN_VAL;
                    }
					counter++;
				} else if (board.row[i] == 4 && board.state.EP == i-7) {
                    moves[counter].promote = 0;
                    moves[counter].from = i;
                    moves[counter].to = i-7;
                    moves[counter].value = CAP_VAL;
                    moves[counter].type = Move.EN_PASSANT;
                    counter++;
                }
			}
				break;
			}
		
		
		////////////////////////// knights ///////////////////////////////////
			case Board.WN:
			{
			for (int k=0; k<KnightMove.KnightMove[i].length; k++)
			{
				if (board.board[KnightMove.KnightMove[i][k]] < 0)
				{
					moves[counter].from = i;
					moves[counter].to = KnightMove.KnightMove[i][k];
                    moves[counter].value = 	Evaluator.WN_MAX[moves[counter].to] - 
                                                Evaluator.WN_MAX[moves[counter].from];
					moves[counter].promote = 0;
					moves[counter].type = Move.CAPTURE;
                    int toPiece = Math.abs(board.board[moves[counter].to]);
                    moves[counter].value += Evaluator.P_VALUE[toPiece] - Evaluator.B_KNIGHT_VAL + CAP_VAL;
					counter++;
				}
			}
				break;
			}
		
		////////////////////////// bishops //////////////////////////////////
		case Board.WB:
		{
			for (int l=0; l<4; l++)
			{
				for (int k=0; k<BishopMove.BishopMove[i][l].length; k++)
				{
					if (board.board[BishopMove.BishopMove[i][l][k]] > 0) { // same colour piece
                        break;
					} else if (board.board[BishopMove.BishopMove[i][l][k]] < 0) {
						moves[counter].from = i;
						moves[counter].to = BishopMove.BishopMove[i][l][k];
						moves[counter].value = 	Evaluator.WB_MAX[moves[counter].to] - 
										Evaluator.WB_MAX[moves[counter].from];
						moves[counter].promote = 0;					
						moves[counter].type = Move.CAPTURE;
                        int toPiece = Math.abs(board.board[moves[counter].to]);
                        moves[counter].value += Evaluator.P_VALUE[toPiece] - Evaluator.B_BISHOP_VAL + CAP_VAL;
					    counter++;
					    break;
					}
				}
			}
			break;
		}
		
		/////////////////////////// rooks //////////////////////////////////
		case Board.WR:
		{
			for (int l=0; l<4; l++)
			{
				for (int k=0; k<RookMove.RookMove[i][l].length; k++)
				if (board.board[RookMove.RookMove[i][l][k]] > 0) {
                    break;
                } else if (board.board[RookMove.RookMove[i][l][k]] < 0) {
					moves[counter].from = i;
					moves[counter].to = RookMove.RookMove[i][l][k];
					moves[counter].value = 	Evaluator.WR_MAX[moves[counter].to] - 
										Evaluator.WR_MAX[moves[counter].from];
					moves[counter].promote = 0;
					moves[counter].type = Move.CAPTURE;
                    int toPiece = Math.abs(board.board[moves[counter].to]);
                    moves[counter].value += Evaluator.P_VALUE[toPiece] - Evaluator.B_ROOK_VAL + CAP_VAL;
					counter++;
					break;
				}
			}
			break;
		}
		
		/////////////////////// kings! /////////////////////////////////////
		case Board.WK:
		{
			/* this part isn't confirmed.. it may be slow
			 */
			for (int k=0; k<KingMove.KingMove[i].length; k++)
			{
				if (board.board[KingMove.KingMove[i][k]] < 0)
				{
					moves[counter].from = i;
					moves[counter].to = KingMove.KingMove[i][k];
					moves[counter].value = 	Evaluator.WK_MAX[moves[counter].to] - 
										Evaluator.WK_MAX[moves[counter].from];
					moves[counter].promote = 0;
                    int toPiece = Math.abs(board.board[moves[counter].to]);
                    moves[counter].value += Evaluator.P_VALUE[toPiece] - Evaluator.B_PAWN_VAL + CAP_VAL;
				    moves[counter].type = Move.CAPTURE;
					counter++;
				}
			}
			break;
		}
		
		/////////////////////////// queens /////////////////////////////////////
		case Board.WQ:
		{
			for (int l=0; l<4; l++)
			{
				for (int k=0; k<RookMove.RookMove[i][l].length; k++)
				{
					if (board.board[RookMove.RookMove[i][l][k]] > 0) {
                        break;
                    } else if (board.board[RookMove.RookMove[i][l][k]] < 0) {
						moves[counter].from = i;
						moves[counter].to = RookMove.RookMove[i][l][k];
						moves[counter].value = 	Evaluator.WQ_MAX[moves[counter].to] - 
										Evaluator.WQ_MAX[moves[counter].from];
						moves[counter].promote = 0;
						int toPiece = Math.abs(board.board[moves[counter].to]);
                        moves[counter].value += Evaluator.P_VALUE[toPiece] - Evaluator.B_QUEEN_VAL + CAP_VAL;
						moves[counter].type = Move.CAPTURE;
						counter++;
						break;
					}
				}
			}
			
			
			for (int l=0; l<4; l++)
			{
				for (int k=0; k<BishopMove.BishopMove[i][l].length; k++)
				{
					if (board.board[BishopMove.BishopMove[i][l][k]] > 0) {
                        break;
                    } else if (board.board[BishopMove.BishopMove[i][l][k]] < 0) {
						moves[counter].from = i;
						moves[counter].to = BishopMove.BishopMove[i][l][k];
						moves[counter].value = 	Evaluator.WQ_MAX[moves[counter].to] - 
										Evaluator.WQ_MAX[moves[counter].from];		
						moves[counter].promote = 0;		
						moves[counter].type = Move.CAPTURE;
                        int toPiece = Math.abs(board.board[moves[counter].to]);
                        moves[counter].value += Evaluator.P_VALUE[toPiece] - Evaluator.B_QUEEN_VAL + CAP_VAL;
					    counter++;
						break;
					}
				}
			}
			break;	
			}
		} // end switch
		}
        
		return counter;
    }
	
	public static int genWhite(Board board, Move moves[])
	{
		int counter = 0;
		/////////////////////////// pawns //////////////////////////////////////
		for (int j=0; j<board.nWhitePieces; j++)
		{
            int i = board.wPieces[j];
			switch (board.board[i])
			{
				case Board.WP:
				{
			if (board.board[i-8] == Board.EM)
			{
				moves[counter].from = i;
				moves[counter].to = i-8;
				moves[counter].value = 	Evaluator.WP_MAX[moves[counter].to] - 
										Evaluator.WP_MAX[moves[counter].from];
				moves[counter].type = Move.NORMAL;
				moves[counter].promote = 0;
				
				if (board.row[i] == 6) // promotion
				{
					moves[counter].promote = Board.WQ;
                    moves[counter].value += Evaluator.W_QUEEN_VAL - Evaluator.W_PAWN_VAL;
				}
                counter++;
				
				if 	((i > 47) &&
					(board.board[i-16] == Board.EM))
				{
					moves[counter].promote = 0;
					moves[counter].from = i;
					moves[counter].to = i-16;
					moves[counter].type = Move.DOUBLE_PUSH;
					moves[counter].promote = 0;
					moves[counter].value = 	Evaluator.WP_MAX[moves[counter].to] - 
										Evaluator.WP_MAX[moves[counter].from];
					counter++;
					
				}
			}
			
			if (board.column[i] > 0) // can pawn capture in this direction
			{
				if (board.board[i-9] < Board.EM)
				{
					moves[counter].promote = 0;
					moves[counter].from = i;
					moves[counter].to = i-9;
                    int toPiece = Math.abs(board.board[moves[counter].to]);
					moves[counter].value = Evaluator.P_VALUE[toPiece] - Evaluator.B_PAWN_VAL + CAP_VAL;
					moves[counter].type = Move.CAPTURE;
                    if (board.row[i] == 6) {// promotion
                        moves[counter].promote = Board.WQ;
                        moves[counter].value += Evaluator.W_QUEEN_VAL;
                    }
					counter++;
				} else if (board.row[i] == 4 && board.state.EP == i-9) {
                    moves[counter].promote = 0;
                    moves[counter].from = i;
                    moves[counter].to = i-9;
                    moves[counter].value = CAP_VAL;
                    moves[counter].type = Move.EN_PASSANT;
                    counter++;
                }
			}
			
			
			if (board.column[i] < 7)
			{
				if (board.board[i-7] < Board.EM)
				{
					moves[counter].promote = 0;
					moves[counter].from = i;
					moves[counter].to = i-7;
                    int toPiece = Math.abs(board.board[moves[counter].to]);
					moves[counter].value = Evaluator.P_VALUE[toPiece] - Evaluator.B_PAWN_VAL + CAP_VAL;
					moves[counter].type = Move.CAPTURE;
                    if (board.row[i] == 6) {// promotion
                        moves[counter].promote = Board.WQ;
                        moves[counter].value += Evaluator.W_QUEEN_VAL;
                    }
					counter++;
				} else if (board.row[i] == 4 && board.state.EP == i-7) {
                    moves[counter].promote = 0;
                    moves[counter].from = i;
                    moves[counter].to = i-7;
                    moves[counter].value = CAP_VAL;
                    moves[counter].type = Move.EN_PASSANT;
                    counter++;
                }
			}
				break;
			}
		
		
		////////////////////////// knights ///////////////////////////////////
			case Board.WN:
			{
			for (int k=0; k<KnightMove.KnightMove[i].length; k++)
			{
				if (board.board[KnightMove.KnightMove[i][k]] < 1)
				{
					moves[counter].from = i;
					moves[counter].to = KnightMove.KnightMove[i][k];
                    moves[counter].value = 	Evaluator.WN_MAX[moves[counter].to] - 
                                                Evaluator.WN_MAX[moves[counter].from];
					moves[counter].promote = 0;
					if (board.board[KnightMove.KnightMove[i][k]] < 0)
					{
						moves[counter].type = Move.CAPTURE;
                        int toPiece = Math.abs(board.board[moves[counter].to]);
                        moves[counter].value += Evaluator.P_VALUE[toPiece] - Evaluator.B_KNIGHT_VAL + CAP_VAL;
					} else {
						moves[counter].type = Move.NORMAL;
					}
					counter++;
				}
			}
				break;
			}
		
		////////////////////////// bishops //////////////////////////////////
		case Board.WB:
		{
			for (int l=0; l<4; l++)
			{
				for (int k=0; k<BishopMove.BishopMove[i][l].length; k++)
				{
					if (board.board[BishopMove.BishopMove[i][l][k]] < 1)
					{
						moves[counter].from = i;
						moves[counter].to = BishopMove.BishopMove[i][l][k];
						moves[counter].value = 	Evaluator.WB_MAX[moves[counter].to] - 
										Evaluator.WB_MAX[moves[counter].from];
					
						moves[counter].type = Move.NORMAL;	
						moves[counter].promote = 0;					
						if (board.board[BishopMove.BishopMove[i][l][k]] < 0)
						{
							moves[counter].type = Move.CAPTURE;
                            int toPiece = Math.abs(board.board[moves[counter].to]);
                            moves[counter].value += Evaluator.P_VALUE[toPiece] - Evaluator.B_BISHOP_VAL + CAP_VAL;
							counter++;
							break;
						}
						counter++;
					} else break;
				}
			}
			break;
		}
		
		/////////////////////////// rooks //////////////////////////////////
		case Board.WR:
		{
			for (int l=0; l<4; l++)
			{
				for (int k=0; k<RookMove.RookMove[i][l].length; k++)
				if (board.board[RookMove.RookMove[i][l][k]] < 1)
				{
					moves[counter].from = i;
					moves[counter].to = RookMove.RookMove[i][l][k];
					moves[counter].value = 	Evaluator.WR_MAX[moves[counter].to] - 
										Evaluator.WR_MAX[moves[counter].from];
					moves[counter].type = Move.NORMAL;
					moves[counter].promote = 0;
					if  (board.board[RookMove.RookMove[i][l][k]] < 0)
					{
						moves[counter].type = Move.CAPTURE;
                        int toPiece = Math.abs(board.board[moves[counter].to]);
                        moves[counter].value += Evaluator.P_VALUE[toPiece] - Evaluator.B_ROOK_VAL + CAP_VAL;
						counter++;
						break;
					}
					counter++;
				} else break;
			}
			break;
		}
		
		/////////////////////// kings! /////////////////////////////////////
		case Board.WK:
		{
			/* this part isn't confirmed.. it may be slow
			 */
			 
			 if ((board.board[61] == Board.EM) &&
			 	(board.board[62] == Board.EM) &&
			 	(board.board[60] == Board.WK) &&
			 	(board.board[63] == Board.WR))
			 {
			 	moves[counter].from = i;
			 	moves[counter].to = 62;
			 	moves[counter].value = 	90;
			 	moves[counter].type = Move.KCASTLE;
			 	moves[counter].promote = 0;
			 	counter++;
			 }
			 
			 if ((board.board[60] == board.WK) &&
			 	(board.board[59] == board.EM) &&
			 	(board.board[58] == board.EM) &&
			 	(board.board[57] == board.EM) &&
			 	(board.board[56] == board.WR))
			 {
			 	moves[counter].from = i;
			 	moves[counter].to = 58;
			 	moves[counter].value = 	90;
			 	moves[counter].type = Move.QCASTLE;
			 	moves[counter].promote = 0;
			 	counter++;
			 }
			 
			 /*
			  *
			  */
			for (int k=0; k<KingMove.KingMove[i].length; k++)
			{
				if (board.board[KingMove.KingMove[i][k]] < 1)
				{
					moves[counter].from = i;
					moves[counter].to = KingMove.KingMove[i][k];
					moves[counter].type = Move.NORMAL;
					moves[counter].value = 	Evaluator.WK_MAX[moves[counter].to] - 
										Evaluator.WK_MAX[moves[counter].from];
					moves[counter].promote = 0;
					if (board.board[KingMove.KingMove[i][k]] < 0)
					{
                        int toPiece = Math.abs(board.board[moves[counter].to]);
                        moves[counter].value += Evaluator.P_VALUE[toPiece] - Evaluator.B_PAWN_VAL + CAP_VAL;
						moves[counter].type = Move.CAPTURE;
					}
					counter++;
				}
			}
			break;
		}
		
		/////////////////////////// queens /////////////////////////////////////
		case Board.WQ:
		{
			for (int l=0; l<4; l++)
			{
				for (int k=0; k<RookMove.RookMove[i][l].length; k++)
				{
					if (board.board[RookMove.RookMove[i][l][k]] < 1)
					{
						moves[counter].from = i;
						moves[counter].to = RookMove.RookMove[i][l][k];
						moves[counter].type = Move.NORMAL;
						moves[counter].value = 	Evaluator.WQ_MAX[moves[counter].to] - 
										Evaluator.WQ_MAX[moves[counter].from];
						moves[counter].promote = 0;
						if  (board.board[RookMove.RookMove[i][l][k]] < 0)
						{
							int toPiece = Math.abs(board.board[moves[counter].to]);
                            moves[counter].value += Evaluator.P_VALUE[toPiece] - Evaluator.B_QUEEN_VAL + CAP_VAL;
							moves[counter].type = Move.CAPTURE;
							counter++;
							break;
						}
						counter++;
					} else break;
				}
			}
			
			
			for (int l=0; l<4; l++)
			{
				for (int k=0; k<BishopMove.BishopMove[i][l].length; k++)
				{
					if (board.board[BishopMove.BishopMove[i][l][k]] < 1)
					{
						moves[counter].from = i;
						moves[counter].to = BishopMove.BishopMove[i][l][k];
						moves[counter].type = Move.NORMAL;	
						moves[counter].value = 	Evaluator.WQ_MAX[moves[counter].to] - 
										Evaluator.WQ_MAX[moves[counter].from];		
						moves[counter].promote = 0;		
						if (board.board[BishopMove.BishopMove[i][l][k]] < 0)
						{
							moves[counter].type = Move.CAPTURE;
                            int toPiece = Math.abs(board.board[moves[counter].to]);
                            moves[counter].value += Evaluator.P_VALUE[toPiece] - Evaluator.B_QUEEN_VAL + CAP_VAL;
							counter++;
							break;
						}
						counter++;
					} else break;
				}
			}
			break;	
			}
		} // end switch
		}
        
		return counter;
	}
    
    public static void sortMoves(Move moves[], int counter) {
        // orders first counter elements of moves by value (decreasing order)
		int max = -300000;
		int pos = -1;
		for (int i=0; i<counter; i++)
		{
			max = -300000;
			pos = -1;
			for (int k=i; k<counter; k++)
			{
				if (moves[k].value > max)
				{
					max = moves[k].value;
					pos = k;
				}
			}
			moves[i].copyTo(moves[counter]); //moves[counter+1] = moves[i];
			moves[pos].copyTo(moves[i]); //moves[i] = moves[k];
			moves[counter].copyTo(moves[pos]); //moves[k] = moves[counter+1];
		}
    }
    
	public static int genBlack(Board board, Move moves[])
	{
		int counter = 0;
		/////////////////////////// pawns //////////////////////////////////////
		for (int j=0; j<board.nBlackPieces; j++)
		{
            int i = board.bPieces[j];
			switch (board.board[i])
			{
			case Board.BP:
			{	
			if (board.board[i+8] == Board.EM)
			{
				moves[counter].from = i;
				moves[counter].to = i+8;
				moves[counter].value = 	Evaluator.BP_MAX[moves[counter].to] - 
										Evaluator.BP_MAX[moves[counter].from];
				moves[counter].promote = 0;
				moves[counter].type = Move.NORMAL;
				if (board.row[i] == 1) {// promotion
					moves[counter].promote = Board.BQ;
                    moves[counter].value += Evaluator.B_QUEEN_VAL - Evaluator.B_PAWN_VAL;
                }
				counter++;
				
				if 	((i < 16) &&
					(board.board[i+16] == Board.EM))
				{
					moves[counter].from = i;
					moves[counter].to = i+16;
					moves[counter].type = Move.DOUBLE_PUSH;
					moves[counter].value = 	Evaluator.BP_MAX[moves[counter].to] - 
										Evaluator.BP_MAX[moves[counter].from];
					moves[counter].promote = 0;
					counter++;
				}
			}
			
			if (board.column[i] < 7)
			{
				if (board.board[i+9] > Board.EM)
				{
					moves[counter].from = i;
					moves[counter].to = i+9;
                    int toPiece = Math.abs(board.board[moves[counter].to]);
					moves[counter].value = Evaluator.P_VALUE[toPiece] - Evaluator.B_PAWN_VAL + CAP_VAL;
					moves[counter].promote = 0;
					moves[counter].type = Move.CAPTURE;
                    if (board.row[i] == 1) { // promotion
                        moves[counter].promote = Board.BQ;
                        moves[counter].value += Evaluator.B_QUEEN_VAL;
                    }
					counter++;
				} else if (board.row[i] == 3 && board.state.EP == i+9) {
                    moves[counter].promote = 0;
                    moves[counter].from = i;
                    moves[counter].to = i+9;
					moves[counter].value = CAP_VAL;
                    moves[counter].type = Move.EN_PASSANT;
                    counter++;
                }
			}
			
			if (board.column[i] > 0)
			{
				if (board.board[i+7] > Board.EM)
				{
					moves[counter].promote = 0;
					moves[counter].from = i;
					moves[counter].to = i+7;
                    int toPiece = Math.abs(board.board[moves[counter].to]);
					moves[counter].value = Evaluator.P_VALUE[toPiece] - Evaluator.B_PAWN_VAL + CAP_VAL;
					moves[counter].type = Move.CAPTURE;
                    if (board.row[i] == 1) {// promotion
                        moves[counter].promote = Board.BQ;
                        moves[counter].value += Evaluator.B_QUEEN_VAL;
                    }
					counter++;
				}  else if (board.row[i] == 3 && board.state.EP == i+7) {
                    moves[counter].promote = 0;
                    moves[counter].from = i;
                    moves[counter].to = i+7;
                    moves[counter].value = CAP_VAL;
                    moves[counter].type = Move.EN_PASSANT;
                    counter++;
                }
			}
			break;
			}
		
		
		////////////////////////// knights ///////////////////////////////////
		case Board.BN:
		{
			for (int k=0; k<KnightMove.KnightMove[i].length; k++)
			{
				if (board.board[KnightMove.KnightMove[i][k]] > -1)
				{
					moves[counter].from = i;
					
					moves[counter].to = KnightMove.KnightMove[i][k];
					moves[counter].type = Move.NORMAL;			
					moves[counter].value = 	Evaluator.BN_MAX[moves[counter].to] - 
										Evaluator.BN_MAX[moves[counter].from];
					moves[counter].promote = 0;	
					if (board.board[KnightMove.KnightMove[i][k]] > 0)
					{
						moves[counter].type = Move.CAPTURE;
                        int toPiece = Math.abs(board.board[moves[counter].to]);
                        moves[counter].value += Evaluator.P_VALUE[toPiece] - Evaluator.B_KNIGHT_VAL + CAP_VAL;
					}
					counter++;
				}
			}
			break;
		}
		
		////////////////////////// bishops //////////////////////////////////
		case Board.BB:
		{
			for (int l=0; l<4; l++)
			{
				for (int k=0; k<BishopMove.BishopMove[i][l].length; k++)
				{
					if (board.board[BishopMove.BishopMove[i][l][k]] > -1)
					{
						moves[counter].from = i;
						moves[counter].to = BishopMove.BishopMove[i][l][k];
						moves[counter].type = Move.NORMAL;	
						moves[counter].value = 	Evaluator.BB_MAX[moves[counter].to] - 
										Evaluator.BB_MAX[moves[counter].from];		
						moves[counter].promote = 0;			
						if (board.board[BishopMove.BishopMove[i][l][k]] > 0)
						{
							moves[counter].type = Move.CAPTURE;
                            int toPiece = Math.abs(board.board[moves[counter].to]);
                            moves[counter].value += Evaluator.P_VALUE[toPiece] - Evaluator.B_BISHOP_VAL + CAP_VAL;
							counter++;
							break;
						}
						counter++;
					} else break;
				}
			}
			break;
		}
		
		
		/////////////////////////// rooks //////////////////////////////////
		case Board.BR:
		{
			for (int l=0; l<4; l++)
			{
				for (int k=0; k<RookMove.RookMove[i][l].length; k++)
				if (board.board[RookMove.RookMove[i][l][k]] > -1)
				{
					moves[counter].from = i;
					moves[counter].to = RookMove.RookMove[i][l][k];
					moves[counter].type = Move.NORMAL;		
					moves[counter].value = 	Evaluator.BR_MAX[moves[counter].to] - 
										Evaluator.BR_MAX[moves[counter].from];
					moves[counter].promote = 0;		
					if  (board.board[RookMove.RookMove[i][l][k]] > 0)
					{
						moves[counter].type = Move.CAPTURE;
                        int toPiece = Math.abs(board.board[moves[counter].to]);
                        moves[counter].value += Evaluator.P_VALUE[toPiece] - Evaluator.B_ROOK_VAL + CAP_VAL;
						counter++;
						break;
					}
					counter++;
				} else break;
			}
			break;
		}
		
		/////////////////////// kings! /////////////////////////////////////
		case Board.BK:
		{
			/* this part isn't confirmed.. it may be slow
			 */
			 
			 if ((board.board[6] == Board.EM) &&
			 	(board.board[5] == Board.EM) &&
			 	(board.board[4] == Board.BK) &&
			 	(board.board[7] == Board.BR))
			 {
			 	moves[counter].from = i;
			 	moves[counter].to = 6;
			 	moves[counter].value = 90;
			 	moves[counter].type = Move.KCASTLE;
			 	moves[counter].promote = 0;
			 	counter++;
			 }
			 
			 if ((board.board[4] == board.BK) &&
			 	(board.board[3] == board.EM) &&
			 	(board.board[2] == board.EM) &&
			 	(board.board[1] == board.EM) &&
			 	(board.board[0] == board.BR))
			 {
			 	moves[counter].from = i;
			 	moves[counter].to = 2;
			 	moves[counter].value = 	90;
			 	moves[counter].type = Move.QCASTLE;
			 	moves[counter].promote = 0;
			 	counter++;
			 }
			 
			 /*
			  *
			  */
			for (int k=0; k<KingMove.KingMove[i].length; k++)
			{
				if (board.board[KingMove.KingMove[i][k]] > -1)
				{
					moves[counter].from = i;
					moves[counter].to = KingMove.KingMove[i][k];
					moves[counter].type = Move.NORMAL;
					moves[counter].value = 	Evaluator.BK_MAX[moves[counter].to] - 
										Evaluator.BK_MAX[moves[counter].from];
					moves[counter].promote = 0;
					if (board.board[KingMove.KingMove[i][k]] > 0)
					{
						moves[counter].type = Move.CAPTURE;
                        int toPiece = Math.abs(board.board[moves[counter].to]);
                        moves[counter].value += Evaluator.P_VALUE[toPiece] + CAP_VAL;
						moves[counter].promote = 0;
					}
					counter++;
				}
			}
			break;
		}
		
		/////////////////////////// queens /////////////////////////////////////
		case Board.BQ:
		{
			for (int l=0; l<4; l++)
			{
				for (int k=0; k<RookMove.RookMove[i][l].length; k++)
				{
					if (board.board[RookMove.RookMove[i][l][k]] > -1)
					{
						moves[counter].from = i;
						moves[counter].to = RookMove.RookMove[i][l][k];
						moves[counter].type = Move.NORMAL;
						moves[counter].value = 	Evaluator.BQ_MAX[moves[counter].to] - 
										Evaluator.BQ_MAX[moves[counter].from];
						moves[counter].promote = 0;
						if  (board.board[RookMove.RookMove[i][l][k]] > 0)
						{
							moves[counter].type = Move.CAPTURE;
                            int toPiece = Math.abs(board.board[moves[counter].to]);
                            moves[counter].value += Evaluator.P_VALUE[toPiece] - Evaluator.B_QUEEN_VAL + CAP_VAL;
							counter++;
							break;
						}
						counter++;
					} else break;
				}
			}
			
			
			for (int l=0; l<4; l++)
			{
				for (int k=0; k<BishopMove.BishopMove[i][l].length; k++)
				{
					if (board.board[BishopMove.BishopMove[i][l][k]] > -1)
					{
						moves[counter].from = i;
						moves[counter].to = BishopMove.BishopMove[i][l][k];
						moves[counter].type = Move.NORMAL;	
						moves[counter].value = 	Evaluator.BQ_MAX[moves[counter].to] - 
										Evaluator.BQ_MAX[moves[counter].from];	
						moves[counter].promote = 0;		
						if (board.board[BishopMove.BishopMove[i][l][k]] > 0)
						{
							moves[counter].type = Move.CAPTURE;
                            int toPiece = Math.abs(board.board[moves[counter].to]);
                            moves[counter].value += Evaluator.P_VALUE[toPiece] - Evaluator.B_BISHOP_VAL + CAP_VAL;
							counter++;
							break;
						}
						counter++;
					} else break;
				}
			}	
			break;
		}
		} // end switch
		} // end for loop
		
		return counter;
	}
    
    public static int genBlackCaptures(Board board, Move moves[])
	{
		int counter = 0;
		/////////////////////////// pawns //////////////////////////////////////
		for (int j=0; j<board.nBlackPieces; j++)
		{
            int i = board.bPieces[j];
			switch (board.board[i])
			{
			case Board.BP:
			{	
			if (board.board[i+8] == Board.EM && board.row[i] == 1)
			{
				moves[counter].from = i;
				moves[counter].to = i+8;
				moves[counter].value = 	Evaluator.BP_MAX[moves[counter].to] - 
										Evaluator.BP_MAX[moves[counter].from];
				moves[counter].promote = 0;
				moves[counter].type = Move.NORMAL;
				moves[counter].promote = Board.BQ;
                moves[counter].value += Evaluator.B_QUEEN_VAL - Evaluator.B_PAWN_VAL;
				counter++;
			}
			
			if (board.column[i] < 7)
			{
				if (board.board[i+9] > Board.EM)
				{
					moves[counter].from = i;
					moves[counter].to = i+9;
                    int toPiece = Math.abs(board.board[moves[counter].to]);
					moves[counter].value = Evaluator.P_VALUE[toPiece] - Evaluator.B_PAWN_VAL + CAP_VAL;
					moves[counter].promote = 0;
					moves[counter].type = Move.CAPTURE;
                    if (board.row[i] == 1) { // promotion
                        moves[counter].promote = Board.BQ;
                        moves[counter].value += Evaluator.B_QUEEN_VAL;
                    }
					counter++;
				} else if (board.row[i] == 3 && board.state.EP == i+9) {
                    moves[counter].promote = 0;
                    moves[counter].from = i;
                    moves[counter].to = i+9;
					moves[counter].value = CAP_VAL;
                    moves[counter].type = Move.EN_PASSANT;
                    counter++;
                }
			}
			
			if (board.column[i] > 0)
			{
				if (board.board[i+7] > Board.EM && board.row[i] == 1)
				{
					moves[counter].promote = 0;
					moves[counter].from = i;
					moves[counter].to = i+7;
                    int toPiece = Math.abs(board.board[moves[counter].to]);
					moves[counter].value = Evaluator.P_VALUE[toPiece] - Evaluator.B_PAWN_VAL + CAP_VAL;
					moves[counter].type = Move.CAPTURE;
                    moves[counter].promote = Board.BQ;
                    moves[counter].value += Evaluator.B_QUEEN_VAL;
					counter++;
				}  else if (board.row[i] == 3 && board.state.EP == i+7) {
                    moves[counter].promote = 0;
                    moves[counter].from = i;
                    moves[counter].to = i+7;
                    moves[counter].value = CAP_VAL;
                    moves[counter].type = Move.EN_PASSANT;
                    counter++;
                }
			}
			break;
			}
		
		
		////////////////////////// knights ///////////////////////////////////
		case Board.BN:
		{
			for (int k=0; k<KnightMove.KnightMove[i].length; k++)
			{
				if (board.board[KnightMove.KnightMove[i][k]] > 0) {
					moves[counter].from = i;
					moves[counter].to = KnightMove.KnightMove[i][k];	
					moves[counter].value = 	Evaluator.BN_MAX[moves[counter].to] - 
										Evaluator.BN_MAX[moves[counter].from];
					moves[counter].promote = 0;	
					moves[counter].type = Move.CAPTURE;
                    int toPiece = Math.abs(board.board[moves[counter].to]);
                    moves[counter].value += Evaluator.P_VALUE[toPiece] - Evaluator.B_KNIGHT_VAL + CAP_VAL;
					counter++;
				}
			}
			break;
		}
		
		////////////////////////// bishops //////////////////////////////////
		case Board.BB:
		{
			for (int l=0; l<4; l++)
			{
				for (int k=0; k<BishopMove.BishopMove[i][l].length; k++)
				{
					if (board.board[BishopMove.BishopMove[i][l][k]] < 0) {
                        break;
                    } else if (board.board[BishopMove.BishopMove[i][l][k]] > 0) {
						moves[counter].from = i;
						moves[counter].to = BishopMove.BishopMove[i][l][k];
						moves[counter].value = 	Evaluator.BB_MAX[moves[counter].to] - 
										Evaluator.BB_MAX[moves[counter].from];		
						moves[counter].promote = 0;			
						moves[counter].type = Move.CAPTURE;
                        int toPiece = Math.abs(board.board[moves[counter].to]);
                        moves[counter].value += Evaluator.P_VALUE[toPiece] - Evaluator.B_BISHOP_VAL + CAP_VAL;
					    counter++;
						break;
					}
				}
			}
			break;
		}
		
		
		/////////////////////////// rooks //////////////////////////////////
		case Board.BR:
		{
			for (int l=0; l<4; l++)
			{
				for (int k=0; k<RookMove.RookMove[i][l].length; k++)
				if (board.board[RookMove.RookMove[i][l][k]] < 0) {
                    break;
                } else if (board.board[RookMove.RookMove[i][l][k]] > 0) {
					moves[counter].from = i;
					moves[counter].to = RookMove.RookMove[i][l][k];
					moves[counter].value = 	Evaluator.BR_MAX[moves[counter].to] - 
										Evaluator.BR_MAX[moves[counter].from];
					moves[counter].promote = 0;		
					moves[counter].type = Move.CAPTURE;
                    int toPiece = Math.abs(board.board[moves[counter].to]);
                    moves[counter].value += Evaluator.P_VALUE[toPiece] - Evaluator.B_ROOK_VAL + CAP_VAL;
					counter++;
					break;
				}
			}
			break;
		}
		
		/////////////////////// kings! /////////////////////////////////////
		case Board.BK:
		{
			/* this part isn't confirmed.. it may be slow
			 */

			for (int k=0; k<KingMove.KingMove[i].length; k++)
			{
				if (board.board[KingMove.KingMove[i][k]] > 0)
				{
					moves[counter].from = i;
					moves[counter].to = KingMove.KingMove[i][k];
					moves[counter].value = 	Evaluator.BK_MAX[moves[counter].to] - 
										Evaluator.BK_MAX[moves[counter].from];
					moves[counter].promote = 0;
					moves[counter].type = Move.CAPTURE;
                    int toPiece = Math.abs(board.board[moves[counter].to]);
                    moves[counter].value += Evaluator.P_VALUE[toPiece] + CAP_VAL;
					moves[counter].promote = 0;
					counter++;
				}
			}
			break;
		}
		
		/////////////////////////// queens /////////////////////////////////////
		case Board.BQ:
		{
			for (int l=0; l<4; l++)
			{
				for (int k=0; k<RookMove.RookMove[i][l].length; k++)
				{
					if (board.board[RookMove.RookMove[i][l][k]] < 0) {
                        break;
                    } else if (board.board[RookMove.RookMove[i][l][k]] > 0) {
						moves[counter].from = i;
						moves[counter].to = RookMove.RookMove[i][l][k];
						moves[counter].value = 	Evaluator.BQ_MAX[moves[counter].to] - 
										Evaluator.BQ_MAX[moves[counter].from];
						moves[counter].promote = 0;
						moves[counter].type = Move.CAPTURE;
                        int toPiece = Math.abs(board.board[moves[counter].to]);
                        moves[counter].value += Evaluator.P_VALUE[toPiece] - Evaluator.B_QUEEN_VAL + CAP_VAL;
						counter++;
						break;
					}
				}
			}
			
			
			for (int l=0; l<4; l++)
			{
				for (int k=0; k<BishopMove.BishopMove[i][l].length; k++)
				{
					if (board.board[BishopMove.BishopMove[i][l][k]] < 0) {
                        break;
                    } else if (board.board[BishopMove.BishopMove[i][l][k]] > 0) {
						moves[counter].from = i;
						moves[counter].to = BishopMove.BishopMove[i][l][k];
						moves[counter].value = 	Evaluator.BQ_MAX[moves[counter].to] - 
										Evaluator.BQ_MAX[moves[counter].from];	
						moves[counter].promote = 0;		
						moves[counter].type = Move.CAPTURE;
                        int toPiece = Math.abs(board.board[moves[counter].to]);
                        moves[counter].value += Evaluator.P_VALUE[toPiece] - Evaluator.B_BISHOP_VAL + CAP_VAL;
					    counter++;
						break;
					}
				}
			}	
			break;
		}
		} // end switch
		} // end for loop
		
		return counter;
	}
	
}