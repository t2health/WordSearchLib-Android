/**
 * 
 */
package com.t2.wordsearch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * @author wes
 * 
 */
public class WordsearchCellLayout extends LinearLayout {

	public WordsearchCellLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		setWillNotDraw(false);
	}

	public WordsearchCellLayout(Context context) {
		super(context);
		setWillNotDraw(false);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint p = new Paint();
		p.setStyle(Style.FILL);
		p.setColor(0xFF000000);
		canvas.drawRect(canvas.getClipBounds(), p);
	}
}
