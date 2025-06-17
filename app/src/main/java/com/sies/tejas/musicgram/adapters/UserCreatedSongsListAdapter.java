package com.sies.tejas.musicgram.adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sies.tejas.musicgram.ApplicationClass;
import com.sies.tejas.musicgram.R;
import com.sies.tejas.musicgram.activities.MusicOverviewActivity;
import com.sies.tejas.musicgram.databinding.ActivityListSongItemBinding;
import com.sies.tejas.musicgram.records.sharedpref.SavedLibraries;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserCreatedSongsListAdapter extends RecyclerView.Adapter<UserCreatedSongsListAdapter.ViewHolder> {

    private final List<SavedLibraries.Library.Songs> data;

    public UserCreatedSongsListAdapter(List<SavedLibraries.Library.Songs> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View _v = View.inflate(parent.getContext(), R.layout.activity_list_song_item,null);
        _v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(_v);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.title.setText(data.get(position).title());
        holder.binding.artist.setText(data.get(position).description());
        if(!data.get(position).image().isBlank())
            Picasso.get().load(Uri.parse(data.get(position).image())).into(holder.binding.coverImage);

        holder.itemView.setOnClickListener(view -> {
            if(ApplicationClass.trackQueue != null)
                if(ApplicationClass.trackQueue.contains(data.get(position).id()))
                    ApplicationClass.track_position = holder.getBindingAdapterPosition();
            holder.itemView.getContext().startActivity(new Intent(view.getContext(), MusicOverviewActivity.class).putExtra("id", data.get(position).id()));
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ActivityListSongItemBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ActivityListSongItemBinding.bind(itemView);
        }
    }
}
