package com.mygdx.zegame.java.data_structures;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.zegame.java.gameworld.entities.Entity;
import com.mygdx.zegame.java.physics.collision.shapes.CircleShape;

import java.util.ArrayList;
import java.util.List;

public class Quadtree {

    private int MAX_OBJECTS = 10;
    private int MAX_LEVELS = 5;

    private int level;
    private List<Entity> objects;
    private QRect bounds;
    private Quadtree[] nodes;

    public class QRect{
        public float x1, y1, x2, y2;
        public float w, h;

        public QRect(float x1, float y1, float x2, float y2){
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.w = x2-x1;
            this.h = y2-y1;
        }

    }



    /*
     * Constructor
     */
    public Quadtree(int pLevel, float x1, float y1, float x2,float y2) {
        level = pLevel;
        objects = new ArrayList();
        bounds = new QRect(x1, y1, x2, y2);
        nodes = new Quadtree[4];
    }

    /*
    * Clears the quadtree
    */
    public void clear() {
        objects.clear();

        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] != null) {
                nodes[i].clear();
                nodes[i] = null;
            }
        }
    }

    /*
     * Splits the node into 4 subnodes
     *
     *  2  |  1
     * ---------
     *  3  |  4
     */
    private void split() {
        int subWidth = (int)(bounds.w / 2);
        int subHeight = (int)(bounds.h / 2);
        int x = (int)bounds.x1;
        int y = (int)bounds.y1;

        nodes[0] = new Quadtree(level+1,x + subWidth, y, x + subWidth *2, y + subHeight);
        nodes[1] = new Quadtree(level+1, x, y, x+ subWidth, y + subHeight);
        nodes[2] = new Quadtree(level+1, x,y + subHeight, x + subWidth, y + subHeight*2);
        nodes[3] = new Quadtree(level+1,x + subWidth, y + subHeight, x + subWidth*2, y + subHeight*2);
    }

    /*
    * Determine which node the object belongs to. -1 means
    * object cannot completely fit within a child node and is part
    * of the parent node
    */
    private int getIndex(CircleShape cs) {
        int index = -1;
        double xMid = bounds.x1 + (bounds.w / 2);
        double yMid = bounds.y1 + (bounds.h / 2);

        boolean top, bot, left, right;
        top = (cs.center.y - cs.radius > bounds.y1 && cs.center.y + cs.radius < yMid);
        bot = (cs.center.y - cs.radius > yMid && cs.center.y + cs.radius < this.bounds.y2);

        left = (cs.center.x - cs.radius > bounds.x1 && cs.center.x + cs.radius < xMid);
        right = (cs.center.x - cs.radius > xMid && cs.center.x + cs.radius < bounds.x2);

        if(top){
            if(left)       index = 0;
            else if(right) index = 1;
        }
        else if(bot){
            if(left)       index = 2;
            else if(right) index = 3;
        }

        return index;
    }


    /*
    * Insert the object into the quadtree. If the node
    * exceeds the capacity, it will split and add all
    * objects to their corresponding nodes.
    */
    public void insert(Entity e) {
        if (nodes[0] != null) {
            int index = getIndex((CircleShape) e.getCollisionShape());

            if (index != -1) {
                nodes[index].insert(e);

                return;
            }
        }

        objects.add(e);

        if (objects.size() > MAX_OBJECTS && level < MAX_LEVELS) {
            if (nodes[0] == null) {
                split();
            }

            int i = 0;
            while (i < objects.size()) {
                int index = getIndex((CircleShape) objects.get(i).getCollisionShape());
                if (index != -1) {
                    nodes[index].insert(objects.remove(i));
                }
                else {
                    i++;
                }
            }
        }
    }

    /*
    * Return all objects that could collide with the given object
    */
    public List retrieve(List returnObjects, Entity e) {
        int index = getIndex((CircleShape) e.getCollisionShape());
        if (index != -1 && nodes[0] != null) {
            nodes[index].retrieve(returnObjects, e);
        }

        returnObjects.addAll(objects);

        return returnObjects;
    }


}
