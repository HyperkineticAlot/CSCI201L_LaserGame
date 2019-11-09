package com.hyperkinetic.game.playflow;

import com.badlogic.gdx.InputProcessor;
import com.hyperkinetic.game.board.AbstractGameBoard;

import javax.websocket.Session;

/**
 * Player class
 */
public class Player {
    private Session s;
    public String playerID;
    public String playerName;
    public boolean isGuest = false;
    public boolean isAI = false;

    /**
     * if not signed in, play as guest
     */
    public Player() {
        this.s = null;
        this.playerID = "-1";
        this.playerName = "Guest";
        this.isGuest = true;
    }

    /**
     * verify player identity in database, and get playerName
     * @param s Session object the player is assigned
     * @param playerID playerID of this player
     */
    public Player(Session s, String playerID) {
        this.s = s;
        this.playerID = playerID;
        // TODO: get playerName from database
    }

    /**
     * player is AI with level AILevel
     * @param AILevel difficulty of AI
     */
    public Player(String AILevel) {
        this.s = null;
        this.playerID = "-2";
        this.playerName = AILevel + " Bot";
        this.isAI = true;
    }

    /**
     * getter of Session
     * @return Session of player
     */
    public Session getSession() {
        return this.s;
    }

    /**
     * remove player Session
     */
    public void remove() {
        this.s = null;
    }

    /**
     * check if player is connected
     * @return true if connected
     */
    public boolean isConnected() {
        if(isAI) return true;
        if(isGuest) return true;
        return s != null;
    }

    /**
     * make a movement on board, called by GameRoom class
     * @param board GameBoard to operate on
     * @param pID "a" or "b" signifies which pieces to operate on in board
     * @return true if a valid move is made
     */
    public boolean makeMove(AbstractGameBoard board, String pID) {
        // make a valid move
        while(true) {
            // TODO: get input from gameInputProcessor: left click - select piece
            // TODO: get legal moves for selected piece
            // TODO: get input from gameInputProcessor: L - rotate left, R - rotate right, Arrows - move
            /* Ex:
            // pseudo code to get input
            AbstractGamePiece piece = GameInputProcessor.getPieceFromTouchUp();
            while(piece!=null) { // get one piece selected
                piece = GameInputProcessor.getPieceFromTouchUp();
            }
            String move = GameInputProcessor.getKeyBoardInput();
            while(move!=null) { // get one input movement
                move = GameInputProcessor.getKeyBoardInput();
            }

            // real code to move selected piece
            if(!board.isValidMove(pID,piece,move)) {
                continue;
            } else { // call board move method
                if(move=='L') {
                    board.pieceRotateLeft(piece);
                    break;
                } else if(move=='R') {
                    board.pieceRotateRight(piece);
                    break;
                } else if(move=='W') {
                    int x = piece.getX();
                    int y = piece.getY()+1;
                    board.pieceMove(piece, x, y);
                    break;
                } else if(move=='S') {
                    int x = piece.getX();
                    int y = piece.getY()-1;
                    board.pieceMove(piece, x, y);
                    break;
                } else if(move=='A') {
                    int x = piece.getX()-1;
                    int y = piece.getY();
                    board.pieceMove(piece, x, y);
                    break;
                } else if(move=='D') {
                    int x = piece.getX()+1;
                    int y = piece.getY();
                    board.pieceMove(piece, x, y);
                    break;
                }
            }
            */
            if(false) break; // delete this line
        }
        return true;
    }
}
