package gui_test.model;

import gui.model.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestJSON {
    protected void checkBlock(Block blk, int x, int y, int n) {
        assertEquals(x, blk.getRowNumber());
        assertEquals(y, blk.getColumnNumber());
        assertEquals(n, blk.getBlockNumber());
    }
}
