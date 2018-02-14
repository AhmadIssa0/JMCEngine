
public class Mobility {
    public static int whiteMobility(Board board)
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
				    counter++;
				
				    if 	((i > 47) &&
					 (board.board[i-16] == Board.EM))
					{
					    counter++;			  
					}
				}
			
			    if (board.column[i] > 0) // can pawn capture in this direction
				{
				    if (board.board[i-9] < Board.EM)
					{
					    counter++;
					} else if (board.row[i] == 4 && board.state.EP == i-9) {
					counter++;
				    }
				}
			
			
			    if (board.column[i] < 7)
				{
				    if (board.board[i-7] < Board.EM)
					{
					    counter++;
					} else if (board.row[i] == 4 && board.state.EP == i-7) {
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
				    counter++;
				}
			 
			    if ((board.board[60] == board.WK) &&
			 	(board.board[59] == board.EM) &&
			 	(board.board[58] == board.EM) &&
			 	(board.board[57] == board.EM) &&
			 	(board.board[56] == board.WR))
				{
				    counter++;
				}
			 
			    /*
			     *
			     */
			    for (int k=0; k<KingMove.KingMove[i].length; k++)
				{
				    if (board.board[KingMove.KingMove[i][k]] < 1)
					{
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
    
    public static int blackMobility(Board board)
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
				    counter++;
				
				    if 	((i < 16) &&
					 (board.board[i+16] == Board.EM))
					{
					    counter++;
					}
				}
			
			    if (board.column[i] < 7)
				{
				    if (board.board[i+9] > Board.EM)
					{
					    counter++;
					} else if (board.row[i] == 3 && board.state.EP == i+9) {
					counter++;
				    }
				}
			
			    if (board.column[i] > 0)
				{
				    if (board.board[i+7] > Board.EM)
					{
					    counter++;
					}  else if (board.row[i] == 3 && board.state.EP == i+7) {
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
				    counter++;
				}
			 
			    if ((board.board[4] == board.BK) &&
			 	(board.board[3] == board.EM) &&
			 	(board.board[2] == board.EM) &&
			 	(board.board[1] == board.EM) &&
			 	(board.board[0] == board.BR))
				{
				    counter++;
				}
			 
			    /*
			     *
			     */
			    for (int k=0; k<KingMove.KingMove[i].length; k++)
				{
				    if (board.board[KingMove.KingMove[i][k]] > -1)
					{
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

}
