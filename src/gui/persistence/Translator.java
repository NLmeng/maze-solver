package gui.persistence;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Translator {

    public static int translateBlockLetterToBNo(char letter, String[] txtData, Map<String, Object> jsonData) {
        int row = -1;
        int col = -1;
    
        // Find the position of the letter in txtData
        for (int i = 0; i < txtData.length; i++) {
            int index = txtData[i].indexOf(letter);
            if (index != -1) {
                row = i;
                col = index;
                break;
            }
        }
    
        if (row == -1 || col == -1) {
            return -1; // Letter not found
        }
    
        // Find the corresponding bNo in jsonData
        for (int keyValue = 0; keyValue < (int) jsonData.get("noBlocks"); keyValue++) {
            String rowKey = "row" + keyValue;
            String colKey = "col" + keyValue;
            if (jsonData.containsKey(rowKey) && jsonData.containsKey(colKey)) {
                int rowValue = (int) jsonData.get(rowKey);
                int colValue = (int) jsonData.get(colKey);
                if (rowValue == row && colValue == col) {
                    String bNoKey = "bNo" + keyValue;
                    if (jsonData.containsKey(bNoKey)) {
                        return (int) jsonData.get(bNoKey);
                    }
                }
            }
        }
    
        return -1; // bNo not found
    }

    public static String[] readTxtFile(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            return br.lines().toArray(String[]::new);
        } catch (IOException e) {
            throw new IOException("Error reading the txt file: " + e.getMessage());
        }
    }
    
    public static Map<String, Object> readJsonFile(String filePath) throws IOException {
        String jsonContent = new String(Files.readAllBytes(Paths.get(filePath)));
        JSONObject jsonObject = new JSONObject(jsonContent);
    
        Map<String, Object> jsonData = new HashMap<>();
        Iterator<String> keys = jsonObject.keys();
    
        while (keys.hasNext()) {
            String key = keys.next();
            Object value = jsonObject.get(key);
    
            if (value instanceof Integer) {
                jsonData.put(key, jsonObject.getInt(key));
            } else if (value instanceof Boolean) {
                jsonData.put(key, jsonObject.getBoolean(key));
            }
        }
    
        return jsonData;
    }

    public static List<String> readSolutionFile(String filePath) throws IOException {
        List<String> moves = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                moves.add(line);
            }
        }
        return moves;
    }

    public static void convertAndSaveTxtBoard(String inputPath, String outputPath) {
        try {
            String txtBoard = Converter.jsonToTxt(inputPath);
            System.out.println(txtBoard);
    
            try (FileWriter writer = new FileWriter(outputPath)) {
                writer.write(txtBoard);
                System.out.println("Successfully written to " + outputPath);
            } catch (IOException e) {
                System.err.println("Error writing to " + outputPath + ": " + e.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}