package com.hyperkinetic.game.pieces;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.hyperkinetic.game.util.Directions;

// can only rotate
public class LaserPiece extends AbstractBlockPiece{
    public LaserPiece(int x, int y, boolean c) {
        super(x, y, c);
    }

    @Override
    public Array<Directions.Direction> acceptLaser(Directions.Direction laserDirection) {
        return null;
    }

    @Override
    public void render(SpriteBatch sb) {

    }

}
