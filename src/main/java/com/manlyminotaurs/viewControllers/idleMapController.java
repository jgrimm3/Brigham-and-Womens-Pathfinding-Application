package com.manlyminotaurs.viewControllers;

import com.manlyminotaurs.core.KioskInfo;
import com.manlyminotaurs.core.Main;
import com.manlyminotaurs.timeout.Memento;
import com.manlyminotaurs.timeout.ResetTask;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.manlyminotaurs.core.KioskInfo;
import com.manlyminotaurs.databases.DataModelI;
import com.sun.deploy.association.Action;
import edu.wpi.cs3733d18.teamOapi.giftShop.GiftShop;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;

public class idleMapController implements Initializable {

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
                try {
                    Parent login;
                    Stage stage;
                    //get reference to the button's stage
                    stage = (Stage) btnLogin.getScene().getWindow();
                    //load up Home FXML document

                    KioskInfo.currentUserID = DataModelI.getInstance().getIDByUserPassword(userName.toLowerCase(), password.toLowerCase());

                    if(DataModelI.getInstance().getUserByID(KioskInfo.currentUserID).isType("admin")) {
                        login = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/adminRequestDashBoard.fxml"));
                    }else{
                        login = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/userRequestDashBoard.fxml"));
                    }

                    stage.addEventHandler(InputEvent.ANY, KioskInfo.myHandler);

                    if(KioskInfo.myTimer != null){
                        KioskInfo.myTimer.cancel();
                    }
                    KioskInfo.myTimer = new Timer();
                    KioskInfo.myTimer.schedule(new ResetTask(stage), KioskInfo.myDelay);

                    Main.memnto = new Memento(KioskInfo.getCurrentUserID());

                    //create a new scene with root and set the stage
                    Scene scene = new Scene(login);
                    stage.setScene(scene);
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                // print message
                System.out.println("Wrong username and password!");

            }
        }

        public void goHome(ActionEvent event) {
            try {
                Parent login;
                Stage stage;
                //get reference to the button's stage
                stage = (Stage) btnLogin.getScene().getWindow();
                //load up Home FXML document
                login = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/home.fxml"));

                stage.addEventHandler(InputEvent.ANY, KioskInfo.myHandler);

                if(KioskInfo.myTimer != null){
                    KioskInfo.myTimer.cancel();
                }
                KioskInfo.myTimer = new Timer();
                KioskInfo.myTimer.schedule(new ResetTask(stage), KioskInfo.myDelay);

                Main.memnto = new Memento(KioskInfo.getCurrentUserID());

                //create a new scene with root and set the stage
                Scene scene = new Scene(login);
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        /* Open patient Web Portal *********************************************/

        public void patientPortal(ActionEvent event) {
            StackPane secondaryLayout = new StackPane();
            Stage primaryStage = (Stage) btnLogin.getScene().getWindow();
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
            newWindow.setX(primaryStage.getX() + 600);
            newWindow.setY(primaryStage.getY() + 250);

            newWindow.show();

        }

        public void giftShop(ActionEvent event) {


        GiftShop gshop = new GiftShop();
        try{
           gshop.run(0, 0, 1920, 1080, null, null, null);
        }catch(
                Exception e)

        {
            e.printStackTrace();
        }

}


        @Override
        public void initialize(URL location, ResourceBundle resources) {
            if (Main.memnto != null) {
                KioskInfo.currentUserID = Main.memnto.getState();
            }
            Stage stage = KioskInfo.myStage;
            stage.removeEventHandler(InputEvent.ANY, KioskInfo.myHandler);
        }
    }
