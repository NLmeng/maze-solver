package gui.model;

public class Horiblock extends Block {

    // EFFECTS: create a Horizontal Block
    public Horiblock(int x, int y, int n) {
        super(x, y, n);
    }

    // EFFECT: return false, indicating Horizontal
    @Override
    public boolean isVertical() {
        return !ISVERTICAL;
    }
}
