package com.manlyminotaurs.viewControllers;

import com.manlyminotaurs.databases.NodesEditor;
import com.manlyminotaurs.nodes.Edge;
import com.manlyminotaurs.nodes.Node;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import com.manlyminotaurs.core.Main;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;


import java.sql.*;
import java.util.List;

public class landingController {

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
    /**
     * initializes the given floor by printing the points on the map
     */
    protected void initialize() {
        printPoints("L2");
    }

    public void promptLogin(ActionEvent event){
        Main.addPrompt(0); //go to login prompt
    }

    public void nurseRequestAction(ActionEvent event){
        Main.setActionBars(0).setTranslateY(715);
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
        final double NODEXMIN = 0;
        final double NODEXMAX = 5000;
        final double NODEYMIN = 0;
        final double NODEYMAX = 3400;
        final double NEWMAPXMIN = 0;
        final double NEWMAPXMAX = 1000;
        final double NEWMAPYMIN = 0;
        final double NEWMAPYMAX = 680;
        int i = 0;
        // Iterate through each node
        while(i < nodeList.size()) {

            // If the node is on the correct floor
            if(nodeList.get(i).getFloor().equals(floor)) {

                // Get x and y coords
                int x = nodeList.get(i).getXCoord();
                int y = nodeList.get(i).getYCoord();

                // Translate onto our size map
                double newX = map(x, NODEXMIN, NODEXMAX, NEWMAPXMIN, NEWMAPXMAX);
                double newY = map(y, NODEYMIN, NODEYMAX, NEWMAPYMIN, NEWMAPYMAX);

                // draw the point on the image
                circle = new Circle(newX, newY, 2);
                pane.getChildren().add(circle);
            }
            i++;
        }
    }

    /**
     * Prints the given path on the map
     * @param edgeList the nodes to draw a bath between
     */
    public void printEdges(List<Edge> edgeList) {
        int i = 0;
        while(i < edgeList.size()) {
             Node startNode = edgeList.get(i).getStartNode();
             Node endNode = edgeList.get(i).getEndNode();
            Path path = new Path();

            // Give starting point
            MoveTo moveTo = new MoveTo();
            moveTo.setX(startNode.getXCoord());
            moveTo.setY(startNode.getYCoord());

            // Draw to end point
            LineTo lineTo = new LineTo();
            lineTo.setX(endNode.getXCoord());
            lineTo.setY(endNode.getYCoord());
            path.getElements().add(moveTo);
            path.getElements().add(lineTo);

            i++;
        }
    }

    /**
     * Maps the value from the old boundary to the new boundary
     * @param value the value to be transferred
     * @param oldMin the old min value (x or y)
     * @param oldMax the old max value (x or y)
     * @param newMin the new min value (x or y)
     * @param newMax the new max calue (x or y)
     * @return
     */
    static double map(double value, double oldMin, double oldMax, double newMin, double newMax) {
        return (((value - oldMin)*(newMax - newMin))/(oldMax-oldMin)) + newMin;
    }

    @FXML
    /**
     * Prints the x and y value of the mouse click on the object specified in scenebuilder
     */
    public void getXandY(MouseEvent event) {
        System.out.println("Coords: ");
        System.out.println("X: " + event.getX());
        System.out.println("Y: " + event.getY());
    }
    public void directionAction(ActionEvent event){
        //open direction actionBar
        Main.setActionBars(1).setTranslateY(715); //go to directions
    }

}
