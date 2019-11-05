package com.hyperkinetic.game.pieces;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DataInput;
import com.hyperkinetic.game.util.Directions;

public class LaserPiece extends AbstractBlockPiece {

    public LaserPiece(int x, int y, Directions.Direction orientation) {
        super(x,y);
        this.orientation = orientation;
    }

    @Override
    public Array<Directions.Direction> acceptLaser(Directions.Direction laserDirection){
        return null;
    }

    @Override
    public void render(SpriteBatch sb){
        // render LaserPiece here
        if(orientation == Directions.Direction.EAST) {
            // TODO: load appropriate asset
        } else if(orientation == Directions.Direction.WEST) {
            // TODO: load appropriate asset
        } else if(orientation == Directions.Direction.NORTH) {
            // TODO: load appropriate asset
        } else {
            // TODO: load appropriate asset
        }
    }
}
