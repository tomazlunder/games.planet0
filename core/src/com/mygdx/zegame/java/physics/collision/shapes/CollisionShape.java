package com.mygdx.zegame.java.physics.collision.shapes;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public interface CollisionShape {
    public boolean hasParent();
    public boolean hasChildren();
    public List<CollisionShape> getChildren();
    public boolean isCollidingWith(CollisionShape cs);
    public CollisionShapeType getType();


}
