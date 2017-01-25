package rangbro.reclinerhop.object.collectable;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import rangbro.reclinerhop.game.Assets;
import rangbro.reclinerhop.object.GameObject;

public class Dress extends GameObject implements Collectable{

	private TextureRegion dress;
	private int color;
	
	private boolean collected;
	
	public Dress() {
		init();
	}
	
	public void init() {
		dimension.set(0.6f, 0.8f);
		color = MathUtils.random(0 ,4);
		switch(color) {
			case 0:
				dress = Assets.instance.dress.dress1;
				break;
			case 1:
				dress = Assets.instance.dress.dress2;
				break;
			case 2:
				dress = Assets.instance.dress.dress3;
				break;
			case 3:
				dress = Assets.instance.dress.dress4;
				break;
			default:
				dress = Assets.instance.dress.dress5;
				break;
		}
		bounds.set(0, 0, dimension.x, dimension.y);
		collected = false;
	}
	
	@Override
	public void render(SpriteBatch batch) {
		if (collected) {
			return;
		}
		
		TextureRegion reg = null;
		reg = dress;
		batch.draw(reg.getTexture(), getPosition().x, getPosition().y,
				origin.x, origin.y, dimension.x, dimension.y,
				scale.x, scale.y, rotation, reg.getRegionX(),
				reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(),
				false, false);
	}

	@Override
	public boolean isCollected() {
		return collected;
	}
	
	@Override
	public void setCollected(boolean collected) {
		this.collected = collected;
	}

	@Override
	public Vector2 getPosition() {
		return position;
	}

	@Override
	public Rectangle getBounds() {
		return bounds;
	}

	@Override
	public boolean isDress() {
		return true;
	}

	@Override
	public boolean isExchange() {
		return false;
	}

	@Override
	public int getType() { //Returns the color of the dress
		return color;
	}
}
