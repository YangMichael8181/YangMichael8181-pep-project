package DAO;

// ConnectionUtil import
import Util.ConnectionUtil;

// class imports
import Model.Account;
import Model.Message;

// Necessary imports to connect to database
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;

import java.util.ArrayList;

// Table structure:

/*
Account Table:
account_id integer primary key auto_increment,
username varchar(255) unique,
password varchar(255)
*/

/*
Message Table:
message_id integer primary key auto_increment,
posted_by integer,
message_text varchar(255),
time_posted_epoch long,
foreign key (posted_by) references Account(account_id)
*/

public class DAO {
    private Connection con;

    public DAO() {
        this.con = ConnectionUtil.getConnection();
    }
    // Account queries
    // executes the registration query
    public Account executeRegistrationQuery(Account newUser)
    {
        // error checking
        String username = newUser.getUsername();
        String password = newUser.getPassword();
        if (username == "") return null;
        if (password.length() < 4) return null;

        try
        {
            // checks to see if user already exists
            PreparedStatement check = this.con.prepareStatement("SELECT * FROM Account WHERE username = ? OR password = ?");
            check.setString(1, username);
            check.setString(2, password);
            if (!(check.executeQuery().next())) return null;

            // all error checking passed, insert new user into database and return true
            PreparedStatement preparedStatement = this.con.prepareStatement("INSERT INTO Account(username, password) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.executeUpdate();
            ResultSet res = preparedStatement.getGeneratedKeys();

            if (res.next())
            {
                int newAccountId = res.getInt(1);
                return new Account(newAccountId, username, password);
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return null;
    }

    // executes the login query
    public Account executeLoginQuery(Account loginUser)
    {
        String username = loginUser.getUsername();
        String password = loginUser.getPassword();

        try
        {
            // checks to see if user already exists
            PreparedStatement preparedStatement = this.con.prepareStatement("SELECT * FROM Account WHERE username = ? AND password = ?");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet res = preparedStatement.executeQuery();

            if (res.next())
            {

                return new Account(res.getInt(1), username, password);
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return null;
    }


    // Message queries

    public ArrayList<Message> getAllMessagesQuery()
    {
        ArrayList<Message> allMessages = new ArrayList<Message>();
        try
        {
            ResultSet res = this.con.prepareStatement("SELECT * FROM Message").executeQuery();

            while (res.next())
            {
                allMessages.add(new Message(res.getInt(1), res.getInt(2), res.getString(3), res.getLong(4)));
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return allMessages;
    }


    public Message getMessageByIdQuery(int messageId)
    {
        try
        {
            PreparedStatement preparedStatement = this.con.prepareStatement("SELECT * FROM Message WHERE message_id = ?");
            preparedStatement.setInt(1, messageId);
            ResultSet res = preparedStatement.executeQuery();

            if (res.next())
            {
                return new Message(messageId, res.getInt(2), res.getString(3), res.getLong(4));
            }

        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public ArrayList<Message> getAllMessagesByAccountIdQuery(int accountId)
    {
        ArrayList<Message> allMessagesByUser = new ArrayList<Message>();
        try
        {
            PreparedStatement preparedStatement = this.con.prepareStatement("SELECT * FROM Message WHERE posted_by = ?");
            preparedStatement.setInt(1, accountId);
            ResultSet res = preparedStatement.executeQuery();

            while (res.next())
            {
                allMessagesByUser.add(new Message(res.getInt(1), accountId, res.getString(3), res.getLong(4)));
            }

        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return allMessagesByUser;
    }

    // executes new message query
    public Message executeNewMessageQuery(Message newMessage)
    {
        // error checking
        int postUserId = newMessage.getPosted_by();
        String messageText = newMessage.getMessage_text();
        long timePostedEpoch = newMessage.getTime_posted_epoch();
        if (messageText.length() == 0) return null;
        if (messageText.length() >= 255) return null;

        try
        {
            // checks to see if postUserId exists
            PreparedStatement check = this.con.prepareStatement("SELECT * FROM Account WHERE account_id = ?");
            check.setInt(1, postUserId);
            if (!(check.executeQuery().next())) return null;

            // all error checking passed, insert new user into database and return true
            PreparedStatement preparedStatement = this.con.prepareStatement("INSERT INTO Message(posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, postUserId);
            preparedStatement.setString(2, messageText);
            preparedStatement.setLong(3, timePostedEpoch);
            preparedStatement.executeUpdate();
            ResultSet res = preparedStatement.getGeneratedKeys();

            if (res.next())
            {
                int newMessageId = res.getInt(1);
                return new Message(newMessageId, postUserId, messageText, timePostedEpoch);
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return null;
    }
    
    // executes delete message query
    public Message executeDeleteMessageQuery(int messageId)
    {
        try
        {
            // checks to see if message exists, to return deleted message
            PreparedStatement preparedStatement = this.con.prepareStatement("SELECT * FROM Message WHERE message_id = ?");
            preparedStatement.setInt(1, messageId);
            ResultSet res = preparedStatement.executeQuery();

            if (res.next())
            {
                Message deletedMessage = new Message(res.getInt(1), res.getInt(2), res.getString(3), res.getLong(4));
                preparedStatement = this.con.prepareStatement("DELETE FROM Message WHERE message_id = ?");
                preparedStatement.setInt(1, messageId);
                preparedStatement.executeUpdate();
                return deletedMessage;
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return null;
    }

    // executes update message query
    public Message executeUpdateMessageQuery(Message updatedMessage)
    {
        String messageText = updatedMessage.getMessage_text();
        int messageId = updatedMessage.getMessage_id();
        if (messageText.length() == 0) return null;
        if (messageText.length() >= 255) return null;
        try
        {
            // checks to see if message exists, to return deleted message
            PreparedStatement preparedStatement = this.con.prepareStatement("SELECT * FROM Message WHERE message_id = ?");
            preparedStatement.setInt(1, messageId);
            ResultSet res = preparedStatement.executeQuery();

            if (res.next())
            {
                updatedMessage.setPosted_by(res.getInt(2));
                updatedMessage.setTime_posted_epoch(res.getLong(4));
                preparedStatement = this.con.prepareStatement("UPDATE Message SET message_text = ? WHERE message_id = ?");
                preparedStatement.setString(1, messageText);
                preparedStatement.setInt(2, messageId);
                preparedStatement.executeUpdate();
                return updatedMessage;
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return null;
    }
}