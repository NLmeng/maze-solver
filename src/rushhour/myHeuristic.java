package rushhour;
/**
 * 
 * @author Mengo
 *
 */
public class myHeuristic implements heuristic {
	
	private int val;
	/**
	 * Analyze a board's state using the following algorithm:
	 * 1.Find the row with red car
	 * 2.Calculate distance of red car to the exit, assuming an empty-space is one step less
	 * 3.Calculate the number of cars blocking the path to exit
	 * 4.Calculate the number of cars blocking (3)
	 * 5.Set the heuristic value to be the weighted average of (4) and (2)
	 * @param State
	 * @throws Exception
	 */
	public myHeuristic(state State) throws Exception {
		char[][] b = State.get();
		
		//number of cars blocking red-car, and cars blocking those:
		int curBlock = 0;
		//distance of red-car from exit:
		int fromExit = 4;
		
		boolean found = false;
		for(int i = 0; i < 6; i++) {
			//solved
			if(b[i][5]=='X') {
				val = 0;
				return;
			}
			
			for(int j = 0; j < 6; j++) {
				if(b[i][j]!='X' && !found)
					continue;
				//reach the X row
				else if(b[i][j]=='X') {
					found=true;
					fromExit-=j;
					j+=2;
				}
				//ignore '.'
				if(b[i][j]=='.') 
					fromExit--;
				if(b[i][j]!='.') {
					//blocking red directly, close to exit = higher cost
					curBlock+=j;
					//blocking red's blockers
					for(int col = 0; col < 6; col++) {
						//assume need at least 2
						if(b[col][j]!=b[i][j]) {
							curBlock+=2;
						}
						//case of vertical
						if(col>0) {
							if(b[col-1][j]==b[col][j]) {
							curBlock--;
							}
						}
					}
				}
			}
			//checked X row
			if(found)
				break;
		}
		
		val=curBlock+fromExit;
		
		//weighted average
		//val=(curBlock+fromExit)/2;

    	return;
    }
	
    /**
     * Returns the value of the heuristic
     */
    public int getValue() {
    	return val;
    }
}
