import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StoredMessages {

    public static List<Message> readStoredMessagesFromJson(String filePath) {
        List<Message> storedMessages = new ArrayList<>();
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(filePath)) {
            JSONArray jsonArray = (JSONArray) parser.parse(reader);
            for (Object obj : jsonArray) {
                JSONObject jsonObject = (JSONObject) obj;
                String messageID = (String) jsonObject.get("MessageID");
                String recipient = (String) jsonObject.get("Recipient");
                String messageText = (String) jsonObject.get("Message");
                // MessageHash is also stored but not directly used to reconstruct the Message unless it's necessary for display.
                storedMessages.add(new Message(messageID, recipient, messageText));
            }
        } catch (IOException | ParseException e) {
            System.err.println("Error reading JSON file: " + e.getMessage());
            // Return an empty list if the file doesn't exist or is empty
            return new ArrayList<>();
        }
        return storedMessages;
    }
}
