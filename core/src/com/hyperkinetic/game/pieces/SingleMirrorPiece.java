package com.hyperkinetic.game.pieces;

import com.badlogic.gdx.utils.Array;
import com.hyperkinetic.game.util.Directions;

/**
 * A piece with only one mirror and three unreflective sides.
 *
 * @author cqwillia
 */
public class SingleMirrorPiece extends AbstractGamePiece
{
    public SingleMirrorPiece()
    {
        this.orientation = Directions.MirrorDirection.NORTHWEST;
    }

    public SingleMirrorPiece(Directions.MirrorDirection o)
    {
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
