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
    DataModelI dataModelI = DataModelI.getInstance();
    CsvFileController csvFileController = new CsvFileController();

    /*------------------------------------ Add/remove/modify message -------------------------------------------------*/
    public Message addMessage(String messageID, String message, Boolean isRead, String senderID, String receiverID){
        Message messageObject = new Message(messageID, message, isRead, senderID, receiverID);

        try {
            // Connect to the database
            System.out.println("Getting connection to database...");
            Connection connection;
            connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
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
        }
        new CsvFileController().updateMessageCSVFile("./MessageTable.csv");
        return messageObject;
    }

    public boolean removeMessage(Message message){
        boolean isSuccess = false;

        try {
            // Get connection to database and delete the node from the database
            Connection connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            Statement stmt = connection.createStatement();
            String str = "DELETE FROM MESSAGE WHERE messageID = '" + message.getMessageID() + "'";
            stmt.executeUpdate(str);
            stmt.close();
            connection.close();
            System.out.println("Node removed from database");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        new CsvFileController().updateMessageCSVFile("./MessageTable.csv");
        return isSuccess;
    }

    public boolean modifyMessage(Message newMessage) {
        Message oldMessage = getMessageFromList(newMessage.getMessageID());
        oldMessage.setMessage(newMessage.getMessage());
        oldMessage.setRead(newMessage.getRead());
        oldMessage.setReceiverID(newMessage.getReceiverID());
        oldMessage.setSenderID(newMessage.getSenderID());
        boolean isSuccess = false;
        try {
            // Connect to the database
            System.out.println("Getting connection to database...");
            Connection connection = dataModelI.getNewConnection();
            String str = "UPDATE map_nodes SET messageID = ?, message = ?, isRead = ?, senderID = ?, receiver = ?";

            // Create the prepared statement
            PreparedStatement statement = connection.prepareStatement(str);
            statement.setString(1, newMessage.getMessageID());
            statement.setString(2, newMessage.getMessage());
            statement.setBoolean(3, newMessage.getRead());
            statement.setString(4, newMessage.getSenderID());
            statement.setString(5, newMessage.getReceiverID());
            System.out.println("Prepared statement created...");
            statement.executeUpdate();
            System.out.println("Node added to database");
            csvFileController.updateMessageCSVFile("./MessageTable.csv");
            isSuccess = true;
        } catch (SQLException e)
        {
            System.out.println("Message already in the database");
        }
        return isSuccess;
    }

    /*------------------------------------ Search message by Receiver/Sender -------------------------------------------------*/
    public List<Message> searchMessageByReceiver(String userID){
        ObservableList<Message> listOfMessages = FXCollections.observableArrayList();

        return listOfMessages;
    }

    public List<Message> searchMessageBySender(String userID){
        List<Message> listOfMessages = new ArrayList<>();

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
                    messageIDCounter++;
                    System.out.println("Message added to the list: "+messageID);
                }
                rset.close();
                stmt.close();
                System.out.println("Done adding Messages");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return listOfMessages;
    } // retrieveMessages() ends

    public Message getMessageFromList(String messageID){

        System.out.println("getMessageFromList: Something might break--------------------");
        return null;
    }
}
