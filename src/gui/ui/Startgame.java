package gui.ui;

import gui.model.*;
import gui.persistence.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Startgame {
    private static final String JSON_STORE = "./data/savedBoard.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private Board board;
    private Scanner input;
    private String command;
    private int currentNumber = 1;

    // EFFECTS: input 1 or 2, 1 for GUI, 2 for UI
    private int chooseInterface() {
        int nxt;

        while (true) {
            System.out.println("Select from the following (Enter 1 or 2):");
            System.out.println("\t1. GUI");
            System.out.println("\t2. UI");
            try {
                this.input = new Scanner(System.in);
                nxt = this.input.nextInt();
                if (nxt != 1 && nxt != 2) {
                    System.out.println("Option is not available!");
                } else if (nxt == 1) {
                    new StartMenu();
                }
                return nxt;
            } catch (InputMismatchException e) {
                System.out.println("Option is not available!");
            }
        }
    }


    // EFFECTS: choose an interface
    //          start a GUI or
    //          initialize and prompt till appropriate fields and start the game
    //          handles InputMismatchException, and negative int
    public Startgame() {
        int nxt = 0;

        if (this.chooseInterface() == 1) {
            return;
        }

        while (true) {
            System.out.println("Please insert the size of the board: (suggested 6 or bigger)");
            try {
                this.input = new Scanner(System.in);
                nxt = this.input.nextInt();

            } catch (InputMismatchException e) {
                System.out.println("Input not an integer!");
            }
            if (nxt <= 0) {
                System.out.println("Negative/Zero is not available!");
                continue;
            }
            break;
        }
        this.board = new Board(nxt);
        this.jsonWriter = new JsonWriter(JSON_STORE);
        this.jsonReader = new JsonReader(JSON_STORE);
        System.out.println("Move Block#1: xx to the right edge to win!");
        this.run();
    }
    
    // MODIFIES: this
    // EFFECTS: check if won (XX is at right edge) after each command,
    //          if not then processes user inputs to create a board and blocks
    private void run() {
        boolean running = true;
        while (running) {
            if (this.board.isWon()) {
                System.out.println("You Won!");
                running = false;
            } else {
                this.displayMenu();
                this.command = this.input.next().toLowerCase();

                if (this.command.equals("q")) {
                    running = false;
                } else {
                    this.processCommand(this.command);
                }
            }
        }

        for (Event next : EventLog.getInstance()) {
            System.out.println(next.getDescription());
        }
    }

    // EFFECTS: displays menu of options to user
    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\td -> display the current state of game");
        System.out.println("\ta -> add Blocks");
        System.out.println("\tr -> remove Blocks");
        System.out.println("\tm -> move Blocks");
        System.out.println("\ts -> save game");
        System.out.println("\tl -> load game");
        System.out.println("\tq -> quit");
    }

    // MODIFIES: this
    // EFFECTS: processes user commands
    private void processCommand(String command) {
        switch (command) {
            case "d":
                this.displayGame();
                break;
            case "a":
                this.menuforAdd();
                break;
            case "r":
                this.menuforRemove();
                break;
            case "m":
                this.menuforMove();
                break;
            case "s":
                this.saveGame();
                break;
            case "l":
                this.loadGame();
                break;
            default:
                System.out.println("The input entered is invalid! Please try again!");
        }
    }

    // MODIFIES: this
    // EFFECTS: prompt for direction, column and row numbers, to create block
    //          if cell is empty and block is within board, insert block and add to list
    //          else disregard the block, do nothing
    //          handles InputMismatchException, if x,y inputs aren't as expected
    private void menuforAdd() { //throws InvalidInputException {
        try {
            System.out.println("Give direction: 'v' for Vertical and 'h' for Horizontal");
            this.command = this.input.next().toLowerCase();
            if (!(this.command.equals("v") || this.command.equals("h"))) {
                System.out.println("The input entered is invalid! Please try again!");
                return;
            }

            int x;
            int y;
            System.out.println("Give position (within board)");
            System.out.println("\trow number:");
            x = this.input.nextInt();
            System.out.println("\tcolumn number:");
            y = this.input.nextInt();
            this.addAndInsert(x, y);
        } catch (InputMismatchException e) {
            System.out.println("The input entered is invalid! Please try again!");
        }
    }

    // MODIFIES: this
    // EFFECTS: creates block from given x, y, and dir
    //          if cell is empty and block is within board, insert block and add to list
    //          else disregard the block, do nothing
    private void addAndInsert(int x, int y) {
        Block nxtblock;

        if (this.command.equals("v")) {
            nxtblock = new Vertblock(x, y, this.currentNumber);
        } else {
            nxtblock = new Horiblock(x, y, this.currentNumber);
        }

        if (this.board.insertBlock(nxtblock)) {
            this.currentNumber++;
        } else {
            System.out.println("Invalid Block: out of board or cell is not empty there");
        }
    }

    // MODIFIES: this
    // EFFECTS: show the list, prompt for an index,
    //          if index exists then remove the block at that index in the list, else nothing is removed
    //          handles InputMismatchException, if ind input isn't as expected
    private void menuforRemove() {
        this.printList();
        try {
            System.out.println("Enter #number to be removed from the list: ");
            int ind = this.input.nextInt();
            boolean tryRemove = (ind == 0) ? false : this.board.removeBlock(ind);
            if (tryRemove) {
                System.out.println("Removed Block#" + ind);
                this.currentNumber--;
            } else {
                System.out.println("Remove unsuccessful");
            }
        } catch (InputMismatchException e) {
            System.out.println("The input entered is invalid! Please try again!");
        }
    }

    // MODIFIES: this
    // EFFECTS: show the list, prompt for an index,
    //          if index exists, check if move is appropriate then move the block
    //          else disregard the move, do nothing
    //          handles InputMismatchException, if ind input isn't as expected
    private void menuforMove() {
        this.printList();
        System.out.println("Enter #number to be moved: ");

        try {
            int ind = this.input.nextInt();
            Block blocktoMove = this.board.getBlockAt(ind);

            System.out.println("Select movement: ");
            if (blocktoMove.isVertical()) {
                System.out.println("\td -> move down by 1");
                System.out.println("\tu -> move up by 1");
            } else {
                System.out.println("\tl -> move left by 1");
                System.out.println("\tr -> move right by 1");
            }

            this.command = this.input.next();
            if (this.board.move(this.command, ind)) {
                System.out.println("Move successful");
                this.displayGame();
            } else {
                System.out.println("Unavailable Move");
            }
        } catch (InputMismatchException e) {
            System.out.println("The input entered is invalid! Please try again!");
        }
    }


    // EFFECTS: save the state of the game (blocks) to file
    //          handles FileNotFoundException
    private void saveGame() {
        try {
            this.jsonWriter.open();
            this.jsonWriter.write(this.board);
            this.jsonWriter.close();
            System.out.println("Saved game!");
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
            System.out.println("Save file could not be found! Game not saved!");
        }
    }

    // MODIFIES: this
    // EFFECTS: loads the saved state from file
    //          handles IOException
    private void loadGame() {
        try {
            this.board = this.jsonReader.read();
            this.currentNumber = this.board.numberofBlocks();
            System.out.println("Loaded from " + JSON_STORE);
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // EFFECTS: print out the state of the Board with indices followed by the list of all existing Blocks in order
    private void displayGame() {
        int rowNo = -1;
        int colNo = -1;
        for (int i = -1; i < this.board.size(); i++) {
            for (int j = -1; j < this.board.size(); j++) {
                if (i < 0 || j < 0) {
                    System.out.print(rowNo++ + " ");
                } else {
                    int curr = this.board.getCellAt(i, j);
                    if (curr >= 121) {
                        System.out.print('#' + " ");
                    } else if (curr == 120) {
                        System.out.print((char) curr + " ");
                    } else {
                        System.out.print(curr + " ");
                    }
                }
            }
            colNo++;
            rowNo = colNo;
            System.out.println();
        }
        this.printList();
    }

    // EFFECTS: print out the list of all existing Blocks in order
    private void printList() {
        System.out.println("List of Blocks currently in game: ");
        int i = 0;
        while (i < this.board.numberofBlocks()) {
            Block b = this.board.getBlockAt(i);

            if (b.isVertical()) {
                System.out.println("Vertical Block #" + i + ": tail at ("
                        + b.getRowNumber() + ", " + b.getColumnNumber() + ")");
            } else {
                System.out.println("Horizontal Block #" + i + ": tail at ("
                        + b.getRowNumber() + ", " + b.getColumnNumber() + ")");
            }

            i++;
        }
    }

    // EFFECTS: mimic pause by asking for an input and does nothing
    private void pause() {
        System.out.println("Press any key to continue");
        this.input.next();
    }
}
