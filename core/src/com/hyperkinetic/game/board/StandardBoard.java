package com.hyperkinetic.game.board;

/**
 * Defines the standard 8x8 laser game board with laser tiles in the upper left
 * and lower right corners.
 *
 * @author cqwillia
 */
public class StandardBoard extends AbstractGameBoard
{
    public StandardBoard()
    {
        super(8, 8);
        create();
    }

    @Override
    public void create()
    {
        for(int i = 0; i < y; i++)
        {
            for(int j = 0; j < x; j++)
            {
                if(j == 0)
                {
                    if(i == 0)
                        tiles.add(new StandardTile(AbstractBoardTile.TileType.LL_CORNER));
                    else if(i == y-1)
                        tiles.add(new LaserTile());
                    else
                        tiles.add(new StandardTile(AbstractBoardTile.TileType.LEFT_EDGE));
                }
                else if(i == 0)
                {
                    if(j == x-1)
                        tiles.add(new LaserTile());
                    else
                        tiles.add(new StandardTile(AbstractBoardTile.TileType.LOWER_EDGE));
                }
                else if(j == x-1)
                {
                    if(i == y-1)
                        tiles.add(new StandardTile(AbstractBoardTile.TileType.UR_CORNER));
                    else
                        tiles.add(new StandardTile(AbstractBoardTile.TileType.RIGHT_EDGE));
                }
                else if(i == y-1)
                {
                    tiles.add(new StandardTile(AbstractBoardTile.TileType.UPPER_EDGE));
                }
                else
                {
                    tiles.add(new StandardTile(AbstractBoardTile.TileType.CENTER));
                }
            }
        }

        tiles.add(new LaserTile());
        for(int j = 1; j < x; j++)
        {
            tiles.add(new BlankTile());
        }
    }
}
