
public class Move {
	static final int NORMAL = 0;
	static final int CAPTURE = 1;
	static final int EN_PASSANT = 2;
	static final int DOUBLE_PUSH = 3;
	static final int KCASTLE = 4;
	static final int QCASTLE = 5;
	int from;
	int to;
	int promote;
	int type;
	int value;
	
	public void copyTo(Move move)
	{
		move.from = from;
		move.to = to;
		move.promote = promote;
		move.type = type;
		move.value = value;
	}
}