package com.mygdx.zegame.java;

import com.mygdx.zegame.java.enemies.roller.Roller;
import com.mygdx.zegame.java.gameworld.entities.Entity;
import com.mygdx.zegame.java.gameworld.entities.moving.player.CirclePlayer;
import com.mygdx.zegame.java.weapons.ammo.Bullet;

public class CollisionHandler {

    public static void handleCollision(Entity e1, Entity e2){
        //CIRCLE PLAYER IS COLIDING WITH ...
        if(e1 instanceof CirclePlayer){
            //ROLLER
            if(e2 instanceof Roller){
                ((CirclePlayer) e1).squash();
                System.out.printf("Action: Squash.\n");
                return;
            }
        }

        //BULLET IS COLIDING WITH ...
        else if (e1 instanceof Bullet){
            //Any Living entity that is not the shooter
            if(e2 instanceof Living && !e2.equals(((Bullet) e1).getShooter())){
                ((Living) e2).takeDamage(((Bullet) e1).getDamage());
                System.out.printf("Action: Damage.\n");
                e1.removeFromPlanet();
                return;
            }
        }

        System.out.printf("No action.\n");
    }
}
