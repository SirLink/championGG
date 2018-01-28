package com.sirlink.championgg.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.sirlink.championgg.R;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public InfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoFragment newInstance(String param1, String param2) {
        InfoFragment fragment = new InfoFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        String jString = getArguments().getString("JSON");
        int pos = getArguments().getInt("pos");
        try {
            //Statistics
            JSONArray jsonArray = new JSONArray(jString);
            double winD = Double.parseDouble(jsonArray.getJSONObject(pos).getString("winRate")) * 100;
            double playD = Double.parseDouble(jsonArray.getJSONObject(pos).getString("playRate")) * 100;
            double banD = Double.parseDouble(jsonArray.getJSONObject(pos).getString("banRate")) * 100;

            String win = round(winD,2)+"%";
            String play = round(playD,2)+"%";
            String ban = round(banD,2)+"%";

            TextView winV = view.findViewById(R.id.stat_winrateNumber);
            winV.setText(win);
            TextView playV = view.findViewById(R.id.stat_playrateNumber);
            playV.setText(play);
            TextView banV = view.findViewById(R.id.stat_banrateNumber);
            banV.setText(ban);

            JSONObject hashes = jsonArray.getJSONObject(pos).getJSONObject("hashes");

            //Items

            JSONObject fItems = hashes.getJSONObject("finalitemshashfixed");

            //WINRATE
            String fItemsWin = fItems.getJSONObject("highestWinrate").getString("winrate");
            String fItemsId = fItems.getJSONObject("highestWinrate").getString("hash");

            Double itemWinD = Double.parseDouble(fItemsWin);
            itemWinD = itemWinD *100;
            itemWinD = round(itemWinD,2);

            TextView itemWin = view.findViewById(R.id.highest_win_number);
            itemWin.setText(itemWinD+"%");

            if(itemWinD > 50){
                itemWin.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
            }else{
                itemWin.setTextColor(ContextCompat.getColor(getActivity(), R.color.red));
            }

            String[] delete = {"items", "-"};
            for(String replace : delete){
                fItemsId = fItemsId.replace(replace," ").trim();
            }
            while (fItemsId.contains("  ")) {
                fItemsId = fItemsId.replace("  ", " ");
            }
            String[] values = fItemsId.split(" ");

            ArrayList<ImageView> items = new ArrayList<>();
            items.add((ImageView)view.findViewById(R.id.item_1));
            items.add((ImageView)view.findViewById(R.id.item_2));
            items.add((ImageView)view.findViewById(R.id.item_3));
            items.add((ImageView)view.findViewById(R.id.item_4));
            items.add((ImageView)view.findViewById(R.id.item_5));
            items.add((ImageView)view.findViewById(R.id.item_6));

            for(int i=0;i<values.length;i++) {
                Picasso.with(getActivity()).load("http://ddragon.leagueoflegends.com/cdn/7.23.1/img/item/" + values[i] + ".png").into(items.get(i));
            }

            //FREQUENT
            String fItemsFreq = fItems.getJSONObject("highestCount").getString("winrate");
            String fItemsIdFreq = fItems.getJSONObject("highestCount").getString("hash");

            Double itemFreqD = Double.parseDouble(fItemsFreq);
            itemFreqD = itemFreqD *100;
            itemFreqD = round(itemFreqD,2);

            TextView itemFreq = view.findViewById(R.id.most_frequent_number);
            itemFreq.setText(itemFreqD+"%");

            if(itemFreqD > 50){
                itemFreq.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
            }else{
                itemFreq.setTextColor(ContextCompat.getColor(getActivity(), R.color.red));
            }

            for(String replace : delete){
                fItemsIdFreq = fItemsIdFreq.replace(replace," ").trim();
            }
            while (fItemsIdFreq.contains("  ")) {
                fItemsIdFreq = fItemsIdFreq.replace("  ", " ");
            }
            String[] valuesFreq = fItemsIdFreq.split(" ");

            ArrayList<ImageView> itemsFreq = new ArrayList<>();
            itemsFreq.add((ImageView)view.findViewById(R.id.item_1_));
            itemsFreq.add((ImageView)view.findViewById(R.id.item_2_));
            itemsFreq.add((ImageView)view.findViewById(R.id.item_3_));
            itemsFreq.add((ImageView)view.findViewById(R.id.item_4_));
            itemsFreq.add((ImageView)view.findViewById(R.id.item_5_));
            itemsFreq.add((ImageView)view.findViewById(R.id.item_6_));

            for(int i=0;i<values.length;i++) {
                Picasso.with(getActivity()).load("http://ddragon.leagueoflegends.com/cdn/7.23.1/img/item/" + valuesFreq[i] + ".png").into(itemsFreq.get(i));
            }

            //SUMMONERS
            JSONObject summoners = hashes.getJSONObject("summonershash");

            String summonersHigh = summoners.getJSONObject("highestCount").getString("hash");
            String summonersWin = summoners.getJSONObject("highestWinrate").getString("hash");

            ArrayList<ImageView> sumsHigh = new ArrayList<>();
            ArrayList<ImageView> sumsWin = new ArrayList<>();
            sumsHigh.add((ImageView)view.findViewById(R.id.summoner_1_high));
            sumsHigh.add((ImageView)view.findViewById(R.id.summoner_2_high));
            sumsWin.add((ImageView)view.findViewById(R.id.summoner_1_win));
            sumsWin.add((ImageView)view.findViewById(R.id.summoner_2_win));

            String[] replaceSum = {"-"};
            for(String replace : replaceSum){
                summonersHigh = summonersHigh.replace(replace," ").trim();
                summonersWin = summonersWin.replace(replace," ").trim();
            }
            while (summonersHigh.contains("  ")) {
                summonersHigh = summonersHigh.replace("  ", " ");
            }
            while (summonersWin.contains("  ")) {
                summonersWin = summonersWin.replace("  ", " ");
            }
            String[] valuesSumHigh = summonersHigh.split(" ");
            String[] valuesSumWin = summonersWin.split(" ");

            for(int i =0;i<valuesSumHigh.length;i++){
                switch (valuesSumHigh[i]){
                    case "1":
                        Picasso.with(getActivity()).load("http://ddragon.leagueoflegends.com/cdn/7.23.1/img/spell/SummonerBoost.png").into(sumsHigh.get(i));
                        break;
                    case "3":
                        Picasso.with(getActivity()).load("http://ddragon.leagueoflegends.com/cdn/7.23.1/img/spell/SummonerExhaust.png").into(sumsHigh.get(i));
                        break;
                    case "4":
                        Picasso.with(getActivity()).load("http://ddragon.leagueoflegends.com/cdn/7.23.1/img/spell/SummonerFlash.png").into(sumsHigh.get(i));
                        break;
                    case "6":
                        Picasso.with(getActivity()).load("http://ddragon.leagueoflegends.com/cdn/7.23.1/img/spell/SummonerHaste.png").into(sumsHigh.get(i));
                        break;
                    case "7":
                        Picasso.with(getActivity()).load("http://ddragon.leagueoflegends.com/cdn/7.23.1/img/spell/SummonerHeal.png").into(sumsHigh.get(i));
                        break;
                    case "11":
                        Picasso.with(getActivity()).load("http://ddragon.leagueoflegends.com/cdn/7.23.1/img/spell/SummonerSmite.png").into(sumsHigh.get(i));
                        break;
                    case "12":
                        Picasso.with(getActivity()).load("http://ddragon.leagueoflegends.com/cdn/7.23.1/img/spell/SummonerTeleport.png").into(sumsHigh.get(i));
                        break;
                    case "13":
                        Picasso.with(getActivity()).load("http://ddragon.leagueoflegends.com/cdn/7.23.1/img/spell/SummonerMana.png").into(sumsHigh.get(i));
                        break;
                    case "14":
                        Picasso.with(getActivity()).load("http://ddragon.leagueoflegends.com/cdn/7.23.1/img/spell/SummonerDot.png").into(sumsHigh.get(i));
                        break;
                    case "21":
                        Picasso.with(getActivity()).load("http://ddragon.leagueoflegends.com/cdn/7.23.1/img/spell/SummonerBarrier.png").into(sumsHigh.get(i));
                        break;
                    case "30":
                        Picasso.with(getActivity()).load("http://ddragon.leagueoflegends.com/cdn/7.23.1/img/spell/SummonerPoroRecall.png").into(sumsHigh.get(i));
                        break;
                    case "31":
                        Picasso.with(getActivity()).load("http://ddragon.leagueoflegends.com/cdn/7.23.1/img/spell/SummonerPoroThrow.png").into(sumsHigh.get(i));
                        break;
                    case "32":
                        Picasso.with(getActivity()).load("http://ddragon.leagueoflegends.com/cdn/7.23.1/img/spell/SummonerSnowball.png").into(sumsHigh.get(i));
                        break;
                }
            }
            for(int i =0;i<valuesSumWin.length;i++){
                switch (valuesSumWin[i]){
                    case "1":
                        Picasso.with(getActivity()).load("http://ddragon.leagueoflegends.com/cdn/7.23.1/img/spell/SummonerBoost.png").into(sumsWin.get(i));
                        break;
                    case "3":
                        Picasso.with(getActivity()).load("http://ddragon.leagueoflegends.com/cdn/7.23.1/img/spell/SummonerExhaust.png").into(sumsWin.get(i));
                        break;
                    case "4":
                        Picasso.with(getActivity()).load("http://ddragon.leagueoflegends.com/cdn/7.23.1/img/spell/SummonerFlash.png").into(sumsWin.get(i));
                        break;
                    case "6":
                        Picasso.with(getActivity()).load("http://ddragon.leagueoflegends.com/cdn/7.23.1/img/spell/SummonerHaste.png").into(sumsWin.get(i));
                        break;
                    case "7":
                        Picasso.with(getActivity()).load("http://ddragon.leagueoflegends.com/cdn/7.23.1/img/spell/SummonerHeal.png").into(sumsWin.get(i));
                        break;
                    case "11":
                        Picasso.with(getActivity()).load("http://ddragon.leagueoflegends.com/cdn/7.23.1/img/spell/SummonerSmite.png").into(sumsWin.get(i));
                        break;
                    case "12":
                        Picasso.with(getActivity()).load("http://ddragon.leagueoflegends.com/cdn/7.23.1/img/spell/SummonerTeleport.png").into(sumsWin.get(i));
                        break;
                    case "13":
                        Picasso.with(getActivity()).load("http://ddragon.leagueoflegends.com/cdn/7.23.1/img/spell/SummonerMana.png").into(sumsWin.get(i));
                        break;
                    case "14":
                        Picasso.with(getActivity()).load("http://ddragon.leagueoflegends.com/cdn/7.23.1/img/spell/SummonerDot.png").into(sumsWin.get(i));
                        break;
                    case "21":
                        Picasso.with(getActivity()).load("http://ddragon.leagueoflegends.com/cdn/7.23.1/img/spell/SummonerBarrier.png").into(sumsWin.get(i));
                        break;
                    case "30":
                        Picasso.with(getActivity()).load("http://ddragon.leagueoflegends.com/cdn/7.23.1/img/spell/SummonerPoroRecall.png").into(sumsWin.get(i));
                        break;
                    case "31":
                        Picasso.with(getActivity()).load("http://ddragon.leagueoflegends.com/cdn/7.23.1/img/spell/SummonerPoroThrow.png").into(sumsWin.get(i));
                        break;
                    case "32":
                        Picasso.with(getActivity()).load("http://ddragon.leagueoflegends.com/cdn/7.23.1/img/spell/SummonerSnowball.png").into(sumsWin.get(i));
                        break;
                }
            }

            //STARTING ITEMS
            JSONObject sItems = hashes.getJSONObject("firstitemshash");

            String sItemsHigh = sItems.getJSONObject("highestCount").getString("hash");
            String sItemsWin = sItems.getJSONObject("highestWinrate").getString("hash");

            ArrayList<ImageView> startingItemsHigh = new ArrayList<>();
            ArrayList<ImageView> startingItemsWin = new ArrayList<>();
            startingItemsHigh.add((ImageView)view.findViewById(R.id.starting_1_high));
            startingItemsHigh.add((ImageView)view.findViewById(R.id.starting_2_high));
            startingItemsWin.add((ImageView)view.findViewById(R.id.starting_1_win));
            startingItemsWin.add((ImageView)view.findViewById(R.id.starting_2_win));

            String[] replaceStarting = {"first","-"};
            for(String replace : replaceStarting){
                sItemsHigh = sItemsHigh.replace(replace," ").trim();
                sItemsWin = sItemsWin.replace(replace," ").trim();
            }
            while (sItemsHigh.contains("  ")) {
                sItemsHigh = sItemsHigh.replace("  ", " ");
            }
            while (sItemsWin.contains("  ")) {
                sItemsWin = sItemsWin.replace("  ", " ");
            }
            String[] valuesSHigh = sItemsHigh.split(" ");
            String[] valuesSWin = sItemsWin.split(" ");

            String prevVW="";
            String prevVH="";
            for(int i= 0;i<startingItemsHigh.size();i++){
                if(i<valuesSHigh.length) {
                    if(!prevVH.equals(valuesSHigh[i])) {
                        Picasso.with(getActivity()).load("http://ddragon.leagueoflegends.com/cdn/7.23.1/img/item/" + valuesSHigh[i] + ".png").into(startingItemsHigh.get(i));
                    }else {
                        if (valuesSHigh.length>i+1&&!prevVH.equals(valuesSHigh[i+1])) {
                            Picasso.with(getActivity()).load("http://ddragon.leagueoflegends.com/cdn/7.23.1/img/item/" + valuesSHigh[i + 1] + ".png").into(startingItemsHigh.get(i));
                        }else {
                            if (valuesSHigh.length>i+2&&!prevVH.equals(valuesSHigh[i + 2])) {
                                Picasso.with(getActivity()).load("http://ddragon.leagueoflegends.com/cdn/7.23.1/img/item/" + valuesSHigh[i + 2] + ".png").into(startingItemsHigh.get(i));
                            }
                        }
                    }
                    prevVH = valuesSHigh[i];
                }
            }
            for(int i= 0;i<startingItemsWin.size();i++){
                if(i<valuesSWin.length) {
                    if(!prevVW.equals(valuesSWin[i])) {
                        Picasso.with(getActivity()).load("http://ddragon.leagueoflegends.com/cdn/7.23.1/img/item/" + valuesSWin[i] + ".png").into(startingItemsWin.get(i));
                    }else {
                        if (valuesSWin.length>i+1&&!prevVW.equals(valuesSWin[i+1])) {
                            Picasso.with(getActivity()).load("http://ddragon.leagueoflegends.com/cdn/7.23.1/img/item/" + valuesSWin[i + 1] + ".png").into(startingItemsWin.get(i));
                        }else {
                            if (valuesSWin.length>i+2&&!prevVW.equals(valuesSWin[i + 2])) {
                                Picasso.with(getActivity()).load("http://ddragon.leagueoflegends.com/cdn/7.23.1/img/item/" + valuesSWin[i + 2] + ".png").into(startingItemsWin.get(i));
                            }
                        }
                    }
                    prevVW = valuesSWin[i];
                }
            }

            //SKILLS

            JSONObject skills = hashes.getJSONObject("skillorderhash");

            String skillsString = skills.getJSONObject("highestCount").getString("hash");

            String[] deleteSkills = {"skill", "-"};
            for(String replace : deleteSkills){
                skillsString = skillsString.replace(replace," ").trim();
            }
            while (skillsString.contains("  ")) {
                skillsString = skillsString.replace("  ", " ");
            }
            String[] valuesSkills = skillsString.split(" ");
            ViewGroup qRow = view.findViewById(R.id.q_row);
            ViewGroup wRow = view.findViewById(R.id.w_row);
            ViewGroup eRow = view.findViewById(R.id.e_row);
            ViewGroup rRow = view.findViewById(R.id.r_row);


            ArrayList<View> skillViews = getViewsByTag(qRow,"SKILL");
            ArrayList<View> skillWViews = getViewsByTag(wRow,"SKILLW");
            ArrayList<View> skillEViews = getViewsByTag(eRow,"SKILLE");
            ArrayList<View> skillRViews = getViewsByTag(rRow,"SKILLR");

            for(int i = 0; i<valuesSkills.length;i++){
                if(valuesSkills[i].equals("Q")){
                    skillViews.get(i).setVisibility(View.VISIBLE);
                }else{
                    if(valuesSkills[i].equals("W")){
                        skillWViews.get(i).setVisibility(View.VISIBLE);
                    }else{
                        if(valuesSkills[i].equals("E")){
                            skillEViews.get(i).setVisibility(View.VISIBLE);
                        }else{
                            if(valuesSkills[i].equals("R")){
                                skillRViews.get(i).setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }
            JSONObject runes = hashes.getJSONObject("runehash");
            String runesString = runes.getJSONObject("highestCount").getString("hash");

            String[] deleteRunes= {"-"};
            for(String replace : deleteRunes){
                runesString = runesString.replace(replace," ").trim();
            }
            while (runesString.contains("  ")) {
                runesString = runesString.replace("  ", " ");
            }
            String[] valuesRunes = runesString.split(" ");
            ArrayList<ImageView> runeViews = new ArrayList<>();
            runeViews.add((ImageView)view.findViewById(R.id.primary_element));
            runeViews.add((ImageView)view.findViewById(R.id.primary_keystone));
            runeViews.add((ImageView)view.findViewById(R.id.primary_greater_rune));
            runeViews.add((ImageView)view.findViewById(R.id.primary_rune_one));
            runeViews.add((ImageView)view.findViewById(R.id.primary_rune_two));
            runeViews.add((ImageView)view.findViewById(R.id.secondary_element));
            runeViews.add((ImageView)view.findViewById(R.id.secondary_rune_one));
            runeViews.add((ImageView)view.findViewById(R.id.secondary_rune_two));
            ArrayList<TextView> runeText = new ArrayList<>();
            runeText.add((TextView)view.findViewById(R.id.primary_element_name));
            runeText.add((TextView)view.findViewById(R.id.primary_keystone_name));
            runeText.add((TextView)view.findViewById(R.id.primary_greater_rune_name));
            runeText.add((TextView)view.findViewById(R.id.primary_rune_one_name));
            runeText.add((TextView)view.findViewById(R.id.primary_rune_two_name));
            runeText.add((TextView)view.findViewById(R.id.secondary_element_name));
            runeText.add((TextView)view.findViewById(R.id.secondary_rune_one_name));
            runeText.add((TextView)view.findViewById(R.id.secondary_rune_two_name));

            //ID RUNES
            InputStream is = getResources().openRawResource(R.raw.perks);
            Writer writer = new StringWriter();
            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            String jsonString = writer.toString();
            JSONArray jsonRunes = new JSONArray(jsonString);
            ImageView pPath = view.findViewById(R.id.primary_path);
            ImageView sPath = view.findViewById(R.id.secondary_path);
            pPath.getDrawable().setColorFilter(Color.CYAN, PorterDuff.Mode.MULTIPLY);
            sPath.getDrawable().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);


            Display display = getActivity().getWindowManager().getDefaultDisplay();
            DisplayMetrics outMetrics = new DisplayMetrics();
            display.getMetrics(outMetrics);
            float density  = getResources().getDisplayMetrics().density;
            float dpWidth  = outMetrics.widthPixels / density;



            for(int i=0;i<jsonRunes.length();i++){
                String id =  jsonRunes.getJSONObject(i).getString("id");

                for(int j=0;j<valuesRunes.length;j++) {
                    if (valuesRunes[j].equals(id)) {
                        Drawable drawable = GetImage(getActivity(),jsonRunes.getJSONObject(i).getString("iconPath"));
                        runeViews.get(j).setImageDrawable(drawable);
                        String runeN;
                        if(getResources().getConfiguration().locale.getLanguage().equals("es")){
                            runeN = jsonRunes.getJSONObject(i).getString("nameEs");
                            if (dpWidth <= 600) {
                                runeN = trimString(runeN,18,false);
                            }
                            runeText.get(j).setText(runeN);
                        }else if(getResources().getConfiguration().locale.getLanguage().equals("pt")) {
                            runeN = jsonRunes.getJSONObject(i).getString("namePr");
                            if (dpWidth <= 600) {
                                runeN = trimString(runeN,18,false);
                            }
                            runeText.get(j).setText(runeN);
                        }else{
                            runeN = jsonRunes.getJSONObject(i).getString("name");
                            if (dpWidth <= 600) {
                                runeN = trimString(runeN,18,false);
                            }
                            runeText.get(j).setText(runeN);
                        }
                    }
                }
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

        //RUNES



        return view;
    }

    public static Drawable GetImage(Context c, String ImageName) {
        return c.getResources().getDrawable(c.getResources().getIdentifier(ImageName, "drawable", c.getPackageName()));
    }

    public static String trimString(String string, int length, boolean soft) {
        if(string == null || string.trim().isEmpty()){
            return string;
        }

        StringBuffer sb = new StringBuffer(string);
        int actualLength = length - 3;
        if(sb.length() > actualLength){
            // -3 because we add 3 dots at the end. Returned string length has to be length including the dots.
            if(!soft)
                return sb.insert(actualLength, "...").substring(0, actualLength+3);
            else {
                int endIndex = sb.indexOf(" ",actualLength);
                return sb.insert(endIndex,"...").substring(0, endIndex+3);
            }
        }
        return string;
    }

    private static ArrayList<View> getViewsByTag(ViewGroup root, String tag){
        ArrayList<View> views = new ArrayList<View>();
        final int childCount = root.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = root.getChildAt(i);
            if (child instanceof ViewGroup) {
                views.addAll(getViewsByTag((ViewGroup) child, tag));
            }

            final Object tagObj = child.getTag();
            if (tagObj != null && tagObj.equals(tag)) {
                views.add(child);
            }

        }
        return views;
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
