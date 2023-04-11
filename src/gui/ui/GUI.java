package gui.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.json.JSONObject;

import gui.model.Block;
import gui.model.Board;
import gui.model.Event;
import gui.model.EventLog;
import gui.model.Horiblock;
import gui.model.Vertblock;
import gui.persistence.JsonReader;
import gui.persistence.JsonWriter;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import gui.persistence.*;
import rushhour.*;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class GUI extends JFrame implements ActionListener {
    private int size;
    private int currRow;
    private int currColumn;
    private boolean toMove;
    private boolean toCreate;
    private boolean toRemove;
    private boolean isVert;
    private int startInd = 1; // empty == 'y' == " ", goalBlock == 'x' == "x"
    private Block blockToMove;
    private int blockInd;
    private JButton[][] btn;
    private JPanel boardPanel;
    private JButton btnforNewVertical;
    private JButton btnforNewHorizontal;
    private JButton btnforRemove;
    private JButton btnforSave;
    private JButton btnforLoad;
    private JButton btnforSolve;
    private Board brd;
    // private static final String JSON_STORE = "./data/savedBoard.json";
    private String jsonName = "./data/savedBoard.json";
    private String jsonNameForSolve = "./data/solver.json";
    private JsonWriter jsonWriter;
    private JsonWriter jsonWriterForSolve;
    private JsonReader jsonReader;
    private HashMap<Integer, Color> blockColors = new HashMap<>();


    // MODIFIES: this
    // EFFECTS: create Board with the given size, set up JFrame with panels and
    // buttons
    public GUI(int size) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        this.size = size;
        this.initDefault();
        this.loadBoard();
        this.colorBoard();
        this.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: create Board with size 2 then load the save, set up JFrame with
    // panels and buttons
    public GUI(String s) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        this.size = 2;
        this.jsonName = s;
        this.initDefault();
        this.loadBoard();
        this.loadGame();
        this.colorBoard();
        this.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: set the title, color, size of JFrame, then initialize Board,
    // buttons, jsonWriter, and jsonReader
    // then add titlePanel, boardPanel, and buttonPanel
    private void initDefault() {
        this.setTitle("BLOCKSLIDER");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.getContentPane().setBackground(Color.CYAN);
        this.setSize(this.size * 100, this.size * 100);
        this.getContentPane().setBackground(Color.decode("#5c3c92"));

        this.toCreate = this.toRemove = this.toMove = this.isVert = false;
        this.brd = new Board(this.size);
        this.btn = new JButton[120][120];
        this.jsonWriter = new JsonWriter(this.jsonName);
        this.jsonReader = new JsonReader(this.jsonName);
        this.jsonWriterForSolve = new JsonWriter(this.jsonNameForSolve);

        this.createTitle();
        this.createBoard();
        this.createDefaultButtons();

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                for (Event next : EventLog.getInstance()) {
                    System.out.println(next.getDescription());
                }
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: create and add boardPanel as a GridLayout of size
    private void createBoard() {
        this.boardPanel = new JPanel(new GridLayout(this.size, this.size, 0, 0));
        this.boardPanel.setSize(this.size * 50, this.size * 50);
        this.add(this.boardPanel, BorderLayout.CENTER);
    }

    // MODIFIES: this
    // EFFECTS: create and add titlePanel, set the title, font, colors, and
    // alignment
    private void createTitle() {
        JPanel titlePanel = new JPanel();
        JLabel title = new JLabel("BlockSlider");
        title.setFont(new Font("Algerian", Font.BOLD, 50));
        title.setBackground(Color.decode("#5c3c92"));
        title.setForeground(Color.decode("#d72631"));
        title.setHorizontalAlignment(JLabel.CENTER);
        titlePanel.add(title);
        this.add(titlePanel, BorderLayout.PAGE_START);
    }

    // MODIFIES: this
    // EFFECTS: create and add buttonPanel, set the names and add Listeners
    private void createDefaultButtons() {
        JPanel buttonPanel = new JPanel(new FlowLayout());

        this.btnforNewVertical = new JButton("Vert");
        this.btnforNewVertical.addActionListener(this);
        this.btnforNewHorizontal = new JButton("Hori");
        this.btnforNewHorizontal.addActionListener(this);
        this.btnforRemove = new JButton("Remove");
        this.btnforRemove.addActionListener(this);
        this.btnforSave = new JButton("Save");
        this.btnforSave.addActionListener(this);
        this.btnforLoad = new JButton("Load");
        this.btnforLoad.addActionListener(this);
        this.btnforSolve = new JButton("Solve");
        this.btnforSolve.addActionListener(this);

        buttonPanel.add(this.btnforNewVertical);
        buttonPanel.add(this.btnforNewHorizontal);
        buttonPanel.add(this.btnforSave);
        buttonPanel.add(this.btnforLoad);
        buttonPanel.add(this.btnforRemove);
        buttonPanel.add(this.btnforSolve);
        this.add(buttonPanel, BorderLayout.PAGE_END);
    }

    // EFFECTS: create and add buttons according to the board into the buttonPanel
    private void loadBoard() {
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                int ind = this.brd.getCellAt(i, j);
                JButton b;
                if (ind == 121) {
                    // b = new JButton(" ");
                    b = new JButton("");
                } else if (ind == 120) {
                    b = new JButton("x");
                } else {
                    // b = new JButton(String.valueOf(ind));
                    b = new JButton("");
                }
                
                b.setForeground(Color.decode("#4c0000"));
                b.setFocusable(false);
                b.addActionListener(this);
                this.btn[i][j] = b;
                this.boardPanel.add(b);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: change the color of the pair of buttons
    private void colorBoard() {
        Block b;
        int row1;
        int col1;
        // System.out.println(this.brd.numberofBlocks());
        for (int i = 0; i < this.brd.numberofBlocks(); i++) {
            b = this.brd.getBlockAt(i);
            row1 = b.getRowNumber();
            col1 = b.getColumnNumber();
            if (b.isVertical()) {
                this.setColor(row1, col1, row1 + 1, col1);
            } else {
                this.setColor(row1, col1, row1, col1 + 1);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: repaint the boardPanel in order to decrease the level of blocks
    private void reload() {
        this.remove(this.boardPanel);
        this.boardPanel.removeAll();

        this.size = this.brd.size();
        this.startInd = this.brd.numberofBlocks();
        this.setSize(this.size * 100, this.size * 100);
        this.boardPanel = new JPanel(new GridLayout(this.size, this.size, 0, 0));

        this.loadBoard();
        this.boardPanel.revalidate();
        this.boardPanel.repaint();

        this.add(this.boardPanel, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }

    // MODIFIES: this
    // EFFECTS: handles buttons for save, load, creat new, remove, and move
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.btnforSave) {
            this.saveGame();
            return;
        } else if (e.getSource() == this.btnforLoad) {
            this.loadGame();
            return;
        }

        if (e.getSource() == this.btnforNewHorizontal || e.getSource() == this.btnforNewVertical) {
            this.isVert = e.getSource() != this.btnforNewHorizontal;
            this.toCreate = true;
            this.toRemove = false;
            this.toMove = false;
            return;
        }

        if (e.getSource() == this.btnforRemove) {
            this.toCreate = false;
            this.toRemove = true;
            this.toMove = false;
            return;
        }

        if (e.getSource() == this.btnforSolve) {
            this.solve();
            return;
        }

        JButton clicked = this.findButton(e);
        byte curr = this.brd.getCellAt(this.currRow, this.currColumn);

        this.tryActions(clicked, curr);
    }

    //
    //
    private void solve() {
        try {
            this.jsonWriterForSolve.open();
            this.jsonWriterForSolve.write(this.brd);
            this.jsonWriterForSolve.close();
            System.out.println("saved board!");
        } catch (FileNotFoundException e) {
            // e.printStackTrace();
            System.out.println("failed to save board");
        }

        String filePath = "data/solver.json";
		String outFile = "data/solver.sol";

        try {
            String txtBoard = Converter.jsonToTxt(filePath);
            System.out.println(txtBoard);
    
            // Write the txtBoard to data/solver.txt
            try (FileWriter writer = new FileWriter("data/solver.txt")) {
                writer.write(txtBoard);
                System.out.println("Successfully written to data/solver.txt");
            } catch (IOException e) {
                System.err.println("Error writing to data/solver.txt: " + e.getMessage());
            }

			
			rushhour.Solver.solveFromFile("data/solver.txt", outFile);
			new RushHour("data/solver.txt");

            List<String> moves = readSolutionFile("data/solver.sol");
            performMoves(moves);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public List<String> readSolutionFile(String filePath) throws IOException {
        List<String> moves = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                moves.add(line);
            }
        }
        return moves;
    }
    public void performMoves(List<String> moves) {
        new Thread(() -> {
            String[] txtData;
            Map<String, Object> jsonData;
            try {
                txtData = readTxtFile("data/solver.txt");
                jsonData = readJsonFile("data/solver.json");
                for (String move : moves) {
                    char block = move.charAt(0);
                    char direction = move.charAt(1);
                    int steps = Character.getNumericValue(move.charAt(2));
                    this.blockInd = translateBlockLetterToBNo(block, txtData, jsonData);
                    if (this.blockInd == 120) {
                        this.blockInd = 0;
                    }
                    this.blockToMove = this.brd.getBlockAt(this.blockInd);
                    for (int i = 0; i < steps; i++) {
                        SwingUtilities.invokeLater(() -> {
                            switch (direction) {
                                case 'L':
                                    // Move block left
                                    this.move(this.btn[this.blockToMove.getRowNumber()][this.blockToMove.getColumnNumber()], "l", this.blockToMove.getRowNumber(), this.blockToMove.getColumnNumber());
                                    break;
                                case 'U':
                                    // Move block up
                                    this.move(this.btn[this.blockToMove.getRowNumber()][this.blockToMove.getColumnNumber()], "u", this.blockToMove.getRowNumber(), this.blockToMove.getColumnNumber());
                                    break;
                                case 'D':
                                    this.move(this.btn[this.blockToMove.getRowNumber()][this.blockToMove.getColumnNumber()], "d", this.blockToMove.getRowNumber(), this.blockToMove.getColumnNumber());
                                    // Move block down
                                    break;
                                case 'R':
                                    this.move(this.btn[this.blockToMove.getRowNumber()][this.blockToMove.getColumnNumber()], "r", this.blockToMove.getRowNumber(), this.blockToMove.getColumnNumber());
                                    // Move block right
                                    break;
                                default:
                                    throw new IllegalStateException("Unexpected direction: " + direction);
                            }
                        });
                        try {
                            Thread.sleep(500); // Add a 500ms delay between moves
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading the txt or json file: " + e.getMessage());
            }
        }).start();
    }
    
    public int translateBlockLetterToBNo(char letter, String[] txtData, Map<String, Object> jsonData) {
        int row = -1;
        int col = -1;
    
        // Find the position of the letter in txtData
        for (int i = 0; i < txtData.length; i++) {
            int index = txtData[i].indexOf(letter);
            if (index != -1) {
                row = i;
                col = index;
                break;
            }
        }
    
        if (row == -1 || col == -1) {
            return -1; // Letter not found
        }
    
        // Find the corresponding bNo in jsonData
        for (int keyValue = 0; keyValue < (int) jsonData.get("noBlocks"); keyValue++) {
            String rowKey = "row" + keyValue;
            String colKey = "col" + keyValue;
            if (jsonData.containsKey(rowKey) && jsonData.containsKey(colKey)) {
                int rowValue = (int) jsonData.get(rowKey);
                int colValue = (int) jsonData.get(colKey);
                if (rowValue == row && colValue == col) {
                    String bNoKey = "bNo" + keyValue;
                    if (jsonData.containsKey(bNoKey)) {
                        return (int) jsonData.get(bNoKey);
                    }
                }
            }
        }
    
        return -1; // bNo not found
    }
    public String[] readTxtFile(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            return br.lines().toArray(String[]::new);
        } catch (IOException e) {
            throw new IOException("Error reading the txt file: " + e.getMessage());
        }
    }
    public Map<String, Object> readJsonFile(String filePath) throws IOException {
        String jsonContent = new String(Files.readAllBytes(Paths.get(filePath)));
        JSONObject jsonObject = new JSONObject(jsonContent);
    
        Map<String, Object> jsonData = new HashMap<>();
        Iterator<String> keys = jsonObject.keys();
    
        while (keys.hasNext()) {
            String key = keys.next();
            Object value = jsonObject.get(key);
    
            if (value instanceof Integer) {
                jsonData.put(key, jsonObject.getInt(key));
            } else if (value instanceof Boolean) {
                jsonData.put(key, jsonObject.getBoolean(key));
            }
        }
    
        return jsonData;
    }
    

    // EFFECTS: save the state of the game (blocks) to file
    // handles FileNotFoundException
    private void saveGame() {
        try {
            this.jsonWriter.open();
            this.jsonWriter.write(this.brd);
            this.jsonWriter.close();
            System.out.println("Saved game!");
        } catch (FileNotFoundException e) {
            // e.printStackTrace();
            System.out.println("Save file could not be found! Game not saved!");
        }
    }

    // MODIFIES: this
    // EFFECTS: loads the saved state from file
    // handles IOException
    private void loadGame() {
        try {
            this.brd = this.jsonReader.read();
            this.reload();
            System.out.println("Loaded from " + this.jsonName);
        } catch (IOException e) {
            // e.printStackTrace();
            System.out.println("Unable to read from file: " + this.jsonName);
        }
    }

    // MODIFIES: this
    // EFFECTS: if pressed create new button then create new Blocks onto the board
    // if valid
    // if pressed remove button then remove the block at the clicked location
    // if pressed on an existing block then prepare to move otherwise move if valid
    private void tryActions(JButton jb, byte curr) {
        if (this.toCreate) {
            this.trytoCreate(jb, curr);
            return;
        }

        if (this.toRemove) {
            this.trytoRemove(curr);
            return;
        }

        if (!this.toMove) {
            if (curr != 'y') {
                this.toCreate = false;
                this.toRemove = false;
                this.toMove = true;
                this.blockInd = (curr == 'x') ? 0 : Integer.parseInt(String.valueOf(curr));
                this.blockToMove = this.brd.getBlockAt(this.blockInd);
            }
        } else {
            this.trytoMove(jb, curr);
        }
    }

    // MODIFIES: this
    // EFFECTS: After create new button is pressed, if an empty and valid location
    // is pressed
    // then create a new block there
    private void trytoCreate(JButton jb, byte curr) {
        Block nxt;
        if (curr == 'y') {
            if (this.isVert) {
                nxt = new Vertblock(this.currRow, this.currColumn, this.startInd);
            } else {
                nxt = new Horiblock(this.currRow, this.currColumn, this.startInd);
            }
            if (this.brd.insertBlock(nxt)) {
                // jb.setText(String.valueOf(this.startInd));
                if (this.isVert) {
                    // this.btn[this.currRow + 1][this.currColumn].setText(String.valueOf(this.startInd));
                    this.setColor(this.currRow, this.currColumn, this.currRow + 1, this.currColumn);
                } else {
                    // this.btn[this.currRow][this.currColumn + 1].setText(String.valueOf(this.startInd));
                    this.setColor(this.currRow, this.currColumn, this.currRow, this.currColumn + 1);
                }
                this.startInd++;
            }
        }
        this.toCreate = false;
        this.toRemove = false;
        this.toMove = false;
        this.blockInd = -1;
    }

    // MODIFIES: this
    // EFFECTS: After remove button is pressed, if an existing block is pressed
    // then remove the existing block and relevel the other blocks
    private void trytoRemove(byte curr) {
        if (curr != 'x' && curr != 'y') {
            this.blockInd = curr;
            Block currB = this.brd.getBlockAt(this.blockInd);
            int row = currB.getRowNumber();
            int col = currB.getColumnNumber();
            this.isVert = currB.isVertical();
            if (this.brd.removeBlock(this.blockInd)) {
                // this.btn[row][col].setText(" ");
                if (this.isVert) {
                    // this.btn[row + 1][col].setText(" ");
                    this.btn[row + 1][col].setBackground(null);
                    this.btn[row][col].setBackground(null);
                } else {
                    // this.btn[row][col + 1].setText(" ");
                    this.btn[row][col].setBackground(null);
                    this.btn[row][col + 1].setBackground(null);
                }
                this.startInd--;
                this.relevel(this.blockInd, this.startInd);
            }
        }
        this.toCreate = false;
        this.toRemove = false;
        this.toMove = false;
        this.blockInd = -1;
    }

    // MODIFIES: this
    // EFFECTS: decrease the level of each block indexed start to end
    private void relevel(int start, int end) {
        for (int i = start; i < end; i++) {
            Block currB = this.brd.getBlockAt(i);
            int row = currB.getRowNumber();
            int col = currB.getColumnNumber();

            // this.btn[row][col].setText(String.valueOf(i));
            this.brd.setCellAt(row, col, (byte) i);
            if (currB.isVertical()) {
                // this.btn[row + 1][col].setText(String.valueOf(i));
                this.brd.setCellAt(row + 1, col, (byte) i);
            } else {
                // this.btn[row][col + 1].setText(String.valueOf(i));
                this.brd.setCellAt(row, col + 1, (byte) i);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: After an exisiting block is pressed, if a valid position next to it
    // is pressed
    // then move the block there
    private void trytoMove(JButton jb, byte curr) {
        int row = this.blockToMove.getRowNumber();
        int col = this.blockToMove.getColumnNumber();

        if (curr == 'y') {
            if (this.blockToMove.isVertical()) {
                if (this.currRow == row - 1 && this.currColumn == col) {
                    this.move(jb, "u", row, col);

                } else if (this.currRow == row + 2 && this.currColumn == col) {
                    this.move(jb, "d", row, col);
                }
            } else {
                if (this.currColumn == col - 1 && this.currRow == row) {
                    this.move(jb, "l", row, col);
                } else if (this.currColumn == col + 2 && this.currRow == row) {
                    this.move(jb, "r", row, col);
                }
            }
        }
        this.toCreate = false;
        this.toRemove = false;
        this.toMove = false;
        this.blockInd = -1;
        this.blockToMove = null;
    }

    // MODIFIES: this
    // EFFECTS: move the vertical block up or down and move the horizontal block
    // left or right
    // check if won after each move
    private void move(JButton jb, String dir, int row, int col) {
        int bn = this.blockToMove.getBlockNumber();
        if (bn == 120) {
            jb.setText(String.valueOf((char) bn));
        } else {
            // jb.setText(String.valueOf(bn));
        }
    
        this.handleMoveColor(row, col, dir);
    
        switch (dir) {
            case "u":
                row += 1;
                break;
            case "l":
                col += 1;
                break;
        }
    
        this.btn[row][col].setText("");
        this.btn[row][col].setForeground(Color.decode("#4c0000"));
        this.btn[row][col].setBackground(null); // Add this line to uncolor the button
    
        this.brd.move(dir, this.blockInd);
    
        if (this.brd.isWon()) {
            this.displayWon();
        }
    }
    

    // MODIFIES: this
    // EFFECTS: change color of the pair of buttons
    private void handleMoveColor(int row, int col, String dir) {
        switch (dir) {
            case "u":
                this.changeColor(row, col, row - 1, col);
                break;
            case "l":
                this.changeColor(row, col, row, col - 1);
                break;
            case "r":
                this.changeColor(row, col + 1, row, col + 2);
                break;
            case "d":
                this.changeColor(row + 1, col, row + 2, col);
                break;
        }
    }

    //
    //
    private void changeColor(int row1, int col1, int row2, int col2) {
        int blockNumber = this.brd.getCellAt(row1, col1);
        Color color = this.blockColors.get(blockNumber);
    
        this.btn[row1][col1].setOpaque(true);
        this.btn[row1][col1].setContentAreaFilled(true);
        this.btn[row2][col2].setOpaque(true);
        this.btn[row2][col2].setContentAreaFilled(true);
        this.btn[row1][col1].setBackground(color);
        this.btn[row2][col2].setBackground(color);
    }

    //
    //
    private void setColor(int row1, int col1, int row2, int col2) {
        int blockNumber = this.brd.getCellAt(row1, col1);
        Color color;
        if (this.blockColors.containsKey(blockNumber)) {
            color = this.blockColors.get(blockNumber);
        } else {
            Random random = new Random();
            color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            this.blockColors.put(blockNumber, color);
        }
    
        this.btn[row1][col1].setOpaque(true);
        this.btn[row1][col1].setContentAreaFilled(true);
        this.btn[row2][col2].setOpaque(true);
        this.btn[row2][col2].setContentAreaFilled(true);
        this.btn[row1][col1].setBackground(color);
        this.btn[row2][col2].setBackground(color);
    }
    

    // MODIFIES: this
    // EFFECTS: find and return the button that is pressed
    private JButton findButton(ActionEvent e) {
        int i = 0;
        int j = 0;
        for (; i < this.size; i++) {
            for (; j < this.size; j++) {

                if (e.getSource() == this.btn[i][j]) {
                    this.currRow = i;
                    this.currColumn = j;
                    return this.btn[i][j];
                }

            }
            j = 0;
        }
        return null;
    }

    // EFFECTS: dispose and create EndWindow
    private void displayWon() {
        this.dispose();
        new EndWindow();
    }
}