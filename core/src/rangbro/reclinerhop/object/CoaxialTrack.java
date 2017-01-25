package rangbro.reclinerhop.object;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.IntArray;

import rangbro.reclinerhop.game.Assets;

public class CoaxialTrack extends GameObject{
	private static final float IMAGE_WIDTH = 
			Assets.instance.coaxial.center.getRegionWidth();
	private static final float IMAGE_HEIGHT = 
			Assets.instance.coaxial.center.getRegionHeight();
	
	public static final String TAG = Recliner.class.getName();
	
	public TextureRegion center; //0
	public TextureRegion top; //1
	public TextureRegion bottom; //2
	public TextureRegion centerEnd; //3
	public TextureRegion centerBottomEnd; //4
	public TextureRegion centerTopEnd; //5
	public TextureRegion bottomCenterEnd; //6
	public TextureRegion topCenterEnd; //7
	
	public Polygon polyBounds;
	Texture textureSolid;
	IntArray pieces;
	private float[] vertices;
	private float[][] convexVertices;
	
	public CoaxialTrack(IntArray pieces) {
		this.pieces = pieces;
		init();
	}
	
	public CoaxialTrack(CoaxialTrack track) {
		pieces = track.pieces;
		origin.x = dimension.x / 2;
		origin.y = dimension.y / 2;
		
		polyBounds = track.polyBounds;
		bounds = track.bounds;
		dimension = track.dimension;
		vertices = track.vertices;
		convexVertices = track.convexVertices;
		
		center = Assets.instance.coaxial.center;
		top = Assets.instance.coaxial.top;
		bottom = Assets.instance.coaxial.bottom;
		centerEnd = Assets.instance.coaxial.centerEnd;
		centerBottomEnd = Assets.instance.coaxial.centerBottomEnd;
		centerTopEnd = Assets.instance.coaxial.centerTopEnd;
		bottomCenterEnd = Assets.instance.coaxial.bottomCenterEnd;
		topCenterEnd = Assets.instance.coaxial.topCenterEnd;
	}
	
	private void init() {
		origin.x = dimension.x / 2;
		origin.y = dimension.y / 2;
		
		polyBounds = new Polygon();
		polyBounds.setVertices(getBounds().toArray());
		bounds.set(0, 0, (dimension.x * pieces.size) + 0.5f, dimension.y);
		dimension.set(1.274336f, 1);
		polyBounds.setScale(dimension.x, dimension.y);
		
		vertices = polyBounds.getTransformedVertices();
		convexVertices = getConvexBounds();
		
		center = Assets.instance.coaxial.center;
		top = Assets.instance.coaxial.top;
		bottom = Assets.instance.coaxial.bottom;
		centerEnd = Assets.instance.coaxial.centerEnd;
		centerBottomEnd = Assets.instance.coaxial.centerBottomEnd;
		centerTopEnd = Assets.instance.coaxial.centerTopEnd;
		bottomCenterEnd = Assets.instance.coaxial.bottomCenterEnd;
		topCenterEnd = Assets.instance.coaxial.topCenterEnd;
	}
	/**
	 * This function takes all the pieces generated earlier,
	 * and it gets the x, y coordinates, and returns them
	 * as an array of floats.
	 * @return float boundaries
	 */
	private FloatArray getBounds() {
		FloatArray points = new FloatArray();
		points.addAll(getTopStarts(pieces.get(0)));
		for (int i = 1; i < pieces.size; i++) {
			points.addAll(getTopPoints(pieces.get(i), i));
		}
		for (int i = pieces.size - 1; i >= 1; i--) {
			points.addAll(getBottomPoints(pieces.get(i), i));
		}
		points.addAll(getBottomStarts(pieces.get(0)));
		return points;
	}

	private float[][] getConvexBounds() {
		int numberOfParts = (vertices.length / 4) - 1;
		float[][] convexBounds = new float[numberOfParts][];
		for (int i = 0; i < numberOfParts; i++) {
			int offset = 2 * i;
			convexBounds[i] = new float[8];
			int index = 0;
			for (int j = 0; j < 4; j++){ //Add Top Vertices
				convexBounds[i][index] = vertices[offset + j];
				index++;
			}
			for (int j = 4; j >= 1; j--){ //Add Bottom Vertices
				convexBounds[i][index] = vertices[vertices.length - j - offset];
				index++;
			}
		}
		return convexBounds;
	}
	
	private float relativeX(float x) {
		return (x - origin.x)/IMAGE_WIDTH;
	}
	
	private float relativeY(float y) {
		return -(y + origin.y)/IMAGE_HEIGHT;
	}

	private float relativeOffsetX(float x, int pieceOn) {
		return (x - origin.x)/IMAGE_WIDTH + pieceOn;
	}
	
	private FloatArray getTopStarts(int firstPiece) {
		FloatArray topStarts = new FloatArray();
		switch(firstPiece) {
			case 0:
				break;
			case 1:
				break;
			case 2:
				break;
			case 3:
				topStarts.add(relativeX(60f));
				topStarts.add(relativeY(279f));
				break;
			case 4:
				topStarts.add(relativeX(48f));
				topStarts.add(relativeY(513f));
				
				topStarts.add(relativeX(368f));
				topStarts.add(relativeY(513f));
				
				topStarts.add(relativeX(734f));
				topStarts.add(relativeY(279f));
				break;
			case 5:
				topStarts.add(relativeX(33f));
				topStarts.add(relativeY(96f));
				
				topStarts.add(relativeX(436f));
				topStarts.add(relativeY(96f));
				
				topStarts.add(relativeX(731f));
				topStarts.add(relativeY(279f));
				break;
			case 6:
				topStarts.add(relativeX(48f));
				topStarts.add(relativeY(279f));
				
				topStarts.add(relativeX(375f));
				topStarts.add(relativeY(279f));
				
				topStarts.add(relativeX(751f));
				topStarts.add(relativeY(516f));
				break;
			case 7:
				topStarts.add(relativeX(37f));
				topStarts.add(relativeY(268f));
				
				topStarts.add(relativeX(435f));
				topStarts.add(relativeY(268f));
				
				topStarts.add(relativeX(724f));
				topStarts.add(relativeY(90f));
				break;
			default:
		}	
		return topStarts;
	}
	
	private FloatArray getBottomStarts(int firstPiece) {
		FloatArray bottomStarts = new FloatArray();
		switch(firstPiece) {
			case 0:
				break;
			case 1:
				break;
			case 2:
				break;
			case 3:
				bottomStarts.add(relativeX(60f));
				bottomStarts.add(relativeY(347f));
				break;
			case 4:
				bottomStarts.add(relativeX(734f));
				bottomStarts.add(relativeY(347f));
				
				bottomStarts.add(relativeX(375f));
				bottomStarts.add(relativeY(584f));
				
				bottomStarts.add(relativeX(48f));
				bottomStarts.add(relativeY(584f));
				break;
			case 5:
				bottomStarts.add(relativeX(739f));
				bottomStarts.add(relativeY(347f));
				
				bottomStarts.add(relativeX(441f));
				bottomStarts.add(relativeY(164f));
				
				bottomStarts.add(relativeX(33f));
				bottomStarts.add(relativeY(164f));
				break;
			case 6:
				bottomStarts.add(relativeX(756f));
				bottomStarts.add(relativeY(584f));

				bottomStarts.add(relativeX(380f));
				bottomStarts.add(relativeY(339f));
				
				bottomStarts.add(relativeX(48f));
				bottomStarts.add(relativeY(339f));
				break;
			case 7:
				bottomStarts.add(relativeX(729f));
				bottomStarts.add(relativeY(158f));
				
				bottomStarts.add(relativeX(435f));
				bottomStarts.add(relativeY(347f));
				
				bottomStarts.add(relativeX(37f));
				bottomStarts.add(relativeY(347f));
			default:
		}
		return bottomStarts;
	}

	private FloatArray getTopPoints(int currentPiece, int pieceOn) {//From Left To Right
		FloatArray topPoints = new FloatArray();
		switch(currentPiece) {
			case 0:
				break;
			case 1:
				break;
			case 2:
				break;
			case 3:
				topPoints.add(relativeOffsetX(804f, pieceOn));
				topPoints.add(relativeY(279f));
				break;
			case 4:
				//Before Bend
				topPoints.add(relativeOffsetX(130f, pieceOn));
				topPoints.add(relativeY(279f));
				//After Bend
				topPoints.add(relativeOffsetX(498f, pieceOn));
				topPoints.add(relativeY(513f));
				topPoints.add(relativeOffsetX(816f, pieceOn));
				topPoints.add(relativeY(513f));
				break;
			case 5:
				//Before Bend
				topPoints.add(relativeOffsetX(133f, pieceOn));
				topPoints.add(relativeY(279f));
				//After Bend
				topPoints.add(relativeOffsetX(428f, pieceOn));
				topPoints.add(relativeY(96f));
				topPoints.add(relativeOffsetX(831f, pieceOn));
				topPoints.add(relativeY(96f));
				break;
			case 6:
				//Before Bend
				topPoints.add(relativeOffsetX(113f, pieceOn));
				topPoints.add(relativeY(516f));
				//After Bend
				topPoints.add(relativeOffsetX(489f, pieceOn));
				topPoints.add(relativeY(279f));
				topPoints.add(relativeOffsetX(816f, pieceOn));
				topPoints.add(relativeY(279f));
				break;
			case 7:
				//Before Bend
				topPoints.add(relativeOffsetX(140f, pieceOn));
				topPoints.add(relativeY(90f));
				//After Bend
				topPoints.add(relativeOffsetX(426f, pieceOn));
				topPoints.add(relativeY(269f));
				topPoints.add(relativeOffsetX(827f, pieceOn));
				topPoints.add(relativeY(269f));
				break;
			default:
		}
		return topPoints;
	}
	
	private FloatArray getBottomPoints(int currentPiece , int pieceOn) {//From Right To Left
		FloatArray bottomPoints = new FloatArray();
		switch(currentPiece) {
			case 0:
				break;
			case 1:
				break;
			case 2:
				break;
			case 3:
				bottomPoints.add(relativeOffsetX(804f, pieceOn));
				bottomPoints.add(relativeY(347f));
				break;
			case 4:
				//After Bend
				bottomPoints.add(relativeOffsetX(816f, pieceOn));
				bottomPoints.add(relativeY(584f));
				bottomPoints.add(relativeOffsetX(484f, pieceOn));
				bottomPoints.add(relativeY(584f));
				//Before Bend
				bottomPoints.add(relativeOffsetX(125f, pieceOn));
				bottomPoints.add(relativeY(347f));
				break;
			case 5:
				//After Bend
				bottomPoints.add(relativeOffsetX(831f, pieceOn));
				bottomPoints.add(relativeY(164f));
				bottomPoints.add(relativeOffsetX(423f, pieceOn));
				bottomPoints.add(relativeY(164f));
				//Before Bend
				bottomPoints.add(relativeOffsetX(128f, pieceOn));
				bottomPoints.add(relativeY(347f));
				break;
			case 6:
				//After Bend
				bottomPoints.add(relativeOffsetX(816f, pieceOn));
				bottomPoints.add(relativeY(347f));
				bottomPoints.add(relativeOffsetX(484f, pieceOn));
				bottomPoints.add(relativeY(347f));
				//Before Bend
				bottomPoints.add(relativeOffsetX(108f, pieceOn));
				bottomPoints.add(relativeY(584f));
				break;
			case 7:
				//After Bend
				bottomPoints.add(relativeOffsetX(827f, pieceOn));
				bottomPoints.add(relativeY(347f));
				bottomPoints.add(relativeOffsetX(421f, pieceOn));
				bottomPoints.add(relativeY(347f));
				//Before Bend
				bottomPoints.add(relativeOffsetX(135f, pieceOn));
				bottomPoints.add(relativeY(158f));
				break;
			default:
		}
		return bottomPoints;
	}
	
	public int numOfParts() {
		return pieces.size;
	}
	
	@Override
	public void render(SpriteBatch batch) {
		batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 1.0f);
		TextureRegion reg = null;
		float relX = 0;
		float relY = 0;
		//Draw Starting Piece
		reg = coaxialPiece(pieces.get(0));
		relX = 0;
		batch.draw(reg.getTexture(), position.x + relX, position.y + relY,
				origin.x, origin.y, dimension.x, dimension.y, 
				scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
				reg.getRegionWidth(), reg.getRegionHeight(), true, false);
		//Draw Other Pieces
		relX += dimension.x;
		for(int i = 1; i < pieces.size; i++) {
			reg = coaxialPiece(pieces.get(i));
			batch.draw(reg.getTexture(), position.x + relX, position.y + relY,
					origin.x, origin.y, dimension.x, dimension.y, 
					scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
					reg.getRegionWidth(), reg.getRegionHeight(), false, false);
					relX += dimension.x;
		}
	}
	
	private TextureRegion coaxialPiece(int region) {
		switch(region) {
			case 0:
				return center;
			case 1:
				return top;
			case 2:
				return bottom;
			case 3:
				return centerEnd;
			case 4:
				return centerBottomEnd;
			case 5:
				return centerTopEnd;
			case 6:
				return bottomCenterEnd;
			case 7:
				return topCenterEnd;
			default:
				return center;
		}
	}

	/**
	 * Returns the vertices of a convex polygon for 
	 * each of the parts that make up the track piece.
	 * 
	 * getConvexVertices[i].length is the number of
	 * parts, and each vertex is stored in there in
	 * x, y order.
	 * @return convex vertices of track piece
	 * @see 
	 */
	public float[][] getConvexVertices() {
		return convexVertices;
	}
	
	/**
	 * Returns an array of vertices in x, y order
	 * corresponding to the bounds of the polygon
	 * around the track piece.
	 * 
	 * @return track piece bounding vertices
	 */
	@Override
	public float[] getVertices(){
		return vertices;
	}
}