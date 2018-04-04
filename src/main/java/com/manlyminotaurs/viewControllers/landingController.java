package com.manlyminotaurs.viewControllers;

import com.manlyminotaurs.databases.NodesEditor;
import com.manlyminotaurs.nodes.Edge;
import com.manlyminotaurs.nodes.Node;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import com.manlyminotaurs.core.Main;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class landingController {


    private static int curActionPane = 0;

    final double NODEXMIN = 0;
    final double NODEXMAX = 5000;
    final double NODEYMIN = 0;
    final double NODEYMAX = 2774;
    final double NEWMAPXMIN = 0;
    final double NEWMAPXMAX = 1250;
    final double NEWMAPYMIN = 0;
    final double NEWMAPYMAX = 693.8;

    @FXML
    Button btnLogin;

    @FXML
    Button btnHelp;

    @FXML
    Button btnNurseRequest;

    @FXML
    Button btnDirections;

    @FXML
    ImageView mapImg;

    @FXML
    Pane pane;

    @FXML
    Path path;

    @FXML
    ScrollPane scrollPane;

    @FXML
    /**
     * initializes the given floor by printing the points on the map
     */
    protected void initialize() {
        System.out.println("initializing");
        printPoints("L2");

       /* NodesEditor ne = new NodesEditor();
        ne.retrieveNodes();
        ne.retrieveEdges();
        List<Edge> edgelist = ne.edgeList;
        List<Edge> l2List = new ArrayList<>();

        int i = 0;
        while(i < edgelist.size()) {
            if (edgelist.get(i).getStartNode().getFloor().equals("L2") && edgelist.get(i).getEndNode().getFloor().equals("L2")) {
                l2List.add(edgelist.get(i));
            }
            i++;
        }

		printEdgePath(l2List);*/
    }

    private static landingController instance;
    public landingController() {
        instance = this;
    }
    // static method to get instance of view
    public static landingController getInstance() {
        return instance;
    }

    /**
     * Opens the login prompt
     * @param event login button clicked
     */
    public void promptLogin(ActionEvent event){
        Main.addPrompt(0); //go to login prompt
    }

    /**
     *
     * @param event btnNurseRequest
     */
    public void nurseRequestAction(ActionEvent event){
       Pane action = Main.setActionBars(0);
       action.setTranslateY(735);
       action.setTranslateX(85);
    }

    /**
     * Prints points on the map
     * @param floor the floor that you wish to print nodes onto
     */
    public void printPoints(String floor) {
        // Connection for the database
        Connection connection;
        Circle circle;
        List<Node> nodeList;
        NodesEditor nodeEditor = new NodesEditor();
        nodeEditor.retrieveNodes();
        nodeList = nodeEditor.nodeList;

        // map boundaries

        int i = 0;
        // Iterate through each node
        while(i < nodeList.size()) {

            // If the node is on the correct floor
            if(nodeList.get(i).getFloor().equals(floor)) {

                // Get x and y coords
                int x = nodeList.get(i).getXCoord();
                int y = nodeList.get(i).getYCoord();

                // Translate onto our size map
                //double newX = map(x, NODEXMIN, NODEXMAX, NEWMAPXMIN, NEWMAPXMAX);
                //double newY = map(y, NODEYMIN, NODEYMAX, NEWMAPYMIN, NEWMAPYMAX);

                // Translate onto 3D map
                // X: 99.9
                // Y: -18.4
                // Z: 77.2


                // draw the point on the image
                circle = new Circle(x, y, 2);
                circle.setFill(Color.DARKRED);
                pane.getChildren().add(circle);
            }
            i++;
        }
    }

    /**
     *
     * @param startNode node to draw from
     * @param endNode node to draw to
     * @param moveTo start point of line to be drawn
     * @param lineTo end point of line to be drawn
     *
     */
    public void addPath(Node startNode, Node endNode, MoveTo moveTo, LineTo lineTo) {
        if(startNode != null && endNode != null) {
            System.out.println("Found a path!");

            // get the start point
            // Map the x and y coords onto our map
            moveTo.setX(startNode.getXCoord());//, NODEXMIN, NODEXMAX, NEWMAPXMIN, NEWMAPXMAX));
            moveTo.setY(startNode.getYCoord());// NODEYMIN, NODEYMAX, NEWMAPYMIN, NEWMAPYMAX));

            // Draw to end point
            // Map the x and y coords onto our map
            lineTo.setX(endNode.getXCoord());// NODEXMIN, NODEXMAX, NEWMAPXMIN, NEWMAPXMAX));
            lineTo.setY(endNode.getYCoord());// NODEYMIN, NODEYMAX, NEWMAPYMIN, NEWMAPYMAX));

            // add the elements to the path
            path.getElements().add(moveTo);
            path.getElements().add(lineTo);
        }
    }

    /**
     * Prints the given path on the map
     * @param edgeList the edges to draw a path between
     */
    public void printEdgePath(List<Edge> edgeList) {
        System.out.println("Attempting to print path between edges...");

        int i = 0;
        while(i < edgeList.size()) {
            // Give starting point
            MoveTo moveTo = new MoveTo();
            LineTo lineTo = new LineTo();
            Node startNode = edgeList.get(i).getStartNode();
            Node endNode = edgeList.get(i).getEndNode();
            // add the path
            addPath(startNode, endNode, moveTo, lineTo);
            i++;
            System.out.println("Path added...");
        }
    }

    /**
     * Prints the given path on the map
     * @param nodeList the nodes to draw a path between
     */
    public void printNodePath(List<Node> nodeList) {
        System.out.println("Attempting to print path between nodes...");
        int i = 0;
        while(i < nodeList.size()) {
            // Give starting point
            MoveTo moveTo = new MoveTo();
            LineTo lineTo = new LineTo();
            Node startNode = nodeList.get(i);
            Node endNode = nodeList.get(i+1);
            // add the path
            addPath(startNode, endNode, moveTo, lineTo);
            i++;
            System.out.println("Path added...");
        }
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


    @FXML
    /**
     * Prints the x and y value of the mouse click on the object specified in scenebuilder
     * @param event mouseclick
     */
    public void getXandY(MouseEvent event) {
        System.out.println("Coords: ");
        System.out.println("X: " + event.getX());
        System.out.println("Y: " + event.getY());
    }

    /**
     *
     * @param event
     */
    public void directionAction(ActionEvent event){
        //open direction actionBar
        Pane action = Main.setActionBars(1);
        action.setTranslateY(755);
        action.setTranslateX(85);

    }

    /**
     *
     * @param event Directory button pressed
     */
    public void directoryAction(ActionEvent event) {
        // bring up directory action panels in future iterations
    }

    public void helpAction(ActionEvent event) {
        // bring up help screen in future iterations
    }

}
