package com.mygdx.zegame.java.enemies.roller;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.zegame.java.constants.Constants;
import com.mygdx.zegame.java.gameworld.entities.MovingEntity;
import com.mygdx.zegame.java.gameworld.planets.Planet;

public class Roller extends MovingEntity {

    final float MAX_HORIZONTAL_SPEED = 10;

    Sprite sprite;

    public float circumference;

    public float untranslatedHorizontalSpeed;

    public float angle;

    public Roller(float x, float y, Planet planet){
        super(x,y, Constants.DEFAULT_PLAYER_SIZE*1.5f,planet);

        sprite = new Sprite(new Texture("sprites/enemies/roller/roller512.png"));

        //When roller moves the length of circumference in any direction he makes full rotation
        circumference = 2f * (float) Math.PI * this.radius;
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        sprite.setPosition(center.x-sprite.getWidth()/2,center.y-sprite.getHeight()/2);
        sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
        sprite.setScale(radius*2/sprite.getWidth());
        //sprite.setRotation(rotationFromCenter-90);
    }

    @Override
    public void draw(ShapeRenderer shapeRenderer) {

    }

    public void updateAngle(float movedHorizontally){
        float prevAngle = angle;
        angle = 360/ (circumference/movedHorizontally);

    }

    private void calculateAngle(){

    }
}
