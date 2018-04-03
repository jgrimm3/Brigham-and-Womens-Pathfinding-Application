package com.manlyminotaurs.viewControllers;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.manlyminotaurs.core.Main;

import java.awt.*;

public class adminNurseSendController {

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
        Main.removePrompt(2);
    }

    public void sendNurse(ActionEvent event){
        if(txtNurse.getText().trim().isEmpty()){
            lblError.setText("Please Enter a Correct Nurse Name");
        }
        else{
            Main.removePrompt(2);
        }
    }

}