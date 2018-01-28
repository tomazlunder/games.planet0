package com.mygdx.zegame.java.weapons.ammo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.zegame.java.Living;
import com.mygdx.zegame.java.commons.Commons;
import com.mygdx.zegame.java.gameworld.entities.Entity;
import com.mygdx.zegame.java.gameworld.entities.moving.player.CirclePlayer;
import com.mygdx.zegame.java.gameworld.planets.Planet;

public class StartBullet extends Bullet{


    public StartBullet(Vector2 from, Vector2 to, Planet planet, Entity shooter){
        super(from.x,from.y,3, planet);

        name = "StartBullet";
        range = 2000;
        dist_traveled = 0;
        damage = 10;

        texture = new Texture("bullet.png");

        direction = Commons.unitBetweenTwo(from,to);

        this.shooter = shooter;
    }

    @Override
    public void update(float deltaTime) {
        Vector2 newPos = this.center;
        newPos.add(direction.cpy().scl(deltaTime*1200));
        dist_traveled += direction.cpy().scl(deltaTime*1200).len();

        this.center = newPos;
        this.baseCollision.updatePosition(center);

        if(dist_traveled > range){
            this.removeFromPlanet();
            System.out.println("[StartBullet] removed.");
        }

    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        //spriteBatch.begin();
        spriteBatch.draw(texture,center.x-radius, center.y - radius, radius*2, radius*2);
        //spriteBatch.end();

    }

    @Override
    public void draw(ShapeRenderer shapeRenderer) {

    }
}
