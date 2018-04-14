package com.manlyminotaurs.viewControllers;

//import com.manlyminotaurs.core.KioskInfo;
import com.jfoenix.controls.*;
import com.manlyminotaurs.core.KioskInfo;
import com.manlyminotaurs.core.Main;
import com.manlyminotaurs.databases.DataModelI;
import com.manlyminotaurs.nodes.INode;
import com.manlyminotaurs.nodes.Node;
import com.manlyminotaurs.nodes.Room;
import com.manlyminotaurs.pathfinding.*;
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

	//Nested Private Singleton
	private static class Singleton {
		private static Singleton instance = null;
		PathfindingContext pathfindingContext = new PathfindingContext();
		Boolean handicap;
		private Singleton() {

		}
		private static class SingletonHolder {
			private static Singleton MapController = new Singleton();

		}

		public static Singleton getInstance() {
			return homeController.Singleton.SingletonHolder.MapController;
		}
	}

	//-----------------------------------------------------------------------------------------------------------------
	//
	//                                           Create objects
	//
	//-----------------------------------------------------------------------------------------------------------------
	final static ObservableList<String> floors = FXCollections.observableArrayList("L2", "L1", "1", "2", "3");
	final static ObservableList<String> mapFloors = FXCollections.observableArrayList("FLOOR: L2", "FLOOR: L1", "FLOOR: 1", "FLOOR: 2", "FLOOR: 3");
	final static ObservableList<String> empty = FXCollections.observableArrayList();
	final ObservableList<String> buildings = FXCollections.observableArrayList(DataModelI.getInstance().getBuildingsFromList());
	final ObservableList<String> types = FXCollections.observableArrayList(DataModelI.getInstance().getTypesFromList());
	final int MAPX2D = 5000;
	final int MAPY2D = 3400;
	Parent adminRequest;
	Parent staffRequest;
	Circle finishCircle = new Circle();
	Circle startCircle = new Circle();
	Circle finishCircle2 = new Circle();
	List<Node> nodeList = DataModelI.getInstance().retrieveNodes();
	List<Node> pathList = new ArrayList<>();
	LinkedList<Node> listForQR = new LinkedList<Node>();
	Image imageQRCode;
	String startFloor = "";
	String endFloor = "";
	List<Circle> circleList = new ArrayList<>();
	boolean isStart = true;
	//Map<Integer, Map<Integer, Node>> nodeMap = new HashMap<>(); was trying to speed up start and end choose time

	//-----------------------------------------------------------------------------------------------------------------
	//
	//                                           Path-finding
	//
	//-----------------------------------------------------------------------------------------------------------------
	@FXML
	VBox panePathfinding;

	@FXML
	JFXToggleButton tglHandicap;

	@FXML
	Label lblHandicap;

	@FXML
	JFXToggleButton tglMap;

	@FXML
	Label lblMap;

	@FXML
	JFXButton btnStart;

	@FXML
	Label lblBuildingStart;

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
	ComboBox<String> comLocationStart;

	@FXML
	Button btnEnd;

	@FXML
	Label lblBuildingEnd;

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

	@FXML
	Path pathOnDifferentFloor;

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
			tglMap.setSelected(false);
			tglMap.setText("2-D");

			comChangeFloor.getSelectionModel().select(2);
			floor2DMapLoader("1");
			//staffRequest = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/adminRequestDashBoard.fxml"));
			//adminRequest = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/userRequestDashBoard.fxml"));

			scrollPaneMap.setHvalue(.35);
			scrollPaneMap.setVvalue(.2);
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
		tglMap.setSelected(false);
		tglMap.setText("2-D");

		comChangeFloor.getSelectionModel().select(2);
		floor2DMapLoader("1");
		setStrategy();
		//createMap();
	}

	public void setStrategy(){
		if (Main.pathStrategy.equals("A*")) {
			Singleton.getInstance().pathfindingContext.strategy = new AStarStrategyI();
		}
		if (Main.pathStrategy.equals("BFS")) {
			Singleton.getInstance().pathfindingContext.strategy = new BreadthFirstStrategyI();
		}
		if (Main.pathStrategy.equals("DFS")) {
			Singleton.getInstance().pathfindingContext.strategy = new DepthFirstStrategyI();
		}
	}

	public void toggleHandicap(ActionEvent event) {

		if (tglHandicap.isSelected()) {
			Singleton.getInstance().handicap = true;
			// Switch on
			tglHandicap.setText("ON");

		} else {

			// Switch off
			Singleton.getInstance().handicap = false;
			tglHandicap.setText("OFF");
		}
	}

	public void toggleMap(ActionEvent event) {
		clearPoints();
		circleList.clear();
		printKiosk();
		btnStart.setDisable(false);
		btnEnd.setDisable(false);
		isStart = true;
		comBuildingStart.setDisable(false);
		comBuildingEnd.setDisable(false);
		comFloorStart.setDisable(false);
		comFloorEnd.setDisable(false);
		comTypeStart.setDisable(false);
		comTypeEnd.setDisable(false);
		comLocationStart.setDisable(false);
		comLocationEnd.setDisable(false);
		if (tglMap.isSelected()) {

			// Switch 3-D
			tglMap.setText("3-D");
			stackPaneMap.setPrefHeight(2774);
			stackPaneMap.setPrefWidth(5000);
			mapImg.setFitHeight(2774);
			mapImg.setFitWidth(5000);
			paneMap.setPrefHeight(2774);
			paneMap.setPrefWidth(5000);
			floor3DMapLoader(comChangeFloor.getValue());
		} else {

			// Switch 2-D
			tglMap.setText("2-D");
			stackPaneMap.setPrefHeight(3400);
			stackPaneMap.setPrefWidth(5000);
			mapImg.setFitHeight(3400);
			mapImg.setFitWidth(5000);
			paneMap.setPrefHeight(3400);
			paneMap.setPrefWidth(5000);
			floor2DMapLoader(comChangeFloor.getValue());
		}

	}

	public void printKiosk() {

		Circle kioskOutline = new Circle();
		Circle kiosk = new Circle();
		Circle kioskRemove = new Circle();
		Circle outlineRemove = new Circle();

		if(tglMap.isSelected()) {
			kioskOutline = new Circle(KioskInfo.myLocation.getXCoord3D(), KioskInfo.myLocation.getYCoord3D(), 15);
			kiosk = new Circle(KioskInfo.myLocation.getXCoord3D(), KioskInfo.myLocation.getYCoord3D(), 13);
			/*kioskRemove = new Circle(KioskInfo.myLocation.getXCoord(), KioskInfo.myLocation.getYCoord(), 15);
			outlineRemove = new Circle(KioskInfo.myLocation.getXCoord(), KioskInfo.myLocation.getYCoord(), 13);
			paneMap.getChildren().remove(kioskRemove);
			paneMap.getChildren().remove(outlineRemove);*/
			clearPoints();
		} else {
			kioskOutline = new Circle(KioskInfo.myLocation.getXCoord(), KioskInfo.myLocation.getYCoord(), 15);
			kiosk = new Circle(KioskInfo.myLocation.getXCoord(), KioskInfo.myLocation.getYCoord(), 13);
			/*kioskRemove = new Circle(KioskInfo.myLocation.getXCoord3D(), KioskInfo.myLocation.getYCoord3D(), 15);
			outlineRemove = new Circle(KioskInfo.myLocation.getXCoord3D(), KioskInfo.myLocation.getYCoord3D(), 13);
			paneMap.getChildren().remove(kioskRemove);
			paneMap.getChildren().remove(outlineRemove);*/
			clearPoints();
		}

		circleList.add(kioskOutline);
		circleList.add(kiosk);
		if(comChangeFloor.getValue().equals("FLOOR: 1") || comChangeFloor.getValue().equals("1")) {
			kiosk.setFill(Color.BLUE);
			kiosk.setFill(Color.RED);
		} else {
			kiosk.setFill(Color.GRAY);
			kioskOutline.setFill(Color.BLACK);
		}


		paneMap.getChildren().add(kioskOutline);
		paneMap.getChildren().add(kiosk);
	}

	public void goToKiosk() {
		if(tglMap.isSelected()) {
			scrollPaneMap.setVvalue((double) KioskInfo.myLocation.getYCoord() / 2774.0);
			scrollPaneMap.setHvalue((double) KioskInfo.myLocation.getXCoord() / 5000.0);
		} else {
			scrollPaneMap.setVvalue((double) KioskInfo.myLocation.getYCoord() / 3400.0);
			scrollPaneMap.setHvalue((double) KioskInfo.myLocation.getXCoord() / 5000.0);
		}
	}

	private void setKiosk() {
		initializeBuildingStart(null);
		initializeFloorStart(null);
		initializeTypeStart(null);
		setStartLocation(null);
		comBuildingStart.getSelectionModel().select(KioskInfo.myLocation.getBuilding());
		comFloorStart.getSelectionModel().select(KioskInfo.myLocation.getFloor());
		comTypeStart.getSelectionModel().select(KioskInfo.myLocation.getNodeType());
		comLocationStart.getSelectionModel().select(KioskInfo.myLocation.getLongName());
		scrollPaneMap.setVvalue((double) KioskInfo.myLocation.getYCoord() / 3400.0);
		scrollPaneMap.setHvalue((double) KioskInfo.myLocation.getXCoord() / 5000.0);
		lblStartLocation.setText(KioskInfo.myLocation.getLongName());
		printKiosk();
	}

	public void printNodesOnFloorStart(MouseEvent event) {
		btnStart.setDisable(true);
		btnEnd.setDisable(true);
		isStart = true;
		comBuildingStart.setDisable(true);
		comBuildingEnd.setDisable(true);
		comFloorStart.setDisable(true);
		comFloorEnd.setDisable(true);
		comTypeStart.setDisable(true);
		comTypeEnd.setDisable(true);
		comLocationStart.setDisable(true);
		comLocationEnd.setDisable(true);
		clearPoints();
		//printKiosk();
		circleList.clear();
		if (tglMap.isSelected())
			printPoints(returnFloorName(comChangeFloor.getValue()), "3-D");
		else
			printPoints(returnFloorName(comChangeFloor.getValue()), "2-D");

	}

	public void printNodesOnFloorEnd(MouseEvent event) {
		btnStart.setDisable(true);
		btnEnd.setDisable(true);
		isStart = false;
		comBuildingStart.setDisable(true);
		comBuildingEnd.setDisable(true);
		comFloorStart.setDisable(true);
		comFloorEnd.setDisable(true);
		comTypeStart.setDisable(true);
		comTypeEnd.setDisable(true);
		comLocationStart.setDisable(true);
		comLocationEnd.setDisable(true);
		clearPoints();
		circleList.clear();
		if (tglMap.isSelected())
			printPoints(returnFloorName(comChangeFloor.getValue()), "3-D");
		else
			printPoints(returnFloorName(comChangeFloor.getValue()), "2-D");

	}

	public void chooseStartNode(MouseEvent event) {
		Circle circle = (Circle)event.getTarget();
		if(!tglMap.isSelected()) {
			for (Node node : nodeList) {
				if (node.getXCoord() == circle.getCenterX()) {
					if (node.getYCoord() == circle.getCenterY()) {
						System.out.println("Click recognized");
						comBuildingStart.getSelectionModel().select(node.getBuilding());
						comFloorStart.getSelectionModel().select(node.getFloor());
						comTypeStart.getSelectionModel().select(node.getNodeType());
						comLocationStart.getSelectionModel().select(node.getLongName());
						clearPoints();
						printKiosk();
						btnStart.setDisable(false);
						btnEnd.setDisable(false);
						comBuildingStart.setDisable(false);
						comBuildingEnd.setDisable(false);
						comFloorStart.setDisable(false);
						comFloorEnd.setDisable(false);
						comTypeStart.setDisable(false);
						comTypeEnd.setDisable(false);
						comLocationStart.setDisable(false);
						comLocationEnd.setDisable(false);
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
						comTypeStart.getSelectionModel().select(node.getNodeType());
						comLocationStart.getSelectionModel().select(node.getLongName());
						clearPoints();
						printKiosk();
						btnStart.setDisable(false);
						btnEnd.setDisable(false);
						comBuildingStart.setDisable(false);
						comBuildingEnd.setDisable(false);
						comFloorStart.setDisable(false);
						comFloorEnd.setDisable(false);
						comTypeStart.setDisable(false);
						comTypeEnd.setDisable(false);
						comLocationStart.setDisable(false);
						comLocationEnd.setDisable(false);
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
		if(!tglMap.isSelected()) {
			for (Node node : nodeList) {
				if (node.getXCoord() == circle.getCenterX()) {
					if (node.getYCoord() == circle.getCenterY()) {
						System.out.println("Click recognized");
						comBuildingEnd.getSelectionModel().select(node.getBuilding());
						comFloorEnd.getSelectionModel().select(node.getFloor());
						comTypeEnd.getSelectionModel().select(node.getNodeType());
						comLocationEnd.getSelectionModel().select(node.getLongName());
						clearPoints();
						printKiosk();
						btnStart.setDisable(false);
						btnEnd.setDisable(false);
						comBuildingStart.setDisable(false);
						comBuildingEnd.setDisable(false);
						comFloorStart.setDisable(false);
						comFloorEnd.setDisable(false);
						comTypeStart.setDisable(false);
						comTypeEnd.setDisable(false);
						comLocationStart.setDisable(false);
						comLocationEnd.setDisable(false);
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
						comTypeEnd.getSelectionModel().select(node.getNodeType());
						comLocationEnd.getSelectionModel().select(node.getLongName());
						clearPoints();
						printKiosk();
						btnStart.setDisable(false);
						btnEnd.setDisable(false);
						comBuildingStart.setDisable(false);
						comBuildingEnd.setDisable(false);
						comFloorStart.setDisable(false);
						comFloorEnd.setDisable(false);
						comTypeStart.setDisable(false);
						comTypeEnd.setDisable(false);
						comLocationStart.setDisable(false);
						comLocationEnd.setDisable(false);
						break;
					}
				}
			}
			System.out.println("Node not found");
		}
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
		comLocationStart.setItems(FXCollections.observableArrayList(DataModelI.getInstance().getLongNameByBuildingTypeFloor(comBuildingStart.getValue(), comTypeStart.getValue(), comFloorStart.getValue())));


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
		comLocationEnd.setItems(FXCollections.observableArrayList(DataModelI.getInstance().getLongNameByBuildingTypeFloor(comBuildingEnd.getValue(), comTypeEnd.getValue(), comFloorEnd.getValue())));

		// Able Objects
		comLocationEnd.setDisable(false);

		// Clear Past Selection
		comLocationEnd.getSelectionModel().clearSelection();

		// Set End Location Label to Default
		lblEndLocation.setText("END LOCATION");

	}

	public void changeFloorMap(ActionEvent event) {
		clearPoints();
		circleList.clear();
		printKiosk();
		btnStart.setDisable(false);
		btnEnd.setDisable(false);
		isStart = true;
		comBuildingStart.setDisable(false);
		comBuildingEnd.setDisable(false);
		comFloorStart.setDisable(false);
		comFloorEnd.setDisable(false);
		comTypeStart.setDisable(false);
		comTypeEnd.setDisable(false);
		comLocationStart.setDisable(false);
		comLocationEnd.setDisable(false);
		if (tglMap.isSelected()) { // 3-D

			stackPaneMap.setPrefHeight(2774);
			stackPaneMap.setPrefWidth(5000);
			mapImg.setFitHeight(2772);
			mapImg.setFitWidth(5000);
			paneMap.setPrefHeight(2774);
			paneMap.setPrefWidth(5000);
			floor3DMapLoader(comChangeFloor.getValue());

		} else { // 2-D

			// !!!

			stackPaneMap.setPrefHeight(3400);
			stackPaneMap.setPrefWidth(5000);
			mapImg.setFitHeight(3400);
			mapImg.setFitWidth(5000);
			paneMap.setPrefHeight(3400);
			paneMap.setPrefWidth(5000);
			floor2DMapLoader(comChangeFloor.getValue());
		}

	}

	public void setStartLocation(ActionEvent event) {
		lblStartLocation.setText(comLocationStart.getValue());
	}

	public void setEndLocation(ActionEvent event) {
		lblEndLocation.setText(comLocationEnd.getValue());
	}

	//-----------------------------------------------------------------------------------------------------------------
	//
	//                                           Directions
	//
	//-----------------------------------------------------------------------------------------------------------------
	@FXML
	VBox paneDirections;

	@FXML
	Label lblStartLocation1;

	@FXML
	Label lblEndLocation1;

	@FXML
	JFXButton btnOpenSend;

	@FXML
	JFXListView<String> lstDirections;

	@FXML
	Button btnRestart;

	@FXML
	Pane paneSend;

	@FXML
	JFXButton btnCloseSend;

	@FXML
	JFXTextField txtEmail;

	@FXML
	JFXTextField txtPhone;

	@FXML
	JFXTextField txtFax;

	@FXML
	Button btnEmail;

	@FXML
	Button btnPhone;

	@FXML
	Button btnFax;

	@FXML
	Label lblEmailMessage;

	@FXML
	Label lblPhoneMessage;

	@FXML
	Label lblFaxMessage;

	@FXML
	Label lblPin;

	public void openSendPanel(ActionEvent event) {

		// Generate QR Code

		// Load QR Code into panel

		// Show QR code
		paneSend.setVisible(true);

		// Disable Everything Else
		btnRestart.setDisable(true);
		btnOpenLogin.setDisable(true);
		btnLogin.setDisable(true);
		btnCloseLogin.setDisable(true);
		btnHelp.setDisable(true);
		btnCloseHelp.setDisable(true);
		btnQuickDirections.setDisable(true);
//		btnQuickCafe.setDisable(true);
		btnQuickBathroom.setDisable(true);
//		btnQuickCoffee.setDisable(true);
		//btnQuickShop.setDisable(true);
		comChangeFloor.setDisable(true);
		btnOpenSend.setDisable(true);
		txtPassword.setDisable(true);
		txtUsername.setDisable(true);

		//new ProxyImage(imgQRCode, "CrunchifyQR.png").display2();
	}

	public void restartNavigation(ActionEvent event) {
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

		// Show pathfinding interface and hide directions interface
		panePathfinding.setVisible(true);
		paneDirections.setVisible(false);

		tglHandicap.setSelected(false);
		tglHandicap.setText("OFF");
		tglMap.setSelected(false);
		tglMap.setText("2-D");
		floor2DMapLoader("1");
		comChangeFloor.setValue("1");

		paneMap.getChildren().remove(startCircle);
		paneMap.getChildren().remove(finishCircle);
		paneMap.getChildren().remove(finishCircle2);

		if (paneHelp.isVisible()) {
			lblHelp1.setVisible(true);
			lblHelp2.setVisible(false);
		}

		setKiosk();

	}

	public void closeSendPanel(ActionEvent event) {

		// Hide QR code
		paneSend.setVisible(false);

		// Disable Everything Else
		btnRestart.setDisable(false);
		btnOpenLogin.setDisable(false);
		btnLogin.setDisable(false);
		btnCloseLogin.setDisable(false);
		btnHelp.setDisable(false);
		btnCloseHelp.setDisable(false);
		btnQuickDirections.setDisable(false);
		//btnQuickCafe.setDisable(false);
		btnQuickBathroom.setDisable(false);
		//btnQuickCoffee.setDisable(false);
		//btnQuickShop.setDisable(false);
		comChangeFloor.setDisable(false);
		btnOpenSend.setDisable(false);
		txtPassword.setDisable(false);
		txtUsername.setDisable(false);
	}

	public void sendDirectionsViaEmail(ActionEvent event) {

	}

	public void sendDirectionsViaPhone(ActionEvent event) {

	}

	public void sendDirectionsViaFax(ActionEvent event) {

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
	ImageView imgNavigation;

	//@FXML
	//Button btnQuickCafe;

	//@FXML
	//Button btnQuickCoffee;

	//@FXML
	//Button btnQuickShop;

	public void toggleQuickButtons(ActionEvent event) {

		if (btnQuickBathroom.isVisible() == true) {

			new ProxyImage(imgNavigation, "NearestIcon.png").displayIcon();

			btnQuickBathroom.setVisible(false);
			//btnQuickCafe.setVisible(false);
			//btnQuickCoffee.setVisible(false);
			//.setVisible(false);

		} else if (btnQuickBathroom.isVisible() == false) {

			new ProxyImage(imgNavigation,"BackIcon.png").displayIcon();

			btnQuickBathroom.setVisible(true);
			//btnQuickCafe.setVisible(true);
			//btnQuickCoffee.setVisible(true);
			//btnQuickShop.setVisible(true);

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
			path = pf.getPath(DataModelI.getInstance().getNodeByLongNameFromList(comLocationStart.getValue(), nodeList), bathroomNode, new ClosestStrategyI());
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
		ObservableList<String> directions = FXCollections.observableArrayList(pu.angleToText((LinkedList<Node>)path));
		lstDirections.setItems(directions);
		listForQR = (LinkedList<Node>)path;
		pu.generateQR(pu.angleToText((LinkedList<Node>)path));
		// new ProxyImage(imgQRCode,"CrunchifyQR.png").display2();
		// Draw path code
		if (tglHandicap.isSelected()) {
			// use elevator
			if (tglMap.isSelected()) {
				// use 3-D
				printNodePath(path, startFloor, "3-D");
				floor3DMapLoader(startFloor);
				comChangeFloor.setValue("FLOOR: " + startFloor);
			} else {
				// use 2-D
				printNodePath(path, startFloor, "2-D");
				floor2DMapLoader(startFloor);
				comChangeFloor.setValue("FLOOR: " + startFloor);
			}
		} else {
			// use stairs
			if (tglMap.isSelected()) {
				// use 3-D
				System.out.println("using 3d stairs");
				printNodePath(path, startFloor, "3-D");
				floor3DMapLoader(startFloor);
				comChangeFloor.setValue("FLOOR: " + startFloor);
			} else {
				// use 2-D
				printNodePath(path, startFloor, "2-D");
				floor2DMapLoader(startFloor);
				comChangeFloor.setValue("FLOOR: " + startFloor);
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

	//-----------------------------------------------------------------------------------------------------------------
	//
	//                                           Help
	//
	//-----------------------------------------------------------------------------------------------------------------
	@FXML
	JFXButton btnHelp;

	@FXML
	Pane paneHelp;

	@FXML
	JFXButton btnCloseHelp;

	@FXML
	Label lblHelp1;

	@FXML
	Label lblHelp2;

	@FXML
	Label lblHelp;


	public void openHelpPanel(ActionEvent event) {

		if (panePathfinding.isVisible()) {
			paneHelp.setVisible(true);
			lblHelp1.setVisible(true);
			lblHelp2.setVisible(false);
		} else if (paneDirections.isVisible()) {
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

	//-----------------------------------------------------------------------------------------------------------------
	//
	//                                           Login
	//
	//-----------------------------------------------------------------------------------------------------------------
	@FXML
	Pane paneLogin;

	@FXML
	JFXButton btnOpenLogin;

	@FXML
	JFXButton btnCloseLogin;

	@FXML
	JFXButton btnLogin;

	@FXML
	JFXTextField txtUsername;

	@FXML
	JFXPasswordField txtPassword;

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

	public void login(ActionEvent event) throws Exception {

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
				//btnQuickShop.setVisible(false);
				//btnQuickCoffee.setVisible(false);
				//btnQuickCafe.setVisible(false);
				tglHandicap.setSelected(false);
				tglHandicap.setText("OFF");
				tglMap.setSelected(false);
				tglMap.setText("2-D");
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

				KioskInfo.currentUserID = DataModelI.getInstance().getIDByUserPassword(userName, password);

				Stage stage;
				//get reference to the button's stage
				stage = (Stage) btnLogin.getScene().getWindow();
				//load up Home FXML document
				if (DataModelI.getInstance().getUserByID(KioskInfo.currentUserID).isType("admin")) {
					staffRequest = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/adminRequestDashBoard.fxml"));
				}else{
					staffRequest = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/userRequestDashBoard.fxml"));
				}

				KioskInfo.currentUserID = DataModelI.getInstance().getIDByUserPassword(userName , password);

				//create a new scene with root and set the stage
				Scene scene = new Scene(staffRequest);
				stage.setScene(scene);
				stage.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {

			// print message
			System.out.println("Wrong username and password!");

		}
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
	}

	//-----------------------------------------------------------------------------------------------------------------
	//
	//                                           Drawing on map
	//
	//-----------------------------------------------------------------------------------------------------------------

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
						addPath(this.path, dimension, startNode, endNode, moveTo, lineTo);
					} else {
						// add the path
						addPath(pathOnDifferentFloor, dimension, startNode, endNode, moveTo, lineTo);
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

			if (dimension.equals("2-D")) {
				finishX = endNode.getXCoord();
				finishY = endNode.getYCoord();
				startX = startNode.getXCoord();
				startY = startNode.getYCoord();
			} else if (dimension.equals("3-D")) {
				finishX = endNode.getXCoord3D();
				finishY = endNode.getYCoord3D();
				startX = startNode.getXCoord3D();
				startY = startNode.getYCoord3D();
			} else {
				System.out.println("Invalid dimension");
			}

			// Draw Start Circle
			startCircle.setRadius(10);
			startCircle.setFill(Color.NAVY);
			startCircle.setVisible(true);
			startCircle.setCenterX(startX);
			startCircle.setCenterY(startY);

			// Set on mouse clicked to switch between floors
			startFloor = startNode.getFloor();
			startCircle.setOnMouseClicked(this::startCircleClicked);

			if (!startNode.getFloor().equals(floor)) {
				startCircle.setFill(Color.GRAY);
				startCircle.setOpacity(25);
			}

			// Draw finish circle-outside
			finishCircle.setRadius(15);
			finishCircle.setFill(Color.NAVY);
			finishCircle.setOpacity(50);
			finishCircle.setVisible(true);
			finishCircle.setCenterX(finishX);
			finishCircle.setCenterY(finishY);
			// Draw finish circle-inside
			finishCircle2.setRadius(11);
			finishCircle2.setFill(Color.WHITE);
			finishCircle2.setOpacity(100);
			finishCircle2.setVisible(true);
			finishCircle2.setCenterX(finishX);
			finishCircle2.setCenterY(finishY);

			// Set on mouse clicked to switch between floors
			endFloor = endNode.getFloor();
			finishCircle.setOnMouseClicked(this::endCircleClicked);
			finishCircle2.setOnMouseClicked(this::endCircleClicked);

			if (!endNode.getFloor().equals(floor)) {
				finishCircle.setFill(Color.GRAY);
				finishCircle2.setFill(Color.WHITE);
				finishCircle.setOpacity(25);
				finishCircle2.setOpacity(25);
			}

			// adds circles to map
			paneMap.getChildren().remove(finishCircle);
			paneMap.getChildren().remove(finishCircle2);
			paneMap.getChildren().remove(startCircle);
			paneMap.getChildren().add(finishCircle);
			paneMap.getChildren().add(finishCircle2);
			paneMap.getChildren().add(startCircle);
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


			PathfinderUtil pathfinderUtil = new PathfinderUtil();

			//List<Node> nodeList = new ArrayList<>();
			//LinkedList<Node> pathList = new LinkedList<>();
			nodeList = DataModelI.getInstance().retrieveNodes();

			try {
				pathList = Singleton.getInstance().pathfindingContext.getPath(DataModelI.getInstance().getNodeByLongNameFromList(lblStartLocation.getText(), nodeList), DataModelI.getInstance().getNodeByLongNameFromList(lblEndLocation.getText(), nodeList), new AStarStrategyI());

			} catch (PathNotFoundException e) {
				e.printStackTrace();
			}

			ObservableList<String> directions = FXCollections.observableArrayList(pathfinderUtil.angleToText((LinkedList) pathList));
			lstDirections.setItems(directions);
			listForQR = (LinkedList) pathList;
			pathfinderUtil.generateQR(pathfinderUtil.angleToText((LinkedList) pathList));
			// new ProxyImage(imgQRCode,"CrunchifyQR.png").display2();

			// Draw path code

			if (tglHandicap.isSelected()) {
				// use elevator


				if (tglMap.isSelected()) {
					// use 3-D
					printNodePath(pathList, startFloor, "3-D");
					floor3DMapLoader(startFloor);
					comChangeFloor.setValue("FLOOR: " + startFloor);

				} else {
					// use 2-D
					printNodePath(pathList, startFloor, "2-D");
					floor2DMapLoader(startFloor);
					comChangeFloor.setValue("FLOOR: " + startFloor);
				}
			} else {
				// use stairs
				if (tglMap.isSelected()) {
					// use 3-D
					System.out.println("using 3d stairs");
					printNodePath(pathList, startFloor, "3-D");
					floor3DMapLoader(startFloor);
					comChangeFloor.setValue("FLOOR: " + startFloor);
				} else {
					// use 2-D
					printNodePath(pathList, startFloor, "2-D");
					floor2DMapLoader(startFloor);
					comChangeFloor.setValue("FLOOR: " + startFloor);

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

	private void clearPath() {
		path.getElements().clear();
		path.getElements().add(new MoveTo(-100, -100));
		path.getElements().add(new LineTo(5000, -100));
		path.getElements().add(new LineTo(5000, 5000));
		path.getElements().add(new LineTo(-100, 5000));
		pathOnDifferentFloor.getElements().clear();
		pathOnDifferentFloor.getElements().add(new MoveTo(-100, -100));
		pathOnDifferentFloor.getElements().add(new LineTo(5000, -100));
		pathOnDifferentFloor.getElements().add(new LineTo(5000, 5000));
		pathOnDifferentFloor.getElements().add(new LineTo(-100, 5000));
	}

	private void startCircleClicked(MouseEvent event) {
		System.out.println("Recognized a click");
		if (tglMap.isSelected()) { // 3-D

			stackPaneMap.setPrefHeight(2774);
			stackPaneMap.setPrefWidth(5000);
			mapImg.setFitHeight(2772);
			mapImg.setFitWidth(5000);
			paneMap.setPrefHeight(2774);
			paneMap.setPrefWidth(5000);
			floor3DMapLoader(startFloor);

		} else { // 2-D

			// !!!

			stackPaneMap.setPrefHeight(3400);
			stackPaneMap.setPrefWidth(5000);
			mapImg.setFitHeight(3400);
			mapImg.setFitWidth(5000);
			paneMap.setPrefHeight(3400);
			paneMap.setPrefWidth(5000);
			floor2DMapLoader(startFloor);
		}
	}

	private void endCircleClicked(MouseEvent event) {
		System.out.println("Recognized a click");
		if (tglMap.isSelected()) { // 3-D

			stackPaneMap.setPrefHeight(2774);
			stackPaneMap.setPrefWidth(5000);
			mapImg.setFitHeight(2772);
			mapImg.setFitWidth(5000);
			paneMap.setPrefHeight(2774);
			paneMap.setPrefWidth(5000);
			floor3DMapLoader(endFloor);

		} else { // 2-D

			// !!!

			stackPaneMap.setPrefHeight(3400);
			stackPaneMap.setPrefWidth(5000);
			mapImg.setFitHeight(3400);
			mapImg.setFitWidth(5000);
			paneMap.setPrefHeight(3400);
			paneMap.setPrefWidth(5000);
			floor2DMapLoader(endFloor);
		}
	}

	private void clearPoints() {
		for(Circle c: circleList) {
			paneMap.getChildren().remove(c);
		}

	}

	private void printPoints(String floor, String dimension) {
		// Connection for the database
		//List<Node> nodeList = DataModelI.getInstance().retrieveNodes();

		// map boundaries

		int i = 0;
		int x = 0;
		int y = 0;
		// Iterate through each node
		while (i < nodeList.size()) {
			Node currNode = nodeList.get(i);
			// If the node is on the correct floor
			if (currNode.getFloor().equals(floor) && !currNode.getNodeType().equals("HALL")) {

				if (dimension.equals("2-D")) {
					// Get x and y coords
					x = nodeList.get(i).getXCoord();
					y = nodeList.get(i).getYCoord();
				} else if (dimension.equals("3-D")) {
					x = nodeList.get(i).getXCoord3D();
					y = nodeList.get(i).getYCoord3D();
				} else {
					System.out.println("Invalid dimension");
				}

				Circle circle = new Circle(x, y, 8);
				circle.setStroke(Color.BLACK);
				circle.setStrokeWidth(3);
				circle.setId(currNode.getShortName());
				circle.setFill(Color.WHITE);

				if (isStart)
					circle.setOnMouseClicked(this::chooseStartNode);
				else
					circle.setOnMouseClicked(this::chooseEndNode);

				circleList.add(circle);
				paneMap.getChildren().add(circle);
			}
			i++;
		}
	}

	private void printName(MouseEvent mouseEvent) {
		Circle currCircle = (Circle)mouseEvent.getTarget();
		javafx.scene.text.Text name = new javafx.scene.text.Text(currCircle.getId());
		name.setLayoutX(currCircle.getCenterX() + 5 + currCircle.getId().length());
		name.setFont(new Font(30));
		name.setUnderline(true);
		name.setStrokeWidth(2);
		name.setStrokeType(StrokeType.OUTSIDE);
		name.setFill(Color.WHITE);
		name.setStroke(Color.BLACK);
		name.setLayoutY(currCircle.getCenterY());
		name.setVisible(false);
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


	// The zooming is a bit weird... should be looked into more in the future
	public void zoomIn(MouseEvent mouseEvent) {
		if(!(overMap.getScaleX() > 1.2) || !(overMap.getScaleY() > 1.2)) {
			overMap.setScaleX(overMap.getScaleX() + .1);
			overMap.setScaleY(overMap.getScaleY() + .1);
		}
	}

	public void zoomOut(MouseEvent mouseEvent) {
		if(!(overMap.getScaleX() < .70) || !(overMap.getScaleY() < .70)) {
			overMap.setScaleX(overMap.getScaleX() - .1);
			overMap.setScaleY(overMap.getScaleY() - .1);
		}
	}

	public void rotateLeft(MouseEvent mouseEvent) {
		overMap.setRotate(overMap.getRotate() - 20);
	}

	public void rotateRotate(MouseEvent mouseEvent) {
		overMap.setRotate(overMap.getRotate() + 20);
	}
}
