package gui.model;

import java.util.LinkedList;

import org.json.JSONObject;

import gui.persistence.Writable;

public class Board implements Writable {
    private final int size;
    private byte[][] board;
    private LinkedList<Block> listofBlocks;

    // EFFECTS: create a square 2D matrix of chars,
    // 'y' represents empty cell, 'int' represents Blocks held in a list
    // 'x' represents the block that needs to go to the right edge
    // log the size
    public Board(int s) {
        EventLog.getInstance().logEvent(new Event("Game set to size " + s));

        this.size = s;
        this.board = new byte[s][s];
        this.listofBlocks = new LinkedList<>();

        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                this.board[i][j] = (byte) 'y';
            }
        }
        this.insertBlock(new Goalblock(this.size / 2, 0, 1));
    }

    // EFFECTS: return the byte that is currently in position (x, y)
    public byte getCellAt(int x, int y) {
        return this.board[x][y];
    }

    // EFFECTS: set the byte c in position (x, y)
    public void setCellAt(int x, int y, byte c) {
        this.board[x][y] = c;
    }

    // EFFECTS: return the size of the board
    public int size() {
        return this.size;
    }

    // MODIFIES: this
    // EFFECTS: add a Block to the list
    public void addBlock(Block b) {
        this.listofBlocks.add(b);
    }

    // EFFECTS: return a Block at index ind in the list
    public Block getBlockAt(int ind) {
        return this.listofBlocks.get(ind);
    }

    // EFFECTS: return the size of the list
    public int numberofBlocks() {
        return this.listofBlocks.size();
    }

    // EFFECTS: return false if the Block is out of the board or if the cells are
    // currently not empty,
    // true otherwise
    private boolean checkCell(Block b) {
        int row = b.getRowNumber();
        int col = b.getColumnNumber();

        if ((col >= this.size || row >= this.size) || (col < 0 || row < 0) || this.board[row][col] != 'y') {
            return false;
        }

        if (b.isVertical() && (row + 1 < this.size) && this.board[row + 1][col] == 'y') {
            return true;
        }
        return (!b.isVertical() && (col + 1 < this.size) && this.board[row][col + 1] == 'y');
    }

    // MODIFIES: this
    // EFFECTS: if cells are empty, change the cells to BlockNumber and add Block to
    // the list,
    // otherwise return false
    // log whether insertion worked and the location of the new block
    public boolean insertBlock(Block b) {
        boolean valid = this.checkCell(b);
        int row = b.getRowNumber();
        int col = b.getColumnNumber();
        int name = b.getBlockNumber();

        if (valid) {
            if (name != 120) {
                EventLog.getInstance().logEvent(new Event("Block#" + name + " successfully added to board"
                        + " at [row number:" + row + ", column number: " + col + "]"));
            }
            if (b.isVertical()) {
                this.board[row][col] = (byte) name;
                this.board[row + 1][col] = (byte) name;
            } else {
                this.board[row][col] = (byte) name;
                this.board[row][col + 1] = (byte) name;
            }
            this.addBlock(b);
        } else {
            EventLog.getInstance().logEvent(new Event("Block#" + name + " failed to add to board"
                    + " at [row number:" + row + ", column number: " + col + "]"));
        }

        return valid;
    }

    // MODIFIES: this
    // EFFECTS: if index exists in list, remove from list, then change cell to empty
    // and return true,
    // else return false
    // log whether removal worked, the index of the block, and the location of the
    // block
    public boolean removeBlock(int ind) {
        if (ind < 0 || ind >= this.listofBlocks.size()) {
            EventLog.getInstance().logEvent(new Event("Failed to remove block at index(" + ind + ")"));
            return false;
        }

        for (int i = ind + 1; i < this.listofBlocks.size(); i++) {
            this.listofBlocks.get(i).relevelBlockNumber();
        }

        Block rm = this.listofBlocks.remove(ind);
        int row = rm.getRowNumber();
        int col = rm.getColumnNumber();
        this.board[row][col] = 'y';
        if (rm.isVertical()) {
            this.setCellAt(row + 1, col, (byte) 'y');
        } else {
            this.setCellAt(row, col + 1, (byte) 'y');
        }

        this.renumber();

        EventLog.getInstance().logEvent(new Event("Successfully removed block at index(" + ind + ") "
                + "at [row number:" + row + ", column number: " + col + "]"));
        return true;
    }

    // EFFECTS: go through all the blocks and change the blockNumbers appropriately
    private void renumber() {
        int row;
        int col;
        byte name;
        for (Block b : this.listofBlocks) {
            if (b.getBlockNumber() < 120) {
                row = b.getRowNumber();
                col = b.getColumnNumber();
                name = (byte) b.getBlockNumber();
                this.board[row][col] = name;
                if (b.isVertical()) {
                    this.setCellAt(row + 1, col, name);
                } else {
                    this.setCellAt(row, col + 1, name);
                }
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: call the move method according to the direction and return true,
    // if failed to move, return false
    public boolean move(String dir, int ind) {
        dir = dir.toLowerCase();

        switch (dir) {
            case "d":
                return this.moveDown(ind);
            case "u":
                return this.moveUp(ind);
            case "l":
                return this.moveLeft(ind);
            case "r":
                return this.moveRight(ind);
        }
        return false;
    }

    // MODIFIES: this
    // EFFECTS: check if the cell below is empty,
    // then move and return true, otherwise do nothing and return false
    // log whether the block indexed ind is moved down and the location of the block
    private boolean moveDown(int ind) {
        Block toMove = this.getBlockAt(ind);
        int col = toMove.getColumnNumber();
        int row = toMove.getRowNumber();

        if ((row + 2 >= this.size) || this.board[row + 2][col] != (byte) 'y' || !toMove.isVertical()) {
            EventLog.getInstance().logEvent(new Event("Failed to move block"
                    + " at [row number:" + row + ", column number: " + col + "]" + " down."));
            return false;
        }

        this.board[row][col] = (byte) 'y';
        this.board[row + 2][col] = (byte) toMove.getBlockNumber();
        toMove.setRowNumber(row + 1);

        EventLog.getInstance().logEvent(new Event("Successfully moved block"
                + " at [row number:" + row + ", column number: " + col + "]" + " down."));
        return true;
    }

    // MODIFIES: this
    // EFFECTS: check if the cell above is empty,
    // then move and return true, otherwise do nothing and return false
    // log whether the block indexed ind is moved up and the location of the block
    private boolean moveUp(int ind) {
        Block toMove = this.getBlockAt(ind);
        int col = toMove.getColumnNumber();
        int row = toMove.getRowNumber();

        if ((row - 1 < 0) || this.board[row - 1][col] != (byte) 'y' || !toMove.isVertical()) {
            EventLog.getInstance().logEvent(new Event("Failed to move block"
                    + " at [row number:" + row + ", column number: " + col + "]" + " up."));
            return false;
        }

        this.board[row + 1][col] = (byte) 'y';
        this.board[row - 1][col] = (byte) toMove.getBlockNumber();
        toMove.setRowNumber(row - 1);

        EventLog.getInstance().logEvent(new Event("Successfully moved block"
                + " at [row number:" + row + ", column number: " + col + "]" + " up."));
        return true;
    }

    // MODIFIES: this
    // EFFECTS: check if the cell to left is empty,
    // then move and return true, otherwise do nothing and return false
    // log whether the block indexed ind is moved left and the location of the block
    private boolean moveLeft(int ind) {
        Block toMove = this.getBlockAt(ind);
        int col = toMove.getColumnNumber();
        int row = toMove.getRowNumber();

        if ((col - 1 < 0) || this.board[row][col - 1] != (byte) 'y' || toMove.isVertical()) {
            EventLog.getInstance().logEvent(new Event("Failed to move block"
                    + " at [row number:" + row + ", column number: " + col + "]" + " left."));
            return false;
        }

        this.board[row][col + 1] = (byte) 'y';
        this.board[row][col - 1] = (byte) toMove.getBlockNumber();
        toMove.setColumnNumber(col - 1);

        EventLog.getInstance().logEvent(new Event("Successfully moved block"
                + " at [row number:" + row + ", column number: " + col + "]" + " left."));
        return true;
    }

    // MODIFIES: this
    // EFFECTS: check if the cell to right is empty,
    // then move and return true, otherwise do nothing and return false
    // log whether the block indexed ind is moved right and the location of the
    // block
    private boolean moveRight(int ind) {
        Block toMove = this.getBlockAt(ind);
        int col = toMove.getColumnNumber();
        int row = toMove.getRowNumber();

        if ((col + 2 >= this.size) || this.board[row][col + 2] != (byte) 'y' || toMove.isVertical()) {
            EventLog.getInstance().logEvent(new Event("Failed to move block"
                    + " at [row number:" + row + ", column number: " + col + "]" + " right."));
            return false;
        }

        this.board[row][col] = (byte) 'y';
        this.board[row][col + 2] = (byte) toMove.getBlockNumber();
        toMove.setColumnNumber(col + 1);

        EventLog.getInstance().logEvent(new Event("Successfully moved block"
                + " at [row number:" + row + ", column number: " + col + "]" + " right."));
        return true;
    }

    // EFFECTS: check if the goal block is at the right edge at any row
    // log when game is won
    public boolean isWon() {
        for (int i = 0; i < this.size; i++) {
            if (this.board[i][this.size - 1] == (byte) 'x') {
                EventLog.getInstance().logEvent(new Event("Game won!"));
                return true;
            }
        }
        return false;
    }

    // MODIFIES: json
    // EFFECTS: write size, numberofBlocks, columnNumber, rowNumber, blockNumber,
    // and verticality into json
    // then return it
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        int i = 0;
        for (Block b : this.listofBlocks) {
            json.put("col" + i, b.getColumnNumber());
            json.put("row" + i, b.getRowNumber());
            json.put("bNo" + i, b.getBlockNumber());
            json.put("isV" + i++, b.isVertical());
        }
        json.put("size", this.size);
        json.put("noBlocks", this.listofBlocks.size());
        return json;
    }
}