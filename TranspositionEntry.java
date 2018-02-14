
public class TranspositionEntry {
	
	long key;
	int depth;
	int flags;
	int value;
	Move best_move;
    
    public TranspositionEntry() {
        best_move = new Move();
    }
    
    public void copyTo(TranspositionEntry entry) {
        entry.key = key;
        entry.depth = depth;
        entry.flags = flags;
        entry.value = value;
        entry.best_move = best_move;
    }
}