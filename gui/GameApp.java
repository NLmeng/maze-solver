import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.ColumnConstraints;

public class GameApp extends Application {

    private static final int BOARD_SIZE = 6;
    private static final int CELL_SIZE = 100;

    @Override
    public void start(Stage primaryStage) {
        GridPane board = this.createBoard();

        this.createVehicle(board, Color.RED, "horizontal", 2, 1, 1, 2);

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
    
    private GridPane createVehicle(GridPane board, Color color, String type, int width, int height, int row, int col) {
        Vehicle vehicle = new Vehicle(width, height, color, type);
        
        // Add mouse drag event handler to each rectangle
        EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
            double mouseX;
            double mouseY;
        
            @Override
            public void handle(MouseEvent event) {
                if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {

                    this.mouseX = event.getSceneX();
                    this.mouseY = event.getSceneY();

                } else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {

                    double deltaX = event.getSceneX() - this.mouseX;
                    double deltaY = event.getSceneY() - this.mouseY;
                    Vehicle vehicle = (Vehicle) event.getSource();
    
                    if (vehicle.getType().equals("horizontal")) {
                        vehicle.setTranslateX(1 + vehicle.getTranslateX() + deltaX);
                    } else if (vehicle.getType().equals("vertical")) {
                        vehicle.setTranslateY(vehicle.getTranslateY() + deltaY);
                    }
    
                    this.mouseX = event.getSceneX();
                    this.mouseY = event.getSceneY();

                } else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {

                    Vehicle vehicle = (Vehicle) event.getSource();
                    vehicle.setTranslateX(1 + Math.round(vehicle.getTranslateX() / CELL_SIZE) * CELL_SIZE);
                    vehicle.setTranslateY(Math.round(vehicle.getTranslateY() / CELL_SIZE) * CELL_SIZE);
                    
                }
            }
        };
    
        vehicle.setOnMousePressed(mouseHandler);
        vehicle.setOnMouseDragged(mouseHandler);
        vehicle.setOnMouseReleased(mouseHandler);
    
        board.add(vehicle, row, col);
        return board;
    }
    

    public static void main(String[] args) {
        launch(args);
    }
}
