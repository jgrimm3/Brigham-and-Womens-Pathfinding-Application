package com.manlyminotaurs.databases;

import com.manlyminotaurs.users.*;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDBUtil {
    DataModelI dataModelI = DataModelI.getInstance();
    /*------------------------------------ Add / Remove / Modify User -------------------------------------------------*/
    User addUser(String userID, String firstName, String middleName, String lastName, String language, String userType){
        User userObject = new Patient(userID,firstName,middleName,lastName,language, userType);
        Connection connection = DataModelI.getInstance().getNewConnection();
        try {
            connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            String str = "INSERT INTO UserAccount(userID,firstName,middleName,lastName,language,userType) VALUES (?,?,?,?,?,?)";

            // Create the prepared statement
            PreparedStatement statement = connection.prepareStatement(str);
            statement.setString(1, userObject.getUserID());
            statement.setString(2, userObject.getFirstName());
            statement.setString(3, userObject.getMiddleName());
            statement.setString(4, userObject.getLastName());
            statement.setString(5, userObject.getLanguage());
            statement.setString(6, userObject.getUserType());
            System.out.println("Prepared statement created...");
            statement.executeUpdate();
            System.out.println("User added to database");
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return userObject;
    }
    boolean removeUser(User oldUser){
        boolean isSuccess = false;
        Connection connection = DataModelI.getInstance().getNewConnection();
        try {
            Statement stmt = connection.createStatement();
            String str = "DELETE FROM UserAccount WHERE userID = '" + oldUser.getUserID() + "'";
            stmt.executeUpdate(str);
            stmt.close();
            System.out.println("Node removed from database");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataModelI.closeConnection(connection);
        }
        return isSuccess;
    }

    public boolean modifyUser(User newUser) {
        Connection connection = DataModelI.getInstance().getNewConnection();
        boolean isSuccess = false;
        try {
            String str = "UPDATE UserAccount SET firstName = ?,middleName = ?,lastName = ?,language = ?,userType =? WHERE userID = '"+ newUser.getUserID() +"'" ;

            // Create the prepared statement
            PreparedStatement statement = connection.prepareStatement(str);
            statement.setString(1, newUser.getFirstName());
            statement.setString(2, newUser.getMiddleName());
            statement.setString(3, newUser.getLastName());
            statement.setString(4, newUser.getLanguage());
            statement.setString(5, newUser.getUserType());
            statement.executeUpdate();
            System.out.println("User added to database");
            isSuccess = true;
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally {
            dataModelI.closeConnection(connection);
        }
        return isSuccess;
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
        String middleName;
        String lastName;
        String language;
        String userType;
        List<User> listOfUsers = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            String str = "SELECT * FROM UserAccount";
            ResultSet rset = stmt.executeQuery(str);

            while (rset.next()) {
                userID = rset.getString("userID");
                firstName = rset.getString("firstName");
                middleName = rset.getString("middleName");
                lastName = rset.getString("lastName");
                language = rset.getString("language");
                userType = rset.getString("userType");

                // Add the new edge to the list
                userObject = userBuilder(userID, firstName, middleName, lastName, language, userType);
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

    User getUserByID(String userID){
        // Connection
        Connection connection = dataModelI.getNewConnection();

        // Variables
        User userObject = null;
        String firstName;
        String middleName;
        String lastName;
        String language;
        String userType;

        try {
            Statement stmt = connection.createStatement();
            String str = "SELECT * FROM UserAccount WHERE userID = '" + userID + "'";
            ResultSet rset = stmt.executeQuery(str);

            if (rset.next()) {
                firstName = rset.getString("firstName");
                middleName = rset.getString("middleName");
                lastName = rset.getString("lastName");
                language = rset.getString("language");
                userType = rset.getString("userType");

                // Add the new edge to the list
                userObject = userBuilder(userID, firstName, middleName, lastName, language, userType);
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
        return userObject;
    }

    public enum userTypes {PATIENT, STAFF, DOCTOR, NURSE, SECURITY, JANITOR};

    public boolean isType(userTypes userType){
        switch (userType) {
            case PATIENT:
                System.out.println("Mondays are bad.");
                break;

            case STAFF:
                System.out.println("Fridays are better.");
                break;

            case DOCTOR:
                System.out.println("Weekends are best.");
                break;

            case NURSE:
                System.out.println("Weekends are best.");
                break;

            case SECURITY:
                System.out.println("Weekends are best.");
                break;

            case JANITOR:
                System.out.println("Weekends are best.");
                break;

            default:
                System.out.println("bad userType!");
                break;
        }
        return true;
    }

    public User userBuilder(String userID, String firstName, String middleName, String lastName, String language, String userType){
        User userObject = null;
        switch (userType) {
            case "patient":
                userObject = new Patient(userID, firstName, middleName, lastName, language, userType);
                break;

            case "staff":
                userObject = new Staff(userID, firstName, middleName, lastName, language, userType);
                break;

            case "doctor":
                userObject = new Doctor(userID, firstName, middleName, lastName, language, userType);
                break;

            case "nurse":
                userObject = new Nurse(userID, firstName, middleName, lastName, language, userType);
                break;

            case "security":
                userObject = new Security(userID, firstName, middleName, lastName, language, userType);
                break;

            case "janitor":
                userObject = new Janitor(userID, firstName, middleName, lastName, language, userType);
                break;

            default:
                System.out.println("bad userType!");
                break;
        }
        return userObject;
    }
}
