package com.mygdx.zegame.java.weapons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.zegame.java.settings.DefaultVolumeSettings;
import com.mygdx.zegame.java.sound.SoundSingleton;

public class StartGun extends Weapon {

    public StartGun(){
        this.name = "Start gun";

        this.sprite = new Sprite(new Texture("sprites/weapons/gun.png"),256,256);
        this.icon = new Texture("sprites/weapons/gun_icon.png");
        this.magSize = 7;
        this.magAmmoLeft = magSize;

        this.timeBetweenShots = 0.01f;
        this.timeForReload = 2.8f;
        this.reloadWastesAmmo = false;

        this.betweenShotsCooldown = 0;
        this.reloadCooldown = 0;
    }


    public class StartGunProjectile {

    }

    public boolean shoot(){
        if(magAmmoLeft > 0 && betweenShotsCooldown <= 0 && reloadCooldown <= 0){
            System.out.println("[StartGun] boom!");
            betweenShotsCooldown = timeBetweenShots;
            magAmmoLeft --;
            SoundSingleton.getInstance().shoot.play(DefaultVolumeSettings.FX_STARTGUN_SHOT);
            return true;
        }
        return false;
    }



}
