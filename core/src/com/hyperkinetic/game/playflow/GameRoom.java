package com.hyperkinetic.game.playflow;

import com.hyperkinetic.game.board.AbstractGameBoard;

import javax.websocket.Session;

/**
 * GameRoom class, implements basic game flow
 */
public class GameRoom extends Thread {
    public Player a;
    public Player b;
    public boolean aTurn = true; // player a goes first
    public boolean bTurn = false;
    public boolean aWon = false;
    public boolean bWon = false;
    public int roomID;
    public AbstractGameBoard board;

    public GameRoom(Player a, Player b, int roomID, AbstractGameBoard board) {
        this.a = a;
        this.b = b;
        this.roomID = roomID;
        this.board = board;
    }

    @Override
    public void run() {
        while(!aWon && !bWon) {
            // if both disconnect
            if(!a.isConnected() && !b.isConnected()) {
                // delete this GameRoom
                GameSocket.delete(this);
                return;
            }

            if(aTurn) { // a's turn
                a.makeMove(board);
            } else { // b's turn
                b.makeMove(board);
            }

            // fires laser
            if(aTurn) { // a's turn -> fires a's laser
                // calls appropriate LaserEntity function
            } else { // b's turn -> fires b's laser
                // calls appropriate LaserEntity function
            }

            String res = board.isGameOver();
            if(res.equals("AWin")) {
                aWon = true;
            } else if(res.equals("BWin")) {
                bWon = true;
            } else {
                // game continues
                aTurn = !aTurn;
                bTurn = !bTurn;
            }

        }
    }
}
