package a2016.soft.ing.unipd.metronomepro.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by Federico Favotto on 07/12/2016.
 */

public class TimeSlice implements Streamable, Parcelable{

    private int bpm;
    private int durationInBeats;
    private int timeFigureNumerator;
    private int timeFigureDenominator;
    /**
     * The size of an object of this class streamed, because all objects have same size in bytes
     */
    public static final int STREAMED_SIZE=16;

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
        byte[] toReturn = new byte[STREAMED_SIZE];
        ByteBuffer bb = ByteBuffer.allocate(STREAMED_SIZE);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.putInt(bpm);
        bb.putInt(durationInBeats);
        bb.putInt(timeFigureNumerator);
        bb.putInt(timeFigureDenominator);
        bb.position(0);
        toReturn=bb.array();
        return toReturn;
    }

    /**
     * Todecode length must be Streamedsize of this class
     * @param toDecode
     */
    @Override
    public void decode(byte[] toDecode) {
        ByteBuffer bb= ByteBuffer.allocate(toDecode.length);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.put(toDecode);
        bb.position(0);
        bpm=bb.getInt();
        durationInBeats=bb.getInt();
        timeFigureNumerator=bb.getInt();
        timeFigureDenominator=bb.getInt();
    }

    @Override
    public int getStreamedSize() {
        return STREAMED_SIZE;
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
