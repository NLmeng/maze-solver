package gui_test.model;

import gui.model.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestforBoard {
    protected Board brd1, brd2;
    protected Block block1, block2;

    @BeforeEach
    public void start() {
        this.brd1 = new Board(6);
        this.brd2 = new Board(12);
        this.block1 = new Vertblock(0, 0, 1);
        this.block2 = new Horiblock(0, 1, 2);
        this.brd1.removeBlock(0);
        this.brd2.removeBlock(0);
    }

    @Test
    public void testEmptyBoards() {
        assertEquals(6, this.brd1.size());
        assertEquals(12, this.brd2.size());

        assertEquals(0, this.brd1.numberofBlocks());
        assertEquals(0, this.brd2.numberofBlocks());

        this.testCells(this.brd1.size(), this.brd1);
        this.testCells(this.brd2.size(), this.brd2);
    }
    //
    private void testCells(int size, Board b) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                assertEquals('y', b.getCellAt(i, j));
            }
        }
    }

    @Test
    public void testAddBlocks() {
        this.brd1.addBlock(this.block1);
        assertEquals(1, this.brd1.numberofBlocks());
        assertEquals(this.block1, this.brd1.getBlockAt(0));

        this.brd1.addBlock(this.block2);
        assertEquals(2, this.brd1.numberofBlocks());
        assertEquals(this.block2, this.brd1.getBlockAt(1));
        // renumber (case blockNumber > 120)
        Block ott = new Horiblock(3, 4, 121);
        this.brd1.addBlock(ott);
        this.brd1.removeBlock(1);
        assertEquals(120, this.brd1.getBlockAt(1).getBlockNumber());
    }

    @Test
    public void testInsertBlock() {
        assertTrue(this.brd1.insertBlock(this.block1));
        assertTrue(this.brd1.insertBlock(this.block2));

        assertFalse(this.brd1.insertBlock(this.block1));
        assertFalse(this.brd1.insertBlock(this.block2));

        this.brd1.removeBlock(0);
        this.brd1.removeBlock(0);
        // bottom left and top right
        Block temp1 = new Horiblock(5, 0, 3);
        Block temp2 = new Vertblock(0, 5, 4);
        this.brd1.insertBlock(temp1);
        this.brd1.insertBlock(temp2);
        // row: T & T & F58
        this.block1.setRowNumber(4);
        this.block1.setColumnNumber(0);
        assertFalse(this.brd1.insertBlock(this.block1));
        this.block1.setRowNumber(5);
        this.block1.setColumnNumber(4);
        assertFalse(this.brd1.insertBlock(this.block1));
        // ?row : T & T & F62
        this.block2.setRowNumber(0);
        this.block2.setColumnNumber(4);
        assertFalse(this.brd1.insertBlock(this.block2));
        this.block2.setRowNumber(4);
        this.block2.setColumnNumber(5);
        assertFalse(this.brd1.insertBlock(this.block2));
    }

    @Test
    public void testInsertMoreEdgeCases() {
        // row > size, col > size
        this.block1.setRowNumber(6);
        this.block1.setColumnNumber(6);
        assertFalse(this.brd1.insertBlock(this.block1));
        // col, row > size
        this.block1.setRowNumber(100);
        this.block1.setColumnNumber(5);
        assertFalse(this.brd1.insertBlock(this.block1));
        // row < 0, col < 0
        this.block2.setRowNumber(-11);
        this.block2.setColumnNumber(-1);
        assertFalse(this.brd1.insertBlock(this.block2));
        // row, col < size
        this.block2.setRowNumber(-12);
        this.block2.setColumnNumber(0);
        assertFalse(this.brd1.insertBlock(this.block2));
    }

    @Test
    public void testRemoveBlocks() {
        this.brd1.insertBlock(this.block1);
        this.brd1.insertBlock(this.block2);

        assertFalse(this.brd1.removeBlock(-1));
        assertFalse(this.brd1.removeBlock(6));

        assertTrue(this.brd1.removeBlock(0));
        assertTrue(this.brd1.removeBlock(0));
    }

    @Test
    public void testBadVerticalMoves() {
        Block temp1 = new Horiblock(2, 0, 3);
        this.brd1.insertBlock(this.block1);
        this.brd1.insertBlock(temp1);
        // downward taken
        assertFalse(this.brd1.move("d", 0));
        assertFalse(this.brd1.move("d", 1));

        this.brd1.removeBlock(0);
        this.brd1.removeBlock(0);
        temp1 = new Horiblock(0, 0 , 3);
        this.block1.setRowNumber(1);
        this.brd1.insertBlock(this.block1);
        this.brd1.insertBlock(temp1);
        // upward taken
        assertFalse(this.brd1.move("u", 0));
        // T & T & F146
        temp1.setRowNumber(5);
        assertFalse(this.brd1.move("u", 1));
    }

    @Test
    public void testBadHorizontalMoves() {
        Block temp2 = new Vertblock(0, 0, 3);
        this.brd1.insertBlock(this.block2);
        this.brd1.insertBlock(temp2);
        // leftward taken
        assertFalse(this.brd1.move("l", 0));
        // T & T & F163
        temp2.setColumnNumber(5);
        assertFalse(this.brd1.move("l", 1));

        this.brd1.removeBlock(0);
        this.brd1.removeBlock(0);
        temp2 = new Vertblock(0, 3 , 3);
        this.brd1.insertBlock(this.block2);
        this.brd1.insertBlock(temp2);
        // rightward taken
        assertFalse(this.brd1.move("r", 0));
        assertFalse(this.brd1.move("r", 1));
    }

    @Test
    public void testMoves() {
        this.brd1.insertBlock(this.block1);
        this.brd1.insertBlock(this.block2);
        assertFalse(this.brd1.move("c", 0));
        assertFalse(this.brd1.move("u", 0));
        assertFalse(this.brd1.move("l", 1));

        assertTrue(this.brd1.move("d", 0));
        assertEquals(1, this.brd1.getBlockAt(0).getRowNumber());
        assertTrue(this.brd1.move("u", 0));
        assertEquals(0, this.brd1.getBlockAt(0).getRowNumber());
        assertFalse(this.brd1.move("u", 0));

        assertTrue(this.brd1.move("r", 1));
        assertEquals(2, this.brd1.getBlockAt(1).getColumnNumber());
        assertTrue(this.brd1.move("l", 1));
        assertEquals(1, this.brd1.getBlockAt(1).getColumnNumber());
        this.brd1.move("d", 0);
        this.brd1.move("l", 1);
        assertFalse(this.brd1.move("l", 1));

        assertFalse(this.brd1.move("l", 0));
        assertFalse(this.brd1.move("r", 0));
        assertFalse(this.brd1.move("u", 1));
        assertFalse(this.brd1.move("d", 1));
    }

    @Test
    public void testConsecutiveMoves() {
        this.brd1.insertBlock(this.block1);

        this.tryVerticalMoves("d", this.brd1, 0);
        this.tryVerticalMoves("u", this.brd1, 0);

        this.brd1.removeBlock(0);
        this.brd1.insertBlock(this.block2);
        this.tryHorizontalMove("r", this.brd1, 0);
        this.tryHorizontalMove("l", this.brd1, 0);
    }
    //
    private void tryVerticalMoves(String dir, Board b, int ind) {
        int col = 0;

        for (int i = 0; i < b.size(); i++) {
            if (dir.equals("d")) {
                col = 2;
            }
            col += b.getBlockAt(ind).getRowNumber();

            if (col < b.size() && col > 0) {
                assertTrue(b.move(dir, ind));
            } else {
                assertFalse(b.move(dir, ind));
            }
            col = 0;
        }
    }
    //
    private void tryHorizontalMove(String dir, Board b, int ind) {
        int row = 0;

        for (int i = 0; i < b.size(); i++) {
            if (dir.equals("r")) {
                row = 2;
            }
            row += b.getBlockAt(ind).getColumnNumber();

            if (row < b.size() && row > 0) {
                assertTrue(b.move(dir, ind));
            } else {
                assertFalse(b.move(dir, ind));
            }
            row = 0;
        }
    }

    @Test
    public void testWin() {
        this.brd1.insertBlock(new Goalblock(this.brd1.size() / 2, 0, 1));

        assertEquals('x', this.brd1.getBlockAt(0).getBlockNumber());
        boolean isWon = false;
        while(!isWon) {
            this.brd1.move("r", 0);
            isWon = this.brd1.isWon();

            if (this.brd1.getBlockAt(0).getColumnNumber() + 1 < this.brd1.size() - 1) {
                assertFalse(isWon);
            }
        }
        assertTrue(isWon);
    }
}
