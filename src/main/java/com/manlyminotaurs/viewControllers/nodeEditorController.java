
package com.manlyminotaurs.viewControllers;

import com.jfoenix.controls.*;

import com.manlyminotaurs.core.KioskInfo;
import com.manlyminotaurs.core.Main;
import com.manlyminotaurs.databases.DataModelI;
import com.manlyminotaurs.nodes.Edge;
import com.manlyminotaurs.nodes.Location;
import com.manlyminotaurs.nodes.Node;
import com.manlyminotaurs.pathfinding.PathfindingContext;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;

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
    Boolean mapNodeChoice = false;
    Boolean selectNode = false;
    Parent history;


    @FXML
    JFXToggleButton tglMap;
    @FXML
    JFXButton navBtnManageRequests;
    @FXML
    Path path;
    @FXML
    JFXComboBox<String> cmboPathfinding;
    @FXML
    JFXButton btnMenuAdd;
    @FXML
    JFXButton btnModifyNode;
    @FXML
    JFXButton btnDeleteNode;
    @FXML
    Pane paneAdd;
    @FXML
    Pane paneModify;
    @FXML
    Pane paneDelete;
    @FXML
    JFXComboBox<String> cmboBuilding;
    @FXML
    JFXComboBox<String> cmboType;
    @FXML
    JFXComboBox<String> cmboBuildingMod;
    @FXML
    JFXComboBox<String> cmboTypeMod;
    @FXML
    JFXComboBox<String> cmboBuildingDel;
    @FXML
    JFXComboBox<String> cmboTypeDel;
    @FXML
    JFXComboBox<String> cmboNodeDel;
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
    JFXTextField txtShortNameDel;
    @FXML
    JFXTextField txtLongNameDel;
    @FXML
    JFXToggleButton tglGeofence;
    @FXML
    JFXComboBox<String> cmboFloor;
    @FXML
    JFXComboBox<String> cmboFloorAdd;
    @FXML
    JFXComboBox<String> cmboFloorDel;
    @FXML
    Pane pane;
    @FXML
    ScrollPane scrollPane;
    @FXML
    JFXButton btnLogOut;
    @FXML
    JFXButton btnAddNode;
    @FXML
    JFXButton btnModify;
    @FXML
    ImageView mapImg;
    @FXML
    Button btn2DMap;
    @FXML
    Button btn3DMap;
    @FXML
    JFXButton btnDeleteNodePane;
    @FXML
    JFXTextField txtAdminUser;
    @FXML
    JFXPasswordField txtAdminPassword;
    @FXML
    StackPane stackPaneMap;
    @FXML
    Pane paneMap;
    @FXML
    JFXButton navBtnManageAccounts;
    @FXML
    JFXButton btnHistory;
    @FXML
    JFXToggleButton tglAddMapChange;


    final ObservableList<String> buildings = FXCollections.observableArrayList(DataModelI.getInstance().getBuildingsFromList());
    final ObservableList<String> types = FXCollections.observableArrayList(DataModelI.getInstance().getTypesFromList());
    final static ObservableList<String> floors = FXCollections.observableArrayList("L2", "L1", "1", "2", "3");
    final static ObservableList<String> locations = FXCollections.observableArrayList("thePlace", "Jerry's house", "another place", "wong's house", "fdskjfas", "fsdfds", "Dfsd", "sfdd", "SFd");
    final static ObservableList<String> Algorithms = FXCollections.observableArrayList("A*", "Breadth-First Search", "Depth-First Search", "Dykstra");


    String longName;
    String shortName;
    String type;
    String floor;
    String building;
    Node node;
    String currentFloor;
    int xCoord2D;
    int yCoord2D;
    int xCoord3D;
    int yCoord3D;

    @FXML
    public void initialize() throws Exception {
        try {
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

            //disable toggle
            tglMap.disableProperty().bind((cmboFloor.valueProperty().isNull()));
            tglAddMapChange.disableProperty().bind((cmboFloorAdd.valueProperty().isNull()));

            scrollPane.setVvalue(0.65);
            scrollPane.setHvalue(0.25);
            path.setStrokeWidth(5);
            //printPoints("L2");

            floor2DMapLoader("2");
            drawEdges("2", "2-D");
            drawCircles("2", "2-D");


            // drawCircles("1", "2-D");
            // drawEdges("1", "2-D" );

        } catch (Exception e) {
            e.printStackTrace();
        }
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


        cmboBuildingMod.setItems(buildings);
    }

    public void displayAddPane(ActionEvent event) {   //add Node
        floor2DMapLoader("2");
        paneDelete.setVisible((false));
        paneDelete.setDisable(true);
        paneModify.setVisible(false);
        paneModify.setDisable(true);
        paneAdd.setDisable(false);
        paneAdd.setVisible(true);

        //clear other panes children

        txtLongNameMod.clear();
        txtShortNameMod.clear();
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


        BooleanBinding booleanBind = Bindings.or(txtAdminPassword.textProperty().isEmpty(),
                txtAdminUser.textProperty().isEmpty());
        btnDeleteNode.disableProperty().bind(booleanBind);

        cmboBuildingDel.setItems(buildings);
    }


    public void getXandY(MouseEvent event) throws Exception {
        //see which pane is visible and set the corresponding x and y coordinates
        if (paneAdd.isVisible() == true) {
            if (mapNodeChoice == true) { // 2D
                txtXCoord.setText(Double.toString(event.getX()));
                txtYCoord.setText(Double.toString(event.getY()));
            }

            if (mapNodeChoice == false) { //3D
                txtXCoord3D.setText(Double.toString(event.getX()));
                txtYCoord3D.setText(Double.toString(event.getY()));
            }
        }

    }


    Parent createRequest;

    //Combo Box selected update next
    //Add node
    public void addSetBuilding(ActionEvent event) {
        building = cmboBuilding.getValue().toString();
        cmboFloorAdd.setItems(floors);

    }

    public void addSetFloor(ActionEvent event) {
        //set floor to selected value, use new value to populate Types
        floor = cmboFloorAdd.getValue().toString();

        cmboType.setItems(types);

        if(paneAdd.isVisible() == true){
            floor = cmboFloorAdd.getValue().toString();
            cmboType.setItems(types);
            if (tglAddMapChange.isSelected()) {
                // Switch 3-D
                tglMap.setText("3-D");
                stackPaneMap.setPrefHeight(2774);
                stackPaneMap.setPrefWidth(5000);
                mapImg.setFitHeight(2774);
                mapImg.setFitWidth(5000);
                paneMap.setPrefHeight(2774);
                paneMap.setPrefWidth(5000);
                edgeLines.clear();

                floor3DMapLoader(cmboFloorAdd.getValue());
                drawEdges(cmboFloorAdd.getValue(), "3-D");
                drawCircles(cmboFloorAdd.getValue(), "3-D");
            } else {
                stackPaneMap.setPrefHeight(3400);
                stackPaneMap.setPrefWidth(5000);
                mapImg.setFitHeight(3400);
                mapImg.setFitWidth(5000);
                paneMap.setPrefHeight(3400);
                paneMap.setPrefWidth(5000);
                edgeLines.clear();
                floor2DMapLoader(cmboFloorAdd.getValue());

                drawEdges(cmboFloorAdd.getValue(), "2-D");
                drawCircles(cmboFloorAdd.getValue(), "2-D");
            }
        }
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

        if (paneModify.isVisible()== true){
            floor = cmboFloor.getValue().toString();
            cmboTypeMod.setItems(types);
        if (tglMap.isSelected()) {
            // Switch 3-D
            tglMap.setText("3-D");
            stackPaneMap.setPrefHeight(2774);
            stackPaneMap.setPrefWidth(5000);
            mapImg.setFitHeight(2774);
            mapImg.setFitWidth(5000);
            paneMap.setPrefHeight(2774);
            paneMap.setPrefWidth(5000);
            edgeLines.clear();

            floor3DMapLoader(cmboFloor.getValue());
            drawEdges(cmboFloor.getValue(), "3-D");
            drawCircles(cmboFloor.getValue(), "3-D");
        } else {
            stackPaneMap.setPrefHeight(3400);
            stackPaneMap.setPrefWidth(5000);
            mapImg.setFitHeight(3400);
            mapImg.setFitWidth(5000);
            paneMap.setPrefHeight(3400);
            paneMap.setPrefWidth(5000);
            edgeLines.clear();
            floor2DMapLoader(cmboFloor.getValue());

            drawEdges(cmboFloor.getValue(), "2-D");
            drawCircles(cmboFloor.getValue(), "2-D");
        }
        }
        else if(paneAdd.isVisible() == true){
            floor = cmboFloorAdd.getValue().toString();
            cmboType.setItems(types);
        if (tglAddMapChange.isSelected()) {
            // Switch 3-D
            tglMap.setText("3-D");
            stackPaneMap.setPrefHeight(2774);
            stackPaneMap.setPrefWidth(5000);
            mapImg.setFitHeight(2774);
            mapImg.setFitWidth(5000);
            paneMap.setPrefHeight(2774);
            paneMap.setPrefWidth(5000);
            edgeLines.clear();

            floor3DMapLoader(cmboFloorAdd.getValue());
            drawEdges(cmboFloorAdd.getValue(), "3-D");
            drawCircles(cmboFloorAdd.getValue(), "3-D");
        } else {
            stackPaneMap.setPrefHeight(3400);
            stackPaneMap.setPrefWidth(5000);
            mapImg.setFitHeight(3400);
            mapImg.setFitWidth(5000);
            paneMap.setPrefHeight(3400);
            paneMap.setPrefWidth(5000);
            edgeLines.clear();
            floor2DMapLoader(cmboFloorAdd.getValue());

            drawEdges(cmboFloorAdd.getValue(), "2-D");
            drawCircles(cmboFloorAdd.getValue(), "2-D");
        }
        }
    }

    public void modSetType(ActionEvent event) {
        //set type to selected value
        type = cmboTypeMod.getValue().toString();
        List<Node> curNode = DataModelI.getInstance().retrieveNodes();
        System.out.println((int) curNode.size());
        List<String> currentN = DataModelI.getInstance().getLongNameByBuildingTypeFloor(cmboBuildingMod.getValue(), cmboTypeMod.getValue(), cmboFloor.getValue());


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
        stackPaneMap.setPrefHeight(3400);
        stackPaneMap.setPrefWidth(5000);
        mapImg.setFitHeight(3400);
        mapImg.setFitWidth(5000);
        paneMap.setPrefHeight(3400);
        paneMap.setPrefWidth(5000);
        edgeLines.clear();
        floor2DMapLoader(floor);
        drawEdges(floor, "2-D");
        drawCircles(floor, "2-D");
    }

    public void delSetType(ActionEvent event) {
        //set type to selected value
        type = cmboTypeDel.getValue().toString();
        cmboNodeDel.setItems(FXCollections.observableArrayList(DataModelI.getInstance().getLongNameByBuildingTypeFloor(cmboBuildingDel.getValue(), cmboTypeDel.getValue(), cmboFloorDel.getValue())));

    }

    public void delSetNode(ActionEvent event) {
        //set type to selected value

    }


    //Add Node
    public void addNode(ActionEvent event) {

        longName = txtLongName.getText();
        shortName = txtShortName.getText();
        xCoord2D = (int) Double.parseDouble(txtXCoord.getText());
        yCoord2D = (int) Double.parseDouble(txtYCoord.getText());
        xCoord3D = (int)Double.parseDouble(txtXCoord3D.getText());
        yCoord3D = (int)Double.parseDouble(txtYCoord3D.getText());
        building = cmboBuilding.getValue().toString();
        floor = cmboFloorAdd.getValue().toString();
        type = cmboType.getValue().toString();
        //call add node function
        DataModelI.getInstance().addNode("", xCoord2D, yCoord2D, floor, building, type, longName, shortName, 1, xCoord3D, yCoord3D);
        btnAddNode.setText("Node Added!");
        //redraw map
        clearPoints();
       nodeList = DataModelI.getInstance().getNodeList();
        drawCircles(cmboFloorAdd.getValue(), "2-D");
        btnAddNode.setText("Add Node");
    }


    //modify node
    public void modifyNode(ActionEvent event) {
        longName = txtLongNameMod.getText();
        shortName = txtShortNameMod.getText();
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

            DataModelI.getInstance().permanentlyRemoveNode(node);
            clearPoints();
            nodeList = DataModelI.getInstance().getNodeList();
            drawCircles(cmboFloorDel.getValue(), "2-D");

        } else {
            txtAdminUser.setText("incorrect User and Password");
            txtAdminPassword.clear();
        }

        //redraw map

    }



    public void geofence(ActionEvent event) {
        int newStatus = 0;
        Node modNode = DataModelI.getInstance().getNodeByLongName(txtLongNameMod.getText());
        switch (modNode.getStatus()) {
            case 0:
                newStatus = 0;
                break;
            case 1:
                newStatus = 1;
                break;
            default:
                newStatus = 0;
        }
        modNode.setStatus(newStatus);
        nodeList = DataModelI.getInstance().getNodeList();
    }

    public void setPathfindAlgorithm(ActionEvent event) {
        String Pathfinding;
        Pathfinding = cmboPathfinding.getValue().toString();
        PathfindingContext Pf = new PathfindingContext();
        switch (Pathfinding) {
            case "A*":
                Main.pathStrategy = "A*";
                break;
            case "Breadth-First Search":
                Main.pathStrategy = "BFS";
                break;
            case "Depth-First Search":
                Main.pathStrategy = "DFS";
                break;
            case "Dykstra":
                Main.pathStrategy = "DYK";
                break;
            default:
                Main.pathStrategy = "A*";
        }
    }


    public void drawCircles(String floor, String dimension) {

        //paneMap.getChildren().clear();

        // Iterate through each node
        for (Node currNode : nodeList) {
            // If the node is on the correct floor
            if (currNode.getFloor().equals(floor)) {
                if (dimension.equals("2-D")) {

                    Circle tempCircle = new Circle();
                    tempCircle.setCenterX(currNode.getXCoord());
                    tempCircle.setCenterY(currNode.getYCoord());
                    tempCircle.setRadius(8);
                    tempCircle.setFill(Color.NAVY);
                    tempCircle.setVisible(true);
                    tempCircle.setOnMousePressed(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            if (paneModify.isVisible() == true) {
                                tempCircle.setFill(Color.RED);
                                // record a delta distance for the drag and drop operation.
                                dragDelta.x = tempCircle.getLayoutX() - mouseEvent.getSceneX();
                                dragDelta.y = tempCircle.getLayoutY() - mouseEvent.getSceneY();
                                txtLongNameMod.setText(currNode.getLongName());
                                txtShortNameMod.setText( currNode.getShortName());
                                tempCircle.setCursor(Cursor.MOVE);
                            }
                            if (paneDelete.isVisible() == true) {
                                {
                                    tempCircle.setFill(Color.RED);
                                    txtLongNameDel.setText(currNode.getLongName());
                                    txtShortNameDel.setText(currNode.getShortName());
                                    cmboBuildingDel.getSelectionModel().select(currNode.getBuilding());
                                    cmboFloorDel.getSelectionModel().select(currNode.getFloor());
                                    cmboTypeDel.getSelectionModel().select(currNode.getNodeType());
                                    cmboNodeDel.getSelectionModel().select(currNode.getLongName());
                                    tempCircle.setFill(Color.RED);
                                }
                            }
                        }

                    });/**/
                    tempCircle.setOnMouseDragged(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            if (paneModify.isVisible() == true) {
                                tempCircle.setLayoutX(mouseEvent.getSceneX() + dragDelta.x);
                                tempCircle.setLayoutY(mouseEvent.getSceneY() + dragDelta.y);
                                tempCircle.setFill(Color.RED);
                                currNode.getLoc().setxCoord((int) tempCircle.getCenterX());
                                currNode.getLoc().setyCoord((int) tempCircle.getCenterY());
                            }
                        }
                    });
                    tempCircle.setOnMouseReleased(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            // record a delta distance for the drag and drop operation.
                                if (paneModify.isVisible() == true) {
                            tempCircle.setFill(Color.DARKCYAN);
                                    currNode.getLoc().setxCoord((int) tempCircle.getCenterX());
                                    currNode.getLoc().setyCoord((int) tempCircle.getCenterY());

                            DataModelI.getInstance().modifyNode(currNode);
                            nodeList = DataModelI.getInstance().getNodeList();
                            drawEdges(floor, "2-D");
                            drawCircles(floor, "2-D");
                        }}
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
                    tempCircle.setOnMousePressed(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            if (paneModify.isVisible() == true) {
                                tempCircle.setFill(Color.RED);
                                // record a delta distance for the drag and drop operation.
                                dragDelta.x = tempCircle.getLayoutX() - mouseEvent.getSceneX();
                                dragDelta.y = tempCircle.getLayoutY() - mouseEvent.getSceneY();
                                txtLongNameMod.setText(currNode.getLongName());
                                txtShortNameMod.setText( currNode.getShortName());
                                tempCircle.setCursor(Cursor.MOVE);
                            }
                            if (paneDelete.isVisible() == true) {
                                {
                                    tempCircle.setFill(Color.RED);
                                    txtLongNameDel.setText(currNode.getLongName());
                                    txtShortNameDel.setText(currNode.getShortName());
                                    cmboBuildingDel.getSelectionModel().select(currNode.getBuilding());
                                    cmboFloorDel.getSelectionModel().select(currNode.getFloor());
                                    cmboTypeDel.getSelectionModel().select(currNode.getNodeType());
                                    cmboNodeDel.getSelectionModel().select(currNode.getLongName());
                                    tempCircle.setFill(Color.RED);
                                }
                            }
                        }

                    });/**/
                    tempCircle.setOnMouseDragged(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            if (paneModify.isVisible() == true) {
                                tempCircle.setLayoutX(mouseEvent.getSceneX() + dragDelta.x);
                                tempCircle.setLayoutY(mouseEvent.getSceneY() + dragDelta.y);
                                tempCircle.setFill(Color.RED);
                                currNode.getLoc().setxCoord((int) tempCircle.getCenterX());
                                currNode.getLoc().setyCoord((int) tempCircle.getCenterY());
                            }
                        }
                    });
                    tempCircle.setOnMouseReleased(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            // record a delta distance for the drag and drop operation.
                            if (paneModify.isVisible() == true) {
                                tempCircle.setFill(Color.DARKCYAN);
                                currNode.getLoc().setxCoord3D((int) tempCircle.getCenterX());
                                currNode.getLoc().setyCoord3D((int) tempCircle.getCenterY());

                                DataModelI.getInstance().modifyNode(currNode);
                                nodeList = DataModelI.getInstance().getNodeList();
                                drawEdges(floor, "3-D");
                                drawCircles(floor, "3-D");
                            }}
                    });
                    tempCircle.setCursor(Cursor.HAND);
                    paneMap.getChildren().add(tempCircle);
                    circleList.add(tempCircle);


                    paneMap.getChildren().add(tempCircle);
                    circleList.add(tempCircle);
                }
            }
        }
    }


    final Delta dragDelta = new Delta();

    class Delta {
        double x, y;
    }

    ///draws edges based on 2d or 3d map and floor,
    public void drawEdges(String floor, String dimension) {
        nodeList = DataModelI.getInstance().getNodeList();
        List<Edge> allEdges = DataModelI.getInstance().getEdgeList();
        paneMap.getChildren().clear();

        for (Edge curEdge : allEdges) {
            Line edgeLine = new Line();
            Node start = DataModelI.getInstance().getNodeByID(curEdge.getStartNodeID());
            Node end = DataModelI.getInstance().getNodeByID(curEdge.getEndNodeID());

            Circle startCirc = new Circle();
            Circle endCirc = new Circle();

            for (Circle curCirc : circleList) {
                if (curCirc.getCenterX() == start.getXCoord() && (curCirc.getCenterY() == start.getYCoord())) {
                    startCirc = curCirc;
                } else if (curCirc.getCenterX() == end.getXCoord() && (curCirc.getCenterY() == end.getYCoord())) {
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

    public void edgeSelected(MouseEvent event) {
        Line selected = (Line) event.getTarget();

    }

    private void clearPoints() {
        for (Circle c : circleList) {
            paneMap.getChildren().remove(c);
        }
    }

    private void clearEdges() {
        for (Line L : edgeLines) {
            paneMap.getChildren().remove(L);
        }

    }

    public void loadMap(ActionEvent event) {
        if (tglMap.isSelected()) {

            // Switch 3-D
            tglMap.setText("3-D");
            stackPaneMap.setPrefHeight(2774);
            stackPaneMap.setPrefWidth(5000);
            mapImg.setFitHeight(2774);
            mapImg.setFitWidth(5000);
            paneMap.setPrefHeight(2774);
            paneMap.setPrefWidth(5000);


            floor3DMapLoader(cmboFloor.getValue());
        } else {

            // Switch 2-D
            tglMap.setText("2-D");
            stackPaneMap.setPrefHeight(3400);
            stackPaneMap.setPrefWidth(5000);
            mapImg.setFitHeight(3400);
            mapImg.setFitWidth(5000);
            paneMap.setPrefHeight(3400);
            paneMap.setPrefWidth(5000);

            floor2DMapLoader(cmboFloor.getValue());
        }

    }

    public void floor2DMapLoader(String floor) {
        mapNodeChoice = true;
        if (floor.equals("FLOOR: L2") || floor.equals("L2")) {

            new ProxyImage(mapImg, "00_thelowerlevel2.png").display();
            clearPoints();
            clearEdges();
            drawEdges("L2", "2-D");
            drawCircles("L2", "2-D");

        } else if (floor.equals("FLOOR: L1") || floor.equals("L1")) {

            new ProxyImage(mapImg, "00_thelowerlevel1.png").display();
            clearPoints();
            clearEdges();
            drawEdges("L1", "2-D");
            drawCircles("L1", "2-D");

        } else if (floor.equals("FLOOR: 1") || floor.equals("1")) {

            new ProxyImage(mapImg, "01_thefirstfloor.png").display();
            clearPoints();
            clearEdges();
            drawEdges("1", "2-D");
            drawCircles("1", "2-D");

        } else if (floor.equals("FLOOR: 2") || floor.equals("2")) {

            new ProxyImage(mapImg, "02_thesecondfloor.png").display();
            clearPoints();
            clearEdges();
            drawEdges("2", "2-D");
            drawCircles("2", "2-D");

        } else if (floor.equals("FLOOR: 3") || floor.equals("3")) {

            new ProxyImage(mapImg, "03_thethirdfloor.png").display();
            clearPoints();
            clearEdges();
            drawEdges("3", "2-D");
            drawCircles("3", "2-D");

        }

        currentFloor = floor;
    }

    public void floor3DMapLoader(String floor) {
//		cancelFinish.setVisible(false);
//		cancelStart.setVisible(false);
        mapNodeChoice = false;
        if (floor.equals("FLOOR: L2") || floor.equals("L2")) {
            new ProxyImage(mapImg, "L2-ICONS.png").display();

            clearPoints();
            clearEdges();
            drawEdges("L2", "3-D");
            drawCircles("L2", "3-D");

        } else if (floor.equals("FLOOR: L1") || floor.equals("L1")) {
            new ProxyImage(mapImg, "L1-ICONS.png").display();

            clearPoints();
            clearEdges();
            drawEdges("L1", "3-D");
            drawCircles("L1", "3-D");

        } else if (floor.equals("FLOOR: 1") || floor.equals("1")) {
            new ProxyImage(mapImg, "1-ICONS.png").display();

            clearPoints();
            clearEdges();
            drawEdges("1", "3-D");
            drawCircles("1", "3-D");

        } else if (floor.equals("FLOOR: 2") || floor.equals("2")) {
            new ProxyImage(mapImg, "2-ICONS.png").display();
            clearPoints();
            clearEdges();
            drawEdges("2", "3-D");
            drawCircles("2", "3-D");

        } else if (floor.equals("FLOOR: 3") || floor.equals("3")) {
            new ProxyImage(mapImg, "3-ICONS.png").display();

            clearPoints();
            clearEdges();
            drawEdges("3", "3-D");
            drawCircles("3", "3-D");

        }

        currentFloor = floor;
    }
    //logout and return to the home screen
    public void LogOut(ActionEvent event) throws Exception {
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

    public void loadHistory(ActionEvent event) throws Exception {
        try {
            Stage stage;
            Parent root;
            //get reference to the button's stage
            stage = (Stage) btnLogOut.getScene().getWindow();
            //load up Home FXML document;
            history = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/adminHistory.fxml"));
            //create a new scene with root and set the stage
            Scene scene = new Scene(history);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void manageRequests(ActionEvent event) throws Exception {
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

    public void createRequest(ActionEvent event) throws Exception {
        try {
            Stage stage;
            Parent root;
            //get reference to the button's stage
            stage = (Stage) btnLogOut.getScene().getWindow();
            //load up Home FXML document;
            createRequest = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/CreateRequest.fxml"));
            //create a new scene with root and set the stage
            Scene scene = new Scene(createRequest);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
