package com.manlyminotaurs.databases;

import com.manlyminotaurs.messaging.Message;
import com.manlyminotaurs.messaging.Request;
import com.manlyminotaurs.nodes.*;
import com.manlyminotaurs.users.StaffFields;
import com.manlyminotaurs.users.User;
import com.manlyminotaurs.users.UserPassword;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

//
//  '||''|.             .              '||    ||'              '||          '||
//   ||   ||   ....   .||.   ....       |||  |||    ...      .. ||    ....   ||
//   ||    || '' .||   ||   '' .||      |'|..'||  .|  '|.  .'  '||  .|...||  ||
//   ||    || .|' ||   ||   .|' ||      | '|' ||  ||   ||  |.   ||  ||       ||
//  .||...|'  '|..'|'  '|.' '|..'|'    .|. | .||.  '|..|'  '|..'||.  '|...' .||.
//
//

public class DataModelI implements IDataModel{

    /*---------------------------------------------- Variables -------------------------------------------------------*/

    // all the utils
	private MessagesDBUtil messagesDBUtil;
	private NodesDBUtil nodesDBUtil;
	private RequestsDBUtil requestsDBUtil;
	private UserDBUtil userDBUtil;
	private TableInitializer tableInitializer;
	private UserSecurity userSecurity;

    // list of all objects

    private static DataModelI dataModelI = null;
    private Connection connection = null;

    /*------------------------------------------------ Methods -------------------------------------------------------*/

    public static void main(String[] args){
        DataModelI.getInstance().startDB();

        DataModelI.getInstance().updateNodeCSVFile("./nodes.csv");
        DataModelI.getInstance().updateEdgeCSVFile("./edges.csv");
        DataModelI.getInstance().updateMessageCSVFile("./MessageTable.csv");
        DataModelI.getInstance().updateRequestCSVFile("./RequestTable.csv");
        DataModelI.getInstance().updateUserCSVFile("./UserAccountTable.csv");
        DataModelI.getInstance().updateUserPasswordFile("./UserPasswordTable.csv");
        DataModelI.getInstance().updateStaffTable("./StaffTable.csv");

        TableInitializer tableInitializer = new TableInitializer();
    //    System.out.println(tableInitializer.convertStringToDate("2018-04-06"));
     //   System.out.println(tableInitializer.convertStringToTimestamp("2018-04-06 07:43:10:2").toLocalDateTime().toString().replace("T"," "));
    }

    private DataModelI() {
        messagesDBUtil = new MessagesDBUtil();
        nodesDBUtil = new NodesDBUtil();
        requestsDBUtil = new RequestsDBUtil();
        userDBUtil = new UserDBUtil();
        tableInitializer = new TableInitializer();
        userSecurity = new UserSecurity();
    }

    public static DataModelI getInstance(){
       if(dataModelI == null) {
           dataModelI = new DataModelI();
       }
       return dataModelI;
    }

    @Override
    public void startDB() {
        tableInitializer.setupDatabase();

      // System.out.println(Timestamp.valueOf("0000-00-00 00:00:00").toLocalDateTime());
        //System.out.println(tableInitializer.convertStringToDate("12-04-2017"));
    }

    @Override
    public Connection getNewConnection() {
        try {
            if(DataModelI.getInstance().connection == null || DataModelI.getInstance().connection.isClosed()) {
                DataModelI.getInstance().connection = DriverManager.getConnection("jdbc:derby:nodesDB;create=true");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return DataModelI.getInstance().connection;
    }

    @Override
    public boolean closeConnection() {
        try {
            if(DataModelI.getInstance().connection != null) {
                DataModelI.getInstance().connection.close();
                DataModelI.getInstance().connection = null;
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (NullPointerException en){
            en.printStackTrace();
            return false;
        }
    }

	/*------------------------------------------------ Nodes -------------------------------------------------------*/
    @Override
    @Deprecated
    public List<Node> retrieveNodes() {
        return nodesDBUtil.retrieveNodes();
    }

    public Map<String, Node> getNodeMap(){
        return nodesDBUtil.getNodeMap();
    }

    @Override
    public List<Node> getNodeList() {
        return nodesDBUtil.getNodeList();
    }

    @Override
    public boolean modifyNode(Node newNode) {
        return nodesDBUtil.modifyNode(newNode);
    }

    @Override
    public Node addNode(int xCoord, int yCoord, String floor, String building, String nodeType, String longName, String shortName, int status, int xCoord3D, int yCoord3D) {
        return nodesDBUtil.addNode(xCoord, yCoord, floor, building, nodeType, longName, shortName, status, yCoord3D, xCoord3D);
    }

    @Override
    public boolean removeNode(Node badNode) {
        return nodesDBUtil.removeNode(badNode);
    }

    @Override
    @Deprecated
    public List<Node> getNodesByType(String type) {
        return nodesDBUtil.getNodesByType(type);
    }

    @Override
    public boolean doesNodeExist(String nodeID) {
        return nodesDBUtil.doesNodeExist(nodeID);
    }

    @Override
    public List<String> getNamesByBuildingFloorType(String building, String floor, String type) {
        return nodesDBUtil.getNamesByBuildingFloorType(building, floor, type);
    }

    @Override
    public Node getNodeByID(String nodeID) {
        return nodesDBUtil.getNodeByID(nodeID);
    }

    @Override
    @Deprecated
	public Node getNodeByIDFromList(String nodeID, List<Node> nodeList) {
    	return nodesDBUtil.getNodeByIDFromList(nodeID, nodeList);
	}

    @Override
    @Deprecated
    public List<Node> getNodesByFloor(String floor) {
        return nodesDBUtil.getNodesByFloor(floor);
    }

    @Override
	public List<Node> getNodesByBuilding(String building) { return nodesDBUtil.getNodesByBuilding(building); }

    @Override
    public List<String> getBuildingsFromList() {
        return nodesDBUtil.getBuildingsFromList();
    }

    @Override
    public List<String> getTypesFromList() {
        return nodesDBUtil.getTypesFromList();
    }

    @Override
    public Node getNodeByCoords(int xCoord, int yCoord) {
        return nodesDBUtil.getNodeByCoords(xCoord, yCoord);
    }

    @Override
    public Node getNodeByLongName(String longName) {
        return nodesDBUtil.getNodeByLongName(longName);
    }

    @Override
    @Deprecated
    public Node getNodeByLongNameFromList(String longName, List<Node> nodeList) {
        return nodesDBUtil.getNodeByLongNameFromList(longName, nodeList);
    }

    @Override
    public List<String> getLongNames() {
        return nodesDBUtil.getLongNames();
    }

    @Override
    @Deprecated
    public List<Node> getNodesByBuildingTypeFloor (String building, String type, String floor) {
        return nodesDBUtil.getNodesByBuildingTypeFloor(building, type, floor);
    }

    @Override
    public List<String> getLongNameByBuildingTypeFloor(String building, String type, String floor) {
        return nodesDBUtil.getLongNameByBuildingTypeFloor(building,type,floor);
    }

    @Override
    public List<String> getAdjacentNodes(Node node) {
        return nodesDBUtil.getAdjacentNodes(node);
    }

    //-------------------------------------------Edges---------------------------------------------------

    @Override
    public List<Edge> getEdgeList() {
        return nodesDBUtil.getEdgeList();
    }

    @Override
    public void addEdge(Node startNode, Node endNode) {
        nodesDBUtil.addEdge(startNode, endNode);
    }

    @Override
    public void removeEdge(Node startNode, Node endNode) {
        nodesDBUtil.removeEdge(startNode, endNode);
    }

    @Override
    public void modifyEdge(Node startNode, Node endNode, int status) {
        nodesDBUtil.modifyEdge(startNode, endNode, status);
    }

    /*------------------------------------------------ Messages -------------------------------------------------------*/
    @Override
    public Message addMessage(Message messageObject) {
        return messagesDBUtil.addMessage(messageObject);
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
    public String getNextMessageID() {
        return messagesDBUtil.generateMessageID();
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
    public Request addRequest(Request requestObject, Message messageObject) {
        return requestsDBUtil.addRequest(requestObject, messageObject);
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
    public String getNextRequestID() {
        return requestsDBUtil.generateRequestID();
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
    public User addUser(String firstName, String middleName, String lastName, List<String> languages, String userType, String userName, String password) {
        return userDBUtil.addUser(firstName, middleName, lastName, languages, userType, userName, password);
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
    public List<StaffFields> retrieveStaffs() {
        return userDBUtil.retrieveStaffs();
    }

    @Override
    public User getUserByID(String userID) {
        return userDBUtil.getUserByID(userID);
    }

    @Override
    public String getLanguageString(List<String> languages) {
        return userDBUtil.getLanguageString(languages);
    }

    @Override
    public List<String> getLanguageList(String languagesConcat) {
        return userDBUtil.getLanguageList(languagesConcat);
    }

    @Override
    public String getIDByUserPassword(String userName, String password) {
        UserSecurity userSecurity = new UserSecurity();
        return userSecurity.getIDByUserPassword(userName, password);
    }

    @Override
    public List<UserPassword> retrieveUserPasswords() {
        return userSecurity.retrieveUserPasswords();
    }


    @Override
    public void updateUserPasswordFile(String csvFileName) {
        new CsvFileController().updateUserPasswordFile(csvFileName);
    }

    @Override
    public void updateStaffTable(String csvFileName) {
        new CsvFileController().updateStaffTable(csvFileName);
    }

    @Override
    public boolean doesUserPasswordExist(String userName, String password) {
        return userSecurity.doesUserPasswordExist(userName, password);
    }


    //--------------------------------------CSV stuffs------------------------------------------

    @Override
    public void updateNodeCSVFile(String csvFileName) {
        new CsvFileController().updateNodeCSVFile(csvFileName);
    }

    @Override
    public void updateEdgeCSVFile(String csvFileName) {
        new CsvFileController().updateEdgeCSVFile(csvFileName);
    }

    @Override
    public void updateRoomCSVFile(String csvFileName) {
        new CsvFileController().updateRoomCSVFile(csvFileName);
    }

    @Override
    public void updateMessageCSVFile(String csvFileName) {
        new CsvFileController().updateMessageCSVFile(csvFileName);
    }

    @Override
    public void updateRequestCSVFile(String csvFileName) {
        new CsvFileController().updateRequestCSVFile(csvFileName);
    }

    @Override
    public void updateUserCSVFile(String csvFileName) {
        new CsvFileController().updateUserCSVFile(csvFileName);
    }
}
