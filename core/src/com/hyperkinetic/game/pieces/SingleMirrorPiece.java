package com.hyperkinetic.game.pieces;

import com.badlogic.gdx.utils.Array;
import com.hyperkinetic.game.util.Directions;

/**
 * A piece with only one mirror and three unreflective sides.
 *
 * @author cqwillia briannlz
 */
public class SingleMirrorPiece extends AbstractMirrorPiece
{
    public SingleMirrorPiece(int x, int y)
    {
        super(x, y);
        this.orientation = Directions.MirrorDirection.NORTHWEST;
    }

    public SingleMirrorPiece(int x, int y, Directions.MirrorDirection o)
    {
        super(x, y);
        this.orientation = o;
    }

    @Override
    public Array<Directions.Direction> acceptLaser(Directions.Direction laserDirection)
    {
        Directions.Direction result = Directions.reflect(this.orientation, laserDirection);
        if(result == null) return null;

        Array<Directions.Direction> retval = new Array<>();
        retval.add(result);
        return retval;
    }
}
