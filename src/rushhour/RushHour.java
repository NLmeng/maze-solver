package rushhour;
import java.io.*;
import java.util.*;

public class RushHour
{
	public final static int UP = 0;
	public final static int DOWN = 1;
	public final static int LEFT = 2;
	public final static int RIGHT = 3;

	public final static int SIZE = 6;

	char[][] board = new char[SIZE][SIZE];
	VehicleList vehicles = new VehicleList();
	
	/**
	 * Printing the current state of the board (6x6)
	 */
	public void printBoard() {
		for(int i = 0; i < SIZE; i++) {
			for(int j = 0; j < SIZE; j++)
				System.out.print(this.board[i][j]);
			System.out.println();
		}
		System.out.println();
	}
	public char[][] getBoard(){
		return this.board;
	}
	public void readInto(char[][] c) throws Exception {
		this.vehicles.clear();
		for(int i = 0 ; i < SIZE; i++) {
			for(int j = 0; j < SIZE; j++) {
				this.board[i][j] = c[i][j];
				if(this.board[i][j] != '.') 
					this.vehicles.add(this.board[i][j], i, j);
			}
		}
		this.vehicles.format();
		
	}
	//make a series of moves in accordance to a solution file
	public void testSol(String input) throws FileNotFoundException, IllegalMoveException {
		File read = new File(input);
		Scanner in = new Scanner(read);
		String tokens;
		int dir = -1; int l = -1;

		while(in.hasNext()) {
			tokens=in.nextLine();

			switch(tokens.charAt(1)) {
				case('L'):
					dir=LEFT;
					break;
				case('R'):
					dir=RIGHT;
					break;
				case('U'):
					dir=UP;
					break;
				case('D'):
					dir=DOWN;
					break;
			}
			l=(int)tokens.charAt(2)-48;

			//printBoard();
			this.makeMove(tokens.charAt(0), dir, l);
		}
		in.close();
		if(this.isSolved()) {
			System.out.println("solved");
		} else {
			System.out.println("!solved");
		}
	}
	//extra constructors
	public RushHour() {
		return;
	}
	public RushHour(char[][] c) throws Exception {
		this.readInto(c);
	}
	public State nextState(Vehicle vh, int dr, int ln) {

		this.move(vh, dr, ln);
		State ret = new State(this.board);
		
		switch(dr) {
			case UP:
				this.move(vh, DOWN, ln);
				break;
				
			case DOWN:
				this.move(vh, UP, ln);
				break;
				
			case LEFT:
				this.move(vh, RIGHT, ln);
				break;
				
			case RIGHT:
				this.move(vh, LEFT, ln);
				break;
		}

		return ret;
	}
	
	/**
	 * @param fileName
	 * Reads a board from file and creates the board
	 * @throws Exception if the file not found or the board is bad
	 */
	public RushHour(String fileName) throws Exception {
		File file = new File(fileName);
		if(!file.exists()) 
			throw new FileNotFoundException("File doesnt exist");
		
		Scanner read = new Scanner(file);
		
		for(int i = 0 ; i < SIZE; i++) {
			String data = read.nextLine();
			if(data.length()!=SIZE) {
				read.close();
				throw new Exception("Badboard");
			}
				
			for(int j = 0; j < SIZE; j++) {
				
				this.board[i][j] = data.charAt(j);
				if(this.board[i][j] != '.') 
					this.vehicles.add(this.board[i][j], i, j);
			}
		}
		read.close();
		this.vehicles.format();
	}
	/**
	 * 
	 * @param carName
	 * @param dir
	 * @param length
	 * @return true, if the move can be made, else false
	 * @throws IllegalMoveException
	 */
	public boolean tryMove(char carName, int dir, int length) throws IllegalMoveException {
		
		try {
			int indexofCar = this.vehicles.contains(carName);
			Vehicle cur = this.vehicles.list.get(indexofCar);
			this.validate(cur, dir, length);
			return true;
		} catch(Exception e) {
			return false;
		}
	
	}
	
	/**
	 * @param carName
	 * @param dir
	 * @param length
	 * Moves car with the given name for length steps in the given direction  
	 * @throws IllegalMoveException if the move is illegal
	 */
	public void makeMove(char carName, int dir, int length) throws IllegalMoveException {
		
		try {
			int indexofCar = this.vehicles.contains(carName);
			Vehicle cur = this.vehicles.list.get(indexofCar);
			this.validate(cur, dir, length);
			this.move(cur, dir, length);
		} catch(Exception e) {
			throw e;
		}
	
	}
	
	private void validate (Vehicle vh, int dr, int ln) throws IllegalMoveException {
		if(vh.direction == 'h') {
			if(dr==UP || dr==DOWN) 
				throw new IllegalMoveException("Invalid Direction");
		}
		if(vh.direction == 'v') {
			if(dr==LEFT || dr==RIGHT) 
				throw new IllegalMoveException("Invalid Direction");
		}
		
		switch(dr) {
			case UP:
				int uY = vh.coord.get(0).get(0);
				int uX = vh.coord.get(0).get(1);
				if(uY-ln<0) 
					throw new IllegalMoveException("Outside Board");
				uY--;
				while(ln--!=0) {
					if(this.board[uY--][uX]!='.')
						throw new IllegalMoveException("Space Taken");
				}
				break;
				
			case DOWN:
				int dY = vh.coord.get(vh.rightMost()).get(0);
				int dX = vh.coord.get(vh.rightMost()).get(1);
				if(dY+ln>=6) 
					throw new IllegalMoveException("Outside Board");
				dY++;
				while(ln--!=0) {
					if(this.board[dY++][dX]!='.')
						throw new IllegalMoveException("Space Taken");
				}
				break;
				
			case LEFT:
				int lY = vh.coord.get(0).get(0);
				int lX = vh.coord.get(0).get(1);
				if(lX-ln<0) 
					throw new IllegalMoveException("Outside Board");
				lX--;
				while(ln--!=0) {
					if(this.board[lY][lX--]!='.')
						throw new IllegalMoveException("Space Taken");
				}
				break;
				
			case RIGHT:
				int rY = vh.coord.get(vh.rightMost()).get(0);
				int rX = vh.coord.get(vh.rightMost()).get(1);
				if(rX+ln>=6) 
					throw new IllegalMoveException("Outside Board");
				rX++;
				while(ln--!=0) {
					if(this.board[rY][rX++]!='.')
						throw new IllegalMoveException("Space Taken");
				}
				break;
		}
	}
	private void move(Vehicle vh, int dr, int ln) {
		
		if(dr==LEFT || dr==UP) {	
			
			for(int k = 0; k < vh.coord.size(); k++) {
				int i = vh.coord.get(k).get(0);
				int j = vh.coord.get(k).get(1);
				
				switch(dr) {
					case UP:
						this.board[i][j]='.';
						this.board[i-ln][j]=vh.name;
						vh.coord.get(k).set(0, i-ln);
						break;
						
					case LEFT:
						this.board[i][j]='.';
						this.board[i][j-ln]=vh.name;
						vh.coord.get(k).set(1, j-ln);
						break;
				}
			}
			
		} else {
			
			for(int k = vh.rightMost(); k >= 0; k--) {
				int i = vh.coord.get(k).get(0);
				int j = vh.coord.get(k).get(1);
				
				switch(dr) {
					case DOWN:
						this.board[i][j]='.';
						this.board[i+ln][j]=vh.name;
						vh.coord.get(k).set(0, i+ln);
						break;
						
					case RIGHT:
						this.board[i][j]='.';
						this.board[i][j+ln]=vh.name;
						vh.coord.get(k).set(1, j+ln);
						break;
				}
			}
			
		}
	
	}
	
	/**
	 * @return true if and only if the board is solved,
	 * i.e., the XX car is touching the right edge of the board
	 */
	public boolean isSolved() {
		Vehicle goal = this.vehicles.list.get(this.vehicles.contains('X'));
		if(goal.coord.get(goal.rightMost()).get(1) == 5) 
			return true;
		return false;
	}

}