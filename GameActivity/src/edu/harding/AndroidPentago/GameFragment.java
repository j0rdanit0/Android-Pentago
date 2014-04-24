package edu.harding.AndroidPentago;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import edu.harding.androidtictactoe.R;

public class GameFragment extends Fragment {
	
	private FrameLayout[] mBoardImages = new FrameLayout[6];
	
	private int[] mViewIds = new int[4];
	
	// Indicates if game is currently over or not
	private boolean mGameOver = false;
	
	private boolean mPlacePiece = true;
	
	private int mQuadrant = 0;
	
	private AudioPlayer mAudioPlayer = new AudioPlayer(); 
	
	// Whose turn to go first
	private char mGoFirst = PentagoGame.PLAYER_1;
	
	// Whose turn is it
	private char mTurn = PentagoGame.PLAYER_2;    
	
	private int mHumanWins = 0;
	private int mComputerWins = 0;
	private int mTies = 0;
	
	// Represents the internal state of the game
	private PentagoGame mGame;
	
	private Chronometer mChronometer;
	
	private AlertDialog.Builder pvpNamesDialog;
	private AlertDialog.Builder aiNamesDialog;
	
	private TextView mInfoTextView; 
	private TextView mHumanScoreTextView;
	private TextView mComputerScoreTextView;
	private TextView mTieScoreTextView;
	
	private ImageView mClockwiseImage;
	private ImageView mCounterClockwiseImage;
	
	private DrawableSurface mDrawingView;
	
	private SharedPreferences mPrefs;
	
	private boolean mSoundOn;
	
	private ImageView mImages[] = new ImageView[36];
	
	private String mInfoText;
	private String mPlayer1Name;
	private String mPlayer2Name;
	
	private char[] mBoard;
	
	private boolean mPvP = true;
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Cause onCreateOptionsMenu to trigger
		setHasOptionsMenu(true);
		
		// Retain this fragment across configuration changes
		// This fragment is explicitly storing/restoring its own state instead.
	    //setRetainInstance(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		
		// Load from xml file
		inflater.inflate(R.menu.game_options, menu);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_game, container, false);
		
		// Clear prefs for testing
		//mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity()); 
		//mPrefs.edit().clear().commit();
		
		final View pvpNamesView = inflater.inflate(R.layout.pvp_dialog, null);
		final View aiNameView = inflater.inflate(R.layout.ai_dialog, null);
		
		pvpNamesDialog = new AlertDialog.Builder(getActivity());
		pvpNamesDialog.setView(pvpNamesView)
			.setPositiveButton(R.string.ok_button, 
					new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				EditText player1Name = (EditText) pvpNamesView.findViewById(R.id.player1Name); 
				EditText player2Name = (EditText) pvpNamesView.findViewById(R.id.player2Name);
				
				mPlayer1Name = player1Name.getText().toString();
				mPlayer2Name = player2Name.getText().toString();
				
				if(mPlayer1Name.trim().equals("")) {
					mPlayer1Name = "Player 1";
				}
				
				if(mPlayer2Name.trim().equals("")) {
					mPlayer2Name = "Player 2";
				}
			}
		}).setNegativeButton(R.string.cancel_button, 
				new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent i = new Intent(getActivity(), MainActivity.class);
				startActivity(i);
				
			}
		}).create();
		
		aiNamesDialog = new AlertDialog.Builder(getActivity());
		aiNamesDialog.setView(inflater.inflate(R.layout.ai_dialog, null))
			.setPositiveButton(R.string.ok_button, 
					new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				EditText playerName = (EditText) aiNameView.findViewById(R.id.playerName); 
				
				mPlayer1Name = playerName.getText().toString();
				mPlayer2Name = "Android";
				
				if(mPlayer1Name.trim().equals("")) {
					mPlayer1Name = "Player 1";
				}
				
				if(mPlayer2Name.trim().equals("")) {
					mPlayer2Name = "Player 2";
				}
				
			}
		}).setNegativeButton(R.string.cancel_button, 
				new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent i = new Intent(getActivity(), MainActivity.class);
				startActivity(i);
				
			}
		}).create();

	
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setSingleChoiceItems(new CharSequence[] {"PvP", "AI"} , 0, 
				new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(which == 0) {
					mPvP = true;
				} else {
					mPvP = false;
				}
				
			}
		}).setPositiveButton(R.string.ok_button, 
					new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
					if(mPvP) {
						pvpNamesDialog.show();
					} else {
						aiNamesDialog.show();
					}
			}
		}).setNegativeButton(R.string.cancel_button, 
				new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		}).create();
		
		builder.show();
		
		if (mGame == null) {
			mGame = new PentagoGame();
		}
		
		mBoardImages[0] = (FrameLayout) v.findViewById(R.id.quadrant1);
		mBoardImages[1] = (FrameLayout) v.findViewById(R.id.quadrant2);
		mBoardImages[2] = (FrameLayout) v.findViewById(R.id.quadrant3);
		mBoardImages[3] = (FrameLayout) v.findViewById(R.id.quadrant4);
	
		int topLeft = Gravity.TOP | Gravity.LEFT;
		int topCenter = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
		int topRight = Gravity.TOP | Gravity.RIGHT;
		
		int midLeft = Gravity.CENTER_VERTICAL | Gravity.LEFT;
		int midCenter = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;
		int midRight = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
		
		int botLeft = Gravity.BOTTOM | Gravity.LEFT;
		int botCenter = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
		int botRight = Gravity.BOTTOM | Gravity.RIGHT;
		
		int[] imageGravity = {topLeft, topCenter, topRight, 
				midLeft, midCenter, midRight, botLeft, botCenter, botRight, 
				topLeft, topCenter, topRight, midLeft, midCenter, midRight, 
				botLeft, botCenter, botRight,
				topLeft, topCenter, topRight, midLeft, midCenter, midRight, 
				botLeft, botCenter, botRight,
				topLeft, topCenter, topRight, midLeft, midCenter, midRight, 
				botLeft, botCenter, botRight};
		
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels / 9;
		int height = width;
		
		for (int i = 0; i < mImages.length; i++)
		{
			mImages[i] = new ImageView(getActivity());
			mImages[i].setImageResource(R.drawable.blank);
			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams
					(width, height);
			params.gravity = imageGravity[i];
			
			mImages[i].setLayoutParams(params);
			mImages[i].setOnTouchListener(mTouchListener);
			mImages[i].setId(i);
		}
		
		for(int j = 0; j < mImages.length; j++) {
			int index = j / 9;
			mBoardImages[index].addView(mImages[j]);
			mBoardImages[index].bringChildToFront(mImages[j]);
		}
		
		updateImages();
		
		mClockwiseImage = (ImageView)v.findViewById(R.id.clockwise);
		mCounterClockwiseImage = (ImageView)v.findViewById(R.id.counterClockwise);
		
		mClockwiseImage.setOnTouchListener(mTouchListener);
		mCounterClockwiseImage.setOnTouchListener(mTouchListener);
		
		mChronometer = (Chronometer)v.findViewById(R.id.chronometer);

       	//mInfoTextView = (TextView) v.findViewById(R.id.information);               	
        //mHumanScoreTextView = (TextView) v.findViewById(R.id.player_score);
        //mComputerScoreTextView = (TextView) v.findViewById(R.id.ai_score);
        //mTieScoreTextView = (TextView) v.findViewById(R.id.tie_score);   
                
        loadPreferences();
    	
        
        if (savedInstanceState == null) {   
        	startNewGame();
        }
        else {        	
        	// Restore the game's state
        	// The same thing can be accomplished with onRestoreInstanceState
        	mGame.setBoardState(savedInstanceState.getCharArray("board"));
        	mGameOver = savedInstanceState.getBoolean("mGameOver");        	
        	//updateInfoText(savedInstanceState.getCharSequence("info").toString());
        	mTurn = savedInstanceState.getChar("mTurn");
        	mGoFirst = savedInstanceState.getChar("mGoFirst");
        	
        	// If it's the computer's turn, the previous turn did not take, so go again  
        	if (!mGameOver && mTurn == PentagoGame.PLAYER_2) {        		
        		int move = mGame.getComputerMove();
        		setMove(PentagoGame.PLAYER_2, move);
        	}        	
        }       
        
        
        
        //updateInfoText(mInfoText);
                
        //displayScores();
		
		return v;
	}
	
	
	
	@Override
	public void onStop() {
       super.onStop();
              
       // Save the current score, but not the state of the current game        
       SharedPreferences.Editor ed = mPrefs.edit();
       ed.putInt("mHumanWins", mHumanWins);
       ed.putInt("mComputerWins", mComputerWins);
       ed.putInt("mTies", mTies);
       ed.commit(); 
	}
	
	
	// Warning: This func is called when fragment is retained (setRetainInstance), 
	// but the savedInstanceState value will always be null!
	@Override
	public void onSaveInstanceState(Bundle outState) {		
		super.onSaveInstanceState(outState);		
		outState.putCharArray("board", mGame.getBoardState());		
		outState.putBoolean("mGameOver", mGameOver);	
		outState.putCharSequence("info", mInfoTextView.getText());
		outState.putChar("mGoFirst", mGoFirst);
		outState.putChar("mTurn", mTurn);		
	}
	
	// Handles menu item selections 
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.new_game:
        	startNewGame();
            return true;
        case R.id.settings: 
        	startActivityForResult(new Intent(getActivity(), Settings.class), 0);     	
        	return true;
       /* case R.id.reset_scores:
        	mHumanWins = 0;
        	mComputerWins = 0;
            mTies = 0;
            displayScores();
            return true;
        case R.id.about:
        	showDialog(DIALOG_ABOUT);
        	return true;
       */
        }
        return false;
    }
    
    private void loadPreferences() {
    	
    	// Restore the scores from the persistent preference data source
    	if (mPrefs == null)
    		mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity()); 
        
    	mHumanWins = mPrefs.getInt("mHumanWins", 0);  
        mComputerWins = mPrefs.getInt("mComputerWins", 0);
        mTies = mPrefs.getInt("mTies", 0);
        
    	mSoundOn = mPrefs.getBoolean(Settings.SOUND_PREFERENCE_KEY, true);
    	
    	//mBoardView1.setBoardColor(mPrefs.getInt(Settings.BOARD_COLOR_PREFERENCE_KEY, Color.GRAY));
    	//mBoardView1.invalidate(); mBoardView2.invalidate(); mBoardView3.invalidate(); mBoardView4.invalidate();    // Repaint with new color
    	
    	String difficultyLevel = mPrefs.getString(Settings.DIFFICULTY_PREFERENCE_KEY, 
    			getResources().getString(R.string.difficulty_harder));
    	
    	if (difficultyLevel.equals(getResources().getString(R.string.difficulty_easy)))
    		mGame.setDifficultyLevel(PentagoGame.DifficultyLevel.Easy);
    	else if (difficultyLevel.equals(getResources().getString(R.string.difficulty_harder)))
    		mGame.setDifficultyLevel(PentagoGame.DifficultyLevel.Harder);
    	else
    		mGame.setDifficultyLevel(PentagoGame.DifficultyLevel.Expert); 
    	
    	String goes_first = mPrefs.getString(Settings.GOES_FIRST_PREFERENCE_KEY,
    			getResources().getString(R.string.goes_first_alternate));
    	if (!goes_first.equals(getResources().getString(R.string.goes_first_alternate))) {
    		// See if any moves have been made.  If not, start a new game
    		// which will use the selected setting
    		if (mGame.boardIsClear()) {    			
    			Handler handler = new Handler();     		
        		handler.postDelayed(new Runnable() {
                    public void run() {
                    	startNewGame();                             	
                    } 
         		}, 1000);       		
    		}
    	}    	
    }
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		// See if Back button was pressed on Settings activity
        if (requestCode == Activity.RESULT_CANCELED) {
        	// Apply potentially new settings      
        	
        	loadPreferences();	    	
        }
	}
	
	// Show the scores
    /*private void displayScores() {
    	mHumanScoreTextView.setText(Integer.toString(mHumanWins));
    	mComputerScoreTextView.setText(Integer.toString(mComputerWins));
    	mTieScoreTextView.setText(Integer.toString(mTies));
    }*/
    
    // Set up the game board. 
    private void startNewGame() {   	
    	
    	mGame.newGame();  
    	mPlacePiece = true;
    	for (int i = 0; i < mImages.length; i++)
		{
			mImages[i].setImageResource(R.drawable.blank);
		}
    	//mBoardView1.invalidate(); mBoardView2.invalidate(); mBoardView3.invalidate(); mBoardView4.invalidate();   // Redraw the board    	

    	// Determine who should go first based on settings
    	String goesFirst = mPrefs.getString(Settings.GOES_FIRST_PREFERENCE_KEY, 
    			getResources().getString(R.string.goes_first_alternate));
    	
    	if (goesFirst.equals(getResources().getString(R.string.goes_first_alternate))) {
    		// Alternate who goes first
    		if (mGoFirst == PentagoGame.PLAYER_2) {    		
        		mGoFirst = PentagoGame.PLAYER_1;
        		mTurn = PentagoGame.PLAYER_2;        		
        	}
        	else {
        		mGoFirst = PentagoGame.PLAYER_2;
        		mTurn = PentagoGame.PLAYER_1;
        	}	
    	}
    	else if (goesFirst.equals(getResources().getString(R.string.goes_first_human))) 
    		mTurn = PentagoGame.PLAYER_1;    	
    	else
    		mTurn = PentagoGame.PLAYER_2;
    	
    	// Start the game
    	//if (mTurn == PentagoGame.PLAYER_2) {
    		//updateInfoText(R.string.first_computer);
    		//int move = mGame.getComputerMove();
    		//setMove(PentagoGame.PLAYER_2, move);
    	//}
    	//else
    		//updateInfoText(R.string.first_human);    	
    	
    	mGameOver = false;
    } 
    
    /*private void updateInfoText(int textResourceId) {
    	mInfoTextView.setText(textResourceId);
    	mInfoText = mInfoTextView.getText().toString();
    }
    
    private void updateInfoText(String text) {
    	mInfoText = text;
    	mInfoTextView.setText(mInfoText);
    }*/
    
    // Make a move
    private boolean setMove(char player, int location) {
    	
    	/*if (player == PentagoGame.PLAYER_2) {    		
    		// Make the computer move after a delay of 1 second
    		final int loc = location;
	    	Handler handler = new Handler();     		
    		handler.postDelayed(new Runnable() {
                public void run() {
                	mGame.setMove(PentagoGame.PLAYER_2, loc);
                	//mBoardView1.invalidate(); mBoardView2.invalidate(); mBoardView3.invalidate(); mBoardView4.invalidate();   // Redraw the board
                	
                	try {
                		if (mSoundOn)
                			mAudioPlayer.play(getActivity(), R.raw.sword);
                	}
                	catch (IllegalStateException e) {};  // Happens if orientation changed before playing
                	
                	int winner = mGame.checkForWinner();
                	if (winner == 0) {
                		mTurn = PentagoGame.PLAYER_1;	                                	
                		//updateInfoText(R.string.turn_human);
                	}
                	else 
    	            	endGame(winner);                              	
                } 
     		}, 1000);     		
                
    		return true;
    	}*/
    	
    	
    	int winner = mGame.checkForWinner();
    	
    	if(player == PentagoGame.PLAYER_2 && mGame.setMove(PentagoGame.PLAYER_2, location)) {
    		
        	//mBoardView1.invalidate(); mBoardView2.invalidate(); mBoardView3.invalidate(); mBoardView4.invalidate();   // Redraw the board
    	   	if (mSoundOn) 
    	   		mAudioPlayer.play(getActivity(), R.raw.sword);	   	
    	   	return true;
    	}
    	else if (mGame.setMove(PentagoGame.PLAYER_1, location)) { 
    		
        	//mBoardView1.invalidate(); mBoardView2.invalidate(); mBoardView3.invalidate(); mBoardView4.invalidate();   // Redraw the board
    	   	if (mSoundOn) 
    	   		mAudioPlayer.play(getActivity(), R.raw.swish);	   	
    	   	return true;
    	}
    		   	    	
    	return false;
    }
    
    
    
    // Game is over logic
    private void endGame(int winner) {
    	if (winner == 3) {
    		mTies++;
    		Toast.makeText(getActivity(), "It's a tie!", Toast.LENGTH_LONG).show();
    		//mTieScoreTextView.setText(Integer.toString(mTies));
    		//updateInfoText(R.string.result_tie); 
    	} 
    	else if (winner == 1) {
    		mHumanWins++;
    		Toast.makeText(getActivity(), mPlayer1Name + " won!", Toast.LENGTH_LONG).show();
    		//mHumanScoreTextView.setText(Integer.toString(mHumanWins));
    		//String defaultMessage = getResources().getString(R.string.result_human_wins);
    		//updateInfoText(mPrefs.getString(Settings.VICTORY_MESSAGE_PREFERENCE_KEY, 
    				//defaultMessage));
    		mAudioPlayer.play(getActivity(), R.raw.cheer);
    	}
    	else if (winner == 2) {
    		mComputerWins++;
    		Toast.makeText(getActivity(), mPlayer2Name + " won!", Toast.LENGTH_LONG).show();
    		//mComputerScoreTextView.setText(Integer.toString(mComputerWins));
    		//updateInfoText(R.string.result_computer_wins);
    		mAudioPlayer.play(getActivity(), R.raw.lose);
    	}
    	
    	mGameOver = true;
    }
    
    private void updateImages()
    {
    	mBoard = mGame.getBoard();
    	for (int i = 0; i < mGame.BOARD_SIZE; i++)
    	{
    		if(mBoard[i] == mGame.PLAYER_1)
    		{
    			mImages[i].setImageResource(R.drawable.white_piece);
    		}
    		else if (mBoard[i] == mGame.PLAYER_2)
    		{
    			mImages[i].setImageResource(R.drawable.black_piece);
    		}
    		else
    		{
    			mImages[i].setImageResource(R.drawable.blank);
    		}
    	}
    }
    
    private void makeComputerMove() {
    	int place = mGame.getRandomPlace();
    	
    	setMove(PentagoGame.PLAYER_2, place);
    	updateImages();
		
		/*try {
		    Thread.sleep(1000);
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}*/
		
		mGame.makeRotation(mGame.getRandomQuadrant(), mGame.getRandomDirection());
		updateImages();
		mTurn = mGame.PLAYER_1;
	
    }
    
    // Listen for touches on the board
    private OnTouchListener mTouchListener = new OnTouchListener() {
    	
    	@Override
        public boolean onTouch(View v, MotionEvent event) {
    		if(mGame.boardIsClear()) {
    			mChronometer.setBase(SystemClock.elapsedRealtime());
    			mChronometer.start();
    		}
    		if(mTurn == mGame.PLAYER_1 || (mTurn == mGame.PLAYER_2 && mPvP))
    		{
	       	    int viewId = v.getId();
	
	       	    if (viewId == R.id.clockwise || viewId == R.id.counterClockwise)
	       	    {
	       	    	mGame.makeRotation(mQuadrant, viewId == R.id.clockwise);

					mClockwiseImage.setVisibility(View.INVISIBLE);
					mCounterClockwiseImage.setVisibility(View.INVISIBLE);
					
					mPlacePiece = true;
					updateImages();
					
					if(mTurn == mGame.PLAYER_1) {
						mTurn = mGame.PLAYER_2;
					}
					else {
						mTurn = mGame.PLAYER_1;
					}
					if(!mPvP && !mGameOver) {
			    		makeComputerMove();
			    	}
	       	    }
	       	   
	       	    else
	       	    {
			    	for (int i = 0; i < mImages.length; i++)
			    	{
			    		if (mImages[i].getId() == viewId)
			    		{
			    			if(mPlacePiece) {
				    			if(!mGameOver && mTurn == PentagoGame.PLAYER_1 &&
				    	    			setMove(PentagoGame.PLAYER_1, i)) {
					    			Bitmap b = BitmapFactory.decodeResource(getResources(), 
					    					R.drawable.white_piece);
					    			mImages[i].setImageBitmap(b);
					    			mPlacePiece = !mPlacePiece;
				    			}
				    			else if(!mGameOver && mTurn == PentagoGame.PLAYER_2 &&
				    	    			setMove(PentagoGame.PLAYER_2, i)) {
				    				Bitmap b = BitmapFactory.decodeResource(getResources(), 
					    					R.drawable.black_piece);
					    			mImages[i].setImageBitmap(b);
					    			mPlacePiece = !mPlacePiece;
				    			}
			    			}
			    			else {
			    				
			    				mClockwiseImage.setVisibility(View.VISIBLE);
			    				mCounterClockwiseImage.setVisibility(View.VISIBLE);
			    				
			    				 if(i <= 8) {
			    					 mQuadrant = 1;
			    				 } else if(i <= 17) {
			    					 mQuadrant = 2;
			    				 } else if(i <= 26) {
			    					 mQuadrant = 3;
			    				 } else if(i <=35) {
			    					 mQuadrant = 4;
			    				 }
			    			}
			    		}
			    	}
	       	    }
				
		    	/*if (!mGameOver && mTurn == TicTacToeGame.HUMAN_PLAYER &&
		    			setMove(TicTacToeGame.HUMAN_PLAYER, pos)) {        		
	            	
	            	// If no winner yet, let the computer make a move
	            	int winner = mGame.checkForWinner();
	            	if (winner == 0) { 
	            		//updateInfoText(R.string.turn_computer); 
	            		int move = mGame.getComputerMove();
	            		setMove(TicTacToeGame.COMPUTER_PLAYER, move);            		
	            	} 
	            	else
	            		endGame(winner);            	
	            }*/
		    	
		    	if (!mGameOver)
		    	{
			    	int winner = mGame.checkForWinner();
			    	if (winner != 0)
			    	{
			    		mChronometer.stop();
			    		endGame(winner);
			    	}
		    	}
		    	
		    	
		    	
		    	// So we aren't notified of continued events when finger is moved
		    	  
    		}	
    		return false;
        }
    };
	
}
