package gui_test.model;

import gui.model.*;
import org.junit.jupiter.api.Test;
import gui.persistence.JsonReader;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TestJsonReader extends TestJSON {

    @Test
    void testReadNoFile() {
        JsonReader reader = new JsonReader("./data/noFile.json");
        try {
            Board brd = reader.read();
            fail("Exception expected and not caught");
        } catch (IOException e) {
            System.out.println("Exception expected and caught");
        }
    }

    @Test
    void testReaderEmpty() {
        JsonReader reader = new JsonReader("./data/testMT.json");
        try {
            Board brd = reader.read();
            assertEquals(6, brd.size());
            assertEquals(0, brd.numberofBlocks());
        } catch (IOException e) {
            fail("Exception not expected");
        }
    }

    @Test
    void testReaderDecentSize() {
        JsonReader reader = new JsonReader("./data/decentSized.json");
        try {
            Board brd = reader.read();
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
