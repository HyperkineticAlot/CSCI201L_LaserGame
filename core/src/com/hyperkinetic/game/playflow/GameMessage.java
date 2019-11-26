package com.hyperkinetic.game.playflow;

import com.hyperkinetic.game.board.AbstractGameBoard;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class GameMessage implements Serializable {
    public static final long serialVersionUID = 1;
    private String timeStamp;
    private GameMessage.messageType type;

    public String playerID="";
    public String player2ID="";
    public int x=-1;
    public int y=-1;
    public String moveType="";
    public int moveX=-1;
    public int moveY=-1;
    public String errorMessage;
    public String startBoard;
    public Class<? extends AbstractGameBoard> boardClass;

    public String password;
    public int numPlayed;
    public int numWin;
    public int numLoss;

    public enum messageType implements Serializable {
        LOGIN_ATTEMPT,
        LOGIN_SUCCESS,
        LOGIN_FAILURE,
        REGISTER_ATTEMPT,
        REGISTER_SUCCESS,
        REGISTER_FAILURE,
        STATS_REQUEST,
        STATS_RESPONSE,
        MATCHMAKING_REQUEST,

        ROOM_CREATE,
        PLAYER_MOVE,
        MOVE_SUCCESS,
        MOVE_FAILURE,
        GAME_OVER,
    }

    public GameMessage(GameMessage.messageType type){
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        this.timeStamp = timestamp.toString();
        this.type = type;
        startBoard = null;
    }

    /**
     * Getter of the type of the message
     * @return type of this message
     */
    public GameMessage.messageType getMessageType(){
        return this.type;
    }

    /**
     * Gets the string format of a message.
     * @return the string format of this message
     */
    public String getMessage(){
        /*...other types of messages...*/
        if(type==messageType.PLAYER_MOVE){
            return timeStamp+" "+playerID+" selects piece at "+x
                    +", "+y+", moveType="+moveType+", moveX="+moveX+", moveY="+moveY+".";
        } else if(type==messageType.GAME_OVER){
            return timeStamp+" Game is over. Winner is "+playerID+". Loser is "+player2ID+".";
        } else if(type==messageType.ROOM_CREATE){
            return timeStamp+" Game room with players "+playerID+", "+player2ID+" is created.";
        } else if(type==messageType.MOVE_SUCCESS){
            return timeStamp+" "+playerID+" selects piece at "+x
                    +", "+y+", moveType="+moveType+", moveX="+moveX+", moveY="+moveY+" is approved.";
        } else if(type==messageType.MOVE_FAILURE){
            return timeStamp+" "+playerID+" selects piece at "+x+", "+y+", moveType="
                    +moveType+", moveX="+moveX+", moveY="+moveY+" is disapproved because "+errorMessage+".";
        } else if(type==messageType.LOGIN_ATTEMPT){
            return timeStamp+" User "+playerID+" attempts to log in with password "+password+".";
        } else if(type==messageType.LOGIN_SUCCESS){
            return timeStamp+" User "+playerID+" is successfully logged in.";
        } else if(type==messageType.LOGIN_FAILURE){
            return timeStamp+" User "+playerID+" is not logged in with error "+errorMessage+".";
        } else if(type==messageType.REGISTER_ATTEMPT){
            return timeStamp+" User "+playerID+" wants to create an account with password "+password+".";
        } else if(type==messageType.REGISTER_SUCCESS){
            return timeStamp+" User "+playerID+"  has successfully created an account.";
        } else if(type==messageType.REGISTER_FAILURE){
            return timeStamp+" User "+playerID+"  has failed to create an account with error "+errorMessage+".";
        } else if(type==messageType.STATS_REQUEST){
            return timeStamp+" User "+playerID+" is requesting his records.";
        } else if(type==messageType.STATS_RESPONSE){
            return timeStamp+" User "+playerID+"'s records: Game Played: " + numPlayed + "; Wins: " + numWin + "; Losses: " + numLoss + ".";
        } else if(type == messageType.MATCHMAKING_REQUEST) {
            return timeStamp+" User "+playerID+" is requesting an online match.";
        }
        return "";
    }
}
