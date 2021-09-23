package com.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.roomdb.TracksterRoomDb;
import com.trackster.R;

public class RenamePlaylistDialog extends Dialog {

    private EditText vPlaylistName;
    private Button vOkButton, vCancelButton;



    public RenamePlaylistDialog(@NonNull Context context,String mCurrentPlaylistName) {
        super(context);
        View view = LayoutInflater.from(context).
                inflate(R.layout.add_playlist,
                        (RelativeLayout) findViewById(R.id.playlist_name_container));

        vPlaylistName = view.findViewById(R.id.added_playlist_name);
        vOkButton = view.findViewById(R.id.AddPlaylist);
        vCancelButton = view.findViewById(R.id.cancelAddingPlaylist);

        vOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TracksterRoomDb.getInstance(getContext()).playlistDao().isPlaylistExist(vPlaylistName.getText().toString()) && !vPlaylistName.getText().toString().trim().isEmpty())
                {
                    TracksterRoomDb.getInstance(getContext()).playlistDao().rename(mCurrentPlaylistName,vPlaylistName.getText().toString());
                    Toast.makeText(getContext(), "Playlist Renamed", Toast.LENGTH_SHORT).show();

                }
                else if (vPlaylistName.getText().toString().trim().isEmpty() || vPlaylistName.getText().toString().length() == 0)
                    Toast.makeText(getContext(), "Please Enter a Name", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getContext(), "This playlist already exist", Toast.LENGTH_SHORT).show();

                dismiss();
            }
        });
        vCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        setContentView(view);

    }


}