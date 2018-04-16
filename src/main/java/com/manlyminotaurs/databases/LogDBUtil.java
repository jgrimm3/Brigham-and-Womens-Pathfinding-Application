package com.manlyminotaurs.databases;

import com.manlyminotaurs.log.Log;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LogDBUtil {

    private static int logIDCounter = 0;

    public static void setlogIDCounter(int logIDCounter) {
        LogDBUtil.logIDCounter = logIDCounter;
    }

    private String generateLogID(){
        logIDCounter++;
        return Integer.toString(logIDCounter);
    }

    //get logs from data
    public List<Log> retrieveLogData(){
        List<Log> listOfLogs = new ArrayList<>();
        Connection connection = DataModelI.getInstance().getNewConnection();

        try {
            Statement stmt = connection.createStatement();
            String str = "SELECT * FROM Log";
            ResultSet rset = stmt.executeQuery(str);

            String logID = "";
            String description = "";
            Timestamp logTime = null;
            String userID = "";
            String associatedID = "";
            String associatedType = "";

            while (rset.next()) {
                logID = rset.getString("logID");
                description = rset.getString("description");
                logTime = rset.getTimestamp("logTime");
                userID = rset.getString("userID");
                associatedID = rset.getString("associatedID");
                associatedType = rset.getString("associatedType");

                Log newLog = new Log(logID, description, logTime.toLocalDateTime(), userID, associatedID, associatedType);
                listOfLogs.add(newLog);

                System.out.println("Log added to the list: " + logID);
            }
            rset.close();
            stmt.close();
            System.out.println("Done adding users");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataModelI.getInstance().closeConnection();
        }

        return listOfLogs;
    }

    //add log
    Log addLog(String description, LocalDateTime logTime, String userID, String associatedID, String associatedType) {

        String logID = generateLogID();
        Log newLog = new Log(logID, description, logTime, userID, associatedID, associatedType);

        Connection connection = DataModelI.getInstance().getNewConnection();
        try {
            String str = "INSERT INTO Log(logID,description,logTime,userID,associatedID,associatedType) VALUES (?,?,?,?,?,?)";

            // Create the prepared statement
            PreparedStatement statement = connection.prepareStatement(str);
            statement.setString(1, logID);
            statement.setString(2, description);
            statement.setTimestamp(3, Timestamp.valueOf(logTime));
            statement.setString(4, userID);
            statement.setString(5, associatedID);
            statement.setString(6, associatedType);
            System.out.println("Prepared statement created...");
            statement.executeUpdate();
            System.out.println("Log added to database");
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return newLog;
    }

    //delete log
    boolean removeLog(Log oldLog){
        boolean isSuccess = false;
        Connection connection = DataModelI.getInstance().getNewConnection();
        try {
            Statement stmt = connection.createStatement();
            String str = "DELETE FROM Log WHERE logID = '" + oldLog.getLogID() + "'";
            stmt.executeUpdate(str);
            stmt.close();
            isSuccess = true;
            System.out.println("Log removed from database");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataModelI.getInstance().closeConnection();
        }
        return isSuccess;
    }

    //get log by iD
    Log getLogByLogID(String logID){
        // Connection
        Connection connection = DataModelI.getInstance().getNewConnection();

        // Variables
        Log newLog = null;
        String description = "";
        Timestamp logTime = null;
        String userID = "";
        String associatedID = "";
        String associatedType = "";

        try {
            Statement stmt = connection.createStatement();
            String str = "SELECT * FROM LOG WHERE logID = '" + logID + "'";
            ResultSet rset = stmt.executeQuery(str);

            if (rset.next()) {
                logID = rset.getString("logID");
                description = rset.getString("description");
                logTime = rset.getTimestamp("logTime");
                userID = rset.getString("userID");
                associatedID = rset.getString("associatedID");
                associatedType = rset.getString("associatedType");

                newLog = new Log(logID, description, logTime.toLocalDateTime(), userID, associatedID, associatedType);
                System.out.println("Log Found: " + logID);
            }
            rset.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataModelI.getInstance().closeConnection();
        }
        return newLog;
    }

    List<Log> getLogsByUserID(String userID){
        // Connection
        Connection connection = DataModelI.getInstance().getNewConnection();

        // Variables
        Log newLog = null;
        List<Log> listOfLog = new ArrayList<>();
        String logID = "";
        String description = "";
        Timestamp logTime = null;
        String associatedID = "";
        String associatedType = "";

        try {
            Statement stmt = connection.createStatement();
            String str = "SELECT * FROM LOG WHERE userID = '" + userID + "'";
            ResultSet rset = stmt.executeQuery(str);

            while (rset.next()) {
                logID = rset.getString("logID");
                description = rset.getString("description");
                logTime = rset.getTimestamp("logTime");
                associatedID = rset.getString("associatedID");
                associatedType = rset.getString("associatedType");

                newLog = new Log(logID, description, logTime.toLocalDateTime(), userID, associatedID, associatedType);
                listOfLog.add(newLog);
                System.out.println("Log Found: " + logID);
            }
            rset.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataModelI.getInstance().closeConnection();
        }
        return listOfLog;
    }

    List<Log> getLogsByAssociatedType(String associatedType){
        // Connection
        Connection connection = DataModelI.getInstance().getNewConnection();

        // Variables
        Log newLog = null;
        List<Log> listOfLog = new ArrayList<>();
        String logID = "";
        String description = "";
        Timestamp logTime = null;
        String userID = "";
        String associatedID = "";

        try {
            Statement stmt = connection.createStatement();
            String str = "SELECT * FROM LOG WHERE associatedType = '" + associatedType + "'";
            ResultSet rset = stmt.executeQuery(str);

            while (rset.next()) {
                logID = rset.getString("logID");
                description = rset.getString("description");
                logTime = rset.getTimestamp("logTime");
                userID = rset.getString("userID");
                associatedID = rset.getString("associatedID");

                newLog = new Log(logID, description, logTime.toLocalDateTime(), userID, associatedID, associatedType);
                listOfLog.add(newLog);
                System.out.println("Log Found: " + logID);
            }
            rset.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataModelI.getInstance().closeConnection();
        }
        return listOfLog;
    }

    List<Log> getLogsByLogTime(LocalDateTime startTime, LocalDateTime endTime){
        // Connection
        Connection connection = DataModelI.getInstance().getNewConnection();

        // Variables
        List<Log> listOfLog = new ArrayList<>();
        Log newLog = null;
        String logID = "";
        String description = "";
        Timestamp logTime = null;
        String userID = "";
        String associatedID = "";
        String associatedType = "";

        try {
            Statement stmt = connection.createStatement();
            String str = "SELECT * FROM LOG WHERE logTime >= " + Timestamp.valueOf(startTime) + " AND logTime <= " + Timestamp.valueOf(endTime);
            ResultSet rset = stmt.executeQuery(str);

            while (rset.next()) {
                logID = rset.getString("logID");
                description = rset.getString("description");
                logTime = rset.getTimestamp("logTime");
                userID = rset.getString("userID");
                associatedID = rset.getString("associatedID");
                associatedType = rset.getString("associatedType");

                newLog = new Log(logID, description, logTime.toLocalDateTime(), userID, associatedID, associatedType);
                listOfLog.add(newLog);
                System.out.println("Log Found: " + logID);
            }
            rset.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataModelI.getInstance().closeConnection();
        }
        return listOfLog;
    }


}

