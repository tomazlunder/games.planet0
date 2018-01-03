package com.mygdx.zegame.java.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.zegame.java.CameraType;
import com.mygdx.zegame.java.GameClass;
import com.mygdx.zegame.java.commons.Commons;
import com.mygdx.zegame.java.constants.Constants;
import com.mygdx.zegame.java.gamemodes.GamemodeDemo;
import com.mygdx.zegame.java.gameworld.Universe;
import com.mygdx.zegame.java.gameworld.entities.moving.player.CirclePlayer;
import com.mygdx.zegame.java.gameworld.planets.FirstPlanet;
import com.mygdx.zegame.java.gameworld.planets.Planet;
import com.mygdx.zegame.java.playercontrollers.CirclePlayerController;
import com.mygdx.zegame.java.sound.SoundSingleton;

public class GameScreen implements Screen {

    private GameClass game;

    //Drawing
    private int drawMode;

    //Camera
    private OrthographicCamera cam;
    private CameraType cameraType;

    //Main game objects
    private Universe universe;
    private CirclePlayer circlePlayer;
    private CirclePlayerController cpc;
    private GamemodeDemo gamemodeDemo;

    private int tick;

    //Sounds
    private long mainloopId;

    public GameScreen(GameClass game) {
        this.game = game;
    }

    @Override
    public void show() {
        tick = 0;
        drawMode = Constants.DEFAULT_DRAW_MODE;

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        //Create gameworld
        universe = new Universe(Constants.DEFAULT_UNIVERSE_SIZE);
        Planet firstPlanet = new FirstPlanet(universe);
        universe.planets.add(firstPlanet);
        circlePlayer = new CirclePlayer(Constants.DEFAULT_PLAYER_SIZE, firstPlanet, cam);
        cpc = new CirclePlayerController(circlePlayer);

        //Init camera
        cameraType = CameraType.PLAYER;
        cam = new OrthographicCamera(Constants.DEFAULT_UNIVERSE_SIZE, Constants.DEFAULT_UNIVERSE_SIZE * (h / w));
        cam.position.set(cam.viewportWidth, cam.viewportHeight, 0);
        cam.update();

        //Init gamemode
        gamemodeDemo = new GamemodeDemo(circlePlayer, universe);

        //Init main sound track
        mainloopId = SoundSingleton.getInstance().mainLoop.loop(0.30f);
    }

    @Override
    public void render(float delta) {
        float deltaTime = Gdx.graphics.getDeltaTime();
        tick++;
        handleInputs();

        cpc.updatePlayer(deltaTime);
        cam.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (drawMode == 0) {
            drawSprite();
        }
        if (drawMode == 1) {
            drawSimple();
        }

        if (cameraType == CameraType.PLAYER) {
            centerCameraOnPlayer();
            cameraFollowSmooth();
        }

        /*
         * TEST
         */
        if (tick % 100 == 0) {
            //System.out.println(circlePlayer.toString());
            //logger.log();
        }

        gamemodeDemo.update(deltaTime);
        gamemodeDemo.drawHud();
    }


    private void drawSprite() {
        game.spriteBatch.setProjectionMatrix(cam.combined);

        universe.draw(game.spriteBatch);
        circlePlayer.draw(game.spriteBatch);
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
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.dispose();
    }

    @Override
    public void dispose() {
        SoundSingleton.getInstance().mainLoop.stop(mainloopId);
    }

    private void centerCameraOnPlayer() {
        cam.up.set(0, -1, 0);
        cam.position.set(circlePlayer.getX(), circlePlayer.getY(), 0);
        cam.rotate(-circlePlayer.getRotationFromCenter() - 90);
        cam.zoom = ((20f * 40f) / (float) Constants.DEFAULT_UNIVERSE_SIZE);
    }

    private void cameraFollowSmooth() {
        //Cur possition
        Vector2 camVec = Commons.vec3to2(cam.position);
        Vector2 newVec = circlePlayer.getPosition().sub(camVec).scl(0.4f);

        cam.position.x += newVec.x;
        cam.position.y += newVec.y;

        //cam vec repurposed to increase camera heigh from ground
        camVec.x = cam.position.x;
        camVec.y = cam.position.y;

        Vector2 planetVec = circlePlayer.getNearestPlanet().getPosition();
        camVec.x -= planetVec.x;
        camVec.y -= planetVec.y;
        camVec.setLength(circlePlayer.getRadius() * 2.5f);

        cam.position.x += camVec.x;
        cam.position.y += camVec.y;


        cam.up.set(0, -1, 0);
        cam.rotate(-circlePlayer.getRotationFromCenter() - 90);
        cam.zoom = ((20f * 40f) / (float) Constants.DEFAULT_UNIVERSE_SIZE);
    }

    private void handleInputs() {
        handleGameUniversalInputs();

        if (cameraType == CameraType.PLAYER) {
            Vector3 cursorPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            Vector3 aimingAt = cam.unproject(cursorPos);
            circlePlayer.aimingAt = aimingAt;

            cpc.handlePlayerInputs(cam);

        } else if (cameraType == CameraType.FREE) {
            handleFreeInputs();
        }
    }

    private void handleFreeInputs() {
        if (Gdx.input.isTouched()) {
            System.out.printf("Clicked: [" + Gdx.input.getX() + ", " + Gdx.input.getY() + "] \n");
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
        if (Gdx.input.isKeyPressed(Input.Keys.BACKSPACE)) {
            ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU);
        }
    }
}
