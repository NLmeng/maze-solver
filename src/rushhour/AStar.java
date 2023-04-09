package rushhour;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class AStar {
    public static void findPath(String inputPath, String outputPath) throws Exception {
        State source = new State(inputPath);
        Graph<State> start = new Graph<>();
        Heuristic h = new MyHeuristic(source);

        aStar(start, new Node<>(source), h, outputPath);
    }

    public static void aStar(Graph<State> graph, Node<State> source, Heuristic h, String outputPath) throws Exception {
		// PriorityQueue to store nodes, sorted by their F score (F = G + H)
        PriorityQueue<Node<State>> openList = new PriorityQueue<>(Comparator.comparingInt(Node::getF));
		// Map to store closed nodes (processed nodes)
        Map<Node<State>, Integer> closedSet = new HashMap<>();

		// Initialize source node's G score (cost from the start node to the current node) and parent
        source.setG(0);
        source.setParent(null);

		// Calculate the H score (estimated cost to reach the goal) for the source node
        h = new MyHeuristic(source.getCurrent());
        source.setH(h.getValue());

		// Add the source node to the graph and open list
        graph.addNode(source);
        openList.add(source);

		// Main loop of the A* algorithm
        while (!openList.isEmpty()) {
			// Remove the node with the lowest F score from the open list
            Node<State> curr = openList.remove();
			// Generate neighbors for the current node
            generateNeighbors(graph, curr);

			// Loop through the neighbors of the current node
            for (Node<State> u : graph.getNeighbours(curr)) {
				// If the neighbor is a goal (solved) state, update its G score and parent, and write the solution
                if (u.getCurrent().isSolved()) {
                    u.setG(curr.getG() + 1);
                    u.setParent(curr);
                    writeSolution(u, outputPath);
                    return;
				// If the neighbor is not in the open or closed list, update its G, H scores and parent, and add it to the open list
                } else if (!openList.contains(u) && !closedSet.containsKey(u)) {
                    h = new MyHeuristic(u.getCurrent());
                    u.setH(h.getValue());
                    u.setG(curr.getG() + 1);
                    u.setParent(curr);
                    openList.add(u);
                }
            }
			// Add the current node to the closed set
            closedSet.put(curr, curr.getF());
        }
		// If no solution is found, print a message
        System.out.println(outputPath + ": no solution found");
    }

    public static void generateNeighbors(Graph<State> graph, Node<State> currentNode) throws Exception {
        State next;
        String moveCode;
        RushHour mover = new RushHour();

		// Read the current state into the RushHour object
        mover.readInto(currentNode.getCurrent().get());
        int l = 4;
		// Loop through the vehicles in the current state
        for (Vehicle v : mover.vehicles.list) {
			// Loop through the possible directions (LEFT, RIGHT, UP, DOWN)
            for (int direction : new int[]{RushHour.LEFT, RushHour.RIGHT, RushHour.UP, RushHour.DOWN}) {
				// Try to move the vehicle in the current direction by l units
                while (l >= 1) {
					// If the move is valid, generate the next state and add it as a neighbor to the graph
                    if (mover.tryMove(v.name, direction, l)) {
                        next = mover.nextState(v, direction, l);
                        moveCode = v.name + (direction == RushHour.LEFT ? "L" : direction == RushHour.RIGHT ? "R" : direction == RushHour.UP ? "U" : "D") + l;
                        next.setStep(moveCode);
                        graph.addNode(next);
                        graph.addEdge(currentNode.getCurrent(), next);
						 // If the next state is a goal state, return early to save time
                        if (next.isSolved()) return;
                        break;
                    }
                    l--;
                }
                l = 4;
            }
        }
    }

    private static <T> void writeSolution(Node<T> target, String outputPath) throws IOException {
        String aS = ((State) target.getCurrent()).getStep();
        target = target.getParent();
		// Traverse the path from the goal node back to the source node
        while (true) {
            aS = ((State) target.getCurrent()).getStep() + "\n" + aS;
            target = target.getParent();
            if (target.getParent() == null)
                break;
        }
		// Write the solution to the output file
        Path path = Paths.get(outputPath);
        byte[] str = aS.getBytes();
        Files.write(path, str);
    }
}
