package com.example.youtubeapp.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtubeapp.R;
import com.example.youtubeapp.fragment.ShortsFragment;
import com.example.youtubeapp.model.itemrecycleview.VideoItem;
import com.example.youtubeapp.utiliti.Util;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.PlayerUiController;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShortsVideoAdapter extends RecyclerView.Adapter<ShortsVideoAdapter.ShortsViewHolder> {
    ArrayList<VideoItem> listItems;
    String idVideo;
    Context context;
    int id ;

    public ShortsVideoAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<VideoItem> listItems) {

        this.listItems = listItems;
    }
    @NonNull
    @Override
    public ShortsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video_shorts, parent, false);
        id = View.generateViewId();
        return new ShortsViewHolder(view);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull ShortsViewHolder holder, int position) {
        Random random = new Random(18291);
        VideoItem item = listItems.get(position);
        if (item == null) {
            return;
        }
        holder.setData(item);
//        holder.flShorts.setVisibility(View.VISIBLE);
//        final YouTubePlayerFragment youTubePlayerFragment = YouTubePlayerFragment.newInstance();
//
//        ((AppCompatActivity) context).getFragmentManager().beginTransaction().replace(
//               holder.flShorts.getId(), youTubePlayerFragment).addToBackStack(null).commit();
//        youTubePlayerFragment.initialize(Util.API_KEY, new YouTubePlayer.OnInitializedListener() {
//            @Override
//            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
//                YouTubePlayer.PlayerStyle playerStyle = YouTubePlayer.PlayerStyle.DEFAULT;
//                if (!b) {
//                    youTubePlayer.setPlayerStyle(playerStyle);
//                    youTubePlayer.loadVideo(idVideo);
//                }
//            }
//            {
//            }
//
//            @Override
//            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        if (listItems != null) {
            return listItems.size();
        }
        return 0;
    }

    class ShortsViewHolder extends RecyclerView.ViewHolder{

//        YouTubePlayerFragment youTubePlayerFragment;
        FrameLayout flShorts;
        YouTubePlayerView ypvShort;
        TextView tvLike, tvCmtCount, tvTitleChannel, tvDesc;
        CircleImageView civLogoChannel;
        ProgressBar pbLoading;
        public ShortsViewHolder(@NonNull View itemView) {
            super(itemView);
//            youTubePlayerFragment =
//                    (YouTubePlayerFragment) ((AppCompatActivity) context)
//                            .getFragmentManager().findFragmentById(R.id.fm_shorts);
            ypvShort = itemView.findViewById(R.id.ypv_shorts);
            tvLike = itemView.findViewById(R.id.tv_like_count_shorts);
            tvCmtCount = itemView.findViewById(R.id.tv_comment_count_shorts);
            tvTitleChannel = itemView.findViewById(R.id.tv_title_channel_shorts);
            tvDesc = itemView.findViewById(R.id.tv_desc_shorts);
            civLogoChannel = itemView.findViewById(R.id.civ_logo_channel_shorts);
//            pbLoading = itemView.findViewById(R.id.pb_loading_shorts);
        }
        public void setData(VideoItem item) {
            String urlLogoChannel = item.getUrlLogoChannel();
            String titleChannel = item.getTvTitleChannel();
            String titleVideo = item.getTvTitleVideo();
//            String likeCount = Util.convertViewCount(Double.parseDouble(item.getLikeCountVideo()));
//            String cmtCount = Util.convertViewCount(Double.parseDouble(item.getCommentCount()));
            idVideo = item.getIdVideo();
            String idChannel = item.getIdChannel();

            tvDesc.setText(titleVideo);
            tvCmtCount.setText("cmtCount");
            tvTitleChannel.setText(titleChannel);
            tvLike.setText("likeCount");
            Picasso.get().load(urlLogoChannel).into(civLogoChannel);
//            youTubePlayerFragment.initialize(Util.API_KEY, new YouTubePlayer.OnInitializedListener() {
//                @Override
//                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
//                    YouTubePlayer.PlayerStyle playerStyle = YouTubePlayer.PlayerStyle.CHROMELESS;
//                    if (!b) {
//                        youTubePlayer.setPlayerStyle(playerStyle);
//                        youTubePlayer.loadVideo(idVideo);
//                    }
//                }
//
//                @Override
//                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
//
//                }
//            });
            ypvShort.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                    super.onReady(youTubePlayer);
                    youTubePlayer.loadVideo(idVideo, 0);
                }
            });
        }


    }

}
