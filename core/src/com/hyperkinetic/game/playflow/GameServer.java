package com.hyperkinetic.game.playflow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.sql.*;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

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
    private static final int port = 8000;
    /**
     * Database connection URL
     */
    private static final String url = "mysql://localhost:3306/finalproject";
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
     * Stores player sockets in login
     */
    private static Vector<Socket> loginQueue = new Vector<>();
    // stores mapping from playerID to sockets
    // private static ConcurrentHashMap<String,Socket> playerIdMap = new ConcurrentHashMap<>();
    /**
     * stores mapping from playerID to GameRooms
     */
    private static Vector<GameRoom> gameRooms = new Vector<>();

    public static void main(String[] args){
        GameServer gs = new GameServer();
    }

    public GameServer(){
        // test database connection
        if(!checkConnection()) return;
        try {
            ServerSocket ss = new ServerSocket(port);

            while(true) {
                Socket s = ss.accept();
                loginQueue.add(s);

                // TODO: implement actual logging with sql
                // TODO: keep track of connected logged-in ServerThreads (?)
                for(Socket login : loginQueue)
                {
                    BufferedReader pwReader = new BufferedReader(new InputStreamReader(login.getInputStream()));
                    String line = pwReader.readLine();
                    if(line != null)
                    {
                        ServerThread loggedUser = new ServerThread(login, line.split(",")[0]);
                        matchingQueue.add(loggedUser);
                    }
                    // TODO: delete login from loginQueue: separate Login thread (?)
                }

                // first come first served matchmaking
                for(int i = 0; i < matchingQueue.size() - 1; i++)
                {
                    GameRoom gr = new GameRoom(this, matchingQueue.get(i), matchingQueue.get(i+1));
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

                // check for dead games
                for(GameRoom room : gameRooms)
                {
                    if(room.isOver) gameRooms.remove(room);
                }
            }
        } catch(IOException e) {
            System.out.println("Unable to create server: "+e.getMessage());
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
        Class.forName("com.mysql.cj.jdbc.Driver");
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
                ps = conn.prepareStatement("");
                rs = ps.executeQuery();
                // TODO: query USER table for userID & update RECORD table accordingly
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
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            conn = getConnection();
            ps = conn.prepareStatement("");
            rs = ps.executeQuery();
            if(gm.getMessageType()==GameMessage.messageType.LOGIN_ATTEMPT){
                String playerID = gm.playerID;
                String password = gm.password;
                // TODO: query USER table to login player
            } else if(gm.getMessageType()==GameMessage.messageType.ACCOUNT_CREATE){
                String playerID = gm.playerID;
                String password = gm.password;
                // TODO: append to USER table & RECORD table the new player
            } else if(gm.getMessageType()==GameMessage.messageType.ACCOUNT_STATS_REQUEST){
                String playerID = gm.playerID;
                // TODO: query USER table for userID & query RECORED table accordingly
            }
        } catch(ClassNotFoundException e){
            System.out.println("ClassNotFound error in queryDatabase(): "+e.getMessage());
        } catch(SQLException e){
            System.out.println("SQL error in queryDatabase(): "+e.getMessage());
        } finally {
            try {
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();
                if(conn!=null) conn.close();
            } catch(SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    public void deleteRoom(GameRoom gr){
        this.gameRooms.remove(gr);
    }
}
