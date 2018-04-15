package com.manlyminotaurs.viewControllers;

import com.jfoenix.controls.JFXTextField;
import com.manlyminotaurs.core.KioskInfo;
import com.manlyminotaurs.databases.DataModelI;
import com.manlyminotaurs.users.User;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
    String firstName;
    String middleName;
    String lastName;
    List<String> languages;
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
//        cmboLanguageModify.setItems(language);
//        cmboLanguageModify.setItems(type);
    }
    public void targetUserDelete(ActionEvent event){
        userID = txtUserIDDelete.getText();
    }
    public void addUser(ActionEvent event){
        firstName = txtFirstNameAdd.getText();
        middleName = txtMiddleNameAdd.getText();
        lastName = txtLastNameAdd.getText();
        languages.add(cmboLanguageAdd.getValue().toString());
        type = cmboTypeAdd.getValue().toString();
        username = txtUsernameAdd.getText();
        password = txtPasswordAdd.getText();

        DataModelI.getInstance().addUser(firstName,middleName,lastName,languages,type,username,password);

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
        firstName = txtFirstNameDelete.getText();
        middleName = txtMiddleNameDelete.getText();
        lastName = txtLastNameDelete.getText();
        languages.add(cmboLanguageDelete.getValue().toString());
        type = cmboTypeDelete.getValue().toString();
        username = txtUsernameDelete.getText();
        password = txtPasswordDelete.getText();

        DataModelI.getInstance().removeUser(user);

    }
}
