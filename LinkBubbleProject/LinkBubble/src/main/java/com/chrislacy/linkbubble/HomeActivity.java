package com.chrislacy.linkbubble;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;

import java.util.Vector;

public class HomeActivity extends Activity {

    private static final String TAG = "HomeActivity";

    private static final String PLAYED_INTRO_ANIM_KEY = "PlayedIntroAnimation";

    View mContentView;
    View mBackgroundView;

    boolean mPlayedIntroAnimation;

    final Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        mBackgroundView = findViewById(R.id.background);
        mContentView = findViewById(R.id.content);

        if (savedInstanceState != null) {
            mPlayedIntroAnimation = savedInstanceState.getBoolean(PLAYED_INTRO_ANIM_KEY);
        }

        if (mPlayedIntroAnimation) {
            mBackgroundView.setAlpha(1.f);
            mContentView.setVisibility(View.VISIBLE);
        }

        if (Settings.get().debugAutoLoadUrl()) {
            MainApplication.openLink(this, "http://abc.net.au");
            //MainApplication.openLink(getActivity(), "https://twitter.com/lokibartleby/status/412160702707539968", false);
        }

        Vector<String> urls = Settings.get().loadCurrentBubbles();
        for (String url : urls) {
            MainApplication.openLink(this, url);
        }

        /*
        if (MainController.get() != null && MainController.get().getBubbleCount() > 0) {
            FancyCoverFlow fancyCoverFlow = (FancyCoverFlow) findViewById(R.id.fancyCoverFlow);

            BubbleCoverFlowAdapter adapter = new BubbleCoverFlowAdapter(this, MainController.get().getBubbles(), false);
            fancyCoverFlow.setAdapter(adapter);
            fancyCoverFlow.setSelection(adapter.getStartIndex(), false);
        }*/

        View historyButton = findViewById(R.id.history);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(HomeActivity.this, HistoryActivity.class), 0);
            }
        });

        View settingsButton = findViewById(R.id.settings);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(HomeActivity.this, SettingsActivity.class), 0);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        if (mPlayedIntroAnimation == false) {
            animateOn();
            mPlayedIntroAnimation = true;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putBoolean(PLAYED_INTRO_ANIM_KEY, mPlayedIntroAnimation);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mPlayedIntroAnimation = savedInstanceState.getBoolean(PLAYED_INTRO_ANIM_KEY);
    }

    void animateOn() {

        mBackgroundView.setScaleX(0.0f);
        mBackgroundView.animate().alpha(1f).scaleX(1f).start();

        mContentView.setAlpha(0f);
        mContentView.setVisibility(View.VISIBLE);
        mContentView.setScaleX(0.5f);
        mContentView.setScaleY(0.5f);
        mContentView.animate().alpha(1f).scaleX(1f).scaleY(1f)
                .setDuration(1000)
                .setInterpolator(new AnticipateOvershootInterpolator())
                .start();

        //tv.setAlpha(0f);
        //tv.setVisibility(View.VISIBLE);
        //tv.animate().alpha(1f).setDuration(1000).setStartDelay(1000).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_activity, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_history:
                startActivityForResult(new Intent(this, HistoryActivity.class), 0);
                break;

            case R.id.action_settings: {
                startActivityForResult(new Intent(this, SettingsActivity.class), 0);
                return true;
            }

            /*
            case R.id.action_upgrade_to_pro: {
                Intent intent = Config.getStoreIntent(this, Config.STORE_PRO_URL);
                if (intent != null) {
                    startActivity(intent);
                    return true;
                }
            }*/
        }

        return super.onOptionsItemSelected(item);
    }

}
