package a2016.soft.ing.unipd.metronomepro;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by giuli on 22/10/2016.
 */

public class SoundManager {

    private MediaPlayer mp;

    public SoundManager(Context c) {
        mp = new MediaPlayer();
        try {
            AssetFileDescriptor afd = c.getAssets().openFd("Tick.mid");
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mp.prepare();
        } catch (IOException ex) {
            //eccezione da gestire
        }
    }

    public void soundStart() {
        if (mp.isPlaying()) {
            // mp.pause();
            mp.seekTo(0);
        }
        mp.start();
    }
}
