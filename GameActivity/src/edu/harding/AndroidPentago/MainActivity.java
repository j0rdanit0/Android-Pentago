package edu.harding.AndroidPentago;

import edu.harding.androidtictactoe.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	private SensorManager mSensorManager;
	private float mAccel;
	private float mAccelCurrent;
	private float mAccelLast;

	private Button mNewGameButton;
	private Button mSettings;
	private Button mHelp;
	private Button mRecords;

	private final SensorEventListener mSensorListener = new SensorEventListener() {
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
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mNewGameButton = (Button)findViewById(R.id.new_game_button);
		mNewGameButton.setOnClickListener(new View.OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{
				Intent i = new Intent(MainActivity.this, GameActivity.class);
				startActivity(i);
			}
		});

		mSettings = (Button)findViewById(R.id.settings_button);
		mSettings.setEnabled(false);

		mHelp = (Button)findViewById(R.id.help_button);
		mHelp.setEnabled(false);

		mRecords = (Button)findViewById(R.id.records_button);
		mRecords.setEnabled(false);

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	    mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	    mAccel = 0.00f;
	    mAccelCurrent = SensorManager.GRAVITY_EARTH;
	    mAccelLast = SensorManager.GRAVITY_EARTH;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_options, menu);
		return true;
	}

	  @Override
	  protected void onResume() 
	  {
	    super.onResume();
	    mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	  }

	  @Override
	  protected void onPause() 
	  {
	    mSensorManager.unregisterListener(mSensorListener);
	    super.onPause();
	  }

}
