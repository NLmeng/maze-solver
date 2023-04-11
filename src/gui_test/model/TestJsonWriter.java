package gui_test.model;

import gui.model.*;
import org.junit.jupiter.api.Test;
import gui.persistence.JsonReader;
import gui.persistence.JsonWriter;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

public class TestJsonWriter extends TestJSON {

    @Test
    void testInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/\0noFile.json");
            writer.open();
            fail("Exception was expected");
        } catch (IOException e) {
            System.out.println("Exception expected and caught");
        }
    }

    @Test
    void testWriterOnEmpty() {
        try {
            Board brd = new Board(6);
            brd.removeBlock(0);
            JsonWriter writer = new JsonWriter("./data/testMT.json");
            writer.open();
            writer.write(brd);
            writer.close();

            JsonReader reader = new JsonReader("./data/testMT.json");
            brd = reader.read();
            assertEquals(6, brd.size());
            assertEquals(0, brd.numberofBlocks());
        } catch (IOException e) {
            fail("Exception not expected");
        }
    }

    @Test
    void testWriterDecentSize() {
        try {
            Board brd = new Board(6);
            brd.insertBlock(new Horiblock(0, 2, 2));
            brd.insertBlock(new Vertblock(1, 2, 3));
            brd.insertBlock(new Horiblock(5, 0, 4));
            brd.insertBlock(new Vertblock(3, 5, 5));

            JsonWriter writer = new JsonWriter("./data/decentSized.json");
            writer.open();
            writer.write(brd);
            writer.close();

            JsonReader reader = new JsonReader("./data/decentSized.json");
            brd = reader.read();
            assertEquals(6, brd.size());
            assertEquals(5, brd.numberofBlocks());
            this.checkBlock(brd.getBlockAt(0), 3, 0, 'x');
            this.checkBlock(brd.getBlockAt(1), 0, 2, 2);
            this.checkBlock(brd.getBlockAt(2), 1, 2, 3);
            this.checkBlock(brd.getBlockAt(3), 5, 0, 4);
            this.checkBlock(brd.getBlockAt(4), 3, 5, 5);
        } catch (IOException e) {
            fail("Exception not expected");
        }
    }
}
