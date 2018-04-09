package com.manlyminotaurs.viewControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import javax.swing.*;
import javax.xml.soap.Text;
import java.net.URL;
import java.util.ResourceBundle;

public class homeController implements Initializable {


    public void initialize(URL location, ResourceBundle resources) {

        // set comboboxes for buildings to default lists
        // comBuildingStart.setItems(types);
        // comBuildingEnd.setItems(floors);
        comFloorStart.setDisable(true);
        comFloorEnd.setDisable(true);
        comTypeStart.setDisable(true);
        comTypeEnd.setDisable(true);
    }

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

        if (tglHandicap.isSelected()) {

            // Switch off
            tglHandicap.setText("Off");

        } else {

            // Switch on
            tglHandicap.setText("On");
        }
    }

    public void toggleMap(ActionEvent event) {

        if (tglMap.isSelected()) {

            // Switch 2-D
            tglMap.setText("2-D");

        } else {

            // Switch 3-D
            tglMap.setText("3-D");
        }

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

        // Draw path code

        if (tglHandicap.isSelected()) {
            // use elevator

            if (tglMap.isSelected()) {
                // use 3-D

            } else {
                // use 2-D
            }

        } else {
            // use stairs

            if (tglMap.isSelected()) {
                // use 3-D

            } else {
                // use 2-D

            }

        }

        // Clear old fields

        // Show directions interface and hide pathfinding interface
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

        // Generate QR Code

        // Load QR Code into panel

        // Show QR code
        paneQRCode.setVisible(true);

    }

    public void restartNavigation(ActionEvent event) {


        // Clear Fields
        comBuildingStart.getSelectionModel().clearSelection();


        // Disable Fields
        comFloorStart.setDisable(true);
        comFloorEnd.setDisable(true);
        comTypeStart.setDisable(true);
        comTypeEnd.setDisable(true);

        // Show pathfinding interface and hide directions interface
        panePathfinding.setVisible(true);
        paneDirections.setVisible(false);


    }

    public void closeQRCodePanel(ActionEvent event) {

        // Hide QR code
        paneQRCode.setVisible(false);
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

        if (btnQuickBathroom.isVisible() == true) {

            btnQuickBathroom.setVisible(false);
            btnQuickCafe.setVisible(false);
            btnQuickCoffee.setVisible(false);
            btnQuickShop.setVisible(false);

        } else if (btnQuickBathroom.isVisible() == false) {

            btnQuickBathroom.setVisible(true);
            btnQuickCafe.setVisible(true);
            btnQuickCoffee.setVisible(true);
            btnQuickShop.setVisible(true);

        }
    }

    public void getXandY(ActionEvent event){

    }

    public void findQuickBathroom(ActionEvent event) {

        // Pathfind to nearest bathroom

    }

    public void findQuickCafe(ActionEvent event) {

        // Pathfind to nearest cafe

    }

    public void findQuickCoffee(ActionEvent event) {

        // Pathfind to nearest coffee shop

    }

    public void findQuickShop(ActionEvent event) {

        // Pathfind to nearest gift shop
    }

    // Help
    @FXML
    Button btnHelp;

    @FXML
    Pane paneHelp;

    @FXML
    Button btnCloseHelp;

    @FXML
    Label lblDetails;

    @FXML
    Label lblHelp;


    public void openHelpPanel(ActionEvent event) {

        // Show help panel
        paneHelp.setVisible(true);
    }

    public void closeHelpPanel(ActionEvent event) {

        // Hide help panel
        paneHelp.setVisible(false);
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

        // Show login panel
        paneLogin.setVisible(true);
    }

    public void closeLoginPanel(ActionEvent event) {

        // Hide login panel
        paneLogin.setVisible(false);
    }

    public void login(ActionEvent event) {

        if (txtUsername.getText().equals("") || txtPassword.getText().equals("")) {

            // print message
            System.out.println("Please completely fill in the username and password fields");

        } else if (txtUsername.getText().equals("user") && txtPassword.getText().equals("password")){

            // print message
            System.out.println("User logged in");

        } else {

            // print message
            System.out.println("Wrong username and password!");

        }
    }



}
