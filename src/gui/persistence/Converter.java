package gui.persistence;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.nio.file.Path;

public class Converter {

    public static JSONObject textToJSON(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        int size = 6;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("size", size);

        char[][] grid = new char[size][size];
        int rowIndex = 0;
        for (String line : Files.readAllLines(path, StandardCharsets.UTF_8)) {
            grid[rowIndex++] = line.toCharArray();
        }

        int blockCounter = 0;
        Map<Character, Integer> blockMapping = new HashMap<>();

        // First, process the "XX" block
        boolean xxProcessed = false;
        for (int row = 0; row < size && !xxProcessed; row++) {
            for (int col = 0; col < size && !xxProcessed; col++) {
                if (grid[row][col] == 'X') {
                    jsonObject.put("row" + blockCounter, row);
                    jsonObject.put("col" + blockCounter, col);
                    jsonObject.put("isV" + blockCounter, false);
                    jsonObject.put("bNo" + blockCounter, 120);
                    blockCounter++;
                    grid[row][col] = '.';
                    grid[row][col + 1] = '.';
                    xxProcessed = true;
                }
            }
        }

        // Then process the rest of the blocks
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                char current = grid[row][col];
                if (current != '.') {
                    boolean isVertical = (row + 1 < size) && (grid[row + 1][col] == current);
                    grid[row][col] = '.';
                    int nextRow = row + (isVertical ? 1 : 0);
                    int nextCol = col + (isVertical ? 0 : 1);
                    while (nextRow < size && nextCol < size && grid[nextRow][nextCol] == current) {
                        grid[nextRow][nextCol] = '.';
                        nextRow += isVertical ? 1 : 0;
                        nextCol += isVertical ? 0 : 1;
                    }

                    if (!blockMapping.containsKey(current)) {
                        blockMapping.put(current, blockCounter++);
                    }
                    int blockNo = blockMapping.get(current);

                    jsonObject.put("row" + blockNo, row);
                    jsonObject.put("col" + blockNo, col);
                    jsonObject.put("isV" + blockNo, isVertical);
                    jsonObject.put("bNo" + blockNo, blockNo);
                }
            }
        }

        jsonObject.put("noBlocks", blockCounter);
        return jsonObject;
    }  

    public static String jsonToTxt(String filePath) throws IOException {
        String jsonString = readFile(filePath);
        JSONObject jsonObject = new JSONObject(jsonString);
        int size = jsonObject.getInt("size");
        char[][] board = new char[size][size];

        // Initialize the board with dots
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = '.';
            }
        }

        int noBlocks = jsonObject.getInt("noBlocks");
        for (int i = 0; i < noBlocks; i++) {
            int row = jsonObject.getInt("row" + i);
            int col = jsonObject.getInt("col" + i);
            boolean isV = jsonObject.getBoolean("isV" + i);
            char blockNo = (char) jsonObject.getInt("bNo" + i);
            if (blockNo == 120) {
                blockNo = 'X';
            } else {
                blockNo = (char) ('A' + (blockNo - 1));
            }

            board[row][col] = blockNo;
            if (isV) {
                board[row + 1][col] = blockNo;
            } else {
                board[row][col + 1] = blockNo;
            }
        }

        // Convert the board to a single string
        StringBuilder txtBoard = new StringBuilder();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                txtBoard.append(board[i][j]);
            }
            if (i < size - 1) {
                txtBoard.append("\n");
            }
        }

        return txtBoard.toString();
    }

    // Reads a file and returns its content as a string
    private static String readFile(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
    }

}
