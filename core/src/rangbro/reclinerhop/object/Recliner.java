package rangbro.reclinerhop.object;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import rangbro.reclinerhop.controller.CollisionController;
import rangbro.reclinerhop.game.Assets;

public class Recliner extends GameObject{

	private static final float STARTING_VELOCITY = 3.4f;
	 //Higher VEL_JUMP_IMPACT makes velocity have less impact on jump.
	//private static final float VEL_X_JUMP_IMPACT = 6;
	//private static final float VEL_Y_JUMP_IMPACT = 14;
	
	private static final float DENSITY = 1.0f;
	private static final float FRICTION = 0.2f;
	private static final float RESTITUTION = 0.1f;
	
	private static final float JUMP_Y = 2.2f;

	private float maxVelocity = STARTING_VELOCITY;
	private float startPosition = 0;
	private float lastPosition = 0;
			//(maxVelocity / VEL_X_JUMP_IMPACT);
	private float gravityScale;
	private float jumpAmountY = JUMP_Y;// + VEL_Y_JUMP_IMPACT;
	private float jumpAmountX = 0.0f;
	private boolean isFrozen = false;
	private boolean isTouchJumping = false;
	private boolean isJumping = false;
	private Sound jumpSound;
	
	World gameWorld;
	CollisionController collision;
	private TextureRegion recliner;
	PolygonShape reclinerPolygon;
	PolygonShape reclinerExtensionPolygon;
	BodyDef reclinerBodyDef;
	Body reclinerBody;
	FixtureDef reclinerFixtureDef;
	FixtureDef reclinerExtensionFixtureDef;
	Fixture reclinerFixture;
	Fixture reclinerExtensionFixture;
	
	public Recliner(World gameWorld, CollisionController collision, float startPosition) {
		this.startPosition = startPosition;
		this.gameWorld = gameWorld;
		init();
	}
	
	public void init() {
		dimension.set(1, 1);
		jumpSound = Assets.instance.jumpSound;
		recliner = Assets.instance.recliner.reclinerOpen;
		bounds.set(0, 0, dimension.x, dimension.y);
		lastPosition = position.x;
		
		reclinerBodyDef = new BodyDef();
		reclinerBodyDef.type = BodyType.DynamicBody;
		reclinerBodyDef.position.set(startPosition, getPosition().y);
		reclinerBody = gameWorld.createBody(reclinerBodyDef);
		reclinerBody.setAngularDamping(90.0f);
		origin.set(reclinerBody.getLocalCenter());
		
		reclinerPolygon = new PolygonShape();
		reclinerPolygon.set(getVertices());
		reclinerFixtureDef = new FixtureDef();
		reclinerFixtureDef.shape = reclinerPolygon;
		reclinerFixtureDef.density = DENSITY; 
		reclinerFixtureDef.friction = FRICTION;
		reclinerFixtureDef.restitution = RESTITUTION;
		reclinerFixture = reclinerBody.createFixture(reclinerFixtureDef);

		reclinerExtensionPolygon = new PolygonShape();
		reclinerExtensionPolygon.set(getExtensionVertices());
		reclinerExtensionFixtureDef = new FixtureDef();
		reclinerExtensionFixtureDef.shape = reclinerExtensionPolygon;
		reclinerExtensionFixtureDef.density = DENSITY; 
		reclinerExtensionFixtureDef.friction = FRICTION;
		reclinerExtensionFixtureDef.restitution = RESTITUTION;
		reclinerExtensionFixture = reclinerBody.createFixture(reclinerExtensionFixtureDef);
		
		reclinerPolygon.dispose();
		reclinerExtensionPolygon.dispose();
	}
	
	private float[] getExtensionVertices() {
		float topRightx = 0.973f;
		float topRighty = 0.32f;
		float bottomRight1x = 0.973f;
		float bottomRight1y = 0.26f;
		float bottomRight2x = 0.825f;
		float bottomRight2y = 0.15f;
		float topLeftx = 0.66f;
		float topLefty = 0.55f;
		float bottomLeftx = 0.66f;
		float bottomLefty = 0.15f;
		return new float[]{bottomLeftx, bottomLefty, topLeftx, topLefty,
				topRightx, topRighty, bottomRight1x, bottomRight1y, 
				bottomRight2x, bottomRight2y};
	}

	@Override
	public float[] getVertices(){
		float topRightx = 0.66f;
		float topRighty = 0.57f;
		float bottomRightx = 0.66f;
		float bottomRighty = 0.09f;
		float topLeftx = 0.02f;
		float topLefty = 0.88f;
		float topLeft1x = 0.09f;
		float topLeft1y = 0.97f;
		float topLeftx2 = 0.17f;
		float topLefty2 = 0.98f;
		float bottomLeftx = 0.2f;
		float bottomLefty = 0.11f;
		return new float[]{bottomLeftx, bottomLefty, topLeftx, topLefty,
				topLeft1x, topLeft1y, topLeftx2, topLefty2, topRightx, 
				topRighty, bottomRightx, bottomRighty};
	}
	
	public float[] getTotalVertices() {
		float topRightx = 0.66f;
		float topRighty = 0.57f;
		
		float eTopLeftx = 0.66f;
		float eTopLefty = 0.55f;
		float eTopRightx = 0.973f;
		float eTopRighty = 0.32f;
		float eBottomRight1x = 0.973f;
		float eBottomRight1y = 0.26f;
		float eBottomRight2x = 0.825f;
		float eBottomRight2y = 0.15f;
		float eBottomLeftx = 0.66f;
		float eBottomLefty = 0.15f;
		
		float bottomRightx = 0.66f;
		float bottomRighty = 0.09f;
		float bottomLeftx = 0.2f;
		float bottomLefty = 0.11f;
		
		float topLeftx = 0.02f;
		float topLefty = 0.88f;
		float topLeft1x = 0.09f;
		float topLeft1y = 0.97f;
		float topLeftx2 = 0.17f;
		float topLefty2 = 0.98f;
		
		return new float[]{topRightx, topRighty, eTopLeftx, 
				eTopLefty, eTopRightx, eTopRighty, eBottomRight1x, eBottomRight1y, 
				eBottomRight2x, eBottomRight2y, eBottomLeftx, eBottomLefty,
				bottomRightx, bottomRighty, bottomLeftx, bottomLefty, topLeftx, topLefty, 
				topLeft1x, topLeft1y, topLeftx2, topLefty2};
	}
	
	@Override
	public void update (float deltaTime) {
		position.x = reclinerBody.getPosition().x;
		position.y = reclinerBody.getPosition().y;
		fixReclinerRotation();
		if(!isFrozen) {
			checkReclinerSpeed();
			checkReclinerStuck();
		}
		super.update(deltaTime);
	}

	private void checkReclinerStuck() {
		if (lastPosition > position.x + 0.04f) {
			reclinerBody.applyLinearImpulse(-0.4f, 0, reclinerBody.getPosition().x,
			reclinerBody.getPosition().y, true);
		}
		lastPosition = position.x;
	}

	private void fixReclinerRotation() {
		float degrees = MathUtils.radiansToDegrees * reclinerBody.getAngle() % 360;
		if (degrees > 10) {
			reclinerBody.applyAngularImpulse(-0.25f, true);
		}
		if (degrees > 50) {
			reclinerBody.applyAngularImpulse(-0.5f, true);
		}
		if (degrees < -30) {
			reclinerBody.applyAngularImpulse(0.25f, true);
		}
		if (degrees < -50) {
			reclinerBody.applyAngularImpulse(0.5f, true);
		}
	}
	
	private void checkReclinerSpeed() {
		if (reclinerBody.getLinearVelocity().x < maxVelocity) {
			reclinerBody.applyLinearImpulse(0.4f, 0, reclinerBody.getPosition().x,
					reclinerBody.getPosition().y, true);
		}
	}
	
	public void jump() {
		if (gameWorld.getContactCount() > 0) {
			reclinerBody.applyLinearImpulse(jumpAmountX, jumpAmountY, 
					reclinerBody.getPosition().x + 0.5f,
					reclinerBody.getPosition().y, true);
			jumpSound.play(0.32f);
		}
	}
	
	public void setTouchJumping(boolean jumping) {
		isTouchJumping = jumping;
	}
	
	public void freeze() {
		reclinerBody.setLinearVelocity(0, 0);
		gravityScale = reclinerBody.getGravityScale();
		reclinerBody.setGravityScale(0);
		isFrozen = true;
	}
	
	public void unFreeze() {
		reclinerBody.setGravityScale(gravityScale);
		isFrozen = false;
	}
	 
	public boolean isTouchJumping() {
		return isTouchJumping;
	}
	
	public void setJumping(boolean jumping) {
		isJumping = jumping;
	}
	
	public boolean isJumping() {
		return isJumping;
	}
	
	@Override
	public void render(SpriteBatch batch) {
		TextureRegion reg = recliner;
		batch.draw(reg.getTexture(), reclinerBody.getPosition().x, reclinerBody.getPosition().y,
				origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, 
				reclinerBody.getAngle() * MathUtils.radiansToDegrees, reg.getRegionX(), 
				reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
	}
}
