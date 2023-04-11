package gui.ui;

import gui.model.Event;
import gui.model.EventLog;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class EndWindow extends JFrame {
    private ImageIcon img;
    private JLabel jl;

    // MODIFIES: this
    // EFFECTS: set up JFrame with title and then add an image
    public EndWindow() {
        this.img = new ImageIcon("./data/won.jpg");
        this.jl = new JLabel(this.img);

        this.setTitle("You Won!");
        this.add(this.jl);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                for (Event next : EventLog.getInstance()) {
                    System.out.println(next.getDescription());
                }
            }
        });
    }
}
