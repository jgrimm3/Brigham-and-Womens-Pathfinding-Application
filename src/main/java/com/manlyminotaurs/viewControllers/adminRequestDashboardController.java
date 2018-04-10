package com.manlyminotaurs.viewControllers;

import com.jfoenix.controls.JFXProgressBar;
import com.manlyminotaurs.databases.DataModelI;
import com.manlyminotaurs.messaging.Request;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.awt.event.MouseEvent;


public class adminRequestDashboardController  {
    DataModelI dBUtil = DataModelI.getInstance();
    ObservableList<requestInfo> openList = FXCollections.observableArrayList();
    ObservableList<requestInfo> closedList = FXCollections.observableArrayList();
    ObservableList<Request> reqestList = FXCollections.observableArrayList(dBUtil.retrieveRequests());

    public class requestInfo{
        protected String requestID;
        String requestType;
        String message;
        Boolean isAssigned;

        requestInfo(String requestType, String message, Boolean isAssigned, String requestID){
            this.requestType = requestType;
            this.message = message;
            this.isAssigned = isAssigned;
            this.requestID = requestID;
        }

        public String getRequestType() {
            return requestType;
        }

        public String getMessage() {
            return message;
        }

        public Boolean getIsAssigned() {
            return isAssigned;
        }
    }

    @FXML
    TableView tblOpenRequests;
    @FXML
    Button btnCompleteRequest;
    @FXML
    Button btnDeleteRequest;
    @FXML
    TableView tblClosedRequests;
    @FXML
    Label lblRequestDetails;
    @FXML
    Button btnLogOut;

    Parent logout;

    @FXML
    public void initialize() throws Exception{
        try{
            logout = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/home.fxml"));
            //OPEN LIST-----------------------
            TableColumn typeColOpen = new TableColumn("Request Type");
            TableColumn msgColOpen = new TableColumn("Request Message");
            TableColumn isAssignedColOpen = new TableColumn("Is Assigned");

            tblOpenRequests.getColumns().addAll(typeColOpen, msgColOpen, isAssignedColOpen);

            typeColOpen.setCellValueFactory(new PropertyValueFactory<requestInfo, String>("requestType"));
            msgColOpen.setCellValueFactory(new PropertyValueFactory<requestInfo, String>("message"));
            isAssignedColOpen.setCellValueFactory(new PropertyValueFactory<requestInfo, Boolean>("isAssigned"));

            //CLOSED LIST----------------------
            TableColumn typeColClosed = new TableColumn("Request Type");
            TableColumn msgColClosed = new TableColumn("Request Message");
            TableColumn reqConfirmedClosed = new TableColumn("Was Confirmed");

            tblClosedRequests.getColumns().addAll(typeColClosed, msgColClosed, reqConfirmedClosed);

            typeColClosed.setCellValueFactory(new PropertyValueFactory<requestInfo, String>("requestType"));
            msgColClosed.setCellValueFactory(new PropertyValueFactory<requestInfo, String>("message"));
            reqConfirmedClosed.setCellValueFactory(new PropertyValueFactory<requestInfo, String>("isAssigned"));

            //POPULATE LISTS----------------------------------------
            System.out.println(reqestList.get(0).getMessageID());
            for(Request currReq : reqestList) {
                if (!currReq.getComplete()) {
                    openList.add(new requestInfo(currReq.getRequestType(), dBUtil.getMessageByID(currReq.getMessageID()).getMessage(), currReq.getAdminConfirm(), currReq.getRequestID()));
                } else {
                    closedList.add(new requestInfo(currReq.getRequestType(), dBUtil.getMessageByID(currReq.getMessageID()).getMessage(), currReq.getAdminConfirm(), currReq.getRequestID()));
                }
            }

            tblOpenRequests.setItems(openList);
            tblClosedRequests.setItems(closedList);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }


    public void LogOut(ActionEvent event){
        try{
            Stage stage;
            //get reference to the button's stage
            //TODO: Figure out why this is giving a NullPointerException
            stage=(Stage)btnLogOut.getScene().getWindow();
            //load up Home FXML document

            //create a new scene with root and set the stage
            Scene scene=new Scene(logout);
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();}
    }


    public void openListClicked(){
        if(tblOpenRequests.getSelectionModel().getSelectedItem() == null){
            lblRequestDetails.setText("");
        }
        else {
            requestInfo selectedRequest = (requestInfo) tblOpenRequests.getSelectionModel().getSelectedItem();

            lblRequestDetails.setText("SenderID: " + dBUtil.getMessageByID(dBUtil.getRequestByID(selectedRequest.requestID).getMessageID()).getSenderID() + "\n" +
                                      "Priority: " + dBUtil.getRequestByID(selectedRequest.requestID).getPriority() + "\n" +
                                      "Location: " + dBUtil.getNodeByID(dBUtil.getRequestByID(selectedRequest.requestID).getNodeID()).getLongName() + "\n" +
                                      "Message: " + dBUtil.getMessageByID(dBUtil.getRequestByID(selectedRequest.requestID).getMessageID()).getMessage());
        }
    }






















    public void testController(){
        System.out.println("YOU SUMMONED ME?" +  tblOpenRequests.getSelectionModel().getSelectedItem());
    }
}