package a2016.soft.ing.unipd.metronomepro.data.access.layer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
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
            + FIELD_SONG_TITLE + " TEXT PRIMARY KEY);";

    private static final String CREATE_TABLE_PLAYLIST = "CREATE TABLE "
            + TBL_PLAYLIST + "("
            + FIELD_PLAYLIST_NAME + " TEXT PRIMARY KEY);";

    private static final String CREATE_TABLE_TIMESLICES = "CREATE TABLE "
            + TBL_TS_SONG + "("
            + FIELD_SONG_TITLE + " TEXT PRIMARY KEY, "
            + FIELD_TIME_SLICES_BLOB + " BLOB, "
            + "FOREIGN KEY(" + FIELD_SONG_TITLE + ") REFERENCES " + TBL_SONG + "(" + FIELD_SONG_TITLE + "));";

    private static final String CREATE_TABLE_MIDI = "CREATE TABLE "
            + TBL_MIDI_SONG + "("
            + FIELD_SONG_TITLE + " TEXT PRIMARY KEY, "
            + FIELD_MIDI_PATH + " UNIQUE TEXT, "
            + FIELD_MIDI_DURATION + " INTEGER, "
            + "FOREIGN KEY(" + FIELD_SONG_TITLE + ") REFERENCES " + TBL_SONG + "(" + FIELD_SONG_TITLE + "));";

    private static final String CREATE_TABLE_SONG_PLAYLIST = "CREATE TABLE "
            + TBL_SONG_PLAYLIST + "("
            + FIELD_SONG_TITLE + " TEXT NOT NULL, "
            + FIELD_PLAYLIST_NAME + " TEXT NOT NULL, "
            + "FOREIGN KEY("+ FIELD_PLAYLIST_NAME + ") REFERENCES " + TBL_PLAYLIST + "(" + FIELD_PLAYLIST_NAME + "), "
            + "FOREIGN KEY("+ FIELD_SONG_TITLE  + ") REFERENCES " + TBL_SONG + "(" + FIELD_SONG_TITLE + "), "
            + "PRIMARY KEY("+ FIELD_PLAYLIST_NAME + ", " + FIELD_SONG_TITLE + ")); ";

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
        valuesToInsertInSong.put(FIELD_SONG_TITLE, songToAdd.getName());
        try {
            database.insertOrThrow(TBL_SONG, null, valuesToInsertInSong);
        }catch(SQLException e){
            //TODO handle the exception
            //No need to handle the exception in the next tables, if the insert goes fine on Song tbale
        }


        if (songToAdd instanceof TimeSlicesSong) {
            ContentValues valuesToInsertInTimeSlices = new ContentValues();
            valuesToInsertInTimeSlices.put(FIELD_TIME_SLICES_BLOB, ((TimeSlicesSong) songToAdd).encode());
            database.insert(TBL_TS_SONG, null, valuesToInsertInSong);

        } else {
            ContentValues valuesToInsertInMidiSong = new ContentValues();
            valuesToInsertInMidiSong.put(FIELD_MIDI_PATH, ((MidiSong) songToAdd).getPath());
            valuesToInsertInMidiSong.put(FIELD_MIDI_DURATION, ((MidiSong) songToAdd).getDuration());
            database.insert(TBL_MIDI_SONG, null, valuesToInsertInSong);
        }
    }

    @Override
    public void savePlaylist(Playlist playlistToAdd) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues valesToInsert = new ContentValues();
        valesToInsert.put(FIELD_PLAYLIST_NAME, playlistToAdd.getName());
        try {
            database.insertOrThrow(TBL_PLAYLIST, null, valesToInsert);
        }catch(SQLException e){}
    }


    @Override
    public List<Song> getSongs() {
        List<Song> songsToReturn = new ArrayList<Song>();
        SQLiteDatabase database = this.getReadableDatabase();

        String queryForMidiSongs = "SELECT * FROM " + TBL_SONG + " NATURAL JOIN " + TBL_MIDI_SONG + ";";
        Cursor cursorOfMidi = database.rawQuery(queryForMidiSongs, null);
        if (cursorOfMidi.moveToFirst()) {
            do {
                MidiSong newMidi = EntitiesBuilder.getMidiSong();
                newMidi.setName(cursorOfMidi.getString(cursorOfMidi.getColumnIndex(FIELD_SONG_TITLE)));
                newMidi.setDuration(cursorOfMidi.getInt(cursorOfMidi.getColumnIndex(FIELD_MIDI_DURATION)));
                newMidi.setPath(cursorOfMidi.getString(cursorOfMidi.getColumnIndex(FIELD_MIDI_PATH)));
                songsToReturn.add(newMidi);
            } while (cursorOfMidi.moveToNext());
        }

        String queryForTimeSlicesSongs = "SELECT * FROM " + TBL_SONG + " NATURAL JOIN " + TBL_TS_SONG + ";";
        Cursor cursorOfTimeSlices = database.rawQuery(queryForTimeSlicesSongs, null);
        if (cursorOfTimeSlices.moveToFirst()) {
            do {
                TimeSlicesSong newTimeSlices = EntitiesBuilder.getTimeSlicesSong();
                newTimeSlices.setName(cursorOfTimeSlices.getString(cursorOfTimeSlices.getColumnIndex(FIELD_SONG_TITLE)));
                newTimeSlices.decode(cursorOfTimeSlices.getBlob(cursorOfTimeSlices.getColumnIndex(FIELD_TIME_SLICES_BLOB)));
                songsToReturn.add(newTimeSlices);
            } while (cursorOfMidi.moveToNext());
        }
        return songsToReturn;
    }

    @Override
    public List<Song> getSongs(String searchName, Playlist playlist) {
        List<Song> songsToReturn = new ArrayList<Song>();
        Playlist returnedPlaylist;
        if(playlist == null && searchName == null) songsToReturn = getSongs();
        if(playlist != null){
            SQLiteDatabase database = this.getReadableDatabase();
            String queryFindPlaylist = "SELECT * FROM " + TBL_PLAYLIST
                                    + " WHERE " + FIELD_PLAYLIST_NAME + " LIKE %" + searchName +"% ;";
            Cursor cursorPlaylist = database.rawQuery(queryFindPlaylist, null);
            if (cursorPlaylist.moveToFirst())
                returnedPlaylist = EntitiesBuilder.getPlaylist(cursorPlaylist.getString(cursorPlaylist.getColumnIndex(FIELD_PLAYLIST_NAME)));
        }
        //TODO end this method
        return songsToReturn;
    }

    @Override
    public List<Playlist> getPlaylists(String searchName) {
        List<Playlist> playlistsToReturn = new ArrayList<Playlist>();
        SQLiteDatabase database = this.getReadableDatabase();
        String pieceOfQuery = "";
        if(searchName != null) pieceOfQuery += " WHERE " + FIELD_PLAYLIST_NAME + " LIKE %" + searchName + "%";
        String queryFindPlaylists = "SELECT * FROM " + TBL_PLAYLIST + pieceOfQuery + ";";
        Cursor cursorPlaylists = database.rawQuery(queryFindPlaylists, null);
        if(cursorPlaylists.moveToFirst()){
            do{
                playlistsToReturn.add(EntitiesBuilder.getPlaylist(cursorPlaylists.getString(cursorPlaylists.getColumnIndex(FIELD_PLAYLIST_NAME))));
            }
            while(cursorPlaylists.moveToNext());
        }
        return playlistsToReturn;
    }

    @Override
    public void deleteSong(Song songToDelete) {
        SQLiteDatabase database = this.getWritableDatabase();
        String tableType = (songToDelete instanceof MidiSong)? TBL_MIDI_SONG : TBL_TS_SONG;
        String queryDeleteFromSongType = "DELETE FROM " + tableType + " WHERE " + FIELD_SONG_TITLE + " = " + songToDelete + ";";
        String queryDeleteFromSong = "DELETE FROM " + TBL_SONG + " WHERE " + FIELD_SONG_TITLE + " = " + songToDelete + ";";
        String queryDeleteFromPlaylistSong = "DELETE FROM " + TBL_SONG_PLAYLIST + " WHERE " + FIELD_SONG_TITLE + " = " + songToDelete + ";";
        database.execSQL(queryDeleteFromSongType);
        database.execSQL(queryDeleteFromSong);
    }

    @Override
    public void deletePlaylist(Playlist playlist) {
        //TODO I have to end this
        SQLiteDatabase database = this.getWritableDatabase();
        //String tableType = (songToDelete instanceof MidiSong)? TBL_MIDI_SONG : TBL_TS_SONG;
        //String queryDeleteSongType = "DELETE FROM " + tableType + " WHERE " + FIELD_SONG_TITLE + " = " + songToDelete + ";";
        //String queryDeleteSong = "DELETE FROM " + TBL_SONG + " WHERE " + FIELD_SONG_TITLE + " = " + songToDelete + ";";
        //database.execSQL(queryDeleteSongType);
        //database.execSQL(queryDeleteSong);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
