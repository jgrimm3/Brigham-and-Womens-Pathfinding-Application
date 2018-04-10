package com.manlyminotaurs.viewControllers;

import com.manlyminotaurs.databases.DataModelI;
import com.manlyminotaurs.messaging.Request;
import com.manlyminotaurs.users.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class userRequestDashBoardController implements Initializable{
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
    requestInfo selectedRequest;

    @FXML
    TableView tblOpenRequests;
    @FXML
    Label lblRequestDetails;
    @FXML
    TableView tblClosedRequests;

    @Override
    @FXML
    public void initialize(URL location, ResourceBundle resources){
        try{
            //logout = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/home.fxml"));
            //OPEN LIST-----------------------
            TableColumn typeColOpen = new TableColumn("Request Type");
            TableColumn msgColOpen = new TableColumn("Request Message");
            TableColumn isAssignedColOpen = new TableColumn("Is Assigned");

            tblOpenRequests.getColumns().addAll(typeColOpen, msgColOpen, isAssignedColOpen);

            typeColOpen.setCellValueFactory(new PropertyValueFactory<adminRequestDashboardController.requestInfo, String>("requestType"));
            msgColOpen.setCellValueFactory(new PropertyValueFactory<adminRequestDashboardController.requestInfo, String>("message"));
            isAssignedColOpen.setCellValueFactory(new PropertyValueFactory<adminRequestDashboardController.requestInfo, Boolean>("isAssigned"));

            //CLOSED LIST----------------------
            TableColumn typeColClosed = new TableColumn("Request Type");
            TableColumn msgColClosed = new TableColumn("Request Message");
            TableColumn reqConfirmedClosed = new TableColumn("Was Confirmed");

            tblClosedRequests.getColumns().addAll(typeColClosed, msgColClosed, reqConfirmedClosed);

            typeColClosed.setCellValueFactory(new PropertyValueFactory<adminRequestDashboardController.requestInfo, String>("requestType"));
            msgColClosed.setCellValueFactory(new PropertyValueFactory<adminRequestDashboardController.requestInfo, String>("message"));
            reqConfirmedClosed.setCellValueFactory(new PropertyValueFactory<adminRequestDashboardController.requestInfo, String>("isAssigned"));

            //POPULATE LISTS----------------------------------------
            for(Request currReq : reqestList) {
                if (!currReq.getComplete()) {
                    openList.add(new requestInfo(currReq.getRequestType(), dBUtil.getMessageByID(currReq.getMessageID()).getMessage(), currReq.getAdminConfirm(), currReq.getRequestID()));
                } else {
                    closedList.add(new requestInfo(currReq.getRequestType(), dBUtil.getMessageByID(currReq.getMessageID()).getMessage(), currReq.getAdminConfirm(), currReq.getRequestID()));
                }
            }

            tblOpenRequests.setItems(openList);
            tblClosedRequests.setItems(closedList);

            List<User> userList = dBUtil.retrieveUsers();
            List<String> nurseNames = new ArrayList<>();
            for(User currUser : userList){
                if(currUser != null && currUser.isType("Nurse")){
                    nurseNames.add(currUser.getFirstName() + " " + currUser.getLastName());
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public void openListClicked(){
        if(tblOpenRequests.getSelectionModel().getSelectedItem() == null){
            lblRequestDetails.setText("");
        }
        else {
            selectedRequest = (requestInfo) tblOpenRequests.getSelectionModel().getSelectedItem();
            Request actualRequest = dBUtil.getRequestByID(selectedRequest.requestID);

            updateTextBox("SenderID: " + dBUtil.getMessageByID(actualRequest.getMessageID()).getSenderID() + "\n" +
                    "Priority: " + dBUtil.getRequestByID(selectedRequest.requestID).getPriority() + "\n" +
                    "Location: " + dBUtil.getNodeByID(actualRequest.getNodeID()).getLongName() + "\n" +
                    "Message: " + dBUtil.getMessageByID(actualRequest.getMessageID()).getMessage());
        }
    }

    public void closedListClicked(){
        if(tblClosedRequests.getSelectionModel().getSelectedItem() == null){
            lblRequestDetails.setText("");
        }
        else {
            selectedRequest = (requestInfo) tblClosedRequests.getSelectionModel().getSelectedItem();
            Request actualRequest = dBUtil.getRequestByID(selectedRequest.requestID);

            updateTextBox("SenderID: " + dBUtil.getMessageByID(actualRequest.getMessageID()).getSenderID() + "\n" +
                    "Priority: " + dBUtil.getRequestByID(selectedRequest.requestID).getPriority() + "\n" +
                    "Location: " + dBUtil.getNodeByID(actualRequest.getNodeID()).getLongName() + "\n" +
                    "Message: " + dBUtil.getMessageByID(actualRequest.getMessageID()).getMessage());
        }
    }

    public void completeClicked() {
        if (selectedRequest == null) {
        } else {

            Request newReq = dBUtil.getRequestByID(selectedRequest.requestID);
            newReq.setComplete(true);
            dBUtil.modifyRequest(newReq);
        }
    }

       public void deleteSelected(){
        if(selectedRequest == null){}
        else {
            dBUtil.removeMessage(dBUtil.getMessageByID(dBUtil.getRequestByID(selectedRequest.requestID).getMessageID()));
            dBUtil.removeRequest(dBUtil.getRequestByID(selectedRequest.requestID));
        }
    }

    private void updateTextBox(String string){
        lblRequestDetails.setText(string);
    }
}
