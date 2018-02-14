

public class Test {

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
    
    public Test() {
        Board b = new Board();
        /*
        b.board = new int[]{
		EM, EM, EM, EM, EM, EM, EM, EM,
		BP, BK, BP, WN, EM, EM, EM, EM,
		EM, BP, EM, EM, EM, EM, EM, EM,
		EM, EM, WP, EM, EM, EM, EM, EM,
		EM, EM, EM, EM, EM, EM, EM, EM,
		EM, EM, EM, EM, EM, EM, EM, EM,
		EM, WQ, EM, EM, EM, EM, EM, EM,	
		EM, EM, EM, EM, EM, EM, EM, EM,			
        };*/
        b.board = new int[]{
		EM, EM, EM, EM, EM, EM, EM, EM,
		EM, EM, EM, EM, BR, EM, EM, EM,
		EM, EM, EM, EM, WP, EM, EM, EM,
		EM, EM, EM, WP, EM, EM, EM, EM,
		EM, EM, EM, EM, EM, EM, EM, EM,
		EM, EM, EM, EM, EM, EM, EM, EM,
		EM, EM, EM, EM, EM, EM, EM, EM,
		EM, EM, EM, EM, EM, EM, EM, EM	
        };
        SearchAgent sa = new SearchAgent();
        Move move = new Move();
        move.from = 12;
        move.to = 20;
        System.out.println(sa.see(b, move, false));
    }
    
    public static void main(String[] args) {
        new Test();
    }
}