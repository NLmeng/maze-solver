package gui.persistence;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import java.nio.file.Path;

public class TxtToJsonConverter {

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
                        length++;
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

}
