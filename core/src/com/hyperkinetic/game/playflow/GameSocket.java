package com.hyperkinetic.game.playflow;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Vector;
import java.util.HashMap;
import java.io.IOException;

/**
 * GameSocket class, server side of websocket, hosts all GameRooms and implements match making
 * Note: This class should be in a separate server project instead of this local game project (client)
 */
@ServerEndpoint(value="/gs")
public class GameSocket {
    /**
     * Stores all living sessions.
     */
    private static Vector<Session> sessionVector = new Vector<>();
    /**
     * Stores all players in matchmaking.
     */
    private static Vector<Player> matchingQueue = new Vector<>();
    /**
     * Stores all ongoing GameRooms
     */
    private static Vector<GameRoom> gameRooms = new Vector<>();
    /**
     * Stores the mapping from sessionID to GameRooms
     */
    private static HashMap<String, GameRoom> gameRoomMap = new HashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Connecting complete.");
        sessionVector.add(session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        GameRoom gameRoom = gameRoomMap.get(session.getId());
        // TODO: handles sending message
    }

    @OnError
    public void onError(Throwable error) {
        System.out.println("Error: "+error.getMessage());
    }

    @OnClose
    public void onClose(Session session) {
        // delete from matchingQueue
        for(int i=0;i<matchingQueue.size();++i) {
            if(matchingQueue.get(i).getSession() == session) {
                matchingQueue.remove(i);
                return; // player still in matchingQueue does not have registered gameRoom yet
            }
        }
        // delete from gameRooms
        GameRoom gameRoom = gameRoomMap.get(session.getId());
        try{
            if(gameRoom!=null) {
                gameRoom.a.remove();
                gameRoom.b.remove();
            }
            // delete from gameRoomMap
            gameRoomMap.remove(session.getId());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // delete from sessionVector
        sessionVector.remove(session);
    }

    public static void delete(GameRoom gameRoom) {
        gameRooms.remove(gameRoom);
    }
}
