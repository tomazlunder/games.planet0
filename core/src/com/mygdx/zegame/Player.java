package com.mygdx.zegame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Player {
    private Physics physics = Physics.getPhysics();
    final int PLAYER_HEIGHT = 20;
    final int PLAYER_WIDTH = 10;

    float playerCenterX, playerCenterY;

    float worldCenterXY, worldRadious;
    float rotationFromCenter, rotationStep;

    Vector2 leftMoveUnitVector, rightMoveUnitVector, fromCenterUnitVector;
    float acceleration, maxSpeed, deAcceleration;

    Sprite sprite;

    public Player(float worldSize, float worldRadious){
        this.worldCenterXY = worldSize/2;
        this.worldRadious = worldRadious;

        this.playerCenterX = worldCenterXY;
        this.playerCenterY = worldCenterXY + worldRadious + PLAYER_HEIGHT /2;
        this.rotationFromCenter = 90;
        this.rotationStep = 1f;

    }

    public float getPlayerCenterX(){return playerCenterX;}
    public float getPlayerCenterY(){return playerCenterY;}
    public float getPlayerRotationFromCenter(){return rotationFromCenter;}

    public void drawPlayerSimple(ShapeRenderer shapeRenderer){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(playerCenterX-PLAYER_WIDTH/2, playerCenterY-PLAYER_HEIGHT/2,
                    PLAYER_WIDTH/2, PLAYER_HEIGHT/2,
                    PLAYER_WIDTH, PLAYER_HEIGHT,
                    1,1,
                    rotationFromCenter-90);
        shapeRenderer.end();
    }

    public void calculateMoveUnitVectors(){
        Vector2 center = new Vector2(worldCenterXY,worldCenterXY);
        Vector2 player = new Vector2(playerCenterX, playerCenterY);

        //Getting the unit vector pointing from center to player
        //Subtract two points, normalize
        Vector2 calc = player.sub(center);
        calc.scl(distanceFromCenter());

        //We then rotate it by 90 and -90 degrees to get the unit vectors in the LEFT and RIGHT moving directions (relative to player head up).
        Vector2 left = calc.cpy();
        Vector2 right = calc.cpy();
        left.rotate(90);
        right.rotate(-90);
        this.leftMoveUnitVector = left;
        this.rightMoveUnitVector = right;
        this.fromCenterUnitVector = calc;

    }

    private float distanceFromCenter(){
        return worldRadious + this.PLAYER_HEIGHT/2;
    }


    public void moveByAngle(){
        this.rotationFromCenter = this.rotationFromCenter + this.rotationStep;
        if(rotationFromCenter >= 360) rotationFromCenter = 0 + (rotationFromCenter-360);
        if(rotationFromCenter < 0) rotationFromCenter = 360 - rotationFromCenter;

        playerCenterX = worldCenterXY + (float) Math.cos((double)rotationFromCenter * Math.PI / 180) * distanceFromCenter();
        playerCenterY = worldCenterXY + (float) Math.sin((double)rotationFromCenter * Math.PI / 180) * distanceFromCenter();
    }

    public void logPos(){
        System.out.println("Player pos: [" + this.playerCenterX + ", " + this.playerCenterY +"]");
        System.out.println("Player rotation: " + this.rotationFromCenter);
    }



}
