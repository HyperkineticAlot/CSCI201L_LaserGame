package com.hyperkinetic.game.playflow;

import com.badlogic.gdx.InputProcessor;
import com.hyperkinetic.game.board.AbstractGameBoard;
import com.sun.security.ntlm.Client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

/**
 * Player class
 */
public class Player extends Thread {

    public String playerID;
    private ClientThread ct;

    private Socket socket;
    private ObjectOutputStream out;
    private AbstractGameBoard board;


    /**
     * If not signed in, play as guest.
     */
    public Player(Socket socket, ClientThread ct) {
        this.ct = ct;
        this.socket = socket;
        this.board = null;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            // this.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setBoard(AbstractGameBoard board){
        this.board = board;
    }

    public void setPlayerID(String playerID){
        this.playerID = playerID;
    }

    @Override
    public void run()
    {
        while(true)
        {
            // WHEN THE PLAYER MAKES A MOVE, SET THE nextMove FIELD IN GAME BOARD TO NULL!!
            // ONLY SET NEXT MOVE WHEN FIRING LASER
            if(board==null){
                System.out.println("Still in game queue...");
            } else {
                GameMessage nextMove = board.getNextMove();
                if(nextMove!=null){
                    try{
                        out.writeObject(nextMove);
                        out.flush();
                    } catch(IOException ioe){
                        ioe.printStackTrace();
                    }
                }
            }
        }
    }
}
