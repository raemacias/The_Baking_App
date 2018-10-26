package com.raemacias.thebakingapp.fragments;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.raemacias.thebakingapp.R;
import com.raemacias.thebakingapp.models.Step;

public class RecipeStepFragment extends Fragment {

   private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
   private static final String TAG = "PlayerActivity";

    public static final String AUTOPLAY = "autoplay";
    public static final String CURRENT_WINDOW_INDEX = "current_window_index";
    public static final String PLAYBACK_POSITION = "playback_position";

   private SimpleExoPlayer player;
   private SimpleExoPlayerView playerView;
   private ComponentListener componentListener;
   private boolean autoPlay = false;

   private long playbackPosition;
   private int currentWindow;
   private boolean playWhenReady = true;
   TextView recipeInstruction;
   String stepDescription;
   String videoUrl;
   Step step;

//    private OnFragmentInteractionListener mListener;

    public RecipeStepFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_step_fragment, container, false);

        Bundle bundle = this.getArguments();
//        if (bundle != null) {
            step = bundle.getParcelable("Steps");
            stepDescription = step.getDescription();
//        }

        recipeInstruction = rootView.findViewById(R.id.tv_step_description);
        recipeInstruction.setText(stepDescription);

        componentListener = new ComponentListener();
        playerView = rootView.findViewById(R.id.video_view);

        if (savedInstanceState != null) {
            playbackPosition = savedInstanceState.getLong(PLAYBACK_POSITION, 0);
            currentWindow = savedInstanceState.getInt(CURRENT_WINDOW_INDEX, 0);
            autoPlay = savedInstanceState.getBoolean(AUTOPLAY, false);
        }


            initializePlayer();


        return rootView;

    }


    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void initializePlayer() {
        if (player == null) {
            TrackSelection.Factory adaptiveTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);

            player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getContext()),
            new DefaultTrackSelector(adaptiveTrackSelectionFactory), new DefaultLoadControl());

            player.addListener(componentListener);
            player.setVideoDebugListener(componentListener);
            player.setAudioDebugListener(componentListener);
            playerView.setPlayer(player);
            player.setPlayWhenReady(playWhenReady);
            player.seekTo(currentWindow, playbackPosition);

            Bundle bundle = this.getArguments();
                videoUrl = step.getVideoURL();
                if (videoUrl.isEmpty()) {
                    playerView.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "No video available.", Toast.LENGTH_SHORT).show();

                } else {

                    String userAgent = Util.getUserAgent(getContext(), "Recipe Instructions");
                    MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(videoUrl), new DefaultDataSourceFactory(getContext(),
                            userAgent), new DefaultExtractorsFactory(), null, null);

                    player.prepare(mediaSource);
                    player.setPlayWhenReady(true);
                }
            }
        }


    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.removeListener(componentListener);
            player.setVideoListener(null);
            player.setVideoDebugListener(null);
            player.release();
            player = null;
        }
    }

    //This came from this gist: https://gist.github.com/codeshifu/c26bb8a5f27f94d73b3a4888a509927c
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        /*
         * A simple configuration change such as screen rotation will destroy this activity
         * so we'll save the player state here in a bundle (that we can later access in onCreate) before everything is lost
         * NOTE: we cannot save player state in onDestroy like we did in onPause and onStop
         * the reason being our activity will be recreated from scratch and we would have lost all members (e.g. variables, objects) of this activity
         */
        if (player != null) {
            outState.putLong(PLAYBACK_POSITION, player.getCurrentPosition());
            outState.putInt(CURRENT_WINDOW_INDEX, currentWindow);
            outState.putBoolean(AUTOPLAY, autoPlay);
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri,
                new DefaultHttpDataSourceFactory("ua"),
                new DefaultExtractorsFactory(), null, null);
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
        | View.SYSTEM_UI_FLAG_FULLSCREEN
        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
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
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }

    public class ComponentListener implements ExoPlayer.EventListener, VideoRendererEventListener,
            AudioRendererEventListener{

        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest) {
            //do nothing
        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            //do nothing
        }

        @Override
        public void onLoadingChanged(boolean isLoading) {
            //do nothing
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            String stateString;
            switch (playbackState) {
                case ExoPlayer.STATE_IDLE:
                    stateString = "ExoPlayer.STATE_IDLE -";
                    break;
                    case ExoPlayer.STATE_BUFFERING:
                        stateString = "ExoPlayer.STATE_BUFFERING -";
                        break;
                        case ExoPlayer.STATE_READY:
                            stateString = "ExoPlayer.STATE_READY -";
                            break;
                            case ExoPlayer.STATE_ENDED:
                                stateString = "ExoPlayer.STATE_ENDED -";
                                break;
                                default:
                                    stateString = "UNKNOWN_STATE    -";
                                    break;

            }
            Log.d(TAG, "changed state to " + stateString + " playWhenReady: " + playWhenReady);

        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            //do nothing
        }

        @Override
        public void onPositionDiscontinuity() {
            //do nothing
        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            //do nothing
        }

        @Override
        public void onAudioEnabled(DecoderCounters counters) {
        }

        @Override
        public void onAudioSessionId(int audioSessionId) {

        }

        @Override
        public void onAudioDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {

        }

        @Override
        public void onAudioInputFormatChanged(Format format) {

        }

        @Override
        public void onAudioTrackUnderrun(int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {
        }

        @Override
        public void onAudioDisabled(DecoderCounters counters) {

        }

        @Override
        public void onVideoEnabled(DecoderCounters counters) {
        }

        @Override
        public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {
        }

        @Override
        public void onVideoInputFormatChanged(Format format) {
        }

        @Override
        public void onDroppedFrames(int count, long elapsedMs) {
        }

        @Override
        public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        }

        @Override
        public void onRenderedFirstFrame(Surface surface) {
        }

        @Override
        public void onVideoDisabled(DecoderCounters counters) {
        }
    }
}
