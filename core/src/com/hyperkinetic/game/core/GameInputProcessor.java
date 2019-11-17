package com.hyperkinetic.game.core;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.hyperkinetic.game.board.AbstractBoardTile;
import com.hyperkinetic.game.board.AbstractGameBoard;

/**
 * Receives and processes inputs.
 *
 * @author cqwillia
 */
public class GameInputProcessor implements InputProcessor
{
    // TODO: make sure you are on the game board screen
    private int rightClickX;
    private int rightClickY;
    private int leftClickX;
    private int leftClickY;

    public boolean keyDown (int keycode)
    {
        return false;
    }

    public boolean keyUp (int keycode)
    {
        if(keycode == Input.Keys.Q)
        {
            return AbstractGameBoard.keyPressed("Q");
        }
        else if(keycode == Input.Keys.E)
        {
            return AbstractGameBoard.keyPressed("E");
        }
        return false;
    }

    public boolean keyTyped (char character)
    {
        return false;
    }

    public boolean touchDown (int x, int y, int pointer, int button)
    {
        if(button == Input.Buttons.RIGHT)
        {
            rightClickX = x;
            rightClickY = y;
            return true;
        }
        else if(button == Input.Buttons.LEFT)
        {
            leftClickX = x;
            leftClickY = y;
        }
        return false;
    }

    public boolean touchUp (int x, int y, int pointer, int button)
    {
        if(button == Input.Buttons.RIGHT)
        {
            return AbstractGameBoard.rightClick(rightClickX, rightClickY, x, y);
        }
        if(button == Input.Buttons.LEFT)
        {
            return AbstractGameBoard.leftClick(leftClickX, leftClickY, x, y);
        }
        return false;
    }

    public boolean touchDragged (int x, int y, int pointer)
    {
        return false;
    }

    public boolean mouseMoved (int x, int y)
    {
        return false;
    }

    public boolean scrolled (int amount)
    {
        return false;
    }
}
