package com.manlyminotaurs.core;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Rectangle2D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;


public class Main extends Application {

    static AnchorPane root; //root holds all other screens
    static List<AnchorPane> screens = new ArrayList<AnchorPane>(); // list of other screens
    public static List<Pane> actionBars = new ArrayList<Pane>(); // list of action bar displays
    private static int curScreen =0;
    private static int curAction = 0;

    @Override
    public void start(Stage primaryStage) throws Exception{
        try{
        //root is anchor pane that all other screens will be held in
        root = (AnchorPane)FXMLLoader.load(getClass().getResource("/FXMLs/world.fxml"));


        //add screens here
        screens.add((AnchorPane)FXMLLoader.load(getClass().getResource("/FXMLs/idleMap.fxml"))); //map page index 0
        screens.add((AnchorPane)FXMLLoader.load(getClass().getResource("/FXMLs/landing.fxml"))); //landing page index 1
        screens.add((AnchorPane)FXMLLoader.load(getClass().getResource("/FXMLs/login.fxml"))); //login page index 2
        screens.add((AnchorPane)FXMLLoader.load(getClass().getResource("/FXMLs/adminHome.fxml"))); //Admin page index 3
        screens.add((AnchorPane)FXMLLoader.load(getClass().getResource("/FXMLs/userHome.fxml"))); //Admin page index 4

            actionBars.add((Pane)FXMLLoader.load(getClass().getResource("/FXMLs/userNrActionBar.fxml")));//0
            actionBars.add((Pane)FXMLLoader.load(getClass().getResource("/FXMLs/directionsActionBar.fxml")));//1
            actionBars.add((Pane)FXMLLoader.load(getClass().getResource("/FXMLs/editNodesActionBar.fxml")));//2
            actionBars.add((Pane)FXMLLoader.load(getClass().getResource("/FXMLs/manageRequestsActionBar.fxml")));//3


        root.getChildren().add(screens.get(0));

        Scene world = new Scene(root, 1920, 1080);
        primaryStage.setTitle("Brigham and Women's Hospital Navigation");
        //add style sheets here

       // world.getStylesheets().add(getClass().getResource("landingStyle.css").toExternalForm());
        primaryStage.setScene(world);

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        //set Stage boundaries to visible bounds of the main screen
        primaryStage.setX(primaryScreenBounds.getMinX());
        primaryStage.setY(primaryScreenBounds.getMinY());
        primaryStage.setWidth(primaryScreenBounds.getWidth());
        primaryStage.setHeight(primaryScreenBounds.getHeight());


        primaryStage.show();
    }catch(Exception e){
        e.printStackTrace();
    }
}

    //function to switch screens
    public static AnchorPane setScreen(int indxNxt){
        root.getChildren().remove(screens.get(curScreen));
        root.getChildren().add(screens.get(indxNxt));
        curScreen = indxNxt;
        return root;
    }
    public static Pane setActionBars(int indxNxtBar){
        root.getChildren().remove(actionBars.get(curAction));
        root.getChildren().add(actionBars.get(indxNxtBar));
        curAction = indxNxtBar;
        return actionBars.get(indxNxtBar);
    }
    public static void main(String[] args) {
        launch(args);
    }
}