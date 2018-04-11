package com.manlyminotaurs.viewControllers;

import com.manlyminotaurs.core.KioskInfo;
import com.manlyminotaurs.databases.DataModelI;
import com.manlyminotaurs.nodes.INode;
import com.manlyminotaurs.nodes.Node;
import com.manlyminotaurs.pathfinding.AStarStrategyI;
import com.manlyminotaurs.pathfinding.PathNotFoundException;
import com.manlyminotaurs.pathfinding.PathfinderUtil;
import com.manlyminotaurs.pathfinding.PathfindingContext;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;

import javax.swing.*;
import javax.xml.soap.Text;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class homeController implements Initializable {

    // Test Objects
    //final static ObservableList<String> types = FXCollections.observableArrayList("HALL", "ELEV", "REST", "STAI", "DEPT", "LABS", "INFO", "CONF", "EXIT", "RETL", "SERV");
    final static ObservableList<String> floors = FXCollections.observableArrayList("L2", "L1","1","2","3");
    //final static ObservableList<String> locations = FXCollections.observableArrayList("thePlace", "Jerry's house", "another place", "wong's house", "fdskjfas", "fsdfds", "Dfsd","sfdd","SFd");
    final static ObservableList<String> mapFloors = FXCollections.observableArrayList("FLOOR: L2", "FLOOR: L1", "FLOOR: 1","FLOOR: 2","FLOOR: 3");
    final static ObservableList<String> empty = FXCollections.observableArrayList();
    final ObservableList<String> buildings = FXCollections.observableArrayList(DataModelI.getInstance().getBuildingsFromList());
    final ObservableList<String> types = FXCollections.observableArrayList(DataModelI.getInstance().getTypesFromList());
    final int MAPX2D = 5000;
    final int MAPY2D = 3400;
    //final ObservableList<String> locations = FXCollections.observableArrayList(DataModelI.getInstance().getLongNameByBuildingTypeFloor());
    //final static ObservableList<String> buildings = FXCollections.observableArrayList("Shapiro");


    /*
    // Files
    File fileL22D = new File("/MapImages/00_thelowerlevel2.png");
    File fileL12D = new File("/MapImages/00_thelowerlevel1.png");
    //File fileG2D = new File("/MapImages/00_thegroundfloor.png");
    File file12D = new File("/MapImages/01_thefirstfloor.png");
    File file22D = new File("/MapImages/02_thesecondfloor.png");
    File file32D = new File("/MapImages/03_thethirdfloor.png");
    File fileL23D = new File("/MapImages/L2-ICONS.png");
    File fileL13D = new File("/MapImages/L1-ICONS.png");
    //File fileG3D = new File("/MapImages/G.png");
    File file13D = new File("/MapImages/1-ICONS.png");
    File file23D = new File("/MapImages/2-ICONS.png");
    File file33D = new File("/MapImages/3-ICONS.png");

    // Map Images
    Image imageL22D = new Image(fileL22D.toURI().toString());
    Image imageL12D = new Image(fileL12D.toURI().toString());
    Image image12D = new Image(file12D.toURI().toString());
    Image image22D = new Image(file22D.toURI().toString());
    Image image32D = new Image(file32D.toURI().toString());

    Image imageL23D = new Image(fileL23D.toURI().toString());
    Image imageL13D = new Image(fileL13D.toURI().toString());
    Image image13D = new Image(file13D.toURI().toString());
    Image image23D = new Image(file23D.toURI().toString());
    Image image33D = new Image(file33D.toURI().toString());
    */

    /*
    ProxyImage imageL22D = new ProxyImage("00_thelowerlevel2.png");
    ProxyImage imageL12D = new ProxyImage("00_thelowerlevel1.png");
    ProxyImage image12D = new ProxyImage("01_thefirstfloor.png");
    ProxyImage image22D = new ProxyImage("02_thesecondfloor.png");
    ProxyImage image32D = new ProxyImage("03_thethirdfloor.png");

    ProxyImage imageL23D = new ProxyImage("L2-ICONS.png");
    ProxyImage imageL13D = new ProxyImage("L1-ICONS.png");
    ProxyImage image13D = new ProxyImage("1-ICONS.png");
    ProxyImage image23D = new ProxyImage("2-ICONS.png");
    ProxyImage image33D = new ProxyImage("3-ICONS.png");
    */


    Parent adminRequest;
    Parent staffRequest;

    Circle finishCircle = new Circle();
    Circle startCircle = new Circle();
    Circle finishCircle2 = new Circle();

    LinkedList<Node> listForQR = new LinkedList<Node>();
    Image imageQRCode;

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
    ScrollPane scrollPaneMap;

    @FXML
    StackPane stackPaneMap;

    @FXML
    Pane paneMap;

    @FXML
    Path path;

    public void initialize(URL location, ResourceBundle resources) {
    try {

        //final ObservableList<String> buildings = FXCollections.observableArrayList(DataModelI.getInstance().getBuildingsFromList());

        // Set comboboxes for buildings to default lists
        comBuildingStart.setItems(FXCollections.observableArrayList(DataModelI.getInstance().getBuildingsFromList()));
        comBuildingEnd.setItems(FXCollections.observableArrayList(DataModelI.getInstance().getBuildingsFromList()));
        comFloorStart.setDisable(true);
        comFloorEnd.setDisable(true);
        comTypeStart.setDisable(true);
        comTypeEnd.setDisable(true);
        comChangeFloor.setItems(mapFloors);
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

        tglHandicap.setSelected(false);
        tglHandicap.setText("OFF");
        lblHandicap.setText("HANDICAP");
        tglMap.setSelected(false);
        tglMap.setText("2-D");
        lblMap.setText("MAP: 2-D");

        comChangeFloor.getSelectionModel().select(2);
        pathfloor2DMapLoader("1");
        //staffRequest = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/adminRequestDashBoard.fxml"));
        //adminRequest = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/userRequestDashBoard.fxml"));

    }

        catch (Exception e){
        e.printStackTrace();}
    }
        // Remember to refresh nodes

        // Set Floor Map and Floor Combobox to correct setting

    @FXML
    public void initialize() {

        comBuildingStart.setItems(FXCollections.observableArrayList(DataModelI.getInstance().getBuildingsFromList()));
        comBuildingEnd.setItems(FXCollections.observableArrayList(DataModelI.getInstance().getBuildingsFromList()));
        comFloorStart.setDisable(true);
        comFloorEnd.setDisable(true);
        comTypeStart.setDisable(true);
        comTypeEnd.setDisable(true);
        comChangeFloor.setItems(mapFloors);
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

        tglHandicap.setSelected(false);
        tglHandicap.setText("OFF");
        lblHandicap.setText("HANDICAP");
        tglMap.setSelected(false);
        tglMap.setText("2-D");
        lblMap.setText("MAP: 2-D");

        comChangeFloor.getSelectionModel().select(2);
        pathfloor2DMapLoader("1");
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


            stackPaneMap.setPrefHeight(2774);
            stackPaneMap.setPrefWidth(5000);
            mapImg.setFitHeight(2774);
            mapImg.setFitWidth(5000);
            paneMap.setPrefHeight(2774);
            paneMap.setPrefWidth(5000);

            floor3DMapLoader(comChangeFloor.getValue());
            /*
            // !!!
            if(comChangeFloor.getValue().equals("FLOOR: L2")) {
                new ProxyImage(mapImg,"L2-ICONS.png").display();

            } else if(comChangeFloor.getValue().equals("FLOOR: L1")) {
                new ProxyImage(mapImg,"L1-ICONS.png").display();

            } else if(comChangeFloor.getValue().equals("FLOOR: 1")) {
                new ProxyImage(mapImg,"1-ICONS.png").display();

            } else if(comChangeFloor.getValue().equals("FLOOR: 2")) {
                new ProxyImage(mapImg,"2-ICONS.png").display();

            } else if(comChangeFloor.getValue().equals("FLOOR: 3")) {
                new ProxyImage(mapImg,"3-ICONS.png").display();

            }*/

        } else {

            // Switch 2-D
            tglMap.setText("2-D");
            lblMap.setText("MAP: 2-D");

            stackPaneMap.setPrefHeight(3400);
            stackPaneMap.setPrefWidth(5000);
            mapImg.setFitHeight(3400);
            mapImg.setFitWidth(5000);
            paneMap.setPrefHeight(3400);
            paneMap.setPrefWidth(5000);

            floor2DMapLoader(comChangeFloor.getValue());
            /*
            if(comChangeFloor.getValue().equals("FLOOR: L2")) { ;
                new ProxyImage(mapImg,"00_thelowerlevel2.png").display();

            } else if(comChangeFloor.getValue().equals("FLOOR: L1")) {
                new ProxyImage(mapImg,"00_thelowerlevel1.png").display();

            } else if(comChangeFloor.getValue().equals("FLOOR: 1")) {
                new ProxyImage(mapImg,"01_thefirstfloor.png").display();

            } else if(comChangeFloor.getValue().equals("FLOOR: 2")) {
                new ProxyImage(mapImg,"02_thesecondfloor.png").display();

            } else if(comChangeFloor.getValue().equals("FLOOR: 3")) {
                new ProxyImage(mapImg,"03_thethirdfloor.png").display();

            }*/



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
        comLocationStart.setItems(FXCollections.observableArrayList(DataModelI.getInstance().getLongNameByBuildingTypeFloor(comBuildingStart.getValue(),comTypeStart.getValue(),comFloorStart.getValue())));


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
        comLocationEnd.setItems(FXCollections.observableArrayList(DataModelI.getInstance().getLongNameByBuildingTypeFloor(comBuildingEnd.getValue(),comTypeEnd.getValue(),comFloorEnd.getValue())));

        // Able Objects
        comLocationEnd.setDisable(false);

        // Clear Past Selection
        comLocationEnd.getSelectionModel().clearSelection();

        // Set End Location Label to Default
        lblEndLocation.setText("END LOCATION");

    }

    public void changeFloorMap(ActionEvent event) {

        if (tglMap.isSelected()) { // 3-D

            stackPaneMap.setPrefHeight(2774);
            stackPaneMap.setPrefWidth(5000);
            mapImg.setFitHeight(2772);
            mapImg.setFitWidth(5000);
            paneMap.setPrefHeight(2774);
            paneMap.setPrefWidth(5000);

            floor3DMapLoader(comChangeFloor.getValue());
/*
            if(comChangeFloor.getValue().equals("FLOOR: L2")) {
                new ProxyImage(mapImg,"L2-ICONS.png").display();

            } else if(comChangeFloor.getValue().equals("FLOOR: L1")) {
                new ProxyImage(mapImg,"L1-ICONS.png").display();

            } else if(comChangeFloor.getValue().equals("FLOOR: 1")) {
                new ProxyImage(mapImg,"1-ICONS.png").display();

            } else if(comChangeFloor.getValue().equals("FLOOR: 2")) {
                new ProxyImage(mapImg,"2-ICONS.png").display();

            } else if(comChangeFloor.getValue().equals("FLOOR: 3")) {
                new ProxyImage(mapImg,"3-ICONS.png").display();

            }*/



        } else { // 2-D

            // !!!

            stackPaneMap.setPrefHeight(3400);
            stackPaneMap.setPrefWidth(5000);
            mapImg.setFitHeight(3400);
            mapImg.setFitWidth(5000);
            paneMap.setPrefHeight(3400);
            paneMap.setPrefWidth(5000);

            floor2DMapLoader(comChangeFloor.getValue());

            /*
            if(comChangeFloor.getValue().equals("FLOOR: L2")) {
                new ProxyImage(mapImg,"00_thelowerlevel2.png").display();

            } else if(comChangeFloor.getValue().equals("FLOOR: L1")) {
                new ProxyImage(mapImg,"00_thelowerlevel1.png").display();

            } else if(comChangeFloor.getValue().equals("FLOOR: 1")) {
                new ProxyImage(mapImg,"01_thefirstfloor.png").display();

            } else if(comChangeFloor.getValue().equals("FLOOR: 2")) {
                new ProxyImage(mapImg,"02_thesecondfloor.png").display();

            } else if(comChangeFloor.getValue().equals("FLOOR: 3")) {
                new ProxyImage(mapImg,"03_thethirdfloor.png").display();

            }*/


        }

    }

    public void setStartLocation(ActionEvent event) {
        lblStartLocation.setText(comLocationStart.getValue());
    }

    public void setEndLocation(ActionEvent event) {
        lblEndLocation.setText(comLocationEnd.getValue());
    }

    /**
     * Maps the value from the old boundary to the new boundary
     * @param value the value to be transferred
     * @param oldMin the old min value (x or y)
     * @param oldMax the old max value (x or y)
     * @param newMin the new min value (x or y)
     * @param newMax the new max calue (x or y)
     * @return the value mapped from the old boundary to the new boundary
     */
    static double map(double value, double oldMin, double oldMax, double newMin, double newMax) {
        return (((value - oldMin)*(newMax - newMin))/(oldMax-oldMin)) + newMin;
    }

    /**
     *
     * @param startNode node to draw from
     * @param endNode node to draw to
     * @param moveTo start point of line to be drawn
     * @param lineTo end point of line to be drawn
     *
     */
    private void addPath(String dimension, Node startNode, Node endNode, MoveTo moveTo, LineTo lineTo) {
        if(startNode != null && endNode != null) {
            System.out.println("Found a path!");

            if(dimension.equals("2-D")) { // if 2D
                moveTo.setX(startNode.getXCoord());
                moveTo.setY(startNode.getYCoord());
                // Draw to end point
                // Map the x and y coords onto our map
                lineTo.setX(endNode.getXCoord());
                lineTo.setY(endNode.getYCoord());
            } else if(dimension.equals("3-D"))// if 3D
            {
                moveTo.setX(startNode.getXCoord3D());
                moveTo.setY(startNode.getYCoord3D());
                // Draw to end point
                // Map the x and y coords onto our map
                lineTo.setX(endNode.getXCoord3D());
                lineTo.setY(endNode.getYCoord3D());
            } else {
                System.out.println("Invalid dimension");
            }

            // add the elements to the path
            path.getElements().add(moveTo);
            path.getElements().add(lineTo);
        }
    }

    /**
     * Prints the given path on the map
     * @param nodeList the nodes to draw a path between
     */
    public void printNodePath(List<Node> nodeList, String floor, String dimension) {
        System.out.println("Attempting to print path between nodes...");
        int i = 0;
        while(i < nodeList.size()) {
            // Give starting point
            MoveTo moveTo = new MoveTo();
            LineTo lineTo = new LineTo();
            Node startNode = nodeList.get(i);
            Node endNode;

            if(i+1<nodeList.size() && nodeList.get(i).getFloor().equals(floor)) {
                endNode = nodeList.get(i + 1);

                // add the path
                addPath(dimension, startNode, endNode, moveTo, lineTo);
            }
            i++;
            System.out.println("Path added...");
        }
    }

    public void drawPath(ActionEvent event) {
        String startFloor;
        String endFloor;
        startFloor = comFloorStart.getValue();
        endFloor = comLocationEnd.getValue();

        if (lblStartLocation.getText().equals("START LOCATION") || lblEndLocation.getText().equals("END LOCATION")) { // !!! add .equals using as a tester

            System.out.println("Pick a start and end location!");

        } else {

            PathfindingContext pathfindingContext = new PathfindingContext();
            PathfinderUtil pathfinderUtil = new PathfinderUtil();

            List<Node> nodeList = new ArrayList<>();
            LinkedList<Node> pathList = new LinkedList<>();
            nodeList = DataModelI.getInstance().retrieveNodes();

            try {
                pathList = pathfindingContext.getPath(DataModelI.getInstance().getNodeByLongNameFromList(lblStartLocation.getText(), nodeList),DataModelI.getInstance().getNodeByLongNameFromList(lblEndLocation.getText(), nodeList),new AStarStrategyI());

            } catch (PathNotFoundException e) {
                e.printStackTrace();
            }

            ObservableList<String> directions = FXCollections.observableArrayList(pathfinderUtil.angleToText(pathList));
            lstDirections.setItems(directions);
            listForQR = pathList;
            pathfinderUtil.generateQR(pathfinderUtil.angleToText(pathList));
            // new ProxyImage(imgQRCode,"CrunchifyQR.png").display2();

            int finishX = 0;
            int finishY = 0;
            int startX = 0;
            int startY = 0;

            // Draw path code

            if (tglHandicap.isSelected()) {
                // use elevator


                if (tglMap.isSelected()) {
                    // use 3-D
                    printNodePath(pathList, startFloor, "3-D");

                    finishX = pathList.get(pathList.size()-1).getXCoord3D();
                    finishY = pathList.get(pathList.size()-1).getYCoord3D();
                    startX = pathList.get(0).getXCoord3D();
                    startY = pathList.get(0).getYCoord3D();

                    pathfloor3DMapLoader(startFloor);
                    comChangeFloor.setValue("FLOOR: " + startFloor);

                } else {
                    // use 2-D
                    printNodePath(pathList, startFloor, "2-D");
                    finishX = pathList.get(pathList.size()-1).getXCoord();
                    finishY = pathList.get(pathList.size()-1).getYCoord();
                    startX = pathList.get(0).getXCoord();
                    startY = pathList.get(0).getYCoord();

                    pathfloor2DMapLoader(startFloor);
                    comChangeFloor.setValue("FLOOR: " + startFloor);
                }
            } else {
                // use stairs
                if (tglMap.isSelected()) {
                    // use 3-D
                    System.out.println("using 3d stairs");
					printNodePath(pathList, startFloor, "3-D");
                    finishX = pathList.get(pathList.size()-1).getXCoord3D();
                    finishY = pathList.get(pathList.size()-1).getYCoord3D();
                    startX = pathList.get(0).getXCoord3D();
                    startY = pathList.get(0).getYCoord3D();

                    pathfloor3DMapLoader(startFloor);
                    comChangeFloor.setValue("FLOOR: " + startFloor);
                } else {
                    // use 2-D
					printNodePath(pathList, startFloor, "2-D");
                    finishX = pathList.get(pathList.size()-1).getXCoord();
                    finishY = pathList.get(pathList.size()-1).getYCoord();
                    startX = pathList.get(0).getXCoord();
                    startY = pathList.get(0).getYCoord();

                    pathfloor2DMapLoader(startFloor);
                    comChangeFloor.setValue("FLOOR: " + startFloor);

                }
            }


            // Draw Circle
            startCircle.setRadius(7);
            startCircle.setFill(Color.NAVY);
            startCircle.setVisible(true);
            startCircle.setCenterX(startX);
            startCircle.setCenterY(startY);

            finishCircle.setRadius(14);
            finishCircle.setFill(Color.NAVY);
            finishCircle.setOpacity(50);
            finishCircle.setVisible(true);
            finishCircle.setCenterX(finishX);
            finishCircle.setCenterY(finishY);

			finishCircle2.setRadius(10);
			finishCircle2.setFill(Color.WHITE);
			finishCircle2.setOpacity(100);
			finishCircle2.setVisible(true);
			finishCircle2.setCenterX(finishX);
			finishCircle2.setCenterY(finishY);

            paneMap.getChildren().add(finishCircle);
            paneMap.getChildren().add(finishCircle2);
            paneMap.getChildren().add(startCircle);

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

        new ProxyImage(imgQRCode,"CrunchifyQR.png").display2();
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
        path.getElements().clear();
        path.getElements().add(new MoveTo(-100, -100));
        path.getElements().add(new LineTo(5000, -100));
        path.getElements().add(new LineTo(5000, 5000));
        path.getElements().add(new LineTo(-100, 5000));

        tglHandicap.setSelected(false);
        tglHandicap.setText("OFF");
        lblHandicap.setText("HANDICAP");
        tglMap.setSelected(false);
        tglMap.setText("2-D");
        lblMap.setText("MAP: 2-D");
        pathfloor2DMapLoader("1");
        comChangeFloor.getSelectionModel().select(2);

        paneMap.getChildren().remove(startCircle);
        paneMap.getChildren().remove(finishCircle);
        paneMap.getChildren().remove(finishCircle2);

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
    PasswordField txtPassword;

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

    public void login(ActionEvent event)throws Exception {

        String userName = txtUsername.getText();
        String password = txtPassword.getText();

        if (userName.equals("") || password.equals("")) {

            // print message
            System.out.println("Please completely fill in the username and password fields");
        } else if (DataModelI.getInstance().doesUserPasswordExist(userName.toLowerCase(), password.toLowerCase())) {
            try {
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

                Stage stage;
                //get reference to the button's stage
                stage = (Stage) btnLogin.getScene().getWindow();
                //load up Home FXML document
                staffRequest = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/adminRequestDashBoard.fxml"));

                KioskInfo.currentUserID = DataModelI.getInstance().getIDByUserPassword(userName , password);

                //create a new scene with root and set the stage
                Scene scene = new Scene(staffRequest);
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
                else if (txtUsername.getText().toLowerCase().equals("admin") && txtPassword.getText().toLowerCase().equals("admin")) {
                try {
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

                    Stage stage;
                    //get reference to the button's stage
                    stage = (Stage)btnLogin.getScene().getWindow();
                    //load up Home FXML document
                    adminRequest = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/userRequestDashBoard.fxml"));

                    //create a new scene with root and set the stage
                    Scene scene = new Scene(adminRequest);
                    stage.setScene(scene);
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                // Login
                System.out.println("User logged in");

            }else {

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

    // Map Touch Capability

    public void getXandY(MouseEvent event) throws Exception{
        //see which pane is visible and set the corresponding x and y coordinates
        if (paneMap.isVisible() == true) {
            System.out.println("X: " + String.format("%1.3f", event.getX()));
            System.out.println("Y: " + String.format("%1.3f", event.getY()));
        }
    }

    public void floor2DMapLoader(String floor) {
        if(floor.equals("FLOOR: L2")) {
            new ProxyImage(mapImg,"00_thelowerlevel2.png").display();
        } else if(floor.equals("FLOOR: L1")) {
            new ProxyImage(mapImg,"00_thelowerlevel1.png").display();
        } else if(floor.equals("FLOOR: 1")) {
            new ProxyImage(mapImg,"01_thefirstfloor.png").display();
        } else if(floor.equals("FLOOR: 2")) {
            new ProxyImage(mapImg,"02_thesecondfloor.png").display();
        } else if(floor.equals("FLOOR: 3")) {
            new ProxyImage(mapImg,"03_thethirdfloor.png").display();
        }
    }

    public void pathfloor2DMapLoader(String floor) {
        if(floor.equals("L2")) {
            new ProxyImage(mapImg,"00_thelowerlevel2.png").display();
        } else if(floor.equals("L1")) {
            new ProxyImage(mapImg,"00_thelowerlevel1.png").display();
        } else if(floor.equals("1")) {
            new ProxyImage(mapImg,"01_thefirstfloor.png").display();
        } else if(floor.equals("2")) {
            new ProxyImage(mapImg,"02_thesecondfloor.png").display();
        } else if(floor.equals("3")) {
            new ProxyImage(mapImg,"03_thethirdfloor.png").display();
        }
    }

    public void floor3DMapLoader(String floor) {
        if(floor.equals("FLOOR: L2")) {
            new ProxyImage(mapImg,"L2-ICONS.png").display();
        } else if(floor.equals("FLOOR: L1")) {
            new ProxyImage(mapImg,"L1-ICONS.png").display();
        } else if(floor.equals("FLOOR: 1")) {
            new ProxyImage(mapImg,"1-ICONS.png").display();
        } else if(floor.equals("FLOOR: 2")) {
            new ProxyImage(mapImg,"2-ICONS.png").display();
        } else if(floor.equals("FLOOR: 3")) {
            new ProxyImage(mapImg,"3-ICONS.png").display();
        }
    }

    public void pathfloor3DMapLoader(String floor) {
        if(floor.equals("L2")) {
            new ProxyImage(mapImg,"L2-ICONS.png").display();
        } else if(floor.equals("L1")) {
            new ProxyImage(mapImg,"L1-ICONS.png").display();
        } else if(floor.equals("1")) {
            new ProxyImage(mapImg,"1-ICONS.png").display();
        } else if(floor.equals("2")) {
            new ProxyImage(mapImg,"2-ICONS.png").display();
        } else if(floor.equals("3")) {
            new ProxyImage(mapImg,"3-ICONS.png").display();
        }
    }

}
