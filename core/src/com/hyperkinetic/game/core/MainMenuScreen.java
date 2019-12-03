package com.hyperkinetic.game.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;

//import javax.xml.soap.Text;

public class MainMenuScreen  extends InputAdapter implements Screen {
    LaserGame game;
    private Stage stage;
    private SpriteBatch batch;
    private Label outputLabel;
    private Texture backgroundPic;
    private Texture titlePic;
    private float width;
    private float height;
    private OrthographicCamera camera;

    public static Music bgm;
    public static boolean playBgm = true;
    public static boolean initialPlaying = true;

    public MainMenuScreen (final LaserGame game) {
        if(initialPlaying) {
            bgm = Gdx.audio.newMusic(Gdx.files.internal("scsv2872.mp3"));
            bgm.setVolume(0.5f);                 // sets the volume to half the maximum volume
            bgm.setLooping(true);                // will repeat playback until music.stop() is called
        }

        if(playBgm && initialPlaying) {
            bgm.play();                      // resumes the playback
        }

        this.width = Gdx.graphics.getWidth();
        this.height = Gdx.graphics.getHeight();
        // constructor
        this.game = game;

        // to make background and button scope resizable
        camera = new OrthographicCamera(width, height);
        camera.setToOrtho(false,width, height);
        stage = new Stage(new StretchViewport(width, height, camera));


        batch = new SpriteBatch();

        backgroundPic = new Texture(Gdx.files.internal("reboundBackground.jpg"));
        //titlePic = new Texture(Gdx.files.internal("LaserGameTitle.png"));

        Skin neon = new Skin(Gdx.files.internal("skin/neon-ui.json"));
        neon.getFont("font").getData().setScale(1.20f, 1.20f);


        Button guest = new TextButton(LaserGame.client == null ? "GUEST" : "PLAY", neon);
        guest.setSize((float)(width / 9.6),(float)(height / 10.8));
        guest.setPosition(width/2 - (float)(width / 9.6) / 2, height/2 + (float)(height / 10.8));
        if(LaserGame.client == null)
        {
            guest.addListener(new InputListener(){
                @Override
                public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                    game.setScreen(new LocalGameScreen(game));
                }
                @Override
                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                    return true;
                }
            });
        }
        else
        {
            guest.addListener(new InputListener()
            {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button)
                {
                    LaserGame.client.getPlayer().sendMatchmakingRequest();
                    game.setScreen(new LaserGameScreen(game));
                }
                @Override
                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                    return true;
                }
            });
        }
        stage.addActor(guest);


        Button login = new TextButton(LaserGame.client == null ? "LOG IN" : "LOG OUT", neon);
        login.setSize((float)(width / 9.6),(float)(height / 10.8));
        login.setPosition(width / 2 - (float)(width / 9.6) / 2, height / 2);
        if(LaserGame.client == null) {
            login.addListener(new InputListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    game.setScreen(new LogInScreen(game));
                }

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    return true;
                }
            });
        }
        else
        {
            login.addListener(new InputListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    LaserGame.client = null;
                    game.setScreen(new MainMenuScreen(game));
                }
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    return true;
                }
            });
        }
        stage.addActor(login);


        Button settings = new TextButton("SETTINGS", neon);
        settings.setSize((float)(width / 9.6),(float)(height / 10.8));
        settings.setPosition(width/2 - (float)(width / 9.6) / 2, height/2 - (float)(height / 10.8));
        settings.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new SettingsScreen(game));
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(settings);


        Button quit = new TextButton("QUIT", neon);
        quit.setSize((float)(width / 9.6),(float)(height / 10.8));
        quit.setPosition(width/2 - (float)(width / 9.6) / 2, height / 2 - 2 * (float)(height / 10.8));
        quit.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }
        });
        stage.addActor(quit);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.getBatch().begin();
        stage.getBatch().draw(backgroundPic, 0, 0, width, height);
        //stage.getBatch().draw(titlePic, width / 2 - 958/ 2 , height - 4 * 86);
        stage.getBatch().end();

        stage.act();
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
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