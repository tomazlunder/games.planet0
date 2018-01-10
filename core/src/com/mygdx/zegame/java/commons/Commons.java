package com.mygdx.zegame.java.commons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Commons {
    public static Vector2 vec3to2(Vector3 v3){
        return new Vector2(v3.x,v3.y);
    }

    public static void drawLine(SpriteBatch batch, Vector2 v1, Vector2 v2)
    {
        Texture red1p = new Texture("commons/drawing/red1p.png");


        float thickness = 1;
        float dx = v1.x-v2.x;
        float dy = v1.y-v2.y;
        //float length = (float)Math.sqrt(dx*dx + dy*dy);
        float length = 250;
        float angle = MathUtils.radiansToDegrees*MathUtils.atan2(dy, dx);
        System.out.println(angle);
        angle = angle-180;
        batch.draw(red1p, v1.x, v1.y, 0f, thickness*0.5f, length, thickness, 1f, 1f, angle, 0, 0, red1p.getWidth(), red1p.getHeight(), false, false);
    }

    public static float angleBetweenPoints(Vector2 v1, Vector2 v2){
        float dx = v1.x  - v2.x;
        float dy = v1.y - v2.y;
        return MathUtils.radiansToDegrees*MathUtils.atan2(dy, dx);
    }

    public static double randomWithRange(double min, double max)
    {
        double range = (max - min);
        return (Math.random() * range) + min;
    }
}
