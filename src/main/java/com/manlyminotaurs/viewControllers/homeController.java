package com.manlyminotaurs.viewControllers;

//import com.manlyminotaurs.core.KioskInfo;

import com.jfoenix.controls.*;
import com.manlyminotaurs.communications.SendEmail;
import com.manlyminotaurs.communications.SendTxt;
import com.manlyminotaurs.core.KioskInfo;
import com.manlyminotaurs.core.Main;
import com.manlyminotaurs.databases.DataModelI;
import com.manlyminotaurs.nodes.Node;
import com.manlyminotaurs.nodes.Room;
import com.manlyminotaurs.pathfinding.*;
import javafx.animation.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.TextFields;

public class homeController implements Initializable {

	//Nested Private Singleton
//    private static class Singleton {
//        private static Singleton instance = null;
//        PathfindingContext pathfindingContext = new PathfindingContext();
//        Boolean handicap;
//        //ratio of  map pixel to a real life meter
//		final double meterPerPixel = 0.099914;
//		final double feetPerMeter = 3.28084;
//        //average walking speed in meters per second
//		final double walkSpeed = 1.4;
//		final double walkSpeedFt = 4.593176;
//
//        private Singleton() {
//
//		}
//		private static class SingletonHolder {
//			private static Singleton MapController = new Singleton();
//
//		}
//
//		public static Singleton getInstance() {
//			return homeController.Singleton.SingletonHolder.MapController;
//		}
//	}

	//-----------------------------------------------------------------------------------------------------------------
	//
	//                                           Create objects
	//
	//-----------------------------------------------------------------------------------------------------------------

	OptionSingleton optionPicker = OptionSingleton.getOptionPicker();
	final static ObservableList<String> floors = FXCollections.observableArrayList("None", "L2", "L1", "1", "2", "3");
	//final static ObservableList<String> mapFloors = FXCollections.observableArrayList("FLOOR: L2", "FLOOR: L1", "FLOOR: 1", "FLOOR: 2", "FLOOR: 3");
	final static ObservableList<String> empty = FXCollections.observableArrayList();
	//final ObservableList<String> buildings = FXCollections.observableArrayList(DataModelI.getInstance().getBuildingsFromList());
	final static ObservableList<String> buildings = FXCollections.observableArrayList("None", "45 Francis", "Tower", "Shapiro", "BTM", "15 Francis");
	//final ObservableList<String> types = FXCollections.observableArrayList(DataModelI.getInstance().getTypesFromList());
	final static ObservableList<String> types = FXCollections.observableArrayList("None", "Laboratory", "Information", "Retail", "Bathroom", "Stair", "Service", "Restroom", "Elevator", "Department", "Conference", "Exit");
	ObservableList<String> directions;
	final int MAPX2D = 5000;
	final int MAPY2D = 3400;
	int nodeIconWidth = 40;
	int nodeIconHeight = 40;

	String currentFloor = "1";
	String currentDimension = "2-D";

	Parent adminRequest;
	Parent staffRequest;
	Circle startCircle = new Circle();
	List<Node> nodeList = new ArrayList<>();
	List<Node> mapNodeListL2 = new ArrayList<>();
	List<Node> mapNodeListL1 = new ArrayList<>();
	List<Node> mapNodeList1 = new ArrayList<>();
	List<Node> mapNodeList2 = new ArrayList<>();
	List<Node> mapNodeList3 = new ArrayList<>();
	List<Node> pathList = new LinkedList<>();
	LinkedList<Node> listForQR = new LinkedList<Node>();
	//List<javafx.scene.text.Text> nameList = new ArrayList<>();
	Image imageQRCode;
	String startFloor = "";
	String endFloor = "";
	List<Circle> circleList = new ArrayList<>();
	javafx.scene.text.Text currName;
	FadeTransition fade;
	List<String> breadcrumbs = new ArrayList<>();
	Boolean forStart = false;
	//Map<Integer, Map<Integer, Node>> nodeMap = new HashMap<>(); was trying to speed up start and end choose time

	//-----------------------------------------------------------------------------------------------------------------
	//
	//                                           Path-finding
	//
	//-----------------------------------------------------------------------------------------------------------------
	@FXML
	VBox panePathfinding;

	@FXML
	Label lblHandicap;

	@FXML
	Label lblMap;

	@FXML
	JFXComboBox<String> comBuildingStart;

	@FXML
	Label lblFloorStart;

	@FXML
	JFXComboBox<String> comFloorStart;

	@FXML
	Label lblTypeStart;

	@FXML
	JFXComboBox<String> comTypeStart;

	@FXML
	Label lblLocationStart;

	@FXML
	JFXComboBox<String> comBuildingEnd;

	@FXML
	Label lblFloorEnd;

	@FXML
	JFXComboBox<String> comFloorEnd;

	@FXML
	Label lblTypeEnd;

	@FXML
	JFXComboBox<String> comTypeEnd;

	@FXML
	Label lblLocationEnd;

	@FXML
	ImageView mapImg;

	@FXML
	ScrollPane scrollPaneMap;

	@FXML
	StackPane stackPaneMap;

	@FXML
	Pane paneMap;

	@FXML
	Path diffPath;

	@FXML
	Path currPath;

	@FXML
	JFXTextField txtLocationStart;

	@FXML
	JFXTextField txtLocationEnd;

	@FXML
	JFXListView<String> lstStartDirectory;

	@FXML
	JFXListView<String> lstEndDirectory;

	@FXML
	StackPane nodePane;

	@FXML
	JFXButton btnGo;

	@FXML
	JFXButton btnToggleMap;

	@FXML
	ImageView imgBtnMap;

	@FXML
	Group scrollGroup;

    @FXML
    TreeTableView<String> tblDirections;

    @FXML
    TreeTableColumn<String,String> colDirections;




    TreeItem<String> floorL2 = new TreeItem<>("Floor L2");
    TreeItem<String> floorL1 = new TreeItem<>("Floor L1");
    TreeItem<String> floor1 = new TreeItem<>("Floor 1");
    TreeItem<String> floor2 = new TreeItem<>("Floor 2");
    TreeItem<String> floor3 = new TreeItem<>("Floor 3");

    String etaDistance = "";

    TreeItem<String> root = new TreeItem<>(etaDistance);

    LinkedList<Node> floorL2DirectionNodes = new LinkedList<>();
    LinkedList<Node> floorL1DirectionNodes = new LinkedList<>();
    LinkedList<Node> floor1DirectionNodes = new LinkedList<>();
    LinkedList<Node> floor2DirectionNodes = new LinkedList<>();
    LinkedList<Node> floor3DirectionNodes = new LinkedList<>();

    ArrayList<String> directionsL2 = new ArrayList();
	ArrayList<String> directionsL1 = new ArrayList();
	ArrayList<String> directions1 = new ArrayList();
	ArrayList<String> directions2 = new ArrayList();
	ArrayList<String> directions3 = new ArrayList();


	public void setPathfindingScreen() {

		paneDirections.setVisible(false);

		comBuildingStart.setItems(buildings);
		comBuildingEnd.setItems(buildings);
		comFloorStart.setItems(floors);
		comFloorEnd.setItems(floors);
		comTypeStart.setItems(types);
		comTypeEnd.setItems(types);

		currentFloor = "1";
		currentDimension = "2-D";

		nodeList = DataModelI.getInstance().getNodeList();
		filterFloorLists(nodeList);

		changeFloor("1");

		printPoints("1", "2-D");

		TextFields.bindAutoCompletion(txtLocationStart, FXCollections.observableArrayList(DataModelI.getInstance().getNamesByBuildingFloorType(null, null, null)));
		TextFields.bindAutoCompletion(txtLocationEnd, FXCollections.observableArrayList(DataModelI.getInstance().getNamesByBuildingFloorType(null, null, null)));

		lstStartDirectory.setItems(FXCollections.observableList(DataModelI.getInstance().getNamesByBuildingFloorType(comBuildingStart.getValue(), comFloorStart.getValue(), convertType(comTypeStart.getValue()))));
		lstEndDirectory.setItems(FXCollections.observableList(DataModelI.getInstance().getNamesByBuildingFloorType(comBuildingEnd.getValue(), comFloorEnd.getValue(), convertType(comTypeEnd.getValue()))));

	}

	public void initialize(URL location, ResourceBundle resources) {
		try {


		 directionsL2.add("");
		 directionsL1.add("");
		 directions1.add("");
		 directions2.add("");
		 directions3.add("");


			setPathfindingScreen();
			printPoints("1", "2-D");
			scrollPaneMap.setContent(scrollGroup);
			setKiosk();
			printKiosk();
			goToKiosk();


		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// Remember to refresh nodes

	// Set Floor Map and Floor Combobox to correct setting

	@FXML
	public void initialize() {

		setPathfindingScreen();
		changeFloor("1");
		setStrategy();
		//createMap();


		root.getChildren().setAll(floor3, floor2,  floor1, floorL1, floorL2);
		colDirections.setCellValueFactory(
				(TreeTableColumn.CellDataFeatures<String, String> param) ->  new SimpleStringProperty(param.getValue().getValue()));

		tblDirections.setRoot(root);

		tblDirections.getRoot().setExpanded(true);
		tblDirections.getRoot().setExpanded(true);
		scrollPaneMap.setContent(scrollGroup);
		printPoints("1", "2-D");
		setKiosk();
		printKiosk();
		goToKiosk();

	}

	public void filterFloorLists(List<Node> listOfNodes) {

		ArrayList<Node> floorL2 = new ArrayList<Node>();
		ArrayList<Node> floorL1 = new ArrayList<Node>();
		ArrayList<Node> floor1 = new ArrayList<Node>();
		ArrayList<Node> floor2 = new ArrayList<Node>();
		ArrayList<Node> floor3 = new ArrayList<Node>();

		for(Node node: listOfNodes) {
			if (node.getFloor().equals("L2"))  {
				if(node.getNodeType().equals("HALL")) {
					continue;
				} else {
					floorL2.add(node);
				}
			} else if (node.getFloor().equals("L1")) {
				if(node.getNodeType().equals("HALL")) {
					continue;
				} else {
					floorL1.add(node);
				}
			} else if (node.getFloor().equals("1")) {
				if(node.getNodeType().equals("HALL")) {
					continue;
				} else {
					floor1.add(node);
				}
			} else if (node.getFloor().equals("2")) {
				if(node.getNodeType().equals("HALL")) {
					continue;
				} else {
					floor2.add(node);
				}
			} else if (node.getFloor().equals("3")) {
				if(node.getNodeType().equals("HALL")) {
					continue;
				} else {
					floor3.add(node);
				}
			}
		}

		mapNodeListL2 = floorL2;
		mapNodeListL1 = floorL1;
		mapNodeList1 = floor1;
		mapNodeList2 = floor2;
		mapNodeList3 = floor3;
	}

	public void setStrategy() {
		if (Main.pathStrategy.equals("A*")) {
//			Singleton.getInstance().pathfindingContext.strategy = new AStarStrategyI();
			optionPicker.pathfindingContext.strategy = new AStarStrategyI();
		}
		if (Main.pathStrategy.equals("BFS")) {
//			Singleton.getInstance().pathfindingContext.strategy = new BreadthFirstStrategyI();
			optionPicker.pathfindingContext.strategy = new BreadthFirstStrategyI();
		}
		if (Main.pathStrategy.equals("DFS")) {
//			Singleton.getInstance().pathfindingContext.strategy = new DepthFirstStrategyI();
			optionPicker.pathfindingContext.strategy = new DepthFirstStrategyI();
		}
		if (Main.pathStrategy.equals("DYK")) {
//		    Singleton.getInstance().pathfindingContext.strategy = new ClosestStrategyI();
			optionPicker.pathfindingContext.strategy = new ClosestStrategyI();
		}
	}

	public void toggleMap(ActionEvent event) {
		if (currentDimension.equals("2-D")) {
			// Switch 3-D
			new ProxyImage(imgBtnMap, "3DIcon.png").displayIcon();
			currentDimension = "3-D";
			stackPaneMap.setPrefHeight(2774);
			stackPaneMap.setPrefWidth(5000);
			mapImg.setFitHeight(2774);
			mapImg.setFitWidth(5000);
			paneMap.setPrefHeight(2774);
			paneMap.setPrefWidth(5000);
			floor3DMapLoader(currentFloor);
			printKiosk();
			clearPoints();
			printPoints(currentFloor, currentDimension);
		} else {

			// Switch 2-D
			new ProxyImage(imgBtnMap, "2DIcon.png").displayIcon();
			currentDimension = "2-D";
			stackPaneMap.setPrefHeight(3400);
			stackPaneMap.setPrefWidth(5000);
			mapImg.setFitHeight(3400);
			mapImg.setFitWidth(5000);
			paneMap.setPrefHeight(3400);
			paneMap.setPrefWidth(5000);
			floor2DMapLoader(currentFloor);
			printKiosk();
			clearPoints();
			printPoints(currentFloor, currentDimension);
		}

	}

	public void printKiosk() {

		Circle kiosk = new Circle();

		if (currentDimension.equals("3-D")) {
			kiosk = new Circle(KioskInfo.myLocation.getXCoord3D(), KioskInfo.myLocation.getYCoord3D(), 13);
		} else {
			kiosk = new Circle(KioskInfo.myLocation.getXCoord(), KioskInfo.myLocation.getYCoord(), 13);
		}
		//circleList.remove(kiosk);

		System.out.println(currentFloor + " is the floor");
		if (currentFloor.equals("1")) {
			kiosk.setFill(Color.BLUE);
			kiosk.setFill(Color.RED);
		} else {
			kiosk.setFill(Color.GRAY);
		}

		kiosk.setStrokeWidth(3);
		kiosk.setStroke(Color.BLACK);
		kiosk.setOnMouseClicked(this::startCircleClicked);
		kiosk.setOnMouseEntered(this::printStartName);
		kiosk.setOnMouseExited(this::removeStartName);
		pointMap.getChildren().add(kiosk);
		circleList.add(kiosk);
	}

	public void goToKiosk() {
		if (currentDimension.equals("3-D")) {
			scrollPaneMap.setVvalue((double) KioskInfo.myLocation.getYCoord() / 2774.0);
			scrollPaneMap.setHvalue((double) KioskInfo.myLocation.getXCoord() / 5000.0);
		} else {
			scrollPaneMap.setVvalue((double) KioskInfo.myLocation.getYCoord() / 3400.0);
			scrollPaneMap.setHvalue((double) KioskInfo.myLocation.getXCoord() / 5000.0);
		}
	}

	public void snap(Node startNode, Node endNode) {

		// 2D Variables
		int startX2D = startNode.getXCoord();
		int startY2D = startNode.getYCoord();
		int endX2D = endNode.getXCoord();
		int endY2D = endNode.getYCoord();

		// 3D Variables
		int startX3D = startNode.getXCoord3D();
		int startY3D = startNode.getYCoord3D();
		int endX3D = endNode.getXCoord3D();
		int endY3D = endNode.getYCoord3D();

		double snapX;
		double snapY;

		if (currentDimension.equals("3-D")) { // 3D

			snapY = (startY3D + ((endY3D - startY3D) / 2)) / 2774.0;
			snapX = (startX3D + ((endX3D - startX3D) / 2)) / 5000.0;

		} else { // 2D

			snapY = (startY2D + (((endY2D - startY2D) / 2)) - 200) / 3400.0;
			snapX = (startX2D + (((endX2D - startX2D) / 2)) - 300) / 5000.0;
		}

		scrollPaneMap.setVvalue(snapY);
		scrollPaneMap.setHvalue(snapX);

		System.out.println(snapX);
		System.out.println(snapY);
		System.out.println("start x2d " + startX2D);
		System.out.println("start y2d " + startY2D);
		System.out.println("start x3d " + startX3D);
		System.out.println("start y3d " + startY3D);
		System.out.println("end x2d " + endX2D);
		System.out.println("end y2d " + endY2D);
		System.out.println("end x3d " + endX3D);
		System.out.println("end y3d " + endY3D);
		System.out.println(scrollPaneMap.getVvalue());
		System.out.println(scrollPaneMap.getHvalue());


	}

	private void setKiosk() { // location isnt getting set correctly for floor or type
		comBuildingStart.getSelectionModel().select(KioskInfo.myLocation.getBuilding());
		comFloorStart.getSelectionModel().select(KioskInfo.myLocation.getFloor());
		comTypeStart.getSelectionModel().select(convertTypeReverse(KioskInfo.myLocation.getNodeType()));
		txtLocationStart.setText(KioskInfo.myLocation.getLongName());
		scrollPaneMap.setVvalue((double) KioskInfo.myLocation.getYCoord() / 3400.0);
		scrollPaneMap.setHvalue((double) KioskInfo.myLocation.getXCoord() / 5000.0);
		printKiosk();
	}

	public void printNodesOnFloorStart(MouseEvent event) {
		showStartAndEnd();
		hideStartAndEnd();
		//printKiosk();
		if (currentDimension.equals("3-D"))
			printPoints(currentFloor, "3-D");
		else
			printPoints(currentFloor, "2-D");

	}

	public void printNodesOnFloorEnd(MouseEvent event) {
		showStartAndEnd();
		hideStartAndEnd();
		if (currentDimension.equals("3-D"))
			printPoints(currentFloor, "3-D");
		else
			printPoints(currentFloor, "2-D");

	}

	private void showStartAndEnd() {
		clearPoints();
		printKiosk();
	}

	private void hideStartAndEnd() {
		comBuildingStart.setDisable(true);
		comBuildingEnd.setDisable(true);
		comFloorStart.setDisable(true);
		comFloorEnd.setDisable(true);
		comTypeStart.setDisable(true);
		comTypeEnd.setDisable(true);
	}

	public void chooseNode(MouseEvent event) {
		ImageView circle = (ImageView) event.getTarget();

		System.out.println("hi i clicked on a node");

		if (forStart == true) { // Update Location Start Text Field
			if (!currentDimension.equals("3-D")) { // 2D Start
				if (currentFloor.equals("L2")) {
					for (Node node : mapNodeListL2) {
						if ((node.getXCoord() >= circle.getX()) && (node.getXCoord() <= circle.getX()+nodeIconWidth)) {


							if ((node.getYCoord() >= circle.getY()) && (node.getYCoord() <= circle.getY()+nodeIconHeight)) {


								System.out.println("Click recognized");

								// Set Directory to Identify Start Location
								comBuildingStart.getSelectionModel().select(node.getBuilding());
								comFloorStart.getSelectionModel().select(node.getFloor());
								comTypeStart.getSelectionModel().select(convertTypeReverse(node.getNodeType()));

								// Set Start Text Field to Start Location
								txtLocationStart.setText(node.getLongName());
								//showStartAndEnd();
								break;
							}
						}
					}
					System.out.println("2D Node not found for start");
				} else if (currentFloor.equals("L1")) {
					for (Node node : mapNodeListL1) {
						if ((node.getXCoord() >= circle.getX()) && (node.getXCoord() <= circle.getX()+nodeIconWidth)) {


							if ((node.getYCoord() >= circle.getY()) && (node.getYCoord() <= circle.getY()+nodeIconHeight)) {


								System.out.println("Click recognized");

								// Set Directory to Identify Start Location
								comBuildingStart.getSelectionModel().select(node.getBuilding());
								comFloorStart.getSelectionModel().select(node.getFloor());
								comTypeStart.getSelectionModel().select(convertTypeReverse(node.getNodeType()));

								// Set Start Text Field to Start Location
								txtLocationStart.setText(node.getLongName());
								//showStartAndEnd();
								break;
							}
						}
					}
					System.out.println("2D Node not found for start");
				} else if (currentFloor.equals("1")) {
					for (Node node : mapNodeList1) {
						if ((node.getXCoord() >= circle.getX()) && (node.getXCoord() <= circle.getX()+nodeIconWidth)) {


							if ((node.getYCoord() >= circle.getY()) && (node.getYCoord() <= circle.getY()+nodeIconHeight)) {


								System.out.println("Click recognized");

								// Set Directory to Identify Start Location
								comBuildingStart.getSelectionModel().select(node.getBuilding());
								comFloorStart.getSelectionModel().select(node.getFloor());
								comTypeStart.getSelectionModel().select(convertTypeReverse(node.getNodeType()));

								// Set Start Text Field to Start Location
								txtLocationStart.setText(node.getLongName());
								//showStartAndEnd();
								break;
							}
						}
					}
					System.out.println("2D Node not found for start");
				}else if (currentFloor.equals("2")) {
					for (Node node : mapNodeList2) {
						if ((node.getXCoord() >= circle.getX()) && (node.getXCoord() <= circle.getX()+nodeIconWidth)) {


							if ((node.getYCoord() >= circle.getY()) && (node.getYCoord() <= circle.getY()+nodeIconHeight)) {


								System.out.println("Click recognized");

								// Set Directory to Identify Start Location
								comBuildingStart.getSelectionModel().select(node.getBuilding());
								comFloorStart.getSelectionModel().select(node.getFloor());
								comTypeStart.getSelectionModel().select(convertTypeReverse(node.getNodeType()));

								// Set Start Text Field to Start Location
								txtLocationStart.setText(node.getLongName());
								//showStartAndEnd();
								break;
							}
						}
					}
					System.out.println("2D Node not found for start");
				}else if (currentFloor.equals("3")) {
					for (Node node : mapNodeList3) {
						if ((node.getXCoord() >= circle.getX()) && (node.getXCoord() <= circle.getX()+nodeIconWidth)) {


							if ((node.getYCoord() >= circle.getY()) && (node.getYCoord() <= circle.getY()+nodeIconHeight)) {


								System.out.println("Click recognized");

								// Set Directory to Identify Start Location
								comBuildingStart.getSelectionModel().select(node.getBuilding());
								comFloorStart.getSelectionModel().select(node.getFloor());
								comTypeStart.getSelectionModel().select(convertTypeReverse(node.getNodeType()));

								// Set Start Text Field to Start Location
								txtLocationStart.setText(node.getLongName());
								//showStartAndEnd();
								break;
							}
						}
					}
					System.out.println("2D Node not found for start");
				}
			} else { // 3D Start
				if (currentFloor.equals("L2")) {
					for (Node node : mapNodeListL2) {
						if ((node.getXCoord3D() >= circle.getX()) && (node.getXCoord3D() <= circle.getX()+nodeIconWidth)) {
							if ((node.getYCoord3D() >= circle.getY()) && (node.getYCoord3D() <= circle.getY()+nodeIconHeight)) {
								System.out.println("Click recognized");

								// Set Directory to Identify Start Location
								comBuildingStart.getSelectionModel().select(node.getBuilding());
								comFloorStart.getSelectionModel().select(node.getFloor());
								comTypeStart.getSelectionModel().select(convertTypeReverse(node.getNodeType()));
								//comLocationStart.getSelectionModel().select(node.getLongName());

								// Set Start Text Field to Start Location
								txtLocationStart.setText(node.getLongName());
								//showStartAndEnd();
								break;
							}
						}
					}
					System.out.println("3D Node not found for start");
				} else if (currentFloor.equals("L1")) {
					for (Node node : mapNodeListL1) {
						if ((node.getXCoord3D() >= circle.getX()) && (node.getXCoord3D() <= circle.getX()+nodeIconWidth)) {
							if ((node.getYCoord3D() >= circle.getY()) && (node.getYCoord3D() <= circle.getY()+nodeIconHeight)) {
								System.out.println("Click recognized");

								// Set Directory to Identify Start Location
								comBuildingStart.getSelectionModel().select(node.getBuilding());
								comFloorStart.getSelectionModel().select(node.getFloor());
								comTypeStart.getSelectionModel().select(convertTypeReverse(node.getNodeType()));
								//comLocationStart.getSelectionModel().select(node.getLongName());

								// Set Start Text Field to Start Location
								txtLocationStart.setText(node.getLongName());
								//showStartAndEnd();
								break;
							}
						}
					}
					System.out.println("3D Node not found for start");
				} else if (currentFloor.equals("1")) {
					for (Node node : mapNodeList1) {
						if ((node.getXCoord3D() >= circle.getX()) && (node.getXCoord3D() <= circle.getX()+nodeIconWidth)) {
							if ((node.getYCoord3D() >= circle.getY()) && (node.getYCoord3D() <= circle.getY()+nodeIconHeight)) {
								System.out.println("Click recognized");

								// Set Directory to Identify Start Location
								comBuildingStart.getSelectionModel().select(node.getBuilding());
								comFloorStart.getSelectionModel().select(node.getFloor());
								comTypeStart.getSelectionModel().select(convertTypeReverse(node.getNodeType()));
								//comLocationStart.getSelectionModel().select(node.getLongName());

								// Set Start Text Field to Start Location
								txtLocationStart.setText(node.getLongName());
								//showStartAndEnd();
								break;
							}
						}
					}
					System.out.println("3D Node not found for start");
				} else if (currentFloor.equals("2")) {
					for (Node node : mapNodeList2) {
						if ((node.getXCoord3D() >= circle.getX()) && (node.getXCoord3D() <= circle.getX()+nodeIconWidth)) {
							if ((node.getYCoord3D() >= circle.getY()) && (node.getYCoord3D() <= circle.getY()+nodeIconHeight)) {
								System.out.println("Click recognized");

								// Set Directory to Identify Start Location
								comBuildingStart.getSelectionModel().select(node.getBuilding());
								comFloorStart.getSelectionModel().select(node.getFloor());
								comTypeStart.getSelectionModel().select(convertTypeReverse(node.getNodeType()));
								//comLocationStart.getSelectionModel().select(node.getLongName());

								// Set Start Text Field to Start Location
								txtLocationStart.setText(node.getLongName());
								//showStartAndEnd();
								break;
							}
						}
					}
					System.out.println("3D Node not found for start");
				} else if (currentFloor.equals("3")) {
					for (Node node : mapNodeList3) {
						if ((node.getXCoord3D() >= circle.getX()) && (node.getXCoord3D() <= circle.getX()+nodeIconWidth)) {
							if ((node.getYCoord3D() >= circle.getY()) && (node.getYCoord3D() <= circle.getY()+nodeIconHeight)) {
								System.out.println("Click recognized");

								// Set Directory to Identify Start Location
								comBuildingStart.getSelectionModel().select(node.getBuilding());
								comFloorStart.getSelectionModel().select(node.getFloor());
								comTypeStart.getSelectionModel().select(convertTypeReverse(node.getNodeType()));
								//comLocationStart.getSelectionModel().select(node.getLongName());

								// Set Start Text Field to Start Location
								txtLocationStart.setText(node.getLongName());
								//showStartAndEnd();
								break;
							}
						}
					}
					System.out.println("3D Node not found for start");
				}
			}
		} else{ // Update Location End Text Field
			if(!currentDimension.equals("3-D")) { // 2D End
				if (currentFloor.equals("L2")) {
					for (Node node : mapNodeListL2) {
						if ((node.getXCoord() >= circle.getX()) && (node.getXCoord() <= circle.getX()+nodeIconWidth)) {
							if ((node.getYCoord() >= circle.getY()) && (node.getYCoord() <= circle.getY()+nodeIconHeight)) {
								System.out.println("Click recognized");

								// Set Directory to Identify End Location
								comBuildingEnd.getSelectionModel().select(node.getBuilding());
								comFloorEnd.getSelectionModel().select(node.getFloor());
								comTypeEnd.getSelectionModel().select(convertTypeReverse(node.getNodeType()));

								// Set End Text Field to End Location
								txtLocationEnd.setText(node.getLongName());
								//showStartAndEnd();
								break;
							}
						}
					}
					System.out.println("2D Node not found for end");
				} else if (currentFloor.equals("L1")) {
					for (Node node : mapNodeListL1) {
						if ((node.getXCoord() >= circle.getX()) && (node.getXCoord() <= circle.getX()+nodeIconWidth)) {
							if ((node.getYCoord() >= circle.getY()) && (node.getYCoord() <= circle.getY()+nodeIconHeight)) {
								System.out.println("Click recognized");

								// Set Directory to Identify End Location
								comBuildingEnd.getSelectionModel().select(node.getBuilding());
								comFloorEnd.getSelectionModel().select(node.getFloor());
								comTypeEnd.getSelectionModel().select(convertTypeReverse(node.getNodeType()));

								// Set End Text Field to End Location
								txtLocationEnd.setText(node.getLongName());
								//showStartAndEnd();
								break;
							}
						}
					}
					System.out.println("2D Node not found for end");
				} else if (currentFloor.equals("1")) {
					for (Node node : mapNodeList1) {
						if ((node.getXCoord() >= circle.getX()) && (node.getXCoord() <= circle.getX()+nodeIconWidth)) {
							if ((node.getYCoord() >= circle.getY()) && (node.getYCoord() <= circle.getY()+nodeIconHeight)) {
								System.out.println("Click recognized");

								// Set Directory to Identify End Location
								comBuildingEnd.getSelectionModel().select(node.getBuilding());
								comFloorEnd.getSelectionModel().select(node.getFloor());
								comTypeEnd.getSelectionModel().select(convertTypeReverse(node.getNodeType()));

								// Set End Text Field to End Location
								txtLocationEnd.setText(node.getLongName());
								//showStartAndEnd();
								break;
							}
						}
					}
					System.out.println("2D Node not found for end");
				} else if (currentFloor.equals("2")) {
					for (Node node : mapNodeList2) {
						if ((node.getXCoord() >= circle.getX()) && (node.getXCoord() <= circle.getX()+nodeIconWidth)) {
							if ((node.getYCoord() >= circle.getY()) && (node.getYCoord() <= circle.getY()+nodeIconHeight)) {
								System.out.println("Click recognized");

								// Set Directory to Identify End Location
								comBuildingEnd.getSelectionModel().select(node.getBuilding());
								comFloorEnd.getSelectionModel().select(node.getFloor());
								comTypeEnd.getSelectionModel().select(convertTypeReverse(node.getNodeType()));

								// Set End Text Field to End Location
								txtLocationEnd.setText(node.getLongName());
								//showStartAndEnd();
								break;
							}
						}
					}
					System.out.println("2D Node not found for end");
				} else if (currentFloor.equals("3")) {
					for (Node node : mapNodeList3) {
						if ((node.getXCoord() >= circle.getX()) && (node.getXCoord() <= circle.getX()+nodeIconWidth)) {
							if ((node.getYCoord() >= circle.getY()) && (node.getYCoord() <= circle.getY()+nodeIconHeight)) {
								System.out.println("Click recognized");

								// Set Directory to Identify End Location
								comBuildingEnd.getSelectionModel().select(node.getBuilding());
								comFloorEnd.getSelectionModel().select(node.getFloor());
								comTypeEnd.getSelectionModel().select(convertTypeReverse(node.getNodeType()));

								// Set End Text Field to End Location
								txtLocationEnd.setText(node.getLongName());
								//showStartAndEnd();
								break;
							}
						}
					}
					System.out.println("2D Node not found for end");
				}
			} else { // 3D End
				if (currentFloor.equals("L2")) {
					for (Node node : mapNodeListL2) {
						if ((node.getXCoord3D() >= circle.getX()) && (node.getXCoord3D() <= circle.getX()+nodeIconWidth)) {
							if ((node.getYCoord3D() >= circle.getY()) && (node.getYCoord3D() <= circle.getY()+nodeIconHeight)) {
								System.out.println("Click recognized");

								// Set Directory to Identify End Location
								comBuildingEnd.getSelectionModel().select(node.getBuilding());
								comFloorEnd.getSelectionModel().select(node.getFloor());
								comTypeEnd.getSelectionModel().select(convertTypeReverse(node.getNodeType()));

								// Set End Text Field to End Location
								txtLocationEnd.setText(node.getLongName());
								//showStartAndEnd();
								break;
							}
						}
					}
					System.out.println("3D Node not found for end");
				} else if (currentFloor.equals("L1")) {
					for (Node node : mapNodeListL1) {
						if ((node.getXCoord3D() >= circle.getX()) && (node.getXCoord3D() <= circle.getX()+nodeIconWidth)) {
							if ((node.getYCoord3D() >= circle.getY()) && (node.getYCoord3D() <= circle.getY()+nodeIconHeight)) {
								System.out.println("Click recognized");

								// Set Directory to Identify End Location
								comBuildingEnd.getSelectionModel().select(node.getBuilding());
								comFloorEnd.getSelectionModel().select(node.getFloor());
								comTypeEnd.getSelectionModel().select(convertTypeReverse(node.getNodeType()));

								// Set End Text Field to End Location
								txtLocationEnd.setText(node.getLongName());
								//showStartAndEnd();
								break;
							}
						}
					}
					System.out.println("3D Node not found for end");
				} else if (currentFloor.equals("1")) {
					for (Node node : mapNodeList1) {
						if ((node.getXCoord3D() >= circle.getX()) && (node.getXCoord3D() <= circle.getX()+nodeIconWidth)) {
							if ((node.getYCoord3D() >= circle.getY()) && (node.getYCoord3D() <= circle.getY()+nodeIconHeight)) {
								System.out.println("Click recognized");

								// Set Directory to Identify End Location
								comBuildingEnd.getSelectionModel().select(node.getBuilding());
								comFloorEnd.getSelectionModel().select(node.getFloor());
								comTypeEnd.getSelectionModel().select(convertTypeReverse(node.getNodeType()));

								// Set End Text Field to End Location
								txtLocationEnd.setText(node.getLongName());
								//showStartAndEnd();
								break;
							}
						}
					}
					System.out.println("3D Node not found for end");
				} else if (currentFloor.equals("2")) {
					for (Node node : mapNodeList2) {
						if ((node.getXCoord3D() >= circle.getX()) && (node.getXCoord3D() <= circle.getX()+nodeIconWidth)) {
							if ((node.getYCoord3D() >= circle.getY()) && (node.getYCoord3D() <= circle.getY()+nodeIconHeight)) {
								System.out.println("Click recognized");

								// Set Directory to Identify End Location
								comBuildingEnd.getSelectionModel().select(node.getBuilding());
								comFloorEnd.getSelectionModel().select(node.getFloor());
								comTypeEnd.getSelectionModel().select(convertTypeReverse(node.getNodeType()));

								// Set End Text Field to End Location
								txtLocationEnd.setText(node.getLongName());
								//showStartAndEnd();
								break;
							}
						}
					}
					System.out.println("3D Node not found for end");
				} else if (currentFloor.equals("3")) {
					for (Node node : mapNodeList3) {
						if ((node.getXCoord3D() >= circle.getX()) && (node.getXCoord3D() <= circle.getX()+nodeIconWidth)) {
							if ((node.getYCoord3D() >= circle.getY()) && (node.getYCoord3D() <= circle.getY()+nodeIconHeight)) {
								System.out.println("Click recognized");

								// Set Directory to Identify End Location
								comBuildingEnd.getSelectionModel().select(node.getBuilding());
								comFloorEnd.getSelectionModel().select(node.getFloor());
								comTypeEnd.getSelectionModel().select(convertTypeReverse(node.getNodeType()));

								// Set End Text Field to End Location
								txtLocationEnd.setText(node.getLongName());
								//showStartAndEnd();
								break;
							}
						}
					}
					System.out.println("3D Node not found for end");
				}



			}
		}
	}

	public void setStartLocation(ActionEvent event) {
		System.out.println("You set a start location: " + txtLocationStart.getText());
	}

	public void setEndLocation(ActionEvent event) {
		System.out.println("You set a start location: " + txtLocationEnd.getText());

	}


	//-----------------------------------------------------------------------------------------------------------------
	//
	//                                           Directions
	//
	//-----------------------------------------------------------------------------------------------------------------
	@FXML
	Pane paneDirections;


	@FXML
	JFXButton btnOpenSend;


	@FXML
	Pane paneSend;

	@FXML
	JFXButton btnCloseSend;

	@FXML
	JFXTextField txtEmail;

	@FXML
	JFXTextField txtPhone;

	@FXML
	Button btnEmail;

	@FXML
	Button btnPhone;

	@FXML
	Label lblEmailMessage;

	@FXML
	Label lblPhoneMessage;

	@FXML
	JFXButton btnDirections;

	public void openSendPanel(ActionEvent event) {
		paneSend.setVisible(true);
	}

	public void restart(ActionEvent event) {
		// Clear path
		clearPath();
		pathList.clear();

		// Clear Fields
		comBuildingStart.getSelectionModel().clearSelection();

		// Disable Fields
		comFloorStart.setDisable(true);
		comFloorEnd.setDisable(true);
		comTypeStart.setDisable(true);
		comTypeEnd.setDisable(true);

		//tglMap.setSelected(false);
		//tglMap.setText("2-D");
		changeFloor("1");
		currentFloor = "1";

		paneMap.getChildren().remove(startCircle);
		//overMap.getChildren().remove(nameList.get(0)); // TODO wont remove the text why
		//overMap.getChildren().remove(nameList.get(1)); // TODO wont remove the text why
		destinationText.setVisible(false);
		destination.setVisible(false);
		//nameList.clear();
		startName.setText("");
		endName.setText("");
		startName.setVisible(false);
		endName.setVisible(false);

		arrow.setVisible(false);
		setKiosk();

	}

	public void closeSendPanel(ActionEvent event) {

		// Hide QR code
		paneSend.setVisible(false);

		// Disable Everything Else
		btnHelp.setDisable(false);
		btnQuickDirections.setDisable(false);
		btnQuickBathroom.setDisable(false);
		btnOpenSend.setDisable(false);
	}

	private String turnListToString() {
		String out = "";
		for (String CurrInstruction : directions) {
			out += CurrInstruction + '\n';
		}
		return out;
	}

	public void sendDirectionsViaEmail(ActionEvent event) {
		lblEmailMessage.setText("");
		SendEmail email = new SendEmail(txtEmail.getText(), "B&W Turn-By-Turn Directions", turnListToString());
		email.send();
		lblEmailMessage.setText("Email Sent");
		txtEmail.setText("");
	}

	public void sendDirectionsViaPhone(ActionEvent event) {
		lblPhoneMessage.setText("");
		SendTxt txt = new SendTxt();
		txt.send(txtPhone.getText(), turnListToString());
		lblPhoneMessage.setText("Text Message Sent");
		txtPhone.setText("");
	}

	public void openDirectionsPane(ActionEvent event) {
		if (paneDirections.isVisible()) {
			paneDirections.setVisible(false);
		} else if (paneDirections.isVisible() == false) {
			paneDirections.setVisible(true);
		}
	}

	//-----------------------------------------------------------------------------------------------------------------
	//
	//                                           Quick Directions
	//
	//-----------------------------------------------------------------------------------------------------------------
	@FXML
	JFXButton btnQuickDirections;

	@FXML
	JFXButton btnQuickBathroom;

	@FXML
	JFXButton btnQuickExit;

	@FXML
	JFXButton btnQuickElevator;

	@FXML
	ImageView imgNavigation;

	public void toggleQuickButtons(ActionEvent event) {

		if (btnQuickBathroom.isVisible() == true) {

			new ProxyImage(imgNavigation, "NearestIcon.png").displayIcon();

			btnQuickBathroom.setVisible(false);
			btnQuickElevator.setVisible(false);
			btnQuickExit.setVisible(false);

		} else if (btnQuickBathroom.isVisible() == false) {

			new ProxyImage(imgNavigation, "BackIcon.png").displayIcon();

			btnQuickBathroom.setVisible(true);
			btnQuickElevator.setVisible(true);
			btnQuickExit.setVisible(true);
		}
	}

    public void findQuickBathroom(ActionEvent event) {

        // Pathfind to nearest bathroom
        String startFloor = "1";
		clearPath();
		breadcrumbs.clear();
		setTheBreadyBoysBackToTheirGrayStateAsSoonAsPossibleSoThatItMakesSenseAgainPlease();
        clearPath();

        System.out.println(txtLocationStart.getText());
        System.out.println(txtLocationEnd.getText());

        String dimension;


        PathfinderUtil pathfinderUtil = new PathfinderUtil();

        //List<Node> nodeList = new ArrayList<>();
        //LinkedList<Node> pathList = new LinkedList<>();
        //nodeList = DataModelI.getInstance().retrieveNodes();
        Node startNode = KioskInfo.getMyLocation();
        Node bathroomNode = new Room("N1X3Y", 1, 3, "F1", "BUILD1", "REST", "Node 1, 3", "n1x3y", 1, 0, 0);

        startFloor = startNode.getFloor();
        endFloor = bathroomNode.getFloor();
        currentFloor = startNode.getFloor();
        // update end name

        try {
//				pathList = Singleton.getInstance().pathfindingContext.getPath(startNode, endNode, new AStarStrategyI());
            pathList = optionPicker.pathfindingContext.getPath(startNode, bathroomNode, new ClosestStrategyI());

        } catch (PathNotFoundException e) {
            e.printStackTrace();
        }
        txtLocationEnd.setText(pathList.get(pathList.size()-1).getLongName());


		String dirFloorL2 = "";
		String dirFloorL1 = "";
		String dirFloor1 = "";
		String dirFloor2 = "";
		String dirFloor3 = "";

		floorL2DirectionNodes.clear();
		floorL1DirectionNodes.clear();
		floor1DirectionNodes.clear();
		floor2DirectionNodes.clear();
		floor3DirectionNodes.clear();

		directionsL2.clear();
		directionsL1.clear();
		directions1.clear();
		directions2.clear();
		directions3.clear();

		boolean useL2 = false;
		boolean useL1 = false;
		boolean use1 = false;
		boolean use2 = false;
		boolean use3 = false;

			for (Node node: pathList) {
			String floorName = node.getFloor();
			if (floorName.equals("L2")) {
				floorL2DirectionNodes.add(node);
			}
			if (floorName.equals("L1")) {
				floorL1DirectionNodes.add(node);
			}
			if (floorName.equals("1")) {
				floor1DirectionNodes.add(node);
			}
			if (floorName.equals("2")) {
				floor2DirectionNodes.add(node);
			}
			if (floorName.equals("3")) {
				floor3DirectionNodes.add(node);
			}
		}

		directionsL2 = ((pathfinderUtil.angleToText(floorL2DirectionNodes)));
		directionsL1 = (pathfinderUtil.angleToText(floorL1DirectionNodes));
		directions1 = (pathfinderUtil.angleToText(floor1DirectionNodes));
		directions2 = (pathfinderUtil.angleToText(floor2DirectionNodes));
		directions3 = (pathfinderUtil.angleToText(floor3DirectionNodes));

		if(directionsL2.size() > 1){
			useL2 = true;
		}
		if(directionsL1.size() > 1){
			useL1 = true;
		}
		if(directions1.size() > 1){
			use1 = true;
		}
		if(directions2.size() > 1){
			use2 = true;
		}
		if(directions3.size() > 1){
			use3 = true;
		}

		for (int i = 0; i < directionsL2.size(); i++){
			dirFloorL2 += directionsL2.get((i)) + "\n";
		}
		for (int i = 0; i < directionsL1.size(); i++){
			dirFloorL1 += directionsL1.get((i)) + "\n";
		}
		for (int i = 0; i < directions1.size(); i++){
			dirFloor1 += directions1.get((i)) + "\n";
		}
		for (int i = 0; i < directions2.size(); i++){
			dirFloor2 += directions2.get((i)) + "\n";
		}
		for (int i = 0; i < directions3.size(); i++) {
			dirFloor3 += directions3.get((i)) + "\n";
		}
		TreeItem<String> floorDirectionL2 = new TreeItem<>(dirFloorL2);
		TreeItem<String> floorDirectionL1 = new TreeItem<>(dirFloorL1);
		TreeItem<String> floorDirection1 = new TreeItem<>(dirFloor1);
		TreeItem<String> floorDirection2 = new TreeItem<>(dirFloor2);
		TreeItem<String> floorDirection3 = new TreeItem<>(dirFloor3);

		floorL2.getChildren().setAll(floorDirectionL2);
		floorL1.getChildren().setAll(floorDirectionL1);
		floor1.getChildren().setAll(floorDirection1);
		floor2.getChildren().setAll(floorDirection2);
		floor3.getChildren().setAll(floorDirection3);

		// calcDistance function now converts to feet
		double dist = CalcDistance.calcDistance(pathList) * OptionSingleton.getOptionPicker().feetPerPixel;
		etaDistance = String.format("TOTAL DISTANCE: %.1f ft      ETA: %.1f s", dist, dist / OptionSingleton.getOptionPicker().walkSpeedFt);
		TreeItem<String> root = new TreeItem<>(etaDistance);

		root.getChildren().setAll(floor3, floor2,  floor1, floorL1, floorL2);
		colDirections.setCellValueFactory(
				(TreeTableColumn.CellDataFeatures<String, String> param) ->  new SimpleStringProperty(param.getValue().getValue()));

		tblDirections.setRoot(root);

		tblDirections.getRoot().setExpanded(true);

		if( useL2 == true){tblDirections.getTreeItem(5).setExpanded(true);}
		if( useL1 == true){tblDirections.getTreeItem(4).setExpanded(true);}
		if( use1 == true){tblDirections.getTreeItem(3).setExpanded(true);}
		if( use2 == true){tblDirections.getTreeItem(2).setExpanded(true);}
		if( use3 == true){tblDirections.getTreeItem(1).setExpanded(true);}

		directions = FXCollections.observableArrayList(pathfinderUtil.angleToText((LinkedList) pathList));

		paneDirections.setVisible(true);

        listForQR = (LinkedList) pathList;

        // Draw path code
        if (currentDimension.equals("3-D")) {
            // use 3-D
            dimension = "3-D";
            printNodePath(pathList, startFloor, dimension);
            changeFloor(startFloor);
        } else {
            // use 2-D
            dimension = "2-D";
            printNodePath(pathList, startFloor, dimension);
            changeFloor(startFloor);
        }

		// Clear old fields
		// Show directions interface and hide pathfinding interface
		//panePathfinding.setVisible(false);
		//paneDirections.setVisible(true);

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

		// Directions Update
		breadBoy();
		animatePath();

	}

    public void findQuickExit(ActionEvent event) {
        // Pathfind to nearest exit
        clearPath();
		clearPath();
		breadcrumbs.clear();
		setTheBreadyBoysBackToTheirGrayStateAsSoonAsPossibleSoThatItMakesSenseAgainPlease();

        System.out.println(txtLocationStart.getText());
        System.out.println(txtLocationEnd.getText());



        PathfinderUtil pathfinderUtil = new PathfinderUtil();

        //List<Node> nodeList = new ArrayList<>();
        //LinkedList<Node> pathList = new LinkedList<>();
        //nodeList = DataModelI.getInstance().retrieveNodes();
        Node startNode = KioskInfo.getMyLocation();
        Node exitNode = new Room("N1X3Y", 1, 3, "F1", "BUILD1", "EXIT", "Node 1, 3", "n1x3y", 1, 0, 0);

        startFloor = startNode.getFloor();
        endFloor = exitNode.getFloor();
        currentFloor = startNode.getFloor();
        // update end name
        txtLocationEnd.setText(exitNode.getLongName());



        try {
//				pathList = Singleton.getInstance().pathfindingContext.getPath(startNode, endNode, new AStarStrategyI());
            pathList = optionPicker.pathfindingContext.getPath(startNode, exitNode, new ClosestStrategyI());

        } catch (PathNotFoundException e) {
            e.printStackTrace();
        }

		String dirFloorL2 = "";
		String dirFloorL1 = "";
		String dirFloor1 = "";
		String dirFloor2 = "";
		String dirFloor3 = "";

		floorL2DirectionNodes.clear();
		floorL1DirectionNodes.clear();
		floor1DirectionNodes.clear();
		floor2DirectionNodes.clear();
		floor3DirectionNodes.clear();

		directionsL2.clear();
		directionsL1.clear();
		directions1.clear();
		directions2.clear();
		directions3.clear();

		boolean useL2 = false;
		boolean useL1 = false;
		boolean use1 = false;
		boolean use2 = false;
		boolean use3 = false;


		for (Node node: pathList) {
			String floorName = node.getFloor();
			if (floorName.equals("L2")) {
				floorL2DirectionNodes.add(node);
			}
			if (floorName.equals("L1")) {
				floorL1DirectionNodes.add(node);
			}
			if (floorName.equals("1")) {
				floor1DirectionNodes.add(node);
			}
			if (floorName.equals("2")) {
				floor2DirectionNodes.add(node);
			}
			if (floorName.equals("3")) {
				floor3DirectionNodes.add(node);
			}
		}

		directionsL2 = ((pathfinderUtil.angleToText(floorL2DirectionNodes)));
		directionsL1 = (pathfinderUtil.angleToText(floorL1DirectionNodes));
		directions1 = (pathfinderUtil.angleToText(floor1DirectionNodes));
		directions2 = (pathfinderUtil.angleToText(floor2DirectionNodes));
		directions3 = (pathfinderUtil.angleToText(floor3DirectionNodes));

		if(directionsL2.size() > 1){
			useL2 = true;
		}
		if(directionsL1.size() > 1){
			useL1 = true;
		}
		if(directions1.size() > 1){
			use1 = true;
		}
		if(directions2.size() > 1){
			use2 = true;
		}
		if(directions3.size() > 1){
			use3 = true;
		}

		for (int i = 0; i < directionsL2.size(); i++){
			dirFloorL2 += directionsL2.get((i)) + "\n";
		}
		for (int i = 0; i < directionsL1.size(); i++){
			dirFloorL1 += directionsL1.get((i)) + "\n";
		}
		for (int i = 0; i < directions1.size(); i++){
			dirFloor1 += directions1.get((i)) + "\n";
		}
		for (int i = 0; i < directions2.size(); i++){
			dirFloor2 += directions2.get((i)) + "\n";
		}
		for (int i = 0; i < directions3.size(); i++) {
			dirFloor3 += directions3.get((i)) + "\n";
		}
		TreeItem<String> floorDirectionL2 = new TreeItem<>(dirFloorL2);
		TreeItem<String> floorDirectionL1 = new TreeItem<>(dirFloorL1);
		TreeItem<String> floorDirection1 = new TreeItem<>(dirFloor1);
		TreeItem<String> floorDirection2 = new TreeItem<>(dirFloor2);
		TreeItem<String> floorDirection3 = new TreeItem<>(dirFloor3);

		floorL2.getChildren().setAll(floorDirectionL2);
		floorL1.getChildren().setAll(floorDirectionL1);
		floor1.getChildren().setAll(floorDirection1);
		floor2.getChildren().setAll(floorDirection2);
		floor3.getChildren().setAll(floorDirection3);

		// calcDistance function now converts to feet
		double dist = CalcDistance.calcDistance(pathList) * OptionSingleton.getOptionPicker().feetPerPixel;
		etaDistance = String.format("TOTAL DISTANCE: %.1f ft      ETA: %.1f s", dist, dist / OptionSingleton.getOptionPicker().walkSpeedFt);
		TreeItem<String> root = new TreeItem<>(etaDistance);

		root.getChildren().setAll(floor3, floor2,  floor1, floorL1, floorL2);
		colDirections.setCellValueFactory(
				(TreeTableColumn.CellDataFeatures<String, String> param) ->  new SimpleStringProperty(param.getValue().getValue()));

		tblDirections.setRoot(root);
		directions = FXCollections.observableArrayList(pathfinderUtil.angleToText((LinkedList) pathList));
		tblDirections.getRoot().setExpanded(true);

		if( useL2 == true){tblDirections.getTreeItem(5).setExpanded(true);}
		if( useL1 == true){tblDirections.getTreeItem(4).setExpanded(true);}
		if( use1 == true){tblDirections.getTreeItem(3).setExpanded(true);}
		if( use2 == true){tblDirections.getTreeItem(2).setExpanded(true);}
		if( use3 == true){tblDirections.getTreeItem(1).setExpanded(true);}

		paneDirections.setVisible(true);

       String dimension;
        listForQR = (LinkedList) pathList;

        // Draw path code
        if (currentDimension.equals("3-D")) {
            // use 3-D
            dimension = "3-D";
            printNodePath(pathList, startFloor, dimension);
            changeFloor(startFloor);
        } else {
            // use 2-D
            dimension = "2-D";
            printNodePath(pathList, startFloor, dimension);
            changeFloor(startFloor);
        }

		// Clear old fields
		// Show directions interface and hide pathfinding interface
//		panePathfinding.setVisible(false);
//		paneDirections.setVisible(true);

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

		// Directions Update
		breadBoy();
		animatePath();

	}

    public void findQuickElevator(ActionEvent event) {
        // Pathfind to nearest bathroom
		clearPath();
		breadcrumbs.clear();
		setTheBreadyBoysBackToTheirGrayStateAsSoonAsPossibleSoThatItMakesSenseAgainPlease();
        String startFloor = "1";

        clearPath();

        System.out.println(txtLocationStart.getText());
        System.out.println(txtLocationEnd.getText());

        String dimension;

        PathfinderUtil pathfinderUtil = new PathfinderUtil();

        //List<Node> nodeList = new ArrayList<>();
        //LinkedList<Node> pathList = new LinkedList<>();
        //nodeList = DataModelI.getInstance().retrieveNodes();
        Node startNode = KioskInfo.getMyLocation();
        Node elevatorNode = new Room("N1X3Y", 1, 3, "F1", "BUILD1", "ELEV", "Node 1, 3", "n1x3y", 1, 0, 0);

        startFloor = startNode.getFloor();
        endFloor = elevatorNode.getFloor();
        currentFloor = startNode.getFloor();
        // update end name
        txtLocationEnd.setText(elevatorNode.getLongName());


        try {
//				pathList = Singleton.getInstance().pathfindingContext.getPath(startNode, endNode, new AStarStrategyI());
            pathList = optionPicker.pathfindingContext.getPath(startNode, elevatorNode, new ClosestStrategyI());

        } catch (PathNotFoundException e) {
            e.printStackTrace();
        }

        txtLocationEnd.setText(pathList.get(pathList.size()-1).getLongName());

        String dirFloorL2 = "";
        String dirFloorL1 = "";
        String dirFloor1 = "";
        String dirFloor2 = "";
        String dirFloor3 = "";

        floorL2DirectionNodes.clear();
        floorL1DirectionNodes.clear();
        floor1DirectionNodes.clear();
        floor2DirectionNodes.clear();
        floor3DirectionNodes.clear();

        directionsL2.clear();
        directionsL1.clear();
        directions1.clear();
        directions2.clear();
        directions3.clear();

		boolean useL2 = false;
		boolean useL1 = false;
		boolean use1 = false;
		boolean use2 = false;
		boolean use3 = false;

        for (Node node: pathList) {
            String floorName = node.getFloor();
            if (floorName.equals("L2")) {
                floorL2DirectionNodes.add(node);
            }
            if (floorName.equals("L1")) {
                floorL1DirectionNodes.add(node);
            }
            if (floorName.equals("1")) {
                floor1DirectionNodes.add(node);
            }
            if (floorName.equals("2")) {
                floor2DirectionNodes.add(node);
            }
            if (floorName.equals("3")) {
                floor3DirectionNodes.add(node);
            }
        }

        directionsL2 = ((pathfinderUtil.angleToText(floorL2DirectionNodes)));
        directionsL1 = (pathfinderUtil.angleToText(floorL1DirectionNodes));
        directions1 = (pathfinderUtil.angleToText(floor1DirectionNodes));
        directions2 = (pathfinderUtil.angleToText(floor2DirectionNodes));
        directions3 = (pathfinderUtil.angleToText(floor3DirectionNodes));

        if(directionsL2.size() > 1){
        	useL2 = true;
		}
		if(directionsL1.size() > 1){
			useL1 = true;
		}
		if(directions1.size() > 1){
			use1 = true;
		}
		if(directions2.size() > 1){
			use2 = true;
		}
		if(directions3.size() > 1){
			use3 = true;
		}

        for (int i = 0; i < directionsL2.size(); i++){
            dirFloorL2 += directionsL2.get((i)) + "\n";
        }
        for (int i = 0; i < directionsL1.size(); i++){
            dirFloorL1 += directionsL1.get((i)) + "\n";
        }
        for (int i = 0; i < directions1.size(); i++){
            dirFloor1 += directions1.get((i)) + "\n";
        }
        for (int i = 0; i < directions2.size(); i++){
            dirFloor2 += directions2.get((i)) + "\n";
        }
        for (int i = 0; i < directions3.size(); i++) {
            dirFloor3 += directions3.get((i)) + "\n";
        }
        TreeItem<String> floorDirectionL2 = new TreeItem<>(dirFloorL2);
        TreeItem<String> floorDirectionL1 = new TreeItem<>(dirFloorL1);
        TreeItem<String> floorDirection1 = new TreeItem<>(dirFloor1);
        TreeItem<String> floorDirection2 = new TreeItem<>(dirFloor2);
        TreeItem<String> floorDirection3 = new TreeItem<>(dirFloor3);

        floorL2.getChildren().setAll(floorDirectionL2);
        floorL1.getChildren().setAll(floorDirectionL1);
        floor1.getChildren().setAll(floorDirection1);
        floor2.getChildren().setAll(floorDirection2);
        floor3.getChildren().setAll(floorDirection3);

		// calcDistance function now converts to feet
		double dist = CalcDistance.calcDistance(pathList) * OptionSingleton.getOptionPicker().feetPerPixel;
		etaDistance = String.format("TOTAL DISTANCE: %.1f ft      ETA: %.1f s", dist, dist / OptionSingleton.getOptionPicker().walkSpeedFt);
		TreeItem<String> root = new TreeItem<>(etaDistance);

        root.getChildren().setAll(floor3, floor2,  floor1, floorL1, floorL2);
        colDirections.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<String, String> param) ->  new SimpleStringProperty(param.getValue().getValue()));

        tblDirections.setRoot(root);

        tblDirections.getRoot().setExpanded(true);

        if( useL2 == true){tblDirections.getTreeItem(5).setExpanded(true);}
		if( useL1 == true){tblDirections.getTreeItem(4).setExpanded(true);}
		if( use1 == true){tblDirections.getTreeItem(3).setExpanded(true);}
		if( use2 == true){tblDirections.getTreeItem(2).setExpanded(true);}
		if( use3 == true){tblDirections.getTreeItem(1).setExpanded(true);}
		directions = FXCollections.observableArrayList(pathfinderUtil.angleToText((LinkedList) pathList));
        paneDirections.setVisible(true);
        //directions = FXCollections.observableArrayList(pathfinderUtil.angleToText((LinkedList) pathList));
        // calcDistance function now converts to feet

        listForQR = (LinkedList) pathList;

        // Draw path code
        if (currentDimension.equals("3-D")) {
            // use 3-D
            dimension = "3-D";
            printNodePath(pathList, startFloor, dimension);
            changeFloor(startFloor);
        } else {
            // use 2-D
            dimension = "2-D";
            printNodePath(pathList, startFloor, dimension);
            changeFloor(startFloor);
        }

		// Clear old fields
		// Show directions interface and hide pathfinding interface
//		panePathfinding.setVisible(false);
//		paneDirections.setVisible(true);

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

		// Directions Update
		breadBoy();
		animatePath();

	}

	//-----------------------------------------------------------------------------------------------------------------
	//
	//                                           Help
	//
	//-----------------------------------------------------------------------------------------------------------------
	@FXML
	JFXButton btnHelp;

	@FXML
	StackPane paneHelp;

	public void openHelpPanel(ActionEvent event) {
		paneHelp.setVisible(true);
	}

	public void closeHelp(MouseEvent mouseEvent) {
		paneHelp.setVisible(false);
	}


	//-----------------------------------------------------------------------------------------------------------------
	//
	//                                           Click on map
	//
	//-----------------------------------------------------------------------------------------------------------------

	public void getXandY(MouseEvent event) throws Exception {
		//see which pane is visible and set the corresponding x and y coordinates
		if (paneMap.isVisible() == true) {
			System.out.println("X: " + String.format("%1.3f", event.getX()));
			System.out.println("Y: " + String.format("%1.3f", event.getY()));
		}
	}

	//-----------------------------------------------------------------------------------------------------------------
	//
	//                                           Map loaders
	//
	//-----------------------------------------------------------------------------------------------------------------

	private void floor2DMapLoader(String floor) {

		if (floor.equals("FLOOR: L2") || floor.equals("L2")) {

			new ProxyImage(mapImg, "L2_NoIcons.png").display();
			clearPath();
			printNodePath(pathList, "L2", "2-D");

		} else if (floor.equals("FLOOR: L1") || floor.equals("L1")) {

			new ProxyImage(mapImg, "L1_NoIcons.png").display();
			clearPath();
			printNodePath(pathList, "L1", "2-D");

		} else if (floor.equals("FLOOR: 1") || floor.equals("1")) {

			new ProxyImage(mapImg, "1_NoIcons.png").display();
			clearPath();
			printNodePath(pathList, "1", "2-D");

		} else if (floor.equals("FLOOR: 2") || floor.equals("2")) {

			new ProxyImage(mapImg, "2_NoIcons.png").display();
			clearPath();
			printNodePath(pathList, "2", "2-D");

		} else if (floor.equals("FLOOR: 3") || floor.equals("3")) {

			new ProxyImage(mapImg, "3_NoIcons.png").display();
			clearPath();
			printNodePath(pathList, "3", "2-D");


		}
		if (breadcrumbs.contains(floor)) {
			arrow.setVisible(true);
			animatePath();
		} else {
			arrow.setVisible(false);
		}
		currentFloor = floor;
	}

	private void floor3DMapLoader(String floor) {

		if (floor.equals("FLOOR: L2") || floor.equals("L2")) {
			new ProxyImage(mapImg, "L2-NO-ICONS.png").display();

			clearPath();
			printNodePath(pathList, "L2", "3-D");

		} else if (floor.equals("FLOOR: L1") || floor.equals("L1")) {
			new ProxyImage(mapImg, "L1-NO-ICONS.png").display();

			clearPath();
			printNodePath(pathList, "L1", "3-D");

		} else if (floor.equals("FLOOR: 1") || floor.equals("1")) {
			new ProxyImage(mapImg, "1-NO-ICONS.png").display();

			clearPath();
			printNodePath(pathList, "1", "3-D");

		} else if (floor.equals("FLOOR: 2") || floor.equals("2")) {
			new ProxyImage(mapImg, "2-NO-ICONS.png").display();

			clearPath();
			printNodePath(pathList, "2", "3-D");

		} else if (floor.equals("FLOOR: 3") || floor.equals("3")) {
			new ProxyImage(mapImg, "3-NO-ICONS.png").display();

			clearPath();
			printNodePath(pathList, "3", "3-D");

		}
		if (breadcrumbs.contains(floor)) {
			arrow.setVisible(true);
			animatePath();
		} else {
			arrow.setVisible(false);
		}
		currentFloor = floor;
	}

	//-----------------------------------------------------------------------------------------------------------------
	//
	//                                           Drawing on map
	//
	//-----------------------------------------------------------------------------------------------------------------

	@FXML
	ImageView destination;

	@FXML
	javafx.scene.text.Text startName;

	@FXML
	Pane pointMap;

	@FXML
	javafx.scene.text.Text endName;

	@FXML
	javafx.scene.text.Text destinationText;

	@FXML
	ImageView arrow;

	List<ImageView> iconList = new ArrayList<>();

	boolean pathRunning; // used to check whether the scale animation for destination should be created and played or not

	/**
	 * Maps the value from the old boundary to the new boundary
	 *
	 * @param value  the value to be transferred
	 * @param oldMin the old min value (x or y)
	 * @param oldMax the old max value (x or y)
	 * @param newMin the new min value (x or y)
	 * @param newMax the new max calue (x or y)
	 * @return the value mapped from the old boundary to the new boundary
	 */
	static double map(double value, double oldMin, double oldMax, double newMin, double newMax) {
		return (((value - oldMin) * (newMax - newMin)) / (oldMax - oldMin)) + newMin;
	}

	/**
	 * @param startNode node to draw from
	 * @param endNode   node to draw to
	 * @param moveTo    start point of line to be drawn
	 * @param lineTo    end point of line to be drawn
	 */
	private void addPath(Path currPath, String dimension, Node startNode, Node endNode, MoveTo moveTo, LineTo lineTo) {
		if (startNode != null && endNode != null) {
			System.out.println("Found a path!");

			if (dimension.equals("2-D")) { // if 2D
				moveTo.setX(startNode.getXCoord());
				moveTo.setY(startNode.getYCoord());
				// Draw to end point
				// Map the x and y coords onto our map
				lineTo.setX(endNode.getXCoord());
				lineTo.setY(endNode.getYCoord());
			} else if (dimension.equals("3-D"))// if 3D
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
			currPath.getElements().add(moveTo);
			currPath.getElements().add(lineTo);
		}
	}

	private void setText(javafx.scene.text.Text text, int finishX, int finishY, int subX, int subY, Font font) {
		text.setTranslateX(finishX - subX);
		text.setTranslateY(finishY - subY);
		text.setFill(Color.WHITE);
		text.setFont(font);
		text.setStroke(Color.BLACK);
		text.setStrokeType(StrokeType.CENTERED);
		text.setStrokeWidth(2);
	}

	/**
	 * Prints the given path on the map
	 *
	 * @param path the nodes to draw a path between
	 */
	private void printNodePath(List<Node> path, String floor, String dimension) {
		System.out.println("Attempting to print path between nodes...");
		clearPath();
		int i = 0;
		if (!path.isEmpty()) {
			while (i < path.size()) {
				// Give starting point
				MoveTo moveTo = new MoveTo();
				LineTo lineTo = new LineTo();
				Node startNode = path.get(i);
				Node endNode;

				if (i + 1 < path.size()) {
					endNode = path.get(i + 1);
					if (path.get(i).getFloor().equals(floor)) {
						// add the path
						addPath(currPath, dimension, startNode, endNode, moveTo, lineTo);
					} else {
						// add the path
						addPath(diffPath, dimension, startNode, endNode, moveTo, lineTo);
					}
				}
				i++;
				System.out.println("Path added...");
			}

			int finishX = 0;
			int finishY = 0;
			int startX = 0;
			int startY = 0;

			Node endNode = pathList.get(pathList.size() - 1);
			Node startNode = pathList.get(0);
			snap(startNode, endNode);

			//javafx.scene.text.Text startName = new javafx.scene.text.Text(startNode.getLongName());
			//javafx.scene.text.Text endName = new javafx.scene.text.Text(endNode.getLongName());
			if (currentFloor == endFloor) {
				destination.setVisible(true);
			}
			startName.setVisible(true);
			endName.setVisible(true);
			startName.setText(startNode.getShortName());
			endName.setText(endNode.getShortName());
			endName.setOpacity(0);
			startName.setOpacity(0);
			destinationText.setText("FL " + endFloor);
			Font font = Font.font("Verdana", FontWeight.BOLD, 40);

			if (dimension.equals("2-D")) {
				finishX = endNode.getXCoord();
				finishY = endNode.getYCoord();
				startX = startNode.getXCoord();
				startY = startNode.getYCoord();
				destination.setTranslateX(finishX - 29);
				destination.setTranslateY(finishY - 52);
				setText(destinationText, finishX, finishY, 35, 60, font);
				setText(startName, startX, startY, -15, 0, font);
				setText(endName, finishX, finishY, -15, 0, font);
				//endName.setRotate(-overMap.getRotate());
			} else if (dimension.equals("3-D")) {
				finishX = endNode.getXCoord3D();
				finishY = endNode.getYCoord3D();
				startX = startNode.getXCoord3D();
				startY = startNode.getYCoord3D();
				destination.setX(finishX);
				destination.setY(finishY);
				setText(destinationText, finishX, finishY, 35, 60, font);
				setText(startName, startX, startY, -15, 0, font);
				//startName.setRotate(-overMap.getRotate());
				setText(endName, finishX, finishY, -15, 0, font);
				//endName.setRotate(-overMap.getRotate());
			} else {
				System.out.println("Invalid dimension");
			}

			// Draw Start Circle
			startCircle.setRadius(8);
			startCircle.setFill(Color.NAVY);
			startCircle.setVisible(true);
			startCircle.setCenterX(startX);
			startCircle.setCenterY(startY);
			startCircle.setStroke(Color.BLACK);
			startCircle.setStrokeWidth(3);

			// Set on mouse clicked to switch between floors
			startFloor = startNode.getFloor();
			startCircle.setOnMouseClicked(this::startCircleClicked);
			startCircle.setOnMouseEntered(this::printStartName);
			startCircle.setOnMouseExited(this::removeStartName);

			if (!startFloor.equals(floor)) {
				startCircle.setFill(Color.GRAY);
			}

			endFloor = endNode.getFloor();

			// adds circles to map
			paneMap.getChildren().remove(startCircle);
			paneMap.getChildren().add(startCircle);
			//overMap.getChildren().add(startName);
			//overMap.getChildren().add(endName);
		}
	}

	public void drawPath(ActionEvent event) {
		try {
			clearPath();
			breadcrumbs.clear();
			setTheBreadyBoysBackToTheirGrayStateAsSoonAsPossibleSoThatItMakesSenseAgainPlease();

			System.out.println(txtLocationStart.getText());
			System.out.println(txtLocationEnd.getText());

			String startName = txtLocationStart.getText();
			String endName = txtLocationEnd.getText();

			String dimension;

			if (startName.equals("") || endName.equals("")) {
				System.out.println("Pick a start and end location!");
			} else {

				PathfinderUtil pathfinderUtil = new PathfinderUtil();

				//List<Node> nodeList = new ArrayList<>();
				//LinkedList<Node> pathList = new LinkedList<>();
				//nodeList = DataModelI.getInstance().retrieveNodes();
				Node startNode = DataModelI.getInstance().getNodeByLongName(startName);
				Node endNode = DataModelI.getInstance().getNodeByLongName(endName);

				startFloor = startNode.getFloor();
				endFloor = endNode.getFloor();
				currentFloor = startNode.getFloor();

				try {
//				pathList = Singleton.getInstance().pathfindingContext.getPath(startNode, endNode, new AStarStrategyI());
					pathList = optionPicker.pathfindingContext.getPath(startNode, endNode, new AStarStrategyI());

				} catch (PathNotFoundException e) {
					e.printStackTrace();
				}

				String dirFloorL2 = "";
				String dirFloorL1 = "";
				String dirFloor1 = "";
				String dirFloor2 = "";
				String dirFloor3 = "";

				floorL2DirectionNodes.clear();
				floorL1DirectionNodes.clear();
				floor1DirectionNodes.clear();
				floor2DirectionNodes.clear();
				floor3DirectionNodes.clear();

				directionsL2.clear();
				directionsL1.clear();
				directions1.clear();
				directions2.clear();
				directions3.clear();

				boolean useL2 = false;
				boolean useL1 = false;
				boolean use1 = false;
				boolean use2 = false;
				boolean use3 = false;

				for (Node node: pathList) {
					String floorName = node.getFloor();
					if (floorName.equals("L2")) {
						floorL2DirectionNodes.add(node);
					}
					if (floorName.equals("L1")) {
						floorL1DirectionNodes.add(node);
					}
					if (floorName.equals("1")) {
						floor1DirectionNodes.add(node);
					}
					if (floorName.equals("2")) {
						floor2DirectionNodes.add(node);
					}
					if (floorName.equals("3")) {
						floor3DirectionNodes.add(node);
					}
				}

				directionsL2 = ((pathfinderUtil.angleToText(floorL2DirectionNodes)));
				directionsL1 = (pathfinderUtil.angleToText(floorL1DirectionNodes));
				directions1 = (pathfinderUtil.angleToText(floor1DirectionNodes));
				directions2 = (pathfinderUtil.angleToText(floor2DirectionNodes));
				directions3 = (pathfinderUtil.angleToText(floor3DirectionNodes));

				if(directionsL2.size() > 1){
					useL2 = true;
				}
				if(directionsL1.size() > 1){
					useL1 = true;
				}
				if(directions1.size() > 1){
					use1 = true;
				}
				if(directions2.size() > 1){
					use2 = true;
				}
				if(directions3.size() > 1){
					use3 = true;
				}

				for (int i = 0; i < directionsL2.size(); i++){
					dirFloorL2 += directionsL2.get((i)) + "\n";
				}
				for (int i = 0; i < directionsL1.size(); i++){
					dirFloorL1 += directionsL1.get((i)) + "\n";
				}
				for (int i = 0; i < directions1.size(); i++){
					dirFloor1 += directions1.get((i)) + "\n";
				}
				for (int i = 0; i < directions2.size(); i++){
					dirFloor2 += directions2.get((i)) + "\n";
				}
				for (int i = 0; i < directions3.size(); i++) {
					dirFloor3 += directions3.get((i)) + "\n";
				}
				TreeItem<String> floorDirectionL2 = new TreeItem<>(dirFloorL2);
				TreeItem<String> floorDirectionL1 = new TreeItem<>(dirFloorL1);
				TreeItem<String> floorDirection1 = new TreeItem<>(dirFloor1);
				TreeItem<String> floorDirection2 = new TreeItem<>(dirFloor2);
				TreeItem<String> floorDirection3 = new TreeItem<>(dirFloor3);

				floorL2.getChildren().setAll(floorDirectionL2);
				floorL1.getChildren().setAll(floorDirectionL1);
				floor1.getChildren().setAll(floorDirection1);
				floor2.getChildren().setAll(floorDirection2);
				floor3.getChildren().setAll(floorDirection3);

				// calcDistance function now converts to feet
				double dist = CalcDistance.calcDistance(pathList) * OptionSingleton.getOptionPicker().feetPerPixel;
				etaDistance = String.format("TOTAL DISTANCE: %.1f ft       ETA: %.1f s", dist, dist / OptionSingleton.getOptionPicker().walkSpeedFt);
				TreeItem<String> root = new TreeItem<>(etaDistance);

				root.getChildren().setAll(floor3, floor2,  floor1, floorL1, floorL2);
				colDirections.setCellValueFactory(
						(TreeTableColumn.CellDataFeatures<String, String> param) ->  new SimpleStringProperty(param.getValue().getValue()));

				tblDirections.setRoot(root);

				tblDirections.getRoot().setExpanded(true);

				if( useL2 == true){tblDirections.getTreeItem(5).setExpanded(true);}
				if( useL1 == true){tblDirections.getTreeItem(4).setExpanded(true);}
				if( use1 == true){tblDirections.getTreeItem(3).setExpanded(true);}
				if( use2 == true){tblDirections.getTreeItem(2).setExpanded(true);}
				if( use3 == true){tblDirections.getTreeItem(1).setExpanded(true);}

				directions = FXCollections.observableArrayList(pathfinderUtil.angleToText((LinkedList) pathList));
				paneDirections.setVisible(true);


				listForQR = (LinkedList) pathList;

				// Draw path code
				if (currentDimension.equals("3-D")) {
					// use 3-D
					dimension = "3-D";
					printNodePath(pathList, startFloor, dimension);
					changeFloor(startFloor);
				} else {
					// use 2-D
					dimension = "2-D";
					printNodePath(pathList, startFloor, dimension);
					changeFloor(startFloor);
				}

				breadBoy();
				animatePath();

			}
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	private void animatePath() {
		arrow.setVisible(true);
		PathTransition pathTransition = new PathTransition(Duration.millis(7500), currPath, arrow);
		pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
		pathTransition.setCycleCount(Timeline.INDEFINITE);
		pathTransition.play();
	}

	private double getDistance(Node a, Node b) {
		double num;
		if (Math.abs((num = a.getXCoord() - b.getXCoord())) > 0) {
			return Math.abs(num);
		} else if (Math.abs((num = a.getYCoord() - b.getYCoord())) > 0) {
			return Math.abs(num);
		}
		return 0;
	}

	private double calcTime(List<Node> pathList, int i, double wantedVelocity) {
		double d = getDistance(pathList.get(i - 1), pathList.get(i));
		return Math.abs(d / wantedVelocity);
	}

	private void clearPath() {
		currPath.getElements().clear();
		diffPath.getElements().clear();
	}

	private void startCircleClicked(MouseEvent event) {
		System.out.println("Recognized a click");

		if (!startFloor.equals(currentFloor)) {
			if (currentDimension.equals("3-D")) { // 3-D
				changeFloor(startFloor);

			} else { // 2-D

				// !!!
				changeFloor(startFloor);
			}
			currentFloor = startFloor;
		}

	}

	@FXML
	private void endCircleClicked(MouseEvent event) {
		System.out.println("Recognized a click");
		if (!endFloor.equals(currentFloor)) {
			if (currentDimension.equals("3-D")) { // 3-D
				changeFloor(endFloor);

			} else { // 2-D

				// !!!
				changeFloor(endFloor);
			}
			currentFloor = endFloor;
		}
	}

	private void clearPoints() {
		for (Circle c : circleList) {
			pointMap.getChildren().remove(c);    // clears all the node circles (only the kiosk now)
		}
		circleList.clear();

		for (ImageView i : iconList) {
			pointMap.getChildren().remove(i);    // clears all the node icons
		}
		iconList.clear();
	}

	@FXML
	private void cancel(MouseEvent mouseEvent) {
		showStartAndEnd();
	}

	private void printPoints(String floor, String dimension) {
		// Connection for the database
		//List<Node> nodeList = DataModelI.getInstance().retrieveNodes();

		// map boundaries

		int i = 0;
		int x = 0;
		int y = 0;
		Node currNode;
		// Iterate through each node
		while (i < nodeList.size()) {
			currNode = nodeList.get(i);
			// If the node is on the correct floor
			if (currNode.getFloor().equals(floor) && !currNode.getNodeType().equals("HALL")) {

				if (dimension.equals("2-D")) {
					// Get x and y coords
					x = currNode.getXCoord();
					y = currNode.getYCoord();
				} else if (dimension.equals("3-D")) {
					x = currNode.getXCoord3D();
					y = currNode.getYCoord3D();
				} else {
					System.out.println("Invalid dimension");
				}

				ImageView icon = new ImageView();                // The icon for the node
				icon.setId(currNode.getLongName());
				//icon.setX(x-25);
				//icon.setY(y-25);

				icon.setX(x-nodeIconWidth/2);
				icon.setY(y-nodeIconHeight/2);
				icon.setOnMouseClicked(this::chooseNode);
				icon.setOnMouseEntered(this::printName);		// When hovered show name
				icon.setOnMouseExited(this::removeName);		// Remove name when mouse exited
				makeNodeIcon(currNode, icon);
			}
			i++;
		}
	}

	private void updateIconRotate() {
		for(ImageView icon: iconList) {
			RotateTransition rt = new RotateTransition(Duration.millis(1000), icon);
			rt.setToAngle(-scrollGroup.getRotate());
			rt.setCycleCount(1);
			rt.setAutoReverse(true);
			rt.play();
		}
	}

	/**
	 * Makes the icon for the node and displays it on the map
	 *
	 * @param node the node to be given an icon
	 * @param icon the imageview the icon will be displayed on
	 */
	private void makeNodeIcon(Node node, ImageView icon) {
		icon.setFitHeight(nodeIconHeight);
		icon.setFitWidth(nodeIconWidth);
		pointMap.getChildren().add(icon);
		iconList.add(icon);                        // Used to remove the icons later
		if (node.getNodeType().equals("REST")) {
			new ProxyImage(icon, "RestroomIcon.png").displayIcon();
		} else if (node.getNodeType().equals("CONF")) {
			new ProxyImage(icon, "ConferenceIcon.png").displayIcon();
		} else if(node.getNodeType().equals("EXIT")) {
			new ProxyImage(icon, "ExitDoorIcon.png").displayIcon();
		} else if(node.getNodeType().equals("SERV")) {
			new ProxyImage(icon, "RestroomIcon.png").displayIcon();
		} else if (node.getNodeType().equals("INFO")) {
			new ProxyImage(icon, "InfoIcon.png").displayIcon();
		} else if (node.getNodeType().equals("LABS")) {
			new ProxyImage(icon, "LaboratoryIcon.png").displayIcon();
		} else if (node.getNodeType().equals("DEPT")) {
			new ProxyImage(icon, "DepartmentIcon.png").displayIcon();
		} else if (node.getNodeType().equals("STAI")) {
			new ProxyImage(icon, "StairIcon.png").displayIcon();
		} else if (node.getNodeType().equals("ELEV")) {
			new ProxyImage(icon, "ElevatorIcon.png").displayIcon();
		} else if (node.getNodeType().equals("RETL")) {
			new ProxyImage(icon, "RetailIcon.png").displayIcon();
		}
	}

	private String returnFloorName(String floorName) {
		switch (floorName) {
			case "FLOOR: L2":
				return "L2";
			case "FLOOR: L1":
				return "L1";
			case "FLOOR: 1":
				return "1";
			case "FLOOR: 2":
				return "2";
			case "FLOOR: 3":
				return "3";
			default:
				return "1";
		}
	}

	/*private void createMap() {
		Map<Integer, Node> embeddedMap;
		for (Node node : nodeList) {
			embeddedMap = new HashMap<>();
			embeddedMap.put(node.getYCoord(), node);
			nodeMap.put(node.getXCoord(), embeddedMap);
		}
	} */

	//-----------------------------------------------------------------------------------------------------------------
	//
	//                                           Rotate and Zoom on 2D map
	//
	//-----------------------------------------------------------------------------------------------------------------
	@FXML
	Pane overMap; // this is just a pane that i put the nodes/paths/map on so they will all scale and rotate together

	@FXML
	JFXButton btnZoomOut;

	@FXML
	JFXButton btnZoomIn;

	@FXML
	JFXButton btnRotateClockwise;

	@FXML
	JFXButton btnRotateCounterClockwise;

	@FXML
	ImageView imgCompass;

	@FXML
	Button btnCompass;

	@FXML
	JFXButton btnL2;

	@FXML
	JFXButton btnL1;

	@FXML
	JFXButton btn1;

	@FXML
	JFXButton btn2;

	@FXML
	JFXButton btn3;

	@FXML
	Text nodeFloor;

	@FXML
	Label lblNode;

	@FXML
	Slider sldZoom;

	// The zooming is a bit weird... should be looked into more in the future
	public void zoomIn(ActionEvent event) {
		if (!(scrollGroup.getScaleX() > 2) || !(scrollGroup.getScaleY() > 2)) {
			scrollGroup.setScaleX(scrollGroup.getScaleX() + .1);
			scrollGroup.setScaleY(scrollGroup.getScaleY() + .1);
			sldZoom.setValue(((scrollGroup.getScaleX() + .1) - .7) * 100);
		}
	}

	public void zoomOut(ActionEvent event) {
		if (!(scrollGroup.getScaleX() < .5) || !(scrollGroup.getScaleY() < .5)) {
			scrollGroup.setScaleX(scrollGroup.getScaleX() - .1);
			scrollGroup.setScaleY(scrollGroup.getScaleY() - .1);
			sldZoom.setValue(((scrollGroup.getScaleX() - .1) - .7) * 100);
		}
	}

	public void zoom(Event event) {
		if (sldZoom.getValue() > 50) {
			scrollGroup.setScaleX(.7 + (sldZoom.getValue() / 100));
			scrollGroup.setScaleY(.7 + (sldZoom.getValue() / 100));
		} else {
			scrollGroup.setScaleX(.7 + (sldZoom.getValue() / 100));
			scrollGroup.setScaleY(.7 + (sldZoom.getValue() / 100));
		}
	}

	public void rotateRight(ActionEvent event) {
		scrollGroup.setRotate(scrollGroup.getRotate() - 30);
		double currentRotation = imgCompass.getRotate();
		imgCompass.setRotate(currentRotation - 30);
		updateIconRotate();
	}

	public void rotateLeft(ActionEvent event) {
		scrollGroup.setRotate(scrollGroup.getRotate() + 30);
		double currentRotation = imgCompass.getRotate();
		imgCompass.setRotate(currentRotation + 30);
		updateIconRotate();

	}

	public void resetRotate(ActionEvent event) {
		scrollGroup.setRotate(0);
		imgCompass.setRotate(0);
		updateIconRotate();
	}

	//-----------------------------------------------------------------------------------------------------------------
	//
	//                                           Names over nodes
	//
	//-----------------------------------------------------------------------------------------------------------------
	//private FadeTransition nameTransition = new FadeTransition(Duration.millis(400), currName);

	private void printName(MouseEvent mouseEvent) {
		ImageView currCircle = (ImageView) mouseEvent.getTarget();
		lblNode.setText("  " + currCircle.getId());
		nodePane.setVisible(true);
		nodePane.setLayoutX(currCircle.getX()+45);
		nodePane.setLayoutY(currCircle.getY());//+45*Math.sin(Math.toRadians(scrollGroup.getRotate())));
//		nodePane.setRotate(-scrollGroup.getRotate());
		Rotate rotationTransform = new Rotate(-scrollGroup.getRotate(), -45, 0);
// 		nodePane.getTransforms().set(1, rotationTransform);
		nodePane.getTransforms().clear();
		nodePane.getTransforms().add(rotationTransform);
		fade = new FadeTransition(Duration.millis(200), nodePane);
		fade.setFromValue(0);
		fade.setToValue(1);
		fade.setAutoReverse(true);
		fade.setCycleCount(1);
		fade.play();
	}

	private void removeName(MouseEvent mouseEvent) {
		fade = new FadeTransition(Duration.millis(200), nodePane);
		fade.setFromValue(1);
		fade.setToValue(0);
		fade.setAutoReverse(true);
		fade.setCycleCount(1);
		fade.play();
		nodePane.setVisible(false);
	}


	@FXML
	private void printStartName(MouseEvent mouseEvent) {
		fade = new FadeTransition(Duration.millis(200), startName);
		Rotate rotationTransform = new Rotate(-scrollGroup.getRotate(), -45, 0);
		startName.getTransforms().clear();
		startName.getTransforms().add(rotationTransform);
		fade.setFromValue(0);
		fade.setToValue(1);
		fade.setAutoReverse(true);
		fade.setCycleCount(1);
		fade.play();
	}

	@FXML
	private void removeStartName(MouseEvent mouseEvent) {
		fade = new FadeTransition(Duration.millis(200), startName);
		fade.setFromValue(1);
		fade.setToValue(0);
		fade.setAutoReverse(true);
		fade.setCycleCount(1);
		fade.play();
	}

	@FXML
	private void printEndName(MouseEvent mouseEvent) {
		fade = new FadeTransition(Duration.millis(200), endName);
		Rotate rotationTransform = new Rotate(-scrollGroup.getRotate(), -45, 0);
		endName.getTransforms().clear();
		endName.getTransforms().add(rotationTransform);
		fade.setFromValue(0);
		fade.setToValue(1);
		fade.setAutoReverse(true);
		fade.setCycleCount(1);
		fade.play();
	}

	@FXML
	private void removeEndName(MouseEvent mouseEvent) {
		fade = new FadeTransition(Duration.millis(200), endName);
		fade.setFromValue(1);
		fade.setToValue(0);
		fade.setAutoReverse(true);
		fade.setCycleCount(1);
		fade.play();
	}

	//-----------------------------------------------------------------------------------------------------------------
	//
	//                                           Bread Crumb
	//
	//-----------------------------------------------------------------------------------------------------------------

	@FXML
	JFXButton btnLeft;

	@FXML
	JFXButton btnRight;

	//-----------------------------------------------------------------------------------------------------------------
	//
	//                                           Change Floors
	//
	//-----------------------------------------------------------------------------------------------------------------

	@FXML
	ImageView imgL2;

	@FXML
	ImageView imgL1;

	@FXML
	ImageView img1;

	@FXML
	ImageView img2;

	@FXML
	ImageView img3;

	private void changeFloor(String floor) {
		if (floor.equals("L2"))
			changeFloorL2(null);
		if (floor.equals("L1"))
			changeFloorL1(null);
		if (floor.equals("1"))
			changeFloor1(null);
		if (floor.equals("2"))
			changeFloor2(null);
		if (floor.equals("3"))
			changeFloor3(null);
	}

	public void changeFloorL2(ActionEvent event) {
		if (currentDimension.equals("3-D")) {
			floor3DMapLoader("L2");
		} else {
			floor2DMapLoader("L2");
		}

		currentFloor = "L2";
		clearPoints();
		printPoints(currentFloor, currentDimension);
		printKiosk();

		if (currentFloor.equals(endFloor)) {
			destination.setVisible(true);
		} else {
			destination.setVisible(false);
		}
		btnL2.setLayoutX(0);
		btnL1.setLayoutX(20);
		btn1.setLayoutX(0);
		btn2.setLayoutX(0);
		btn3.setLayoutX(0);

		new ProxyImage(imgL2, "FloorL2IconSelected.png").displayIcon();
		new ProxyImage(imgL1, "FloorL1Icon.png").displayIcon();
		new ProxyImage(img1, "Floor1Icon.png").displayIcon();
		new ProxyImage(img2, "Floor2Icon.png").displayIcon();
		new ProxyImage(img3, "Floor3Icon.png").displayIcon();

		System.out.println("you selected floor L2");
	}

	public void changeFloorL1(ActionEvent event) {
		if (currentDimension.equals("3-D")) {
			floor3DMapLoader("L1");
		} else {
			floor2DMapLoader("L1");
		}

		currentFloor = "L1";
		clearPoints();
		printPoints(currentFloor, currentDimension);
		printKiosk();

		if (currentFloor.equals(endFloor)) {
			destination.setVisible(true);
		} else {
			destination.setVisible(false);
		}

		btnL2.setLayoutX(0);
		btnL1.setLayoutX(20);
		btn1.setLayoutX(0);
		btn2.setLayoutX(0);
		btn3.setLayoutX(0);


		new ProxyImage(imgL2, "FloorL2Icon.png").displayIcon();
		new ProxyImage(imgL1, "FloorL1IconSelected.png").displayIcon();
		new ProxyImage(img1, "Floor1Icon.png").displayIcon();
		new ProxyImage(img2, "Floor2Icon.png").displayIcon();
		new ProxyImage(img3, "Floor3Icon.png").displayIcon();

		System.out.println("you selected floor L1");
	}

	public void changeFloor1(ActionEvent event) {
		if (currentDimension.equals("3-D")) {
			floor3DMapLoader("1");
		} else {
			floor2DMapLoader("1");
		}

		currentFloor = "1";
		clearPoints();
		printPoints(currentFloor, currentDimension);
		printKiosk();

		if (currentFloor.equals(endFloor)) {
			destination.setVisible(true);
		} else {
			destination.setVisible(false);
		}

		btnL2.setLayoutX(0);
		btnL1.setLayoutX(0);
		btn1.setLayoutX(20);
		btn2.setLayoutX(0);
		btn3.setLayoutX(0);

		new ProxyImage(imgL2, "FloorL2Icon.png").displayIcon();
		new ProxyImage(imgL1, "FloorL1Icon.png").displayIcon();
		new ProxyImage(img1, "Floor1IconSelected.png").displayIcon();
		new ProxyImage(img2, "Floor2Icon.png").displayIcon();
		new ProxyImage(img3, "Floor3Icon.png").displayIcon();

		System.out.println("you selected floor 1");

	}

	public void changeFloor2(ActionEvent event) {
		if (currentDimension.equals("3-D")) {
			floor3DMapLoader("2");
		} else {
			floor2DMapLoader("2");
		}

		currentFloor = "2";
		clearPoints();
		printPoints(currentFloor, currentDimension);
		printKiosk();

		if (currentFloor.equals(endFloor)) {
			destination.setVisible(true);
		} else {
			destination.setVisible(false);
		}

		btnL2.setLayoutX(0);
		btnL1.setLayoutX(0);
		btn1.setLayoutX(0);
		btn2.setLayoutX(20);
		btn3.setLayoutX(0);

		new ProxyImage(imgL2, "FloorL2Icon.png").displayIcon();
		new ProxyImage(imgL1, "FloorL1Icon.png").displayIcon();
		new ProxyImage(img1, "Floor1Icon.png").displayIcon();
		new ProxyImage(img2, "Floor2IconSelected.png").displayIcon();
		new ProxyImage(img3, "Floor3Icon.png").displayIcon();
		System.out.println("you selected floor 2");

	}

	public void changeFloor3(ActionEvent event) {
		if (currentDimension.equals("3-D")) {
			floor3DMapLoader("3");
		} else {
			floor2DMapLoader("3");
		}

		currentFloor = "3";
		clearPoints();
		printPoints(currentFloor, currentDimension);
		printKiosk();

		if (currentFloor.equals(endFloor)) {
			destination.setVisible(true);
		} else {
			destination.setVisible(false);
		}

		btnL2.setLayoutX(0);
		btnL1.setLayoutX(0);
		btn1.setLayoutX(0);
		btn2.setLayoutX(0);
		btn3.setLayoutX(20);


		new ProxyImage(imgL2, "FloorL2Icon.png").displayIcon();
		new ProxyImage(imgL1, "FloorL1Icon.png").displayIcon();
		new ProxyImage(img1, "Floor1Icon.png").displayIcon();
		new ProxyImage(img2, "Floor2Icon.png").displayIcon();
		new ProxyImage(img3, "Floor3IconSelected.png").displayIcon();

		System.out.println("you selected floor 3");

	}

	//-----------------------------------------------------------------------------------------------------------------
	//
	//                                           Directory
	//
	//-----------------------------------------------------------------------------------------------------------------

	@FXML
	JFXButton btnCloseStartDirectory;

	@FXML
	JFXButton btnOpenStartDirectory;

	@FXML
	JFXButton btnCloseEndDirectory;

	@FXML
	JFXButton btnOpenEndDirectory;

	@FXML
	Pane paneStartDirectory;

	@FXML
	Pane paneEndDirectory;
	@FXML
	JFXButton btnHome;

	private String convertType(String type) {

		if (type == null) {
			return null;
		} else {

			String finalString;
			switch (type) {
				case "Laboratory":
					finalString = "LABS";
					break;
				case "Information":
					finalString = "INFO";
					break;
				case "Retail":
					finalString = "RETL";
					break;
				case "Bathroom":
					finalString = "BATH";
					break;
				case "Stair":
					finalString = "STAI";
					break;
				case "Service":
					finalString = "SERV";
					break;
				case "Restroom":
					finalString = "REST";
					break;
				case "Elevator":
					finalString = "ELEV";
					break;
				case "Department":
					finalString = "DEPT";
					break;
				case "Conference":
					finalString = "CONF";
					break;
				case "Exit":
					finalString = "EXIT";
					break;
				default:
					finalString = "None";
			}
			return finalString;
		}
	}

	public String convertTypeReverse(String type) {

		if (type == null) {
			return null;
		} else {
			String finalString;
			switch (type) {
				case "LABS":
					finalString = "Laboratory";
					break;
				case "INFO":
					finalString = "Information";
					break;
				case "RETL":
					finalString = "Retail";
					break;
				case "BATH":
					finalString = "Bathroom";
					break;
				case "STAI":
					finalString = "Stair";
					break;
				case "SERV":
					finalString = "Service";
					break;
				case "REST":
					finalString = "Restroom";
					break;
				case "ELEV":
					finalString = "Elevator";
					break;
				case "DEPT":
					finalString = "Department";
					break;
				case "CONF":
					finalString = "Conference";
					break;
				case "EXIT":
					finalString = "Exit";
					break;
				default:
					finalString = "None";
			}
			return finalString;
		}
	}

	public void openStartDirectory(ActionEvent event) {
		btnCloseStartDirectory.setVisible(true);
		btnOpenStartDirectory.setVisible(false);
		paneStartDirectory.setVisible(true);
		paneEndDirectory.setVisible(false);

		btnOpenEndDirectory.setVisible(true);
		btnCloseEndDirectory.setVisible(false);

		comBuildingStart.getSelectionModel().clearSelection();
		comFloorStart.getSelectionModel().clearSelection();
		comTypeStart.getSelectionModel().clearSelection();

	}

	public void closeStartDirectory(ActionEvent event) {
		btnCloseStartDirectory.setVisible(false);
		btnOpenStartDirectory.setVisible(true);
		paneStartDirectory.setVisible(false);
	}

	public void openEndDirectory(ActionEvent event) {
		btnCloseEndDirectory.setVisible(true);
		btnOpenEndDirectory.setVisible(false);
		paneEndDirectory.setVisible(true);
		paneStartDirectory.setVisible(false);

		btnOpenStartDirectory.setVisible(true);
		btnCloseStartDirectory.setVisible(false);
	}

	public void closeEndDirectory(ActionEvent event) {
		btnCloseEndDirectory.setVisible(false);
		btnOpenEndDirectory.setVisible(true);
		paneEndDirectory.setVisible(false);
	}

	public void setStart(ActionEvent event) {
		String startLocation = lstStartDirectory.getSelectionModel().getSelectedItem();
		txtLocationStart.setText(startLocation);

		btnCloseStartDirectory.setVisible(false);
		btnOpenStartDirectory.setVisible(true);
		paneStartDirectory.setVisible(false);
	}

	public void setEnd(ActionEvent event) {
		String endLocation = lstEndDirectory.getSelectionModel().getSelectedItem();
		txtLocationEnd.setText(endLocation);

		btnCloseEndDirectory.setVisible(false);
		btnOpenEndDirectory.setVisible(true);
		paneEndDirectory.setVisible(false);
	}

	public void filterStart(ActionEvent event) {
		lstStartDirectory.setItems(FXCollections.observableList(DataModelI.getInstance().getNamesByBuildingFloorType(comBuildingStart.getValue(), comFloorStart.getValue(), convertType(comTypeStart.getValue()))));
	}

	public void filterEnd(ActionEvent event) {
		lstEndDirectory.setItems(FXCollections.observableList(DataModelI.getInstance().getNamesByBuildingFloorType(comBuildingEnd.getValue(), comFloorEnd.getValue(), convertType(comTypeEnd.getValue()))));
	}

	public void goHome(ActionEvent event) {
		try {
			Parent login;
			Stage stage;
			//get reference to the button's stage
			stage = (Stage) btnHome.getScene().getWindow();
			//load up Home FXML document
			login = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/idleMap.fxml"));


			//create a new scene with root and set the stage
			Scene scene = new Scene(login);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void listenForStartLocation(MouseEvent mouseEvent) {
		System.out.println("Start Location Text Field Touched");
		forStart = true;
	}

	public void listenForEndLocation(MouseEvent mouseEvent) {
	    System.out.println("End Location Text Field Touched");
	    forStart = false;
    }

	//-----------------------------------------------------------------------------------------------------------------
	//
	//                                           Bread Boi's - SO MUCH BREAD
	//
	//-----------------------------------------------------------------------------------------------------------------
	@FXML
	JFXButton btnStep1;

	@FXML
	JFXButton btnStep2;

	@FXML
	JFXButton btnStep3;

	@FXML
	ImageView imageStep1;

	@FXML
	ImageView imageStep2;

	@FXML
	ImageView imageStep3;

	List<Node> breadSection1 = new ArrayList<>();

	List<Node> breadSection2 = new ArrayList<>();

	List<Node> breadSection3 = new ArrayList<>();

	@FXML
	public void nextBreadcrumb() {
		int i = 0;
		while(i < breadcrumbs.size()) {
			if(currentFloor.equals(breadcrumbs.get(i)) && i != breadcrumbs.size()-1) {
				if(i == 0) {
					breadFloorSwitch2(null);
					break;
				} else if (i == 1) {
					breadFloorSwitch3(null);
					break;
				}
			}
			i++;
		}
	}

	@FXML
	public void lastBreadcrumb () {
		int i = 0;
		while(i < breadcrumbs.size()) {
			if(currentFloor.equals(breadcrumbs.get(i)) && i != 0) {
				if(i == 2) {
					breadFloorSwitch2(null);
					break;
				} else if (i == 1) {
					breadFloorSwitch1(null);
					break;
				}
			}
			i++;
		}
	}

	/**
		 * Sets the global variable breadrumbs to all the floors that need to be traversed
		 */
	private void getBreadcrumbs() {
			int i = 0;
			String curr = "";
			while (i < pathList.size()) {
				if (pathList.get(i).getFloor().equals(curr)) {
				} else {
					curr = pathList.get(i).getFloor();
					breadcrumbs.add(curr);
				}
				i++;
			}
	}

	private void getBreadSections() {
		int i = 0;
		int section = 0;
		String curr = "";
		while(i < pathList.size()) {
			if(pathList.get(i).getFloor().equals(curr)) {

			} else {
				curr = pathList.get(i).getFloor();
				section++;
				if(section == 1) {
					breadSection1.add(pathList.get(i));
				} else if (section == 2) {
					breadSection2.add(pathList.get(i));
					breadSection1.add(pathList.get(i-1));
				} else if (section == 3) {
					breadSection3.add(pathList.get(i));
					breadSection2.add(pathList.get(i-1));
				}
			}
			i++;
		}
		breadSection3.add(pathList.get(pathList.size()-1));
	}

	/**
	 * Sets the images and buttons for the breadcrumbs based on the global variable bradcrumbs
	 */
	private void breadBoy() {
		getBreadcrumbs();
		getBreadSections();
		int i = 0;
		while (i < breadcrumbs.size()) {
			if (i == 0) {
				btnStep1.setDisable(false);
				breadHelper(btnStep1, breadcrumbs.get(i), imageStep1);
			} else if (i == 1) {
				btnStep2.setDisable(false);
				breadHelper(btnStep2, breadcrumbs.get(i), imageStep2);
			} else if (i == 2) {
				btnStep3.setDisable(false);
				breadHelper(btnStep3, breadcrumbs.get(i), imageStep3);
			}
			i++;
		}
		breadFloorSwitch1(null);
	}

	/**
	 * Sets the graphic for the given button and image
	 *
	 * @param btn   button to be given the grahpic
	 * @param floor floor of the icon
	 * @param icon  the icon to hold the graphic
	 */
	private void breadHelper(Button btn, String floor, ImageView icon) {
		icon.setFitWidth(70);
		icon.setFitHeight(70);

		if (floor.equals("1")) {
			new ProxyImage(icon, "Floor1Icon.png").displayIcon();
			btn.setGraphic(icon);
		} else if (floor.equals("2")) {
			new ProxyImage(icon, "Floor2Icon.png").displayIcon();
			btn.setGraphic(icon);
		} else if (floor.equals("3")) {
			new ProxyImage(icon, "Floor3Icon.png").displayIcon();
			btn.setGraphic(icon);
		} else if (floor.equals("L1")) {
			new ProxyImage(icon, "FloorL1Icon.png").displayIcon();
			btn.setGraphic(icon);
		} else if (floor.equals("L2")) {
			new ProxyImage(icon, "FloorL2Icon.png").displayIcon();
			btn.setGraphic(icon);
		}
	}

	@FXML
	public void breadFloorSwitch1(MouseEvent mouseEvent) {
		try {
			changeFloor(breadcrumbs.get(0));
			new ProxyImage(imageStep1, "Floor" + breadcrumbs.get(0) + "IconSelected.png").displayIcon();
			new ProxyImage(imageStep2, "Floor" + breadcrumbs.get(1) + "Icon.png").displayIcon();
			new ProxyImage(imageStep3, "Floor" + breadcrumbs.get(2) + "Icon.png").displayIcon();
			breadSnap("1");
		} catch (IndexOutOfBoundsException e) {
			System.out.println("Sall good");
		}
	}

	@FXML
	public void breadFloorSwitch2(MouseEvent mouseEvent) {
		try {
			changeFloor(breadcrumbs.get(1));
			new ProxyImage(imageStep2, "Floor" + breadcrumbs.get(1) + "IconSelected.png").displayIcon();
			new ProxyImage(imageStep1, "Floor" + breadcrumbs.get(0) + "Icon.png").displayIcon();
			new ProxyImage(imageStep3, "Floor" + breadcrumbs.get(2) + "Icon.png").displayIcon();
			breadSnap("2");
		} catch (IndexOutOfBoundsException e) {
			System.out.println("Sall good");
		}
	}

	@FXML
	public void breadFloorSwitch3(MouseEvent mouseEvent) {
		try {
			changeFloor(breadcrumbs.get(2));
			new ProxyImage(imageStep3, "Floor" + breadcrumbs.get(2) + "IconSelected.png").displayIcon();
			new ProxyImage(imageStep1, "Floor" + breadcrumbs.get(0) + "Icon.png").displayIcon();
			new ProxyImage(imageStep2, "Floor" + breadcrumbs.get(1) + "Icon.png").displayIcon();
			breadSnap("3");
		} catch (IndexOutOfBoundsException e) {
			System.out.println("Sall good");
		}
	}

	private void breadSnap(String num) {
		if(num.equals("1")) {
			snap(breadSection1.get(0), breadSection1.get(breadSection1.size()-1));
		} else if(num.equals("2")) {
			snap(breadSection2.get(0), breadSection2.get(breadSection2.size()-1));
		}else if(num.equals("3")) {
			snap(breadSection3.get(0), breadSection3.get(breadSection3.size()-1));
		}
	}

	private void setTheBreadyBoysBackToTheirGrayStateAsSoonAsPossibleSoThatItMakesSenseAgainPlease() {
		new ProxyImage(imageStep3, "InvalidIcon.png").displayIcon();
		new ProxyImage(imageStep1, "InvalidIcon.png").displayIcon();
		new ProxyImage(imageStep2, "InvalidIcon.png").displayIcon();
		btnStep1.setDisable(true);
		btnStep2.setDisable(true);
		btnStep3.setDisable(true);
	}

	public void closeHelp() {}
}
