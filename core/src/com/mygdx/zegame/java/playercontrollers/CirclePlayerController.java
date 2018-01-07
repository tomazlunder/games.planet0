package com.mygdx.zegame.java.playercontrollers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.zegame.java.gameworld.entities.moving.player.CirclePlayer;
import com.mygdx.zegame.java.sound.SoundSingleton;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class CirclePlayerController implements PlayerController {

    CirclePlayer circlePlayer;



    public CirclePlayerController(CirclePlayer cp) {

        this.circlePlayer = cp;
    }

    @Override
    public void handlePlayerInputs(OrthographicCamera camera) {
        //HANDLE PLAYER INPUTS MUST BE CALLED BEFORE UPDATING THE PLAYER, SO THESE SETTINGS ARE USED (movingLeft, movingRight, jumping)
        if (Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D)){
            circlePlayer.movingLeft = true;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D) && !Gdx.input.isKeyPressed(Input.Keys.A)) {
            circlePlayer.movingRight = true;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            circlePlayer.jumping = true;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)){
            circlePlayer.reload();
        }

        if (Gdx.input.justTouched()){
            circlePlayer.fireWeapon();
        }
    }

    @Override
    public void updatePlayer(float deltaTime){
        circlePlayer.calculateRotationFromCenter();
        circlePlayer.calcGravityForce();
        circlePlayer.calcUnitVectors();
    }


}

