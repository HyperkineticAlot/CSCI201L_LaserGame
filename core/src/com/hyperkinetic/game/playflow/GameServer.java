package com.hyperkinetic.game.playflow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.HashMap;
import java.sql.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * GameSocket class, server side of websocket, hosts all GameRooms and implements match making
 * Note: This class should be in a separate server project instead of this local game project (client)
 */

public class GameServer {
    // server info
    private static final String host = "localhost";
    private static final int port = 8000;
    // database info
    private static final String url = "mysql://localhost:3306/finalproject";
    private static final String user = "root";
    private static final String pwd = "root";
    // stores all player sockets in matchmaking
    private static Vector<ServerThread> matchingQueue = new Vector<>();
    // stores player sockets in login
    private static Vector<Socket> loginQueue = new Vector<>();
    // stores mapping from playerID to sockets
    // private static ConcurrentHashMap<String,Socket> playerIdMap = new ConcurrentHashMap<>();
    // stores mapping from playerID to GameRooms
    private static Vector<GameRoom> gameRooms = new Vector<>();

    public static void main(String[] args){
        GameServer gs = new GameServer();
    }

    public GameServer(){
        // test database connection
        if(!checkConnection()) return;
        try {
            ServerSocket ss = new ServerSocket(port);
            // TODO: add actual match making logic

            while(true) {
                Socket s = ss.accept();
                loginQueue.add(s);

                // logging users in the queue
                for(Socket login : loginQueue)
                {
                    BufferedReader pwReader = new BufferedReader(new InputStreamReader(login.getInputStream()));
                    String line = pwReader.readLine();
                    if(line != null)
                    {
                        ServerThread loggedUser = new ServerThread(login, line.split(",")[0]);
                        matchingQueue.add(loggedUser);
                    }
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

    public boolean checkConnection() {
        boolean success = false;
        try{
            Connection conn = GameServer.getConnection();
            success = true;
        } catch(ClassNotFoundException | SQLException e){
            System.out.println("Unable to connect to database: "+e.getMessage());
        }
        return success;
    }

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:"+url+"?user="+user+"&password="+pwd);
        return conn;
    }
}
