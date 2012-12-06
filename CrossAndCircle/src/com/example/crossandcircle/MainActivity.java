package com.example.crossandcircle;

import java.io.IOException;
import java.io.InputStream;
import java.util.EventObject;

import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.debug.Debug;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.input.touch.TouchEvent;

public class MainActivity extends SimpleBaseGameActivity implements FieldClickedListener {

	private static int CAMERA_WIDTH = 480;
	private static int CAMERA_HEIGHT = 800;
	
	private ITiledTextureRegion mTTxtRegStates;
	
	private Field[] mFields;
	private FieldType mRound;
	private int[][] mWinningPatterns = {
			{0, 1, 2},
			{3, 4, 5},
			{6, 7, 8},
			{0, 3, 6},
			{1, 4, 7},
			{2, 5, 8},
			{0, 4, 8},
			{2, 4, 6}
	};
	
	public MainActivity()
	{
		mRound = FieldType.CIRCLE;
	}
	
	private boolean checkWinningCondition()
	{
		for(int i = 0; i < mWinningPatterns.length; i++)
		{
			int x = mWinningPatterns[i][0];
			int y = mWinningPatterns[i][1];
			int z = mWinningPatterns[i][2];
			
			int sum = mFields[x].getType().getValue() + mFields[y].getType().getValue() + mFields[z].getType().getValue();
			if(sum == 3 || sum == -3) return true;
		}
		return false;
	}
	
	@Override
	public void fieldClicked(Field field) {
		field.setType(mRound);
		
		if(checkWinningCondition()) Debug.e("WIIIIIIIN");
		
		if(mRound == FieldType.CROSS || mRound == FieldType.EMPTY)
		{
			mRound = FieldType.CIRCLE;
			
		}
		else
		{
			mRound = FieldType.CROSS;
		}
	}
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, 
		    new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
	}

	@Override
	protected void onCreateResources() {
		ITexture states = null;
		
		try
		{
			
			states = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
				
				@Override
				public InputStream open() throws IOException {
					return getAssets().open("gfx/states.png");
				}
			});
			
			states.load();
		}
		catch(IOException e)
		{
			Debug.e(e);
		}
		
		this.mTTxtRegStates = TextureRegionFactory.extractTiledFromTexture(states, 3, 1);
	}

	@Override
	protected Scene onCreateScene() {
		final Scene scene = new Scene();
		
		mFields = new Field[9];
		

		
		for(int i = 0; i < mFields.length; i++) {
			int x = 0, y = 0;
			
			if(i < 3) {
				x = i * 133;
				y = 0;
			}
			else if(i < 6) {
				x = (i - 3) * 133;
				y = 133;
			}
			else if(i < 9) {
				x = (i - 6) * 133;
				y = 266;
			}
			
			mFields[i] = new Field(x, y, this.mTTxtRegStates, getVertexBufferObjectManager()) {
				@Override
			    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
					if(this.getType() != FieldType.EMPTY) return false;
					this.fireFieldClickedEvent();
			        return true;
			    }
			};
			mFields[i].registerFieldClickedListener(this);
			scene.registerTouchArea(mFields[i]);
			scene.attachChild(mFields[i]);
		}
		
		scene.setTouchAreaBindingOnActionDownEnabled(true);
		
		return scene;
	}
}
