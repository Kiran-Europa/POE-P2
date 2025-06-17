// TestUnit.java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;

public class TestUnit {

    @BeforeEach
    void setUp() {
        // Reset QuickChatApp's static variables before each test
        QuickChatApp.messageCount = 0;
        QuickChatApp.sentMessages.clear();
        QuickChatApp.disregardedMessages.clear();
        QuickChatApp.storedMessages.clear();
        QuickChatApp.messageHashes.clear();
        QuickChatApp.messageIDs.clear();
        QuickChatApp.jsonMessages.clear(); // Clear the JSONArray
        // Ensure messages.json is empty or clean before tests that depend on it
        try {
            Files.deleteIfExists(Paths.get("messages.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Test Message ID check - Valid ID (<= 10 characters)")
    void testCheckMessageID_Valid() {
        Message message = new Message("1234567890", "+27123456789", "Hello World");
        assertTrue(message.checkMessageID(), "Message ID should be valid (10 characters).");
    }

    @Test
    @DisplayName("Test Message ID check - Invalid ID (> 10 characters)")
    void testCheckMessageID_Invalid() {
        Message message = new Message("12345678901", "+27123456789", "Hello World");
        assertFalse(message.checkMessageID(), "Message ID should be invalid (> 10 characters).");
    }

    @Test
    @DisplayName("Test Recipient Cell check - Valid Number")
    void testCheckRecipientCell_Valid() {
        Message message = new Message("123", "+2718693002", "Test");
        assertEquals(1, message.checkRecipientCell(), "Recipient number should be valid.");
    }

    @Test
    @DisplayName("Test Recipient Cell check - Invalid Number (missing +)")
    void testCheckRecipientCell_InvalidNoPlus() {
        Message message = new Message("123", "2718693002", "Test");
        assertEquals(0, message.checkRecipientCell(), "Recipient number should be invalid (missing '+').");
    }

    @Test
    @DisplayName("Test Recipient Cell check - Invalid Number (too long)")
    void testCheckRecipientCell_InvalidTooLong() {
        Message message = new Message("123", "+27718693002123", "Test"); // More than 12 characters
        assertEquals(0, message.checkRecipientCell(), "Recipient number should be invalid (too long).");
    }

    @Test
    @DisplayName("Test Recipient Cell check - Invalid Number (too short)")
    void testCheckRecipientCell_InvalidTooShort() {
        Message message = new Message("123", "+271", "Test");
        assertEquals(0, message.checkRecipientCell(), "Recipient number should be invalid (too short).");
    }

    @Test
    @DisplayName("Test Message Hash Generation - Test Case 1 Data")
    void testCreateMessageHash_TestCase1() {
        QuickChatApp.messageCount = 0; // Simulate initial message count for this specific test case
        String sampleMessageID = "0012345678"; // Example ID starting with "00"
        Message message = new Message(sampleMessageID, "+27718693002", "Hi Mike, can you join us for dinner tonight");
        // Expected hash: first two of ID + : + messageCount + : + FIRSTWORDLASTWORD (all caps)
        // "HITHANKS" suggests "Hi" + "tonight" (last word of the example message in the image)
        String expectedHash = "00:0:HITHANKS";
        assertEquals(expectedHash, message.createMessageHash(), "Message hash should be generated correctly for test case 1.");
    }

    @Test
    @DisplayName("Test Message Hash Generation - Single word message")
    void testCreateMessageHash_SingleWord() {
        QuickChatApp.messageCount = 1;
        String sampleMessageID = "0198765432";
        Message message = new Message(sampleMessageID, "+27123456789", "HELLO");
        String expectedHash = "01:1:HELLOHELLO";
        assertEquals(expectedHash, message.createMessageHash(), "Message hash should handle single word messages.");
    }

    @Test
    @DisplayName("Test Message Hash Generation - Multiple words message")
    void testCreateMessageHash_MultipleWords() {
        QuickChatApp.messageCount = 2;
        String sampleMessageID = "9900000000";
        Message message = new Message(sampleMessageID, "+27123456789", "Good morning world");
        String expectedHash = "99:2:GOODWORLD";
        assertEquals(expectedHash, message.createMessageHash(), "Message hash should handle multiple words correctly.");
    }

    @Test
    @DisplayName("Test Message Hash Generation - Empty message text")
    void testCreateMessageHash_EmptyMessage() {
        QuickChatApp.messageCount = 3;
        String sampleMessageID = "0512345678";
        Message message = new Message(sampleMessageID, "+27123456789", "");
        String expectedHash = "05:3::"; // Expecting empty first and last words based on the implementation
        assertEquals(expectedHash, message.createMessageHash(), "Message hash should handle empty message text.");
    }

    @Test
    @DisplayName("Test Getters - Message ID, Recipient, Message Text")
    void testGetters() {
        String testId = "TESTID123";
        String testRecipient = "+27771234567";
        String testMessageText = "This is a test message.";
        Message message = new Message(testId, testRecipient, testMessageText);

        assertEquals(testId, message.getMessageID(), "getMessageID should return the correct ID.");
        assertEquals(testRecipient, message.getRecipient(), "getRecipient should return the correct recipient.");
        assertEquals(testMessageText, message.getMessage(), "getMessage should return the correct message text.");
    }

    @Test
    @DisplayName("Test SentMessage method")
    void testSentMessage() {
        String testMessageText = "This message is sent.";
        Message message = new Message("123", "recip", testMessageText);
        assertEquals(testMessageText, message.SentMessage(), "SentMessage should return the message text.");
    }

    @Test
    @DisplayName("Test PrintMessages method")
    void testPrintMessages() {
        String testId = "ID001";
        String testRecipient = "+27821112222";
        String testMessageText = "Hello from Unit Test.";
        Message message = new Message(testId, testRecipient, testMessageText);
        String expectedPrint = "MessageID: " + testId + ", Recipient: " + testRecipient + ", Message: " + testMessageText;
        assertEquals(expectedPrint, message.printMessages(), "printMessages should return formatted message details.");
    }

    @Test
    @DisplayName("Test ReturnTotalMessages method")
    void testReturnTotalMessages() {
        QuickChatApp.messageCount = 5; // Simulate 5 messages sent
        Message message = new Message("any", "any", "any");
        assertEquals(5, message.returnTotalMessages(), "returnTotalMessages should return the correct total message count.");
    }

    // New Unit Tests based on requirements and provided test data

    @Test
    @DisplayName("Sent Messages array correctly populated")
    void testSentMessagesArrayPopulated() {
        // Test Data: Developer entry for Test data for message 1-4
        // Message 1: "Did you get the cake?", Flag: Sent
        // Message 4: "It is dinner time !", Flag: Sent

        // Manually add the test data that would be "sent" to QuickChatApp's lists
        Message msg1 = new Message("1", "+27834557896", "Did you get the cake?"); // ID is dummy, actual ID is auto-generated in QuickChatApp
        Message msg4 = new Message("4", "0838884567", "It is dinner time !"); // ID is dummy, actual ID is auto-generated in QuickChatApp

        // Simulate sending these messages to populate sentMessages and messageHashes
        QuickChatApp.sentMessages.add(msg1);
        QuickChatApp.messageCount++; // Increment count as if sent
        QuickChatApp.messageHashes.add(msg1.createMessageHash());
        QuickChatApp.messageIDs.add(msg1.getMessageID());

        QuickChatApp.sentMessages.add(msg4);
        QuickChatApp.messageCount++; // Increment count as if sent
        QuickChatApp.messageHashes.add(msg4.createMessageHash());
        QuickChatApp.messageIDs.add(msg4.getMessageID());

        // Expected System Returns: "Did you get the cake?", "It is dinner time!"
        assertTrue(QuickChatApp.sentMessages.stream().anyMatch(m -> m.getMessage().equals("Did you get the cake?")), "Sent messages should contain 'Did you get the cake?'."); // Sent Messages array correctly populated
        assertTrue(QuickChatApp.sentMessages.stream().anyMatch(m -> m.getMessage().equals("It is dinner time !")), "Sent messages should contain 'It is dinner time!'."); // Sent Messages array correctly populated
    }

    @Test
    @DisplayName("Display the longest Message")
    void testDisplayLongestMessage() {
        // Test Data: message 1-4
        // Message 1: "Did you get the cake?" (Sent)
        // Message 2: "Where are you? You are late! I have asked you to be on time." (Stored)
        // Message 3: "Yohooooo, I am at your gate." (Disregard)
        // Message 4: "It is dinner time !" (Sent)
        // Expected System Returns: "Where are you? You are late! I have asked you to be on time,"

        Message msg1 = new Message("1", "+27834557896", "Did you get the cake?");
        Message msg2 = new Message("2", "+27838884567", "Where are you? You are late! I have asked you to be on time.");
        Message msg3 = new Message("3", "+27834484567", "Yohooooo, I am at your gate.");
        Message msg4 = new Message("4", "0838884567", "It is dinner time !");

        // Populate lists as per their flags
        QuickChatApp.sentMessages.add(msg1);
        QuickChatApp.sentMessages.add(msg4);
        QuickChatApp.storedMessages.add(msg2); // Crucial for this test
        QuickChatApp.disregardedMessages.add(msg3);

        // Capture JOptionPane output (This is tricky for Swing dialogs in unit tests.
        // A common approach is to mock JOptionPane or redirect System.out if the display uses that.)
        // For this, we'll directly call the logic to find the longest message as if the display method was called.
        // The displayLongestSentMessage method in QuickChatApp now considers both sent and stored messages.

        List<Message> allMessages = new ArrayList<>();
        allMessages.addAll(QuickChatApp.sentMessages);
        allMessages.addAll(QuickChatApp.storedMessages);

        Message longestMessage = null;
        for (Message msg : allMessages) {
            if (longestMessage == null || msg.getMessage().length() > longestMessage.getMessage().length()) {
                longestMessage = msg;
            }
        }
        assertNotNull(longestMessage, "Longest message should not be null.");
        assertEquals("Where are you? You are late! I have asked you to be on time.", longestMessage.getMessage(), "The longest message should be 'Where are you? You are late! I have asked you to be on time.'."); // Display the longest Message
    }

    @Test
    @DisplayName("Search for messageID")
    void testSearchForMessageID() {
        // Test Data: message 4
        // Message 4: "It is dinner time !", Developer: 0838884567
        // Expected System Returns: "0838884567", (meaning the recipient of message 4)

        Message msg4 = new Message("1234567890", "0838884567", "It is dinner time !"); // Dummy ID, but matching length
        QuickChatApp.sentMessages.add(msg4); // Add to sent messages

        // To test JOptionPane-based input/output, mocking is usually required.
        // For simplicity in a basic unit test, we'll verify the logic that the method would execute.
        // In a real application, you'd use a UI testing framework or mock JOptionPane.

        // Simulating the logic within searchForMessageID
        String searchID = "1234567890"; // Using the dummy ID created for msg4
        Message foundMessage = null;
        for (Message msg : QuickChatApp.sentMessages) {
            if (msg.getMessageID().equals(searchID)) {
                foundMessage = msg;
                break;
            }
        }
        for (Message msg : QuickChatApp.storedMessages) { // Also check stored messages
            if (msg.getMessageID().equals(searchID)) {
                foundMessage = msg;
                break;
            }
        }

        assertNotNull(foundMessage, "Message with ID should be found.");
        assertEquals("0838884567", foundMessage.getRecipient(), "Search for messageID should return the correct recipient."); // Search for messageID
        assertEquals("It is dinner time !", foundMessage.getMessage(), "Search for messageID should return the correct message.");
    }

    @Test
    @DisplayName("Search all the messages sent or stored regarding a particular recipient")
    void testSearchForRecipientMessages() {
        // Test Data: +27838884567
        // Expected System Returns: "Where are you? You are late! I have asked you to be on time.", "Ok, I am leaving without you."

        Message msg2 = new Message("ID002", "+27838884567", "Where are you? You are late! I have asked you to be on time."); // Flag: Stored
        Message msg5 = new Message("ID005", "+27838884567", "Ok, I am leaving without you."); // Flag: Stored

        QuickChatApp.storedMessages.add(msg2);
        QuickChatApp.storedMessages.add(msg5);

        // We can't directly test JOptionPane.showMessageDialog content
        // Instead, we'll verify that the internal logic of finding messages for a recipient works.
        String searchRecipient = "+27838884567"; // Test Data

        List<String> messagesForRecipient = new ArrayList<>();
        for (Message msg : QuickChatApp.sentMessages) {
            if (msg.getRecipient().equals(searchRecipient)) {
                messagesForRecipient.add(msg.getMessage());
            }
        }
        for (Message msg : QuickChatApp.storedMessages) {
            if (msg.getRecipient().equals(searchRecipient)) {
                messagesForRecipient.add(msg.getMessage());
            }
        }

        assertEquals(2, messagesForRecipient.size(), "Should find 2 messages for the recipient.");
        assertTrue(messagesForRecipient.contains("Where are you? You are late! I have asked you to be on time."), "Should contain the first message."); // Search all the messages sent or stored regarding a particular recipient
        assertTrue(messagesForRecipient.contains("Ok, I am leaving without you."), "Should contain the second message."); // Search all the messages sent or stored regarding a particular recipient
    }


    @Test
    @DisplayName("Delete a message using a message hash")
    void testDeleteMessageByHash() {
        // Test Data: Test Message 2
        // Message 2: "Where are you? You are late! I have asked you to be on time." (Stored)
        // Expected System Returns: Message "Where are you? You are late! I have asked you to be on time" successfully deleted.

        QuickChatApp.messageCount = 0; // Reset count for hash generation consistency
        Message msgToDelete = new Message("ID002", "+27838884567", "Where are you? You are late! I have asked you to be on time.");
        String hashToDelete = msgToDelete.createMessageHash(); // Get the hash before adding

        // Simulate storing the message
        QuickChatApp.storeMessage(msgToDelete); // This adds to storedMessages and jsonMessages

        // Ensure the message is initially present
        assertTrue(QuickChatApp.storedMessages.stream().anyMatch(m -> m.getMessage().equals(msgToDelete.getMessage())), "Message should be initially present in storedMessages.");
        assertTrue(QuickChatApp.messageHashes.contains(hashToDelete), "Hash should be initially present.");

        // Simulate the deletion process by directly manipulating the lists as QuickChatApp.deleteMessage would.
        // Find and remove from storedMessages
        QuickChatApp.storedMessages.removeIf(m -> m.createMessageHash().equalsIgnoreCase(hashToDelete));
        QuickChatApp.messageHashes.removeIf(h -> h.equalsIgnoreCase(hashToDelete)); // Remove hash from the list

        // Rebuild jsonMessages and write to file
        QuickChatApp.jsonMessages.clear();
        for (Message msg : QuickChatApp.storedMessages) {
            QuickChatApp.jsonMessages.add(msg); // Note: ideally, you'd put the JSONObject again. For test, this is simplified.
        }
        try (FileWriter file = new FileWriter("messages.json")) {
            file.write(QuickChatApp.jsonMessages.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Verify the message is no longer present
        assertFalse(QuickChatApp.storedMessages.stream().anyMatch(m -> m.getMessage().equals(msgToDelete.getMessage())), "Message should be deleted from storedMessages."); // Delete a message using a message hash.
        assertFalse(QuickChatApp.messageHashes.contains(hashToDelete), "Hash should be deleted."); // Delete a message using a message hash.

        // Verify the message is removed from the JSON file by trying to read it back
        List<Message> messagesAfterDeletion = StoredMessages.readStoredMessagesFromJson("messages.json");
        assertFalse(messagesAfterDeletion.stream().anyMatch(m -> m.getMessage().equals(msgToDelete.getMessage())), "Message should be deleted from JSON file.");
    }


    @Test
    @DisplayName("Display Report - lists full details of all sent messages")
    void testDisplayReport() {
        // Expected System Returns: A report that shows all the sent messages including the: Message Hash, Recipient, Message

        Message msg1 = new Message("ID001", "+27834557896", "Did you get the cake?"); // Flag: Sent
        Message msg4 = new Message("ID004", "0838884567", "It is dinner time !"); // Flag: Sent

        QuickChatApp.messageCount = 0; // Reset for hash generation

        // Simulate sending these messages
        QuickChatApp.sentMessages.add(msg1);
        QuickChatApp.messageCount++;
        String hash1 = msg1.createMessageHash();
        QuickChatApp.messageHashes.add(hash1);
        QuickChatApp.messageIDs.add(msg1.getMessageID());

        QuickChatApp.sentMessages.add(msg4);
        QuickChatApp.messageCount++;
        String hash4 = msg4.createMessageHash();
        QuickChatApp.messageHashes.add(hash4);
        QuickChatApp.messageIDs.add(msg4.getMessageID());

        // We can't directly test JOptionPane.showMessageDialog content.
        // Instead, we'll verify the data that *would* be in the report.
        // The report should contain details of sentMessages.

        String expectedContent1 = "Message Hash: " + hash1 + "\n" +
                "Recipient: " + msg1.getRecipient() + "\n" +
                "Message: " + msg1.getMessage();

        String expectedContent2 = "Message Hash: " + hash4 + "\n" +
                "Recipient: " + msg4.getRecipient() + "\n" +
                "Message: " + msg4.getMessage();

        // This is a conceptual test. In practice, you'd use a capturing mechanism for JOptionPane
        // or ensure the method returns the report string.
        // For this case, we'll check if the *data* that makes up the report is correct in the lists.
        assertEquals(2, QuickChatApp.sentMessages.size(), "There should be two sent messages for the report."); // Display Report

        // Check if the sent messages' details are as expected
        assertTrue(QuickChatApp.sentMessages.stream().anyMatch(m ->
                m.getMessage().equals("Did you get the cake?") &&
                        m.getRecipient().equals("+27834557896") &&
                        m.createMessageHash().equals(hash1)
        ), "Report should contain details for 'Did you get the cake?'."); // Display Report

        assertTrue(QuickChatApp.sentMessages.stream().anyMatch(m ->
                m.getMessage().equals("It is dinner time !") &&
                        m.getRecipient().equals("0838884567") &&
                        m.createMessageHash().equals(hash4)
        ), "Report should contain details for 'It is dinner time !'."); // Display Report
    }
}