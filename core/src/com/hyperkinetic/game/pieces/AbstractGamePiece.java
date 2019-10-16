package com.hyperkinetic.game.pieces;

import com.badlogic.gdx.utils.Array;
import com.hyperkinetic.game.entities.LaserEntity;
import com.hyperkinetic.game.util.Directions;

public abstract class AbstractGamePiece
{
    protected Directions.MirrorDirection orientation;

    public void rotate()
    {
        if(orientation == null) return;

        if(orientation == Directions.MirrorDirection.NORTHWEST)
            orientation = Directions.MirrorDirection.NORTHEAST;
        else if(orientation == Directions.MirrorDirection.NORTHEAST)
            orientation = Directions.MirrorDirection.SOUTHEAST;
        else if(orientation == Directions.MirrorDirection.SOUTHEAST)
            orientation = Directions.MirrorDirection.SOUTHWEST;
        else
            orientation = Directions.MirrorDirection.NORTHWEST;
    }
    public abstract Array<Directions.Direction> acceptLaser(Directions.Direction laserDirection);
}
