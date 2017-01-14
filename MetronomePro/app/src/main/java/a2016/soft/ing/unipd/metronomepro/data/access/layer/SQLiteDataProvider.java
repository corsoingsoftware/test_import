package a2016.soft.ing.unipd.metronomepro.data.access.layer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import a2016.soft.ing.unipd.metronomepro.entities.EntitiesBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.MidiSong;
import a2016.soft.ing.unipd.metronomepro.entities.ParcelableTimeSlicesSong;
import a2016.soft.ing.unipd.metronomepro.entities.Playlist;
import a2016.soft.ing.unipd.metronomepro.entities.Song;
import a2016.soft.ing.unipd.metronomepro.entities.TimeSlice;
import a2016.soft.ing.unipd.metronomepro.entities.TimeSlicesSong;

/**
 * Created by Francesco on 09/12/2016.
 */

public class SQLiteDataProvider extends SQLiteOpenHelper implements DataProvider, DataProviderConstants {

    public SQLiteDataProvider(Context context) {
        super(context, DBNAME, null, DB_VERSION);
    }

    private static final String CREATE_TABLE_SONG = "CREATE TABLE "
            + TBL_SONG + " ("
            + FIELD_SONG_ID + " INTEGER PRIMARY KEY, "
            + FIELD_SONG_TITLE + " UNIQUE TEXT);";

    private static final String CREATE_TABLE_PLAYLIST = "CREATE TABLE "
            + TBL_PLAYLIST + " ("
            + FIELD_PLAYLIST_ID + " INTEGER PRIMARY KEY, "
            + FIELD_PLAYLIST_NAME + " UNIQUE TEXT);";

    private static final String CREATE_TABLE_TIMESLICES = "CREATE TABLE "
            + TBL_TS_SONG + " ("
            + FIELD_TIME_SLICES_ID + " INTEGER FOREIGN KEY (" + FIELD_TIME_SLICES_ID
            + ") REFERENCES " + TBL_SONG + " (" + FIELD_SONG_ID + "), "
            + FIELD_TIME_SLICES_SONG + " BLOB);";

    private static final String CREATE_TABLE_MIDI = "CREATE TABLE "
            + TBL_MIDI_SONG + " ("
            + FIELD_MIDI_ID + " INTEGER FOREIGN KEY (" + FIELD_MIDI_ID
            + ") REFERENCES " + TBL_SONG + " (" + FIELD_SONG_ID + "), "
            + FIELD_MIDI_PATH + " UNIQUE TEXT,"
            + FIELD_MIDI_DURATION + " INTEGER);";

    private static final String CREATE_TABLE_SONG_PLAYLIST = "CREATE TABLE "
            + TBL_SONG_PLAYLIST + " ("
            + FIELD_SP_SONG_ID + " UNIQUE INTEGER, "
            + FIELD_SP_MIDI_ID + " UNIQUE INTEGER);";

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_MIDI);
        database.execSQL(CREATE_TABLE_PLAYLIST);
        database.execSQL(CREATE_TABLE_SONG);
        database.execSQL(CREATE_TABLE_TIMESLICES);
    }

    @Override
    public void save(Song songToAdd) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues valuesToInsert = new ContentValues();
        valuesToInsert.put(FIELD_SONG_TITLE, songToAdd.getName());
        valuesToInsert.put(FIELD_SONG_ID, songToAdd.getId());

        if (songToAdd instanceof TimeSlicesSong) {

            valuesToInsert.put(FIELD_TIME_SLICES_ID, songToAdd.getId());
            valuesToInsert.put(FIELD_TIME_SLICES_SONG, ((ParcelableTimeSlicesSong) songToAdd).encode());

        } else {
            valuesToInsert.put(FIELD_SONG_TITLE, songToAdd.getName());
            valuesToInsert.put(FIELD_SONG_ID, songToAdd.getId());
            valuesToInsert.put(FIELD_MIDI_ID, songToAdd.getId());
            valuesToInsert.put(FIELD_MIDI_PATH, ((MidiSong) songToAdd).getPath());
            valuesToInsert.put(FIELD_MIDI_DURATION, ((MidiSong) songToAdd).getDuration());
        }
    }

    @Override
    public void savePlaylist(Playlist playlistToAdd) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues valesToInsert = new ContentValues();

        valesToInsert.put(FIELD_PLAYLIST_ID, playlistToAdd.getId());
        valesToInsert.put(FIELD_PLAYLIST_NAME, playlistToAdd.getName());
    }


    @Override
    public List<Song> getSongs() {
        List<Song> songsToReturn = new ArrayList<Song>();
        SQLiteDatabase database = this.getReadableDatabase();

        String queryForMidiSongs = "SELECT FROM " + TBL_MIDI_SONG;
        Cursor cursorOfMidi = database.rawQuery(queryForMidiSongs, null);
        if (cursorOfMidi.moveToFirst()) {
            do {
                MidiSong midiToAdd = EntitiesBuilder.getMidiSong();
                midiToAdd.setName(cursorOfMidi.getString(cursorOfMidi.getColumnIndex(FIELD_SONG_TITLE)));
                midiToAdd.setDuration(cursorOfMidi.getInt(cursorOfMidi.getColumnIndex(FIELD_MIDI_DURATION)));
                midiToAdd.setPath(cursorOfMidi.getString(cursorOfMidi.getColumnIndex(FIELD_MIDI_PATH)));
                songsToReturn.add(midiToAdd);
            } while (cursorOfMidi.moveToNext());
        }

        String queryForTimeSlicesSongs = "SELECT FROM " + TBL_TS_SONG;
        Cursor cursorOfTimeSlices = database.rawQuery(queryForTimeSlicesSongs, null);
        if (cursorOfTimeSlices.moveToFirst()) {
            do {
                TimeSlicesSong TimeSlicesToAdd = EntitiesBuilder.getTimeSlicesSong();
                TimeSlicesToAdd.setName(cursorOfTimeSlices.getString(cursorOfMidi.getColumnIndex(FIELD_SONG_TITLE)));
                songsToReturn.add(TimeSlicesToAdd);
            } while (cursorOfMidi.moveToNext());
        }
        return songsToReturn;
    }

    @Override
    public List<Song> getSongs(String searchName, Playlist playlist) {
        return null;
    }

    @Override
    public List<Playlist> getPlaylists(String searchName) {
        return null;
    }

    @Override
    public void deleteSong(Song songToDelete) {

    }

    @Override
    public void deletePlaylist(Playlist playlist) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
