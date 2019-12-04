package com.hyperkinetic.game.playflow;

import com.hyperkinetic.game.core.LaserGame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.sql.*;

/**
 * GameSocket class, server side of websocket, hosts all GameRooms and implements match making
 * Note: This class should be in a separate server project instead of this local game project (client)
 */

public class GameServer {
    /**
     * Hostname of the server
     */
    private static final String host = "localhost";
    /**
     * Port number of the server
     */
    public static final int port = 8000;
    /**
     * Database connection URL
     */
    private static final String url = "mysql://localhost:3306/finalproject";
    /**
     * Database connection
     */
    private static Connection conn;
    /**
     * Database username
     */
    private static final String user = "root";
    /**
     * Database password
     */
    private static final String pwd = "root";
    /**
     * Stores all player sockets in matchmaking
     */
    private static Vector<ServerThread> matchingQueue = new Vector<>();
    /**
     * Stores ServerThreads that are not logged in
     */
    private static Vector<ServerThread> loginQueue = new Vector<>();
    /**
     * Stores ServerThreads that are logged in
     */
    private static Vector<ServerThread> loggedInQueue = new Vector<>();
    /**
     * stores mapping from playerID to GameRooms
     */
    private static Vector<GameRoom> gameRooms = new Vector<>();

    public static void main(String[] args){
        LaserGame.IS_SERVER = true;
        GameServer gs = new GameServer();
    }

    public GameServer(){
        try {
            conn = getConnection();
            System.out.println("Connected to database!");
            System.out.println("Binding to port: "+port);
            ServerSocket ss = new ServerSocket(port);
            System.out.println("Connected!");

            while(true) {
                Socket s = ss.accept();
                ServerThread st = new ServerThread(s, this);
                loginQueue.add(st);
                System.out.println("Accepted one new connection!");

                // TODO: this doesn't run lole

                // check for dead games
                for(GameRoom room : gameRooms)
                {
                    if(room.isOver) gameRooms.remove(room);
                }
            }
        } catch(IOException e) {
            System.out.println("Unable to create server: "+e.getMessage());
        } catch (SQLException e) {
            System.out.println("Unable to connect the database: ");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: ");
            e.printStackTrace();
        }
    }

    /**
     * Remove the ServerThread from loginQueue and add that ServerThread to loggedInQueue.
     *
     * @param serverThread the ServerThread that is to be removed and added
     */
    public void loginServerThread(ServerThread serverThread) {
        ServerThread threadToBeMoved = null;
        for (int i = 0; i < loginQueue.size(); i++) {
            if (serverThread == loginQueue.get(i)) {
                threadToBeMoved = loginQueue.get(i);
                loginQueue.remove(i);
                loggedInQueue.add(threadToBeMoved);
            }
        }
    }

    /**
     * Checks if the server is successfully connected to the database.
     * @return true if the connection is success, false otherwise
     */
    public boolean checkConnection() {
        boolean success = false;
        try{
            Connection conn = GameServer.getConnection();
            success = true;
        } catch(ClassNotFoundException e){
            System.out.println("ClassNotFound error in checkConnection(): "+e.getMessage());
        } catch(SQLException e){
            System.out.println("SQL error in checkConnection(): "+e.getMessage());
        }
        return success;
    }

    /**
     * Get a connection to the database.
     * @return the connection to the database
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:"+url+"?user="+user+"&password="+pwd);
        return conn;
    }

    /**
     * Update records for both players after one game is over.
     *
     * @param gm GameMessage
     * @return true if update is complete & successful
     */
    public boolean updateDatabase(GameMessage gm){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        if(gm.getMessageType()==GameMessage.messageType.GAME_OVER){
            String winner = gm.playerID;
            String loser = gm.player2ID;
            try{
                conn = getConnection();
                // winner
                ps = conn.prepareStatement("SELECT userID FROM USER WHERE userName=?");
                ps.setString(1,winner);
                rs = ps.executeQuery();
                int winnerID = 0;
                if(rs.next()){
                    winnerID = rs.getInt("userID");
                }
                ps = conn.prepareStatement("SELECT recordID, numPlayed, numWin, numLoss FROM RECORD WHERE userID=?");
                ps.setInt(1,winnerID);
                rs = ps.executeQuery();
                int winnerRecordID = 0;
                int winnerNumPlayed = 0;
                int winnerNumWin = 0;
                int winnerNumLoss = 0;
                if(rs.next()){
                    winnerRecordID = rs.getInt("recordID");
                    winnerNumPlayed = rs.getInt("numPlayed");
                    winnerNumWin = rs.getInt("numWin");
                    winnerNumLoss = rs.getInt("numLoss");
                }
                ps = conn.prepareStatement("UPDATE RECORD SET numPlayed=?, numWin=? WHERE recordID=?");
                ps.setInt(1,winnerNumPlayed+1);
                ps.setInt(2,winnerNumWin+1);
                ps.setInt(3,winnerRecordID);
                ps.executeUpdate();

                // loser
                ps = conn.prepareStatement("SELECT userID FROM USER WHERE userName=?");
                ps.setString(1,loser);
                rs = ps.executeQuery();
                int loserID = 0;
                if(rs.next()){
                    loserID = rs.getInt("userID");
                }
                ps = conn.prepareStatement("SELECT recordID, numPlayed, numWin, numLoss FROM RECORD WHERE userID=?");
                ps.setInt(1,loserID);
                rs = ps.executeQuery();
                int loserRecordID = 0;
                int loserNumPlayed = 0;
                int loserNumWin = 0;
                int loserNumLoss = 0;
                if(rs.next()){
                    loserRecordID = rs.getInt("recordID");
                    loserNumPlayed = rs.getInt("numPlayed");
                    loserNumWin = rs.getInt("numWin");
                    loserNumLoss = rs.getInt("numLoss");
                }
                ps = conn.prepareStatement("UPDATE RECORD SET numPlayed=?, numLoss=? WHERE recordID=?");
                ps.setInt(1,loserNumPlayed+1);
                ps.setInt(2,loserNumLoss+1);
                ps.setInt(3,loserRecordID);
                ps.executeUpdate();
            } catch(ClassNotFoundException e){
                System.out.println("ClassNotFound error in updateDatabase(): "+e.getMessage());
            } catch(SQLException e){
                System.out.println("SQL error in updateDatabase(): "+e.getMessage());
            } finally {
                try {
                    if(rs!=null) rs.close();
                    if(ps!=null) ps.close();
                    if(conn!=null) conn.close();
                } catch(SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return false;
    }

    /**
     * Query/update the database for login, register, stats_request
     *
     * @param gm GameMessage
     * @return a responsive GameMessage object
     */
    public GameMessage queryDatabase(GameMessage gm){
        PreparedStatement pst = null;
        ResultSet rs = null;
        GameMessage retval = null;
        try{
            if(gm.getMessageType()==GameMessage.messageType.LOGIN_ATTEMPT){
                System.out.println("Received login attempt from user " + gm.playerID + ".");
                String playerID = gm.playerID;
                String password = gm.password;
                pst = conn.prepareStatement("SELECT * FROM USER WHERE userName = ?");
                pst.setString(1, playerID);
                rs = pst.executeQuery();
                if (rs.next()) {
                    // playerID exist
                    if (password.equals(rs.getString("passWord"))) {
                        // password correct
                        retval = new GameMessage(GameMessage.messageType.LOGIN_SUCCESS);
                        System.out.println("User " + playerID + " logged in.");
                    }
                    else {
                        // password incorrect
                        retval = new GameMessage(GameMessage.messageType.LOGIN_FAILURE);
                        retval.errorMessage = "The password does not match the username. ";
                        System.out.println("User " + playerID + " failed to login on account of bad password.");
                    }
                }
                else {
                    // playerID does not exist
                    retval = new GameMessage(GameMessage.messageType.LOGIN_FAILURE);
                    retval.errorMessage = "The username does not exist. ";
                    System.out.println("User " + playerID + " failed to login on account of no matching username.");
                }
                retval.playerID = playerID;
            } else if(gm.getMessageType()==GameMessage.messageType.REGISTER_ATTEMPT){
                String playerID = gm.playerID;
                String password = gm.password;
                pst = conn.prepareStatement("SELECT * FROM USER WHERE userName = ?");
                pst.setString(1, playerID);
                rs = pst.executeQuery();
                if (rs.next()) {
                    // Username already exist
                    retval = new GameMessage(GameMessage.messageType.REGISTER_FAILURE);
                    retval.errorMessage = "This user name already exists. ";
                }
                else {
                    // register success
                    pst = conn.prepareStatement("INSERT INTO USER (userName, password) VALUES (?, ?)");
                    pst.setString(1, playerID);
                    pst.setString(2, password);
                    pst.executeUpdate();
                    pst = conn.prepareStatement("SELECT userID FROM USER WHERE userName=?");
                    pst.setString(1, playerID);
                    rs = pst.executeQuery();
                    int newID = 0;
                    if(rs.next()){
                        newID = rs.getInt("userID");
                    }
                    pst = conn.prepareStatement("INSERT INTO RECORD (userID, numPlayed, numWin, numLoss) VALUES (?, ?, ?, ?)");
                    pst.setInt(1, newID);
                    pst.setInt(2, 0);
                    pst.setInt(3, 0);
                    pst.setInt(4, 0);
                    pst.executeUpdate();
                    retval = new GameMessage(GameMessage.messageType.REGISTER_SUCCESS);
                }
                retval.playerID = playerID;
            } else if(gm.getMessageType()==GameMessage.messageType.STATS_REQUEST){
                retval = new GameMessage(GameMessage.messageType.STATS_RESPONSE);
                String playerID = gm.playerID;
                pst = conn.prepareStatement("SELECT * FROM USER u, RECORD r WHERE u.userID = r.userID AND u.userName=?");
                pst.setString(1, playerID);
                rs = pst.executeQuery();
                if (rs.next()) {
                    retval.numPlayed = rs.getInt("numPlayed");
                    retval.numWin = rs.getInt("numWin");
                    retval.numLoss = rs.getInt("numLoss");
                }
                retval.playerID = playerID;
            }
        } catch(SQLException e) {
            System.out.println("SQL error in queryDatabase(): "+e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
            } catch(SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return retval;
    }

    /**
     * Remove the game room from the gameRooms vector.
     * @param gr the GameRoom to be removed
     */
    public void deleteRoom(GameRoom gr){
        gameRooms.remove(gr);
    }

    /**
     * Print the game message to the server console.
     * @param message the GameMessage to be logged
     */
    public void logMessage(GameMessage message){
        System.out.println(message.getMessage());
    }

    /**
     * Add the logged in user to the matchmaking queue
     * @param playerID the id of the player that is added to the matching queue
     */
    public void addToMatchmaking(String playerID)
    {
        for(ServerThread st : loggedInQueue)
        {
            if(st.getPlayerID().equals(playerID))
            {
                loggedInQueue.remove(st);
                matchingQueue.add(st);
                break;
            }
        }

        // first come first served matchmaking
        for(int i = 0; i < matchingQueue.size() - 1; i+=2)
        {
            GameRoom gr = new GameRoom(this, matchingQueue.get(i), matchingQueue.get(i+1));
            matchingQueue.get(i).enterGame(gr);
            matchingQueue.get(i+1).enterGame(gr);
            gameRooms.add(gr);
        }
        if(matchingQueue.size() % 2 == 0)
        {
            matchingQueue.clear();
        }
        else
        {
            ServerThread lastUser = matchingQueue.lastElement();
            matchingQueue.clear();
            matchingQueue.add(lastUser);
        }
    }

}
