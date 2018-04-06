package com.manlyminotaurs.viewControllers;

import com.manlyminotaurs.core.Main;
import com.manlyminotaurs.databases.NodesDBUtil;
import com.manlyminotaurs.nodes.Node;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class addEdgeSelectionController {

    String node1;
    String node2;

    @FXML
    Label lblTitle;

    @FXML
    Button btnAddEdge;

    @FXML
    Button btnCancel;

    @FXML
    Label lblNode1;

    @FXML
    Label lblNode2;

    @FXML
    Button btnGetCurrentNodes1;

    @FXML
    Button btnGetCurrentNodes2;

    @FXML
    ListView<String> lstLocation1;

    @FXML
    ListView<String> lstBuilding1;

    @FXML
    ListView<String> lstType1;

    @FXML
    ListView<String> lstLocation2;

    @FXML
    ListView<String> lstBuilding2;

    @FXML
    ListView<String> lstType2;

    @FXML
    Label lblBuilding1;

    @FXML
    Label lblType1;

    @FXML
    Label lblLocation1;

    @FXML
    Label lblBuilding2;

    @FXML
    Label lblType2;

    @FXML
    Label lblLocation2;

    public void cancel(ActionEvent event) {

        lstType1.setItems(null);
        lstBuilding1.setItems(null);
        lstLocation1.setItems(null);
        lblNode1.setText("Node 1");
        lstType2.setItems(null);
        lstBuilding2.setItems(null);
        lstLocation2.setItems(null);
        lblNode2.setText("Node 2");

        Main.removePrompt(8);
    }

    public void addEdge(ActionEvent event) {

        NodesDBUtil nodesDBUtil = new NodesDBUtil();
        Node startNode = nodesDBUtil.getNodeFromList(node1);
        Node endNode = nodesDBUtil.getNodeFromList(node2);
        nodesDBUtil.addEdge(startNode,endNode);

        lstType1.setItems(null);
        lstBuilding1.setItems(null);
        lstLocation1.setItems(null);
        lblNode1.setText("Node 1");
        lstType2.setItems(null);
        lstBuilding2.setItems(null);
        lstLocation2.setItems(null);
        lblNode2.setText("Node 2");

        Main.removePrompt(8);
    }

    public void getNode1(String location){
        node1 = location;
        lblNode1.setText(location);
    }

    public void getNode2(String location){
        node2 = location;
        lblNode2.setText(location);
    }

    public void loadNodes1(ActionEvent event) {
        lstBuilding1.setVisible(false);
        lstType1.setVisible(false);
        lstLocation1.setVisible(false);

        lstType1.setItems(null);
        //lstendLocation.setItems(null);
        lstBuilding1.setItems(null);

        // allows user to select a location from either map or list of locations
        // which sets location to the start location
        NodesDBUtil node = new NodesDBUtil();
        node.retrieveNodes();
        lstBuilding1.setItems(node.getBuildingsFromList(node.getNodeList()));
        lstBuilding1.setVisible(true);

        lstBuilding1.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue)->lstType1.setItems(node.getTypesFromList(newValue, node.getNodeList())));
        lstType1.setVisible(true);

        lstType1.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> lstLocation1.setItems(node.getNodeFromList("Shapiro", newValue, node.getNodeList())));
        lstLocation1.setVisible(true);

        lstLocation1.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> getNode1(newValue));
        lstBuilding1.getSelectionModel().selectedItemProperty().removeListener(Object::notifyAll);
    }

    public void loadNodes2(ActionEvent event) {
        lstBuilding2.setVisible(false);
        lstType2.setVisible(false);
        lstLocation2.setVisible(false);

        lstType2.setItems(null);
        //lstendLocation.setItems(null);
        lstBuilding2.setItems(null);

        // allows user to select a location from either map or list of locations
        // which sets location to the start location
        NodesDBUtil node = new NodesDBUtil();
        node.retrieveNodes();
        lstBuilding2.setItems(node.getBuildingsFromList(node.getNodeList()));
        lstBuilding2.setVisible(true);

        lstBuilding2.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue)->lstType2.setItems(node.getTypesFromList(newValue, node.getNodeList())));
        lstType2.setVisible(true);

        lstType2.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> lstLocation2.setItems(node.getNodeFromList("Shapiro", newValue, node.getNodeList())));
        lstLocation2.setVisible(true);

        lstLocation2.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> getNode2(newValue));
        lstBuilding2.getSelectionModel().selectedItemProperty().removeListener(Object::notifyAll);
    }

}
