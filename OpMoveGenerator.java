
public class OpMoveGenerator {
       	
	public int genWhite(Board board, Move moves[])
	{
		int counter = 0;

		
		
		////////////////////////// knights ///////////////////////////////////
		for (int i=1; i<=board.wknights[0]; i++)
		{
			for (int k=0; k<KnightMove.KnightMove[board.wknights[i]].length; k++)
			{
				if (board.board[KnightMove.KnightMove[board.wknights[i]][k]] < 1)
				{
					/*
					moves[counter].from = board.wknights[i];
					
					moves[counter].to = KnightMove.KnightMove[board.wknights[i]][k];
					moves[counter].promote = 0;
					if (board.board[KnightMove.KnightMove[board.wknights[i]][k]] < 0)
					{
						moves[counter].type = Move.CAPTURE;
						moves[counter].value = 
						Math.abs(board.board[moves[counter].from]) -
						Math.abs(board.board[moves[counter].to]);
					} else {
						moves[counter].type = Move.NORMAL;
						moves[counter].value = 0;
					}
					*/
					counter++;
				}
			}
		}
		
		/*
		int max = -300000;
		for (int i=0; i<counter; i++)
		{
			for (int k=0; k<counter; k++)
			{
				if (moves[k].value > max)
				{
					max = moves[k].value;
					moves[counter] = moves[k];
				}
			}
			max = -300000;
			moves[counter+1] = moves[i];
			moves[i] = moves[counter];
			moves[i].value = -300000;
		}
		*/
		return counter;
	}
	
	
	
	
	
	public int genBlack(Board board, Move moves[])
	{
		int counter = 0;
		/////////////////////////// pawns //////////////////////////////////////
		for (int i=1; i<=board.bpawns[0]; i++)
		{
			if (board.board[board.bpawns[i]+8] == Board.EM)
			{
				moves[counter].from = board.bpawns[i];
				moves[counter].to = board.bpawns[i]+8;
				moves[counter].value = 0;
				moves[counter].promote = 0;
				moves[counter].type = Move.NORMAL;
				if (board.row[board.bpawns[i]] == 1) // promotion
					moves[counter].promote = Board.BQ;
				counter++;
				
				if 	((board.bpawns[i] < 16) &&
					(board.board[board.bpawns[i]+16] == Board.EM))
				{
					moves[counter].from = board.bpawns[i];
					moves[counter].to = board.bpawns[i]+16;
					moves[counter].type = Move.DOUBLE_PUSH;
					moves[counter].promote = 0;
					counter++;
				}
			}
			
			if (board.column[board.bpawns[i]] < 7)
			{
				if (board.board[board.bpawns[i]+9] > Board.EM)
				{
					moves[counter].from = board.bpawns[i];
					moves[counter].to = board.bpawns[i]+9;
											moves[counter].value = 
						Math.abs(board.board[moves[counter].from]) -
						Math.abs(board.board[moves[counter].to]);
					moves[counter].promote = 0;
					moves[counter].type = Move.CAPTURE;
				if (board.row[board.bpawns[i]] == 1) // promotion
					moves[counter].promote = Board.BQ;
					counter++;
				}
			}
			
			
			if (board.column[board.bpawns[i]] > 0)
			{
				if (board.board[board.bpawns[i]+7] > Board.EM)
				{
					moves[counter].promote = 0;
					moves[counter].from = board.bpawns[i];
					moves[counter].to = board.bpawns[i]+7;
											moves[counter].value = 
						Math.abs(board.board[moves[counter].from]) -
						Math.abs(board.board[moves[counter].to]);
					moves[counter].type = Move.CAPTURE;
				if (board.row[board.bpawns[i]] == 1) // promotion
					moves[counter].promote = Board.BQ;
					counter++;
				}
			}
			
			
		}
		
		
		////////////////////////// knights ///////////////////////////////////
		for (int i=1; i<=board.bknights[0]; i++)
		{
			for (int k=0; k<KnightMove.KnightMove[board.bknights[i]].length; k++)
			{
				if (board.board[KnightMove.KnightMove[board.bknights[i]][k]] > -1)
				{
					moves[counter].from = board.bknights[i];
					
					moves[counter].to = KnightMove.KnightMove[board.bknights[i]][k];
					moves[counter].type = Move.NORMAL;			
					moves[counter].value = 0;	
					moves[counter].promote = 0;	
					if (board.board[KnightMove.KnightMove[board.bknights[i]][k]] > 0)
					{
						moves[counter].type = Move.CAPTURE;
												moves[counter].value = 
						Math.abs(board.board[moves[counter].from]) -
						Math.abs(board.board[moves[counter].to]);
					}
					counter++;
				}
			}
		}
		
		////////////////////////// bishops //////////////////////////////////
		for (int i=1; i<=board.bbishops[0]; i++)
		{
			for (int l=0; l<4; l++)
			{
				for (int k=0; k<BishopMove.BishopMove[board.bbishops[i]][l].length; k++)
				{
					if (board.board[BishopMove.BishopMove[board.bbishops[i]][l][k]] > -1)
					{
						moves[counter].from = board.bbishops[i];
						moves[counter].to = BishopMove.BishopMove[board.bbishops[i]][l][k];
						moves[counter].type = Move.NORMAL;	
						moves[counter].value = 0;		
						moves[counter].promote = 0;			
						if (board.board[BishopMove.BishopMove[board.bbishops[i]][l][k]] > 0)
						{
							moves[counter].type = Move.CAPTURE;
													moves[counter].value = 
						Math.abs(board.board[moves[counter].from]) -
						Math.abs(board.board[moves[counter].to]);
							counter++;
							break;
						}
						counter++;
					} else break;
				}
			}
		}
		
		
		/////////////////////////// rooks //////////////////////////////////
		for (int i=1; i<=board.brooks[0]; i++)
		{
			for (int l=0; l<4; l++)
			{
				for (int k=0; k<RookMove.RookMove[board.brooks[i]][l].length; k++)
				if (board.board[RookMove.RookMove[board.brooks[i]][l][k]] > -1)
				{
					moves[counter].from = board.brooks[i];
					moves[counter].to = RookMove.RookMove[board.brooks[i]][l][k];
					moves[counter].type = Move.NORMAL;		
					moves[counter].value = 0;
					moves[counter].promote = 0;		
					if  (board.board[RookMove.RookMove[board.brooks[i]][l][k]] > 0)
					{
						moves[counter].type = Move.CAPTURE;
												moves[counter].value = 
						Math.abs(board.board[moves[counter].from]) -
						Math.abs(board.board[moves[counter].to]);
						counter++;
						break;
					}
					counter++;
				} else break;
			}
		}
		
		/////////////////////// kings! /////////////////////////////////////
		for (int i=1; i<=board.bkings[0]; i++)
		{
			/* this part isn't confirmed.. it may be slow
			 */
			 
			 if ((board.board[6] == Board.EM) &&
			 	(board.board[5] == Board.EM) &&
			 	(board.board[4] == Board.BK) &&
			 	(board.board[7] == Board.BR))
			 {
			 	moves[counter].from = board.bkings[i];
			 	moves[counter].to = 6;
			 	moves[counter].value = 20;
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
			 	moves[counter].from = board.bkings[i];
			 	moves[counter].to = 2;
			 	moves[counter].value = 20;
			 	moves[counter].type = Move.QCASTLE;
			 	moves[counter].promote = 0;
			 	counter++;
			 }
			 
			 /*
			  *
			  */
			for (int k=0; k<KingMove.KingMove[board.bkings[i]].length; k++)
			{
				if (board.board[KingMove.KingMove[board.bkings[i]][k]] > -1)
				{
					moves[counter].from = board.bkings[i];
					moves[counter].to = KingMove.KingMove[board.bkings[i]][k];
					moves[counter].type = Move.NORMAL;
					moves[counter].value = 0;
					moves[counter].promote = 0;
					if (board.board[KingMove.KingMove[board.bkings[i]][k]] > 0)
					{
						moves[counter].type = Move.CAPTURE;
											moves[counter].value = 
						Math.abs(board.board[moves[counter].from]) -
						Math.abs(board.board[moves[counter].to]);
						moves[counter].promote = 0;
					}
					counter++;
				}
			}
		}
		
		/////////////////////////// queens /////////////////////////////////////
		for (int i=1; i<=board.bqueens[0]; i++)
		{
			for (int l=0; l<4; l++)
			{
				for (int k=0; k<RookMove.RookMove[board.bqueens[i]][l].length; k++)
				{
					if (board.board[RookMove.RookMove[board.bqueens[i]][l][k]] > -1)
					{
						moves[counter].from = board.bqueens[i];
						moves[counter].to = RookMove.RookMove[board.bqueens[i]][l][k];
						moves[counter].type = Move.NORMAL;
						moves[counter].value = 0;
						moves[counter].promote = 0;
						if  (board.board[RookMove.RookMove[board.bqueens[i]][l][k]] > 0)
						{
							moves[counter].type = Move.CAPTURE;
												moves[counter].value = 
						Math.abs(board.board[moves[counter].from]) -
						Math.abs(board.board[moves[counter].to]);
							counter++;
							break;
						}
						counter++;
					} else break;
				}
			}
			
			
			for (int l=0; l<4; l++)
			{
				for (int k=0; k<BishopMove.BishopMove[board.bqueens[i]][l].length; k++)
				{
					if (board.board[BishopMove.BishopMove[board.bqueens[i]][l][k]] > -1)
					{
						moves[counter].from = board.bqueens[i];
						moves[counter].to = BishopMove.BishopMove[board.bqueens[i]][l][k];
						moves[counter].type = Move.NORMAL;	
						moves[counter].value = 0;	
						moves[counter].promote = 0;		
						if (board.board[BishopMove.BishopMove[board.bqueens[i]][l][k]] > 0)
						{
							moves[counter].type = Move.CAPTURE;
												moves[counter].value = 
						Math.abs(board.board[moves[counter].from]) -
						Math.abs(board.board[moves[counter].to]);
							counter++;
							break;
						}
						counter++;
					} else break;
				}
			}	
		}
		
		
		/*
		int max = -300000;
		for (int i=0; i<counter; i++)
		{
			for (int k=0; k<counter; k++)
			{
				if (moves[k].value > max)
				{
					max = moves[k].value;
					moves[counter] = moves[k];
				}
			}
			max = -300000;
			moves[counter+1] = moves[i];
			moves[i] = moves[counter];
			moves[i].value = -300000;
		}
		*/
		return counter;
	}
	
}
