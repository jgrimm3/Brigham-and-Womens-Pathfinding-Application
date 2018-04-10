package com.manlyminotaurs.viewControllers;

import com.manlyminotaurs.nodes.INode;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import javax.swing.*;
import javax.xml.soap.Text;
import java.net.URL;
import java.util.ResourceBundle;

public class homeController {

    // Test Objects
    final static ObservableList<String> types = FXCollections.observableArrayList("HALL", "ELEV", "REST", "STAI", "DEPT", "LABS", "INFO", "CONF", "EXIT", "RETL", "SERV");
    final static ObservableList<String> floors = FXCollections.observableArrayList("L2", "L1", "1","2","3");
    final static ObservableList<String> buildings = FXCollections.observableArrayList("Shapiro", "Jank", "Somerset");
    final static ObservableList<String> locations = FXCollections.observableArrayList("thePlace", "Jerry's house", "another place", "wong's house", "fdskjfas", "fsdfds", "Dfsd","sfdd","SFd");
    final static ObservableList<String> finalFloors = FXCollections.observableArrayList("BUILDING: SHAPIRO | FLOOR: L2", "BUILDING: SHAPIRO | FLOOR: L1", "BUILDING: SHAPIRO | FLOOR: 1","BUILDING: SHAPIRO | FLOOR: 2","BUILDING: SHAPIRO | FLOOR: 3");
    final static ObservableList<String> empty = FXCollections.observableArrayList();


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
    ComboBox<String> comLocationStart;

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
    ComboBox<String> comLocationEnd;

    @FXML
    Pane paneStartLocation;

    @FXML
    Label lblStartLocation;

    @FXML
    Pane paneEndLocation;

    @FXML
    Label lblEndLocation;

    @FXML
    ImageView mapImg;

    @FXML
    ComboBox<String> comChangeFloor;
    @FXML
    public void initialize() {

        // Set comboboxes for buildings to default lists
        comBuildingStart.setItems(buildings);
        comBuildingEnd.setItems(buildings);
        comFloorStart.setDisable(true);
        comFloorEnd.setDisable(true);
        comTypeStart.setDisable(true);
        comTypeEnd.setDisable(true);
        comChangeFloor.setItems(finalFloors);
        comLocationStart.setDisable(true);
        comLocationEnd.setDisable(true);

        paneDirections.setVisible(false);
        panePathfinding.setVisible(true);
        paneLogin.setVisible(false);
        paneHelp.setVisible(false);
        lblHelp1.setVisible(false);
        lblHelp2.setVisible(false);

        txtUsername.setText("");
        txtPassword.setText("");

        comChangeFloor.getSelectionModel().select(0);

        // Remember to refresh nodes

        // Set Floor Map and Floor Combobox to correct setting
    }

    public void toggleHandicap(ActionEvent event) {

        if (tglHandicap.isSelected()) {

            // Switch on
            tglHandicap.setText("ON");

        } else {

            // Switch off
            tglHandicap.setText("OFF");
        }
    }

    public void toggleMap(ActionEvent event) {

        if (tglMap.isSelected()) {

            // Switch 3-D
            tglMap.setText("3-D");
            lblMap.setText("MAP: 3-D");

            // !!!
            if(comChangeFloor.getValue().equals("BUILDING: SHAPIRO | FLOOR: L2")) {
                mapImg.setImage(new Image("/MapImages/L2-ICONS"));
            } else if(comChangeFloor.getValue().equals("BUILDING: SHAPIRO | FLOOR: L1")) {
                mapImg.setImage(new Image("/MapImages/L2-ICONS"));
            } else if(comChangeFloor.getValue().equals("BUILDING: SHAPIRO | FLOOR: 1")) {
                mapImg.setImage(new Image("/MapImages/L2-ICONS"));
            } else if(comChangeFloor.getValue().equals("BUILDING: SHAPIRO | FLOOR: 2")) {
                mapImg.setImage(new Image("/MapImages/L2-ICONS"));
            } else if(comChangeFloor.getValue().equals("BUILDING: SHAPIRO | FLOOR: 3")) {
                mapImg.setImage(new Image("/MapImages/L2-ICONS"));
            }

        } else {

            // Switch 2-D
            tglMap.setText("2-D");
            lblMap.setText("MAP: 2-D");

            // !!!
            if(comChangeFloor.getValue().equals("BUILDING: SHAPIRO | FLOOR: L2")) {
                mapImg.setImage(new Image("/MapImages/L2-ICONS"));
            } else if(comChangeFloor.getValue().equals("BUILDING: SHAPIRO | FLOOR: L1")) {
                mapImg.setImage(new Image("/MapImages/L2-ICONS"));
            } else if(comChangeFloor.getValue().equals("BUILDING: SHAPIRO | FLOOR: 1")) {
                mapImg.setImage(new Image("/MapImages/L2-ICONS"));
            } else if(comChangeFloor.getValue().equals("BUILDING: SHAPIRO | FLOOR: 2")) {
                mapImg.setImage(new Image("/MapImages/L2-ICONS"));
            } else if(comChangeFloor.getValue().equals("BUILDING: SHAPIRO | FLOOR: 3")) {
                mapImg.setImage(new Image("/MapImages/L2-ICONS"));
            }
        }





    }

    public void chooseStartNode(ActionEvent event) {

    }

    public void chooseEndNode(ActionEvent event) {

    }

    public void initializeBuildingStart(ActionEvent event) {

        System.out.println("Start Building: " + comBuildingStart.getValue());

        // Set floor depending on building
        comFloorStart.setItems(floors); // eventually set depending on building

        // Able Comboboxes
        comFloorStart.setDisable(false);

        // Clear Past Selections
        comFloorStart.getSelectionModel().clearSelection();
        comTypeStart.getSelectionModel().clearSelection();
        comLocationStart.getSelectionModel().clearSelection();

        // Clear Lists in Objects
        comTypeStart.setItems(empty);
        comLocationStart.setItems(empty);

        // Disable Objects
        comTypeStart.setDisable(true);
        comLocationStart.setDisable(true);

        // Set Start Location Label to Default
        lblStartLocation.setText("START LOCATION");

    }

    public void initializeFloorStart(ActionEvent event) {

        System.out.println("Start Floor: " + comFloorStart.getValue());

        // Set types depending on floor
        comTypeStart.setItems(types); // eventually set depending on floor

        // Able Objects
        comTypeStart.setDisable(false);

        // Clear Past Selection
        comTypeStart.getSelectionModel().clearSelection();
        comLocationStart.getSelectionModel().clearSelection();

        // Disable Objects
        comLocationStart.setDisable(true);

        // Set Start Location Label to Default
        lblStartLocation.setText("START LOCATION");

    }

    public void initializeTypeStart(ActionEvent event) {

        System.out.println("Start Type: " + comTypeStart.getValue());

        /*
        // Set up table
        TableColumn startLocations = new TableColumn("Start Locations");
        startLocations.setCellValueFactory(new PropertyValueFactory<NodeLocation, String>("LocationName"));
        ObservableList<NodeLocation> newStartlocations = FXCollections.observableArrayList();

        for(NodeLocation curLocation: tableLocations) {
            newStartlocations.add(new NodeLocation(curLocation.getLocationName()));
        }

        tblLocationStart.setItems(newStartlocations);
        tblLocationStart.getColumns().addAll(startLocations);
        tblLocationStart.refresh();

        // Set types depending on floor
        //tblColLocationsStart.setCellFactory(locations); // eventually set depending on type
        //tblLocationStart.refresh();
        */

        // Set types depending on floor
        comLocationStart.setItems(locations);

        // Able Objects
        comLocationStart.setDisable(false);

        // Clear Past Selection
        comLocationStart.getSelectionModel().clearSelection();

        // Set Start Location Label to Default
        lblStartLocation.setText("START LOCATION");

    }

    public void initializeBuildingEnd(ActionEvent event) {

        System.out.println("End Building: " + comBuildingEnd.getValue());

        // Set floor depending on building
        comFloorEnd.setItems(floors); // eventually set depending on building

        // Able Comboboxes
        comFloorEnd.setDisable(false);

        // Clear Past Selections
        comFloorEnd.getSelectionModel().clearSelection();
        comTypeEnd.getSelectionModel().clearSelection();
        comLocationEnd.getSelectionModel().clearSelection();

        // Clear Lists in Objects
        comTypeEnd.setItems(empty);

        comLocationEnd.setItems(empty);

        // Disable Objects
        comTypeEnd.setDisable(true);
        comLocationEnd.setDisable(true);

        // Set End Location Label to Default
        lblEndLocation.setText("END LOCATION");

    }

    public void initializeFloorEnd(ActionEvent event) {

        System.out.println("End Floor: " + comFloorEnd.getValue());

        // Set types depending on floor
        comTypeEnd.setItems(types); // eventually set depending on floor

        // Able Objects
        comTypeEnd.setDisable(false);

        // Clear Past Selection
        comTypeEnd.getSelectionModel().clearSelection();
        comLocationEnd.getSelectionModel().clearSelection();

        // Disable Objects
        comLocationEnd.setDisable(true);

        // Set End Location Label to Default
        lblEndLocation.setText("END LOCATION");

    }

    public void initializeTypeEnd(ActionEvent event) {

        System.out.println("End Type: " + comTypeEnd.getValue());

        // Set types depending on floor
        comLocationEnd.setItems(locations);

        // Able Objects
        comLocationEnd.setDisable(false);

        // Clear Past Selection
        comLocationEnd.getSelectionModel().clearSelection();

        // Set End Location Label to Default
        lblEndLocation.setText("END LOCATION");

    }

    public void changeFloorMap(ActionEvent event) {

        if (tglMap.isSelected()) { // 3-D

            if(comChangeFloor.getValue().equals("BUILDING: SHAPIRO | FLOOR: L2")) {
                mapImg.setImage(new Image("/MapImages/L2-ICONS"));
            } else if(comChangeFloor.getValue().equals("BUILDING: SHAPIRO | FLOOR: L1")) {
                mapImg.setImage(new Image("/MapImages/L2-ICONS"));
            } else if(comChangeFloor.getValue().equals("BUILDING: SHAPIRO | FLOOR: 1")) {
                mapImg.setImage(new Image("/MapImages/L2-ICONS"));
            } else if(comChangeFloor.getValue().equals("BUILDING: SHAPIRO | FLOOR: 2")) {
                mapImg.setImage(new Image("/MapImages/L2-ICONS"));
            } else if(comChangeFloor.getValue().equals("BUILDING: SHAPIRO | FLOOR: 3")) {
                mapImg.setImage(new Image("/MapImages/L2-ICONS"));
            }

        } else { // 2-D

            if(comChangeFloor.getValue().equals("BUILDING: SHAPIRO | FLOOR: L2")) {
                mapImg.setImage(new Image("/MapImages/L2-ICONS"));
            } else if(comChangeFloor.getValue().equals("BUILDING: SHAPIRO | FLOOR: L1")) {
                mapImg.setImage(new Image("/MapImages/L2-ICONS"));
            } else if(comChangeFloor.getValue().equals("BUILDING: SHAPIRO | FLOOR: 1")) {
                mapImg.setImage(new Image("/MapImages/L2-ICONS"));
            } else if(comChangeFloor.getValue().equals("BUILDING: SHAPIRO | FLOOR: 2")) {
                mapImg.setImage(new Image("/MapImages/L2-ICONS"));
            } else if(comChangeFloor.getValue().equals("BUILDING: SHAPIRO | FLOOR: 3")) {
                mapImg.setImage(new Image("/MapImages/L2-ICONS"));
            }
        }

    }

    public void setStartLocation(ActionEvent event) {
        lblStartLocation.setText(comLocationStart.getValue());
    }

    public void setEndLocation(ActionEvent event) {
        lblEndLocation.setText(comLocationEnd.getValue());
    }

    public void drawPath(ActionEvent event) {

        if (lblStartLocation.equals("START LOCATION") || lblEndLocation.equals("END LOCATION")) { // !!! add .equals using as a tester

            System.out.println("Pick a start and end location!");

        } else {

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

            // Set new overview panel to correct parameters
            lblStartLocation1.setText(comLocationStart.getValue());
            lblEndLocation1.setText(comLocationEnd.getValue());

            // Clean up Navigation Fields
            comBuildingStart.setItems(buildings); // Set comboboxes for buildings to default lists
            comBuildingStart.getSelectionModel().clearSelection(); // eventually set to default kiosk
            comBuildingEnd.setItems(buildings);
            comBuildingEnd.getSelectionModel().clearSelection(); // eventually set to default kiosk
            comFloorStart.setDisable(true);
            comFloorStart.getSelectionModel().clearSelection();
            comFloorStart.setItems(empty);
            comFloorEnd.setDisable(true);
            comFloorEnd.getSelectionModel().clearSelection();
            comFloorEnd.setItems(empty);
            comTypeStart.setDisable(true);
            comTypeStart.getSelectionModel().clearSelection();
            comTypeStart.setItems(empty);
            comTypeEnd.setDisable(true);
            comTypeEnd.getSelectionModel().clearSelection();
            comTypeEnd.setItems(empty);
            comLocationStart.setDisable(true);
            comLocationStart.getSelectionModel().clearSelection();
            comLocationStart.setItems(empty);
            comLocationEnd.setDisable(true);
            comLocationEnd.getSelectionModel().clearSelection();
            comLocationEnd.setItems(empty);
            lblStartLocation.setText("START LOCATION");
            lblEndLocation.setText("END LOCATION");

            if (paneHelp.isVisible()) {
                lblHelp1.setVisible(false);
                lblHelp2.setVisible(true);
            }

            // Update Directions
        }
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

        // Disable Everything Else
        btnRestart.setDisable(true);
        btnOpenLogin.setDisable(true);
        btnLogin.setDisable(true);
        btnCloseLogin.setDisable(true);
        btnHelp.setDisable(true);
        btnCloseHelp.setDisable(true);
        btnQuickDirections.setDisable(true);
        btnQuickCafe.setDisable(true);
        btnQuickBathroom.setDisable(true);
        btnQuickCoffee.setDisable(true);
        btnQuickShop.setDisable(true);
        comChangeFloor.setDisable(true);
        btnOpenQRCode.setDisable(true);
        txtPassword.setDisable(true);
        txtUsername.setDisable(true);
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

        tglHandicap.setSelected(false);
        tglHandicap.setText("OFF");
        lblHandicap.setText("HANDICAP");
        tglMap.setSelected(false);
        tglMap.setText("2-D");
        lblMap.setText("MAP: 2-D");

        if (paneHelp.isVisible()) {
            lblHelp1.setVisible(true);
            lblHelp2.setVisible(false);
        }

    }

    public void closeQRCodePanel(ActionEvent event) {

        // Hide QR code
        paneQRCode.setVisible(false);

        // Disable Everything Else
        btnRestart.setDisable(false);
        btnOpenLogin.setDisable(false);
        btnLogin.setDisable(false);
        btnCloseLogin.setDisable(false);
        btnHelp.setDisable(false);
        btnCloseHelp.setDisable(false);
        btnQuickDirections.setDisable(false);
        btnQuickCafe.setDisable(false);
        btnQuickBathroom.setDisable(false);
        btnQuickCoffee.setDisable(false);
        btnQuickShop.setDisable(false);
        comChangeFloor.setDisable(false);
        btnOpenQRCode.setDisable(false);
        txtPassword.setDisable(false);
        txtUsername.setDisable(false);
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

        // Show directions interface and hide pathfinding interface
        panePathfinding.setVisible(false);
        paneDirections.setVisible(true);

        // Set new overview panel to correct parameters
        lblStartLocation1.setText("Current Location"); // !!! change to default kiosk location
        lblEndLocation1.setText("Nearest Bathroom"); // !!! change to nearest bathoom

        // Clean up Navigation Fields
        comBuildingStart.setItems(buildings); // Set comboboxes for buildings to default lists
        comBuildingStart.getSelectionModel().clearSelection(); // eventually set to default kiosk
        comBuildingEnd.setItems(buildings);
        comBuildingEnd.getSelectionModel().clearSelection(); // eventually set to default kiosk
        comFloorStart.setDisable(true);
        comFloorStart.getSelectionModel().clearSelection();
        comFloorStart.setItems(empty);
        comFloorEnd.setDisable(true);
        comFloorEnd.getSelectionModel().clearSelection();
        comFloorEnd.setItems(empty);
        comTypeStart.setDisable(true);
        comTypeStart.getSelectionModel().clearSelection();
        comTypeStart.setItems(empty);
        comTypeEnd.setDisable(true);
        comTypeEnd.getSelectionModel().clearSelection();
        comTypeEnd.setItems(empty);
        comLocationStart.setDisable(true);
        comLocationStart.getSelectionModel().clearSelection();
        comLocationStart.setItems(empty);
        comLocationEnd.setDisable(true);
        comLocationEnd.getSelectionModel().clearSelection();
        comLocationEnd.setItems(empty);
        lblStartLocation.setText("START LOCATION");
        lblEndLocation.setText("END LOCATION");

        // Directions Update
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
    Label lblHelp1;

    @FXML
    Label lblHelp2;

    @FXML
    Label lblHelp;


    public void openHelpPanel(ActionEvent event) {

        if(panePathfinding.isVisible()) {
            paneHelp.setVisible(true);
            lblHelp1.setVisible(true);
            lblHelp2.setVisible(false);
        } else if(paneDirections.isVisible()) {
            paneHelp.setVisible(true);
            lblHelp1.setVisible(false);
            lblHelp2.setVisible(true);
        }
    }

    public void closeHelpPanel(ActionEvent event) {

        // Hide help panel
        paneHelp.setVisible(false);
        lblHelp1.setVisible(false);
        lblHelp2.setVisible(false);
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

        txtUsername.setText("");
        txtPassword.setText("");

        // Show login panel
        paneLogin.setVisible(true);

    }

    public void closeLoginPanel(ActionEvent event) {

        txtUsername.setText("");
        txtPassword.setText("");

        // Hide login panel
        paneLogin.setVisible(false);
    }

    public void login(ActionEvent event) {

        if (txtUsername.getText().equals("") || txtPassword.getText().equals("")) {

            // print message
            System.out.println("Please completely fill in the username and password fields");

        } else if (txtUsername.getText().equals("user") && txtPassword.getText().equals("password")){

            // Reset Fields
            panePathfinding.setVisible(true);
            paneDirections.setVisible(false);
            paneLogin.setVisible(false);
            btnQuickBathroom.setVisible(false);
            btnQuickShop.setVisible(false);
            btnQuickCoffee.setVisible(false);
            btnQuickCafe.setVisible(false);
            tglHandicap.setSelected(false);
            tglHandicap.setText("OFF");
            lblHandicap.setText("HANDICAP");
            tglMap.setSelected(false);
            tglMap.setText("2-D");
            lblMap.setText("MAP: 2-D");
            comBuildingStart.setItems(buildings); // Set comboboxes for buildings to default lists
            comBuildingStart.getSelectionModel().clearSelection(); // eventually set to default kiosk
            comBuildingEnd.setItems(buildings);
            comBuildingEnd.getSelectionModel().clearSelection(); // eventually set to default kiosk
            comFloorStart.setDisable(true);
            comFloorStart.getSelectionModel().clearSelection();
            comFloorStart.setItems(empty);
            comFloorEnd.setDisable(true);
            comFloorEnd.getSelectionModel().clearSelection();
            comFloorEnd.setItems(empty);
            comTypeStart.setDisable(true);
            comTypeStart.getSelectionModel().clearSelection();
            comTypeStart.setItems(empty);
            comTypeEnd.setDisable(true);
            comTypeEnd.getSelectionModel().clearSelection();
            comTypeEnd.setItems(empty);
            comLocationStart.setDisable(true);
            comLocationStart.getSelectionModel().clearSelection();
            comLocationStart.setItems(empty);
            comLocationEnd.setDisable(true);
            comLocationEnd.getSelectionModel().clearSelection();
            comLocationEnd.setItems(empty);
            lblStartLocation.setText("START LOCATION");
            lblEndLocation.setText("END LOCATION");
            // Set floor map !!!

            // Login
            System.out.println("User logged in");

        } else {

            // print message
            System.out.println("Wrong username and password!");

        }
    }

    /*
    // Fill in node manual items based on node selected
    // !!! method not finished...
    public void touchNode(INode node, String type) {
        if (type.equals("Start")) {

            comBuildingStart.getSelectionModel().select(node.getBuilding);
            //initializeBuildingStart(new ActionEvent()); // don't need this BUT keep for potential reference

            comFloorStart.getSelectionModel().select(node.getFloor);
            //initializeFloorStart(new ActionEvent());

            comTypeStart.getSelectionModel().select(node.getType);
            //initializeTypeStart(new ActionEvent());

            comLocationStart.getSelectionModel().select(node.getLongName);


        } else if (type.equals("End")) {

            comBuildingEnd.getSelectionModel().select(node.getBuilding);
            //initializeBuildingEnd(new ActionEvent());

            comFloorEnd.getSelectionModel().select(node.getFloor);
            //initializeFloorEnd(new ActionEvent());

            comTypeEnd.getSelectionModel().select(node.getType);
            //initializeTypeEnd(new ActionEvent());

            comLocationEnd.getSelectionModel().select(node.getLongName);

        }
    }
    */

}
