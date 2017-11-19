package com.mygdx.zegame.java;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.zegame.java.objects.moving.player.CirclePlayer;
import com.mygdx.zegame.java.playercontrollers.CirclePlayerController;

public class GameTest extends ApplicationAdapter {

    private final int DRAW_MODE = 0;

    private final int WORLD_SIZE = 10000;
    private final int CAM_SPEED = 3;
    private final float CAM_ROT_SPEED = 0.5f;
    private final float CAM_FOLLOW_MAX_DIST = 20;
    private OrthographicCamera cam;

    /*
     * DRAWING
     */
    private int drawMode;
    // Mode 0 - Sprites
    private SpriteBatch spriteBatch;
    private Sprite worldSprite;
    private Sprite bgSprite;

    //Mode 1 - Shapes
    private ShapeRenderer shapeRenderer;

    /*
     * CAMERa
     */
    CameraType cameraType;
    int camChangeTimeout = 0;



    /*
     * MAIN OBJECTS AND VARIABLES
     */
    private World world;
    private CirclePlayer circlePlayer;
    private CirclePlayerController cpc;
    int tick;

    /**
     * Called when the game launches. Initializes main objects.
     */
    @Override
    public void create () {
        tick = 1;
        //TODO: Drawing controller
        drawMode = DRAW_MODE;

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        //Init drawing tool
        if(drawMode == 0){initSpriteDraw();}
        else if(drawMode == 1){initSimpleDraw();}

        //Create main objects
        world = new World(WORLD_SIZE);
        circlePlayer = new CirclePlayer(WORLD_SIZE/2, WORLD_SIZE/2, world.getRadious(),20);
        cpc = new CirclePlayerController(circlePlayer);

        //Init camera
        cameraType = CameraType.FREE;
        cam = new OrthographicCamera(WORLD_SIZE,WORLD_SIZE * (h/w));
        cam.position.set(cam.viewportWidth , cam.viewportHeight , 0);
        cam.update();
    }

    public void initSpriteDraw(){
        spriteBatch = new SpriteBatch();
    }

    public void initSimpleDraw(){
        shapeRenderer = new ShapeRenderer();
    }


    @Override
    public void render () {
        tick++;
        handleInputs();
        cpc.updatePlayer();

        cam.update();
        //shapeRenderer.setProjectionMatrix(cam.combined);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(drawMode == 0){drawSprite();}
        if(drawMode == 1){drawSimple();}


        if(cameraType == CameraType.PLAYER) {
            centerCameraOnPlayer();
        }
        //System.out.println(circlePlayer.toString());
        /**
         * TEST
         */
        if(tick%100000 == 0){
            System.out.println(circlePlayer.getRotationFromCenter());
        }
    }

    public void drawSprite(){
        spriteBatch.setProjectionMatrix(cam.combined);

        world.draw(spriteBatch);
        circlePlayer.draw(spriteBatch);
    }

    public void drawSimple(){
        shapeRenderer.setProjectionMatrix(cam.combined);

        world.draw(shapeRenderer);
        circlePlayer.draw(shapeRenderer);
        shapeRenderer.end();
    }



    @Override
    public void dispose () {
        if(shapeRenderer != null){
            shapeRenderer.dispose();
        }
        if(spriteBatch != null){
            spriteBatch.dispose();
        }
    }


    private void centerCameraOnPlayer(){
        cam.up.set(0, -1, 0);
        cam.position.set(circlePlayer.getCenterX(), circlePlayer.getCenterY(), 0);
        cam.rotate(-circlePlayer.getRotationFromCenter() - 90);
        cam.zoom = 0.03f;
    }

    private void followCamera(){
        Vector2 camVec = Commons.vec3to2(cam.position);
        Vector2 diff =camVec.cpy().sub(new Vector2(circlePlayer.getCenterX(),circlePlayer.getCenterY()));
        float lenght = diff.len();
        if(lenght > 0){
            if(lenght<CAM_FOLLOW_MAX_DIST){
                //camVec.add(diff.)
            }
            else{

            }
        }
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
        if(Gdx.input.isTouched()){
            System.out.printf("Clicked: ["+Gdx.input.getX()+", "+Gdx.input.getY()+"]\n");
        }

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

