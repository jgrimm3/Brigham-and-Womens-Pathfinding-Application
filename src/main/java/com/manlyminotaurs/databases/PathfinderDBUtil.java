//package com.manlyminotaurs.databases;
//
//import com.manlyminotaurs.log.Pathfinder;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class PathfinderDBUtil {
//
//    private static int pathFinderIDCounter = 0;
//
//    public static void setPathFinderIDCounter(int logIDCounter) {
//        PathfinderDBUtil.pathFinderIDCounter = pathFinderIDCounter;
//    }
//
//    private String generateLogID(){
//        pathFinderIDCounter++;
//        return Integer.toString(pathFinderIDCounter);
//    }
//
//
//    //retrieve pathfindings
//    public List<Pathfinder> retrieveLogData(){
//        List<Pathfinder> listOfPath = new ArrayList<>();
//        Connection connection = DataModelI.getInstance().getNewConnection();
//
//        try {
//            Statement stmt = connection.createStatement();
//            String str = "SELECT * FROM PATHFINDER";
//            ResultSet rset = stmt.executeQuery(str);
//
//            String pathfinderID = "";
//            String startNodeID = "";
//            String endNodeID = "";
//
//
//            while (rset.next()) {
//                logID = rset.getString("logID");
//                description = rset.getString("description");
//                logTime = rset.getTimestamp("logTime");
//                userID = rset.getString("userID");
//                associatedID = rset.getString("associatedID");
//                associatedType = rset.getString("associatedType");
//
//                Log newLog = new Log(logID, description, logTime.toLocalDateTime(), userID, associatedID, associatedType);
//                listOfLogs.add(newLog);
//
//                System.out.println("Log added to the list: " + logID);
//            }
//            rset.close();
//            stmt.close();
//            System.out.println("Done adding users");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            DataModelI.getInstance().closeConnection();
//        }
//
//        return listOfLogs;
//    }
//
//    //Add pathfinding
//
//    //remove pathfinding
//
//    //get pathfinding by IDs
//}
