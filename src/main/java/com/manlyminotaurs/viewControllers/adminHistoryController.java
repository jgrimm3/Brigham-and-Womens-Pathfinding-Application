package com.manlyminotaurs.viewControllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.manlyminotaurs.core.KioskInfo;
import com.manlyminotaurs.databases.DataModelI;
import com.manlyminotaurs.log.Log;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.List;

public class adminHistoryController {
    ObservableList<Log> logList = FXCollections.observableArrayList(DataModelI.getInstance().retrieveLogData());
    ObservableList<logEntry> histList = FXCollections.observableArrayList();


    @FXML
    TableView tblHistory;
    @FXML
    Label lblNodeID;
    @FXML
    Label lblLogID;
    @FXML
    Label lblUserID;
    @FXML
    Label lblDescription;
    @FXML
    Label lblType;
    @FXML
    Label lblTime;
    @FXML
    JFXButton btnFilter;
    @FXML
    JFXButton btnUnFilter;
    @FXML
    JFXButton btnRevert;
    @FXML
    JFXTextField txtType;
    @FXML
    JFXTextField txtUserID;
    @FXML
    JFXButton btnLogOut;
    @FXML
    JFXButton navBtnManageAccounts;
    @FXML
    JFXButton btnRequestManager;
    @FXML
    JFXButton navBtnNodeEditor;
    @FXML
    JFXButton btnCreateRequest;
    @FXML
    JFXButton btnRemove;



    @FXML
    public void initialize() throws Exception {
        try {
            logList.clear();
            logList.setAll(DataModelI.getInstance().retrieveLogData());

            TableColumn logID = new TableColumn("Log ID");
            TableColumn timeCol = new TableColumn("Date/Time");
            TableColumn useridCol = new TableColumn("User ID");
            TableColumn nodeIdCol = new TableColumn(("Node Id"));
            TableColumn typeCol = new TableColumn("Type");
            TableColumn descriptionCol = new TableColumn("Description");


            tblHistory.getColumns().addAll(logID, timeCol, descriptionCol, useridCol, nodeIdCol, typeCol);

            timeCol.setCellValueFactory(new PropertyValueFactory<logEntry, LocalDateTime>("logTime"));
            descriptionCol.setCellValueFactory(new PropertyValueFactory<logEntry, String>("description"));
            useridCol.setCellValueFactory(new PropertyValueFactory<logEntry, String>("userID"));
            nodeIdCol.setCellValueFactory(new PropertyValueFactory<logEntry, String>("nodeID"));
            typeCol.setCellValueFactory(new PropertyValueFactory<logEntry, String>("nodeType"));
            logID.setCellValueFactory(new PropertyValueFactory<logEntry, String>("logID"));

            for (Log currLog : logList) {
                histList.add(new logEntry(currLog.getLogID(), currLog.getLogTime(), currLog.getDescription(), currLog.getUserID(), currLog.getAssociatedID(), currLog.getAssociatedType()));
            }

            tblHistory.setItems(histList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void entryClicked() {
        logEntry selectedEntry = (logEntry) tblHistory.getSelectionModel().getSelectedItem();
        Log actualLog = DataModelI.getInstance().getLogByLogID(selectedEntry.logID);
        lblNodeID.setText("Node ID: " + actualLog.getAssociatedID());
        lblUserID.setText("User ID: " + actualLog.getUserID());
        lblLogID.setText("Log ID: " + actualLog.getLogID());
        lblDescription.setText("Description: " + actualLog.getDescription());
        lblTime.setText("Time Stamp: " + actualLog.getLogTime());
        lblType.setText("Type: " + actualLog.getAssociatedType());
    }
    public void removeLog(){
        logEntry selectedEntry = (logEntry) tblHistory.getSelectionModel().getSelectedItem();
        Log actualLog = DataModelI.getInstance().getLogByLogID(selectedEntry.logID);
        lblNodeID.setText("Node ID:");
        lblUserID.setText("User ID: ");
        lblLogID.setText("Log ID: " );
        lblDescription.setText("Description: ");
        lblTime.setText("Time Stamp: ");
        lblType.setText("Type: ");
        DataModelI.getInstance().removeLog(actualLog);
        histList.removeAll();
        histList.clear();
        tblHistory.setItems(null);
        ObservableList<Log> logList = FXCollections.observableArrayList(DataModelI.getInstance().retrieveLogData());
        for (Log currLog : logList) {
            histList.add(new logEntry(currLog.getLogID(), currLog.getLogTime(), currLog.getDescription(), currLog.getUserID(), currLog.getAssociatedID(), currLog.getAssociatedType()));
            tblHistory.setItems(histList);
        }

    }

    public class logEntry {
        String description;
        LocalDateTime logTime;
        String userID;
        String nodeID;
        String nodeType;
        String logID;

        logEntry(String logID, LocalDateTime logTime, String description, String userID, String nodeID, String nodeType) {
            this.description = description;
            this.logTime = logTime;
            this.userID = userID;
            this.nodeID = nodeID;
            this.nodeType = nodeType;
            this.logID = logID;
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
        public String getLogID() {
            return logID;
        }

    }

    public void filterLog(ActionEvent event) {
        histList.removeAll();
        histList.clear();
        tblHistory.setItems(null);

        String filtertType = txtType.getText();
        String filterUser = txtUserID.getText();

        List<Log> typeFiltered = DataModelI.getInstance().getLogsByAssociatedType(filtertType);
        List<Log> userFiltered = DataModelI.getInstance().getLogsByUserID(filterUser);


        if (!typeFiltered.equals("Filter By Type")) {
            for (Log currLog : typeFiltered) {
                histList.add(new logEntry(currLog.getLogID(), currLog.getLogTime(), currLog.getDescription(), currLog.getUserID(), currLog.getAssociatedID(), currLog.getAssociatedType()));
            }

            tblHistory.setItems(histList);
        }
        if (!userFiltered.equals("Filter by User ID")) {
            for (Log currLog : userFiltered) {
                histList.add(new logEntry(currLog.getLogID(), currLog.getLogTime(), currLog.getDescription(), currLog.getUserID(), currLog.getAssociatedID(), currLog.getAssociatedType()));
            }

            tblHistory.setItems(histList);
        }

    }

    public void unFilterLog(ActionEvent event) {
        histList.removeAll();
        histList.clear();
        tblHistory.setItems(null);
        ObservableList<Log> logList = FXCollections.observableArrayList(DataModelI.getInstance().retrieveLogData());
        for (Log currLog : logList) {
            histList.add(new logEntry(currLog.getLogID(), currLog.getLogTime(), currLog.getDescription(), currLog.getUserID(), currLog.getAssociatedID(), currLog.getAssociatedType()));
            tblHistory.setItems(histList);
        }
    }

    Parent nodeEdit;
    Parent logout;
    Parent accountManager;
    Parent createRequest;
    Parent manageRequests;
    public void nodeEditor(ActionEvent event) throws Exception {
        try {
            Stage stage;
            Parent root;
            //get reference to the button's stage
            stage = (Stage) btnLogOut.getScene().getWindow();
            //load up Home FXML document;
            nodeEdit = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLS/nodeEditor.fxml"));
            //create a new scene with root and set the stage
            Scene scene = new Scene(nodeEdit);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void LogOut(ActionEvent event){
        try{
            Stage stage;
            //get reference to the button's stage
            stage=(Stage)btnLogOut.getScene().getWindow();
            //load up Home FXML document
            logout = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/home.fxml"));

            KioskInfo.currentUserID = "";

            //create a new scene with root and set the stage
            Scene scene=new Scene(logout);
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();}
    }
    public void accountManager(ActionEvent event) throws Exception {
        try {
            Stage stage;
            Parent root;
            //get reference to the button's stage
            stage = (Stage) navBtnManageAccounts.getScene().getWindow();
            //load up Home FXML document;
            accountManager = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/accountManager.fxml"));

            //create a new scene with root and set the stage
            Scene scene = new Scene(accountManager);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void createRequest(ActionEvent event) throws Exception {
        try {
            Stage stage;
            Parent root;
            //get reference to the button's stage
            stage = (Stage) btnLogOut.getScene().getWindow();
            //load up Home FXML document;
            createRequest = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/CreateRequest.fxml"));
            //create a new scene with root and set the stage
            Scene scene = new Scene(createRequest);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void manageRequests (ActionEvent event) throws Exception {
        try {
            Stage stage;
            Parent root;
            //get reference to the button's stage
            stage = (Stage) btnLogOut.getScene().getWindow();
            if (DataModelI.getInstance().getUserByID(KioskInfo.currentUserID).isType("admin")) {
                manageRequests = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/adminRequestDashBoard.fxml"));
            }else{
                manageRequests = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/userRequestDashBoard.fxml"));
            }
            //create a new scene with root and set the stage
            Scene scene = new Scene(manageRequests);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

