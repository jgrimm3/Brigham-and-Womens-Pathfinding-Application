package com.manlyminotaurs.viewControllers;

import com.jfoenix.controls.JFXTextField;
import com.manlyminotaurs.core.KioskInfo;
import com.manlyminotaurs.users.User;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class accountManagerController {
    Parent logout;
    Parent nodeEdit;
    Parent manageRequests;

    @FXML
    JFXTextField txtFirstNameAdd;
    @FXML
    JFXTextField txtMiddleNameAdd;
    @FXML
    JFXTextField txtLastNameAdd;
    @FXML
    ComboBox cmboLanguageAdd;
    @FXML
    ComboBox cmboTypeAdd;
    @FXML
    JFXTextField txtUsernameAdd;
    @FXML
    JFXTextField txtPasswordAdd;
    @FXML
    Button btnAddUser;
    @FXML
    JFXTextField txtUserIDModify;
    @FXML
    JFXTextField txtFirstNameModify;
    @FXML
    JFXTextField txtMiddleNameModify;
    @FXML
    JFXTextField txtLastNameModify;
    @FXML
    ComboBox cmboLanguageModify;
    @FXML
    ComboBox cmboTypeModify;
    @FXML
    JFXTextField txtUsernameModify;
    @FXML
    JFXTextField txtPasswordModify;
    @FXML
    Button btnModifyUser;
    @FXML
    JFXTextField txtUserIDDelete;
    @FXML
    JFXTextField txtFirstNameDelete;
    @FXML
    JFXTextField txtMiddleNameDelete;
    @FXML
    JFXTextField txtLastNameDelete;
    @FXML
    ComboBox cmboLanguageDelete;
    @FXML
    ComboBox cmboTypeDelete;
    @FXML
    JFXTextField txtUsernameDelete;
    @FXML
    JFXTextField txtPasswordDelete;
    @FXML
    Button btnDeleteUser;
    @FXML
    Button btnLogOut;
    @FXML
    GridPane gridAdd;
    @FXML
    GridPane gridModify;
    @FXML
    GridPane gridDelete;

    final static ObservableList<String> Languages = FXCollections.observableArrayList("English", "Spanish", "Chinese", "Filipino", "Vietnamese", "Arabic","French","Korean","Russian","German","Hindi","Haitian Creole","Hindi","Portuge","Italian","Polish","Urdu","Japanese","Dothraki","Klingon");
    final static ObservableList<String> UserTypes = FXCollections.observableArrayList("Doctor", "Nurse", "Visitor", "Admin", "Janitor", "Interpreter", "Patient", "Security");

    @FXML
    public void initialize() throws Exception{
        try {
            System.out.println("initializing");
            gridDelete.setVisible((false));
            gridDelete.setDisable(true);
            gridModify.setVisible(false);
            gridModify.setDisable(true);
            gridAdd.setDisable(false);
            gridAdd.setVisible(true);
            btnAddUser.setDisable(true);
            BooleanBinding booleanBind = Bindings.or(txtFirstNameAdd.textProperty().isEmpty(),
                    txtMiddleNameAdd.textProperty().isEmpty()).or(txtLastNameAdd.textProperty().isEmpty()).or(txtUsernameAdd.textProperty().isEmpty()).or(txtPasswordAdd.textProperty().isEmpty());
            btnAddUser.disableProperty().bind(booleanBind);
            cmboLanguageAdd.setItems(Languages);
            cmboTypeAdd.setItems(UserTypes);



        }
        catch (Exception e){
            e.printStackTrace();}
    }
    //-----------------Swapping Grids-------------/
    public void displayModifyGrid(ActionEvent event){
        gridDelete.setVisible((false));
        gridDelete.setDisable(true);
        gridModify.setVisible(true);
        gridModify.setDisable(false);
        gridAdd.setDisable(true);
        gridAdd.setVisible(false);
        btnModifyUser.setDisable(true);
        BooleanBinding booleanBind = Bindings.or(txtFirstNameModify.textProperty().isEmpty(),
                txtMiddleNameModify.textProperty().isEmpty()).or(txtLastNameModify.textProperty().isEmpty()).or(txtUsernameModify.textProperty().isEmpty()).or(txtPasswordModify.textProperty().isEmpty());
        btnModifyUser.disableProperty().bind(booleanBind);
        cmboLanguageModify.setItems(Languages);
        cmboTypeModify.setItems(UserTypes);

    }
    public void displayDeleteGrid(ActionEvent event){
        gridDelete.setVisible((true));
        gridDelete.setDisable(false);
        gridModify.setVisible(false);
        gridModify.setDisable(true);
        gridAdd.setDisable(true);
        gridAdd.setVisible(false);
        btnDeleteUser.setDisable(true);
        BooleanBinding booleanBind = Bindings.or(txtFirstNameDelete.textProperty().isEmpty(),
                txtMiddleNameDelete.textProperty().isEmpty()).or(txtLastNameDelete.textProperty().isEmpty()).or(txtUsernameDelete.textProperty().isEmpty()).or(txtPasswordDelete.textProperty().isEmpty());
        btnDeleteUser.disableProperty().bind(booleanBind);
        cmboLanguageDelete.setItems(Languages);
        cmboTypeDelete.setItems(UserTypes);

    }
    public void displayAddGrid(ActionEvent event) {
        gridDelete.setVisible((false));
        gridDelete.setDisable(true);
        gridModify.setVisible(false);
        gridModify.setDisable(true);
        gridAdd.setDisable(false);
        gridAdd.setVisible(true);
        btnAddUser.setDisable(true);
        BooleanBinding booleanBind = Bindings.or(txtFirstNameAdd.textProperty().isEmpty(),
                txtMiddleNameAdd.textProperty().isEmpty()).or(txtLastNameAdd.textProperty().isEmpty()).or(txtUsernameAdd.textProperty().isEmpty()).or(txtPasswordAdd.textProperty().isEmpty());
        btnAddUser.disableProperty().bind(booleanBind);
        cmboLanguageModify.setItems(Languages);
        cmboTypeModify.setItems(UserTypes);
    }
    public void logOut(javafx.event.ActionEvent event) throws Exception {
        try {
            Stage stage;
            Parent root;
            //get reference to the button's stage
            stage = (Stage) btnLogOut.getScene().getWindow();
            //load up Home FXML document;
            logout = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/home.fxml"));

            KioskInfo.currentUserID = "";

            //create a new scene with root and set the stage
            Scene scene = new Scene(logout);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void manageRequest (ActionEvent event) throws Exception {
        try {
            Stage stage;
            Parent root;
            //get reference to the button's stage
            stage = (Stage) btnLogOut.getScene().getWindow();
            //load up Home FXML document;
            manageRequests = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/adminRequestDashBoard.fxml"));
            //create a new scene with root and set the stage
            Scene scene = new Scene(manageRequests);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void nodeEditor(ActionEvent event) throws Exception {
        try {
            Stage stage;
            Parent root;
            //get reference to the button's stage
            stage = (Stage) btnLogOut.getScene().getWindow();
            //load up Home FXML document;
            nodeEdit = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/nodeEditor.fxml"));
            //create a new scene with root and set the stage
            Scene scene = new Scene(nodeEdit);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLanguageAdd(ActionEvent event){

    }
}
