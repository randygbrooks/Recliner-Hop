package rangbro.reclinerhop.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Disposable;
import rangbro.reclinerhop.util.Constants;

public class Assets implements Disposable, AssetErrorListener{
	
	public Texture gameover;
	public Texture background;
	public Music music;
	public Sound jumpSound;
	public Sound collectSound;
	public Sound exchangeSound;
	public Sound gameOverSound;
	public AssetFonts fonts;
	public AssetCoaxial coaxial;
	public AssetDollar dollar;
	public AssetDress dress;
	public AssetHorse horse;
	public AssetRecliner recliner;
	public AssetBox box;
	public AssetDecoration decoration;
	public AssetExchange exchange;
	
	public static final String TAG = Assets.class.getName();
	public static final Assets instance = new Assets();
	
	private AssetManager assetManager;
	
	private Assets() {}
	
	public class AssetFonts {
		public static final float SMALL_FONT = 0.75f;
		public static final float MEDIUM_FONT = 1.0f;
		public static final float BIG_FONT = 2.0f;
		
		public final BitmapFont defaultReallySmall;
		public final BitmapFont defaultSmall;
		public final BitmapFont defaultTiny;
		public final BitmapFont defaultBig;
	
		public AssetFonts() {
			defaultReallySmall = new BitmapFont(
					Gdx.files.internal(Constants.FONT), true);
			defaultSmall = new BitmapFont(
					Gdx.files.internal(Constants.FONT), true);
			defaultTiny = new BitmapFont(
					Gdx.files.internal(Constants.FONT), true);
			defaultBig = new BitmapFont(
					Gdx.files.internal(Constants.FONT), true);
			
			defaultReallySmall.getData().setScale(0.5f, 0.5f);
			defaultSmall.getData().setScale(0.75f, 0.75f);
			defaultTiny.getData().setScale(0.35f, 0.35f);
			defaultBig.getData().setScale(1.0f, 1.0f);
			
			defaultReallySmall.getRegion().getTexture().setFilter(
					TextureFilter.Linear, TextureFilter.Linear);
			defaultSmall.getRegion().getTexture().setFilter(
					TextureFilter.Linear, TextureFilter.Linear);
			defaultTiny.getRegion().getTexture().setFilter(
					TextureFilter.Linear, TextureFilter.Linear);
			defaultBig.getRegion().getTexture().setFilter(
					TextureFilter.Linear, TextureFilter.Linear);
		}
		
	}
	
	public void init(AssetManager assetManager) {
		this.assetManager = assetManager;
		assetManager.setErrorListener(this);
		//Load Images
		assetManager.load(Constants.GAME_OVER_TEXTURE, Texture.class);
		assetManager.load(Constants.BACKGROUND_TEXTURE, Texture.class);
		//Load Texture Atlases
		assetManager.load(Constants.COAXIAL_ATLAS, TextureAtlas.class);
		assetManager.load(Constants.DRESS_ATLAS, TextureAtlas.class);
		assetManager.load(Constants.MISC_ATLAS, TextureAtlas.class);
		//Finish Loading
		assetManager.finishLoading();
		Gdx.app.debug(TAG, "# of assets loaded: " 
		    + assetManager.getAssetNames().size);
		for (String name : assetManager.getAssetNames()) {
			Gdx.app.debug(TAG, "Asset: " + name);
		}
		
		TextureAtlas coaxialAtlas =
				assetManager.get(Constants.COAXIAL_ATLAS);
		TextureAtlas dressAtlas =
				assetManager.get(Constants.DRESS_ATLAS);
		TextureAtlas miscAtlas =
				assetManager.get(Constants.MISC_ATLAS);
		
		for (Texture t : coaxialAtlas.getTextures()) {
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		for (Texture t : dressAtlas.getTextures()) {
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		for (Texture t : miscAtlas.getTextures()) {
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		background = assetManager.get(Constants.BACKGROUND_TEXTURE);
		gameover = assetManager.get(Constants.GAME_OVER_TEXTURE);
		
		music = Gdx.audio.newMusic(Gdx.files.internal(Constants.BACKGROUND_MUSIC));
		collectSound = Gdx.audio.newSound(Gdx.files.internal(Constants.COLLECT_SOUND));
		jumpSound = Gdx.audio.newSound(Gdx.files.internal(Constants.JUMP_SOUND));
		exchangeSound = Gdx.audio.newSound(Gdx.files.internal(Constants.EXCHANGE_SOUND));
		gameOverSound = Gdx.audio.newSound(Gdx.files.internal(Constants.GAME_OVER_SOUND));
		
		fonts = new AssetFonts();
		coaxial = new AssetCoaxial(coaxialAtlas);
		dollar = new AssetDollar(miscAtlas);
		dress = new AssetDress(dressAtlas);
		horse = new AssetHorse(miscAtlas);
		recliner = new AssetRecliner(miscAtlas);
		decoration = new AssetDecoration(miscAtlas);
		exchange = new AssetExchange(miscAtlas);
		box = new AssetBox(miscAtlas);
	}
	
	
	public void error(String filename, @SuppressWarnings("rawtypes") Class type, Throwable throwable) {
		Gdx.app.error(TAG, "Couldn't Load Asset: '" + filename 
				+ "'", (Exception)throwable);
	}
	
	@Override
	public void error(@SuppressWarnings("rawtypes") AssetDescriptor asset, Throwable throwable) {
		Gdx.app.error(TAG, "Couldn't Load Asset: '" + asset.fileName
				+ "'", (Exception)throwable);
	}
	
	@Override
	public void dispose(){
		fonts.defaultReallySmall.dispose();
		fonts.defaultSmall.dispose();
		fonts.defaultTiny.dispose();
		fonts.defaultBig.dispose();
		assetManager.dispose();
	}

	public class AssetCoaxial {
		public final AtlasRegion center; //0
		public final AtlasRegion top; //1
		public final AtlasRegion bottom; //2
		public final AtlasRegion centerEnd; //3
		public final AtlasRegion centerBottomEnd; //4
		public final AtlasRegion centerTopEnd; //5
		public final AtlasRegion bottomCenterEnd; //6
		public final AtlasRegion topCenterEnd; //7
		
		public AssetCoaxial(TextureAtlas atlas) {
			center = atlas.findRegion("MMN");
			top = atlas.findRegion("HHN");
			bottom = atlas.findRegion("LLN");
			centerEnd = atlas.findRegion("MME");
			centerBottomEnd = atlas.findRegion("MLE");
			centerTopEnd = atlas.findRegion("MHE");
			bottomCenterEnd = atlas.findRegion("LME");
			topCenterEnd = atlas.findRegion("HME");
		}
	}
	
	public class AssetDollar {
		public final AtlasRegion dollar;
		
		public AssetDollar(TextureAtlas atlas) {
			dollar = atlas.findRegion("Dollar");
		}
	}
	
	public class AssetDress {
		public final AtlasRegion dress1;
		public final AtlasRegion dress2;
		public final AtlasRegion dress3;
		public final AtlasRegion dress4;
		public final AtlasRegion dress5;
		
		public AssetDress(TextureAtlas atlas) {
			dress1 = atlas.findRegion("Dress1");
			dress2 = atlas.findRegion("Dress2");
			dress3 = atlas.findRegion("Dress3");
			dress4 = atlas.findRegion("Dress4");
			dress5 = atlas.findRegion("Dress5");
		}
	}
	
	public class AssetHorse {
		public final AtlasRegion horse;
		
		public AssetHorse(TextureAtlas atlas){
			horse = atlas.findRegion("Horse");
		}
	}
	
	public class AssetRecliner {
		public final AtlasRegion reclinerShut;
		public final AtlasRegion reclinerMiddle;
		public final AtlasRegion reclinerOpen;
		
		public AssetRecliner(TextureAtlas atlas){
			reclinerShut = atlas.findRegion("Recliner3");
			reclinerMiddle = atlas.findRegion("Recliner2");
			reclinerOpen = atlas.findRegion("Recliner");
		}
	}
	
	public class AssetDecoration {
		public final AtlasRegion star;
		
		public AssetDecoration(TextureAtlas atlas){
			star = atlas.findRegion("Wooden Star");
		}
	}
	
	public class AssetExchange {
		public final AtlasRegion horseToMoney;
		public final AtlasRegion dressToHorse;
		
		public AssetExchange(TextureAtlas atlas){
			horseToMoney = atlas.findRegion("MTC");
			dressToHorse = atlas.findRegion("DTM");
		}
	}
	
	public class AssetBox {
		public final AtlasRegion box;
		
		public AssetBox(TextureAtlas atlas) {
			box = atlas.findRegion("Box");
		}
	}
}
