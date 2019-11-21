package com.hyperkinetic.game.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

//import javax.xml.soap.Text;

public class LogInScreen  extends InputAdapter implements Screen {
    LaserGame game;
    private Stage stage;
    private SpriteBatch batch;
    private Label outputLabel;
    private Texture backgroundPic;
    private Texture titlePic;
    private int width;
    private int height;

    public LogInScreen (final LaserGame game) {
        // constructor
        this.game = game;

        this.width = Gdx.graphics.getWidth();
        this.height = Gdx.graphics.getHeight();

        stage = new Stage(new ScreenViewport());
        batch = new SpriteBatch();

        backgroundPic = new Texture(Gdx.files.internal("tempBackground.png"));
        titlePic = new Texture(Gdx.files.internal("LaserGameTitle.png"));

        Skin neon = new Skin(Gdx.files.internal("skin/neon-ui.json"));
        neon.getFont("font").getData().setScale(1.20f, 1.20f);



        final TextField username = new TextField("", neon);
        username.setPosition(width / 2,height / 2);
        username.setSize(200, 50);
        TextField password = new TextField("", neon);

        Button settings = new TextButton("LOG IN", neon);
        settings.setSize(200,100);
        settings.setPosition(1920/2 - 100, 1280/2 - 200);
        settings.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println(username.getText());
                //game.setScreen(new MainMenuScreen(game));
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(settings);
        stage.addActor(username);
    }



    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(255,255,255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.getBatch().begin();
        stage.getBatch().draw(backgroundPic, 0, 0, 1920, 1280);
        stage.getBatch().draw(titlePic, 1920 / 2 - 958/ 2 , 1000);
        stage.getBatch().end();

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