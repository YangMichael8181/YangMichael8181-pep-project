// This file will process information
// Will be given the body of endpoints, turn into objects, call the DAO to check for validity and such
package Service;


import com.fasterxml.jackson.core.JsonProcessingException;
// process json strings --> Java objects
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import DAO.DAO;


public class Service {
    ObjectMapper om;
    DAO dao;

    public Service () {
        om = new ObjectMapper();
        dao = new DAO();
    }

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
}