package com.roomdb;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.trackster.AudioModelDB;
import com.trackster.Trackinfo;

import java.util.Set;

@Database(entities = {Track.class,Playlist.class,Contains.class}, version = 1)
public abstract class TracksterRoomDb extends RoomDatabase {
    private static TracksterRoomDb instence;

    public  abstract TrackDao trackDao();
    public  abstract PlaylistDao playlistDao();
    public  abstract ContainsDao containsDao();
    private static Context context;


    //Singleton
    public static synchronized TracksterRoomDb getInstance(Context context){

        if(instence==null){
            TracksterRoomDb.context =context;
            instence= Room.databaseBuilder(context.getApplicationContext(),
                    TracksterRoomDb.class, "Trackster-db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .addCallback(roomCallback)
                    .build();
        }
        return instence;
    }
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new fillDataAsyncTask(instence).execute();
            new fillDataAsyncTask(instence).execute();
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new fillDataAsyncTask(instence).execute();
        }
    };



    private static class fillDataAsyncTask extends AsyncTask<Void,Void,Void>{
        private TrackDao mtrackDao;
        private PlaylistDao mplaylistDao;
        private ContainsDao mcontainsDao;
        private AudioModelDB audioModelDB;

        public fillDataAsyncTask(TracksterRoomDb db) {
            mtrackDao = db.trackDao();
            mplaylistDao = db.playlistDao();
            mcontainsDao = db.containsDao();
            audioModelDB=new AudioModelDB(context);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //TODO find a way to execute the image loader after scanning the files
            audioModelDB.scanDeviceForMp3Files();
            Set tracks=audioModelDB.getTracks();
            mplaylistDao.insert(new Playlist("favourites"));
            for(Object track:tracks){
              mtrackDao.insert(new Track(((String[])track)[0],
                        ((String[])track)[1],
                        (((String[])track)[2]),
                        (((String[])track)[3]),
                        ((String[])track)[4],
                        Integer.parseInt(((String[])track)[5]),
                        Integer.parseInt(((String[])track)[6]),
                      ((String[])track)[7]
                      ));
            }
            return null;
        }
    }


}
