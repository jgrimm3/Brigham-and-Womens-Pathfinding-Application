package com.manlyminotaurs.databases;

import com.manlyminotaurs.log.BackupEntity;
import com.manlyminotaurs.messaging.Message;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BackupDBUtil {
    //logID, otherID, allOtherAttributes
    //I should store all of the attributes in String

    List<BackupEntity> retrieveBackups(){

        // Variables
        List<BackupEntity> listOfBackup = new ArrayList<>();
        BackupEntity aBackup = null;
        String logID = "";
        String otherID = "";
        String firstItem = "";
        String secondItem = "";
        String thirdItem = "";
        String fourthItem = "";
        String fifthItem = "";
        String sixthItem = "";
        String seventhItem = "";
        String eighthItem = "";
        String ninethItem = "";
        String tenthItem = "";
        PreparedStatement stmt = null;
        Connection connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:derby:nodesDB");
            String str = "SELECT * FROM BACKUP";
            stmt = connection.prepareStatement(str);
            ResultSet rset = stmt.executeQuery();

            // For every node, get the information
            while (rset.next()) {
                logID = rset.getString("logID");
                otherID = rset.getString("otherID");
                firstItem = rset.getString("firstItem");
                secondItem = rset.getString("secondItem");
                thirdItem = rset.getString("thirdItem");
                fourthItem = rset.getString("fourthItem");
                fifthItem = rset.getString("fifthItem");
                sixthItem = rset.getString("sixthItem");
                seventhItem = rset.getString("seventhItem");
                eighthItem = rset.getString("eighthItem");
                ninethItem = rset.getString("ninethItem");
                tenthItem = rset.getString("tenthItem");

                // Add the new node to the list
                aBackup = new BackupEntity(logID, otherID, firstItem, secondItem,thirdItem,fourthItem,fifthItem,sixthItem,seventhItem,eighthItem,ninethItem,tenthItem);
                listOfBackup.add(aBackup);
            }
            rset.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return listOfBackup;
    }



    //Bring data back from backup to currently used tables such as Nodes, User, Message, or Request
    boolean revertFromBackupByID(String logID, String associatedType){
        BackupEntity aBackup = getBackupByID(logID);
        if(associatedType.equals("node")){
            DataModelI.getInstance().addNode(aBackup.getOtherID(),
                    Integer.parseInt(aBackup.getFirstItem()),
                    Integer.parseInt(aBackup.getSecondItem()),
                    aBackup.getThirdItem(), aBackup.getFourthItem(),
                    aBackup.getFifthItem(), aBackup.getSixthItem(),
                    aBackup.getSeventhItem(),
                    Integer.parseInt(aBackup.getEighthItem()),
                    Integer.parseInt(aBackup.getNinethItem()),
                    Integer.parseInt(aBackup.getTenthItem()));
        }
        else if(associatedType.equals("message")){
            Message aMessage = new Message(aBackup.getOtherID(),
                    aBackup.getFifthItem(),
                    Boolean.parseBoolean(aBackup.getSecondItem()),
                    DataModelI.getInstance().convertStringToDate(aBackup.getThirdItem()).toLocalDate(),
                    aBackup.getFourthItem(),
                    aBackup.getFifthItem());
            DataModelI.getInstance().addMessage(aMessage);
        }
        else if(associatedType.equals("request")){
          //  DataModelI.getInstance().addRequest();
        }
        else if(associatedType.equals("user")){
            DataModelI.getInstance().addUser(aBackup.getOtherID(),
                    aBackup.getFirstItem(),
                    aBackup.getSecondItem(),
                    aBackup.getThirdItem(),
                    new UserDBUtil().getLanguageList(aBackup.getFourthItem()),
                    aBackup.getFifthItem(),
                    aBackup.getSixthItem(),
                    aBackup.getSecondItem());
        }
        return false;
    }



    BackupEntity getBackupByID(String logID){
        // Variables
        BackupEntity aBackup = null;
        String otherID = "";
        String firstItem = "";
        String secondItem = "";
        String thirdItem = "";
        String fourthItem = "";
        String fifthItem = "";
        String sixthItem = "";
        String seventhItem = "";
        String eighthItem = "";
        String ninethItem = "";
        String tenthItem = "";
        PreparedStatement stmt = null;
        Connection connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:derby:nodesDB");
            String str = "SELECT * FROM BACKUP WHERE logID = '" + logID + "'";
            stmt = connection.prepareStatement(str);
            ResultSet rset = stmt.executeQuery();

            if (rset.next()) {
                otherID = rset.getString("otherID");
                firstItem = rset.getString("firstItem");
                secondItem = rset.getString("secondItem");
                thirdItem = rset.getString("thirdItem");
                fourthItem = rset.getString("fourthItem");
                fifthItem = rset.getString("fifthItem");
                sixthItem = rset.getString("sixthItem");
                seventhItem = rset.getString("seventhItem");
                eighthItem = rset.getString("eighthItem");
                ninethItem = rset.getString("ninethItem");
                tenthItem = rset.getString("tenthItem");

                // Add the new node to the list
                aBackup = new BackupEntity(logID, otherID, firstItem, secondItem,thirdItem,fourthItem,fifthItem,sixthItem,seventhItem,eighthItem,ninethItem,tenthItem);
            }
            rset.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return aBackup;
    }

    //add to backup table
    void addBackup(String logID, String associatedID, String associatedType) {
        if(associatedType.equals("node")){
            DataModelI.getInstance().addNodeToBackup(associatedID);
        }
        else if(associatedType.equals("message")){
            DataModelI.getInstance().addMessageToBackup(associatedID);
        }
        else if(associatedType.equals("request")){
            DataModelI.getInstance().addRequestToBackup(associatedID);
        }
        else if(associatedType.equals("user")){
            DataModelI.getInstance().addUserToBackup(associatedID);
            DataModelI.getInstance().addUserPasswordToBackup(associatedID);
        }
    }


    //------------------------------------------------------------------------------------------------------
    //remove from backup table
    void permanentlyRemoveBackup(String logID, String associatedID, String associatedType) {
        if(associatedType.equals("node")){
            DataModelI.getInstance().removeNodeFromBackup(associatedID);
        }
        else if(associatedType.equals("message")){
            DataModelI.getInstance().removeMessageFromBackup(associatedID);
        }
        else if(associatedType.equals("request")){
            DataModelI.getInstance().removeRequestFromBackup(associatedID);
        }
        else if(associatedType.equals("user")){
            DataModelI.getInstance().removeUserFromBackup(associatedID);
            DataModelI.getInstance().removeUserPasswordFromBackup(associatedID);
        }
    }
}
