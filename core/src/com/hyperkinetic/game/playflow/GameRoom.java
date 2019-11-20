package com.hyperkinetic.game.playflow;

import java.net.*;
import com.hyperkinetic.game.board.AbstractGameBoard;
import com.hyperkinetic.game.board.StandardBoard;
import com.hyperkinetic.game.pieces.LaserPiece;
import com.hyperkinetic.game.playflow.GameMessage.messageType;

/**
 * GameRoom class, implements basic game flow - this version only supports local mode
 */
public class GameRoom {
    private ServerThread aThread;
    private ServerThread bThread;
    private AbstractGameBoard board;
    private GameServer gs;
    private boolean turn; //who's turn is it. Player A = White = True, Player B = Black = False
    public boolean isOver = false;

    public GameRoom(GameServer gs, ServerThread a, ServerThread b) {
        this.gs = gs;
        this.board = new StandardBoard(true);
        turn = true; // white (a) plays first

        aThread = a;
        bThread = b;
        // aThread.setColor(true);
        // bThread.setColor(false);
        aThread.enterGame(this);
        bThread.enterGame(this);
        aThread.start();
        bThread.start();

        GameMessage gm1 = new GameMessage(messageType.ROOM_CREATE);
        gm1.startBoard = new StandardBoard(true);
        gm1.playerID = aThread.getPlayerID();
        gm1.player2ID = bThread.getPlayerID();
        aThread.sendMessage(gm1);

        GameMessage gm2 = new GameMessage(messageType.ROOM_CREATE);
        gm2.startBoard = new StandardBoard(false);
        gm2.playerID = aThread.getPlayerID();
        gm2.player2ID = bThread.getPlayerID();
        bThread.sendMessage(gm2);
    }

    public void broadcast(GameMessage message) {
        // broadcast message to both PlayerThreads
        aThread.sendMessage(message);
        bThread.sendMessage(message);
    }

    private void handleMoveAttempt(GameMessage move){
        if(move.getMessageType() != messageType.PLAYER_MOVE) return;

        // validate move, send move_success/failure message to server
        if(move.playerID.equals(getActivePlayerID())) {
            if(board.isValidMove(turn, move.x, move.y, move.moveType, move.moveX, move.moveY)) {
                GameMessage gm = new GameMessage(messageType.MOVE_SUCCESS);
                gm.playerID = getActivePlayerID();
                gm.x = move.x;
                gm.y = move.y;
                gm.moveType = move.moveType;
                gm.moveX = move.moveX;
                gm.moveY = move.moveY;
                broadcast(gm);

                updateBoard(move.x, move.y, move.moveType, move.moveX, move.moveY);
            }
            else
            {
                GameMessage fail = new GameMessage(messageType.MOVE_FAILURE);
                fail.playerID = getActivePlayerID();
                fail.x = move.x;
                fail.y = move.y;
                fail.moveType = move.moveType;
                fail.moveX = move.moveX;
                fail.moveY = move.moveY;
                fail.errorMessage = "illegal move";
                broadcast(fail);
            }
        } else {
            // move attempt invalid - wrong turn
            GameMessage gm = new GameMessage(messageType.MOVE_FAILURE);
            gm.playerID = move.playerID;
            gm.x = move.x;
            gm.y = move.y;
            gm.moveType = move.moveType;
            gm.moveX = move.moveX;
            gm.moveY = move.moveY;
            gm.errorMessage = "move attempt has wrong turn";
            broadcast(gm);
        }
    }

    private void updateBoard(int x,int y,String moveType,int nX,int nY) {
        // update board
        board.update(x,y,moveType,nX,nY);
        // LaserPiece laser = board.getActiveLaser();
        // board.fireLaser(laser.getX(),laser.getY(),laser.getOrientation());
        LaserPiece aLaser = board.getALaser();
        LaserPiece bLaser = board.getBLaser();
        if(turn) board.fireLaser(aLaser.getX(), aLaser.getY(), aLaser.getOrientation());
        else board.fireLaser(bLaser.getX(), bLaser.getY(), bLaser.getOrientation());
        turn = !turn;

        String res = board.isGameOver();
        if(res.equals("AWin")){
            GameMessage gm = new GameMessage(messageType.GAME_OVER);
            gm.playerID = aThread.getPlayerID();
            gm.player2ID = bThread.getPlayerID();
            gs.updateDatabase(gm);
            broadcast(gm);
            isOver = true;

            clear();
        } else if(res.equals("BWin")){
            GameMessage gm = new GameMessage(messageType.GAME_OVER);
            gm.playerID = bThread.getPlayerID();
            gm.player2ID = aThread.getPlayerID();
            gs.updateDatabase(gm);
            broadcast(gm);
            isOver = true;

            clear();
        }
    }

    private void handleReady(GameMessage message){
        if(message.getMessageType()!=messageType.READY) return;

        String playerID = message.playerID;
        GameMessage copy = new GameMessage(messageType.COPY);
        copy.playerID = playerID;
        if(playerID.equals(aThread.getPlayerID())){
            aThread.sendMessage(copy);
        } else {
            bThread.sendMessage(copy);
        }
    }

    public void readMessage(GameMessage message)
    {
        if(message.getMessageType()==messageType.PLAYER_MOVE){
            handleMoveAttempt(message);
        } else if(message.getMessageType()==messageType.READY){
            handleReady(message);
        } else if(message.getMessageType()==messageType.ACCOUNT_STATS_REQUEST){
            GameMessage gm = gs.queryDatabase(message);
            broadcast(gm);
        }
    }

    private String getActivePlayerID()
    {
        return turn ? aThread.getPlayerID() : bThread.getPlayerID();
    }

    public void clear(){
        gs.deleteRoom(this);
    }
}
