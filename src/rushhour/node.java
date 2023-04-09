package rushhour;

public class Node<T> {
	private int h;
	private int g;
	private int f;
	private T current;
//	private boolean visited;
	private Node<T> parent;
	
	public Node<T> getParent() {
		return this.parent;
	}

	public void setParent(Node<T> parent) {
		this.parent = parent;
	}

	public int getH() {
		return this.h;
	}

	public void setH(int h) {
		this.h = h;
		this.f=this.g+this.h;
	}

	public int getG() {
		return this.g;
	}

	public void setG(int g) {
		this.g = g;
		this.f=this.g+this.h;
	}

	public int getF() {
		return this.f;
	}

	public void setF(int f) {
		this.f = f;
	}

	public Node(T name) {
		this.current = name;
		this.hash=this.computeHash();
	}

	public void setCurrent(T name) {
		this.current = name;
		this.hash=this.computeHash();
	}
	
	public T getCurrent() {
		return this.current;
	}

//	public void setVisited(boolean flag) {
//		visited = flag;
//	}
//
//	public boolean isVisited() {
//		return visited;
//	}
	
	private int hash;
	private int computeHash() {
		return 31*((State)this.current).hashCode();
	}
	@Override
	public int hashCode() {
		return this.hash;
	}
	@Override
	public boolean equals(Object obj) {
		
		if(this==obj) 
			return true;

		if (!(obj instanceof Node<?>)) 
			return false;
		
		if ((this.hashCode() != ((Node<?>)obj).hashCode()))
			return false;
		return true;

	}

}
