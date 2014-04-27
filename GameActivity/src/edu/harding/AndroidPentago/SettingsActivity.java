package edu.harding.AndroidPentago;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;

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
    }
}
