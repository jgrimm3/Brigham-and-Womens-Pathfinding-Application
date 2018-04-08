package com.manlyminotaurs.databases;

import com.manlyminotaurs.messaging.Message;
import com.manlyminotaurs.messaging.Request;
import com.manlyminotaurs.nodes.*;
import com.manlyminotaurs.users.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public class DataModelI implements IDataModel{

    /*---------------------------------------------- Variables -------------------------------------------------------*/

    // all the utils
	MessagesDBUtil messagesDBUtil = new MessagesDBUtil();
	NodesDBUtil nodesDBUtil = new NodesDBUtil();
	RequestsDBUtil requestsDBUtil = new RequestsDBUtil();
	UserDBUtil userDBUtil = new UserDBUtil();

    // list of all objects

    private TableInitializer tableInitializer = new TableInitializer();

    private static DataModelI dataModelI;

    private static Connection connection;

    /*------------------------------------------------ Methods -------------------------------------------------------*/
    private DataModelI() {}

    public static DataModelI getInstance(){
        if(dataModelI == null) {
            dataModelI = new DataModelI();
        }
        return dataModelI;
    }

    @Override
    public void startDB() {
        tableInitializer.setupDatabase();
    }

    @Override
    public Connection getNewConnection() {
        if(connection == null) {
            try {
                connection = DriverManager.getConnection("jdbc:derby:./nodesDB;create=true");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    @Override
    public boolean closeConnection(Connection connection) {
        try {
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

	/*------------------------------------------------ Nodes/Edges -------------------------------------------------------*/
    @Override
    public List<Node> retrieveNodes() {
        return nodesDBUtil.retrieveNodes();
    }

    @Override
    public boolean modifyNode(Node newNode) {
        return nodesDBUtil.modifyNode(newNode);
    }

    @Override
    public Node addNode(String nodeID, int xCoord, int yCoord, String floor, String building, String nodeType, String longName, String shortName, int xCoord3D, int yCoord3D) {
        return nodesDBUtil.addNode(nodeID, xCoord, yCoord, floor, building, nodeType, longName, shortName, xCoord3D, yCoord3D);
    }

    @Override
    public boolean removeNode(Node badNode) {
        return nodesDBUtil.removeNode(badNode);
    }


    @Override
    public List<Node> getNodesByType(String type) {
        return nodesDBUtil.getNodesByType(type);
    }

    @Override
    public Node getNodeByID(String nodeID) {
        return nodesDBUtil.getNodeByID(nodeID);
    }

    @Override
    public List<Node> getNodesByFloor(String floor) {
        return nodesDBUtil.getNodesByFloor(floor);
    }

    @Override
    public List<String> getBuildingsFromList() {
        return nodesDBUtil.getBuildingsFromList();
    }

    @Override
    public List<String> getTypesFromList(String building) {
        return nodesDBUtil.getTypesFromList(building);
    }

    @Override
    public List<Node> getNodesFromList(String building, String type) {
        return nodesDBUtil.getNodesFromList(building, type);
    }

    @Override
    public List<Node> getAdjacentNodes() {
        return null;
    }

    @Override
    public Set<Edge> getEdgeList(List<Node> nodeList) {
        return nodesDBUtil.getEdgeList(nodeList);
    }
/*
    @Override
	public List<Node> getAdjacentNodes(Node node) { return nodesDBUtil.getAdjacentNodesFromNode(node); }

    @Override
    public void addEdge(Node startNode, Node endNode) {
        nodesDBUtil.addEdge(startNode, endNode);
    }

    @Override
    public void removeEdge(Node startNode, Node endNode) {
        nodesDBUtil.removeEdge(startNode, endNode);
    }

    @Override
    public boolean hasEdge(Node startNode, Node endNode) {
        return nodesDBUtil.hasEdge(startNode, endNode);
    }
*/
    /*------------------------------------------------ Messages -------------------------------------------------------*/
    @Override
    public Message addMessage(String message, Boolean isRead, String senderID, String receiverID) {
        return messagesDBUtil.addMessage(message, isRead, senderID, receiverID);
    }

    @Override
    public boolean removeMessage(Message oldMessage) {
        return messagesDBUtil.removeMessage(oldMessage);
    }

    @Override
    public boolean modifyMessage(Message newMessage) {
        return messagesDBUtil.modifyMessage(newMessage);
    }

    @Override
    public List<Message> retrieveMessages() {
        return messagesDBUtil.retrieveMessages();
    }

    @Override
    public List<Message> getMessageBySender(String senderID) {
        return messagesDBUtil.searchMessageBySender(senderID);
    }

    @Override
    public List<Message> getMessageByReceiver(String receiverID) {
        return messagesDBUtil.searchMessageByReceiver(receiverID);
    }

    @Override
    public Message getMessageByID(String ID) {
        return messagesDBUtil.getMessageByID(ID);
    }

	/*------------------------------------------------ Requests -------------------------------------------------------*/
    @Override
    public Request addRequest(String requestType, int priority,  String nodeID, String message, String senderID) {
        return requestsDBUtil.addRequest(requestType,priority,nodeID,message,senderID);
    }

    @Override
    public boolean removeRequest(Request oldRequest) {
        return requestsDBUtil.removeRequest(oldRequest);
    }

    @Override
    public boolean modifyRequest(Request newRequest) {
        return requestsDBUtil.modifyRequest(newRequest);
    }

    @Override
    public List<Request> retrieveRequests() {
        return requestsDBUtil.retrieveRequests();
    }

    @Override
    public List<Request> getRequestBySender(String senderID) {
        return requestsDBUtil.searchRequestsBySender(senderID);
    }

    @Override
    public List<Request> getRequestByReceiver(String receiverID) {
        return requestsDBUtil.searchRequestsByReceiver(receiverID);
    }

    @Override
    public Request getRequestByID(String requestID) {
        return requestsDBUtil.getRequestByID(requestID);
    }

	/*------------------------------------------------ Users -------------------------------------------------------*/

    @Override
    public User addUser(String userID, String firstName, String middleName, String lastName, String language,String userType) {
        return userDBUtil.addUser(userID, firstName, middleName, lastName, language, userType);
    }

    @Override
    public boolean removeUser(User oldUser) {
        return userDBUtil.removeUser(oldUser);
    }

    @Override
    public boolean modifyUser(User newUser) {
        return userDBUtil.modifyUser(newUser);
    }

    @Override
    public List<User> retrieveUsers() {
        return userDBUtil.retrieveUsers();
    }

    @Override
    public User getUserByID(String userID) {
        return userDBUtil.getUserByID(userID);
    }

}
