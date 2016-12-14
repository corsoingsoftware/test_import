package a2016.soft.ing.unipd.metronomepro.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by feder on 07/12/2016.
 */

public class TimeSlice implements Streamable, Parcelable{

    private int bpm;
    private int durationInBeats;
    private int timeFigureNumerator;
    private int timeFigureDenominator;

    protected TimeSlice(Parcel in) {
        bpm = in.readInt();
        durationInBeats = in.readInt();
        timeFigureNumerator = in.readInt();
        timeFigureDenominator = in.readInt();
    }

    public TimeSlice(){

    }

    public static final Creator<TimeSlice> CREATOR = new Creator<TimeSlice>() {
        @Override
        public TimeSlice createFromParcel(Parcel in) {
            return new TimeSlice(in);
        }

        @Override
        public TimeSlice[] newArray(int size) {
            return new TimeSlice[size];
        }
    };

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

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(bpm);
        dest.writeInt(durationInBeats);
        dest.writeInt(timeFigureNumerator);
        dest.writeInt(timeFigureDenominator);
    }
}
