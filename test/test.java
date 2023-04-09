import java.io.File;
import java.util.Scanner;
import rushhour.*;

public class test {

    public static int dirChar2Int(char d) {
        switch (d) {
            case 'U':
                return RushHour.UP;
            case 'D':
                return RushHour.DOWN;
            case 'L':
                return RushHour.LEFT;
            case 'R':
                return RushHour.RIGHT;
            default:
                throw new IllegalArgumentException("Unexpected direction: " + d);
        }
    }

    public static void testSolution(String puzzleName, String solName) {
        try {
            RushHour puzzle = new RushHour(puzzleName);
            Scanner scannerSolution = new Scanner(new File(solName));

            while (scannerSolution.hasNextLine()) {
                String line = scannerSolution.nextLine();
                if (line.length() != 3) {
                    throw new IllegalMoveException(line);
                }
                puzzle.makeMove(line.charAt(0), dirChar2Int(line.charAt(1)), line.charAt(2) - '0');
            }

            if (puzzle.isSolved()) {
                System.out.println("Solved");
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void runTests(String prefix, int startIndex, int endIndex) {
		for (int i = startIndex; i <= endIndex; i++) {
			String inFile = String.format("test/test_files/%s%02d.txt", prefix, i);
			String outFile = String.format("test/test_files/%s%02d.sol", prefix, i);
			try {
				rushhour.Solver.solveFromFile(inFile, outFile);
				RushHour game = new RushHour(inFile);
				game.testSol(outFile);
			} catch (Exception e) {
				System.out.println("Error in test case " + inFile + ": " + e.getMessage());
			}
		}
	}
	

    public static void main(String[] args) throws Exception {
        long start = System.nanoTime();

        runTests("A", 0, 10);
        runTests("B", 11, 20);
        runTests("C", 21, 29);
        runTests("D", 30, 35);

        long stop = System.nanoTime();
        long totalTime = stop - start;
        double seconds = (double) totalTime / 1_000_000_000.0;
        System.out.println(seconds);
    }
}
