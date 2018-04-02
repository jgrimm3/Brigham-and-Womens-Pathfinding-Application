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


    public void promptAddCoordinate(ActionEvent event){
        Main.addPrompt(3); //prompt add node
    }


}
