// This file will process information
// Will be given the body of endpoints, turn into objects, call the DAO to check for validity and such
package Service;


import com.fasterxml.jackson.core.JsonProcessingException;
// process json strings --> Java objects
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import DAO.DAO;

import java.util.ArrayList;

public class Service {
    ObjectMapper om;
    DAO dao;

    public Service () {
        om = new ObjectMapper();
        dao = new DAO();
    }
    // Account business logic
    public Account processRegistration(String jsonString)
    {
        try
        {
            Account newUser = om.readValue(jsonString, Account.class);
            return dao.executeRegistrationQuery(newUser);
        }
        catch (JsonProcessingException e)
        {
            System.out.println(e.getMessage());
        }

        return null;
    }
    public Account processLogin(String jsonString)
    {
        try
        {
            Account loginUser = om.readValue(jsonString, Account.class);
            return dao.executeLoginQuery(loginUser);
        }
        catch (JsonProcessingException e)
        {
            System.out.println(e.getMessage());
        }

        return null;
    }

    //Message business logic
    public ArrayList<Message> getAllMessages()
    {
        return dao.getAllMessagesQuery();
    }

    public Message getMessageById(int messageId)
    {
        return dao.getMessageByIdQuery(messageId);
    }

    public ArrayList<Message> getAllMessagesByUser(int accountId)
    {
        return dao.getAllMessagesByAccountIdQuery(accountId);
    }
    
    public Message processNewMessage(String jsonString)
    {
        try
        {
            Message newMessage = om.readValue(jsonString, Message.class);
            return dao.executeNewMessageQuery(newMessage);
        }
        catch (JsonProcessingException e)
        {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public Message processDeleteMessage(int messageId)
    {
        return dao.executeDeleteMessageQuery(messageId);
    }

    public Message processUpdateMessage(String jsonString, int messageId)
    {
        try
        {
            Message updatedMessage = om.readValue(jsonString, Message.class);
            updatedMessage.setMessage_id(messageId);
            return dao.executeUpdateMessageQuery(updatedMessage);
        }
        catch (JsonProcessingException e)
        {
            System.out.println(e.getMessage());
        }

        return null;
    }
}