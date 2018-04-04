package com.manlyminotaurs.viewControllers;

import com.manlyminotaurs.databases.NodesEditor;
import com.manlyminotaurs.nodes.Node;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import com.manlyminotaurs.core.Main;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.annotation.Resource;
import javax.swing.*;
import javax.xml.soap.Text;
import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class addCoordinateController implements Initializable{

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
    ComboBox<String> comType;

    @FXML
    ComboBox<String> comFloor;

    @FXML
    ComboBox<String> comBuilding;

    @FXML
    Label lblFloor;

    @FXML
    Label lblBuilding;

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

    @FXML
    Label lblNodeInfo;

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

        Main.removePrompt(3);



        // if some fields are populated, then back will send confirmation notice that data will be deleted
    }

    public void addNode(ActionEvent event1) {

        NodesEditor nodeEditor = new NodesEditor();
        nodeEditor.addNode(txtLongName.getText(),txtShortName.getText(),comType.getValue(),Integer.parseInt(txtXCoord.getText()),Integer.parseInt(txtYCoord.getText()),comFloor.getValue(),comBuilding.getValue());

        // clear populated fields back to default
        txtLongName.clear();
        txtShortName.clear();
        txtXCoord.clear();
        txtYCoord.clear();
        comType.getSelectionModel().clearSelection();
        comFloor.getSelectionModel().clearSelection();
        comBuilding.getSelectionModel().clearSelection();

        //lblNodeInfo.setText(nodeEditor.generateNodeID("G",comType.getValue(), comFloor.getValue(),"A" ));

    }


}
