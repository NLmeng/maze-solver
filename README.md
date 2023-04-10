# Board Game's Path Searching
This project implements an A* algorithm for path traversal to solve a given board game. The A* algorithm, along with other data structures like graphs, is used to find the most efficient path in the game. By fine-tuning the heuristic function, the algorithm's performance can be improved, allowing for faster and more accurate pathfinding, about 200% time improvements with about the same accuracy.

### Getting Started

#### Prerequisites
- Java Development Kit (JDK)
- JavaFX SDK
- A Unix-like shell (e.g., bash) if you want to use the provided script

#### Running the project
- Clone the repository to your local machine.

- Compile the Java files using the following command (on Unix-based systems):
  - `chmod +x run.sh`
  - `./run.sh`

- Alternatively, you can run the project manually with the following command:
  - `javac -cp src -d bin src/rushhour/*.java test/test.java`
  - `java -cp "bin:test" test`

#### Running the GUI
- `cd gui` (assuming you are in root directory)

- Add an environment variable: (download and find the path to your own `javafx-sdk-20/lib`)
  - `export PATH_TO_FX=/path/to/javafx-sdk-20/lib`

- Compile and Run the application: (make sure you are in /gui)
  - `chmod +x run.sh`
  - `./run.sh`

- Alternatively, you can run the project manually with the following command:
  - Compile the application:
    - `javac --module-path $PATH_TO_FX --add-modules javafx.controls GameApp.java`

  - Run the application:
    - `java --module-path $PATH_TO_FX --add-modules javafx.controls GameApp`