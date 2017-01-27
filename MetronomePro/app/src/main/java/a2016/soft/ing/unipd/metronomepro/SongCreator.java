package a2016.soft.ing.unipd.metronomepro;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;


import a2016.soft.ing.unipd.metronomepro.adapters.TimeSlicesAdapter;
import a2016.soft.ing.unipd.metronomepro.adapters.listeners.OnTimeSliceSelectedListener;
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.OnStartDragListener;
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.inverted.HorizontalDragTouchHelperCallback;
import a2016.soft.ing.unipd.metronomepro.entities.EntitiesBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.ParcelableSong;
import a2016.soft.ing.unipd.metronomepro.entities.Song;
import a2016.soft.ing.unipd.metronomepro.entities.TimeSlice;

import static a2016.soft.ing.unipd.metronomepro.ActivityExtraNames.*;

public class SongCreator extends AppCompatActivity implements OnStartDragListener, OnTimeSliceSelectedListener {

    private RecyclerView rVTimeSlices;
    private RecyclerView.LayoutManager rVLayoutManager;
    private TimeSlicesAdapter timeSlicesAdapter;
    private ItemTouchHelper itemTouchHelper;
    private EditText bpmEditText;
    private EditText beatsEditText;
    private EditText songNameEditText;
    private ImageButton addEditTimeSliceButton;
    private View backgroundView;
    private boolean oneSlicePresent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_creator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rVTimeSlices = (RecyclerView) findViewById(R.id.recycler_view_time_slices);
        bpmEditText=(EditText)findViewById(R.id.editText_bpm);
        beatsEditText=(EditText)findViewById(R.id.editText_beats);
        addEditTimeSliceButton=(ImageButton)findViewById(R.id.button_add_or_save_timeslice);
        backgroundView=findViewById(R.id.background_relative_layout);
        songNameEditText=(EditText)findViewById(R.id.name_song_edit_text);
        //sarà a false
        rVTimeSlices.setHasFixedSize(false);
        rVLayoutManager =  new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rVTimeSlices.setLayoutManager(rVLayoutManager);
        Song songToEdit= createTestSong();
        oneSlicePresent = false;
        if(savedInstanceState!=null&&savedInstanceState.containsKey(SONG_TO_EDIT)){
            songToEdit=savedInstanceState.getParcelable(SONG_TO_EDIT);
        }else{
            Intent intent=getIntent();
            try {
                songToEdit = intent.getParcelableExtra(SONG_TO_EDIT);
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
        timeSlicesAdapter = new TimeSlicesAdapter(this, this,songToEdit);
        rVTimeSlices.setAdapter(timeSlicesAdapter);
        HorizontalDragTouchHelperCallback myItemTouchHelper = new HorizontalDragTouchHelperCallback(timeSlicesAdapter);
        itemTouchHelper = new ItemTouchHelper(myItemTouchHelper);
        itemTouchHelper.attachToRecyclerView(rVTimeSlices);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabOk);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkAllFields() && oneSlicePresent) {
                    Snackbar.make(view, getString(R.string.saved_string), Snackbar.LENGTH_LONG).show();
                    Intent returnIntent = new Intent();
                    ParcelableSong ps = (ParcelableSong) timeSlicesAdapter.getSongToEdit();
                    ps.setName(songNameEditText.getText().toString());
                    returnIntent.putExtra(SONG_TO_EDIT, ps);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            }
        });
        //register listener for selected timeslice
        timeSlicesAdapter.addOnTimeSliceSelectedListener(this);
        backgroundView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeSlicesAdapter.setSelectedItem(null);
            }
        });
        addEditTimeSliceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkAllFields()) {
                    onAddOrEditClicked(v);
                    oneSlicePresent=true;
                }
            }
        });

    }

    public boolean checkAllFields(){
        boolean checked = true;
        String bpm = bpmEditText.getText().toString();
        String beat = beatsEditText.getText().toString();
        String songName = songNameEditText.getText().toString();
        if(TextUtils.isEmpty(bpm)) {
            bpmEditText.setError("BPM cannot be empty");
            checked=false;
        }
        if(TextUtils.isEmpty(beat)) {
            beatsEditText.setError("Beat cannot be empty");
            checked=false;

        }
        if(TextUtils.isEmpty(songName)) {
            songNameEditText.setError("Please insert a name!");
            checked=false;

        }
        return checked;

    }
    /**
     * Used when a timeslice be edited or clicked
     * @param v the v called
     */
    private void onAddOrEditClicked(View v) {
        if(timeSlicesAdapter.getTimeSliceSelected()!=null){
            //edit
        }else{
            TimeSlice ts= new TimeSlice();
            ts.setBpm(Integer.parseInt(bpmEditText.getText().toString()));
            ts.setDurationInBeats(Integer.parseInt(beatsEditText.getText().toString()));
            timeSlicesAdapter.addTimeSlice(timeSlicesAdapter.getItemCount(),ts);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SONG_TO_EDIT,(Parcelable) timeSlicesAdapter.getSongToEdit());

    }

    /**
     * Called when an item start to get drag
     * @param viewHolder The holder of the view to drag.
     */
    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    /**
     * Called when selected item change
     * @param timeSlice the new timeSlice
     * @param position the position
     */
    @Override
    public void onTimeSliceSelected(TimeSlice timeSlice, int position) {

    }

    /**
     * This method create a test song just used to test usability
     * @return a demo song
     */
    private Song createTestSong(){
        TimeSlice t1, t2, t3;
        Song s =EntitiesBuilder.getSong("pippo");
        t1 = new TimeSlice();
        t2 = new TimeSlice();
        t3 = new TimeSlice();
        t1.setBpm(60);
        t1.setDurationInBeats(20);
        t2.setBpm(80);
        t2.setDurationInBeats(40);
        t3.setBpm(100);
        t3.setDurationInBeats(50);
        s.add(t1);
        s.add(t2);
        s.add(t3);
        t1 = new TimeSlice();
        t2 = new TimeSlice();
        t3 = new TimeSlice();
        t1.setBpm(110);
        t1.setDurationInBeats(60);
        t2.setBpm(140);
        t2.setDurationInBeats(100);
        t3.setBpm(100);
        t3.setDurationInBeats(60);
        s.add(t1);
        s.add(t2);
        s.add(t3);
        t1 = new TimeSlice();
        t2 = new TimeSlice();
        t3 = new TimeSlice();
        t1.setBpm(60);
        t1.setDurationInBeats(20);
        t2.setBpm(248);
        t2.setDurationInBeats(100);
        t3.setBpm(280);
        t3.setDurationInBeats(20);
        s.add(t1);
        s.add(t2);
        s.add(t3);
        t1 = new TimeSlice();
        t2 = new TimeSlice();
        t3 = new TimeSlice();
        t1.setBpm(60);
        t1.setDurationInBeats(20);
        t2.setBpm(80);
        t2.setDurationInBeats(20);
        t3.setBpm(300);
        t3.setDurationInBeats(20);
        s.add(t1);
        s.add(t2);
        s.add(t3);
        return s;
    }



}
