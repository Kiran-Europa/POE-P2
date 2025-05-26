
public class Message {
    public String messageID; //
    public String recipient; //
    public String messageText; //
    public int sentNumber; // Number of messages sent

    public Message(String ID, String Recipient, String messageText) { // Added messageText to constructor
        this.messageID = ID;
        this.recipient = Recipient;
        this.messageText = messageText; // Initialize messageText
    }
    public String getMessageID() { // getMessageID method should return messageID
        return messageID;
    }

    public String getRecipient() { // getRecipient method should return recipient
        return recipient;
    }

    public String getMessage() { // getMessage method should return messageText
        return messageText;
    }

    public boolean checkMessageID() { // checkMessageID method
        // Ensures that the Message ID is not more than ten characters.
        return messageID.length() <= 10;
    }

    public int checkRecipientCell() { // checkRecipientCell method
        // Ensures that the recipient cell number is no more than 12 characters long and starts with +
        return (recipient.length() <= 12 && recipient.startsWith("+")) ? 1 : 0;
    }

    public String createMessageHash() { // createMessageHash method
        // The system must autogenerate a Message Hash, which contains the first two numbers of the message ID,
        // a colon (:), the number of the message (:), and the first and last words in the message.
        // The has should be displayed in all caps: 00:0:HITHANKS

        // Ensures that messageText is not null or empty to prevent errors
        if (messageText == null || messageText.trim().isEmpty()) {
            return messageID.substring(0, 2) + ":" + QuickChatApp.messageCount + "::";
        }

        String[] words = messageText.split(" ");
        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 1 ? words[words.length - 1] : firstWord; // If only one word, first and last are the same

        // Message Hash format: first two of messageID : messageCount : FIRSTWORDLASTWORD (all caps)
        return messageID.substring(0, 2) + ":" + QuickChatApp.messageCount + ":" + firstWord.toUpperCase() + lastWord.toUpperCase();
    }

    public String SentMessage() { // SentMessage method
        // Allow the user to choose if they want to send, store, or disregard the message.
        // (This logic is handled in QuickChatApp.sendMessages, but should return the messageText)
        return messageText;
    }

    public String printMessages() { // printMessages method
        // This method returns a list of all the messages sent while the program is running.
        // (The implementation in QuickChatApp would iterate a list of Message objects and call this on each)
        return "MessageID: " + messageID + ", Recipient: " + recipient + ", Message: " + messageText;
    }

    public int returnTotalMessages() { // returnTotalMessages method
        // This method returns the total number of messages sent.
        return QuickChatApp.messageCount;
    }

    public void storeMessage() { // storeMessage method
        QuickChatApp.storeMessage(this); // Calls the static method in QuickChatApp
    }
}