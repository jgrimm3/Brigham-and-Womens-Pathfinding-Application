
package com.manlyminotaurs.viewControllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;

import com.manlyminotaurs.core.KioskInfo;
import com.manlyminotaurs.core.Main;
import com.manlyminotaurs.databases.DataModelI;
import com.manlyminotaurs.databases.IDataModel;
import com.manlyminotaurs.nodes.Edge;
import com.manlyminotaurs.nodes.Location;
import com.manlyminotaurs.nodes.Node;
import com.manlyminotaurs.pathfinding.PathfinderUtil;
import com.manlyminotaurs.pathfinding.PathfindingContext;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchPoint;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;



public class nodeEditorController {
    final double NODEXMIN = 0;
    final double NODEXMAX = 5000;
    final double NODEYMIN = 0;
    final double NODEYMAX = 2774;
    final double NEWMAPXMIN = 0;
    final double NEWMAPXMAX = 1250;
    final double NEWMAPYMIN = 0;
    final double NEWMAPYMAX = 693.8;
    Parent logout;
    Parent manageRequests;
    Parent accountManager;
    Node edgeNodeAdd = null;
    Circle finishCircle = new Circle();
    Circle startCircle = new Circle();
    Circle finishCircle2 = new Circle();
    List<Node> nodeList = DataModelI.getInstance().getNodeList();
    List<Node> pathList = new ArrayList<>();
    LinkedList<Node> listForQR = new LinkedList<Node>();
    Image imageQRCode;
    String startFloor = "";
    String endFloor = "";
    List<Circle> circleList = new ArrayList<>();
    List<Line> edgeLines = new ArrayList<>();
    Boolean mapNodeChoice;
    Boolean selectNode = false;


    @FXML
    Button btnSelectEdgeNode;
    @FXML
    Button navBtnManageRequests;
    @FXML
    Path path;
    @FXML
    ComboBox<String> cmboPathfinding;
    @FXML
    Button btnMenuAdd;
    @FXML
    Button btnModifyNode;
    @FXML
    Button btnDeleteNode;
    @FXML
    Pane paneAdd;
    @FXML
    Pane paneModify;
    @FXML
    Pane paneDelete;
    @FXML
    ComboBox<String> cmboBuilding;
    @FXML
    ComboBox<String> cmboType;
    @FXML
    ComboBox<String> cmboBuildingMod;
    @FXML
    ComboBox<String> cmboTypeMod;
    @FXML
    ComboBox<String> cmboNodeMod;
    @FXML
    ComboBox<String> cmboBuildingDel;
    @FXML
    ComboBox<String> cmboTypeDel;
    @FXML
    ComboBox<String> cmboNodeDel;
    @FXML
    JFXTextField txtShortName;
    @FXML
    JFXTextField txtLongName;
    @FXML
    JFXTextField txtXCoord;
    @FXML
    JFXTextField txtYCoord;
    @FXML
    JFXTextField txtXCoord3D;
    @FXML
    JFXTextField txtYCoord3D;
    @FXML
    JFXTextField txtShortNameMod;
    @FXML
    JFXTextField txtLongNameMod;
    @FXML
    JFXTextField txtXCoordMod;
    @FXML
    JFXTextField txtYCoordMod;
    @FXML
    JFXTextField txtXCoordMod3D;
    @FXML
    JFXTextField txtYCoordMod3D;
    @FXML
    JFXTextField txtShortNameDel;
    @FXML
    JFXTextField txtLongNameDel;
    @FXML
    ToggleButton tglGeofence;
    @FXML
    ComboBox<String> cmboFloor;
    @FXML
    ComboBox<String> cmboFloorAdd;
    @FXML
    ComboBox<String> cmboFloorDel;
    @FXML
    Pane pane;
    @FXML
    ScrollPane scrollPane;
    @FXML
    Button btnLogOut;
    @FXML
    Button btnAddNode;
    @FXML
    Button btnModify;
    @FXML
    ImageView mapImg;
    @FXML
    Button btn2DMap;
    @FXML
    Button btn3DMap;
    @FXML
    Button btn2DMapMod;
    @FXML
    Button btn3DMapMod;
    @FXML
    Button btnDeleteNodePane;
    @FXML
    JFXTextField txtAdminUser;
    @FXML
    JFXPasswordField txtAdminPassword;
    @FXML
    StackPane stackPaneMap;
    @FXML
    Pane paneMap;
    @FXML
    Button navBtnManageAccounts;


    final ObservableList<String> buildings = FXCollections.observableArrayList(DataModelI.getInstance().getBuildingsFromList());
    final ObservableList<String> types = FXCollections.observableArrayList(DataModelI.getInstance().getTypesFromList());
    final static ObservableList<String> floors = FXCollections.observableArrayList("L2", "L1", "1", "2", "3");
    final static ObservableList<String> locations = FXCollections.observableArrayList("thePlace", "Jerry's house", "another place", "wong's house", "fdskjfas", "fsdfds", "Dfsd", "sfdd", "SFd");
    final static ObservableList<String> Algorithms = FXCollections.observableArrayList("A*", "Breadth-First Search", "Depth-First Search");


    String longName;
    String shortName;
    String type;
    String floor;
    String building;
    Node node;
    int xCoord2D;
    int yCoord2D;
    int xCoord3D;
    int yCoord3D;

    @FXML
    public void initialize() throws Exception{
        try{
            System.out.println("initializing");
            paneDelete.setVisible((false));
            paneDelete.setDisable(true);
            paneModify.setVisible(false);
            paneModify.setDisable(true);
            paneAdd.setDisable(false);
            paneAdd.setVisible(true);
            btnAddNode.setDisable(true);
            BooleanBinding booleanBind = Bindings.or(txtYCoord.textProperty().isEmpty(),
                    txtXCoord.textProperty().isEmpty()).or(txtShortName.textProperty().isEmpty()).or(txtLongName.textProperty().isEmpty()).or(txtYCoord3D.textProperty().isEmpty()).or(txtXCoord3D.textProperty().isEmpty());
            btnAddNode.disableProperty().bind(booleanBind);

            cmboBuilding.setItems(buildings);
            cmboFloorAdd.setItems(floors);
            cmboPathfinding.setItems(Algorithms);

            //set jfx text colors


            //prompt text colors
            txtShortNameDel.setStyle("-fx-text-fill: White ");
            txtShortName.setStyle("-fx-prompt-text-fill:  White");
            txtShortNameMod.setStyle("-fx-prompt-text-fill:  White");
            txtLongNameDel.setStyle("-fx-prompt-text-fill:  White");
            txtLongNameMod.setStyle("-fx-prompt-text-fill:  White");
            txtLongName.setStyle("-fx-prompt-text-fill:  White");
            txtXCoordMod3D.setStyle("-fx-prompt-text-fill:  White");
            txtYCoordMod3D.setStyle("-fx-prompt-text-fill:  White");
            txtXCoord3D.setStyle("-fx-prompt-text-fill:  White");
            txtYCoord3D.setStyle("-fx-prompt-text-fill:  White");
            txtAdminPassword.setStyle("-fx-prompt-text-fill:  White");
            txtAdminUser.setStyle("-fx-prompt-text-fill:  White");
            txtXCoord.setStyle("-fx-prompt-text-fill:  White");
            txtYCoord.setStyle("-fx-prompt-text-fill:  White");
            txtXCoordMod.setStyle("-fx-prompt-text-fill:  White");
            txtYCoordMod.setStyle("-fx-prompt-text-fill:  White");

            txtShortNameDel.setStyle("-fx-text-fill: White ");
            txtShortName.setStyle("-fx-text-fill:  White");
            txtShortNameMod.setStyle("-fx-text-fill:  White");
            txtLongNameDel.setStyle("-fx-text-fill: White");
            txtLongNameMod.setStyle("-fx-text-fill: White");
            txtLongName.setStyle("-fx-text-fill: White");
            txtXCoordMod3D.setStyle("-fx-text-fill: White");
            txtYCoordMod3D.setStyle("-fx-text-fill: White");
            txtXCoord3D.setStyle("-fx-text-fill:  White");
            txtYCoord3D.setStyle("-fx-text-fill: White");
            txtAdminPassword.setStyle("-fx-text-fill: White");
            txtAdminUser.setStyle("-fx-text-fill: White");
            txtXCoord.setStyle("-fx-text-fill:  White");
            txtYCoord.setStyle("-fx-text-fill: White");
            txtXCoordMod.setStyle("-fx-text-fill:  White");
            txtYCoordMod.setStyle("-fx-text-fill:  White");

            scrollPane.setVvalue(0.65);
            scrollPane.setHvalue(0.25);
            path.setStrokeWidth(5);
            //printPoints("L2");

            // Initialize Map
            stackPaneMap.setPrefHeight(3400);
            stackPaneMap.setPrefWidth(5000);
            mapImg.setFitHeight(3400);
            mapImg.setFitWidth(5000);
            paneMap.setPrefHeight(3400);
            paneMap.setPrefWidth(5000);



            drawCircles("1", "2-D");
            drawEdges("1", "2-D" );




            /*paneMap.getChildren().clear();

            pathfloor2DMapLoader("1");

            List<Node> nodeList = new ArrayList<>();
            nodeList = DataModelI.getInstance().getNodesByFloor("1");

            for(int x=0;x<nodeList.size(); x++) {
                Circle tempCircle = new Circle();
                tempCircle.setCenterX(nodeList.get(x).getXCoord());
                tempCircle.setCenterY(nodeList.get(x).getYCoord());
                tempCircle.setRadius(5);
                tempCircle.setFill(Color.PURPLE);
                tempCircle.setVisible(true);
                paneMap.getChildren().add(tempCircle);
            } */

        }
        catch (Exception e){
            e.printStackTrace();}
    }

    //Swap active panes
    public void displayModifyPane(ActionEvent event) {   //modify node
        paneAdd.setVisible(false);
        paneDelete.setVisible((false));
        paneAdd.setDisable(true);
        paneDelete.setDisable(true);
        paneModify.setDisable(false);
        paneModify.setVisible(true);

        txtLongName.clear();
        txtShortName.clear();
        txtXCoord.clear();
        txtYCoord.clear();
        txtLongNameDel.clear();
        txtShortNameDel.clear();
        txtAdminPassword.clear();
        txtAdminUser.clear();

        BooleanBinding booleanBind = Bindings.or(txtYCoordMod.textProperty().isEmpty(),
                txtXCoordMod.textProperty().isEmpty()).or(txtLongNameMod.textProperty().isEmpty()).or(txtYCoordMod3D.textProperty().isEmpty()).or(txtXCoordMod3D.textProperty().isEmpty());
        btnModify.disableProperty().bind(booleanBind);

        cmboBuildingMod.setItems(buildings);
    }

    public void displayAddPane(ActionEvent event) {   //add Node
        paneDelete.setVisible((false));
        paneDelete.setDisable(true);
        paneModify.setVisible(false);
        paneModify.setDisable(true);
        paneAdd.setDisable(false);
        paneAdd.setVisible(true);

        //clear other panes children

        txtLongNameMod.clear();
        txtShortNameMod.clear();
        txtXCoordMod.clear();
        txtYCoordMod.clear();
        txtLongNameDel.clear();
        txtShortNameDel.clear();
        txtAdminPassword.clear();
        txtAdminUser.clear();
        BooleanBinding booleanBind = Bindings.or(txtYCoord.textProperty().isEmpty(),
                txtXCoord.textProperty().isEmpty()).or(txtLongName.textProperty().isEmpty()).or(txtXCoord3D.textProperty().isEmpty()).or(txtYCoord3D.textProperty().isEmpty());

        btnAddNode.disableProperty().bind(booleanBind);


        //set up comboboxes
        cmboBuilding.setItems(buildings);

    }

    public void displayDeletePane(ActionEvent event) {   //delete Node
        paneAdd.setVisible(false);
        paneAdd.setDisable(true);
        paneModify.setVisible(false);
        paneModify.setDisable(true);

        paneDelete.setDisable(false);
        paneDelete.setVisible((true));

        txtLongNameDel.setEditable(true);
        txtShortNameDel.setEditable(true);


        txtLongName.clear();
        txtShortName.clear();
        txtXCoord.clear();
        txtYCoord.clear();
        txtLongNameMod.clear();
        txtShortNameMod.clear();
        txtXCoordMod.clear();
        txtYCoordMod.clear();

        BooleanBinding booleanBind = Bindings.or(txtAdminPassword.textProperty().isEmpty(),
                txtAdminUser.textProperty().isEmpty());
        btnDeleteNode.disableProperty().bind(booleanBind);

        cmboBuildingDel.setItems(buildings);

    }


    public void getXandY(MouseEvent event) throws Exception {
        //see which pane is visible and set the corresponding x and y coordinates
       /* if (selectNode == true) {
            if ((paneModify.isVisible() == true) && (selectNode == true)) {
                System.out.println("looking for edge node");
                ArrayList<Node> nodesXY = new ArrayList<>(DataModelI.getInstance().retrieveNodes());
                double tapX = event.getX();
                double tapY = event.getY();
                for (Node cur : nodesXY) {
                    if (((tapX - 10 <= cur.getXCoord()) && (cur.getXCoord() <= tapX + 10)) && ((tapY - 10 <= cur.getYCoord()) && (cur.getYCoord() <= tapY + 10))) {
                        edgeNodeAdd = cur;
                        btnSelectEdgeNode.setText(edgeNodeAdd.getLongName());
                        selectNode = false;

                    } else {
                        btnSelectEdgeNode.setText("no Node Found");
                    }

                }
            }
        } else {

            if (selectNode == false) {
                if ((paneAdd.isVisible() == true) && (mapNodeChoice == true)) {
                    txtXCoord.setText(String.format("%1.0f", event.getX()));
                    txtYCoord.setText(String.format("%1.0f", event.getY()));
                } else if ((paneAdd.isVisible() == true) && (mapNodeChoice == false)) {
                    txtXCoord3D.setText(String.format("%1.0f", event.getX()));
                    txtYCoord3D.setText(String.format("%1.0f", event.getY()));
                } else if ((paneAdd.isVisible() == false) && (mapNodeChoice == true)) {
                    txtXCoordMod.setText(String.format("%1.0f", event.getX()));
                    txtYCoordMod.setText(String.format("%1.0f", event.getY()));
                } else if ((paneAdd.isVisible() == false) && (mapNodeChoice == false)) {
                    txtXCoordMod3D.setText(String.format("%1.0f", event.getX()));
                    txtYCoordMod3D.setText(String.format("%1.0f", event.getY()));
                }
            }

        }*/
    }



    //logout and return to the home screen
    public void logOut(ActionEvent event) throws Exception {
        try {
            Stage stage;
            Parent root;
            //get reference to the button's stage
            stage = (Stage) btnLogOut.getScene().getWindow();
            //load up Home FXML document;
            logout = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/home.fxml"));

            KioskInfo.currentUserID = "";

            //create a new scene with root and set the stage
            Scene scene = new Scene(logout);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void accountManager(ActionEvent event) throws Exception {
        try {
            Stage stage;
            Parent root;
            //get reference to the button's stage
            stage = (Stage) navBtnManageAccounts.getScene().getWindow();
            //load up Home FXML document;
            accountManager = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/accountManager.fxml"));

            //create a new scene with root and set the stage
            Scene scene = new Scene(accountManager);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void manageRequests (ActionEvent event) throws Exception {
        try {
            Stage stage;
            Parent root;
            //get reference to the button's stage
            stage = (Stage) btnLogOut.getScene().getWindow();
            //load up Home FXML document;
            manageRequests = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/adminRequestDashBoard.fxml"));
            //create a new scene with root and set the stage
            Scene scene = new Scene(manageRequests);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Combo Box selected update next
    //Add node
    public void addSetBuilding(ActionEvent event) {
        building = cmboBuilding.getValue().toString();
        cmboFloorAdd.setItems(floors);

    }

    public void addSetFloor(ActionEvent event) {
        //set floor to selected value, use new value to populate Types
        floor = cmboFloorAdd.getValue().toString();
        btn2DMap.setDisable(false);
        btn3DMap.setDisable(false);
        cmboType.setItems(types);

        drawCircles(cmboFloorAdd.getValue(),"2-D");

    }

    public void addSetType(ActionEvent event) {
        //set type to selected value
        type = cmboType.getValue().toString();
    }

    //modify node
    public void modSetBuilding(ActionEvent event) {

        cmboFloor.setItems(floors);

    }

    public void modSetFloor(ActionEvent event) {
        //set floor to selected value, use new value to populate Types
        floor = cmboFloor.getValue().toString();
        btn2DMapMod.setDisable(false);
        btn3DMapMod.setDisable(false);
        cmboTypeMod.setItems(types);

        drawCircles(cmboFloor.getValue(),"2-D");
    }

    public void modSetType(ActionEvent event) {
        //set type to selected value
        type = cmboTypeMod.getValue().toString();
        List<Node> curNode = DataModelI.getInstance().retrieveNodes();
        System.out.println((int) curNode.size());
        List<String> currentN = DataModelI.getInstance().getLongNameByBuildingTypeFloor(cmboBuildingMod.getValue(),cmboTypeMod.getValue(),cmboFloor.getValue());
        cmboNodeMod.setItems(FXCollections.observableArrayList(currentN));


    }

    public void modSetNode(ActionEvent event) {
        //set type to selected value
        DataModelI.getInstance().retrieveNodes();
        node = DataModelI.getInstance().getNodeByLongName(cmboNodeMod.getValue().toString());
        txtLongNameMod.setText(node.getLongName());
        txtShortNameMod.setText(node.getShortName());

    }

    //delete node
    public void delSetBuilding(ActionEvent event) {
        building = cmboBuildingDel.getValue().toString();
        cmboFloorDel.setItems(floors);

    }

    public void delSetFloor(ActionEvent event) {
        //set floor to selected value, use new value to populate Types
        floor = cmboFloorDel.getValue().toString();
        cmboTypeDel.setItems(types);

    }

    public void delSetType(ActionEvent event) {
        //set type to selected value
        type = cmboTypeDel.getValue().toString();
        cmboNodeDel.setItems(FXCollections.observableArrayList(DataModelI.getInstance().getLongNameByBuildingTypeFloor(cmboBuildingDel.getValue(),cmboTypeDel.getValue(),cmboFloorDel.getValue())));

    }

    public void delSetNode(ActionEvent event) {
        //set type to selected value
        DataModelI.getInstance().retrieveNodes();
        node = DataModelI.getInstance().getNodeByLongName(cmboNodeDel.getValue().toString());
        txtLongNameDel.setText(node.getLongName());
        txtShortNameDel.setText(node.getShortName());
    }


    //Add Node
    public void addNode(ActionEvent event) {

        longName = txtLongName.getText();
        shortName = txtShortName.getText();
        xCoord2D = Integer.parseInt(txtXCoord.getText());
        yCoord2D = Integer.parseInt(txtYCoord.getText());
        xCoord3D = Integer.parseInt(txtXCoord3D.getText());
        yCoord3D = Integer.parseInt(txtYCoord3D.getText());
        building = cmboBuilding.getValue().toString();
        floor = cmboFloorAdd.getValue().toString();
        type = cmboType.getValue().toString();
        //call add node function
        DataModelI.getInstance().addNode(xCoord2D, yCoord2D, floor, building, type, longName, shortName, 1, xCoord3D, yCoord3D);
        btnAddNode.setText("Node Added!");
        //redraw map
        drawCircles(cmboFloorAdd.getValue(),"2-D");
        btnAddNode.setText("Add Node");
    }




    //modify node
    public void modifyNode(ActionEvent event) {
        longName = txtLongNameMod.getText();
        shortName = txtShortNameMod.getText();
        xCoord2D = Integer.parseInt(txtXCoordMod.getText());
        yCoord2D = Integer.parseInt(txtYCoordMod.getText());
        xCoord3D = Integer.parseInt(txtXCoordMod3D.getText());
        yCoord3D = Integer.parseInt(txtYCoordMod3D.getText());
        building = cmboBuildingMod.getValue().toString();
        floor = cmboFloor.getValue().toString();
        type = cmboTypeMod.getValue().toString();

        node.setLongName(longName);
        node.setNodeType(type);
        node.setLoc(new Location(xCoord2D, yCoord2D, xCoord3D, yCoord3D, building, floor));


        //call modify node function

        DataModelI.getInstance().addEdge(edgeNodeAdd, node);
        DataModelI.getInstance().modifyNode(node);

        btnModify.setText("Node Updated");
        //redraw map
    }

    //delete ode
    public void deleteNode(ActionEvent event) {
        longName = txtLongNameDel.getText();
        shortName = txtShortNameDel.getText();

        //set login check
        if (DataModelI.getInstance().doesUserPasswordExist(txtAdminUser.getText().toLowerCase(), txtAdminUser.getText().toLowerCase())) {


            building = cmboBuildingDel.getValue().toString();
            floor = cmboFloorDel.getValue().toString();
            type = cmboTypeDel.getValue().toString();

            //call delete node function
            node = DataModelI.getInstance().getNodeByLongName(longName);
            DataModelI.getInstance().removeNode(node);
        }
        else{
            txtAdminUser.setText("incorrect User and Password");
            txtAdminPassword.clear();
        }

        //redraw map
        drawCircles(cmboFloorAdd.getValue(),"2-D");
        btnAddNode.setText("Delete Node");
    }


    public void load2DMap(ActionEvent event) {
        drawCircles(cmboFloorAdd.getValue(),"2-D");
        mapNodeChoice = true;
    }

    public void load3DMap(ActionEvent event) {
        drawCircles(cmboFloorAdd.getValue(),"3-D");
        mapNodeChoice = false;
    }

    public void load2DMapMod(ActionEvent event) {
        drawCircles(cmboFloor.getValue(),"2-D");
        mapNodeChoice = true; //select 2d coord

    }

    public void load3DMapMod(ActionEvent event) {
        drawCircles(cmboFloor.getValue(),"3-D");
        mapNodeChoice = false; //select 3d coord


    }

    public void waitOnTapNode(ActionEvent event){
        selectNode = true;

    }

    public void geofence(ActionEvent event){
        node = DataModelI.getInstance().getNodeByLongName(cmboNodeMod.getValue().toString());
        int newStatus = 0;
                switch(node.getStatus()){
                    case 0:
                        newStatus = 0;
                        break;
                    case 1:
                        newStatus = 1;
                        break;
                        default:
                            newStatus = 0;
                }
                node.setStatus(newStatus);
    }

public void setPathfindAlgorithm(ActionEvent event) {
        String Pathfinding;
        Pathfinding = cmboPathfinding.getValue().toString();
         PathfindingContext Pf = new PathfindingContext();
        switch (Pathfinding){
            case "A*":
                Main.pathStrategy = "A*";
                break;
            case "Breadth-First Search":
                Main.pathStrategy = "BFS";
                break;
            case "Depth-First Search":
                Main.pathStrategy = "DFS";
                break;
        }
    }

    public void printPoints(String floor, String dimension) {
        // Connection for the database
        List<Node> nodeList = DataModelI.getInstance().retrieveNodes();

        // map boundaries

        int i = 0;
        int x = 0;
        int y = 0;
        // Iterate through each node
        while(i < nodeList.size()) {

            // If the node is on the correct floor
            if(nodeList.get(i).getFloor().equals(floor)) {

                if(dimension.equals("2-D")) {
                    // Get x and y coords
                    x = nodeList.get(i).getXCoord();
                    y = nodeList.get(i).getYCoord();
                } else if (dimension.equals("3-D")){
                    x = nodeList.get(i).getXCoord3D();
                    y = nodeList.get(i).getYCoord3D();
                } else {
                    System.out.println("Invalid dimension");
                }

                // draw the point on the image
                Circle circle = new Circle(x, y, 2);
                Circle outline = new Circle(x,y, 3);
                circle.setFill(Color.BLACK);
                outline.setFill(Color.GRAY);
                pane.getChildren().add(outline);
                pane.getChildren().add(circle);
            }
            i++;
        }
    }


    public void floor2DMapLoader(String floor) {


        if (floor.equals("FLOOR: L2") || floor.equals("L2")) {

            new ProxyImage(mapImg, "00_thelowerlevel2.png").display();


        } else if (floor.equals("FLOOR: L1") || floor.equals("L1")) {

            new ProxyImage(mapImg, "00_thelowerlevel1.png").display();


        } else if (floor.equals("FLOOR: 1") || floor.equals("1")) {

            new ProxyImage(mapImg, "01_thefirstfloor.png").display();


        } else if (floor.equals("FLOOR: 2") || floor.equals("2")) {

            new ProxyImage(mapImg, "02_thesecondfloor.png").display();


        } else if (floor.equals("FLOOR: 3") || floor.equals("3")) {

            new ProxyImage(mapImg, "03_thethirdfloor.png").display();


        }
    }

    public void floor3DMapLoader(String floor) {

        if (floor.equals("FLOOR: L2") || floor.equals("L2")) {
            new ProxyImage(mapImg, "L2-ICONS.png").display();

        } else if (floor.equals("FLOOR: L1") || floor.equals("L1")) {
            new ProxyImage(mapImg, "L1-ICONS.png").display();


        } else if (floor.equals("FLOOR: 1") || floor.equals("1")) {
            new ProxyImage(mapImg, "1-ICONS.png").display();


        } else if (floor.equals("FLOOR: 2") || floor.equals("2")) {
            new ProxyImage(mapImg, "2-ICONS.png").display();


        } else if (floor.equals("FLOOR: 3") || floor.equals("3")) {
            new ProxyImage(mapImg, "3-ICONS.png").display();
        }
    }

    public void drawCircles(String floor,String dimension) {

       // paneMap.getChildren().clear();

        // Iterate through each node
       for(Node currNode: nodeList) {
           // If the node is on the correct floor
           if (currNode.getFloor().equals(floor)) {
               if (dimension.equals("2-D")) {

                   Circle tempCircle = new Circle();
                   tempCircle.setCenterX(currNode.getXCoord());
                   tempCircle.setCenterY(currNode.getYCoord());
                   tempCircle.setRadius(8);
                   tempCircle.setFill(Color.NAVY);
                   tempCircle.setVisible(true);
                  // tempCircle.setOnMouseClicked(this::chooseNodeEdge);
                   tempCircle.setOnMousePressed(new EventHandler<MouseEvent>() {
                       @Override public void handle(MouseEvent mouseEvent) {
                           // record a delta distance for the drag and drop operation.
                           dragDelta.x = tempCircle.getLayoutX() - mouseEvent.getSceneX();
                           dragDelta.y = tempCircle.getLayoutY() - mouseEvent.getSceneY();
                           tempCircle.setCursor(Cursor.MOVE);

                       }
                   });
                   tempCircle.setOnMouseDragged(new EventHandler<MouseEvent>() {
                       @Override public void handle(MouseEvent mouseEvent) {
                           tempCircle.setLayoutX(mouseEvent.getSceneX() + dragDelta.x);
                           tempCircle.setLayoutY(mouseEvent.getSceneY() + dragDelta.y);

                           currNode.getLoc().setxCoord((int)tempCircle.getCenterX());
                           currNode.getLoc().setyCoord((int)tempCircle.getCenterY());
                           DataModelI.getInstance().modifyNode(currNode);


                       }
                   });
                   tempCircle.setCursor(Cursor.HAND);
                   paneMap.getChildren().add(tempCircle);
                   circleList.add(tempCircle);

               } else {

                   Circle tempCircle = new Circle();
                   tempCircle.setCenterX(currNode.getXCoord3D());
                   tempCircle.setCenterY(currNode.getYCoord3D());
                   tempCircle.setRadius(8);
                   tempCircle.setFill(Color.NAVY);
                   tempCircle.setVisible(true);
                   tempCircle.setOnMouseClicked(this::chooseNodeEdge);
                   paneMap.getChildren().add(tempCircle);
                   circleList.add(tempCircle);
               }
           }
       }
            }


    public void chooseNodeEdge(MouseEvent event) {
        Circle circle = (Circle)event.getTarget();
        if(mapNodeChoice == true) {
            for (Node node : nodeList) {
                if (node.getXCoord() == circle.getCenterX()) {
                    if (node.getYCoord() == circle.getCenterY()) {
                        edgeNodeAdd = node;
                        circle.setFill(Color.RED);
                        System.out.println("Click recognized");
                        btnSelectEdgeNode.setText(node.getLongName());
                        break;
                    }
                }
            }
            System.out.println("Node not found");
        } else {
            for (Node node : nodeList) {
                if (node.getXCoord3D() == circle.getCenterX()) {
                    if (node.getYCoord3D() == circle.getCenterY()) {
                        edgeNodeAdd = node;
                        circle.setFill(Color.RED);
                        System.out.println("Click recognized");
                        btnSelectEdgeNode.setText(node.getLongName());
                        break;
                    }
                }
            }
            System.out.println("Node not found");
        }
    }

    final Delta dragDelta = new Delta();
    class Delta { double x, y; }

    ///draws edges based on 2d or 3d map and floor,
    public void drawEdges(String floor, String dimension) {
        List<Edge> allEdges = DataModelI.getInstance().getEdgeList();

        for (Edge curEdge : allEdges) {
            Line edgeLine = new Line();
            Node start = DataModelI.getInstance().getNodeByID(curEdge.getStartNodeID());
            Node end = DataModelI.getInstance().getNodeByID(curEdge.getEndNodeID());

            Circle startCirc = new Circle();
            Circle endCirc = new Circle();

            for(Circle curCirc : circleList) {
                if (curCirc.getCenterX() == start.getXCoord()&& (curCirc.getCenterY() == start.getYCoord())){
                    startCirc = curCirc;
                }
                else if (curCirc.getCenterX() == end.getXCoord()&& (curCirc.getCenterY() == end.getYCoord())){
                    endCirc = curCirc;
                }

            }
            if ((start.getFloor().equals(floor) && (end.getFloor().equals(floor)))) {
                if (dimension.equals("2-D")) {
                   DoubleProperty startx = startCirc.centerXProperty();
                    DoubleProperty starty = startCirc.centerYProperty();
                    DoubleProperty endx = endCirc.centerXProperty();
                    DoubleProperty endy = endCirc.centerYProperty();

                    edgeLine.startXProperty().bindBidirectional(startx);
                    edgeLine.startYProperty().bindBidirectional(starty);
                    edgeLine.endXProperty().bindBidirectional(endx);
                    edgeLine.endYProperty().bindBidirectional(endy);

                } else if (dimension.equals("3-D")) {
                    DoubleProperty startx = new SimpleDoubleProperty(Double.valueOf(start.getXCoord3D()));
                    DoubleProperty starty = new SimpleDoubleProperty(Double.valueOf(start.getYCoord3D()));
                    DoubleProperty endx = new SimpleDoubleProperty(Double.valueOf(end.getXCoord3D()));
                    DoubleProperty endy = new SimpleDoubleProperty(Double.valueOf(end.getYCoord3D()));

                    edgeLine.startXProperty().bind(startx);
                    edgeLine.startYProperty().bind(starty);
                    edgeLine.endXProperty().bind(endx);
                    edgeLine.endYProperty().bind(endy);

                }
                if (curEdge.getStatus() == 1) {
                    edgeLine.setStrokeWidth(5.0);
                    edgeLine.setStroke(Color.DARKMAGENTA);
                } else {
                    edgeLine.setStrokeWidth(5.0);
                    edgeLine.setStroke(Color.RED);
                }

            }
            edgeLine.setOnMouseClicked(this::edgeSelected);
            paneMap.getChildren().add(edgeLine);

        }
    }

       public void edgeSelected(MouseEvent event){
        Line selected = (Line)event.getTarget();

    }
}
