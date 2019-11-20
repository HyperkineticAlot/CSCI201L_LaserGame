package com.hyperkinetic.game.playflow;

import com.hyperkinetic.game.board.StandardBoard;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class LocalGameRoom extends GameRoom
{
    public LocalGameRoom(String playerID)
    {
        try
        {
            ServerSocket serverSocket = new ServerSocket(GameRoom.PORT);
            while(aThread == null || bThread == null)
            {
                Socket socket = serverSocket.accept();
                if(aThread == null)
                {
                    aThread = new ServerThread(socket, playerID);
                }
                else
                {
                    bThread = new ServerThread(socket, "Opponent");
                }
            }

            this.board = new StandardBoard(true, true);
            GameMessage gm = new GameMessage(GameMessage.messageType.ROOM_CREATE);
            gm.startBoard = board;
            gm.playerID = playerID;
            gm.player2ID = "Opponent";
            aThread.sendMessage(gm);
        }
        catch(IOException ioe)
        {
            System.out.println("IOException in creation of LocalGameRoom");
            ioe.printStackTrace();
        }
    }
}
