package com.mygdx.zegame.java.playercontrollers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.zegame.java.objects.moving.player.CirclePlayer;

public class CirclePlayerController implements PlayerController {
    private float accMax, acc, deAcc;
    private float speedLeft, speedRight;

    boolean leftPressed, rightPressed;
    boolean leftPressedLast, rightPressedLast;
    boolean jump;

    CirclePlayer circlePlayer;


    public CirclePlayerController(CirclePlayer cp) {
        this.speedLeft = 0;
        this.speedRight = 0;
        this.leftPressed = false;
        this.rightPressed = false;
        this.leftPressedLast = false;
        this.rightPressedLast = false;
        this.circlePlayer = cp;

        this.accMax = 10;
        this.acc = 1f;
        this.deAcc = 1f;

    }

    @Override
    public void handlePlayerInputs(float deltaTime) {
        this.jump = false;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            if (speedLeft < accMax) speedLeft += acc * deltaTime * 60;

        } else if (speedLeft > 0) {
            if(speedLeft -deAcc>=0) {
                speedLeft -= deAcc * deltaTime * 60;
            } else {
                speedLeft = 0;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            if (speedRight < accMax) speedRight += acc * deltaTime * 60;
            rightPressedLast = true;
        } else if (speedRight > 0) {
            if(speedRight -deAcc>=0) {
                speedRight -= deAcc * deltaTime * 60;
            } else {
                speedRight = 0;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            this.jump = true;
        }
    }

    @Override
    public void updatePlayer(){
        circlePlayer.calcDistanceFromCenter();
        circlePlayer.calcGravityForce();
        circlePlayer.calcUnitVectors();
        circlePlayer.calcMoveVector(speedLeft, speedRight, jump);
        circlePlayer.movePlayerByMoveVector();
    }

    public String toString(){
        return "[CPController] L["+this.speedLeft +"] R["+this.speedRight +"] U["+this.speedRight +"] D[";
    }

}

