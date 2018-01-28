package com.sirlink.championgg;

import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.sirlink.championgg.Adapter.PageAdapter;
import com.sirlink.championgg.Fragments.InfoFragment;
import com.sirlink.championgg.Fragments.PerformanceFragment;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

public class ChampInfoActivity extends AppCompatActivity {

    String name;
    String url;
    int id;
    List<Fragment> fragments;
    int quantity;
    JSONArray jsonArray;
    PagerAdapter pagerAdapter;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champ_info);
        Bundle extras =getIntent().getExtras();
        if(extras != null){
            name = extras.getString("NAME");
            url = extras.getString("URL");
        }
        showToolbar(name, true);

//        Picasso.with(this).load(url).into((ImageView)findViewById(R.id.champ_portrait));
        String img = fixName(name);
        Picasso.with(this).load("http://ddragon.leagueoflegends.com/cdn/img/champion/splash/"+img+"_0.jpg").into((ImageView)findViewById(R.id.champ_portrait));
//        TextView viewName = findViewById(R.id.champ_name);
//        viewName.setText(name);

        MobileAds.initialize(getApplicationContext(),
                "ca-app-pub-5585648622243229~5887566448");

        mAdView = findViewById(R.id.adView3);
        AdRequest adRequest3 = new AdRequest.Builder().addTestDevice("44E689DFDD65CBAE7727BA26E27C6296").build();
        mAdView.loadAd(adRequest3);

        HttpUrl.Builder urlBuilder1 = HttpUrl.parse("http://ddragon.leagueoflegends.com/cdn/81/data/en_US/champion.json").newBuilder();
        String url1 = urlBuilder1.build().toString();
        final Request request1 = new Request.Builder()
                .url(url1)
                .build();

        OkClient.getHttp().newCall(request1).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {
                    JSONObject jsonOb = new JSONObject(response.body().string());
                    JSONObject jsonObject = jsonOb.getJSONObject("data");
                    jsonArray = jsonObject.toJSONArray(jsonObject.names());

                    for(int i = 0;i < jsonArray.length();i++) {
                        try {
                            String nameArray = jsonArray.getJSONObject(i).getString("name");
                            if(name.equals(nameArray)){
                                id = Integer.parseInt(jsonArray.getJSONObject(i).getString("key"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                
                HttpUrl.Builder urlBuilder = HttpUrl.parse("http://api.champion.gg/v2/champions/"+id).newBuilder();
                urlBuilder.addQueryParameter("champData", "hashes");
                urlBuilder.addQueryParameter("api_key", "fb2bfa1bfa0c8b2dcf2001a24f1a49a7");

                final String url = urlBuilder.build().toString();

                final Request request = new Request.Builder()
                        .url(url)
                        .build();

                OkClient.getHttp().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String myResponse = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    jsonArray = new JSONArray(myResponse);
                                    quantity = jsonArray.length();
                                    ArrayList<String> roles = new ArrayList<>();
                                    for(int i =0;i<jsonArray.length();i++){
                                        roles.add(jsonArray.getJSONObject(i).getString("role"));
                                    }
                                    fragments = getFragments(quantity,roles,myResponse);
                                    pagerAdapter = new PageAdapter(getSupportFragmentManager(), fragments) {
                                    };
                                    ViewPager pager = findViewById(R.id.pager);
                                    pager.setAdapter(pagerAdapter);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
            }
        });
    }



    public String fixName(String name){
        String nameImg = name.replaceAll("\\s+", "");
        if (nameImg.equals("Cho'Gath")) {
            nameImg = "Chogath";
        }
        if (nameImg.equals("Dr.Mundo")) {
            nameImg = "DrMundo";
        }
        if (nameImg.equals("Kha'Zix")) {
            nameImg = "Khazix";
        }
        if (nameImg.equals("Kog'Maw")) {
            nameImg = "KogMaw";
        }
        if (nameImg.equals("LeBlanc")) {
            nameImg = "Leblanc";
        }
        if (nameImg.equals("Wukong")) {
            nameImg = "MonkeyKing";
        }
        if (nameImg.equals("Rek'Sai")) {
            nameImg = "RekSai";
        }
        if (nameImg.equals("Vel'Koz")) {
            nameImg = "Velkoz";
        }
        return nameImg;
    }

    private List<Fragment> getFragments(int quantity, ArrayList<String> roles, String JSONarray) {
        List<Fragment> fList = new ArrayList<Fragment>();
        String roleName = "";
        for(int i=0;i<quantity;i++){
            switch (roles.get(i)){
                case "MIDDLE":
                    roleName = getString(R.string.mid_lane);
                    break;
                case "DUO_SUPPORT":
                    roleName = getString(R.string.support_lane);
                    break;
                case "DUO_CARRY":
                    roleName = getString(R.string.bot_lane);
                    break;
                case "TOP":
                    roleName = getString(R.string.top_lane);
                    break;
                case "JUNGLE":
                    roleName = getString(R.string.jg_lane);
                    break;
            }
            Bundle bundle = new Bundle();
            bundle.putString("param2",roleName);
            bundle.putString("JSON",JSONarray);
            bundle.putInt("pos",i);
            bundle.putStringArrayList("Array",roles);
            Fragment fragment = InfoFragment.newInstance("role",roleName);
            fragment.setArguments(bundle);
            fList.add(fragment);
        }
        return fList;
    }



    public void showToolbar(String title, boolean upButton){
        Toolbar toolbar = findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);

    }

}
