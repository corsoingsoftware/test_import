package a2016.soft.ing.unipd.metronomepro.entities;

/**
 * Created by feder on 07/12/2016.
 */

public class TimeSlice implements Streamable{

    private int bpm;
    private int durationInBeats;
    private int timeFigureNumerator;
    private int timeFigureDenominator;

    @Override
    public byte[] encode() {
        return new byte[0];
    }

    @Override
    public void decode(byte[] toDecode) {

    }

    public int getBpm() {
        return bpm;
    }

    public void setBpm(int bpm) {
        this.bpm = bpm;
    }

    public int getDurationInBeats() {
        return durationInBeats;
    }

    public void setDurationInBeats(int durationInBeats) {
        this.durationInBeats = durationInBeats;
    }

    public int getTimeFigureNumerator() {
        return timeFigureNumerator;
    }

    public void setTimeFigureNumerator(int timeFigureNumerator) {
        this.timeFigureNumerator = timeFigureNumerator;
    }

    public int getTimeFigureDenominator() {
        return timeFigureDenominator;
    }

    public void setTimeFigureDenominator(int timeFigureDenominator) {
        this.timeFigureDenominator = timeFigureDenominator;
    }
}
