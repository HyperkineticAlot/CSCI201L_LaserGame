package com.hyperkinetic.game.board;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hyperkinetic.game.LaserGame;
import com.hyperkinetic.game.pieces.AbstractGamePiece;

public abstract class AbstractBoardTile
{
    public enum TileType
    {
        UL_CORNER,
        UPPER_EDGE,
        UR_CORNER,
        LEFT_EDGE,
        CENTER,
        RIGHT_EDGE,
        LL_CORNER,
        LOWER_EDGE,
        LR_CORNER
    }

    private Texture texture;

    public AbstractBoardTile()
    {
        loadRegion("center.png");
    }
    public AbstractBoardTile(AbstractBoardTile.TileType type)
    {
        loadRegion(type);
    }
    public AbstractBoardTile(String basePath, AbstractBoardTile.TileType type)
    {
        loadRegion(basePath, type);
    }

    public void onPiecePlaced(AbstractGamePiece piece) {}
    public void onPieceRotated(AbstractGamePiece piece) {}

    public void render(SpriteBatch sb, int x, int y, int width, int height)
    {
        sb.draw(texture, x, y, width, height);
    }

    private void loadRegion(String image)
    {
        texture = LaserGame.loadTexture(image);
    }

    private void loadRegion(AbstractBoardTile.TileType type)
    {
        texture = LaserGame.loadTexture(getPathFromTileType(type));
    }

    private void loadRegion(String basePath, AbstractBoardTile.TileType type)
    {
        texture = LaserGame.loadTexture(basePath + getPathFromTileType(type));
    }

    private String getPathFromTileType(AbstractBoardTile.TileType type)
    {
        switch(type)
        {
            case UL_CORNER:
                return "ulcorner.png";
            case UPPER_EDGE:
                return "upperedge.png";
            case UR_CORNER:
                return "urcorner.png";
            case LEFT_EDGE:
                return "leftedge.png";
            case CENTER:
                return "center.png";
            case RIGHT_EDGE:
                return "rightedge.png";
            case LL_CORNER:
                return "llcorner.png";
            case LOWER_EDGE:
                return "loweredge.png";
            case LR_CORNER:
                return "lrcorner.png";
            default:
                return "center.png";
        }
    }
}
