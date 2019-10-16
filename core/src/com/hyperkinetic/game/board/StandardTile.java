package com.hyperkinetic.game.board;

/**
 * The normal blank tile with no added effects. There are 62 of these in the standard
 * laser game configuration.
 *
 * @author cqwillia
 */
public class StandardTile extends AbstractBoardTile
{
    public StandardTile(AbstractBoardTile.TileType type)
    {
        super("board/", type);
    }
}
