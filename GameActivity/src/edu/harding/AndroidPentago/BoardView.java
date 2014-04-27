package edu.harding.AndroidPentago;

import android.view.View;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

public class BoardView extends View  {
	
	// Dimensions are set in onSizeChanged
	private int mBoardWidth;
	private int mBoardHeight;
	private int mBoardCellWidth;
	private int mBoardCellHeight;
	
	private static int mCount = 0;
	
	private int mBoardGridWidth = 6;
	private int mBoardColor = Color.GRAY;
	
	// We need a reference to the game so we can draw the board
	private PentagoGame mGame;
			
	private Bitmap mHumanBitmap;
	private Bitmap mComputerBitmap;
	
	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public BoardView(Context context) {
		super(context);		
		initialize();
	}
	
	public BoardView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);		 
		initialize();
	}

	public BoardView(Context context, AttributeSet attrs) {
	    super(context, attrs);	            	
    	initialize();
	}

	public void initialize() {   	
		mHumanBitmap = BitmapFactory.decodeResource(getResources(), 
               R.drawable.white_piece); 
		mComputerBitmap = BitmapFactory.decodeResource(getResources(), 
               R.drawable.black_piece);			 	    	
	} 

	public void setGame(PentagoGame game) {
		mGame = game;
	}
	
	public int getBoardWidth() {
		return mBoardWidth;
	}
	
	public int getBoardHeight() {
		return mBoardHeight;
	}
	
	public int getBoardCellWidth() {
		return mBoardCellWidth;
	}
	
	public int getBoardCellHeight() {
		return mBoardCellHeight;
	}
	
	public void setBoardColor(int boardColor) {
		mBoardColor = boardColor;
	}
	
	public int getBoardColor() {
		return mBoardColor;
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			
	   	// w and h contain the new width and height	   	
	   	mBoardHeight = h;
		mBoardWidth = w;
		
		mBoardCellWidth = mBoardWidth / 3;
    	mBoardCellHeight = mBoardHeight / 3;
	}	
	
	@Override
	public void onDraw(Canvas canvas) {
		
		mCount %= 4;
		
		int h = canvas.getHeight();
		int w = canvas.getWidth();
		 super.onDraw(canvas);		 

		 int halfGridWidth = mBoardGridWidth / 2;
		 		
		 mPaint.setColor(mBoardColor);
         mPaint.setStyle(Paint.Style.FILL);
         
         Paint dividerLines = new Paint(Paint.ANTI_ALIAS_FLAG);
         dividerLines.setColor(Color.BLUE);
         dividerLines.setStyle(Paint.Style.FILL);
         
         // Draw Quadrant
         canvas.drawRect(5, (mBoardCellHeight) - halfGridWidth,
        		 mBoardWidth - 10, (mBoardCellHeight) + halfGridWidth, mPaint);
         canvas.drawRect(5, mBoardCellHeight * 2 - halfGridWidth,
        		 mBoardWidth - 10, mBoardCellHeight * 2 + halfGridWidth, mPaint);
         canvas.drawRect((mBoardCellWidth) - halfGridWidth , 5, 
        		 (mBoardCellWidth) + halfGridWidth, mBoardHeight - 10, mPaint);
         canvas.drawRect(mBoardCellWidth * 2 - halfGridWidth, 5, 
        		 mBoardCellWidth * 2 + halfGridWidth, mBoardHeight - 10, mPaint);
         
         canvas.drawRect(5, (mBoardCellHeight) - halfGridWidth,
        		 mBoardWidth - 10, (mBoardCellHeight) + halfGridWidth, mPaint);
         canvas.drawRect(5, mBoardCellHeight * 2 - halfGridWidth,
        		 mBoardWidth - 10, mBoardCellHeight * 2 + halfGridWidth, mPaint);
         canvas.drawRect((mBoardCellWidth) - halfGridWidth , 5, 
        		 (mBoardCellWidth) + halfGridWidth, mBoardHeight - 10, mPaint);
         canvas.drawRect(mBoardCellWidth * 2 - halfGridWidth, 5, 
        		 mBoardCellWidth * 2 + halfGridWidth, mBoardHeight - 10, mPaint);
         
         canvas.drawRect(5, (mBoardCellHeight) - halfGridWidth,
        		 mBoardWidth - 10, (mBoardCellHeight) + halfGridWidth, mPaint);
         canvas.drawRect(5, mBoardCellHeight * 2 - halfGridWidth,
        		 mBoardWidth - 10, mBoardCellHeight * 2 + halfGridWidth, mPaint);
         canvas.drawRect((mBoardCellWidth) - halfGridWidth , 5, 
        		 (mBoardCellWidth) + halfGridWidth, mBoardHeight - 10, mPaint);
         canvas.drawRect(mBoardCellWidth * 2 - halfGridWidth, 5, 
        		 mBoardCellWidth * 2 + halfGridWidth, mBoardHeight - 10, mPaint);
         
         canvas.drawRect(5, (mBoardCellHeight) - halfGridWidth,
        		 mBoardWidth - 10, (mBoardCellHeight) + halfGridWidth, mPaint);
         canvas.drawRect(5, mBoardCellHeight * 2 - halfGridWidth,
        		 mBoardWidth - 10, mBoardCellHeight * 2 + halfGridWidth, mPaint);
         canvas.drawRect((mBoardCellWidth) - halfGridWidth , 5, 
        		 (mBoardCellWidth) + halfGridWidth, mBoardHeight - 10, mPaint);
         canvas.drawRect(mBoardCellWidth * 2 - halfGridWidth, 5, 
        		 mBoardCellWidth * 2 + halfGridWidth, mBoardHeight - 10, mPaint);
         

         //Draw Dividing Lines
         /*canvas.drawRect((float)(mBoardCellWidth * 1.5) - halfGridWidth , 2, 
        		 (float)(mBoardCellWidth * 1.5) + halfGridWidth, mBoardHeight, dividerLines);
         canvas.drawRect(0, (float)(mBoardCellHeight * 1.5) - halfGridWidth,
        		 mBoardWidth, (float)(mBoardCellHeight * 1.5) + halfGridWidth, dividerLines);*/
 
		 // Draw all the pieces
		 for (int i = 0; i < PentagoGame.BOARD_SIZE; i++) {
			 boolean draw = false;
			 int col = i % 6;
			 int row = i / 6;
			 
			 
			 /*if(mCount == 0 && col <= 2 && row <= 2) {
				 draw=true;
			 } else if(mCount == 1 && col > 2 && row <= 2) {
				 col -= 3;
				 draw=true;
			 } else if(mCount == 2 && col <= 2 && row > 2) {
				 row -= 3;
				 draw=true;
			 } else if( mCount == 3 && col > 2 && row > 2) {
				 col -= 3;
				 row -= 3;
				 draw=true;
			 }*/
			 
			 draw = true;
			 
			 if(draw) {
				 // Define the boundaries of a destination rectangle for the image
				 int left = col * mBoardCellWidth + mBoardGridWidth;
				 int top = row * mBoardCellHeight + mBoardGridWidth;
				 int right = left + mBoardCellWidth - 10;
				 int bottom = top + mBoardCellHeight - mBoardGridWidth - 6;
				 				 
				 if (mGame != null && mGame.getBoardOccupant(i) == PentagoGame.PLAYER_1) {				 					 
					 canvas.drawBitmap(mHumanBitmap, 
							 null,  // src
							 new Rect(left, top, right, bottom),  // dest
							 null);
					 
				 }
				 else if (mGame != null && mGame.getBoardOccupant(i) == PentagoGame.PLAYER_2) {
					 canvas.drawBitmap(mComputerBitmap, 
							 null,  // src
							 new Rect(left, top, right, bottom),  // dest 
							 null);		
				 }
			 }
		 }	
		 
		 mCount++;
	 }
}
