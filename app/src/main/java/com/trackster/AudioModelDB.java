package com.trackster;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class AudioModelDB {
    private Context context;
    private Set<String[]> Tracks;

    public AudioModelDB(Context context){
        this.context=context;
        Tracks= new HashSet<>();
    }
    public Bitmap getAlbumImage(String path) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(path);
        byte[] data = mmr.getEmbeddedPicture();
        if (data != null) return BitmapFactory.decodeByteArray(data, 0, data.length);
        return null;
    }
    public void scanDeviceForMp3Files(){
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ARTIST_ID,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DURATION
        };

        Cursor cursor = null;
        try {
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            cursor = context.getApplicationContext().getContentResolver().query(uri, projection, selection, null, null);
            if( cursor != null){
                cursor.moveToFirst();

                while( !cursor.isAfterLast()){
                    String title = cursor.getString(0);
                    String artist = cursor.getString(1);
                    String path = cursor.getString(3);
                    String album  = cursor.getString(4);
                    String albumID = cursor.getString(5);
                    String ID = (cursor.getString(6));
                    String duration = (cursor.getString(7));
                   // saveToInternalStorage(path,ID);
                    String[]trackData={path,title,"",artist,album,ID,duration};
                    Tracks.add(trackData);
                    cursor.moveToNext();

                }

            }

        } catch (Exception e) {
            Log.e("TAG", e.toString());
        }finally{
            if( cursor != null){
                cursor.close();
            }
        }

    }
    public void saveToInternalStorage(String filePath, String ID){
        if(new File("/data/user/0/com.trackster/app_imageDir/"+ID+".jpg").exists())
            return;
        Bitmap bitmapImage=getAlbumImage(filePath);
        if(bitmapImage==null)
            return;
        ContextWrapper cw = new ContextWrapper(context);
        File trackImg = cw.getDir("imageDir", Context.MODE_PRIVATE);
        trackImg.mkdir();
        File path = new File(trackImg,ID+".jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 10, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ;
    }

    public Set<String[]> getTracks() {
        return Tracks;
    }

}
