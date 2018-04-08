package com.manlyminotaurs.viewControllers;

import com.manlyminotaurs.databases.MessagesDBUtil;
import com.manlyminotaurs.databases.RequestsDBUtil;
import com.manlyminotaurs.messaging.Request;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import com.manlyminotaurs.core.Main;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class manageRequestsActionBarController implements Initializable{
    MessagesDBUtil msgUtil = new MessagesDBUtil();
    RequestsDBUtil reqUtil = new RequestsDBUtil();
    ObservableList<Request> reqestList = reqUtil.searchRequestBySender("user");
    ObservableList<requestInfo> openList = FXCollections.observableArrayList();
    ObservableList<requestInfo> closedList = FXCollections.observableArrayList();


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
    Label lblRequestDetails;
@FXML
    Button btnCompleteRequest;
@FXML
    Button btnSendToNurse;
@FXML
    Button btnDeleteRequest;
@FXML
    TableView tblOpenRequests;
@FXML
    TableView tblClosedRequests;

    /**
     * Instruct a nurse to help a user at their kiosk
     * @param event btnSendToNurse pressed
     */
    public void promptSendNurse(ActionEvent event){
        requestInfo selectedRequest = (requestInfo) tblOpenRequests.getSelectionModel().getSelectedItem();
        new userNrActionBarController().selectedRequestID = selectedRequest.requestID;
        Main.addPrompt(2); //go to nurse prompt
    }

    /**
     * Mark a service request completed
     * @param event btnCompleteRequest pressed
     */
    public void promptCompleteRequest(ActionEvent event){
        requestInfo selectedRequest = (requestInfo) tblOpenRequests.getSelectionModel().getSelectedItem();
        new userNrActionBarController().selectedRequestID = selectedRequest.requestID;
        System.out.println(new userNrActionBarController().selectedRequestID);
        Main.addPrompt(1); //go to complete request
    }

    //TODO: when removeRequest deletes the message remove my line that does that
    /**
     * Delete a service request before it is fulfilled
     * @param event btnDeleteRequest pressed
     */
    public void promptDeleteRequest(ActionEvent event) {
        Request reqToDelete = reqUtil.searchRequestsByID(((requestInfo) tblOpenRequests.getSelectionModel().getSelectedItem()).requestID);
        if(reqToDelete != null ){
            reqUtil.removeRequest(reqToDelete);
            msgUtil.removeMessage(msgUtil.getMessageByID(reqToDelete.getMessageID()));
        }
        refreshReqList(null);    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
        for(Request currReq : reqestList) {
            if (!currReq.getComplete()) {
                openList.add(new requestInfo(currReq.getRequestType(), msgUtil.getMessageByID(currReq.getMessageID()).getMessage(), currReq.getAdminConfirm(), currReq.getRequestID()));
            } else {
                closedList.add(new requestInfo(currReq.getRequestType(), msgUtil.getMessageByID(currReq.getMessageID()).getMessage(), currReq.getAdminConfirm(), currReq.getRequestID()));
            }
        }

        tblOpenRequests.setItems(openList);
        tblClosedRequests.setItems(closedList);
    }

    public void refreshReqList(ActionEvent event){
        reqestList = reqUtil.searchRequestBySender("user");
        System.out.println("Requests From User: " + reqestList.size());

        openList.clear();
        closedList.clear();

        for(Request currReq : reqestList) {
            if (!currReq.getComplete()) {
                openList.add(new requestInfo(currReq.getRequestType(), msgUtil.getMessageByID(currReq.getMessageID()).getMessage(), currReq.getAdminConfirm(), currReq.getRequestID()));
            } else {
                closedList.add(new requestInfo(currReq.getRequestType(), msgUtil.getMessageByID(currReq.getMessageID()).getMessage(), currReq.getAdminConfirm(), currReq.getRequestID()));
            }
        }

        System.out.println("Requests In List: " + openList.size());

        tblOpenRequests.setItems(openList);
        tblOpenRequests.refresh();

        tblClosedRequests.setItems(closedList);
        tblClosedRequests.refresh();
    }
}
