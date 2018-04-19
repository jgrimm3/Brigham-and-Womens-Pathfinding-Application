package com.manlyminotaurs.communications;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * A simple Swing-based client for the chat server.  Graphically
 * it is a frame with a text field for entering messages and a
 * textarea to see the whole dialog.
 *
 * The client follows the Chat Protocol which is as follows.
 * When the server sends "SUBMITNAME" the client replies with the
 * desired screen name.  The server will keep sending "SUBMITNAME"
 * requests as long as the client submits screen names that are
 * already in use.  When the server sends a line beginning
 * with "NAMEACCEPTED" the client is now allowed to start
 * sending the server arbitrary strings to be broadcast to all
 * chatters connected to the server.  When the server sends a
 * line beginning with "MESSAGE " then all characters following
 * this string should be displayed in its message area.
 */
public class ClientSetup {

    BufferedReader in;
    PrintWriter out;
    Stage stage;
    public static String IP;
    /**
     * Constructs the client by laying out the GUI and registering a
     * listener with the textfield so that pressing Return in the
     * listener sends the textfield contents to the server.  Note
     * however that the textfield is initially NOT editable, and
     * only becomes editable AFTER the client receives the NAMEACCEPTED
     * message from the server.
     */
    public ClientSetup(Stage stage) {
        try {
            // Make connection and initialize streams
            Socket socket = new Socket(IP, 9001);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            //connect To Server
            out.println("NEWCONNECTION");
            this.stage = stage;
            while(!in.readLine().startsWith("NAMEACCEPTED")){}
            spoolUpClient();
        } catch (IOException ioe){
            ioe.printStackTrace();
        }

    }

    public void spoolUpClient() {
        Thread serverThread = new Thread() {
            public void run() {
                while (true) {
                    try {
                        String line = in.readLine();
                        if (line.startsWith("EMERGENCY")) {
                            if(stage != null){
                                // Avoid throwing IllegalStateException by running from a non-JavaFX thread.
                                Platform.runLater(
                                        () -> {
                                            Parent root;
                                            //load up Home FXML document;
                                            try {
                                                root = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/emergencyScreen.fxml"));
                                                //create a new scene with root and set the stage
                                                Scene scene = new Scene(root);
                                                stage.setScene(scene);
                                                stage.show();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                );
                            }
                            System.out.println("ENTERING EMERGENCY MODE!");
                        } else if (line.startsWith("Reset")) {
                            if(stage != null){
                                // Avoid throwing IllegalStateException by running from a non-JavaFX thread.
                                Platform.runLater(
                                        () -> {
                                            Parent root;
                                            //load up Home FXML document;
                                            try {
                                                root = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/home.fxml"));
                                                //create a new scene with root and set the stage
                                                Scene scene = new Scene(root);
                                                stage.setScene(scene);
                                                stage.show();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                );
                            }
                            System.out.println("Resuming Standard Operation");
                        } else {
                            System.out.println("Unknown Message Recieved");
                        }
                    } catch(IOException ioe){
                        ioe.printStackTrace();
                    }
                }
            }
        };
        serverThread.start();
    }

    public void sendEmergency(){
        out.println("EMERGENCY");
    }

    public String requestState() {
        out.println("STATE");
        try {
            return in.readLine();
        } catch (Exception e) {
            return null;
        }
    }
    public void sendReset(){
        out.println("Reset");
    }
}

