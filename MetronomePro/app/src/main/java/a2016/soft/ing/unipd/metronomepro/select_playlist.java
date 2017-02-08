package a2016.soft.ing.unipd.metronomepro;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import a2016.soft.ing.unipd.metronomepro.adapters.ModifyPlaylistAdapter;
import a2016.soft.ing.unipd.metronomepro.adapters.SelectPlaylistAdapter;
import a2016.soft.ing.unipd.metronomepro.entities.EntitiesBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.Playlist;

import static a2016.soft.ing.unipd.metronomepro.ActivityExtraNames.PLAYLIST;

/**
 * Created by Alberto on 30/01/17.
 */

public class select_playlist extends AppCompatActivity implements AdapterView.OnItemSelectedListener{


    private ModifyPlaylistAdapter modifyPlaylistAdapter;
    private String chosenPl;
    private static final int START_EDIT_NEW_SONG=1;
    private SelectPlaylistAdapter playListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_playlist);

        Spinner plSpinner = (Spinner) findViewById(R.id.playlistspinner);
        plSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> playlistAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,createTestPlaylist());
        playlistAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        plSpinner.setAdapter(playlistAdapter);

        Button newPlButton = (Button) findViewById(R.id.newPlaylist);
        newPlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPlaylist();
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        chosenPl = (String) parent.getItemAtPosition(position);
        Toast.makeText(this, "Playlist selected: " + chosenPl, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //TODO autogenerated method stub
    }

    private void createPlaylist() {
        Playlist playlistToEdit = modifyPlaylistAdapter.getPlaylistToModify();
        Intent intent = new Intent(this,SelectSongForPlaylist.class);
        intent.putParcelableArrayListExtra(PLAYLIST,modifyPlaylistAdapter.getAllSongs());
        startActivityForResult(intent, START_EDIT_NEW_SONG);
        //Intent intent = new Intent(this,SelectSongForPlaylist.class);
        //startActivity(intent);
    }

    public void customDialog(){
        final Dialog dialog = new Dialog(select_playlist.this);
        dialog.setContentView(R.layout.dialog_submit);
        final EditText edit_name = (EditText) dialog.findViewById(R.id.edit_name_p);
        Button cancel = (Button) dialog.findViewById(R.id.but_cancel_p);
        Button submit = (Button) dialog.findViewById(R.id.but_submit);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Playlist p = EntitiesBuilder.getPlaylist(edit_name.getText().toString());
                playListAdapter.addPlaylist(p);
                dialog.cancel();
            }
        });
        dialog.show();

    }

    //metodo di test per vedere se funziona
    private ArrayList<String> createTestPlaylist(){
        Playlist p1 = EntitiesBuilder.getPlaylist("prova di playlist 1");
        Playlist p2 = EntitiesBuilder.getPlaylist("prova di playlist 2");
        Playlist p3 = EntitiesBuilder.getPlaylist("prova di playlist 3");
        Playlist p4 = EntitiesBuilder.getPlaylist("prova di playlist 4");
        Playlist p5 = EntitiesBuilder.getPlaylist("prova di playlist 5");

        ArrayList<String> arrayList = new ArrayList<>();
        //HARDCODED FOR TEST
        arrayList.add(0,p1.getName());
        arrayList.add(1,p2.getName());
        arrayList.add(2,p3.getName());
        arrayList.add(3,p4.getName());
        arrayList.add(4,p5.getName());

        return arrayList;
    }
}