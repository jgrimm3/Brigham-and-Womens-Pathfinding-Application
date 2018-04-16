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

    //delete log

}

