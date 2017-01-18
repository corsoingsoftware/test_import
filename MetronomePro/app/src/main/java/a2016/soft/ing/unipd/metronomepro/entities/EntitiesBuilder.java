package a2016.soft.ing.unipd.metronomepro.entities;

/**
 * Created by Federico Favotto on 10/12/2016.
 */

public class EntitiesBuilder {

    public static TimeSlicesSong getTimeSlicesSong() {
        return new ParcelableTimeSlicesSong();
    }

    public static Song getSong(String name) {
        return new ParcelableTimeSlicesSong(name);
    }

    public static Playlist getPlaylist(String name){
        return new ParcelablePlaylist(name);
    }

    public static MidiSong getMidiSong(){
        return new ParcelableMidiSong();
    }

}
