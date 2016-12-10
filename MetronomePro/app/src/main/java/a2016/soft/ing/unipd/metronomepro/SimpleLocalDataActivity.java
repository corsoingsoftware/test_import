package a2016.soft.ing.unipd.metronomepro;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by giuli on 10/12/2016.
 */

public class SimpleLocalDataActivity extends FragmentActivity {
    private ListView mListView;
    private ArrayAdapter<LocalDataModel> mAdapter;
    private List<LocalDataModel> mModel;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adapter);
        mListView =(ListView)findViewById(R.id.adapter_layout);
        mAdapter = new ArrayAdapter<LocalDataModel>(this,android.R.layout.simple_list_item_1,mModel);
        mListView.setAdapter(mAdapter);
    }
}
