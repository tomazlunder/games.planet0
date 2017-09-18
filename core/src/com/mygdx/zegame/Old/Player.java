package com.mygdx.zegame.Old;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Player {
    final int PLAYER_HEIGHT = 20;
    final int PLAYER_WIDTH = 10;

    float playerCenterX, playerCenterY;

    float worldCenterXY, worldRadious;
    float rotationFromCenter, rotationStep;

    Vector2 leftMoveUnitVector, rightMoveUnitVector, fromCenterUnitVector;
    Vector2 center;
    Vector2 moveVector;
    float speedLeft, speedRight, speedUp, speedMax = 6, accelerationSpeed = 0.6f;
    float acceleration, deAcceleration;

    Sprite sprite;

    public Player(float worldSize, float worldRadious){
        this.worldCenterXY = worldSize/2;
        this.worldRadious = worldRadious;
        this.center = new Vector2(worldCenterXY, worldCenterXY);

        this.playerCenterX = worldCenterXY;
        this.playerCenterY = worldCenterXY + worldRadious + PLAYER_HEIGHT /2;

        this.rotationFromCenter = 90;
        this.speedLeft = 0;
        this.speedRight = 0;
        this.speedUp = 0;
    }

    //Simple getters
    public float getPlayerCenterX(){return playerCenterX;}
    public float getPlayerCenterY(){return playerCenterY;}
    public float getPlayerRotationFromCenter(){return rotationFromCenter;}

    private float distanceFromCenter(){
        return this.center.dst(new Vector2(this.playerCenterX, this.playerCenterY));
    }
    private float distanceFromCenterOld(){
        return worldRadious + this.PLAYER_HEIGHT/2;
    }


    //Draws the player based on his Center coordinates and rotation from center
    public void drawPlayerSimple(ShapeRenderer shapeRenderer){
        logPos();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(playerCenterX-PLAYER_WIDTH/2, playerCenterY-PLAYER_HEIGHT/2,
                    PLAYER_WIDTH/2, PLAYER_HEIGHT/2,
                    PLAYER_WIDTH, PLAYER_HEIGHT,
                    1,1,
                    rotationFromCenter-90);
        shapeRenderer.end();
    }

    public void increaseSpeedLeft(){
        if(speedLeft<speedMax && isOnGround()) speedLeft += accelerationSpeed;
    }
    public void increaseSpeedRight(){
        if(speedRight<speedMax && isOnGround()) speedRight += accelerationSpeed;
    }
    public void decreaseSpeed(){
        if(speedLeft>0) speedLeft-=1;
        if(speedRight>0) speedRight-=1;
    }
    public void jump(){
        if(isOnGround()){
            speedUp = 3;
        }
    }

    public boolean isOnGround(){
        return(distanceFromCenter() <= worldRadious+(PLAYER_HEIGHT/2)+0.5);
    }

    //Calculates unit vectors
    public void calculateMoveUnitVectors(){
        Vector2 center = new Vector2(worldCenterXY,worldCenterXY);
        Vector2 player = new Vector2(playerCenterX, playerCenterY);

        //Getting the unit vector pointing from center to player
        //Subtract two points, normalize
        Vector2 calc = player.sub(center);
        calc.scl(1/distanceFromCenter());

        //We then rotate it by 90 and -90 degrees to get the unit vectors in the LEFT and RIGHT moving directions (relative to player head up).
        Vector2 left = calc.cpy();
        Vector2 right = calc.cpy();
        left.rotate(90);
        right.rotate(-90);
        this.leftMoveUnitVector = left;
        this.rightMoveUnitVector = right;
        this.fromCenterUnitVector = calc;
    }

    public void calculateMoveVector(){
        if(speedUp > 0) speedUp-=0.1;
        Vector2 gravityVector = fromCenterUnitVector.cpy();
        gravityVector.rotate(180);
        gravityVector.scl(1);

        gravityVector.add(fromCenterUnitVector.cpy().scl(speedUp));
        gravityVector.add(leftMoveUnitVector.scl(speedLeft));
        gravityVector.add(rightMoveUnitVector.scl(speedRight));

        this.moveVector = gravityVector.cpy();
    }

    public void movePlayerByMoveVector(){
        if(moveVector.len() > speedMax) moveVector.setLength(speedMax);
        Vector2 currPos = new Vector2(this.playerCenterX, this.playerCenterY);
        currPos.add(this.moveVector);
        float distance = currPos.dst(this.center);
        float diff = (this.worldRadious + this.PLAYER_HEIGHT/2) - distance;
        if(diff > 0){
            currPos.add(this.fromCenterUnitVector.scl(Math.abs(diff)));
        }
        this.playerCenterX = currPos.x;
        this.playerCenterY = currPos.y;
        calculateRotationFromCenter();
        //System.out.println("Pos: " + currPos.toString() + " | Move vector: " + this.moveVector.toString() );

    }

    public void calculateRotationFromCenter(){
        double theta = Math.atan2(playerCenterY - worldCenterXY, playerCenterX - worldCenterXY);
        double angle = Math.toDegrees(theta);
        if(angle < 0 ){
            angle += 360;
        }
        this.rotationFromCenter = (float) angle;
    }


    public void movePlayerByRotationStep(){
        //Changes rotation by rotationStep
        this.rotationFromCenter = this.rotationFromCenter + this.rotationStep;
        //Handles if degrees go above 360 or under 0
        if(rotationFromCenter >= 360) rotationFromCenter = 0 + (rotationFromCenter-360);
        if(rotationFromCenter < 0) rotationFromCenter = 360 - rotationFromCenter;
        //Calculates player cooridanes with rotation from center and distance.
        playerCenterX = worldCenterXY + (float) Math.cos((double)rotationFromCenter * Math.PI / 180) * distanceFromCenter();
        playerCenterY = worldCenterXY + (float) Math.sin((double)rotationFromCenter * Math.PI / 180) * distanceFromCenter();
    }


    public void logPos(){
        System.out.println("Player pos: [" + this.playerCenterX + ", " + this.playerCenterY +"]");
        System.out.println("Player rotation: " + this.rotationFromCenter);
        System.out.println("Player move vector: " + this.moveVector);
    }



}
