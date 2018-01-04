package com.mygdx.zegame.java.gamemodes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.zegame.java.enemies.EnemyController;
import com.mygdx.zegame.java.gameworld.Universe;
import com.mygdx.zegame.java.gameworld.entities.Entity;
import com.mygdx.zegame.java.gameworld.entities.moving.player.CirclePlayer;
import com.mygdx.zegame.java.gameworld.entities.nonmoving.CircleFire;
import com.mygdx.zegame.java.gameworld.entities.nonmoving.PickupShield;
import com.mygdx.zegame.java.gameworld.planets.Planet;

import java.util.ArrayList;
import java.util.List;

public class GamemodeDemo {
    public List<Entity> fires, shieldPickups;
    boolean gameOver;
    private CirclePlayer circlePlayer;
    SpriteBatch hudBatch;
    BitmapFont font, fontStatus;

    private EnemyController enemyController;

    private Texture playerStatusUnderlay, playerStatusOverlay, playerStatusHpBar,playerStatusArmorBar;
    private Texture greyBar;

    private Texture inventoryUnderlay, inventoryFrame, inventoryFrameSelected;

    Planet fp;

    Texture goTexture;
    public GamemodeDemo(CirclePlayer circlePlayer, Universe universe){
        this.circlePlayer = circlePlayer;

        //HUD
        hudBatch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.RED);

        fontStatus = new BitmapFont();
        fontStatus.setColor(Color.BLACK);
        fontStatus.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        fontStatus.getData().setScale(1.3f);

        goTexture = new Texture("gameover.jpg");

        playerStatusUnderlay = new Texture("sprites/gui/gui_underlay.png");
        playerStatusOverlay = new Texture("sprites/gui/gui_overlay.png");
        playerStatusHpBar = new Texture("sprites/gui/gui_hp_bar.png");
        playerStatusArmorBar = new Texture("sprites/gui/gui_armor_bar.png");

        greyBar = new Texture("sprites/gui/grey_bar.png");

        inventoryUnderlay = new Texture("sprites/gui/inventory/underlay.png");
        inventoryFrame = new Texture("sprites/gui/inventory/frame.png");
        inventoryFrameSelected = new Texture("sprites/gui/inventory/frame_sel.png");



        //Gamemode specific
        gameOver = false;

        this.circlePlayer = circlePlayer;

        fp = universe.planets.get(0);

        fires = new ArrayList<Entity>();
        CircleFire cs = new CircleFire(fp.getX()-fp.getRadius()-5f,fp.getY(),10f,fp);
        fires.add(cs);
        fp.entities.add(cs);  //Drawn with wordld

        shieldPickups = new ArrayList<Entity>();
        PickupShield ps = new PickupShield(0, fp);
        shieldPickups.add(ps);
        fp.entities.add(ps);
    }

    public void update(float deltaTime){
        //Shield pickup
        ArrayList<Entity> toRemove = new ArrayList<Entity>();
        for(Entity e : shieldPickups){
            if(e.getBaseCollision().isCollidingWith(circlePlayer.getBaseCollision())){
                toRemove.add(e);
                fp.entities.remove(e);
                circlePlayer.gainShield();
                System.out.println("[Gamemode]: Shield removed");
            }
        }
        shieldPickups.removeAll(toRemove);

        //Collision with spikes
        for(Entity e : fires){
            if(e.getBaseCollision().isCollidingWith(circlePlayer.getBaseCollision())){
                circlePlayer.takeDamage(30*deltaTime);
            }
        }
        if(circlePlayer.isDead()){
            gameOver = true;
        }
    }

    public void drawHud(){
        hudBatch.begin();

        //DEBUG HUD ---
        font.draw(hudBatch, "Health: " + circlePlayer.healthPoints, 20, Gdx.graphics.getHeight()-20);
        font.draw(hudBatch, "Shield: " + circlePlayer.shieldPoints, 20, Gdx.graphics.getHeight()-40);
        font.draw(hudBatch, "FPS: "+Gdx.graphics.getFramesPerSecond(),20,Gdx.graphics.getHeight()-60);

        String weaponName = (circlePlayer.getSelectedWeapon() != null) ? circlePlayer.getSelectedWeapon().name : "null";
        font.draw(hudBatch, "Weapon ["+circlePlayer.selectedWeapon +"]: " + weaponName, 20, Gdx.graphics.getHeight()-80);

        //PLAYER HUD ---
        hudBatch.draw(greyBar, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/6);

        //PLAYER STATUS
        //Width and height of the player status underlay, every other piece of the underlay is based on this
        //Underlay and overlay dimensions are the same
        float underlayWidth = Gdx.graphics.getWidth()/4;
        float underlayHeight = Gdx.graphics.getHeight()/9;

        float underlayX = Gdx.graphics.getWidth()/2 - underlayWidth/2;
        float underlayY = 0;

        //Width and height of the bars
        float hpLen = circlePlayer.healthPoints * (12 * underlayWidth/13) / 100;
        float armorLen = circlePlayer.shieldPoints * (12 * underlayWidth/13) / 100;
        float barH = 3 * underlayHeight/10;

        //Text (percentages)
        GlyphLayout hpLayout = new GlyphLayout(fontStatus, "" +(int) circlePlayer.healthPoints + "%");
        GlyphLayout armorLayout = new GlyphLayout(fontStatus, "" +(int) circlePlayer.shieldPoints + "%");

        //Drawing
        hudBatch.draw(playerStatusUnderlay, underlayX , underlayY, underlayWidth, underlayHeight);
        hudBatch.draw(playerStatusHpBar, underlayX  + underlayWidth/26, underlayY + underlayHeight/10,hpLen,barH);
        hudBatch.draw(playerStatusArmorBar, underlayX + underlayWidth/26, underlayY + underlayHeight/10 + underlayHeight/2, armorLen, barH);
        hudBatch.draw(playerStatusOverlay, underlayX , underlayY, underlayWidth, underlayHeight);

        //Drawing font
        fontStatus.draw(hudBatch, hpLayout, underlayX + underlayWidth/2 - hpLayout.width/2 , underlayY + underlayHeight/3);
        fontStatus.draw(hudBatch, armorLayout, underlayX + underlayWidth/2 - armorLayout.width/2 , underlayY + underlayHeight*2.5f/3);

        //PLAYER INVENTORY

        hudBatch.draw(inventoryUnderlay, underlayX + underlayWidth, underlayY, underlayWidth/2, underlayHeight);

        hudBatch.end();
    }



}
