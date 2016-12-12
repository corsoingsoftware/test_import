package a2016.soft.ing.unipd.metronomepro;

import android.os.Bundle;
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
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.DragTouchHelperCallback;
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.OnStartDragListener;

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
        rVTimeSlices.setHasFixedSize(true);
        rVLayoutManager =  new LinearLayoutManager(this);
        rVTimeSlices.setLayoutManager(rVLayoutManager);
        timeSlicesAdapter = new TimeSlicesAdapter(this, this);
        rVTimeSlices.setAdapter(timeSlicesAdapter);
        DragTouchHelperCallback myItemTouchHelper = new DragTouchHelperCallback(timeSlicesAdapter);
        itemTouchHelper = new ItemTouchHelper(myItemTouchHelper);
        itemTouchHelper.attachToRecyclerView(rVTimeSlices);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }
}
