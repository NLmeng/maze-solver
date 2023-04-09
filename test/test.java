import java.io.File;
import java.util.Scanner;
import rushhour.*;

public class test
{
	public static int dirChar2Int(char d) {
		switch (d) {
		case 'U': {
			return RushHour.UP;
		}
		case 'D': {
			return RushHour.DOWN;
		}
		case 'L': {
			return RushHour.LEFT;
		}
		case 'R': {
			return RushHour.RIGHT;
		}
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
				if (line.length() != 3)
					throw new IllegalMoveException(line);
				puzzle.makeMove(line.charAt(0), dirChar2Int(line.charAt(1)), line.charAt(2) - '0');
			}
			
			if (puzzle.isSolved()) {
				System.out.println("Solved");
			}

		} catch (Exception e) {
			System.out.println(e);
		}

	}
	public static void main(String[] args) throws Exception {
		for(int t = 0; t < 1; t++) {
		long start=System.nanoTime();

		// System.out.println("Working Directory = " + System.getProperty("user.dir"));
		// Solver.solveFromFile(puzzleName, "sol-"+puzzleName);
		
		for(int i = 0; i <= 10; i++) {
			String inA = "test/test_files/A";
			String outA = "test/test_files/A";
			if(i<10) {
				inA += "0" + i +".txt";
				outA += "0" + i +".sol";
			} else {
				inA += i +".txt";
				outA += i +".sol";
			}
			rushhour.Solver.solveFromFile(inA, outA);
		}
		
		
		for(int i = 11; i <= 20; i++) {
			String inB = "test/test_files/B";
			String outB = "test/test_files/B";
		
			inB += i +".txt";
			outB += i +".sol";
			
			Solver.solveFromFile(inB, outB);
		}
		
		
		for(int i = 21; i <= 29; i++) {
			String inC = "test/test_files/C";
			String outC = "test/test_files/C";
		
			inC += i +".txt";
			outC += i +".sol";
			
			Solver.solveFromFile(inC, outC);
		}
		
		
		for(int i = 30; i <= 35; i++) {
			String inD = "test/test_files/D";
			String outD = "test/test_files/D";
		
			inD += i +".txt";
			outD += i +".sol";
			
			Solver.solveFromFile(inD, outD);
		}
		
// 		Solver.solveFromFile("hdest.txt", "hdest.sol");
//		Solver.solveFromFile("D33.txt", "D33.sol");
		
//		String h = "";
//		String sol = "";
//		for(int i = 1; i <= 5; i++) {
//			h="h" + i + ".txt";
//			sol="h" + i + ".sol";
//			Solver.solveFromFile(h, sol);
//		}
		
		RushHour g;
		for(int i = 0; i <= 10; i++) {
			String inA = "test/test_files/A";
			String outA = "test/test_files/A";
			if(i<10) {
				inA += "0" + i +".txt";
				outA += "0" + i +".sol";
			} else {
				inA += i +".txt";
				outA += i +".sol";
			}
			g=new RushHour(inA);
			g.testSol(outA);
		}
		
		
		for(int i = 11; i <= 20; i++) {
			String inB = "test/test_files/B";
			String outB = "test/test_files/B";
		
			inB += i +".txt";
			outB += i +".sol";
			
			g=new RushHour(inB);
			g.testSol(outB);
		}
		
		
		for(int i = 21; i <= 29; i++) {
			String inC = "test/test_files/C";
			String outC = "test/test_files/C";
		
			inC += i +".txt";
			outC += i +".sol";
			
			g=new RushHour(inC);
			g.testSol(outC);
		}
		
		
		for(int i = 30; i <= 35; i++) {
			String inD = "test/test_files/D";
			String outD = "test/test_files/D";
		
			inD += i +".txt";
			outD += i +".sol";
			
			g=new RushHour(inD);
			g.testSol(outD);
		}
//		g=new RushHour("hdest.txt");
//		g.testSol("hdest.sol");

		// testSolution("D35.txt", "D35.sol");

		long stop=System.nanoTime();
		long tt = stop-start;
		double seconds = (double)tt / 1_000_000_000.0;
		System.out.println(seconds);
		}
	}
}
