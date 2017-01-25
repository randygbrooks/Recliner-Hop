package rangbro.reclinerhop.object.decorative;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import rangbro.reclinerhop.game.Assets;
import rangbro.reclinerhop.object.GameObject;

public class Background extends GameObject{
	
	private static final float COLOR_OPACITY = 1.0f;
	
	private boolean isTop;
	private Color defaultColor;
	public Color tintColor;
	private Texture background;
	
	public Background(boolean isTop) {
		this.isTop = isTop;
		init();
	}
	
	private void init() {
		tintColor = new Color();
		tintColor.r = 1;
		tintColor.g = 1;
		tintColor.b = 1;
		tintColor.a = COLOR_OPACITY;
		background = Assets.instance.background;
	}
	
	@Override
	public void render(SpriteBatch batch) {
		batch.disableBlending();
		float xRel = dimension.x;
		float yRel = dimension.y;
		defaultColor = batch.getColor();
		batch.setColor(tintColor);
		if(isTop) {
			batch.draw(background, position.x + xRel, position.y + yRel,
					origin.x , origin.y, dimension.x, dimension.y, scale.x,
					scale.y, rotation, 0, 0, background.getWidth(),
					background.getHeight(), true, true);
		} else {
			batch.draw(background, position.x + xRel, position.y + yRel,
					origin.x , origin.y, dimension.x, dimension.y, scale.x,
					scale.y, rotation, 0, 0, background.getWidth(),
					background.getHeight(), false, false);
		}
		batch.setColor(defaultColor);
		batch.enableBlending();
	}

}
