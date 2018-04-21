package main.java.com.manlyminotaurs.viewControllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.sun.deploy.association.Action;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class idleMapController {
    @FXML
    JFXButton btnBeginNav;

    @FXML
    JFXButton btnLogin;

    @FXML
    JFXButton btnInfo;

    @FXML
    JFXButton btnOpenPortal;

    @FXML
    Button idleMap;

    // login pane
    @FXML
    Pane paneLogin;

    @FXML
    JFXButton btnLogin3;

    @FXML
    JFXTextField txtUsername;

    @FXML
    JFXPasswordField txtPassword;



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
