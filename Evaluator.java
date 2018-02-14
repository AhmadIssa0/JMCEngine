
public class Evaluator {
	public static final int W_PAWN_VAL = 100;
	public static final int W_KNIGHT_VAL = 325;
	public static final int W_BISHOP_VAL = 350;
	public static final int W_ROOK_VAL = 500;
	public static final int W_QUEEN_VAL = 975;
	public static int W_KING_VAL = 100000;
	
	public static final int B_PAWN_VAL = 100;
	public static final int B_KNIGHT_VAL = 325;
	public static final int B_BISHOP_VAL = 350;
	public static final int B_ROOK_VAL = 500;
	public static final int B_QUEEN_VAL = 975;
	public static  int B_KING_VAL = 100000;
    public static int P_VALUE[] = new int[]{0, W_PAWN_VAL, W_KNIGHT_VAL, W_BISHOP_VAL, W_ROOK_VAL, W_QUEEN_VAL, W_KING_VAL};
    
    public static final int PV_REWARD = 10000;
	
	static final int WP_MAX[] = {
		0, 0, 0, 0, 0, 0, 0, 0,		
		60,60,60,60,60,60,60,60,		
		0, 0, 0, 0, 0, 0, 0, 0,		
		0, 0, 0, 0, 0, 0, 0, 0,	
		0, 0, 5,50,50,-25,-40,-30,
		0, 0, 0,20,20,-25,-25,0,	
		0, 0, 0,-5,-5,10,60,10,		
		0, 0, 0, 0, 0, 0, 0, 0,	
	};
    
	static final int WP_END[] = {
		0, 0, 0, 0, 0, 0, 0, 0,		
		70,70,70,70,70,70,70,70,	
		40,40,40,40,40,40,40,40,		
		20,20,20,20,20,20,20,20,	
		10,10,10,10,10,10,10,10,	
		5, 5, 5, 5, 5, 5, 5, 5,	
		0, 0, 0, 0, 0, 0, 0, 0,	
		0, 0, 0, 0, 0, 0, 0, 0,	
	};
    
	static final int BP_END[] = {
		0, 0, 0, 0, 0, 0, 0, 0,	
		0, 0, 0, 0, 0, 0, 0, 0,	
		5, 5, 5, 5, 5, 5, 5, 5,	
		10,10,10,10,10,10,10,10,	
		20,20,20,20,20,20,20,20,	
		40,40,40,40,40,40,40,40,		
		70,70,70,70,70,70,70,70,	
		0, 0, 0, 0, 0, 0, 0, 0,		
	};

	static final int WB_MAX[] = {
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0,11, 0, 0, 0, 0,11, 0,
		0, 0,12, 0, 0, 12, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0,19, 0, 8, 8, 0,20, 0,
		0, 0,-29,0, 0,-30, 0, 0		
	};
	
	static final int WN_MAX[] = {
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 2, 2, 2, 2, 2, 2, 0,
		0, 2, 3, 3, 3, 3, 2, 0,
		0, 2, 3, 4, 4, 3, 2, 0,
		0, 2, 3, 4, 4, 3, 2, 0,		
		0, 2, 3, 3, 3, 3, 2, 0,
		0, 2, 2, 2, 2, 2, 2, 0,
		0, -29, 0, 0, 0, 0, -30, 0
	};
	
	static final int WR_MAX[] = {
		0, 0, 0, 0, 0, 0, 0, 0,
		40,40,40,40,40,40,40,40,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		-10, 0, 0, 0, 0, 0, 0,-15			
	};	
	
	static final int WQ_MAX[] = {
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 3, 3, 0, 0, 0, 0,
		0, 0, 0,20, 0, 0, 0, 0			
	};
	
	static final int WK_MAX[] = {
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,	
		0, 0, 0, 0, 0, 0, 0, 0,	
		0, 0, 0,-10,-10,-10, 0, 0,
		0, 10,5,-10, -5,-20,30,30,			
	};
    
	static final int WK_END[] = {
		0, 0, 0, 0, 0, 0, 0, 0,
		0,10,10,10,10,10,10, 0,
		0,10,15,15,15,15,10, 0,
		0,10,15,20,20,15,10, 0,
		0,10,15,20,20,15,10, 0,		
		0,10,15,15,15,15,10, 0,
		0,10,10,10,10,10,10, 0,
		0, 0, 0, 0, 0,10,-20,-20		
	};
	
	
	static final int BP_MAX[] = {
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0,-5,-5,10,60,10,
		0, 0, 0,20,20,-25,-25,0,
		0, 0, 5,50,50,-25,-40,-30,
		0, 0, 0, 0, 0, 0, 0, 0,	
		0, 0, 0, 0, 0, 0, 0, 0,	
		60,60,60,60,60,60,60,60,		
		0, 0, 0, 0, 0, 0, 0, 0,	
	};
	
	static final int BN_MAX[] = {
		0,-29, 0, 0, 0, 0,-30, 0,
		0, 2, 2, 2, 2, 2, 2, 0,
		0, 2, 4, 3, 3, 4, 2, 0,
		0, 2, 3, 4, 4, 3, 2, 0,
		0, 2, 3, 4, 4, 3, 2, 0,		
		0, 2, 3, 3, 3, 3, 2, 0,
		0, 2, 2, 2, 2, 2, 2, 0,
		0, 0, 0, 0, 0, 0, 0, 0		
	};
	
	static final int BB_MAX[] = {
		0, 0,-29, 0, 0,-30, 0, 0,
		0,19, 0, 0, 0, 0,20, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0,12, 0, 0, 12, 0, 0,
		0,11, 0, 0, 0, 0,11, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0		
	};
	
	static final int BR_MAX[] = {
	  -10, 0, 0, 0, 0, 0, 0,-15,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
	   40,40,40,40,40,40,40,40,
		0, 0, 0, 0, 0, 0, 0, 0,		
	};	
	
	static final int BQ_MAX[] = {
		0, 0, 0,20, 0, 0, 0, 0,
		0, 0, 3, 3, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0			
	};
	
	static final int BK_MAX[] = {
		0, 10,5,-10,-5,-20,30,30,
		0, 0, 0,-10,-10,-10, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,	
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,			
	};

	static final int BK_END[] = {
		0, 0, 0, 0, 0, 0,-20,-20,
		0,10,10,10,10,10,10, 0,
		0,10,15,15,15,15,10, 0,
		0,10,15,20,20,15,10, 0,
		0,10,15,20,20,15,10, 0,		
		0,10,15,15,15,15,10, 0,
		0,10,10,10,10,10,10, 0,
		0, 0, 0, 0, 0,0,0,0	
	};
    
    static final int pawnPhase = 0;
    static final int knightPhase = 1;
    static final int bishopPhase = 1;
    static final int rookPhase = 2;
    static final int queenPhase = 1;
    static final int totalPhase = pawnPhase*16 + knightPhase*4 + bishopPhase*4 + rookPhase*4 + queenPhase*2;
    static int wPawns[] = new int[8]; // wPawns[i] number of pawns in column i
    static int bPawns[] = new int[8]; // bPawns[i] number of pawns in column i
	
	public static int material(boolean whiteTurn, Board board)
	{
        // opening and ending material evaluations
		int black=0, white=0;
        int e_black=0, e_white=0; // endgame
		int phase = totalPhase;
        
        for (int i=0; i<8; i++) {
            wPawns[i] = 0;
            bPawns[i] = 0;
        }
        
		for (int j=0;j<board.nWhitePieces;j++)
		{
            int i = board.wPieces[j];
			int piece = board.board[i];
			if (piece == Board.WP)
			{
                phase -= pawnPhase;
				white += W_PAWN_VAL;
				white += WP_MAX[i];
				e_white += W_PAWN_VAL;
				e_white += WP_END[i];
                wPawns[Board.column[i]]++;
			}
			else if (piece == Board.WN)
			{
                phase -= knightPhase;
				white += W_KNIGHT_VAL;
				white += WN_MAX[i];
				e_white += W_KNIGHT_VAL;
				e_white += WN_MAX[i];
			}
			else if (piece == Board.WB)
			{
                phase -= bishopPhase;
				white += W_BISHOP_VAL;
				white += WB_MAX[i];
				e_white += W_BISHOP_VAL;
				e_white += WB_MAX[i];
			}
			else if (piece == Board.WR)
			{
                phase -= rookPhase;
				white += W_ROOK_VAL;
				e_white += W_ROOK_VAL;
			}
			else if (piece == Board.WQ)
			{
                phase -= queenPhase;
				white += W_QUEEN_VAL;
				white += WQ_MAX[i];
				e_white += W_QUEEN_VAL;
				e_white += WQ_MAX[i];
			}
			else if (piece == Board.WK)
			{
				white += W_KING_VAL;
				white += WK_MAX[i];
				e_white += W_KING_VAL;
				e_white += WK_END[i];
			}
        }
        for (int j=0; j<board.nBlackPieces; j++) {
            int i = board.bPieces[j];
			int piece = board.board[i];
            if (piece == Board.BP)
			{
                phase -= pawnPhase;
				black += BP_MAX[i];
				black += B_PAWN_VAL;
				e_black += BP_END[i];
				e_black += B_PAWN_VAL;
                bPawns[Board.column[i]]++;
			}
			else if (piece == Board.BN)
			{
                phase -= knightPhase;
				black += B_KNIGHT_VAL;
				black += BN_MAX[i];
				e_black += B_KNIGHT_VAL;
				e_black += BN_MAX[i];
			}
			else if (piece == Board.BB)
			{
                phase -= bishopPhase;
				black += B_BISHOP_VAL;
				black += BB_MAX[i];
				e_black += B_BISHOP_VAL;
				e_black += BB_MAX[i];
			}
			else if (piece == Board.BR)
			{
                phase -= rookPhase;
				black += B_ROOK_VAL;
				e_black += B_ROOK_VAL;
			}
			else if (piece == Board.BQ)
			{
                phase -= queenPhase;
				black += B_QUEEN_VAL;
				black += BQ_MAX[i];
				e_black += B_QUEEN_VAL;
				e_black += BQ_MAX[i];
			}
			else if (piece == Board.BK)
			{
				black += B_KING_VAL;
				black += BK_MAX[i];
				e_black += B_KING_VAL;
				e_black += BK_END[i];
			}
		}
        
        // pawn considerations
        if (wPawns[0] > 0 && wPawns[1] == 0) {
            white -= 30;
            e_white -= 30;
        }
        if (wPawns[7] > 0 && wPawns[6] == 0) {
            white -= 30;
            e_white -= 30;
        }
        // passed pawn
        if (wPawns[0] > 0 && bPawns[0] == 0 && bPawns[1] == 0) {
            e_white += 70;
        }
        if (wPawns[7] > 0 && bPawns[7] == 0 && bPawns[6] == 0) {
            e_white += 70;
        }
        
        if (bPawns[0] > 0 && bPawns[1] == 0) {
            black -= 30;
            e_black -= 30;
        }
        if (bPawns[7] > 0 && bPawns[6] == 0) {
            black -= 30;
            e_black -= 30;
        }
        // passed pawn
        if (bPawns[0] > 0 && wPawns[0] == 0 && wPawns[1] == 0) {
            e_black += 70;
        }
        if (bPawns[7] > 0 && wPawns[7] == 0 && wPawns[6] == 0) {
            e_black += 70;
        }
        
        for (int i=1; i<7; i++) {
            if (wPawns[i] > 0 && wPawns[i-1] == 0 && wPawns[i+1] == 0) { // isolated white
                white -= 30;
                e_white -= 30;
            }
            if (wPawns[i] > 0 && bPawns[i] == 0 && bPawns[i-1] == 0 && bPawns[i+1] == 0) { // passer
                e_white += 70;
            }
            if (wPawns[i] > 1) { // doubled
                white -= 30*(wPawns[i]-1);
                e_white -= 30*(wPawns[i]-1);
            }
            if (bPawns[i] > 0 && bPawns[i-1] == 0 && bPawns[i+1] == 0) { // isolated black
                black -= 30;
                e_black -= 30;
            }
            if (bPawns[i] > 0 && wPawns[i] == 0 && wPawns[i-1] == 0 && wPawns[i+1] == 0) { // passer
                e_black += 70;
            }
            if (bPawns[i] > 1) { // doubled
                black -= 30*(bPawns[i]-1);
                e_black -= 30*(bPawns[i]-1);
            }
        }
	// Mobility, only counts pseudo valid moves
	/*
	white += Mobility.whiteMobility(board)*3;
	e_white += Mobility.whiteMobility(board)*3;
	black += Mobility.blackMobility(board)*3;
	e_black += Mobility.blackMobility(board)*3;
	*/
	
		phase = (phase * 256 + totalPhase/2)/totalPhase;
		if (whiteTurn)
		{
			return ((white - black) * (256 - phase) + (e_white - e_black) * phase)/256;
		} else {
			return ((black - white) * (256 - phase) + (e_black - e_white) * phase)/256;
		}
	}
}
