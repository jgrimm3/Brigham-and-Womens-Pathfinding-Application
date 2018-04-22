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
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
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

    @FXML
    Button paneAbout;
    // login pane
    @FXML
    Pane paneLogin;

    @FXML
    JFXButton btnLogin3;

    @FXML
    JFXTextField txtUsername;

    @FXML
    JFXPasswordField txtPassword;

    public void initialize() {}

    public void closeAboutPanel(ActionEvent event) {
        paneAbout.setVisible(false);
    }

    public void openAboutPanel(ActionEvent event) {
        paneAbout.setVisible(true);
    }

    //Open patient Web Portal
    public void patientPortal(ActionEvent event) throws Exception {
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
