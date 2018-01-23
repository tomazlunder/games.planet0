package com.mygdx.zegame.java.weapons.ammo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.zegame.java.gameworld.entities.Entity;
import com.mygdx.zegame.java.gameworld.planets.Planet;

public abstract class Bullet extends Entity {

    protected float range;
    protected float dist_traveled;
    protected Entity shooter;
    protected float damage;
    protected Texture texture;
    protected Vector2 direction;

    public Bullet(float x, float y, float radius, Planet planet) {
        super(x, y, radius, planet);
    }


    public Entity getShooter() {
        return shooter;
    }

    public float getDamage() {
        return damage;
    }
}
