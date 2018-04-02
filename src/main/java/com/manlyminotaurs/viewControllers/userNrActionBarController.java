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
    Button btnComleteRequest;
    @FXML
    TextArea txtCustomRequest;

    @FXML
    TableView tblOpenRequests;


    public void promptCompleteRequest(ActionEvent event){
        Main.addPrompt(1); //go to complete request
    }
}
