package com.manlyminotaurs.viewControllers;

//import com.manlyminotaurs.core.KioskInfo;
import com.jfoenix.controls.*;
import com.manlyminotaurs.communications.ClientSetup;
import com.manlyminotaurs.communications.SendEmail;
import com.manlyminotaurs.communications.SendTxt;
import com.manlyminotaurs.core.KioskInfo;
import com.manlyminotaurs.core.Main;
import com.manlyminotaurs.databases.DataModelI;
import com.manlyminotaurs.nodes.INode;
import com.manlyminotaurs.nodes.Node;
import com.manlyminotaurs.nodes.Room;
import com.manlyminotaurs.pathfinding.*;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.io.File;
import java.net.Proxy;
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
	final static ObservableList<String> floors = FXCollections.observableArrayList("None","L2", "L1", "1", "2", "3");
	//final static ObservableList<String> mapFloors = FXCollections.observableArrayList("FLOOR: L2", "FLOOR: L1", "FLOOR: 1", "FLOOR: 2", "FLOOR: 3");
	final static ObservableList<String> empty = FXCollections.observableArrayList();
	//final ObservableList<String> buildings = FXCollections.observableArrayList(DataModelI.getInstance().getBuildingsFromList());
	final static ObservableList<String> buildings = FXCollections.observableArrayList("None","45 Francis", "Tower", "Shapiro", "BTM", "15 Francis");
	//final ObservableList<String> types = FXCollections.observableArrayList(DataModelI.getInstance().getTypesFromList());
	final static ObservableList<String> types = FXCollections.observableArrayList("None","Laboratory","Information", "Retail", "Bathroom", "Stair", "Service","Restroom","Elevator", "Department", "Conference","Exit");
	ObservableList<String> directions;
	final int MAPX2D = 5000;
	final int MAPY2D = 3400;

	String currentFloor = "1";
	String currentDimension = "2-D";

	Parent adminRequest;
	Parent staffRequest;
	Circle startCircle = new Circle();
	List<Node> nodeList = DataModelI.getInstance().retrieveNodes();
	List<Node> pathList = new ArrayList<>();
	LinkedList<Node> listForQR = new LinkedList<Node>();
	//List<javafx.scene.text.Text> nameList = new ArrayList<>();
	Image imageQRCode;
	String startFloor = "";
	String endFloor = "";
	List<Circle> circleList = new ArrayList<>();
	boolean isStart = true;
	javafx.scene.text.Text currName;
	FadeTransition fade;
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
	Path pathL2;

	@FXML
	Path pathL1;

	@FXML
	Path path1;

	@FXML
	Path path2;

	@FXML
	Path path3;

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

		changeFloor("1");

		TextFields.bindAutoCompletion(txtLocationStart, FXCollections.observableArrayList(DataModelI.getInstance().getNamesByBuildingFloorType(null, null, null)));
		TextFields.bindAutoCompletion(txtLocationEnd, FXCollections.observableArrayList(DataModelI.getInstance().getNamesByBuildingFloorType(null, null, null)));

		lstStartDirectory.setItems(FXCollections.observableList(DataModelI.getInstance().getNamesByBuildingFloorType(comBuildingStart.getValue(),comFloorStart.getValue(),convertType(comTypeStart.getValue()))));
		lstEndDirectory.setItems(FXCollections.observableList(DataModelI.getInstance().getNamesByBuildingFloorType(comBuildingEnd.getValue(),comFloorEnd.getValue(),convertType(comTypeEnd.getValue()))));

	}

	public void initialize(URL location, ResourceBundle resources) {
		try {

			setPathfindingScreen();

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

		setKiosk();
		printKiosk();
		goToKiosk();

	}

	public void setStrategy(){
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
		if (Main.pathStrategy.equals("DYK")){
//		    Singleton.getInstance().pathfindingContext.strategy = new ClosestStrategyI();
			optionPicker.pathfindingContext.strategy = new ClosestStrategyI();
        }
	}

	public void toggleMap(ActionEvent event) {
		clearPoints();
		circleList.clear();
		printKiosk();
		isStart = true;

		if (currentDimension.equals("2-D")) {

			// Switch 3-D
			new ProxyImage(imgBtnMap,"3DIcon.png").displayIcon();
			currentDimension = "3-D";
			stackPaneMap.setPrefHeight(2774);
			stackPaneMap.setPrefWidth(5000);
			mapImg.setFitHeight(2774);
			mapImg.setFitWidth(5000);
			paneMap.setPrefHeight(2774);
			paneMap.setPrefWidth(5000);
			floor3DMapLoader(currentFloor);
		} else {

			// Switch 2-D
			new ProxyImage(imgBtnMap,"2DIcon.png").displayIcon();
			currentDimension = "2-D";
			stackPaneMap.setPrefHeight(3400);
			stackPaneMap.setPrefWidth(5000);
			mapImg.setFitHeight(3400);
			mapImg.setFitWidth(5000);
			paneMap.setPrefHeight(3400);
			paneMap.setPrefWidth(5000);
			floor2DMapLoader(currentFloor);
		}

	}

	public void printKiosk() {

		Circle kiosk = new Circle();

		if(currentDimension.equals("3-D")) {
			kiosk = new Circle(KioskInfo.myLocation.getXCoord3D(), KioskInfo.myLocation.getYCoord3D(), 13);
			clearPoints();
		} else {
			kiosk = new Circle(KioskInfo.myLocation.getXCoord(), KioskInfo.myLocation.getYCoord(), 13);
			clearPoints();
		}
		circleList.clear();
		//circleList.remove(kiosk);

		System.out.println(currentFloor + " is the floor");
		if(currentFloor.equals("1")) {
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
		paneMap.getChildren().add(kiosk);
		circleList.add(kiosk);
	}

	public void goToKiosk() {
		if(currentDimension.equals("3-D")) {
			scrollPaneMap.setVvalue((double) KioskInfo.myLocation.getYCoord() / 2774.0);
			scrollPaneMap.setHvalue((double) KioskInfo.myLocation.getXCoord() / 5000.0);
		} else {
			scrollPaneMap.setVvalue((double) KioskInfo.myLocation.getYCoord() / 3400.0);
			scrollPaneMap.setHvalue((double) KioskInfo.myLocation.getXCoord() / 5000.0);
		}
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
		isStart = true;
		showStartAndEnd();
		clearPoints();
		hideStartAndEnd();
		//printKiosk();
		circleList.clear();
		if (currentDimension.equals("3-D"))
			printPoints(currentFloor, "3-D");
		else
			printPoints(currentFloor, "2-D");

	}

	public void printNodesOnFloorEnd(MouseEvent event) {
		isStart = false;
		showStartAndEnd();
		clearPoints();
		hideStartAndEnd();
		circleList.clear();
		if (currentDimension.equals("3-D"))
			printPoints(currentFloor, "3-D");
		else
			printPoints(currentFloor, "2-D");

	}

	private void showStartAndEnd() {
		clearPoints();
		printKiosk();
		comBuildingStart.setDisable(false);
		comBuildingEnd.setDisable(false);
		comFloorStart.setDisable(false);
		comFloorEnd.setDisable(false);
		comTypeStart.setDisable(false);
		comTypeEnd.setDisable(false);
	}

	private void hideStartAndEnd() {
		comBuildingStart.setDisable(true);
		comBuildingEnd.setDisable(true);
		comFloorStart.setDisable(true);
		comFloorEnd.setDisable(true);
		comTypeStart.setDisable(true);
		comTypeEnd.setDisable(true);
	}

	public void chooseStartNode(MouseEvent event) {
		Circle circle = (Circle)event.getTarget();
		if(!currentDimension.equals("3-D")) {
			for (Node node : nodeList) {
				if (node.getXCoord() == circle.getCenterX()) {
					if (node.getYCoord() == circle.getCenterY()) {
						System.out.println("Click recognized");
						comBuildingStart.getSelectionModel().select(node.getBuilding());
						comFloorStart.getSelectionModel().select(node.getFloor());
						comTypeStart.getSelectionModel().select(convertTypeReverse(node.getNodeType()));
						txtLocationStart.setText(node.getLongName());
						showStartAndEnd();
						break;
					}
				}
			}
			System.out.println("Node not found");
		} else {
			for (Node node : nodeList) {
				if (node.getXCoord3D() == circle.getCenterX()) {
					if (node.getYCoord3D() == circle.getCenterY()) {
						System.out.println("Click recognized");
						comBuildingStart.getSelectionModel().select(node.getBuilding());
						comFloorStart.getSelectionModel().select(node.getFloor());
						comTypeStart.getSelectionModel().select(convertTypeReverse(node.getNodeType()));
						//comLocationStart.getSelectionModel().select(node.getLongName());
						txtLocationStart.setText(node.getLongName());
						showStartAndEnd();
						break;
					}
				}
			}
			System.out.println("Node not found");
		}
	}

	public void chooseEndNode(MouseEvent event) {
		Circle circle = (Circle)event.getTarget();
		System.out.println(circle.getCenterX());
		if(!currentDimension.equals("3-D")) {
			for (Node node : nodeList) {
				if (node.getXCoord() == circle.getCenterX()) {
					if (node.getYCoord() == circle.getCenterY()) {
						System.out.println("Click recognized");
						comBuildingEnd.getSelectionModel().select(node.getBuilding());
						comFloorEnd.getSelectionModel().select(node.getFloor());
						comTypeEnd.getSelectionModel().select(convertTypeReverse(node.getNodeType()));
						txtLocationEnd.setText(node.getLongName());
						showStartAndEnd();
						break;
					}
				}
			}
			System.out.println("Node not found");
		} else {
			for (Node node : nodeList) {
				if (node.getXCoord3D() == circle.getCenterX()) {
					if (node.getYCoord3D() == circle.getCenterY()) {
						System.out.println("Click recognized");
						comBuildingEnd.getSelectionModel().select(node.getBuilding());
						comFloorEnd.getSelectionModel().select(node.getFloor());
						comTypeEnd.getSelectionModel().select(convertTypeReverse(node.getNodeType()));
						txtLocationEnd.setText(node.getLongName());
						showStartAndEnd();
						break;
					}
				}
			}
			System.out.println("Node not found");
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
	Label lblStartLocation1;

	@FXML
	Label lblEndLocation1;

	@FXML
	JFXButton btnOpenSend;

	@FXML
	JFXListView<String> lstDirections;

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

		animationCircle.setVisible(false);
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

	private String turnListToString(){
		String out = "";
		for(String CurrInstruction : directions){ out += CurrInstruction + '\n';}
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

			new ProxyImage(imgNavigation,"BackIcon.png").displayIcon();

			btnQuickBathroom.setVisible(true);
			btnQuickElevator.setVisible(true);
			btnQuickExit.setVisible(true);
		}
	}

	public void findQuickBathroom(ActionEvent event) {

		// Pathfind to nearest bathroom
		String startFloor = "1";
		Node bathroomNode = new Room("N1X3Y", 1, 3, "F1", "BUILD1", "REST", "Node 1, 3", "n1x3y", 1, 0, 0);
		// Pathfind to nearest bathroom
		PathfinderUtil pu = new PathfinderUtil();
		PathfindingContext pf = new PathfindingContext();
		List<Node> path = new LinkedList<Node>();


		//ArrayList<Node> nodes = new ArrayList<>(DataModelI.getInstance().retrieveNodes());
		//Node startNode = DataModelI.getInstance().getNodeByLongNameFromList("Hallway Node 2 Floor 1", nodes);

		try {
			path = pf.getPath(KioskInfo.getMyLocation(), bathroomNode, new ClosestStrategyI());

			pathList = path;

		} catch (PathNotFoundException e) {
			e.printStackTrace();
		}

		// Show directions interface and hide pathfinding interface
		panePathfinding.setVisible(false);
		paneDirections.setVisible(true);
		// Set new overview panel to correct parameters
		lblStartLocation1.setText("Current Location"); // !!! change to default kiosk location
		lblEndLocation1.setText("Nearest Bathroom"); // !!! change to nearest bathoom
		directions = FXCollections.observableArrayList(pu.angleToText((LinkedList<Node>)path));
        // calcDistance function now converts to feet
		double dist = CalcDistance.calcDistance(pathList)*OptionSingleton.getOptionPicker().feetPerPixel;
		directions.add(String.format("TOTAL DISTANCE: %.1f ft", dist));
		directions.add(String.format("ETA: %.1f s", dist/OptionSingleton.getOptionPicker().walkSpeedFt));
        lstDirections.setItems(directions);
		lstDirections.setItems(directions);
		listForQR = (LinkedList<Node>)path;
		pu.generateQR(pu.angleToText((LinkedList<Node>)path));
		// new ProxyImage(imgQRCode,"CrunchifyQR.png").display2();
		// Draw path code

		// Change floor
		if (currentDimension.equals("3-D")) {
			// use 3-D
			System.out.println("using 3d stairs");
			printNodePath(path, startFloor, "3-D");
			changeFloor(startFloor);
		} else {
			// use 2-D
			printNodePath(path, startFloor, "2-D");
			changeFloor(startFloor);
		}

		// Clear old fields
		// Show directions interface and hide pathfinding interface
		panePathfinding.setVisible(false);
		paneDirections.setVisible(true);

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

	}

	public void findQuickExit(ActionEvent event) {

    }

    public void findQuickElevator(ActionEvent event) {

    }

	//-----------------------------------------------------------------------------------------------------------------
	//
	//                                           Help
	//
	//-----------------------------------------------------------------------------------------------------------------
	@FXML
	JFXButton btnHelp;

	@FXML
	StackPane paneHelpDirections;

	@FXML
    StackPane paneHelpPathfind;

	public void openHelpPanel(ActionEvent event) {
	    paneHelpPathfind.setVisible(true);
	}

	public void closeHelpDirections(MouseEvent mouseEvent) {
		paneHelpDirections.setVisible(false);
	}

	public void closeHelpPathfind(MouseEvent mouseEvent) {
		paneHelpPathfind.setVisible(false);
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

	public void floor2DMapLoader(String floor) {

		if (floor.equals("FLOOR: L2") || floor.equals("L2")) {

			new ProxyImage(mapImg, "00_thelowerlevel2.png").display();
			clearPath();
			printNodePath(pathList, "L2", "2-D");

		} else if (floor.equals("FLOOR: L1") || floor.equals("L1")) {

			new ProxyImage(mapImg, "00_thelowerlevel1.png").display();
			clearPath();
			printNodePath(pathList, "L1", "2-D");

		} else if (floor.equals("FLOOR: 1") || floor.equals("1")) {

			new ProxyImage(mapImg, "01_thefirstfloor.png").display();
			clearPath();
			printNodePath(pathList, "1", "2-D");

		} else if (floor.equals("FLOOR: 2") || floor.equals("2")) {

			new ProxyImage(mapImg, "02_thesecondfloor.png").display();
			clearPath();
			printNodePath(pathList, "2", "2-D");

		} else if (floor.equals("FLOOR: 3") || floor.equals("3")) {

			new ProxyImage(mapImg, "03_thethirdfloor.png").display();
			clearPath();
			printNodePath(pathList, "3", "2-D");

		}

		currentFloor = floor;
	}

	public void floor3DMapLoader(String floor) {

		if (floor.equals("FLOOR: L2") || floor.equals("L2")) {
			new ProxyImage(mapImg, "L2-ICONS.png").display();

			clearPath();
			printNodePath(pathList, "L2", "3-D");

		} else if (floor.equals("FLOOR: L1") || floor.equals("L1")) {
			new ProxyImage(mapImg, "L1-ICONS.png").display();

			clearPath();
			printNodePath(pathList, "L1", "3-D");

		} else if (floor.equals("FLOOR: 1") || floor.equals("1")) {
			new ProxyImage(mapImg, "1-ICONS.png").display();

			clearPath();
			printNodePath(pathList, "1", "3-D");

		} else if (floor.equals("FLOOR: 2") || floor.equals("2")) {
			new ProxyImage(mapImg, "2-ICONS.png").display();

			clearPath();
			printNodePath(pathList, "2", "3-D");

		} else if (floor.equals("FLOOR: 3") || floor.equals("3")) {
			new ProxyImage(mapImg, "3-ICONS.png").display();

			clearPath();
			printNodePath(pathList, "3", "3-D");

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
	javafx.scene.text.Text endName;

	@FXML
	javafx.scene.text.Text destinationText;

	@FXML
	Circle animationCircle;

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
		text.setTranslateX(finishX-subX);
		text.setTranslateY(finishY-subY);
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
			double snapX = 0.0;
			double snapY = 0.0;
			if (dimension.equals("3-D")) {
				snapX = (double) path.get(0).getXCoord() / 5000.0;
				snapY = (double) path.get(0).getYCoord() / 2744.0;
			} else if (dimension.equals("2-D")) {
				snapX = (double) path.get(0).getXCoord() / 5000.0;
				snapY = (double) path.get(0).getYCoord() / 3400.0;
			} else {
				System.out.println("Invalid dimension");
			}
			scrollPaneMap.setVvalue(snapY);
			scrollPaneMap.setHvalue(snapX);
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
					} else if(path.get(i).getFloor().equals("L2")){
						// add the path
						addPath(pathL2, dimension, startNode, endNode, moveTo, lineTo);
					} else if(path.get(i).getFloor().equals("L1")){
						// add the path
						addPath(pathL1, dimension, startNode, endNode, moveTo, lineTo);
					} else if(path.get(i).getFloor().equals("1")){
						// add the path
						addPath(path1, dimension, startNode, endNode, moveTo, lineTo);
					} else if(path.get(i).getFloor().equals("2")){
						// add the path
						addPath(path2, dimension, startNode, endNode, moveTo, lineTo);
					} else if(path.get(i).getFloor().equals("3")){
						// add the path
						addPath(path3, dimension, startNode, endNode, moveTo, lineTo);
					}
				}
				i++;
				System.out.println("Path added...");
			}

			int finishX = 0;
			int finishY = 0;
			int startX = 0;
			int startY = 0;

			Node endNode = pathList.get(pathList.size()-1);
			Node startNode = pathList.get(0);
			//javafx.scene.text.Text startName = new javafx.scene.text.Text(startNode.getLongName());
			//javafx.scene.text.Text endName = new javafx.scene.text.Text(endNode.getLongName());
			destination.setVisible(true);
			destinationText.setVisible(true);
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
				destination.setTranslateX(finishX -29);
				destination.setTranslateY(finishY -52);
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

			/*
			ScaleTransition scaleTransitionCircle = new ScaleTransition(Duration.millis(1000), startCircle);
			scaleTransitionCircle.setToX(1.0f);
			scaleTransitionCircle.setToY(1.0f);
			scaleTransitionCircle.setToX(2f);
			scaleTransitionCircle.setToY(2f);
			scaleTransitionCircle.setCycleCount(Timeline.INDEFINITE);
			scaleTransitionCircle.setAutoReverse(true);

			FadeTransition fadeTransitionCircle = new FadeTransition(Duration.millis(1000), startCircle);
			fadeTransitionCircle.setFromValue(1);
			fadeTransitionCircle.setToValue(0);
			fadeTransitionCircle.setAutoReverse(true);
			fadeTransitionCircle.setCycleCount(Timeline.INDEFINITE);

			ParallelTransition parallelTransition = new ParallelTransition();
			parallelTransition.getChildren().add(scaleTransition);
			parallelTransition.getChildren().add(scaleTransitionCircle);
			parallelTransition.getChildren().add(fadeTransitionCircle); */

			//nameList.add(startName);
			//nameList.add(endName);

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
			if(!pathRunning) {
				pathRunning = true;
				ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(2000), destination);
				scaleTransition.setToX(1.3f);
				scaleTransition.setToY(1.3f);
				scaleTransition.setToX(1.2f);
				scaleTransition.setToY(1.2f);
				scaleTransition.setCycleCount(Timeline.INDEFINITE);
				scaleTransition.setAutoReverse(true);
				scaleTransition.play();
			}
		}
	}

	public void drawPath(ActionEvent event) {

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

            directions = FXCollections.observableArrayList(pathfinderUtil.angleToText((LinkedList) pathList));
			// calcDistance function now converts to feet
            double dist = CalcDistance.calcDistance(pathList)*OptionSingleton.getOptionPicker().feetPerPixel;
			directions.add(String.format("TOTAL DISTANCE: %.1f ft", dist));
            directions.add(String.format("ETA: %.1f s", dist/OptionSingleton.getOptionPicker().walkSpeedFt));
            lstDirections.setItems(directions);

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

			animationCircle.setVisible(true);
			//overMap.getChildren().add(animationCircle);

			SequentialTransition sequentialTransition = new SequentialTransition();
			animationCircle.setVisible(true);
			double wantedVelocity = .03;

			for(int i = 1; i<pathList.size(); i++) {
				TranslateTransition translateTransition = new TranslateTransition(Duration.millis(calcTime(pathList, i, wantedVelocity)), animationCircle);
				translateTransition.setFromX(pathList.get(i-1).getXCoord()+11);
				translateTransition.setFromY(pathList.get(i-1).getYCoord()+11);
				translateTransition.setToX(pathList.get(i).getXCoord()+11);
				translateTransition.setToY(pathList.get(i).getYCoord()+11);
				translateTransition.setCycleCount(1);
				translateTransition.setAutoReverse(true);
				sequentialTransition.getChildren().add(translateTransition);
			}
			sequentialTransition.setCycleCount(Timeline.INDEFINITE);
			sequentialTransition.play();

			//printPoints(comChangeFloor.getValue(), dimension);

			// Update Directions

		}
	}

	private double getDistance(Node a, Node b) {
		double num;
		if(Math.abs((num = a.getXCoord() - b.getXCoord())) > 0) {
			return Math.abs(num);
		} else if(Math.abs((num = a.getYCoord()-b.getYCoord())) > 0)
		{
			return Math.abs(num);
		}
		return 0;
	}

	private double calcTime(List<Node> pathList, int i, double wantedVelocity) {
		double d = getDistance(pathList.get(i-1), pathList.get(i));
		return Math.abs(d/wantedVelocity);
	}

	private void clearPath() {
		currPath.getElements().clear();
		currPath.getElements().add(new MoveTo(-100, -100));
		currPath.getElements().add(new LineTo(5000, -100));
		currPath.getElements().add(new LineTo(5000, 5000));
		currPath.getElements().add(new LineTo(-100, 5000));
		pathL2.getElements().clear();
		pathL2.getElements().add(new MoveTo(-100, -100));
		pathL2.getElements().add(new LineTo(5000, -100));
		pathL2.getElements().add(new LineTo(5000, 5000));
		pathL2.getElements().add(new LineTo(-100, 5000));
		pathL1.getElements().clear();
		pathL1.getElements().add(new MoveTo(-100, -100));
		pathL1.getElements().add(new LineTo(5000, -100));
		pathL1.getElements().add(new LineTo(5000, 5000));
		pathL1.getElements().add(new LineTo(-100, 5000));
		path1.getElements().clear();
		path1.getElements().add(new MoveTo(-100, -100));
		path1.getElements().add(new LineTo(5000, -100));
		path1.getElements().add(new LineTo(5000, 5000));
		path1.getElements().add(new LineTo(-100, 5000));
		path2.getElements().clear();
		path2.getElements().add(new MoveTo(-100, -100));
		path2.getElements().add(new LineTo(5000, -100));
		path2.getElements().add(new LineTo(5000, 5000));
		path2.getElements().add(new LineTo(-100, 5000));
		path3.getElements().clear();
		path3.getElements().add(new MoveTo(-100, -100));
		path3.getElements().add(new LineTo(5000, -100));
		path3.getElements().add(new LineTo(5000, 5000));
		path3.getElements().add(new LineTo(-100, 5000));
	}

	private void startCircleClicked(MouseEvent event) {
		System.out.println("Recognized a click");

		if(!startFloor.equals(currentFloor)) {
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
		if(!endFloor.equals(currentFloor)) {
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
		for(Circle c: circleList) {
			paneMap.getChildren().remove(c);
		}
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

				Circle circle = new Circle(x, y, 10);
				circle.setId(currNode.getShortName());
				circle.setFill(Color.WHITE);
				circle.setStroke(Color.BLACK);
				circle.setStrokeWidth(4);

				if (isStart)
					circle.setOnMouseClicked(this::chooseStartNode);
				else
					circle.setOnMouseClicked(this::chooseEndNode);

				circleList.add(circle);
				circle.setOnMouseEntered(this::printName);
				circle.setOnMouseExited(this::removeName);
				paneMap.getChildren().add(circle);
			}
			i++;
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

	// The zooming is a bit weird... should be looked into more in the future
	public void zoomIn(ActionEvent event) {
		if(!(overMap.getScaleX() > 1.2) || !(overMap.getScaleY() > 1.2)) {
			overMap.setScaleX(overMap.getScaleX() + .1);
			overMap.setScaleY(overMap.getScaleY() + .1);
		}
	}

	public void zoomOut(ActionEvent event) {
		if(!(overMap.getScaleX() < .70) || !(overMap.getScaleY() < .70)) {
			overMap.setScaleX(overMap.getScaleX() - .1);
			overMap.setScaleY(overMap.getScaleY() - .1);
		}
	}

	public void rotateRight(ActionEvent event) {
		overMap.setRotate(overMap.getRotate() - 30);
		double currentRotation = imgCompass.getRotate();
		imgCompass.setRotate(currentRotation - 30);
	}

	public void rotateLeft(ActionEvent event) {
		overMap.setRotate(overMap.getRotate() + 30);
		double currentRotation = imgCompass.getRotate();
		imgCompass.setRotate(currentRotation + 30);

	}

	public void resetRotate(ActionEvent event) {
		overMap.setRotate(0);
		imgCompass.setRotate(0);
	}

	//-----------------------------------------------------------------------------------------------------------------
	//
	//                                           Names over nodes
	//
	//-----------------------------------------------------------------------------------------------------------------
	//private FadeTransition nameTransition = new FadeTransition(Duration.millis(400), currName);

	private void printName(MouseEvent mouseEvent) {
		Circle currCircle = (Circle)mouseEvent.getTarget();
		nodeFloor.setText(currentFloor);
		lblNode.setText("  " + currCircle.getId());
		nodePane.setVisible(true);
		nodePane.setLayoutX(currCircle.getCenterX());
		nodePane.setLayoutY(currCircle.getCenterY());
		//name.setRotate(-overMap.getRotate());
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

	public void previousStep(ActionEvent event) {
		System.out.println("selected previous step");
	}

	public void nextStep(ActionEvent event) {
		System.out.println("selected next step");
	}


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
		if(floor.equals("L2"))
			changeFloorL2(null);
		if(floor.equals("L1"))
			changeFloorL1(null);
		if(floor.equals("1"))
			changeFloor1(null);
		if(floor.equals("2"))
			changeFloor2(null);
		if(floor.equals("3"))
			changeFloor3(null);
	}

	public void changeFloorL2(ActionEvent event) {
		if (currentDimension.equals("3-D")) {
			floor3DMapLoader("L2");
		} else {
			floor2DMapLoader("L2");
		}

		currentFloor = "L2";
		circleList.clear();
		printKiosk();
		cancel(null);
		btnL2.setLayoutX(20);
		btnL1.setLayoutX(0);
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
		circleList.clear();
		printKiosk();
		cancel(null);
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
		circleList.clear();
		printKiosk();
		cancel(null);
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
		circleList.clear();
		printKiosk();
		cancel(null);
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
		circleList.clear();
		printKiosk();
		cancel(null);

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


	public String convertType(String type) {

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
	    lstStartDirectory.setItems(FXCollections.observableList(DataModelI.getInstance().getNamesByBuildingFloorType(comBuildingStart.getValue(),comFloorStart.getValue(),convertType(comTypeStart.getValue()))));
	}

	public void filterEnd(ActionEvent event) {
		lstEndDirectory.setItems(FXCollections.observableList(DataModelI.getInstance().getNamesByBuildingFloorType(comBuildingEnd.getValue(),comFloorEnd.getValue(),convertType(comTypeEnd.getValue()))));
	}

	public void goHome(ActionEvent event) {

	}
}
