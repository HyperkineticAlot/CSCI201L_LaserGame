package com.hyperkinetic.game.core;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;

public class GameOverScreen  extends InputAdapter implements Screen {

    LaserGame game;

    public GameOverScreen(final LaserGame game){
        this.game = game;
    }

    @Override
    public void show() {
        int numPlayed = game.client.getPlayer().getNumPlayed();
        int numWin = game.client.getPlayer().getNumWin();
        int numLoss = game.client.getPlayer().getNumLoss();

    }

    @Override
    public void render(float delta) {

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

    }
}
