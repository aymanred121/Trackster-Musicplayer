package com.Dialogs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.roomdb.Playlist;
import com.roomdb.TracksterRoomDb;
import com.trackster.R;

import io.reactivex.annotations.Nullable;

public class AddPlaylistDialog extends AppCompatDialogFragment {

    private EditText mPlaylistName;

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_playlist, null);
        mPlaylistName=view.findViewById(R.id.added_playlist_name);

        builder.setView(view)
                .setTitle("Playlist name")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!TracksterRoomDb.getInstance(getContext()).playlistDao().isPlaylistExist(mPlaylistName.getText().toString()))
                            TracksterRoomDb.getInstance(getContext()).playlistDao().insert(new Playlist(mPlaylistName.getText().toString()));
                        else
                            Toast.makeText(getContext(), "This playlist already exist", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        mPlaylistName = view.findViewById(R.id.added_playlist_name);


        return builder.create();
    }
}