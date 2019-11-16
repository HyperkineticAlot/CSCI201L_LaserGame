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
    private AbstractBoardTile rightClickDown;

    public boolean keyDown (int keycode)
    {
        return false;
    }

    public boolean keyUp (int keycode)
    {
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
            rightClickDown = AbstractGameBoard.getTileFromLocation(x, y);
            return true;
        }
        return false;
    }

    public boolean touchUp (int x, int y, int pointer, int button)
    {
        if(button == Input.Buttons.RIGHT)
        {
            if(AbstractGameBoard.getTileFromLocation(x, y) == rightClickDown)
            {
                rightClickDown.onRightClick();
                return true;
            }

            return false;
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
