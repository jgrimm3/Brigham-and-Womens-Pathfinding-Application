package com.manlyminotaurs.viewControllers;

import com.manlyminotaurs.core.KioskInfo;
import com.manlyminotaurs.databases.DataModelI;
import com.manlyminotaurs.nodes.Node;
import com.manlyminotaurs.nodes.Room;
import com.manlyminotaurs.pathfinding.ClosestStrategyI;
import com.manlyminotaurs.pathfinding.PathNotFoundException;
import com.manlyminotaurs.pathfinding.PathfinderUtil;
import com.manlyminotaurs.pathfinding.PathfindingContext;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class emergencyScreenController {
    @FXML
    ScrollPane scrollPaneMap;

    @FXML
    StackPane stackPaneMap;

    @FXML
    Pane paneMap;

    @FXML
    Path path;

    @FXML
    ImageView mapImg;

    @FXML
    Label lblEmergency;

    List<Node> nodeList = DataModelI.getInstance().retrieveNodes();
    List<Node> pathList = new ArrayList<>();

    @FXML
    public void initialize() {
        printKiosk();
        goToKiosk();
        findExit(null);
    }

    public void printKiosk() {
        Circle kiosk = new Circle();
            kiosk = new Circle(KioskInfo.myLocation.getXCoord(), KioskInfo.myLocation.getYCoord(), 13);
        }

       /* circleList.add(kiosk);
        System.out.println(currentFloor + " is the floor");
        if (currentFloor.equals("1")) {
            kiosk.setFill(Color.BLUE);
            kiosk.setFill(Color.RED);
        } else {
            kiosk.setFill(Color.GRAY);*/
      //  }

      //  kiosk.setStrokeWidth(3);
        //kiosk.setStroke(Color.BLACK);
       // overMap.getChildren().add(kiosk);

    public void goToKiosk() {
            scrollPaneMap.setVvalue((double) KioskInfo.myLocation.getYCoord() / 3400.0);
            scrollPaneMap.setHvalue((double) KioskInfo.myLocation.getXCoord() / 5000.0);
    }

    public void findExit(ActionEvent event){
        // Pathfind to nearest Exit
        String startFloor = "1";
        Node exitNode = new Room("N1X3Y", 1, 3, "F1", "BUILD1", "EXIT", "Node 1, 3", "n1x3y", 1, 0, 0);
        // Pathfind to nearest bathroom
        PathfinderUtil pu = new PathfinderUtil();
        PathfindingContext pf = new PathfindingContext();
        List<Node> path = new LinkedList<Node>();

        try {
            path = pf.getPath(KioskInfo.myLocation, exitNode, new ClosestStrategyI());
            pathList = path;
        } catch (PathNotFoundException e) {
            e.printStackTrace();
        }

        }


}
