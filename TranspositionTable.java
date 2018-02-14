

import java.util.*;
import java.io.*;

public class TranspositionTable {
	// piece type, piece colour, square which contains piece.
    // TO DO: Incorporate castling rights into hash keys.
	
	static final int TABLE_SIZE = 10000000;//52428 * 32; // 10 indicates amount of megabytes
	static final int HASH_EXACT = 2;
	static final int HASH_LOWERBOUND = 4;
	static final int HASH_UPPERBOUND = 8;
	static final int HASH_UNKNOWN = 31313131;
	
	static long zobrist[][][] = new long[7][2][64];
	TranspositionEntry table[] = new TranspositionEntry[TABLE_SIZE];
	static long BLACKS_TURN;
    static long EN_PASSANT[] = new long[64];
	
	private static TranspositionTable t;
	
	private TranspositionTable()
	{
        for (int i=0; i<TABLE_SIZE; i++)
		{
			table[i] = new TranspositionEntry();
		}
        // load keys from a file
		String hashes[] = Logger.readFile("zobrist.txt").split("\n");
        int ind=0;
        for (int i=0; i<7; i++)
			for (int j=0; j<2; j++)
				for (int k=0; k<64; k++)
					zobrist[i][j][k] = Long.parseLong(hashes[ind++]);
		
		BLACKS_TURN = Long.parseLong(hashes[ind++]);
        for (int i=0; i<EN_PASSANT.length; i++)
            EN_PASSANT[i] = Long.parseLong(hashes[ind++]);
	}
    
    public void randomZobrist() {
        Random rand = new Random();
		for (int i=0; i<7; i++)
			for (int j=0; j<2; j++)
				for (int k=0; k<64; k++)
					zobrist[i][j][k] = rand.nextLong();

		BLACKS_TURN = rand.nextLong();
        for (int i=0; i<EN_PASSANT.length; i++)
            EN_PASSANT[i] = rand.nextLong();
    }
    
    public void saveZobrist(String filename) {
        try{
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename)));
            for (int i=0; i<7; i++)
                for (int j=0; j<2; j++)
                    for (int k=0; k<64; k++) {
                        bw.write(""+zobrist[i][j][k]);
                        bw.newLine();
                        bw.flush();
                    }
                
            bw.write(""+BLACKS_TURN);
            bw.newLine();
        
            for (int i=0; i<EN_PASSANT.length; i++) {
                bw.write(""+EN_PASSANT[i]);
                bw.newLine();
                bw.flush();
            }
            bw.close();
        } catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	public static TranspositionTable getTable()
	{
		if (t == null)
		{
			t = new TranspositionTable();
		}
		
		return t;
	}
	
	public static long getHash(Board board, boolean isWhite)
	{
		long hash = 0;
		
		for (int i=0; i<64; i++)
		{
			if (board.board[i] != 0)
			{
				if (board.board[i] > 0)
				{
					hash ^= zobrist[board.board[i]][0][i];
				} else {
					hash ^= zobrist[-board.board[i]][1][i];
				}
			}
		}
		
		if (!isWhite)
		{
			hash ^= BLACKS_TURN;
		}
        
        if (board.state.EP > 0) {
            hash ^= EN_PASSANT[board.state.EP];
        }
		
		return hash;
	}
	
	public void store(int depth, int val, int hashf, long key, Move best_move)
	{
        int i = (int)Math.abs(key % TABLE_SIZE);
		if ((depth >= table[i].depth) ||
			(key != table[i].key))
			{
				table[i].key = key;
				table[i].depth = depth;
				table[i].value = val;
				table[i].flags = hashf;
                if (best_move != null) {
                    best_move.copyTo(table[i].best_move);
                }
			}
	}
    
    public TranspositionEntry getEntry(long key) {
        TranspositionEntry entry = table[(int)Math.abs(key % TABLE_SIZE)];
        if (entry.key == key)
            return entry;
        return null;
    }
	
	public int lookup(TranspositionEntry entry, long key, int depth, int alpha, int beta) {
		table[(int)Math.abs(key % TABLE_SIZE)].copyTo(entry);
		if (entry.key == key) {
			if (entry.depth >= depth) {
				if (entry.flags == HASH_EXACT) {
					return entry.flags;
				} else if ((entry.flags == HASH_UPPERBOUND) && (entry.value <= alpha)) {
					return entry.flags;
				} else if ((entry.flags == HASH_LOWERBOUND) && (entry.value >= beta)) {
					return entry.flags;
				}
			}
		}
		
		return HASH_UNKNOWN;
	}
	
	
}
