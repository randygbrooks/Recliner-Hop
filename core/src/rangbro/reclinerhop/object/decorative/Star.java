package rangbro.reclinerhop.object.decorative;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import rangbro.reclinerhop.game.Assets;
import rangbro.reclinerhop.object.GameObject;

public class Star extends GameObject{
	float randomX;
	float randomY;
	
	private TextureRegion star;
	
	public Star() {
		init();
	}
	
	private void init() {
		rotation = (MathUtils.random(359));
		randomY = MathUtils.random(-2.0f, 2.0f);
		dimension.set(MathUtils.random(1.0f, 1.5f), MathUtils.random(1.0f, 1.5f));
		
		star = Assets.instance.decoration.star;
		
		origin.x = 0.5f;
		origin.y = 0.5f;
	}
	
	@Override
	public void render(SpriteBatch batch) {
		TextureRegion reg = star;
		float xRel = dimension.x;
		float yRel = dimension.y + randomY;
		batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 0.7f);
		batch.draw(reg.getTexture(), position.x + xRel, position.y + yRel,
				origin.x , origin.y, dimension.x, dimension.y, scale.x,
				scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
				reg.getRegionWidth(), reg.getRegionHeight(), false, false);
	}
}
