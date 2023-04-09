package rushhour;
/**
 * Assume heuristic value is always zero
 * @author Lymeng
 *
 */
public class zeroHeuristic implements heuristic {
	
	private int val;
    public zeroHeuristic(state state) {
    	this.val = 0;
    	return;
    }

    public int getValue() {
    	return this.val;
    }

}