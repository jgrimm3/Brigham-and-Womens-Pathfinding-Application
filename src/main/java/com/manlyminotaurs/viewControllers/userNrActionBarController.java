package com.manlyminotaurs.viewControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import com.manlyminotaurs.core.Main;


public class userNrActionBarController {
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


    public void promptCompleteRequest(ActionEvent event){
        Main.addPrompt(1); //go to complete request
    }

    public void promptDeleteRequest(ActionEvent event) {
        // assuming that a request has been selected from the table,
        // the request will be deleted
    }

    public void requestNurse(ActionEvent event) {
        // takes custom request information from text box below and sends a nurse request with the information to an admin
    }


}
