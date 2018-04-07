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

    /*------------------------------------------------ Methods -------------------------------------------------------*/
    public Message addMessage(String messageID, String message, Boolean isRead, String senderID, String receiverID){
        Message messageObject = new Message(messageID, message, isRead, senderID, receiverID);
        dataModelI.getMessageList().add(messageObject);

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

    public void removeMessage(Message message){
        for(int i = 0; i < dataModelI.getMessageList().size(); i++){
            if(dataModelI.getMessageList().get(i).getMessageID().equals(message.getMessageID())) {
                // remove the node
                System.out.println("Node removed from object list...");
                dataModelI.getMessageList().remove(i);
            }
        }
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
    }

    public void modifyMessage(Message newMessage) {
        Message oldMessage = getMessageFromList(newMessage.getMessageID());
        oldMessage.setMessage(newMessage.getMessage());
        oldMessage.setRead(newMessage.getRead());
        oldMessage.setReceiverID(newMessage.getReceiverID());
        oldMessage.setSenderID(newMessage.getSenderID());

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
        } catch (SQLException e)
        {
            System.out.println("Node already in the database");
        }
    }

    public ObservableList<Message> searchMessageByReceiver(String userID){
        ObservableList<Message> listOfMessages = FXCollections.observableArrayList();
        Iterator<Message> iterator = dataModelI.getMessageList().iterator();

        //insert rows
        while (iterator.hasNext()) {
            Message a_message = iterator.next();
            if(a_message.getReceiverID().equals(userID)){
                listOfMessages.add(a_message);
            }
        }
        return listOfMessages;
    }

    public ObservableList<Message> searchMessageBySender(String userID){
        ObservableList<Message> listOfMessages = FXCollections.observableArrayList();
        Iterator<Message> iterator = dataModelI.getMessageList().iterator();

        //insert rows
        while (iterator.hasNext()) {
            Message a_message = iterator.next();
            if(a_message.getSenderID().equals(userID)){
                listOfMessages.add(a_message);
            }
        }
        return listOfMessages;
    }

    public String generateMessageID(){
        messageIDCounter++;
        return Integer.toString(messageIDCounter-1);
    }

    /**
     * Creates a list of objects and stores them in the global variable dataModelI.getMessageList()
     */
    public void retrieveMessage() {
        try {
            // Connection
            Connection connection;
            connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");

            // Variables
            Message messageObject;
            String messageID;
            String message;
            Boolean isRead;
            String senderID;
            String receiverID;

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
                    dataModelI.getMessageList().add(messageObject);
                    messageIDCounter++;
                    System.out.println("Message added to the list: "+messageID);
                }
                rset.close();
                stmt.close();
                System.out.println("Done adding Messages");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch(SQLException e)
        {
            e.printStackTrace();
        }
    } // retrieveMessage() ends

    public void getMessageFromList(){ } // needs implementation

    public Message getMessageFromList(String messageID){
        Iterator<Message> iterator = dataModelI.getMessageList().iterator();
        while (iterator.hasNext()) {
            Message a_message = iterator.next();
            if (a_message.getMessageID().equals(messageID)) {
                return a_message;
            }
        }
        System.out.println("getMessageFromList: Something might break--------------------");
        return null;
    }

    /**
     * Print the message list
     */
    public void printMessageList (){
        int i = 0;
        while(i < dataModelI.getMessageList().size()) { System.out.println("Object " + i + ": " + dataModelI.getMessageList().get(i).getMessageID()); i++; }
    } // end printNodeList
}
