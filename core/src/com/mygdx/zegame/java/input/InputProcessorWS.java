package com.mygdx.zegame.java.input;

import com.badlogic.gdx.InputProcessor;
import com.mygdx.zegame.java.gameworld.entities.moving.player.CirclePlayer;

public class InputProcessorWS implements InputProcessor {

    CirclePlayer cp;

    public InputProcessorWS(CirclePlayer cp){
        this.cp = cp;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        if(amount > 0){
            cp.nextWeapon();
        }
        else{
            cp.prevWeapon();
        }
        return false;
    }
}
