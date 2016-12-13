package a2016.soft.ing.unipd.metronomepro.entities;

/**
 * Created by feder on 10/12/2016.
 */

public class EntitiesBuilder {
    public static Song getSong() {
        return new ParcelableSong();
    }
    public static Song getSong(String name) {
        return new ParcelableSong(name);
    }
    public static Playlist getPlaylist(String name){
        return new ParcelablePlaylist(name);
    }
}
