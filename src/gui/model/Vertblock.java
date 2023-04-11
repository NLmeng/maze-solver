package gui.model;

public class Vertblock extends Block {

    // EFFECTS: create a Vertical Block
    public Vertblock(int x, int y, int n) {
        super(x, y, n);
    }

    // EFFECT: return true, indicating Vertical
    @Override
    public boolean isVertical() {
        return ISVERTICAL;
    }
}
