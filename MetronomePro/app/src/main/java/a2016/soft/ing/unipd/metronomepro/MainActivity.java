package a2016.soft.ing.unipd.metronomepro;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int MIN, MAX, INITIAL_VALUE;
    private static SoundThread clackThread;

    static {
        MIN = 30;
        MAX = 300;
        INITIAL_VALUE = 100;
    }

    private Button fasterButton, slowerButton, fastForwardButton, backForwardButton, playButton;
    private TextView bPMTextView;
    private int actualBPM;

    public int getActualBPM() {
        return actualBPM;
    }

    public void setActualBPM(int actualBPM) {
        this.actualBPM = actualBPM;
        this.bPMTextView.setText(Integer.toString(actualBPM));
        if (clackThread != null)
            clackThread.setStepMillis(SoundThread.millisIntervalFromBPM(actualBPM));
        //Ricalcola ora il delay tra un clack e l'altro
        //controlla se il thread è in esecuzione
        //lo mette in pausa e cambia i millis e riparte
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fasterButton = (Button) findViewById(R.id.button_faster);
        slowerButton = (Button) findViewById(R.id.button_slower);
        fastForwardButton = (Button) findViewById(R.id.button_fast_forward);
        backForwardButton = (Button) findViewById(R.id.button_back_forward);
        playButton = (Button) findViewById(R.id.start_button);
        bPMTextView = (TextView) findViewById(R.id.number_of_BPM);
        fasterButton.setOnClickListener(this);
        slowerButton.setOnClickListener(this);
        fastForwardButton.setOnClickListener(this);
        backForwardButton.setOnClickListener(this);
        if(clackThread!=null) {
            setActualBPM(INITIAL_VALUE);
        }
        final Context c = this;
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clackThread != null && clackThread.isRun()) {
                    clackThread.setRun(false);
                    clackThread = null;
                } else {
                    //clackThread.join();
                    clackThread = new SoundThread(c, actualBPM);
                    clackThread.start();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v instanceof Button) {
            Button b = (Button) v;
            int toAdd = 0;
            switch (b.getId()) {

                case R.id.button_faster:
                    toAdd = 1;
                    break;

                case R.id.button_slower:
                    toAdd = -1;
                    break;

                case R.id.button_fast_forward:
                    toAdd = 10;
                    break;

                case R.id.button_back_forward:
                    toAdd = -10;
                    break;

            }
            setActualBPM(getActualBPM() + toAdd);
        }
    }
}
