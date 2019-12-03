package com.hyperkinetic.game.pieces;

import com.badlogic.gdx.utils.Array;
import com.hyperkinetic.game.core.LaserGame;
import com.hyperkinetic.game.util.Directions;
import com.hyperkinetic.game.util.Status;

import java.security.Guard;

public class GuardianPiece extends AbstractBlockPiece {

    public Status.PieceStatus status;
    public GuardianPiece(int x, int y, boolean c, Directions.Direction o) {
        super(x, y, c, o);
    }
    public GuardianPiece() {}

    // TODO: get rid of status
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

    @Override
    public void loadRegion()
    {
        textures[0] = LaserGame.loadTexture("pieces/guardianback" + (!color ? "Red.png" : ".png"));
        textures[1] = LaserGame.loadTexture("pieces/guardian" + (!color ? "Red1.png" : "1.png"));
        textures[2] = LaserGame.loadTexture("pieces/guardianfront" + (!color ? "Red.png" : ".png"));
        textures[3] = LaserGame.loadTexture("pieces/guardian" + (!color ? "Red2.png" : "2.png"));
    }
}
