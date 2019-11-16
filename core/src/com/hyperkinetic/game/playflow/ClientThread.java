package com.hyperkinetic.game.playflow;

import com.hyperkinetic.game.board.AbstractGameBoard;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientThread extends Thread {
    private ObjectInputStream in;
    private AbstractGameBoard board;

    private Player player;
    private Socket socket;

    public ClientThread(String hostname, int port)
    {
        board = null;
        try
        {
            System.out.println("Trying to connect to "+hostname+":"+port);
            socket = new Socket(hostname, port);
            player = new Player("guest", socket);
            System.out.println("Player "+player.playerID+" is connected to "+hostname+":"+port);
            this.start();
            player.start();

            in = new ObjectInputStream(socket.getInputStream());
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        // receive start-of-game message

        while(true)
        {
            // TODO: CHECK FOR SERVER PACKETS AND PROCESS
        }
    }
}
