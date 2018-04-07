package com.manlyminotaurs.databases;

import com.manlyminotaurs.messaging.Message;
import com.manlyminotaurs.messaging.Request;
import com.manlyminotaurs.nodes.Node;
import com.manlyminotaurs.users.User;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.util.List;

public interface IDataModel {

    void startDB();

    Connection getNewConnection();
    boolean closeConnection(Connection connection);

    void retrieveNodes();
    boolean modifyNode(Node newNode);
    boolean addNode(Node newNode);
    List<Node> getNodesByType(String type);
    Node getNodeByID(String ID);
    List<Node> getNodesByFloor(String floor);
    ObservableList<String> getBuildingsFromList();
    ObservableList<String> getTypesFromList(String building);
    Node getNodeFromList(String building, String type);

    Message addMessage(Message newMessage);
    boolean removeMessage(Message oldMessage);
    boolean modifyMessage(Message newMessage);
    void retrieveMessages();
    List<Message> getMessageBySender(String senderID);
    List<Message> getMessageByReceiver(String receiverID);
    Message getMessageByID(String ID);

    Request addRequest(Request newRequest);
    boolean removeRequest(Request oldRequest);
    boolean modifyRequest(Request newRequest);
    void retrieveRequest();
    List<Request> getRequestBySender(String senderID);
    List<Request> getRequestByReceiver(String receiverID);
    Request getRequestByID(String ID);

    boolean addUser(String userID, String firstName, String middleName, String lastName, String language);
    boolean removeUser(User oldUser);
    boolean modifyUser(String stuff);
    void retrieveUser();
    User getUserByID(String ID);
}
