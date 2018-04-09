package com.manlyminotaurs.viewControllers;

import com.jfoenix.controls.JFXProgressBar;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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


public class createRequestController{
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
    Button btnlogOut;
    @FXML
    TextArea txtMessage;

    String requestType;
    String message;
    int priority;

    @FXML
    protected void initialize() {

    }

    public void submitRequest(javafx.event.ActionEvent event){

        message = txtMessage.getText();
        //pull and send all request info.




    }
    public void setHighPriority(javafx.event.ActionEvent event){
        //if high priority slected, clear other selections and set integer 3
    chkHighPriority.setSelected(true);
    chkMedPriority.setSelected(false);
    chkLowPriority.setSelected(false);
    priority = 3;
    }
    public void setMedPriority(javafx.event.ActionEvent event){
        //if medium priority slected, clear other selections and set integer 2
        chkHighPriority.setSelected(false);
        chkMedPriority.setSelected(true);
        chkLowPriority.setSelected(false);
        priority = 2;
    }
    public void setLowPriority(javafx.event.ActionEvent event){
        //if medium priority slected, clear other selections and set integer 1
        chkHighPriority.setSelected(false);
        chkMedPriority.setSelected(false);
        chkLowPriority.setSelected(true);
        priority = 1;
    }

    public void getXandY(){}

    public void LogOut(javafx.event.ActionEvent event) throws Exception{
        try{
            Stage stage;
            Parent root;
            //get reference to the button's stage
            stage=(Stage)btnlogOut.getScene().getWindow();
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