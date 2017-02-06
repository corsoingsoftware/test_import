package a2016.soft.ing.unipd.metronomepro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Button soloPlayButton = (Button) findViewById(R.id.solo_button);
        Button syncPlayButton = (Button) findViewById(R.id.sync_button);
        Button importSongButton = (Button) findViewById(R.id.import_button);
        Button playlistButton = (Button) findViewById(R.id.playlist_button);
        Button settingsButton = (Button) findViewById(R.id.settings_button);

        soloPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MetronomeActivity.class);
                startActivity(intent);
            }
        });

        syncPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(getBaseContext(), MetronomeActivity.class);
                startActivity(intent);*/
            }
        });

        importSongButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ActivityImportMidi.class);
                startActivity(intent);
            }
        });

        playlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), PlaylistView.class);
                startActivity(intent);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(getBaseContext(), MetronomeActivity.class);
                startActivity(intent);*/
            }
        });
    }
}
