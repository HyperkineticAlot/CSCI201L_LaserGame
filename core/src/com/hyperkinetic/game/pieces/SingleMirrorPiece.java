package com.hyperkinetic.game.pieces;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

    @Override
    public void render(SpriteBatch sb) {
        // render single mirror piece here
        if(orientation == Directions.MirrorDirection.NORTHEAST) {
            // TODO: load appropriate asset
        } else if(orientation == Directions.MirrorDirection.NORTHWEST) {
            // TODO: load appropriate asset
        } else if(orientation == Directions.MirrorDirection.SOUTHEAST) {
            // TODO: load appropriate asset
        } else {
            // TODO: load appropriate asset
        }
    }
}
