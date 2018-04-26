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
import java.time.LocalDate;
import java.time.LocalDateTime;
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
	private FirebaseDBUtil firebaseDBUtil;

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
        //firebaseDBUtil = new FirebaseDBUtil();
    }

    public static DataModelI getInstance(){
       if(dataModelI == null) {
           dataModelI = new DataModelI();
       }
       return dataModelI;
    }

    /**
     * Initialize database
     */
    @Override
    public void startDB() {
        tableInitializer.setupDatabase();
//        firebaseDBUtil.initializeFirebase();
//        firebaseDBUtil.updateUserFirebase();
//        firebaseDBUtil.updateRequestFirebase();
      // System.out.println(Timestamp.valueOf("0000-00-00 00:00:00").toLocalDateTime());
        //System.out.println(tableInitializer.convertStringToDate("12-04-2017"));
    }

    /**
     * Set up database connection
     * @return Connection
     */
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

    /**
     * Close connection to database using jdbc
     * @return True if successful
     */
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

    /**
     * retrieve list of nodes in db
     * @return list of nodes
     */
    @Override
    @Deprecated
    public List<Node> retrieveNodes() {
        return nodesDBUtil.getNodeList();
    }

    /**
     * Retrieve Map of Nodes using updateNodeMap
     * @return Map of nodeID's and Nodes
     */
    public Map<String, Node> getNodeMap(){
        return nodesDBUtil.getNodeMap(false);
    }

    /**
     * Get List of Nodes from local Map of Nodes
     * @return List of Nodes
     */
    @Override
    public List<Node> getNodeList() {
        return nodesDBUtil.getNodeList();
    }

    /**
     * Update the Node in the database with the matching nodeID
     * @param newNode the updated Node
     * @return True if successful
     */
    @Override
    public boolean modifyNode(Node newNode) {
        boolean tempBool =  nodesDBUtil.modifyNode(newNode);
        addLog("Modified "+ newNode.getNodeID()+" Node",LocalDateTime.now(), KioskInfo.getCurrentUserID(),newNode.getNodeID(),"node");
        return tempBool;
    }

    /**
     * Adds the java object and the corresponding entry in the database table
     * @param nodeID unique ID
     * @param xCoord xcoord
     * @param yCoord ycoord
     * @param nodeType node type
     * @param longName long name of the node
     * @param shortName short name of the node
     * @param status active status of the Node
     * @param yCoord3D yCoord3D
     * @param xCoord3D xCoord3D
     */
    @Override
    public Node addNode(String nodeID, int xCoord, int yCoord, String floor, String building, String nodeType, String longName, String shortName, int status, int xCoord3D, int yCoord3D) {
        Node tempNode =  nodesDBUtil.addNode(nodeID, xCoord, yCoord, floor, building, nodeType, longName, shortName, status, yCoord3D, xCoord3D);
        addLog("Added "+ tempNode.getNodeID()+" Node",LocalDateTime.now(), KioskInfo.getCurrentUserID(), tempNode.getNodeID(),"node");
        return tempNode;
    }

    /**
     * Soft remove Node from database
     * @param badNode the Node to be deleted
     * @return True if successful
     */
    @Override
    public boolean removeNode(Node badNode) {
        boolean tempBool = nodesDBUtil.removeNode(badNode.getNodeID());
        addLog("Removed "+ badNode.getNodeID()+" Node",LocalDateTime.now(), KioskInfo.getCurrentUserID(),badNode.getNodeID(),"node");
        return tempBool;
    }

    /**
     * Retrieve Nodes from database that match a given type
     * @param type the Node type to match
     */
    @Override
    @Deprecated
    public List<Node> getNodesByType(String type) {
        return nodesDBUtil.getNodesByType(type);
    }

    /**
     * Query Node existence in database
     * @param nodeID the ID of the node to check
     * @return True if Node is found
     */
    @Override
    public boolean doesNodeExist(String nodeID) {
        return nodesDBUtil.doesNodeExist(nodeID);
    }

    /**
     * Retrieve Nodes from database that match a given building, floor, and type
     * @param building the building to filter by
     * @param floor the floor to filter by
     * @param type the type to filter by
     * @return List of Strings
     */
    @Override
    public List<String> getNamesByBuildingFloorType(String building, String floor, String type) {
        return nodesDBUtil.getNamesByBuildingFloorType(building, floor, type);
    }

    /**
     * return the node object that has the matching nodeID with the ID provided in the argument
     * return null if it can't  find any
     * @param nodeID the ID of the node to search for
     * @return Node object
     */
    @Override
    public Node getNodeByID(String nodeID) {
        return nodesDBUtil.getNodeByID(nodeID);
    }

    /**
     * get node from list by checking ID
     * @param nodeID id of desired node
     * @param nodeList list of nodes
     * @return desired node
     */
    @Override
    @Deprecated
	public Node getNodeByIDFromList(String nodeID, List<Node> nodeList) {
    	return nodesDBUtil.getNodeByIDFromList(nodeID, nodeList);
	}

    /**
     * get list of nodes based on floor
     * @param floor floor to get nodes from
     * @return list of nodes on said floor
     */
    @Override
    @Deprecated
    public List<Node> getNodesByFloor(String floor) {
        return nodesDBUtil.getNodesByFloor(floor);
    }

    /**
     * get nodes based on building
     * @param building to grab node from
     * @return list of nodes in building
     */
    @Override
    @Deprecated
	public List<Node> getNodesByBuilding(String building) { return nodesDBUtil.getNodesByBuilding(building); }

    /**
     * Retrieve list of buildings in database
     * @return List of Nodes
     */
    @Override
    public List<String> getBuildingsFromList() {
        return nodesDBUtil.getBuildingsFromList();
    }

    /**
     * Retrieve list of types in database
     * @return List of Strings
     */
    @Override
    public List<String> getTypesFromList() {
        return nodesDBUtil.getTypesFromList();
    }

    /**
     * Find nearest Node to given X and Y coordinates
     * @param xCoord the X coordinate
     * @param yCoord the Y coordinate
     * @return Node object
     */
    @Override
    public Node getNodeByCoords(int xCoord, int yCoord) {
        return nodesDBUtil.getNodeByCoords(xCoord, yCoord);
    }

    /**
     * get the node that has non-unique long name which describes the node
     * @param longName the longName of a Node
     * @return Node object
     */
    @Override
    public Node getNodeByLongName(String longName) {
        return nodesDBUtil.getNodeByLongName(longName);
    }

    /**
     * get node by long name
     * @param longName name of node
     * @param nodeList list of nodes to get node from
     * @return node
     */
    @Override
    @Deprecated
    public Node getNodeByLongNameFromList(String longName, List<Node> nodeList) {
        return nodesDBUtil.getNodeByLongNameFromList(longName, nodeList);
    }

    /**
     * Retrieve list of longNames in database
     * @return List of Strings
     */
    @Override
    public List<String> getLongNames() {
        return nodesDBUtil.getLongNames();
    }

    /**
     * get nodes by building type floor
     * @param building building to get nodes from
     * @param type type of node in building
     * @param floor floor in building
     * @return list of nodes
     */
    @Override
    @Deprecated
    public List<Node> getNodesByBuildingTypeFloor (String building, String type, String floor) {
        return nodesDBUtil.getNodesByBuildingTypeFloor(building, type, floor);
    }

    /**
     * get long name by building type floor
     * @param building building to search
     * @param type type to search
     * @param floor floor to search
     * @return list of long names
     */
    @Override
    @Deprecated
    public List<String> getLongNameByBuildingTypeFloor(String building, String type, String floor) {
        return nodesDBUtil.getLongNameByBuildingTypeFloor(building,type,floor);
    }

    /**
     * Retrieve list of Nodes adjacent to given Node
     * @param node the Node for which to check adjacent Nodes
     * @return List of Strings
     */
    @Override
    public List<String> getAdjacentNodes(Node node) {
        return nodesDBUtil.getAdjacentNodes(node);
    }

    //-------------------------------------------Edges---------------------------------------------------

    /**
     * Retrieve list of Edges in database
     * @return List of Edges
     */
    @Override
    public List<Edge> getEdgeList() {
        return nodesDBUtil.getEdgeList(false);
    }

    /**
     * Retrieve Edge by edgeID from database
     * @param edgeID the edgeID to search for
     * @return Edge object
     */
    @Override
    public Edge getEdgeByID(String edgeID) {
        return nodesDBUtil.getEdgeByID(edgeID);
    }

    /**
     * Add Edge - a connected pair of Nodes - to database
     * @param startNode one of the Nodes in the Edge
     * @param endNode the other Node in the Edge
     * @return	Edge object
     */
    @Override
    public Edge addEdge(Node startNode, Node endNode) {
        Edge tempEdge = nodesDBUtil.addEdge(startNode, endNode);
        addLog("Added "+ tempEdge.getEdgeID()+" Edge",LocalDateTime.now(), KioskInfo.getCurrentUserID(),tempEdge.getEdgeID(),"edge");
        return tempEdge;
    }

    /**
     * Mark Edge as removed in database
     * @param startNode one of the Nodes in the Edge
     * @param endNode the other Node in the Edge
     */
    @Override
    public void removeEdge(Node startNode, Node endNode) {
        String edgeID = startNode.getNodeID() + "_" + endNode.getNodeID();
        nodesDBUtil.removeEdge(startNode, endNode);
        addLog("Removed "+ edgeID+" Edge",LocalDateTime.now(), KioskInfo.getCurrentUserID(), edgeID,"edge");
    }

    /**
     * Modify Edge in database
     * @param startNode one of the Nodes in the Edge
     * @param endNode the other Node in the Edge
     * @param status the active status of the Edge
     */
    @Override
    public void modifyEdge(Node startNode, Node endNode, int status) {
        String edgeID = startNode.getNodeID() + "_" + endNode.getNodeID();
        nodesDBUtil.modifyEdge(startNode, endNode, status);
        addLog("Modified "+ edgeID+" Edge",LocalDateTime.now(), KioskInfo.getCurrentUserID(),edgeID,"edge");
    }

    /*------------------------------------------------ Messages -------------------------------------------------------*/

    /**
     * Add message to the database
     * @param messageObject the Message object to add to the database
     */
    @Override
    public void addMessage(Message messageObject) {
        messagesDBUtil.addMessage(messageObject);
    }

    public String addMessage(String messageID, String message, boolean isRead, LocalDate sentDate, String senderID, String receiverID){
        String tempMessageID = messagesDBUtil.addMessage(messageID, message,isRead,sentDate,senderID,receiverID);
        addLog("Added "+ tempMessageID+" Message",LocalDateTime.now(), KioskInfo.getCurrentUserID(),tempMessageID,"message");
        return tempMessageID;
    }

    /**
     * Remove message from the database
     * @param messageID the messageID of the Message object to be removed from the database
     */
    @Override
    public boolean removeMessage(String messageID) {
        boolean tempBool = messagesDBUtil.removeMessage(messageID);
        addLog("Removed "+ messageID+" Message",LocalDateTime.now(), KioskInfo.getCurrentUserID(), messageID,"message");
        return tempBool;
    }

    /**
     * Modify message in the database
     * @param newMessage the Message object to modify in the database
     */
    @Override
    public boolean modifyMessage(Message newMessage) {
        boolean tempBool = messagesDBUtil.modifyMessage(newMessage);
        addLog("Modified "+ newMessage.getMessageID()+" Message",LocalDateTime.now(), KioskInfo.getCurrentUserID(),newMessage.getMessageID(),"message");
        return tempBool;
    }

    /**
     * Generate new messageID
     * @return unique ID
     */
    @Override
    public String getNextMessageID() {
        return messagesDBUtil.generateMessageID();
    }

    /**
     * Retrieve list of all Message objects in database
     * @return List of Message objects
     */
    @Override
    public List<Message> retrieveMessages() {
        return messagesDBUtil.retrieveMessages(false);
    }

    /**
     * Retrieve list of all Message objects in database filtered by senderID
     * @param senderID the String identifying the sender of a message
     * @return List of Message objects
     */
    @Override
    public List<Message> getMessageBySender(String senderID) {
        return messagesDBUtil.searchMessageBySender(senderID);
    }

    /**
     * Retrieve list of all Message objects in database filtered by receiverID
     * @param receiverID the String identifying the receiver of a message
     * @return List of Message objects
     */
    @Override
    public List<Message> getMessageByReceiver(String receiverID) {
        return messagesDBUtil.searchMessageByReceiver(receiverID);
    }

    /**
     * Retrieve Message object from database matching a given messageID
     * @param ID the String identifying the unique ID of a message
     * @return Message object
     */
    @Override
    public Message getMessageByID(String ID) {
        return messagesDBUtil.getMessageByID(ID);
    }


    /*------------------------------------------------ Requests -------------------------------------------------------*/

    /**
     * Add Request object to database with Message object
     * @param requestObject the Request to add
     * @param messageObject the Message to add
     * @return Request object
     */
    @Override
    public Request addRequest(Request requestObject, Message messageObject) {
        Request newRequest = requestsDBUtil.addRequest(requestObject, messageObject);
//        firebaseDBUtil.updateRequestFirebase();
        addLog("Added "+ newRequest.getRequestID()+" Request",LocalDateTime.now(), KioskInfo.getCurrentUserID(),newRequest.getRequestID(),"request");
        return newRequest;
    }

    @Override
    public Request addRequest(Request requestObject) {
        return requestsDBUtil.addRequest(requestObject);
    }

    /**
     * Remove Request object from database by requestID
     * @param requestID the unique string identifier for Requests
     */
    @Override
    public boolean removeRequest(String requestID) {
        boolean tempBool = requestsDBUtil.removeRequest(requestID);
//        firebaseDBUtil.removeRequestFirebase(requestID);
        addLog("Removed "+ requestID +" Request",LocalDateTime.now(), KioskInfo.getCurrentUserID(),requestID,"request");
        return tempBool;
    }

    /**
     * Modify Request object in database
     * @param newRequest the updated Request object
     * @return True if successful
     */
    @Override
    public boolean modifyRequest(Request newRequest) {
        boolean tempBool = requestsDBUtil.modifyRequest(newRequest);
//        firebaseDBUtil.updateRequestFirebase();
        addLog("Modified "+ newRequest.getRequestID()+" Request",LocalDateTime.now(), KioskInfo.getCurrentUserID(),newRequest.getRequestID(),"request");
        return tempBool;
    }

    /**
     * Request new unique string identifier for Requests
     * @return unique ID
     */
    @Override
    public String getNextRequestID() {
        return requestsDBUtil.generateRequestID();
    }

    /**
     * Retrieve list of all Request objects in database
     * @return List of Request objects
     */
    @Override
    public List<Request> retrieveRequests() {
        return requestsDBUtil.retrieveRequests(false);
    }

    /**
     * Retrieve list of all Request objects in database filtered by senderID
     * @param senderID the unique string identifier for sender
     * @return List of Request objects
     */
    @Override
    public List<Request> getRequestBySender(String senderID) {
        return requestsDBUtil.searchRequestsBySender(senderID);
    }

    /**
     * Retrieve list of all Request objects in database filtered by receiverID
     * @param receiverID the unique string identifier for receiver
     * @return List of Request objects
     */
    @Override
    public List<Request> getRequestByReceiver(String receiverID) {
        return requestsDBUtil.searchRequestsByReceiver(receiverID);
    }

    /**
     * Retrieve Request object from database by requestID
     * @param requestID the unique string identifier for Request
     * @return Request object
     */
    @Override
    public Request getRequestByID(String requestID) {
        return requestsDBUtil.getRequestByID(requestID);
    }

	/*------------------------------------------------ Users -------------------------------------------------------*/

    /**
     * Add a User object to the database
     * @param userID id
     * @param firstName fname
     * @param middleName mname
     * @param lastName lname
     * @param languages lang
     * @param userType usertype
     * @param userName username
     * @param password pass
     * @return User object created
     */
    @Override
    public User addUser(String userID, String firstName, String middleName, String lastName, List<String> languages, String userType, String userName, String password) {
        User newUser = userDBUtil.addUser(userID, firstName, middleName, lastName, languages, userType, userName, password);
//        firebaseDBUtil.updateUserFirebase();
        addLog("Added "+ newUser.getUserID()+" User",LocalDateTime.now(), KioskInfo.getCurrentUserID(),newUser.getUserID(),"user");
        return newUser;
    }

    /**
     * add a user to db
     * @param userObject user to add
     */
    @Override
    public void addUser(User userObject) {
        userDBUtil.addUser(userObject);
    }


    /**
     * remove a user from db
     * @param userID of user to remove
     * @return true if successful
     */
    @Override
    public boolean removeUser(String userID) {
        boolean tempBool = userDBUtil.removeUser(userID);
//        firebaseDBUtil.removeUserFirebase(userID);
        addLog("Removed "+ userID +" User",LocalDateTime.now(), KioskInfo.getCurrentUserID(), userID,"user");
        return tempBool;
    }

    /**
     * modify a user in db
     * @param newUser user to modify
     * @return true if successful
     */
    @Override
    public boolean modifyUser(User newUser) {
        boolean tempBool = userDBUtil.modifyUser(newUser);
//        firebaseDBUtil.updateUserFirebase();
        addLog("Modified "+ newUser.getUserID()+" User",LocalDateTime.now(), KioskInfo.getCurrentUserID(),newUser.getUserID(),"user");
        return tempBool;
    }

    /**
     * retrieve list of users from db
     * @return list of user
     */
    @Override
    public List<User> retrieveUsers() {
        return userDBUtil.retrieveUsers(false);
    }

    /**
     * retreive staff fields form db
     * @return list of staff fields
     */
    @Override
    public List<StaffFields> retrieveStaffs() {
        return userDBUtil.retrieveStaffs();
    }

    /**
     * return User by getting ID
     * @param userID of user
     * @return User
     */
    @Override
    public User getUserByID(String userID) {
        return userDBUtil.getUserByID(userID);
    }

    /**
     * get language string
     * @param languages
     * @return language string
     */
    @Override
    public String getLanguageString(List<String> languages) {
        return userDBUtil.getLanguageString(languages);
    }

    /**
     * get list of language from long string
     * @param languagesConcat languages string to parse
     * @return list of string of languages
     */
    @Override
    public List<String> getLanguageList(String languagesConcat) {
        return userDBUtil.getLanguageList(languagesConcat);
    }

    //-----------------------------------------------User Password---------------------------------------------------

    /**
     * gets id by user password from db
     * @param userName of user
     * @param password of user
     * @return user's id
     */
    @Override
    public String getIDByUserPassword(String userName, String password) {
        UserSecurity userSecurity = new UserSecurity();
        return userSecurity.getIDByUserPassword(userName, password);
    }

    /**
     * retrieves user passwords from db
     * @return list of user passwords
     */
    @Override
    public List<UserPassword> retrieveUserPasswords() {
        return userSecurity.retrieveUserPasswords(false);
    }

    /**
     * adds a password to a user
     * @param userName of a user
     * @param password to add
     * @param userID of user
     */
    @Override
    public void addUserPassword(String userName, String password, String userID) {
        userSecurity.addUserPassword(userName, password, userID);
        addLog("Added username and password for UserID: "+ userID +" ",LocalDateTime.now(), KioskInfo.getCurrentUserID(),userID,"userpassword");
    }

    /**
     * remove users password from db
     * @param userID of user to delete password from
     * @return true if success
     */
    @Override
    public boolean removeUserPassword(String userID) {
        boolean tempBool = userSecurity.removeUserPassword(userID);
        addLog("Removed username and password for UserID: "+ userID +" ",LocalDateTime.now(), KioskInfo.getCurrentUserID(),userID,"userpassword");
        return tempBool;
    }

    /**
     * checks if username has that password
     * @param userName username
     * @param password password
     * @return true if it does
     */
    @Override
    public boolean doesUserPasswordExist(String userName, String password) {
        return userSecurity.doesUserPasswordExist(userName, password);
    }

    //---------------------------------------------------------------------------------------------------

    /**
     * retrieves log data from db
     * @return list of log
     */
    @Override
    public List<Log> retrieveLogData() {
        return logDBUtil.retrieveLogData();
    }

    /**
     * adds a log to db by creating a log
     * @param description
     * @param logTime
     * @param userID
     * @param associatedID
     * @param associatedType
     * @return newly made and newly added log
     */
    @Override
    public Log addLog(String description, LocalDateTime logTime, String userID, String associatedID, String associatedType) {
        return logDBUtil.addLog(description, logTime, userID, associatedID, associatedType);
    }

    /**
     * adds a log to db
     * @param newLog log to add
     */
    @Override
    public void addLog(Log newLog) {
        logDBUtil.addLog(newLog);
    }

    /**
     * removes log from db
     * @param logID id of log to remove
     * @return true if success
     */
    @Override
    public boolean removeLog(String logID) {
        return logDBUtil.removeLog(logID);
    }

    /**
     * gets log from log id
     * @param logID id of log to get
     * @return true if success
     */
    @Override
    public Log getLogByLogID(String logID) {
        return logDBUtil.getLogByLogID(logID);
    }

    /**
     * gets list of logs performed by certain userID
     * @param userID id to get list of log
     * @return list of logs
     */
    @Override
    public List<Log> getLogsByUserID(String userID) {
        return logDBUtil.getLogsByUserID(userID);
    }

    /**
     * gets log by association type from db
     * @param associatedType type
     * @return list of logs
     */
    @Override
    public List<Log> getLogsByAssociatedType(String associatedType) {
        return logDBUtil.getLogsByAssociatedType(associatedType);
    }

    /**
     * returns list of logs from time range
     *
     * @param startTime start
     * @param endTime end
     * @return list of logs between two time periods
     */
    @Override
    public List<Log> getLogsByLogTime(LocalDateTime startTime, LocalDateTime endTime) {
        return logDBUtil.getLogsByLogTime(startTime,endTime);
    }

    /**
     * get logs by log time choice in db
     * @param timeChoice time choice
     * @return list of logs
     */
    @Override
    public List<Log> getLogsByLogTimeChoice(String timeChoice) {
        return logDBUtil.getLogsByLogTimeChoice(timeChoice);
    }


    //----------------------------------Pathfinding Log-------------------------------------------

    /**
     * retrieves pathdinder data from db
     * @return list of pathfinder
     */
    public List<Pathfinder> retrievePathfinderData(){
        return pathfinderDBUtil.retrievePathfinderData();
    }

    /**
     * adds path between two nodes
     * @param startNodeID id of start node
     * @param endNodeID id of end node
     * @return pathfinder
     */
    public Pathfinder addPath(String startNodeID, String endNodeID){
        Pathfinder tempPath = pathfinderDBUtil.addPath(startNodeID, endNodeID);
        addLog("Pathfind from" + tempPath.getStartNodeID() +" to " + tempPath.getEndNodeID() + " is done",LocalDateTime.now(), KioskInfo.getCurrentUserID(), tempPath.getPathfinderID(),"pathfind");
        return tempPath;
    }

    /**
     * removes path of pathfinder
     * @param pathfinder path to remove
     * @return true if success
     */
    public boolean removePath(Pathfinder pathfinder){
        boolean tempBool = pathfinderDBUtil.removePath(pathfinder);
        addLog("Pathfind from" + pathfinder.getStartNodeID() +" to " + pathfinder.getEndNodeID() + " is removed",LocalDateTime.now(), KioskInfo.getCurrentUserID(), pathfinder.getPathfinderID(),"pathfind");
        return tempBool;
    }

    /**
     * gets path by pathfinder ID from DB
     * @param pathfinderID pathfinder id of path
     * @return pathfinder
     */
    public Pathfinder getPathByPathfinderID(String pathfinderID){
        return pathfinderDBUtil.getPathByPathfinderID(pathfinderID);
    }

    /**
     * gets pathfinder by startnode id from db
     * @param startNodeID id of start node
     * @return list of pathfnder
     */
    public List<Pathfinder> getPathByStartNodeID(String startNodeID){
        return pathfinderDBUtil.getPathByStartNodeID(startNodeID);
    }

    /**
     * gets path by end node id from db
     * @param endNodeID id of end node
     * @return lists of path to end node
     */
    public List<Pathfinder> getPathByEndNodeID(String endNodeID){
        return pathfinderDBUtil.getPathByEndNodeID(endNodeID);
    }

    /**
     * converts string timestamp to type timestamp
     * @param timeString to convert
     * @return converted
     */
    @Override
    public Timestamp convertStringToTimestamp(String timeString) {
        return tableInitializer.convertStringToTimestamp(timeString);
    }

    /**
     *  converts string to type Date
     * @param timeString to convert
     * @return converted
     */
    @Override
    public Date convertStringToDate(String timeString) {
        return tableInitializer.convertStringToDate(timeString);
    }



    //----------------------------------------Backup--------------------------------------------

    /**
     * permanently removes a node
     * @param badNode to remove
     * @return true if success
     */
    @Override
    public boolean permanentlyRemoveNode(Node badNode) {
        boolean tempBool = nodesDBUtil.permanentlyRemoveNode(badNode);
        addLog("Permanently Removed "+ badNode.getNodeID()+" Node",LocalDateTime.now(), KioskInfo.getCurrentUserID(),badNode.getNodeID(),"node");
        return tempBool;
    }

    /**
     * PERMANENTLY removes an edge from db
     * @param startNode start
     * @param endNode end
     * @return true if success
     */
    @Override
    public boolean permanentlyRemoveEdge(Node startNode, Node endNode) {
        String edgeID = startNode.getNodeID() + "_" + endNode.getNodeID();
        addLog("Permanently Removed "+ edgeID+" Edge",LocalDateTime.now(), KioskInfo.getCurrentUserID(),edgeID,"edge");
        return nodesDBUtil.permanentlyRemoveEdge(startNode, endNode);
    }

    /**
     * PERMANENTLY removes message from db
     * @param messageID of message to remove
     * @return true if success
     */
    @Override
    public boolean permanentlyRemoveMessage(String messageID) {
        addLog("Permanently Removed "+ messageID+" Message",LocalDateTime.now(), KioskInfo.getCurrentUserID(),messageID,"message");
        return messagesDBUtil.permanentlyRemoveMessage(messageID);
    }

    /**
     * PERMANENTLY removes a request from db
     * @param oldRequest to remove
     * @return true if success
     */
    @Override
    public boolean permanentlyRemoveRequest(Request oldRequest) {
        addLog("Permanently Removed "+ oldRequest.getRequestID()+" Request",LocalDateTime.now(), KioskInfo.getCurrentUserID(),oldRequest.getRequestID(),"request");
        return requestsDBUtil.permanentlyRemoveRequest(oldRequest);
    }

    /**
     * PERMANENTLY removes user
     * @param oldUser to remove
     * @return true if success
     */
    @Override
    public boolean permanentlyRemoveUser(User oldUser) {
        addLog("Permanently Removed "+ oldUser.getUserID()+" User",LocalDateTime.now(), KioskInfo.getCurrentUserID(),oldUser.getUserID(),"user");
        return userDBUtil.permanentlyRemoveUser(oldUser);
    }

    /**
     * PERMANENTLY removes user passwword from db
     * @param userID of user to remove
     * @return true if success
     */
    @Override
    public boolean permanentlyRemoveUserPassword(String userID) {
        addLog("Permanently Removed "+ userID+" username and password",LocalDateTime.now(), KioskInfo.getCurrentUserID(),userID,"userpassword");
        return userSecurity.permanentlyRemoveUserPassword(userID);
    }

    /**
     * brings back deleted node
     * @param nodeID of node to revert
     * @return true if success
     */
    @Override
    public boolean restoreNode(String nodeID) {
        addLog("Restored "+ nodeID+" Node",LocalDateTime.now(), KioskInfo.getCurrentUserID(), nodeID,"node");
        return nodesDBUtil.restoreNode(nodeID);
    }

    /**
     * reverts deletion of edge
     * @param startNodeID of start node
     * @param endNodeID of end node
     * @return true if success
     */
    @Override
    public boolean restoreEdge(String startNodeID, String endNodeID) {
        String edgeID = startNodeID + "_"+ endNodeID;
        addLog("Restored "+ edgeID+" Edge",LocalDateTime.now(), KioskInfo.getCurrentUserID(), edgeID,"edge");
        return nodesDBUtil.restoreEdge(startNodeID, endNodeID);
    }

    /**
     * reverts deletion of message
     * @param messageID of message
     * @return true if success
     */
    @Override
    public boolean restoreMessage(String messageID) {
        addLog("Restored "+ messageID+" Message",LocalDateTime.now(), KioskInfo.getCurrentUserID(), messageID,"message");
        return messagesDBUtil.restoreMessage(messageID);
    }

    /**
     * reverts deletion of req
     * @param requestID req id of req
     * @return true if success
     */
    @Override
    public boolean restoreRequest(String requestID) {
        addLog("Restored "+ requestID+" Request",LocalDateTime.now(), KioskInfo.getCurrentUserID(), requestID,"request");
        return requestsDBUtil.restoreRequest(requestID);
    }

    /**
     * reverts deletion of user
     * @param userID of user
     * @return true if success
     */
    @Override
    public boolean restoreUser(String userID) {
        addLog("Restored "+ userID+" User",LocalDateTime.now(), KioskInfo.getCurrentUserID(), userID,"user");
        return userDBUtil.restoreUser(userID);
    }

    /**
     * reverts to old user password
     *
     * @param userID of user
     * @return true if success
     */
    @Override
    public boolean restoreUserPassword(String userID) {
        addLog("Restored "+ userID+" username and password",LocalDateTime.now(), KioskInfo.getCurrentUserID(), userID,"userpassword");
        return userSecurity.restoreUserPassword(userID);
    }

    //--------------------------------Firebase Database----------------------------------

    /**
     * initializes firebase db
     */
    @Override
    public void initializeFirebase() {
//        firebaseDBUtil.initializeFirebase();
    }

    /**
     * update requests of firebase from db
     */
    @Override
    public void updateRequestFirebase() {
//        firebaseDBUtil.updateRequestFirebase();
    }

    /**
     * retrieve request from firebase db
     */
    @Override
    public List<Request> retrieveRequestFirebase() {
        return firebaseDBUtil.retrieveRequestFirebase();
    }

    /**
     * update log from firebase db
     */
    @Override
    public void updateLogFirebase() {
        firebaseDBUtil.updateLogFirebase();
    }

    /**
     * retrieve log from firebase db
     */
    @Override
    public List<Log> retrieveLogFirebase() {
        return firebaseDBUtil.retrieveLogFirebase();
    }

    /**
     * updates user firebase db
     */
    @Override
    public void updateUserFirebase() {
        firebaseDBUtil.updateUserFirebase();
    }

    /**
     * retrieves user firebase from db
     */
    @Override
    public List<User> retrieveUserFirebase() {
        return firebaseDBUtil.retrieveUserFirebase();
    }

    @Override
    public void updateRequestDerby(List<Request> listOfRequest) {
        firebaseDBUtil.updateRequestDerby(listOfRequest);
    }

    @Override
    public void updateUserDerby(List<User> listOfUser) {
        firebaseDBUtil.updateUserDerby(listOfUser);
    }

    @Override
    public void updateLogDerby(List<Log> listOfLog) {
        firebaseDBUtil.updateLogDerby(listOfLog);
    }

    @Override
    public void removeRequestFirebase(String requestID) {
        firebaseDBUtil.removeRequestFirebase(requestID);
    }

    @Override
    public void removeUserFirebase(String userID) {
        firebaseDBUtil.removeUserFirebase(userID);
    }

    @Override
    public void listenToEmergency() {
        firebaseDBUtil.listenToEmergency();
    }


    //--------------------------------------CSV stuffs------------------------------------------

    /**
     * update csv files from db
     */
    @Override
    public void updateAllCSVFiles() {
        new CsvFileController().updateAllCSVFiles();
    }

}
