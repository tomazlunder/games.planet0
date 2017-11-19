package com.mygdx.zegame.java;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Commons {
    public static Vector2 vec3to2(Vector3 v3){
        return new Vector2(v3.x,v3.y);
    }
}
