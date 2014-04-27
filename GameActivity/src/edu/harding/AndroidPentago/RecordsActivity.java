package edu.harding.AndroidPentago;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecordsActivity extends Activity implements OnItemSelectedListener {
	private PlayerManager mManager;
	private Spinner spinner1;
	private Spinner spinner2;
	private TextView wins;
	private TextView losses;
    private boolean mMovingWithinApp = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_records);
		
		mManager = PlayerManager.get(this);
		PlayerRecord[] topTen = mManager.getTopTen();
		
		spinner1 = (Spinner)findViewById(R.id.player1Spinner);
		spinner2 = (Spinner)findViewById(R.id.player2Spinner);
		
		List<String> list1 = new ArrayList<String>();
		List<String> list2 = new ArrayList<String>();
		List<String> names = mManager.getNames();
		
		list1.add("Player");
		for (int i = 0; i < names.size(); i++) {
			list1.add(names.get(i));
		}
		list2.add("Everyone");
		for (int i = 0; i < names.size(); i++) {
			list2.add(names.get(i));
		}
		
		spinner1.setOnItemSelectedListener(this);
		spinner2.setOnItemSelectedListener(this);
		wins = (TextView)findViewById(R.id.Wins);
		losses = (TextView)findViewById(R.id.Losses);
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list1);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner1.setAdapter(dataAdapter);
		
		ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list2);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner2.setAdapter(dataAdapter1);
		
		TextView firstName = (TextView)findViewById(R.id.firstPlaceName);
		TextView firstWins = (TextView)findViewById(R.id.firstPlaceWins);
		TextView firstLosses = (TextView)findViewById(R.id.firstPlaceLosses);
		TextView secondName = (TextView)findViewById(R.id.secondPlaceName);
		TextView secondWins = (TextView)findViewById(R.id.secondPlaceWins);
		TextView secondLosses = (TextView)findViewById(R.id.secondPlaceLosses);
		TextView thirdName = (TextView)findViewById(R.id.thirdPlaceName);
		TextView thirdWins = (TextView)findViewById(R.id.thirdPlaceWins);
		TextView thirdLosses = (TextView)findViewById(R.id.thirdPlaceLosses);
		TextView fourthName = (TextView)findViewById(R.id.fourthPlaceName);
		TextView fourthWins = (TextView)findViewById(R.id.fourthPlaceWins);
		TextView fourthLosses = (TextView)findViewById(R.id.fourthPlaceLosses);
		TextView fifthName = (TextView)findViewById(R.id.fifthPlaceName);
		TextView fifthWins = (TextView)findViewById(R.id.fifthPlaceWins);
		TextView fifthLosses = (TextView)findViewById(R.id.fifthPlaceLosses);
		TextView sixthName = (TextView)findViewById(R.id.sixthPlaceName);
		TextView sixthWins = (TextView)findViewById(R.id.sixthPlaceWins);
		TextView sixthLosses = (TextView)findViewById(R.id.sixthPlaceLosses);
		TextView seventhName = (TextView)findViewById(R.id.seventhPlaceName);
		TextView seventhWins = (TextView)findViewById(R.id.seventhPlaceWins);
		TextView seventhLosses = (TextView)findViewById(R.id.seventhPlaceLosses);
		TextView eighthName = (TextView)findViewById(R.id.eighthPlaceName);
		TextView eighthWins = (TextView)findViewById(R.id.eighthPlaceWins);
		TextView eighthLosses = (TextView)findViewById(R.id.eighthPlaceLosses);
		TextView ninthName = (TextView)findViewById(R.id.ninthPlaceName);
		TextView ninthWins = (TextView)findViewById(R.id.ninthPlaceWins);
		TextView ninthLosses = (TextView)findViewById(R.id.ninthPlaceLosses);
		TextView tenthName = (TextView)findViewById(R.id.tenthPlaceName);
		TextView tenthWins = (TextView)findViewById(R.id.tenthPlaceWins);
		TextView tenthLosses = (TextView)findViewById(R.id.tenthPlaceLosses);
		
		if (topTen.length != 0){
			
			if (topTen[0] != null) {
				firstName.setText(topTen[0].name);
				firstWins.setText(Integer.toString(topTen[0].wins));
				firstLosses.setText(Integer.toString(topTen[0].losses));
			}
			else {
				firstName.setText("-");
				firstWins.setText("-");
				firstLosses.setText("-");
			}
			if (topTen[1] != null) {
				secondName.setText(topTen[1].name);
				secondWins.setText(Integer.toString(topTen[1].wins));
				secondLosses.setText(Integer.toString(topTen[1].losses));
			}
			else {
				secondName.setText("-");
				secondWins.setText("-");
				secondLosses.setText("-");
			}
			if (topTen[2] != null) {
				thirdName.setText(topTen[2].name);
				thirdWins.setText(Integer.toString(topTen[2].wins));
				thirdLosses.setText(Integer.toString(topTen[2].losses));
			}
			else {
				thirdName.setText("-");
				thirdWins.setText("-");
				thirdLosses.setText("-");
			}
			if (topTen[3] != null) {
				fourthName.setText(topTen[3].name);
				fourthWins.setText(Integer.toString(topTen[3].wins));
				fourthLosses.setText(Integer.toString(topTen[3].losses));
			}
			else {
				fourthName.setText("-");
				fourthWins.setText("-");
				fourthLosses.setText("-");
			}
			if (topTen[4] != null) {
				fifthName.setText(topTen[4].name);
				fifthWins.setText(Integer.toString(topTen[4].wins));
				fifthLosses.setText(Integer.toString(topTen[4].losses));
			}
			else {
				fifthName.setText("-");
				fifthWins.setText("-");
				fifthLosses.setText("-");
			}
			if (topTen[5] != null) {
				sixthName.setText(topTen[5].name);
				sixthWins.setText(Integer.toString(topTen[5].wins));
				sixthLosses.setText(Integer.toString(topTen[5].losses));
			}
			else {
				sixthName.setText("-");
				sixthWins.setText("-");
				sixthLosses.setText("-");
			}
			if (topTen[6] != null) {
				seventhName.setText(topTen[6].name);
				seventhWins.setText(Integer.toString(topTen[6].wins));
				seventhLosses.setText(Integer.toString(topTen[6].losses));
			}
			else {
				seventhName.setText("-");
				seventhWins.setText("-");
				seventhLosses.setText("-");
			}
			if (topTen[7] != null) {
				eighthName.setText(topTen[7].name);
				eighthWins.setText(Integer.toString(topTen[7].wins));
				eighthLosses.setText(Integer.toString(topTen[7].losses));
			}
			else {
				eighthName.setText("-");
				eighthWins.setText("-");
				eighthLosses.setText("-");
			}
			if (topTen[8] != null) {
				ninthName.setText(topTen[8].name);
				ninthWins.setText(Integer.toString(topTen[8].wins));
				ninthLosses.setText(Integer.toString(topTen[8].losses));
			}
			else {
				ninthName.setText("-");
				ninthWins.setText("-");
				ninthLosses.setText("-");
			}
			if (topTen[9] != null) {
				tenthName.setText(topTen[9].name);
				tenthWins.setText(Integer.toString(topTen[9].wins));
				tenthLosses.setText(Integer.toString(topTen[9].losses));
			}
			else {
				tenthName.setText("-");
				tenthWins.setText("-");
				tenthLosses.setText("-");
			}
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		TextView spinner1Text = (TextView)spinner1.getSelectedView();
		TextView spinner2Text = (TextView)spinner2.getSelectedView();
		String sldkf = spinner1Text.getText().toString();
		String sldifh= spinner2Text.getText().toString();
		if(spinner2Text.getText().toString() == "Everyone" && spinner1Text.getText().toString() != "Player") {
			wins.setText(Integer.toString(mManager.getSingleRecord(spinner1.getSelectedItem().toString()).wins));
			losses.setText(Integer.toString(mManager.getSingleRecord(spinner1.getSelectedItem().toString()).losses));
		}
		else if (spinner1.getSelectedItem().toString() == "Player") {
			
		}
		else {
			
			try{
				PlayerRecord record = mManager.getVS(spinner1Text.getText().toString(), spinner2Text.getText().toString());
				wins.setText(Integer.toString(record.wins));
				losses.setText(Integer.toString(record.losses));
			}
			catch(android.database.CursorIndexOutOfBoundsException e){
				wins.setText("0");
				losses.setText("0");
			}
			finally{
				
			}
			
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

    @Override
    protected void onResume()
    {
        super.onResume();
        mMovingWithinApp = false;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(!(prefs.getBoolean("musicMute", false)))
        {
            AudioPlayer.playMusic(this, R.raw.cold_funk);
        }
        //mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause()
    {
        //mSensorManager.unregisterListener(mSensorListener);
        super.onPause();

        if (!mMovingWithinApp)
        {
            AudioPlayer.stopMusic();
        }
    }

    @Override
    public void onBackPressed()
    {
        mMovingWithinApp = true;
        super.onBackPressed();
    }
}
