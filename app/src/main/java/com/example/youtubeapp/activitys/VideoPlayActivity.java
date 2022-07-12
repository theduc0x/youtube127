package com.example.youtubeapp.activitys;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.example.youtubeapp.R;
import com.example.youtubeapp.model.itemrecycleview.SearchItem;
import com.example.youtubeapp.utiliti.Util;
import com.example.youtubeapp.fragment.VideoContainDataFragment;
import com.example.youtubeapp.fragment.VideoContainYoutubePlayFragment;
import com.example.youtubeapp.model.itemrecycleview.VideoItem;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;


public class VideoPlayActivity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener {
    YouTubePlayerView ypvVideo;
    String idVideo;
    VideoItem itemVideo;
    SearchItem itemVideoS;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        addFragmentMain();
//        addFragmentRelated();

        YouTubePlayerFragment youTubePlayerFragment = (YouTubePlayerFragment) getFragmentManager()
                .findFragmentById(R.id.fm_youtube_play_view);
        youTubePlayerFragment.initialize(Util.API_KEY, this);

    }
//    public void goToDetailRepliesFragment(CommentItem item) {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        DetailRepliesFragment repliesFragment = new DetailRepliesFragment();
//
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(Util.BUNDLE_EXTRA_ITEM_VIDEO_TO_REPLIES, item);
//        repliesFragment.setArguments(bundle);
//        transaction.replace(R.id.fl_content_data, repliesFragment);
//        transaction.addToBackStack("abc123");
//        transaction.commit();
//    }
    // add fragment chứa dữ liệu như desc, comment, video liên quan,
    private void addFragmentMain() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        VideoContainDataFragment videoContainDataFragment = new VideoContainDataFragment();
        Intent getVideoInfo = getIntent();
        Bundle bundle = getVideoInfo.getExtras();
        if (bundle != null) {
            String key = bundle.getString(Util.EXTRA_KEY_ITEM_VIDEO);
            if (key.equals("Search")) {
                itemVideoS =
                        (SearchItem) bundle.getSerializable(Util.BUNDLE_EXTRA_OBJECT_ITEM_VIDEO);
                idVideo = itemVideoS.getIdVideo();
            } else {
                itemVideo =
                        (VideoItem) bundle.getSerializable(Util.BUNDLE_EXTRA_OBJECT_ITEM_VIDEO);
                idVideo = itemVideo.getIdVideo();
            }

        }
        videoContainDataFragment.setArguments(bundle);
        transaction.replace(R.id.fl_content_data, videoContainDataFragment);

        transaction.commit();
    }

    private void addFragmentRelated() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        VideoContainDataFragment videoContainDataFragment = new VideoContainDataFragment();
        Intent getVideoInfo = getIntent();
        Bundle bundle = getVideoInfo.getExtras();
        if (bundle != null) {
            itemVideo =
                    (VideoItem) bundle.getSerializable(Util.BUNDLE_EXTRA_OBJECT_ITEM_VIDEO_FROM_RELATED);
            idVideo = itemVideo.getIdVideo();
        }
        videoContainDataFragment.setArguments(bundle);
        transaction.replace(R.id.fl_content_data, videoContainDataFragment);

        transaction.commit();
    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer youTubePlayer, boolean b) {
        YouTubePlayer.PlayerStyle playerStyle = YouTubePlayer.PlayerStyle.DEFAULT;
        if(!b) {
            youTubePlayer.setPlayerStyle(playerStyle);
            youTubePlayer.loadVideo(idVideo);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult youTubeInitializationResult) {
        final int REQUEST_CODE = 1;

        if(youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this,REQUEST_CODE).show();
        } else {
            String errorMessage =
                    String.format("There was an error initializing the YoutubePlayer (%1$s)",
                            youTubeInitializationResult.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }
}