package com.hyperkinetic.game.playflow;

import com.hyperkinetic.game.board.AbstractGameBoard;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientThread extends Thread {
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private AbstractGameBoard board;

    public ClientThread(Socket s, AbstractGameBoard board)
    {
        board = null;
        try
        {
            in = new ObjectInputStream(s.getInputStream());
            out = new ObjectOutputStream(s.getOutputStream());

            this.start();
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
            try {
                GameMessage nextMessage = (GameMessage)in.readObject();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
