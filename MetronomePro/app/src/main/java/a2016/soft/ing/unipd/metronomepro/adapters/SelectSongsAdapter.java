package a2016.soft.ing.unipd.metronomepro.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import a2016.soft.ing.unipd.metronomepro.R;
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.ItemTouchHelperAdapter;
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.ItemTouchHelperViewHolder;
import a2016.soft.ing.unipd.metronomepro.entities.PlayableSong;
import a2016.soft.ing.unipd.metronomepro.entities.Playlist;
import a2016.soft.ing.unipd.metronomepro.entities.Song;

/**
 * Created by Omar on 12/12/2016.
 */

public class SelectSongsAdapter extends RecyclerView.Adapter<SelectSongsAdapter.ViewHolder> implements ItemTouchHelperAdapter{

    private ArrayList<PlayableSong> arraySongs;
    private Context context;
    private int selectedSongs;
    private int maxSelectable;

    public SelectSongsAdapter(Context context, Playlist p, int selectedSongs, int maxSelectable){

        this.context = context;
        arraySongs = new ArrayList<>(p.size());
        int i = 0;
        for (Song s :
                p) {
            arraySongs.add(new PlayableSong(s, i++, PlayableSong.STATE_READYTOPLAY));
        }

        this.selectedSongs = selectedSongs;
        this.maxSelectable = maxSelectable;

    }

    public SelectSongsAdapter(ArrayList<PlayableSong> savedArray, int selectedSongs, int maxSelectable) {

        arraySongs = new ArrayList<>(savedArray.size());
        int i = 0;
        for(PlayableSong s : savedArray){

            arraySongs.add(new PlayableSong(s.getInnerSong(), i++, s.getSongState()));
        }

        this.selectedSongs = selectedSongs;
        this.maxSelectable = maxSelectable;

    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {

        notifyItemMoved(fromPosition, toPosition);

    }

    private void onSongPositionChange(int from, int to) {

        PlayableSong ps = arraySongs.get(from);
        arraySongs.remove(from);
        arraySongs.add(to, ps);
        notifyItemRangeChanged(Math.min(from,to),Math.abs(from-to)+1);
    }

    @Override
    public void onItemSwiped(int position) {

    }

    @Override
    public void onViewRecycled(ViewHolder holder) {

        //Unregister from listeners
        holder.itemView.setOnTouchListener(null);
        super.onViewRecycled(holder);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.next_song_item_layout, parent, false);
        ViewHolder vh = new ViewHolder(v, (TextView)v.findViewById(R.id.song_title_text_view));

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        PlayableSong s = arraySongs.get(position);

        switch (s.getSongState()){
            case PlayableSong.STATE_TOPLAY: holder.nameSong.setTextColor(Color.BLACK);
                break;

            case PlayableSong.STATE_READYTOPLAY: holder.nameSong.setTextColor(Color.BLUE);
                break;

            case PlayableSong.STATE_PLAYED: holder.nameSong.setTextColor(Color.RED);
                break;
        }

        holder.nameSong.setText(s.getInnerSong().getName());

        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                onSongTouch(position);
                return false;
            }
        });
    }

    private void onSongTouch(int position) {

        if(position < selectedSongs) {

            if(selectedSongs!=arraySongs.size()) {
                PlayableSong toMove = arraySongs.get(position);
                int i = selectedSongs;
                PlayableSong current = arraySongs.get(i);

                while (i < arraySongs.size() &&
                        current.getSongState() == PlayableSong.STATE_READYTOPLAY &&
                        current.getPlaylistPosition() < toMove.getPlaylistPosition()) {

                    i++;
                    if (i < arraySongs.size()) {
                        current = arraySongs.get(i);
                    }
                }

                toMove.setSongState(PlayableSong.STATE_READYTOPLAY);
                selectedSongs--;
                onSongPositionChange(position, i - 1);
            }
            else
            {
                PlayableSong toMove = arraySongs.get(position);
                toMove.setSongState(PlayableSong.STATE_READYTOPLAY);
                selectedSongs--;
                onSongPositionChange(position, arraySongs.size() - 1);
            }
        }
        else
        {
            arraySongs.get(position).setSongState(PlayableSong.STATE_TOPLAY);
            onSongPositionChange(position, selectedSongs++);
        }
    }

    @Override
    public int getItemCount() {
        return arraySongs.size();
    }

    public ArrayList<PlayableSong> getArraySongs() {
        return arraySongs;
    }

    public int getSelectedSongs() {

        return selectedSongs;
    }

    /**
     * Returns selected songs
     * @return toReturn which contains selected songs
     */

    public Song[] getSongs() {

        ArrayList<Song> app = new ArrayList<Song>();
        for(int i = 0; i < selectedSongs; i++) {
            app.add(arraySongs.get(i).getInnerSong());
        }

        Song[] toReturn = new Song[selectedSongs];
        toReturn = app.toArray(toReturn);

        return toReturn;
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        TextView nameSong;

        public ViewHolder(View itemView, TextView nameSong) {
            super(itemView);
            this.nameSong = nameSong;
        }


        @Override
        public void onItemSelected() {

        }

        @Override
        public void onItemClear() {

        }
    }
}