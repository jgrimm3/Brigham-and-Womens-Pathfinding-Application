package com.manlyminotaurs.communications;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashSet;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * A multithreaded chat room server.  When a client connects the
 * server requests a screen name by sending the client the
 * text "SUBMITNAME", and keeps requesting a name until
 * a unique one is received.  After a client submits a unique
 * name, the server acknowledges with "NAMEACCEPTED".  Then
 * all messages from that client will be broadcast to all other
 * clients that have submitted a unique screen name.  The
 * broadcast messages are prefixed with "MESSAGE ".
 *
 * Because this is just a teaching example to illustrate a simple
 * chat server, there are a few features that have been left out.
 * Two are very useful and belong in production code:
 *
 *     1. The protocol should be enhanced so that the client can
 *        send clean disconnect messages to the server.
 *
 *     2. The server should do some logging.
 */
public class ChatServer {

    /**
     * The port that the server listens on.
     */
    private static final int PORT = 9001;

    /**
     * The set of all names of clients in the chat room.  Maintained
     * so that we can check that new clients are not registering name
     * already in use.
     */
    private static HashSet<String> names = new HashSet<String>();

    /**
     * The set of all the print writers for all the clients.  This
     * set is kept so we can easily broadcast messages.
     */
    private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();

    private static int state = 0; // 0 is normal, 1 is emergency

    /**
     * The appplication main method, which just listens on a port and
     * spawns handler threads.
     */
    public static void main(String[] args) throws Exception {
        System.out.println("Main: the chat server is running.");
        ServerSocket listener = new ServerSocket(PORT);
        try {
            while (true) {
                System.out.println("Main: Handler");
                Handler aHandler = new Handler(listener.accept());
                aHandler.doHandshake();
                aHandler.start();
            }
        } finally {
            listener.close();
        }
    }

    /**
     * A handler thread class.  Handlers are spawned from the listening
     * loop and are responsible for a dealing with a single client
     * and broadcasting its messages.
     */
    private static class Handler extends Thread {
        private String name;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        /**
         * Constructs a handler thread, squirreling away the socket.
         * All the interesting work is done in the run method.
         */
        public Handler(Socket socket) {
            this.socket = socket;
            doHandshake();
        }

        /**
         * Services this thread's client by repeatedly requesting a
         * screen name until a unique one has been submitted, then
         * acknowledges the name and registers the output stream for
         * the client in a global set, then repeatedly gets inputs and
         * broadcasts them.
         */
        public void run() {
            try {
                System.out.println("");
                // Create character streams for the socket.
                int socketCounter = 0;
                in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                String input = null;

                //String aSocketInput = input;//socket.getInputStream().toString();


                // Accept messages from this client and broadcast them.
                // Ignore other clients that cannot be broadcasted to.
                while ((input = in.readLine()) != null) {
//                    if (input == null) {
//                        System.out.println("----------------------Stop--------------------------");
//
//                        //return;
//                    }
                    if (".".equals(input)) {
                        out.println("good bye");
                        break;
                    }
                    if (input.startsWith("NEWCONNECTION")){
                        names.add(String.valueOf(names.size()));
                        writers.add(out);
                        out.println("NAMEACCEPTED");
                        System.out.println("New Client Added: " + writers.size());
                    } else if (input.startsWith("EMERGENCY")) {
                        System.out.println("We Have Entered Emergency Mode");
                        if (state == 0) {
                            state = 1;
                            for (PrintWriter writer : writers) {
                                writer.println("EMERGENCY" + name + " Has issued an Emergency");
                            }
                        }
                    } else if (input.startsWith("Reset")) {
                        System.out.println("We Have Entered Normal Mode");
                        if (state == 1) {
                            state = 0;
                            for (PrintWriter writer : writers) {
                                writer.println("Reset" + name + " Has Reset The System");
                            }
                        }
                    } else if (input.startsWith("STATE")) {
                        for (PrintWriter writer : writers) {
                            writer.println(state);
                        }
                    } else {
                        System.out.println("else statement: " + input);
                        decodeMessage(input);
                        decodeMessage2(input);
                        for (PrintWriter writer : writers) {
                            writer.println("received some data");
                        }
                    }
                }
            }
             catch (IOException e) {
                System.out.println(e);
            } finally {
                // This client is going down!  Remove its name and its print
                // writer from the sets, and close its socket.
                System.out.println("client going down!");
                if (name != null) {
                    names.remove(name);
                }
                if (out != null) {
                    writers.remove(out);
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //--------------handshaking to upgrade http connection to web socket connection------------------
        public void doHandshake() {
            //translate bytes of request to string
            String data = null;
            try {
                data = new Scanner(socket.getInputStream(), "UTF-8").useDelimiter("\\r\\n\\r\\n").next();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("------------------handshaking----------------");
            Matcher get = Pattern.compile("^GET").matcher(data);
            if (get.find())
            {
                Matcher match = Pattern.compile("Sec-WebSocket-Key: (.*)").matcher(data);
                match.find();
                byte[] response = new byte[0];
                try {
                    response = ("HTTP/1.1 101 Switching Protocols\r\n"
                            + "Connection: Upgrade\r\n"
                            + "Upgrade: websocket\r\n"
                            + "Sec-WebSocket-Accept: "
                            + DatatypeConverter
                            .printBase64Binary(
                                    MessageDigest
                                            .getInstance("SHA-1")
                                            .digest((match.group(1) + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11")
                                                    .getBytes("UTF-8")))
                            + "\r\n\r\n")
                            .getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                try {
                    socket.getOutputStream().write(response, 0, response.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void spoolUpServer() {
        Thread serverThread = new Thread() {
            public void run() {
                ServerSocket listener = null;
                try {
                    System.out.println("SpoolUpServer: The chat server is running.");
                    listener = new ServerSocket(PORT);

                    while (true) {
                        System.out.println("SpoolUpServer: handler");
                        Handler aHandler = new Handler(listener.accept());
                        aHandler.start();
                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                } finally {
                    try {
                        System.out.println("listener close.");
                        listener.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        serverThread.start();
    }

    static void decodeMessage(String aString){
        byte[] byteArray = new byte[0];
        try {
            byteArray = aString.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        for(Byte aByte: byteArray){
            System.out.print(Integer.parseInt(Integer.toBinaryString(aByte & 255 | 256).substring(1),2) + " ");
        }
        System.out.println("");

        for(Byte aByte: byteArray){
            System.out.print(String.format("%8s", Integer.toBinaryString(aByte & 0xFF)).replace(' ', '0') + " ");
        }
        System.out.println("");
    }

    static void decodeMessage2(String aString) throws IOException{
        byte[] _rawIn = aString.getBytes("UTF-8");
        int maskIndex = 2;
        byte[] maskBytes = new byte[4];

        if ((_rawIn[1] & (byte) 127) == 126) {
        maskIndex = 4;
        } else if ((_rawIn[1] & (byte) 127) == 127) {
        maskIndex = 10;
        }

        System.arraycopy(_rawIn, maskIndex, maskBytes, 0, 4);

        byte[] message = new byte[_rawIn.length - maskIndex - 4];

        System.out.println("");
        System.out.println("");
        for (int i = maskIndex + 4; i < _rawIn.length; i++) {
            message[i - maskIndex - 4] = (byte) (_rawIn[i] ^ maskBytes[(i - maskIndex - 4) % 4]);
        }
    //    System.out.println(message.toString() + " maskIndex: " + maskIndex);
        for(Byte aByte: message){
            System.out.print(" char: " + Integer.parseInt(Integer.toBinaryString(aByte & 255 | 256).substring(1),2) + " ");
        }
        System.out.println();
    }


}

