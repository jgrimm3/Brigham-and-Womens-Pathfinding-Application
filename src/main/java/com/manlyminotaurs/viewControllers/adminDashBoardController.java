package com.manlyminotaurs.viewControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


public class adminDashBoardController  implements Initializable {

    @FXML
    Button btnCreateRequests;
    @FXML
    Button btnManageRequests;
    @FXML
    Button btnNodeEditor;
    @FXML
    Label lblOpenRequests;
    @FXML
    Label lblAssignedRequests;
    @FXML
    Label lblTotalActive;
    @FXML
    Label lblTotalInactive;
    @FXML
    Button btnLogOut;

//initialize screen with updated info of open reqestsm, node ctive and inactive information.
public void initialize(URL location, ResourceBundle resources) {

    //Load Request Data
    lblOpenRequests.setText(lblOpenRequests.getText() + " 6"); //fill in with database function
    lblAssignedRequests.setText(lblAssignedRequests.getText() + " 6"); //fill in with database info

    //load Node information
    lblTotalActive.setText(lblTotalActive.getText() + " 6");
    lblTotalInactive.setText(lblTotalInactive.getText() + " 6");
}
    //events to handle button presses and switch to appropriate scene

    //create request clicked, open screen
    public void openCreateRequests(ActionEvent event)throws Exception{
        try{
            Stage stage;
            Parent root;
            //get reference to the button's stage
            stage=(Stage)btnCreateRequests.getScene().getWindow();
            //load up OTHER FXML document
            root= FXMLLoader.load(getClass().getClassLoader().getResource("/FXMLs/CreateRequest.fxml"));

            //create a new scene with root and set the stage
            Scene scene=new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();}
    }
    //Node editor button clicked, switch to screen
    public void openNodeEditor(ActionEvent event)throws Exception{
        try{
            Stage stage;
            Parent root;
            //get reference to the button's stage
            stage=(Stage)btnNodeEditor.getScene().getWindow();
            //load up OTHER FXML document
            root= FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/NodeEditor.fxml"));

            //create a new scene with root and set the stage
            Scene scene=new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();}
    }
    //View Requests pressed, open respective scene
    public void openViewRequests(ActionEvent event)throws Exception{
        try{
            Stage stage;
            Parent root;
            //get reference to the button's stage
            stage=(Stage)btnManageRequests.getScene().getWindow();
            //load up OTHER FXML document
            root = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/adminRequestDashBoard.fxml"));

            //create a new scene with root and set the stage
            Scene scene=new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();}
    }

    public void LogOut(ActionEvent event) throws Exception{
        try{
            Stage stage;
            Parent root;
            //get reference to the button's stage
            stage=(Stage)btnLogOut.getScene().getWindow();
            //load up Home FXML document
            root=FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/home.fxml"));

            //create a new scene with root and set the stage
            Scene scene=new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();}
    }


}
