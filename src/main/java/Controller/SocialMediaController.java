package Controller;

import Model.*;
import Service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.*;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        accountService = new AccountService();
        messageService = new MessageService();
    }
    
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        //app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::postAccountHandler);
        app.post("/login", this::loginAccountHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIDHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIDHandler);
        app.patch("/messages/{message_id}", this::updateMessageByIDHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByAccountIDHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    //private void exampleHandler(Context context) {
    //    context.json("sample text");
    //}

    private void postAccountHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        
        Account addedAccount = accountService.addAccount(account);

        if (addedAccount != null) {
            context.json(mapper.writeValueAsString(addedAccount));
        }
        else {
            context.status(400);
        }
    }

    private void loginAccountHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        
        boolean accountFound = false;

        for (Account existingAccount : accountService.getAllAccounts()) {
            if ((account.getUsername().equals(existingAccount.getUsername()) && 
                account.getPassword().equals(existingAccount.getPassword()))) {
                    accountFound = true;
                    context.json(mapper.writeValueAsString(existingAccount));
                }
        }
        if (!accountFound) {
        context.status(401);
        }
    }

     private void postMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        
        Message addedMessage = messageService.addMessage(message);

        if (addedMessage != null) {
            context.json(mapper.writeValueAsString(addedMessage));
        }
        else {
            context.status(400);
        }

    }

    private void getAllMessagesHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Message> messages = messageService.getAllMessages();
        context.json(mapper.writeValueAsString(messages));
    }

    private void getMessageByIDHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = messageService.getMessageByID(Integer.parseInt(context.pathParam("message_id")));

        if(message != null) {
            context.json(mapper.writeValueAsString(message));
        }
    }
    
    private void deleteMessageByIDHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = messageService.deleteMessageByID(Integer.parseInt(context.pathParam("message_id")));
        
        if(message != null) {
            context.json(mapper.writeValueAsString(message));
        }
    }

    private void updateMessageByIDHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);

        String message_text = message.getMessage_text();
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        
        Message updatedMessage = messageService.updateMessage_textbyID(message_id, message_text);

        if (updatedMessage != null) {
            context.json(mapper.writeValueAsString(updatedMessage));
        }
        else {
            context.status(400);
        }

    }

    private void getAllMessagesByAccountIDHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int account_id = Integer.parseInt(context.pathParam("account_id"));

        List<Message> messages = messageService.getAllMessageByAccountID(account_id);
        
        context.json(mapper.writeValueAsString(messages));
    }

}