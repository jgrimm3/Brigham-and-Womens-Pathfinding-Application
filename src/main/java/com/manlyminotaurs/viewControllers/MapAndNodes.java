package com.manlyminotaurs.viewControllers;

import com.manlyminotaurs.databases.DataModelI;
import com.manlyminotaurs.nodes.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class MapAndNodes {

    /**
     * Prints points on the map
     * @param floor the floor that you wish to print nodes onto
     */
    public void printPoints2D(Pane pane, String floor) {
        // Connection for the database
        Connection connection;
        Circle circle;
        Circle outline;
        List<Node> nodeList;
        nodeList = DataModelI.getInstance().retrieveNodes();

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
                circle = new Circle(x, y, 3);
                outline = new Circle(x,y, 5);
                circle.setFill(javafx.scene.paint.Color.RED);
                outline.setFill(Color.BLACK);
                circle.setId("circle" + i);
                pane.getChildren().add(outline);
                pane.getChildren().add(circle);
            }
            i++;
        }

    }
    /**
     * Will clear all the points on the map... for some reason only deletes a few at a time...
     */
    public void clearPoints(Pane pane) {
        int i = 0;
        while (i < pane.getChildren().size()) {
            if (pane.getChildren().get(i).getId().contains("circle")) {
                pane.getChildren().remove(pane.getChildren().get(i));
            }
            i++;
        }

    }



}
