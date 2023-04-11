package gui.model;

public class Goalblock extends Horiblock {

    // EFFECTS: create a Horizontal Block
    public Goalblock(int x, int y, int n) {
        super(x, y, n);
    }

    // EFFECTS: return number representing 'X'
    @Override
    public int getBlockNumber() {
        return 'x';
    }
}
