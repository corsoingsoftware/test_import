package a2016.soft.ing.unipd.metronomepro.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by feder on 09/12/2016.
 */

public class ParcelableSong implements Song, Parcelable {

    public static final Creator<ParcelableSong> CREATOR = new Creator<ParcelableSong>() {
        /**
         * create song from parcel, just call default parcel constructor
         * @param in
         * @return
         */
        @Override
        public ParcelableSong createFromParcel(Parcel in) {
            return new ParcelableSong(in);
        }

        @Override
        public ParcelableSong[] newArray(int size) {
            return new ParcelableSong[size];
        }
    };
    /**
     * Unparcel the song
     *
     * @param in
     */

    protected ParcelableSong(Parcel in) {

        ArrayList<byte[]> support = (ArrayList<byte[]>) in.readSerializable();
        this.name = in.readString();

        timeSliceList = new ArrayList<>(support.size());

        for(byte[] b : support){
            TimeSlice ts = new TimeSlice();
            ts.decode(b);
            timeSliceList.add(ts);
        }
    }

    protected String name;
    protected ArrayList<TimeSlice> timeSliceList;

    public ParcelableSong() {
        this("");
    }

    ParcelableSong(String name) {
        this.name = name;
        timeSliceList = new ArrayList<TimeSlice>();
    }


    @Override
    public byte[] encode() {
        return new byte[0];
    }

    @Override
    public void decode(byte[] toDecode) {


    }

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    /**
     * Parcel the song!
     * @param dest
     * @param flags
     */


    @Override
    public void writeToParcel(Parcel dest, int flags) {

        ArrayList<byte[]> timeSlicesByte = new ArrayList<>();

        for(TimeSlice ts: timeSliceList){

            timeSlicesByte.add(ts.encode());
        }

        dest.writeSerializable(timeSlicesByte);
        dest.writeString(name);
    }

    @Override
    public int size() {
        return timeSliceList.size();
    }

    @Override
    public boolean isEmpty() {
        return timeSliceList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return timeSliceList.contains(o);
    }

    @NonNull
    @Override
    public Iterator<TimeSlice> iterator() {
        return timeSliceList.iterator();
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return timeSliceList.toArray();
    }

    @NonNull
    @Override
    public <TimeSlice> TimeSlice[] toArray(TimeSlice[] a) {
        return timeSliceList.toArray(a);
    }

    @Override
    public boolean add(TimeSlice timeSlice) {
        return timeSliceList.add(timeSlice);
    }

    @Override
    public boolean remove(Object o) {
        return timeSliceList.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return timeSliceList.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends TimeSlice> c) {
        return timeSliceList.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends TimeSlice> c) {
        return timeSliceList.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return timeSliceList.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return timeSliceList.retainAll(c);
    }

    @Override
    public void clear() {
        timeSliceList.clear();
    }

    @Override
    public TimeSlice get(int index) {
        return timeSliceList.get(index);
    }

    @Override
    public TimeSlice set(int index, TimeSlice element) {
        return timeSliceList.set(index, element);
    }

    @Override
    public void add(int index, TimeSlice element) {
        timeSliceList.add(index, element);
    }

    @Override
    public TimeSlice remove(int index) {
        return timeSliceList.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return timeSliceList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return timeSliceList.lastIndexOf(o);
    }

    @Override
    public ListIterator<TimeSlice> listIterator() {
        return timeSliceList.listIterator();
    }

    @NonNull
    @Override
    public ListIterator<TimeSlice> listIterator(int index) {
        return timeSliceList.listIterator();
    }

    @NonNull
    @Override
    public List<TimeSlice> subList(int fromIndex, int toIndex) {
        return timeSliceList.subList(fromIndex, toIndex);
    }

    public String getName() {
        return name;
    }

}
