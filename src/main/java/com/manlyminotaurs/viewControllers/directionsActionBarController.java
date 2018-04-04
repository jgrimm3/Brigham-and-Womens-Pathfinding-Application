package com.manlyminotaurs.viewControllers;

import com.manlyminotaurs.core.Pathfinder;
import com.manlyminotaurs.databases.NodesEditor;
import com.manlyminotaurs.nodes.Node;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import com.manlyminotaurs.core.Main;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.shape.Path;

import javax.annotation.Resources;
import javax.xml.transform.stream.StreamSource;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class directionsActionBarController {

    public String building;
    public String type;
    String startLocation;
    String endLocation;
    Node startNode;
    Node endNode;
    NodesEditor node = new NodesEditor();
    Pathfinder pFind = new Pathfinder();
    List<Node> path;
    landingController land1;

    @FXML
    Label lblStart;

    @FXML
    Label lblEnd;

    @FXML
    Button btnSelectDestination;

    @FXML
    Button btnSelectStart;

    @FXML
    Button btnPathfind;

    @FXML
    ListView<String> lststartBuilding;

    @FXML
    ListView<String> lststartType;

    @FXML
    ListView<String> lststartLocation;
    @FXML
    ListView<String> lstendBuilding;

    @FXML
    ListView<String> lstendType;

    @FXML
    ListView<String> lstendLocation;


    public void checkStartEnd(ActionEvent event){

        if(lblEnd.getText().equals("End Location")) {
            lblEnd.setText("Please Select Destination");
        } else{
           land1.setController(land1);
            path = pFind.find(startNode, endNode);
            //land.printNodePath(path);
        }

    }

    public void getStartLocation(String startID){

        startNode = node.getNodeFromList(startID);
        startLocation = startNode.getLongName();
        lblStart.setText(startLocation);

    }
    public void getEndLocation(String endID){
        endNode = node.getNodeFromList(endID);
        endLocation = endNode.getLongName();
        lblEnd.setText(endLocation);
    }

    public void changeStartingLocation(ActionEvent event) {
        lstendBuilding.setVisible(false);
        lstendType.setVisible(false);
        lstendLocation.setVisible(false);

       lstendType.setItems(null);
        //lstendLocation.setItems(null);
        lstendBuilding.setItems(null);

        // allows user to select a location from either map or list of locations
        // which sets location to the start location

        node.retrieveNodes();
        lststartBuilding.setItems(node.getBuildingsFromList(node.getNodeList()));
        lststartBuilding.setVisible(true);

        lststartBuilding.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue)->lststartType.setItems(node.getTypesFromList(newValue, node.getNodeList())));
        lststartType.setVisible(true);

        lststartType.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> lststartLocation.setItems(node.getNodeFromList("Shapiro", newValue, node.getNodeList())));
        lststartLocation.setVisible(true);

        lststartLocation.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> getStartLocation(newValue));
        lststartBuilding.getSelectionModel().selectedItemProperty().removeListener(Object::notifyAll);
    }

    public void selectDestination(ActionEvent event) {
        lststartBuilding.setVisible(false);
        lststartType.setVisible(false);
        lststartLocation.setVisible(false);

        lststartType.setItems(null);
        //lststartLocation.setItems(null);
        lststartBuilding.setItems(null);
        // allows user to select a location from either map or list of locations
        // which sets location to the end location
        node.retrieveNodes();
        lstendBuilding.setItems(node.getBuildingsFromList(node.getNodeList()));
        lstendBuilding.setVisible(true);

        lstendBuilding.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue)->lstendType.setItems(node.getTypesFromList(newValue, node.getNodeList())));
        lstendType.setVisible(true);

        lstendType.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> lstendLocation.setItems(node.getNodeFromList("Shapiro", newValue, node.getNodeList())));
        lstendLocation.setVisible(true);

        lstendLocation.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> getEndLocation(newValue));

    }


}




