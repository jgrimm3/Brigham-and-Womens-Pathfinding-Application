package com.manlyminotaurs.viewControllers;

import com.manlyminotaurs.core.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.xml.soap.Text;
import java.net.URL;
import java.util.ResourceBundle;

public class editCoordinateController implements Initializable {

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
    Label lblFloor;

    @FXML
    Label lblBuilding;

    @FXML
    ComboBox<String> comType;

    @FXML
    ComboBox<String> comFloor;

    @FXML
    ComboBox<String> comBuilding;

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

    // Population for ComboBoxes
    final static ObservableList<String> types = FXCollections.observableArrayList("HALL", "ELEV", "REST", "STAI", "DEPT", "LABS", "INFO", "CONF", "EXIT", "RETL", "SERV");
    final static ObservableList<String> floors = FXCollections.observableArrayList("L2", "L1", "1","2","3");
    final static ObservableList<String> buildings = FXCollections.observableArrayList("Shapiro");

    // Populate ComboBoxes
    public void initialize(URL location, ResourceBundle resources) {

        // add Type strings to combo box
        comType.setItems(types);
        comFloor.setItems(floors);
        comBuilding.setItems(buildings);
    }

    public void back(ActionEvent event) {

        // clear populated fields back to default
        txtLongName.clear();
        txtShortName.clear();
        txtXCoord.clear();
        txtYCoord.clear();
        comType.getSelectionModel().clearSelection();
        comFloor.getSelectionModel().clearSelection();
        comBuilding.getSelectionModel().clearSelection();

        Main.removePrompt(4);
    }
    public void saveNode(ActionEvent event1){

        // clear populated fields back to default
        txtLongName.clear();
        txtShortName.clear();
        txtXCoord.clear();
        txtYCoord.clear();
        comType.getSelectionModel().clearSelection();
        comFloor.getSelectionModel().clearSelection();
        comBuilding.getSelectionModel().clearSelection();

        //put all fields from text boxes and update database

        Main.removePrompt(4);
    }

}
