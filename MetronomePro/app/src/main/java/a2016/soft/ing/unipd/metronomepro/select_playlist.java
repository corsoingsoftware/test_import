package a2016.soft.ing.unipd.metronomepro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

import a2016.soft.ing.unipd.metronomepro.entities.EntitiesBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.Playlist;

public class select_playlist extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_playlist);

        Spinner plSpinner = (Spinner) findViewById(R.id.playlistspinner);
        ArrayAdapter<String> playlistAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,createTestPlaylist());
        playlistAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        plSpinner.setAdapter(playlistAdapter);
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
