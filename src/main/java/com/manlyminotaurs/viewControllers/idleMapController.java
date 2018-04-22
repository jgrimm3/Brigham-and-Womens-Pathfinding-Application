package com.manlyminotaurs.viewControllers;

import com.manlyminotaurs.core.KioskInfo;
import com.manlyminotaurs.core.Main;
import com.manlyminotaurs.timeout.Memento;
import com.manlyminotaurs.timeout.ResetTask;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;

public class idleMapController implements Initializable{
    @FXML
    Button idleMap;

    //event listener on Map
    @FXML
    public void mapActive(ActionEvent event)throws Exception{
    try{

        Stage stage;
        Parent root;
        //get reference to the button's stage
        stage=(Stage)idleMap.getScene().getWindow();
        stage.addEventHandler(MouseEvent.ANY, KioskInfo.myHandler);

        if(KioskInfo.myTimer != null){
            KioskInfo.myTimer.cancel();
        }
        KioskInfo.myTimer = new Timer();
        KioskInfo.myTimer.schedule(new ResetTask(stage), KioskInfo.myDelay);

        Main.memnto = new Memento(KioskInfo.getCurrentUserID());

        //load up OTHER FXML document
        root=FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/home.fxml"));

        //create a new scene with root and set the stage
        Scene scene=new Scene(root);
        stage.setScene(scene);
        stage.show();
        }
        catch (Exception e){
        e.printStackTrace();}
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(Main.memnto != null){
            KioskInfo.currentUserID = Main.memnto.getState();
        }
        Stage stage = KioskInfo.myStage;
        stage.removeEventHandler(MouseEvent.ANY, KioskInfo.myHandler);
    }
}
