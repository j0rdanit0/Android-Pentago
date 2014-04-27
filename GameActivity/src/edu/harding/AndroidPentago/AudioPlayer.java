package edu.harding.AndroidPentago;

import android.content.Context;
import android.media.MediaPlayer;

public class AudioPlayer {

	private MediaPlayer mPlayer;

    private static MediaPlayer mMusicPlayer;
	
	public void stop() {
		if (mPlayer != null) {
			mPlayer.release();
			mPlayer = null;
		}
	}
	
	public void play(Context c, int soundId) {
		stop();
		
		mPlayer = MediaPlayer.create(c, soundId);
		
		mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				stop();				
			}
		});
		
		mPlayer.start();
	}

    public static void playMusic(Context c, int musicId)
    {
        if (mMusicPlayer == null)
        {
            mMusicPlayer = MediaPlayer.create(c,musicId);
            mMusicPlayer.setLooping(true);
            mMusicPlayer.start();
        }
    }

    public static void stopMusic()
    {
        if (mMusicPlayer != null) {
            mMusicPlayer.release();
            mMusicPlayer = null;
        }
    }
}
