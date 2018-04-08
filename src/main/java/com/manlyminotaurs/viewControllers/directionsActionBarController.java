package com.manlyminotaurs.viewControllers;

import com.manlyminotaurs.core.Pathfinder;
import com.manlyminotaurs.core.PathfinderUtil;
import com.manlyminotaurs.databases.NodesDBUtil;
import com.manlyminotaurs.nodes.Node;
import com.manlyminotaurs.viewControllers.newViewControllers.landingController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.manlyminotaurs.core.Main;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class directionsActionBarController {

    public String building;
    public String type;
    String startLocation;
    String endLocation;
    Node startNode;
    Node endNode;
    NodesDBUtil node = new NodesDBUtil();
    Pathfinder pFind = new Pathfinder();
    PathfinderUtil pUtil = new PathfinderUtil();
    List<Node> path;
    LinkedList<Node> pathForTurns;
    ArrayList<String> textDirections;
    String directions = "";

    @FXML
    Label lblStart;

    @FXML
    Label lblEnd;

    @FXML
	ImageView qrCode;

    @FXML
	Text qrText;

    @FXML
	Label lblDirections;

    @FXML
    Button btnSelectDestination;

    @FXML
    Button btnSelectStart;

    @FXML
    Button btnPathfind;

    @FXML
    ListView<String> lststartBuilding;

    @FXML
    ListView<String> lststartType;

    @FXML
    ListView<String> lststartLocation;
    @FXML
    ListView<String> lstendBuilding;

    @FXML
    ListView<String> lstendType;

    @FXML
    ListView<String> lstendLocation;


    public void checkStartEnd(ActionEvent event) {

        if(lblEnd.getText().equals("End Location")){
        lblEnd.setText("Please Select Destination");
    }
        else if(lblStart.getText().equals("Start Location")){
            lblStart.setText("Please select a Start");
        }

        else{
        	path = pFind.find(startNode, endNode);
        	landingController.getInstance().printNodePath(path);
        	pathForTurns = (LinkedList)path;
        	textDirections = pUtil.angleToText(pathForTurns);
        	for(int i = 0; i<textDirections.size(); i++) {
        		directions = directions + textDirections.get(i) + "\n";
			}
			System.out.println(directions);
			lblDirections.setText(directions);
			pUtil.generateQR(textDirections);
			visTurnByTurn();

            switch(Main.getScreen()) {
                case 1:{
                    path = pFind.find(startNode, endNode);
                    landingController.getInstance().printNodePath(path);
                    pathForTurns = (LinkedList)path;
                    textDirections = pUtil.angleToText(pathForTurns);
                    for(int i = 0; i<textDirections.size(); i++) {
                        directions = directions + textDirections.get(i) + "\n";
                    }
                    System.out.println(directions);
                    visTurnByTurn();
                    lblDirections.setText(directions);
                    break;
                }
                case 3:{
                    path = pFind.find(startNode, endNode);
                    userHomeController.getInstance().printNodePath(path);
                    pathForTurns = (LinkedList)path;
                    textDirections = pUtil.angleToText(pathForTurns);
                    for(int i = 0; i<textDirections.size(); i++) {
                        directions = directions + textDirections.get(i) + "\n";
                    }
                    System.out.println(directions);
                    visTurnByTurn();
                    lblDirections.setText(directions);
                    break;
                }
            }

        }

    }

    public void visTurnByTurn () {
    	lblDirections.setVisible(true);
		//Image image = new Image("/Users/andrew/Documents/Soft Eng Rep/CS3733TeamM-Proj/src/main/resources/QR/CrunchifyQR.png");
		//qrCode.setImage(image);
    	//qrCode.setVisible(true);
    	//qrText.setVisible(true);
    	lststartLocation.setVisible(false);
    	lblStart.setVisible(false);
    	lblEnd.setVisible(false);
    	lststartLocation.setVisible(false);
    	lstendLocation.setVisible(false);
    	lststartType.setVisible(false);
    	lstendType.setVisible(false);
    	lststartBuilding.setVisible(false);
    	lstendBuilding.setVisible(false);
    	lstendLocation.setVisible(false);
    	btnPathfind.setVisible(false);
    	btnSelectStart.setVisible(false);
    	btnSelectDestination.setVisible(false);
	}

    public void getStartLocation(String startID){

        startNode = node.getNodeFromList(startID);
        startLocation = startNode.getLongName();
        lblStart.setText(startLocation);

    }
    public void getEndLocation(String endID){
        endNode = node.getNodeFromList(endID);
        endLocation = endNode.getLongName();
        lblEnd.setText(endLocation);
    }

    public void changeStartingLocation(ActionEvent event) {
        lstendBuilding.setVisible(false);
        lstendType.setVisible(false);
        lstendLocation.setVisible(false);

       lstendType.setItems(null);
        //lstendLocation.setItems(null);
        lstendBuilding.setItems(null);

        // allows user to select a location from either map or list of locations
        // which sets location to the start location

        node.retrieveNodes();
        lststartBuilding.setItems(node.getBuildingsFromList(node.getNodeList()));
        lststartBuilding.setVisible(true);

        lststartBuilding.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue)->lststartType.setItems(node.getTypesFromList(newValue, node.getNodeList())));
        lststartType.setVisible(true);

        lststartType.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> lststartLocation.setItems(node.getNodeFromList("Shapiro", newValue, node.getNodeList())));
        lststartLocation.setVisible(true);

        lststartLocation.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> getStartLocation(newValue));
        lststartBuilding.getSelectionModel().selectedItemProperty().removeListener(Object::notifyAll);
    }

    public void selectDestination(ActionEvent event) {
        lststartBuilding.setVisible(false);
        lststartType.setVisible(false);
        lststartLocation.setVisible(false);

        lststartType.setItems(null);
        //lststartLocation.setItems(null);
        lststartBuilding.setItems(null);
        // allows user to select a location from either map or list of locations
        // which sets location to the end location
        NodesDBUtil node = new NodesDBUtil();
        node.retrieveNodes();
        lstendBuilding.setItems(node.getBuildingsFromList(node.getNodeList()));
        lstendBuilding.setVisible(true);

        lstendBuilding.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue)->lstendType.setItems(node.getTypesFromList(newValue, node.getNodeList())));
        lstendType.setVisible(true);

        lstendType.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> lstendLocation.setItems(node.getNodeFromList("Shapiro", newValue, node.getNodeList())));
        lstendLocation.setVisible(true);

        lstendLocation.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> getEndLocation(newValue));

    }


}




