package rangbro.reclinerhop.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;

import rangbro.reclinerhop.object.collectable.Collectable;
import rangbro.reclinerhop.object.collectable.Dress;
import rangbro.reclinerhop.object.collectable.Exchange;
import rangbro.reclinerhop.game.Assets;
import rangbro.reclinerhop.game.LevelGenerator;
import rangbro.reclinerhop.game.MainGame;

public class LogicController extends InputAdapter{
	
	public static final float CAMERA_ZOOM_DESKTOP = 1.0f;
	public static final float CAMERA_ZOOM_OTHER = 1.7f;
	
	public UserInterfaceController UI;
	public Music backgroundMusic;
	public CameraController camera;
	public LevelGenerator level;
	public int dresses = 0;
	public int horses = 0;
	public int dollars = 0;
	public int highScore = 0;
	public boolean startScreen = true;
	public boolean gameOver = false;
	
	private Sound dressSound;
	private Sound exchangeSound;
	private Sound gameOverSound;
	private Preferences prefs;
	private Polygon reclinerPoly = new Polygon();
	private Polygon object = new Polygon();
	private static final String TAG = 
			MainGame.class.getName();
	
	
	public LogicController() {
		dressSound = Assets.instance.collectSound;
		exchangeSound = Assets.instance.exchangeSound;
		gameOverSound = Assets.instance.gameOverSound;
		prefs = Gdx.app.getPreferences("My Preferences");
		highScore = prefs.getInteger("highScore", 0);
		UI = new UserInterfaceController();
		backgroundMusic = Assets.instance.music;
		backgroundMusic.play();
		backgroundMusic.setLooping(true);
		init();
	}
	
	public void update(float deltaTime) {
		handleDebugInput(deltaTime);
		handleGameInput(deltaTime);
		level.update(deltaTime, dresses, horses);
		if (startScreen) 
			handleStartInput(deltaTime);
		if (!gameOver)
			camera.update(deltaTime);
		updatePhysics(deltaTime);
		testCollections();
		checkGameOver();
	}
	
	private void handleStartInput(float deltaTime) {
			if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Keys.SPACE)){
				startScreen = false;
				level.recliner.unFreeze();
			}
	}

	@Override
	public boolean keyUp (int keycode) {
		if (keycode == Keys.R) {
			init();
			Gdx.app.debug(TAG, "Game world reset");
		} else if (keycode == Keys.ENTER) {
			camera.setTarget(camera.hasTarget() 
					? null : level.recliner);
			Gdx.app.debug(TAG, "Camera follow enabled: " 
					+ camera.hasTarget());
			if(!camera.hasTarget())
				level.recliner.freeze();
			else
				level.recliner.unFreeze();
		}
		return false;
	}
	
	public void init() {
		gameOver = false;
		gameOverSound.stop();
		Gdx.input.setInputProcessor(this);
		camera = new CameraController();
		if (Gdx.app.getType() == ApplicationType.Desktop) {
			camera.setZoom(CAMERA_ZOOM_DESKTOP);
		} else {
			camera.setZoom(CAMERA_ZOOM_OTHER);
		}
		dollars = 0;
		horses = 0;
		dresses = 0;
		level = new LevelGenerator();
		camera.setTarget(level.recliner);
		if (startScreen)
			level.recliner.freeze();
	}
	
	private void checkPickedUp(Collectable item) {
		if (item.isCollected()) {
			return;
		}
		object.setVertices(getBoundingVertices(item));
		if (Intersector.overlapConvexPolygons(reclinerPoly, object)){
			onCollisionWithCollectable(item);
		}
	}
	
	private void updatePhysics(float deltaTime) {
		level.gameWorld.step(1/45f, 6, 2);
	}
	
	private void testCollections() {
		reclinerPoly.setVertices(level.recliner.getTotalVertices());
		reclinerPoly.setPosition(level.recliner.getPosition().x, level.recliner.getPosition().y);
		reclinerPoly.rotate(level.recliner.getRotation());
		for (Dress dress : level.dresses) {
			checkPickedUp(dress);
		}
		for (Exchange exchange : level.exchanges) {
			checkPickedUp(exchange);
		}
	}
	
	private void onCollisionWithCollectable(Collectable item) {
		item.setCollected(true);
		if (item.isDress()) {
			onCollisionWithDress(item);
		} else if (item.isExchange()) {
			onCollisionWithExchange(item);
		}
	}
	
	private void onCollisionWithDress(Collectable dress){
		dressSound.play(0.5f);
		dresses++;
		Gdx.app.log(TAG, "Dress Collected");
	}
	
	private void onCollisionWithExchange(Collectable exchange){
		level.generateExchanges();
		exchangeSound.play(0.8f);
		if (exchange.getType() == 0) { //Dress To Horse
			if (dresses >= 5) {
				int horseTemp = dresses/5;
				dresses %= 5;
				horses += horseTemp;
			}
		} else { //Horse To Cash
			if (horses >= 5) {
				int cashTemp = horses/5;
				horses %= 5;
				dollars += cashTemp;
			}
		}
		Gdx.app.log(TAG, "Exchange Collected");
	}
	
	private float[] getBoundingVertices(Collectable collectable) {
		float x = collectable.getPosition().x;
		float y = collectable.getPosition().y;
		float x2 = x + collectable.getBounds().width;
		float y2 = y + collectable.getBounds().height;
		return new float[]{x, y, x, y2, x2, y2, x2, y};
	}
	
	private void checkGameOver() {
		if (level.recliner.getPosition().y < level.getLastPieceHeight() - 2.3f) {
			if (dollars > highScore)
				highScore = dollars;
			if (gameOver == false)
				gameOverSound.play(0.5f);
			gameOver = true;
			showGameOverScreen();
		}
	}
	
	private void showGameOverScreen() {
		
		prefs.putInteger("highScore", dollars);
		prefs.flush();
		if (Gdx.input.isKeyPressed(Keys.SPACE) || 
				Gdx.input.isTouched()) { 
			gameOver = false;
			level.dresses = null;
			level.exchanges = null;
			level.panels = null;
			level.trackPieces = null;
			level.recliner = null;
			level.stars = null;
			level.piecesToPullFrom = null;
			level.gameWorld = null;
			init();
		}
	}

	private void handleDebugInput(float deltaTime){
		//INPUT CONTROLS
		if (Gdx.app.getType() != ApplicationType.Desktop) {
			return;
		}
		if (!camera.hasTarget(level.recliner)) {
			//CAMERA CONTROLS (move)
			float camMoveSpeed = 5 * deltaTime;
			float camMoveSpeedAccelerationFactor = 5;
			if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
				camMoveSpeed *= camMoveSpeedAccelerationFactor;
			}
			if (Gdx.input.isKeyPressed(Keys.LEFT)) {
				moveCamera(-camMoveSpeed, 0);
			}
			if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
				moveCamera(camMoveSpeed, 0);
			}
			if (Gdx.input.isKeyPressed(Keys.UP)) {
				moveCamera(0, camMoveSpeed);
			}
			if (Gdx.input.isKeyPressed(Keys.DOWN)) {
				moveCamera(0, -camMoveSpeed);
			}
			if (Gdx.input.isKeyPressed(Keys.BACKSPACE)) {
				camera.setPosition(0,0);
			}
			
			//Camera Controls (Zoom)
			float camZoomSpeed = 1 * deltaTime;
			float camZoomSpeedAccelerationFactor = 5;
			if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
				camZoomSpeed *= camZoomSpeedAccelerationFactor;
			}
			if (Gdx.input.isKeyPressed(Keys.COMMA)) {
				camera.addZoom(camZoomSpeed);
			}
			if (Gdx.input.isKeyPressed(Keys.PERIOD)) {
				camera.addZoom(-camZoomSpeed);
			}
			if (Gdx.input.isKeyPressed(Keys.SLASH)) {
				camera.setZoom(1);
			}
		}
	}
	
	private void handleGameInput (float deltaTime) {
		if (!Gdx.input.isKeyPressed(Keys.SPACE)) {
			level.recliner.setJumping(false);
		}
		if (!Gdx.input.isTouched()) {
			level.recliner.setTouchJumping(false);
		}
		if (!level.recliner.isTouchJumping() && Gdx.input.isTouched()) {
						level.recliner.jump();
						level.recliner.setTouchJumping(true);
			}
		if (!level.recliner.isJumping() && Gdx.input.isKeyPressed(Keys.SPACE)) {
					level.recliner.jump();
					level.recliner.setJumping(true);
		}
	}

	private void moveCamera(float x, float y) {
		x += camera.getPosition().x;
		y += camera.getPosition().y;
		camera.setPosition(x, y);
	}

	
} 
