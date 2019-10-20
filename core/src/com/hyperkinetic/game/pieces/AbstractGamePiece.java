package com.hyperkinetic.game.pieces;

import com.badlogic.gdx.utils.Array;
import com.hyperkinetic.game.board.AbstractBoardTile;
import com.hyperkinetic.game.board.AbstractGameBoard;
import com.hyperkinetic.game.util.Directions;

/**
 * Abstract superclass describing the behaviour of a game piece.
 *
 * @author cqwillia briannlz
 */
public abstract class AbstractGamePiece
{
    protected int x, y;

    public AbstractGamePiece(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Transforms the orientation of this piece appropriately for one quarter turn.
     */
    /*public void rotate()
    {
        if(orientation == null) return;

        if(orientation == Directions.MirrorDirection.NORTHWEST)
            orientation = Directions.MirrorDirection.NORTHEAST;
        else if(orientation == Directions.MirrorDirection.NORTHEAST)
            orientation = Directions.MirrorDirection.SOUTHEAST;
        else if(orientation == Directions.MirrorDirection.SOUTHEAST)
            orientation = Directions.MirrorDirection.SOUTHWEST;
        else
            orientation = Directions.MirrorDirection.NORTHWEST;
    }*/

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
        return b.getPieces().removeIndex(this.y*b.getY() + this.x);
    }

    /**
     * Defines the behaviour of a game piece when it is placed on the board
     *
     * @param b the game board that contains this piece
     */
    public void placePiece(AbstractGameBoard b) {
        b.getPieces().insert(this.y*b.getY() + this.x, this);
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

}
