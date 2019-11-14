package com.hyperkinetic.game.playflow;

import com.badlogic.gdx.InputProcessor;
import com.hyperkinetic.game.board.AbstractGameBoard;

import java.io.IOException;
import java.net.Socket;
import java.util.Random;

/**
 * Player class
 */
public class Player {
    public String playerID;
    public boolean isGuest = false;
    public boolean isAI = false;

    /**
     * if not signed in, play as guest
     */
    public Player(String hostname, int port) {
        this.playerID = "Guest";
        this.isGuest = true;
        try {
            System.out.println("Trying to connect to "+hostname+":"+port);
            Socket s = new Socket(hostname,port);
            System.out.println("Player "+playerID+" is connected to "+hostname+":"+port);
        } catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * make a movement on board, called by GameRoom class
     * @param board GameBoard to operate on
     * @param pID "a" or "b" signifies which pieces to operate on in board
     * @return true if a valid move is made
     */
    public boolean makeMove(AbstractGameBoard board, String pID) {
        // make a valid move
        while(true) {
            // TODO: get input from gameInputProcessor: left click - select piece
            // TODO: get legal moves for selected piece
            // TODO: get input from gameInputProcessor: L - rotate left, R - rotate right, Arrows - move
            /* Ex:
            // pseudo code to get input
            AbstractGamePiece piece = GameInputProcessor.getPieceFromTouchUp();
            while(piece!=null) { // get one piece selected
                piece = GameInputProcessor.getPieceFromTouchUp();
            }
            String move = GameInputProcessor.getKeyBoardInput();
            while(move!=null) { // get one input movement
                move = GameInputProcessor.getKeyBoardInput();
            }

            // real code to move selected piece
            if(!board.isValidMove(pID,piece,move)) {
                continue;
            } else { // call board move method
                if(move=='L') {
                    board.pieceRotateLeft(piece);
                    break;
                } else if(move=='R') {
                    board.pieceRotateRight(piece);
                    break;
                } else if(move=='W') {
                    int x = piece.getX();
                    int y = piece.getY()+1;
                    board.pieceMove(piece, x, y);
                    break;
                } else if(move=='S') {
                    int x = piece.getX();
                    int y = piece.getY()-1;
                    board.pieceMove(piece, x, y);
                    break;
                } else if(move=='A') {
                    int x = piece.getX()-1;
                    int y = piece.getY();
                    board.pieceMove(piece, x, y);
                    break;
                } else if(move=='D') {
                    int x = piece.getX()+1;
                    int y = piece.getY();
                    board.pieceMove(piece, x, y);
                    break;
                }
            }
            */
            if(false) break; // delete this line
        }
        return true;
    }
}
