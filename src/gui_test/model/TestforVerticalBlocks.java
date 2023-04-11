package gui_test.model;

import gui.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestforVerticalBlocks extends TestforBlocks {
    @Override
    @BeforeEach
    public void start() {
        this.block1 = new Vertblock(0,0,0);
        this.block2 = new Vertblock(10,9,8);
    }

    @Override
    @Test
    public void testDirection() {
        assertTrue(this.block1.isVertical());
        assertTrue(this.block2.isVertical());
    }
}