package com.mygdx.zegame.java;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.zegame.java.commons.Commons;
import com.mygdx.zegame.java.enemies.rocket.ERocket;
import com.mygdx.zegame.java.enemies.roller.Roller;
import com.mygdx.zegame.java.gameworld.Universe;
import com.mygdx.zegame.java.gameworld.entities.Entity;
import com.mygdx.zegame.java.gameworld.entities.moving.player.CirclePlayer;
import com.mygdx.zegame.java.gameworld.entities.nonmoving.*;
import com.mygdx.zegame.java.gameworld.planets.Planet;
import com.mygdx.zegame.java.physics.collision.shapes.CircleShape;
import com.mygdx.zegame.java.physics.collision.shapes.CollisionShape;
import com.mygdx.zegame.java.weapons.ammo.AmmoEnum;
import com.mygdx.zegame.java.weapons.ammo.Bullet;

import java.util.List;

public class CollisionHandler {

    /*
     * COLLISION CONSEQUENCES
     */
    public static void handleCollision(Entity e1, Entity e2){
        //CIRCLE PLAYER IS COLIDING WITH ...
        if(e1 instanceof CirclePlayer){
            //ROLLER
            if(e2 instanceof Roller){
                ((CirclePlayer) e1).squash();
                System.out.printf("Action: Squash.\n");
                return;
            }
            //PICKUP SHIELD
            if(e2 instanceof PickupShield){
                e2.removeFromPlanet();
                ((CirclePlayer) e1).gainShield();
                return;
            }

            if(e2 instanceof PickupAmmo){
                e2.removeFromPlanet();
                AmmoEnum ammoType = ((CirclePlayer) e1).weapons[((CirclePlayer) e1).selectedWeapon].ammoType;
                ((CirclePlayer) e1).addAmmo(ammoType, 4);
                return;
            }

            if(e2 instanceof PickupFuel){
                e2.removeFromPlanet();
                ((CirclePlayer) e1).refuel();
                return;
            }

            if(e2 instanceof ERocket){
                e2.removeFromPlanet();

                ((CirclePlayer) e1).takeDamage(20);
                System.out.printf("Action: Damage.\n");
                return;
            }
            if(e2 instanceof CircleFire){
                ((CirclePlayer) e1).takeDamage(0.1f);
            }
        }

        //BULLET IS COLIDING WITH ...
        else if (e1 instanceof Bullet){
            //Any Living entity that is not the shooter
            if(e2 instanceof Living && !e2.equals(((Bullet) e1).getShooter())){
                ((Living) e2).takeDamage(((Bullet) e1).getDamage());
                System.out.printf("Action: Damage.\n");

                //If the roller was killed by a human player, he gets points!
                Entity shooter = ((Bullet) e1).getShooter();
                if(((Living) e2).getHealth() <= 0 && shooter instanceof CirclePlayer){
                    ((CirclePlayer)shooter).addScore(10);
                    e2.calculateRotationFromCenter();
                    spawnRandomPickup(e2.getNearestPlanet(), e2.getRotationFromCenter());


                }


                e1.removeFromPlanet();
                return;
            }
        }

        if(e1 instanceof JetpackDeathzone){
            //ROLLER
            if(e2 instanceof Living && !(e2 instanceof CirclePlayer)){
                ((Living) e2).takeDamage(3);
                if(((Living) e2).getHealth() <= 0){
                    Entity source = ((JetpackDeathzone) e1).source;
                    if(source instanceof CirclePlayer){
                        ((CirclePlayer) source).addScore(10);
                        e2.calculateRotationFromCenter();
                        spawnRandomPickup(e2.getNearestPlanet(), e2.getRotationFromCenter());
                    }
                }
            }

        }

        //System.out.printf("No action.\n");
    }

    /*
     * DRAWING OPTIMIZATION - ONLY ENTITIES COLIDING WITH CAMERA ARE TO BE DRAWN
     */
    public static void setEntitiyVisibility(OrthographicCamera camera, List<Entity> entities){
        Vector2 center = Commons.vec3to2(camera.unproject(new Vector3(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2,0)));

        float w = Commons.vec3to2(camera.unproject(new Vector3(Gdx.graphics.getWidth(), 0,0))).x - Commons.vec3to2(camera.unproject(new Vector3(0, 0,0))).x;
        float h = Commons.vec3to2(camera.unproject(new Vector3(0, Gdx.graphics.getHeight(),0))).x - Commons.vec3to2(camera.unproject(new Vector3(0, 0,0))).x;


        float radius = (float) Math.sqrt((w*w)+(h*h));
        CollisionShape cameraShape = new CircleShape(center.x,center.y,radius);


        for(Entity e : entities){
            if(cameraShape.isCollidingWith(e.getCollisionShape())){
                e.setVisibility(true);
            }
        }
    }

    public static void spawnRandomPickup(Planet planet, float rotation){
        double random = Commons.randomWithRange(0,1);
        if(random >= 0 && random <= 0.8){
            new PickupAmmo(rotation, planet);
        }
        else if(random > 0.8 && random < 0.9){
            new PickupShield(rotation,planet);
        } else {
            new PickupFuel(rotation, planet);
        }
    }
}
