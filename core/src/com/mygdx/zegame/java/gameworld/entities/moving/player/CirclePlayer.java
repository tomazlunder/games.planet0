package com.mygdx.zegame.java.gameworld.entities.moving.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.zegame.java.gameworld.planets.Planet;
import com.mygdx.zegame.java.gameworld.entities.MovingEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private float DEFAULT_HEALTH = 100f;
    private float DEFAULT_SHIELD = 0;

    private float DEFAULT_GRAVITY = 0.4f;

    private boolean inJump;

    private Texture texture;
    private Sprite sprite;
    private float radius;
    private float airtime;
    private float jumpTimeout;
    private int testTick;
    public float healthPoints, shieldPoints;

    private int currentFrame;
    private TextureAtlas ta;
    private TextureRegion trBody, trLegs, trFace;
    private Sprite spriteBody, spriteLegs, spriteFace;

    String LEG_DEFAULT = "legs_idle";
    String LEG_JUMP = "legs_jump";
    String FACE_IDLE = "face_idle";
    String FACE_LEFT = "face_left";
    String FACE_JUMP = "face_jump";

    List<String> LEG_RUN_ANIMATION = Arrays.asList("legs_run_left_0","legs_run_left_2","legs_run_left_4",
                                          "legs_run_left_6","legs_run_left_8","legs_run_left_10",
                                          "legs_run_left_12","legs_run_left_14", "legs_run_left_16",
                                          "legs_run_left_18");
    Animation<TextureRegion> runAnimation;
    float elapsedTime;

    private float movingDirection;


    float run_pointer = 0;

    public CirclePlayer(float radius, Planet planet, String path){
        super(planet.getX(),planet.getY()+planet.getRadius()+radius,radius,planet);
        this.center = new Vector2(planet.getX(), planet.getY()+planet.getRadius()+radius);
        this.radius = radius;
        this.maxSpeed = DEFAULT_MAX_SPEED;
        this.maxAcceleration = DEFAULT_MAX_ACC;
        this.accelerationStep = DEFAULT_ACC_STEP;
        this.jumpTimeout = DEFAULT_JUMP_TIMEOUT;
        this.inJump = false;

        this.healthPoints = DEFAULT_HEALTH;
        this.shieldPoints = DEFAULT_SHIELD;
        this.texture = new Texture(path);
        //sprite = new Sprite(texture,500,1000);
        testTick=0;

        ta = new TextureAtlas("spritesheets/player0.atlas");
        trFace = ta.findRegion(FACE_IDLE);
        spriteFace = new Sprite(trFace);

        trBody = ta.findRegion("body1");
        spriteBody = new Sprite(trBody);

        trLegs = ta.findRegion(LEG_DEFAULT);
        spriteLegs = new Sprite(trLegs);

        TextureRegion[] trs = new TextureRegion[LEG_RUN_ANIMATION.size()];
        for(int i = 0; i < LEG_RUN_ANIMATION.size(); i++){
            trs[i] = (ta.findRegion(LEG_RUN_ANIMATION.get(i)));
        }
        runAnimation = new Animation(1f/2.25f,trs);

        elapsedTime = 0;
    }

    public void takeDamage(float damage){
        if(this.shieldPoints > 0){
            shieldPoints-= damage;
            if(shieldPoints < 0){shieldPoints = 0;}
        } else {
            healthPoints-=damage;
        }
    }

    public void gainShield(){
        this.shieldPoints = 100;
    }

    public boolean isDead(){
        return healthPoints <= 0;
    }

    public void draw(SpriteBatch spriteBatch){
        /*
        testTick++;
        spriteBatch.begin();

        sprite.setPosition(center.x-sprite.getWidth()/2,center.y-sprite.getHeight()/2);
        sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
        sprite.setScale(radius/sprite.getWidth());
        sprite.setRotation(rotationFromCenter-90);

        sprite.draw(spriteBatch);
        spriteBatch.end();
        */
        testTick++;
        spriteBatch.begin();
        spriteBody.setPosition(center.x-spriteBody.getWidth()/2,center.y-spriteBody.getHeight()/2);
        spriteBody.setOrigin(spriteBody.getWidth()/2, spriteBody.getHeight()/2);
        spriteBody.setScale(radius*2/spriteBody.getWidth());
        spriteBody.setRotation(rotationFromCenter-90);
        spriteBody.draw(spriteBatch);

        //spriteBatch.draw(runAnimation.getKeyFrame(elapsedTime,true),0,0);

        if(trLegs.isFlipX()){
            trLegs.flip(true,false);
        }
        if(trFace.isFlipX()){
            trFace.flip(true,false);
        }

        if(this.movingDirection != 0){
            trLegs = ta.findRegion(LEG_RUN_ANIMATION.get(currentFrame%LEG_RUN_ANIMATION.size()));
            trFace = ta.findRegion(FACE_LEFT);
            if(this.movingDirection == 1){
                trLegs.flip(true,false);
                trFace.flip(true, false);
            }
        } else {
            trLegs = ta.findRegion(LEG_DEFAULT);
            trFace = ta.findRegion(FACE_IDLE);
        }
        if(this.inJump){
            trLegs = ta.findRegion(LEG_JUMP);
            trFace = ta.findRegion(FACE_JUMP);
        }

        spriteLegs.setRegion(trLegs);
        spriteLegs.setPosition(center.x-spriteLegs.getWidth()/2,center.y-spriteLegs.getHeight()/2);
        spriteLegs.setOrigin(spriteLegs.getWidth()/2, spriteLegs.getHeight()/2);
        spriteLegs.setScale(radius*2/spriteLegs.getWidth());
        spriteLegs.setRotation(rotationFromCenter-90);

        spriteLegs.draw(spriteBatch);

        spriteFace.setRegion(trFace);
        spriteFace.setPosition(center.x-spriteFace.getWidth()/2,center.y-spriteFace.getHeight()/2);
        spriteFace.setOrigin(spriteFace.getWidth()/2, spriteFace.getHeight()/2);
        spriteFace.setScale(radius*2/spriteFace.getWidth());
        spriteFace.setRotation(rotationFromCenter-90);

        spriteFace.draw(spriteBatch);


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
        elapsedTime+= deltaTime;
        //SETTING VARIABLES FOR ANIMATION
        currentFrame ++;
        if(leftPressed && movingDirection != -1){
            currentFrame = 0;
            movingDirection = -1;
        } else if (rightPressed && movingDirection != 1){
            currentFrame = 0;
            movingDirection = 1;
        } else if (!leftPressed && !rightPressed){
            movingDirection = 0;
            currentFrame = 0;
        }
        //end animation

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
