package edu.harding.AndroidPentago;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PentagoDatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "pentago_database.db";
	private static final int DATABASE_VERSION = 1;
	
	public PentagoDatabaseHelper(Context context) {		
		super(context, DATABASE_NAME, null, DATABASE_VERSION); 
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		String createSql1 = "create table players(" +
				"playerName TEXT PRIMARY KEY, " +
				"playerWins INTEGER NOT NULL, " +
				"playerLosses INTEGER NOT NULL, " +
				"playerTies INTEGER NOT NULL, " +
				"playerTime INTEGER " +
				");";
		String createSql2 = "create table player_record(" +
				"player1 TEXT NOT NULL REFERENCES players(playerName), " +
				"player2 TEXT NOT NULL REFERENCES players(playerName), " +
				"player1Wins INTEGER NOT NULL, " +
				"player1Losses INTEGER NOT NULL, " +
				"player1Ties INTEGER NOT NULL, " +
				"player1Time INTEGER NOT NULL, " +
				"PRIMARY KEY(player1, player2)" +
				");";
				
		db.execSQL(createSql1);
		db.execSQL(createSql2);

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}
	
	public List<String> getNames() {
		String[] args = {"playerName"};
		List<String> names = new ArrayList<String>();
		Cursor wrapped = getReadableDatabase().query("players", args, null, null, null, null, "playerName");
		wrapped.moveToFirst();
		while(!wrapped.isAfterLast()){
			names.add(wrapped.getString(0));
			
			wrapped.moveToNext();
		}
		wrapped.close();
		return names;
	}
	
	public boolean addName(String name) {
		boolean success = true;
		String[] args = {"playerName"};
		Cursor wrapped = getReadableDatabase().rawQuery("SELECT * FROM players WHERE EXISTS (SELECT * FROM players WHERE playerName = \"" + name + "\")", null);
		if(wrapped.moveToFirst()) {
			success = false;
		}
		if(success == true) {
			ContentValues values = new ContentValues();
			values.put("playerName", name);
			values.put("PlayerWins", 0);
			values.put("PlayerLosses", 0);
			values.put("PlayerTies", 0);
			values.put("PlayerTime", 0);
			getWritableDatabase().insert("players", null, values);
		}
		wrapped.close();
		return success;
	}
	
	public PlayerCursor getTopTen() {
		String[] args = {"playerName", "playerWins", "playerLosses", "playerTies", "playerTime"};
		Cursor wrapped = getReadableDatabase().query("players", args, null, null, null, null, "playerWins DESC");
		return new PlayerCursor(wrapped);
	}
	
	public PlayerCursor getSingleRecord(String player) {
		String args[] = {"playerName", "playerWins", "playerLosses", "playerTies", "playerTime"};
		Cursor wrapped = getReadableDatabase().query("players", args, "playerName = \"" + player + "\"", null, null, null, null);
		wrapped.moveToFirst();
		return new PlayerCursor(wrapped);
	}
	
	
	public PlayerCursor getVS(String player1, String player2) {
		String[] args = {"player1", "player1Wins", "player1Losses", "player1Ties", "player1Time"};
		Cursor wrapped = getReadableDatabase().query("player_record", args, "player1 = \"" + player1 + "\"" + " AND player2 = \"" + player2 + "\"", null, null, null, null);
		wrapped.moveToFirst();
		return new PlayerCursor(wrapped);
	}
	
	public void updatePlayerWins(String name) {
		String sql = "UPDATE players SET playerWins = playerWins+1 WHERE playerName = '" + name + "'";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(sql);
		db.close();
	}
	
	public void updatePlayerLosses(String name) {
		String sql = "UPDATE players SET playerLosses = playerLosses+1 WHERE playerName = '" + name + "'";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(sql);
		db.close();	}
	
	public void updatePlayerTies(String name) {
		String sql = "UPDATE players SET playerTies = playerTies+1 WHERE playerName = '" + name + "'";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(sql);
		db.close();	}
	
	public void updatePlayerTime(String name, int time) {
		String sql = "UPDATE players SET playerTime = " + time + " WHERE playerName = '" + name + "'";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(sql);
		db.close();	}
	
	public void updateVsWins(String player1, String player2) {
		String sql = "UPDATE player_record SET player1wins = player1wins+1 WHERE player1 = '" + player1  + "'" + " AND player2 = '" + player2 + "'";
		SQLiteDatabase db = getWritableDatabase();
		Cursor c = db.query("player_record", null, "player1 = '" + player1  + "'" + " AND player2 = '" + player2 + "'", null, null, null, null, null);
		if (c.moveToFirst()) {
			db.execSQL(sql);
		}else {
			ContentValues values = new ContentValues();
			values.put("player1", player1);
			values.put("player2", player2);
			values.put("player1Wins", 1);
			values.put("player1Losses", 0);
			values.put("player1Ties", 0);
			values.put("player1Time", 0);
			getWritableDatabase().insert("player_record", null, values);
		}
		
		db.close();	}
	
	public void updateVsLosses(String player1, String player2) {
		String sql = "UPDATE player_record SET player1Losses = player1Losses+1 WHERE player1 = '" + player1 + "'" + " AND player2 = '" + player2 + "'";
		SQLiteDatabase db = getWritableDatabase();
		Cursor c = db.query("player_record", null, "player1 = '" + player1  + "'" + " AND player2 = '" + player2 + "'", null, null, null, null, null);
		if (c.moveToFirst()) {
			db.execSQL(sql);
		}else {
			ContentValues values = new ContentValues();
			values.put("player1", player1);
			values.put("player2", player2);
			values.put("player1Wins", 0);
			values.put("player1Losses", 1);
			values.put("player1Ties", 0);
			values.put("player1Time", 0);
			getWritableDatabase().insert("player_record", null, values);
		}
		db.close();	}
	
	public void updateVsTies(String player1, String player2) {
		String sql = "UPDATE player_record SET player1Ties = player1Ties+1 WHERE player1 = '" + player1 + "'" + " AND player2 = '" + player2 + "'";
		SQLiteDatabase db = getWritableDatabase();
		Cursor c = db.query("player_record", null, "player1 = '" + player1  + "'" + " AND player2 = '" + player2 + "'", null, null, null, null, null);
		if (c.moveToFirst()) {
			db.execSQL(sql);
		}else { 
			ContentValues values = new ContentValues();
			values.put("player1", player1);
			values.put("player2", player2);
			values.put("player1Wins", 0);
			values.put("player1Losses", 0);
			values.put("player1Lies", 1);
			values.put("player1Time", 0);
			getWritableDatabase().insert("player_record", null, values);
		}
		db.close();	}
	
	public void updateVsTime(String player1, String player2, int time) {
		String sql = "UPDATE player_record SET player1Time = " + time + " WHERE player1 = '" + player1 + "'" + " AND player2 = '" + player2 + "'";
		SQLiteDatabase db = getWritableDatabase();
		Cursor c = db.query("player_record", null, "player1 = '" + player1  + "'" + " AND player2 = '" + player2 + "'", null, null, null, null, null);
		if (c.moveToFirst()) {
			db.execSQL(sql);
		}else {
			ContentValues values = new ContentValues();
			values.put("player1", player1);
			values.put("player2", player2);
			values.put("player1Wins", 0);
			values.put("player1Losses", 0);
			values.put("player1Ties", 0);
			values.put("player1Time", time);
			getWritableDatabase().insert("player_record", null, values);
		}
		db.close();	}
	
	public static class PlayerCursor extends CursorWrapper {
		public PlayerCursor(Cursor c) {
			super(c);
		}
		
		public PlayerRecord getPlayerRecord() {
			PlayerRecord record = new PlayerRecord();
			record.name = getString(0);
			record.wins = getInt(1);
			record.losses = getInt(2);
			record.ties = getInt(3);
			record.time = getInt(4);
			return record;
		}
	}

}
