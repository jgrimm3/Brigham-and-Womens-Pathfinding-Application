package com.manlyminotaurs.viewControllers;

import com.manlyminotaurs.core.Kiosk;
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
    ObservableList<requestInfo> finalList = FXCollections.observableArrayList();


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
            msgUtil.removeMessage(msgUtil.getMessageFromList(reqToDelete.getMessageID()));
        }
        refreshReqList(null);    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TableColumn typeCol = new TableColumn("Request Type");
        TableColumn msgCol = new TableColumn("Request Message");
        TableColumn isAssignedCol = new TableColumn("Is Assigned");

        tblOpenRequests.getColumns().addAll(typeCol, msgCol, isAssignedCol);

        typeCol.setCellValueFactory(new PropertyValueFactory<requestInfo, String>("requestType"));
        msgCol.setCellValueFactory(new PropertyValueFactory<requestInfo, String>("message"));
        isAssignedCol.setCellValueFactory(new PropertyValueFactory<requestInfo, Boolean>("isAssigned"));

        for(Request currReq : reqestList) {
            finalList.add(new requestInfo(currReq.getRequestType(), msgUtil.getMessageFromList(currReq.getMessageID()).getMessage(), currReq.getAdminConfirm(), currReq.getRequestID()));
        }

        tblOpenRequests.setItems(finalList);
    }

    public void refreshReqList(ActionEvent event){
        reqestList = reqUtil.searchRequestBySender("user");
        System.out.println("Requests From User: " + reqestList.size());
        finalList.clear();

        for(Request currReq : reqestList) {
            if (!currReq.getComplete()) {
                finalList.add(new requestInfo(currReq.getRequestType(), msgUtil.getMessageFromList(currReq.getMessageID()).getMessage(), currReq.getAdminConfirm(), currReq.getRequestID()));
            }
        }

        System.out.println("Requests In List: " + finalList.size());
        tblOpenRequests.setItems(finalList);
        tblOpenRequests.refresh();
    }
}
