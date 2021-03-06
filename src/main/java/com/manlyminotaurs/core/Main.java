package com.manlyminotaurs.core;

import com.manlyminotaurs.communications.ChatServer;
import com.manlyminotaurs.communications.ClientSetup;
import com.manlyminotaurs.databases.DataModelI;
import com.manlyminotaurs.databases.FirebaseDBUtil;
import com.manlyminotaurs.timeertasks.Memento;
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
import java.util.prefs.Preferences;

import javafx.geometry.Rectangle2D;
import javafx.scene.layout.AnchorPane;


public class Main extends Application {
    public static Memento memnto;

    static AnchorPane root; //root holds all other screens
    static boolean createTables = true;


    private static DataModelI dataModelI = DataModelI.getInstance();
    public static String pathStrategy = "";

    private FireDetector fd;

    /**
     *  starts application
     *
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        KioskInfo.myStage = primaryStage;
        try{
            System.out.println("Checking Status of Emergency");
            if(ChatServer.getState() == 0) {
                //root is anchor pane that all other screens will be held in
                root = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/idleMap.fxml"));
            }else{
                root = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/emergencyScreen.fxml"));
            }
            Scene world = new Scene(root, 1900, 1000);
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

            fd = new FireDetector(primaryStage);
            fd.startDetecting();

            primaryStage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    // wait for application to finish,calls Platform exit, save files.

    /**
     *   exit the program
     *
     * @param event
     */
    @FXML
    public void exitApplication(ActionEvent event) {
        Platform.exit();
    }
    @Override
    public void stop(){
        System.out.println("closing Application");

        DataModelI.getInstance().addLog("Application Closed",LocalDateTime.now(), "N/A", "N/A","application");
       // DataModelI.getInstance().updateAllCSVFiles();
        System.out.println("Files Saved!");

        System.exit(0);
    }

    /**
     * Main function
     *
     * @param args
     * @throws IOException
     */

    public static void main(String[] args) throws IOException {
        if(createTables) {
            System.out.println("version 7");
            DataModelI.getInstance().startDB();
 //           DataModelI.getInstance().addLog("Database Setup", LocalDateTime.now(), "N/A", "N/A", "database");
        }

        if(args.length < 1){
            System.out.println("An IP for the server must be specified");
            System.exit(-1);
        }

        ClientSetup.IP = args[0];

        if(args.length > 1 && args[1].equals("-s")) {
            new ChatServer().spoolUpServer();
            System.out.println("SERVER IS UP");
        }



        KioskInfo.setMyLocation(DataModelI.getInstance().getNodeByID("EINFO00101"));
//        DataModelI.getInstance().addLog("Application Started",LocalDateTime.now(), "N/A", "N/A","application");

        Preferences.userRoot().node(Main.class.getName()).getInt("DelayTime", 15000);


        DataModelI.getInstance().listenToEmergency();
        launch(args);
    }



}