package rushhour;
/**
 * Assume heuristic value is always zero
 * @author Lymeng
 *
 */
public class ZeroHeuristic implements Heuristic {
	
	private int val;
    public ZeroHeuristic(State state) {
    	this.val = 0;
    	return;
    }

    public int getValue() {
    	return this.val;
    }

}
