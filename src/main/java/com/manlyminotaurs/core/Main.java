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
    static boolean createTables = true;

    private static DataModelI dataModelI = DataModelI.getInstance();
    public static String pathStrategy = "";


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

        DataModelI.getInstance().updateAllCSVFiles();

        System.out.println("Files Saved!");
    }

    public static void main(String[] args) throws IOException {
        if(createTables) {
            System.out.println("version 7");
            DataModelI.getInstance().startDB();
        }
        KioskInfo.setMyLocation(DataModelI.getInstance().getNodeByID("EINFO00101"));
        launch(args);
    }
}