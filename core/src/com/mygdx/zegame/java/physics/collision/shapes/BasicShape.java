package com.mygdx.zegame.java.physics.collision.shapes;

import java.util.ArrayList;
import java.util.List;

public abstract class BasicShape implements CollisionShape{
    protected List<CollisionShape> children;
    protected CollisionShapeType type;

    @Override
    public boolean hasParent() {
        return false;
    }

    @Override
    public boolean hasChildren(){
        return !children.isEmpty();
    }

    @Override
    public List<CollisionShape> getChildren() {
        return children;
    }

    @Override
    public boolean isCollidingWith(CollisionShape cs){
        return false;
    }

    @Override
    public CollisionShapeType getType() {
        return type;
    }
}
