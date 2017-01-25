package rangbro.reclinerhop.game;

import rangbro.reclinerhop.controller.*;
import rangbro.reclinerhop.game.Assets;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class MainGame extends ApplicationAdapter {
	public static final String TAG =
	    MainGame.class.getName();
	private RenderController renderController;
	private LogicController logicController;
	
	private boolean paused;
	
	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		Assets.instance.init(new AssetManager());
		logicController = new LogicController();
		renderController = new RenderController(logicController);
		paused = false;
	}

	@Override
	public void render () {
		if(!paused) {
			logicController.update(Gdx.graphics.getDeltaTime()); //Update Game World
		}
		Gdx.gl.glClearColor(0x64/255.0f, 0x95/255.0f, 0xed/255.0f, 0xff/255.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //Clears the screen
		renderController.render();
	}
	
	@Override
	public void pause() {
		paused = true;
	}
	
	@Override
	public void resume() {
		paused = false;
	}
	
	@Override
	public void dispose () {
		renderController.dispose();
		Assets.instance.dispose();
	}
	
	@Override
	public void resize(int width, int height) {
		renderController.resize(width, height);
	}
}
