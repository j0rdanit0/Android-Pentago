package edu.harding.AndroidPentago;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
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

	/*private final SensorEventListener mSensorListener = new SensorEventListener() {
		public void onSensorChanged(SensorEvent se) {
			float x = se.values[0];
			float y = se.values[1];
			float z = se.values[2];
			mAccelLast = mAccelCurrent;
			mAccelCurrent = (float) Math.sqrt((double)(x*x + y*y + z*z));
			float delta = mAccelCurrent - mAccelLast;
			mAccel = mAccel * .9f + delta;

			if(mAccel > 2) {
				Toast.makeText(MainActivity.this, "Shake", Toast.LENGTH_SHORT).show();
			}
		}

		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}
	};*/

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

		//LayoutInflater inflater = new LayoutInflater(MainActivity.this);
		mHelp = (Button)findViewById(R.id.help_button);
		mHelp.setOnClickListener(new View.OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{
				AlertDialog.Builder helpDialog = new AlertDialog.Builder(MainActivity.this);
				helpDialog.setView(getLayoutInflater().inflate(R.layout.help_dialog, null))
					.setPositiveButton(R.string.ok_button, 
							new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
				}).create();
				
				helpDialog.show(); 
			}
		});

		mRecords = (Button)findViewById(R.id.records_button);
		mRecords.setEnabled(false);

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	    //mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	    mAccel = 0.00f;
	    mAccelCurrent = SensorManager.GRAVITY_EARTH;
	    mAccelLast = SensorManager.GRAVITY_EARTH;
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
