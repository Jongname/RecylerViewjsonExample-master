package com.example.kccistc.recylerviewjsonexample;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ExampleAdapter.OnItemClickListener {
    public static final String EXTRA_URL = "imageUrl";
    public static final String EXTRA_CREATOR = "creatorName";
    public static final String EXTRA_LIKES = "likeCount";

    private RecyclerView mRecyclerView;
    private ExampleAdapter mExampleAdapter;
    private ArrayList<ExampleItem> mExampleList;
    private RequestQueue mRequestQueue;
//    private TabLayout tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        mExampleList = new ArrayList<>();

        mRequestQueue = Volley.newRequestQueue(this);
        parseJSON();


    }

    private void parseJSON() {
        String url = "http://openapi.seoul.go.kr:8088/485a594245736c6f3432625a64736b/json/SearchConcertDetailService/1/100/";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject search = response.getJSONObject("SearchConcertDetailService");
                            JSONArray row = search.getJSONArray("row");

//                            JSONObject response1 = response.getJSONObject("response");
//                            JSONObject body = response1.getJSONObject("body");
//                            JSONObject items = body.getJSONObject("items");
//                            JSONArray item = items.getJSONArray("item");
//                            int[] id = {2540940, 2540171, 2539864, 2535363, 2535433,
//                                    2535419, 2535416, 2535410, 2535395, 2535373,
//                                    2519467, 2519476, 2541571, 2535370, 2535365};

                            String play = "연극";


                            for (int i = 0; i < row.length(); i++) {

                                JSONObject gal = row.getJSONObject(i);
//                                    if(gal.getString("CODENAME")==play) {
//                                        String img_string = gal.getString("MAIN_IMG");
//
//                                        String sub = img_string.substring(27);
//                                        String sub1 = "http://culture.seoul.go.kr" + sub;
//                                int galID = gal.getInt("galContentId");

//
//                                for (int j = 0; j < 15; j++) {
//                                    if (galID == id[j]) {
                                String address = gal.getString("MAIN_IMG");
                                String add = address.substring(26); //뒷부분
                                String add1;  //출력할 부분
                                String add2 = "HTTP://CULTURE.SEOUL.GO.KR"; //앞부분
                                String add3 = address.substring(0,26); //비교할부분
                                String add4 = "http://culture.seoul.go.kr";

                                    if(add2.equals(add3)) {add1 = add4+ add;}
                                    else{add1 = address;}



                                String creatorName = gal.getString("TITLE");
                                String imageUrl = add1;
                                int likeCount = gal.getInt("CULTCODE");


//                            JSONArray jsonArray = response.getJSONArray("hits");
//
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject hit = jsonArray.getJSONObject(i);
//
//                                String creatorName = hit.getString("user");
//                                String imageUrl = hit.getString("webformatURL");
//                                int likeCount = hit.getInt("likes");

                                mExampleList.add(new ExampleItem(imageUrl, creatorName, likeCount));
//                                }
                            }


                            mExampleAdapter = new ExampleAdapter(MainActivity.this, mExampleList);
                            mRecyclerView.setAdapter(mExampleAdapter);
                            mExampleAdapter.setOnItemClickListener(MainActivity.this);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(request);
    }

    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(this, DetailActivity.class);
        ExampleItem clickedItem = mExampleList.get(position);

        detailIntent.putExtra(EXTRA_URL, clickedItem.getImageUrl());
        detailIntent.putExtra(EXTRA_CREATOR, clickedItem.getCreator());
        detailIntent.putExtra(EXTRA_LIKES, clickedItem.getLikeCount());

        startActivity(detailIntent);
    }
}