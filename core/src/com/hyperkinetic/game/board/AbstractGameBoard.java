package com.hyperkinetic.game.board;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.hyperkinetic.game.pieces.AbstractGamePiece;

/**
 * A superclass for all laser gameboards. Contains code to render the gameboard as well as static
 * functionality to track the gamestate.
 *
 * @author cqwillia
 */
public abstract class AbstractGameBoard
{
    /**
     * Tracks the gameboard currently being played on for centralized modification by various game objects.
     */
    private static AbstractGameBoard board = null;

    protected int x;
    protected int y;
    private int screenX;
    private int screenY;
    private int tileDim;
    protected Array<AbstractBoardTile> tiles;
    protected Array<AbstractGamePiece> pieces;

    public AbstractGameBoard(int x, int y)
    {
        tiles = new Array<>();
        pieces = new Array<>();

        int xSpace = (int)(Gdx.graphics.getWidth() * .60);
        int ySpace = (int)(Gdx.graphics.getHeight() * .80);
        this.x = x;
        this.y = y;

        double boardRatio = ((double) x / y);
        double screenRatio = ((double) Gdx.graphics.getWidth() / Gdx.graphics.getHeight());

        if(boardRatio > screenRatio)
        {
            tileDim = xSpace / x;
            screenX = (int)(Gdx.graphics.getWidth() * .20);
            screenY = (Gdx.graphics.getHeight() - y * tileDim) / 2;
        }
        else
        {
            tileDim = ySpace / y;
            screenY = (int)(Gdx.graphics.getHeight() * .10);
            screenX = (Gdx.graphics.getWidth() - x * tileDim) / 2;
        }

        AbstractGameBoard.board = this;
    }

    /**
     * Abstract method which populates the board with tiles based on the board type.
     */
    public abstract void create();

    /**
     * Renders the current game board. Called by the main game loop.
     *
     * @param sb the {@link SpriteBatch} responsible for drawing game objects.
     */
    public void render(SpriteBatch sb)
    {
        // starting from the bottom left
        for(int i = 0; i < y; i++)
        {
            for(int j = 0; j < x; j++)
            {
                tiles.get(j + i * x).render(sb, screenX + j * tileDim, screenY + i * tileDim, tileDim, tileDim);
            }
        }
    }

    /**
     * Converts a location on the virtual screen to the corresponding tile on the current game board.
     *
     * @param mouseX the x location on the virtual screen
     * @param mouseY the y location on the virtal screen
     * @return the board tile at the given location, or <code>null</code> if the mouse is outside the game board.
     */
    public static AbstractBoardTile getTileFromLocation(int mouseX, int mouseY)
    {
        if(mouseX < board.screenX || mouseY < board.screenY
                || mouseX > board.screenX + board.x * board.tileDim || mouseY > board.screenY + board.y * board.tileDim)
            return null;

        // int j = (mouseY - screenY) / tileDim;
        // int i = (mouseX - screenX) / tileDim;
        return board.tiles.get((mouseY - board.screenY) / board.tileDim + ((mouseX - board.screenX) / board.tileDim) * board.x);
    }
}
