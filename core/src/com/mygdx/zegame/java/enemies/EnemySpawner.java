package com.mygdx.zegame.java.enemies;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.zegame.java.commons.Commons;
import com.mygdx.zegame.java.enemies.rocket.ERocket;
import com.mygdx.zegame.java.enemies.roller.Roller;
import com.mygdx.zegame.java.gameworld.entities.moving.player.CirclePlayer;
import com.mygdx.zegame.java.gameworld.planets.FirstPlanet;

import java.util.List;

public class EnemySpawner {

    FirstPlanet planet;

    float TIME_BETWEEN_ROLLERS = 4;
    float TIME_BETWEEN_ROCKETS = 8;

    float timeBetweenRollers;
    float timeBetweenRockets;

    int rollersSpawned, rocketsSpawned;

    public EnemySpawner(FirstPlanet planet){
        this.planet = planet;

        this.timeBetweenRollers = TIME_BETWEEN_ROLLERS;
        timeBetweenRockets = TIME_BETWEEN_ROCKETS;
        rollersSpawned = 0;
    }

    public void update(float deltaTime, CirclePlayer cp){
        timeBetweenRollers -= deltaTime;
        timeBetweenRockets -= deltaTime;
        if(timeBetweenRollers <= 0){
            spawnRandomRoller();
            timeBetweenRollers = TIME_BETWEEN_ROLLERS;
        }

        if(timeBetweenRockets <= 0){
            spawnRandomRocket(cp);
            timeBetweenRockets = TIME_BETWEEN_ROLLERS;
        }
    }

    public void spawnRandomRoller(){
        int day = planet.getDay();

        float scaleToPlayer = (float) Commons.randomWithRange(0.75,2) + day;

        Vector2 spawnLocation = planet.getPointOnSurfaceWithRotation(planet.getDayAngle()+180, 50);

        float maxSpeed = (float) Commons.randomWithRange(4,8) + day*1.5f;

        if(rollersSpawned%2 == 0){
            maxSpeed = -maxSpeed;
        }

        new Roller(spawnLocation.x, spawnLocation.y, planet, scaleToPlayer, 40, 0, maxSpeed);

        rollersSpawned++;
    }

    public void spawnRandomRocket(CirclePlayer cp){
        int day = planet.getDay();

        float scaleToPlayer = 1;


        float maxSpeed = (float) Commons.randomWithRange(8,10) + day;

        if(rocketsSpawned%2 == 0){
            maxSpeed = -maxSpeed;
        }

        new ERocket(planet.getDayAngle()+180,300, planet, 1, 5,0, 11, cp);


        rocketsSpawned++;
    }
}
