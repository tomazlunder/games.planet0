package com.mygdx.zegame.java.physics.collision.shapes;

import com.badlogic.gdx.math.Vector2;

public class CircleShape extends BasicShape {
    public Vector2 center;
    public float radius;

    public CircleShape(float x, float y, float r){
        this.center = new Vector2(x,y);
        this.radius = r;

        this.type = CollisionShapeType.CIRCLE;
    }

    @Override
    public boolean isCollidingWith(CollisionShape cs) {
        if(cs.getType()==CollisionShapeType.CIRCLE){
            CircleShape cs2 = (CircleShape) cs;
            return (center.cpy().sub(cs2.center).len() < this.radius +cs2.radius);
        }
        return false;
    }

    public void updatePosition(Vector2 v) {
        this.center = v;
    }
}
