package a2016.soft.ing.unipd.metronomepro;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioTrack;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by giuli on 22/10/2016.
 */

public class SoundManager {

    //    MediaPlayer mp;
    public static final int AUDIO_BUFFER_SIZE_IN_BYTES = 8192;
    public static final int SAMPLE_RATE_IN_HERTZ = 44100;
    MediaPlayer mp;
    private AudioTrack at;
    private byte[] musicWithoutHeader;

    public SoundManager(Context c) {

        try {
            AssetFileDescriptor afd = c.getAssets().openFd(c.getString(R.string.fileAudioName));
            /*FileDescriptor file = afd.getFileDescriptor();
            ArrayList<Integer> musicLoader = new ArrayList<Integer>(8000);
            InputStream inputStream = new FileInputStream(file);
            boolean startOfFile = true;
            int sampleFreq = 48000;

            int shortSizeInBytes = Short.SIZE / Byte.SIZE;

            int minBufferSize = AudioTrack.getMinBufferSize(sampleFreq, AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT);

            int bufferSizeInBytes = (int)(inputStream.available() / shortSizeInBytes);

            final AudioTrack at = new AudioTrack(AudioManager.STREAM_MUSIC, sampleFreq,
                    AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, minBufferSize,
                    AudioTrack.MODE_STREAM);
            int i = 0;
            byte[] s = new byte[bufferSizeInBytes];

            try {
                final FileInputStream fin = new FileInputStream(file);
                final DataInputStream dis = new DataInputStream(fin);
                at.setNotificationMarkerPosition((int)(inputStream.available() / 2));

                at.play();
                while ((i = dis.read(s, 0, bufferSizeInBytes)) > -1) {
                    at.write(s, 0, i);

                }

            } catch (FileNotFoundException e) {

            } catch (IOException e) {

            } catch (Exception e) {

            }


            //checkFormat(dataSize > 0, "wrong datasize: " + dataSize);

            //I should skip the header
            //gli header nei wav son da 44 o 46 bytes in base al numero contenuto nel byte sedici per 4 bytes.
            //il byte 16 è il meno significativo
//            int bytesOfHeader = 28;
//            try {
//                dis.skipBytes(16);
//                byte[] lenOfHeader = new byte[4];
//                dis.read(lenOfHeader);
//                int a = lenOfHeader[0] +
//                        (lenOfHeader[1]) << 8 +
//                        lenOfHeader[2] << 16 +
//                        lenOfHeader[3] << 24;
//                bytesOfHeader += a;
//                dis.skipBytes(bytesOfHeader);
//            } catch (Exception ex) {
//                //fallito il caricamento del file audio per il clack
//            }
            *//*while (inputStream.available() > 0) {
                //escludo la parte con bytes di silenzio, inizio a prendere i bytes solo dal primo diverso da 0
                int t = inputStream.read();
                if (startOfFile) {
                    if (t > 0) startOfFile = false;
                } else {

                    //musicLoader.add(t);
                }
            }
            inputStream.close();
            int i = 0;
            musicWithoutHeader = new byte[musicLoader.size()];//size & length of the file
            for (Byte b : musicLoader) {
                musicWithoutHeader[i++] = b.byteValue();
            }
            int intBufferSize=8000;
            try {
                intBufferSize = android.media.AudioTrack.getMinBufferSize(SAMPLE_RATE_IN_HERTZ, AudioFormat.CHANNEL_OUT_MONO,
                        AudioFormat.ENCODING_PCM_16BIT);

            }catch (Exception ex){

            }

            at = new AudioTrack(AudioManager.STREAM_MUSIC,
                    SAMPLE_RATE_IN_HERTZ,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    intBufferSize,
                    AudioTrack.MODE_STREAM);
            at.play();
            at.write(musicWithoutHeader,0,musicWithoutHeader.length);
            at.stop();
            at.release();*//*
            //Codice per API 23:
            //// TODO: 03/11/2016 vedere se è necessario implementare due metodi differenti in base alle API
            //.Builder()).setAudioAttributes(AudioAttributes.CONTENT_TYPE_MUSIC).setAudioFormat(AudioFormat.CHANNEL_OUT_DEFAULT)
            //.setBufferSizeInBytes(65536).setSessionId(0).setTransferMode(AudioTrack.MODE_STREAM).build();

        } catch (IOException ex) {
        }*/
        mp = new MediaPlayer();

            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mp.prepare();

        } catch (IOException ex) {
            //eccezione da gestire
        }
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
        if (mp.isPlaying()) {
            // mp.pause();
            mp.seekTo(0);
        }
        mp.start();
    }
}
