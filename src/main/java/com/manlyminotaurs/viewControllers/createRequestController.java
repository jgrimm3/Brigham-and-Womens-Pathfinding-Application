package com.manlyminotaurs.viewControllers;

import com.manlyminotaurs.databases.DataModelI;
import com.manlyminotaurs.messaging.RequestFactory;
import com.manlyminotaurs.messaging.RequestType;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Path;
import javafx.stage.Stage;

public class createRequestController{
    DataModelI dbUtil = DataModelI.getInstance();
    RequestFactory rFactory = new RequestFactory();

    Parent manageRequests;
    Parent nodeEdit;

    @FXML
    ScrollPane scrollPane;
    @FXML
    ImageView mapImg;
    @FXML
    Pane pane;
    @FXML
    Path path;
    @FXML
    Pane paneAdd;
    @FXML
    ComboBox<String> cmboBuilding;
    @FXML
    Button btnSubmitRequest;
    @FXML
    ComboBox<String> cmboType;
    @FXML
    ComboBox<String> cmboNode;
    @FXML
    CheckBox chkHighPriority;
    @FXML
    CheckBox chkMedPriority;
    @FXML
    CheckBox chkLowPriority;
    @FXML
    Button btnlogOut;
    @FXML
    TextArea txtMessage;
    @FXML
    Label lblError;
    @FXML
    ComboBox<RequestType> cmboReqType;
    @FXML
    Button btnLogOut;
    @FXML
    Button navBtnManageRequests;
    @FXML
    Button navBtnNodeEditor;

    String requestType;
    String message;
    int priority;

    @FXML
    protected void initialize() {
        cmboReqType.setItems(FXCollections.observableArrayList(RequestType.values()));
    }

    public void submitRequest(javafx.event.ActionEvent event){
        boolean isErrored = false;
        lblError.setText("");
        if(txtMessage.getText() == ""){
            lblError.setText("A message is required");
            isErrored = true;
        }
        if(priority == 0){
            lblError.setText(lblError.getText() + "\n" +
                    "A priority must be selected");
            isErrored = true;
        }
        if(cmboReqType.getSelectionModel().getSelectedItem() == null){
            lblError.setText(lblError.getText() + "\n" +
                    "Please Select A Request Type");
            isErrored = true;
        }
        if(cmboBuilding.getSelectionModel().getSelectedItem() == null ||
                cmboNode.getSelectionModel().getSelectedItem() == null ||
                cmboType.getSelectionModel().getSelectedItem() == null){
            lblError.setText(lblError.getText() + "\n" +
                    "Please Select A Complete Location");
            isErrored = true;
        }
        if(!isErrored){
            rFactory.genNewRequest(cmboReqType.getSelectionModel().getSelectedItem(),
                    (dbUtil.getNodesByBuildingTypeFloor(cmboBuilding.getSelectionModel().getSelectedItem(), cmboType.getSelectionModel().getSelectedItem(), cmboNode.getSelectionModel().getSelectedItem())).get(0),
                    txtMessage.getText(), "5");
        }
    }
    public void setHighPriority(javafx.event.ActionEvent event){
        //if high priority slected, clear other selections and set integer 3
        chkHighPriority.setSelected(true);
        chkMedPriority.setSelected(false);
        chkLowPriority.setSelected(false);
        priority = 3;
    }
    public void setMedPriority(javafx.event.ActionEvent event){
        //if medium priority slected, clear other selections and set integer 2
        chkHighPriority.setSelected(false);
        chkMedPriority.setSelected(true);
        chkLowPriority.setSelected(false);
        priority = 2;
    }
    public void setLowPriority(javafx.event.ActionEvent event){
        //if medium priority slected, clear other selections and set integer 1
        chkHighPriority.setSelected(false);
        chkMedPriority.setSelected(false);
        chkLowPriority.setSelected(true);
        priority = 1;
    }

    public void getXandY(){}

    public void logOut(ActionEvent event){
        try{
            Stage stage;
            Parent root;
            //get reference to the button's stage
            stage=(Stage)btnlogOut.getScene().getWindow();
            //load up Home FXML document
            root= FXMLLoader.load(getClass().getClassLoader().getResource("/FXMLs/home.fxml"));

            //create a new scene with root and set the stage
            Scene scene=new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();}
    }
    public void manageRequests (ActionEvent event) throws Exception {
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


}