package gui_test.model;

import gui.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestforHorizontalBlocks extends TestforBlocks {
    @Override
    @BeforeEach
    public void start() {
        this.block1 = new Horiblock(0,0,0);
        this.block2 = new Horiblock(10,9,8);
    }

    @Override
    @Test
    public void testDirection() {
        assertFalse(this.block1.isVertical());
        assertFalse(this.block2.isVertical());
    }
}
