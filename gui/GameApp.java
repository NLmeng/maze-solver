import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class GameApp extends Application {

    private static final int BOARD_SIZE = 6;
    private static final int CELL_SIZE = 100;

    @Override
    public void start(Stage primaryStage) {
        GridPane board = this.createBoard();

        this.createVehicle(board, Color.RED, "HORIZONTAL", 2, 1, 1, 2);

        // Set up the scene and stage
        Scene scene = new Scene(board);
        primaryStage.setTitle("Rush Hour Solver");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createBoard() {
        GridPane board = new GridPane();

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                StackPane cell = new StackPane();
                Rectangle background = new Rectangle(CELL_SIZE, CELL_SIZE);
                background.setFill(Color.LIGHTGRAY);
                background.setStroke(Color.BLACK);
                cell.getChildren().add(background);
                board.add(cell, i, j);
            }
        }

        return board;
    }

    private GridPane createVehicle(GridPane board, Color color, String orientation, int width, int height, int row, int col) {
        Rectangle vehicle1 = new Rectangle(CELL_SIZE, CELL_SIZE);
        Rectangle vehicle2 = new Rectangle(CELL_SIZE, CELL_SIZE);
        vehicle1.setFill(color);
        vehicle1.setStroke(Color.BLACK);
        vehicle2.setFill(color);
        vehicle2.setStroke(Color.BLACK);
        // Place the vehicle HORIZONTALLY
        board.add(vehicle1, row, col); 
        board.add(vehicle2, row+1, col); 
        return board;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
