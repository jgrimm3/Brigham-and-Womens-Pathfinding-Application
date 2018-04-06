package com.manlyminotaurs.databases;

import com.manlyminotaurs.messaging.Message;
import com.manlyminotaurs.messaging.Request;
import com.manlyminotaurs.nodes.Node;
import com.manlyminotaurs.users.User;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.util.List;

public class DataModelI implements IDataModel{
    MessagesDBUtil messagesDBUtil;
    NodesDBUtil nodesDBUtil;
    RequestsDBUtil requestsDBUtil;
    CsvFileController csvFileController;
    TableInitializer tableInitializer;
    private static DataModelI dataModelI;

    private DataModelI(){}

    public static DataModelI getInstance(){
        if(dataModelI == null) {
            dataModelI = new DataModelI();
        }
        return dataModelI;
    }

    @Override
    public void initializeTable() {

    }

    @Override
    public Connection getNewConnection() {
        return null;
    }

    @Override
    public List<Node> retrieveNodes() {
        return null;
    }

    @Override
    public boolean modifyNode(Node newNode) {
        return false;
    }

    @Override
    public boolean addNode(Node newNode) {
        return false;
    }

    @Override
    public List<Node> getNodesByType(String type) {
        return null;
    }

    @Override
    public Node getNodeByID(String ID) {
        return null;
    }

    @Override
    public List<Node> getNodesByFloor(String floor) {
        return null;
    }

    @Override
    public ObservableList<String> getBuildingsFromList(List<Node> listOfNodes) {
        return null;
    }

    @Override
    public ObservableList<String> getTypesFromList(String building, List<Node> listOfNodes) {
        return null;
    }

    @Override
    public ObservableList<String> getNodeFromList(String building, String type, List<Node> listOfNodes) {
        return null;
    }

    @Override
    public List<Message> retrieveMessages() {
        return null;
    }

    @Override
    public boolean modifyMessage(Message newMessage) {
        return false;
    }

    @Override
    public boolean addMessage(Message newMessage) {
        return false;
    }

    @Override
    public List<Message> getMessageBySender() {
        return null;
    }

    @Override
    public Message getMessageByID(String ID) {
        return null;
    }

    @Override
    public List<Request> retrieveRequest() {
        return null;
    }

    @Override
    public boolean modifyRequest(Request newRequest) {
        return false;
    }

    @Override
    public boolean addRequest(Request newRequest) {
        return false;
    }

    @Override
    public Request getRequestByID(String ID) {
        return null;
    }

    @Override
    public List<User> retrieveUser() {
        return null;
    }

    @Override
    public boolean modifyUser(String stuff) {
        return false;
    }

    @Override
    public boolean addUser(User newUser) {
        return false;
    }

    @Override
    public User getUserByID(String ID) {
        return null;
    }
}
