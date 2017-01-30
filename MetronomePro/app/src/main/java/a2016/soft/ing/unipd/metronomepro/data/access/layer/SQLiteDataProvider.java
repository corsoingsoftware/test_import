package a2016.soft.ing.unipd.metronomepro.data.access.layer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.Settings;
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
import a2016.soft.ing.unipd.metronomepro.entities.TimeSlicesSong;

/**
 * Created by Francesco, Alessio, Alberto on 09/12/2016.
 * @author Alessio Munerato
 */

public class SQLiteDataProvider extends SQLiteOpenHelper implements DataProvider, DataProviderConstants {

    public SQLiteDataProvider(Context context) {
        super(context, DBNAME, null, DB_VERSION);
    }

    //queries edited by Munerato and Moretto
    private static final String CREATE_TABLE_SONG = "CREATE TABLE "
            + TBL_SONG + "("
            + FIELD_SONG_ID + " VARCHAR(50) PRIMARY KEY);";

    //We dont need that anymore, all playlists are stored on TBL_SONG_PLAYLIST, by Munerato
    /*private static final String CREATE_TABLE_PLAYLIST = "CREATE TABLE "
            + TBL_PLAYLIST + "("
            + FIELD_PLAYLIST_NAME + " TEXT PRIMARY KEY);";
    */

    private static final String CREATE_TABLE_TIMESLICES = "CREATE TABLE "
            + TBL_TS_SONG + "("
            + FIELD_SONG_ID + " VARCHAR(50) PRIMARY KEY, "
            + FIELD_TS_BLOB + " BLOB, "
            + "FOREIGN KEY(" + FIELD_SONG_ID + ") REFERENCES " + TBL_SONG + "(" + FIELD_SONG_ID + "));";

    private static final String CREATE_TABLE_MIDI = "CREATE TABLE "
            + TBL_MD_SONG + " ("
            + FIELD_SONG_ID + " VARCHAR(50) PRIMARY KEY, "
            + FIELD_MD_PATH + " TEXT, "
            + "FOREIGN KEY(" + FIELD_SONG_ID + ") REFERENCES " + TBL_SONG + "(" + FIELD_SONG_ID + "));";

    private static final String CREATE_TABLE_PLAYLIST = "CREATE TABLE "
            + TBL_PLAYLIST + "("
            + FIELD_PLAYLIST_ID + " VARCHAR(50) NOT NULL, "
            + FIELD_SONG_ID + " VARCHAR(50) NOT NULL, "
            + FIELD_SONG_INDEX + " INTEGER NOT NULL, "
            + "FOREIGN KEY(" + FIELD_SONG_ID + ") REFERENCES " + TBL_SONG + "(" + FIELD_SONG_ID + "), "
            + "PRIMARY KEY(" + FIELD_PLAYLIST_ID + ", " + FIELD_SONG_ID + ")); ";

    /**
     * @param database
     */
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
    public boolean saveSong(Song newSong) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues songValues = new ContentValues();
        songValues.put(FIELD_SONG_ID, newSong.getName());
        try {
            database.insertOrThrow(TBL_SONG, null, songValues);
            if (newSong instanceof TimeSlicesSong) {
                ContentValues timeSliceValues = new ContentValues();
                timeSliceValues.put(FIELD_SONG_ID, newSong.getName());
                timeSliceValues.put(FIELD_TS_BLOB, ((TimeSlicesSong) newSong).encode());
                database.insert(TBL_TS_SONG, null, timeSliceValues);
            } else {
                ContentValues midiValues = new ContentValues();
                midiValues.put(FIELD_SONG_ID, newSong.getName());
                midiValues.put(FIELD_MD_PATH, ((MidiSong) newSong).getPath());
                database.insert(TBL_MD_SONG, null, midiValues);
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


    public Playlist getPlaylist(String playlistName){
        SQLiteDatabase database = this.getReadableDatabase();
        String queryPlaylist = "SELECT * FROM " + TBL_PLAYLIST + " WHERE " + FIELD_PLAYLIST_ID + " = '" + playlistName + "'";
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
            //Fa un po' schifo...rendere più efficiente
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

    /*@Override
    public List<Song> getSong(String songTitle, Playlist playlist) {
        SparseArray<Song> songMap = new SparseArray<Song>();
        int maxIndexSong = -1;
        List<Song> songsToReturn = new ArrayList<Song>();
        SQLiteDatabase database = this.getReadableDatabase();
        String queryMd = "SELECT * FROM " + TBL_MD_SONG;
        String queryTs = "SELECT * FROM " + TBL_TS_SONG;
        if(playlist == null){
            if(songTitle!=null) {
                queryMd += " WHERE " + FIELD_SONG_ID + " = '" + songTitle +"'";
                queryTs += " WHERE " + FIELD_SONG_ID + " = '" + songTitle +"'";
            }
        }else{
            queryMd += " INNER JOIN " + TBL_PLAYLIST + " ON " + TBL_MD_SONG + "." + FIELD_SONG_ID + " = " + TBL_PLAYLIST + "." + FIELD_SONG_ID;
            queryTs += " INNER JOIN " + TBL_PLAYLIST + " ON " + TBL_TS_SONG + "." + FIELD_SONG_ID + " = " + TBL_PLAYLIST + "." + FIELD_SONG_ID;
            queryMd += " WHERE " + TBL_PLAYLIST + "." + FIELD_PLAYLIST_ID + " = '" + playlist.getName() +"'";
            queryTs += " WHERE " + TBL_PLAYLIST + "." + FIELD_PLAYLIST_ID + " = '" + playlist.getName() +"'";
            if(songTitle!=null) {
                queryMd += " AND " + TBL_MD_SONG + "." + FIELD_SONG_ID + " = '" + songTitle +"'";
                queryTs += " AND " + TBL_TS_SONG + "." + FIELD_SONG_ID + " = '" + songTitle +"'";
            }
        }
        Cursor cursorSongs = database.rawQuery(queryMd, null);
        if (cursorSongs.moveToFirst()) {
            int tempIndexSong;
            do {
                MidiSong newMidi = EntitiesBuilder.getMidiSong();
                newMidi.setName(cursorSongs.getString(cursorSongs.getColumnIndex(FIELD_SONG_ID)));
                newMidi.setPath(cursorSongs.getString(cursorSongs.getColumnIndex(FIELD_MD_PATH)));
                if(playlist!=null) {
                    tempIndexSong = cursorSongs.getInt(cursorSongs.getColumnIndex(FIELD_SONG_INDEX));
                    songMap.append(tempIndexSong, newMidi);
                    if (tempIndexSong > maxIndexSong) {
                        maxIndexSong = tempIndexSong;
                    }
                }else{
                    songsToReturn.add(newMidi);
                }
            } while (cursorSongs.moveToNext());
        }
        cursorSongs = database.rawQuery(queryTs, null);
        if (cursorSongs.moveToFirst()) {
            int tempIndexSong;
            do {
                TimeSlicesSong newTimeSlices = EntitiesBuilder.getTimeSlicesSong();
                newTimeSlices.setName(cursorSongs.getString(cursorSongs.getColumnIndex(FIELD_SONG_ID)));
                newTimeSlices.decode(cursorSongs.getBlob(cursorSongs.getColumnIndex(FIELD_TS_BLOB)));
                if(playlist!=null) {
                    tempIndexSong = cursorSongs.getInt(cursorSongs.getColumnIndex(FIELD_SONG_INDEX));
                    songMap.append(tempIndexSong, newTimeSlices);
                    if (tempIndexSong > maxIndexSong) {
                        maxIndexSong = tempIndexSong;
                    }
                }else{
                    songsToReturn.add(newTimeSlices);
                }
            } while (cursorSongs.moveToNext());
        }
        if (maxIndexSong > -1) {
            for (int i = 0; i < maxIndexSong + 1; i++) {
                songsToReturn.add(songMap.get(i));
            }
        }
        return songsToReturn;
    }*/

    @Override
    public List<String> getAllPlaylists() {
        List<String> playlistsToReturn = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        String queryPlaylist = "SELECT " + FIELD_PLAYLIST_ID + " FROM " + TBL_PLAYLIST + " GROUP BY " + FIELD_PLAYLIST_ID;
        Cursor cursorPlaylist = database.rawQuery(queryPlaylist, null);
        if(cursorPlaylist.moveToFirst()){
            do{
                playlistsToReturn.add(cursorPlaylist.getString(cursorPlaylist.getColumnIndex(FIELD_PLAYLIST_ID)));
            } while (cursorPlaylist.moveToNext());
        }
        return playlistsToReturn;
    }

    @Override
    public boolean deleteSong(Song songToDelete) {
        SQLiteDatabase database = this.getWritableDatabase();
        String tableType = (songToDelete instanceof MidiSong) ? TBL_MD_SONG : TBL_TS_SONG;
        String queryDelSongType = "DELETE FROM " + tableType + " WHERE " + FIELD_SONG_ID + " = '" + songToDelete.getName() + "';";
        String queryDelSong = "DELETE FROM " + TBL_SONG + " WHERE " + FIELD_SONG_ID + " = '" + songToDelete.getName() + "';";
        String queryDelPlaylistSong = "DELETE FROM " + TBL_PLAYLIST + " WHERE " + FIELD_SONG_ID + " = '" + songToDelete.getName() + "';";
        try {
            database.execSQL(queryDelPlaylistSong);
            database.execSQL(queryDelSongType);
            database.execSQL(queryDelSong);
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean deletePlaylist(Playlist playlist) {
        SQLiteDatabase database = this.getWritableDatabase();
        String queryDelPlaylist = "DELETE FROM " + TBL_PLAYLIST + " WHERE " + FIELD_PLAYLIST_ID + " = '" + playlist.getName() + "';";
        try {
            database.execSQL(queryDelPlaylist);
        } catch (SQLException e) {
            return false;
        }
        return true;
    }


    @Override
    public boolean modifySong(Song oldSong, Song newSong) {
        SQLiteDatabase database = getWritableDatabase();
        if (oldSong.getName().compareTo(newSong.getName()) != 0) {
            String queryUpdate = "UPDATE " + TBL_PLAYLIST + " SET " + FIELD_SONG_ID + " = '" + newSong.getName()
                    + "' WHERE " + FIELD_SONG_ID + " = '" + oldSong.getName() + "';";
            try {
                database.execSQL(queryUpdate);
            } catch (SQLException e) {
                return false;
            }
        }
        //TODO Verificare che DELETE + INSERT qui sotto non crei violazioni di integrità referenziale nel db
        return deleteSong(oldSong) && saveSong(newSong);
    }

    @Override
    public boolean modifyPlaylist(Playlist oldPlaylist, Playlist newPlaylist) {
        return deletePlaylist(oldPlaylist) && savePlaylist(newPlaylist);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //// TODO: 27/01/2017 sistemare nelle prossime versioni
        onCreate(db);
    }

}
