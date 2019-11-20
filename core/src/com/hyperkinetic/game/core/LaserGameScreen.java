package com.hyperkinetic.game.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.hyperkinetic.game.board.AbstractGameBoard;
import com.hyperkinetic.game.board.StandardBoard;
import com.hyperkinetic.game.playflow.ClientThread;
import com.hyperkinetic.game.playflow.GameRoom;

public class LaserGameScreen implements Screen {

    /**
     * The {@link SpriteBatch} object responsible for rendering textures onto the game canvas.
     */
    private static SpriteBatch batch;
    /**
     * The game board that tracks current configuration and player movements.
     */
    private AbstractGameBoard board;
    /**
     * Tracks all objects (such as Textures) that implement {@link Disposable} and need to be freed.
     */
    private static Array<Disposable> disposables = new Array<>();

    private Stage stage;
    private Game game;

    public LaserGameScreen(Game aGame) {
        game = aGame;
        stage = new Stage(new ScreenViewport());
        // board = new StandardBoard();
        ClientThread localPlayer = new ClientThread("localhost", GameRoom.PORT,true,false);
        batch = new SpriteBatch();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(100, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        board.render(batch);

        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        for(Disposable d : disposables)
        {
            d.dispose();
        }
        disposables.clear();
    }
}