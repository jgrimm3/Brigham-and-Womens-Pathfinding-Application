package com.manlyminotaurs.viewControllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.manlyminotaurs.core.KioskInfo;
import com.manlyminotaurs.databases.DataModelI;
import com.manlyminotaurs.users.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.util.ArrayList;
import java.util.List;

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
    JFXComboBox cmboLanguageAdd;
    @FXML
    JFXComboBox cmboTypeAdd;
    @FXML
    JFXTextField txtUsernameAdd;
    @FXML
    JFXTextField txtPasswordAdd;
    @FXML
    JFXButton btnAddUser;
    @FXML
    JFXTextField txtUserIDModify;
    @FXML
    JFXTextField txtFirstNameModify;
    @FXML
    JFXTextField txtMiddleNameModify;
    @FXML
    JFXTextField txtLastNameModify;
    @FXML
    JFXComboBox cmboLanguageModify;
    @FXML
    JFXComboBox cmboTypeModify;
    @FXML
    JFXTextField txtUsernameModify;
    @FXML
    JFXTextField txtPasswordModify;
    @FXML
    JFXButton btnModifyUser;
    @FXML
    JFXTextField txtUserIDDelete;
    @FXML
    JFXTextField txtFirstNameDelete;
    @FXML
    JFXTextField txtMiddleNameDelete;
    @FXML
    JFXTextField txtLastNameDelete;
    @FXML
    JFXComboBox cmboLanguageDelete;
    @FXML
    JFXComboBox cmboTypeDelete;
    @FXML
    JFXTextField txtUsernameDelete;
    @FXML
    JFXTextField txtPasswordDelete;
    @FXML
    JFXButton btnDeleteUser;
    @FXML
    JFXButton btnLogOut;
    @FXML
    GridPane gridAdd;
    @FXML
    GridPane gridModify;
    @FXML
    GridPane gridDelete;
    @FXML
    JFXButton btnHistory;

    final static ObservableList<String> Languages = FXCollections.observableArrayList("English", "Spanish", "Chinese", "Filipino", "Vietnamese", "Arabic","French","Korean","Russian","German","Hindi","Haitian Creole","Hindi","Portuge","Italian","Polish","Urdu","Japanese","Dothraki","Klingon");
    final static ObservableList<String> UserTypes = FXCollections.observableArrayList("Doctor", "Nurse", "Visitor", "Admin", "Janitor", "Interpreter", "Patient", "Security");
    String firstName;
    String middleName;
    String lastName;
    List<String> languages = FXCollections.observableArrayList();
    String language;
    String type;
    String username;
    String password;
    String userID;
    User user;
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


        cmboLanguageModify.setItems(Languages);
        cmboTypeModify.setItems(UserTypes);
    }
    public void LogOut(javafx.event.ActionEvent event) throws Exception {
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
    Parent history;
    public void loadHistory(ActionEvent event) throws Exception {
        try {
            Stage stage;
            Parent root;
            //get reference to the button's stage
            stage = (Stage) btnLogOut.getScene().getWindow();
            //load up Home FXML document;
            history = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/adminHistory.fxml"));
            //create a new scene with root and set the stage
            Scene scene = new Scene(history);
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
            if (DataModelI.getInstance().getUserByID(KioskInfo.currentUserID).isType("admin")) {
                manageRequests = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/adminRequestDashBoard.fxml"));
            }else{
                manageRequests = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/userRequestDashBoard.fxml"));
            }
            //create a new scene with root and set the stage
            Scene scene = new Scene(manageRequests);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void manageAcc (ActionEvent event) throws Exception {
        try {
            Stage stage;
            Parent root;
            //get reference to the button's stage
            stage = (Stage) btnLogOut.getScene().getWindow();
            //load up Home FXML document;
            manageRequests = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/accountManager.fxml"));
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
            nodeEdit = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/NodeEditor.fxml"));
            //create a new scene with root and set the stage
            Scene scene = new Scene(nodeEdit);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLanguageAdd(ActionEvent event){
        languages.add(cmboLanguageAdd.getValue().toString());

    }
    public void setTypeAdd(ActionEvent event){
        type = cmboTypeAdd.getValue().toString();

    }
    public void setLanguageModify(ActionEvent event){
        languages.add(cmboLanguageModify.getValue().toString());

    }
    public void setTypeModify(ActionEvent event){
        type = cmboTypeModify.getValue().toString();

    }
    public void setLanguageDelete(ActionEvent event){
        languages.add(cmboLanguageDelete.getValue().toString());

    }
    public void setTypeDelete(ActionEvent event){
        type = cmboTypeDelete.getValue().toString();

    }
    public void targetUserModify(ActionEvent event){
        userID = txtUserIDModify.getText();
        firstName = DataModelI.getInstance().getUserByID(userID).getFirstName();
        middleName = DataModelI.getInstance().getUserByID(userID).getMiddleName();
        lastName = DataModelI.getInstance().getUserByID(userID).getLastName();
        languages = DataModelI.getInstance().getUserByID(userID).getLanguages();
        type = DataModelI.getInstance().getUserByID(userID).getUserType();
        txtFirstNameModify.setText(firstName);
        txtMiddleNameModify.setText(middleName);
        txtLastNameModify.setText(lastName);
        language = languages.get(0);
        cmboLanguageModify.getSelectionModel().select(language);
        cmboTypeModify.getSelectionModel().select(type);
    }
    public void targetUserDelete(ActionEvent event){
        userID = txtUserIDDelete.getText();
        firstName = DataModelI.getInstance().getUserByID(userID).getFirstName();
        middleName = DataModelI.getInstance().getUserByID(userID).getMiddleName();
        lastName = DataModelI.getInstance().getUserByID(userID).getLastName();
        languages = DataModelI.getInstance().getUserByID(userID).getLanguages();
        type = DataModelI.getInstance().getUserByID(userID).getUserType();
        txtFirstNameDelete.setText(firstName);
        txtMiddleNameDelete.setText(middleName);
        txtLastNameDelete.setText(lastName);
        language = languages.get(0);
        cmboLanguageDelete.getSelectionModel().select(language);
        cmboTypeDelete.getSelectionModel().select(type);
    }
    public void addUser(ActionEvent event){
        firstName = txtFirstNameAdd.getText();
        middleName = txtMiddleNameAdd.getText();
        lastName = txtLastNameAdd.getText();
        languages.add(cmboLanguageAdd.getValue().toString());
        type = cmboTypeAdd.getValue().toString();
        username = txtUsernameAdd.getText();
        password = txtPasswordAdd.getText();

        DataModelI.getInstance().addUser("", firstName, middleName, lastName, languages, type, username, password);

    }
    public void modifyUser(ActionEvent event){
        firstName = txtFirstNameModify.getText();
        middleName = txtMiddleNameModify.getText();
        lastName = txtLastNameModify.getText();
        languages = new ArrayList<>();
        languages.add(cmboLanguageModify.getValue().toString());
        type = cmboTypeModify.getValue().toString();
        username = txtUsernameModify.getText();
        password = txtPasswordModify.getText();

        DataModelI.getInstance().getUserByID(userID).setFirstName(firstName);
        DataModelI.getInstance().getUserByID(userID).setLastName(lastName);
        DataModelI.getInstance().getUserByID(userID).setMiddleName(middleName);
        DataModelI.getInstance().getUserByID(userID).setUserType(type);
        DataModelI.getInstance().getUserByID(userID).addLanguage(language);

    }
    public void deleteUser(ActionEvent event){
        /*firstName = txtFirstNameDelete.getText();
        middleName = txtMiddleNameDelete.getText();
        lastName = txtLastNameDelete.getText();
        languages.add(cmboLanguageDelete.getValue().toString());
        type = cmboTypeDelete.getValue().toString();
        username = txtUsernameDelete.getText();
        password = txtPasswordDelete.getText();*/
        userID = txtUserIDDelete.getText();

        DataModelI.getInstance().removeUser(userID);

    }
}
