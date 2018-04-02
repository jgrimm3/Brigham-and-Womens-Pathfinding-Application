package com.manlyminotaurs.viewControllers;

import com.manlyminotaurs.core.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class completeRequestController {

    @FXML
    Button btnHide;

    @FXML
    Label lblTitle;

    @FXML
    Label lblInstructions;

    @FXML
    Label lblConfirmationCode;

    @FXML
    TextField txtConfirmationCode;

    @FXML
    Button btnCompleteRequest;
    @FXML
    Label lblError;

    public void back(ActionEvent event) {
        Main.removePrompt(1);
    }

    public void completeRequest(ActionEvent event) {
        if (txtConfirmationCode.getText().trim().isEmpty()) {
            lblError.setText("Please Enter a Correct Confirmation Code");
        } else {
            Main.removePrompt(1);
        }
    }
}
