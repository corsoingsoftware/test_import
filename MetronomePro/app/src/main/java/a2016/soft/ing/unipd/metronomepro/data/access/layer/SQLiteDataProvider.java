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
import a2016.soft.ing.unipd.metronomepro.entities.Playlist;
import a2016.soft.ing.unipd.metronomepro.entities.Song;
import a2016.soft.ing.unipd.metronomepro.entities.TimeSlicesSong;

/**
 * Created by Francesco, Alessio, Alberto on 09/12/2016.
 */

public class SQLiteDataProvider extends SQLiteOpenHelper implements DataProvider, DataProviderConstants {

    public final String ALL_SONGS = null;
    public final Playlist NO_PLAYLISTS = null;

    public SQLiteDataProvider(Context context) {
        super(context, DBNAME, null, DB_VERSION);
    }

    //queries edited by Munerato and Moretto
    private static final String CREATE_TABLE_SONG = "CREATE TABLE "
            + TBL_SONG + "("
            + FIELD_SONG_TITLE + " VARCHAR(50) PRIMARY KEY);";

    //We dont need that anymore, all playlists are stored on TBL_SONG_PLAYLIST, by Munerato
    /*private static final String CREATE_TABLE_PLAYLIST = "CREATE TABLE "
            + TBL_PLAYLIST + "("
            + FIELD_PLAYLIST_NAME + " TEXT PRIMARY KEY);";
    */

    private static final String CREATE_TABLE_TIMESLICES = "CREATE TABLE "
            + TBL_TS_SONG + "("
            + FIELD_SONG_TITLE + " VARCHAR(50) PRIMARY KEY, "
            + FIELD_TIME_SLICES_BLOB + " BLOB, "
            + "FOREIGN KEY(" + FIELD_SONG_TITLE + ") REFERENCES " + TBL_SONG + "(" + FIELD_SONG_TITLE + "));";

    private static final String CREATE_TABLE_MIDI = "CREATE TABLE "
            + TBL_MIDI_SONG + " ("
            + FIELD_SONG_TITLE + " VARCHAR(50) PRIMARY KEY, "
            + FIELD_MIDI_PATH + " TEXT, "
            + "FOREIGN KEY(" + FIELD_SONG_TITLE + ") REFERENCES " + TBL_SONG + "(" + FIELD_SONG_TITLE + "));";

    private static final String CREATE_TABLE_SONG_PLAYLIST = "CREATE TABLE "
            + TBL_SONG_PLAYLIST + "("
            + FIELD_SONG_TITLE + " TEXT NOT NULL, "
            + FIELD_PLAYLIST_NAME + " TEXT NOT NULL, "
            + FIELD_INDEX_SONG + " INTEGER NOT NULL, "
            + "FOREIGN KEY(" + FIELD_PLAYLIST_NAME + ") REFERENCES " + TBL_PLAYLIST + "(" + FIELD_PLAYLIST_NAME + "), "
            + "FOREIGN KEY(" + FIELD_SONG_TITLE + ") REFERENCES " + TBL_SONG + "(" + FIELD_SONG_TITLE + "), "
            + "PRIMARY KEY(" + FIELD_PLAYLIST_NAME + ", " + FIELD_SONG_TITLE + ")); ";

    /**
     * @param database
     */
    @Override
    public void onCreate(SQLiteDatabase database) {
        try {

            database.execSQL(CREATE_TABLE_SONG);
            database.execSQL(CREATE_TABLE_MIDI);
            database.execSQL(CREATE_TABLE_TIMESLICES);
            database.execSQL(CREATE_TABLE_SONG_PLAYLIST);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean saveSong(Song newSong) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues songValues = new ContentValues();
        songValues.put(FIELD_SONG_TITLE, newSong.getName());
        try {
            database.insertOrThrow(TBL_SONG, null, songValues);
            if (newSong instanceof TimeSlicesSong) {
                ContentValues timeSliceValues = new ContentValues();
                timeSliceValues.put(FIELD_TIME_SLICES_BLOB, ((TimeSlicesSong) newSong).encode());
                database.insert(TBL_TS_SONG, null, songValues);
            } else {
                ContentValues midiValues = new ContentValues();
                midiValues.put(FIELD_MIDI_PATH, ((MidiSong) newSong).getPath());
                database.insert(TBL_MIDI_SONG, null, songValues);
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
            playlistValues.put(FIELD_PLAYLIST_NAME, newPlaylist.getName());
            playlistValues.put(FIELD_SONG_TITLE, song.getName());
            playlistValues.put(FIELD_INDEX_SONG, newPlaylist.getSongIndex(song));
            try {
                database.insertOrThrow(TBL_SONG_PLAYLIST, null, playlistValues);
            } catch (SQLException e) {
                return false;
            }
        }
        return true;
    }


    @Override
    public List<Song> getSongs() {
        return getSongs(ALL_SONGS, NO_PLAYLISTS);
    }

    @Override
    public List<Song> getSongs(String songTitle, Playlist playlist) {
        List<Song> songsToReturn = new ArrayList<Song>();
        SQLiteDatabase database = this.getReadableDatabase();
        StringBuilder sbM = new StringBuilder();
        sbM.append("Select * from " + TBL_MIDI_SONG);
        if (playlist != null) {
            sbM.append(" natural join " + TBL_SONG_PLAYLIST);
        }
        sbM.append(" where 1=1");
        if (songTitle != null) {
            sbM.append(" AND " + FIELD_SONG_TITLE + " = '" + songTitle + "'");
        }
        if (playlist != null) {
            sbM.append(" AND " + FIELD_PLAYLIST_NAME + " = '" + playlist.getName() + "'");
        }
        sbM.append(";");

        String playlistName = (playlist != null) ? playlist.getName() : null;
        List<String> songsFinded = search(songTitle, playlistName);
        String songsToMatch = "";
//        if(songsFinded.size() > 0) {
//            if(songsFinded.size() != 1) {
//                int indexMatchedSongs = 0;
//                for (; indexMatchedSongs < songsFinded.size() - 1; indexMatchedSongs++)
//                    songsToMatch += FIELD_SONG_TITLE + " = " + songsFinded.get(indexMatchedSongs) + " OR ";
//                songsToMatch += FIELD_SONG_TITLE + " = " + songsFinded.get(++indexMatchedSongs) + ";";
//            }else{
//                songsToMatch += songsToMatch += FIELD_SONG_TITLE + " = " + songsFinded.get(0) + ";";
//            }
        String querySongs = sbM.toString();
        Cursor cursorSongs = database.rawQuery(querySongs, null);
        if (cursorSongs.moveToFirst()) {
            do {
                MidiSong newMidi = EntitiesBuilder.getMidiSong();
                newMidi.setName(cursorSongs.getString(cursorSongs.getColumnIndex(FIELD_SONG_TITLE)));
                newMidi.setPath(cursorSongs.getString(cursorSongs.getColumnIndex(FIELD_MIDI_PATH)));
                songsToReturn.add(newMidi);
            } while (cursorSongs.moveToNext());
        }

        sbM = new StringBuilder();
        sbM.append("Select * from " + TBL_TS_SONG);
        if (playlist != null) {
            sbM.append(" natural join " + TBL_SONG_PLAYLIST);
        }
        sbM.append(" where 1=1");
        if (songTitle != null) {
            sbM.append(" AND " + FIELD_SONG_TITLE + " = '" + songTitle + "'");
        }
        if (playlist != null) {
            sbM.append(" AND " + FIELD_PLAYLIST_NAME + " = '" + playlist.getName() + "'");
        }
        sbM.append(";");

        querySongs = sbM.toString();
        cursorSongs = database.rawQuery(querySongs, null);
        if (cursorSongs.moveToFirst()) {
            do {
                TimeSlicesSong newTimeSlices = EntitiesBuilder.getTimeSlicesSong();
                newTimeSlices.setName(cursorSongs.getString(cursorSongs.getColumnIndex(FIELD_SONG_TITLE)));
                newTimeSlices.decode(cursorSongs.getBlob(cursorSongs.getColumnIndex(FIELD_TIME_SLICES_BLOB)));
                songsToReturn.add(newTimeSlices);
            } while (cursorSongs.moveToNext());
        }
//        }
        return songsToReturn;
    }

    @Override
    public List<Playlist> getPlaylists(String playlistName) {
        List<Playlist> playlistsToReturn = new ArrayList<Playlist>();
        List<String> playlistsFinded = search(null, playlistName);
        for (int indexPlaylistFinded = 0; indexPlaylistFinded < playlistsFinded.size(); indexPlaylistFinded++) {
            Playlist newPlaylist = EntitiesBuilder.getPlaylist(playlistsFinded.get(indexPlaylistFinded));
            playlistsToReturn.add(newPlaylist);
        }
        return playlistsToReturn;
    }

    @Override
    public boolean deleteSong(Song songToDelete) {
        SQLiteDatabase database = this.getWritableDatabase();
        String tableType = (songToDelete instanceof MidiSong) ? TBL_MIDI_SONG : TBL_TS_SONG;
        String queryDelSongType = "DELETE FROM " + tableType + " WHERE " + FIELD_SONG_TITLE + " = " + songToDelete.getName() + ";";
        String queryDelSong = "DELETE FROM " + TBL_SONG + " WHERE " + FIELD_SONG_TITLE + " = " + songToDelete.getName() + ";";
        String queryDelPlaylistSong = "DELETE FROM " + TBL_SONG_PLAYLIST + " WHERE " + FIELD_SONG_TITLE + " = " + songToDelete.getName() + ";";
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
        String queryDelPlaylist = "DELETE FROM " + TBL_SONG_PLAYLIST + " WHERE " + FIELD_PLAYLIST_NAME + " = " + playlist.getName() + ";";
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
            String queryUpdate = "UPDATE " + TBL_SONG_PLAYLIST + " SET " + FIELD_SONG_TITLE + " = " + newSong.getName()
                    + " WHERE " + FIELD_SONG_TITLE + " = " + oldSong.getName() + ";";
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

    private List<String> search(String songName, String playlistName) {
        SQLiteDatabase database = this.getReadableDatabase();
        String querySongName = (songName == ALL_SONGS) ? " LIKE '%%';" : " = " + songName + ";";
        String queryMatchAll;
        if (playlistName == null) {
            queryMatchAll = "SELECT * FROM " + TBL_SONG + " WHERE " + FIELD_SONG_TITLE + querySongName;
        } else {
            queryMatchAll = "SELECT * FROM " + TBL_SONG_PLAYLIST + " WHERE " + FIELD_PLAYLIST_NAME + " = " + playlistName + " AND "
                    + FIELD_SONG_TITLE + querySongName;
        }
        Cursor cursorResults = database.rawQuery(queryMatchAll, null);
        List<String> results = new ArrayList<String>();
        if (cursorResults.moveToFirst()) {
            String field = (playlistName != null && songName == null) ? FIELD_PLAYLIST_NAME : FIELD_SONG_TITLE;
            do {
                results.add(cursorResults.getString(cursorResults.getColumnIndex(field)));
            } while (cursorResults.moveToNext());
        }
        return results;
    }
}
