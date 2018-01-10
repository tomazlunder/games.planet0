package com.mygdx.zegame.java.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.zegame.java.sound.SoundSingleton;

import java.awt.*;

public class SettingBar {
    Texture bgTex;
    Texture barTex;

    private float x,y,width,height;

    private float barX, barPercent;

    private String title;
    private float minValue, maxValue;

    private BitmapFont font;

    public SettingBar(String title, float minValue, float maxValue, float percentage, float x, float y, float width, float height){
        bgTex = new Texture("menus/settings/bar_bg.png");
        barTex = new Texture("menus/settings/g11.png");

        this.title = title;
        this.minValue = minValue;
        this.maxValue = maxValue;

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        font = new BitmapFont();
        font.setColor(Color.BLACK);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        setBarX(percentage);

    }

    public void draw(SpriteBatch batch){
        batch.draw(bgTex, x, y, width, height);

        batch.draw(barTex,x, y, barX-x, height/2);

        GlyphLayout titleLayout = new GlyphLayout(font, title);
        GlyphLayout minValLayout = new GlyphLayout(font, Float.toString(minValue));

        int percentage = (int) (getPercentage() * 100);
        GlyphLayout maxValLayout = new GlyphLayout(font, Float.toString(maxValue));
        GlyphLayout valLayout = new GlyphLayout(font, Integer.toString(percentage));


        font.draw(batch, title,x, y+height);
        font.draw(batch, Float.toString(minValue), x, y + height/2 +titleLayout.height);
        font.draw(batch, Float.toString(maxValue), x + width - maxValLayout.width, y + height/2 +titleLayout.height);
        font.draw(batch, Integer.toString(percentage), x + width/2 - valLayout.width, y + height/2 +titleLayout.height);
    }

    public boolean update(float x_mouse, float y_mouse){
        if(x_mouse > x && x_mouse < x + width){
            if(y_mouse > y && y_mouse < y + height){
                if(Gdx.input.isTouched()){
                    this.barX = x_mouse;
                }
            }
        }
        return false;
    }

    public float getPercentage(){
        return ((barX - x)/width);
    }

    public void setBarX(float percentage){
        barX = x + width* percentage/100;
    }



}
