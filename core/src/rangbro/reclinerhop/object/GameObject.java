package rangbro.reclinerhop.object;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;

public abstract class GameObject {
	
	protected Rectangle bounds;
	protected Vector2 position;
	protected Vector2 dimension;
	protected Vector2 origin;
	protected Vector2 scale;
	protected float rotation;
	
	public GameObject() {
		position = new Vector2();
		dimension = new Vector2(1, 1);
		origin = new Vector2();
		scale = new Vector2(1, 1);
		rotation = 0;
		bounds = new Rectangle();
	}
	
	public void update(float deltaTime) {
		updateMotionX(deltaTime);
		updateMotionY(deltaTime);
	}
	
	protected void updateMotionX(float deltaTime) {	
	}

	protected void updateMotionY(float deltaTime) {
	}
	
	public abstract void render(SpriteBatch batch);
	
	public void setPositionX(float x) {
		position.x = x;
	}
	
	public void setPositionY(float y) {
		position.y = y;
	}
	
	public void setPosition(float x, float y) {
		position.x = x;
		position.y = y;
	}
	
	public void setPosition(Vector2 position) {
		this.position = position;
	}
	
	public Vector2 getPosition() {
		return position;
	}
	
	public Vector2 getDimension() {
		return dimension;
	}
	
	public Vector2 getOrigin() {
		return origin;
	}
	
	public void setScaleX(float x) {
		scale.x = x;
	}
	
	public void setScaleY(float y) {
		scale.y = y;
	}
	
	public void setScale(float x, float y) {
		scale.x = x;
		scale.y = y;
	}
	
	public void setScale(Vector2 scale) {
		this.scale = scale;
	}
	
	public Vector2 getScale() {
		return scale;
	}
	
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}
	
	public float getRotation() {
		return rotation;
	}
	
	public float[] getVertices() {
		float[] vertices = new float[8];
		vertices[0] = bounds.x;
		vertices[1] = bounds.y;
		vertices[2] = bounds.x;
		vertices[3] = bounds.y + bounds.height;
		vertices[4] = bounds.x + bounds.width;
		vertices[5] = bounds.y + bounds.height;
		vertices[6] = bounds.x + bounds.width;
		vertices[7] = bounds.y;
		return vertices;
	}
	
	public Rectangle getRectangleBounds() {
		return bounds;
	}
}
