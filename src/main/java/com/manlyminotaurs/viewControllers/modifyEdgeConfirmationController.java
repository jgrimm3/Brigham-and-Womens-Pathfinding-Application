package com.manlyminotaurs.viewControllers;

import com.manlyminotaurs.core.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class modifyEdgeConfirmationController {

    @FXML
    Label lblHeader;

    @FXML
    Button btnYes;

    @FXML
    Button btnNo;

    public void no(ActionEvent event) {
        Main.removePrompt(7);
    } // cancel functionality

    public void yes(ActionEvent event) {
        Main.removePrompt(7);
    } // needs to execute certain functionality
}
