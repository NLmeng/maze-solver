package gui_test.model;

import gui.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TestforBlocks {
    protected Block block1, block2;

    @BeforeEach
    public abstract void start();

    @Test
    public void testBlock() {
        assertEquals(0, this.block1.getRowNumber());
        assertEquals(0, this.block1.getColumnNumber());
        assertEquals(0, this.block1.getBlockNumber());

        assertEquals(10, this.block2.getRowNumber());
        assertEquals(9, this.block2.getColumnNumber());
        assertEquals(8, this.block2.getBlockNumber());
    }

    @Test
    public abstract void testDirection();

    @Test
    public void testSetters() {
        this.block1.setColumnNumber(11);
        this.block1.setRowNumber(12);
        assertEquals(11, this.block1.getColumnNumber());
        assertEquals(12, this.block1.getRowNumber());

        this.block1.setColumnNumber(13);
        this.block1.setRowNumber(14);
        assertEquals(13, this.block1.getColumnNumber());
        assertEquals(14, this.block1.getRowNumber());
    }

    @Test
    public void testRelevel() {
        this.block1.relevelBlockNumber();
        assertEquals(-1, this.block1.getBlockNumber());

        this.block2.relevelBlockNumber();
        this.block2.relevelBlockNumber();
        assertEquals(6, this.block2.getBlockNumber());
    }
}
