package com.hyperkinetic.game.playflow;

import com.hyperkinetic.game.board.AbstractGameBoard;
import com.hyperkinetic.game.util.Directions;

/**
 * GameRoom class, implements basic game flow - this version only supports local mode
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
    // public GameSocket gs;

    public GameRoom(Player a, Player b, int roomID, AbstractGameBoard board) {
        this.a = a;
        this.b = b;
        this.roomID = roomID;
        this.board = board;
        this.start();
    }

    @Override
    public void run() {
        // TODO: add sendMessage calls to GameSocket
        /*while(!aWon && !bWon) {
            // if both disconnect
            if(!a.isConnected() && !b.isConnected()) {
                // delete this GameRoom
                GameSocket.delete(this);
                return;
            }

            if(aTurn) { // a's turn
                a.makeMove(board, "a");
            } else { // b's turn
                b.makeMove(board, "b");
            }

            // fires laser, update board & pieces on board
            LaserPiece laserPiece = null;
            if(aTurn) {
                laserPiece = board.getALaser();
            } else {
                laserPiece = board.getBLaser();
            }
            Directions.Direction dir = laserPiece.getOrientation();
            int x = laserPiece.getX();
            int y = laserPiece.getY();
            if(dir == Directions.Direction.NORTH) {
                x = x;
                y = y - 1;
            }
            else if(dir == Directions.Direction.EAST) {
                x = x + 1;
                y = y;
            }
            else if(dir == Directions.Direction.SOUTH) {
                x = x;
                y = y + 1;
            }
            else {
                x = x - 1;
                y = y;
            }
            board.fireLaser(x,y,dir);

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

        }*/
    }
}
