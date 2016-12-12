package a2016.soft.ing.unipd.metronomepro;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by giuli on 22/10/2016.
 *
 * Classe modificata da Federico per mettere un esempio di come caricare il file audio ecc ecc
 * Totalmente da cancellare.
 */

public class SoundManager {

    //    MediaPlayer mp;
    private static final int SAMPLE_RATE_IN_HERTZ = 44100;
    private static final int WAV_HEADER_SIZE_IN_BYTES=44;
    private static final int DEFAULT_FRAME_SIZE=4;

    MediaPlayer mp;
    private AudioTrack at;
    private byte[] musicWithoutHeader;

    public SoundManager(Context c) {

        try {
            AssetFileDescriptor afd = c.getAssets().openFd(c.getString(R.string.fileAudioName));
            short[] shortData = null; // for ex. path= "/sdcard/samplesound.pcm" or "/sdcard/samplesound.wav"
            FileInputStream inputStream = new FileInputStream(afd.getFileDescriptor());
            byte[] byteData= new byte[((int) inputStream.available())];
            shortData = new short[((int) inputStream.available()/2)];
            try {
                inputStream.read( byteData );
                inputStream.close();
                ByteBuffer.wrap(byteData).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shortData);

            } catch (FileNotFoundException e) {
// TODO Auto-generated catch block
                e.printStackTrace();
            }
// Set and push to audio track..
            int sampleRateInHz=SAMPLE_RATE_IN_HERTZ;
            int channelConfig=AudioFormat.CHANNEL_OUT_STEREO;
            int audioFormat=AudioFormat.ENCODING_PCM_16BIT;
            int intSize = android.media.AudioTrack.getMinBufferSize(sampleRateInHz,channelConfig,audioFormat);
            AudioTrack at = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRateInHz, channelConfig,
                    audioFormat, intSize, AudioTrack.MODE_STATIC);
            // Write the byte array to the track
            byte[] prova= new byte[byteData.length*2-WAV_HEADER_SIZE_IN_BYTES];
            ByteBuffer bb = ByteBuffer.allocate(2);
            bb.order(ByteOrder.LITTLE_ENDIAN);
            bb.putShort(Short.MIN_VALUE);
            bb.position(0);
            byte first=bb.get();
            byte second=bb.get();
            short shortVal = bb.getShort(0);
            for(int i=byteData.length-WAV_HEADER_SIZE_IN_BYTES;i<prova.length-1;i++){
                prova[i++]=first;
                prova[i]=second;
            }
            System.arraycopy(byteData,WAV_HEADER_SIZE_IN_BYTES,prova,0,byteData.length-WAV_HEADER_SIZE_IN_BYTES);
            at.write(prova, 0, prova.length);
            int a=at.setLoopPoints(0,intSize/DEFAULT_FRAME_SIZE,-1);
            at.play();

            a=at.setLoopPoints(0,(intSize/DEFAULT_FRAME_SIZE)/2,10);
            at.play();
            at.stop();
            at.release();

        } catch (IOException ex) {
        }
        /*FileDescriptor file = afd.getFileDescriptor();

        } catch (IOException ex) {
        }ArrayList<Integer> musicLoader = new ArrayList<Integer>(8000);
            InputStream inputStream = new FileInputStream(file);

            /*while (inputStream.available() > 0) {
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
            at.release();*/
            //Codice per API 23:
            //// TODO: 03/11/2016 vedere se è necessario implementare due metodi differenti in base alle API
            //.Builder()).setAudioAttributes(AudioAttributes.CONTENT_TYPE_MUSIC).setAudioFormat(AudioFormat.CHANNEL_OUT_DEFAULT)
            //.setBufferSizeInBytes(65536).setSessionId(0).setTransferMode(AudioTrack.MODE_STREAM).build();

//        mp = new MediaPlayer();
//
//            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
//            mp.prepare();
//
//        } catch (IOException ex) {
//            //eccezione da gestire
//        }
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
