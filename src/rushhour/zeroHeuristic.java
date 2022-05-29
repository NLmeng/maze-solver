package rushhour;
/**
 * Assume heuristic value is always zero
 * @author Mengo
 *
 */
public class zeroHeuristic implements heuristic {
	
	private int val;
    public zeroHeuristic(state state) {
    	val = 0;
    	return;
    }

    public int getValue() {
    	return val;
    }

}