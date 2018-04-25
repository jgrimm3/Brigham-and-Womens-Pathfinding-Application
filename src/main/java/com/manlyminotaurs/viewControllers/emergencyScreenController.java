package com.manlyminotaurs.viewControllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.manlyminotaurs.communications.ClientSetup;
import com.manlyminotaurs.core.KioskInfo;
import com.manlyminotaurs.databases.DataModelI;
import com.manlyminotaurs.nodes.Exit;
import com.manlyminotaurs.nodes.Node;
import com.manlyminotaurs.pathfinding.*;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class emergencyScreenController {
    @FXML
    ScrollPane scrollPaneMap;
    @FXML
    AnchorPane backPane;
    @FXML
    StackPane stackPaneMap;
    @FXML
    Pane paneMap;
    @FXML
    JFXPasswordField txtPswd;
    @FXML
    JFXButton btnReset;
    @FXML
    ImageView mapImg;
    @FXML
    Pane overMap;
    @FXML
    Label lblEmergency;
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
    javafx.scene.text.Text startName;
    @FXML
    javafx.scene.text.Text endName;
    @FXML
    javafx.scene.text.Text destinationText;
    @FXML
    ImageView destination;

    javafx.scene.text.Text currName;
    FadeTransition fade;

    @FXML
    Path currPath;
    List<Node> nodeList = DataModelI.getInstance().retrieveNodes();
    List<Node> pathList = new ArrayList<>();

    String startFloor = "1";
    String endFloor = "1";
    String currentFloor = "1";
    ClientSetup client;
    MediaPlayer a;

    /**
     * sets up emergency screen
     */
    @FXML
    public void initialize() {
        Stage stage = KioskInfo.myStage;
        stage.removeEventFilter(InputEvent.ANY, KioskInfo.myHandler);

        printKiosk();
        goToKiosk();
        txtPswd.setStyle("-fx-prompt-text-fill: WHITE");
        // Pathfind to nearest Exit
        String startFloor = KioskInfo.getMyLocation().getFloor();
        Node realExit= DataModelI.getInstance().getNodeByID("EEXIT00101");
        Node exitNode = new Exit("388738", 0, 0, "1", "BUILD2", "EXIT", "Node 2, 1", "11y", 1, 1, 1);
        // Pathfind to nearest bathroom
        PathfinderUtil pu = new PathfinderUtil();
        PathfindingContext pf = new PathfindingContext();
        List<Node> path = new LinkedList<Node>();

        URL resource = getClass().getResource("/Sound/EvacuationEmergency Voice Sample from Matthew Kelly (Highcroft.com).mp3");
        a = new MediaPlayer(new Media(resource.toString()));
        a.setOnEndOfMedia(new Runnable() {
            public void run() {
                a.seek(Duration.ZERO);
            }
        });
        a.play();

        try {
            path = pf.getPath(KioskInfo.getMyLocation(), realExit, new AStarStrategyI());
            pathList = path;
        } catch (PathNotFoundException e) {
            e.printStackTrace();
        }
        printNodePath(path, startFloor, "2-D");
        changeFloor(startFloor);

        client = new ClientSetup(null);

    }

    /**
     * generates the kiosk circle
     */
    public void printKiosk() {
        Circle kiosk = new Circle();
        kiosk = new Circle(KioskInfo.myLocation.getXCoord(), KioskInfo.myLocation.getYCoord(), 13);
        kiosk.setFill(Color.RED);
    }

    /**
     * draws lines start node and end node
     * @param currPath current path
     * @param dimension dimension: 2d or 3d
     * @param startNode start node
     * @param endNode end node
     * @param moveTo move To
     * @param lineTo line To
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
     * sets the text at desired location
     * @param text text
     * @param finishX finish x coordinate
     * @param finishY finish y coordinate
     * @param subX subtract x coordinate used for calculation
     * @param subY subtract y coordinate used for calculation
     * @param font font
     */
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
     * sets screen to kiosk location
     */
    public void goToKiosk() {
            scrollPaneMap.setVvalue((double) KioskInfo.myLocation.getYCoord() / 3400.0);
            scrollPaneMap.setHvalue((double) KioskInfo.myLocation.getXCoord() / 5000.0);
            floor2DMapLoader(startFloor);
    }

    boolean pathRunning;

    /**
     * print node path and snap to the path
     * @param path path which is a list of nodes
     * @param floor floor
     * @param dimension dimension: 2d or 3d
     */
    private void printNodePath(List<Node> path, String floor, String dimension) {
        System.out.println("Attempting to print path between nodes...");
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
            Circle startCircle = new Circle();
            //javafx.scene.text.Text startName = new javafx.scene.text.Text(startNode.getLongName());
            //javafx.scene.text.Text endName = new javafx.scene.text.Text(endNode.getLongName());
            destination.setVisible(true);
            destinationText.setVisible(true);

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

    /**
     * change floor based on floor input
     * @param floor floor
     */
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

    /**
     * change floor to L2
     * @param event
     */
    public void changeFloorL2(ActionEvent event) {

            floor2DMapLoader("L2");

        currentFloor = "L2";
        printKiosk();


        System.out.println("you selected floor L2");
    }

    /**
     * change floor to L1
     * @param event
     */
    public void changeFloorL1(ActionEvent event) {

            floor2DMapLoader("L1");
        currentFloor = "L1";
        printKiosk();

        System.out.println("you selected floor L1");

    }

    /**
     * change floor to 1
     * @param event
     */
    public void changeFloor1(ActionEvent event) {

            floor2DMapLoader("1");


        currentFloor = "1";
        printKiosk();


        System.out.println("you selected floor 1");

    }

    /**
     * change floor to 2
     * @param event
     */
    public void changeFloor2(ActionEvent event) {

            floor2DMapLoader("2");


        currentFloor = "2";
        printKiosk();


        System.out.println("you selected floor 2");

    }

    /**
     * change floor to 3
     * @param event
     */
    public void changeFloor3(ActionEvent event) {

            floor2DMapLoader("3");

        currentFloor = "3";
        printKiosk();

        System.out.println("you selected floor 3");

    }

    /**
     * sets the floor of the circle clicked
     * @param event
     */
    private void startCircleClicked(MouseEvent event) {
        System.out.println("Recognized a click");

        if(!startFloor.equals(currentFloor)) {


                // !!!
                changeFloor(startFloor);

            currentFloor = startFloor;
        }

    }

    /**
     * loads 2D floor maps based on floor
     * @param floor floor
     */
    public void floor2DMapLoader(String floor) {
//		cancelFinish.setVisible(false);
//		cancelStart.setVisible(false);

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

    /**
     * print name of circle clicked
     * @param mouseEvent
     */
    private void printName(MouseEvent mouseEvent) {
        Circle currCircle = (Circle)mouseEvent.getTarget();
        javafx.scene.text.Text name = new javafx.scene.text.Text(currCircle.getId());
        name.setVisible(true);
        name.setLayoutX(currCircle.getCenterX() + 5 + currCircle.getId().length());
        name.setFont(new Font(40));
        name.setFill(Color.BLACK);
        name.setStrokeWidth(1);
        name.setStroke(Color.WHITE);
        name.setLayoutY(currCircle.getCenterY());
        //name.setRotate(-overMap.getRotate());
        currName = name;
        overMap.getChildren().add(name);
        fade = new FadeTransition(Duration.millis(200), name);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setAutoReverse(true);
        fade.setCycleCount(1);
        fade.play();
    }

    private void removeName(MouseEvent mouseEvent) {
        fade = new FadeTransition(Duration.millis(200), currName);
        fade.setFromValue(1);
        fade.setToValue(0);
        fade.setAutoReverse(true);
        fade.setCycleCount(1);
        fade.play();
        currName = null;
        paneMap.getChildren().remove(currName);
    }

    /**
     * print start name with fade
     * @param mouseEvent
     */
    @FXML
    private void printStartName(MouseEvent mouseEvent) {
        fade = new FadeTransition(Duration.millis(200), startName);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setAutoReverse(true);
        fade.setCycleCount(1);
        fade.play();
    }

    /**
     * remove start name with fade
     * @param mouseEvent
     */
    @FXML
    private void removeStartName(MouseEvent mouseEvent) {
        fade = new FadeTransition(Duration.millis(200), startName);
        fade.setFromValue(1);
        fade.setToValue(0);
        fade.setAutoReverse(true);
        fade.setCycleCount(1);
        fade.play();
    }

    /**
     * print end name with fade
     * @param mouseEvent
     */
    @FXML
    private void printEndName(MouseEvent mouseEvent) {
        fade = new FadeTransition(Duration.millis(200), endName);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setAutoReverse(true);
        fade.setCycleCount(1);
        fade.play();
    }

    /**
     * remove end name with fade
     * @param mouseEvent
     */
    @FXML
    private void removeEndName(MouseEvent mouseEvent) {
        fade = new FadeTransition(Duration.millis(200), endName);
        fade.setFromValue(1);
        fade.setToValue(0);
        fade.setAutoReverse(true);
        fade.setCycleCount(1);
        fade.play();
    }

    /**
     * clear path from screen
     */
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

    /**
     * resets system
     * @param e
     */
    @FXML
    public void resetSystem(ActionEvent e){
        if(txtPswd.getText().equals("Password")){
            Stage stage = (Stage) txtPswd.getScene().getWindow();
            stage.addEventFilter(InputEvent.ANY, KioskInfo.myHandler);

            a.stop();

            client.sendReset();
        }
    }
}

