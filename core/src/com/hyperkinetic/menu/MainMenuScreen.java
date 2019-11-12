package com.hyperkinetic.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.hyperkinetic.game.core.LaserGame;

public class MainMenuScreen  extends InputAdapter implements Screen {
    LaserGame game;
    private Stage stage;
    private SpriteBatch batch ;
    private Label outputLabel;

    Texture exitButtonActive;
    Texture exitButtonInactive;

    Texture playButtonActive;
    Texture playButtonInactive;
    Texture playButtonGreen;

    public MainMenuScreen (LaserGame game) {
        // constructor
        this.game = game;
        batch = new SpriteBatch();
        //playButtonInactive = new Texture("playButtonInactive.png");
        //playButtonActive = new Texture("playButtonActive.png");
        //playButtonGreen = new Texture("buttonGreen.png");
        stage = new Stage(new ScreenViewport());


        Skin neon = new Skin(Gdx.files.internal("skin/neon-ui.json"));

        Button button2 = new TextButton("LOG IN", neon);
        button2.setSize(200,100);
        button2.setPosition(1920/2, 1280/2);
        button2.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                outputLabel.setText("Press a Button");
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                outputLabel.setText("Pressed Text Button");
                return true;
            }
        });
        stage.addActor(button2);


        Button button1 = new TextButton("GUEST", neon);
        button1.setSize(200,100);
        button1.setPosition(1920/2, 1280/2 + 200);
        button1.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                outputLabel.setText("Press a Button");
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                outputLabel.setText("Pressed Text Button");
                return true;
            }
        });
        stage.addActor(button1);


        outputLabel = new Label("Press a Button",neon);
        outputLabel.setSize(100,100);
        outputLabel.setPosition(0,500);
        outputLabel.setAlignment(Align.center);
        stage.addActor(outputLabel);
    }



    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(255,255,255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
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
        stage.dispose();
    }
}
