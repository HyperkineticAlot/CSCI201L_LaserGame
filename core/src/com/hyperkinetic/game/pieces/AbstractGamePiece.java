package com.hyperkinetic.game.pieces;

import com.badlogic.gdx.utils.Array;
import com.hyperkinetic.game.util.Directions;

/**
 * Abstract superclass describing the behaviour of a game piece.
 *
 * @author cqwillia
 */
public abstract class AbstractGamePiece
{
    protected Directions.MirrorDirection orientation;

    /**
     * Transforms the orientation of this piece appropriately for one quarter turn.
     */
    public void rotate()
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
    }

    /**
     * Defines the behaviour of a laser when it encounters this game piece.
     *
     * @param laserDirection the direction of the incoming laser
     * @return an {@link Array} of Directions representing the outgoing lasers from this piece,
     * or null if the laser is not reflected.
     */
    public abstract Array<Directions.Direction> acceptLaser(Directions.Direction laserDirection);
}
