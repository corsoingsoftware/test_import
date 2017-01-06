package a2016.soft.ing.unipd.metronomepro.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Omar on 12/12/2016.
 */

public class ParcelablePlaylist implements Playlist, Parcelable {

    protected ParcelablePlaylist(Parcel in) {
        this(in.readString());
        while(){
            int a=in.readInt();
            if(a==0) {
                ParcelableTimeSlicesSong ps = in.readParcelable(ParcelableTimeSlicesSong.class.getClassLoader());
                songList.add(ps);
            }
        }
        ArrayList<byte[]> arrayByte = (ArrayList<byte[]>) in.readSerializable();
        ArrayList<String> names=(ArrayList<String>) in.readSerializable();
        int indexToInsert = 0;
        for (byte[] bt : arrayByte) {
            Song s = EntitiesBuilder.getSong(names.get(indexToInsert));
            s.decode(bt);
            songList.add(indexToInsert, s);
            indexToInsert++;
        }
    }

    public static final Creator<ParcelablePlaylist> CREATOR = new Creator<ParcelablePlaylist>() {
        @Override
        public ParcelablePlaylist createFromParcel(Parcel in) {
            return new ParcelablePlaylist(in);
        }

        @Override
        public ParcelablePlaylist[] newArray(int size) {
            return new ParcelablePlaylist[size];
        }
    };

    private String name;
    private ArrayList<Song> songList;

    public ParcelablePlaylist(String name) {
        this.name = name;
        songList = new ArrayList<Song>();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        Parcelable[] p=new Parcelable[songList.size()];
        p=songList.toArray(p);
        dest.writeString(getName());
        dest.writeParcelableArray(p, flags);
        //prendo la song e la metto in array list convertita in array di byte
        //passo al parser aray di byte quando li riprendo li devo riconvertire in song
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int size() {
        return songList.size();
    }

    @Override
    public boolean isEmpty() {
        return songList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return songList.contains(o);
    }

    @NonNull
    @Override
    public Iterator<Song> iterator() {
        return songList.iterator();
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return songList.toArray();
    }

    @NonNull
    @Override
    public <Song> Song[] toArray(Song[] a) {
        return songList.toArray(a);
    }

    @Override
    public boolean add(Song song) {
        return songList.add(song);
    }

    @Override
    public boolean remove(Object o) {
        return songList.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return songList.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Song> c) {
        return songList.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Song> c) {
        return songList.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return songList.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return songList.retainAll(c);
    }

    @Override
    public void clear() {
        songList.clear();
    }

    @Override
    public Song get(int index) {return songList.get(index);}

    @Override
    public Song set(int index, Song element) {
        return songList.set(index, element);
    }

    @Override
    public void add(int index, Song element) {
        songList.add(index, element);
    }

    @Override
    public Song remove(int index) {
        return songList.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return songList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return songList.lastIndexOf(o);
    }

    @Override
    public ListIterator<Song> listIterator() {
        return songList.listIterator();
    }

    @NonNull
    @Override
    public ListIterator<Song> listIterator(int index) {
        return songList.listIterator(index);
    }

    @NonNull
    @Override
    public List<Song> subList(int fromIndex, int toIndex) {
        return songList.subList(fromIndex, toIndex);
    }
}
