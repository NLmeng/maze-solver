# Board Game's Path Searching
This project implements an A* algorithm for path traversal to solve a given board game. The A* algorithm, along with other data structures like graphs, is used to find the most efficient path in the game. By fine-tuning the heuristic function, the algorithm's performance can be improved, allowing for faster and more accurate pathfinding, about 200% time improvements with about the same accuracy. The application features a user-friendly graphical user interface (GUI) built with Java Swing and tested with JUnit that allows users to interact with the board game and visualize the pathfinding process. 

### Auto pathfinding demo in my website [SEE MORE](https://lymengblog.vercel.app/):
<p align="center">
  <img src="https://github.com/NLmeng/maze-solver/blob/master/rh2.gif" alt="DEMO" width="600" height="500">
</p>

### Auto pathfinding demo implemented in Java (goal is to move the BLACK horizontal block to the rightmost edge):
<p align="center">
  <img src="https://github.com/NLmeng/maze-solver/blob/NLmeng-testgif/demo%20(1).gif" alt="DEMO" width="600" height="500">
</p>

### Getting Started

#### Prerequisites
- Java Development Kit (JDK)
- A Unix-like shell (e.g., bash) if you want to use the provided script

#### Running the project
- Clone the repository to your local machine.

- Compile the Java files using the following command (on Unix-based systems):
  - `chmod +x run.sh`
  - `./run.sh`

- Alternatively, you can run the project manually with the following command:
  - `javac -cp "src:lib/json-20210307.jar" -d bin src/gui/model/*.java src/gui/persistence/*.java src gui/ui/*.java`
  - `java -cp "bin:lib/json-20210307.jar" gui/ui/Main`
