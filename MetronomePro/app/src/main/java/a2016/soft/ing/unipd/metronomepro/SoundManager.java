package a2016.soft.ing.unipd.metronomepro;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioFormat;
import android.media.AudioTrack;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by giuli on 22/10/2016.
 */

public class SoundManager {

    //    MediaPlayer mp;
    public static final int AUDIO_BUFFER_SIZE_IN_BYTES = 8192;
    public static final int SAMPLE_RATE_IN_HERTZ = 44000;
    private AudioTrack at;
    private byte[] musicWithoutHeader;


    public SoundManager(Context c) {

        try {
            AssetFileDescriptor afd = c.getAssets().openFd(c.getString(R.string.fileAudioName));
            FileDescriptor file = afd.getFileDescriptor();
            ArrayList<Byte> musicLoader = new ArrayList<Byte>(1024);
            InputStream is = new FileInputStream(file);

            BufferedInputStream bis = new BufferedInputStream(is, 8000);
            DataInputStream dis = new DataInputStream(bis);
            //  Create a DataInputStream to read the audio data from the saved file

            //  Read the file into the "music" array
            boolean startOfFile = true;
            //I should skip the header
            //gli header nei wav son da 44 o 46 bytes in base al numero contenuto nel byte sedici per 4 bytes.
            //il byte 16 è il meno significativo
            int bytesOfHeader = 28;
            try {
                dis.skipBytes(16);
                byte[] lenOfHeader = new byte[4];
                dis.read(lenOfHeader);
                int a = lenOfHeader[0] +
                        (lenOfHeader[1]) << 8 +
                        lenOfHeader[2] << 16 +
                        lenOfHeader[3] << 24;
                bytesOfHeader += a;
                dis.skipBytes(bytesOfHeader);
            } catch (Exception ex) {
                //fallito il caricamento del file audio per il clack
            }
            while (dis.available() > 0) {
                //escludo la parte con bytes di silenzio, inizio a prendere i bytes solo dal primo diverso da 0
                byte t = dis.readByte();
                if (startOfFile) {
                    if (t > 0) startOfFile = false;
                } else {

                    musicLoader.add(t);
                }
            }
            dis.close();
            int i = 0;
            musicWithoutHeader = new byte[musicLoader.size()];//size & length of the file
            for (Byte b : musicLoader) {
                musicWithoutHeader[i++] = b.byteValue();
            }
            at = new AudioTrack(AudioTrack.MODE_STREAM,
                    SAMPLE_RATE_IN_HERTZ,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_8BIT,
                    AUDIO_BUFFER_SIZE_IN_BYTES,
                    AudioTrack.MODE_STREAM);
            //Codice per API 23:
            //// TODO: 03/11/2016 vedere se è necessario implementare due metodi differenti in base alle API
            //.Builder()).setAudioAttributes(AudioAttributes.CONTENT_TYPE_MUSIC).setAudioFormat(AudioFormat.CHANNEL_OUT_DEFAULT)
            //.setBufferSizeInBytes(65536).setSessionId(0).setTransferMode(AudioTrack.MODE_STREAM).build();

        } catch (IOException ex) {
        }
       /*old method:
        TODO cancellare se funziona bene la nuova versione
        mp = new MediaPlayer();
        try {

            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mp.prepare();
        } catch (IOException ex) {
            //eccezione da gestire
        }*/
    }

    /**
     * Imposta i BPM del file audio allungando il clack con del silenzio fino a coprire un periodo
     *
     * @param BPM Battiti per minuto dell'audio
     */
    public void setBPMTime(int BPM) {
        //considera sample campionamento e lunghezza

    }

    /**
     * This method start the current sound loaded
     */
    public void soundStart() {
        /*
        * Codice vecchia versione:
        * TODO cancellare se funziona nuova versione
        **/
        /*if (mp.isPlaying()) {
            // mp.pause();
            mp.seekTo(0);
        }
        mp.start();*/
    }
}
