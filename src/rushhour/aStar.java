package rushhour;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class aStar {
	
	public static void findPath(String inputPath, String outputPath) throws Exception {
		state source = new state(inputPath);
		graph<state> start = new graph<>();
		heuristic h = null;
		
		A_Star(start, new node<state>(source), h, outputPath);
	}
	
	public static void A_Star(graph<state> G, node<state> source, heuristic h, String outputPath) throws Exception {
		//list to be processed, compare using F=H+G, less is better
		PriorityQueue<node<state>> openList = new PriorityQueue<>(
				new Comparator<node<state>>() {
					public int compare(node<state> n1, node<state> n2) {
						return n1.getF() - n2.getF();
					}
				}
				);
		//set of processed
		Map<node<state>, Integer> closedSet = new HashMap<>();
		//Set<node<state>> closed = new HashSet<>();
		
		//add the starting node
		source.setG(0);
		source.setParent(null);
		
		//starting H
		h=new myHeuristic(source.getCurrent());
		source.setH(h.getValue());
		
		//add the start state
		G.addnode(source);
		openList.add(source);		

//		Map<state, Integer> openSet = new HashMap<>();
//		openSet.put(source.getCurrent(), source.getF());

		while(!openList.isEmpty()) {
			
			node<state> curr = openList.remove();
			//System.out.println(curr.getF());
			generateNeighbors(G, curr);
			Set<node<state>> currNeighbors = G.getNeighbours(curr);
			for(node<state> u : currNeighbors) {
				
				if(u.getCurrent().isSolved()) {
					u.setG(curr.getG()+1);
					u.setParent(curr);
					writeSolution(u, outputPath);
					return;
				} 
				else if(!openList.contains(u) && !closedSet.containsKey(u)) {
				//else if(!openList.contains(u) && !closed.contains(u)) {
					//calculate heuristic then set values and parent
					h=new myHeuristic(u.getCurrent());
					u.setH(h.getValue());
					u.setG(curr.getG()+1);
					u.setParent(curr);
					openList.add(u);
				} 
				
			}
			closedSet.put(curr, curr.getF());	
				
//				if(u.getCurrent().isSolved()) {
//					u.setG(curr.getG()+1);
//					u.setParent(curr);
//					writeSolution(u,outputPath);
//					return;
//				}
//				else { 
//					//calculate F
//					h=new myHeuristic(u.getCurrent());
//					u.setH(h.getValue());
//					u.setG(curr.getG()+1);
//					
//					if(openSet.containsKey(u.getCurrent()) && (openSet.get(u.getCurrent()) > u.getF())) {
////						System.out.print(openList.remove(u));
//						openList.remove(u);
//
//						u.setParent(curr);
//						
//						openList.add(u);
//						openSet.put(u.getCurrent(), u.getF());
//					}
//					else if(closedSet.containsKey(u) && (closedSet.get(u) > u.getF())) {
////						System.out.print(closedSet.remove(u));
//						closedSet.remove(u);
//
//						u.setParent(curr);
//						
//						openList.add(u);
//						openSet.put(u.getCurrent(), u.getF());
//					} else if (!openList.contains(u) && !closedSet.containsKey(u)) {
//						u.setParent(curr);
//						
//						openList.add(u);
//						openSet.put(u.getCurrent(), u.getF());
//					}
//				}
//			}
			//closed.add(curr);
		}
		System.out.println(outputPath + ": no solution found");
	}
	
	
	
	/**
	 * Generate all states derived from the current state
	 * @param Graph
	 * @param currentNode
	 * @throws Exception
	 */
	public static void generateNeighbors(graph<state> Graph, node<state> currentNode) throws Exception {
		state next;
		String moveCode;
		RushHour mover = new RushHour();
		
		mover.readInto(currentNode.getCurrent().get());
		int l = 4;
		for(Vehicle v : mover.vehicles.list) {
			while(l>=1) {
				if(mover.tryMove(v.name, RushHour.LEFT, l)) {
					next = mover.nextState(v, RushHour.LEFT, l);
					moveCode = v.name + "L" + l;
					next.setStep(moveCode);
					Graph.addnode(next);
					Graph.addEdge(currentNode.getCurrent(), next);
					if(next.isSolved()) return;
					break;
				}
				l--;
			}
			l=4;
			while(l>=1) {
				if(mover.tryMove(v.name, RushHour.RIGHT, l)) { 
					next = mover.nextState(v, RushHour.RIGHT, l);
					moveCode = v.name + "R" + l;
					next.setStep(moveCode);
					Graph.addnode(next);
					Graph.addEdge(currentNode.getCurrent(), next);
					if(next.isSolved()) return;
					break;
				}
				l--;
			}
			l=4;
			while(l>=1) {
				if(mover.tryMove(v.name, RushHour.UP, l)) {
					next = mover.nextState(v, RushHour.UP, l);
					moveCode = v.name + "U" + l;
					next.setStep(moveCode);
					Graph.addnode(next);
					Graph.addEdge(currentNode.getCurrent(), next);
					if(next.isSolved()) return;
					break;
				}
				l--;
			}
			l=4;
			while(l>=1) {
				if(mover.tryMove(v.name, RushHour.DOWN, l)) {
					next = mover.nextState(v, RushHour.DOWN, l);
					moveCode = v.name + "D" + l;
					next.setStep(moveCode);
					Graph.addnode(next);
					Graph.addEdge(currentNode.getCurrent(), next);
					if(next.isSolved()) return;
					break;
				}
				l--;
			}
			l=4;
		}
	}
	//backtrack print
	private static <T> void printPathRec(node<T> target) {
		if (target.getParent() == null) {
			((state)target.getCurrent()).display();
		}
		else {
			printPathRec(target.getParent());
			System.out.println(" -> " + ((state)target.getCurrent()).getStep()); 
			((state)target.getCurrent()).display();
		}
	}
	private static <T> void printPath(node<T> target) {
		printPathRec(target);
		System.out.println();
	}
	//backtrack write from solved state to beginning
	private static <T> void writeSolution(node<T> target, String outputPath) throws IOException {
		String aS = ((state)target.getCurrent()).getStep();
		target=target.getParent();
		while(true) {
			aS = ((state)target.getCurrent()).getStep() + "\n" + aS;
			target=target.getParent();
			if(target.getParent()==null)
				break;
		}
		Path path = Paths.get(outputPath);
		byte[] str = aS.getBytes();
		Files.write(path, str);
	}
}
