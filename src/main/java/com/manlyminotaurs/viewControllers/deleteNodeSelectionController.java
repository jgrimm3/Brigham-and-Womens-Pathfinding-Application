package com.manlyminotaurs.viewControllers;

import com.manlyminotaurs.core.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;


public class deleteNodeSelectionController implements Initializable {

    @FXML
    Label lblTitle;

    @FXML
    Button btnPathFind;

    @FXML
    Label lblNodeDeleted;

    @FXML
    Button btnSelectDestination;

    @FXML
    ListView lstLocation;

    @FXML
    ListView lstType;

    @FXML
    ListView lstBuilding;

    @FXML
    Label lblLocation;

    @FXML
    Label lblType;

    @FXML
    Label lblBuilding;

    @FXML
    Button btnCancel;

    public void initialize(URL location, ResourceBundle resources) {

    }

    public void deleteNode(ActionEvent event) {
        Main.removePrompt(6);
    }

    public void selectDestination(ActionEvent event) {

    }

    public void cancel(ActionEvent event) {
        Main.removePrompt(6);
    }



}
