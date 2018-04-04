package com.manlyminotaurs.viewControllers;

import com.manlyminotaurs.core.Kiosk;
import com.manlyminotaurs.databases.MessagesDBUtil;
import com.manlyminotaurs.databases.RequestsDBUtil;
import com.manlyminotaurs.messaging.Message;
import com.manlyminotaurs.messaging.Request;
import com.manlyminotaurs.nodes.Node;
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
    String selectedRequestID;

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

    class requestInfo{
       // protected String requestID;
        String requestType;
        String message;

        requestInfo(String requestType, String message, String requestID){
            this.requestType = requestType;
            this.message = message;
        //    this.requestID = requestID;
        }
    }

    /**
     * Complete service requestS
     * @param event btnCompleteRequest pressed
     */
    public void promptCompleteRequest(ActionEvent event){
        requestInfo selectedRequest = (requestInfo) tblOpenRequests.getSelectionModel().getSelectedItem();
       // selectedRequestID = selectedRequest.requestID;
        System.out.println(selectedRequestID);
        Main.addPrompt(1); //go to complete request
    }

    /**
     *
     * @param event btnDeleteRequest pressed
     */
    public void promptDeleteRequest(ActionEvent event) {
        // assuming that a request has been selected from the table,
        // the request will be deleted
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

        tblOpenRequests.getColumns().addAll(typeCol, msgCol);

        typeCol.setCellValueFactory(new PropertyValueFactory<requestInfo, String>("requestType"));
        msgCol.setCellValueFactory(new PropertyValueFactory<requestInfo, String>("message"));

        for(Request currReq : reqestList) {
            finalList.add(new requestInfo(currReq.getRequestType(), msgUtil.getMessageFromList(currReq.getMessageID()).getMessage(), currReq.getRequestID()));
        }

        tblOpenRequests.setItems(finalList);
        tblOpenRequests.refresh();
    }

    public void refreshReqList(ActionEvent event){
        reqestList = reqUtil.searchRequestBySender("user");
        System.out.println("Requests From User: " + reqestList.size());
        tblOpenRequests.getItems().clear();

        for(Request currReq : reqestList) {
            System.out.println("Type: " + currReq.getRequestType() +" Message: " + msgUtil.getMessageFromList(currReq.getMessageID()).getMessage());
            tblOpenRequests.getItems().add(new requestInfo(currReq.getRequestType(), msgUtil.getMessageFromList(currReq.getMessageID()).getMessage(), currReq.getRequestID()));
        }

        System.out.println("Requests In List: " + finalList.size());

        tblOpenRequests.refresh();
    }
}