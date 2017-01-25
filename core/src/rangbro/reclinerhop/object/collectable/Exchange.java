package rangbro.reclinerhop.object.collectable;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import rangbro.reclinerhop.game.Assets;
import rangbro.reclinerhop.object.GameObject;

public class Exchange extends GameObject implements Collectable{
	
	private boolean collected;
	private int type; //0 is Dress to Horse, 1 is Horse to Cash
	
	private TextureRegion exchange;
	
	public Exchange(int type) {
		this.type = type;
		init();
	}
	
	private void init() {
		dimension.set(1.5f, 1.5f);
		if (type == 0) {
			exchange = Assets.instance.exchange.dressToHorse;
		} else {
			exchange = Assets.instance.exchange.horseToMoney;
		}
		bounds.set(0, 0, dimension.x, dimension.y);
		collected = false;
	}
	
	@Override
	public void render(SpriteBatch batch) {
		if (collected) {
			return;
		}
		TextureRegion reg = exchange;
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
	public Vector2 getPosition() {
		return position;
	}

	@Override
	public Rectangle getBounds() {
		return bounds;
	}

	@Override
	public boolean isDress() {
		return false;
	}

	@Override
	public boolean isExchange() {
		return true;
	}

	@Override
	public void setCollected(boolean collected) {
		this.collected = collected;
	}

	@Override
	public int getType() {
		return type;
	}

}
