package gui.persistence;

import gui.model.*;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;


public class JsonWriter {
    private static final int TAB = 4;
    private String destination;
    private PrintWriter writer;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    //          be opened for writing
    public void open() throws FileNotFoundException {
        this.writer = new PrintWriter(new File(this.destination));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of board to file
    public void write(Board brd) {
        JSONObject json = brd.toJson();
        this.saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        this.writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        this.writer.print(json);
    }
}
