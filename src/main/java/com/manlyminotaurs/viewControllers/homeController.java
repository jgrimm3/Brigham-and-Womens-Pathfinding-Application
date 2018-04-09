package com.manlyminotaurs.viewControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import javax.swing.*;
import javax.xml.soap.Text;

public class homeController {

    // Pathfinding Panel
    @FXML
    Pane panePathfinding;

    @FXML
    Label lblHandicap;

    @FXML
    ToggleButton tglHandicap;

    @FXML
    Label lblMap;

    @FXML
    ToggleButton tglMap;

    @FXML
    Button btnStart;

    @FXML
    Label lblBuildingStart;

    @FXML
    ComboBox<String> comBuildingStart;

    @FXML
    Label lblFloorStart;

    @FXML
    ComboBox<String> comFloorStart;

    @FXML
    Label lblTypeStart;

    @FXML
    ComboBox<String> comTypeStart;

    @FXML
    Label lblLocationStart;

    @FXML
    ListView<String> lstLocationStart;

    @FXML
    Button btnEnd;

    @FXML
    Label lblBuildingEnd;

    @FXML
    ComboBox<String> comBuildingEnd;

    @FXML
    Label lblFloorEnd;

    @FXML
    ComboBox<String> comFloorEnd;

    @FXML
    Label lblTypeEnd;

    @FXML
    ComboBox<String> comTypeEnd;

    @FXML
    Label lblLocationEnd;

    @FXML
    ListView<String> lstLocationEnd;

    @FXML
    Pane paneStartLocation;

    @FXML
    Label lblStartLocation;

    @FXML
    Pane paneEndLocation;

    @FXML
    Label lblEndLocation;


    public void toggleHandicap(ActionEvent event) {

    }

    public void toggleMap(ActionEvent event) {

    }

    public void chooseStartNode(ActionEvent event) {

    }

    public void chooseEndNode(ActionEvent event) {

    }

    public void initializeBuildingStart(ActionEvent event) {

    }

    public void initializeFloorStart(ActionEvent event) {

    }

    public void initializeTypeStart(ActionEvent event) {

    }

    public void initializeBuildingEnd(ActionEvent event) {

    }

    public void initializeFloorEnd(ActionEvent event) {

    }

    public void initializeTypeEnd(ActionEvent event) {

    }

    public void drawPath(ActionEvent event) {
        panePathfinding.setVisible(false);
        paneDirections.setVisible(true);
    }

    // Directions Panel
    @FXML
    Pane paneDirections;

    @FXML
    Label lblStartLocation1;

    @FXML
    Label lblEndLocation1;

    @FXML
    Button btnOpenQRCode;

    @FXML
    ListView<String> lstDirections;

    @FXML
    Button btnRestart;

    @FXML
    Pane paneQRCode;

    @FXML
    Button btnCloseQRCode;

    @FXML
    ImageView imgQRCode;

    public void openQRCodePanel(ActionEvent event) {

    }

    public void restartNavigation(ActionEvent event) {
        panePathfinding.setVisible(true);
        paneDirections.setVisible(false);
    }

    public void closeQRCodePanel(ActionEvent event) {

    }


    // Quick Directions
    @FXML
    Button btnQuickDirections;

    @FXML
    Button btnQuickBathroom;

    @FXML
    Button btnQuickCafe;

    @FXML
    Button btnQuickCoffee;

    @FXML
    Button btnQuickShop;

    public void toggleQuickButtons(ActionEvent event) {

    }

    public void findQuickBathroom(ActionEvent event) {

    }

    public void findQuickCafe(ActionEvent event) {

    }

    public void findQuickCoffee(ActionEvent event) {

    }

    public void findQuickShop(ActionEvent event) {

    }

    // Help
    @FXML
    Button btnHelp;

    public void getHelp(ActionEvent event) {

    }

    // Login
    @FXML
    Pane paneLogin;

    @FXML
    Button btnOpenLogin;

    @FXML
    Button btnCloseLogin;

    @FXML
    Button btnLogin;

    @FXML
    TextField txtUsername;

    @FXML
    TextField txtPassword;

    public void openLoginPanel(ActionEvent event) {

    }

    public void closeLoginPanel(ActionEvent event) {

    }

    public void login(ActionEvent event) {

    }



}
