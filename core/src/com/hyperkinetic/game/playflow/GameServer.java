package com.hyperkinetic.game.playflow;

import java.io.IOException;
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
    private static Vector<Socket> matchingQueue = new Vector<>();
    // stores mapping from roomID to GameRooms
    private static ConcurrentHashMap<String,GameRoom> roomIdMap = new ConcurrentHashMap<>();
    // stores mapping from playerID to GameRooms
    // private static HashMap<String, GameRoom> playerIdMap = new HashMap<>();

    public static void main(String[] args){
        GameServer gs = new GameServer();
    }

    public GameServer(){
        // test database connection
        if(!checkConnection()) return;
        try {
            ServerSocket ss = new ServerSocket(port);
            int count = 0;
            // TODO: add actual match making logic
            while(count!=2) {
                Socket s = ss.accept();
                matchingQueue.add(s);
                count++;
            }
            GameRoom gr = new GameRoom("testGameRoom",this, matchingQueue.get(0),matchingQueue.get(1));
            matchingQueue.clear();
            roomIdMap.put("testGameRoom",gr);
            // playerIdMap.put("testGameRoom:A",gr);
            // playerIdMap.put("testGameRoom:B",gr);
        } catch(IOException e) {
            System.out.println("Unable to create server: "+e.getMessage());
        }
    }

    public void readMessage(GameMessage gm){
        System.out.println(gm.getMessage());
        GameMessage.messageType type = gm.getMessageType();
        if(type==GameMessage.messageType.ROOM_CREATE){
            // do nothing
        } else if(type==GameMessage.messageType.PLAYER_MOVE){
            String roomID = gm.roomID;
            String playerID = gm.playerID;
            int x = gm.x;
            int y = gm.y;
            String moveType = gm.moveType;
            int moveX = gm.moveX;
            int moveY = gm.moveY;
            GameRoom gr = roomIdMap.get(roomID);
            gr.handleMoveAttempt(playerID,x,y,moveType,moveX,moveY);
        } else if(type==GameMessage.messageType.PLAYER_TURN){
            String roomID = gm.roomID;
            String playerID = gm.playerID;
            GameRoom gr = roomIdMap.get(roomID);
            gr.handleTurn(playerID);
        } else if(type==GameMessage.messageType.GAME_OVER){
            String roomID = gm.roomID;
            String playerID = gm.playerID;
            String player2ID = gm.player2ID;
            GameRoom gr = roomIdMap.get(roomID);
            // update database
            // delete gr from server
            // notify clientThread
        } else if(type==GameMessage.messageType.MOVE_SUCCESS){
            String roomID = gm.roomID;
            String playerID = gm.playerID;
            int x = gm.x;
            int y = gm.y;
            String moveType = gm.moveType;
            int moveX = gm.moveX;
            int moveY = gm.moveY;
            GameRoom gr = roomIdMap.get(roomID);
            gr.updateBoard(playerID,x,y,moveType,moveX,moveY);
        } else if(type==GameMessage.messageType.MOVE_FAILURE){
            // do what(?)
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
