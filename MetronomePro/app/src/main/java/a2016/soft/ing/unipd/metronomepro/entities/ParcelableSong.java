package a2016.soft.ing.unipd.metronomepro.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by feder on 09/12/2016.
 */

public class ParcelableSong implements Song, Parcelable {

    /**
     * Unparcel the song
     * @param in
     */
    protected ParcelableSong(Parcel in) {
    }

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

    @Override
    public byte[] encode() {
        return new byte[0];
    }

    @Override
    public void decode(byte[] toDecode) {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Parcel the song!
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @NonNull
    @Override
    public Iterator<TimeSlice> iterator() {
        return null;
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @NonNull
    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean add(TimeSlice timeSlice) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends TimeSlice> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends TimeSlice> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public TimeSlice get(int index) {
        return null;
    }

    @Override
    public TimeSlice set(int index, TimeSlice element) {
        return null;
    }

    @Override
    public void add(int index, TimeSlice element) {

    }

    @Override
    public TimeSlice remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<TimeSlice> listIterator() {
        return null;
    }

    @NonNull
    @Override
    public ListIterator<TimeSlice> listIterator(int index) {
        return null;
    }

    @NonNull
    @Override
    public List<TimeSlice> subList(int fromIndex, int toIndex) {
        return null;
    }
}
