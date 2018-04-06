package com.manlyminotaurs.databases;

import com.manlyminotaurs.messaging.Message;
import com.manlyminotaurs.messaging.Request;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class MessagesDBUtil {

    /*------------------------------------------------ Variables -----------------------------------------------------*/
    public static List<Message> messageList = new ArrayList<>();
    private static int messageIDCounter = 1;

    /*------------------------------------------------ Methods -------------------------------------------------------*/
    public Message addMessage(String messageID, String message, Boolean isRead, String senderID, String receiverID){
        Message messageObject = new Message(messageID, message, isRead, senderID, receiverID);
        messageList.add(messageObject);

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
        for(int i = 0; i < messageList.size(); i++){
            if(messageList.get(i).getMessageID().equals(message.getMessageID())) {
                // remove the node
                System.out.println("Node removed from object list...");
                messageList.remove(i);
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

    public void setIsRead(Message message, boolean newReadStatus){
        message.setRead(newReadStatus);
        try {
            Connection connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            Statement stmt = connection.createStatement();
            String sql = "UPDATE Message SET ISREAD = '" + newReadStatus + "'" + " WHERE messageID = '" + message.getMessageID() + "'";
            stmt.executeUpdate(sql);
            stmt.close();
            connection.close();
            System.out.println("Modification successful");
        }catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        }
        new CsvFileController().updateMessageCSVFile("./MessageTable.csv");
    }

    public void setMessage(Message message, String newMessage){
        message.setMessage(newMessage);
        try {
            Connection connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            Statement stmt = connection.createStatement();
            String sql = "UPDATE Message SET MESSAGE = '" + newMessage + "'" + " WHERE messageID = '" + message.getMessageID() + "'";
            stmt.executeUpdate(sql);
            stmt.close();
            connection.close();
            System.out.println("Modification successful");
        }catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        }
    }

    public void setReceiver(Message message, String receiverID){
        message.setReceiverID(receiverID);
        try {
            Connection connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            Statement stmt = connection.createStatement();
            String sql = "UPDATE Message SET RECEIVERID = '" + receiverID + "'" + " WHERE messageID = '" + message.getMessageID() + "'";
            stmt.executeUpdate(sql);
            stmt.close();
            connection.close();
            System.out.println("Modification successful");
        }catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        }
        new CsvFileController().updateMessageCSVFile("./MessageTable.csv");
    }

    public Message getMessageFromList(String messageID){
        Iterator<Message> iterator = messageList.iterator();
        while (iterator.hasNext()) {
            Message a_message = iterator.next();
            if (a_message.getMessageID().equals(messageID)) {
                return a_message;
            }
        }
        System.out.println("getMessageFromList: Something might break--------------------");
        return null;
    }

    public ObservableList<Message> searchMessageByReceiver(String userID){
        ObservableList<Message> listOfMessages = FXCollections.observableArrayList();
        Iterator<Message> iterator = messageList.iterator();

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
        Iterator<Message> iterator = messageList.iterator();

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
     * Creates a list of objects and stores them in the global variable messageList
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
                    MessagesDBUtil.messageList.add(messageObject);
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

    public void getMessageFromList(){

    }

    /**
     * Print the messageList
     */
    public void printMessageList() {
        int i = 0;
        while(i < messageList.size()) { System.out.println("Object " + i + ": " + messageList.get(i).getMessageID()); i++; }
    } // end printNodeList
}
