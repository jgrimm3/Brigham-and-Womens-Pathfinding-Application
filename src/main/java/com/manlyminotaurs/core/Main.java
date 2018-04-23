package com.manlyminotaurs.core;

import com.manlyminotaurs.communications.ChatServer;
import com.manlyminotaurs.communications.ClientSetup;
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
import java.time.LocalDateTime;

import javafx.geometry.Rectangle2D;
import javafx.scene.layout.AnchorPane;


public class Main extends Application {

    static AnchorPane root; //root holds all other screens
    static boolean createTables = true;

    private static DataModelI dataModelI = DataModelI.getInstance();
    public static String pathStrategy = "";

    //private FireDetector fd;

    @Override
    public void start(Stage primaryStage) throws Exception{
        try{
            System.out.println("Checking Status of Emergency");
            if(true) {//new ClientSetup(null).requestState().equals("0")) {
                //root is anchor pane that all other screens will be held in
                root = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/home.fxml"));
            }else{
                root = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/emergencyScreen.fxml"));
            }
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

       // fd = new FireDetector(primaryStage);
       // fd.startDetecting();
    }
    // wait for application to finish,calls Platform exit, save files.
    @FXML
    public void exitApplication(ActionEvent event) {
        Platform.exit();
    }
    @Override
    public void stop(){
        System.out.println("closing Application");

        DataModelI.getInstance().addLog("Application Closed",LocalDateTime.now(), "N/A", "N/A","application");
        DataModelI.getInstance().updateAllCSVFiles();
        //DataModelI.getInstance().addLog("Database saved to CSV files",LocalDateTime.now(), "N/A", "N/A","database");

        System.out.println("Files Saved!");
    }

    public static void main(String[] args) throws IOException {
        if(createTables) {
            System.out.println("version 7");
            DataModelI.getInstance().startDB();
            DataModelI.getInstance().addLog("Database Setup", LocalDateTime.now(), "N/A", "N/A", "database");
        }
        KioskInfo.setMyLocation(DataModelI.getInstance().getNodeByID("EINFO00101"));
        DataModelI.getInstance().addLog("Application Started",LocalDateTime.now(), "N/A", "N/A","application");

        if(args.length < 1){
            System.out.println("An IP for the server must be specified");
            System.exit(-1);
        }

        ClientSetup.IP = args[0];

        if(args.length > 1 && args[1].equals("-s")) {
            new ChatServer().spoolUpServer();
        }

        launch(args);
    }
}