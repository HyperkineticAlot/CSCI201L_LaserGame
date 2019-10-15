package com.hyperkinetic.game.board;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.hyperkinetic.game.pieces.AbstractGamePiece;

public abstract class AbstractGameBoard
{
    protected int x;
    protected int y;
    private int screenX;
    private int screenY;
    private int tileDim;
    protected Array<AbstractBoardTile> tiles;
    protected Array<AbstractGamePiece> pieces;

    public AbstractGameBoard(int x, int y)
    {
        tiles = new Array<>();
        pieces = new Array<>();

        int xSpace = (int)(Gdx.graphics.getWidth() * .60);
        int ySpace = (int)(Gdx.graphics.getHeight() * .80);
        this.x = x;
        this.y = y;

        double boardRatio = ((double) x / y);
        double screenRatio = ((double) Gdx.graphics.getWidth() / Gdx.graphics.getHeight());

        if(boardRatio > screenRatio)
        {
            tileDim = xSpace / x;
            screenX = (int)(Gdx.graphics.getWidth() * .20);
            screenY = (Gdx.graphics.getHeight() - y * tileDim) / 2;
        }
        else
        {
            tileDim = ySpace / y;
            screenY = (int)(Gdx.graphics.getHeight() * .10);
            screenX = (Gdx.graphics.getWidth() - x * tileDim) / 2;
        }
    }

    public abstract void create();

    public void render(SpriteBatch sb)
    {
        // starting from the bottom left
        for(int i = 0; i < y; i++)
        {
            for(int j = 0; j < x; j++)
            {
                tiles.get(j + i * x).render(sb, screenX + j * tileDim, screenY + i * tileDim, tileDim, tileDim);
            }
        }
    }
}
