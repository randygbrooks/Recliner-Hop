package rangbro.reclinerhop.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.IntArray;

public class TrackPartsGenerator {//This Generates Track Pieces
							//And the Distance between them.
	private static final int PROB_OF_MIDDLE = 5; //Out of 10
	
	public IntArray getCoaxialSequence() {
		int lastHeight = -1; //0 Bottom, 1 Middle, 2 Top
		int lastPiece = -1;
		int firstPiece = -1;
		IntArray currentList = new IntArray();
		//Get Starting Piece
		firstPiece = getStartPiece();
		lastHeight = getStartHeight(firstPiece);
		currentList.add(firstPiece);
		//Get Rest Of Pieces
		while (!isEndPiece(lastPiece)) {
			lastPiece = getNextPiece(lastHeight);
			lastHeight = getHeight(lastPiece);
			currentList.add(lastPiece);
		}
		return currentList;
	}
	
	private int getHeight(int lastPiece) {
		switch(lastPiece) {
			case 1:
				return 2;
			case 2:
				return 0;
			case 4:
				return 0;
			case 5:
				return 2;
			default:
				return 1;
		}
	}

	private int getStartPiece() {
		return (MathUtils.random(4) + 3);
	}

	private int getStartHeight(int firstPiece) {
		switch(firstPiece) {
		case 6:
			return 0;
		case 7:
			return 2;
		default:
			return 1;
		}
	}

	private int getNextPiece(int lastHeight) {
		boolean isMiddle = (MathUtils.random(9) < PROB_OF_MIDDLE);
		if (isMiddle) {
			switch(lastHeight) {
				case 0:
					return 2;
				case 1:
					return 0;
				case 2:
					return 1;
			}
		} else if (lastHeight == 0) {
			return 6;
		} else if (lastHeight == 2) {
			return 7;
		} else {
			return (MathUtils.random(2) + 3);
		}
		return -1;
	}

	private boolean isEndPiece(int lastPiece) {
		return (lastPiece > 2);
	}
}
