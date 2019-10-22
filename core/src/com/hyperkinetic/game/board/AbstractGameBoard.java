package com.hyperkinetic.game.board;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.hyperkinetic.game.pieces.AbstractGamePiece;

/**
 * A superclass for all laser gameboards. Contains code to render the gameboard as well as static
 * functionality to track the gamestate.
 *
 * @author cqwillia briannlz
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

    protected Array<AbstractGamePiece> aPieces;
    protected Array<AbstractGamePiece> bPieces;
    protected AbstractGamePiece aPharaoh;
    protected AbstractGamePiece bPharaoh;

    public AbstractGameBoard(int x, int y)
    {
        tiles = new Array<>();
        pieces = new Array<>();
        aPieces = new Array<>();
        bPieces = new Array<>();
        aPharaoh = null;
        bPharaoh = null;

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
     * Getter of the x-dimension of the board
     *
     * @return the x-dimension of te current board
     */
    public int getX() {
        return x;
    }

    /**
     * Getter of the y-dimension of the board
     *
     * @return the y-dimension of te current board
     */
    public int getY() {
        return y;
    }

    /**
     * Getter of the tiles array
     *
     * @return the tiles array of te current board
     */
    public Array<AbstractBoardTile> getTiles() {
        return tiles;
    }

    /**
     * Getter of the pieces array
     *
     * @return array of pieces
     */
    public Array<AbstractGamePiece> getPieces() { return pieces; }

    /**
     * Getter of the aPieces array
     *
     * @return array of player a's pieces of the current board
     */
    public Array<AbstractGamePiece> getAPieces() {
        return aPieces;
    }

    /**
     * Getter of the bPieces array
     *
     * @return array of player b's pieces of the current board
     */
    public Array<AbstractGamePiece> getBPieces() { return bPieces; }

    /**
     * Abstract method which populates the board with tiles based on the board type.
     */
    public abstract void createTiles();

    /**
     * Abstract method which places pieces on the board
     */
    public abstract void createPieces();

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

        // TODO: render pieces here

    }

    /**
     * Converts a location on the virtual screen to the corresponding tile on the current game board.
     *
     * @param mouseX the x location on the virtual screen
     * @param mouseY the y location on the virtual screen
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

    /**
     * Converts a coordinate to the corresponding tile on the current game board.
     *
     * @param x the x location on the board
     * @param y the y location on the board
     * @return the board tile at the given location,
     * or <code>null</code> if the coordinate is invalid.
     */
    public static AbstractBoardTile getTileFromCoordinate(int x, int y)
    {
        if (x < 0 || y < 0 || x >= board.x || y >= board.y) {
            return null;
        }
        return (board.tiles.get(y * board.y + x));
    }

    /**
     * Converts a coordinate to the corresponding piece on the current game board.
     *
     * @param x the x location on the board
     * @param y the y location on the board
     * @return the game piece at the given location,
     * or <code>null</code> if either the coordinate is invalid or no piece is place on that tile.
     */
    public static AbstractGamePiece getPieceFromCoordinate(int x, int y)
    {
        if (x < 0 || y < 0 || x >= board.x || y >= board.y) {
            return null;
        }
        return (board.pieces.get(y * board.y + x));
    }

    /**
     * Abstract method which returns whether game is over based on board type
     *
     * @return "AWin" or "BWin" or "NoWin"
     */
    public abstract String isGameOver();

    /**
     *
     * @param pID specifies which pieces to operate
     * @param piece chosen piece to ratate left
     * @return true if success
     */
    public boolean rotateLeft(String pID, AbstractGamePiece piece) {
        // check piece returned matches pID
        if(pID.equals("a")) {
            //
        } else {
            //
        }
        piece.rotateLeft();
        return true;
    }

    /**
     *
     * @param pID specifies which pieces to operate
     * @param piece chosen piece to ratate right
     * @return true if success
     */
    public boolean rotateRight(String pID, AbstractGamePiece piece) {
        // check piece returned matches pID
        if(pID.equals("a")) {
            //
        } else {
            //
        }
        piece.rotateRight();
        return true;
    }

    // TODO: implement move operations
}
