package com.manlyminotaurs.communications;

import com.sun.istack.internal.Nullable;
import javafx.scene.Scene;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

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
    Scene scene;
    /**
     * Constructs the client by laying out the GUI and registering a
     * listener with the textfield so that pressing Return in the
     * listener sends the textfield contents to the server.  Note
     * however that the textfield is initially NOT editable, and
     * only becomes editable AFTER the client receives the NAMEACCEPTED
     * message from the server.
     */
    public ClientSetup (String IP, Scene scene) {
        try {
            // Make connection and initialize streams
            Socket socket = new Socket(IP, 9001);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            //connect To Server
            out.println("NEWCONNECTION");
            this.scene = scene;
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
                            //GO INTO EMERGENCY MODE
                            System.out.println("ENTERING EMERGENCY MODE!");
                        } else if (line.startsWith("Reset")) {
                            //Return to normal operation
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


    public void sendReset(){
        out.println("Reset");
    }
}

