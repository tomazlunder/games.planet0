package com.mygdx.zegame.java.weapons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class StartGun extends Weapon {

    public StartGun(){
        this.name = "Start gun";

        this.sprite = new Sprite(new Texture("sprites/weapons/gun.png"),256,256);
        this.icon = new Texture("sprites/weapons/gun_icon.png");
        this.magSize = 7;
        this.magAmmoLeft = magSize;

        this.timeBetweenShots = 0.01f;
        this.timeForReload = 0.1f;
        this.reloadWastesAmmo = false;
    }


    public class StartGunProjectile {

    }

    public void shoot(){

    }



}
