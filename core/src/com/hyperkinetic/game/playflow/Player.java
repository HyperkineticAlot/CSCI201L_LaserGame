package com.hyperkinetic.game.playflow;

import com.hyperkinetic.game.board.AbstractGameBoard;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Player class
 */
public class Player{

    public String playerID;
    private ClientThread ct;
  
    private Socket s;
    private ObjectOutputStream out;
    private AbstractGameBoard board;

    private int numPlayed;
    private int numWin;
    private int numLoss;
    private boolean lastGame;

    /**
     * If not signed in, play as guest.
     */
    public Player(Socket socket, ClientThread ct) {
        this.ct = ct;
        this.s = socket;
        this.board = null;
        this.playerID = null;

        try {
            out = new ObjectOutputStream(this.s.getOutputStream());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setBoard(AbstractGameBoard board){
        this.board = board;
    }

    public AbstractGameBoard getBoard()
    {
        return board;
    }

    public void setPlayerID(String playerID){
        this.playerID = playerID;
    }

    public String getPlayerID()
    {
        return playerID;
    }

    public boolean login(String playerID, String pass)
    {
        this.playerID = playerID;
        GameMessage loginMessage = new GameMessage(GameMessage.messageType.LOGIN_ATTEMPT);
        loginMessage.userName = playerID;
        loginMessage.password = pass;

        sendMessage(loginMessage);

        return false;
    }

    public boolean register(String playerID, String pass)
    {
        this.playerID = playerID;
        GameMessage registerMessage = new GameMessage(GameMessage.messageType.REGISTER_ATTEMPT);
        registerMessage.userName = playerID;
        registerMessage.password = pass;

        sendMessage(registerMessage);

        return false;
    }

    public void requestStats(){
        GameMessage request = new GameMessage(GameMessage.messageType.STATS_REQUEST);
        request.userName = playerID;
        sendMessage(request);
    }

    public void sendMessage(GameMessage message)
    {
        try
        {
            out.writeObject(message);
            out.flush();
        }
        catch(IOException ioe)
        {
            System.out.println("ioe in sendMessage() of ClientThread " + playerID);
            ioe.printStackTrace();
        }
    }

    public void sendMatchmakingRequest()
    {
        GameMessage request = new GameMessage(GameMessage.messageType.MATCHMAKING_REQUEST);
        request.userName = playerID;
        sendMessage(request);
    }

    public void updateRecord(int numPlayed,int numWin,int numLoss){
        this.numPlayed = numPlayed;
        this.numWin = numWin;
        this.numLoss = numLoss;
    }

    public int getNumPlayed(){
        return this.numPlayed;
    }

    public int getNumWin(){
        return this.numWin;
    }

    public int getNumLoss(){
        return this.numLoss;
    }

    public void won() {
        lastGame = true;
    }

    public void lost() {
        lastGame = false;
    }

    public boolean getLastGame(){
        return this.lastGame;
    }
}
