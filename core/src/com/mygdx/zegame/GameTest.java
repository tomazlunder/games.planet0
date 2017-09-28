package com.mygdx.zegame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.zegame.Objects.moving.player.CirclePlayer;
import com.mygdx.zegame.Old.Player;
import com.mygdx.zegame.PlayerControllers.CirclePlayerController;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.zegame.Old.Player;
import com.mygdx.zegame.Objects.moving.player.CirclePlayer;
import com.mygdx.zegame.PlayerControllers.CirclePlayerController;

public class GameTest extends ApplicationAdapter {

    private final int WORLD_SIZE = 10000;
    private final int CAM_SPEED = 3;
    private final float CAM_ROT_SPEED = 0.5f;
    private OrthographicCamera cam;

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    private Sprite worldSprite;
    private Sprite bgSprite;
    private World world;
    private CirclePlayer circlePlayer;
    private CirclePlayerController cpc;
    private Player player;
    private SimpleObstacle so;

    boolean leftPressed = false;
    boolean rightPressed = false;
    int camChangeTimeout = 0;

    CameraType cameraType;

    int tick;

    @Override
    public void create () {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        cameraType = CameraType.FREE;
        shapeRenderer = new ShapeRenderer();

        world = new World(WORLD_SIZE);
        circlePlayer = new CirclePlayer(WORLD_SIZE/2, WORLD_SIZE/2, world.getRadious(),20);
        cpc = new CirclePlayerController(circlePlayer);

        cam = new OrthographicCamera(WORLD_SIZE,WORLD_SIZE * (h/w));
        cam.position.set(cam.viewportWidth , cam.viewportHeight , 0);
        cam.update();

        tick = 1;
    }

    @Override
    public void render () {
        handleInputs();
        cpc.updatePlayer();

        cam.update();
        shapeRenderer.setProjectionMatrix(cam.combined);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



        world.drawWorldSimple(shapeRenderer);
        circlePlayer.drawSimple(shapeRenderer);

        if(cameraType == CameraType.PLAYER) {
            centerCameraOnPlayer();
        }
        System.out.println(circlePlayer.toString());
    }

    @Override
    public void dispose () {
        shapeRenderer.dispose();
    }


    private void centerCameraOnPlayer(){
        cam.up.set(0, -1, 0);
        cam.position.set(circlePlayer.getCenterX(), circlePlayer.getCenterY(), 0);
        cam.rotate(-circlePlayer.getRotationFromCenter() - 90);
        cam.zoom = 0.03f;
    }

    private void handleInputs(){
        handleUniversalInputs();
        if(cameraType == CameraType.PLAYER){
            //handlePlayerInputs();
            cpc.handlePlayerInputs();
            //circlePlayer.calcMoveVector(1,0,false);
        }
        else if(cameraType == CameraType.FREE){
            handleFreeInputs();
        }
    }

    private void handleFreeInputs() {
        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            cam.zoom += 0.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.F)) {
            cam.zoom -= 0.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            cam.translate(-CAM_SPEED, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            cam.translate(CAM_SPEED, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            cam.translate(0, -CAM_SPEED, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            cam.translate(0, CAM_SPEED, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            cam.rotate(-CAM_ROT_SPEED, 0, 0, 1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            cam.rotate(CAM_ROT_SPEED, 0, 0, 1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            centerCameraOnPlayer();
        }

        cam.zoom = MathUtils.clamp(cam.zoom, 0.001f, WORLD_SIZE/cam.viewportWidth);

        float effectiveViewportWidth = cam.viewportWidth * cam.zoom;
        float effectiveViewportHeight = cam.viewportHeight * cam.zoom;

        cam.position.x = MathUtils.clamp(cam.position.x, effectiveViewportWidth / 2f, WORLD_SIZE - effectiveViewportWidth / 2f);
        cam.position.y = MathUtils.clamp(cam.position.y, effectiveViewportHeight / 2f, WORLD_SIZE - effectiveViewportHeight / 2f);
    }

    private void handleUniversalInputs(){
        if(camChangeTimeout > 0) camChangeTimeout--;

        if (Gdx.input.isKeyPressed(Input.Keys.V)){
            if(camChangeTimeout == 0) {
                camChangeTimeout = 50;
                if (cameraType == CameraType.PLAYER) {
                    cameraType = CameraType.FREE;
                } else if (cameraType == CameraType.FREE) {
                    cameraType = CameraType.PLAYER;
                }
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.BACKSPACE)){
            this.dispose();
            this.create();
        }
    }
}

