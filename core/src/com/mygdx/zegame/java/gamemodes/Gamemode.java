package com.mygdx.zegame.java.gamemodes;

import com.mygdx.zegame.java.deprecated.Player;
import com.mygdx.zegame.java.gameworld.Universe;
import com.mygdx.zegame.java.gameworld.entities.moving.player.CirclePlayer;

public abstract class Gamemode {
    public Universe universe;
    public CirclePlayer circlePlayer;

    public abstract void update();

}
