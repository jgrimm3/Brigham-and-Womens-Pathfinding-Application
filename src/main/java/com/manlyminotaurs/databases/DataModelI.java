package com.manlyminotaurs.databases;

import com.manlyminotaurs.core.KioskInfo;
import com.manlyminotaurs.log.Log;
import com.manlyminotaurs.log.Pathfinder;
import com.manlyminotaurs.messaging.Message;
import com.manlyminotaurs.messaging.Request;
import com.manlyminotaurs.nodes.*;
import com.manlyminotaurs.users.StaffFields;
import com.manlyminotaurs.users.User;
import com.manlyminotaurs.users.UserPassword;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

//
//  '||''|.             .              '||    ||'              '||          '||
//   ||   ||   ....   .||.   ....       |||  |||    ...      .. ||    ....   ||
//   ||    || '' .||   ||   '' .||      |'|..'||  .|  '|.  .'  '||  .|...||  ||
//   ||    || .|' ||   ||   .|' ||      | '|' ||  ||   ||  |.   ||  ||       ||
//  .||...|'  '|..'|'  '|.' '|..'|'    .|. | .||.  '|..|'  '|..'||.  '|...' .||.
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
	private LogDBUtil logDBUtil;
	private PathfinderDBUtil pathfinderDBUtil;

    // list of all objects

    private static DataModelI dataModelI = null;
    private Connection connection = null;

    /*------------------------------------------------ Methods -------------------------------------------------------*/

    public static void main(String[] args){
         DataModelI.getInstance().startDB();
         DataModelI.getInstance().updateAllCSVFiles();

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
        logDBUtil = new LogDBUtil();
        pathfinderDBUtil = new PathfinderDBUtil();
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
        return nodesDBUtil.getNodeList();
    }

    public Map<String, Node> getNodeMap(){
        return nodesDBUtil.getNodeMap(false);
    }

    @Override
    public List<Node> getNodeList() {
        return nodesDBUtil.getNodeList();
    }

    @Override
    public boolean modifyNode(Node newNode) {
        boolean tempBool =  nodesDBUtil.modifyNode(newNode);
        addLog("Modified "+ newNode.getNodeID()+" Node",LocalDateTime.now(), KioskInfo.getCurrentUserID(),newNode.getNodeID(),"node");
        return tempBool;
    }

    @Override
    public Node addNode(String nodeID, int xCoord, int yCoord, String floor, String building, String nodeType, String longName, String shortName, int status, int xCoord3D, int yCoord3D) {
        Node tempNode =  nodesDBUtil.addNode(nodeID, xCoord, yCoord, floor, building, nodeType, longName, shortName, status, yCoord3D, xCoord3D);
        addLog("Added "+ tempNode.getNodeID()+" Node",LocalDateTime.now(), KioskInfo.getCurrentUserID(), tempNode.getNodeID(),"node");
        return tempNode;
    }

    @Override
    public boolean removeNode(Node badNode) {
        boolean tempBool = nodesDBUtil.removeNode(badNode.getNodeID());
        addLog("Removed "+ badNode.getNodeID()+" Node",LocalDateTime.now(), KioskInfo.getCurrentUserID(),badNode.getNodeID(),"node");
        return tempBool;
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
    @Deprecated
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
    @Deprecated
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
        return nodesDBUtil.getEdgeList(false);
    }

    @Override
    public Edge getEdgeByID(String edgeID) {
        return nodesDBUtil.getEdgeByID(edgeID);
    }

    @Override
    public Edge addEdge(Node startNode, Node endNode) {
        Edge tempEdge = nodesDBUtil.addEdge(startNode, endNode);
        addLog("Added "+ tempEdge.getEdgeID()+" Edge",LocalDateTime.now(), KioskInfo.getCurrentUserID(),tempEdge.getEdgeID(),"edge");
        return tempEdge;
    }

    @Override
    public void removeEdge(Node startNode, Node endNode) {
        String edgeID = startNode.getNodeID() + "_" + endNode.getNodeID();
        nodesDBUtil.removeEdge(startNode, endNode);
        addLog("Removed "+ edgeID+" Edge",LocalDateTime.now(), KioskInfo.getCurrentUserID(), edgeID,"edge");
    }

    @Override
    public void modifyEdge(Node startNode, Node endNode, int status) {
        String edgeID = startNode.getNodeID() + "_" + endNode.getNodeID();
        nodesDBUtil.modifyEdge(startNode, endNode, status);
        addLog("Modified "+ edgeID+" Edge",LocalDateTime.now(), KioskInfo.getCurrentUserID(),edgeID,"edge");
    }

    /*------------------------------------------------ Messages -------------------------------------------------------*/
    @Override
    public Message addMessage(Message messageObject) {
        Message tempMessage = messagesDBUtil.addMessage(messageObject);
        addLog("Added "+ messageObject.getMessageID()+" Message",LocalDateTime.now(), KioskInfo.getCurrentUserID(),messageObject.getMessageID(),"message");
        return tempMessage;
    }

    @Override
    public boolean removeMessage(String messageID) {
        boolean tempBool = messagesDBUtil.removeMessage(messageID);
        addLog("Removed "+ messageID+" Message",LocalDateTime.now(), KioskInfo.getCurrentUserID(), messageID,"message");
        return tempBool;
    }

    @Override
    public boolean modifyMessage(Message newMessage) {
        boolean tempBool = messagesDBUtil.modifyMessage(newMessage);
        addLog("Modified "+ newMessage.getMessageID()+" Message",LocalDateTime.now(), KioskInfo.getCurrentUserID(),newMessage.getMessageID(),"message");
        return tempBool;
    }

    @Override
    public String getNextMessageID() {
        return messagesDBUtil.generateMessageID();
    }

    @Override
    public List<Message> retrieveMessages() {
        return messagesDBUtil.retrieveMessages(false);
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
        Request newRequest = requestsDBUtil.addRequest(requestObject, messageObject);
        addLog("Added "+ newRequest.getRequestID()+" Request",LocalDateTime.now(), KioskInfo.getCurrentUserID(),newRequest.getRequestID(),"request");
        return newRequest;
    }

    @Override
    public boolean removeRequest(Request oldRequest) {
        boolean tempBool = requestsDBUtil.removeRequest(oldRequest);
        addLog("Removed "+ oldRequest.getRequestID()+" Request",LocalDateTime.now(), KioskInfo.getCurrentUserID(),oldRequest.getRequestID(),"request");
        return tempBool;
    }

    @Override
    public boolean modifyRequest(Request newRequest) {
        boolean tempBool = requestsDBUtil.modifyRequest(newRequest);
        addLog("Modified "+ newRequest.getRequestID()+" Request",LocalDateTime.now(), KioskInfo.getCurrentUserID(),newRequest.getRequestID(),"request");
        return tempBool;
    }

    @Override
    public String getNextRequestID() {
        return requestsDBUtil.generateRequestID();
    }

    @Override
    public List<Request> retrieveRequests() {
        return requestsDBUtil.retrieveRequests(false);
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
    public User addUser(String userID, String firstName, String middleName, String lastName, List<String> languages, String userType, String userName, String password) {
        User newUser = userDBUtil.addUser(userID, firstName, middleName, lastName, languages, userType, userName, password);
        addLog("Added "+ newUser.getUserID()+" User",LocalDateTime.now(), KioskInfo.getCurrentUserID(),newUser.getUserID(),"user");
        return newUser;
    }

    @Override
    public boolean removeUser(User oldUser) {
        boolean tempBool = userDBUtil.removeUser(oldUser);
        addLog("Removed "+ oldUser.getUserID()+" User",LocalDateTime.now(), KioskInfo.getCurrentUserID(),oldUser.getUserID(),"user");
        return tempBool;
    }

    @Override
    public boolean modifyUser(User newUser) {
        boolean tempBool = userDBUtil.modifyUser(newUser);
        addLog("Modified "+ newUser.getUserID()+" User",LocalDateTime.now(), KioskInfo.getCurrentUserID(),newUser.getUserID(),"user");
        return tempBool;
    }

    @Override
    public List<User> retrieveUsers() {
        return userDBUtil.retrieveUsers(false);
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

    //-----------------------------------------------User Password---------------------------------------------------

    @Override
    public String getIDByUserPassword(String userName, String password) {
        UserSecurity userSecurity = new UserSecurity();
        return userSecurity.getIDByUserPassword(userName, password);
    }

    @Override
    public List<UserPassword> retrieveUserPasswords() {
        return userSecurity.retrieveUserPasswords(false);
    }

    @Override
    public void addUserPassword(String userName, String password, String userID) {
        userSecurity.addUserPassword(userName, password, userID);
        addLog("Added username and password for UserID: "+ userID +" ",LocalDateTime.now(), KioskInfo.getCurrentUserID(),userID,"userpassword");
    }


    @Override
    public boolean removeUserPassword(String userID) {
        boolean tempBool = userSecurity.removeUserPassword(userID);
        addLog("Removed username and password for UserID: "+ userID +" ",LocalDateTime.now(), KioskInfo.getCurrentUserID(),userID,"userpassword");
        return tempBool;
    }

    @Override
    public boolean doesUserPasswordExist(String userName, String password) {
        return userSecurity.doesUserPasswordExist(userName, password);
    }

    //---------------------------------------------------------------------------------------------------

    @Override
    public List<Log> retrieveLogData() {
        return logDBUtil.retrieveLogData();
    }

    @Override
    public Log addLog(String description, LocalDateTime logTime, String userID, String associatedID, String associatedType) {
        return logDBUtil.addLog(description, logTime, userID, associatedID, associatedType);
    }

    @Override
    public boolean removeLog(Log oldLog) {
        return logDBUtil.removeLog(oldLog);
    }

    @Override
    public Log getLogByLogID(String logID) {
        return logDBUtil.getLogByLogID(logID);
    }

    @Override
    public List<Log> getLogsByUserID(String userID) {
        return logDBUtil.getLogsByUserID(userID);
    }

    @Override
    public List<Log> getLogsByAssociatedType(String associatedType) {
        return logDBUtil.getLogsByAssociatedType(associatedType);
    }

    @Override
    public List<Log> getLogsByLogTime(LocalDateTime startTime, LocalDateTime endTime) {
        return logDBUtil.getLogsByLogTime(startTime,endTime);
    }

    @Override
    public List<Log> getLogsByLogTimeChoice(String timeChoice) {
        return logDBUtil.getLogsByLogTimeChoice(timeChoice);
    }


    //----------------------------------Pathfinding Log-------------------------------------------

    public List<Pathfinder> retrievePathfinderData(){
        return pathfinderDBUtil.retrievePathfinderData();
    }
    public Pathfinder addPath(String startNodeID, String endNodeID){
        Pathfinder tempPath = pathfinderDBUtil.addPath(startNodeID, endNodeID);
        addLog("Pathfind from" + tempPath.getStartNodeID() +" to " + tempPath.getEndNodeID() + " is done",LocalDateTime.now(), KioskInfo.getCurrentUserID(), tempPath.getPathfinderID(),"pathfind");
        return tempPath;
    }
    public boolean removePath(Pathfinder pathfinder){
        boolean tempBool = pathfinderDBUtil.removePath(pathfinder);
        addLog("Pathfind from" + pathfinder.getStartNodeID() +" to " + pathfinder.getEndNodeID() + " is removed",LocalDateTime.now(), KioskInfo.getCurrentUserID(), pathfinder.getPathfinderID(),"pathfind");
        return tempBool;
    }
    public Pathfinder getPathByPathfinderID(String pathfinderID){
        return pathfinderDBUtil.getPathByPathfinderID(pathfinderID);
    }
    public List<Pathfinder> getPathByStartNodeID(String startNodeID){
        return pathfinderDBUtil.getPathByStartNodeID(startNodeID);
    }
    public List<Pathfinder> getPathByEndNodeID(String endNodeID){
        return pathfinderDBUtil.getPathByEndNodeID(endNodeID);
    }

    @Override
    public Timestamp convertStringToTimestamp(String timeString) {
        return tableInitializer.convertStringToTimestamp(timeString);
    }

    @Override
    public Date convertStringToDate(String timeString) {
        return tableInitializer.convertStringToDate(timeString);
    }



    //----------------------------------------Backup--------------------------------------------

    @Override
    public boolean permanentlyRemoveNode(Node badNode) {
        boolean tempBool = nodesDBUtil.permanentlyRemoveNode(badNode);
        addLog("Permanently Removed "+ badNode.getNodeID()+" Node",LocalDateTime.now(), KioskInfo.getCurrentUserID(),badNode.getNodeID(),"node");
        return tempBool;
    }

    @Override
    public boolean permanentlyRemoveEdge(Node startNode, Node endNode) {
        String edgeID = startNode.getNodeID() + "_" + endNode.getNodeID();
        addLog("Permanently Removed "+ edgeID+" Edge",LocalDateTime.now(), KioskInfo.getCurrentUserID(),edgeID,"edge");
        return nodesDBUtil.permanentlyRemoveEdge(startNode, endNode);
    }

    @Override
    public boolean permanentlyRemoveMessage(String messageID) {
        addLog("Permanently Removed "+ messageID+" Message",LocalDateTime.now(), KioskInfo.getCurrentUserID(),messageID,"message");
        return messagesDBUtil.permanentlyRemoveMessage(messageID);
    }

    @Override
    public boolean permanentlyRemoveRequest(Request oldRequest) {
        addLog("Permanently Removed "+ oldRequest.getRequestID()+" Request",LocalDateTime.now(), KioskInfo.getCurrentUserID(),oldRequest.getRequestID(),"request");
        return requestsDBUtil.permanentlyRemoveRequest(oldRequest);
    }

    @Override
    public boolean permanentlyRemoveUser(User oldUser) {
        addLog("Permanently Removed "+ oldUser.getUserID()+" User",LocalDateTime.now(), KioskInfo.getCurrentUserID(),oldUser.getUserID(),"user");
        return userDBUtil.permanentlyRemoveUser(oldUser);
    }

    @Override
    public boolean permanentlyRemoveUserPassword(String userID) {
        addLog("Permanently Removed "+ userID+" username and password",LocalDateTime.now(), KioskInfo.getCurrentUserID(),userID,"userpassword");
        return userSecurity.permanentlyRemoveUserPassword(userID);
    }

    @Override
    public boolean restoreNode(String nodeID) {
        addLog("Restored "+ nodeID+" Node",LocalDateTime.now(), KioskInfo.getCurrentUserID(), nodeID,"node");
        return nodesDBUtil.restoreNode(nodeID);
    }

    @Override
    public boolean restoreEdge(String startNodeID, String endNodeID) {
        String edgeID = startNodeID + "_"+ endNodeID;
        addLog("Restored "+ edgeID+" Edge",LocalDateTime.now(), KioskInfo.getCurrentUserID(), edgeID,"edge");
        return nodesDBUtil.restoreEdge(startNodeID, endNodeID);
    }

    @Override
    public boolean restoreMessage(String messageID) {
        addLog("Restored "+ messageID+" Message",LocalDateTime.now(), KioskInfo.getCurrentUserID(), messageID,"message");
        return messagesDBUtil.restoreMessage(messageID);
    }

    @Override
    public boolean restoreRequest(String requestID) {
        addLog("Restored "+ requestID+" Request",LocalDateTime.now(), KioskInfo.getCurrentUserID(), requestID,"request");
        return requestsDBUtil.restoreRequest(requestID);
    }

    @Override
    public boolean restoreUser(String userID) {
        addLog("Restored "+ userID+" User",LocalDateTime.now(), KioskInfo.getCurrentUserID(), userID,"user");
        return userDBUtil.restoreUser(userID);
    }

    @Override
    public boolean restoreUserPassword(String userID) {
        addLog("Restored "+ userID+" username and password",LocalDateTime.now(), KioskInfo.getCurrentUserID(), userID,"userpassword");
        return userSecurity.restoreUserPassword(userID);
    }

    //--------------------------------------CSV stuffs------------------------------------------
    @Override
    public void updateAllCSVFiles() {
        new CsvFileController().updateAllCSVFiles();
    }

}
