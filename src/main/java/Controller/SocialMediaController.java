// NOTE: this file will handle endpoints
// Will gather the body of the request, then send the body of the request to Service.java

package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

// import Service layer class
import Service.Service;
import Model.Account;
import Model.Message;

import java.util.ArrayList;

public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    
    public Service service;

    public SocialMediaController () {
        service = new Service();
    }
    
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        // user registration endpoint
        // url: POST localhost:8080/register
        app.post("register", this::registrationHandler);

        // user login
        // url: POST localhost:8080/login
        app.post("login", this::loginHandler);

        // get all messages
        // url: GET localhost:8080/messages
        app.get("messages", this::getAllMessagesHandler);

        // get one message by id
        // url: GET localhost:8080/accounts/{account_id}/messages
        app.get("messages/{message_id}", this::getMessageByMessageIdHandler);

        // get all message by user id
        // url: GET localhost:8080/messages/{message_id}
        app.get("accounts/{account_id}/messages", this::getAllMessagesByUserIdHandler);

        // message creation
        // url: POST localhost:8080/messages
        app.post("messages", this::messageCreationHandler);

        // message deletion
        // url: DELETE localhost:8080/messages{message_id}
        app.delete("messages/{message_id}", this::messageDeletionHandler);

        // message update
        // url: PATCH localhost:8080/messages{message_id}
        app.patch("messages/{message_id}", this::messageUpdateHandler);


        return app;
    }

    // Account Handlers
    private void registrationHandler(Context ctx)
    {
        String jsonString = ctx.body();
        Account newUser = service.processRegistration(jsonString);
        if (newUser == null)
        {
            ctx.status(400);
        }
        else
        {
            ctx.status(200);
            ctx.json(newUser);
        }
    }
    private void loginHandler(Context ctx)
    {
        String jsonString = ctx.body();
        Account loginUser = service.processLogin(jsonString);
        if (loginUser == null)
        {
            ctx.status(401);
        }
        else
        {
            ctx.status(200);
            ctx.json(loginUser);
        }
    }

    // Message Handlers
    private void getAllMessagesHandler(Context ctx)
    {
        ArrayList<Message> allMessages = service.getAllMessages();
        if (allMessages == null) ctx.json("");
        else ctx.json(allMessages);
        ctx.status(200);

    }

    private void getMessageByMessageIdHandler(Context ctx)
    {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message foundMessage = service.getMessageById(messageId);
        if (foundMessage == null) ctx.json("");
        else ctx.json(foundMessage);
        ctx.status(200);
    }

    private void getAllMessagesByUserIdHandler(Context ctx)
    {
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        ArrayList<Message> allMessagesByUser = service.getAllMessagesByUser(accountId);
        if(allMessagesByUser == null) ctx.json("");
        else ctx.json(allMessagesByUser);
        ctx.status(200);

    }

    private void messageCreationHandler(Context ctx)
    {
        String jsonString = ctx.body();
        Message newMessage = service.processNewMessage(jsonString);
        if (newMessage == null)
        {
            ctx.status(400);
        }
        else
        {
            ctx.status(200);
            ctx.json(newMessage);
        }
    }
    private void messageDeletionHandler(Context ctx)
    {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = service.processDeleteMessage(messageId);
        ctx.status(200);
        if (deletedMessage == null) ctx.json("");
        else ctx.json(deletedMessage);
    }

    private void messageUpdateHandler(Context ctx)
    {
        String jsonString = ctx.body();
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message updatedMessage = service.processUpdateMessage(jsonString, messageId);
        if (updatedMessage == null) ctx.status(400);
        else
        {
            ctx.status(200);
            ctx.json(updatedMessage);
        }
    }
    


}