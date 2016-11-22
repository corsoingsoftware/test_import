package a2016.soft.ing.unipd.metronomepro.sound.management;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by feder on 12/11/2016.
 * Si occupa di gestire le interazioni con audioTrack, un'istanza di questa classe sarà contenuta nel service.
 * Sara lui che implementa completamente soundmanager e i suoi metodi non saranno più degli eco a qualcun altro ma
 * farà veramente qualcosa.
 */
//
public class AudioTrackController implements SoundManager {

    private static final int WAV_HEADER_SIZE_IN_BYTES = 44;
    private static final int SAMPLE_RATE_IN_HERTZ = 8000;
    private static final int SIN_FREQUENCY=610;
    private static final int FRAME_SIZE = 2;
    private static final int SIN_LENGTH_IN_BYTES=500;
    private static final int SECOND = 60;
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_OUT_MONO;
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    /**
     * sample rate in hertz per numero di byte per sample per numero di canali
     */
    private int maxPeriodLengthInBytes;
    private int maxBPM;
    private int minBPM;
    private int currBPM;
    private int bufferSize;
    private int bufferSizeInFrame;
    private byte[] initialClack; //metto i due array come variabili per potervi accedere da fuori
    private byte[] finalClack;
    //    private ByteBuffer silence = ByteBuffer.allocate(2);
    private AudioTrack at;

    public int getCurrBPM() {
        return currBPM;
    }

    /**
     * Questo metodo carica questi due file in due array, per poi poterli gestire
     *
     * @param clack      il clack normale
     * @param clackFinal il clack di fine battuta
     */
    void loadFile(FileDescriptor clack, FileDescriptor clackFinal) {
        try {
            //inizializzo i due array di byte
            FileInputStream streamInClack = new FileInputStream(clack);
            FileInputStream streamFinClack = new FileInputStream(clackFinal);

            //adesso tolgo gli header di 44 byte all'inizio dei file
            //inizializzo i due array al doppio della dimensione del suono
            initialClack = new byte[SIN_LENGTH_IN_BYTES];
            for(int i=0;i<SIN_LENGTH_IN_BYTES-1;i++){
                double b=Math.sin(2*Math.PI*i/(SAMPLE_RATE_IN_HERTZ/SIN_FREQUENCY));
                ByteBuffer bb = ByteBuffer.allocate(2);
                bb.order(ByteOrder.LITTLE_ENDIAN);
                bb.putShort((short)(b*Short.MIN_VALUE));
                bb.position(0);
                initialClack[i++]=bb.get();
                initialClack[i]=bb.get();
            }

        } catch (Exception e) {
        }
    }

    @Override
    public void initialize( int minBPM,int maxBPM) {
        this.maxBPM = maxBPM;
        this.minBPM = minBPM;
        maxPeriodLengthInBytes = (SAMPLE_RATE_IN_HERTZ * FRAME_SIZE * (SECOND / minBPM));
        bufferSize = maxPeriodLengthInBytes;

        bufferSizeInFrame= bufferSize/FRAME_SIZE;
        at = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE_IN_HERTZ, CHANNEL_CONFIG,
                AUDIO_FORMAT, bufferSize, AudioTrack.MODE_STATIC);
        byte[] period = new byte[maxPeriodLengthInBytes];
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.putShort(Short.MIN_VALUE);
        bb.position(0);
        byte first=bb.get();
        byte second=bb.get();
        for(int i=initialClack.length;i<period.length-1;i++){
            period[i++]=first;
            period[i]=second;
        }
        //Math.min dovrebbe essere utile se il clack è più lungo del periodo massimo
        System.arraycopy(initialClack, 0, period, 0, Math.min(initialClack.length, period.length));
        //scrivo un array di byte in AudioTrack
        at.write(period, 0, period.length);
        setBPM(minBPM);
    }

    @Override
    public void play() {
        at.play();
        setBPM(currBPM);
    }

    @Override
    public void stop() {
        at.stop();
    }

    @Override
    public int getState() {
        switch(at.getPlayState()){
            case AudioTrack.PLAYSTATE_STOPPED:
                return 0;
            case AudioTrack.PLAYSTATE_PLAYING:
                return 1;
            case AudioTrack.PLAYSTATE_PAUSED:
                return 2;
            default: return -1; //Non dovrebbe mai essere qui.
        }
    }

    @Override
    public void setRhythmics(int numerator, int denom) {

    }

    @Override
    public void setBPM(int newBPM) {

        if(newBPM>=minBPM&&newBPM<=maxBPM) {
            currBPM = newBPM;
            boolean shouldRestart=false;
            if (at.getPlayState() != AudioTrack.PLAYSTATE_STOPPED) {
                at.stop();
                shouldRestart=true;
            }
            int frameStop = (int) Math.round((double) (SECOND * bufferSizeInFrame) / (double) (currBPM * (SECOND / minBPM)));
            int aaa = ((SECOND * bufferSizeInFrame) % (currBPM * (SECOND / minBPM)));
            at.setLoopPoints(0, frameStop, -1);
            if(shouldRestart) at.play();
        }

    }
}
