package com.manlyminotaurs.viewControllers;

import com.manlyminotaurs.core.Main;
import com.manlyminotaurs.databases.NodesEditor;
import com.manlyminotaurs.nodes.Node;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;



public class modifyNodeSelectorController {

    String nodeToBeModified;

    @FXML
    Label lblTitle;

    @FXML
    Button btnModifyNode;

    @FXML
    Label lblNodeModified;

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

    public void editNode(ActionEvent event) {

        NodesEditor nodeEditor = new NodesEditor();
        nodeEditor.modifyNodeStatus(nodeEditor.getNodeFromList(nodeToBeModified),0);

        System.out.println(nodeToBeModified);

        lstType.setItems(null);
        lstBuilding.setItems(null);
        lstLocation.setItems(null);
        lblNodeModified.setText("Node to be Modified");
        Main.removePrompt(7);
    }

    public void getDeletedLocation(String location){
        nodeToBeModified = location;
        lblNodeModified.setText(location);
        System.out.println("Text" + lblNodeModified.getText());
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
        lstType.setItems(null);
        lstBuilding.setItems(null);
        lstLocation.setItems(null);
        lblNodeModified.setText("Node to be Modified");
        Main.removePrompt(7);
    }
}
