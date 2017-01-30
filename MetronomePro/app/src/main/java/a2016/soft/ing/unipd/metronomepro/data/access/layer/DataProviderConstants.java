package a2016.soft.ing.unipd.metronomepro.data.access.layer;

/**
 * Created by Federico Favotto on 09/12/2016.
 * @author Alessio Munerato
 */

public interface DataProviderConstants {

    public static final String DBNAME = "MetronomeProDB";
    public static final int DB_VERSION = 1;

    public static final String TBL_SONG = "Song";
    public static final String FIELD_SONG_ID = "Title";

    public static final String TBL_PLAYLIST = "Playlist";
    public static final String FIELD_PLAYLIST_ID = "PlaylistName";
    public static final String FIELD_SONG_INDEX = "Position";


    public static final String TBL_TS_SONG = "TimeSlices";
    public static final String FIELD_TS_BLOB = "Blob";

    public static final String TBL_MD_SONG = "Midi";
    public static final String FIELD_MD_PATH = "Path";



    }
