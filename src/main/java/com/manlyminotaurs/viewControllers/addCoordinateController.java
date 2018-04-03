package com.manlyminotaurs.viewControllers;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.manlyminotaurs.core.Main;

import javax.xml.soap.Text;
import java.awt.*;

public class addCoordinateController {

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
    public void addNode(ActionEvent event1){
        //put all fields from text boxes and update database

    }


}
