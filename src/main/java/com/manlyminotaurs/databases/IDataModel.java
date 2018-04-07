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
    Node addNode(String longName, String shortName, String nodeType, int xcoord, int ycoord, String floor, String building, int xCoord3D, int yCoord3D);
    List<Node> getNodesByType(String type);
    Node getNodeByID(String ID);
    List<Node> getNodesByFloor(String floor);
    List<String> getBuildingsFromList();
    List<String> getTypesFromList(String building);
    List<Node> getNodesFromList(String building, String type);
    List<Node> getAdjacentNodes(Node node);
    void addEdge(Node startNode, Node endNode);
    void removeEdge(Node startNode, Node endNode);
    boolean hasEdge(Node startNode, Node endNode);
    void removeNode (Node node);


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
