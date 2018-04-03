package com.manlyminotaurs.viewControllers;

import com.manlyminotaurs.core.Kiosk;
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

    RequestsDBUtil reqUtil = new RequestsDBUtil();
    Kiosk kiosk = new Kiosk();

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
        protected String requestID;
        String requestType;
        String message;

        requestInfo(String requestType, String message, String requestID){
            this.requestType = requestType;
            this.message = message;
            this.requestID = requestID;
        }
    }

    public void promptCompleteRequest(ActionEvent event){
        requestInfo selectedRequest = (requestInfo) tblOpenRequests.getSelectionModel().getSelectedItem();
        selectedRequestID = selectedRequest.requestID;
        System.out.println(selectedRequestID);
        Main.addPrompt(1); //go to complete request
    }

    public void promptDeleteRequest(ActionEvent event) {
        // assuming that a request has been selected from the table,
        // the request will be deleted
    }

    public void requestNurse(ActionEvent event) {
        System.out.println(txtCustomRequest.getText());
        reqUtil.addRequest("ReqNurse", 5, kiosk.getKioskLocation().getID(), txtCustomRequest.getText(), kiosk.getUser());
        txtCustomRequest.clear();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<Request> reqestList = reqUtil.searchRequest("user");

        ObservableList<requestInfo> finalList = FXCollections.observableArrayList();

        for(Request currReq : reqestList) {
            finalList.add(new requestInfo(currReq.getRequestType(), msgUtil.searchMsg(currReq.getMessageID()).message, currReq.getRequestID()));
        }

        TableColumn typeCol = new TableColumn("Request Type");
        TableColumn msgCol = new TableColumn("Request Message");

        tblOpenRequests.getColumns().addAll(typeCol, msgCol);

        typeCol.setCellValueFactory(new PropertyValueFactory<requestInfo, String>("requestType"));
        msgCol.setCellValueFactory(new PropertyValueFactory<requestInfo, String>("message"));

        tblOpenRequests.setItems(finalList);
    }
}
