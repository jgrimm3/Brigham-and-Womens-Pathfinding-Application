package com.manlyminotaurs.databases;

import com.manlyminotaurs.messaging.Message;
import com.manlyminotaurs.messaging.Request;
import com.manlyminotaurs.nodes.Edge;
import com.manlyminotaurs.nodes.Node;
import com.manlyminotaurs.users.User;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.util.List;
import java.util.Set;

public interface IDataModel {

    void startDB();

    Connection getNewConnection();
    boolean closeConnection(Connection connection);


    boolean modifyNode(Node newNode);
    Node addNode(String nodeID, int xCoord, int yCoord, String floor, String building, String nodeType, String longName, String shortName, int xCoord3D, int yCoord3D);
    boolean removeNode(Node badNode);
    List<Node> retrieveNodes();
    List<Node> getNodesByType(String type);
    Node getNodeByID(String ID);
    List<Node> getNodesByFloor(String floor);
    List<String> getBuildingsFromList();
    List<String> getTypesFromList(String building);
    List<Node> getNodesFromList(String building, String type);
    List<Node> getAdjacentNodes();
    Set<Edge> getEdgeList(List<Node> nodeList);


    Message addMessage(String messageID, String message, Boolean isRead, String senderID, String receiverID);
    boolean removeMessage(Message oldMessage);
    boolean modifyMessage(Message newMessage);
    List<Message> retrieveMessages();
    List<Message> getMessageBySender(String senderID);
    List<Message> getMessageByReceiver(String receiverID);
    Message getMessageByID(String ID);

    Request addRequest(String requestType, int priority,  String nodeID, String message, String senderID);
    boolean removeRequest(Request oldRequest);
    boolean modifyRequest(Request newRequest);
    List<Request> retrieveRequests();
    List<Request> getRequestBySender(String senderID);
    List<Request> getRequestByReceiver(String receiverID);
    Request getRequestByID(String ID);

    User addUser(String userID, String firstName, String middleName, String lastName, String language);
    boolean removeUser(User oldUser);
    boolean modifyUser(User newUser);
    List<User> retrieveUsers();
    User getUserByID(String ID);
}
