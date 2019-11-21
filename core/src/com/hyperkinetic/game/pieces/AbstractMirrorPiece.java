package com.hyperkinetic.game.pieces;

import com.badlogic.gdx.utils.Array;
import com.hyperkinetic.game.util.Directions;

import static com.hyperkinetic.game.util.Directions.MirrorDirection.NORTHWEST;

/**
 * Abstract superclass describing the behaviour of a mirror piece.
 *
 * @author cqwillia briannlz
 */
public abstract class AbstractMirrorPiece extends AbstractGamePiece
{
    protected Directions.MirrorDirection orientation;

    public AbstractMirrorPiece(int x, int y, boolean c, Directions.MirrorDirection o) {
        super(x, y, c);
        this.orientation = o;
    }

    public Directions.MirrorDirection getOrientation() {
        return orientation;
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
    public void rotateRight()
    {
        if(orientation == null) return;

        if(orientation == NORTHWEST)
            orientation = Directions.MirrorDirection.NORTHEAST;
        else if(orientation == Directions.MirrorDirection.NORTHEAST)
            orientation = Directions.MirrorDirection.SOUTHEAST;
        else if(orientation == Directions.MirrorDirection.SOUTHEAST)
            orientation = Directions.MirrorDirection.SOUTHWEST;
        else
            orientation = NORTHWEST;
    }

    /**
     * Transforms the orientation of this piece appropriately for one quarter turn counterclockwise.
     */
    public void rotateLeft()
    {
        if(orientation == null) return;

        if(orientation == NORTHWEST)
            orientation = Directions.MirrorDirection.SOUTHWEST;
        else if(orientation == Directions.MirrorDirection.NORTHEAST)
            orientation = NORTHWEST;
        else if(orientation == Directions.MirrorDirection.SOUTHEAST)
            orientation = Directions.MirrorDirection.NORTHEAST;
        else
            orientation = Directions.MirrorDirection.SOUTHEAST;
    }

    /**
     * Defines the behaviour of a laser when it encounters this game piece.
     *
     * @param laserDirection the direction of the incoming laser
     * @return an {@link Array} of Directions representing the outgoing lasers from this piece,
     * or null if the laser is not reflected.
     */
    public abstract Array<Directions.Direction> acceptLaser(Directions.Direction laserDirection);

    @Override
    protected float getRotation()
    {
        switch(orientation)
        {
            case NORTHWEST:
                return 0F;
            case SOUTHWEST:
                return 90F;
            case SOUTHEAST:
                return 180F;
            case NORTHEAST:
                return 270F;
        }

        return 0F;
    }
}
