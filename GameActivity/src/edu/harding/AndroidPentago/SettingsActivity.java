package edu.harding.AndroidPentago;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SettingsActivity extends PreferenceActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    @SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

		CheckBoxPreference confirmMoves = (CheckBoxPreference)findPreference("confirmMoves");
		confirmMoves.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                SharedPreferences.Editor ed = prefs.edit();
                return true;
            }
        });

	}

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            Preference Pref = findPreference(key);
            Pref.setDefaultValue(sharedPreferences.getBoolean(key, ((CheckBoxPreference)findPreference(key)).isChecked()));

            if (key.equals("musicMute"))
            {
                if (((CheckBoxPreference)findPreference(key)).isChecked())
                {
                    AudioPlayer.stopMusic();
                }
                else
                {
                    AudioPlayer.playMusic(this, R.raw.cold_funk);
                }
            }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
