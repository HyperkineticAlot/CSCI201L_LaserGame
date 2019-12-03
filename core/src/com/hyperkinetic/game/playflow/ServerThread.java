package com.hyperkinetic.game.playflow;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {
    private GameServer gs;
    private String playerID;
    private GameRoom room;
    private boolean color;
    private boolean loggedIn;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ServerThread(Socket s, GameServer gs){
        // this.playerID = playerID;
        socket = s;
        this.gs = gs;

        try
        {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();

            in = new ObjectInputStream(socket.getInputStream());
            this.start();
        }
        catch(IOException ioe) {
            System.out.println("oops");
            ioe.printStackTrace();
        }

    }

    /**
     * Send the message object to output
     * @param message the message to be sent
     */
    public void sendMessage(GameMessage message)
    {
        try
        {
            out.writeObject(message);
            out.flush();
        }
        catch(IOException ioe)
        {
            System.out.println("ioe in sendMessage() of ServerThread " + playerID);
            ioe.printStackTrace();
        }
    }

    /**
     * Set the gameRoom that this thread runs
     * @param gm
     */
    public void enterGame(GameRoom gm)
    {
        if(this.room != null) return;
        this.room = gm;
    }

    /**
     * Setter of the color
     * @param color
     */
    public void setColor(boolean color)
    {
        this.color = color;
    }

    /**
     * Getter of the playerID
     * @return
     */
    public String getPlayerID()
    {
        return playerID;
    }

    /**
     * Constantly read message from the game room
     */
    @Override
    public void run() {
        try {
            while (!loggedIn) {
                try {
                    GameMessage message = (GameMessage) in.readObject();
                    gs.logMessage(message);
                    if (message.getMessageType() == GameMessage.messageType.LOGIN_ATTEMPT) {
                        GameMessage loginMessage = gs.queryDatabase(message);
                        if (loginMessage.getMessageType() == GameMessage.messageType.LOGIN_SUCCESS) {
                            // Login success
                            loggedIn = true;
                            playerID = loginMessage.playerID;
                            gs.loginServerThread(this);
                        }
                        sendMessage(loginMessage);
                    } else if (message.getMessageType() == GameMessage.messageType.REGISTER_ATTEMPT) {
                        GameMessage registerMessage = gs.queryDatabase(message);
                        if (registerMessage.getMessageType() == GameMessage.messageType.REGISTER_SUCCESS) {
                            // Register success and automatically login
                            loggedIn = true;
                            playerID = registerMessage.playerID;
                            gs.loginServerThread(this);
                        }
                        sendMessage(registerMessage);
                    }
                } catch (ClassNotFoundException cnfe) {
                    System.out.println("cnfe in run() of ServerThread " + playerID);
                    cnfe.printStackTrace();
                }
            }
            while (room == null) {
                try {
                    GameMessage message = (GameMessage) in.readObject();
                    if (message.getMessageType() == GameMessage.messageType.MATCHMAKING_REQUEST) {
                        gs.addToMatchmaking(message.playerID);
                    }
                } catch (ClassNotFoundException cnfe) {
                    System.out.println("cnfe in run() of ServerThread " + playerID);
                    cnfe.printStackTrace();
                }
            }
            while (!room.isOver) {
                // querying for GameMessage objects
                try {
                    GameMessage message = (GameMessage) in.readObject();
                    room.readMessage(message);
                } catch (ClassNotFoundException cnfe) {
                    System.out.println("cnfe in run() of ServerThread " + playerID);
                    cnfe.printStackTrace();
                }
            }
    
            room = null;
        }
        catch(IOException ioe)
        {
            System.out.println("ioe in run() of ServerThread " + playerID);
            System.out.println("Terminating connection with the client.");
        }
    }
}
