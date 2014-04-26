package edu.harding.AndroidPentago;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import edu.harding.androidtictactoe.R;

public class SettingsActivity extends Activity {
	
	private static boolean confirmMoves = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		SeekBar masterVolumeControl = (SeekBar) findViewById(R.id.masterVolumeControl);
		SeekBar musicVolumeControl = (SeekBar) findViewById(R.id.musicVolumeControl);
		SeekBar sfxVolumeControl = (SeekBar) findViewById(R.id.sfxVolumeControl);
		
		CheckBox confirmMoves = (CheckBox) findViewById(R.id.confirmMoves);
		confirmMoves.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
				setConfirmMoves(isChecked);
			}
		});
		
		CheckBox animations = (CheckBox) findViewById(R.id.animationsCheckBox);
		
		Button editPlayerButton = (Button) findViewById(R.id.editPlayerButton);
		Button cancelButton = (Button) findViewById(R.id.cancelButton);
		Button saveButton = (Button) findViewById(R.id.saveButton);
	}
	
	private void setConfirmMoves(boolean confirm) {
		confirmMoves = confirm;
	}
	
	public static boolean getConfirmMoves() {
		return confirmMoves;
	}
}
