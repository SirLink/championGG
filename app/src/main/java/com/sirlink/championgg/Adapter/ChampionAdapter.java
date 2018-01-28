package com.sirlink.championgg.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.sirlink.championgg.ChampInfoActivity;
import com.sirlink.championgg.R;
import com.squareup.picasso.Picasso;

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
 * Created by compudep on 21/11/2017.
 */

public class ChampionAdapter extends RecyclerView.Adapter<ChampionAdapter.ChampionViewHolder> implements Filterable{
    private int resource;
    private Activity activity;
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> winrates = new ArrayList<>();
    private ArrayList<String> urls = new ArrayList<>();
    private ArrayList<String> mFilteredNames = new ArrayList<>();
    private ArrayList<String> mFilteredUrl = new ArrayList<>();
    private ArrayList<String> mFilteredWin = new ArrayList<>();
    private FirebaseAnalytics mFirebaseAnalytics;

    public ChampionAdapter( int resource, Activity activity, ArrayList<String> names, ArrayList<String> winrates, ArrayList<String> urls ) {
        this.resource = resource;
        this.activity = activity;
        this.names = names;
        this.winrates = winrates;
        this.urls = urls;
        mFilteredNames = names;
        mFilteredUrl = urls;
        mFilteredWin = winrates;
    }



    @Override
    public ChampionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource,parent,false);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(view.getContext());
        return new ChampionViewHolder(view);    }


    @Override
    public void onBindViewHolder(final ChampionViewHolder holder, final int position) {
        double roundWin = Double.parseDouble(mFilteredWin.get(position));
        holder.championName.setText(mFilteredNames.get(position));
        if (roundWin >= 50) {
            holder.championWin.setTextColor(ContextCompat.getColor(activity, R.color.colorAccent));
        } else {
            holder.championWin.setTextColor(ContextCompat.getColor(activity, R.color.red));
        }
        holder.championWin.setText(roundWin + "%");
        Picasso.with(activity).load(mFilteredUrl.get(position)).into(holder.championImage);

        holder.championImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Champion Selected");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, mFilteredNames.get(position));
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                Intent intent = new Intent(activity, ChampInfoActivity.class);
                intent.putExtra("NAME",mFilteredNames.get(position));
                intent.putExtra("URL",mFilteredUrl.get(position));
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    Explode explode = new Explode();
                    activity.getWindow().setExitTransition(explode);
                    activity.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(activity,v,activity.getString(R.string.transitionname_picture)).toBundle());
                }else{
                    activity.startActivity(intent);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return mFilteredNames.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            ArrayList<String> filteredNames= new ArrayList<>();
            ArrayList<String> filteredUrl = new ArrayList<>();
            ArrayList<String> filteredWin = new ArrayList<>();
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                notifyDataSetChanged();
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    mFilteredNames = names;
                    mFilteredUrl = urls;
                    mFilteredWin = winrates;
                    notifyDataSetChanged();
                } else {
                    for(int i=0;i<names.size();i++) {
                        if(names.get(i).toLowerCase().contains(charString)) {
                            filteredNames.add(names.get(i));
                            filteredUrl.add(urls.get(i));
                            filteredWin.add(winrates.get(i));
                        }
                    }
                }

                return null;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if(filteredNames.size()>0) {
                    mFilteredNames = filteredNames;
                    mFilteredUrl = filteredUrl;
                    mFilteredWin = filteredWin;
                    notifyDataSetChanged();
                }
            }
        };
    }

    public class ChampionViewHolder extends RecyclerView.ViewHolder{

        private ImageView championImage;
        private TextView championName;
        private TextView championWin;

        public ChampionViewHolder(View itemView) {
            super(itemView);
            championImage = (ImageView) itemView.findViewById(R.id.championImage);
            championName = (TextView) itemView.findViewById(R.id.champName);
            championWin = (TextView) itemView.findViewById(R.id.champWin);

        }

    }
}
