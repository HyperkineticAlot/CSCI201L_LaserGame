package com.hyperkinetic.game.pieces;

import com.badlogic.gdx.utils.Array;
import com.hyperkinetic.game.util.Directions;
import com.hyperkinetic.game.util.Status;

public class GuardianPiece extends AbstractBlockPiece {

    public Status.PieceStatus status;
    public GuardianPiece(int x, int y) {
        super(x, y);
    }

    @Override
    public Array<Directions.Direction> acceptLaser(Directions.Direction laserDirection) {
        if (this.orientation ==  Directions.Direction.EAST){
            if(laserDirection != Directions.Direction.WEST) // will be destroyed
                this.status = Status.PieceStatus.DEAD;
            else
                this.status = Status.PieceStatus.ALIVE;
        }

        else if (this.orientation ==  Directions.Direction.WEST){
            if(laserDirection != Directions.Direction.EAST) // will be destroyed
                this.status = Status.PieceStatus.DEAD;
            else
                this.status = Status.PieceStatus.ALIVE;
        }

        else if (this.orientation ==  Directions.Direction.NORTH){
            if(laserDirection != Directions.Direction.SOUTH) // will be destroyed
                this.status = Status.PieceStatus.DEAD;
            else
                this.status = Status.PieceStatus.ALIVE;
        }

        else if (this.orientation ==  Directions.Direction.SOUTH){
            if(laserDirection != Directions.Direction.NORTH) // will be destroyed
                this.status = Status.PieceStatus.DEAD;
            else
                this.status = Status.PieceStatus.ALIVE;
        }

        return null; // laser not reflected
    }
}
