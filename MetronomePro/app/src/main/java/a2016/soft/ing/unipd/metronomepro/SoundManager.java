package a2016.soft.ing.unipd.metronomepro;

import android.content.Context;
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
            mp.setDataSource(c.getAssets().openFd("Tick.mid").getFileDescriptor());
        } catch (IOException ex) {
            //eccezione da gestire
        }
    }

    public void soundStart() {
        mp.start();
    }
}
