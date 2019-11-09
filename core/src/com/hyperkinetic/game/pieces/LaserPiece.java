package com.hyperkinetic.game.pieces;

import com.badlogic.gdx.utils.Array;
import com.hyperkinetic.game.util.Directions;

// Sphinx can only rotate
public class LaserPiece extends AbstractBlockPiece{
    public LaserPiece(int x, int y) {
        super(x, y);
    }

    @Override
    public Array<Directions.Direction> acceptLaser(Directions.Direction laserDirection) {
        return null;
    }

}
