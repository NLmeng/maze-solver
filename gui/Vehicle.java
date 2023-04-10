import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Vehicle extends Pane {
    private static final int CELL_SIZE = 100;
    private int width;
    private int height;
    private Color color;
    private String type;

    public Vehicle(int width, int height, Color color, String type) {
        this.width = width;
        this.height = height;
        this.color = color;
        this.type = type;

        Rectangle part = new Rectangle(width * CELL_SIZE, height * CELL_SIZE);
        part.setFill(color);
        part.setStroke(Color.BLACK);

        part.setTranslateX(1.0);

        this.getChildren().add(part);

        // Set the maximum size of the Vehicle Pane
        this.setMaxSize(CELL_SIZE, CELL_SIZE);
    }

    public String getType() {
        return this.type;
    }
}
