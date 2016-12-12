package a2016.soft.ing.unipd.metronomepro.data.access.layer;

/**
 * Created by feder on 09/12/2016.
 */

public interface DataProviderConstants {

    public static final String DBNAME = "MetronomeProDB";
    public static final int DB_VERSION = 1;
    public static final String TBL_TRACK = "Tracce";
    public static final String FIELD_TRACK_NAME = "Nome";
    public static final String FIELD_TRACK_SONG = "Canzone";
    public static final String TBL_PLAYLIST = "Playlist";
    public static final String FIELD_PLAYLIST_NAME = "Nome";
    public static final String TBL_ASSOCIATION = "Include";
    public static final String FIELD_ASSOCIATION_SONGS = "Tracce";
    public static final String FIELD_ASSOCIATION_PLAYLIST = "Playlist";
    public static final String FIELD_ASSOCIATION_POSITION = "Posizione";
}
