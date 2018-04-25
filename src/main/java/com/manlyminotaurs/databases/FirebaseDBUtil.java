package com.manlyminotaurs.databases;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.manlyminotaurs.log.Log;
import com.manlyminotaurs.messaging.Message;
import com.manlyminotaurs.messaging.Request;
import com.manlyminotaurs.messaging.RequestFactory;
import com.manlyminotaurs.users.User;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FirebaseDBUtil {

    public static void main(String[] args){
        DataModelI.getInstance().startDB();
        FirebaseDBUtil firebaseDBUtil = new FirebaseDBUtil();
        // firebaseDBUtil.updateRequestFirebase();
        // firebaseDBUtil.retrieveRequestFirebase();
        // firebaseDBUtil.updateLogFirebase();
        // firebaseDBUtil.retrieveLogFirebase();
   //     firebaseDBUtil.updateUserFirebase();
        // firebaseDBUtil.retrieveUserFirebase();
    }

    Firestore firestoreDB;
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss:S");

    public void initializeFirebase(){
        FileInputStream serviceAccount = null;
        try {
            serviceAccount = new FileInputStream("./cs3733-web-app-firebase-adminsdk-r1g9x-e36575f789.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//
//        FirebaseOptions options = null;
//        try {
//            options = new FirebaseOptions.Builder()
//                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                    .setDatabaseUrl("https://cs3733-web-app.firebaseio.com/")
//                    .build();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        FirebaseApp.initializeApp(options);
//        firestoreDB = FirestoreClient.getFirestore();
        FirestoreOptions firestoreOptions = null;
        try {
            firestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setProjectId("cs3733-web-app")
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        firestoreDB = firestoreOptions.getService();
    }

    //------------------------------------------------------------------------------------------------------
    //---------------------------------------Log Starts-----------------------------------------------------
    //------------------------------------------------------------------------------------------------------

    public void updateLogFirebase(){
        List<Log> listOfLog = DataModelI.getInstance().retrieveLogData();
        for(Log aLog: listOfLog) {
            DocumentReference docRef = firestoreDB.collection("logs").document(aLog.getLogID());
            // Add document data  with id "alovelace" using a hashmap
            Map<String, Object> data = new HashMap<>();
            data.put("logID", aLog.getLogID());
            data.put("description", aLog.getDescription());
            data.put("logTime", aLog.getLogTime().format(dateTimeFormatter));
            data.put("userID", aLog.getUserID());
            data.put("associatedID", aLog.getAssociatedID());
            data.put("associatedType", aLog.getAssociatedType());
            //asynchronously write data
            ApiFuture<WriteResult> result = docRef.set(data);
            // ...
            // result.get() blocks on response
            try {
                System.out.println("Update time : " + result.get().getUpdateTime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Log> retrieveLogFirebase(){
        // asynchronously retrieve all users
        ApiFuture<QuerySnapshot> query = firestoreDB.collection("logs").get();
        // ...
        // query.get() blocks on response
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        // Variables
        List<Log> listOfLog = new ArrayList<>();
        Log logObject = null;
        String logID = "";
        String description = "";
        Timestamp logTime = null;
        String userID = "";
        String associatedID = "";
        String associatedType = "";

        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            logID = document.getString("logID");
            description = document.getString("description");
            logTime = DataModelI.getInstance().convertStringToTimestamp(document.getString("logTime"));
            userID = document.getString("userID");
            associatedID = document.getString("associatedID");
            associatedType = document.getString("associatedType");

            logObject = new Log(logID,description,logTime.toLocalDateTime(),userID,associatedID,associatedType);

            listOfLog.add(logObject);
        }
        //-----------------------------------Update Derby Database----------------------------------------------------
        return listOfLog;
    }

    public void updateLogDerby(List<Log> listOfLog){
        Connection connection = DataModelI.getInstance().getNewConnection();
        boolean isSucessful = true;
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String str = "DELETE FROM LOG";
            stmt.executeUpdate(str);
            stmt.close();
            System.out.println("Log wiped out from database");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            DataModelI.getInstance().closeConnection();
        }
        for(Log aLog: listOfLog) {
            DataModelI.getInstance().addLog(aLog);
        }
        System.out.println("updateLogDerby Done");
    }

    //------------------------------------------------------------------------------------------------------
    //---------------------------------------------Log Ends-------------------------------------------------
    //------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------
    //------------------------------------------Request Starts----------------------------------------------
    //------------------------------------------------------------------------------------------------------


    public void updateRequestFirebase(){
        List<Request> listOfRequest = DataModelI.getInstance().retrieveRequests();
        for(Request aRequest: listOfRequest) {
            DocumentReference docRef = firestoreDB.collection("requests").document(aRequest.getRequestID());
            // Add document data  with id "alovelace" using a hashmap
            Map<String, Object> data = new HashMap<>();
            data.put("requestID", aRequest.getRequestID());
            data.put("requestType", aRequest.getRequestType());
            data.put("priority", aRequest.getPriority());
            data.put("isComplete", aRequest.getComplete());
            data.put("adminConfirm", aRequest.getAdminConfirm());
            data.put("startTime", aRequest.getStartTime().format(dateTimeFormatter));
            data.put("endTime", aRequest.getEndTime().format(dateTimeFormatter));
            data.put("nodeID", aRequest.getNodeID());
            data.put("messageID", aRequest.getMessageID());
            data.put("password", aRequest.getPassword());
            data.put("deleteTime", aRequest.getDeleteTime());
            //asynchronously write data
            ApiFuture<WriteResult> result = docRef.set(data);
            // ...
            // result.get() blocks on response
            try {
                System.out.println("Update time : " + result.get().getUpdateTime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Request> retrieveRequestFirebase(){
        //---------------------------Retrieve data from Firebase database-----------------------------------------------
        // asynchronously retrieve all users
        ApiFuture<QuerySnapshot> query = firestoreDB.collection("requests").get();
        // ...
        // query.get() blocks on response
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        // Variables
        List<Request> listOfRequest = new ArrayList<>();
        RequestFactory rFactory = new RequestFactory();
        Request requestObject;
        String requestID;
        String requestType;
        int priority;
        Boolean isComplete;
        Boolean adminConfirm;
        Timestamp startTimestamp;
        Timestamp endTimestamp;
        String nodeID;
        String messageID;
        String password;
        Timestamp deleteTimestamp = null;
        LocalDateTime deleteTime = null;

        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            requestID = document.getString("requestID");
            requestType = document.getString("requestType");
            priority = Math.toIntExact(document.getLong("priority"));
            adminConfirm = document.getBoolean("adminConfirm");
            isComplete = document.getBoolean("isComplete");
            startTimestamp = DataModelI.getInstance().convertStringToTimestamp(document.getString("startTime"));
            endTimestamp = DataModelI.getInstance().convertStringToTimestamp(document.getString("endTime"));
            nodeID = document.getString("nodeID");
            messageID = document.getString("messageID");
            password = document.getString("password");
            deleteTimestamp = DataModelI.getInstance().convertStringToTimestamp(document.getString("deleteTime"));
            if(deleteTimestamp != null) {
                deleteTime = deleteTimestamp.toLocalDateTime();
            } else{
                deleteTime = null;
            }

            requestObject = rFactory.genExistingRequest(requestID, requestType, priority, isComplete, adminConfirm, startTimestamp.toLocalDateTime(), endTimestamp.toLocalDateTime(),nodeID, messageID, password);
            requestObject.setDeleteTime(deleteTime);

            listOfRequest.add(requestObject);
        }
        //-----------------------------------Update Derby Database----------------------------------------------------
        return listOfRequest;
    }

    public void updateRequestDerby(List<Request> listOfRequest){
        Connection connection = DataModelI.getInstance().getNewConnection();
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String str = "DELETE FROM REQUEST";
            stmt.executeUpdate(str);
            stmt.close();
            System.out.println("Request wiped out from database");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            DataModelI.getInstance().closeConnection();
        }
        for(Request aRequest: listOfRequest) {
            DataModelI.getInstance().addRequest(aRequest);
        }
        System.out.println("updateRequestDerby Done");
    }

    public void removeRequestFirebase(String requestID){
        ApiFuture<WriteResult> writeResult = firestoreDB.collection("requests").document(requestID).delete();
        try {
            System.out.println("Update time : " + writeResult.get().getUpdateTime());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    //------------------------------------------------------------------------------------------------------
    //-------------------------------------------Request Ends-----------------------------------------------
    //------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------
    //--------------------------------------------User Starts-----------------------------------------------
    //------------------------------------------------------------------------------------------------------


    public void updateUserFirebase(){
        List<User> listOfUser = DataModelI.getInstance().retrieveUsers();
        for(User aUser: listOfUser) {
            DocumentReference docRef = firestoreDB.collection("users").document(aUser.getUserID());

            Map<String, Object> data = new HashMap<>();
            data.put("userID", aUser.getUserID());
            data.put("firstName", aUser.getFirstName());
            data.put("middleName", aUser.getMiddleName());
            data.put("lastName", aUser.getLastName());
            data.put("language", DataModelI.getInstance().getLanguageString(aUser.getLanguages()));
            data.put("userType", aUser.getUserType());
            data.put("deleteTime",aUser.getDeleteTime());
            //asynchronously write data
            ApiFuture<WriteResult> result = docRef.set(data);
            // ...
            // result.get() blocks on response
            try {
                System.out.println("Update time : " + result.get().getUpdateTime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public List<User> retrieveUserFirebase(){
        // asynchronously retrieve all users
        ApiFuture<QuerySnapshot> query = firestoreDB.collection("users").get();
        // ...
        // query.get() blocks on response
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        // Variables
        List<User> listOfUser = new ArrayList<>();
        User userObject;
        String userID;
        String firstName;
        String middleName;
        String lastName;
        String languagesConcat;
        String userType;
        Timestamp deleteTimestamp = null;
        LocalDateTime deleteTime = null;


        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            userID = document.getString("userID");
            firstName = document.getString("firstName");
            middleName = document.getString("middleName");
            lastName = document.getString("lastName");
            languagesConcat = document.getString("language");
            userType = document.getString("userType");
            deleteTimestamp = DataModelI.getInstance().convertStringToTimestamp(document.getString("deleteTime"));
            if(deleteTimestamp != null) {
                deleteTime = deleteTimestamp.toLocalDateTime();
            } else{
                deleteTime = null;
            }

            userObject = UserDBUtil.userBuilder(userID, firstName, middleName, lastName, DataModelI.getInstance().getLanguageList(languagesConcat), userType);
            userObject.setDeleteTime(deleteTime);
            listOfUser.add(userObject);
        }

        return listOfUser;
    }

    public void updateUserDerby(List<User> listOfUser){
        Connection connection = DataModelI.getInstance().getNewConnection();
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String str = "DELETE FROM USERACCOUNT";
            stmt.executeUpdate(str);
            stmt.close();
            System.out.println("User wiped out from database");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            DataModelI.getInstance().closeConnection();
        }
        for(User aUser: listOfUser) {
            DataModelI.getInstance().addUser(aUser);
        }
        System.out.println("updateUserDerby Done");
    }

    public void removeUserFirebase(String userID){
        ApiFuture<WriteResult> writeResult = firestoreDB.collection("users").document(userID).delete();
        try {
            System.out.println("Update time : " + writeResult.get().getUpdateTime());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    //------------------------------------------------------------------------------------------------------
    //---------------------------------------------User Ends------------------------------------------------
    //------------------------------------------------------------------------------------------------------

    //------------------------------------------------------------------------------------------------------
    //--------------------------------------------Nodes Start-----------------------------------------------
    //------------------------------------------------------------------------------------------------------

    public void updateNodeFirebase(){

    }

}
