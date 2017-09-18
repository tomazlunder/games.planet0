package com.mygdx.zegame.Objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;

public class CirclePlayer extends MovingGObject {
    private float height, width;


    public CirclePlayer(float x, float y, float wx, float wy, float wr, float height, float width){
        super(x,y,wx,wy, wr);
        this.height = height;
        this.width = width;
        this.collisionCircle = new Circle(this.centerX,this.centerY,height/2);
    }

    public CirclePlayer(float wx, float wy, float wr, float height, float width){
        super(wx,wy+wr+(height/2),wx,wy, wr);
        this.height = height;
        this.width = width;
        this.collisionCircle = new Circle(this.centerX,this.centerY,height);
    }

    public float getHeight(){return this.height;}
    public float getWidth(){return this.width;}
    public void setHeight(float h){this.height = h;}
    public void setWidth(float w){this.height = w;}

    public void drawSimple(ShapeRenderer shapeRenderer){
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
        shapeRenderer.circle(centerX,centerY,height/2,50);

        shapeRenderer.end();
    }

}
