package a2016.soft.ing.unipd.metronomepro.data.access.layer;

/**
 * Created by Federico Favotto on 09/12/2016.
 */

public interface DataProviderConstants {

    public static final String DEFAULT_PLAYLIST = "Default playlist";
    public static final String DBNAME = "MetronomeProDB";
    public static final int DB_VERSION = 1;

    public static final String TBL_SONG = "Song";
    public static final String FIELD_SONG_TITLE = "Titolo";

    public static final String TBL_PLAYLIST = "Playlist";
    public static final String FIELD_PLAYLIST_NAME = "Nome";

    public static final String TBL_TS_SONG = "TimeSlicesSong";
    public static final String FIELD_TIME_SLICES_BLOB = "Blob";

    public static final String TBL_MIDI_SONG = "MidiSlicesSong";
    public static final String FIELD_MIDI_DURATION = "Durata";
    public static final String FIELD_MIDI_PATH = "Path";

    public static final String TBL_SONG_PLAYLIST = "Contiene";
    }
