package rushhour;

public class node<T> {
	private int h;
	private int g;
	private int f;
	private T current;
//	private boolean visited;
	private node<T> parent;
	
	public node<T> getParent() {
		return parent;
	}

	public void setParent(node<T> parent) {
		this.parent = parent;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
		f=g+this.h;
	}

	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g = g;
		f=this.g+h;
	}

	public int getF() {
		return f;
	}

	public void setF(int f) {
		this.f = f;
	}

	public node(T name) {
		this.current = name;
		hash=computeHash();
	}

	public void setCurrent(T name) {
		this.current = name;
		hash=computeHash();
	}
	
	public T getCurrent() {
		return current;
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
		return 31*((state)current).hashCode();
	}
	@Override
	public int hashCode() {
		return hash;
	}
	@Override
	public boolean equals(Object obj) {
		
		if(this==obj) 
			return true;

		if (!(obj instanceof node<?>)) 
			return false;
		
		if ((this.hashCode() != ((node<?>)obj).hashCode()))
			return false;
		return true;

	}

}
