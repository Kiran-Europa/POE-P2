import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class QuickChatApp {
    static int messageCount = 0;
    static List<Message> sentMessages = new ArrayList<>();

    static JSONArray jsonMessages = new JSONArray();
    public static void main(String[] args) {
        JOptionPane.showMessageDialog(null, "Welcome to QuickChat."); // Welcome message
        boolean running = true;



        //Start of login/registration
        boolean loggedIn = false;
        while (!loggedIn) {
            String[] options = {"1.Register & Login", "2.Quit"}; //
            int option = JOptionPane.showOptionDialog(null,
                    "Choose an option:",
                    "QuickChat Menu",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]);

            switch (option) {
                case 0 -> { //
                    // Calls the static method in User.java to handle registration and login.
                    // This method should return true if login is successful, false if it's unsuccessful.
                    loggedIn = User.handleUserRegistrationAndLogin();
                    if (!loggedIn) {
                        JOptionPane.showMessageDialog(null, "Login failed or cancelled. Please try again.");
                    }
                }
                case 1, JOptionPane.CLOSED_OPTION -> { //
                    running = false; // Set running to false to exit the application
                    loggedIn = true; // Also set loggedIn to true to exit this loop
                }
                default -> JOptionPane.showMessageDialog(null, "Invalid option. Try again.");
            }
            if (!running) { // If the user chooses to quit from the menu
                JOptionPane.showMessageDialog(null, "Thank you for using QuickChat. Goodbye!");
                return; // Exit the application
            }
        }
        // --- End of login/registration


        // This do-while loop will only execute if `loggedIn` is true
        do {
            String[] options = {"1.Send Messages", "2.Show recently sent messages", "3.Quit"}; //
            int option = JOptionPane.showOptionDialog(null,
                    "Choose an option:",
                    "QuickChat Menu",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]);

            switch (option) {
                case 0 -> sendMessages(); //
                case 1 -> JOptionPane.showMessageDialog(null, "Coming Soon."); //
                case 2, JOptionPane.CLOSED_OPTION -> running = false; //
                default -> JOptionPane.showMessageDialog(null, "Invalid option. Try again.");
            }
        } while (running); // The Application should run until it is quit

        JOptionPane.showMessageDialog(null, "Thank you for using QuickChat. Goodbye!");
    }
    public static void sendMessages() {
        String countStr = JOptionPane.showInputDialog("How many messages would you like to send?"); //
        if (countStr == null) return; // Cancel is clicked or graphic is closed
        int count;
        try {
            count = Integer.parseInt(countStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid number.");
            return;
        }

        for (int i = 0; i < count; i++) { // Allows user to enter only the set number of messages that they entered
            // Unique Message ID: randomly generated ten-digit number
            String id = String.format("%010d", new Random().nextInt(1_000_000_000));

            String recipient = JOptionPane.showInputDialog("Enter recipient number (+code):");
            if (recipient == null) return;
            // Recipient validation: no more than ten characters and starts with '+'
            if (recipient.length() > 10 || !recipient.startsWith("+")) {
                JOptionPane.showMessageDialog(null, "Invalid recipient number. It must start with '+' and be no more than 10 characters long.");
                continue; // Skip to the next message if the recipient number is invalid
            }

            String msg = JOptionPane.showInputDialog("Enter message:");
            if (msg == null) return;
            // Message length validation: should not exceed 250 characters
            if (msg.length() > 250) {
                JOptionPane.showMessageDialog(null, "Please enter a message of less than 250 characters."); // Error message
                continue; // Skip to the next message if the message is too long
            }

            Message message = new Message(id, recipient, msg); // Pass message text to constructor

            String preview = "ID: " + message.getMessageID() +
                    "\nRecipient: " + message.getRecipient() +
                    "\nMessage: " + message.getMessage();

            String[] actions = {"Send", "Disregard", "Store to send later"}; // Options for what will happen to

            // a message
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
                    sentMessages.add(message);
                    messageCount++; // Increment message count
                    showMessageDialog(message); // Display full message details
                    JOptionPane.showMessageDialog(null, "Message sent."); // "Message sent"
                }
                case 2 -> storeMessage(message); // Store Message to send later
                case 1 -> JOptionPane.showMessageDialog(null, "Message disregarded."); // Disregard Message
                default -> JOptionPane.showMessageDialog(null, "Invalid choice.");
            }
        }

        // Total messages sent should be accumulated and displayed once all messages have been sent.
        JOptionPane.showMessageDialog(null, "Total messages sent: " + messageCount);
    }

    static void showMessageDialog(Message msg) {
        // Full details of each message should be displayed (MessageID, Message Hash, Recipient, Message)
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
        jsonMsg.put("MessageHash", msg.createMessageHash());
        jsonMessages.add(jsonMsg);

        try (FileWriter file = new FileWriter("messages.json")) {
            file.write(jsonMessages.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(null, "Message stored.");
    }
}

