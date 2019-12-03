package com.hyperkinetic.game.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import static com.hyperkinetic.game.core.MainMenuScreen.bgm;

public class GameOverScreen  extends InputAdapter implements Screen {

    LaserGame game;
    private Stage stage;
    private SpriteBatch batch;
    private Texture backgroundPic;
    private float width;
    private float height;
    private OrthographicCamera camera;

    public GameOverScreen(final LaserGame game){
        this.game = game;
        this.width = Gdx.graphics.getWidth();
        this.height = Gdx.graphics.getHeight();
        this.game = game;

        camera = new OrthographicCamera(width, height);
        camera.setToOrtho(false,width, height);
        stage = new Stage(new StretchViewport(width, height, camera));

        backgroundPic = new Texture(Gdx.files.internal("reboundBackground.jpg"));
        //titlePic = new Texture(Gdx.files.internal("LaserGameTitle.png"));

        Skin neon = new Skin(Gdx.files.internal("skin/neon-ui.json"));
        neon.getFont("font").getData().setScale(1.20f * width / 1920, 1.20f * height / 1280);


        Label bar = new Label("VOLUME", neon);
        bar.setSize((float)(width / 9.6),(float)(height / 10.8));
        bar.setPosition(width / 2 - (float)(width / 9.6) / 6, height / 2 + (float) ((1.5) * (height / 10.8)));



        Button back = new TextButton("BACK TO THE MAIN MENU", neon);
        back.setSize((float)(width / 9.6),(float)(height / 10.8));
        back.setPosition(width / 2 - (float)(width / 9.6) / 2, height / 2 - (float)(height / 10.8));
        back.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new MainMenuScreen(game));
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(back);
        stage.addActor(bar);
    }

    @Override
    public void show() {
        boolean lastGame = game.client.getPlayer().getLastGame();
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
