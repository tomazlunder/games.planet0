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

public class Game extends ApplicationAdapter {

	static final int WORLD_SIZE = 1000;
	final int CAM_SPEED = 3;

	private float rotationSpeed = 0.5f;
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	private OrthographicCamera cam;

	private Sprite worldSprite;
	private Sprite bgSprite;
	private World world;
	private Player player;

	CameraType cameraType;

	int tick;

	@Override
	public void create () {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		cameraType = CameraType.FREE;
		shapeRenderer = new ShapeRenderer();

		world = new World(WORLD_SIZE);
		player = new Player(WORLD_SIZE, world.getRadious());

		cam = new OrthographicCamera(500,500 * (h/w));
		cam.position.set(cam.viewportWidth , cam.viewportHeight , 0);
		cam.update();

		tick = 1;
	}

	@Override
	public void render () {
		handleInputs();
		//player.moveByAngle();

		cam.update();
		shapeRenderer.setProjectionMatrix(cam.combined);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		world.drawWorldSimple(shapeRenderer);
		player.drawPlayerSimple(shapeRenderer);

		if(cameraType == CameraType.PLAYER) {
			centerCameraOnPlayer();
		}
	}
	
	@Override
	public void dispose () {
		shapeRenderer.dispose();
	}

	private void centerCameraOnPlayer(){
		cam.up.set(0, -1, 0);
		cam.position.set(player.getPlayerCenterX(), player.getPlayerCenterY(), 0);
		cam.rotate(-player.getPlayerRotationFromCenter() - 90);
	}

	private void handleInputs(){
		handleUniversalInputs();
		if(cameraType == CameraType.PLAYER){
			//handlePlayerInputs();
		}
		else if(cameraType == CameraType.FREE){
			handleFreeInputs();
		}
		else if(cameraType == CameraType.SATELITE){
			//handleSateliteInputs();
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
			cam.rotate(-rotationSpeed, 0, 0, 1);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.E)) {
			cam.rotate(rotationSpeed, 0, 0, 1);
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
		if (Gdx.input.isKeyPressed(Input.Keys.V)){
			if(cameraType == CameraType.PLAYER){
				cameraType = CameraType.FREE;
			}
			if(cameraType == CameraType.FREE){
				cameraType = CameraType.PLAYER;
			}
		}
	}

}
