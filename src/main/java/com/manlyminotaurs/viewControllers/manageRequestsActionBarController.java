package com.manlyminotaurs.viewControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import com.manlyminotaurs.core.Main;

public class manageRequestsActionBarController {
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

    public void promptSendNurse(ActionEvent event){
        Main.addPrompt(2); //go to nurse prompt
    }
    public void promptCompleteRequest(ActionEvent event){
        Main.addPrompt(1); //go to complete request
    }
    public void promptDeleteRequest(ActionEvent event) {
        // allows user to delete existing request
    }

}
