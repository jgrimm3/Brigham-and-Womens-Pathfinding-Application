package com.manlyminotaurs.core;

import com.manlyminotaurs.databases.NodesEditor;
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
    public static List<AnchorPane> screens = new ArrayList<AnchorPane>(); // list of other screens
    public static List<Pane> actionBars = new ArrayList<Pane>(); // list of action bar displays
    public static List<AnchorPane> prompts = new ArrayList<AnchorPane>(); // list of prompts
    private static int curScreen = 0;
    private static int curAction = 0;


    @Override
    public void start(Stage primaryStage) throws Exception{
        try{
        //root is anchor pane that all other screens will be held in
        root = (AnchorPane)FXMLLoader.load(getClass().getResource("/FXMLs/world.fxml"));


        //add screens here
        screens.add((AnchorPane)FXMLLoader.load(getClass().getResource("/FXMLs/idleMap.fxml"))); //map page index 0
        screens.add((AnchorPane)FXMLLoader.load(getClass().getResource("/FXMLs/landing.fxml"))); //landing page index 1
        screens.add((AnchorPane)FXMLLoader.load(getClass().getResource("/FXMLs/adminHome.fxml"))); //Admin page index 2
        screens.add((AnchorPane)FXMLLoader.load(getClass().getResource("/FXMLs/userHome.fxml"))); //Admin page index 3

            //add action bars here
            actionBars.add((Pane)FXMLLoader.load(getClass().getResource("/FXMLs/userNrActionBar.fxml")));//0
            actionBars.add((Pane)FXMLLoader.load(getClass().getResource("/FXMLs/directionsActionBar.fxml")));//1
            actionBars.add((Pane)FXMLLoader.load(getClass().getResource("/FXMLs/editNodesActionBar.fxml")));//2
            actionBars.add((Pane)FXMLLoader.load(getClass().getResource("/FXMLs/manageRequestsActionBar.fxml")));//3

            //add prompts here
            prompts.add((AnchorPane)FXMLLoader.load(getClass().getResource("/FXMLs/login.fxml"))); //login prompt index 0
            prompts.add((AnchorPane)FXMLLoader.load(getClass().getResource("/FXMLs/completeRequest.fxml"))); //login prompt index 1
            prompts.add((AnchorPane)FXMLLoader.load(getClass().getResource("/FXMLs/adminNurseSend.fxml"))); //login prompt index 2
            prompts.add((AnchorPane)FXMLLoader.load(getClass().getResource("/FXMLs/addCoordinate.fxml"))); //login prompt index 3
            prompts.add((AnchorPane)FXMLLoader.load(getClass().getResource("/FXMLs/editCoordinate.fxml"))); //login prompt index 4
            prompts.add((AnchorPane)FXMLLoader.load(getClass().getResource("/FXMLs/deleteNodeConfirmation.fxml"))); //login prompt index 5
            prompts.add((AnchorPane)FXMLLoader.load(getClass().getResource("/FXMLs/deleteNodeSelection.fxml"))); //login prompt index 6



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
    public static int getScreen(){return curScreen;}
    public static void removeScreen(int oldIndex){
        root.getChildren().remove(screens.get(oldIndex));
    }
    public static int getAction(){
        return curAction;
    }
    public static void removeAction(int curAction){
        root.getChildren().remove(actionBars.get(curAction));
    }

    public static Pane setActionBars(int indxNxtBar){
        root.getChildren().remove(actionBars.get(curAction));
        root.getChildren().add(actionBars.get(indxNxtBar));
        curAction = indxNxtBar;
        return actionBars.get(indxNxtBar);
    }

    public static void addPrompt(int indxNxt) {
        root.getChildren().add(prompts.get(indxNxt));
        curScreen = indxNxt;
    }

    public static void removePrompt(int indxNxt) {
        root.getChildren().remove(prompts.get(indxNxt));
    }

    public static void main(String[] args) {
        new NodesEditor().setupDatabase();
        Kiosk kiosk = new Kiosk(new NodesEditor().getNodeFromList("GCONF02001"), "user");

        launch(args);
    }
}