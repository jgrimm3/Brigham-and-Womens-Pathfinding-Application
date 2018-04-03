package com.manlyminotaurs.viewControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import com.manlyminotaurs.core.Main;
import javafx.scene.control.TableView;

public class directionsActionBarController {
    @FXML
    Button btnSelectDestination;

    @FXML
    Button btnSelectStart;

    @FXML
    Button btnPathfind;

    @FXML
    TableView tblLocation;

    public void changeStartingLocation(ActionEvent event) {
        // allows user to select a location from either map or list of locations
        // which sets location to the start location
    }

    public void selectDestination(ActionEvent event1) {
        // allows user to select a location from either map or list of locations
        // which sets location to the destination location
    }


}
