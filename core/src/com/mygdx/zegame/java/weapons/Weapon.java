package com.mygdx.zegame.java.weapons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.zegame.java.sound.SoundSingleton;

public abstract class Weapon {
    public String name;

    public int magSize;
    public int magAmmoLeft;

    public float timeBetweenShots;
    public float timeForReload;

    public boolean reloadWastesAmmo;

    public Sprite sprite;
    public Texture icon;


    //Called with number of bullets player owns, return number of bullets owned after reload
    public int reload(int number){
        if(magAmmoLeft == magSize){
            return number;
        }

        if(reloadWastesAmmo){
            magAmmoLeft = 0;
        }
        int emptySpots = magSize - magAmmoLeft;
        if(emptySpots != 0){
            if(number >= emptySpots){
                magAmmoLeft = magSize;
                return number-emptySpots;
            }
            else{
                magAmmoLeft += number;
                return 0;
            }
        }
        return number;
    }

    private Vector2 getSpritePosition(Vector2 armCenterPos){
        armCenterPos.x-= sprite.getWidth()/2;
        armCenterPos.y-= sprite.getHeight()/2;

        return armCenterPos;
    }

    public abstract void shoot();


}
