package com.manlyminotaurs.viewControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import com.manlyminotaurs.core.Main;


public class idleMapController {
    @FXML
    Button idleMap;

    //event listener on Map
    @FXML
    public void mapActive(ActionEvent event){
        Main.setScreen(1); //go to landing screen
    }

}
