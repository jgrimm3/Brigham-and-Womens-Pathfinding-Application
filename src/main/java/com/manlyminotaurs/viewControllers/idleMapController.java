package com.manlyminotaurs.viewControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import com.manlyminotaurs.core.Main;
import javafx.stage.Stage;


public class idleMapController {
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
        //load up OTHER FXML document
        root=FXMLLoader.load(getClass().getClassLoader().getResource("/FXMLs/home.fxml"));

        //create a new scene with root and set the stage
        Scene scene=new Scene(root);
        stage.setScene(scene);
        stage.show();
        }
        catch (Exception e){
        e.printStackTrace();}
    }

}
