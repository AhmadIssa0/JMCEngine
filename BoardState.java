

public class BoardState {
	int EP;
    int PREV_EP; // previous en passant square if any (i.e. behind the double pawn push)
                 // when we unmake a move we set the PREV_EP to be the new EP
    int last_irrev_move_no;
	int from;
	int fromPiece;
	int to;
	int toPiece;
	int type;
    int b_castle_rights = Board.Q_CASTLE | Board.K_CASTLE;
    int w_castle_rights = Board.Q_CASTLE | Board.K_CASTLE;
	boolean promote=false;
	
	public void copyTo(BoardState bs)
	{
		bs.EP = EP;
        bs.PREV_EP = PREV_EP;
		bs.from = from;
		bs.fromPiece = fromPiece;
		bs.to = to;
		bs.toPiece = toPiece;
		bs.type = type;
		bs.promote = promote;
        bs.last_irrev_move_no = last_irrev_move_no;
        bs.b_castle_rights = b_castle_rights;
        bs.w_castle_rights = w_castle_rights;
	}
}