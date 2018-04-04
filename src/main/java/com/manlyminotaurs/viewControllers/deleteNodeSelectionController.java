package com.manlyminotaurs.viewControllers;

import com.manlyminotaurs.core.Main;
import com.manlyminotaurs.databases.NodesEditor;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;


public class deleteNodeSelectionController {

    String nodeToBeDeleted;

    @FXML
    Label lblTitle;

    @FXML
    Button btnPathFind;

    @FXML
    Label lblNodeDeleted;

    @FXML
    Button btnSelectDestination;

    @FXML
    ListView<String> lstLocation;

    @FXML
    ListView<String> lstType;

    @FXML
    ListView<String> lstBuilding;

    @FXML
    Label lblLocation;

    @FXML
    Label lblType;

    @FXML
    Label lblBuilding;

    @FXML
    Button btnCancel;
/*
    public void initialize(URL location, ResourceBundle resources) {

    }
*/
    public void deleteNode(ActionEvent event) {
        // delete selected node
        Main.removePrompt(6);
    }

    public void getDeletedLocation(String location){
        nodeToBeDeleted = location;
        lblNodeDeleted.setText(location);

    }

    public void selectDestination(ActionEvent event) {
        lstBuilding.setVisible(false);
        lstType.setVisible(false);
        lstLocation.setVisible(false);

        lstType.setItems(null);
        //lstendLocation.setItems(null);
        lstBuilding.setItems(null);

        // allows user to select a location from either map or list of locations
        // which sets location to the start location
        NodesEditor node = new NodesEditor();
        node.retrieveNodes();
        lstBuilding.setItems(node.getBuildingsFromList(node.getNodeList()));
        lstBuilding.setVisible(true);

        lstBuilding.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue)->lstType.setItems(node.getTypesFromList(newValue, node.getNodeList())));
        lstType.setVisible(true);

        lstType.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> lstLocation.setItems(node.getNodeFromList("Shapiro", newValue, node.getNodeList())));
        lstLocation.setVisible(true);

        lstLocation.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> getDeletedLocation(newValue));
        lstBuilding.getSelectionModel().selectedItemProperty().removeListener(Object::notifyAll);
    }

    public void cancel(ActionEvent event) {
        Main.removePrompt(6);
    }



}
