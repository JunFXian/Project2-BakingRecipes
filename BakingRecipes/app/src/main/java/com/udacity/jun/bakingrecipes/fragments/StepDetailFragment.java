/**
 * Created by Jun Xian for Udacity Android Developer Nanodegree project 2
 * Date: July 28, 2017
 * Reference:
 *      https://github.com/udacity/Android_Me
 *      https://github.com/udacity/AdvancedAndroid_ClassicalMusicQuiz
 *      https://stackoverflow.com/questions/20181491/use-picasso-to-get-a-callback-with-a-bitmap
 *      https://google.github.io/ExoPlayer/guide.html
 *      https://medium.com/@yusufcakmak/playing-audio-and-video-with-exoplayer-2-4f4c2c2d9772
 *      http://www.androiddesignpatterns.com/2012/05/using-newinstance-to-instantiate.html
 *      https://github.com/google/ExoPlayer/issues/702
 */

package com.udacity.jun.bakingrecipes.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.udacity.jun.bakingrecipes.R;
import com.udacity.jun.bakingrecipes.models.RecipeStep;

public class StepDetailFragment extends Fragment implements ExoPlayer.EventListener{
    private RecipeStep mStep;
    private int mStepsCount;
    private String mVideoUrl = "";
    private String mThumbnailUrl = "";

    private Target mTarget;
    private SimpleExoPlayer mPlayer;
    private SimpleExoPlayerView mPlayerView;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private long mPlayerPosition;

    // Define a new interface DataPassingFromDetailListener that triggers a callback in the host
    // activity
    private StepDetailFragment.DataPassingFromDetailListener mCallback;

    // DataPassingListener interface, calls a method in the host activity named onStepSelected
    public interface DataPassingFromDetailListener {
        void onNavigationButtonClick(int id);
    }

    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the host activity has implemented the callback interface
        try {
            mCallback = (StepDetailFragment.DataPassingFromDetailListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement interface methods");
        }
    }

    // Mandatory empty constructor
    public StepDetailFragment() {
    }

    //Static factory method that takes an int parameter,initializes the fragment's arguments, and returns the
    // new fragment to the client.
    public static StepDetailFragment newInstance() {
        StepDetailFragment fragment = new StepDetailFragment();
//        Bundle args = new Bundle();
//        args.putInt("index", index);
//        fragment.setArguments(args);
        return fragment;
    }

    public void setStep(RecipeStep myStep) {
        mStep = myStep;
    }

    public void setStepsCount(int myStepsCount) {
        mStepsCount = myStepsCount;
    }

    // Inflates the step detail ui
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);
        //add SimpleExoPlayerView for the ExoPlayer
        mPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.player_view);

        if (mStep != null) {
            TextView stepsTitle = (TextView) rootView.findViewById(R.id.single_step_detail_title);
            if (rootView.findViewById(R.id.single_step_description) != null) {
                TextView stepsDescription = (TextView)
                        rootView.findViewById(R.id.single_step_description);
                stepsDescription.setText(mStep.getDescription());
            }
            String titleText = getString(R.string.single_step_title) + ": " +
                    mStep.getShortDescription();
            stepsTitle.setText(titleText);

            mVideoUrl = mStep.getVideoURL();
            mThumbnailUrl = mStep.getThumbnailURL();

            //if the thumbnail is available, then show the thumbnail image
            if (!TextUtils.isEmpty(mThumbnailUrl) && TextUtils.isEmpty(mVideoUrl)) {
                mTarget = new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        if (bitmap != null) {
                            mPlayerView.setDefaultArtwork(bitmap);
                        }
                    }
                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                    }
                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                };
                // load the image into the view using data object, with Picasso image tool
                Picasso.with(getActivity()).load(mThumbnailUrl).into(mTarget);
            } else if (!TextUtils.isEmpty(mVideoUrl)) {
                //start media when there is a video url available
                //initialize the Media Session, player needs media session so initialize
                //media session first
                initializeMediaSession();
                // Initialize the player.
                initializePlayer(Uri.parse(mVideoUrl));
                //reset player position
                mPlayerPosition = 0;
            } else {
                mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(),
                        R.drawable.player_no_video));
            }

            if ((rootView.findViewById(R.id.previous_button) != null) &&
                    (rootView.findViewById(R.id.next_button) != null)) {
                //Add the navigation button
                Button previousButton = (Button) rootView.findViewById(R.id.previous_button);
                Button nextButton = (Button) rootView.findViewById(R.id.next_button);
                if (mStep.getId() == 0) {
                    previousButton.setVisibility(View.INVISIBLE);
                }
                if (mStep.getId() == mStepsCount - 1) {
                    nextButton.setVisibility(View.INVISIBLE);
                }
                previousButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCallback.onNavigationButtonClick(mStep.getId() - 1);
                    }
                });
                nextButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCallback.onNavigationButtonClick(mStep.getId() + 1);
                    }
                });
            }
        }

        // Return the root view
        return rootView;
    }

    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the video to play.
     */
    private void initializePlayer(@NonNull Uri mediaUri) {
        if (mPlayer == null) {
            //Create a default TrackSelector
            // Measures bandwidth during playback. Can be null if not required.
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            // Create an instance of the ExoPlayer.
            LoadControl loadControl = new DefaultLoadControl();
            mPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            mPlayerView.requestFocus();
            //Connect the player view to the player
            mPlayerView.setPlayer(mPlayer);
            // Set the ExoPlayer.EventListener to this activity.
            mPlayer.addListener(this);

            // Prepare the player and MediaSource.
            // Produces DataSource instances through which media data is loaded.
            String userAgent = Util.getUserAgent(getActivity(), getString(R.string.app_name));
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(),
                    userAgent);
            // Produces Extractor instances for parsing the media data, specially for mp4 format.
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            // This is the MediaSource representing the media to be played.
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri,
                    dataSourceFactory, extractorsFactory, null, null);
            // Prepare the player with the source.
            mPlayer.prepare(mediaSource);
        }
    }

    //resume the player when the fragment is visible again from pause
    public void onResume(){
        super.onResume();
        if (mPlayer == null && !TextUtils.isEmpty(mVideoUrl)) {
            if (mPlayerPosition != 0) {
                mMediaSession.setActive(true);
                // Initialize the player.
                initializePlayer(Uri.parse(mVideoUrl));
                mPlayer.seekTo(mPlayerPosition);
            }
        }
        if (!TextUtils.isEmpty(mVideoUrl)) {
            // play the video
            mPlayer.setPlayWhenReady(true);
        }
    }

    //Release the player when the fragment is paused (stopped, and destroyed).
    //pause the player when the fragment is invisible
    @Override
    public void onPause(){
        super.onPause();
        if (!TextUtils.isEmpty(mThumbnailUrl) && TextUtils.isEmpty(mVideoUrl)) {
            Picasso.with(getActivity()).cancelRequest(mTarget);
        }
        if (mPlayer != null && !TextUtils.isEmpty(mVideoUrl)) {
            mPlayer.setPlayWhenReady(false);
            mPlayerPosition  = mPlayer.getCurrentPosition();
            releasePlayer();
        }
    }
//    @Override
//    public void onStop() {
//        super.onStop();
//    }
//    @Override
//    public void onDestroy(){
//        super.onDestroy();
//    }

    //Release ExoPlayer.
    private void releasePlayer() {
        mPlayer.stop();
        mPlayer.release();
        mPlayer = null;
        mMediaSession.setActive(false);
    }

    //Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
    //and media controller.
    private void initializeMediaSession() {
        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(getActivity(),
                StepDetailFragment.class.getSimpleName());
        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);
        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder().setActions(
                PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PAUSE |
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                        PlaybackStateCompat.ACTION_FAST_FORWARD|
                        PlaybackStateCompat.ACTION_REWIND |
                        PlaybackStateCompat.ACTION_PLAY_PAUSE );
        mMediaSession.setPlaybackState(mStateBuilder.build());
        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());
        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);
    }

    // Media Session Callbacks, where all external clients control the player.
    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mPlayer.setPlayWhenReady(true);
        }
        @Override
        public void onPause() {
            mPlayer.setPlayWhenReady(false);
        }
        @Override
        public void onRewind() {
            long position = mPlayer.getCurrentPosition();
            if (position > 1000) {
                mPlayer.seekTo(position - 1000);
            }
        }
        @Override
        public void onFastForward() {
            long position = mPlayer.getCurrentPosition();
            mPlayer.seekTo(position + 1000);
        }

        @Override
        public void onSkipToPrevious() {
            mPlayer.seekTo(0);
        }
    }

    //implement ExoPlayer Event Listeners
    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
    }
    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
    }
    @Override
    public void onLoadingChanged(boolean isLoading) {
    }
    /**
     * Method that is called when the ExoPlayer state changes. Used to update the MediaSession
     * PlayBackState to keep in sync, and post the media notification.
     * @param playWhenReady true if ExoPlayer is playing, false if it's paused.
     * @param playbackState int describing the state of ExoPlayer. Can be STATE_READY, STATE_IDLE,
     *                      STATE_BUFFERING, or STATE_ENDED.
     */
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (mPlayer != null) {
            if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
                mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                        mPlayer.getCurrentPosition(), 1f);
            } else if ((playbackState == ExoPlayer.STATE_READY)) {
                mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                        mPlayer.getCurrentPosition(), 1f);
            }
            mMediaSession.setPlaybackState(mStateBuilder.build());
        }
    }
    @Override
    public void onPlayerError(ExoPlaybackException error) {
    }
    @Override
    public void onPositionDiscontinuity() {
    }

    //Broadcast Receiver registered to receive the MEDIA_BUTTON intent coming from clients.
    public static class MediaReceiver extends BroadcastReceiver {
        public MediaReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mMediaSession, intent);
        }
    }
}
