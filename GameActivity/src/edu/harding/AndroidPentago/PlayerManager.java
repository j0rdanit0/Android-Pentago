package edu.harding.AndroidPentago;

import java.util.List;

import android.content.Context;
import edu.harding.AndroidPentago.PentagoDatabaseHelper.PlayerCursor;

public class PlayerManager {
	
	private PentagoDatabaseHelper mHelper;
	private Context mAppContext;
	private static PlayerManager sPlayerManager;
	
	private PlayerManager(Context appContext) {
		mAppContext = appContext;
		mHelper = new PentagoDatabaseHelper(mAppContext);
		
	}
	
	public static PlayerManager get(Context c) {
		if (sPlayerManager == null) {
			sPlayerManager = new PlayerManager(c.getApplicationContext());
		}
		return sPlayerManager;
	}
	
	public List<String> getNames() {
		return mHelper.getNames();
	}
	
	public boolean addName(String name) {
		return mHelper.addName(name);
	}
	
	public PlayerRecord[] getTopTen() {
		PlayerRecord[] records = new PlayerRecord[10];
		PlayerCursor players = mHelper.getTopTen();
		int count = 0;
		players.moveToFirst();
		while (!players.isAfterLast() && count < 10) {
			records[count] = players.getPlayerRecord();
			count++;
			players.moveToNext();
		}
		return records;
	}
	
	public PlayerRecord getSingleRecord(String player) {
		return mHelper.getSingleRecord(player).getPlayerRecord();
	}
	
	public PlayerRecord getVS(String player1, String player2) {
		return mHelper.getVS(player1, player2).getPlayerRecord();
	}
	
	public void updatePlayerWins(String name) {
		mHelper.updatePlayerWins(name);
	}
	
	public void updatePlayerLosses(String name) {
		mHelper.updatePlayerLosses(name);
	}
	
	public void updatePlayerTies(String name) {
		mHelper.updatePlayerTies(name);
	}
	
	public void updatePlayerTime(String name, int time) {
		mHelper.updatePlayerTime(name, time);
	}
	
	public void updateVsWins(String player1, String player2) {
		mHelper.updateVsWins(player1, player2);
	}
	
	public void updateVsLosses(String player1, String player2) {
		mHelper.updateVsLosses(player1, player2);
	}
	
	public void updateVsTies(String player1, String player2) {
		mHelper.updateVsTies(player1, player2);
	}
	
	public void updateVsTime(String player1, String player2, int time) {
		mHelper.updateVsTime(player1, player2, time);
	}
}
