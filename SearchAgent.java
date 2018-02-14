
import java.util.*;
import java.io.*;

public class SearchAgent {
    Move all[][] = new Move[100][100];
    Move qall[][] = new Move[100][100];
    int ply=0;
    int dep;
    long prev=0;
    long start_time=0;
    long time;
    BoardState bs[] = new BoardState[100];
    BoardState qbs[] = new BoardState[100];
    Move pv[] = new Move[100];
    Move killer[][] = new Move[100][2]; // move.from = -1 means invalid killer
	
    MoveGenerator mg = new MoveGenerator();
    TranspositionTable table = TranspositionTable.getTable();
    TranspositionEntry entry = new TranspositionEntry();

    // only want to get into a draw if opponent winning by at least a pawn
    static final int CONTEMPT_FACTOR = -100;

    // if set to true we've run out of allocated time, return immediately
    boolean FORCE_BREAK = false; 
    
    // USED IN SEE() static exchange evaluation //////////////
    int w_attackers_pos[] = new int[16];
    int b_attackers_pos[] = new int[16];
    
    int w_attackers_val[] = new int[16];
    int b_attackers_val[] = new int[16];
    
    int w_attackers_delta[][] = new int[16][3];
    // stores the attack direction
    // w_attackers_delta[i][0] is 0 for rook and 1 for bishop
    // w_attackers_delta[i][1] = j, w_attackers_delta[i][2] = k
    // means RookMove[move.to][j][k] is the square the attacking piece is at (or BishopMove)
    int b_attackers_delta[][] = new int[16][3];
    int scores[] = new int[32];
    /////////////////////////////////////////////////////////////////
    
    
	public SearchAgent()
	{
        // used in static exchange evaluation
        for (int i=0; i<16; i++) {
            w_attackers_delta[i] = new int[3];
            b_attackers_delta[i] = new int[3];
        }
        
		for (int i=0; i<all.length; i++)
		{
			for (int k=0; k<all[i].length; k++)
			{
				all[i][k] = new Move();
			}
			bs[i] = new BoardState();
		}
		
		for (int i=0; i<qall.length; i++)
		{
			for (int k=0; k<qall[i].length; k++)
			{
				qall[i][k] = new Move();
			}
			qbs[i] = new BoardState();
		}
		
		for (int i=0; i<pv.length; i++)
		{
			pv[i] = new Move();
		}
        
        for (int i=0; i<killer.length; i++)
            for (int j=0; j<killer[i].length; j++)
                killer[i][j] = new Move();
	}

    public void clearKillers() {
        for (int i=0; i<killer.length; i++) {
            for (int j=0; j<killer[i].length; j++)
                killer[i][j].from = -1;
        }
    }
	
	public Move searchPos(boolean isWhite, Board board, int time) throws Exception
	{
		int depth=1;
		dep = depth+1;
		int size=0;
        
		prev = System.currentTimeMillis();
		start_time = System.currentTimeMillis();
        nodes = 0;
        qnodes = 0;
        FORCE_BREAK = false;
		long prev = System.currentTimeMillis();
        this.time = time;
        
		Move best_move = new Move();
		int best_score = -300001, move_score = -300000;
		Move bestr_move = new Move();
		int dscore = -300000;
		Move play = new Move();
		System.out.println("size = " + size);
        
        
        // alpha and beta here for the aspiration window
		int alpha = -300000;
        int beta = 300000; 
        boolean alpha_is_approx = true;
        clearKillers();
        long prev_time_for_depth = 0;
		while (true)
		{
            if (dep > 5)
            System.out.println("Depth = " + dep + " nodes: " + nodes + " qnodes: " + qnodes + 
                                " nps: " + (nodes+qnodes)*1000L/Math.max(1,(System.currentTimeMillis() - start_time)));
            prev_time_for_depth = System.currentTimeMillis();
            
            if (isWhite) {
                size = mg.genWhite(board, all[ply]);
            } else {
                size = mg.genBlack(board, all[ply]);
            }
            
            
            if (depth <= 10) {
                long hash = board.hash;
        
                if (!isWhite) {
                    hash ^= board.t.BLACKS_TURN;
                }
                
                if (table.lookup(entry, hash, 0, alpha, beta) == TranspositionTable.HASH_EXACT) {
                    bestr_move = entry.best_move;
                }
            }
            
            // Try the best move from previous depth first
            if (depth > 2) {
                for (int i=0; i<size; i++) {
                    if (bestr_move.from == all[ply][i].from && bestr_move.to == all[ply][i].to) {
                        all[ply][i].value += Evaluator.PV_REWARD;
                    } else if (all[ply][i].type == Move.CAPTURE) {
                        all[ply][i].value = Math.max(20, see(board, all[ply][i], isWhite));
                    }
                }
            }
            
            MoveGenerator.sortMoves(all[ply], size);
            
            alpha = -300000;
            int valid_moves = 0;
			for (int i=0; i<size; i++)
			{
				if (board.makeMove(all[ply][i]))
				{
                    boolean wChecked = board.isWhiteChecked();
                    boolean bChecked = board.isBlackChecked();
                    if ((isWhite && wChecked) || (!isWhite && bChecked)) { 
                        // can't make moves which put us in check
                        board.unmakeMove(board.state);
                        continue;
                    }
                    
                    valid_moves++;
					board.state.copyTo(bs[ply]);
                    // move_score = -negamax(!isWhite, board, depth);
                    
                    if (board.threeMoveRep() || board.fiftyMoveRule()) {
                        move_score = CONTEMPT_FACTOR; // three move repetition
                    } else {
                    // Try an aspiration window:
                    for (int k=0; k<=20; k+=20) { // widen aspiration window
                        int ext=0;
                        if (wChecked || bChecked) {
                            ext = 1;
                        }
                        int eval = alpha + 1;
                        if (ext == 0 && depth >= 3 && i > 3 && !(wChecked || bChecked) && all[ply][i].type != Move.CAPTURE) {
                            eval = -ab(!isWhite, depth+ext, board, -best_score-1, -best_score, ext, true);
                        }
                        if (eval > alpha) {
                            move_score = -ab(!isWhite, depth+ext, board, -(beta+k), -(alpha-k), ext, true);
                        } else {
                            move_score = eval-1;
                        }
                        if ((alpha_is_approx && move_score <= alpha) || move_score >= beta) {
                            //System.out.println("aspiration window failed.");
                            // Redo search
                            if (k == 20) {
                                if (wChecked || bChecked) {
                                    ext = 1;
                                }
                                eval = best_score + 1;
                                if (ext == 0 && depth >= 3 && i > 3 && !(wChecked || bChecked) && all[ply][i].type != Move.CAPTURE) {
                                    eval = -ab(!isWhite, depth+ext, board, -best_score-1, -best_score, ext, true);
                                }
                                if (eval > alpha) {
                                    move_score = -ab(!isWhite, depth+ext, board, -300000, -best_score, ext, true);
                                } else {
                                    move_score = eval-1;
                                }
                            }
                        } else {
                            break;
                        }
                    }
                    
                    }
					board.unmakeMove(bs[ply]);
                    
                    if (FORCE_BREAK) {
                        System.out.println("tt_hits " + tt_hits);
                        if (best_score > dscore || i > 0)
                        {
                            System.out.println("Dscore Evaluation: " + best_score);
                            return best_move;
                        } else {
                            System.out.println("Bscore Evaluation: " + dscore);
                            return bestr_move;
                        }
                    }
                        
                    //move_score = -ab(!isWhite, depth, board, -300000, -best_score);
                    if (move_score > alpha) {
                        alpha = move_score;
                        alpha_is_approx = false;
                    }
                    /*
                    if (depth > 6) {
					System.out.println("move_score= "+move_score+" at "+
					XboardParser.m2str[all[ply][i].from] + " , "+
						XboardParser.m2str[all[ply][i].to]);
                    }
                    */
					if (move_score > best_score)
					{
						best_score = move_score;
						all[ply][i].copyTo(best_move);
					}
					
				}
                /*
				if ((System.currentTimeMillis() - prev) > (time/100))
				{
					if (best_score > dscore)
					{
						return best_move;
					} else {
						return bestr_move;
					}
				}*/
			}
            if (valid_moves == 0)
                return null;
            if (depth > 2 && valid_moves == 1) {
                System.out.println("Evaluation: " + best_score + " (single valid move)");
                return best_move;
            }
            best_move.copyTo(bestr_move);
			dscore = best_score;
			best_score = -300000;
			
			depth++;
			dep = depth+1;
            /*
			if ((System.currentTimeMillis() - prev) > (time/100) || dscore > 150000)
			{
				return bestr_move;
			}*/

            // Aspiration window alpha and beta values
            alpha = dscore - 30;
            beta = dscore + 30;
		}
	}
	
	int nodes=0;
    int tt_hits=0;
	public int ab(boolean isWhite, int depth, Board board, int alpha, int beta, int ext, boolean allowNull) throws Exception
	{
        // ext (extension) is how many extra plies we wanna search.
        // when we're in check we may want to extend the search in this line one ply deeper
		nodes++;
		if (nodes % 100000 == 0)
		{
			//if (nodes % 1000000 == 0)
            //    System.out.println("nodes searched: " + nodes + " " + (prev-System.currentTimeMillis()));
			prev = System.currentTimeMillis();	
            if (System.currentTimeMillis() - start_time > time/40 && dep > 4) {
                FORCE_BREAK = true;
                return 0;
            }
		}
        if (FORCE_BREAK)
            return 0;
		
        if (board.threeMoveRep()) {
            return 0; // three move repetition
        }
                
		int move_score = -300000;
        long hash = board.hash;
        
        if (!isWhite) {
            hash ^= board.t.BLACKS_TURN;
        }
        
        if (table.lookup(entry, hash, depth, alpha, beta) != TranspositionTable.HASH_UNKNOWN) {
            tt_hits++;
            move_score = entry.value;
			return move_score;
		}
        
		if (depth == 0) {
        //return Evaluator.material(isWhite, board);
        return qsearch(isWhite, board, alpha, beta, 0);
		}
        
        ////////// NULL MOVE //////////////
        if (allowNull && board.nWhitePieces > 5 && board.nBlackPieces > 5 && depth >= 3 && !(board.isWhiteChecked() || board.isBlackChecked())) {
            move_score = -ab(!isWhite, depth - 1 - 2, board, -beta, -beta+1, ext, false);
            if (move_score >= beta) {
                return move_score;
            } else {
                move_score = -300000;
            }
        }
		
        Move move_arr[] = all[dep-depth+2*ext];
		int size=0;
		if (isWhite)
		{
			size = mg.genWhite(board, move_arr);
		} else {
			size = mg.genBlack(board, move_arr);
		}
		
		if (size == 0) {
			return 0; // stalemate
		}
        
        //TranspositionEntry bmove_entry = table.getEntry(table.getHash(board, isWhite));
        TranspositionEntry bmove_entry = table.getEntry(hash);
        Move tt_move = null;
        if (bmove_entry != null && bmove_entry.flags == TranspositionTable.HASH_EXACT) {
            tt_move = bmove_entry.best_move;
        }
        
        for (int i=0; i<size; i++) {
            if ((move_arr[i].from == killer[dep-depth+2*ext][0].from && move_arr[i].to == killer[dep-depth+2*ext][0].to) ||
                (move_arr[i].from == killer[dep-depth+2*ext][1].from && move_arr[i].to == killer[dep-depth+2*ext][1].to)) {
                move_arr[i].value += MoveGenerator.KILLER_VAL;
            }
            if (tt_move != null && tt_move.from == move_arr[i].from && tt_move.to == move_arr[i].to) {
                move_arr[i].value += Evaluator.PV_REWARD;
            } else if (move_arr[i].type == Move.CAPTURE) {
                //move_arr[i].value = Math.max(35, see(board, move_arr[i], isWhite));
                move_arr[i].value = see(board, move_arr[i], isWhite);
            }
        }
        
        //MoveGenerator.sortMoves(move_arr, size);
                    
        int valid_moves = 0;
        move_score = -300000;
        int best_score = -300000;
        Move best_move = new Move();
        BoardState bstate = bs[dep-depth+2*ext];
                    
		for (int i=0; i<size; i++)
		{
            int max_val = move_arr[i].value;
            int max_ind = i;
            for (int j=i+1; j<size; j++) {
                if (move_arr[j].value > max_val) {
                    max_ind = j;
                    max_val = move_arr[j].value;
                }
            }
            move_arr[i].copyTo(move_arr[size]);
            move_arr[max_ind].copyTo(move_arr[i]);
            move_arr[size].copyTo(move_arr[max_ind]);
            
			if (board.makeMove(move_arr[i]))
			{
                boolean wChecked = board.isWhiteChecked();
                boolean bChecked = board.isBlackChecked();
                if ((isWhite && wChecked) || (!isWhite && bChecked)) { 
                    // can't make moves which put us in check
                    board.unmakeMove(board.state);
                    continue;
                }

                valid_moves++;
                board.state.copyTo(bstate);
                
                if ((wChecked || bChecked) && ext < 7) {
                    move_score = -ab(!isWhite, depth, board, -beta, -Math.max(best_score, alpha), ext+2, true);
                } else {
                    int eval = Math.max(best_score, alpha) + 1;
                    // late move reduction
                    if (depth >= 3 && i > 3 && !(wChecked || bChecked) && move_arr[i].type != Move.CAPTURE && move_arr[i].promote == 0) {
                        if (depth > 7) {
                            // R = 3
                            eval = -ab(!isWhite, depth - 3, board, -Math.max(best_score, alpha)-1, -Math.max(best_score, alpha), ext, true);
                        } else {
                            // R = 2
                            eval = -ab(!isWhite, depth - 2, board, -Math.max(best_score, alpha)-1, -Math.max(best_score, alpha), ext, true);
                        }
                    }
                    if (eval > Math.max(best_score, alpha)) {
                        move_score = -ab(!isWhite, depth - 1, board, -beta, -Math.max(best_score, alpha), ext, true);
                    } else {
                        move_score = eval-1; // move not interesting enough to search deeper
                    }
                }
                
                board.unmakeMove(bstate);
                
                if (move_score >= beta) {
                    table.store(depth, move_score, TranspositionTable.HASH_LOWERBOUND, hash, null);
                    updateKiller(depth, ext, move_arr[i]);
                    return move_score;
                }
			
                if (move_score > best_score) {
                    move_arr[i].copyTo(best_move);
                    best_score = move_score;
                }
			}
		}
        if (valid_moves == 0) {
            if ((isWhite && board.isWhiteChecked()) || (!isWhite && board.isBlackChecked())) {
                return -200000 - depth; // we're checkmated
            } else {
                return 0; // stalemate
            }
        }
        if (best_score <= alpha) {
            table.store(depth, best_score, TranspositionTable.HASH_UPPERBOUND, hash, null);
        } else {
            table.store(depth, best_score, TranspositionTable.HASH_EXACT, hash, best_move);
        }
		return best_score;
	}
    
    public void updateKiller(int depth, int ext, Move move) {
        if (move.promote != 0 || move.type == Move.CAPTURE)
            return; // don't want these in our killer move slots
            
        depth = dep-depth+2*ext;
        
        if ((killer[depth][0].from == move.from && killer[depth][0].to == move.to) ||
            (killer[depth][1].from == move.from && killer[depth][1].to == move.to))
            return; // move already in our killer slots
            
        for (int i=0; i<2; i++) {
            if (killer[depth][i].from == -1) {
                killer[depth][i].from = move.from;
                killer[depth][i].to = move.to;
                return;
            }
        }
        killer[depth][0].from = killer[depth][1].from;
        killer[depth][0].to = killer[depth][1].to;
        killer[depth][1].from = move.from;
        killer[depth][1].to = move.to;
    }
    
    public int see(Board board, Move move, boolean isWhite) {
        
        int w=0, b=0; // number of white/black attackers REMAINING
        
        int sq = move.to;
        int capture_val = Evaluator.P_VALUE[Math.abs(board.board[sq])];
        int attacker_val = Evaluator.P_VALUE[Math.abs(board.board[move.from])];
        
        scores[0] = capture_val;
        int score_ind = 1;
        
        boolean wTurn = !isWhite;
        int attacker_pos = move.from;
        
        // calculate direct attackers, assuming first capture already took place
        
        // knights
        for (int i=0; i<KnightMove.KnightMove[sq].length; i++) {
            int to = KnightMove.KnightMove[sq][i];
            if (to == attacker_pos)
                continue;
            if (board.board[to] == Board.WN) {
                w_attackers_pos[w] = to;
                w_attackers_val[w] = Evaluator.P_VALUE[Board.WN];
                w++;
            } else if (board.board[to] == Board.BN) {
                b_attackers_pos[b] = to;
                b_attackers_val[b] = Evaluator.P_VALUE[Board.WN];
                b++;
            }
        }
        
        // diagonals (bishop, queen and king)
        for (int i=0; i<4; i++) {
            for (int j=0; j<BishopMove.BishopMove[sq][i].length; j++) {
                int to = BishopMove.BishopMove[sq][i][j];
                if (board.board[to] == Board.EM || to == attacker_pos) {
                    continue;
                } else if (board.board[to] == Board.WB || board.board[to] == Board.WQ || board.board[to] == Board.WK) {
                    w_attackers_pos[w] = to;
                    w_attackers_val[w] = Evaluator.P_VALUE[board.board[to]];
                    w_attackers_delta[w][0] = 1;
                    w_attackers_delta[w][1] = i;
                    w_attackers_delta[w][2] = j;
                    w++;
		    break;
                } else if (board.board[to] == Board.BB || board.board[to] == Board.BQ || board.board[to] == Board.BK) {
                    b_attackers_pos[b] = to;
                    b_attackers_val[b] = Evaluator.P_VALUE[-board.board[to]];
                    b_attackers_delta[b][0] = 1;
                    b_attackers_delta[b][1] = i;
                    b_attackers_delta[b][2] = j;
                    b++;
		    break;
                } else {
                    break;
                }
            }
        }
        
        // files/row (rook, queen, king)
        for (int i=0; i<4; i++) {
            for (int j=0; j<RookMove.RookMove[sq][i].length; j++) {
                int to = RookMove.RookMove[sq][i][j];
                if (board.board[to] == Board.EM || to == attacker_pos) {
                    continue;
                } else if (board.board[to] == Board.WR || board.board[to] == Board.WQ || board.board[to] == Board.WK) {
                    w_attackers_pos[w] = to;
                    w_attackers_val[w] = Evaluator.P_VALUE[board.board[to]];
                    w_attackers_delta[w][0] = 0;
                    w_attackers_delta[w][1] = i;
                    w_attackers_delta[w][2] = j;
                    w++;
		    break;
                } else if (board.board[to] == Board.BR || board.board[to] == Board.BQ || board.board[to] == Board.BK) {
                    b_attackers_pos[b] = to;
                    b_attackers_val[b] = Evaluator.P_VALUE[-board.board[to]];
                    b_attackers_delta[b][0] = 0;
                    b_attackers_delta[b][1] = i;
                    b_attackers_delta[b][2] = j;
                    b++;
		    break;
                } else {
                    break;
                }
            }
        }
        
        // pawns
        if (board.row[sq] < 6) { // black pawns
            if (board.column[sq] > 0 && board.board[sq-9] == Board.BP) {
                int to = sq-9;
                if (to != attacker_pos) {
                    b_attackers_pos[b] = to;
                    b_attackers_val[b] = Evaluator.P_VALUE[Board.WP];
                    b++;
                }
            }
            if (board.column[sq] < 7 && board.board[sq-7] == Board.BP) {
                int to = sq-7;
                if (to != attacker_pos) {
                    b_attackers_pos[b] = to;
                    b_attackers_val[b] = Evaluator.P_VALUE[Board.WP];
                    b++;
                }
            }
        }
        if (board.row[sq] > 1) {
            if (board.column[sq] > 0 && board.board[sq+7] == Board.WP) {
                int to = sq+7;
                if (to != attacker_pos) {
                    w_attackers_pos[w] = to;
                    w_attackers_val[w] = Evaluator.P_VALUE[board.board[to]];
                    w++;
                }
            }
            if (board.column[sq] < 7 && board.board[sq+9] == Board.WP) {
                int to = sq+9;
                if (to != attacker_pos) {
                    w_attackers_pos[w] = to;
                    w_attackers_val[w] = Evaluator.P_VALUE[board.board[to]];
                    w++;
                }
            }
        }

        /////////////////////////////////////////////////////////////////////
        // when entering while loop, wTurn is turn of recapture player
        while ((wTurn && w > 0) || (!wTurn && b > 0)) {
            // capture_val is equal to the previous attacker's value
            capture_val = Evaluator.P_VALUE[Math.abs(board.board[attacker_pos])];
            
            int i_min = 0; // index of smallest attacker
            if (wTurn) {
                int lowest_val = w_attackers_val[0];
                for (int i=1; i<w; i++) {
                    int val = Evaluator.P_VALUE[Math.abs(board.board[w_attackers_pos[i]])];
                    if (val < lowest_val) {
                        lowest_val = val;
                        i_min = i;
                    }
                }
                attacker_pos = w_attackers_pos[i_min];
            } else {
                int lowest_val = b_attackers_val[0];
                for (int i=1; i<b; i++) {
                    int val = Evaluator.P_VALUE[Math.abs(board.board[b_attackers_pos[i]])];
                    if (val < lowest_val) {
                        lowest_val = val;
                        i_min = i;
                    }
                }
                attacker_pos = b_attackers_pos[i_min];
            }
            
            scores[score_ind] = capture_val - scores[score_ind-1];
            score_ind++;
            // update capture_val for next iteration
            capture_val = Evaluator.P_VALUE[Math.abs(board.board[attacker_pos])];
            
            // add hidden attackers, then remove current attacker at attacker_pos
            int piece = board.board[attacker_pos];
            
            if (piece == Board.WB || piece == Board.BB || (piece == Board.WQ && w_attackers_delta[i_min][0] == 1)
                                                       || (piece == Board.BQ && b_attackers_delta[i_min][0] == 1)) {
                int attackers_delta[] = new int[3];
                if (wTurn) {
                    attackers_delta = w_attackers_delta[i_min];
                } else {
                    attackers_delta = b_attackers_delta[i_min];
                }
                int i = attackers_delta[1];
                
                for (int j=attackers_delta[2]+1; j<BishopMove.BishopMove[sq][i].length; j++) {
                    int to = BishopMove.BishopMove[sq][i][j];
                    if (board.board[to] == Board.EM) {
                        continue;
                    } else if (board.board[to] == Board.WB || board.board[to] == Board.WQ || board.board[to] == Board.WK) {
                        w_attackers_pos[w] = to;
                        w_attackers_val[w] = Evaluator.P_VALUE[board.board[to]];
                        w_attackers_delta[w][0] = 1;
                        w_attackers_delta[w][1] = i;
                        w_attackers_delta[w][2] = j;
                        w++;
			break;
                    } else if (board.board[to] == Board.BB || board.board[to] == Board.BQ || board.board[to] == Board.BK) {
                        b_attackers_pos[b] = to;
                        b_attackers_val[b] = Evaluator.P_VALUE[-board.board[to]];
                        b_attackers_delta[b][0] = 1;
                        b_attackers_delta[b][1] = i;
                        b_attackers_delta[b][2] = j;
                        b++;
			break;
                    } else {
                        break;
                    }
                }
            } else if (piece == Board.WR || (piece == Board.WQ && w_attackers_delta[i_min][0] == 0)) {
                int attackers_delta[] = new int[3];
                if (wTurn) {
                    attackers_delta = w_attackers_delta[i_min];
                } else {
                    attackers_delta = b_attackers_delta[i_min];
                }
                int i = attackers_delta[1];
                for (int j=attackers_delta[2]+1; j<RookMove.RookMove[sq][i].length; j++) {
                    int to = RookMove.RookMove[sq][i][j];
                    if (board.board[to] == Board.EM) {
                        continue;
                    } else if (board.board[to] == Board.WR || board.board[to] == Board.WQ || board.board[to] == Board.WK) {
                        w_attackers_pos[w] = to;
                        w_attackers_val[w] = Evaluator.P_VALUE[board.board[to]];
                        w_attackers_delta[w][0] = 0;
                        w_attackers_delta[w][1] = i;
                        w_attackers_delta[w][2] = j;
                        w++;
			break;
                    } else if (board.board[to] == Board.BR || board.board[to] == Board.BQ || board.board[to] == Board.BK) {
                        b_attackers_pos[b] = to;
                        b_attackers_val[b] = Evaluator.P_VALUE[-board.board[to]];
                        b_attackers_delta[b][0] = 0;
                        b_attackers_delta[b][1] = i;
                        b_attackers_delta[b][2] = j;
                        b++;
			break;
                    } else {
                        break;
                    }
                }
            } else if (piece == Board.WP || piece == Board.BP) {
                for (int i=0; i<4; i++) {
                    if (BishopMove.BishopMove[sq][i].length == 0)
                        continue;
                    if (BishopMove.BishopMove[sq][i][0] == attacker_pos) {
                        for (int j=1; j<BishopMove.BishopMove[sq][i].length; j++) {
                            int to = BishopMove.BishopMove[sq][i][j];
                            if (board.board[to] == Board.EM) {
                                continue;
                            } else if (board.board[to] == Board.WB || board.board[to] == Board.WQ || board.board[to] == Board.WK) {
                                w_attackers_pos[w] = to;
                                w_attackers_val[w] = Evaluator.P_VALUE[board.board[to]];
                                w_attackers_delta[w][0] = 1;
                                w_attackers_delta[w][1] = i;
                                w_attackers_delta[w][2] = j;
                                w++;
				break;
                            } else if (board.board[to] == Board.BB || board.board[to] == Board.BQ || board.board[to] == Board.BK) {
                                b_attackers_pos[b] = to;
                                b_attackers_val[b] = Evaluator.P_VALUE[-board.board[to]];
                                b_attackers_delta[b][0] = 1;
                                b_attackers_delta[b][1] = i;
                                b_attackers_delta[b][2] = j;
                                b++;
				break;
                            } else {
                                break;
                            }
                        }
                        break;
                    }
                }
            }
            
            // remove current attacker
            if (wTurn) {
                if (w > 1) {
                    w_attackers_pos[i_min] = w_attackers_pos[w-1];
                    w_attackers_val[i_min] = w_attackers_val[w-1];
                    w_attackers_delta[i_min][0] = w_attackers_delta[w-1][0];
                    w_attackers_delta[i_min][1] = w_attackers_delta[w-1][1];
                    w_attackers_delta[i_min][2] = w_attackers_delta[w-1][2];
                }
                w--;
            } else {
                if (b > 1) {
                    b_attackers_pos[i_min] = b_attackers_pos[b-1];
                    b_attackers_val[i_min] = b_attackers_val[b-1];
                    b_attackers_delta[i_min][0] = b_attackers_delta[b-1][0];
                    b_attackers_delta[i_min][1] = b_attackers_delta[b-1][1];
                    b_attackers_delta[i_min][2] = b_attackers_delta[b-1][2];
                }
                b--;
            }
            
            wTurn = !wTurn;
        }

        while (score_ind > 1) {
            score_ind--;
            if (scores[score_ind-1] > -scores[score_ind]) {
                scores[score_ind-1] = -scores[score_ind];
            }
        }
        return scores[0];
    }
	
    int qnodes = 0;
	public int qsearch(boolean isWhite, Board board, int alpha, int beta, int depth) throws Exception
	{
		qnodes++;
        /*
		if (nodes % 100000 == 0)
		{
			if (nodes % 1000000 == 0)
                System.out.println("nodes searched: " + nodes + " " + (prev-System.currentTimeMillis()));
			prev = System.currentTimeMillis();
            if (System.currentTimeMillis() - start_time > time/40 && dep > 4) {
                FORCE_BREAK = true;
                return 0;
            }
		}*/
        
        if (FORCE_BREAK)
            return 0;
		
        // we assume that we can play a move that lets us do at least as well
        // as the immediate evaluation
		int eval = Evaluator.material(isWhite, board);
        alpha = Math.max(alpha, eval);
        if (alpha > beta)
            return beta;
            
        
        long hash = board.hash;
        if (!isWhite) {
            hash ^= board.t.BLACKS_TURN;
        }
        int move_score = 0;
        if (table.lookup(entry, hash, 0, alpha, beta) != TranspositionTable.HASH_UNKNOWN) {
            move_score = entry.value;
			return move_score;
		}
        
		MoveGenerator mg = new MoveGenerator();
		
		int size = 0;
		
		if (isWhite)
		{
            size = mg.genWhiteCaptures(board, qall[depth]);
		} else {
            size = mg.genBlackCaptures(board, qall[depth]);
		}
        
        for (int i=0; i<size; i++) {
            if (qall[depth][i].type == Move.CAPTURE) {
                qall[depth][i].value = see(board, qall[depth][i], isWhite);
            }
        }
        //MoveGenerator.sortMoves(qall[depth], size);
		
		//int move_score = -300000;
        int n_captures = 0;
        move_score = -300000;
		for (int i=0; i<size; i++)
		{
            int max_val = qall[depth][i].value;
            int max_ind = i;
            for (int j=i+1; j<size; j++) {
                if (qall[depth][j].value > max_val) {
                    max_ind = j;
                    max_val = qall[depth][j].value;
                }
            }
            qall[depth][i].copyTo(qall[depth][size]);
            qall[depth][max_ind].copyTo(qall[depth][i]);
            qall[depth][size].copyTo(qall[depth][max_ind]);
            
			if (board.makeMove(qall[depth][i]))
			{
                if ((isWhite && board.isWhiteChecked()) || (!isWhite && board.isBlackChecked())) { 
                    // can't make moves which put us in check
                    board.unmakeMove(board.state);
                    continue;
                }
                n_captures++;
				board.state.copyTo(qbs[depth]);
			
				move_score = -qsearch(!isWhite, board, -beta, -alpha, depth+1);
                        
                board.unmakeMove(qbs[depth]);
                    
                if (move_score >= beta)
                    return beta;
                        
                alpha = Math.max(alpha, move_score);
			}
		}

        if (n_captures == 0) {
            // TO DO: are we being mated or stalemated??
            if ((isWhite && board.whiteHasValidMove()) || (!isWhite && board.blackHasValidMove())) {
                return eval;
            } else if ((isWhite && board.isWhiteChecked()) || (!isWhite && board.isBlackChecked())) {
                return -200000 + depth; // checkmate
            } else {
                return 0; // stalemate
            }
        }
		return alpha;
	}
	
}
