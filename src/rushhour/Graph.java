package rushhour;
import java.util.*;

public class Graph<T> {
	
	private Set<Node<T>> v;
	private Map<Node<T>, Set<Node<T>>> e;
	
	public Graph() {
		this.v = new HashSet<Node<T>>();
		this.e = new HashMap<Node<T>, Set<Node<T>>>();
	}
	public Set<Node<T>> getv() {
		return this.v;
	}
	
	public Node<T> getNodeByName(T name) {
		for (Node<T> v : this.v)
			if (v.getCurrent().equals(name))
				return v;

		return null;
	}

	public Set<Node<T>> getNeighbours(Node<T> v) {
		return this.e.get(v);
	}

	public boolean addNode(Node<T> v) {
		boolean newNode = this.v.add(v);
		if (newNode)
			this.e.put(v, new HashSet<Node<T>>());
		return newNode;
	}

	public boolean addNode(T data) {
		return this.addNode(new Node<T>(data));
	}

	public boolean addEdge(Node<T> v, Node<T> u) {
		if (this.v.contains(v) && this.v.contains(u))
			return (this.e.get(v).add(u) && this.e.get(u).add(v)); 
		else
			return false;
	}

	public boolean addEdge(T v, T u) {
		return this.addEdge(this.getNodeByName(v), this.getNodeByName(u));
	}

	public boolean removeEdge(Node<T> v, Node<T> u) {
		if (this.v.contains(v) && this.v.contains(u))
			return (this.e.get(v).remove(u) && this.e.get(u).remove(v)); 
		else
			return false;
	}
	
//	private void setAllUnvisited() {
//		for (Node<T> v : v) {
//			v.setVisited(false);
//			v.setParent(null);
//		}
//	}
	
}
