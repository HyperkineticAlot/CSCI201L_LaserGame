package com.hyperkinetic.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.hyperkinetic.game.board.AbstractGameBoard;
import com.hyperkinetic.game.board.StandardBoard;

public class LaserGame extends ApplicationAdapter {
	private static SpriteBatch batch;
	private static Array<Disposable> disposables = new Array<>();

	private AbstractGameBoard board;
	
	@Override
	public void create ()
	{
		board = new StandardBoard();
		batch = new SpriteBatch();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		board.render(batch);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		for(Disposable d : disposables)
		{
			d.dispose();
		}
		disposables.clear();
	}

	public static Texture loadTexture(String path)
	{
		Texture retval = new Texture(path);
		disposables.add(retval);
		return retval;
	}
}
