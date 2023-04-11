package gui.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StartMenu extends JFrame implements ActionListener {
    private JButton submitButton;
    private JButton startButton;
    private JButton instructionButton;
    private JButton leveButton;
    private JTextField txt;
    private JLabel error;
    private final int boxSize = 500;
    private final int fromLeft = 50;
    private final int fromTop = 225;
    private final int txtW = 300;
    private final int txtH = 50;

    // MODIFIES: this
    // EFFECTS: set up the JFrame with title, error message, info message, text field, and buttons
    public StartMenu() {
        this.initDefault();

        JLabel info = new JLabel("Enter the size of the board");
        info.setBounds(this.fromLeft, this.fromTop - 50, this.txtW, this.txtH);
        info.setFont(new Font("Serif", Font.BOLD, 24));

        this.txt = new JTextField();
        this.txt.setBounds(this.fromLeft, this.fromTop, this.txtW, this.txtH);

        this.addButtons();

        this.add(info);
        this.add(this.txt);
        this.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: set title, layout, size, and color, then create and add title, error message
    private void initDefault() {
        this.setTitle("Menu");
        this.setLayout(null);
        this.setSize(this.boxSize, this.boxSize);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.getContentPane().setBackground(Color.gray);

        JLabel title = new JLabel("BlockSlider");
        title.setBounds(this.fromLeft + 50, 0, 500, 100);
        title.setFont(new Font("Algerian", Font.BOLD, 50));
        title.setForeground(Color.cyan);

        this.error = new JLabel();
        this.error.setBounds(this.fromLeft, this.fromTop + 25, 200, 100);
        this.error.setForeground(Color.RED);

        this.add(title);
        this.add(this.error);
    }

    // MODIFIES: this
    // EFFECTS: create and add submitButton, instructionButton, and loadButton
    private void addButtons() {
        this.startButton = new JButton("Start");
        this.startButton.addActionListener(this);
        this.startButton.setBounds(this.fromLeft * 4, this.fromTop - 112, 100, 50);

        this.submitButton = new JButton("Submit");
        this.submitButton.addActionListener(this);
        this.submitButton.setBounds(this.fromLeft + this.txtW, this.fromTop, 75, 50);

        this.instructionButton = new JButton("Instruction");
        this.instructionButton.addActionListener(this);
        this.instructionButton.setBounds(this.fromLeft * 4, this.fromTop + 75, 100, 25);

        JButton loadButton = new JButton("load");
        loadButton.addActionListener(this);
        loadButton.setBounds(this.fromLeft * 4, this.fromTop + 112, 100, 25);

        // this.leveButton = new JButton("level");
        // this.leveButton.addActionListener(this);
        // this.leveButton.setBounds(this.fromLeft * 4, this.fromTop + 149, 100, 25);

        this.add(this.startButton);
        this.add(this.submitButton);
        this.add(this.instructionButton);
        // this.add(this.leveButton);
        this.add(loadButton);
    }

    // MODIFIES: this
    // EFFECTS: create the GUI of game if a valid integer is passed into the text field
    //          create the instructionWindow if instructionButton is clicked
    //          create the GUI then load the previous save if loadButton is clicked
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.submitButton) {
            try {
                int size = Integer.parseInt(this.txt.getText());
                this.dispose();
                new GUI(size);
            } catch (NumberFormatException ex) {
                this.txt.setText("");
                this.error.setText("Invalid Submission");
            }
        } else if (e.getSource() == this.instructionButton) {
            this.dispose();
            new InstructionWindow();
        } 
        // else if (e.getSource() == this.leveButton) {
        //     this.dispose();
        //     new LevelWindow();
        // } 
        else if (e.getSource() == this.startButton) {
            // this.dispose();
            // new GUI(6);
            this.dispose();
            new LevelWindow();
        } else {
            this.dispose();
            new GUI("./data/savedBoard.json");
        }
    }
}



