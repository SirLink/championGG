package com.sirlink.championgg.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sirlink.championgg.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PerformanceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PerformanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerformanceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PerformanceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerformanceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PerformanceFragment newInstance(String param1, String param2) {
        PerformanceFragment fragment = new PerformanceFragment();
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

    JSONArray jsonPerf;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_performance, container, false);

        final TextView top = view.findViewById(R.id.top_textview2);
        final TextView jg = view.findViewById(R.id.jg_textview2);
        final TextView mid = view.findViewById(R.id.mid_textview2);
        final TextView bot = view.findViewById(R.id.bot_textview2);
        final TextView support = view.findViewById(R.id.support_textview2);

        final TextView top3 = view.findViewById(R.id.top_textview3);
        final TextView jg3 = view.findViewById(R.id.jg_textview3);
        final TextView mid3 = view.findViewById(R.id.mid_textview3);
        final TextView bot3 = view.findViewById(R.id.bot_textview3);
        final TextView support3 = view.findViewById(R.id.support_textview3);

        final ImageView topImg = view.findViewById(R.id.top_imageview);
        final ImageView jgImg = view.findViewById(R.id.jg_imageview);
        final ImageView midImg = view.findViewById(R.id.mid_imageview);
        final ImageView botImg = view.findViewById(R.id.bot_imageview);
        final ImageView supportImg = view.findViewById(R.id.support_imageview);

        final int type = getArguments().getInt("TYPE");

        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://api.champion.gg/v2/overall").newBuilder();
        urlBuilder.addQueryParameter("elo", "PLATINUM");
        urlBuilder.addQueryParameter("api_key", "fb2bfa1bfa0c8b2dcf2001a24f1a49a7");
        String url = urlBuilder.build().toString();

        final Request request = new Request.Builder()
                .url(url)
                .build();

        com.sirlink.championgg.OkClient.getHttp().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    jsonPerf = new JSONArray(response.body().string());

                    switch (type){
                        case 0:
                            HttpUrl.Builder urlBuilder1 = HttpUrl.parse("http://ddragon.leagueoflegends.com/cdn/7.24.1/data/en_US/champion.json").newBuilder();
                            String url1 = urlBuilder1.build().toString();
                            Request request1 = new Request.Builder()
                                    .url(url1)
                                    .build();

                            com.sirlink.championgg.OkClient.getHttp().newCall(request1).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    call.cancel();
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
                                                //Mid Lane
                                                String championName;
                                                String championWinrate;

                                                int id = jsonPerf.getJSONObject(0).getJSONObject("positions").getJSONObject("MIDDLE").getJSONObject("winrate").getJSONObject("best").getInt("championId");
                                                championWinrate = jsonPerf.getJSONObject(0).getJSONObject("positions").getJSONObject("MIDDLE").getJSONObject("winrate").getJSONObject("best").getString("score");

                                                JSONObject jsonName = new JSONObject(myResponse);
                                                JSONObject jsonObject = jsonName.getJSONObject("data");
                                                JSONArray jsonArray = jsonObject.toJSONArray(jsonObject.names());

                                                for(int i = 0;i < jsonArray.length();i++){

                                                    int idObj = Integer.parseInt(jsonArray.getJSONObject(i).getString("key"));
                                                    if(idObj == id){
                                                        championName = jsonArray.getJSONObject(i).getString("name");
                                                        Double winrate = Double.parseDouble(championWinrate) * 100;
                                                        Double roundWin = round(winrate,2);
                                                        mid.setText(championName);
                                                        String nameImg = championName.replaceAll("\\s+", "");
                                                        nameImg = fixName(nameImg);
                                                        mid3.setText(roundWin+"%");
                                                        Picasso.with(getContext()).load("http://ddragon.leagueoflegends.com/cdn/7.24.1/img/champion/" + nameImg + ".png").into(midImg);
                                                    }
                                                }

                                                //Top Lane
                                                String championNameTop;
                                                String championWinrateTop;

                                                int idTop = jsonPerf.getJSONObject(0).getJSONObject("positions").getJSONObject("TOP").getJSONObject("winrate").getJSONObject("best").getInt("championId");
                                                championWinrateTop = jsonPerf.getJSONObject(0).getJSONObject("positions").getJSONObject("TOP").getJSONObject("winrate").getJSONObject("best").getString("score");

                                                for(int i = 0;i < jsonArray.length();i++){

                                                    int idObj = Integer.parseInt(jsonArray.getJSONObject(i).getString("key"));
                                                    if(idObj == idTop){
                                                        championNameTop = jsonArray.getJSONObject(i).getString("name");
                                                        Double winrate = Double.parseDouble(championWinrateTop) * 100;
                                                        Double roundWin = round(winrate,2);
                                                        top.setText(championNameTop);
                                                        String nameImg = championNameTop.replaceAll("\\s+", "");
                                                        nameImg = fixName(nameImg);
                                                        top3.setText(roundWin + "%");
                                                        Picasso.with(getContext()).load("http://ddragon.leagueoflegends.com/cdn/7.24.1/img/champion/" + nameImg + ".png").into(topImg);
                                                    }
                                                }

                                                //Jg
                                                String championNameJg;
                                                String championWinrateJg;

                                                int idJg = jsonPerf.getJSONObject(0).getJSONObject("positions").getJSONObject("JUNGLE").getJSONObject("winrate").getJSONObject("best").getInt("championId");
                                                championWinrateJg= jsonPerf.getJSONObject(0).getJSONObject("positions").getJSONObject("JUNGLE").getJSONObject("winrate").getJSONObject("best").getString("score");

                                                for(int i = 0;i < jsonArray.length();i++){

                                                    int idObj = Integer.parseInt(jsonArray.getJSONObject(i).getString("key"));
                                                    if(idObj == idJg){
                                                        championNameJg = jsonArray.getJSONObject(i).getString("name");
                                                        Double winrate = Double.parseDouble(championWinrateJg) * 100;
                                                        Double roundWin = round(winrate,2);
                                                        jg.setText(championNameJg);
                                                        String nameImg = championNameJg.replaceAll("\\s+", "");
                                                        nameImg = fixName(nameImg);
                                                        jg3.setText(roundWin + "%");
                                                        Picasso.with(getContext()).load("http://ddragon.leagueoflegends.com/cdn/7.24.1/img/champion/" + nameImg + ".png").into(jgImg);
                                                    }
                                                }

                                                //ADC
                                                String championNameBot;
                                                String championWinrateBot;

                                                int idBot = jsonPerf.getJSONObject(0).getJSONObject("positions").getJSONObject("DUO_CARRY").getJSONObject("winrate").getJSONObject("best").getInt("championId");
                                                championWinrateBot = jsonPerf.getJSONObject(0).getJSONObject("positions").getJSONObject("DUO_CARRY").getJSONObject("winrate").getJSONObject("best").getString("score");

                                                for(int i = 0;i < jsonArray.length();i++){

                                                    int idObj = Integer.parseInt(jsonArray.getJSONObject(i).getString("key"));
                                                    if(idObj == idBot){
                                                        championNameBot = jsonArray.getJSONObject(i).getString("name");
                                                        Double winrate = Double.parseDouble(championWinrateBot) * 100;
                                                        Double roundWin = round(winrate,2);
                                                        bot.setText(championNameBot);
                                                        String nameImg = championNameBot.replaceAll("\\s+", "");
                                                        nameImg = fixName(nameImg);
                                                        bot3.setText(roundWin + "%");
                                                        Picasso.with(getContext()).load("http://ddragon.leagueoflegends.com/cdn/7.24.1/img/champion/" + nameImg + ".png").into(botImg);
                                                    }
                                                }

                                                //Support
                                                String championNameSup;
                                                String championWinrateSup;

                                                int idSup = jsonPerf.getJSONObject(0).getJSONObject("positions").getJSONObject("DUO_SUPPORT").getJSONObject("winrate").getJSONObject("best").getInt("championId");
                                                championWinrateSup = jsonPerf.getJSONObject(0).getJSONObject("positions").getJSONObject("DUO_SUPPORT").getJSONObject("winrate").getJSONObject("best").getString("score");

                                                for(int i = 0;i < jsonArray.length();i++){

                                                    int idObj = Integer.parseInt(jsonArray.getJSONObject(i).getString("key"));
                                                    if(idObj == idSup){
                                                        championNameSup = jsonArray.getJSONObject(i).getString("name");
                                                        Double winrate = Double.parseDouble(championWinrateSup) * 100;
                                                        Double roundWin = round(winrate,2);
                                                        support.setText(championNameSup);
                                                        String nameImg = championNameSup.replaceAll("\\s+", "");
                                                        nameImg = fixName(nameImg);
                                                        support3.setText(roundWin + "%");
                                                        Picasso.with(getContext()).load("http://ddragon.leagueoflegends.com/cdn/7.24.1/img/champion/" + nameImg + ".png").into(supportImg);
                                                    }
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                }
                            });

                            break;
                        case 1:
                            HttpUrl.Builder urlBuilder2 = HttpUrl.parse("http://ddragon.leagueoflegends.com/cdn/7.24.1/data/en_US/champion.json").newBuilder();
                            String url2 = urlBuilder2.build().toString();
                            Request request2 = new Request.Builder()
                                    .url(url2)
                                    .build();

                            com.sirlink.championgg.OkClient.getHttp().newCall(request2).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    call.cancel();
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
                                                TextView winRate = getView().findViewById(R.id.champion_winrate);
                                                winRate.setVisibility(View.GONE);
                                                //Mid Lane
                                                String championName;

                                                int id = jsonPerf.getJSONObject(0).getJSONObject("positions").getJSONObject("MIDDLE").getJSONObject("performanceScore").getJSONObject("best").getInt("championId");

                                                JSONObject jsonName = new JSONObject(myResponse);
                                                JSONObject jsonObject = jsonName.getJSONObject("data");
                                                JSONArray jsonArray = jsonObject.toJSONArray(jsonObject.names());

                                                for(int i = 0;i < jsonArray.length();i++){

                                                    int idObj = Integer.parseInt(jsonArray.getJSONObject(i).getString("key"));
                                                    if(idObj == id){
                                                        championName = jsonArray.getJSONObject(i).getString("name");
                                                        mid.setText(championName);
                                                        String nameImg = championName.replaceAll("\\s+", "");
                                                        nameImg = fixName(nameImg);
                                                        Picasso.with(getContext()).load("http://ddragon.leagueoflegends.com/cdn/7.24.1/img/champion/" + nameImg + ".png").into(midImg);
                                                    }
                                                }

                                                //Top Lane
                                                String championNameTop;

                                                int idTop = jsonPerf.getJSONObject(0).getJSONObject("positions").getJSONObject("TOP").getJSONObject("performanceScore").getJSONObject("best").getInt("championId");

                                                for(int i = 0;i < jsonArray.length();i++){

                                                    int idObj = Integer.parseInt(jsonArray.getJSONObject(i).getString("key"));
                                                    if(idObj == idTop){
                                                        championNameTop = jsonArray.getJSONObject(i).getString("name");
                                                        top.setText(championNameTop);
                                                        String nameImg = championNameTop.replaceAll("\\s+", "");
                                                        nameImg = fixName(nameImg);
                                                        Picasso.with(getContext()).load("http://ddragon.leagueoflegends.com/cdn/7.24.1/img/champion/" + nameImg + ".png").into(topImg);
                                                    }
                                                }

                                                //Jg
                                                String championNameJg;

                                                int idJg = jsonPerf.getJSONObject(0).getJSONObject("positions").getJSONObject("JUNGLE").getJSONObject("performanceScore").getJSONObject("best").getInt("championId");
                                                for(int i = 0;i < jsonArray.length();i++){

                                                    int idObj = Integer.parseInt(jsonArray.getJSONObject(i).getString("key"));
                                                    if(idObj == idJg){
                                                        championNameJg = jsonArray.getJSONObject(i).getString("name");
                                                        jg.setText(championNameJg);
                                                        String nameImg = championNameJg.replaceAll("\\s+", "");
                                                        nameImg = fixName(nameImg);
                                                        Picasso.with(getContext()).load("http://ddragon.leagueoflegends.com/cdn/7.24.1/img/champion/" + nameImg + ".png").into(jgImg);
                                                    }
                                                }

                                                //ADC
                                                String championNameBot;

                                                int idBot = jsonPerf.getJSONObject(0).getJSONObject("positions").getJSONObject("DUO_CARRY").getJSONObject("performanceScore").getJSONObject("best").getInt("championId");

                                                for(int i = 0;i < jsonArray.length();i++){

                                                    int idObj = Integer.parseInt(jsonArray.getJSONObject(i).getString("key"));
                                                    if(idObj == idBot){
                                                        championNameBot = jsonArray.getJSONObject(i).getString("name");
                                                        bot.setText(championNameBot);
                                                        String nameImg = championNameBot.replaceAll("\\s+", "");
                                                        nameImg = fixName(nameImg);
                                                        Picasso.with(getContext()).load("http://ddragon.leagueoflegends.com/cdn/7.24.1/img/champion/" + nameImg + ".png").into(botImg);
                                                    }
                                                }

                                                //Support
                                                String championNameSup;

                                                int idSup = jsonPerf.getJSONObject(0).getJSONObject("positions").getJSONObject("DUO_SUPPORT").getJSONObject("performanceScore").getJSONObject("best").getInt("championId");

                                                for(int i = 0;i < jsonArray.length();i++){

                                                    int idObj = Integer.parseInt(jsonArray.getJSONObject(i).getString("key"));
                                                    if(idObj == idSup){
                                                        championNameSup = jsonArray.getJSONObject(i).getString("name");
                                                        support.setText(championNameSup);
                                                        String nameImg = championNameSup.replaceAll("\\s+", "");
                                                        nameImg = fixName(nameImg);
                                                        Picasso.with(getContext()).load("http://ddragon.leagueoflegends.com/cdn/7.24.1/img/champion/" + nameImg + ".png").into(supportImg);
                                                    }
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                }
                            });
                            break;

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }


    public String fixName(String nameImg){
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

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
