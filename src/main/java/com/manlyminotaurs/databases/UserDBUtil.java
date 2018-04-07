package com.manlyminotaurs.databases;

import com.manlyminotaurs.users.Patient;
import com.manlyminotaurs.users.User;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDBUtil {
    DataModelI dataModelI = DataModelI.getInstance();
    /*------------------------------------ Add / Remove / Modify User -------------------------------------------------*/
    User addUser(String userID, String firstName, String middleName, String lastName, String language){
        return null;
    }
    boolean removeUser(User oldUser){
        return false;
    }
    boolean modifyUser(String stuff){
        return false;
    }
    /*------------------------------------ List all users / find a user -------------------------------------------------*/
    /**
     *
     *  get data from UserAccount table in database and put them into the list of request objects
     */
    public List<User> retrieveUsers() {
        // Connection
        Connection connection = dataModelI.getNewConnection();

        // Variables
        User userObject;
        String userID;
        String firstName;
        String middleInitial;
        String lastName;
        String language;
        List<User> listOfUsers = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            String str = "SELECT * FROM UserAccount";
            ResultSet rset = stmt.executeQuery(str);

            while (rset.next()) {
                userID = rset.getString("userID");
                firstName = rset.getString("firstName");
                middleInitial = rset.getString("middleInitial");
                lastName = rset.getString("lastName");
                language = rset.getString("language");

                // Add the new edge to the list
                userObject = new Patient(userID, firstName, middleInitial, lastName, language);
                listOfUsers.add(userObject);
                System.out.println("User added to the list: " + userID);
            }
            rset.close();
            stmt.close();
            System.out.println("Done adding users");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataModelI.closeConnection(connection);
        }

        return listOfUsers;
    } // retrieveUsers() ends

    User getUserByID(String ID){
        return null;
    }
}
