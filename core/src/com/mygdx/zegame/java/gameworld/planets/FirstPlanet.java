package com.mygdx.zegame.java.gameworld.planets;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.zegame.java.gameworld.Universe;
import com.mygdx.zegame.java.gameworld.entities.Entity;
import com.mygdx.zegame.java.gameworld.entities.moving.player.CirclePlayer;

import java.util.ArrayList;

public class FirstPlanet extends Planet {
    private Sprite spriteLU, spriteSkyDN, spriteSkyBG, spriteClouds;

    float dayNighRotation;

    int day;
    boolean dayChanged = false;



    public FirstPlanet(Universe universe) {
        super(universe,universe.getSize() / 2, universe.getSize() / 2, universe.getSize()/4);

        //Texture texLU = new Texture("world8KqLU_p2.png");
        Texture texLU = new Texture("world_LU_8K_c.png");

        texLU.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);

        spriteLU = new Sprite(texLU);




        //this.textureSky = new Texture("sprites/world/sky2.png");
        this.spriteClouds = new Sprite(new Texture("sprites/world/sky_clouds3.png"));
        this.spriteSkyBG = new Sprite(new Texture("sprites/world/sky_art1_c.png"));
        this.spriteSkyDN = new Sprite(new Texture("sprites/world/night2_c.png"));

        dayNighRotation = 0;
        spriteSkyDN.setRotation(-90);
        spriteSkyBG.setRotation(-90);
    }

    @Override
    public void update(float deltaTime){
        super.update(deltaTime);

        dayNighRotation = deltaTime*2;

        updateDay();
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {

        spriteBatch.begin();
        float universeSize = universe.getSize();
        float planetRadius = this.getRadius();

        spriteSkyBG.setSize(4*planetRadius, 4* planetRadius);
        spriteSkyBG.setPosition(circleShape.center.x - 2f*planetRadius,circleShape.center.y - 2f*planetRadius);
        spriteSkyBG.setOrigin(spriteSkyBG.getWidth()/2, spriteSkyBG.getHeight()/2);
        spriteSkyBG.rotate(dayNighRotation);

        spriteSkyBG.draw(spriteBatch);
        /*
        spriteBatch.draw(texLU, circleShape.center.x - planetRadius, circleShape.center.y, planetRadius, planetRadius);
        spriteBatch.draw(texLD, circleShape.center.x - planetRadius, circleShape.center.y - planetRadius, planetRadius, planetRadius);
        spriteBatch.draw(texRU, circleShape.center.x, circleShape.center.y, planetRadius, planetRadius);
        spriteBatch.draw(texRD, circleShape.center.x, circleShape.center.y - planetRadius, planetRadius, planetRadius);
        */
        spriteLU.setFlip(false, false);

        spriteBatch.draw(spriteLU,circleShape.center.x - planetRadius, circleShape.center.y, planetRadius, planetRadius);

        spriteLU.flip(false,true);
        spriteBatch.draw(spriteLU, circleShape.center.x - planetRadius, circleShape.center.y - planetRadius, planetRadius, planetRadius);
        spriteLU.setFlip(false, false);

        spriteLU.flip(true, false);
        spriteBatch.draw(spriteLU, circleShape.center.x, circleShape.center.y, planetRadius, planetRadius);
        spriteLU.setFlip(false, false);

        spriteLU.flip(true, true);
        spriteBatch.draw(spriteLU, circleShape.center.x, circleShape.center.y - planetRadius, planetRadius, planetRadius);
        spriteLU.setFlip(false, false);

        //spriteBatch.end();

        //PLAYERS ARE DRAWN LAST JUST IN CASE
        ArrayList<Entity> saveForLast = new ArrayList<Entity>();
        for(Entity e : entities){
            if(e instanceof CirclePlayer){
                saveForLast.add(e);
                continue;
            }
            if(e.isVisible()) {
                e.draw(spriteBatch);
            }
        }

        for(Entity e : saveForLast){
            if(e.isVisible()){
                e.draw(spriteBatch);
            }
        }

        spriteClouds.setAlpha(0.5f);

        spriteClouds.setSize(4*planetRadius, 4* planetRadius);
        spriteClouds.setPosition(circleShape.center.x - 2f*planetRadius,circleShape.center.y - 2f*planetRadius);
        spriteClouds.setOrigin(spriteSkyDN.getWidth()/2, spriteSkyDN.getHeight()/2);
        spriteClouds.rotate(dayNighRotation/3);

        spriteSkyDN.setAlpha(0.8f);

        spriteSkyDN.setSize(4*planetRadius, 4* planetRadius);
        spriteSkyDN.setPosition(circleShape.center.x - 2f*planetRadius,circleShape.center.y - 2f*planetRadius);
        spriteSkyDN.setOrigin(spriteSkyDN.getWidth()/2, spriteSkyDN.getHeight()/2);
        spriteSkyDN.rotate(dayNighRotation);

        dayNighRotation = 0;

        //spriteBatch.begin();

        spriteClouds.draw(spriteBatch);
        spriteSkyDN.draw(spriteBatch);
        spriteBatch.end();



    }

    @Override
    public void draw(ShapeRenderer shapeRenderer){
        float universeSize = universe.getSize();
        float planetRadius = this.getRadius();
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(0,0, universeSize, universeSize);
        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.circle(circleShape.center.x, circleShape.center.y, planetRadius,1000);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.circle(circleShape.center.x, circleShape.center.y, 5,1000);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.circle(circleShape.center.x, circleShape.center.y, planetRadius);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Point);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.point(circleShape.center.x, circleShape.center.y, 0);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect((circleShape.center.x)-5, circleShape.center.x + planetRadius, 10, 50);
        shapeRenderer.end();

        for(Entity e : entities){
            e.draw(shapeRenderer);
        }
    }

    public float getDayAngle(){
        return spriteSkyDN.getRotation() + 180;
    }

    public void updateDay(){
        if(getDayAngle() < 10 && dayChanged){
            dayChanged = false;
        }

        if(getDayAngle() > 90 && !dayChanged){
            day++;
            dayChanged = true;
        }
    }

    public int getDay(){
        return day;
    }


}
