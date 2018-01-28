package com.sirlink.championgg;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.sirlink.championgg.Adapter.PageAdapter;
import com.sirlink.championgg.Fragments.ChampionsFragment;
import com.sirlink.championgg.Fragments.PerformanceFragment;

import java.util.ArrayList;
import java.util.List;

import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;

public class MainActivity extends AppCompatActivity {

    PagerAdapter pagerAdapter;
    List<Fragment> fragments;
//    private AdView mAdView;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showToolbar(getString(R.string.main_menu),false);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

//        MobileAds.initialize(getApplicationContext(),
//                "ca-app-pub-5585648622243229~5887566448");
//
//        mAdView = findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().addTestDevice("44E689DFDD65CBAE7727BA26E27C6296").build();
//        mAdView.loadAd(adRequest);


        fragments = getFragments();
        pagerAdapter = new PageAdapter(getSupportFragmentManager(), fragments) {
        };
        ViewPager pager = findViewById(R.id.pager_perfomance);
        pager.setAdapter(pagerAdapter);

        AppRate.with(this)
                .setInstallDays(3) // default 10, 0 means install day.
                .setLaunchTimes(3) // default 10
                .setRemindInterval(2) // default 1
                .setShowLaterButton(true) // default true
                .setDebug(false) // default false
                .setOnClickButtonListener(new OnClickButtonListener() { // callback listener.
                    @Override
                    public void onClickButton(int which) {
                        Bundle bundle = new Bundle();
                        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Rate dialog");
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, Integer.toString(which));
                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                    }
                })
                .monitor();

        // Show a dialog if meets conditions
        AppRate.showRateDialogIfMeetsConditions(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.container_champions, new ChampionsFragment(),"Fragment_Champ")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(null).commit();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Slide slide = new Slide();
            slide.setSlideEdge(Gravity.RIGHT);
            getWindow().setEnterTransition(slide);
        }

    }

    private List<Fragment> getFragments() {

        Bundle bundle = new Bundle();
        bundle.putInt("TYPE", 0);
        bundle.putString("param2",getString(R.string.win_rates));

        Bundle bundle1 = new Bundle();
        bundle1.putInt("TYPE", 1);
        bundle1.putString("param2",getString(R.string.overall_perfomance));

        Fragment winRates = PerformanceFragment.newInstance("win_rates",getString(R.string.win_rates));
        winRates.setArguments(bundle);

        Fragment overallPerfomance = PerformanceFragment.newInstance("overall_perfomance", getString(R.string.overall_perfomance));
        overallPerfomance.setArguments(bundle1);


        List<Fragment> fList = new ArrayList<Fragment>();
        fList.add(winRates);
        fList.add(overallPerfomance);

        return fList;
    }


    public void showToolbar(String title, boolean upButton){
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
            search(searchView);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_reload:
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("Fragment_Champ");
                getSupportFragmentManager().beginTransaction().detach(fragment).attach(fragments.get(0)).commit();
                getSupportFragmentManager().beginTransaction().detach(fragment).attach(fragments.get(1)).commit();
                getSupportFragmentManager().beginTransaction().detach(fragment).attach(fragment).commit();
                break;
            // action with ID action_settings was selected
            default:
                break;
        }

        return true;
    }


    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ChampionsFragment fragment = (ChampionsFragment) getSupportFragmentManager().findFragmentByTag("Fragment_Champ");
                if(fragment.getAdapter() != null) {
                    fragment.getAdapter().getFilter().filter(newText);
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
