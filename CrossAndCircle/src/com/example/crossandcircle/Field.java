package com.example.crossandcircle;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Field extends org.andengine.entity.sprite.TiledSprite {
	
	private FieldType mType;
	private boolean mSelected;

	public Field(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
		mSelected = false;
		mType = FieldType.EMPTY;
	}

	public FieldType getType() {
		return mType;
	}

	public void setType(FieldType t) {
		this.mType = t;
		if(t == FieldType.CIRCLE)
			this.setCurrentTileIndex(1);
		else 
			this.setCurrentTileIndex(2);
	}
	
	public boolean isSelected() {
		return mSelected;
	}

	public void setSelected(boolean s) {
		this.mSelected = s;
	}
}
