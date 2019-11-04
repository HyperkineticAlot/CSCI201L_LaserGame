/**
 * Contains the core elements of the game, including the central loop
 * and the input processor.
 */
package com.hyperkinetic.game.core;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.hyperkinetic.game.board.AbstractGameBoard;
import com.hyperkinetic.game.board.StandardBoard;
import com.hyperkinetic.game.playflow.GameRoom;
import com.hyperkinetic.game.playflow.Player;

/**
 * Implements the main game loop. Instantiated by the Launcher.
 *
 * @author cqwillia
 */
public class LaserGame extends ApplicationAdapter {
	/**
	 * The {@link SpriteBatch} object responsible for rendering textures onto the game canvas.
	 */
	private static SpriteBatch batch;
	/**
	 * Tracks all objects (such as Textures) that implement {@link Disposable} and need to be freed.
	 */
	private static Array<Disposable> disposables = new Array<>();

	private AbstractGameBoard board;
	private InputProcessor gameInputProcessor;
	private Player playerA;
	private Player playerB;
	private GameRoom gameRoom;

	@Override
	public void create ()
	{
		board = new StandardBoard();
		batch = new SpriteBatch();
		gameInputProcessor = new GameInputProcessor();
		Gdx.input.setInputProcessor(gameInputProcessor);
		playerA = new Player();
		playerB = new Player();
		gameRoom = new GameRoom(playerA, playerB,1, board); // trivial roomID
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

	/**
	 * Centralized method for creating textures so they can be freed by <code>dispose()</code>.
	 *
	 * @param path a String representing a file path to the texture image
	 * @return the {@link Texture} created from the image
	 */
	public static Texture loadTexture(String path)
	{
		Texture retval = new Texture(path);
		disposables.add(retval);
		return retval;
	}
}
