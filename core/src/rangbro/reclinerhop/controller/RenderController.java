package rangbro.reclinerhop.controller;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Disposable;

import rangbro.reclinerhop.util.Constants;

public class RenderController implements Disposable{
	
	private OrthographicCamera camera;
	
	private SpriteBatch batch;
	private LogicController logicController;
	Box2DDebugRenderer debugRenderer;
	
	private static final boolean DEBUG_MODE = false;
	
	public RenderController(LogicController logicController) {
		this.logicController = logicController;
		init();
	}
	
	public void render() {
		renderWorld(batch);
		logicController.UI.renderGUI(batch, logicController.dollars, logicController.dresses, 
				logicController.horses, logicController.highScore, 
				logicController.gameOver, logicController.startScreen);
	}
	
	public void resize(int width, int height) {
		camera.viewportWidth = (Constants.VIEWPORT_WIDTH / height) * width;
		camera.update();
		logicController.UI.updateCamera((float)width, (float)height);
	}
	
	@Override
	public void dispose() {
		batch.dispose();
	}

	private void renderWorld(SpriteBatch batch) {
		logicController.camera.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		logicController.level.render(batch);
		batch.end();
		if (DEBUG_MODE)
			debugRenderer.render(logicController.level.gameWorld, camera.combined);
	}
	
	private void init() {
		batch = new SpriteBatch();
		debugRenderer = new Box2DDebugRenderer();
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH,
		    Constants.VIEWPORT_HEIGHT);
		camera.position.set(0, 0, 0);
		camera.update();
	}
}
