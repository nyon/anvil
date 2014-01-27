package com.ggf.anvil.scene2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class LabelBox extends Label {

	public LabelBox(CharSequence text, LabelStyle style) {
		super(text, style);
		// TODO Auto-generated constructor stub
	}
	
	static public class LabelBoxStyle {
		public BitmapFont font;
		/** Optional. */
		public Color fontColor;
		/** Optional. */
		public Drawable background;

		public LabelBoxStyle () {
		}

		public LabelBoxStyle (BitmapFont font, Color fontColor) {
			this.font = font;
			this.fontColor = fontColor;
		}

		public LabelBoxStyle (LabelStyle style) {
			this.font = style.font;
			if (style.fontColor != null) fontColor = new Color(style.fontColor);
			background = style.background;
		}
	}
	
}
