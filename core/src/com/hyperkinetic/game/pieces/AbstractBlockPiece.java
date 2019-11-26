package com.hyperkinetic.game.pieces;

import com.badlogic.gdx.utils.Array;
import com.hyperkinetic.game.util.Directions;

/**
 * Abstract superclass describing the behaviour of a block piece.
 *
 * @author cqwillia briannlz
 */
public abstract class AbstractBlockPiece extends AbstractGamePiece
{
    protected Directions.Direction orientation;

    public AbstractBlockPiece(int x, int y, boolean c, Directions.Direction o) {
        super(x, y, c);
        this.orientation = o;
    }

    public AbstractBlockPiece()
    {
        orientation = Directions.Direction.NORTH;
    }

    public Directions.Direction getOrientation() {
        return orientation;
    }

    /**
     * Transforms the orientation of this piece appropriately for one quarter turn clockwise.
     */
    public void rotateRight()
    {
        if(orientation == null) return;

        if(orientation == Directions.Direction.NORTH)
            orientation = Directions.Direction.EAST;
        else if(orientation == Directions.Direction.EAST)
            orientation = Directions.Direction.SOUTH;
        else if(orientation == Directions.Direction.SOUTH)
            orientation = Directions.Direction.WEST;
        else
            orientation = Directions.Direction.NORTH;
    }

    /**
     * Transforms the orientation of this piece appropriately for one quarter turn counterclockwise.
     */
    public void rotateLeft()
    {
        if(orientation == null) return;

        if(orientation == Directions.Direction.NORTH)
            orientation = Directions.Direction.WEST;
        else if(orientation == Directions.Direction.WEST)
            orientation = Directions.Direction.SOUTH;
        else if(orientation == Directions.Direction.SOUTH)
            orientation = Directions.Direction.EAST;
        else
            orientation = Directions.Direction.NORTH;
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
            case NORTH:
                return 0F;
            case WEST:
                return 90F;
            case SOUTH:
                return 180F;
            case EAST:
                return 270F;
        }

        return 0F;
    }
}
