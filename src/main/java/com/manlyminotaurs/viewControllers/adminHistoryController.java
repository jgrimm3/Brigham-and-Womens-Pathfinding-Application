package com.manlyminotaurs.viewControllers;

import com.manlyminotaurs.databases.DataModelI;
import com.manlyminotaurs.log.Log;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDateTime;

public class adminHistoryController {
    ObservableList<Log> logList = FXCollections.observableArrayList(DataModelI.getInstance().retrieveLogData());
    ObservableList<logEntry> histList = FXCollections.observableArrayList();
    @FXML
    TableView tblHistory;
    @FXML
    TableView tblDetails;

    @FXML
    public void initialize() throws Exception{
        try {
            logList.clear();
            logList.setAll(DataModelI.getInstance().retrieveLogData());

            TableColumn timeCol = new TableColumn("Date/Time");
            TableColumn useridCol = new TableColumn("User ID");
            TableColumn nodeIdCol = new TableColumn(("Node Id"));
            TableColumn typeCol = new TableColumn("Type");
            TableColumn descriptionCol = new TableColumn("Description");



            tblHistory.getColumns().addAll(timeCol, useridCol, nodeIdCol, typeCol, descriptionCol);

            timeCol.setCellValueFactory(new PropertyValueFactory<logEntry, LocalDateTime>("logTime"));
            useridCol.setCellValueFactory(new PropertyValueFactory<logEntry, String>("description"));
            nodeIdCol.setCellValueFactory(new PropertyValueFactory<logEntry, String>("userID"));
            typeCol.setCellValueFactory(new PropertyValueFactory<logEntry, String>("nodeID"));
            descriptionCol.setCellValueFactory(new PropertyValueFactory<logEntry, String>("nodeType"));

            for(Log currLog : logList) {
                    histList.add(new logEntry(currLog.getLogTime(), currLog.getDescription(), currLog.getUserID(), currLog.getAssociatedID(), currLog.getAssociatedType()));

            }

            tblHistory.setItems(histList);


        } catch (Exception e){
            e.printStackTrace();
        }}

    public class logEntry{
        String description;
        LocalDateTime logTime;
        String userID;
        String nodeID;
        String nodeType;

        logEntry( LocalDateTime logTime, String description, String userID, String nodeID, String nodeType){
        this.description = description;
        this.logTime = logTime;
        this.userID = userID;
        this.nodeID = nodeID;
        this.nodeType = nodeType;
        }

        public String getDescription() {
            return description;
        }

        public LocalDateTime getLogTime() {
            return logTime;
        }


        public String getUserID() {
            return userID;
        }

        public String getNodeID() {
            return nodeID;
        }

        public String getNodeType() {
            return nodeType;
        }

    }

}
