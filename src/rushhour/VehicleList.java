package rushhour;
import java.util.*;

/**
 * @author Mengo : Store Objects of vehicle, their names, coordinates, types, and directions
 */
public class VehicleList {
	//bucket of vehicles and their coordinates
	public List<Vehicle> list;
	
	VehicleList(){
		list = new ArrayList<>();
	}
	
	public void add(char name, int y, int x) {
		int id = contains(name);
		if(id ==-1) {
			Vehicle newVehicle = new Vehicle(name, y, x);
			list.add(newVehicle);
		} else {
			list.get(id).addCoord(y, x);
		}
	}
	public int contains(char name) {
		int i = 0;
		for(Vehicle cur : list) {
			if(cur.name==name) return i;
			i++;
		}
		return -1;
	}
	public void clear() {
		list.clear();
		list = new ArrayList<>();
	}
	public void format() throws Exception {
		for(int i = 0; i < list.size(); i++) {
			Vehicle cur = list.get(i);
			//set type
			if(cur.coord.size()==2) 
				cur.type = 'c';
			else if(cur.coord.size()==3)
				cur.type = 't';
			else throw new Exception("invalid length of vehicle");
			//set direction
			if(cur.coord.get(0).get(0) == cur.coord.get(1).get(0))
				cur.direction = 'h';
			else if(cur.coord.get(0).get(1) == cur.coord.get(1).get(1))
				cur.direction = 'v';
			else throw new Exception("invalid direction of vehicle");
			
		}
	}
	
	public void display() {
		for(int i = 0; i < list.size(); i++) {
			System.out.println(i+1 + ")" + list.get(i) + " ");
		}
	}
}
