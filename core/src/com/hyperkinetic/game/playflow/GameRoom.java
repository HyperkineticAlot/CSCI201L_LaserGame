package com.hyperkinetic.game.playflow;

import java.net.*;
import com.hyperkinetic.game.board.AbstractGameBoard;
import com.hyperkinetic.game.board.StandardBoard;
import com.hyperkinetic.game.pieces.LaserPiece;

/**
 * GameRoom class, implements basic game flow - this version only supports local mode
 */
public class GameRoom {
    /**
     * Thread of player a on the server
     */
    private ServerThread aThread;
    /**
     * Thread of player b on the server
     */
    private ServerThread bThread;
    /**
     * The game board that the two players are playing with
     */
    private AbstractGameBoard board;
    /**
     * A reference of the game server
     */
    private GameServer gs;
    /**
     * Variable that keeps track of player's turn
     */
    private boolean turn; //who's turn is it. Player A = White = True, Player B = Black = False
    /**
     * Variable that keeps track of whether the game is over
     */
    public boolean isOver = false;

    public GameRoom(GameServer gs, ServerThread a, ServerThread b) {
        this.gs = gs;
        this.board = new StandardBoard();
        turn = true; // white (a) plays first

        aThread = a;
        bThread = b;
        aThread.setColor(true);
        bThread.setColor(false);
        aThread.start();
        bThread.start();

        GameMessage gm = new GameMessage(GameMessage.messageType.ROOM_CREATE);
        gm.startBoard = board;
        gm.playerID = aThread.getPlayerID();
        gm.player2ID = bThread.getPlayerID();

        broadcast(gm);
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
                GameMessage gm = new GameMessage(GameMessage.messageType.MOVE_SUCCESS);
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
                GameMessage fail = new GameMessage(GameMessage.messageType.MOVE_FAILURE);
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
            GameMessage gm = new GameMessage(GameMessage.messageType.MOVE_FAILURE);
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
     * Update the board on the server, send the board and switch message to both players.
     * @param x the x coordinate of the piece that is to be updated
     * @param y the y coordinate of the piece that is to be updated
     * @param moveType the type of the move, rotated or moved to a new location
     * @param nX the new x coordinate of the piece
     * @param nY the new y coordinate of the piece
     */
    public synchronized void updateBoard(int x,int y,String moveType,int nX,int nY) {
        // update board, send updated board object to both PlayerThread, send switch turn message to server
        board.update(x,y,moveType,nX,nY);
        LaserPiece aLaser = board.getALaser();
        LaserPiece bLaser = board.getBLaser();
        if(turn) board.fireLaser(aLaser.getX(), aLaser.getY(), aLaser.getOrientation());
        else board.fireLaser(bLaser.getX(), bLaser.getY(), bLaser.getOrientation());
        turn = !turn;
    }

    /**
     * Read the message from the server.
     * @param message the message from the server
     */
    public void readMessage(GameMessage message)
    {

    }

    /**
     * Get the player ID of the active player.
     * @return the player ID of the active player
     */
    private String getActivePlayerID()
    {
        return turn ? aThread.getPlayerID() : bThread.getPlayerID();
    }

    /**
     * Player fires the laser, end and switch the turn.
     */
    public void fireLaser(){
        // laser firing called by updateBoard
    }

        /*while(!aWon && !bWon) {
            // if both disconnect
            if(!aThread.isConnected() && !bThread.isConnected()) {
                // delete this GameRoom
                GameSocket.delete(this);
                return;
            }

            if(aTurn) { // aThread's turn
                aThread.makeMove(board, "aThread");
            } else { // bThread's turn
                bThread.makeMove(board, "bThread");
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
