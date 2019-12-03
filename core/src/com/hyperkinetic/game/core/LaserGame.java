package com.hyperkinetic.game.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.hyperkinetic.game.board.AbstractGameBoard;
import com.hyperkinetic.game.board.StandardBoard;
import com.hyperkinetic.game.playflow.ClientThread;
import com.hyperkinetic.game.playflow.GameRoom;
import com.hyperkinetic.game.playflow.Player;

public class LaserGame extends Game {
    /**
     * The {@link SpriteBatch} object responsible for rendering textures onto the game canvas.
     */
    private static SpriteBatch batch;
    /**
     * Tracks all objects (such as Textures) that implement {@link Disposable} and need to be freed.
     */
    private static Array<Disposable> disposables = new Array<>();
    /**
     * The game board that tracks current configuration and player movements.
     */
    private AbstractGameBoard board;
    /**
     * Input processor that handles user mouse and keyboard actions.
     */
    private InputProcessor gameInputProcessor;
    /**
     * Player A of the game.
     */
    private Player playerA;
    /**
     * Player B of the game.
     */
    private Player playerB;
    /**
     * GameRoom that includes the two players of the same game.
     */
    private GameRoom gameRoom;

    public static ClientThread client;

    public static boolean IS_SERVER = false;


    public void create() {
        // board = new StandardBoard();

        client = null;
        batch = new SpriteBatch();
        gameInputProcessor = new GameInputProcessor();
        Gdx.input.setInputProcessor(gameInputProcessor);
        //playerA = new Player();
        //playerB = new Player();
        //gameRoom = new GameRoom(playerA, playerB,1, board); // trivial roomID
        this.setScreen(new MainMenuScreen(this));



    }

    public void render() {
        super.render();
    }

    public void dispose() {
        batch.dispose();
        for(Disposable d : disposables)
        {
            d.dispose();
        }
        disposables.clear();
    }

    /**
     * Centralized method for creating textures so they can be freed by <code>dispose()</code>.
     *
     * @param path a String representing a file path to the texture image
     * @return the {@link Texture} created from the image
     */
    public static Texture loadTexture(String path)
    {
        if(IS_SERVER) return null;
        Texture retval = new Texture(path);
        disposables.add(retval);
        return retval;
    }
}