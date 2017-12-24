package com.mygdx.zegame.java.playercontrollers;

import com.badlogic.gdx.graphics.OrthographicCamera;

public interface PlayerController {
    public void handlePlayerInputs(OrthographicCamera camera);
    public void updatePlayer(float deltaTime);
}
