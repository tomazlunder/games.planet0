package com.mygdx.zegame.java.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.zegame.java.CameraType;
import com.mygdx.zegame.java.CollisionHandler;
import com.mygdx.zegame.java.GameClass;
import com.mygdx.zegame.java.commons.Commons;
import com.mygdx.zegame.java.constants.Constants;
import com.mygdx.zegame.java.data_structures.Quadtree;
import com.mygdx.zegame.java.gamemodes.GamemodeDemo;
import com.mygdx.zegame.java.gameworld.Universe;
import com.mygdx.zegame.java.gameworld.entities.Entity;
import com.mygdx.zegame.java.gameworld.entities.moving.player.CirclePlayer;
import com.mygdx.zegame.java.gameworld.planets.FirstPlanet;
import com.mygdx.zegame.java.gameworld.planets.Planet;
import com.mygdx.zegame.java.input.InputProcessorWS;
import com.mygdx.zegame.java.physics.collision.shapes.CircleShape;
import com.mygdx.zegame.java.physics.collision.shapes.CollisionShape;
import com.mygdx.zegame.java.sound.SoundSingleton;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen, MouseWheelListener{

    private GameClass game;
    private boolean isPaused;
    boolean isLoaded;


    //Drawing
    private int drawMode;

    //Camera
    private OrthographicCamera cam;
    private CameraType cameraType;

    float WALKING_ZOOM = 57;
    float JETPACK_ZOOM = 65;
    float ZOOM_STEP = 0.05f;
    float ZOOM_MAX_HEIGHT_ADDITON = 8;
    float zoom;

    //Main game objects
    private Universe universe;
    private CirclePlayer circlePlayer;
    private GamemodeDemo gamemodeDemo;

    private int tick;

    private Quadtree quad;

    int numberOfEntitiesDrawn;
    int collisionChecks;


    //SpriteBatch for drawing the HUD
    SpriteBatch hudBatch;



    public GameScreen(GameClass game) {
        this.game = game;
    }

    @Override
    public void show() {
        isLoaded = false;
        Pixmap pm = new Pixmap(Gdx.files.internal("sprites/cursors/cursor1.png"));
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, pm.getWidth()/2, pm.getHeight()/2));
        pm.dispose();

        isPaused = false;
        tick = 0;
        drawMode = Constants.DEFAULT_DRAW_MODE;

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        //Create gameworld
        universe = new Universe(Constants.DEFAULT_UNIVERSE_SIZE);
        Planet firstPlanet = new FirstPlanet(universe);
        universe.planets.add(firstPlanet);
        circlePlayer = new CirclePlayer(Constants.DEFAULT_PLAYER_SIZE, firstPlanet, cam);

        //Init camera
        cameraType = CameraType.PLAYER;
        cam = new OrthographicCamera(Constants.DEFAULT_UNIVERSE_SIZE, Constants.DEFAULT_UNIVERSE_SIZE * (h / w));
        cam.position.set(cam.viewportWidth, cam.viewportHeight, 0);
        cam.update();
        zoom = WALKING_ZOOM;

        //Init gamemode
        gamemodeDemo = new GamemodeDemo(circlePlayer, universe);


        //Init main sound track
        SoundSingleton.getInstance().gameMusic.play();

        Gdx.input.setInputProcessor(new InputProcessorWS(circlePlayer));
        System.out.println("[GameScreen] Screen and game have loaded.");
        isLoaded = true;

        quad = new Quadtree(0, 0,0,universe.getSize(), universe.getSize());

        numberOfEntitiesDrawn = 0;
        collisionChecks = 0;
    }

    @Override
    public void render(float delta) {
        if(gamemodeDemo.gameOver){
            game.score = circlePlayer.getScore();
            ScreenManager.getInstance().showScreen(ScreenEnum.GAMEOVER);
        }

        float deltaTime = Gdx.graphics.getDeltaTime();

        //0. clearing the collision tree
        quad.clear();

        //1. INPUTS
        handleInputs();

        //2. UPDATE
        if(!isPaused) {
            collisionDetectionAndHandling();

            drawingOptimizationPrep();

            gamemodeDemo.update(deltaTime);
            universe.update(deltaTime);
            cam.update();

            drawingOptimization();
        }



        //3. DRAW
        draw();

        //4. CAMERA UPDATE
        updateCameraPostition();

        //5. MISC
        miscTasks();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.dispose();
    }

    @Override
    public void dispose() {
        SoundSingleton.getInstance().gameMusic.stop();
        SoundSingleton.getInstance().jetpack.stop();
    }

    /*
     * COLLISION DETECTION AND DRAWING OPTIMISATION (Only draws what camera can see)
     */
    public void collisionDetectionAndHandling() {
        //Inserting all objects into collision tree
        for (Entity e : universe.getAllEntities()) {
            if (e.getCollision()) {
                quad.insert((e));
            }
        }

        collisionChecks = 0;
        //Possible collision detection
        List<Entity> returnObjects = new ArrayList();
        for (Entity e1 : universe.planets.get(0).entities) {
            returnObjects.clear();
            quad.retrieve(returnObjects, e1);
            returnObjects.remove(e1);

            for (Entity e2 : returnObjects) {
                collisionChecks++;
                //If collision is possible, do actual collision detection
                if (e1.getCollisionShape().isCollidingWith(e2.getCollisionShape())) {
                    //System.out.printf("[QUADTREE] Collision detected:" + e1.getName() + " - " + e2.getName() + ". ");
                    CollisionHandler.handleCollision(e1,e2);
                }
            }
        }
    }

    public void drawingOptimizationPrep(){
        for(Entity e : universe.getAllEntities()){
            e.setVisibility(false);
        }
    }

    public void drawingOptimization(){
        Vector2 center = Commons.vec3to2(cam.unproject(new Vector3(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2,0)));

        float w = Commons.vec3to2(cam.unproject(new Vector3(Gdx.graphics.getWidth(), 0,0))).x - Commons.vec3to2(cam.unproject(new Vector3(0, 0,0))).x;
        float h = Commons.vec3to2(cam.unproject(new Vector3(0, Gdx.graphics.getHeight(),0))).x - Commons.vec3to2(cam.unproject(new Vector3(0, 0,0))).x;


        float radius = (float) Math.sqrt((w*w)+(h*h));
        CollisionShape cameraShape = new CircleShape(center.x,center.y,radius);

        numberOfEntitiesDrawn = 0;

        for(Entity e : universe.getAllEntities()){
            if(cameraShape.isCollidingWith(e.getCollisionShape())){
                e.setVisibility(true);
                numberOfEntitiesDrawn++;
            }
        }
    }


    /*
     * DRAWING RELATED FUNCTIONS
     */
    private void draw(){
        //Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Draw the world depending on drawMode (Sprite/Shape)
        if (drawMode == 0) {
            drawSprite();
        }
        else if (drawMode == 1) {
            drawSimple();
        }

        if(isPaused){
            gamemodeDemo.drawPausedScreenAndMouseCmd();
        } else {
            gamemodeDemo.drawHud(numberOfEntitiesDrawn, collisionChecks);
        }
    }

    private void drawSprite() {
        game.spriteBatch.setProjectionMatrix(cam.combined);

        universe.draw(game.spriteBatch);
    }

    private void drawSimple() {
        game.shapeRenderer.setProjectionMatrix(cam.combined);

        universe.draw(game.shapeRenderer);
        circlePlayer.draw(game.shapeRenderer);
    }

    private void switchDrawMode() {
        if (drawMode == 0) {
            //game.spriteBatch.dispose();
            drawMode = 1;
        } else if (drawMode == 1) {
            //game.shapeRenderer.dispose();
            drawMode = 0;
        }
    }



    /*
     * CAMERA RELATED FUNCTIONS
     */

    private void updateCameraPostition(){
        if (cameraType == CameraType.PLAYER) {
            //centerCameraOnPlayer();
            cameraFollowSmooth();
        }
    }

    private void centerCameraOnPlayer() {
        cam.up.set(0, -1, 0);
        cam.position.set(circlePlayer.getX(), circlePlayer.getY(), 0);
        cam.rotate(-circlePlayer.getRotationFromCenter() - 90);
        cam.zoom = ((Constants.DEFAULT_PLAYER_SIZE * 40f) / (float) Constants.DEFAULT_UNIVERSE_SIZE);
    }

    private void cameraFollowSmooth() {
        //Cur possition
        Vector2 camVec = Commons.vec3to2(cam.position);
        Vector2 newVec = circlePlayer.getPosition().sub(camVec).scl(0.2f);

        cam.position.x += newVec.x;
        cam.position.y += newVec.y;

        //cam vec repurposed to increase camera heigh from ground
        camVec.x = cam.position.x;
        camVec.y = cam.position.y;

        Vector2 planetVec = circlePlayer.getNearestPlanet().getPosition();
        camVec.x -= planetVec.x;
        camVec.y -= planetVec.y;
        camVec.setLength(20);

        cam.position.x += camVec.x;
        cam.position.y += camVec.y;



        if(circlePlayer.jetpackOn){
            if(zoom < JETPACK_ZOOM) zoom+=ZOOM_STEP;
        } else{
            if(zoom > WALKING_ZOOM) zoom-= ZOOM_STEP;
        }

        zoom += ZOOM_MAX_HEIGHT_ADDITON * circlePlayer.getPercentageHeight();

        cam.up.set(0, -1, 0);
        cam.rotate(-circlePlayer.getRotationFromCenter() - 90);
        cam.zoom = ((Constants.DEFAULT_PLAYER_SIZE * zoom) / (float) Constants.DEFAULT_UNIVERSE_SIZE);

        zoom -= ZOOM_MAX_HEIGHT_ADDITON * circlePlayer.getPercentageHeight();

    }

    /*
     * INPUT RELATED FUNCTIONS
     */
    private void handleInputs() {
        if(isLoaded){
            if(isPaused){
                handlePausedInputs();
            }
            else {

                handleGameUniversalInputs();

                if (cameraType == CameraType.PLAYER) {
                    handlePlayerInputs();
                } else if (cameraType == CameraType.FREE) {
                    handleFreeInputs();
                }
            }
        }
    }

    private void handleFreeInputs() {
        if (Gdx.input.justTouched()) {
            float x =  Gdx.input.getX();
            float y =  Gdx.input.getY();
            System.out.printf("Clicked: [" + x + ", " + y + "]  " + cam.unproject(new Vector3(x, y, 0)) +"\n");

        }

        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            cam.zoom += 0.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.F)) {
            cam.zoom -= 0.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            cam.translate(-Constants.DEFAULT_CAM_SPEED, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            cam.translate(Constants.DEFAULT_CAM_SPEED, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            cam.translate(0, -Constants.DEFAULT_CAM_SPEED, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            cam.translate(0, Constants.DEFAULT_CAM_SPEED, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            cam.rotate(-Constants.DEDAULT_CAM_ROT_SPEED, 0, 0, 1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            cam.rotate(Constants.DEDAULT_CAM_ROT_SPEED, 0, 0, 1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            centerCameraOnPlayer();
        }

        cam.zoom = MathUtils.clamp(cam.zoom, 0.000001f, Constants.DEFAULT_UNIVERSE_SIZE / cam.viewportWidth);

        float effectiveViewportWidth = cam.viewportWidth * cam.zoom;
        float effectiveViewportHeight = cam.viewportHeight * cam.zoom;

        cam.position.x = MathUtils.clamp(cam.position.x, effectiveViewportWidth / 2f, Constants.DEFAULT_UNIVERSE_SIZE - effectiveViewportWidth / 2f);
        cam.position.y = MathUtils.clamp(cam.position.y, effectiveViewportHeight / 2f, Constants.DEFAULT_UNIVERSE_SIZE - effectiveViewportHeight / 2f);
    }

    private void handleGameUniversalInputs() {
        //Changes camera mode [PLAYER/FREE]
        /*
        if (Gdx.input.isKeyJustPressed(Input.Keys.V)) {
            if (cameraType == CameraType.PLAYER) {
                cameraType = CameraType.FREE;
            } else if (cameraType == CameraType.FREE) {
                cameraType = CameraType.PLAYER;
                centerCameraOnPlayer();
            }
        }

        //Switch draw mode [SPRITE/SHAPE]
        if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            switchDrawMode();
        }


        //Return to main menu
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            isPaused = true;
        }
        */
    }

    private void handlePausedInputs(){
        if(Gdx.input.isKeyJustPressed(Input.Keys.P)){
            isPaused = false;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU);
        }

        if(Gdx.input.justTouched()) {
            for (int i = 0; i < gamemodeDemo.pausedButtons.size(); i++) {
                if (gamemodeDemo.pausedButtons.get(i).isActive) {
                    switch (i) {
                        case 0:
                            ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU);
                            break;
                        case 1:
                            isPaused = false;
                            break;
                        case 2:
                            ScreenManager.getInstance().pauseAndOpen(ScreenEnum.SETTINGS);
                            break;
                    }
                }
            }
        }
    }

    private void handlePlayerInputs() {
        //HANDLE PLAYER INPUTS MUST BE CALLED BEFORE UPDATING THE PLAYER, SO THESE SETTINGS ARE USED (movingLeft, movingRight, jumping)
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)){
            circlePlayer.squash();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D)){
            circlePlayer.movingLeft = true;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D) && !Gdx.input.isKeyPressed(Input.Keys.A)) {
            circlePlayer.movingRight = true;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)){
            circlePlayer.movingUp = true;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            circlePlayer.jumping = true;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)){
            circlePlayer.movingDown = true;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)){
            circlePlayer.reload();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)){
            circlePlayer.toggleJetpack();
        }

        if (Gdx.input.justTouched()){
            if(circlePlayer.aimingAt.dst(new Vector3(circlePlayer.getX(), circlePlayer.getY(), circlePlayer.aimingAt.z)) > circlePlayer.getRadius() * Constants.GUN_TIP_CONST) {
                circlePlayer.fireWeapon();
            }
        }

        /*
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_9)){
            game.score = circlePlayer.getScore();
            ScreenManager.getInstance().showScreen(ScreenEnum.GAMEOVER);

        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_8)){
            circlePlayer.addScore(5 );
        }
        */

        Vector3 cursorPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        Vector3 aimingAt = cam.unproject(cursorPos);
        circlePlayer.aimingAt = aimingAt;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(isLoaded) {
            System.out.println("[CPC] Scolled!");
            if (e.getWheelRotation() > 0) {
                circlePlayer.nextWeapon();
            } else {
                circlePlayer.prevWeapon();
            }
        }
    }

    /*
     * MISC
     */
    private void miscTasks(){
        tick++;
        if (tick % 100 == 0) {
            //System.out.println(circlePlayer.toString());
            //logger.log();
        }
    }

}
