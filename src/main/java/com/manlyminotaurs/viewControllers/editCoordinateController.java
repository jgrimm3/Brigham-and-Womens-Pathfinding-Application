package com.manlyminotaurs.viewControllers;

import com.manlyminotaurs.core.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.xml.soap.Text;

public class editCoordinateController {

    @FXML
    Button btnBack;

    @FXML
    Label lblTitle;

    @FXML
    Label lblLongName;

    @FXML
    TextField txtLongName;

    @FXML
    Label lblShortName;

    @FXML
    TextField txtShortName;

    @FXML
    Label lblType;

    @FXML
    TextField txtType;

    @FXML
    Label lblFloor;

    @FXML
    TextField txtFloor;

    @FXML
    Label lblBuilding;

    @FXML
    TextField txtBuilding;

    @FXML
    Label lblXCoord;

    @FXML
    TextField txtXCoord;

    @FXML
    Label lblYCoord;

    @FXML
    TextField txtYCoord;

    @FXML
    Button btnChooseCoord;

    public void back(ActionEvent event) {
        Main.removePrompt(3);
    }
    public void saveNode(ActionEvent event1){
        //put all fields from text boxes and update database

    }

}
