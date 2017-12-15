package com.mygdx.zegame.java.physics;

import com.mygdx.zegame.java.gameworld.planets.Planet;

public class Gravity {

    //Formula: r / (d/r)^2
    public static float gravity(float distanceFromCenter, Planet planet){
        return 5*(planet.getRadius() / ((distanceFromCenter/planet.getRadius())*(distanceFromCenter/planet.getRadius())));
    }
}
