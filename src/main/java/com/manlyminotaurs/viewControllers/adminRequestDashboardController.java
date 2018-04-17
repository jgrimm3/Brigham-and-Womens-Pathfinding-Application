package com.manlyminotaurs.viewControllers;

import com.jfoenix.controls.JFXProgressBar;
import com.manlyminotaurs.core.KioskInfo;
import com.manlyminotaurs.databases.DataModelI;
import com.manlyminotaurs.messaging.Message;
import com.manlyminotaurs.messaging.Request;
import com.manlyminotaurs.users.User;
import com.manlyminotaurs.users.UserType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;


public class adminRequestDashboardController {
    DataModelI dBUtil = DataModelI.getInstance();
    ObservableList<requestInfo> openList = FXCollections.observableArrayList();
    ObservableList<requestInfo> closedList = FXCollections.observableArrayList();
    ObservableList<Request> reqestList = FXCollections.observableArrayList(dBUtil.retrieveRequests());
    ObservableList<PieChart.Data> pieChartData;
    Parent manageAcc;
    public class requestInfo{
        protected String requestID;
        String requestType;
        String message;
        Boolean isAssigned;


        requestInfo(String requestType, String message, Boolean isAssigned, String requestID){
            this.requestType = requestType;
            this.message = message;
            this.isAssigned = isAssigned;
            this.requestID = requestID;
        }

        public String getRequestType() {
            return requestType;
        }

        public String getMessage() {
            return message;
        }

        public Boolean getIsAssigned() {
            return isAssigned;
        }
    }
    @FXML
Button navManageAcc;

    @FXML
    Button btnEmergency;
    @FXML
    TableView tblOpenRequests;
    @FXML
    Button btnCompleteRequest;
    @FXML
    Button btnDeleteRequest;
    @FXML
    TableView tblClosedRequests;
    @FXML
    Label lblRequestDetails;
    @FXML
    Button btnLogOut;
    @FXML
    Parent logout;
    @FXML
    ComboBox<String> combBoxAssignNurse;
    @FXML
    Parent createRequest;
    @FXML
    PieChart pieChart;
    @FXML
    PasswordField txtPassword;
    @FXML
    Label lblCompleteError;
    @FXML
    Button navBtnManageAccounts;

    Parent nodeEdit;
    Parent accountManager;

    @FXML
    public void initialize() throws Exception{
        try{
            reqestList.clear();
            reqestList.setAll(dBUtil.retrieveRequests());

//            logout = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/home.fxml"));
            //OPEN LIST-----------------------
            TableColumn typeColOpen = new TableColumn("Request Type");
            TableColumn msgColOpen = new TableColumn("Request Message");
            TableColumn isAssignedColOpen = new TableColumn("Is Assigned");

            tblOpenRequests.getColumns().addAll(typeColOpen, msgColOpen, isAssignedColOpen);

            typeColOpen.setCellValueFactory(new PropertyValueFactory<requestInfo, String>("requestType"));
            msgColOpen.setCellValueFactory(new PropertyValueFactory<requestInfo, String>("message"));
            isAssignedColOpen.setCellValueFactory(new PropertyValueFactory<requestInfo, Boolean>("isAssigned"));

            //CLOSED LIST----------------------
            TableColumn typeColClosed = new TableColumn("Request Type");
            TableColumn msgColClosed = new TableColumn("Request Message");
            TableColumn reqConfirmedClosed = new TableColumn("Was Confirmed");

            tblClosedRequests.getColumns().addAll(typeColClosed, msgColClosed, reqConfirmedClosed);

            typeColClosed.setCellValueFactory(new PropertyValueFactory<requestInfo, String>("requestType"));
            msgColClosed.setCellValueFactory(new PropertyValueFactory<requestInfo, String>("message"));
            reqConfirmedClosed.setCellValueFactory(new PropertyValueFactory<requestInfo, String>("isAssigned"));

            //POPULATE LISTS----------------------------------------
            for(Request currReq : reqestList) {
                if (!currReq.getComplete()) {
                    openList.add(new requestInfo(currReq.getRequestType(), dBUtil.getMessageByID(currReq.getMessageID()).getMessage(), currReq.getAdminConfirm(), currReq.getRequestID()));
                } else {
                    closedList.add(new requestInfo(currReq.getRequestType(), dBUtil.getMessageByID(currReq.getMessageID()).getMessage(), currReq.getAdminConfirm(), currReq.getRequestID()));
                }
            }

            tblOpenRequests.setItems(openList);
            tblClosedRequests.setItems(closedList);

            List<User> userList = dBUtil.retrieveUsers();
            List<String> nurseNames = new ArrayList<>();
            for(User currUser : userList){
                if(currUser != null && (currUser.isType("Doctor") || (currUser.isType("Nurse")))){
                    nurseNames.add(currUser.getFirstName() + " " + currUser.getLastName());
                }
            }

            combBoxAssignNurse.setItems(FXCollections.observableArrayList(nurseNames));

            System.out.println("Number of Requests: " + reqestList);

            pieChartData =
                    FXCollections.observableArrayList(
                            new PieChart.Data("Low Priority", reqestList.stream().filter(request -> request.getPriority()==1).count()),
                            new PieChart.Data("Med Priority", reqestList.stream().filter(request -> request.getPriority()==2).count()),
                            new PieChart.Data("High Priority", reqestList.stream().filter(request -> request.getPriority()==3).count()));
            pieChart.getData().clear();
            pieChart.setData(pieChartData);

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @FXML
    public void setEmergency(ActionEvent actionEvent) {
        try{
            Stage stage;
            //get reference to the button's stage
            stage=(Stage)btnEmergency.getScene().getWindow();
            //load up Home FXML document
            logout = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/btnEmergency.fxml"));

            KioskInfo.currentUserID = "";

            //create a new scene with root and set the stage
            Scene scene=new Scene(logout);
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();}
    }

    public void logOut(ActionEvent event){
        try{
            Stage stage;
            //get reference to the button's stage
            stage=(Stage)btnLogOut.getScene().getWindow();
            //load up Home FXML document
            logout = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/home.fxml"));

            KioskInfo.currentUserID = "";

            //create a new scene with root and set the stage
            Scene scene=new Scene(logout);
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();}
    }
    public void accountManager(ActionEvent event) throws Exception {
        try {
            Stage stage;
            Parent root;
            //get reference to the button's stage
            stage = (Stage) navBtnManageAccounts.getScene().getWindow();
            //load up Home FXML document;
            accountManager = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/accountManager.fxml"));

            //create a new scene with root and set the stage
            Scene scene = new Scene(accountManager);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void createRequest(ActionEvent event) throws Exception {
        try {
            Stage stage;
            Parent root;
            //get reference to the button's stage
            stage = (Stage) btnLogOut.getScene().getWindow();
            //load up Home FXML document;
            createRequest = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/CreateRequest.fxml"));
            //create a new scene with root and set the stage
            Scene scene = new Scene(createRequest);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void manageAcc(ActionEvent event) throws Exception {
        try {
            Stage stage;
            Parent root;
            //get reference to the button's stage
            stage = (Stage) btnLogOut.getScene().getWindow();
            //load up Home FXML document;
            manageAcc = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/accountManager.fxml"));
            //create a new scene with root and set the stage
            Scene scene = new Scene(manageAcc);
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
            nodeEdit = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLS/nodeEditor.fxml"));
            //create a new scene with root and set the stage
            Scene scene = new Scene(nodeEdit);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openListClicked(){
        if(tblOpenRequests.getSelectionModel().getSelectedItem() == null){
            lblRequestDetails.setText("");
        }
        else {
            requestInfo selectedRequest = (requestInfo) tblOpenRequests.getSelectionModel().getSelectedItem();
            Request actualRequest = dBUtil.getRequestByID(selectedRequest.requestID);
            lblRequestDetails.setText("SenderID: " + dBUtil.getMessageByID(actualRequest.getMessageID()).getSenderID() + "\n" +
                    "Priority: " + dBUtil.getRequestByID(selectedRequest.requestID).getPriority() + "\n" +
                    "Location: " + dBUtil.getNodeByIDFromList(actualRequest.getNodeID(), dBUtil.retrieveNodes()).getLongName() + "\n" +
                    "Message: " + dBUtil.getMessageByID(actualRequest.getMessageID()).getMessage());
        }
    }

    public void closedListClicked(){
        if(tblClosedRequests.getSelectionModel().getSelectedItem() == null){
            lblRequestDetails.setText("");
        }
        else {
            requestInfo selectedRequest = (requestInfo) tblClosedRequests.getSelectionModel().getSelectedItem();
            Request actualRequest = dBUtil.getRequestByID(selectedRequest.requestID);
            lblRequestDetails.setText("SenderID: " + dBUtil.getMessageByID(actualRequest.getMessageID()).getSenderID() + "\n" +
                    "Priority: " + dBUtil.getRequestByID(selectedRequest.requestID).getPriority() + "\n" +
                    "Location: " + dBUtil.getNodeByIDFromList(actualRequest.getNodeID(), dBUtil.retrieveNodes()).getLongName() + "\n" +
                    "Message: " + dBUtil.getMessageByID(actualRequest.getMessageID()).getMessage());
        }
    }

    public void completeClicked() {
        if (tblOpenRequests.getSelectionModel().getSelectedItem() == null) {
        } else {

            requestInfo selectedRequest = (requestInfo) tblOpenRequests.getSelectionModel().getSelectedItem();
            if(txtPassword.getText().equals("admin")){
                closedList.add((requestInfo)tblOpenRequests.getSelectionModel().getSelectedItem());
                openList.remove(tblOpenRequests.getSelectionModel().getSelectedItem());

                lblCompleteError.setText("");

                Request newReq = dBUtil.getRequestByID(selectedRequest.requestID);
                newReq.setComplete(true);
                dBUtil.modifyRequest(newReq);
            }else{
                lblCompleteError.setText("Incorrect Password");
            }
            txtPassword.clear();
        }
    }

    public void nurseSelected(){
        if(tblOpenRequests.getSelectionModel().getSelectedItem() == null){}
        else {
            requestInfo selectedRequest = (requestInfo) tblOpenRequests.getSelectionModel().getSelectedItem();
            String NurseName = combBoxAssignNurse.getSelectionModel().getSelectedItem();
            System.out.println("You have selected: " + combBoxAssignNurse.getSelectionModel().getSelectedItem());

            (openList.get(openList.indexOf(selectedRequest))).isAssigned = true;

            Request newReq = dBUtil.getRequestByID(selectedRequest.requestID);
            Message newMsg = dBUtil.getMessageByID(newReq.getMessageID());
            newReq.setAdminConfirm(true);
            newMsg.setReceiverID(NurseName);

            dBUtil.modifyRequest(newReq);
        }
    }

    public void deleteSelected(){
        if(tblOpenRequests.getSelectionModel().getSelectedItem() == null){}
        else {
            requestInfo selectedRequest = (requestInfo) tblOpenRequests.getSelectionModel().getSelectedItem();

            openList.remove(selectedRequest);

            dBUtil.removeMessage(dBUtil.getMessageByID(dBUtil.getRequestByID(selectedRequest.requestID).getMessageID()));
            dBUtil.removeRequest(dBUtil.getRequestByID(selectedRequest.requestID));
        }
    }






    public void testController(){
        System.out.println("YOU SUMMONED ME?" +  tblOpenRequests.getSelectionModel().getSelectedItem());
    }
}