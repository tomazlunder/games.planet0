package com.mygdx.zegame.java.weapons.ammo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.zegame.java.commons.Commons;
import com.mygdx.zegame.java.gameworld.entities.Entity;
import com.mygdx.zegame.java.gameworld.planets.Planet;

public class StartBullet extends Entity{

    private float range;
    private float dist_traveled;
    private Texture texture;


    private Vector2 direction;

    public StartBullet(Vector2 from, Vector2 to, Planet planet){
        super(from.x,from.y,3, planet);

        range = 1000;
        dist_traveled = 0;

        texture = new Texture("bullet.png");

        direction = Commons.unitBetweenTwo(from,to);
    }

    @Override
    public void update(float deltaTime) {
        Vector2 newPos = this.center;
        newPos.add(direction.cpy().scl(deltaTime*1200));
        dist_traveled += direction.cpy().scl(deltaTime*1200).len();

        this.center = newPos;
        //System.out.println(this.center.toString());
        //System.out.println(dist_traveled);

        if(dist_traveled > range){
            this.removeFromPlanet();
            System.out.println("[StartBullet] removed.");
        }
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        spriteBatch.begin();
        spriteBatch.draw(texture,center.x-radius, center.y - radius, radius*2, radius*2);
        spriteBatch.end();

    }

    @Override
    public void draw(ShapeRenderer shapeRenderer) {

    }
}
