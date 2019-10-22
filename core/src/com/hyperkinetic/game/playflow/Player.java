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
    InputProcessor gameInputProcessor = null;

    /**
     * if not signed in, play as guest
     * @param gameInputProcessor
     */
    public Player(InputProcessor gameInputProcessor) {
        this.s = null;
        this.playerID = "-1";
        this.playerName = "Guest";
        this.isGuest = true;
        this.gameInputProcessor = gameInputProcessor;
    }

    /**
     * verify player identity in database, and get playerName
     * @param s Session object the player is assigned
     * @param playerID playerID of this player
     * @param gameInputProcessor
     */
    public Player(Session s, String playerID, InputProcessor gameInputProcessor) {
        this.s = s;
        this.playerID = playerID;
        this.gameInputProcessor = gameInputProcessor;
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
            // get input from gameInputProcessor
            // call board movement methods
            if(/*is a valid move*/false) {
                // Ex. board.rotate(pID, piece);
                break;
            }
        }
        return true;
    }
}
