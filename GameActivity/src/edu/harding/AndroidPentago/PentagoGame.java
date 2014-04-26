package edu.harding.AndroidPentago;

import java.util.Random;

import android.graphics.Point;
import android.util.Log;


public class PentagoGame {

	// The computer's difficulty levels 
	public enum DifficultyLevel {Easy, Harder, Expert};
	
	// Current difficulty level
	private DifficultyLevel mDifficultyLevel = DifficultyLevel.Expert;   
	
	public static final int BOARD_SIZE = 36;
	
	public static final int MAX_MOVES = 36; 
	
	// Characters used to represent the human, computer, and open spots
	public static final char PLAYER_1 = '1';
	public static final char PLAYER_2 = '2';
	public static final char OPEN_SPOT = '0';
	
	// Random number generator
	private Random mRand; 
		
	// The game board
	private char mBoard[];
	
	private int mMoves = 0;
		
	public PentagoGame() {
		mBoard = new char[BOARD_SIZE];		
		mRand = new Random();
	}
	
	/** Clear the board of all X's and O's. */
	public void newGame() {
		// Reset all locations
    	for (int i = 0; i < BOARD_SIZE; i++) {
    		mBoard[i] = OPEN_SPOT;    		   
    	}
    	mMoves = 0;
	}
	
	public char[] getBoard()
	{
		return mBoard;
	}
	
	/** 
	 * Set the given player at the given location on the game board.
	 * The location must be available, or the board will not be changed.
	 * 
	 * @param player - The human or computer player
	 * @param location - The location (0-8) to place the move
	 * 
	 * @return The true if the move was made, false otherwise.
	 */
	public boolean setMove(char player, int location, boolean confirmed) {
		if (location >= 0 && location < BOARD_SIZE &&
				mBoard[location] == OPEN_SPOT) {
			if(confirmed) {
				mBoard[location] = player;
				mMoves++;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Return the board occupant (HUMAN_PLAYER, COMPUTER_PLAYER,
	 * or OPEN_SPOT) for the given location or '?' if an invalid
	 * location is given.
	 * 
	 * @param location - A value between 0 and 8
	 * @return The board occupant
	 */
	public char getBoardOccupant(int location) {
		if (location >= 0 && location < BOARD_SIZE)
			return mBoard[location];
		return '?';
	}
	
	/**
	 * Check for a winner.  Return a status value indicating the board status.
	 * @return Return 0 if no winner or tie yet, 1 if it's a tie, 2 if X won,
	 * or 3 if O won.
	 */
	/*public int checkForWinner() {
		
		// Check horizontal wins
		for (int i = 0; i <= 6; i += 3) 
		{
			if (mBoard[i] == PLAYER_1 && 
				mBoard[i+1] == PLAYER_1 &&
				mBoard[i+2]== PLAYER_1)
				return 2;
			if (mBoard[i] == PLAYER_2 && 
				mBoard[i+1]== PLAYER_2 && 
				mBoard[i+2] == PLAYER_2)
				return 3;
		}
	
		// Check vertical wins
		for (int i = 0; i <= 2; i++) 
		{
			if (mBoard[i] == PLAYER_1 && 
				mBoard[i+3] == PLAYER_1 && 
				mBoard[i+6]== PLAYER_1)
				return 2;
			if (mBoard[i] == PLAYER_2 && 
				mBoard[i+3] == PLAYER_2 && 
				mBoard[i+6]== PLAYER_2)
				return 3;
		}
	
		// Check for diagonal wins
		if ((mBoard[0] == PLAYER_1 &&
			 mBoard[4] == PLAYER_1 && 
			 mBoard[8] == PLAYER_1) ||
			(mBoard[2] == PLAYER_1 && 
			 mBoard[4] == PLAYER_1 &&
			 mBoard[6] == PLAYER_1))
			return 2;
		if ((mBoard[0] == PLAYER_2 &&
			 mBoard[4] == PLAYER_2 && 
			 mBoard[8] == PLAYER_2) ||
			(mBoard[2] == PLAYER_2 && 
			 mBoard[4] == PLAYER_2 &&
			 mBoard[6] == PLAYER_2))
			return 3;
	
		// Check for tie
		if (spaceAvailable())
			return 0;
	
		// If we make it through the previous loop, all places are taken, so it's a tie
		return 1;
	}*/
	
	public int checkForWinner()
    {
        boolean res = true;
        boolean p1w = false;
        boolean p2w = false;
        boolean tie = false;

        if (mMoves >= 9) // First check to see if it's even possible to win (Fifth move for player 1)
        {
            // Check for horizontal win. If no win, continue to checking vert and diag.
            int horiz = CheckHorizontals();
            if (horiz == 0) // No one won on a horizontal. Check for verticals.
            {

            }
            else if (horiz == 1) // Player 1 won on a horizontal
            {
                p1w = true;
                res = false;
            }
            else if (horiz == 2) // Player 2 wins on a horizontal
            {
                p2w = true;
                res = false;
            }
            else
            {
                tie = true;
                res = false;
            }

            int vert = CheckVerticals();

            if (vert == 0) // No one won on a vertical. Check for diagonals.
            {

            }
            else if (vert == 1) // Player 1 won on a vertical
            {
                p1w = true;
                res = false;
            }
            else if (vert == 2) // Player 2 won on a vertical
            {
                p2w = true;
                res = false;
            }
            else // vert is 3 (A tie was caused by the move)
            {
                tie = true;
                res = false;
            }

            int diag = CheckDiags();
            if (diag == 0) // No one won on a diagonal. Check to see if it's possible to make more moves.
            {
            }
            else if (diag == 1) // Player 1 won on a diagonal
            {
                p1w = true;
                res = false;
            }
            else if (diag == 2) // Player 2 won on a diagonal
            {
                p2w = true;
                res = false;
            }
            else // diag is 3 (A tie was caused by the move)
            {
                tie = true;
                res = false;
            }


            if (res && mMoves < MAX_MOVES)
                return 0; // The game continues
            if (tie || (p1w && p2w))
                return 3;
            if (p1w)
                return 1;
            if (p2w)
                return 2;
            if (mMoves == MAX_MOVES)
                return 3;
        }
        return 0;
    }

    private int CheckHorizontals()
    {
        boolean res = true;
        boolean p1w = false;
        boolean p2w = false;

        int returnValue = 0;
        short[] possibilities = new short[12];
//        possibilities[0] = (short)CheckPiecesOnBoard(new Point(0, 0), new Point(0, 1), new Point(0, 2), new Point(0, 3), new Point(0, 4));
        possibilities[0] = (short)CheckPiecesOnBoard(new Point(0, 0), new Point(0, 1), new Point(0, 2), new Point(1, 3), new Point(1, 4));
        possibilities[1] = (short)CheckPiecesOnBoard(new Point(0, 1), new Point(0, 2), new Point(1, 3), new Point(1, 4), new Point(1, 5));
        possibilities[2] = (short)CheckPiecesOnBoard(new Point(0, 3), new Point(0, 4), new Point(0, 5), new Point(2, 0), new Point(2, 1));
        possibilities[3] = (short)CheckPiecesOnBoard(new Point(0, 4), new Point(0, 5), new Point(2, 0), new Point(2, 1), new Point(2, 2));
        possibilities[4] = (short)CheckPiecesOnBoard(new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 3), new Point(2, 4));
        possibilities[5] = (short)CheckPiecesOnBoard(new Point(1, 1), new Point(1, 2), new Point(2, 3), new Point(2, 4), new Point(2, 5));
        possibilities[6] = (short)CheckPiecesOnBoard(new Point(3, 0), new Point(3, 1), new Point(3, 2), new Point(4, 3), new Point(4, 4));
        possibilities[7] = (short)CheckPiecesOnBoard(new Point(3, 1), new Point(3, 2), new Point(4, 3), new Point(4, 4), new Point(4, 5));
        possibilities[8] = (short)CheckPiecesOnBoard(new Point(3, 3), new Point(3, 4), new Point(3, 5), new Point(5, 0), new Point(5, 1));
        possibilities[9] = (short)CheckPiecesOnBoard(new Point(3, 4), new Point(3, 5), new Point(5, 0), new Point(5, 1), new Point(5, 2));
        possibilities[10] = (short)CheckPiecesOnBoard(new Point(4, 0), new Point(4, 1), new Point(4, 2), new Point(5, 3), new Point(5, 4));
        possibilities[11] = (short)CheckPiecesOnBoard(new Point(4, 1), new Point(4, 2), new Point(5, 3), new Point(5, 4), new Point(5, 5));

        short s;
        
        for (int i = 0; i < possibilities.length; i++)
        {
        	s = possibilities[i];
            if (s == 1)
            {
                p1w = true;
                res = false;
            }
            if (s == 2)
            {
                p2w = true;
                res = false;
            }
        }

        if (res)
            return 0;
        if (p1w && p2w)
            return 3;
        if (p1w)
            return 1;
        if (p2w)
            return 2;
        return returnValue;
    }

    private int CheckVerticals()
    {
        boolean res = true;
        boolean p1w = false;
        boolean p2w = false;

        int returnValue = 0;
        short[] possibilities = new short[12];

        possibilities[0] = (short)CheckPiecesOnBoard(new Point(0, 0), new Point(0, 3), new Point(1, 0), new Point(3, 0), new Point(3, 3));
        possibilities[1] = (short)CheckPiecesOnBoard(new Point(0, 3), new Point(1, 0), new Point(3, 0), new Point(3, 3), new Point(4, 0));
        possibilities[2] = (short)CheckPiecesOnBoard(new Point(0, 1), new Point(0, 4), new Point(1, 1), new Point(3, 1), new Point(3, 4));
        possibilities[3] = (short)CheckPiecesOnBoard(new Point(0, 4), new Point(1, 1), new Point(3, 1), new Point(3, 4), new Point(4, 1));
        possibilities[4] = (short)CheckPiecesOnBoard(new Point(0, 2), new Point(0, 5), new Point(1, 2), new Point(3, 2), new Point(3, 5));
        possibilities[5] = (short)CheckPiecesOnBoard(new Point(0, 5), new Point(1, 2), new Point(3, 2), new Point(3, 5), new Point(4, 2));
        possibilities[6] = (short)CheckPiecesOnBoard(new Point(1, 3), new Point(2, 0), new Point(2, 3), new Point(4, 3), new Point(5, 0));
        possibilities[7] = (short)CheckPiecesOnBoard(new Point(2, 0), new Point(2, 3), new Point(4, 3), new Point(5, 0), new Point(5, 3));
        possibilities[8] = (short)CheckPiecesOnBoard(new Point(1, 4), new Point(2, 1), new Point(2, 4), new Point(4, 4), new Point(5, 1));
        possibilities[9] = (short)CheckPiecesOnBoard(new Point(2, 1), new Point(2, 4), new Point(4, 4), new Point(5, 1), new Point(5, 4));
        possibilities[10] = (short)CheckPiecesOnBoard(new Point(1, 5), new Point(2, 2), new Point(2, 5), new Point(4, 5), new Point(5, 2));
        possibilities[11] = (short)CheckPiecesOnBoard(new Point(2, 2), new Point(2, 5), new Point(4, 5), new Point(5, 2), new Point(5, 5));

        short s;
        
        for (int i = 0; i < possibilities.length; i++)
        {
        	s = possibilities[i];
            if (s == 1)
            {
                p1w = true;
                res = false;
            }
            if (s == 2)
            {
                p2w = true;
                res = false;
            }
        }

        if (res)
            return 0;
        if (p1w && p2w)
            return 3;
        if (p1w)
            return 1;
        if (p2w)
            return 2;
        return returnValue;
    }

    private int CheckDiags()
    {
        boolean res = true;
        boolean p1w = false;
        boolean p2w = false;

        int returnValue = 0;
        short[] possibilities = new short[8];

        possibilities[0] = (short)CheckPiecesOnBoard(new Point(0, 0), new Point(0, 4), new Point(1, 2), new Point(4, 3), new Point(5, 1));
        possibilities[1] = (short)CheckPiecesOnBoard(new Point(5, 5), new Point(0, 4), new Point(1, 2), new Point(4, 3), new Point(5, 1));
        possibilities[2] = (short)CheckPiecesOnBoard(new Point(4, 0), new Point(3, 4), new Point(3, 2), new Point(2, 3), new Point(2, 1));
        possibilities[3] = (short)CheckPiecesOnBoard(new Point(1, 5), new Point(3, 4), new Point(3, 2), new Point(2, 3), new Point(2, 1));
        possibilities[4] = (short)CheckPiecesOnBoard(new Point(4, 1), new Point(3, 5), new Point(4, 3), new Point(2, 4), new Point(2, 2));
        possibilities[5] = (short)CheckPiecesOnBoard(new Point(3, 3), new Point(3, 1), new Point(1, 2), new Point(2, 0), new Point(1, 4));
        possibilities[6] = (short)CheckPiecesOnBoard(new Point(0, 1), new Point(0, 5), new Point(2, 3), new Point(4, 4), new Point(5, 2));
        possibilities[7] = (short)CheckPiecesOnBoard(new Point(0, 3), new Point(1, 1), new Point(3, 2), new Point(5, 0), new Point(5, 4));

        short s;
        
        for (int i = 0; i < possibilities.length; i++)
        {
        	s = possibilities[i];
            if (s == 1)
            {
                p1w = true;
                res = false;
            }
            if (s == 2)
            {
                p2w = true;
                res = false;
            }
        }

        if (res)
            return 0;
        if (p1w && p2w)
            return 3;
        if (p1w)
            return 1;
        if (p2w)
            return 2;
        return returnValue;

    }

    private int CheckPiecesOnBoard(Point piece1, Point piece2, Point piece3, Point piece4, Point piece5)
    {
        int playerAtPiece1 = mBoard[(short)piece1.x *6 + (short)piece1.y];
        int playerAtPiece2 = mBoard[(short)piece2.x *6 + (short)piece2.y];
        int playerAtPiece3 = mBoard[(short)piece3.x *6 + (short)piece3.y];
        int playerAtPiece4 = mBoard[(short)piece4.x *6 + (short)piece4.y];
        int playerAtPiece5 = mBoard[(short)piece5.x *6 + (short)piece5.y];

        if ((playerAtPiece1 == playerAtPiece2) && (playerAtPiece2 == playerAtPiece3) && (playerAtPiece3 == playerAtPiece4) && (playerAtPiece4 == playerAtPiece5))
        {
            return playerAtPiece1 - 48;
        }
        return 0;
    }
	
	/**
	 * Determines if there is at least one spot available in the board.
	 * @return true if there is at least one spot available
	 */
	public boolean spaceAvailable() {
		
		// See if there is an open spot
		for (int i = 0; i < BOARD_SIZE; i++) 
			if (mBoard[i] == OPEN_SPOT)
				return true;		
		
		// All spaces must be occupied
		return false;
	}
	
	/**
	 * Determines if the entire board is clear.
	 * @return true if the entire board is clear
	 */
	public boolean boardIsClear() {
		
		// See if there is an open spot
		for (int i = 0; i < BOARD_SIZE; i++)
			if (mBoard[i] != OPEN_SPOT) 
				return false;				
			
		// All spots must be open
		return true;
	}
	
	public void makeRotation(int quadrant, boolean clockwise)
	{
		if (clockwise)
		{
			rotateClockwise(quadrant);
		}
		else
		{
			rotateCounterClockwise(quadrant);
		}
	}
	
	private void rotateClockwise(int quadrant)
    {
    	int topLeftPos;
    	if (quadrant == 1)
    	{
    		topLeftPos = 0;
    	}
    	else if (quadrant == 2)
    	{
    		topLeftPos = 9;
    	}
    	else if (quadrant == 3)
    	{
    		topLeftPos = 18;
    	}
    	else
    	{
    		topLeftPos = 27;
    	}
    	
    	char temp = mBoard[topLeftPos];
    	
    	mBoard[topLeftPos] = mBoard[topLeftPos + 6];
    	mBoard[topLeftPos + 6] = mBoard[topLeftPos + 8];
    	mBoard[topLeftPos + 8] = mBoard[topLeftPos + 2];
    	mBoard[topLeftPos + 2] = temp;
    	
    	temp = mBoard[topLeftPos + 1];
    	mBoard[topLeftPos + 1] = mBoard[topLeftPos + 3];
    	mBoard[topLeftPos + 3] = mBoard[topLeftPos + 7];
    	mBoard[topLeftPos + 7] = mBoard[topLeftPos + 5];
    	mBoard[topLeftPos + 5] = temp;
    }
	
	private void rotateCounterClockwise(int quadrant)
    {
    	int topLeftPos;
    	if (quadrant == 1)
    	{
    		topLeftPos = 0;
    	}
    	else if (quadrant == 2)
    	{
    		topLeftPos = 9;
    	}
    	else if (quadrant == 3)
    	{
    		topLeftPos = 18;
    	}
    	else
    	{
    		topLeftPos = 27;
    	}
    	
    	char temp = mBoard[topLeftPos];
    	
    	mBoard[topLeftPos] = mBoard[topLeftPos + 2];
    	mBoard[topLeftPos + 2] = mBoard[topLeftPos + 8];
    	mBoard[topLeftPos + 8] = mBoard[topLeftPos + 6];
    	mBoard[topLeftPos + 6] = temp;
    	
    	temp = mBoard[topLeftPos + 1];
    	mBoard[topLeftPos + 1] = mBoard[topLeftPos + 5];
    	mBoard[topLeftPos + 5] = mBoard[topLeftPos + 7];
    	mBoard[topLeftPos + 7] = mBoard[topLeftPos + 3];
    	mBoard[topLeftPos + 3] = temp;
    }
	
	/** Get the AI's difficulty level.
	 * 
	 * @return The AI's difficulty level.
	 */
	public DifficultyLevel getDifficultyLevel() {
		return mDifficultyLevel;
	}
	
	/** Set the difficulty level. 
	 * 
	 * @param difficultyLevel
	 */
	public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
		mDifficultyLevel = difficultyLevel;
	}
	
	/** Return the best move for the computer to make.
	 * You must call setMove() to actually make the computer
	 * move to that location. 
	 * @return The best move for the computer to make.
	 */
	public int getComputerMove() {
		
		int move = -1;
    	
    	
    		//move = getRandomMove();
    	
    	/*else if (mDifficultyLevel == DifficultyLevel.Harder) {
    		move = getWinningMove();
    		if (move == -1)
    			move = getRandomMove();
    	}
    	else if (mDifficultyLevel == DifficultyLevel.Expert) {
    	
    		// Try to win, but if that's not possible, block.
    		// If that's not possible, move anywhere.
    		move = getWinningMove();
    		if (move == -1)
    			move = getBlockingMove();
    		if (move == -1)
    			move = getRandomMove();
    	}*/
    	
    	return move;
	}
	
	 public int getRandomPlace() {
     	
     	// Generate random move
     	int move;
     	do {
     		move = mRand.nextInt(36);
     	} while (mBoard[move] == PLAYER_1 ||
     			mBoard[move] == PLAYER_2);
     	return move;
     }
	 
     public int getRandomQuadrant() {
    	 int move;
      	
      		move = mRand.nextInt(4);
      	
      	return move + 1;
     }
     
     public boolean getRandomDirection() {
    	 return mRand.nextBoolean();
     }
	 
     private int getBlockingMove() {
     	
     	// See if there's a move I can make to block X from winning
     	for (int i = 0; i < BOARD_SIZE; i++) {
     		char curr = mBoard[i];
     		
     		if (curr != PLAYER_1 && curr != PLAYER_2) {
     			// What if X moved here?
     			mBoard[i] = PLAYER_1;   
     			if (checkForWinner() == 1) {
     				mBoard[i] = OPEN_SPOT;   // Restore space
     				return i;
     			}
     			else
     				mBoard[i] = OPEN_SPOT;
     		}
     	}
     	
     	// No blocking move is possible
     	return -1;
     }
     
     private int getWinningMove() {
     	
     	// See if there's a move I can make to win
     	for (int i = 0; i < BOARD_SIZE; i++) {
     		char curr = mBoard[i];
     		
     		if (curr != PLAYER_1 && curr != PLAYER_2) {
     			// What if O moved here?
     			mBoard[i] = PLAYER_2;   
     			if (checkForWinner() == 2) {
     				mBoard[i] = OPEN_SPOT;   // Restore space
     				return i;
     			}
     			else
     				mBoard[i] = OPEN_SPOT;
     		}
     	}
     	
     	// No winning move is possible
     	return -1;
     }
     
     public char[] getBoardState() { 		
    	 return mBoard;
     }
 	
     public void setBoardState(char[] board) {
    	 mBoard = board.clone();
     }
     
     @Override
     public String toString() {
    	 return mBoard[0] + "|" + mBoard[1] + "|" + mBoard[2] + "\n" +
    	 	mBoard[3] + "|" + mBoard[4] + "|" + mBoard[5] + "\n" +
    	 	mBoard[6] + "|" + mBoard[7] + "|" + mBoard[8];
    	 
     }
}
