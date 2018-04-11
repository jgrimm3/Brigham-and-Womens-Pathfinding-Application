package com.manlyminotaurs.core;

import com.manlyminotaurs.databases.DataModelI;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Rectangle2D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import javax.xml.crypto.Data;


public class Main extends Application {

    static AnchorPane root; //root holds all other screens
    public static List<AnchorPane> screens = new ArrayList<AnchorPane>(); // list of other screens
    public static List<Pane> actionBars = new ArrayList<Pane>(); // list of action bar displays
    public static List<AnchorPane> prompts = new ArrayList<AnchorPane>(); // list of prompts
    private static int curScreen = 0;
    private static int curAction = 0;
    private static int curPrompt = 0;
    private static DataModelI dataModelI = DataModelI.getInstance();



    @Override
    public void start(Stage primaryStage) throws Exception{
        try{
        //root is anchor pane that all other screens will be held in
        root = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/home.fxml"));

        Scene world = new Scene(root, 1920, 1080);
        primaryStage.setTitle("Brigham and Women's Hospital Navigation");
        //add style sheets here

       // world.getStylesheets().add(getClass().getResource("landingStyle.css").toExternalForm());
        primaryStage.setScene(world);

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        //set Stage boundaries to visible bounds of the main screen
        //primaryStage.setX(primaryScreenBounds.getMinX());
       // primaryStage.setY(primaryScreenBounds.getMinY());
        primaryStage.setWidth(primaryScreenBounds.getWidth());
        primaryStage.setHeight(primaryScreenBounds.getHeight());

        primaryStage.show();
    }catch(Exception e){
        e.printStackTrace();
    }
}
    // wait for application to finish,calls Platform exit, save files.
    @FXML
    public void exitApplication(ActionEvent event) {
        Platform.exit();
    }
    @Override
    public void stop(){
        System.out.println("closing Application");

        DataModelI.getInstance().updateNodeCSVFile("./nodes.csv");
        DataModelI.getInstance().updateEdgeCSVFile("./edges.csv");
        DataModelI.getInstance().updateMessageCSVFile("./MessageTable.csv");
        DataModelI.getInstance().updateRequestCSVFile("./RequestTable.csv");
        DataModelI.getInstance().updateUserCSVFile("./UserAccountTable.csv");
        DataModelI.getInstance().updateUserPasswordFile("./UserPasswordTable.csv");
        DataModelI.getInstance().updateStaffTable("./StaffTable.csv");


        System.out.println("Files Saved!");
    }

    public static void main(String[] args) throws IOException {
        System.out.println("version 7");
        DataModelI.getInstance().startDB();
        KioskInfo.setMyLocation(DataModelI.getInstance().getNodeByIDFromList("EINFO00101", DataModelI.getInstance().retrieveNodes()));
        launch(args);
    }
}