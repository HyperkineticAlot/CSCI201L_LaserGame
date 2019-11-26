package com.hyperkinetic.game.pieces;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.hyperkinetic.game.util.Directions;

// can only rotate
public class LaserPiece extends AbstractBlockPiece{
    public LaserPiece(int x, int y, boolean c, Directions.Direction o) {
        super(x, y, c, o);
    }
    public LaserPiece() {}

    @Override
    public Array<Directions.Direction> acceptLaser(Directions.Direction laserDirection) {
        Array<Directions.Direction> retval = new Array<>();
        retval.add(laserDirection);
        return retval;
    }

    @Override
    public boolean equals(Object other)
    {
        if(!(other instanceof LaserPiece)) return false;
        LaserPiece o = (LaserPiece) other;
        return o.color == this.color && o.x == this.x && o.y == this.y;
    }
}
