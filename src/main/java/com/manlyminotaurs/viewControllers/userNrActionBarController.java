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
import javafx.scene.control.*;
import com.manlyminotaurs.core.Main;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;


public class userNrActionBarController implements Initializable{
    static String selectedRequestID;

    MessagesDBUtil msgUtil = new MessagesDBUtil();
    RequestsDBUtil reqUtil = new RequestsDBUtil();
    Kiosk kiosk = new Kiosk();
    ObservableList<Request> reqestList = reqUtil.searchRequestBySender("user");
    ObservableList<requestInfo> finalList = FXCollections.observableArrayList();

    @FXML
    Label lblDoctorInfo;

    @FXML
    Button btnReqNurse;

    @FXML
    Button btnSendCustom;

    @FXML
    Button btnCompleteRequest;

    @FXML
    Button btnDeleteRequest;

    @FXML
    TextArea txtCustomRequest;

    @FXML
    TableView tblOpenRequests;

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

    /**
     * Complete service requestS
     * @param event btnCompleteRequest pressed
     */
    public void promptCompleteRequest(ActionEvent event){
        requestInfo selectedRequest = (requestInfo) tblOpenRequests.getSelectionModel().getSelectedItem();
        selectedRequestID = selectedRequest.requestID;
        System.out.println(selectedRequestID);
        Main.addPrompt(1); //go to complete request
    }

    //TODO: when removeRequest deletes the message remove my line that does that
    /**
     *
     * @param event btnDeleteRequest pressed
     */
    public void promptDeleteRequest(ActionEvent event) {
        Request reqToDelete = reqUtil.searchRequestsByID(((requestInfo) tblOpenRequests.getSelectionModel().getSelectedItem()).requestID);
        if(reqToDelete != null ){
            reqUtil.removeRequest(reqToDelete);
            msgUtil.removeMessage(msgUtil.getMessageByID(reqToDelete.getMessageID()));
        }
        refreshReqList(null);
    }

    /**
     *
     * @param event btnReqNurse pressed
     */
    public void requestNurse(ActionEvent event) {
        System.out.println(txtCustomRequest.getText());
        reqUtil.addRequest("ReqNurse", 5, kiosk.getKioskLocation().getID(), txtCustomRequest.getText(), kiosk.getUser());
        txtCustomRequest.clear();
    }


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
            finalList.add(new requestInfo(currReq.getRequestType(), msgUtil.getMessageByID(currReq.getMessageID()).getMessage(), currReq.getAdminConfirm(), currReq.getRequestID()));
        }

        tblOpenRequests.setItems(finalList);
//        tblOpenRequests.refresh();
    }

    public void refreshReqList(ActionEvent event){
        reqestList = reqUtil.searchRequestBySender("user");
        System.out.println("Requests From User: " + reqestList.size());
        finalList.clear();

        for(Request currReq : reqestList) {
            if (!currReq.getComplete()) {
                finalList.add(new requestInfo(currReq.getRequestType(), msgUtil.getMessageByID(currReq.getMessageID()).getMessage(), currReq.getAdminConfirm(), currReq.getRequestID()));
            }
        }

        System.out.println("Requests In List: " + finalList.size());
        tblOpenRequests.setItems(finalList);
        tblOpenRequests.refresh();
    }
}