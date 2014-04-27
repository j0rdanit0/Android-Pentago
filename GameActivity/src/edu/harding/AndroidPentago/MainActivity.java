package edu.harding.AndroidPentago;

import android.app.Activity;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

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

    private Boolean mMovingWithinApp = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        AudioPlayer.playMusic(this, R.raw.cold_funk);

		mSinglePlayerButton = (Button)findViewById(R.id.single_player_button);
		mSinglePlayerButton.setOnClickListener(new View.OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{
				Intent i = new Intent(MainActivity.this, GameActivity.class);
				i.putExtra("PvP", false);
                		//i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                mMovingWithinApp = true;
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
                mMovingWithinApp = true;
				i.putExtra("PvP", true);
                		//i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
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
                mMovingWithinApp = true;
				startActivity(i);
			}
		});

		mHelp = (Button)findViewById(R.id.help_button);
		mHelp.setOnClickListener(new View.OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{
                mMovingWithinApp = true;
				Intent i = new Intent(MainActivity.this, HelpActivity.class);
				startActivity(i);
			}
		});

		mRecords = (Button)findViewById(R.id.records_button);
		mRecords.setOnClickListener(new View.OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{
				Intent i = new Intent(MainActivity.this, RecordsActivity.class);
				startActivity(i);
			}
		});
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
          mMovingWithinApp = false;
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
    protected void onStop()
    {
        super.onStop();
    }
}
