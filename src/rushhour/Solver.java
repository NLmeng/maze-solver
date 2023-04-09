package rushhour;

public class Solver {
	
	public static void solveFromFile(String inputPath, String outputPath) throws Exception {
		AStar.findPath(inputPath, outputPath);
	}
	
}
