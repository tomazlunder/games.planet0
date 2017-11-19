package com.mygdx.zegame.java.objects.moving.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.zegame.java.objects.MovingGObject;

public class CirclePlayer extends MovingGObject{
    private float GROUNDED_ERROR = 0.05f, MAX_SPEED = 6;

    private float radius;
    private Texture texture;
    private Sprite sprite;
    private TextureRegion textureRegion;
    private int testTick;


    public CirclePlayer(float x, float y, float wx, float wy, float wr, float height, float radius){
        super(x,y,wx,wy, wr);
        this.radius = radius;
        this.collisionCircle = new Circle(this.centerX,this.centerY,height/2);

        this.texture = new Texture("player.png");
        sprite = new Sprite(texture);
        textureRegion = new TextureRegion(texture);
        testTick=0;
    }

    public CirclePlayer(float wx, float wy, float wr, float radius){
        super(wx,wy+wr+radius*3,wx,wy, wr);
        this.radius = radius;
        this.maxSpeed = MAX_SPEED;
        this.collisionCircle = new Circle(this.centerX,this.centerY,radius);

        this.texture = new Texture("player.png");
        sprite = new Sprite(texture);
        textureRegion = new TextureRegion(texture);
        testTick=0;
    }

    public float getRadius(){return this.radius;}
    public void setRadius(float r){this.radius = r;}

    public void draw(SpriteBatch spriteBatch){
        testTick++;
        spriteBatch.begin();



        Vector2 bla = new Vector2(centerX,centerY);
        bla.mulAdd(downUnit,radius);
        bla.mulAdd(downUnit.cpy().rotate(-90f),radius);
        sprite.setPosition(centerX-sprite.getWidth()/2,centerY-sprite.getHeight()/2);
        sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
        sprite.setScale(0.05f);
        sprite.setRotation(rotationFromCenter-90);
        //sprite.setPosition(centerX-radius,centerY-radius);

        //sprite.setPosition(bla.x,bla.y);
        sprite.draw(spriteBatch);
        //spriteBatch.draw(textureRegion,worldCenterX,worldCenterY, textureRegion.getRegionWidth()/2,textureRegion.getRegionHeight()/2, radius*4,radius*4,1f,1f, testTick%360);
        //spriteBatch.draw(textureRegion, worldCenterX, worldCenterY, centerX, centerY, radius*200, radius*200, 10f, 10f, 0);
        spriteBatch.end();
    }

    public void draw(ShapeRenderer shapeRenderer){
        //shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GREEN);
        /*
        shapeRenderer.rect(centerX-width/2, centerY-height/2,
                width/2, height/2,
                width, height,
                1,1,
                calculateRotationFromCenter()-90);
        */
        shapeRenderer.circle(centerX,centerY,radius,50);

        shapeRenderer.end();
    }

    public boolean isGrounded(){
        return (this.distanceFromCenter <= this.worldRadius+this.radius+GROUNDED_ERROR);
    }

    public void calcMoveVector(float speedLeft, float speedRight, boolean jump){
        if(!isGrounded()){
            airtime++;

            if(this.upAcc > 0){
                this.upAcc -= 0.34 * airtime ;
            }
        }

        if(jump && isGrounded()){
            airtime = 0;
            this.upAcc = 8;
        }

        this.downSpeed = (this.worldRadius*this.worldRadius) / (this.distanceFromCenter*this.distanceFromCenter);
        Vector2 newMoveVector = this.downUnit.cpy();
        newMoveVector.scl(downSpeed);

        newMoveVector.add(upUnit.cpy().scl(upAcc));
        newMoveVector.add(leftUnit.scl(speedLeft));
        newMoveVector.add(rightUnit.scl(speedRight));

        this.moveVector = newMoveVector.cpy();
    }

    public void movePlayerByMoveVector(){
        if(moveVector.len() > this.maxSpeed) moveVector.setLength(this.maxSpeed);
        Vector2 currPos = new Vector2(this.centerX, this.centerY);
        currPos.add(this.moveVector);
        float distance = currPos.dst(new Vector2(this.worldCenterX, this.worldCenterY));
        float diff = (this.worldRadius + this.radius) - distance;
        if(diff > 0){
            currPos.add(this.upUnit.scl(Math.abs(diff)));
        }
        this.centerX = currPos.x;
        this.centerY = currPos.y;
        calculateRotationFromCenter();
    }

    public String toString(){
        return "[CirclePlayer] POS["+this.centerX+", "+this.centerY+"] MOVE VECTOR["+this.moveVector.toString()+"]";
    }

}
