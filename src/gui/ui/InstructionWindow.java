package gui.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class InstructionWindow extends JFrame implements ActionListener {
    private JButton returnButton;
    private final int size = 1000;
    private ImageIcon img;
    private Image image;
    private JLabel jl1;
    private JLabel jl2;
    private JLabel text1;
    private JLabel text2;
    private JPanel center;

    // MODIFIES: this
    // EFFECTS: set up JFrame with 2 images, 2 text label, and buttons
    //          reopen StartMenu if close
    public InstructionWindow() {
        this.init();

        this.makePanel();

        this.addButtons();

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                new StartMenu();
            }
        });

        this.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: set title, layout, size
    private void init() {
        this.setTitle("Instruction");
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        this.setContentPane(contentPane);
        this.setSize(this.size, this.size);
    }

    // MODIFIES: this
    // EFFECTS: create and add the 2 images and text into GridLayout
    private void makePanel() {
        this.center = new JPanel(new GridLayout(2, 2, 0, 1));

        this.img = new ImageIcon("./data/instr.png");
        this.image = this.img.getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH);
        this.img = new ImageIcon(this.image);
        this.jl1 = new JLabel(this.img);

        this.img = new ImageIcon("./data/instrgoal.png");
        this.image = this.img.getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH);
        this.img = new ImageIcon(this.image);
        this.jl2 = new JLabel(this.img);

        this.createTexts();

        this.center.add(this.jl1);
        this.center.add(this.text1);
        this.center.add(this.jl2);
        this.center.add(this.text2);
        this.add(this.center);
    }

    // MODIFIES: this
    // EFFECTS: create and add returnButton
    private void addButtons() {
        this.returnButton = new JButton("return");
        this.returnButton.addActionListener(this);
        this.returnButton.setPreferredSize(new Dimension(200, 50));

        this.add(this.returnButton);
        this.add(Box.createVerticalGlue());
    }

    // EFFECTS: set the message in the 2 texts and their fonts and colors
    private void createTexts() {
        this.text1 = new JLabel("<html> TYPE OF BLOCKS <br/>"
                + " There are 2 different kind of blocks. <br/> As shown, the blue is horizontal and can"
                + " only move left and right, and the purple is vertical and can only move up and down. Each move"
                + " cannot be made if there is already an existing block in the way. <br/> "
                + " For example, the purple block can move to the yellow circle but not down.</html>");
        this.text1.setFont(new Font("Dialog", Font.BOLD, 24));
        this.text1.setForeground(Color.decode("#2C3E50"));

        this.text2 = new JLabel("<html> WINNING <br/>"
                + " To win, you must move the GoalBlock to the goal."
                + " The game always start with the GoalBlock labelled 'xx'. "
                + " Move any other blocks that are in the way to move 'xx' all the way to"
                + " the right edge.</html>\"");
        this.text2.setFont(new Font("Dialog", Font.BOLD, 24));
        this.text2.setForeground(Color.decode("#2C3E50"));
    }

    // MODIFIES: this
    // EFFECTS: create StartMenu if returnButton is clicked
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.returnButton) {
            this.dispose();
            new StartMenu();
        }
    }
}
