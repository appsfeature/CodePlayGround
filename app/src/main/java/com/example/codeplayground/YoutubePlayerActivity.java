package com.example.codeplayground;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;

public class YoutubePlayerActivity extends AppCompatActivity {

    private static final String TAG = "@Test";
    private static final String mVideoId = "S0Q4gqBUs7c";
    private float mStartTime = 0;

    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayerTracker tracker;
    private FrameLayout  fullscreenViewContainer;
    private boolean isFullScreen;

    private YouTubePlayer youTubePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);

        initUi();

        applySetting();
    }

    private void applySetting() {
        tracker = new YouTubePlayerTracker();

        getOnBackPressedDispatcher().addCallback(onBackPressedCallback);
//        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
//            @Override
//            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
//                loadVideo(youTubePlayer, true);
//            }
//        });
        youTubePlayerView.setEnableAutomaticInitialization(false);
        custom();

        youTubePlayerView.getYouTubePlayerWhenReady(youTubePlayer -> {
            youTubePlayer.addListener(tracker);
            PlayerConstants.PlayerState state = tracker.getState();
            float currentSecond = getCurrentPlayedTime();
            float duration = getVideoDuration();
            Log.d(TAG, "currentSecond : " + currentSecond);
        });

        youTubePlayerView.addFullscreenListener(new FullscreenListener() {
            @Override
            public void onEnterFullscreen(@NonNull View fullscreenView, @NonNull Function0<Unit> exitFullscreen) {
                isFullScreen = true;
                youTubePlayerView.setVisibility(View.GONE);
                fullscreenViewContainer.setVisibility(View.VISIBLE);
                fullscreenViewContainer.addView(fullscreenView);
            }

            @Override
            public void onExitFullscreen() {
                isFullScreen = false;
                youTubePlayerView.setVisibility(View.VISIBLE);
                fullscreenViewContainer.setVisibility(View.GONE);
                fullscreenViewContainer.removeAllViews();
            }
        });
    }

    /**
     * @return current played time in millis.
     */
    private float getCurrentPlayedTime() {
        if(youTubePlayer != null){
            return tracker.getCurrentSecond() * 1000;
        }
        return 0;
    }

    private float getVideoDuration() {
        if(youTubePlayer != null){
            return tracker.getVideoDuration();
        }
        return 0;
    }

    private void custom() {
        IFramePlayerOptions options = new IFramePlayerOptions.Builder()
                .controls(1)
                .fullscreen(1)
                .build();
        youTubePlayerView.initialize(listener, options);
    }

    YouTubePlayerListener listener = new AbstractYouTubePlayerListener() {
        @Override
        public void onReady(@NonNull YouTubePlayer youTubePlayer) {
            YoutubePlayerActivity.this.youTubePlayer = youTubePlayer;
            loadVideo(youTubePlayer, true);
        }
    };

    private void initUi() {
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        fullscreenViewContainer = findViewById(R.id.full_screen_view_container);
        getLifecycle().addObserver(youTubePlayerView);
    }

    private void loadVideo(YouTubePlayer youTubePlayer, boolean isAutoPlay) {
        if(isAutoPlay) {
            youTubePlayer.loadVideo(mVideoId, mStartTime);
        }else {
            youTubePlayer.cueVideo(mVideoId, mStartTime);
        }
    }

    private final OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            if (isFullScreen) {
                // if the player is in fullscreen, exit fullscreen
                youTubePlayer.toggleFullscreen();
            } else {
                finish();
            }
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        youTubePlayerView.release();
    }



    private void openStandAlonePlayer() {
//        YouTubePlayerUtils.loadOrCueVideo(
//                youTubePlayer,
//                getLifecycle(),
//                videoId,
//                startTime
//        );
    }

}