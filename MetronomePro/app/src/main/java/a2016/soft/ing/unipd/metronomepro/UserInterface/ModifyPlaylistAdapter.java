package a2016.soft.ing.unipd.metronomepro.UserInterface;

/**
 * Created by Francesco on 11/12/2016.
 */

        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import java.util.List;

public class ModifyPlaylistAdapter {

    public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

        private List<ModifyPlaylist> modifyPlaylistList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView title;

            public MyViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.title);
            }
        }


        public MoviesAdapter(List<ModifyPlaylist> modifyPlaylistList) {
            this.modifyPlaylistList = modifyPlaylistList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_list_row, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            ModifyPlaylist song = modifyPlaylistList.get(position);
            holder.title.setText(song.getTitle());

        }

        @Override
        public int getItemCount() {
            return modifyPlaylistList.size();
        }
    }

}
