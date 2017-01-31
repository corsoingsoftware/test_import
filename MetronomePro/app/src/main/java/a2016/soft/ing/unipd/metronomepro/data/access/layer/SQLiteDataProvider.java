package a2016.soft.ing.unipd.metronomepro.data.access.layer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.Settings;
import android.util.Log;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import a2016.soft.ing.unipd.metronomepro.entities.EntitiesBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.MidiSong;
import a2016.soft.ing.unipd.metronomepro.entities.Playlist;
import a2016.soft.ing.unipd.metronomepro.entities.Song;
import a2016.soft.ing.unipd.metronomepro.entities.TimeSlice;
import a2016.soft.ing.unipd.metronomepro.entities.TimeSlicesSong;

/**
 * Created by Francesco, Alessio, Alberto on 09/12/2016.
 * @author Alessio Munerato
 */

public class SQLiteDataProvider extends SQLiteOpenHelper implements DataProvider, DataProviderConstants {

    //queries edited by Munerato and Moretto
    private static final String CREATE_TABLE_SONG = "CREATE TABLE "
            + TBL_SONG + "("
            + FIELD_SONG_ID + " VARCHAR(50) PRIMARY KEY);";

    private static final String CREATE_TABLE_TIMESLICES = "CREATE TABLE "
            + TBL_TS_SONG + "("
            + FIELD_SONG_ID + " VARCHAR(50) PRIMARY KEY, "
            + FIELD_TS_BLOB + " BLOB, "
            + "FOREIGN KEY(" + FIELD_SONG_ID + ") REFERENCES " + TBL_SONG + "(" + FIELD_SONG_ID + ") ON UPDATE CASCADE ON DELETE CASCADE);";

    private static final String CREATE_TABLE_MIDI = "CREATE TABLE "
            + TBL_MD_SONG + " ("
            + FIELD_SONG_ID + " VARCHAR(50) PRIMARY KEY, "
            + FIELD_MD_PATH + " TEXT, "
            + "FOREIGN KEY(" + FIELD_SONG_ID + ") REFERENCES " + TBL_SONG + "(" + FIELD_SONG_ID + ") ON UPDATE CASCADE ON DELETE CASCADE);";

    private static final String CREATE_TABLE_PLAYLIST = "CREATE TABLE "
            + TBL_PLAYLIST + "("
            + FIELD_PLAYLIST_ID + " VARCHAR(50) NOT NULL, "
            + FIELD_SONG_ID + " VARCHAR(50) NOT NULL, "
            + FIELD_SONG_INDEX + " INTEGER NOT NULL, "
            + "FOREIGN KEY(" + FIELD_SONG_ID + ") REFERENCES " + TBL_SONG + "(" + FIELD_SONG_ID + ") ON UPDATE CASCADE ON DELETE CASCADE, "
            + "PRIMARY KEY(" + FIELD_PLAYLIST_ID + ", " + FIELD_SONG_ID + "));";


    public SQLiteDataProvider(Context context) {
        super(context, DBNAME, null, DB_VERSION);
    }

    public SQLiteDataProvider(Context context, int version) {
        super(context, DBNAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        try {
            database.execSQL(CREATE_TABLE_SONG);
            database.execSQL(CREATE_TABLE_MIDI);
            database.execSQL(CREATE_TABLE_TIMESLICES);
            database.execSQL(CREATE_TABLE_PLAYLIST);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion < newVersion){
            db.execSQL("DROP TABLE IF EXIST " + TBL_MD_SONG);
            db.execSQL("DROP TABLE IF EXIST " + TBL_TS_SONG);
            db.execSQL("DROP TABLE IF EXIST " + TBL_PLAYLIST);
            db.execSQL("DROP TABLE IF EXIST " + TBL_TS_SONG);
            onCreate(db);
        }
    }

    @Override
    public void onConfigure(SQLiteDatabase db){
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public boolean saveSong(Song newSong) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues songValues = new ContentValues();
        songValues.put(FIELD_SONG_ID, newSong.getName());
        try {
            database.insertOrThrow(TBL_SONG, null, songValues);
            if (newSong instanceof TimeSlicesSong) {
                songValues.put(FIELD_TS_BLOB, ((TimeSlicesSong) newSong).encode());
                database.insert(TBL_TS_SONG, null, songValues);
            } else {
                songValues.put(FIELD_MD_PATH, ((MidiSong) newSong).getPath());
                database.insert(TBL_MD_SONG, null, songValues);
            }
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean savePlaylist(Playlist newPlaylist) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues playlistValues = new ContentValues();
        for (Song song : newPlaylist) {
            playlistValues.put(FIELD_PLAYLIST_ID, newPlaylist.getName());
            playlistValues.put(FIELD_SONG_ID, song.getName());
            playlistValues.put(FIELD_SONG_INDEX, newPlaylist.getSongIndex(song));
            try {
                database.insertOrThrow(TBL_PLAYLIST, null, playlistValues);
            } catch (SQLException e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Song getSong(String songTitle){
        SQLiteDatabase database = this.getReadableDatabase();
        String queryFirst = "SELECT * FROM ";
        String queryLast = " WHERE " + FIELD_SONG_ID + " = '" + songTitle +"'";
        String querySong = "" + queryFirst + TBL_MD_SONG + queryLast;
        Cursor cursorSongs = database.rawQuery(querySong, null);
        if (cursorSongs.moveToFirst()) {
            MidiSong newMidi = EntitiesBuilder.getMidiSong();
            newMidi.setName(cursorSongs.getString(cursorSongs.getColumnIndex(FIELD_SONG_ID)));
            newMidi.setPath(cursorSongs.getString(cursorSongs.getColumnIndex(FIELD_MD_PATH)));
            return newMidi;
        }
        querySong = "" + queryFirst + TBL_TS_SONG + queryLast;
        cursorSongs = database.rawQuery(querySong, null);
        if (cursorSongs.moveToFirst()) {
            TimeSlicesSong newTimeSlices = EntitiesBuilder.getTimeSlicesSong();
            newTimeSlices.setName(cursorSongs.getString(cursorSongs.getColumnIndex(FIELD_SONG_ID)));
            newTimeSlices.decode(cursorSongs.getBlob(cursorSongs.getColumnIndex(FIELD_TS_BLOB)));
            return newTimeSlices;
        }
        return null;
    }

    @Override
    public List<Song> getAllSongs() {
        List<Song> songList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM ";
        Cursor cursorSongs = database.rawQuery("" + query +TBL_MD_SONG, null);
        if (cursorSongs.moveToFirst()) {
            do {
                MidiSong newMidi = EntitiesBuilder.getMidiSong();
                newMidi.setName(cursorSongs.getString(cursorSongs.getColumnIndex(FIELD_SONG_ID)));
                newMidi.setPath(cursorSongs.getString(cursorSongs.getColumnIndex(FIELD_MD_PATH)));
                songList.add(newMidi);
            } while (cursorSongs.moveToNext());
        }
        cursorSongs = database.rawQuery("" + query + TBL_TS_SONG, null);
        if (cursorSongs.moveToFirst()) {
            do {
                TimeSlicesSong newTimeSlices = EntitiesBuilder.getTimeSlicesSong();
                newTimeSlices.setName(cursorSongs.getString(cursorSongs.getColumnIndex(FIELD_SONG_ID)));
                newTimeSlices.decode(cursorSongs.getBlob(cursorSongs.getColumnIndex(FIELD_TS_BLOB)));
                songList.add(newTimeSlices);
            } while (cursorSongs.moveToNext());
        }
        return songList;
    }

    @Override
    public Playlist getPlaylist(String playlistName){
        SQLiteDatabase database = this.getReadableDatabase();
        String queryPlaylist = "SELECT * FROM " + TBL_PLAYLIST + " WHERE " + FIELD_PLAYLIST_ID
                + " = '" + playlistName + "'";
        Cursor cursorPlaylist = database.rawQuery(queryPlaylist, null);
        if (cursorPlaylist.moveToFirst()) {
            SparseArray<String> songMap = new SparseArray<>();
            String tempPlaylistName = cursorPlaylist.getString(cursorPlaylist.getColumnIndex(FIELD_PLAYLIST_ID));
            Playlist newPlaylist = EntitiesBuilder.getPlaylist(tempPlaylistName);
            int indexSong;
            int maxIndex = -1;
            String song;
            do{
                song = cursorPlaylist.getString(cursorPlaylist.getColumnIndex(FIELD_SONG_ID));
                indexSong = cursorPlaylist.getInt(cursorPlaylist.getColumnIndex(FIELD_SONG_INDEX));
                songMap.append(indexSong, song);
                if (indexSong > maxIndex) maxIndex = indexSong;
            } while (cursorPlaylist.moveToNext());
            List<Song> allSongs = getAllSongs();
            Song searchSong;
            Iterator <Song> songIterator;
            boolean flag;
            for (int i = 0; i < maxIndex + 1; i++){
                flag = true;
                songIterator = allSongs.iterator();
                while (songIterator.hasNext() && flag){
                    searchSong = songIterator.next();
                    if (searchSong.getName().equals(songMap.get(i))) {
                        newPlaylist.add(searchSong);
                        songIterator.remove();
                        flag = false;
                    }
                }

            }
            return newPlaylist;
        }
        return null;
    }

    @Override
    public List<String> getAllPlaylists() {
        List<String> playlistsToReturn = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        String queryPlaylist = "SELECT " + FIELD_PLAYLIST_ID + " FROM " + TBL_PLAYLIST
                + " GROUP BY " + FIELD_PLAYLIST_ID;
        Cursor cursorPlaylist = database.rawQuery(queryPlaylist, null);
        if(cursorPlaylist.moveToFirst()){
            do{
                playlistsToReturn.add(cursorPlaylist.getString(cursorPlaylist.getColumnIndex(FIELD_PLAYLIST_ID)));
            } while (cursorPlaylist.moveToNext());
        }
        return playlistsToReturn;
    }

    @Override
    public boolean deleteSong(Song oldSong) {
        SQLiteDatabase database = this.getWritableDatabase();
        String queryDelete = "DELETE FROM " + TBL_SONG + " WHERE " + FIELD_SONG_ID + " = '"
                + oldSong.getName() + "'";
        try {
            database.execSQL(queryDelete);
        } catch (SQLException e) {
            Log.e("DB", e.toString());
            return false;
        }
        return true;
    }

    @Override
    public boolean deletePlaylist(Playlist oldPlaylist) {
        SQLiteDatabase database = this.getWritableDatabase();
        String queryDelete = "DELETE FROM " + TBL_PLAYLIST + " WHERE " + FIELD_PLAYLIST_ID + " = '"
                + oldPlaylist.getName() + "';";
        try {
            database.execSQL(queryDelete);
        } catch (SQLException e) {
            throw  new SQLException("");
        }
        return true;
    }

    @Override
    public boolean modifySong(Song oldSong, Song newSong) {
        SQLiteDatabase database = getWritableDatabase();
        String oldSongName = oldSong.getName();
        if (oldSong.getName().compareTo(newSong.getName()) != 0) {
            String queryUpdateName = "UPDATE " + TBL_SONG + " SET " + FIELD_SONG_ID + " = '"
                    + newSong.getName() + "' WHERE " + FIELD_SONG_ID + " = '"
                    + oldSong.getName() + "'";
            try {
                database.execSQL(queryUpdateName);
                oldSongName = newSong.getName();
            } catch (SQLException e) {
                Log.e("DB", e.toString());
                return false;
            }
        }
        if (oldSong.getClass().equals(newSong.getClass())) {
            String queryUpdateSong = "";
            if (oldSong instanceof MidiSong) {
                queryUpdateSong = "UPDATE " + TBL_MD_SONG + " SET " + FIELD_MD_PATH + " = '"
                        + ((MidiSong) newSong).getPath() + "' WHERE " + FIELD_SONG_ID + " = '" + oldSongName + "'";
            }
            if (oldSong instanceof TimeSlicesSong) {
                queryUpdateSong = "UPDATE " + TBL_TS_SONG + " SET " + FIELD_TS_BLOB + " = "
                        + ((TimeSlicesSong) newSong).encode() + " WHERE " + FIELD_SONG_ID + " = '" + oldSongName + "'";
            }
            try{
                database.execSQL(queryUpdateSong);
            } catch (SQLException e) {
                Log.e("DB", e.toString());
                return false;
            }
        }else{
            String queryDelete = "";
            String tableType = "";
            ContentValues songValues = new ContentValues();
            if (oldSong instanceof MidiSong) {
                queryDelete = "DELETE FROM " + TBL_MD_SONG + " WHERE " + FIELD_SONG_ID + " = '" + oldSongName + "'";
                songValues.put(FIELD_SONG_ID, newSong.getName());
                songValues.put(FIELD_TS_BLOB, ((TimeSlicesSong) newSong).encode());
                tableType = TBL_TS_SONG;
            }
            if (oldSong instanceof TimeSlicesSong) {
                queryDelete = "DELETE FROM " + TBL_TS_SONG + " WHERE " + FIELD_SONG_ID + " = '" + oldSongName + "'";
                songValues.put(FIELD_SONG_ID, newSong.getName());
                songValues.put(FIELD_MD_PATH, ((MidiSong) newSong).getPath());
                tableType = TBL_MD_SONG;
            }
            try{
                database.execSQL(queryDelete);
                database.insert(tableType, null, songValues);
            } catch (SQLException e) {
                Log.e("DB", e.toString());
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean modifyPlaylist(Playlist oldPlaylist, Playlist newPlaylist) {
        return deletePlaylist(oldPlaylist) && savePlaylist(newPlaylist);
    }

}
