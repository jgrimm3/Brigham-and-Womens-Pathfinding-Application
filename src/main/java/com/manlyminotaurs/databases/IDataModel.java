package com.manlyminotaurs.databases;

import com.manlyminotaurs.messaging.Message;
import com.manlyminotaurs.messaging.Request;
import com.manlyminotaurs.nodes.Node;
import com.manlyminotaurs.users.User;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.util.List;

public interface IDataModel {

    void initializeTable();

    Connection getNewConnection();
    boolean closeConnection(Connection connection);

    List<Node> retrieveNodes();
    boolean modifyNode(Node newNode);
    boolean addNode(Node newNode);
    List<Node> getNodesByType(String type);
    Node getNodeByID(String ID);
    List<Node> getNodesByFloor(String floor);
    ObservableList<String> getBuildingsFromList();
    ObservableList<String> getTypesFromList(String building);
    Node getNodeFromList(String building, String type);


    List<Message>retrieveMessages();
    boolean modifyMessage(Message newMessage);
    boolean addMessage(Message newMessage);
    List<Message> getMessageBySender();
    Message getMessageByID(String ID);

    List<Request> retrieveRequest();
    boolean modifyRequest(Request newRequest);
    boolean addRequest(Request newRequest);
    Request getRequestByID(String ID);

    List<User> retrieveUser();
    boolean modifyUser(String stuff);
    boolean addUser(User newUser);
    User getUserByID(String ID);
}
