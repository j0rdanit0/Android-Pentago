package edu.harding.AndroidPentago;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class HelpActivity extends Activity {

    private boolean mMovingWithinApp = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
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
