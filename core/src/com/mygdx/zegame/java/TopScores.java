package com.mygdx.zegame.java;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.mygdx.zegame.java.settings.DefaultVolumeSettings;

import java.util.ArrayList;


public class TopScores {


    public ArrayList<String> names;
    public ArrayList<Integer> scores;
    Preferences prefs;

    public TopScores(){
        prefs = Gdx.app.getPreferences("fri.tomazlunder.planet0.settings.scores");

        names = new ArrayList<String>();
        scores = new ArrayList<Integer>();
    }

    public void getScoresFromFile(){

        for(int i = 0; i < 10; i++){
            names.add(prefs.getString("top"+(i+1)+"name", "/"));
        }

        for(int i = 0; i < 10; i++){
            scores.add(prefs.getInteger("top"+(i+1)+"score", 0));
        }
    }

    public String getPlacement(int score){
        String placement = "Not in top 10.";
        for(int i = 0; i < 10; i++){
            if(score > scores.get(i)){
                placement = "" + (i+1);
                return placement;
            }
        }
        return placement;
    }

    public void addNameScore(String name, int score){
        int place = -1;

        for(int i = 0; i < 10; i++){
            if(score > scores.get(i)){
                place = i+1;
                break;
            }
        }

        if(place != -1){
            names.add(place-1, name);
            scores.add(place-1,score);
        }
    }

    public void saveScores(){
        for(int i = 0; i < 10; i++){
            prefs.putString("top"+(i+1)+"name", names.get(i));
        }

        for(int i = 0; i < 10; i++){
            prefs.putInteger("top"+(i+1)+"score", scores.get(i));
        }

        prefs.flush();

    }

}
