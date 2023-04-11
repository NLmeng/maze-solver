package gui.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LevelWindow extends JFrame implements ActionListener {
    private final int boxSize = 500;
    private final int fromLeft = 50;
    private final int fromTop = 225;
    private final int width = 250;
    private final int height = 25;
    private JButton[] lvl;

    // MODIFIES: this
    // EFFECTS: set up the JFrame with title and buttons
    public LevelWindow() {
        this.initDefault();
        this.addButtons();
        this.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: set title, layout, size, and color, then create and add title
    private void initDefault() {
        this.setTitle("Select Level of Game");
        this.setLayout(null);
        this.setSize(this.boxSize, this.boxSize);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.getContentPane().setBackground(Color.gray);

        JLabel title = new JLabel("Select Level of Game");
        title.setBounds(this.fromLeft + 75, 0, 500, 100);
        title.setFont(new Font("serif", Font.BOLD, 25));
        title.setForeground(Color.GREEN);

        this.add(title);

    }

    // MODIFIES: this
    // EFFECTS: create and add buttons
    private void addButtons() {
        this.lvl = new JButton[5];
        for (int i = 0; i < 5; i++) {
            this.lvl[i] = new JButton("level" + (i + 1));
            this.lvl[i].addActionListener(this);
            this.lvl[i].setBounds(this.fromLeft * 2 + 10, this.fromTop - 125 + (50 * i), this.width, this.height);
            this.add(this.lvl[i]);
        }
    }

    // EFFECTS: load a pre-made game according to the level selected
    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < 5; i++) {
            if (e.getSource() == this.lvl[i]) {
                this.dispose();
                new GUI("./data/lvl" + (i + 1) + ".json");
            }
        }
    }
}
