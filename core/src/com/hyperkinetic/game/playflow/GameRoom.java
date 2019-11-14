package com.hyperkinetic.game.playflow;

import java.net.*;
import com.hyperkinetic.game.board.AbstractGameBoard;
import com.hyperkinetic.game.board.StandardBoard;

/**
 * GameRoom class, implements basic game flow - this version only supports local mode
 */
public class GameRoom {
    private ServerThread a;
    private ServerThread b;
    private String aID;
    private String bID;
    private String roomID;
    private AbstractGameBoard board;
    private GameServer gs;
    public boolean isOver = false;
    public boolean aTurn = true;
    public boolean bTurn = false;

    public GameRoom(String roomID, GameServer gs, Socket as, Socket bs) {
        this.roomID = roomID;
        this.aID = roomID+":A";
        this.bID = roomID+":B";
        this.gs = gs;
        this.board = new StandardBoard();

        a = new ServerThread(as,this, board, aTurn,aID);
        b = new ServerThread(bs,this, board, bTurn,bID);

        GameMessage gm = new GameMessage(GameMessage.messageType.ROOM_CREATE);
        gm.roomID = roomID;
        gm.playerID = aID;
        gm.player2ID = bID;
        gs.readMessage(gm);
    }

    public void broadCast(String message) {
        // broadcast message to both PlayerThreads
        a.sendMessage(message);
        b.sendMessage(message);
    }

    public void handleMoveAttempt(String pID,int x,int y,String moveType,int nX,int nY){
        // validate move, send move_success/failure message to server
        if((pID.equals(aID) && aTurn) || (pID.equals(bID) && bTurn)) {
            if(board.isValidMove((aTurn) ? "a" : "b",x,y,moveType,nX,nY)) {
                GameMessage gm = new GameMessage(GameMessage.messageType.MOVE_SUCCESS);
                gm.roomID = roomID;
                gm.playerID = pID;
                gm.x = x;
                gm.y = y;
                gm.moveType = moveType;
                gm.moveX = nX;
                gm.moveY = nY;
                gs.readMessage(gm);
            }
        } else if((pID.equals(aID) && bTurn) || (pID.equals(bID) && aTurn)) {
            // move attempt invalid - wrong turn
            GameMessage gm = new GameMessage(GameMessage.messageType.MOVE_FAILURE);
            gm.roomID = roomID;
            gm.playerID = pID;
            gm.x = x;
            gm.y = y;
            gm.moveType = moveType;
            gm.moveX = nX;
            gm.moveY = nY;
            gm.errorMessage = "move attempt has wrong turn";
            gs.readMessage(gm);
        }
    }

    public void updateBoard(String pID,int x,int y,String moveType,int nX,int nY) {
        // update board, send updated board object to both PlayerThread, send switch turn message to server
        board.update(x,y,moveType,nX,nY);
        //

    }

    public void handleTurn(String pID){
        // change turn to player pID, send updated turn to both PlayerThread
        if(pID.equals(aID)){
            aTurn = true;
            bTurn = false;

        } else {
            aTurn = false;
            bTurn = true;
        }
        //
    }

    public void fireLaser(){
        // laser firing called by updateBoard
    }
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
