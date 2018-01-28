package com.mygdx.zegame.java.gamemodes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.zegame.java.enemies.EnemySpawner;
import com.mygdx.zegame.java.enemies.rocket.ERocket;
import com.mygdx.zegame.java.gameworld.Universe;
import com.mygdx.zegame.java.gameworld.entities.moving.player.CirclePlayer;
import com.mygdx.zegame.java.gameworld.entities.nonmoving.NorthPole;
import com.mygdx.zegame.java.gameworld.entities.nonmoving.PickupShield;
import com.mygdx.zegame.java.gameworld.planets.FirstPlanet;
import com.mygdx.zegame.java.input.Button;
import com.mygdx.zegame.java.screens.ScreenEnum;
import com.mygdx.zegame.java.screens.ScreenManager;

import java.util.ArrayList;
import java.util.List;

public class GamemodeDemo {
    public boolean gameOver;

    private Universe universe;
    FirstPlanet fp;

    private CirclePlayer circlePlayer;


    private EnemySpawner enemySpawner;

    //PAUSED SCREEN UI
    SpriteBatch hudBatch;
    BitmapFont font, fontStatus;

    BitmapFont fontAmmo, fontFuel, font22;

    private Texture playerStatusUnderlay, playerStatusOverlay, playerStatusHpBar,playerStatusArmorBar;
    private Texture inventoryUnderlay, inventoryFrame, inventoryFrameSelected;
    private Texture topUnderlay;
    private Texture texPaused;

    public List<Button> pausedButtons;

    Texture goTexture;
    public GamemodeDemo(CirclePlayer circlePlayer, Universe universe){
        this.circlePlayer = circlePlayer;

        this.universe = universe;

        //HUD
        hudBatch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.RED);

        fontAmmo = new BitmapFont();
        fontAmmo.setColor(Color.YELLOW);
        fontAmmo.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        fontFuel = new BitmapFont();
        fontFuel.setColor(Color.ORANGE);
        fontFuel.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        fontStatus = new BitmapFont();
        fontStatus.setColor(Color.BLACK);
        fontStatus.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        fontStatus.getData().setScale(1.3f);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/micross.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter pa = new FreeTypeFontGenerator.FreeTypeFontParameter();
        pa.size = 50;
        font22 = generator.generateFont(pa);
        generator.dispose();

        goTexture = new Texture("gameover.jpg");

        playerStatusUnderlay = new Texture("sprites/gui/gui_underlay.png");
        playerStatusOverlay = new Texture("sprites/gui/gui_overlay.png");
        playerStatusHpBar = new Texture("sprites/gui/gui_hp_bar.png");
        playerStatusArmorBar = new Texture("sprites/gui/gui_armor_bar.png");

        inventoryUnderlay = new Texture("sprites/gui/inventory/underlay.png");
        inventoryFrame = new Texture("sprites/gui/inventory/frame.png");
        inventoryFrameSelected = new Texture("sprites/gui/inventory/frame_sel.png");

        topUnderlay = new Texture("sprites/gui/top/top_underlay.png");

        //PAUSED
        texPaused = new Texture("menus/paused/paused_overlay.png");

        float screenH = Gdx.graphics.getHeight();
        float screenW = Gdx.graphics.getWidth();
        float buttonH = screenH / 6.75f;
        float buttonW = screenW / 4;
        float btnY = screenH/2 - buttonH/2;
        float btnX1 = screenW/3 - buttonW/2;
        float btnX2 = screenW*2/3 - buttonW/2;
        float btnX3 = screenW*2/3 - buttonH/2;

        pausedButtons = new ArrayList<Button>();
        pausedButtons.add(new Button(btnX1,btnY, buttonW, buttonH,"menus/paused/menu_btn.png","menus/paused/menu_btn_sel.png"));
        pausedButtons.add(new Button(btnX2,btnY, buttonW, buttonH,"menus/paused/resume_btn.png","menus/paused/resume_btn_sel.png"));
        pausedButtons.add(new Button(screenW - buttonH*3/2,buttonH/2, buttonH, buttonH,"menus/paused/set_ico.png","menus/paused/set_ico_sel.png"));


        //Gamemode specific
        gameOver = false;

        this.circlePlayer = circlePlayer;

        fp = (FirstPlanet) universe.planets.get(0);

        enemySpawner = new EnemySpawner(fp);

        //TEST OBJECTS
        new PickupShield(92, fp);
        new NorthPole(fp);
        new ERocket(130,300, fp, 1, 5,0, 11, circlePlayer);
        //new Roller(fp.getX()-8000, fp.getY()+fp.getRadius()+1000, fp, 2, 30, 0, 6);
    }

    public void update(float deltaTime){

        enemySpawner.update(deltaTime, circlePlayer);

        if(circlePlayer.isDead()){
            gameOver = true;
        }
    }

    public void drawHud(int objectsDrawn, int collisionChecks){
        hudBatch.begin();

        Vector3 ammoInfo = circlePlayer.getAmmoInOutofTotal();


        //DEBUG HUD ---
        /*
        int i = 0;
        i++;
        font.draw(hudBatch, "Health: " + circlePlayer.healthPoints, 20, Gdx.graphics.getHeight()-i*20);

        i++;
        font.draw(hudBatch, "Shield: " + circlePlayer.armorPoints, 20, Gdx.graphics.getHeight()-i*20);

        i++;
        font.draw(hudBatch, "FPS: "+Gdx.graphics.getFramesPerSecond(),20,Gdx.graphics.getHeight()-i*20);

        i++;
        String weaponName = (circlePlayer.getSelectedWeapon() != null) ? circlePlayer.getSelectedWeapon().name : "null";
        font.draw(hudBatch, "Weapon ["+circlePlayer.selectedWeapon +"]: " + weaponName, 20, Gdx.graphics.getHeight()-i*20);

        i++;
        font.draw(hudBatch, "DRAWING OPTIMIZATION [Drawn/All]: ["+ objectsDrawn+"/"+universe.getAllEntities().size()+"]", 20, Gdx.graphics.getHeight()-i*20);

        i++;
        Vector3 ammoInfo = circlePlayer.getAmmoInOutofTotal();
        font.draw(hudBatch, "Ammo: " + ammoInfo.x +"/"+ammoInfo.y+" | "+ammoInfo.z, 20, Gdx.graphics.getHeight()-i*20);

        i++;
        font.draw(hudBatch, "Fuel: " + circlePlayer.jetpackFuel +"/"+circlePlayer.JETPACK_FUEL_MAX, 20, Gdx.graphics.getHeight()-i*20);

        i++;
        font.draw(hudBatch, "Collision checks with QUADTREE/without: "+ collisionChecks+"/"+universe.getAllEntities().size()*universe.getAllEntities().size(), 20, Gdx.graphics.getHeight()-i*20);

        i++;
        font.draw(hudBatch, "Health: " + circlePlayer.healthPoints, 20, Gdx.graphics.getHeight()-i*20);
        */

        font22.draw(hudBatch, "SCORE: " + circlePlayer.getScore(), Gdx.graphics.getWidth()/100 * 80, Gdx.graphics.getHeight()/100f * 98f );


        //end debug hud

        //PLAYER HUD ---
        //hudBatch.draw(greyBar, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/6);

        //PLAYER STATUS
        //Width and height of the player status underlay, every other piece of the underlay is based on this
        //Underlay and overlay dimensions are the same
        float underlayWidth = Gdx.graphics.getWidth()/4;
        float underlayHeight = Gdx.graphics.getHeight()/9;

        float underlayX = Gdx.graphics.getWidth()/2 - underlayWidth/2;
        float underlayY = 0;

        //Width and height of the bars
        float hpLen = circlePlayer.healthPoints * (12 * underlayWidth/13) / 100;
        float armorLen = circlePlayer.armorPoints * (12 * underlayWidth/13) / 100;
        float barH = 3 * underlayHeight/10;

        //Text (percentages)
        GlyphLayout hpLayout = new GlyphLayout(fontStatus, "" +(int) circlePlayer.healthPoints + "%");
        GlyphLayout armorLayout = new GlyphLayout(fontStatus, "" +(int) circlePlayer.armorPoints + "%");

        //Drawing
        hudBatch.draw(playerStatusUnderlay, underlayX , underlayY, underlayWidth, underlayHeight);
        hudBatch.draw(playerStatusHpBar, underlayX  + underlayWidth/26, underlayY + underlayHeight/10,hpLen,barH);
        hudBatch.draw(playerStatusArmorBar, underlayX + underlayWidth/26, underlayY + underlayHeight/10 + underlayHeight/2, armorLen, barH);
        hudBatch.draw(playerStatusOverlay, underlayX , underlayY, underlayWidth, underlayHeight);

        //Drawing font
        fontStatus.draw(hudBatch, hpLayout, underlayX + underlayWidth/2 - hpLayout.width/2 , underlayY + underlayHeight/3);
        fontStatus.draw(hudBatch, armorLayout, underlayX + underlayWidth/2 - armorLayout.width/2 , underlayY + underlayHeight*2.5f/3);

        //PLAYER INVENTORY
        //underlay
        hudBatch.draw(inventoryUnderlay, underlayX + underlayWidth, underlayY, underlayWidth/2, underlayHeight);

        //frames (all non-selected)
        float frameWH = underlayHeight/3;
        float botRowY = underlayY+underlayHeight/100 * 10;
        float topRowY = underlayY+underlayHeight-frameWH-underlayHeight/100 * 10;
        float midColX = underlayX + underlayWidth + underlayWidth/4 - frameWH/2;

        float betweenStartAndMidX = midColX  - (underlayX + underlayWidth);
        float leftColX =  underlayX + underlayWidth + betweenStartAndMidX/2 - frameWH/2;
        float rightColX = underlayX + underlayWidth + underlayWidth/4 + betweenStartAndMidX/2;


        hudBatch.draw(inventoryFrame, midColX, botRowY, frameWH, frameWH );
        hudBatch.draw(inventoryFrame, midColX, topRowY, frameWH, frameWH );

        hudBatch.draw(inventoryFrame, leftColX, botRowY, frameWH, frameWH );
        hudBatch.draw(inventoryFrame, leftColX, topRowY, frameWH, frameWH );

        hudBatch.draw(inventoryFrame, rightColX, botRowY, frameWH, frameWH );
        hudBatch.draw(inventoryFrame, rightColX, topRowY, frameWH, frameWH );

        //active (selected)
        int wpnNumber = circlePlayer.selectedWeapon;
        float activeX, activeY;
        if(wpnNumber >= 0 && wpnNumber < 3){
            activeY = topRowY;
        }
        else{
            activeY = botRowY;
        }
        if(wpnNumber%3 == 0){
            activeX = leftColX;
        } else if(wpnNumber%3 == 1){
            activeX = midColX;
        } else {
            activeX = rightColX;
        }

        hudBatch.draw(inventoryFrameSelected, activeX, activeY, frameWH, frameWH);

        //Weapon icons
        if(circlePlayer.weapons[0] != null){
            hudBatch.draw(circlePlayer.weapons[0].icon, leftColX, topRowY, frameWH, frameWH);
        }
        if(circlePlayer.weapons[1] != null){
            hudBatch.draw(circlePlayer.weapons[1].icon, midColX, topRowY, frameWH, frameWH);
        }
        if(circlePlayer.weapons[2] != null){
            hudBatch.draw(circlePlayer.weapons[2].icon, rightColX, topRowY, frameWH, frameWH);
        }
        if(circlePlayer.weapons[3] != null){
            hudBatch.draw(circlePlayer.weapons[3].icon, leftColX, botRowY, frameWH, frameWH);
        }
        if(circlePlayer.weapons[4] != null){
            hudBatch.draw(circlePlayer.weapons[4].icon, midColX, botRowY, frameWH, frameWH);
        }
        if(circlePlayer.weapons[5] != null){
            hudBatch.draw(circlePlayer.weapons[5].icon, rightColX, botRowY, frameWH, frameWH);
        }

        //Ammo bar


        if(circlePlayer.weapons[circlePlayer.selectedWeapon] != null) {
            if(ammoInfo.x == 0){
                fontAmmo.setColor(Color.RED);
            } else {
                fontAmmo.setColor(Color.YELLOW);
            }

            fontAmmo.draw(hudBatch, "AMMO: " + (int) ammoInfo.x + "/" +(int)ammoInfo.y+ " | " + (int) ammoInfo.z, underlayX + underlayWidth/2 + underlayWidth/10, underlayY + underlayHeight/2 + underlayHeight/15);
        }


        if(circlePlayer.jetpackFuel <= 0){
            fontFuel.setColor(Color.RED);
        } else {
            fontFuel.setColor(Color.ORANGE);

        }

        int fuelPercent = Math.max((int)((circlePlayer.jetpackFuel/circlePlayer.JETPACK_FUEL_MAX) * 100), 0);
        fontFuel.draw(hudBatch, "FUEL: "+ fuelPercent +"%",underlayX + underlayWidth/10, underlayY + underlayHeight/2 + underlayHeight/15);


        //TOP BAR
        //hudBatch.draw(topUnderlay, underlayX, underlayY+underlayHeight,underlayWidth,underlayHeight/2);

        hudBatch.end();
    }

    public void drawPausedScreenAndMouseCmd(){
        hudBatch.begin();

        float screenH = Gdx.graphics.getHeight();
        float screenW = Gdx.graphics.getWidth();

        hudBatch.draw(texPaused,0,0,screenW,screenH);

        for(Button b: pausedButtons){
            b.updateMouse(Gdx.input.getX(), screenH - Gdx.input.getY());
            b.draw(hudBatch);
        }

        hudBatch.end();

        float x = Gdx.input.getX();
        float y = Gdx.input.getY();
    }
}
