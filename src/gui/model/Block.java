package gui.model;

public abstract class Block {
    private static final int LENGTH = 2;
    protected static final boolean ISVERTICAL = true;
    private int rowNumber;
    private int colNumber;
    private int blockNo;

    // EFFECTS: create a new Block at (x, y) and denoted by n
    public Block(int x, int y, int n) {
        this.rowNumber = x;
        this.colNumber = y;
        this.blockNo = n;
    }

    // REQUIRES: number is at least 0 and is smaller than the board size
    // EFFECTS: return the column number
    public int getColumnNumber() {
        return this.colNumber;
    }

    // REQUIRES: n is at least 0 and is smaller than the board size
    // MODIFIES: this
    // EFFECTS: set the current column number to n
    public void setColumnNumber(int n) {
        this.colNumber = n;
    }

    // REQUIRES: number is at least 0 and is smaller than the board size
    // EFFECTS: return the row number
    public int getRowNumber() {
        return this.rowNumber;
    }

    // REQUIRES: n is at least 0 and is smaller than the board size
    // MODIFIES: this
    // EFFECTS: set the current row number to n
    public void setRowNumber(int n) {
        this.rowNumber = n;
    }

    // EFFECTS: return the number representing the Block (starting from 1)
    public int getBlockNumber() {
        return this.blockNo;
    }

//    // REQUIRES: n is at least 0
//    // MODIFIES: this
//    // EFFECTS: set block number to n
//    public void setBlockNumber(int n) {
//        blockNo = n;
//    }

//    // EFFECTS: return true, if block is at the right edge
//    public boolean isWon(int size) {
//        return this.getColumnNumber() + 1 >= size - 1;
//    }

    // MODIFIES: this
    // EFFECTS: lower the Block number by 1
    public void relevelBlockNumber() {
        this.blockNo -= 1;
    }

    // EFFECTS: return true if vertical and false if horizontal
    public abstract boolean isVertical();
}
