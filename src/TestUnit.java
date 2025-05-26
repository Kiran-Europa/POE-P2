import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestUnit {

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
        Message message = new Message("123", "+2718693002", "Test"); //
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
        Message message = new Message("123", "+27718693002", "Test");
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

        // Simulate QuickChatApp.messageCount being 0 for the first message
        QuickChatApp.messageCount = 0;
        String sampleMessageID = "0012345678"; // Example ID starting with "00"
        Message message = new Message(sampleMessageID, "+27718693002", "Hi Mike, can you join us for dinner tonight "); //

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
        String expectedHash = "05:3::"; // Expecting empty first and last words
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
   //unfinished test unit, do not watch united lose to spurs and finish it
    }
}