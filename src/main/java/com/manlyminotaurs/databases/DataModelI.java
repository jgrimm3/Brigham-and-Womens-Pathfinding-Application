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
    public Message addMessage(Message newMessage) {
        return null;
    }

    @Override
    public boolean removeMessage(Message oldMessage) {
        return false;
    }

    @Override
    public boolean modifyMessage(Message newMessage) {
        return false;
    }

    @Override
    public void retrieveMessages() {
    }

    @Override
    public List<Message> getMessageBySender(String senderID) {
        return null;
    }

    @Override
    public List<Message> getMessageByReceiver(String receiverID) {
        return null;
    }

    @Override
    public Message getMessageByID(String ID) {
        return null;
    }

    @Override
    public Request addRequest(Request newRequest) {
        return null;
    }

    @Override
    public boolean removeRequest(Request oldRequest) {
        return false;
    }

    @Override
    public boolean modifyRequest(Request newRequest) {
        return false;
    }

    @Override
    public void retrieveRequest() {
    }

    @Override
    public List<Request> getRequestBySender(String senderID) {
        return null;
    }

    @Override
    public List<Request> getRequestByReceiver(String receiverID) {
        return null;
    }

    @Override
    public Request getRequestByID(String ID) {
        return null;
    }

    @Override
    public boolean addUser(String userID, String firstName, String middleName, String lastName, String language) {
        return false;
    }

    @Override
    public boolean removeUser(User oldUser) {
        return false;
    }

    @Override
    public boolean modifyUser(String stuff) {
        return false;
    }

    @Override
    public void retrieveUser() {
    }

    @Override
    public User getUserByID(String ID) {
        return null;
    }

}
