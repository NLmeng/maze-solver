package rushhour;
import java.util.*;

/**
 * @author Mengo
 *
 */
public class Vehicle{
	protected char name;
	protected List<List<Integer>> coord = new ArrayList<>();
	protected char type;
	protected char direction;
	//constructor
	Vehicle(char name, int y, int x){
		this.name=name;
		List<Integer> tempCoord = new ArrayList<>();
		tempCoord.add(y); tempCoord.add(x);
		coord.add(tempCoord);
	}
	
	public void addCoord(int y, int x) {
		List<Integer> tempCoord = new ArrayList<>();
		tempCoord.add(y); tempCoord.add(x);
		coord.add(tempCoord);
	}
	
	public String toString() {
		return "Car: " + name + ": " + coord.toString() + "\n" + "-Type: " + type + " -Direction: " + direction + "\n";
	}
	public int rightMost() {
		if(type=='c') 
			return 1;
		else if(type=='t')
			return 2;
		else {
			System.out.println("unexpected error");
			return coord.size()-1;
		}
	}
}
