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
    public SingleMirrorPiece(int x, int y, boolean c)
    {
        super(x, y, c);
        this.orientation = Directions.MirrorDirection.NORTHWEST;
    }

    public SingleMirrorPiece(int x, int y, boolean c, Directions.MirrorDirection o)
    {
        super(x, y, c);
        this.orientation = o;
    }

    @Override
    public Array<Directions.Direction> acceptLaser(Directions.Direction laserDirection)
    {
        Directions.Direction dir = Directions.reflect(this.orientation, laserDirection);
        if(dir == null) return null;

        Array<Directions.Direction> reflectedLaserDirection = new Array<>();
        reflectedLaserDirection.add(dir);
        return reflectedLaserDirection;
    }

    @Override
    public void render(SpriteBatch sb) {

    }
}
