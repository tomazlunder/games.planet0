package com.mygdx.zegame.java.gameworld.entities.moving.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.zegame.java.Living;
import com.mygdx.zegame.java.commons.Commons;
import com.mygdx.zegame.java.constants.Constants;
import com.mygdx.zegame.java.gameworld.entities.nonmoving.JetpackDeathzone;
import com.mygdx.zegame.java.gameworld.planets.Planet;
import com.mygdx.zegame.java.gameworld.entities.MovingEntity;
import com.mygdx.zegame.java.settings.DefaultVolumeSettings;
import com.mygdx.zegame.java.sound.SoundSingleton;
import com.mygdx.zegame.java.weapons.ammo.AmmoEnum;
import com.mygdx.zegame.java.weapons.StartGun;
import com.mygdx.zegame.java.weapons.Weapon;
import com.mygdx.zegame.java.weapons.ammo.StartBullet;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CirclePlayer extends MovingEntity implements Living {
    //Player physics
    private float DEFAULT_MAX_HORIZONTAL_SPEED = 8f;
    private float DEFAULT_MAX_SPEED = 22;
    private float DEFAULT_JUMP_ACC = 2.5f;
    private float DEFAULT_MAX_ACC = 3;
    private float DEFAULT_JUMP_TIMEOUT = 0.5f;
    private float DEFAULT_ACC_STEP = DEFAULT_MAX_ACC / 10;
    private float DEFAULT_DEACC_STEP = DEFAULT_MAX_SPEED / 2;
    private float SPEED_FACTOR = 60;
    private float DEFAULT_GRAVITY = 0.25f;

    public float JETPACK_FUEL_MAX = 100;
    public float jetpackFuel;

    public float MAX_JETPACK_HEIGHT = 400;

    int score;

    private float legHeight;

    public boolean movingLeft, movingRight, jumping, movingDown, movingUp;

    public Vector3 aimingAt;

    public boolean jetpackOn;

    private boolean inJump;
    private float jumpTimeout;
    private float airtime;

    private float SQUASHED_TIME = 2.2f;
    private float squashedTimeLeft;

    //Player health
    private float MAX_HEALTH = 100f;
    private float MAX_ARMOR = 100f;

    private float DEFAULT_HEALTH = 100f;
    private float DEFAULT_SHIELD = 0;
    public float healthPoints, armorPoints;

    //Player animation
    private int currentFrame;
    private TextureAtlas ta, taSquashed;
    private TextureRegion trBody, trLegs, trFace;
    private Sprite spriteBody, spriteLegs, spriteFace;
    private Sprite spriteGun;
    private Sprite spriteJetLeft, spriteJetFront, spriteJetRight;

    private ParticleEffect jetpackParticle;

    String LEG_DEFAULT = "legs_idle0";
    String BODY_DEFAULT = "body0";
    String LEG_JUMP = "legs_jump";
    String FACE_IDLE = "face_idle";
    String FACE_LEFT = "face_left";
    String LEGS_JUMP_LEFT = "legs_jump_left";

    Animation<TextureRegion> bodyAnimation;
    Animation<TextureRegion> runAnimation;

    private Map<AmmoEnum, Integer> ammo;

    private float movingDirection;

    List<String> BODY_ANIMATION = Arrays.asList("body0", "body1", "body2");
    List<String> LEG_RUN_ANIMATION = Arrays.asList("legs_run_left0", "legs_run_left2", "legs_run_left4",
            "legs_run_left6", "legs_run_left8", "legs_run_left10",
            "legs_run_left12", "legs_run_left14", "legs_run_left16",
            "legs_run_left18");

    List<String> SQUASHED_ANIMATION = Arrays.asList("squashed1", "squashed2", "squashed3", "squashed4", "squashed5", "squashed6", "squashed7");


    //Weapon and inventory stuff
    public Weapon[] weapons;
    public int selectedWeapon;

    private float mTimeToNextStep;

    private long jetpackSoundId;


    public CirclePlayer(float radius, Planet planet, OrthographicCamera camera) {
        super(planet.getX(), planet.getY() + planet.getRadius() + radius * 2, radius, planet);

        this.name = "CirclePlayer";
        this.center = new Vector2(planet.getX(), planet.getY() + planet.getRadius() + radius);
        this.radius = radius;
        this.maxSpeed = DEFAULT_MAX_SPEED;
        this.maxAcceleration = DEFAULT_MAX_ACC;
        this.accelerationStep = DEFAULT_ACC_STEP;
        this.jumpTimeout = DEFAULT_JUMP_TIMEOUT;
        this.airtime = 0;
        this.inJump = false;

        this.legHeight = radius / 2.5f;

        this.healthPoints = DEFAULT_HEALTH;
        this.armorPoints = DEFAULT_SHIELD;

        ta = new TextureAtlas("spritesheets/ss_player2.atlas");
        taSquashed = new TextureAtlas("spritesheets/ss_squashed.atlas");

        trFace = ta.findRegion(FACE_IDLE);
        spriteFace = new Sprite(trFace);

        trBody = ta.findRegion(BODY_DEFAULT);
        spriteBody = new Sprite(trBody);

        trLegs = ta.findRegion(LEG_DEFAULT);
        spriteLegs = new Sprite(trLegs);

        TextureRegion[] trs = new TextureRegion[LEG_RUN_ANIMATION.size()];
        for (int i = 0; i < LEG_RUN_ANIMATION.size(); i++) {
            trs[i] = (ta.findRegion(LEG_RUN_ANIMATION.get(i)));
        }
        runAnimation = new Animation(1f / 4.5f, trs);

        trs = new TextureRegion[BODY_ANIMATION.size()];
        for (int i = 0; i < BODY_ANIMATION.size(); i++) {
            trs[i] = (ta.findRegion(BODY_ANIMATION.get(i)));
        }
        bodyAnimation = new Animation<TextureRegion>(1f / 1f, trs);

        spriteGun = new Sprite(new Texture("sprites/weapons/gun.png"), 256, 256);

        spriteJetFront = new Sprite(new Texture("sprites/player/jetpackFront1.png"), 256, 256);
        spriteJetLeft = new Sprite(new Texture("sprites/player/jetpackLeft.png"), 256, 256);
        spriteJetRight = new Sprite(new Texture("sprites/player/jetpackRight.png"), 256, 256);


        score = 0;

        //WEAPONS YEAH
        this.weapons = new Weapon[6];
        this.selectedWeapon = 0;

        this.weapons[0] = new StartGun();

        ammo = new HashMap<AmmoEnum, Integer>();
        ammo.put(AmmoEnum.STARTBULLET, 50);

        this.squashedTimeLeft = 0;

        movingLeft = movingRight = jumping = movingDown = movingUp = false;
        jetpackOn = false;

        mTimeToNextStep = 0;

        jetpackParticle = new ParticleEffect();
        jetpackParticle.load(Gdx.files.internal("effects/flame.p"), Gdx.files.internal("effects"));
        jetpackParticle.load(Gdx.files.internal("effects/flame.p"), Gdx.files.internal("effects"));

        jetpackFuel = JETPACK_FUEL_MAX;

        this.calculateRotationFromCenter();
        this.calcGravityForce();
        this.calcUnitVectors();
    }

    //UPDATE
    @Override
    public void update(float deltaTime) {
        this.calculateRotationFromCenter();
        this.calcGravityForce();
        this.calcUnitVectors();


        //JETPACK PARTICLE EFFECTS
        Vector2 particlePos = center.cpy().add(downUnit.cpy().scl(radius));
        if(movingDirection == -1){
            particlePos.add(rightUnit.cpy().scl(radius));
        } else if(movingDirection == 1){
            particlePos.add(leftUnit.cpy().scl(radius));
        }


        jetpackParticle.setPosition(particlePos.x, particlePos.y);
        jetpackParticle.update(deltaTime);

        float low = getRotationFromCenter();
        low -= 180;
        jetpackParticle.findEmitter("Flame").getAngle().setLow(low);
        jetpackParticle.findEmitter("Flame").getAngle().setHighMin(low - 45);
        jetpackParticle.findEmitter("Flame").getAngle().setHighMax(low + 45);


        //jetpackParticle.findEmitter("flame").getRotation().se

        if (squashedTimeLeft > 0) {
            float heightFromGround = heigthFromGround();
            if(heigthFromGround() > 0){
                this.center = this.center.add(downUnit.cpy().scl(heightFromGround/10));
            }

            squashedTimeLeft -= deltaTime;
            if (squashedTimeLeft <= 0) {
                squashedTimeLeft = 0;
                this.collision = true;
            }
            return;
        }

        //JETPACK DEATH ZONE
        if(jetpackOn){
            Vector2 posToParticle = particlePos.sub(center);


            //Vector2 deathZonePos = center.cpy().add(downUnit.cpy().scl(radius*2));
            Vector2 deathZonePos = center.cpy().add(posToParticle);
            new JetpackDeathzone(deathZonePos.x, deathZonePos.y, radius, nearestPlanet,this);
            deathZonePos = deathZonePos.add(posToParticle);
            new JetpackDeathzone(deathZonePos.x, deathZonePos.y, radius, nearestPlanet,this);
        }

        for (Weapon w : weapons) {
            if (w != null) {
                w.update(deltaTime);
            }
        }

        //SETTING VARIABLES FOR ANIMATION
        currentFrame++;
        /*
        if (movingLeft && movingDirection != -1) {
            currentFrame = 0;
            movingDirection = -1;
        } else if (movingRight && movingDirection != 1) {
            currentFrame = 0;
            movingDirection = 1;
        } else if (!movingLeft && !movingRight) {
            movingDirection = 0;
            currentFrame = 0;
        }
        */
        if (this.speed.x < 0 && movingLeft) {
            movingDirection = -1;
            //currentFrame = 0;
        }else if(this.speed.x > 0 && movingRight){
            movingDirection = 1;
            //currentFrame = 0;
        } else if (this.speed.x < 0.01 && speed.x > -0.01 && !movingLeft && !movingRight){
            movingDirection = 0;
            //currentFrame = 0;
        }

        //If the player is on the ground and has negative acceleration, his acceleration becomes 0
        if (isGrounded() && !inJump && this.acceleration.y < 0) {
            this.acceleration.y = 0;
            this.speed.y = 0;
        }

        if(!jetpackOn) {
            acceleration.y -= DEFAULT_GRAVITY;


            //JUMP RELATED
            //Timeout to prevent double jumping
            if (jumpTimeout > 0) {
                jumpTimeout -= deltaTime;
            }

            //Player landed from jump
            if (inJump && isGrounded() && jumpTimeout < DEFAULT_JUMP_TIMEOUT / 2) {
                inJump = false;
            }

            //PLAYER JUMPS
            if (jumping && isGrounded() && !inJump && jumpTimeout <= 0) {
                SoundSingleton.getInstance().jump.play(DefaultVolumeSettings.FX_JUMP_VOLUME);
                jumpTimeout = DEFAULT_JUMP_TIMEOUT;
                airtime = 0;
                inJump = true;
                this.acceleration.y = DEFAULT_JUMP_ACC;
            } else if (inJump) {
                airtime += deltaTime;
            }
        }

        if(jetpackOn) {
            inJump = false;
            airtime = 0;
            jetpackFuel -= deltaTime*2;

            if(heigthFromGround() > MAX_JETPACK_HEIGHT && speed.y > 0){
                speed.y = speed.y/1.04f;
            }

            if (!movingDown ) {
                acceleration.y = 0;
            }

            if((movingUp || jumping) && heigthFromGround() < MAX_JETPACK_HEIGHT){
                acceleration.y = 0.25f;
            }

            else if (!movingDown) {
                acceleration.y = -DEFAULT_GRAVITY/8;
            } else {
                acceleration.y -= DEFAULT_GRAVITY/2;
            }
        }


        if (isGrounded()) {
            airtime = 0;
        }


        //HORIZONTAL [LEFT/RIGHT] MOVEMENT
        if (!inJump) {
            //if   On ground and pressing left
            //else On ground not pressing left
            if (movingLeft && !movingRight) {
                if (this.acceleration.x > -maxAcceleration) {
                    this.acceleration.x -= accelerationStep;
                }
            }

            //if On ground and pressing right
            //else On ground not pressing right
            if (movingRight && !movingLeft) {
                if (this.acceleration.x < maxAcceleration) {
                    this.acceleration.x += accelerationStep;
                }
            }

            if (!movingRight && !movingLeft) {
                this.acceleration.x = 0;
                this.speed.x /= 3;
            }
        }

        this.speed.add(acceleration);

        /*
        if (Math.abs(speed.x) > maxSpeed) {
            speed.setLength(maxSpeed);
        }
        */

        //FOOTSTEP SOUNDFX
        if ((movingRight || movingLeft) && isGrounded()) {
            mTimeToNextStep -= deltaTime;
            if (mTimeToNextStep < 0) {
                SoundSingleton.getInstance().footstep.play(DefaultVolumeSettings.FX_FOOTSTEP);
                mTimeToNextStep = Constants.TIME_BETWEEN_STEP_SOUNDS + (float) Commons.randomWithRange(0, Constants.TIME_BETWEEN_STEP_SOUNDS_RANDOM_MAX);
            }
        } else {
            mTimeToNextStep = 0; //or whatever delay you want for the first sound when
            //you start walking
        }

        //Converting to universe coordinates
        Vector2 verticalSpeed, horizontalSpeed, trueSpeed;
        verticalSpeed = upUnit.cpy().scl(speed.y);
        horizontalSpeed = rightUnit.cpy().scl(speed.x);

        if (!jetpackOn && horizontalSpeed.len() > DEFAULT_MAX_HORIZONTAL_SPEED) {
            horizontalSpeed.setLength(DEFAULT_MAX_HORIZONTAL_SPEED);
        }
        if (jetpackOn && horizontalSpeed.len() > DEFAULT_MAX_HORIZONTAL_SPEED*1.5f){
            horizontalSpeed.setLength(DEFAULT_MAX_HORIZONTAL_SPEED*1.5f);

        }

        //Scaling with time
        trueSpeed = verticalSpeed.add(horizontalSpeed).scl(deltaTime * SPEED_FACTOR);

        this.newPosition = center.cpy();
        this.newPosition.add(trueSpeed);
        this.center = newPosition;
        correctForPlanet();
        this.baseCollision.updatePosition(center);

        movingLeft = movingRight = jumping = movingDown = movingUp = false;

    }


    //DRAW WITH SPRITES
    public void draw(SpriteBatch spriteBatch) {
        //spriteBatch.begin();
        //drawAiming(spriteBatch);
        if (squashedTimeLeft > 0) {
            drawSquashed(spriteBatch);
        } else if (!jetpackOn) {
            if (movingDirection == -1) {
                drawSelectedWeapon(spriteBatch);
                drawBody(spriteBatch);
                drawLegsAndFace(spriteBatch);
            } else {
                drawBody(spriteBatch);
                drawLegsAndFace(spriteBatch);
                drawSelectedWeapon(spriteBatch);
            }
        } else if (jetpackOn){
            if(movingDirection == -1){
                drawSelectedWeapon(spriteBatch);
                drawJetpackBody(spriteBatch);
            } else {
                drawJetpackBody(spriteBatch);
                drawSelectedWeapon(spriteBatch);
            }

        }
        //spriteBatch.end();
    }

    public void drawSelectedWeapon(SpriteBatch spriteBatch) {
        if (weapons[selectedWeapon] != null) {
            Vector2 weaponPosition = getSelectedWeaponPos();
            Sprite weaponSprite = weapons[selectedWeapon].sprite;

            weaponSprite.setPosition(weaponPosition.x, weaponPosition.y);
            weaponSprite.setOrigin(weaponSprite.getWidth() / 2, weaponSprite.getHeight() / 2);
            //weaponSprite.setScale(radius*2/weaponSprite.getWidth());
            weaponSprite.setScale(radius * 2 / (weaponSprite.getWidth() * 2 / 3));

            weaponSprite.setRotation(0);

            if (aimingAt != null) {
                weaponSprite.rotate(Commons.angleBetweenPoints(newPosition, Commons.vec3to2(aimingAt)));
            }

            weaponSprite.draw(spriteBatch);
        }
        //TODO: ELSE DRAW HANDS (no weapon) or something

    }

    private void drawAiming(SpriteBatch spriteBatch) {
        if (aimingAt != null) {
            Commons.drawLine(spriteBatch, getArmCenterPosition(), Commons.vec3to2(aimingAt));
        }
    }

    public void drawLegsAndFace(SpriteBatch spriteBatch) {
        if (trLegs.isFlipX()) {
            trLegs.flip(true, false);
        }
        if (trFace.isFlipX()) {
            trFace.flip(true, false);
        }

        //DRAWING FACE
        if (movingDirection != 0) {
            trFace = ta.findRegion(FACE_LEFT);
            if (this.movingDirection == 1) {
                trFace.flip(true, false);
            }
        } else {
            trFace = ta.findRegion(FACE_IDLE);
        }

        //DRAWING LEGS
        if (!this.inJump) {
            //Not moving L/R
            if (this.movingDirection == 0) {
                trLegs = ta.findRegion(LEG_DEFAULT);
            }
            //Moving L/R
            else {
                trLegs = ta.findRegion(LEG_RUN_ANIMATION.get(currentFrame % LEG_RUN_ANIMATION.size()));
                if (this.movingDirection == 1) {
                    trLegs.flip(true, false);
                }
            }
        }
        //In jump````
        else {
            //Not moving L/R
            if (this.movingDirection == 0) {
                trLegs = ta.findRegion(LEG_JUMP);
            }
            //Moving L/R
            else {
                trLegs = ta.findRegion(LEGS_JUMP_LEFT);
                if (this.movingDirection == 1) {
                    trLegs.flip(true, false);
                }
            }
        }

        spriteLegs.setRegion(trLegs);
        spriteLegs.setPosition(center.x - spriteLegs.getWidth() / 2, center.y - spriteLegs.getHeight() / 2);
        spriteLegs.setOrigin(spriteLegs.getWidth() / 2, spriteLegs.getHeight() / 2);
        //spriteLegs.setScale(radius*2/spriteLegs.getWidth());
        spriteLegs.setScale(radius * 2 / (spriteLegs.getWidth() * 2 / 3));

        spriteLegs.setRotation(rotationFromCenter - 90);

        spriteLegs.draw(spriteBatch);

        spriteFace.setRegion(trFace);
        spriteFace.setPosition(center.x - spriteFace.getWidth() / 2, center.y - spriteFace.getHeight() / 2);
        spriteFace.setOrigin(spriteFace.getWidth() / 2, spriteFace.getHeight() / 2);
        //spriteFace.setScale(radius*2/spriteFace.getWidth());
        spriteFace.setScale(radius * 2 / (spriteFace.getWidth() * 2 / 3));

        spriteFace.setRotation(rotationFromCenter - 90);

        spriteFace.draw(spriteBatch);
    }

    public void drawBody(SpriteBatch spriteBatch) {
        trBody = ta.findRegion(BODY_ANIMATION.get(currentFrame % BODY_ANIMATION.size()));
        trBody = ta.findRegion("body1");

        spriteBody.setRegion(trBody);
        spriteBody.setPosition(center.x - spriteBody.getWidth() / 2, center.y - spriteBody.getHeight() / 2);
        spriteBody.setOrigin(spriteBody.getWidth() / 2, spriteBody.getHeight() / 2);
        //spriteBody.setScale(radius*2/spriteBody.getWidth());
        spriteBody.setScale(radius * 2 / (spriteBody.getWidth() * 2 / 3));
        spriteBody.setRotation(rotationFromCenter - 90);
        spriteBody.draw(spriteBatch);

    }

    public void drawJetpackBody(SpriteBatch spriteBatch){
        if(jetpackParticle.isComplete()){
            jetpackParticle.reset();
        }

        jetpackParticle.draw(spriteBatch);


        if(movingDirection == -1){
            spriteJetLeft.setPosition(center.x - spriteJetLeft.getWidth() / 2, center.y - spriteJetLeft.getHeight() / 2);
            spriteJetLeft.setOrigin(spriteJetLeft.getWidth() / 2, spriteJetLeft.getHeight() / 2);
            //spriteBody.setScale(radius*2/spriteBody.getWidth());
            spriteJetLeft.setScale(radius * 2 / (spriteJetLeft.getWidth() * 2 / 3));
            spriteJetLeft.setRotation(rotationFromCenter - 90);
            spriteJetLeft.draw(spriteBatch);
        } else if (movingDirection == 0) {
            spriteJetFront.setPosition(center.x - spriteJetFront.getWidth() / 2, center.y - spriteJetFront.getHeight() / 2);
            spriteJetFront.setOrigin(spriteJetFront.getWidth() / 2, spriteJetFront.getHeight() / 2);
            //spriteBody.setScale(radius*2/spriteBody.getWidth());
            spriteJetFront.setScale(radius * 2 / (spriteJetFront.getWidth() * 2 / 3));
            spriteJetFront.setRotation(rotationFromCenter - 90);
            spriteJetFront.draw(spriteBatch);
        } else if (movingDirection == 1 ){
            spriteJetRight.setPosition(center.x - spriteJetRight.getWidth() / 2, center.y - spriteJetRight.getHeight() / 2);
            spriteJetRight.setOrigin(spriteJetRight.getWidth() / 2, spriteJetRight.getHeight() / 2);
            //spriteBody.setScale(radius*2/spriteBody.getWidth());
            spriteJetRight.setScale(radius * 2 / (spriteJetRight.getWidth() * 2 / 3));
            spriteJetRight.setRotation(rotationFromCenter - 90);
            spriteJetRight.draw(spriteBatch);
        }

    }

    public void drawSquashed(SpriteBatch spriteBatch) {
        if (squashedTimeLeft > SQUASHED_TIME / 2) {
            trBody = taSquashed.findRegion(SQUASHED_ANIMATION.get(0));
        } else {
            int numFrames = SQUASHED_ANIMATION.size() - 1;
            float timeForOne = (SQUASHED_TIME / 2) / numFrames;
            int current = (int) (squashedTimeLeft / timeForOne);
            trBody = taSquashed.findRegion(SQUASHED_ANIMATION.get(SQUASHED_ANIMATION.size() - 1 - current));
        }


        spriteBody.setRegion(trBody);
        spriteBody.setPosition(center.x - spriteBody.getWidth() / 2, center.y - spriteBody.getHeight() / 2);
        spriteBody.setOrigin(spriteBody.getWidth() / 2, spriteBody.getHeight() / 2);
        //spriteBody.setScale(radius*2/spriteBody.getWidth());
        spriteBody.setScale(radius * 2 / (spriteBody.getWidth() * 2 / 3));
        spriteBody.setRotation(rotationFromCenter - 90);
        spriteBody.draw(spriteBatch);

    }

    //DRAW SIMPLE
    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.circle(center.x, center.y, radius, 50);

        shapeRenderer.end();
    }


    //UTILITY
    private void correctForPlanet() {
        float distance = distanceFromCenter();
        float diff = (nearestPlanet.getRadius() + this.radius + this.legHeight) - distance;
        if (diff > 0) {
            center.add(this.upUnit.scl(Math.abs(diff)));
        }
        calculateRotationFromCenter();

        if (isGrounded()) {
            airtime = 0;
        }
    }


    private Vector2 getArmCenterPosition() {
        Vector2 diff = new Vector2(0, 0);

        if (movingDirection == 0) { //|| inJump){
            diff = this.leftUnit.cpy().scl(this.radius * 4 / 5);
        }

        return this.center.cpy().add(diff);
    }

    private Vector2 getGunTipPosition() {
        Vector2 unitToBarrel = Commons.unitBetweenTwo(getArmCenterPosition(), Commons.vec3to2(aimingAt));
        unitToBarrel.setLength(this.radius * Constants.GUN_TIP_CONST);
        return getArmCenterPosition().add(unitToBarrel);
    }

    private Vector2 getSelectedWeaponPos() {
        Vector2 result = getArmCenterPosition();
        result.x -= weapons[selectedWeapon].sprite.getWidth() / 2;
        result.y -= weapons[selectedWeapon].sprite.getHeight() / 2;

        return result;
    }

    //HEALTH RELATED
    @Override
    public void takeDamage(float damage) {
        if (this.armorPoints > 0) {
            armorPoints -= damage;
            if (armorPoints < 0) {
                armorPoints = 0;
            }
        } else {
            healthPoints -= damage;
        }
    }

    @Override
    public float getMaxHealth() {
        return MAX_HEALTH;
    }

    @Override
    public float getMaxArmor() {
        return MAX_ARMOR;
    }

    @Override
    public float getHealth() {
        return healthPoints;
    }

    @Override
    public float getArmor() {
        return armorPoints;
    }


    public void gainShield() {
        this.armorPoints = 100;
    }

    public boolean isDead() {
        return healthPoints <= 0;
    }

    //WEAPONS / INVENTORY

    public Weapon getSelectedWeapon() {
        return weapons[selectedWeapon];
    }

    public void nextWeapon() {
        if (selectedWeapon == weapons.length - 1) {
            selectedWeapon = 0;
        } else {
            selectedWeapon++;
        }
    }

    public void prevWeapon() {
        if (selectedWeapon == 0) {
            selectedWeapon = weapons.length - 1;
        } else {
            selectedWeapon--;
        }
    }

    public void fireWeapon() {
        if (weapons[selectedWeapon] != null) {
            if (weapons[selectedWeapon].shoot()) {
                //nearestPlanet.entities.add(new StartBullet(getGunTipPosition(),Commons.vec3to2(aimingAt),nearestPlanet));
                new StartBullet(getGunTipPosition(), Commons.vec3to2(aimingAt), nearestPlanet, this);
            }
        }
    }

    public void reload() {
        if (weapons[selectedWeapon] != null) {
            if(weapons[selectedWeapon].reloadCooldown <= 0) {
                ammo.put(weapons[selectedWeapon].ammoType, weapons[selectedWeapon].reload(ammo.get(weapons[selectedWeapon].ammoType)));
            }
        }
    }

    public Vector3 getAmmoInOutofTotal(){

        Vector3 toReturn = new Vector3(0,0,0);

        if(weapons[selectedWeapon] == null){
            return  toReturn;
        }
        toReturn.x = weapons[selectedWeapon].magAmmoLeft;
        toReturn.y = weapons[selectedWeapon].magSize;
        toReturn.z = ammo.get(weapons[selectedWeapon].ammoType);

        return toReturn;
    }

    public void addAmmo(AmmoEnum ae, int number){
        if(ammo.containsKey(ae)){
            ammo.put(ae, ammo.get(ae) + number);
        }
        else{
            ammo.put(ae, number);
        }
    }

    public void refuel(){
        this.jetpackFuel = JETPACK_FUEL_MAX;
    }

    @Override
    public float distanceFromCenter() {
        if (nearestPlanet == null) {
            return -1;
        }
        return center.dst(nearestPlanet.getPosition());
    }

    @Override
    public boolean isGrounded() {
        return (heigthFromGround() <= 0.01);
    }

    public float heigthFromGround() {
        return (distanceFromCenter() - (nearestPlanet.getRadius() + radius + this.legHeight));
    }

    //String
    public String toString() {
        return "[CirclePlayer] injump[" + inJump + "] POS[" + this.center.toString() + "] SPEED " + this.speed.len() + " [" + this.speed.toString() + "] ACC " + this.acceleration.len() + " [" + this.acceleration.toString() + "]";

    }

    public void toggleJetpack(){
        if(!jetpackOn && jetpackFuel <= 0){
            jetpackFuel = 0;
            return;
        }

        if(!jetpackOn){
            jetpackSoundId = SoundSingleton.getInstance().jetpack.loop(DefaultVolumeSettings.FX_JETPACK_VOLUME);
            jetpackParticle.setPosition(center.x, center.y);
            jetpackParticle.start();
        }
        else {
            SoundSingleton.getInstance().jetpack.stop(jetpackSoundId);
        }

        jetpackOn = !jetpackOn;


    }

    public float getPercentageHeight(){
        if(heigthFromGround()/MAX_JETPACK_HEIGHT <= 1.0f){
            return heigthFromGround()/MAX_JETPACK_HEIGHT;
        }
        if(heigthFromGround() < 0){
            return 0;
        }

        return 1;
    }


    /*
     * SQUASH
     */
    public void squash() {
        if (squashedTimeLeft <= 0) {
            SoundSingleton.getInstance().splat.play();
            this.takeDamage(20);
            this.collision = false;
            this.squashedTimeLeft = SQUASHED_TIME;

            this.acceleration = new Vector2(0, 0);
            this.speed = new Vector2(0, 0);
        }
    }

    public void addScore(int toAdd) {
        score += toAdd;
    }

    public int getScore(){return score;}
}
