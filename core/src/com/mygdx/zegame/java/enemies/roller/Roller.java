package com.mygdx.zegame.java.enemies.roller;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.zegame.java.Living;
import com.mygdx.zegame.java.constants.Constants;
import com.mygdx.zegame.java.gameworld.entities.Entity;
import com.mygdx.zegame.java.gameworld.entities.MovingEntity;
import com.mygdx.zegame.java.gameworld.entities.moving.player.CirclePlayer;
import com.mygdx.zegame.java.gameworld.planets.Planet;
import com.mygdx.zegame.java.screens.ScreenManager;

public class Roller extends MovingEntity implements Living{

    final float MAX_HORIZONTAL_SPEED = 10;
    private float DEFAULT_GRAVITY = 0.38f;
    private float SPEED_FACTOR = 60;

    private float MAX_HEALTH = 30;
    private float MAX_ARMOR = 0;
    private float healthPoints;
    private float armorPoints;


    Sprite sprite;

    public float circumference;

    public float angle;

    public Roller(float x, float y, Planet planet){
        super(x,y, Constants.DEFAULT_PLAYER_SIZE*2f,planet);

        name = "Roller";
        sprite = new Sprite(new Texture("sprites/enemies/roller/roller512.png"));

        //When roller moves the length of circumference in any direction he makes full rotation
        circumference = 2f * (float) Math.PI * this.radius;

        maxAcceleration = 4;
        maxSpeed = 10;

        accelerationStep = 2;

        //Living
        healthPoints = MAX_HEALTH;
        armorPoints = MAX_ARMOR;
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        sprite.setPosition(center.x-sprite.getWidth()/2,center.y-sprite.getHeight()/2);
        sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
        sprite.setScale(radius*2/sprite.getWidth());

        if(this.speed.x > 0){
            sprite.rotate(-angle);
        } else {
            sprite.rotate(angle);
        }

        spriteBatch.begin();
        sprite.draw(spriteBatch);
        spriteBatch.end();
    }

    @Override
    public void draw(ShapeRenderer shapeRenderer) {

    }

    public void updateAngle(float movedHorizontally) {
        angle = 360/ (circumference/movedHorizontally);
    }


    @Override
    public void update(float deltaTime){
        if(healthPoints <= 0){
            this.removeFromPlanet();
            return;
        }

        this.calcUnitVectors();

        this.speed = new Vector2(4,0);

        acceleration.y -= DEFAULT_GRAVITY;
        if(isGrounded() && this.acceleration.y < 0){
            this.acceleration.y = 0;
        }

        this.speed.add(acceleration);

        //Converting to universe coordinates
        Vector2 verticalSpeed, horizontalSpeed, trueSpeed;
        verticalSpeed = upUnit.cpy().scl(speed.y);
        horizontalSpeed = rightUnit.cpy().scl(speed.x);

        this.updateAngle(horizontalSpeed.len());


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
    }

    @Override
    public void takeDamage(float damage) {
        this.healthPoints -= damage;
    }

    @Override
    public float getMaxHealth() {
        return MAX_HEALTH;
    }

    @Override
    public float getHealth() {
        return healthPoints;
    }

    @Override
    public float getMaxArmor() {
        return MAX_ARMOR;
    }

    @Override
    public float getArmor() {
        return armorPoints;
    }
}
