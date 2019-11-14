package com.hyperkinetic.game.playflow;

import com.hyperkinetic.game.board.AbstractGameBoard;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerThread implements Runnable {
    private String playerID;
    private GameRoom gm;
    private AbstractGameBoard b;
    private boolean myTurn;

    public ServerThread(Socket s, GameRoom gm, AbstractGameBoard b, boolean myTurn, String playerID){
        this.gm = gm;
        this.b = b; // change to copy
        this.myTurn = myTurn;
        this.playerID = playerID;
    }

    public void sendMessage(String message){

    }

    @Override
    public void run() {
        while(!gm.isOver){
            //
        }
    }
}
