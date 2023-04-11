package gui.persistence;

import gui.model.*;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;


public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads board from file and returns it,
    //          throws IOException if an error occurs reading data from file
    public Board read() throws IOException {
        String jsonData = this.readFile(this.source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return this.parseBoard(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses board from JSON object and returns it
    private Board parseBoard(JSONObject jsonObject) {
        int size = jsonObject.getInt("size");
        Board brd = new Board(size);
        brd.removeBlock(0);
        this.addBoard(brd, jsonObject);
        return brd;
    }

    // MODIFIES: brd
    // EFFECTS: goes through all blocks that were saved in JSON object and adds them to board
    private void addBoard(Board brd, JSONObject jsonObject) {
        int noOfBlocks = jsonObject.getInt("noBlocks");
        if (noOfBlocks == 0) {
            brd.removeBlock(0);
        }
        for (int i = 0; i < noOfBlocks; i++) {
            this.addBlocks(brd, jsonObject, i);
        }
    }

    // MODIFIES: brd
    // EFFECTS: parses blocks from JSON object and inserts them to board
    private void addBlocks(Board brd, JSONObject jsonObject, int index) {
        int col = jsonObject.getInt("col" + index);
        int row = jsonObject.getInt("row" + index);
        boolean vert = jsonObject.getBoolean("isV" + index);
        char blockNo = (char) jsonObject.getInt("bNo" + index);

        Block nextBlock;
        if (vert) {
            nextBlock = new Vertblock(row, col, blockNo);
        } else {
            nextBlock = new Horiblock(row, col, blockNo);
        }
        brd.insertBlock(nextBlock);
    }
}
