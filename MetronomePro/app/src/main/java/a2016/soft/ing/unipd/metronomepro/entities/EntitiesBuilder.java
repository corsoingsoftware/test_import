package a2016.soft.ing.unipd.metronomepro.entities;

/**
 * Created by Federico Favotto on 10/12/2016.
 */

public class EntitiesBuilder {
    public static Song getSong() {
        return new ParcelableTimeSlicesSong();
    }
    public static Song getSong(String name) {
        return new ParcelableTimeSlicesSong(name);
    }
    public static Playlist getPlaylist(String name){
        return new ParcelablePlaylist(name);
    }
    //edited by Alessio and Alberto on 18/01/17
    public static MidiSong getMidiSong(){
        return new ParcelableMidiSong();
    }
    public static TimeSlicesSong getTimeSlicesSong(){
        return new ParcelableTimeSlicesSong();
    }
}
