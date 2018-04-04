package com.manlyminotaurs.viewControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import com.manlyminotaurs.core.Main;


public class editNodesActionBarController {
    @FXML
    Button btnAddNode;
    @FXML
    Button btnDeleteNode;
    @FXML
    Button btnModifyNode;
    @FXML
    Button btnAddEdge;
    @FXML
    Button btnDeleteEdge;
    @FXML
    Button btnModifyEdge;


    public void addNode(ActionEvent event){
        Main.addPrompt(3); //prompt add node
    }

    public void deleteNode(ActionEvent event) {
        // allows user to select a node from either map or list of nodes
        // which deletes node at selection point
        // bring up confirmation after user selects node

        Main.addPrompt(6);
    }

    public void modifyNode(ActionEvent event) {
        // allows user to select a node from either map or list of nodes
        // which will bring up prompt to edit selected node

        Main.addPrompt(7);
    }

    public void addEdge(ActionEvent event) {
        // allows user to select 2 nodes
        // which will bring up a confirmation notice

        Main.addPrompt(8);
    }

    public void deleteEdge(ActionEvent event) {
        // allows user to select 2 nodes
        // to select an edge
        // which will bring up a confirmation notice
    }

    public void modifyEdge(ActionEvent event) {
        // allows user to modify edge
        // expand on description
    }
}
