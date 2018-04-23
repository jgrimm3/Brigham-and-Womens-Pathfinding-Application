
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


/*
import pointConverter.TeamD.API.PointConverter. The point converter provides two functions for conversion,
 both of which return a double[]: convertTo3D(xCoord2D, yCoord2D, floorString and convertTo2D(xCoord3D, yCoord3D, floorString). Valid floor strings by default are "3", "2", "1", "L1", "L2".
 */

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
    Line selected = new Line();
    List<Node> nodeList = DataModelI.getInstance().getNodeList();
    List<Node> pathList = new ArrayList<>();
    LinkedList<Node> listForQR = new LinkedList<Node>();
    Image imageQRCode;
    String startFloor = "";
    String endFloor = "";
    List<Circle> circleList = new ArrayList<>();
    List<Line> edgeLines = new ArrayList<>();
    Boolean mapNodeChoice = true;
    Boolean selectNode = false;
    Boolean selectEdge = false;
    Parent history;


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
    Pane paneAdd;
    @FXML
    JFXComboBox<String> cmboBuilding;
    @FXML
    JFXComboBox<String> cmboType;

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
    JFXToggleButton tglEdge;
    @FXML
    JFXComboBox<String> cmboFloorAdd;
    @FXML
    Pane pane;
    @FXML
    ScrollPane scrollPane;
    @FXML
    JFXButton btnLogOut;
    @FXML
    ImageView mapImg;
    @FXML
    Button btn2DMap;
    @FXML
    Button btn3DMap;
    @FXML
    JFXButton btnDeleteNodePane;
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

    ArrayList<Circle> circlesSelected = new ArrayList<Circle>();
    ArrayList<Node> nodesSelected = new ArrayList<Node>();

    @FXML
    public void initialize() throws Exception {
        try {
            System.out.println("initializing");
            paneAdd.setDisable(false);
            paneAdd.setVisible(true);
            BooleanBinding booleanBindAdd = Bindings.or(txtYCoord.textProperty().isEmpty(),
                    txtXCoord.textProperty().isEmpty()).or(txtShortName.textProperty().isEmpty()).or(txtLongName.textProperty().isEmpty()).or(txtYCoord3D.textProperty().isEmpty()).or(txtXCoord3D.textProperty().isEmpty());
            btnMenuAdd.disableProperty().bind(booleanBindAdd);

            BooleanBinding booleanBindDel = Bindings.or(txtLongName.textProperty().isEmpty(),
                    (cmboBuilding.valueProperty().isNull()).or(cmboFloorAdd.valueProperty().isNull()).or(cmboType.valueProperty().isNull()));

            btnDeleteNodePane.disableProperty().bind(booleanBindDel);

            cmboBuilding.setItems(buildings);
            cmboFloorAdd.setItems(floors);
            cmboPathfinding.setItems(Algorithms);

            //disable toggle
            tglAddMapChange.disableProperty().bind((cmboFloorAdd.valueProperty().isNull()));

            scrollPane.setVvalue(0.65);
            scrollPane.setHvalue(0.25);
            path.setStrokeWidth(5);

            floor2DMapLoader("2");
            drawCircles("2", "2-D");
            drawEdges("2", "2-D");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void getXandY(MouseEvent event) throws Exception {
        //see which pane is visible and set the corresponding x and y coordinates
        if (mapNodeChoice == true) { // 2D

            txtXCoord.setText(Double.toString(event.getX()));
            txtYCoord.setText(Double.toString(event.getY()));
        }

        if (mapNodeChoice == false) { //3D

            txtXCoord3D.setText(Double.toString(event.getX()));
            txtYCoord3D.setText(Double.toString(event.getY()));
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
        clearEdges();
        clearPoints();
        paneMap.getChildren().removeAll();
        paneMap.getChildren().clear();
        if (tglAddMapChange.isSelected()) {
            // Switch 3-D
            tglAddMapChange.setText("3-D");
            stackPaneMap.setPrefHeight(2774);
            stackPaneMap.setPrefWidth(5000);
            mapImg.setFitHeight(2774);
            mapImg.setFitWidth(5000);
            paneMap.setPrefHeight(2774);
            paneMap.setPrefWidth(5000);
            edgeLines.clear();

            clearPoints();
            clearEdges();

            floor3DMapLoader(cmboFloorAdd.getValue());
            drawCircles(cmboFloorAdd.getValue(), "3-D");
            drawEdges(cmboFloorAdd.getValue(), "3-D");



        } else {
            stackPaneMap.setPrefHeight(3400);
            stackPaneMap.setPrefWidth(5000);
            mapImg.setFitHeight(3400);
            mapImg.setFitWidth(5000);
            paneMap.setPrefHeight(3400);
            paneMap.setPrefWidth(5000);
            edgeLines.clear();
            floor2DMapLoader(cmboFloorAdd.getValue());

            clearPoints();
            clearEdges();

            drawCircles(cmboFloorAdd.getValue(), "2-D");
            drawEdges(cmboFloorAdd.getValue(), "2-D");


        }

    }


    public void addSetType(ActionEvent event) {
        //set type to selected value
        type = cmboType.getValue().toString();
    }

    public void modSetFloor(ActionEvent event) {
        //set floor to selected value, use new value to populate Types
        floor = cmboFloorAdd.getValue().toString();
        clearEdges();
        clearPoints();
        nodesSelected.clear();
        paneMap.getChildren().removeAll();
        paneMap.getChildren().clear();

        floor = cmboFloorAdd.getValue().toString();
        cmboType.setItems(types);
        if (tglAddMapChange.isSelected()) {
            // Switch 3-D
            tglAddMapChange.setText("3-D");
            stackPaneMap.setPrefHeight(2774);
            stackPaneMap.setPrefWidth(5000);
            mapImg.setFitHeight(2774);
            mapImg.setFitWidth(5000);
            paneMap.setPrefHeight(2774);
            paneMap.setPrefWidth(5000);
            edgeLines.clear();

            floor3DMapLoader(cmboFloorAdd.getValue());

            drawCircles(cmboFloorAdd.getValue(), "3-D");
            drawEdges(cmboFloorAdd.getValue(), "3-D");

        } else {
            stackPaneMap.setPrefHeight(3400);
            stackPaneMap.setPrefWidth(5000);
            mapImg.setFitHeight(3400);
            mapImg.setFitWidth(5000);
            paneMap.setPrefHeight(3400);
            paneMap.setPrefWidth(5000);
            edgeLines.clear();
            floor2DMapLoader(cmboFloorAdd.getValue());

            drawCircles(cmboFloorAdd.getValue(), "2-D");
            drawEdges(cmboFloorAdd.getValue(), "2-D");

        }

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
        //redraw map
        clearPoints();
        clearEdges();
        paneMap.getChildren().removeAll();
        paneMap.getChildren().clear();
        nodeList = DataModelI.getInstance().getNodeList();
        if (tglAddMapChange.isSelected() == true) {
            drawCircles(cmboFloorAdd.getValue(), "3-D");
            drawEdges(cmboFloorAdd.getValue(), "3-D");
        }else{
            drawCircles(cmboFloorAdd.getValue(), "2-D");
            drawEdges(cmboFloorAdd.getValue(), "2-D");
        }
    }


    //modify node
    public void modifyNode(ActionEvent event) {
        longName = txtLongName.getText();
        shortName = txtShortName.getText();
        floor = cmboFloorAdd.getValue().toString();

        node.setLongName(longName);
        node.setNodeType(type);
        node.setLoc(new Location(xCoord2D, yCoord2D, xCoord3D, yCoord3D, building, floor));

        //call modify node function

        //DataModelI.getInstance().addEdge(edgeNodeAdd, node);
        DataModelI.getInstance().modifyNode(node);

        //  btnModifyNode.setText("Node Updated");
        //redraw map
    }

    //delete ode
    public void deleteNode(ActionEvent event) {
        longName = txtLongName.getText();
        shortName = txtShortName.getText();
        node = DataModelI.getInstance().getNodeByLongName(longName);

        //redraw map


        DataModelI.getInstance().permanentlyRemoveNode(node);
        txtShortName.clear();
        txtLongName.clear();

        clearPoints();
        clearEdges();
        paneMap.getChildren().clear();

        nodeList = DataModelI.getInstance().getNodeList();
        if (tglAddMapChange.isSelected() == true) {
            drawCircles(cmboFloorAdd.getValue(), "3-D");
            drawEdges(cmboFloorAdd.getValue(), "3-D");

        }else{
            drawCircles(cmboFloorAdd.getValue(), "2-D");
            drawEdges(cmboFloorAdd.getValue(), "2-D");
        }
    }


    public void geofence(ActionEvent event) {
        if(tglEdge.isSelected() == true) {
            nodesSelected.get(0).addAdjacentNode(nodesSelected.get(1));
            nodesSelected.get(1).addAdjacentNode(nodesSelected.get(0));

            DataModelI.getInstance().addEdge(nodesSelected.get(0), nodesSelected.get(1));
            DataModelI.getInstance().addEdge(nodesSelected.get(1), nodesSelected.get(0));
            if (tglAddMapChange.isSelected() == true) {

                drawEdges(cmboFloorAdd.getValue(), "3-D");

            } else {

                drawEdges(cmboFloorAdd.getValue(), "2-D");
            }


        }else{
            nodesSelected.get(0).removeAdjacentNode(nodesSelected.get(1));
            nodesSelected.get(1).removeAdjacentNode(nodesSelected.get(0));
            DataModelI.getInstance().permanentlyRemoveEdge(nodesSelected.get(0), nodesSelected.get(1));
            DataModelI.getInstance().permanentlyRemoveEdge(nodesSelected.get(1), nodesSelected.get(0));

            DataModelI.getInstance().modifyNode(nodesSelected.get(0));
            DataModelI.getInstance().modifyNode(nodesSelected.get(1));
            if (tglAddMapChange.isSelected() == true) {
                clearEdges();
                clearPoints();
                paneMap.getChildren().removeAll();
                paneMap.getChildren().clear();
                drawCircles(cmboFloorAdd.getValue(), "3-D");
                drawEdges(cmboFloorAdd.getValue(), "3-D");

            } else {
                clearEdges();
                clearPoints();
                paneMap.getChildren().removeAll();
                paneMap.getChildren().clear();
                drawCircles(cmboFloorAdd.getValue(), "2-D");
                drawEdges(cmboFloorAdd.getValue(), "2-D");
            }
        }


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
                    tempCircle.setRadius(11);
                    tempCircle.setFill(Color.NAVY);
                    tempCircle.setVisible(true);

                    //Anytime a node is clicked, populate the fields.
                    //then perform the desired action 2-D
                    tempCircle.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            //set affiliated node
                            node = currNode;

                            if (nodesSelected.size() == 0) {
                                nodesSelected.add(currNode);
                                circlesSelected.add(tempCircle);
                                //highlight circle
                                tempCircle.setFill(Color.GREEN);
                                tempCircle.setRadius(13);
                            }
                            else if (nodesSelected.size() == 1) {
                                nodesSelected.add(currNode);
                                circlesSelected.add(tempCircle);
                                //highlight circle
                                tempCircle.setFill(Color.GREEN);
                                tempCircle.setRadius(13);

                                if((nodesSelected.get(0).getAdjacentNodes().contains(nodesSelected.get(1))) ||((nodesSelected.get(1).getAdjacentNodes().contains(nodesSelected.get(0))))){
                                    tglEdge.setText("Delete Edge");
                                    tglEdge.setSelected(true);
                                }else {
                                    tglEdge.setText("Add Edge");
                                    tglEdge.setSelected(false);

                                }

                            }
                            else if (nodesSelected.size() == 2) {
                                circlesSelected.get(0).setFill(Color.NAVY);
                                circlesSelected.get(0).setRadius(11);
                                circlesSelected.remove(0);
                                nodesSelected.remove(0);

                                nodesSelected.add(currNode);
                                circlesSelected.add(tempCircle);
                                //highlight circle
                                tempCircle.setFill(Color.GREEN);
                                tempCircle.setRadius(13);
                                if((nodesSelected.get(0).getAdjacentNodes().contains(nodesSelected.get(1))) ||((nodesSelected.get(1).getAdjacentNodes().contains(nodesSelected.get(0))))){
                                    tglEdge.setText("Delete Edge");
                                    tglEdge.setSelected(true);
                                }else {
                                    tglEdge.setText("Add Edge");
                                    tglEdge.setSelected(false);

                                }
                            }

                            //fill fields
                            cmboBuilding.getSelectionModel().select(currNode.getBuilding());
                            cmboFloorAdd.getSelectionModel().select(currNode.getFloor());
                            cmboType.getSelectionModel().select(currNode.getNodeType());
                            txtLongName.setText(currNode.getLongName());
                            txtShortName.setText(currNode.getShortName());



                            //prepare node for action, add press listener
                            tempCircle.setOnMousePressed(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent mouseEvent) {

                                    tempCircle.setFill(Color.RED);
                                    // record a delta distance for the drag and drop operation.
                                    //dragDelta.x = tempCircle.getLayoutX() - mouseEvent.getSceneX();
                                    //dragDelta.y = tempCircle.getLayoutY() - mouseEvent.getSceneY();
                                    dragDelta.centX = tempCircle.getCenterX() - mouseEvent.getSceneX();
                                    dragDelta.centY = tempCircle.getCenterY() - mouseEvent.getSceneY();
                                    tempCircle.setCursor(Cursor.MOVE);

                                }

                            });
                            tempCircle.setOnMouseDragged(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent mouseEvent) {
                                    Circle mouse = (Circle) mouseEvent.getTarget();
                                    // tempCircle.setLayoutX(mouseEvent.getSceneX() + dragDelta.x);
                                    //tempCircle.setLayoutY(mouseEvent.getSceneY() + dragDelta.y);
                                    tempCircle.setCenterX(mouseEvent.getSceneX() + dragDelta.centX);
                                    tempCircle.setCenterY(mouseEvent.getSceneY() + dragDelta.centY);
                                    tempCircle.setFill(Color.RED);
                                    Location junbongLoc = new Location((int) tempCircle.getCenterX(), (int) tempCircle.getCenterY(), currNode.getXCoord3D(), currNode.getYCoord3D(), currNode.getFloor(), currNode.getBuilding());
                                    currNode.setLoc(junbongLoc);

                                }
                            });
                            tempCircle.setOnMouseReleased(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent mouseEvent) {
                                    // record a delta distance for the drag and drop operation.
                                    Circle mouse = (Circle) mouseEvent.getTarget();
                                    tempCircle.setFill(Color.DARKCYAN);
                                    tempCircle.setCenterX(mouse.getCenterX());
                                    tempCircle.setCenterY(mouse.getCenterY());
                                    Location junbongLoc = new Location((int) mouse.getCenterX(), (int) mouse.getCenterY(), currNode.getXCoord3D(), currNode.getYCoord3D(), currNode.getFloor(), currNode.getBuilding());
                                    currNode.setLoc(junbongLoc);

                                    DataModelI.getInstance().modifyNode(currNode);
                                    nodeList = DataModelI.getInstance().getNodeList();
                                    clearEdges();
                                    clearPoints();
                                    paneMap.getChildren().removeAll();
                                    paneMap.getChildren().clear();
                                    drawCircles(floor, "2-D");
                                    drawEdges(floor, "2-D");

                                }
                            });
                        }
                    });


                    tempCircle.setCursor(Cursor.HAND);
                    paneMap.getChildren().add(tempCircle);
                    circleList.add(tempCircle);

                } else if (dimension.equals("3-D")) {

                    Circle tempCircle = new Circle();
                    tempCircle.setCenterX(currNode.getXCoord3D());
                    tempCircle.setCenterY(currNode.getYCoord3D());
                    tempCircle.setRadius(11);
                    tempCircle.setFill(Color.NAVY);
                    tempCircle.setVisible(true);

                    //Anytime a node is clicked, populate the fields.
                    //then perform the desired action 3-D
                    tempCircle.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            //set affiliated node
                            node = currNode;
                            if (nodesSelected.size() == 0) {
                                nodesSelected.add(currNode);
                                circlesSelected.add(tempCircle);
                                //highlight circle
                                tempCircle.setFill(Color.GREEN);
                                tempCircle.setRadius(13);
                            }
                            else if (nodesSelected.size() == 1) {
                                nodesSelected.add(currNode);
                                circlesSelected.add(tempCircle);
                                //highlight circle
                                tempCircle.setFill(Color.GREEN);
                                tempCircle.setRadius(13);

                                if((nodesSelected.get(0).getAdjacentNodes().contains(nodesSelected.get(1))) ||((nodesSelected.get(1).getAdjacentNodes().contains(nodesSelected.get(0))))){
                                    tglEdge.setText("Delete Edge");
                                    tglEdge.setSelected(true);
                                }else {
                                    tglEdge.setText("Add Edge");
                                    tglEdge.setSelected(false);

                                }

                            }
                            else if (nodesSelected.size() == 2) {
                                circlesSelected.get(0).setFill(Color.NAVY);
                                circlesSelected.get(0).setRadius(11);
                                circlesSelected.remove(0);
                                nodesSelected.remove(0);

                                nodesSelected.add(currNode);
                                circlesSelected.add(tempCircle);
                                //highlight circle
                                tempCircle.setFill(Color.GREEN);
                                tempCircle.setRadius(13);
                                if((nodesSelected.get(0).getAdjacentNodes().contains(nodesSelected.get(1))) ||((nodesSelected.get(1).getAdjacentNodes().contains(nodesSelected.get(0))))){
                                    tglEdge.setText("Delete Edge");
                                    tglEdge.setSelected(true);
                                }else {
                                    tglEdge.setText("Add Edge");
                                    tglEdge.setSelected(false);

                                }
                            }

                            //highlight circle
                            tempCircle.setFill(Color.GREEN);
                            tempCircle.setRadius(13);
                            //fill fields
                            cmboBuilding.getSelectionModel().select(currNode.getBuilding());
                            cmboFloorAdd.getSelectionModel().select(currNode.getFloor());
                            cmboType.getSelectionModel().select(currNode.getNodeType());
                            txtLongName.setText(currNode.getLongName());
                            txtShortName.setText(currNode.getShortName());
                            for (Line curLine : edgeLines) {
                                if (((curLine.getStartY() == tempCircle.getCenterY()) && (curLine.getStartX() == tempCircle.getCenterX())) || (curLine.getEndY() == tempCircle.getCenterY()) && (curLine.getEndX() == tempCircle.getCenterX())) {
                                    curLine.setStrokeWidth(8.0);
                                    curLine.setStroke(Color.YELLOW);
                                }
                            }

                            //prepare node for action, add press listener
                            tempCircle.setOnMousePressed(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent mouseEvent) {

                                    tempCircle.setFill(Color.RED);
                                    // record a delta distance for the drag and drop operation.
                                    //dragDelta.x = tempCircle.getLayoutX() - mouseEvent.getSceneX();
                                    //dragDelta.y = tempCircle.getLayoutY() - mouseEvent.getSceneY();
                                    dragDelta.centX = tempCircle.getCenterX() - mouseEvent.getSceneX();
                                    dragDelta.centY = tempCircle.getCenterY() - mouseEvent.getSceneY();
                                    tempCircle.setCursor(Cursor.MOVE);

                                }

                            });
                            tempCircle.setOnMouseDragged(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent mouseEvent) {
                                    Circle mouse = (Circle) mouseEvent.getTarget();
                                    // tempCircle.setLayoutX(mouseEvent.getSceneX() + dragDelta.x);
                                    //tempCircle.setLayoutY(mouseEvent.getSceneY() + dragDelta.y);
                                    tempCircle.setCenterX(mouseEvent.getSceneX() + dragDelta.centX);
                                    tempCircle.setCenterY(mouseEvent.getSceneY() + dragDelta.centY);
                                    tempCircle.setFill(Color.RED);
                                    Location junbongLoc = new Location(currNode.getXCoord(), currNode.getYCoord(), (int) tempCircle.getCenterX(), (int) tempCircle.getCenterY(), currNode.getFloor(), currNode.getBuilding());
                                    currNode.setLoc(junbongLoc);

                                }
                            });
                            tempCircle.setOnMouseReleased(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent mouseEvent) {
                                    // record a delta distance for the drag and drop operation.
                                    Circle mouse = (Circle) mouseEvent.getTarget();
                                    tempCircle.setFill(Color.DARKCYAN);
                                    tempCircle.setCenterX(mouse.getCenterX());
                                    tempCircle.setCenterY(mouse.getCenterY());
                                    Location junbongLoc = new Location(currNode.getXCoord(), currNode.getYCoord(), (int) mouse.getCenterX(), (int) mouse.getCenterY(), currNode.getFloor(), currNode.getBuilding());
                                    currNode.setLoc(junbongLoc);

                                    DataModelI.getInstance().modifyNode(currNode);
                                    nodeList = DataModelI.getInstance().getNodeList();
                                    clearEdges();
                                    clearPoints();
                                    paneMap.getChildren().removeAll();
                                    paneMap.getChildren().clear();
                                    drawCircles(floor, "3-D");
                                    drawEdges(floor, "3-D");

                                }
                            });
                        }

                    });

                    tempCircle.setCursor(Cursor.HAND);
                    paneMap.getChildren().add(tempCircle);
                    circleList.add(tempCircle);
                }
            }
        }
    }


    final Delta dragDelta = new Delta();

    class Delta {
        double x, y, centX, centY;
    }


    ///draws edges based on 2d or 3d map and floor,
    public void drawEdges(String floor, String dimension) {
        nodeList = DataModelI.getInstance().getNodeList();

        //   paneMap.getChildren().clear();
        List<Edge> allEdges = DataModelI.getInstance().getEdgeList();

        for (Edge curEdge : allEdges) {
            Line edgeLine = new Line();
            Node start = DataModelI.getInstance().getNodeByID(curEdge.getStartNodeID());
            Node end = DataModelI.getInstance().getNodeByID(curEdge.getEndNodeID());

            Circle startCirc = new Circle();
            Circle endCirc = new Circle();

            for (Circle curCirc : circleList) {
                if (curCirc.getCenterX() == start.getXCoord() && (curCirc.getCenterY() == start.getYCoord()) || (curCirc.getCenterX() == start.getXCoord3D() && (curCirc.getCenterY() == start.getYCoord3D()))){
                    startCirc = curCirc;
                } else if (curCirc.getCenterX() == end.getXCoord() && (curCirc.getCenterY() == end.getYCoord()) || (curCirc.getCenterX() == end.getXCoord3D() && (curCirc.getCenterY() == end.getYCoord3D()))) {
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
                    edgeLine.setStrokeWidth(5);
                    edgeLine.setStroke(Color.BLACK);
                } else {
                    edgeLine.setStrokeWidth(5.0);
                    edgeLine.setStroke(Color.RED);
                }

            }
            edgeLine.setCursor(Cursor.HAND);
            //edgeLine.setOnMouseClicked(this::edgeSelected);
            paneMap.getChildren().add(edgeLine);
        }


    }

    public void edgeSelected(MouseEvent event) {

        selected = (Line) event.getTarget();
        if (selectEdge == false) {
            selected.setStroke(Color.YELLOW);
            selected.setStrokeWidth(6);
            selectEdge = true;
        } else if (selectEdge == true) {
            selected.setStroke(Color.BLACK);
            selected.setStrokeWidth(5);
            selectEdge = false;
        }
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

    public void floor2DMapLoader(String floor) {
        mapNodeChoice = true;
        if (floor.equals("FLOOR: L2") || floor.equals("L2")) {

            new ProxyImage(mapImg, "00_thelowerlevel2.png").display();
            clearPoints();
            clearEdges();
            drawCircles("L2", "2-D");
            drawEdges("L2", "2-D");


        } else if (floor.equals("FLOOR: L1") || floor.equals("L1")) {

            new ProxyImage(mapImg, "00_thelowerlevel1.png").display();
            clearPoints();
            clearEdges();
            drawCircles("L1", "2-D");
            drawEdges("L1", "2-D");


        } else if (floor.equals("FLOOR: 1") || floor.equals("1")) {

            new ProxyImage(mapImg, "01_thefirstfloor.png").display();
            clearPoints();
            clearEdges();
            drawCircles("1", "2-D");
            drawEdges("1", "2-D");


        } else if (floor.equals("FLOOR: 2") || floor.equals("2")) {

            new ProxyImage(mapImg, "02_thesecondfloor.png").display();
            clearPoints();
            clearEdges();
            drawCircles("2", "2-D");
            drawEdges("2", "2-D");


        } else if (floor.equals("FLOOR: 3") || floor.equals("3")) {

            new ProxyImage(mapImg, "03_thethirdfloor.png").display();
            clearPoints();
            clearEdges();
            drawCircles("3", "2-D");
            drawEdges("3", "2-D");


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