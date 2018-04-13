package com.manlyminotaurs.databases;

import com.manlyminotaurs.messaging.Message;
import com.manlyminotaurs.messaging.Request;
import com.manlyminotaurs.nodes.Edge;
import com.manlyminotaurs.nodes.Node;
import com.manlyminotaurs.users.StaffFields;
import com.manlyminotaurs.users.User;
import com.manlyminotaurs.users.UserPassword;

import java.sql.Connection;
import java.util.List;
import java.util.Set;

//
//    ____  ____    ___       _             __
//   |  _ \| __ )  |_ _|_ __ | |_ ___ _ __ / _| __ _  ___ ___
//   | | | |  _ \   | || '_ \| __/ _ \ '__| |_ / _` |/ __/ _ \
//   | |_| | |_) |  | || | | | ||  __/ |  |  _| (_| | (_|  __/
//   |____/|____/  |___|_| |_|\__\___|_|  |_|  \__,_|\___\___|
//
//
//

public interface IDataModel {

    void startDB();

    Connection getNewConnection();
    boolean closeConnection();

    /*------------------------------------------ Nodes --------------------------------------------------------------*/
    /*-------------------------------- Add / Modify / Remove Node ---------------------------------------------------*/
    boolean modifyNode(Node newNode);
    Node addNode(int xCoord, int yCoord, String floor, String building, String nodeType, String longName, String shortName, int status, int xCoord3D, int yCoord3D);
    boolean removeNode(Node badNode);
    /*------------------------- Retrieve List of Nodes / All or by Attribute ----------------------------------------*/
    List<Node> retrieveNodes();
    @Deprecated
    Node getNodeByID(String ID);
    Node getNodeByIDFromList(String nodeID, List<Node> nodeList);
    List<Node> getNodesByFloor(String floor);
    List<Node> getNodesByType(String type);
    List<Node> getNodesByBuilding(String building);
    List<Node> getNodesByBuildingTypeFloor(String building, String type, String floor);
    List<String> getLongNameByBuildingTypeFloor(String building, String type, String floor);
    List<String> getBuildingsFromList();
    List<String> getTypesFromList();
    Node getNodeByCoords(int xCoord, int yCoord);
    Node getNodeByLongName(String longName);
    Node getNodeByLongNameFromList(String longName, List<Node> nodeList);
    boolean doesNodeExist(String type);
    /*---------------------------------- Get AdjacentNodes / Edges --------------------------------------------------*/
    List<String> getAdjacentNodesFromNode(Node node);
    Set<Edge> getEdgeList(List<Node> nodeList);
    void addEdge(Node startNode, Node endNode);

    /*----------------------------------------- Messages -------------------------------------------------------------*/
    /*------------------------------ Add / Modify / Remove Message ---------------------------------------------------*/
    Message addMessage(Message messageObject);
    boolean removeMessage(Message oldMessage);
    boolean modifyMessage(Message newMessage);
    String getNextMessageID();
    /*--------------------- Retrieve List of Messages / All or by Attribute ------------------------------------------*/
    List<Message> retrieveMessages();
    List<Message> getMessageBySender(String senderID);
    List<Message> getMessageByReceiver(String receiverID);
    Message getMessageByID(String ID);

    /*----------------------------------------- Requests ------------------------------------------------------------*/
    /*------------------------------ Add / Modify / Remove Request --------------------------------------------------*/
    Request addRequest(Request requestObject, Message messageObject);
    boolean removeRequest(Request oldRequest);
    boolean modifyRequest(Request newRequest);
    String getNextRequestID();
    /*-------------------------- Retrieve List of Requests / All or by Attribute ------------------------------------*/
    List<Request> retrieveRequests();
    List<Request> getRequestBySender(String senderID);
    List<Request> getRequestByReceiver(String receiverID);
    Request getRequestByID(String ID);


    /*------------------------------------------ Users -------------------------------------------------------------*/
    /*-------------------------------- Add / Modify / Remove User --------------------------------------------------*/
    User addUser(String firstName, String middleName, String lastName, String language, String userType, String userName, String password);
    boolean removeUser(User oldUser);
    boolean modifyUser(User newUser);
    /*------------------------ Retrieve List of Users / All or by Attribute ----------------------------------------*/
    List<User> retrieveUsers();
    List<StaffFields> retrieveStaffs();
    User getUserByID(String ID);


    //-------------------------------------UserSecurity---------------------------------
    boolean doesUserPasswordExist(String userName, String password);
    String getIDByUserPassword(String userName, String password);
    List<UserPassword> retrieveUserPasswords();


    //---------------------------------------UPDATE CSV FIles--------------------------------
    void updateNodeCSVFile(String csvFileName);
    void updateEdgeCSVFile(String csvFileName);
    void updateRoomCSVFile(String csvFileName);
    void updateMessageCSVFile(String csvFileName);
    void updateRequestCSVFile(String csvFileName);
    void updateUserCSVFile(String csvFileName);
    void updateUserPasswordFile(String csvFileName);
    void updateStaffTable(String csvFileName);
}