package gui.ui;

import gui.persistence.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LevelWindow extends JFrame implements ActionListener {
    private final int boxSize = 500;
    private final int fromLeft = 50;
    private final int fromTop = 225;
    private final int width = 250;
    private final int height = 25;
    private final int difficulty = 3;
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
        this.lvl = new JButton[this.difficulty];
        for (int i = 0; i < this.difficulty; i++) {
            this.lvl[i] = new JButton("level" + (i + 1));
            this.lvl[i].addActionListener(this);
            this.lvl[i].setBounds(this.fromLeft * 2 + 10, this.fromTop - 125 + (50 * i), this.width, this.height);
            this.add(this.lvl[i]);
        }
    }

    // EFFECTS: load a pre-made game according to the level selected
    @Override
    public void actionPerformed(ActionEvent e) {
        Random random = new Random();
        for (int i = 0; i < this.difficulty; i++) {
            if (e.getSource() == this.lvl[i]) {
                this.dispose();
                
                // Calculate the level prefix based on the button clicked, randomly
                int randomLevel = random.nextInt(3);
                char levelPrefix = (char) ('A' + i); 
    
                String levelFileName = String.format("./data/lvl%c/%c%02d.txt", levelPrefix, levelPrefix, randomLevel);
                String jsonFilePath = String.format("./data/lvl%c/%c%02d.json", levelPrefix, levelPrefix, randomLevel);
                try {
                    JSONObject jsonObject = TxtToJsonConverter.textToJSON(levelFileName);

                    Files.writeString(Paths.get(jsonFilePath), jsonObject.toString());
                } catch (IOException err) {
                    err.printStackTrace();
                }

                new GUI(jsonFilePath);
            }
        }
    }
}
