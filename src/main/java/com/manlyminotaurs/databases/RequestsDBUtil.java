package com.manlyminotaurs.databases;

import com.manlyminotaurs.messaging.Message;
import com.manlyminotaurs.messaging.Request;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class RequestsDBUtil {

    public void addRequest(String RequestType, int priority,  String NodeID, String Message, String senderID){}
    public ObservableList<Request> searchRequest(String UserID){ return FXCollections.observableArrayList(
            new Request("", "Nurse Request", 5, false, false, "", ""));
    }
    public void setIsConfimed(int requestID, boolean newConfirmedStatus){}
    public void setIsCompleted(int requestID, boolean newCompletedStatus){}

}
