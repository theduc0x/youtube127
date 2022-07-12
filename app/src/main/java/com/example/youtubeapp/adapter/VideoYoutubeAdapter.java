package com.example.youtubeapp.adapter;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtubeapp.R;
import com.example.youtubeapp.my_interface.IItemOnClickChannelListener;
import com.example.youtubeapp.utiliti.Util;
import com.example.youtubeapp.model.itemrecycleview.VideoItem;
import com.example.youtubeapp.my_interface.IItemOnClickVideoListener;
import com.squareup.picasso.Picasso;

import java.time.Duration;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class VideoYoutubeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<VideoItem> listItemVideo;
    private IItemOnClickVideoListener itemOnClickVideoListener;
    private IItemOnClickChannelListener onClickChannelListener;

    private final static int VIEW_TYPE_ITEM = 0,
            VIEW_TYPE_LOADING = 1;
    private VideoItem itemVideo;
    private boolean isLoadingAdd;

    public VideoYoutubeAdapter(IItemOnClickVideoListener itemOnClickVideoListener, IItemOnClickChannelListener onClickChannelListener) {
        this.itemOnClickVideoListener = itemOnClickVideoListener;
        this.onClickChannelListener = onClickChannelListener;
    }
    public void setData(ArrayList<VideoItem> listItemVideo) {
        this.listItemVideo = listItemVideo;
    }

    @Override
    public int getItemViewType(int position) {
        if (listItemVideo != null && position == listItemVideo.size() - 1 && isLoadingAdd) {
            return VIEW_TYPE_LOADING;
        }
        return VIEW_TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (VIEW_TYPE_ITEM == viewType) {
            View view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.item_video_home, parent, false);
            return new VideoViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_loading, parent, false);
            return new LoadingPageHomeViewHolder(view);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_ITEM) {
            VideoItem video = listItemVideo.get(position);
            if (video == null) {
                return ;
            }
            VideoViewHolder viewHolder = (VideoViewHolder) holder;
            String urlThumbnailVideo = video.getUrlImageItemVideo();
            String titleVideo = video.getTvTitleVideo();
            String titleChannel = video.getTvTitleChannel();

            String timeVideo = video.getPublishAt();
            // tính khoảng cách từ ngày public video đến nay
            String dateDayDiff = Util.getTime(timeVideo);
            String viewCountVideo = video.getViewCountVideo();
            String likeCount = video.getLikeCountVideo();
            String descVideo = video.getDescVideo();
            String idChannel = video.getIdChannel();
            String duration = video.getDuration();

//        callApiChannel(idChannel, video, position);
//        video.setUrlLogoChannel(urlLogooo);
            String urlLogoChannel;
            if (video.getUrlLogoChannel().equals("")) {
                urlLogoChannel = "https://st.quantrimang.com/photos/image/2020/07/30/Hinh-Nen-Trang-10.jpg";
            } else {
                urlLogoChannel = video.getUrlLogoChannel();
            }

            Picasso.get().load(urlThumbnailVideo).into(viewHolder.ivItemVideo);
            viewHolder.tvTitleVideo.setText(titleVideo);
            viewHolder.tvTitleChannel.setText(titleChannel);
            Picasso.get().load(urlLogoChannel).into(viewHolder.civLogoChannel);
            // chuyển thời gian từ json sang thời gian giống youtube
            Duration d = Duration.parse(duration);
            long s = d.getSeconds();
            String durationI = Util.changeDuration(s);
            viewHolder.tvDuration.setText(durationI);
            viewHolder.tvTimeVideo.setText(dateDayDiff);
            viewHolder.tvViewCountVideo.setText( "• "+ viewCountVideo + " views • ");
            String idVideo = video.getIdVideo();
            viewHolder.clItemClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemOnClickVideoListener.OnClickItemVideo(video);
                }
            });

            viewHolder.civLogoChannel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickChannelListener.onClickOpenChannel(idChannel, titleChannel);
                }
            });

        }
    }
    @Override
    public int getItemCount() {
        if (listItemVideo != null) {
            return listItemVideo.size();
        }
        return 0;
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivItemVideo, ivSettingVideo;
        CircleImageView civLogoChannel;
        TextView tvTitleVideo, tvTitleChannel, tvViewCountVideo, tvTimeVideo, tvDuration;
        ConstraintLayout clItemClick;


        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);

            ivItemVideo = itemView.findViewById(R.id.iv_item_video);
            ivSettingVideo = itemView.findViewById(R.id.iv_setting_video);
            civLogoChannel = itemView.findViewById(R.id.civ_logo_channel);
            tvTitleVideo = itemView.findViewById(R.id.tv_title_video);
            tvTitleChannel = itemView.findViewById(R.id.tv_title_channel);
            tvViewCountVideo = itemView.findViewById(R.id.tv_view_video);
            tvTimeVideo = itemView.findViewById(R.id.tv_time_video);
            clItemClick = itemView.findViewById(R.id.cl_item_click);
            tvDuration = itemView.findViewById(R.id.tv_duration);
        }
    }

    class LoadingPageHomeViewHolder extends RecyclerView.ViewHolder {
        ProgressBar pbLoading;
        public LoadingPageHomeViewHolder(@NonNull View itemView) {
            super(itemView);
            pbLoading = itemView.findViewById(R.id.pb_loading);
        }
    }

    public void addFooterLoading() {
        isLoadingAdd = true;
        listItemVideo.add(new VideoItem("", "", "",
                "", "", "", "", "",
                "", "", "", ""));
    }

    public void removeFooterLoading() {
        isLoadingAdd = false;

        int pos = listItemVideo.size() - 1;
        VideoItem item = listItemVideo.get(pos);
        if (item != null) {
            listItemVideo.remove(pos);
            notifyItemRemoved(pos);
        }

    }

}
