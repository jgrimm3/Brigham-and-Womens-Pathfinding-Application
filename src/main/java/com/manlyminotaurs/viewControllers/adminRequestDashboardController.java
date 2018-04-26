package com.manlyminotaurs.viewControllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.manlyminotaurs.core.KioskInfo;
import com.manlyminotaurs.databases.DataModelI;
import com.manlyminotaurs.messaging.Message;
import com.manlyminotaurs.messaging.Request;
import com.manlyminotaurs.users.User;
import com.manlyminotaursAPI.core.RoomService;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

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

        /**
         * gets the type of a request
         * @return request type
         */
        public String getRequestType() {
            return requestType;
        }

        /**
         * gets the message of a request
         * @return message
         */
        public String getMessage() {
            return message;
        }

        /**
         * gets the assign status of a request
         * @return true or false depending if the request has been assigned
         */
        public Boolean getIsAssigned() {
            return isAssigned;
        }
    }

    @FXML
    JFXButton navBtnManageAccounts;
    @FXML
    Button btnEmergency;
    @FXML
    TableView tblOpenRequests;
    @FXML
    JFXButton btnCompleteRequest;
    @FXML
    JFXButton btnDeleteRequest;
    @FXML
    TableView tblClosedRequests;
    @FXML
    Label lblRequestDetails;
    @FXML
    JFXButton btnLogOut;
    @FXML
    Parent logout;
    @FXML
    JFXComboBox<String> combBoxAssignNurse;
    @FXML
    Parent createRequest;
    @FXML
    PieChart pieChart;
    @FXML
    JFXPasswordField txtPassword;
    @FXML
    Label lblCompleteError;
    @FXML
    JFXButton btnCreateRequest;
    @FXML
    JFXButton navBtnNodeEditor;
    @FXML
    JFXButton btnHistory;
    @FXML
    JFXButton btnRoomServiceAPI;
    @FXML
    Label lblCompleted;
    @FXML
    Label lblDeleted;


    Parent nodeEdit;
    Parent accountManager;
    Parent history;

    /**
     * sets up request tables, populates them, and sets up pie chart
     * @throws Exception
     */
    @FXML
    public void initialize() throws Exception{
        try{
            KioskInfo.myStage.removeEventFilter(InputEvent.ANY, KioskInfo.myHandler);
            if(KioskInfo.myTimer != null){
                KioskInfo.myTimer.cancel();
            }
            
            reqestList.clear();
         //   dBUtil.updateRequestDerby(dBUtil.retrieveRequestFirebase());
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
                            new PieChart.Data("High Priority", reqestList.stream().filter(request -> request.getPriority()==1).count()),
                            new PieChart.Data("Med Priority", reqestList.stream().filter(request -> request.getPriority()==2).count()),
                             new PieChart.Data("Low Priority", reqestList.stream().filter(request -> request.getPriority()==3).count()));

            pieChart.getData().clear();
            pieChart.setData(pieChartData);

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * loads emergency screen
     * @param actionEvent
     */
    @FXML
    public void setEmergency(ActionEvent actionEvent) {
        try{
            Stage stage;
            //get reference to the button's stage
            stage=(Stage)btnEmergency.getScene().getWindow();
            //load up Home FXML document
            logout = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/emergencyScreen.fxml"));

            KioskInfo.currentUserID = "";

            //create a new scene with root and set the stage
            Scene scene=new Scene(logout);
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();}
    }

    /**
     * loads room service api
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
     * loads idle map screen
     * @param event
     */
    public void LogOut(ActionEvent event){
        try{
            Stage stage;
            //get reference to the button's stage
            stage=(Stage)btnLogOut.getScene().getWindow();
            //load up Home FXML document
            logout = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/idleMap.fxml"));

            KioskInfo.currentUserID = "";

            //create a new scene with root and set the stage
            Scene scene=new Scene(logout);
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();}
    }

    /**
     * laods account manager screen
     * @param event
     * @throws Exception
     */
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

    /**
     * loads create screen
     * @param event
     * @throws Exception
     */
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

    /**
     * loads admin history screen
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
     * loads node editor screen
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
     * loads request details based on selected request from open list
     */
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

    /**
     * loads request details based on selected request from closed list
     */
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

    /**
     * complete selected request
     */
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

            FadeTransition fadeTransition = new FadeTransition(Duration.millis(4000), lblCompleted);
            fadeTransition.setFromValue(1);
            fadeTransition.setToValue(0);
            fadeTransition.setCycleCount(1);
            fadeTransition.setAutoReverse(true);
            fadeTransition.play();
        }
    }

    /**
     * selected nurse information is set to request
     */
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

    /**
     * deletes selected request
     */
    public void deleteSelected(){
        if(tblOpenRequests.getSelectionModel().getSelectedItem() == null){}
        else {
            requestInfo selectedRequest = (requestInfo) tblOpenRequests.getSelectionModel().getSelectedItem();

            openList.remove(selectedRequest);

            dBUtil.removeMessage(dBUtil.getRequestByID(selectedRequest.requestID).getMessageID());
            dBUtil.removeRequest(selectedRequest.requestID);

            FadeTransition fadeTransition = new FadeTransition(Duration.millis(4000), lblDeleted);
            fadeTransition.setFromValue(1);
            fadeTransition.setToValue(0);
            fadeTransition.setCycleCount(1);
            fadeTransition.setAutoReverse(true);
            fadeTransition.play();
        }
    }

    /**
     * tests open request table
     */
    public void testController(){
        System.out.println("YOU SUMMONED ME?" +  tblOpenRequests.getSelectionModel().getSelectedItem());
    }


}