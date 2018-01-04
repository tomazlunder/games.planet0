package com.mygdx.zegame.java.gameworld.entities.moving.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.zegame.java.commons.Commons;
import com.mygdx.zegame.java.gameworld.planets.Planet;
import com.mygdx.zegame.java.gameworld.entities.MovingEntity;
import com.mygdx.zegame.java.sound.SoundSingleton;
import com.mygdx.zegame.java.weapons.StartGun;
import com.mygdx.zegame.java.weapons.Weapon;

import java.util.Arrays;
import java.util.List;

public class CirclePlayer extends MovingEntity {
    //Player physics
    private float DEFAULT_MAX_HORIZONTAL_SPEED = 6f;
    private float DEFAULT_MAX_SPEED = 15;
    private float DEFAULT_JUMP_ACC = 2.7f;
    private float DEFAULT_MAX_ACC = 2;
    private float DEFAULT_JUMP_TIMEOUT = 0.5f;
    private float DEFAULT_ACC_STEP = DEFAULT_MAX_ACC/10;
    private float DEFAULT_DEACC_STEP = DEFAULT_MAX_SPEED/2;
    private float SPEED_FACTOR = 60;
    private float DEFAULT_GRAVITY = 0.38f;

    public Vector3 aimingAt;

    private boolean inJump;
    private float jumpTimeout;
    private float airtime;

    //Player health
    private float DEFAULT_HEALTH = 100f;
    private float DEFAULT_SHIELD = 0;
    public float healthPoints, shieldPoints;

    //Player animation
    private OrthographicCamera camera;

    private int currentFrame;
    private TextureAtlas ta;
    private TextureRegion trBody, trLegs, trFace;
    private Sprite spriteBody, spriteLegs, spriteFace;
    private Sprite spriteGun;

    String LEG_DEFAULT = "legs_idle0";
    String BODY_DEFAULT = "body0";
    String LEG_JUMP = "legs_jump";
    String FACE_IDLE = "face_idle";
    String FACE_LEFT = "face_left";
    String FACE_JUMP = "face_jump";

    Animation<TextureRegion> bodyAnimation;
    Animation<TextureRegion> runAnimation;

    private float elapsedTime;
    private float movingDirection;

    List<String> BODY_ANIMATION = Arrays.asList("body0", "body1", "body2");
    List<String> LEG_RUN_ANIMATION = Arrays.asList("legs_run_left0","legs_run_left2","legs_run_left4",
            "legs_run_left6","legs_run_left8","legs_run_left10",
            "legs_run_left12","legs_run_left14", "legs_run_left16",
            "legs_run_left18");

    Sprite handIdleSprite, handWalkingSprite;

    //Weapon and inventory stuff
    public Weapon[] weapons;
    public int selectedWeapon;



    //Sound
    SoundSingleton sound;


    public CirclePlayer(float radius, Planet planet, OrthographicCamera camera){
        super(planet.getX(),planet.getY()+planet.getRadius()+radius,radius,planet);
        this.center = new Vector2(planet.getX(), planet.getY()+planet.getRadius()+radius);
        this.radius = radius;
        this.maxSpeed = DEFAULT_MAX_SPEED;
        this.maxAcceleration = DEFAULT_MAX_ACC;
        this.accelerationStep = DEFAULT_ACC_STEP;
        this.jumpTimeout = DEFAULT_JUMP_TIMEOUT;
        this.airtime = 0;
        this.inJump = false;

        this.healthPoints = DEFAULT_HEALTH;
        this.shieldPoints = DEFAULT_SHIELD;

        ta = new TextureAtlas("spritesheets/ss_player1.atlas");
        trFace = ta.findRegion(FACE_IDLE);
        spriteFace = new Sprite(trFace);

        trBody = ta.findRegion(BODY_DEFAULT);
        spriteBody = new Sprite(trBody);

        trLegs = ta.findRegion(LEG_DEFAULT);
        spriteLegs = new Sprite(trLegs);

        TextureRegion[] trs = new TextureRegion[LEG_RUN_ANIMATION.size()];
        for(int i = 0; i < LEG_RUN_ANIMATION.size(); i++){
            trs[i] = (ta.findRegion(LEG_RUN_ANIMATION.get(i)));
        }
        runAnimation = new Animation(1f/4.5f,trs);

        trs = new TextureRegion[BODY_ANIMATION.size()];
        for(int i = 0; i < BODY_ANIMATION.size(); i++){
            trs[i] = (ta.findRegion(BODY_ANIMATION.get(i)));
        }
        bodyAnimation = new Animation<TextureRegion>(1f/1f, trs);

        spriteGun = new Sprite(new Texture("sprites/weapons/gun.png"),256,256);


        //WEAPONS YEAH
        this.weapons = new Weapon[6];
        this.selectedWeapon = 0;

        this.weapons[0] = new StartGun();


        elapsedTime = 0;

        sound = SoundSingleton.getInstance();
    }

    //HEALTH RELATED
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

    //DRAW WITH SPRITES
    public void draw(SpriteBatch spriteBatch){
        spriteBatch.begin();
        drawAiming(spriteBatch);

        if(movingDirection == -1){
            drawSelectedWeapon(spriteBatch);
            drawBody(spriteBatch);
            drawLegsAndFace(spriteBatch);
        } else {
            drawBody(spriteBatch);
            drawLegsAndFace(spriteBatch);
            drawSelectedWeapon(spriteBatch);
        }

        spriteBatch.end();
    }


    public void drawSelectedWeapon(SpriteBatch spriteBatch){
        if(weapons[selectedWeapon] != null){
            Vector2 weaponPosition = getSelectedWeaponPos();
            Sprite weaponSprite = weapons[selectedWeapon].sprite;

            weaponSprite.setPosition(weaponPosition.x, weaponPosition.y);
            weaponSprite.setOrigin(weaponSprite.getWidth()/2, weaponSprite.getHeight()/2);
            weaponSprite.setScale(radius*2/weaponSprite.getWidth());
            weaponSprite.setRotation(0);

            if(aimingAt != null) {
                weaponSprite.rotate(Commons.angleBetweenPoints(newPosition, Commons.vec3to2(aimingAt)));
            }

            weaponSprite.draw(spriteBatch);
        }
        //TODO: ELSE DRAW HANDS (no weapon) or something

    }


    private void drawAiming(SpriteBatch spriteBatch){
        if(aimingAt != null) {
            //Commons.drawLine(spriteBatch, getArmCenterPosition(), Commons.vec3to2(aimingAt));
        }
    }

    public void drawLegsAndFace(SpriteBatch spriteBatch){
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
    }

    public void drawBody(SpriteBatch spriteBatch){
        //trBody = ta.findRegion(BODY_ANIMATION.get(currentFrame%BODY_ANIMATION.size()));
        trBody = ta.findRegion("body1");

        spriteBody.setRegion(trBody);
        spriteBody.setPosition(center.x-spriteBody.getWidth()/2,center.y-spriteBody.getHeight()/2);
        spriteBody.setOrigin(spriteBody.getWidth()/2, spriteBody.getHeight()/2);
        spriteBody.setScale(radius*2/spriteBody.getWidth());
        spriteBody.setRotation(rotationFromCenter-90);
        spriteBody.draw(spriteBatch);

    }

    //DRAW SIMPLE
    public void draw(ShapeRenderer shapeRenderer){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.circle(center.x,center.y,radius,50);

        shapeRenderer.end();
    }

    //UPDATE
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
            sound.jump.play();
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

    //UTILITY
    private void correctForPlanet(){
        float distance = distanceFromCenter();
        float diff = (nearestPlanet.getRadius() + this.radius) - distance;
        if(diff > 0){
            center.add(this.upUnit.scl(Math.abs(diff)));
        }
        calculateRotationFromCenter();

        if(isGrounded()){airtime=0;}
    }

    private Vector2 getArmCenterPosition(){
        Vector2 diff = new Vector2(0,0);

        if(movingDirection == 0 || inJump){
            diff = this.leftUnit.cpy().scl(10f);
        }

        return  this.center.cpy().add(diff);
    }

    private Vector2 getSelectedWeaponPos(){
        Vector2 result = getArmCenterPosition();
        result.x-= weapons[selectedWeapon].sprite.getWidth()/2;
        result.y-= weapons[selectedWeapon].sprite.getHeight()/2;

        return result;
    }

    //Weapon stuff

    public Weapon getSelectedWeapon(){
        return weapons[selectedWeapon];
    }

    public void nextWeapon(){
        if(selectedWeapon == weapons.length-1){
            selectedWeapon = 0;
        } else {
            selectedWeapon++;
        }
    }

    public void prevWeapon(){
        if(selectedWeapon == 0){
            selectedWeapon = weapons.length-1;
        } else {
            selectedWeapon --;
        }
    }

    //String
    public String toString(){
        return "[CirclePlayer] POS["+this.center.toString()+"] SPEED "+this.speed.len()+" ["+this.speed.toString()+"] ACC "+this.acceleration.len()+" ["+ this.acceleration.toString() +"]";

    }
}
