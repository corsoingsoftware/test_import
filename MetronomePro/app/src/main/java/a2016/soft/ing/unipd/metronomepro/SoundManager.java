package a2016.soft.ing.unipd.metronomepro;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import java.io.IOException;
import java.io.InputStream;
import android.media.AudioTrack;



public  class SoundManager {

    AudioTrack at;
        public void onCreate() {

        int BUFFER_SIZE = 1024;

        at = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.ENCODING_PCM_16BIT,

                AudioFormat.ENCODING_PCM_16BIT, BUFFER_SIZE, AudioTrack.MODE_STREAM);
    }
   public byte[] void go()
        byte[] click = byte[];
        int i = 0;
        int bufferSize = 512;
        byte [] buffer = new byte[bufferSize];
        InputStream is = new InputStream();

        }
        try {
            while((i = inputStream.read()) != -1)
                at.write(buffer, 0, i);
        } catch (IOException e) {}

    }

}


