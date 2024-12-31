package Service;
import DAO.*;
import Model.*;
import java.util.*;

public class MessageService {

    private MessageDAO messageDAO;
    private AccountDAO accountDAO;
    
    public MessageService(){
        messageDAO = new MessageDAO();
        accountDAO = new AccountDAO();
    }

    public MessageService(MessageDAO messageDAO, AccountDAO accountDAO){
        this.messageDAO = messageDAO;
        this.accountDAO = accountDAO;
    }
    
    public Message addMessage(Message message) {
        int posted_by = message.getPosted_by();
        String message_text = message.getMessage_text();
        boolean validMessage = (message_text != null &&
                                message_text != "" &&
                                message_text.length() <= 255);
        boolean userExists = false;

        if (validMessage) {
            for (Account accountFromDB : accountDAO.getAllAccounts()) {
                if (posted_by == accountFromDB.getAccount_id()) {
                    userExists = true;
                }
                
            }

            if (userExists) {
                return messageDAO.insertMessage(message);
            }
        }

        return null;
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageByID(int message_id) {
        return messageDAO.getMessageByID(message_id);
    }

    public List<Message> getAllMessageByAccountID(int account_id) {
        List<Message> allMessages = this.getAllMessages();
        List<Message> allMessagesFromAccount = new ArrayList<>();

        if (allMessages != null) {
            for (Message message : allMessages) {
                if (message.getPosted_by() == account_id) {
                    allMessagesFromAccount.add(message);
                }
            }
        }

        return allMessagesFromAccount;
    }

    public Message deleteMessageByID(int message_id) {
        return messageDAO.deleteMessageByID(message_id);
    }

    public Message updateMessage_textbyID(int message_id, String message_text) {

        boolean validMessage = (message_text != null &&
                                message_text != "" &&
                                message_text.length() <= 255);
        
        Message message = messageDAO.getMessageByID(message_id);

        if (message != null && validMessage) {
            return messageDAO.updateMessage_textbyID(message_id, message_text);
        }
        
        return null;
    }

}
