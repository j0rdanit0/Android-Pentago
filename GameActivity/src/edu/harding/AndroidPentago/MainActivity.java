package edu.harding.AndroidPentago;

import android.app.Activity;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import edu.harding.androidtictactoe.R;

public class MainActivity extends Activity {
	private SensorManager mSensorManager;
	private float mAccel;
	private float mAccelCurrent;
	private float mAccelLast;

	private Button mSinglePlayerButton;
	private Button mTwoPlayerButton;
	private Button mSettings;
	private Button mHelp;
	private Button mRecords;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mSinglePlayerButton = (Button)findViewById(R.id.single_player_button);
		mSinglePlayerButton.setOnClickListener(new View.OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{
				Intent i = new Intent(MainActivity.this, GameActivity.class);
				i.putExtra("PvP", false);
				startActivity(i);
			}
		});
		
		mTwoPlayerButton = (Button)findViewById(R.id.two_player_button);
		mTwoPlayerButton.setOnClickListener(new View.OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{
				Intent i = new Intent(MainActivity.this, GameActivity.class);
				i.putExtra("PvP", true);
				startActivity(i);
			}
		});

		mSettings = (Button)findViewById(R.id.settings_button);
		mSettings.setOnClickListener(new View.OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{
				Intent i = new Intent(MainActivity.this, SettingsActivity.class);
				startActivity(i);
			}
		});

		mHelp = (Button)findViewById(R.id.help_button);
		mHelp.setOnClickListener(new View.OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{
				Intent i = new Intent(MainActivity.this, HelpActivity.class);
				startActivity(i);
			}
		});

		mRecords = (Button)findViewById(R.id.records_button);
		mRecords.setEnabled(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.game_options, menu);
		return true;
	}

	  @Override
	  protected void onResume() 
	  {
	    super.onResume();
	    //mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	  }

	  @Override
	  protected void onPause() 
	  {
	    //mSensorManager.unregisterListener(mSensorListener);
	    super.onPause();
	  }

}
