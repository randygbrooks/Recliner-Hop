package rangbro.reclinerhop.object.collectable;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public interface Collectable{
	public boolean isCollected();
	public void setCollected(boolean collected);
	public int getType();//Returns value specific to each collectable
	
	public boolean isDress();
	public boolean isExchange();
	
	public Vector2 getPosition();
	public Rectangle getBounds();
}
