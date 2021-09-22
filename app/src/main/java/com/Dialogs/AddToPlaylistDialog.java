package com.Dialogs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Adapters.PlaylistAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.roomdb.Contains;
import com.roomdb.Playlist;
import com.roomdb.TracksterRoomDb;
import com.roomdb.roomViewModel;
import com.trackster.R;

import java.util.List;

import io.reactivex.annotations.Nullable;

import static com.trackster.UI.mPlayingNow;

public class AddToPlaylistDialog extends BottomSheetDialog {
    private final roomViewModel viewModel;
    // for Playlists
    private RecyclerView mPlaylistsRecyclerView;
    private PlaylistAdapter mPlaylistAdapter;
    private RecyclerView.LayoutManager mPlaylistLayoutManager;
    private List<Playlist> playlistsList;
    public AddToPlaylistDialog(@NonNull Context context, int theme) {
        super(context, theme);
        View bottomSheetView = LayoutInflater.from(context).
                inflate(R.layout.add_to_playlist_dialog,
                        (LinearLayout) findViewById(R.id.playlists_container));




        mPlaylistsRecyclerView = bottomSheetView.findViewById(R.id.playlists_layout_recyclerview);
        mPlaylistsRecyclerView.setHasFixedSize(true);
        mPlaylistLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        mPlaylistAdapter = new PlaylistAdapter(false);
        mPlaylistsRecyclerView.setLayoutManager(mPlaylistLayoutManager);
        mPlaylistsRecyclerView.setAdapter(mPlaylistAdapter);
        viewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(roomViewModel.class);
        viewModel.getAllPlaylists().observe((LifecycleOwner) context, new Observer<List<Playlist>>() {
            @Override
            public void onChanged(@Nullable List<Playlist> playlists) {
                mPlaylistAdapter.setPlaylistsList(playlists);
                playlistsList=playlists;
            }
        });

        mPlaylistAdapter.setOnItemClickListener(new PlaylistAdapter.OnItemClickListener() {
            @Override
            public void onPlaylistClick(int position) {
                if(!TracksterRoomDb.getInstance(context).containsDao().isExist(mPlayingNow.getID(),playlistsList.get(position).getName()))
                    TracksterRoomDb.getInstance(context).containsDao().insert(new Contains(playlistsList.get(position).getName(),mPlayingNow.getID()));
                else
                    Toast.makeText(context, "Already in this playlist", Toast.LENGTH_SHORT).show();
                dismiss();
            }

            @Override
            public void onMenuClick(int position, View view) {

            }
        });

        setContentView(bottomSheetView);
    }
}
