package com.manlyminotaurs.viewControllers;

import com.manlyminotaurs.databases.MessagesDBUtil;
import com.manlyminotaurs.databases.RequestsDBUtil;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import com.manlyminotaurs.core.Main;

public class adminNurseSendController {
    MessagesDBUtil msgUtil = new MessagesDBUtil();
    RequestsDBUtil reqUtil = new RequestsDBUtil();

    @FXML
    Button btnBack;

    @FXML
    Label lblTitle;

    @FXML
    Label lblInfoTitle;

    @FXML
    Label lblType;

    @FXML
    Label lblLocation;

    @FXML
    Label lblNurseName;

    @FXML
    TextField txtNurse;

    @FXML
    Button btnSendNurse;

    @FXML
    Label lblError;

    public void back(ActionEvent event) {
        lblError.setText("");
        txtNurse.clear();
        Main.removePrompt(2);
    }

    public void sendNurse(ActionEvent event){
        if(txtNurse.getText().trim().isEmpty()){
            lblError.setText("Please Enter a Correct Nurse Name");
        }
        else{
            String selectedRequestID = new userNrActionBarController().selectedRequestID;
            msgUtil.getMessageByID(reqUtil.searchRequestsByID(selectedRequestID).getMessageID()).setReceiverID(txtNurse.getText());
            reqUtil.searchRequestsByID(selectedRequestID).setAdminConfirm(true);
            back(null);
        }
    }


    public void start(){
        System.out.println("Loaded");
    }

}