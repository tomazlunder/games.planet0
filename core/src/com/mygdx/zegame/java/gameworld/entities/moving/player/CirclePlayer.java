package com.mygdx.zegame.java.gameworld.entities.moving.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.zegame.java.gameworld.planets.Planet;
import com.mygdx.zegame.java.gameworld.entities.MovingEntity;

public class CirclePlayer extends MovingEntity {
    private float GROUNDED_ERROR = 0.05f;
    private float DEFAULT_MAX_HORIZONTAL_SPEED = 6f;
    private float DEFAULT_MAX_SPEED = 15;
    private float DEFAULT_JUMP_ACC = 2.5f;
    private float DEFAULT_MAX_ACC = 2;
    private float DEFAULT_JUMP_TIMEOUT = 0.5f;
    private float DEFAULT_ACC_STEP = DEFAULT_MAX_ACC/10;
    private float DEFAULT_DEACC_STEP = DEFAULT_MAX_SPEED/2;
    private float SPEED_FACTOR = 60;

    private float DEFAULT_GRAVITY = 0.4f;

    private boolean inJump;

    private Texture texture;
    private Sprite sprite;
    private float radius;
    private float airtime;
    private float jumpTimeout;
    private int testTick;


    public CirclePlayer(float radius, Planet planet, String path){
        super(planet.getX(),planet.getY()+planet.getRadius()+radius,radius,planet);
        this.center = new Vector2(planet.getX(), planet.getY()+planet.getRadius()+radius);
        this.radius = radius;
        this.maxSpeed = DEFAULT_MAX_SPEED;
        this.maxAcceleration = DEFAULT_MAX_ACC;
        this.accelerationStep = DEFAULT_ACC_STEP;
        this.jumpTimeout = DEFAULT_JUMP_TIMEOUT;
        this.inJump = false;

        this.texture = new Texture(path);
        sprite = new Sprite(texture,500,1000);
        testTick=0;
    }

    public void draw(SpriteBatch spriteBatch){
        testTick++;
        spriteBatch.begin();

        sprite.setPosition(center.x-sprite.getWidth()/2,center.y-sprite.getHeight()/2);
        sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
        sprite.setScale(radius/sprite.getWidth());
        sprite.setRotation(rotationFromCenter-90);

        sprite.draw(spriteBatch);
        spriteBatch.end();
    }

    public void draw(ShapeRenderer shapeRenderer){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.circle(center.x,center.y,radius,50);

        shapeRenderer.end();
    }

    public boolean isGrounded(){
        return (heigthFromGround() < GROUNDED_ERROR);
    }

    public float heigthFromGround(){
        return (distanceFromCenter()-(nearestPlanet.getRadius()+radius));
    }

    public void updatePosition(boolean leftPressed, boolean rightPressed, boolean jumped, float deltaTime){
        //leftPressed = true;

        if(jumpTimeout > 0){ jumpTimeout-= deltaTime; }
        if(isGrounded()){airtime=0;}


        //If the player is on the ground and has negative acceleration, his acceleration becomes 0
        if(isGrounded() && !inJump){
            this.acceleration.y = 0;
            this.speed.y = 0;
        }

        //Player landed from jump
        if(inJump && isGrounded()){
            inJump = false;
        }

        //PLAYER JUMPS
        if(jumped && isGrounded() &&!inJump && jumpTimeout <= 0) {
            jumpTimeout = DEFAULT_JUMP_TIMEOUT;
            airtime = 0;
            inJump = true;
            this.acceleration.y = DEFAULT_JUMP_ACC;
        }
        else if(inJump) {
            airtime += deltaTime;
        }

        acceleration.y -= DEFAULT_GRAVITY;

        //HORIZONTAL [LEFT/RIGHT] MOVEMENT
        if(!inJump) {
            //if   On ground and pressing left
            //else On ground not pressing left
            if (leftPressed && !rightPressed) {
                if (this.acceleration.x > -maxAcceleration) {
                    this.acceleration.x -= accelerationStep;
                }
            }

            //if On ground and pressing right
            //else On ground not pressing right
            if (rightPressed && !leftPressed) {
                if (this.acceleration.x < maxAcceleration) {
                    this.acceleration.x += accelerationStep;
                }
            }

            if(!rightPressed && !leftPressed){
                this.acceleration.x = 0;
                this.speed.x/=3;
            }
        }

        this.speed.add(acceleration);
        if(Math.abs(speed.x) > maxSpeed){
            speed.setLength(maxSpeed);
        }

        //Converting to universe coordinates
        Vector2 verticalSpeed, horizontalSpeed, trueSpeed;
        verticalSpeed = upUnit.cpy().scl(speed.y);
        horizontalSpeed = rightUnit.cpy().scl(speed.x);

        if(horizontalSpeed.len() > DEFAULT_MAX_HORIZONTAL_SPEED){
            horizontalSpeed.setLength(DEFAULT_MAX_HORIZONTAL_SPEED);
        }

        //Scaling with time
        trueSpeed = verticalSpeed.add(horizontalSpeed).scl(deltaTime*SPEED_FACTOR);

        this.newPosition = center.cpy();
        this.newPosition.add(trueSpeed);
        this.center = newPosition;
        correctForPlanet();
        this.baseCollision.updatePosition(center);
    }



    private void correctForPlanet(){
        float distance = distanceFromCenter();
        float diff = (nearestPlanet.getRadius() + this.radius) - distance;
        if(diff > 0){
            center.add(this.upUnit.scl(Math.abs(diff)));
        }
        calculateRotationFromCenter();

        if(isGrounded()){airtime=0;}
    }


    public String toString(){
        return "[CirclePlayer] POS["+this.center.toString()+"] SPEED "+this.speed.len()+" ["+this.speed.toString()+"] ACC "+this.acceleration.len()+" ["+ this.acceleration.toString() +"]";

    }

}
