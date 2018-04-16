package com.manlyminotaurs.databases;

import com.manlyminotaurs.users.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserDBUtil {

    private static int userIDCounter = 0;

    public static void setUserIDCounter(int userIDCounter) {
        UserDBUtil.userIDCounter = userIDCounter;
    }

    /*------------------------------------ Add / Remove / Modify User -------------------------------------------------*/
    User addUser(String firstName, String middleName, String lastName, List<String> languages, String userType, String userName, String password){

        String userID = generateUserID();
        User userObject = userBuilder(userID,firstName,middleName,lastName, languages, userType);
        Connection connection = DataModelI.getInstance().getNewConnection();
        String concatLanguages = String.join("/", languages);
        try {
            connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            String str = "INSERT INTO UserAccount(userID,firstName,middleName,lastName,language,userType) VALUES (?,?,?,?,?,?)";

            // Create the prepared statement
            PreparedStatement statement = connection.prepareStatement(str);
            statement.setString(1, userObject.getUserID());
            statement.setString(2, userObject.getFirstName());
            statement.setString(3, userObject.getMiddleName());
            statement.setString(4, userObject.getLastName());
            statement.setString(5, concatLanguages);
            statement.setString(6, userObject.getUserType());
            System.out.println("Prepared statement created...");
            statement.executeUpdate();
            System.out.println("User added to database");
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        //-----------------Adding username and password later-------------------
        UserSecurity userSecurity = new UserSecurity();
        userSecurity.addUserPassword(userName, password, userID);

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
            DataModelI.getInstance().closeConnection();
        }
        return isSuccess;
    }

    public boolean modifyUser(User newUser) {
        Connection connection = DataModelI.getInstance().getNewConnection();
        boolean isSuccess = false;
        String concatLanguages = String.join("/", newUser.getLanguages());
        try {
            String str = "UPDATE UserAccount SET firstName = ?,middleName = ?,lastName = ?,language = ?,userType =? WHERE userID = '"+ newUser.getUserID() +"'" ;

            // Create the prepared statement
            PreparedStatement statement = connection.prepareStatement(str);
            statement.setString(1, newUser.getFirstName());
            statement.setString(2, newUser.getMiddleName());
            statement.setString(3, newUser.getLastName());
            statement.setString(4, concatLanguages);
            statement.setString(5, newUser.getUserType());
            statement.executeUpdate();
            statement.close();
            System.out.println("User added to database");
            isSuccess = true;
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally {
            DataModelI.getInstance().closeConnection();
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
        Connection connection = DataModelI.getInstance().getNewConnection();

        // Variables
        User userObject;
        String userID;
        String firstName;
        String middleName;
        String lastName;
        List<String> languages;
        String languagesConcat;
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
                languagesConcat = rset.getString("language");
                userType = rset.getString("userType");

                languages = getLanguageList(languagesConcat);

                // Add the new edge to the list
                userObject = userBuilder(userID, firstName, middleName, lastName, languages, userType);
                listOfUsers.add(userObject);
                System.out.println("User added to the list: " + userID);
            }
            rset.close();
            stmt.close();
            System.out.println("Done adding users");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataModelI.getInstance().closeConnection();
        }

        return listOfUsers;
    } // retrieveUsers() ends

    List<String> getLanguageList ( String languagesConcat){
        // Input: String languageConcat = "English/Spanish"
        // Output: List <String> languages = [ "English" , "Spanish" ]
        List<String> languages = new ArrayList<String>(Arrays.asList(languagesConcat.split("/")));
        return languages;
    }

    String getLanguageString(List<String> languages){
        String full_language = languages.get(0);
        for(int i = 1; i<languages.size(); i++){
            full_language = full_language + "/" + languages.get(i);
        }
        return full_language;
    }

    public List<StaffFields> retrieveStaffs() {
        // Connection
        Connection connection = DataModelI.getInstance().getNewConnection();

        // Variables
        boolean isWorking;
        boolean isAvailable;
        String languageSpoken;
        String userID;
        StaffFields staffFields;
        List<StaffFields> listOfStaffs = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            String str = "SELECT * FROM STAFF";
            ResultSet rset = stmt.executeQuery(str);

            while (rset.next()) {
                isWorking = rset.getBoolean("isWorking");
                isAvailable = rset.getBoolean("isAvailable");
                languageSpoken = rset.getString("languageSpoken");
                userID = rset.getString("userID");

                // Add the new edge to the list
                staffFields = new StaffFields(isWorking, isAvailable, languageSpoken, userID);
                listOfStaffs.add(staffFields);
                System.out.println("User added to the list: " + userID);
            }
            rset.close();
            stmt.close();
            System.out.println("Done adding users");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataModelI.getInstance().closeConnection();
        }

        return listOfStaffs;
    } // retrieveStaffs() ends

    User getUserByID(String userID){
        // Connection
        Connection connection = DataModelI.getInstance().getNewConnection();

        // Variables
        User userObject = null;
        String firstName;
        String middleName;
        String lastName;
        String languagesConcat;
        String userType;
        List<String> languages;

        try {
            Statement stmt = connection.createStatement();
            String str = "SELECT * FROM UserAccount WHERE userID = '" + userID + "'";
            ResultSet rset = stmt.executeQuery(str);

            if (rset.next()) {
                firstName = rset.getString("firstName");
                middleName = rset.getString("middleName");
                lastName = rset.getString("lastName");
                languagesConcat = rset.getString("language");
                userType = rset.getString("userType");

                languages = getLanguageList(languagesConcat);

                // Add the new edge to the list
                userObject = userBuilder(userID, firstName, middleName, lastName, languages, userType);
                System.out.println("User added to the list: " + userID);
            }
            rset.close();
            stmt.close();
            System.out.println("Done adding users");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataModelI.getInstance().closeConnection();
        }
        return userObject;
    }

    public static User userBuilder(String userID, String firstName, String middleName, String lastName, List<String> languages, String userType){
        User userObject;
        switch (userType) {
            case "patient":
                userObject = new Patient(userID, firstName, middleName, lastName, languages, userType);
                break;

            case "staff":
                userObject = new Staff(userID, firstName, middleName, lastName, languages, userType);
                break;

            case "doctor":
                userObject = new Doctor(userID, firstName, middleName, lastName, languages, userType);
                break;

            case "nurse":
                userObject = new Nurse(userID, firstName, middleName, lastName, languages, userType);
                break;

            case "security":
                userObject = new Security(userID, firstName, middleName, lastName, languages, userType);
                break;

            case "janitor":
                userObject = new Janitor(userID, firstName, middleName, lastName, languages, userType);
                break;

            case "interpreter":
                userObject = new interpreter(userID, firstName, middleName, lastName, languages, userType);
                break;

            case "admin":
                userObject = new Admin(userID, firstName, middleName, lastName, languages, userType);
                break;

            default:
                userObject = new Visitor(userID, firstName, middleName, lastName, languages, userType);
                break;
        }
        return userObject;
    }

    private String generateUserID(){
        userIDCounter++;
        return Integer.toString(userIDCounter);
    }
}
