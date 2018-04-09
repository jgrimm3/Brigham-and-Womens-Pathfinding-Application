package com.manlyminotaurs.viewControllers;

import com.jfoenix.controls.JFXProgressBar;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.stage.Stage;


import java.awt.event.ActionEvent;


public class createRequestController {
    @FXML
    ScrollPane scrollPane;

    @FXML
    ImageView mapImg;

    @FXML
    Pane pane;

    @FXML
    Path path;

    @FXML
    Pane paneAdd;

    @FXML
    ComboBox cmboBuilding;

    @FXML
    Button btnSubmitRequest;

    @FXML
    ComboBox cmboType;

    @FXML
    ComboBox cmboNode;

    @FXML
    CheckBox chkHighPriority;
    @FXML
    CheckBox chkMedPriority;
    @FXML
    CheckBox chkLowPriority;
    @FXML
    Button btnLogout;

    String requestType;
    String message;
    int priority;

    public void submitRequest(javafx.event.ActionEvent event){

    }
    public void getXandY(){}

    public void LogOut(javafx.event.ActionEvent event) throws Exception{
        try{
            Stage stage;
            Parent root;
            //get reference to the button's stage
            stage=(Stage)btnLogout.getScene().getWindow();
            //load up Home FXML document
            root= FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/home.fxml"));

            //create a new scene with root and set the stage
            Scene scene=new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();}
    }

}