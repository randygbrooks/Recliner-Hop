package rangbro.reclinerhop.controller;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import rangbro.reclinerhop.game.Assets;
import rangbro.reclinerhop.util.Constants;

public class UserInterfaceController {
	
	private OrthographicCamera cameraGUI;
	private Texture gameOver;
	private TextureRegion boxUI;
	private TextureRegion horseUI;
	private TextureRegion dressUI;
	private TextureRegion dollarUI;
	
	private int dollars;
	private int dresses;
	private int horses;
	private int highScore;
	
	public UserInterfaceController() {
		init();
	}
	
	public void init() {
		cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH,
				Constants.VIEWPORT_GUI_HEIGHT);
		cameraGUI.position.set(0,0,0);
		cameraGUI.setToOrtho(true);
		cameraGUI.update();
		gameOver = Assets.instance.gameover;
		horseUI = Assets.instance.horse.horse;
		dressUI = Assets.instance.dress.dress3;
		dollarUI = Assets.instance.dollar.dollar;
		boxUI = Assets.instance.box.box;
		gameOver.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
		
	void renderGUI(SpriteBatch batch, int dollars, int dresses, int horses,
		int highScore, boolean isGameOver, boolean isStartScreen) {
		this.dollars = dollars;
		this.dresses = dresses;
		this.horses = horses;
		this.highScore = highScore;
		batch.setProjectionMatrix(cameraGUI.combined);
		batch.begin();
		if (isStartScreen)
			renderStartScreen(batch);
		if (isGameOver)
			renderGameOver(batch);
		renderGUIMoney(batch);
		renderGUIDresses(batch);
		renderGUIHorses(batch);
		batch.end();
	}
	
	public void updateCamera(float width, float height) {
			cameraGUI.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT;
			cameraGUI.viewportWidth = Constants.VIEWPORT_GUI_HEIGHT
					/ height * width;
			cameraGUI.position.set(cameraGUI.viewportWidth / 2,
					cameraGUI.viewportHeight / 2, 0);
			cameraGUI.update();
	}
	
	private void renderGUIMoney(SpriteBatch batch) {
		float x = 0;
		float y = 0;
		batch.draw(boxUI, x, y, 50, 50, 100, 100, 0.9f, -0.9f, 0);
		batch.draw(dollarUI, x - 28, y + 8, 70, 60, 190, 100, 0.3f, -0.3f, 0);
		Assets.instance.fonts.defaultSmall.draw(batch, "" + dollars,
				x + 35, y + 2);
	}
	
	public void renderGameOver(SpriteBatch batch) {
		float width = gameOver.getWidth();
		float height = gameOver.getHeight();
		float x = (cameraGUI.viewportWidth / 2) - ( width * 0.8f / 2);
		float y = (cameraGUI.viewportHeight / 2) - ( height * 0.8f / 2) + 40;
		
		batch.draw(gameOver, x, y, 0f, 0f, width, height, 0.8f, 0.8f,
				 0f, 0, 0, (int)width, (int)height, false, true);
		Assets.instance.fonts.defaultSmall.draw(batch, "Game Over",
				x + 30, y + 20);
		renderGUIHighScore(batch, x, y);
		Assets.instance.fonts.defaultTiny.draw(batch, "Tap To Continue",
				x + 20, y + 250);
	}
	
	public void renderStartScreen(SpriteBatch batch) {
		float width = gameOver.getWidth();
		float height = gameOver.getHeight();
		float x = (cameraGUI.viewportWidth / 2) - ( width * 0.8f / 2);
		float y = (cameraGUI.viewportHeight / 2) - ( height * 0.8f / 2) + 40;
		batch.draw(gameOver, x, y, 0f, 0f, width, height, 0.8f, 0.8f,
				 0f, 0, 0, (int)width, (int)height, false, true);
		Assets.instance.fonts.defaultSmall.draw(batch, "Recliner Hop",
				x + 15, y + 20);
		Assets.instance.fonts.defaultReallySmall.draw(batch, "Best Score: " + highScore,
				x + 20, y + 130);
		Assets.instance.fonts.defaultTiny.draw(batch, "Tap To Continue",
				x + 20, y + 250);
	}
	
	private void renderGUIHorses(SpriteBatch batch) {
		float x = (cameraGUI.viewportWidth/2 - 50);
		float y = 0;
		batch.draw(boxUI, x, y, 50, 50, 100, 100, 0.9f, -0.9f, 0);
		batch.draw(horseUI, x, y + 20, 50, 50, 100, 100, 0.48f, -0.4f, 0);
		Assets.instance.fonts.defaultSmall.draw(batch, "" + horses,
				x + 35, y + 2);
	}
	
	private void renderGUIDresses(SpriteBatch batch) {
		float x = cameraGUI.viewportWidth - 100;
		float y = 0;
		batch.draw(boxUI, x, y, 50, 50, 100, 100, 0.9f, -0.9f, 0);
		batch.draw(dressUI, x - 2, y + 20, 50, 50, 100, 100, 0.3f, -0.32f, 0);
		Assets.instance.fonts.defaultSmall.draw(batch, "" + dresses,
				x + 37, y + 2);
	}
	
	private void renderGUIHighScore(SpriteBatch batch, float x, float y) {
		Assets.instance.fonts.defaultReallySmall.draw(batch, "Score: " + dollars,
				x + 20, y + 110);
		Assets.instance.fonts.defaultReallySmall.draw(batch, "Best Score: " + highScore,
				x + 20, y + 160);
	}
	
}
