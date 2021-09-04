package com.trackster;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.roomdb.Track;
import com.roomdb.TracksterRoomDb;


import org.json.JSONException;
import org.json.JSONObject;



public class Trackinfo {
    private RequestQueue queue;
    private String lyrics;
    private Context context;
    private Track track;
    private final String MuisxmatchApitoken= "48c5002edae6df6634e104f8b4989333";

    public Trackinfo(Context context, Track track){
        this.track=track;
        this.context=context;
        lyricsGrapper();
    }

    private void lyricsGrapper(){
        String url="https://api.musixmatch.com/ws/1.1/matcher.lyrics.get?format=json&callback=callback&q_track="
                +track.getName()+
                "&q_artist="+
                track.getArtistName()+
                "&apikey="+MuisxmatchApitoken;
        queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    lyrics = response.getJSONObject("message").getJSONObject("body").getJSONObject("lyrics").get("lyrics_body").toString();
                    TracksterRoomDb.getInstance(context).trackDao().updateLyrics(lyrics,track.getID());
                    if(lyrics.trim().isEmpty())
                        TracksterRoomDb.getInstance(context).trackDao().updateLyrics("Not available", track.getID());


                } catch (JSONException e) {
                    e.printStackTrace();
                    TracksterRoomDb.getInstance(context).trackDao().updateLyrics("Not available", track.getID());

                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);

    }





}
