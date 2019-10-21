package com.hyperkinetic.game.playflow;

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

    public Player() {
        // if not signed in, play as guest
        this.s = null;
        this.playerID = "-1";
        this.playerName = "Guest";
        this.isGuest = true;
    }

    public Player(Session s, String playerID) {
        // verify player identity in database, and get playerName
        this.s = s;
        this.playerID = playerID;
    }

    public Player(String AILevel) {
        // player is AI with level AILevel
        this.s = null;
        this.playerID = "-2";
        this.playerName = AILevel + " Bot";
        this.isAI = true;
    }

    public Session getSession() {
        return this.s;
    }

    public void remove() {
        this.s = null;
    }

    public boolean isConnected() {
        if(isAI) return true;
        if(isGuest) return true;
        return s != null;
    }

    public boolean makeMove(AbstractGameBoard board) {
        // make a valid move
        while(true) {
            // TODO: make a valid move
            break;
        }
        return true;
    }
}
