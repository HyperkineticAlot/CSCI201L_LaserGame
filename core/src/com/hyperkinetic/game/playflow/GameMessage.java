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
    public AbstractGameBoard startBoard;

    public enum messageType implements Serializable {
        /*
        LOGIN_ATTEMPT,
        LOGIN_SUCCESS,
        LOGIN_FAILURE_WITH_ACCOUNT,
        LOGIN_FAILURE_WITHOUT_ACCOUNT,
        ACCOUNT_CREATE,
        ACCOUNT_STATS,
        ...
        */
        ROOM_CREATE,
        PLAYER_MOVE,
        MOVE_SUCCESS,
        MOVE_FAILURE,
        GAME_OVER,
        READY
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
            return timeStamp+playerID+" selects piece at "+x
                    +", "+y+", moveType="+moveType+", moveX="+moveX+", moveY="+moveY+".";
        } else if(type==messageType.GAME_OVER){
            return timeStamp+"Game is over. Winner is "+playerID+". Loser is "+player2ID;
        } else if(type==messageType.ROOM_CREATE){
            return timeStamp+" Game room with players "+playerID+", "+player2ID+" is created.";
        } else if(type==messageType.MOVE_SUCCESS){
            return timeStamp+playerID+" selects piece at "+x
                    +", "+y+", moveType="+moveType+", moveX="+moveX+", moveY="+moveY+" is approved.";
        } else if(type==messageType.MOVE_FAILURE){
            return timeStamp+playerID+" selects piece at "+x+", "+y+", moveType="
                    +moveType+", moveX="+moveX+", moveY="+moveY+" is disapproved because "+errorMessage;
        } else if(type==messageType.READY){
            return timeStamp+playerID+" is ready.";
        }
        return "";
    }
}
