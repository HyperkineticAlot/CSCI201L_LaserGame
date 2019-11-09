package com.hyperkinetic.game.pieces;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.hyperkinetic.game.util.Directions;

public class KingPiece extends AbstractBlockPiece {
    public KingPiece(int x, int y) {
        super(x, y);
    }

    @Override
    public Array<Directions.Direction> acceptLaser(Directions.Direction laserDirection) {
        return null;
    }

    @Override
    public void render(SpriteBatch sb) {

    }
}
