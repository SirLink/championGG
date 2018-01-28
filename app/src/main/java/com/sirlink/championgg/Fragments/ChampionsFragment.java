package com.sirlink.championgg.Fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.sirlink.championgg.Adapter.ChampionAdapter;
import com.sirlink.championgg.OkClient;
import com.sirlink.championgg.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.sirlink.championgg.Fragments.PerformanceFragment.round;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChampionsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChampionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChampionsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ChampionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChampionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChampionsFragment newInstance(String param1, String param2) {
        ChampionsFragment fragment = new ChampionsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    ChampionAdapter championAdapter;
    boolean fail = false;
    private AdView mAdView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_champions, container, false);
        view.findViewById(R.id.failLoading).setVisibility(View.GONE);
        mAdView = view.findViewById(R.id.adView2);
        AdRequest adRequest2 = new AdRequest.Builder().addTestDevice("44E689DFDD65CBAE7727BA26E27C6296").build();
        mAdView.loadAd(adRequest2);
        HttpUrl.Builder urlBuilder1 = HttpUrl.parse("http://ddragon.leagueoflegends.com/cdn/7.24.1/data/en_US/champion.json").newBuilder();
        String url1 = urlBuilder1.build().toString();
        Request request1 = new Request.Builder()
                .url(url1)
                .build();

        final ArrayList<String> names = new ArrayList<>();
        final ArrayList<String> winrates = new ArrayList<>();
        final ArrayList<String> urls = new ArrayList<>();
        com.sirlink.championgg.OkClient.getHttp().newCall(request1).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(getActivity() == null)
                    return;

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        view.findViewById(R.id.failLoading).setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String myResponse = response.body().string();
                if(getActivity() == null)
                    return;

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject champions = new JSONObject(myResponse);
                            JSONObject jsonObject = champions.getJSONObject("data");
                            final JSONArray jsonArray = jsonObject.toJSONArray(jsonObject.names());

                            try {
                                for(int i = 0; i<jsonArray.length();i++) {
                                    final String champName = jsonArray.getJSONObject(i).getString("name");
                                    final int j = i;
                                    final String id = jsonArray.getJSONObject(i).getString("key");
                                    HttpUrl.Builder urlBuilder = HttpUrl.parse("http://api.champion.gg/v2/champions/"+id).newBuilder();
                                    urlBuilder.addQueryParameter("limit", "2");
                                    urlBuilder.addQueryParameter("api_key", "fb2bfa1bfa0c8b2dcf2001a24f1a49a7");

                                    final String url = urlBuilder.build().toString();

                                    final Request request = new Request.Builder()
                                            .url(url)
                                            .build();

                                    com.sirlink.championgg.OkClient.getHttp().newCall(request).enqueue(new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                            if(getActivity() == null)
                                                return;

                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    view.findViewById(R.id.failLoading).setVisibility(View.VISIBLE);
                                                }
                                            });
                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            final String myResponse = response.body().string();
                                            if(getActivity() == null)
                                                return;

                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        JSONArray jsonPerf = new JSONArray(myResponse);

                                                        String championWinrate = jsonPerf.getJSONObject(0).getString("winRate");
                                                        Double winrate = Double.parseDouble(championWinrate) * 100;
                                                        Double roundWin = round(winrate, 2);

                                                            String nameImg = champName.replaceAll("\\s+", "");
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
                                                            names.add(champName);
                                                            winrates.add(roundWin.toString());
                                                            urls.add("http://ddragon.leagueoflegends.com/cdn/7.24.1/img/champion/" + nameImg + ".png");
                                                            if(j == jsonArray.length()-1){
                                                                championAdapter = new ChampionAdapter(R.layout.champion_view, getActivity(), names, winrates, urls);
                                                                RecyclerView recyclerView = view.findViewById(R.id.championRecycler);
                                                                Display display = getActivity().getWindowManager().getDefaultDisplay();
                                                                DisplayMetrics outMetrics = new DisplayMetrics();
                                                                display.getMetrics(outMetrics);
                                                                float density  = getResources().getDisplayMetrics().density;
                                                                float dpWidth  = outMetrics.widthPixels / density;

                                                                if (dpWidth >= 600) {
                                                                    // on a large screen device ...
                                                                    if(getResources().getConfiguration().orientation == getResources().getConfiguration().ORIENTATION_LANDSCAPE) {
                                                                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 5);
                                                                        recyclerView.setLayoutManager(gridLayoutManager);
                                                                    }else{
                                                                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
                                                                        recyclerView.setLayoutManager(gridLayoutManager);
                                                                    }
                                                                }else {
                                                                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
                                                                    recyclerView.setLayoutManager(gridLayoutManager);
                                                                }

                                                                recyclerView.setAdapter(championAdapter);
                                                                recyclerView.getAdapter().notifyDataSetChanged();
                                                                view.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                                                                view.findViewById(R.id.progress_bar).setVisibility(View.GONE);
                                                                ProgressBar progressBar = view.findViewById(R.id.progress_bar);
                                                                progressBar.setProgress(0);
                                                            }else{
                                                                ProgressBar progressBar = view.findViewById(R.id.progress_bar);
                                                                double result =  ((double)  j) / (jsonArray.length());
                                                                double progress = result * 100;
                                                                progressBar.setProgress((int)progress);
                                                            }

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                }
                                            });
                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

        });

        return view;
    }

    public ChampionAdapter getAdapter(){
        return championAdapter;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
