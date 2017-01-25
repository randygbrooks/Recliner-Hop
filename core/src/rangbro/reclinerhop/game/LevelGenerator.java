package rangbro.reclinerhop.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import rangbro.reclinerhop.object.Recliner;
import rangbro.reclinerhop.controller.CollisionController;
import rangbro.reclinerhop.object.CoaxialTrack;
import rangbro.reclinerhop.object.collectable.Dress;
import rangbro.reclinerhop.object.collectable.Exchange;
import rangbro.reclinerhop.object.decorative.Background;
import rangbro.reclinerhop.object.decorative.Star;

public class LevelGenerator {
	CollisionController collision;
	TrackPartsGenerator generator;
	public Array<CoaxialTrack> piecesToPullFrom;
	public Array<CoaxialTrack> trackPieces;
	public Array<Star> stars;
	public Array<Background> panels;
	public Array<Dress> dresses;
	public Array<Exchange> exchanges;
	
	private Vector2 lastStar;
	private Vector2 lastPiece;
	private Vector2 lastPanel;

	private float pointToPass = 0f;
	private int pieceOn = 0;
	
	private int numOfDresses;
	private int numOfHorses;
	
	private float timePassed = 0;
	private float scaleFactor = 0.016666667f;
	private int timeInteger = 0;
	private int deleteWidth = 50;
	
	private boolean generateExchanges = true;
	private boolean generateDresses = false;
	
	public Recliner recliner;
	public World gameWorld;
	
	private static final float MIN_PIECE_HEIGHT = 0f;
	private static final float MAX_PIECE_HEIGHT = 2f;
	private static final float MIN_DISTANCE = 1.7f;
	private static final float MAX_DISTANCE = 2.0f;
	private static final float DISTANCE_START_DRESSES = 15.0f;
	private static final float PIECE_JUMP_AMOUNT = 0.2f;
	
	private static final int PIECES_TO_PULL_FROM = 1000;
	private static final int DRESS_EVERY = 2; //One in __ chance
	private static final int EXCHANGE_EVERY = 5; //One in __ chance
	
	private static final float STAR_MAX_HEIGHT = 4;
	
	private static final float BACK_SCALE_X = 21.333f;
	private static final float BACK_SCALE_Y = 6f;
	
	private static final int STAR_SET_SIZE = 15;
	private static final int PIECE_SET_SIZE = 3;
	private static final int BACKGROUND_SET_SIZE = 4;

	
	public LevelGenerator() {
		init();
	}
	
	public void init() {
		generator = new TrackPartsGenerator();
		trackPieces = new Array<CoaxialTrack>();
		stars = new Array<Star>();
		panels = new Array<Background>();
		dresses = new Array<Dress>();
		exchanges = new Array<Exchange>();
		
		collision = new CollisionController();	
		gameWorld = new World(new Vector2(0, -10), true);
		
		
		lastStar = new Vector2(0, 0);
		lastPiece = new Vector2(0, -0.5f);
		lastPanel = new Vector2(-10, -4);
		piecesToPullFrom = new Array<CoaxialTrack>();
		for (int i = 0; i < PIECES_TO_PULL_FROM; i++) {
			piecesToPullFrom.add(new CoaxialTrack(generator.getCoaxialSequence()));
		}
		generatePiece(true);
		recliner = new Recliner(gameWorld, collision, lastPiece.x);
		generateNextSet();
	}
	
	public float getPieceDistance() { //Returns Between MIN_DISTANCE and MAX_DISTANCE
		float variability = MAX_DISTANCE - MIN_DISTANCE;
		return (MathUtils.random(variability) + MIN_DISTANCE);
	}
	
	public void render(SpriteBatch batch) {
		for (Background panel: panels)
			panel.render(batch);
		for (Star star : stars)
			star.render(batch);
		for (CoaxialTrack piece : trackPieces) 
			piece.render(batch);
		for (Dress dress : dresses)
			dress.render(batch);
		for (Exchange exchange : exchanges)
			exchange.render(batch);
		recliner.render(batch);	
	}
	
	public void update(float deltaTime, int dresses, int horses) {
		numOfDresses = dresses;
		numOfHorses = horses;
		recliner.update(deltaTime);
		updateStarRotation(deltaTime);
		updateBackgroundColor(deltaTime);
		if (recliner.getPosition().x >= pointToPass) {
			generateNextSet();
		}
		deleteOldPieces();
	}

	private void deleteOldPieces() {
			for (int i = 0; i < stars.size; i++) {
				if (stars.get(i).getPosition().x < recliner.getPosition().x - 
						deleteWidth) {
					stars.removeIndex(i);
				}
			}
			for (int i = 0; i < panels.size; i++) {
				if (panels.get(i).getPosition().x < recliner.getPosition().x - 
						deleteWidth) {
					panels.removeIndex(i);
				}
			}
			for (int i = 0; i < trackPieces.size; i++) {
				if (trackPieces.get(i).getPosition().x < recliner.getPosition().x - 
						deleteWidth) {
					trackPieces.removeIndex(i);
				}
			}
			for (int i = 0; i < dresses.size; i++) {
				if (dresses.get(i).getPosition().x < recliner.getPosition().x - 
						deleteWidth) {
					dresses.removeIndex(i);
				}
			}
			for (int i = 0; i < exchanges.size; i++) {
				if (exchanges.get(i).getPosition().x < recliner.getPosition().x - 
						deleteWidth) {
					exchanges.removeIndex(i);
				}
			}
	}

	private void generateNextSet() {
		generatePieceSet();
		generateStarSet();
		generateBackgroundSet();
	}

	private void generateBackgroundSet() {
		for (int i = 0; i < BACKGROUND_SET_SIZE; i++) {
			generateBackground();
		}
	}

	private void generateStarSet() {
		for (int i = 0; i < STAR_SET_SIZE; i++) {
			generateStar();
		}
		
	}

	private void generatePieceSet() {
		for (int i = 0; i < PIECE_SET_SIZE/2 + 1; i++) {
			generatePiece(false);
		}
		pointToPass = lastPiece.x;
		for (int i = 0; i < PIECE_SET_SIZE/2; i++) {
			generatePiece(false);
		}
	}

	private void generateBackground() {
		Background bottom2 = new Background(true);
		Background bottom = new Background(false);
		Background top = new Background(true);
		bottom2.setPosition(lastPanel.x, lastPanel.y - 6);
		bottom.setPosition(lastPanel.x, lastPanel.y);
		top.setPosition(lastPanel.x, lastPanel.y + 6);
		bottom.setScale(BACK_SCALE_X, BACK_SCALE_Y);
		bottom2.setScale(BACK_SCALE_X, BACK_SCALE_Y);
		top.setScale(BACK_SCALE_X, BACK_SCALE_Y);
		panels.add(top);
		panels.add(bottom);
		panels.add(bottom2);
		lastPanel.x += (top.getDimension().x * (21.333f));
	}
	
	private void generateStar() {
		Star star = new Star();
		star.setPosition(lastStar.x, star.getPosition().y);
		lastStar.x += (1 + MathUtils.random(STAR_MAX_HEIGHT - 1));
		stars.add(star);
	}
	
	private void generatePiece(boolean isStart) {
		if (pieceOn >= PIECES_TO_PULL_FROM)
			pieceOn = 0;
		CoaxialTrack piece = new CoaxialTrack(piecesToPullFrom.get(pieceOn));
		piece.setPosition(lastPiece.x, lastPiece.y);
		piece.polyBounds.setPosition(lastPiece.x, lastPiece.y + 1);
		trackPieces.add(piece);
		
		if (lastPiece.x > DISTANCE_START_DRESSES)
			generateDresses = true;
		
		BodyDef trackBodyDef = new BodyDef();
		trackBodyDef.position.set(new Vector2(lastPiece.x, lastPiece.y + 1.0f));
		Body trackBody = gameWorld.createBody(trackBodyDef);
		
		float[][] convexVertices = piece.getConvexVertices();
		for (int parts = 0; parts < convexVertices.length; parts++) {
			PolygonShape pieceShape = new PolygonShape();
			pieceShape.set(convexVertices[parts]);
			trackBody.createFixture(pieceShape, 0.0f);
			pieceShape.dispose();
		}
		lastPiece.x += (piece.numOfParts() * piece.getDimension().x) + getPieceDistance();
		
		if (numOfDresses >= 5 && MathUtils.random(EXCHANGE_EVERY - 1) == 0) {
			if(generateExchanges == true) {//Draw Exchange on Piece
				generateExchange(lastPiece.x + piece.getDimension().x / 2, lastPiece.y);
				generateExchanges = false;
			}
		} else if (numOfHorses >= 5 && MathUtils.random(EXCHANGE_EVERY - 1) == 0) {
			if(generateExchanges == true) {//Draw Exchange on Piece
				generateExchange(lastPiece.x + piece.getDimension().x / 2, lastPiece.y);
				generateExchanges = false;
			}
		} else if (MathUtils.random(DRESS_EVERY - 1) == 0 && (generateDresses == true)) {  //Draw a Dress on Piece
			generateDress(lastPiece.x + piece.getDimension().x / 2, lastPiece.y);
			generateDresses = false;
		}
		float random = MathUtils.random(-PIECE_JUMP_AMOUNT, PIECE_JUMP_AMOUNT);
		if ((lastPiece.y + random) <= MAX_PIECE_HEIGHT && (lastPiece.y + random) >= 
				MIN_PIECE_HEIGHT)
			lastPiece.y += random;
		else
			lastPiece.y -= random;
		pieceOn++;
	}
	
	private void generateDress(float x, float y) {
		Dress dress = new Dress();
		dress.setPosition(x + MathUtils.random(-0.2f, 0.2f),
				  (y + 1 + MathUtils.random(0.3f)));
		dresses.add(dress);
	}
	
	private void generateExchange(float x, float y) {
		Exchange exchange;
		if (numOfHorses >= 5)
			exchange = new Exchange(1);
		else
			exchange = new Exchange(0);
		exchange.setPosition(x + MathUtils.random(-0.2f, 0.2f),
				(y + 1 + MathUtils.random(0.3f)));
		exchanges.add(exchange);
	}
	
	private void updateBackgroundColor(float deltaTime) {
		for (Background panel: panels){
			timePassed += deltaTime/120;
			timeInteger = (int)timePassed % 360;
			if (timeInteger >= 0 && timeInteger < 60) {
				panel.tintColor.r = scaleFactor * -(timeInteger - 60);
			} else if (timeInteger >= 60 && timeInteger < 120) {
				panel.tintColor.g = scaleFactor * -(timeInteger - 120);
			} else if (timeInteger >= 120 && timeInteger < 180) {
				panel.tintColor.r = scaleFactor * (timeInteger - 120);
			} else if (timeInteger >= 180 && timeInteger < 240) {
				panel.tintColor.b = scaleFactor * -(timeInteger - 240);
			} else if (timeInteger >= 240 && timeInteger < 300) {
				panel.tintColor.g = scaleFactor * (timeInteger - 240);
			} else if (timeInteger >= 300 && timeInteger < 360) {
				panel.tintColor.b = scaleFactor * (timeInteger - 300);
			}
		}
			
	}
	
	private void updateStarRotation(float deltaTime){
		float rotation;
		for (int star = 0; star < stars.size; star++) {
			rotation = stars.get(star).getRotation();
			if(star % 2 == 0)
				rotation += ((star % 4) + 1) * 60 * deltaTime;
			else
				rotation -= ((star % 4) + 1) * 60 * deltaTime;
			rotation %= 360;
			stars.get(star).setRotation(rotation);
		}			
	}
	
	public float getLastPieceHeight() {
		return lastPiece.y;
	}


	public void generateExchanges() {
		generateExchanges = true;
	}
	
	public void setGenerateDresses(boolean generate) {
		generateDresses = generate;
	}
}
