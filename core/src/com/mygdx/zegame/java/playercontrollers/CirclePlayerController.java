package com.mygdx.zegame.java.playercontrollers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.zegame.java.gameworld.entities.moving.player.CirclePlayer;

public class CirclePlayerController implements PlayerController {
    private boolean leftPressed, rightPressed;
    private boolean jump;

    CirclePlayer circlePlayer;


    public CirclePlayerController(CirclePlayer cp) {
        this.leftPressed = false;
        this.rightPressed = false;

        this.circlePlayer = cp;
    }

    @Override
    public void handlePlayerInputs(OrthographicCamera camera) {
        this.jump = false;
        this.leftPressed = false;
        this.rightPressed = false;

        if (Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D)){
            leftPressed = true;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D) && !Gdx.input.isKeyPressed(Input.Keys.A)) {
            rightPressed = true;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            this.jump = true;
        }

    }

    @Override
    public void updatePlayer(float deltaTime){
        circlePlayer.calculateRotationFromCenter();
        circlePlayer.calcGravityForce();
        circlePlayer.calcUnitVectors();
        circlePlayer.updatePosition(leftPressed, rightPressed, jump, deltaTime);
    }


}

