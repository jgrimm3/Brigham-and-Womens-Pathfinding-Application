package com.manlyminotaurs.viewControllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.manlyminotaurs.core.KioskInfo;
import com.manlyminotaurs.databases.DataModelI;
import com.manlyminotaurs.users.User;
import com.manlyminotaursAPI.core.RoomService;
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

    /**
     * sets up the screen when loaded into view
     * @throws Exception
     */
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

            DataModelI.getInstance().updateUserDerby(DataModelI.getInstance().retrieveUserFirebase());


        }
        catch (Exception e){
            e.printStackTrace();}
    }

    //-----------------Swapping Grids-------------/

    /**
     * swaps out grids to display the modify screen
     * @param event
     */
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

    /**
     * swaps out grids to display the delete screen
     * @param event
     */
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

    /**
     * swaps out grids to display the add screen
     * @param event
     */
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

    /**
     * initializes the room service API and loads the API to come into view
     * @param event
     */
    public void loadAPI(ActionEvent event){


        RoomService roomService = new RoomService();
        try

        {
            roomService.run(0, 0, 1920, 1080, null, null, null);
        }catch(
                Exception e)

        {
            e.printStackTrace();
        }

    }

    /**
     * logs out user and loads up the idle map
     * @param event
     * @throws Exception
     */
    public void LogOut(javafx.event.ActionEvent event) throws Exception {
        try {
            Stage stage;
            Parent root;
            //get reference to the button's stage
            stage = (Stage) btnLogOut.getScene().getWindow();
            //load up Home FXML document;
            logout = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/idleMap.fxml"));

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

    /**
     * loads the admin history screen
     * @param event
     * @throws Exception
     */
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

    /**
     * depending on the user type, the admin manager request or user manager request sreens are loaded
     * @param event
     * @throws Exception
     */
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

    /**
     * loads the account manager screen
     * @param event
     * @throws Exception
     */
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

    /**
     * loads the node editor screen
     * @param event
     * @throws Exception
     */
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

    /**
     * language is added to language list from add language combobox
     * @param event
     */
    public void setLanguageAdd(ActionEvent event){
        languages.add(cmboLanguageAdd.getValue().toString());

    }

    /**
     * type is set to type from add type combobox
     * @param event
     */
    public void setTypeAdd(ActionEvent event){
        type = cmboTypeAdd.getValue().toString();

    }

    /**
     * language is added to language list from modify language combobox
     * @param event
     */
    public void setLanguageModify(ActionEvent event){
        languages.add(cmboLanguageModify.getValue().toString());

    }

    /**
     * type is set to type from modify type combobox
     * @param event
     */
    public void setTypeModify(ActionEvent event){
        type = cmboTypeModify.getValue().toString();

    }

    /**
     * language is added to language list from delete language combobox
     * @param event
     */
    public void setLanguageDelete(ActionEvent event){
        languages.add(cmboLanguageDelete.getValue().toString());

    }

    /**
     * type is set to type from delete combobox
     * @param event
     */
    public void setTypeDelete(ActionEvent event){
        type = cmboTypeDelete.getValue().toString();

    }

    /**
     * set user fields to selected user based on ID in modify screen
     * @param event
     */
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

    /**
     * set user fields to selected user based on ID in delete screen
     * @param event
     */
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

    /**
     * add user to database from fields
     * @param event
     */
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

    /**
     * set user fields to fields in modify screen
     * @param event
     */
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

    /**
     * deletes user based on user id from database
     * @param event
     */
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
