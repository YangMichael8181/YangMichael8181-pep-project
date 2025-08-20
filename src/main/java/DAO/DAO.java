package DAO;

// ConnectionUtil import
import Util.ConnectionUtil;

// class imports
import Model.Account;
import Model.Message;

// Necessary imports to connect to database
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;

import org.h2.jdbcx.JdbcDataSource;

// Accesses the Database
// TODOS:
//  initialize a connection to the database
//  take in certain values, to either ensure the values do not already exist
//  or to check if credentials are valid

public class DAO {
    private Connection con;

    public DAO() {
        this.con = ConnectionUtil.getConnection();
    }

    // executes the registration query
    // handles all error checking
    // checks if username is blank
    // checks if password is at least 4 characters long
    // checks if user already exists
    // if any error occurs, returns false
    // if user does not exist (returning ResultSet is empty) then executes insertion query, returns true
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
}