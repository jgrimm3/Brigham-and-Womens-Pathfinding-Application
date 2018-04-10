package com.manlyminotaurs.viewControllers;

import com.jfoenix.controls.JFXProgressBar;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class adminRequestDashboardController  {

    Parent home;
    Parent nodeEdit;
    Parent createRequest;

    @FXML
    Button navBtnNewRequest;
    @FXML
    Button navBtnNodeEditor;
    @FXML
    TableView tblOpenRequests;
    @FXML
    Button btnCompleteRequest;
    @FXML
    Button btnDeleteRequest;
    @FXML
    TableView tblClosedRequests;
    @FXML
    Label lblRequestDetails;
    @FXML
    Button btnLogOut;

    Parent logout;



    public void logOut(ActionEvent event) throws Exception{
        try{
            Stage stage;
            //get reference to the button's stage
            stage=(Stage)btnLogOut.getScene().getWindow();
            //load up Home FXML document
            home = FXMLLoader.load(getClass().getResource("/FXMLs/home.fxml"));
            //create a new scene with root and set the stage
            Scene scene=new Scene(home);
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();}
    }
    public void createRequest(ActionEvent event) throws Exception{
        try{
            Stage stage;
            //get reference to the button's stage
            stage=(Stage)btnLogOut.getScene().getWindow();
            //load up Home FXML document
            createRequest = FXMLLoader.load(getClass().getResource("/FXMLs/CreateRequest.fxml"));
            //create a new scene with root and set the stage
            Scene scene=new Scene(createRequest);
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();}
    }

    public void nodeEditor(ActionEvent event) throws Exception{

         try{
        Stage stage;
        //get reference to the button's stage
        stage=(Stage)btnLogOut.getScene().getWindow();
        //load up Home FXML document
        nodeEdit = FXMLLoader.load(getClass().getResource("/FXMLs/nodeEditor.fxml"));
        //create a new scene with root and set the stage
        Scene scene=new Scene(nodeEdit);
        stage.setScene(scene);
        stage.show();
    }
        catch (Exception e){
        e.printStackTrace();}
}

}