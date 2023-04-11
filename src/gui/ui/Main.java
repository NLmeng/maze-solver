package gui.ui;
import gui.persistence.*;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        String txtFilePath = "data/lvlA/A00.txt";
        String jsonFilePath = "data/lvlA/A00.json";
        try {
             // Convert the text file to a JSON object
             JSONObject jsonObject = TxtToJsonConverter.textToJSON(txtFilePath);

             // Save the JSON object to a file (optional)
             Files.writeString(Paths.get(jsonFilePath), jsonObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // new Startgame();
        new StartMenu();
    }
}
