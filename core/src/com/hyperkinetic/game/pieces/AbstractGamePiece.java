package com.hyperkinetic.game.pieces;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.hyperkinetic.game.board.AbstractBoardTile;
import com.hyperkinetic.game.board.AbstractGameBoard;
import com.hyperkinetic.game.core.LaserGame;
import com.hyperkinetic.game.util.Directions;

/**
 * Abstract superclass describing the behaviour of a game piece.
 *
 * @author cqwillia briannlz
 */
public abstract class AbstractGamePiece
{
    private static final int IMG_DIM = 300;

    /**
     * The location of the piece on the board.
     */
    protected int x, y;
    /**
     * The color of the piece. True = white, false = black.
     */
    protected boolean color;

    private Texture texture;

    public AbstractGamePiece(int x, int y, boolean c) {
        this.x = x;
        this.y = y;
        color = c;
        loadRegion("pieces/singlemirror.png");
    }

    /**
     * Modifier of the x location of the piece.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Modifier of the y location of the piece.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Getter of the x location of the piece.
     *
     * @return the x location of the piece
     */
    public int getX() {
        return x;
    }

    /**
     * Getter of the y location of the piece.
     *
     * @return the y location of the piece
     */
    public int getY() {
        return y;
    }

    public boolean getColor() { return color; }

    /**
     * Transforms the orientation of this piece appropriately for one quarter turn clockwise.
     */
    public abstract void rotateRight();

    /**
     * Transforms the orientation of this piece appropriately for one quarter turn counterclockwise.
     */
    public abstract void rotateLeft();

    /**
     * Defines the behaviour of a laser when it encounters this game piece.
     *
     * @param laserDirection the direction of the incoming laser
     * @return an {@link Array} of Directions representing the outgoing lasers from this piece,
     * or null if the laser is not reflected.
     */
    public abstract Array<Directions.Direction> acceptLaser(Directions.Direction laserDirection);

    /**
     * Defines the behaviour of a game piece when it is picked up
     *
     * @param b the game board that contains this piece
     */
    public AbstractGamePiece pickUpPiece(AbstractGameBoard b) {
        b.getPieces().set(this.y*b.getX() + this.x,null);
        return this;
    }

    /**
     * Defines the behaviour of a game piece when it is placed on the board
     *
     * @param b the game board that contains this piece
     */
    public void placePiece(AbstractGameBoard b) {
        b.getPieces().set(this.y*b.getY() + this.x, this);
    }

    /**
     * Get the legal tiles that this piece can be moved to.
     * Legal tiles are those next to this piece and not occupied by others.
     *
     * @param b The game board that contains this piece
     * @return The array containing all legal tiles of the board.
     */
    public Array<AbstractBoardTile> getLegalMoves(AbstractGameBoard b) {
        Array<AbstractBoardTile> retval = new Array<AbstractBoardTile>();
        int x = this.x;
        int y = this.y;

        for (int j = y-1; j <= y+1; j++) {
            for (int i = x-1; i <= x+1; i++) {
                if ((b.getTileFromCoordinate(i, j)) != null && b.getPieceFromCoordinate(i, j) == null && j != y && i != x) {
                    retval.add(b.getTileFromCoordinate(i, j));
                }
            }
        }
        return retval;
    }

    /**
     * Render the piece above the tile.
     *
     * @param sb the SpriteBatch of the game
     */
    public void render(SpriteBatch sb, int x, int y, int width, int height)
    {
        render(sb, x, y, width, height, false, false);
    }

    public void render(SpriteBatch sb, int x, int y, int width, int height, boolean flipped)
    {
        render(sb, x, y, width, height, flipped, false);
    }

    public void render(SpriteBatch sb, int x, int y, int width, int height, boolean flipped, boolean pickedUp)
    {
        int scale = pickedUp ? 2 : 1;

        sb.draw(texture, x, y, width/2F, height/2F, width, height, scale, scale, 0, 0, 0, IMG_DIM, IMG_DIM, flipped, flipped);

    }

    protected void loadRegion(String image)
    {
        texture = LaserGame.loadTexture(image);
    }

    protected void loadRegion(Class pieceType)
    {
        //TODO
    }

    private static String getPathFromPieceType(Class pieceType)
    {
        //TODO
        return "pieces/singlemirror.png";
    }
}
