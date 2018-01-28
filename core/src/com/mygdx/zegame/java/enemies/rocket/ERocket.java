package com.mygdx.zegame.java.enemies.rocket;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.zegame.java.Living;
import com.mygdx.zegame.java.commons.Commons;
import com.mygdx.zegame.java.constants.Constants;
import com.mygdx.zegame.java.gameworld.entities.Entity;
import com.mygdx.zegame.java.gameworld.entities.MovingEntity;
import com.mygdx.zegame.java.gameworld.entities.moving.player.CirclePlayer;
import com.mygdx.zegame.java.gameworld.entities.nonmoving.CircleFire;
import com.mygdx.zegame.java.gameworld.planets.Planet;
import com.mygdx.zegame.java.screens.ScreenManager;
import com.mygdx.zegame.java.sound.SoundSingleton;

public class ERocket extends MovingEntity implements Living{

    private float SPEED_FACTOR = 60;

    private float healthPoints;
    private float armorPoints;

    private float maxHealth, maxArmor;

    private float heightFromGround;
    private float playerDetectionRange;

    Sprite sprite;

    private CirclePlayer cp;
    public Vector2 playerPosition;

    private float TIME_TO_NEXT_LOCK = 0.3f;
    private float timeToNextLock;



    public ERocket(float rotation, float height, Planet planet, float scaleToPlayer ,float maxHP, float maxArmor, float maxSpeed, CirclePlayer circlePlayer){
        super(planet.getPointOnSurfaceWithRotation(rotation, height),Constants.DEFAULT_PLAYER_SIZE * scaleToPlayer, planet);

        name = "Rocket";
        sprite = new Sprite(new Texture("sprites/enemies/rocket/rocket256.png"));
        maxAcceleration = 4;

        this.maxHealth = maxHP;
        this.maxArmor = maxArmor;
        healthPoints = maxHP;
        armorPoints = maxArmor;

        this.maxSpeed = maxSpeed;

        heightFromGround = height;
        playerDetectionRange = height+200;
        cp = circlePlayer;

        this.calcUnitVectors();

        timeToNextLock = TIME_TO_NEXT_LOCK;

    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        sprite.setFlip(false, false);

        if(this.speed.x < 0){
            sprite.setFlip(true,false);
        }

        sprite.setPosition(center.x-sprite.getWidth()/2,center.y-sprite.getHeight()/2);
        sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
        sprite.setScale(radius*2/sprite.getWidth());
        if(playerPosition != null){
            sprite.setRotation(speed.angle());
        } else {
            sprite.setRotation(rotationFromCenter -90);
        }

        //spriteBatch.begin();
        sprite.draw(spriteBatch);
        //spriteBatch.end();
    }

    @Override
    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.circle(center.x,center.y,radius,50);

        shapeRenderer.end();
    }



    @Override
    public void update(float deltaTime){


        if(healthPoints <= 0){
            this.removeFromPlanet();
            return;
        }

        this.calcUnitVectors();
        this.calculateRotationFromCenter();

        Vector2 trueSpeed;
        if(playerPosition == null){
            lookForPlayer();

            this.speed = new Vector2(maxSpeed,0);
            //Converting to universe coordinates
            Vector2 verticalSpeed, horizontalSpeed;
            verticalSpeed = upUnit.cpy().scl(speed.y);
            horizontalSpeed = rightUnit.cpy().scl(speed.x);

            //Scaling with time
            trueSpeed = verticalSpeed.add(horizontalSpeed).scl(deltaTime*SPEED_FACTOR);

            this.newPosition = center.cpy();
            this.newPosition.add(trueSpeed);
            this.center = newPosition;

            correctForPlanet();

        } else {
            timeToNextLock-= deltaTime;
            if(timeToNextLock <= 0){
                timeToNextLock = TIME_TO_NEXT_LOCK;
                lookForPlayer();
            }
            //this.speed = Commons.unitBetweenTwo(center, playerPosition).scl(maxSpeed);

            this.acceleration = Commons.unitBetweenTwo(center,playerPosition);

            this.speed.add(acceleration);
            //this.speed.scl(maxSpeed);

            if(speed.len() > maxSpeed){
                speed.setLength(maxSpeed);
            }


            trueSpeed = this.speed.scl(deltaTime*SPEED_FACTOR);


            this.newPosition.add(trueSpeed);
            this.center = newPosition;

            if(nearestPlanet.getCollisionShape().isCollidingWith(this.baseCollision)){
                this.removeFromPlanet();
                new CircleFire(rotationFromCenter, 20, nearestPlanet);
            }
        }


        this.baseCollision.updatePosition(center);
    }


    private void lookForPlayer(){
        if(cp.getPosition().dst(this.center) < playerDetectionRange){
            if(playerPosition == null){
                SoundSingleton.getInstance().lockon.play();
            }
            playerPosition = cp.getPosition();
        }
    }

    private void correctForPlanet(){
        float distance = distanceFromCenter();
        float diff = (nearestPlanet.getRadius() + this.radius + heightFromGround) - distance;
        if(diff > 0){
            center.add(this.upUnit.scl(Math.abs(diff)));
        }
    }


    @Override
    public void takeDamage(float damage) {
        this.healthPoints -= damage;
    }

    @Override
    public float getMaxHealth() {
        return maxHealth;
    }

    @Override
    public float getHealth() {
        return healthPoints;
    }

    @Override
    public float getMaxArmor() {
        return maxArmor;
    }

    @Override
    public float getArmor() {
        return armorPoints;
    }
}
