package com.hyperkinetic.game.playflow;

import com.badlogic.gdx.utils.Json;
import com.hyperkinetic.game.board.AbstractGameBoard;
import com.hyperkinetic.game.core.LaserGame;
import com.hyperkinetic.game.core.LogInScreen;
import com.hyperkinetic.game.pieces.LaserPiece;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientThread extends Thread {
    /**
     * Input from the server
     */
    private ObjectInputStream in;
    /**
     * A reference to the game board
     */
    private AbstractGameBoard board;

    public String playerID;
    public boolean isGuest;
    public boolean isAI;

    private boolean loggedIn;

    /**
     * The player thread that is hold by the client
     */
    private Player player;
    /**
     * The socket of the player
     */
    private Socket s;

    private LaserGame game;

    public ClientThread(String hostname, int port, boolean isGuest, boolean isAI, LaserGame game)
    {
        board = null;
        this.isGuest = isGuest;
        this.isAI = isAI;
        this.playerID = null;
        this.game = game;
        loggedIn = false;

        try
        {
            System.out.println("Trying to connect to "+hostname+":"+port);
            s = new Socket(hostname, port);
            System.out.println("Connected to "+hostname+":"+port);
            player = new Player(s, this);
            in = new ObjectInputStream(s.getInputStream());
            this.start();
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public Player getPlayer(){
        return player;
    }

    public void resetPlayerID() {
        playerID = null;
        player.setPlayerID(null);
    }

    /**
     * Receive the start-of-game message and constantly check for server packets and process
     */
    @Override
    public void run()
    {
        try{
            while(true) {

                while(!loggedIn){ // before login
                    try {
                        GameMessage message = (GameMessage) in.readObject();
                        System.out.println("Message received: "+message.getMessage());

                        if (message.getMessageType() == GameMessage.messageType.LOGIN_SUCCESS || message.getMessageType() == GameMessage.messageType.REGISTER_SUCCESS) {
                            this.player.setPlayerID(message.userName);
                            this.playerID = message.userName;
                            loggedIn = true;
                        } else if (message.getMessageType() == GameMessage.messageType.LOGIN_FAILURE || message.getMessageType() == GameMessage.messageType.REGISTER_FAILURE) {
                            System.out.println(message.errorMessage);
                            this.player.setPlayerID(LogInScreen.LOGIN_FAILURE_FLAG);
                            this.playerID = LogInScreen.LOGIN_FAILURE_FLAG;
                        }

                    } catch (ClassNotFoundException cnfe) {
                        System.out.println("cnfe in ClientThread run(): ");
                        cnfe.printStackTrace();
                    }
                }

                while(loggedIn && board==null){ // logged in & not in game
                    try {
                        GameMessage message = (GameMessage) in.readObject();
                        System.out.println("Message received: "+message.getMessage());

                        if(message.getMessageType()==GameMessage.messageType.ROOM_CREATE){
                            Json json = new Json();
                            AbstractGameBoard start = message.boardClass.cast(json.fromJson(message.boardClass, message.startBoard));
                            this.board = start;
                            player.setBoard(start);
                        }

                    } catch (ClassNotFoundException cnfe) {
                        System.out.println("cnfe in ClientThread run(): ");
                        cnfe.printStackTrace();
                    }
                }

                while(loggedIn && board!=null && !board.isOver){ // logged in & in game
                    try {
                        GameMessage message = (GameMessage) in.readObject();
                        System.out.println("Message received: "+message.getMessage());

                        if(message.getMessageType()==GameMessage.messageType.MOVE_SUCCESS){
                            if(!message.userName.equals(playerID)){
                                board.update(message.x,message.y,message.moveType,message.moveX,message.moveY);
                                LaserPiece laser = board.getActiveLaser();
                                board.fireLaser(laser.getX(),laser.getY(),laser.getOrientation());
                            }
                        } else if(message.getMessageType()==GameMessage.messageType.MOVE_FAILURE){
                            if(message.userName.equals(playerID)){
                                System.out.println("You cheated! You lost!");
                                // TODO end game
                            }
                        } else if(message.getMessageType()==GameMessage.messageType.GAME_OVER){
                            if(message.userName.equals(playerID)){ // wins - update LaserGameScreen status
                                System.out.println(playerID+" has won!");
                                player.won();
                                // waiting for incoming records
                            } else { // loses - update LaserGameScreen status
                                System.out.println(playerID+" has lost.");
                                player.lost();
                                // waiting for incoming records
                            }
                        }

                    } catch (ClassNotFoundException cnfe) {
                        System.out.println("cnfe in ClientThread run(): ");
                        cnfe.printStackTrace();
                    }
                }
            }
        } catch (IOException ioe) {
            System.out.println("ioe in ClientThread run(): ");
            ioe.printStackTrace();
        }
    }
}

/*
                GameMessage message = (GameMessage) in.readObject();
                System.out.println("Message received: "+message.getMessage());

                if(board==null){
                    if (message.getMessageType() == GameMessage.messageType.LOGIN_SUCCESS || message.getMessageType() == GameMessage.messageType.REGISTER_SUCCESS) {
                        this.player.setPlayerID(message.userName);
                        this.playerID = message.userName;
                    }
                    else if (message.getMessageType() == GameMessage.messageType.LOGIN_FAILURE || message.getMessageType() == GameMessage.messageType.REGISTER_FAILURE) {
                        System.out.println(message.errorMessage);
                        this.player.setPlayerID(LogInScreen.LOGIN_FAILURE_FLAG);
                        this.playerID = LogInScreen.LOGIN_FAILURE_FLAG;
                    }
                    else if(message.getMessageType()==GameMessage.messageType.ROOM_CREATE){
                        Json json = new Json();
                        AbstractGameBoard start = message.boardClass.cast(json.fromJson(message.boardClass, message.startBoard));
                        this.board = start;
                        player.setBoard(start);
                        GameMessage g = new GameMessage(GameMessage.messageType.STATS_REQUEST);
                        g.userName = playerID;
                        player.sendMessage(g);
                    } else if(message.getMessageType()==GameMessage.messageType.STATS_RESPONSE){
                        int numPlayed = message.numPlayed;
                        int numWin = message.numWin;
                        int numLoss = message.numLoss;
                        if(message.userName.equals(playerID)){
                            System.out.println(playerID+" has played: " + numPlayed + " games. Wins: " + numWin + "; Losses: " + numLoss + ".");
                            player.updateRecord(numPlayed,numWin,numLoss);
                        }
                    }
                } else {
                    if(message.getMessageType()==GameMessage.messageType.MOVE_SUCCESS){
                        if(!message.userName.equals(playerID)){ // update board
                            board.update(message.x,message.y,message.moveType,message.moveX,message.moveY);

                            // fire laser
                            LaserPiece laser = board.getActiveLaser();
                            board.fireLaser(laser.getX(),laser.getY(),laser.getOrientation());
                        }
                    } else if(message.getMessageType()==GameMessage.messageType.MOVE_FAILURE){
                        if(message.userName.equals(playerID)){
                            System.out.println("You cheated!");
                            // TODO end game
                        }
                    } else if(message.getMessageType()==GameMessage.messageType.GAME_OVER){
                        if(message.userName.equals(playerID)){ // wins - update LaserGameScreen status
                            System.out.println(playerID+" has won!");
                            player.won();
                            // waiting for incoming records
                        } else { // loses - update LaserGameScreen status
                            System.out.println(playerID+" has lost.");
                            player.lost();
                            // waiting for incoming records
                        }
                    } else if(message.getMessageType()==GameMessage.messageType.STATS_RESPONSE){
                        int numPlayed = message.numPlayed;
                        int numWin = message.numWin;
                        int numLoss = message.numLoss;
                        if(message.userName.equals(playerID)){
                            System.out.println(playerID+" has played: " + numPlayed + "games. Wins: " + numWin + "; Losses: " + numLoss + ".");
                            player.updateRecord(numPlayed,numWin,numLoss);
                        }
                    }
                }
            }
*/