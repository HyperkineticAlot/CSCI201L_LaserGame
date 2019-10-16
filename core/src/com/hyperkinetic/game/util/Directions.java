package com.hyperkinetic.game.util;

public class Directions
{
    public enum Direction
    {
        NORTH,
        WEST,
        SOUTH,
        EAST
    }

    public enum MirrorDirection
    {
        NORTHWEST,
        NORTHEAST,
        SOUTHEAST,
        SOUTHWEST
    }

    /*public static boolean singleMirrorReflected(MirrorDirection mirror, Direction laser)
    {
        if(mirror == MirrorDirection.NORTHWEST)
            return laser == Direction.SOUTH || laser == Direction.EAST;
        if(mirror == MirrorDirection.NORTHEAST)
            return laser == Direction.SOUTH || laser == Direction.WEST;
        if(mirror == MirrorDirection.SOUTHWEST)
            return laser == Direction.NORTH || laser == Direction.EAST;
        if(mirror == MirrorDirection.SOUTHEAST)
            return laser == Direction.NORTH || laser == Direction.WEST;

        return false;
    }*/

    public static Direction reflect(MirrorDirection mirror, Direction laser)
    {
        if(mirror == null || laser == null) return null;

        if(mirror == MirrorDirection.NORTHWEST)
        {
            if(laser == Direction.SOUTH) return Direction.WEST;
            if(laser == Direction.EAST) return Direction.NORTH;
        }
        else if(mirror == MirrorDirection.NORTHEAST)
        {
            if(laser == Direction.SOUTH) return Direction.WEST;
            if(laser == Direction.WEST) return Direction.NORTH;
        }
        else if(mirror == MirrorDirection.SOUTHEAST)
        {
            if(laser == Direction.NORTH) return Direction.EAST;
            if(laser == Direction.WEST) return Direction.SOUTH;
        }
        else
        {
            if(laser == Direction.NORTH) return Direction.WEST;
            if(laser == Direction.EAST) return Direction.SOUTH;
        }

        return null;
    }
}
