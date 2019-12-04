package com.hyperkinetic.game.playflow;

import com.badlogic.gdx.InputProcessor;
import com.hyperkinetic.game.board.AbstractGameBoard;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

/**
 * Player class
 */
public class Player{

    /**
     * The id of the player
     */
    public String playerID;
    /**
     * A reference to the client thread tah holds this player
     */
    private ClientThread ct;
    /**
     * The sockect of the client side
     */
    private Socket socket;
    /**
     * The output stream that send game messages to the server
     */
    private ObjectOutputStream out;
    /**
     * A reference to the board that the player is playing
     */
    private AbstractGameBoard board;
    /**
     * A record of how many games this player has player
     */
    private int numPlayed;
    /**
     * A record of how many wins this player has
     */
    private int numWin;
    /**
     * A record of ho many losses this player has
     */
    private int numLoss;

    private boolean lastGame;

    public Player(Socket socket, ClientThread ct) {
        this.ct = ct;
        this.socket = socket;
        this.board = null;
        this.playerID = null;

        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Setter of the board reference of this player.
     * @param board the board to be set
     */
    public void setBoard(AbstractGameBoard board){
        this.board = board;
    }

    /**
     * Getter of the board of this player.
     * @return the board of this player
     */
    public AbstractGameBoard getBoard()
    {
        return board;
    }

    /**
     * Setter of the id of this player.
     * @param playerID the player id to be set
     */
    public void setPlayerID(String playerID){
        this.playerID = playerID;
    }

    /**
     * Getter of the id of this player.
     * @return the playerID of this player
     */
    public String getPlayerID()
    {
        return playerID;
    }

    /**
     * Login the this user
     * @param playerID the id of the player in database
     * @param pass password of the player
     * @return false
     */
    public boolean login(String playerID, String pass)
    {
        this.playerID = playerID;
        GameMessage loginMessage = new GameMessage(GameMessage.messageType.LOGIN_ATTEMPT);
        loginMessage.playerID = playerID;
        loginMessage.password = pass;

        sendMessage(loginMessage);

        return false;
    }

    /**
     * Register this player to the databse.
     * @param playerID the id of this player
     * @param pass password of player
     * @return false
     */
    public boolean register(String playerID, String pass)
    {
        this.playerID = playerID;
        GameMessage registerMessage = new GameMessage(GameMessage.messageType.REGISTER_ATTEMPT);
        registerMessage.playerID = playerID;
        registerMessage.password = pass;

        sendMessage(registerMessage);

        return false;
    }

    /**
     * Send a game message to the output stream.
     * @param message the game message to be sent
     */
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

    /**
     * Send a request for match making to the server.
     */
    public void sendMatchmakingRequest()
    {
        GameMessage request = new GameMessage(GameMessage.messageType.MATCHMAKING_REQUEST);
        request.playerID = playerID;
        sendMessage(request);
    }

    /**
     * Setter of the player status.
     * @param numPlayed the number of game player
     * @param numWin number of wins
     * @param numLoss number of losses
     */
    public void updateRecord(int numPlayed,int numWin,int numLoss){
        this.numPlayed = numPlayed;
        this.numWin = numWin;
        this.numLoss = numLoss;
    }

    /**
     * Getter of the numPlayer.
     * @return the number of games played by this player
     */
    public int getNumPlayed(){
        return this.numPlayed;
    }
    /**
     * Getter of the numWin.
     * @return the number of wins of this player
     */
    public int getNumWin(){
        return this.numWin;
    }
    /**
     * Getter of the numLoss.
     * @return the number of losses of this player
     */
    public int getNumLoss(){
        return this.numLoss;
    }

    /**
     * This player win a game.
     */
    public void won() {
        lastGame = true;
    }

    /**
     * This player lose a game
     */
    public void lost() {
        lastGame = false;
    }

    /**
     * Get the last game result of this player
     * @return the result of last game played
     */
    public boolean getLastGame(){
        return this.lastGame;
    }
}
