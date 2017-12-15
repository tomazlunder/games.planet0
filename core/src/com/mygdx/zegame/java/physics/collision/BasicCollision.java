package com.mygdx.zegame.java.physics.collision;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.zegame.java.physics.collision.shapes.CollisionShape;

public interface BasicCollision {
    public CollisionShape getCollisionShape();

    public void setPosition(Vector2 v);
}
