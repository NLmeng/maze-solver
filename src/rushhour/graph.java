package rushhour;
import java.util.*;

public class graph<T>{
	
	private Set<node<T>> v;
	private Map<node<T>, Set<node<T>>> e;
	
	public graph() {
		v = new HashSet<node<T>>();
		e = new HashMap<node<T>, Set<node<T>>>();
	}
	public Set<node<T>> getv() {
		return v;
	}
	
	public node<T> getnodeByName(T name) {
		for (node<T> v : v)
			if (v.getCurrent().equals(name))
				return v;

		return null;
	}

	public Set<node<T>> getNeighbours(node<T> v) {
		return e.get(v);
	}

	public boolean addnode(node<T> v) {
		boolean newNode = this.v.add(v);
		if (newNode)
			e.put(v, new HashSet<node<T>>());
		return newNode;
	}

	public boolean addnode(T data) {
		return addnode(new node<T>(data));
	}

	public boolean addEdge(node<T> v, node<T> u) {
		if (this.v.contains(v) && this.v.contains(u))
			return (e.get(v).add(u) && e.get(u).add(v)); 
		else
			return false;
	}

	public boolean addEdge(T v, T u) {
		return addEdge(getnodeByName(v), getnodeByName(u));
	}

	public boolean removeEdge(node<T> v, node<T> u) {
		if (this.v.contains(v) && this.v.contains(u))
			return (e.get(v).remove(u) && e.get(u).remove(v)); 
		else
			return false;
	}
	
//	private void setAllUnvisited() {
//		for (node<T> v : v) {
//			v.setVisited(false);
//			v.setParent(null);
//		}
//	}
	
}
