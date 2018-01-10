package com.mygdx.zegame.java.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.zegame.java.sound.SoundSingleton;

public class Button {

    private Texture texture;
    private Texture textureActive;

    private float x,y;
    private float width;
    private float height;

    public boolean isActive;
    private boolean wasActive;

    public Button(float x, float y, float width, float height, String path, String pathActive){
        this.x =x;
        this.y =y;
        this.width = width;
        this.height = height;
        this.wasActive = false;
        this.isActive = false;

        texture = new Texture(path);
        textureActive = new Texture(pathActive);
    }

    public void draw(SpriteBatch batch){
        if(this.isActive){
            batch.draw(textureActive, x, y, width, height);
            this.wasActive = true;
        }
        else{
            batch.draw(texture, x, y, width, height);
            this.wasActive = false;
        }
    }

    public boolean updateMouse(float x_mouse, float y_mouse){
        this.isActive = false;
        if(x_mouse > x && x_mouse < x + width){
            if(y_mouse > y && y_mouse < y + height){
                this.isActive = true;
                if(!this.wasActive){
                    long sid = SoundSingleton.getInstance().menuSelect.play();
                }
                if(Gdx.input.justTouched()){
                    return true;
                }
            }
        }
        return false;
    }

    public void dispose(){
        texture.dispose();
        textureActive.dispose();
    }

}
