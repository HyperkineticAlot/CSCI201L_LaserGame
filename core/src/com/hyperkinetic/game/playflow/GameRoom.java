package com.hyperkinetic.game.playflow;

import com.badlogic.gdx.utils.Json;
import com.hyperkinetic.game.board.AbstractGameBoard;
import com.hyperkinetic.game.board.StandardBoard;
import com.hyperkinetic.game.pieces.LaserPiece;
import com.hyperkinetic.game.playflow.GameMessage.messageType;

/**
 * GameRoom class, implements basic game flow - this version only supports local mode
 */
public class GameRoom {
    public static final int PORT = 8000;

    /**
     * Thread of player a on the server
     */
    protected ServerThread aThread;
    /**
     * Thread of player b on the server
     */
    protected ServerThread bThread;
    /**
     * The game board that the two players are playing with
     */
    protected AbstractGameBoard board;
    /**
     * A reference of the game server
     */
    private GameServer gs;
    /**
     * Variable that keeps track of player's turn
     */
    protected boolean turn; //who's turn is it. Player A = White = True, Player B = Black = False
    /**
     * Variable that keeps track of whether the game is over
     */
    public boolean isOver = false;

    public GameRoom(GameServer gs, ServerThread a, ServerThread b) {
        this.gs = gs;
        turn = true; // white (a) plays first

        aThread = a;
        bThread = b;
        // aThread.setColor(true);
        // bThread.setColor(false);
        aThread.enterGame(this);
        bThread.enterGame(this);

        this.board = new StandardBoard(true);

        Json json = new Json();

        GameMessage gm1 = new GameMessage(messageType.ROOM_CREATE);
        StandardBoard board1 = new StandardBoard(true);
        gm1.startBoard = json.toJson(board1);
        gm1.boardClass = StandardBoard.class;
        gm1.playerID = aThread.getPlayerID();
        gm1.player2ID = bThread.getPlayerID();
        aThread.sendMessage(gm1);

        GameMessage gm2 = new GameMessage(messageType.ROOM_CREATE);
        StandardBoard board2 = new StandardBoard(false);
        gm2.startBoard = json.toJson(board2);
        gm2.boardClass = StandardBoard.class;
        gm2.playerID = aThread.getPlayerID();
        gm2.player2ID = bThread.getPlayerID();
        bThread.sendMessage(gm2);
    }

    protected GameRoom()
    {
        turn = true;
        aThread = null;
        bThread = null;
    }

    /**
     * Broadcast the message to both two clients.
     * @param message the message to be broadcast to each client
     */
    public void broadcast(GameMessage message) {
        // broadcast message to both PlayerThreads
        aThread.sendMessage(message);
        bThread.sendMessage(message);
    }

    /**
     * Validate move, send move success/failure message to server.
     * @param move the move to be validated
     */
    public synchronized void handleMoveAttempt(GameMessage move){
        if(move.getMessageType() != GameMessage.messageType.PLAYER_MOVE) return;

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

    /**
     * Update the board on the server
     * @param x the x coordinate of the piece that is to be updated
     * @param y the y coordinate of the piece that is to be updated
     * @param moveType the type of the move, rotated or moved to a new location
     * @param nX the new x coordinate of the piece
     * @param nY the new y coordinate of the piece
     */
    private void updateBoard(int x,int y,String moveType,int nX,int nY) {
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

    /**
     * Read the message from the server.
     * @param message the message from the server
     */
    public void readMessage(GameMessage message)
    {
        if(message.getMessageType()==messageType.PLAYER_MOVE){
            handleMoveAttempt(message);
        } else if(message.getMessageType()==messageType.READY){
            handleReady(message);
        } else if(message.getMessageType()==messageType.STATS_REQUEST){
            GameMessage gm = gs.queryDatabase(message);
            broadcast(gm);
        }
    }

    /**
     * Get the player ID of the active player.
     * @return the player ID of the active player
     */
    private String getActivePlayerID()
    {
        return turn ? aThread.getPlayerID() : bThread.getPlayerID();
    }

    public void clear(){
        gs.deleteRoom(this);
    }
  
    /**
     * Player fires the laser, end and switch the turn.
     */
    public void fireLaser(){
        // laser firing called by updateBoard
    }
}
