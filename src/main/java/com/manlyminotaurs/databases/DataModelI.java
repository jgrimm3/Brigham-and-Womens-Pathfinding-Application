package com.manlyminotaurs.databases;

import com.manlyminotaurs.messaging.Message;
import com.manlyminotaurs.messaging.Request;
import com.manlyminotaurs.nodes.*;
import com.manlyminotaurs.users.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataModelI implements IDataModel{

    /*---------------------------------------------- Variables -------------------------------------------------------*/

    // all the utils
	MessagesDBUtil messagesDBUtil = new MessagesDBUtil();
	NodesDBUtil nodesDBUtil = new NodesDBUtil();
	RequestsDBUtil requestsDBUtil = new RequestsDBUtil();
	UserDBUtil userDBUtil = new UserDBUtil();

    // list of all objects
    private static List<Node> nodeList = new ArrayList<>();
    private static List<User> userList = new ArrayList<>();
    private static List<Room> roomList = new ArrayList<>();
    private static List<Edge> edgeList = new ArrayList<>();
    private static List<Message> messageList = new ArrayList<>();
    private static List<Request> requestList = new ArrayList<>();

    public static List<Node> getNodeList() {
        return nodeList;
    }

    public static List<User> getUserList() {
        return userList;
    }

    public static List<Room> getRoomList() {
        return roomList;
    }

    public static List<Edge> getEdgeList() {
        return edgeList;
    }

    public static List<Message> getMessageList() {
        return messageList;
    }

    public static List<Request> getRequestList() {
        return requestList;
    }

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

	/*------------------------------------------------ Nodes -------------------------------------------------------*/
    @Override
    public void retrieveNodes() {
        nodesDBUtil.retrieveNodes();
    }

    @Override
    public boolean modifyNode(Node newNode) {
        return nodesDBUtil.modifyNode(newNode);
    }

    @Override
    public Node addNode(String longName, String shortName, String nodeType, int xcoord, int ycoord, String floor, String building, int xCoord3D, int yCoord3D) {
        return nodesDBUtil.addNode(longName, shortName, nodeType, xcoord, ycoord, floor, building, xCoord3D, yCoord3D);
    }

    @Override
    public List<Node> getNodesByType(String type) {
        return nodesDBUtil.getNodesByType(type, nodeList);
    }

    @Override
    public Node getNodeByID(String nodeID) {
        return nodesDBUtil.getNodesByID(nodeID, nodeList);
    }

    @Override
    public List<Node> getNodesByFloor(String floor) {
        return nodesDBUtil.getNodesByFloor(floor, nodeList);
    }

    @Override
    public List<String> getBuildingsFromList() {
        return nodesDBUtil.getBuildingsFromList(nodeList);
    }

    @Override
    public List<String> getTypesFromList(String building) {
        return nodesDBUtil.getTypesFromList(building, nodeList);
    }

    @Override
    public List<Node> getNodesFromList(String building, String type) {
        return nodesDBUtil.getNodesFromList(building, type, nodeList);
    }

    @Override
	public List<Node> getAdjacentNodes(Node node) { return nodesDBUtil.getAdjacentNodesFromNode(node); }

	/*------------------------------------------------ Messages -------------------------------------------------------*/
    @Override
    public Message addMessage(String messageID, String message, Boolean isRead, String senderID, String receiverID) {
        return messagesDBUtil.addMessage(messageID, message, isRead, senderID, receiverID);
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
    public void retrieveMessages() {
        messagesDBUtil.retrieveMessages();
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
        return messagesDBUtil.getMessageFromList(ID);
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
        Request oldRequest = getRequestByID(newRequest.getRequestID());
        if (oldRequest == null){
            return false;
        }
        oldRequest.setRequestType(newRequest.getRequestType());
        oldRequest.setPriority(newRequest.getPriority());
        oldRequest.setComplete(newRequest.getComplete());
        oldRequest.setAdminConfirm(newRequest.getAdminConfirm());
        oldRequest.setNodeID(newRequest.getNodeID());
        oldRequest.setMessageID(newRequest.getMessageID());
        oldRequest.setPassword(newRequest.getPassword());
        return true;
    }

    @Override
    public void retrieveRequests() {
        requestsDBUtil.retrieveRequests();
    }

    @Override
    public List<Request> getRequestBySender(String senderID) {
        return requestsDBUtil.searchRequestBySender(senderID);
    }

    @Override
    public List<Request> getRequestByReceiver(String receiverID) {
        return requestsDBUtil.searchRequestByReceiver(receiverID);
    }

    @Override
    public Request getRequestByID(String requestID) {
        return requestsDBUtil.searchRequestsByID(requestID);
    }

	/*------------------------------------------------ Users -------------------------------------------------------*/

    @Override
    public User addUser(String userID, String firstName, String middleName, String lastName, String language) {
        return userDBUtil.addUser(userID, firstName, middleName, lastName, language);
    }

    @Override
    public boolean removeUser(User oldUser) {
        return userDBUtil.removeUser(oldUser);
    }

    @Override
    public boolean modifyUser(User newUser) {
        User oldUser = getUserByID(newUser.getUserID());
        if (oldUser == null){
            return false;
        }
        oldUser.setFirstName(newUser.getFirstName());
        oldUser.setMiddleInitial(newUser.getMiddleInitial());
        oldUser.setLastName(newUser.getLastName());
        oldUser.setLanguage(newUser.getLanguage());
        return true;
    }

    @Override
    public void retrieveUsers() {
        userDBUtil.retrieveUsers();
    }

    @Override
    public User getUserByID(String userID) {
        return userDBUtil.getUserByID(userID);
    }

}
