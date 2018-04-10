package com.manlyminotaurs.databases;

import com.manlyminotaurs.messaging.Message;
import com.manlyminotaurs.messaging.Request;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class MessagesDBUtil {

    /*------------------------------------------------ Variables -----------------------------------------------------*/
    private static int messageIDCounter = 1;
    CsvFileController csvFileController = new CsvFileController();

    /*------------------------------------ Add/remove/modify message -------------------------------------------------*/
    public Message addMessage(Message messageObject){
        String messageID = generateMessageID();

        Connection connection = DataModelI.getInstance().getNewConnection();
        try {
            String str = "INSERT INTO Message(messageID, message, isRead, senderID, receiverID) VALUES (?,?,?,?,?)";

            // Create the prepared statement
            PreparedStatement statement = connection.prepareStatement(str);
            statement.setString(1, messageObject.getMessageID());
            statement.setString(2, messageObject.getMessage());
            statement.setBoolean(3, messageObject.getRead());
            statement.setString(4, messageObject.getSenderID());
            statement.setString(5, messageObject.getReceiverID());
            System.out.println("Prepared statement created...");
            statement.executeUpdate();
            System.out.println("Node added to database");
        } catch (SQLException e)
        {
            System.out.println("Message already in the database");
            e.printStackTrace();
        } finally {
            DataModelI.getInstance().closeConnection(connection);
        }
        return messageObject;
    }

    public boolean removeMessage(Message message){
        boolean isSuccess = false;
        Connection connection = DataModelI.getInstance().getNewConnection();
        try {
            Statement stmt = connection.createStatement();
            String str = "DELETE FROM Message WHERE messageID = '" + message.getMessageID() + "'";
            stmt.executeUpdate(str);
            stmt.close();
            System.out.println("Node removed from database");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataModelI.getInstance().closeConnection(connection);
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
            statement.setString(3, newMessage.getSenderID());
            statement.setString(4, newMessage.getReceiverID());
            statement.executeUpdate();
            System.out.println("Message added to database");
            isSuccess = true;
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally {
            DataModelI.getInstance().closeConnection(connection);
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
                senderID =rset.getString("senderID");

                // Add the new edge to the list
                messageObject = new Message(messageID,message,isRead,senderID,receiverID);
                listOfMessages.add(messageObject);
                System.out.println("Message added to the list: "+messageID);
            }
            rset.close();
            stmt.close();
            System.out.println("Done adding Messages");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataModelI.getInstance().closeConnection(connection);
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
                receiverID =rset.getString("receiver");

                // Add the new edge to the list
                messageObject = new Message(messageID,message,isRead,senderID,receiverID);
                listOfMessages.add(messageObject);
                System.out.println("Message added to the list: "+messageID);
            }
            rset.close();
            stmt.close();
            System.out.println("Done adding Messages");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataModelI.getInstance().closeConnection(connection);
        }
        return listOfMessages;
    }

    /*------------------------------------ Generate/Retrieve/Get message -------------------------------------------------*/
    public String generateMessageID(){
        messageIDCounter++;
        return Integer.toString(messageIDCounter-1);
    }

    /**
     * Creates a list of objects and stores them in the global variable dataModelI.getMessageList()
     */
    public List<Message> retrieveMessages() {
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

            try {
                Statement stmt = connection.createStatement();
                String str = "SELECT * FROM Message";
                ResultSet rset = stmt.executeQuery(str);

                while (rset.next()) {
                    messageID = rset.getString("messageID");
                    message = rset.getString("message");
                    isRead = rset.getBoolean("isRead");
                    senderID =rset.getString("senderID");
                    receiverID = rset.getString("receiverID");

                    // Add the new edge to the list
                    messageObject = new Message(messageID,message,isRead,senderID,receiverID);
                    listOfMessages.add(messageObject);
                    System.out.println("Message added to the list: "+messageID);
                }
                rset.close();
                stmt.close();
                System.out.println("Done adding Messages");
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DataModelI.getInstance().closeConnection(connection);
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
                senderID =rset.getString("senderID");
                receiverID = rset.getString("receiverID");

                // Add the new edge to the list
                messageObject = new Message(messageID,message,isRead,senderID,receiverID);
                listOfMessages.add(messageObject);
                System.out.println("Message added to the list: "+messageID);
            }
            rset.close();
            stmt.close();
            System.out.println("Done adding Messages");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataModelI.getInstance().closeConnection(connection);
        }
        return messageObject;
    }
}
