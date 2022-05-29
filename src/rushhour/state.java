package rushhour;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class state {
	private char[][] cur;
	private String step;
	//constructors
	state(char[][] c){
		cur=new char[6][6];
		for(int i = 0; i < 6; i++)
			for(int j = 0; j < 6; j++) {
				cur[i][j]=c[i][j];
			}
		hash=computeHash();
	}
	state(String fileName) throws Exception {
		File file = new File(fileName);
		if(!file.exists()) 
			throw new FileNotFoundException("File doesnt exist");
		
		Scanner read = new Scanner(file);
		
		cur=new char[6][6];
		for(int i = 0 ; i < 6; i++) {
			String data = read.nextLine();
			if(data.length()!=6) {
				read.close();
				throw new Exception("Badboard");
			}
				
			for(int j = 0; j < 6; j++) {
				cur[i][j]=data.charAt(j);
			}
			
		}
		read.close();
	}
	//store step to the state
	public void setStep(String s) {
		step=s;
	}
	public String getStep() {
		return step;
	}
	//return board
	public char[][] get() {
		return cur;
	}
	//print board
	public void display(){
		for(int i = 0; i < 6; i++) {
			for(int j = 0; j < 6; j++)
				System.out.print(cur[i][j]);
			System.out.println();
		}
		System.out.println();
	}
	//check if solved
	public boolean isSolved() {
		for(int i = 0; i < 6; i++) {
			if(cur[i][5]=='X') return true;
		}
		return false;
	}
	//hash-equals
	private int hash;
	private int computeHash() {
		int hash = 0;
	    for (int i = 0; i < 6; i++)
	        for (int j = 0; j < 6; j++)
	            hash = (hash) ^ ((int)(Math.pow(31,i)+Math.pow(31,j)) * cur[i][j]);
	    return hash;
	}
	@Override
	public int hashCode() {
		return hash;
	}
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) 
			return true;
		
		if (!(obj instanceof state)) 
			return false;
		
		if(this.hashCode() != ((state)obj).hashCode()) 
			return false;
		return true;
		
	}
	
}
