package a2016.soft.ing.unipd.metronomepro;

import android.content.Intent;
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
import android.view.View;

import a2016.soft.ing.unipd.metronomepro.adapters.TimeSlicesAdapter;
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.OnStartDragListener;
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.inverted.HorizontalDragTouchHelperCallback;
import a2016.soft.ing.unipd.metronomepro.entities.EntitiesBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.ParcelableSong;
import a2016.soft.ing.unipd.metronomepro.entities.Song;
import a2016.soft.ing.unipd.metronomepro.entities.TimeSlice;

import static a2016.soft.ing.unipd.metronomepro.ActivityExtraNames.*;

public class SongCreator extends AppCompatActivity implements OnStartDragListener {

    private RecyclerView rVTimeSlices;
    private RecyclerView.LayoutManager rVLayoutManager;
    private TimeSlicesAdapter timeSlicesAdapter;
    private ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_creator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rVTimeSlices = (RecyclerView) findViewById(R.id.recycler_view_time_slices);
        //sarà a false
        rVTimeSlices.setHasFixedSize(false);
        rVLayoutManager =  new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rVTimeSlices.setLayoutManager(rVLayoutManager);
        Song songToEdit= createTestSong();
        if(savedInstanceState!=null&&savedInstanceState.containsKey(SONG_TO_EDIT)){
            songToEdit=savedInstanceState.getParcelable(SONG_TO_EDIT);
        }else{
            Intent intent=getIntent();
            songToEdit=intent.getParcelableExtra(SONG_TO_EDIT);
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
                Snackbar.make(view,getString(R.string.saved_string), Snackbar.LENGTH_LONG).show();
                Intent returnIntent = new Intent();
                ParcelableSong ps=(ParcelableSong) timeSlicesAdapter.getSongToEdit();
                returnIntent.putExtra(SONG, ps);
                setResult(RESULT_OK,returnIntent);
                finish();
            }
        });

    }

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


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SONG_TO_EDIT,(Parcelable) timeSlicesAdapter.getSongToEdit());

    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }
}
