package a2016.soft.ing.unipd.metronomepro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Questa classe deve permettere all'utente di associarsi al server o cercarne uno.
 * Una volta fatto avvia l'activity di Omar nel caso sia server, altrimenti la clientactivity
 */
public class ConnectionActivity extends AppCompatActivity {
    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_activity);
    }
}
