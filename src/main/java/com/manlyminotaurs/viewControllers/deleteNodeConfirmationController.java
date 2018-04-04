package com.manlyminotaurs.viewControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import com.manlyminotaurs.core.Main;
import javafx.scene.control.Label;

public class deleteNodeConfirmationController {

    @FXML
    Label lblHeader;

    @FXML
    Label lblLongName;

    @FXML
    Button btnYes;

    @FXML
    Button btnNo;

    public void no(ActionEvent event) {
        Main.removePrompt(5); }

    public void yes(ActionEvent event) {
        Main.removePrompt(5);
    }

}
