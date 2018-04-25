package com.manlyminotaurs.databases;

import com.manlyminotaurs.users.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserDBUtil {

    private static int userIDCounter = 0;

    public static void setUserIDCounter(int userIDCounter) {
        UserDBUtil.userIDCounter = userIDCounter;
    }

    private String generateUserID(){
        userIDCounter++;
        return Integer.toString(userIDCounter);
    }

    /*------------------------------------ Add / Remove / Modify User -------------------------------------------------*/

    /**
     * creates a user to add to db
     *
     * @param userID
     * @param firstName
     * @param middleName
     * @param lastName
     * @param languages
     * @param userType
     * @param userName
     * @param password
     * @return user added
     */
    User addUser(String userID, String firstName, String middleName, String lastName, List<String> languages, String userType, String userName, String password){

        if(userID == null || userID.equals("")) {
            userID = generateUserID();
        }
        User userObject = userBuilder(userID,firstName,middleName,lastName, languages, userType);
        String concatLanguages = String.join("/", languages);
        Connection connection =  DataModelI.getInstance().getNewConnection();
        try {
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
        } finally{
            DataModelI.getInstance().closeConnection();
        }
        //-----------------Adding username and password later-------------------
        DataModelI.getInstance().addUserPassword(userName, password, userID);

        return userObject;
    }

    /**
     * adds a user to db
     * @param userObject
     */
    void addUser(User userObject){

        String concatLanguages = String.join("/", userObject.getLanguages());
        Connection connection =  DataModelI.getInstance().getNewConnection();
        try {
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
        } finally{
            DataModelI.getInstance().closeConnection();
        }
    }

    /**
     * removes user from db
     * @param userID
     * @return true if success
     */
    boolean removeUser(String userID){
        Connection connection = DataModelI.getInstance().getNewConnection();
        boolean isSuccess = false;
        try {
            String str = "UPDATE UserAccount SET deleteTime = ? WHERE userID = '"+ userID +"'" ;

            // Create the prepared statement
            PreparedStatement statement = connection.prepareStatement(str);
            statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            statement.executeUpdate();
            statement.close();
            System.out.println("User removed from database");
            isSuccess = true;
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally {
            DataModelI.getInstance().closeConnection();
        }
        return isSuccess;
    }

    /**
     * reverts deleted user back to db
     * @param userID of deleted
     * @return true if success
     */
    boolean restoreUser(String userID){
        Connection connection = DataModelI.getInstance().getNewConnection();
        boolean isSuccess = false;
        try {
            String str = "UPDATE UserAccount SET deleteTime = NULL WHERE userID = ?" ;

            // Create the prepared statement
            PreparedStatement statement = connection.prepareStatement(str);
            statement.setString(1, userID);
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

    /**
     * PERMANENTLY removes user from db
     * @param oldUser to delete
     * @return true if success
     */
    boolean permanentlyRemoveUser(User oldUser){
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
        DataModelI.getInstance().removeUserPassword(oldUser.getUserID());

        return isSuccess;
    }

    /**
     * modifies user in db
     * @param newUser to modify
     * @return true if success
     */
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
     * @param allEntriesExist
     */
    public List<User> retrieveUsers(boolean allEntriesExist) {
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
        LocalDateTime deleteTime = null;
        List<User> listOfUsers = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            String str;
            if(allEntriesExist){
                str = "SELECT * FROM UserAccount";
            }
            else{
                str = "SELECT * FROM UserAccount WHERE deleteTime IS NULL";
            }
            ResultSet rset = stmt.executeQuery(str);

            while (rset.next()) {
                userID = rset.getString("userID");
                firstName = rset.getString("firstName");
                middleName = rset.getString("middleName");
                lastName = rset.getString("lastName");
                languagesConcat = rset.getString("language");
                userType = rset.getString("userType");
                if(rset.getTimestamp("deleteTime") != null) {
                    deleteTime = rset.getTimestamp("deleteTime").toLocalDateTime();
                }else{
                    deleteTime = null;
                }

                languages = getLanguageList(languagesConcat);

                // Add the new edge to the list
                userObject = userBuilder(userID, firstName, middleName, lastName, languages, userType);
                userObject.setDeleteTime(deleteTime);
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

    /**
     * get list of langs
     * @param languagesConcat string of languages
     * @return tokenized list of string
     */
    public List<String> getLanguageList ( String languagesConcat){
        // Input: String languageConcat = "English/Spanish"
        // Output: List <String> languages = [ "English" , "Spanish" ]
        List<String> languages = new ArrayList<String>(Arrays.asList(languagesConcat.split("/")));
        return languages;
    }

    /**
     * gets language string
     * @param languages list
     * @return String
     */
    public String getLanguageString(List<String> languages){
        String full_language = languages.get(0);
        for(int i = 1; i<languages.size(); i++){
            full_language = full_language + "/" + languages.get(i);
        }
        return full_language;
    }

    /**
     * retrieve staff fields from db
     * @return list of staff fields
     */
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

    /**
     * gets user based on id
     * @param userID to get
     * @return user
     */
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
            String str = "SELECT * FROM UserAccount WHERE userID = '" + userID + "' AND deleteTime IS NULL";
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

    /**
     * creates user
     * @param userID
     * @param firstName
     * @param middleName
     * @param lastName
     * @param languages
     * @param userType
     * @return User created
     */
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
}
