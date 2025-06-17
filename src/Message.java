public class Message {
    public String messageID;
    public String recipient;
    public String messageText;

    public Message(String ID, String Recipient, String messageText) {
        this.messageID = ID;
        this.recipient = Recipient;
        this.messageText = messageText;
    }

    public String getMessageID() {
        return messageID;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessage() {
        return messageText;
    }

    public boolean checkMessageID() {
        // Ensures that the Message ID is not more than ten characters.
        return messageID != null && messageID.length() <= 10;
    }

    public int checkRecipientCell() {
        // Ensures that the recipient cell number is no more than 12 characters long and starts with +
        return (recipient != null && recipient.length() <= 12 && recipient.startsWith("+")) ? 1 : 0;
    }

    public String createMessageHash() {
        // Autogenerate a Message Hash, which contains the first two numbers of the message ID,
        // a colon (:), the number of the message (:), and the first and last words in the message.
        // The hash should be displayed in all caps, for example, 00:0:HITHANKS

        // Ensures that messageText is not null or empty to prevent errors
        if (messageText == null || messageText.trim().isEmpty()) {
            return messageID.substring(0, 2) + ":" + QuickChatApp.messageCount + "::"; // Handle empty message text as per test case
        }

        String[] words = messageText.trim().split("\\s+"); // Split by one or more whitespace characters
        String firstWord = "";
        String lastWord = "";

        if (words.length > 0) {
            firstWord = words[0].toUpperCase();
            if (words.length > 1) {
                lastWord = words[words.length - 1].toUpperCase();
            } else {
                lastWord = firstWord; // If only one word is entered, first and last are the same
            }
        }

        return messageID.substring(0, 2) + ":" + QuickChatApp.messageCount + ":" + firstWord + lastWord;
    }

    public String SentMessage() {
        // Allow the user to choose if they want to send, store, or disregard the message.
        return messageText;
    }

    public String printMessages() {
        // This method returns a list of all the messages sent while the program is running.
        return "MessageID: " + messageID + ", Recipient: " + recipient + ", Message: " + messageText;
    }

    public int returnTotalMessages() {
        // This method returns the total number of messages sent.
        return QuickChatApp.messageCount;
    }

    public void storeMessage() {
        QuickChatApp.storeMessage(this); // Calls the static method in QuickChatApp
    }
}