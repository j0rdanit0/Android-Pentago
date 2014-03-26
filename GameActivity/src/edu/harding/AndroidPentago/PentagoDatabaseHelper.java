package edu.harding.AndroidPentago;

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
		String createSql = "create table players(" +
				"playerName TEXT PRIMARY KEY" +
				"playerWins INTEGER NOT NULL" +
				"playerLosses INTEGER NOT NULL" +
				"playerTies INTEGER NOT NULL" +
				"playerTime INTEGER" +
				");" +
				"create table player_record(" +
				"player1 TEXT NOT NULL REFERENCES players(playerName)" +
				"player2 TEXT NOT NULL REFERENCES players(playerName)" +
				"player1Wins INTEGER NOT NULL" +
				"player1Losses INTEGER NOT NULL" +
				"player1Ties INTEGER NOT NULL" +
				"player1Time INTEGER NOT NULL" +
				"PRIMARY KEY(player1, player2)" +
				");";
				
		db.execSQL(createSql);

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}
	
	public String[] getNames() {
		String[] args = {"playerName"};
		String[] names = {};
		Cursor wrapped = getReadableDatabase().query("players", args, null, null, null, null, "playerName");
		int count = 0;
		while(!wrapped.isAfterLast()){
			names[count] = wrapped.getString(0);
			count++;
			wrapped.moveToNext();
		}
		wrapped.close();
		return names;
	}
	
	public boolean addName(String name) {
		boolean success = false;
		String[] args = {"playerName"};
		Cursor wrapped = getReadableDatabase().query("players", args, "playerName =" + name, null, null, null, null);
		if(wrapped.moveToFirst()) {
			success = true;
		}
		if(success == true) {
			ContentValues values = new ContentValues();
			values.put("playerName", name);
			values.put("wins", 0);
			values.put("losses", 0);
			values.put("ties", 0);
			values.put("time", 0);
			getWritableDatabase().insert("players", null, values);
		}
		wrapped.close();
		return success;
	}
	
	public PlayerCursor getTopTen() {
		String[] args = {"playerName", "playerWins"};
		Cursor wrapped = getReadableDatabase().query("players", args, null, null, null, null, "playerWins");
		wrapped.close();
		return new PlayerCursor(wrapped);
	}
	
	public PlayerCursor getVS(String player1, String player2) {
		String[] args = {"player1Wins", "player1Losses", "player1Ties", "player1Time"};
		Cursor wrapped = getReadableDatabase().query("player_record", args, "player1 = '" + player1 + "'" + " AND player2 = '" + player2 + "'", null, null, null, null);
		wrapped.close();
		return new PlayerCursor(wrapped);
	}
	
	public void updatePlayerWins(String name) {
		String sql = "UPDATE players SET wins = wins+1 WHERE playerName = '" + name + "'";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(sql);
		db.close();
	}
	
	public void updatePlayerLosses(String name) {
		String sql = "UPDATE players SET losses = losses+1 WHERE playerName = '" + name + "'";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(sql);
		db.close();	}
	
	public void updatePlayerTies(String name) {
		String sql = "UPDATE players SET ties = ties+1 WHERE playerName = '" + name + "'";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(sql);
		db.close();	}
	
	public void updatePlayerTime(String name, int time) {
		String sql = "UPDATE players SET time = " + time + " WHERE playerName = '" + name + "'";
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
			values.put("wins", 0);
			values.put("ties", 0);
			values.put("time", 0);
			getWritableDatabase().insert("players", null, values);
		}
		
		db.close();	}
	
	public void updateVsLosses(String player1, String player2) {
		String sql = "UPDATE player_record SET player1losses = player1wins+1 WHERE player1 = '" + player1 + "'" + " AND player2 = '" + player2 + "'";
		SQLiteDatabase db = getWritableDatabase();
		Cursor c = db.query("player_record", null, "player1 = '" + player1  + "'" + " AND player2 = '" + player2 + "'", null, null, null, null, null);
		if (c.moveToFirst()) {
			db.execSQL(sql);
		}else {
			ContentValues values = new ContentValues();
			values.put("player1", player1);
			values.put("player2", player2);
			values.put("wins", 0);
			values.put("ties", 0);
			values.put("time", 0);
			getWritableDatabase().insert("players", null, values);
		}
		db.close();	}
	
	public void updateVsTies(String player1, String player2) {
		String sql = "UPDATE player_record SET player1ties = player1ties+1 WHERE player1 = '" + player1 + "'" + " AND player2 = '" + player2 + "'";
		SQLiteDatabase db = getWritableDatabase();
		Cursor c = db.query("player_record", null, "player1 = '" + player1  + "'" + " AND player2 = '" + player2 + "'", null, null, null, null, null);
		if (c.moveToFirst()) {
			db.execSQL(sql);
		}else { 
			ContentValues values = new ContentValues();
			values.put("player1", player1);
			values.put("player2", player2);
			values.put("wins", 0);
			values.put("ties", 0);
			values.put("time", 0);
			getWritableDatabase().insert("players", null, values);
		}
		db.close();	}
	
	public void updateVsTime(String player1, String player2, int time) {
		String sql = "UPDATE player_record SET player1time = " + time + " WHERE player1 = '" + player1 + "'" + " AND player2 = '" + player2 + "'";
		SQLiteDatabase db = getWritableDatabase();
		Cursor c = db.query("player_record", null, "player1 = '" + player1  + "'" + " AND player2 = '" + player2 + "'", null, null, null, null, null);
		if (c.moveToFirst()) {
			db.execSQL(sql);
		}else {
			ContentValues values = new ContentValues();
			values.put("player1", player1);
			values.put("player2", player2);
			values.put("wins", 0);
			values.put("ties", 0);
			values.put("time", 0);
			getWritableDatabase().insert("players", null, values);
		}
		db.close();	}
	
	public static class PlayerCursor extends CursorWrapper {
		public PlayerCursor(Cursor c) {
			super(c);
		}
		
		public PlayerRecord getPlayerRecord() {
			PlayerRecord record = new PlayerRecord();
			record.wins = getInt(getColumnIndex("player1Wins"));
			record.losses = getInt(getColumnIndex("player1Losses"));
			record.ties = getInt(getColumnIndex("player1Ties"));
			record.time = getInt(getColumnIndex("player1Time"));
			return record;
		}
	}

}
