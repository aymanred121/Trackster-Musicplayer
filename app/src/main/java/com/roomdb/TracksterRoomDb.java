package com.roomdb;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.trackster.AudioModelDB;

import java.util.Set;

@Database(entities = {Track.class,Playlist.class,Contains.class}, version = 1,exportSchema = false)
public abstract class TracksterRoomDb extends RoomDatabase {
    private static TracksterRoomDb instence;

    public  abstract TrackDao trackDao();
    public  abstract PlaylistDao playlistDao();
    public  abstract ContainsDao containsDao();
    private static AudioModelDB audioModelDB;
    private static Application application;
    private static TrackDao trackDao;
    private static PlaylistDao playlistDao;
    private static ContainsDao containsDao;




    //Singleton
    public static synchronized TracksterRoomDb getInstance(Context context){

        if(instence==null){
            TracksterRoomDb.application = (Application) context;
            audioModelDB = new AudioModelDB(context);
            instence= Room.databaseBuilder(context.getApplicationContext(),
                    TracksterRoomDb.class, "Trackster-db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .addCallback(roomCallback)
                    .build();
        }

        trackDao = instence.trackDao();
        playlistDao = instence.playlistDao();
        containsDao = instence.containsDao();
        return instence;
    }
    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
           // new fillDataAsyncTask(instence).execute();
                fillDataAsyncTask();


        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
         //   new fillDataAsyncTask(instence).execute();
            fillDataAsyncTask();
        }
    };


private static void fillDataAsyncTask(){
    new Thread(() -> {
        audioModelDB.scanDeviceForMp3Files();
        Set tracks=audioModelDB.getTracks();
        playlistDao.insert(new Playlist("favourites"));
        try{
            for(Object track:tracks){
                trackDao.insert(new Track(((String[])track)[0],
                        ((String[])track)[1],
                        (((String[])track)[2]),
                        (((String[])track)[3]),
                        ((String[])track)[4],
                        Integer.parseInt(((String[])track)[5]),
                        Integer.parseInt(((String[])track)[6])
                ));
            }
            for(Object track:tracks){
                audioModelDB.saveToInternalStorage((((String[])track)[0]),(((String[])track)[5]));
            }
          }catch (Exception e){
        Log.i("filldata",e.toString());
    }
    }).start();

}
//    private static class fillDataAsyncTask extends AsyncTask<Void,Void,Void>{
//        private final TrackDao mtrackDao;
//        private final PlaylistDao mplaylistDao;
//        private final ContainsDao mcontainsDao;
//        private final AudioModelDB audioModelDB;
//
//        public fillDataAsyncTask(TracksterRoomDb db) {
//            mtrackDao = db.trackDao();
//            mplaylistDao = db.playlistDao();
//            mcontainsDao = db.containsDao();
//            audioModelDB=new AudioModelDB(application.getApplicationContext());
//        }
//
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            audioModelDB.scanDeviceForMp3Files();
//            Set tracks=audioModelDB.getTracks();
//            mplaylistDao.insert(new Playlist("favourites"));
//            for(Object track:tracks){
//              mtrackDao.insert(new Track(((String[])track)[0],
//                        ((String[])track)[1],
//                        (((String[])track)[2]),
//                        (((String[])track)[3]),
//                        ((String[])track)[4],
//                        Integer.parseInt(((String[])track)[5]),
//                        Integer.parseInt(((String[])track)[6])
//                      ));
//            }
//            for(Object track:tracks){
//                audioModelDB.saveToInternalStorage((((String[])track)[0]),(((String[])track)[5]));
//            }
//
//                return null;
//        }
//    }


}
