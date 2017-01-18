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
 * Created by Francesco, Alessio, Alberto on 09/12/2016.
 */

public class SQLiteDataProvider extends SQLiteOpenHelper implements DataProvider, DataProviderConstants {

    public SQLiteDataProvider(Context context) {
        super(context, DBNAME, null, DB_VERSION);
    }

    //query's edited by Alessio and Alberto
    private static final String CREATE_TABLE_SONG = "CREATE TABLE "
            + TBL_SONG + "("
            + FIELD_SONG_ID + " INTEGER PRIMARY KEY, "
            + FIELD_SONG_TITLE + " UNIQUE TEXT);";

    private static final String CREATE_TABLE_PLAYLIST = "CREATE TABLE "
            + TBL_PLAYLIST + "("
            + FIELD_PLAYLIST_ID + " INTEGER PRIMARY KEY, "
            + FIELD_PLAYLIST_NAME + " UNIQUE TEXT);";

    private static final String CREATE_TABLE_TIMESLICES = "CREATE TABLE "
            + TBL_TS_SONG + "("
            + FIELD_TIME_SLICES_ID + " INTEGER, "
            + FIELD_TIME_SLICES_SONG + " BLOB, "
            + "FOREIGN KEY(" + FIELD_TIME_SLICES_ID + ") REFERENCES " + TBL_SONG + "(" + FIELD_SONG_ID + "));";

    private static final String CREATE_TABLE_MIDI = "CREATE TABLE "
            + TBL_MIDI_SONG + "("
            + FIELD_MIDI_ID + " INTEGER, "
            + FIELD_MIDI_PATH + " UNIQUE TEXT, "
            + FIELD_MIDI_DURATION + " INTEGER, "
            + "FOREIGN KEY(" + FIELD_MIDI_ID + ") REFERENCES " + TBL_SONG + "(" + FIELD_SONG_ID + "));";

    private static final String CREATE_TABLE_SONG_PLAYLIST = "CREATE TABLE "
            + TBL_SONG_PLAYLIST + "("
            + FIELD_SONG_ID + " INTEGER NOT NULL, "
            + FIELD_PLAYLIST_ID + " INTEGER NOT NULL, "
            + "FOREIGN KEY("+ FIELD_PLAYLIST_ID + ") REFERENCES " + TBL_PLAYLIST + "(" + FIELD_PLAYLIST_ID + "), "
            + "FOREIGN KEY("+ FIELD_SONG_ID + ") REFERENCES " + TBL_SONG + "(" + FIELD_SONG_ID + "), "
            + "PRIMARY KEY("+ FIELD_PLAYLIST_ID + ", " + FIELD_SONG_ID + ")); ";

    @Override

    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_MIDI);
        database.execSQL(CREATE_TABLE_PLAYLIST);
        database.execSQL(CREATE_TABLE_SONG);
        database.execSQL(CREATE_TABLE_TIMESLICES);
        database.execSQL(CREATE_TABLE_SONG_PLAYLIST);
    }

    @Override
    public void save(Song songToAdd) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues valuesToInsertInSong = new ContentValues();
        valuesToInsertInSong.put(FIELD_SONG_ID, songToAdd.getId());
        valuesToInsertInSong.put(FIELD_SONG_TITLE, songToAdd.getName());
        database.insert(TBL_SONG, null, valuesToInsertInSong);


        if (songToAdd instanceof TimeSlicesSong) {
            ContentValues valuesToInsertInTimeSlices = new ContentValues();
            valuesToInsertInTimeSlices.put(FIELD_TIME_SLICES_ID, songToAdd.getId());
            valuesToInsertInTimeSlices.put(FIELD_TIME_SLICES_SONG, ((TimeSlicesSong) songToAdd).encode());
            database.insert(TBL_TS_SONG, null, valuesToInsertInSong);

        } else {
            ContentValues valuesToInsertInMidiSong = new ContentValues();
            valuesToInsertInMidiSong.put(FIELD_MIDI_ID, songToAdd.getId());
            valuesToInsertInMidiSong.put(FIELD_MIDI_PATH, ((MidiSong) songToAdd).getPath());
            valuesToInsertInMidiSong.put(FIELD_MIDI_DURATION, ((MidiSong) songToAdd).getDuration());
            database.insert(TBL_MIDI_SONG, null, valuesToInsertInSong);
        }
    }

    @Override
    public void savePlaylist(Playlist playlistToAdd) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues valesToInsert = new ContentValues();
        valesToInsert.put(FIELD_PLAYLIST_ID, playlistToAdd.getId());
        valesToInsert.put(FIELD_PLAYLIST_NAME, playlistToAdd.getName());
        database.insert(TBL_PLAYLIST, null, valesToInsert);
    }


    @Override
    public List<Song> getSongs() {
        List<Song> songsToReturn = new ArrayList<Song>();
        SQLiteDatabase database = this.getReadableDatabase();

        String queryForMidiSongs = "SELECT * FROM " + TBL_SONG + " JOIN " + TBL_MIDI_SONG + " ON "
                + FIELD_SONG_ID + " = " + FIELD_MIDI_ID +";";
        Cursor cursorOfMidi = database.rawQuery(queryForMidiSongs, null);
        if (cursorOfMidi.moveToFirst()) {
            do {
                MidiSong midiToAdd = EntitiesBuilder.getMidiSong();
                midiToAdd.setId(cursorOfMidi.getInt(cursorOfMidi.getColumnIndex(FIELD_MIDI_ID)));
                midiToAdd.setName(cursorOfMidi.getString(cursorOfMidi.getColumnIndex(FIELD_SONG_TITLE)));
                midiToAdd.setDuration(cursorOfMidi.getInt(cursorOfMidi.getColumnIndex(FIELD_MIDI_DURATION)));
                midiToAdd.setPath(cursorOfMidi.getString(cursorOfMidi.getColumnIndex(FIELD_MIDI_PATH)));
                songsToReturn.add(midiToAdd);
            } while (cursorOfMidi.moveToNext());
        }

        String queryForTimeSlicesSongs = "SELECT * FROM " + TBL_SONG + " JOIN " + TBL_TS_SONG + " ON "
                + FIELD_SONG_ID + "=" + FIELD_TIME_SLICES_ID +";";
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
