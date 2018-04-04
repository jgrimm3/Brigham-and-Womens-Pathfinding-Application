package com.manlyminotaurs.viewControllers;

import com.manlyminotaurs.databases.NodesEditor;
import com.manlyminotaurs.nodes.Node;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import com.manlyminotaurs.core.Main;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;

import java.sql.Connection;
import java.util.List;

public class adminHomeController {
    @FXML
    Button btnProfile;

    @FXML
    ImageView imgMap;

    @FXML
    Button btnEditNodes;

    @FXML
    Button btnMngReq;

    @FXML
    Button btnLogout;


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
        scrollPane.setVvalue(0.65);
        scrollPane.setHvalue(0.25);
        path.setStrokeWidth(5);
        printPoints("L2");
    }
    public void printPoints(String floor) {
        // Connection for the database
        Connection connection;
        Circle circle;
        Circle outline;
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
                circle = new Circle(x, y, 3);
                outline = new Circle(x,y, 5);
                circle.setFill(Color.RED);
                outline.setFill(Color.BLACK);
                circle.setId("circle" + i);
                pane.getChildren().add(outline);
                pane.getChildren().add(circle);
            }
            i++;
        }
    }

    public void logOut(ActionEvent event){
        //logout and go back to landing page

        Main.setScreen(1); //go to landing screen
    }

    public void editNodesAction(ActionEvent event){
        Pane action = Main.setActionBars(2);
        action.setTranslateY(735);
        action.setTranslateX(85);

    }
    public void manageRequestsAction(ActionEvent event){
        //open direction actionBar
        Pane action = Main.setActionBars(3);
        action.setTranslateY(735);
        action.setTranslateX(85);
    }
}


