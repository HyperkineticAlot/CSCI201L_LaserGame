package com.hyperkinetic.game.playflow;

import com.hyperkinetic.game.board.AbstractGameBoard;
import com.hyperkinetic.game.pieces.LaserPiece;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ClientThread extends Thread {
    private ObjectInputStream in;
    private AbstractGameBoard board;

    public String playerID;
    public boolean isGuest;
    public boolean isAI;

    private Player player;
    private Socket socket;

    public ClientThread(String hostname, int port, boolean isGuest, boolean isAI)
    {
        board = null;
        this.isGuest = isGuest;
        this.isAI = isAI;
        try
        {
            System.out.println("Trying to connect to "+hostname+":"+port);
            socket = new Socket(hostname, port);
            System.out.println("Connected to "+hostname+":"+port);
            in = new ObjectInputStream(socket.getInputStream());
            player = new Player(socket, this);
            this.start();
            player.start();
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        while(true)
        {
            try
            {
                GameMessage message = (GameMessage) in.readObject();
                if(board==null){
                    if(message.getMessageType()==GameMessage.messageType.ROOM_CREATE){
                        this.board = message.startBoard;
                        player.setBoard(message.startBoard);
                    }
                } else {
                    if(message.getMessageType()==GameMessage.messageType.MOVE_SUCCESS){
                        if(!message.playerID.equals(playerID)){ // update board
                            board.update(message.x,message.y,message.moveType,message.moveX,message.moveY);
                        }
                        // fire laser
                        LaserPiece laser = board.getActiveLaser();
                        board.fireLaser(laser.getX(),laser.getY(),laser.getOrientation());
                    } else if(message.getMessageType()==GameMessage.messageType.MOVE_FAILURE){
                        if(message.playerID.equals(playerID)){
                            // undo move & wait for next
                            board.undoMove();
                        }
                    } else if(message.getMessageType()==GameMessage.messageType.GAME_OVER){
                        if(message.playerID.equals(playerID)){ // wins - update LaserGameScreen status
                            // TODO: handle client win (display stats?)
                            System.out.println(playerID+" has won!");
                        } else { // loses - update LaserGameScreen status
                            // TODO: handle client loss
                            System.out.println(playerID+" has lost.");
                        }
                    } else if(message.getMessageType()==GameMessage.messageType.ACCOUNT_STATS_RESPONSE){
                        String records = message.records;
                        if(message.playerID.equals(playerID)){
                            // TODO: display records in a nicely fashion
                            System.out.println(playerID+"'s current stats: "+records);
                        }
                    } else if(message.getMessageType()==GameMessage.messageType.COPY){
                        if(message.playerID.equals(playerID)){
                            // TODO: track connection status
                            System.out.println("One handshake complete - connection maintained...");
                        }
                    }
                }
            }
            catch (ClassNotFoundException cnfe)
            {
                System.out.println("cnfe in run() of ClientThread of " + player.playerID);
                cnfe.printStackTrace();
            }
            catch(IOException ioe)
            {
                System.out.println("ioe in run() of ClientThread of " + player.playerID);
            }
        }
    }
}

// account login/register messaging
/*else if(message.getMessageType()==GameMessage.messageType.LOGIN_SUCCESS){
    // updates LaserGame state/Player state (?)
} else if(message.getMessageType()==GameMessage.messageType.LOGIN_FAILURE){
    //
} else if(message.getMessageType()==GameMessage.messageType.ACCOUNT_CREATE_SUCCESS){
    //
} else if(message.getMessageType()==GameMessage.messageType.ACCOUNT_CREATE_FAILURE){
    //
} */