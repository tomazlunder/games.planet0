package com.mygdx.zegame.java.enemies.orb;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.zegame.java.Living;
import com.mygdx.zegame.java.gameworld.entities.MovingEntity;
import com.mygdx.zegame.java.gameworld.planets.Planet;

public class Orb extends MovingEntity implements Living {
    private float maxHealth, maxArmor, healthPoints, armorPoints;

    public Orb(float x, float y, float r, Planet p){
        super(x,y,r,p);


    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
    public void takeDamage(float damage) {

    }

    @Override
    public float getMaxHealth() {
        return 0;
    }

    @Override
    public float getHealth() {
        return 0;
    }

    @Override
    public float getMaxArmor() {
        return 0;
    }

    @Override
    public float getArmor() {
        return 0;
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {

    }

    @Override
    public void draw(ShapeRenderer shapeRenderer) {

    }
}
