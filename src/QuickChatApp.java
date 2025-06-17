import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.File; // Import File class

public class QuickChatApp {
    static int messageCount = 0;
    static List<Message> sentMessages = new ArrayList<>(); // Contains all the messages sent.
    static List<Message> disregardedMessages = new ArrayList<>(); // Contains all the messages that were disregarded.
    static List<Message> storedMessages = new ArrayList<>(); // Contains the stored messages, loaded from JSON.
    static List<String> messageHashes = new ArrayList<>(); // Contains all the message hashes.
    static List<String> messageIDs = new ArrayList<>(); // Contains all the message IDs.

    static JSONArray jsonMessages = new JSONArray(); // Used for writing to messages.json

    public static void main(String[] args) {
        JOptionPane.showMessageDialog(null, "Welcome to QuickChat."); // Welcome message
        boolean running = true;

        // Load stored messages at the start of the application
        loadStoredMessages();

        // Start of login/registration
        boolean loggedIn = false;
        while (!loggedIn) {
            String[] options = {"1.Register & Login", "2.Quit"};
            int option = JOptionPane.showOptionDialog(null,
                    "Choose an option:",
                    "QuickChat Menu",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]);

            switch (option) {
                case 0 -> {
                    // Calls the static method in User.java to handle registration and login.
                    loggedIn = User.handleUserRegistrationAndLogin();
                    if (!loggedIn) {
                        JOptionPane.showMessageDialog(null, "Login failed or cancelled. Please try again.");
                    }
                }
                case 1, JOptionPane.CLOSED_OPTION -> {
                    running = false;
                    loggedIn = true;
                }
                default -> JOptionPane.showMessageDialog(null, "Invalid option. Try again.");
            }
            if (!running) {
                JOptionPane.showMessageDialog(null, "Thank you for using QuickChat. Goodbye!");
                return;
            }
        }
        // --- End of login/registration

        // This do-while loop will only execute if `loggedIn` is true
        do {
            String[] options = {"1.Send Messages", "2.Display Messages", "3.Search Messages", "4.Delete Message", "5.Display Report", "6.Quit"}; // Added new options
            int option = JOptionPane.showOptionDialog(null,
                    "Choose an option:",
                    "QuickChat Menu",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]);

            switch (option) {
                case 0 -> sendMessages();
                case 1 -> displayMessages(); // New method to display various message types
                case 2 -> searchMessages(); // New method to search messages
                case 3 -> deleteMessage(); // New method to delete a message
                case 4 -> displayReport(); // New method to display the report
                case 5, JOptionPane.CLOSED_OPTION -> running = false;
                default -> JOptionPane.showMessageDialog(null, "Invalid option. Try again.");
            }
        } while (running);

        JOptionPane.showMessageDialog(null, "Thank you for using QuickChat. Goodbye!");
    }

    public static void sendMessages() {
        String countStr = JOptionPane.showInputDialog("How many messages would you like to send?");
        if (countStr == null) return;
        int count;
        try {
            count = Integer.parseInt(countStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid number.");
            return;
        }

        for (int i = 0; i < count; i++) {
            String id = String.format("%010d", new Random().nextInt(1_000_000_000));

            String recipient = JOptionPane.showInputDialog("Enter recipient number (+code):");
            if (recipient == null) return;
            // Recipient validation: no more than 12 characters and starts with '+'
            if (recipient.length() > 12 || !recipient.startsWith("+")) {
                JOptionPane.showMessageDialog(null, "Invalid recipient number. It must start with '+' and be no more than 12 characters long.");
                continue;
            }

            String msg = JOptionPane.showInputDialog("Enter message:");
            if (msg == null) return;
            // Message length validation: should not exceed 250 characters
            if (msg.length() > 250) {
                JOptionPane.showMessageDialog(null, "Please enter a message of less than 250 characters.");
                continue;
            }

            Message message = new Message(id, recipient, msg);

            String preview = "ID: " + message.getMessageID() +
                    "\nRecipient: " + message.getRecipient() +
                    "\nMessage: " + message.getMessage();

            String[] actions = {"Send", "Disregard", "Store to send later"};

            int action = JOptionPane.showOptionDialog(null,
                    preview + "\nChoose an action:",
                    "Message Preview",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    actions,
                    actions[0]);

            switch (action) {
                case 0 -> { // Send Message
                    sentMessages.add(message); // Populates the sent Message array
                    messageCount++;
                    messageHashes.add(message.createMessageHash()); // Populates the message Hash array
                    messageIDs.add(message.getMessageID()); // Populates the Message ID array
                    showMessageDialog(message);
                    JOptionPane.showMessageDialog(null, "Message sent.");
                }
                case 1 -> { // Disregard Message
                    disregardedMessages.add(message); // Populates the disregarded Messages array
                    JOptionPane.showMessageDialog(null, "Message disregarded.");
                }
                case 2 -> storeMessage(message); // Store Message to send later
                default -> JOptionPane.showMessageDialog(null, "Invalid choice.");
            }
        }

        JOptionPane.showMessageDialog(null, "Total messages sent: " + messageCount);
    }

    static void showMessageDialog(Message msg) {
        JOptionPane.showMessageDialog(null, "MessageID: " + msg.messageID +
                "\nMessage Hash: " + msg.createMessageHash() +
                "\nRecipient: " + msg.recipient +
                "\nMessage: " + msg.messageText);
    }

    // Method to store messages in JSON file
    static void storeMessage(Message msg) {
        JSONObject jsonMsg = new JSONObject();
        jsonMsg.put("MessageID", msg.messageID);
        jsonMsg.put("Recipient", msg.recipient);
        jsonMsg.put("Message", msg.messageText);
        jsonMsg.put("MessageHash", msg.createMessageHash()); // Store the hash

        jsonMessages.add(jsonMsg); // Add to the JSONArray for writing

        try (FileWriter file = new FileWriter("messages.json")) {
            file.write(jsonMessages.toJSONString());
            file.flush(); // Ensures data is written immediately
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Also add to the storedMessages list in memory for current session access
        storedMessages.add(msg); // Populates the Stored Messages array
        messageHashes.add(msg.createMessageHash()); // Populates the Message Hash array
        messageIDs.add(msg.getMessageID()); // Populates the Message ID array
        JOptionPane.showMessageDialog(null, "Message stored.");
    }

    // New method to load stored messages from JSON file
    private static void loadStoredMessages() {
        File file = new File("messages.json");
        if (file.exists() && file.length() > 0) { // Check if the file exists and is not empty
            storedMessages = StoredMessages.readStoredMessagesFromJson("messages.json");
            // Populate jsonMessages with loaded data to prevent overwriting
            for (Message msg : storedMessages) {
                JSONObject jsonMsg = new JSONObject();
                jsonMsg.put("MessageID", msg.messageID);
                jsonMsg.put("Recipient", msg.recipient);
                jsonMsg.put("Message", msg.messageText);
                jsonMsg.put("MessageHash", msg.createMessageHash()); // Re-create hash for consistency
                jsonMessages.add(jsonMsg);
                messageHashes.add(msg.createMessageHash()); // Populate the Message Hash array
                messageIDs.add(msg.getMessageID()); // Populate the Message ID array
            }
        }
    }


    // New method to display messages
    private static void displayMessages() {
        String[] displayOptions = {"1.Display sender and recipient of all sent messages", "2.Display the longest sent message"}; //
        int choice = JOptionPane.showOptionDialog(null,
                "Choose what to display:",
                "Display Messages",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                displayOptions,
                displayOptions[0]);

        switch (choice) {
            case 0 -> displaySenderRecipientOfSentMessages(); // Display the sender and recipient of all sent messages.
            case 1 -> displayLongestSentMessage(); // Display the longest sent message.
            default -> JOptionPane.showMessageDialog(null, "Invalid choice.");
        }
    }

    // Displays the sender and recipient of all sent messages.
    private static void displaySenderRecipientOfSentMessages() {
        if (sentMessages.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No sent messages to display.");
            return;
        }
        StringBuilder sb = new StringBuilder("Sent Messages (Sender/Recipient):\n");
        for (Message msg : sentMessages) {
            sb.append("Sender: You, Recipient: ").append(msg.getRecipient()).append("\n"); // Display the sender and recipient of all sent messages.
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    // Displays the longest sent message.
    private static void displayLongestSentMessage() {
        if (sentMessages.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No sent messages to determine the longest one.");
            return;
        }

        Message longestMessage = null;
        for (Message msg : sentMessages) {
            if (longestMessage == null || msg.getMessage().length() > longestMessage.getMessage().length()) {
                longestMessage = msg;
            }
        }
        List<Message> allMessages = new ArrayList<>();
        allMessages.addAll(sentMessages);
        allMessages.addAll(storedMessages); // Include stored messages for longest message check

        longestMessage = null;
        for (Message msg : allMessages) {
            if (longestMessage == null || msg.getMessage().length() > longestMessage.getMessage().length()) {
                longestMessage = msg;
            }
        }

        if (longestMessage != null) {
            JOptionPane.showMessageDialog(null, "Longest Message:\n" + longestMessage.getMessage()); // Display the longest sent message.
        }
    }


    // New method to search messages
    private static void searchMessages() {
        String[] searchOptions = {"1.Search for a Message ID", "2.Search for messages sent to a particular recipient"}; //
        int choice = JOptionPane.showOptionDialog(null,
                "Choose search option:",
                "Search Messages",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                searchOptions,
                searchOptions[0]);

        switch (choice) {
            case 0 -> searchForMessageID(); // Search for a message ID and display the corresponding recipient and message.
            case 1 -> searchForRecipientMessages(); // Search for all the messages sent to a particular recipient.
            default -> JOptionPane.showMessageDialog(null, "Invalid choice.");
        }
    }

    // Search for a message ID and display the corresponding recipient and message.
    private static void searchForMessageID() {
        String searchID = JOptionPane.showInputDialog("Enter Message ID to search:");
        if (searchID == null) return;

        Message foundMessage = null;
        // Search in sent messages
        for (Message msg : sentMessages) {
            if (msg.getMessageID().equals(searchID)) {
                foundMessage = msg;
                break;
            }
        }
        // If not found in sent, search in stored messages
        if (foundMessage == null) {
            for (Message msg : storedMessages) {
                if (msg.getMessageID().equals(searchID)) {
                    foundMessage = msg;
                    break;
                }
            }
        }

        if (foundMessage != null) {
            JOptionPane.showMessageDialog(null, "Message Found:\n" +
                    "Recipient: " + foundMessage.getRecipient() + "\n" +
                    "Message: " + foundMessage.getMessage()); // Search for a message ID and display the corresponding recipient and message.
        } else {
            JOptionPane.showMessageDialog(null, "Message with ID '" + searchID + "' not found.");
        }
    }

    // Search for all the messages sent to a particular recipient.
    private static void searchForRecipientMessages() {
        String searchRecipient = JOptionPane.showInputDialog("Enter Recipient number (+code) to search:");
        if (searchRecipient == null) return;

        StringBuilder sb = new StringBuilder("Messages for Recipient " + searchRecipient + ":\n");
        boolean found = false;

        // Search in sent messages
        for (Message msg : sentMessages) {
            if (msg.getRecipient().equals(searchRecipient)) {
                sb.append("- ").append(msg.getMessage()).append("\n");
                found = true;
            }
        }

        // Search in stored messages
        for (Message msg : storedMessages) {
            if (msg.getRecipient().equals(searchRecipient)) {
                sb.append("- ").append(msg.getMessage()).append(" (Stored)\n");
                found = true;
            }
        }

        if (found) {
            JOptionPane.showMessageDialog(null, sb.toString()); // Search for all the messages sent to a particular recipient.
        } else {
            JOptionPane.showMessageDialog(null, "No messages found for recipient '" + searchRecipient + "'.");
        }
    }


    // New method to delete a message
    private static void deleteMessage() {
        String messageHashToDelete = JOptionPane.showInputDialog("Enter the Message Hash of the message to delete:");
        if (messageHashToDelete == null) return;

        boolean deleted = false;
        // Try to delete from sentMessages
        for (int i = 0; i < sentMessages.size(); i++) {
            if (sentMessages.get(i).createMessageHash().equalsIgnoreCase(messageHashToDelete)) {
                String deletedMessageText = sentMessages.get(i).getMessage();
                sentMessages.remove(i);
                messageHashes.remove(messageHashToDelete); // Remove from the hashes list
                JOptionPane.showMessageDialog(null, "Message \"" + deletedMessageText + "\" successfully deleted."); // Delete a message using the message hash.
                deleted = true;
                break;
            }
        }

        // If not deleted, try to delete from storedMessages and update JSON
        if (!deleted) {
            for (int i = 0; i < storedMessages.size(); i++) {
                if (storedMessages.get(i).createMessageHash().equalsIgnoreCase(messageHashToDelete)) {
                    String deletedMessageText = storedMessages.get(i).getMessage();
                    storedMessages.remove(i);
                    messageHashes.remove(messageHashToDelete); // Remove from the hashes list

                    // Rebuild jsonMessages JSONArray to reflect deletion
                    jsonMessages.clear();
                    for (Message msg : storedMessages) {
                        JSONObject jsonMsg = new JSONObject();
                        jsonMsg.put("MessageID", msg.messageID);
                        jsonMsg.put("Recipient", msg.recipient);
                        jsonMsg.put("Message", msg.messageText);
                        jsonMsg.put("MessageHash", msg.createMessageHash());
                        jsonMessages.add(jsonMsg);
                    }

                    // Write updated JSON to file
                    try (FileWriter file = new FileWriter("messages.json")) {
                        file.write(jsonMessages.toJSONString());
                        file.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    JOptionPane.showMessageDialog(null, "Message \"" + deletedMessageText + "\" successfully deleted."); // Delete a message using the message hash.
                    deleted = true;
                    break;
                }
            }
        }

        if (!deleted) {
            JOptionPane.showMessageDialog(null, "Message with hash '" + messageHashToDelete + "' not found.");
        }
    }


    // New method to display a report of all sent messages
    private static void displayReport() {
        if (sentMessages.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No sent messages to display in the report.");
            return;
        }

        StringBuilder report = new StringBuilder("--- Sent Messages Report ---\n");
        // Display a report that lists the full details of all the sent messages.
        for (Message msg : sentMessages) {
            report.append("Message Hash: ").append(msg.createMessageHash()).append("\n"); // Message Hash
            report.append("Recipient: ").append(msg.getRecipient()).append("\n"); // Recipient
            report.append("Message: ").append(msg.getMessage()).append("\n"); // Message
            report.append("----------------------------\n");
        }
        JOptionPane.showMessageDialog(null, report.toString(), "Sent Messages Report", JOptionPane.INFORMATION_MESSAGE); // Display Report
    }
}