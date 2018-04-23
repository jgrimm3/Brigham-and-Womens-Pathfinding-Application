package com.manlyminotaurs.viewControllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.manlyminotaurs.core.KioskInfo;
import com.manlyminotaurs.databases.DataModelI;
import com.sun.deploy.association.Action;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;




public class idleMapController {

    @FXML
    JFXTextField txtUsername;

    @FXML
    JFXPasswordField txtPassword;

    @FXML
    JFXButton btnBeginNav;

    @FXML
    JFXButton btnLogin;

    @FXML
    JFXButton btnInfo;

    @FXML
    JFXButton btnOpenPortal;

    @FXML
    Pane paneAbout;

    @FXML
    Pane paneLogin;

    @FXML
    JFXButton btnLogin3;

    @FXML
    JFXButton btnCloseAbout;

    @FXML
    Label lblHour;

    @FXML
    Label lblMinute;

    @FXML
    Label lblDate;

    @FXML
    JFXButton btnGiftShop;



    public void initialize() {
        paneAbout.setVisible(false);
        paneLogin.setVisible(false);
    }

    /* opening and closing the about panel ***********************************/

    public void closeAboutPanel(ActionEvent event) {
        paneAbout.setVisible(false);
    }

    public void openAboutPanel(ActionEvent event) {
        paneAbout.setVisible(true);
    }


    /* opening and closing the login panel ***********************************/

    public void openLoginPanel(ActionEvent event) {

        txtUsername.setText("");
        txtPassword.setText("");

        // Show login panel
        paneLogin.setVisible(true);

    }

    public void closeLoginPanel(ActionEvent event) {

        txtUsername.setText("");
        txtPassword.setText("");

        // Hide login panel
        paneLogin.setVisible(false);
    }

    public void login(ActionEvent event) {

        String userName = txtUsername.getText();
        String password = txtPassword.getText();

        if (userName.equals("") || password.equals("")) {

            // print message
            System.out.println("Please completely fill in the username and password fields");
        } else if (DataModelI.getInstance().doesUserPasswordExist(userName.toLowerCase(), password.toLowerCase())) {
            try{
                    Parent login;
                    Stage stage;
                    //get reference to the button's stage
                    stage=(Stage)btnLogin.getScene().getWindow();
                    //load up Home FXML document
                    login = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/adminRequestDashBoard.fxml"));

                    KioskInfo.currentUserID = "";

                    //create a new scene with root and set the stage
                    Scene scene=new Scene(login);
                    stage.setScene(scene);
                    stage.show();
            } catch (Exception e){
            e.printStackTrace();}

        } else {
            // print message
            System.out.println("Wrong username and password!");

        }
    }
    public void goHome(ActionEvent event) {
        try{
            Parent login;
            Stage stage;
            //get reference to the button's stage
            stage=(Stage)btnLogin.getScene().getWindow();
            //load up Home FXML document
            login = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/home.fxml"));


            //create a new scene with root and set the stage
            Scene scene=new Scene(login);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e){
            e.printStackTrace();}
    }


    /* Open patient Web Portal *********************************************/

    public void patientPortal(ActionEvent event) {
        StackPane secondaryLayout = new StackPane();
        Stage primaryStage = (Stage)btnLogin.getScene().getWindow();
        //secondaryLayout.getChildren().add(secondLabel);

        Stage stage;

        //get reference to the button's stage

        WebView web = new WebView();
        web.getEngine().load("https://patientgateway.partners.org/public/");
        Scene scene = new Scene(web);

        // New window (Stage)
        Stage newWindow = new Stage();
        newWindow.setTitle("Patient Portal");
        newWindow.setScene(scene);

        // Specifies the modality for new window.
        newWindow.initModality(Modality.WINDOW_MODAL);

        // Specifies the owner Window (parent) for new window
        newWindow.initOwner(primaryStage);

        // Set position of second window, related to primary window.
        newWindow.setX(primaryStage.getX()+600);
        newWindow.setY(primaryStage.getY()+250);

        newWindow.show();

    }

    public void giftShop(ActionEvent event) {

    }

    /*event listener on Map
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
    } */



}
