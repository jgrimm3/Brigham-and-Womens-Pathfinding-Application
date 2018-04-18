package com.manlyminotaurs.databases;

import com.manlyminotaurs.messaging.Message;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//   __   __          ___          ___
//  |  \ |__)    |  |  |  | |    |  |  \ /
//  |__/ |__)    \__/  |  | |___ |  |   |
//
//               ___  __   __        __   ___  __
//     __  |\/| |__  /__` /__`  /\  / _` |__  /__`
//         |  | |___ .__/ .__/ /~~\ \__> |___ .__/
//

class MessagesDBUtil {

    /*------------------------------------- messageID generation ---------------------------------*/
    private static int messageIDCounter = 0;

    public static void setMessageIDCounter(int messageIDCounter) {
        MessagesDBUtil.messageIDCounter = messageIDCounter;
    }

    public String generateMessageID(){
        messageIDCounter++;
        return Integer.toString(messageIDCounter);
    }

    /*------------------------------------ Add/remove/modify message -------------------------------------------------*/
    public Message addMessage(Message messageObject){
        System.out.println("addMessage");
        if(messageObject.getMessageID() == null || messageObject.getMessageID().equals("")) {
            messageObject.setMessageID(generateMessageID());
        }
        if(messageObject.getSentDate() == null || messageObject.getSentDate().equals("")){
            messageObject.setSentDate(LocalDate.now());
        }

        Connection connection = DataModelI.getInstance().getNewConnection();
        try {
            String str = "INSERT INTO Message(messageID, message, isRead, sentDate, senderID, receiverID) VALUES (?,?,?,?,?,?)";

            // Create the prepared statement
            PreparedStatement statement = connection.prepareStatement(str);
            statement.setString(1, messageObject.getMessageID());
            statement.setString(2, messageObject.getMessage());
            statement.setBoolean(3, messageObject.getRead());
            statement.setDate(4, Date.valueOf(messageObject.getSentDate()));
            statement.setString(5, messageObject.getSenderID());
            statement.setString(6, messageObject.getReceiverID());
            System.out.println("Prepared statement created...");
            statement.executeUpdate();
            statement.close();
            System.out.println("Node added to database");
        } catch (SQLException e)
        {
            System.out.println("Message already in the database");
            e.printStackTrace();
        } finally {
            DataModelI.getInstance().closeConnection();
        }
        return messageObject;
    }

    public boolean removeMessage(String messageID){
        Connection connection = DataModelI.getInstance().getNewConnection();
        boolean isSuccess = false;
        try {
            String str = "UPDATE Message SET deleteTime = ? WHERE messageID = '"+ messageID +"'" ;

            // Create the prepared statement
            PreparedStatement statement = connection.prepareStatement(str);
            statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            statement.executeUpdate();
            System.out.println("Message added to database");
            statement.close();
            isSuccess = true;
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally {
            DataModelI.getInstance().closeConnection();
        }
        return isSuccess;
    }

    public boolean restoreMessage(String messageID){
        Connection connection = DataModelI.getInstance().getNewConnection();
        boolean isSuccess = false;
        try {
            String str = "UPDATE Message SET deleteTime = NULL WHERE messageID = ?" ;

            // Create the prepared statement
            PreparedStatement statement = connection.prepareStatement(str);
            statement.setString(1, messageID);
            statement.executeUpdate();
            System.out.println("Message added to database");
            statement.close();
            isSuccess = true;
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally {
            DataModelI.getInstance().closeConnection();
        }
        return isSuccess;
    }

    public boolean permanentlyRemoveMessage(String messageID){
        boolean isSuccess = false;
        Connection connection = DataModelI.getInstance().getNewConnection();
        try {
            Statement stmt = connection.createStatement();
            String str = "DELETE FROM Message WHERE messageID = '" + messageID + "'";
            stmt.executeUpdate(str);
            stmt.close();
            System.out.println("Node removed from database");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataModelI.getInstance().closeConnection();
            isSuccess = true;
        }
        return isSuccess;
    }

    public boolean modifyMessage(Message newMessage) {
        Connection connection = DataModelI.getInstance().getNewConnection();
        boolean isSuccess = false;
        try {
            String str = "UPDATE Message SET message = ?, isRead = ?, senderID = ?, receiver = ? WHERE messageID = '"+ newMessage.getMessageID() +"'" ;

            // Create the prepared statement
            PreparedStatement statement = connection.prepareStatement(str);
            statement.setString(1, newMessage.getMessage());
            statement.setBoolean(2, newMessage.getRead());
            statement.setDate(3, Date.valueOf(newMessage.getSentDate()));
            statement.setString(3, newMessage.getSenderID());
            statement.setString(4, newMessage.getReceiverID());
            statement.executeUpdate();
            System.out.println("Message added to database");
            statement.close();
            isSuccess = true;
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally {
            DataModelI.getInstance().closeConnection();
        }
        return isSuccess;
    }

    /*------------------------------------ Search message by Receiver/Sender -------------------------------------------------*/
    public List<Message> searchMessageByReceiver(String receiverID){
        // Connection
        Connection connection = DataModelI.getInstance().getNewConnection();

        // Variables
        Message messageObject = null;
        String messageID;
        String message;
        Boolean isRead;
        String senderID;
        List<Message> listOfMessages = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            String str = "SELECT * FROM Message Where receiverID = '" + receiverID + "'";
            ResultSet rset = stmt.executeQuery(str);

            while (rset.next()) {
                messageID = rset.getString("messageID");
                message = rset.getString("message");
                isRead = rset.getBoolean("isRead");
                Date sentDate = rset.getDate("sentDate");
                senderID =rset.getString("senderID");

                // Add the new edge to the list
                messageObject = new Message(messageID,message,isRead, sentDate.toLocalDate(), receiverID, senderID);
                listOfMessages.add(messageObject);
                System.out.println("Message added to the list: "+messageID);
            }
            rset.close();
            stmt.close();
            System.out.println("Done adding Messages");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataModelI.getInstance().closeConnection();
        }
        return listOfMessages;
    }

    public List<Message> searchMessageBySender(String senderID){
        // Connection
        Connection connection = DataModelI.getInstance().getNewConnection();

        // Variables
        Message messageObject = null;
        String messageID;
        String message;
        Boolean isRead;
        String receiverID;
        List<Message> listOfMessages = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            String str = "SELECT * FROM Message Where senderID = '" + senderID + "'";
            ResultSet rset = stmt.executeQuery(str);

            while (rset.next()) {
                messageID = rset.getString("messageID");
                message = rset.getString("message");
                isRead = rset.getBoolean("isRead");
                Date sentDate = rset.getDate("sentDate");
                receiverID =rset.getString("receiver");

                // Add the new edge to the list
                messageObject = new Message(messageID,message,isRead, sentDate.toLocalDate(), receiverID, senderID);
                listOfMessages.add(messageObject);
                System.out.println("Message added to the list: "+messageID);
            }
            rset.close();
            stmt.close();
            System.out.println("Done adding Messages");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataModelI.getInstance().closeConnection();
        }
        return listOfMessages;
    }

    /*------------------------------------ Generate/Retrieve/Get message -------------------------------------------------*/

    /**
     * Creates a list of objects and stores them in the global variable dataModelI.getMessageList()
     *
     */
    public List<Message> retrieveMessages(boolean allEntriesExist) {
            // Connection
            Connection connection = DataModelI.getInstance().getNewConnection();

            // Variables
            Message messageObject;
            String messageID;
            String message;
            Boolean isRead;
            String senderID;
            String receiverID;
            List<Message> listOfMessages = new ArrayList<>();
            LocalDateTime deleteTime = null;

            try {
                Statement stmt = connection.createStatement();
                String str;
                if(allEntriesExist) {
                    str = "SELECT * FROM Message";
                }
                else{
                    str = "SELECT * FROM Message WHERE deleteTime IS NULL";
                }
                ResultSet rset = stmt.executeQuery(str);

                while (rset.next()) {
                    messageID = rset.getString("messageID");
                    message = rset.getString("message");
                    isRead = rset.getBoolean("isRead");
                    Date sentDate = rset.getDate("sentDate");
                    senderID =rset.getString("senderID");
                    receiverID = rset.getString("receiverID");
                    if(rset.getTimestamp("deleteTime") != null) {
                        deleteTime = rset.getTimestamp("deleteTime").toLocalDateTime();
                    } else{
                        deleteTime = null;
                    }

                    // Add the new edge to the list
                    messageObject = new Message(messageID,message,isRead, sentDate.toLocalDate(), receiverID, senderID);
                    messageObject.setDeleteTime(deleteTime);
                    listOfMessages.add(messageObject);
                    System.out.println("Message added to the list: "+messageID);
                }
                rset.close();
                stmt.close();
                System.out.println("Done adding Messages");
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DataModelI.getInstance().closeConnection();
            }
        return listOfMessages;
    } // retrieveMessages() ends

    public Message getMessageByID(String messageID){
        // Connection
        Connection connection = DataModelI.getInstance().getNewConnection();

        // Variables
        Message messageObject = null;
        String message;
        Boolean isRead;
        String senderID;
        String receiverID;
        List<Message> listOfMessages = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            String str = "SELECT * FROM Message Where messageID = '" + messageID + "'";
            ResultSet rset = stmt.executeQuery(str);

            if (rset.next()) {
                message = rset.getString("message");
                isRead = rset.getBoolean("isRead");
                Date sentDate = rset.getDate("sentDate");
                senderID =rset.getString("senderID");
                receiverID = rset.getString("receiverID");

                // Add the new edge to the list
                messageObject = new Message(messageID,message,isRead, sentDate.toLocalDate(), receiverID, senderID);
                listOfMessages.add(messageObject);
                System.out.println("Message added to the list: "+messageID);
            }
            rset.close();
            stmt.close();
            System.out.println("Done adding Messages");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataModelI.getInstance().closeConnection();
        }
        return messageObject;
    }

}
