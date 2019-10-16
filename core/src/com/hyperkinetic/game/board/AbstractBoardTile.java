package com.hyperkinetic.game.board;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hyperkinetic.game.core.LaserGame;
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
    private AbstractGamePiece piece;

    public AbstractBoardTile()
    {
        loadRegion("board/center.png");
        piece = null;
    }
    public AbstractBoardTile(AbstractBoardTile.TileType type)
    {
        loadRegion(type);
        piece = null;
    }
    public AbstractBoardTile(String basePath, AbstractBoardTile.TileType type)
    {
        loadRegion(basePath, type);
        piece = null;
    }

    public void onPiecePlaced(AbstractGamePiece piece) {}
    public void onPieceRotated(AbstractGamePiece piece) {}
    public void onLeftClick() {}
    public void onRightClick()
    {
        if(piece != null)
        {
            piece.rotate();
        }
    }

    public void render(SpriteBatch sb, int x, int y, int width, int height)
    {
        sb.draw(texture, x, y, width, height);
    }

    protected void loadRegion(String image)
    {
        texture = LaserGame.loadTexture(image);
    }

    private void loadRegion(AbstractBoardTile.TileType type)
    {
        loadRegion(getPathFromTileType(type));
    }

    private void loadRegion(String basePath, AbstractBoardTile.TileType type)
    {
        loadRegion(basePath + getPathFromTileType(type));
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
