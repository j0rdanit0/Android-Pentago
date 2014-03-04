package edu.harding.AndroidPentago;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class DrawableSurface extends View {
	
	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public DrawableSurface(Context context) {
		super(context);
	}
	
	public DrawableSurface(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public DrawableSurface(Context context, AttributeSet attrs) {
	    super(context, attrs);
	}

	@Override
	public void onDraw(Canvas canvas)
	{
		mPaint.setColor(Color.BLUE);
		canvas.drawRect(0, 0, 50, 50, mPaint);
	}
	
}
