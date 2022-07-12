package com.example.youtubeapp.adapter;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtubeapp.R;
import com.example.youtubeapp.utiliti.Util;
import com.example.youtubeapp.api.ApiServicePlayList;
import com.example.youtubeapp.model.detailvideo.DetailVideo;
import com.example.youtubeapp.model.detailvideo.ItemVideo;
import com.example.youtubeapp.model.itemrecycleview.VideoChannelItem;
import com.example.youtubeapp.model.itemrecycleview.VideoItem;
import com.example.youtubeapp.my_interface.IItemOnClickVideoListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoChannelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static int VIEW_TYPE_ITEM = 0,
                            VIEW_TYPE_LOADING = 1;
    private ArrayList<VideoChannelItem> listItems;
    private IItemOnClickVideoListener onClickVideoListener;
    private VideoItem itemVideo;
    private boolean isLoadingAdd;

    public VideoChannelAdapter(IItemOnClickVideoListener onClickVideoListener) {
        this.onClickVideoListener = onClickVideoListener;
    }

    public void setData(ArrayList<VideoChannelItem> listItems) {
        this.listItems = listItems;
        notifyDataSetChanged();

    }

    @Override
    public int getItemViewType(int position) {
        if (listItems != null && position == listItems.size() - 1 && isLoadingAdd) {
            return VIEW_TYPE_LOADING;
        }
        return VIEW_TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (VIEW_TYPE_ITEM == viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_video_in_channel, parent, false);
            return new ItemVideoViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_loading, parent, false);
            return new LoadingPageViewHolder(view);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_ITEM) {
            VideoChannelItem item = listItems.get(position);

            ItemVideoViewHolder itemVideoViewHolder = (ItemVideoViewHolder) holder;


                if (item.getTitleVideo().equals("")) {
                    itemVideoViewHolder.tvTitleVideo.setText("");
                } else {
                    itemVideoViewHolder.tvTitleVideo.setText(item.getTitleVideo());
                }

                if (item.getViewCount().equals("")) {
                    itemVideoViewHolder.tvViewCount.setText("0");
                } else {
                    itemVideoViewHolder.tvViewCount
                            .setText(Util.convertViewCount(Double.parseDouble(item.getViewCount())));
                }
//                if (item.getPublishAt().equals("")) {
//                    itemVideoViewHolder.tvTimeDiff.setText("");
//                } else {
                    itemVideoViewHolder.tvTimeDiff.setText(Util.getTime(item.getPublishAt()));
//                }

                if (item.getUrlThumbnails().equals("")) {
                    Picasso.get().load("https://i.ytimg.com/vi/1jIdw2mQDp4/maxresdefault.jpg")
                            .into(itemVideoViewHolder.ivThumbnails);
                } else {
                    Picasso.get().load(item.getUrlThumbnails()).into(itemVideoViewHolder.ivThumbnails);
                }



                itemVideoViewHolder.llOpenVideo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callApiDetailVideo(item.getIdVideo());
                    }
                });

        }
    }

    @Override
    public int getItemCount() {
        if (listItems != null) {
            return listItems.size();
        }
        return 0;
    }

    class ItemVideoViewHolder extends RecyclerView.ViewHolder {
        ImageView ivThumbnails;
        TextView tvTitleVideo, tvViewCount, tvTimeDiff;
        LinearLayout llOpenVideo;
        public ItemVideoViewHolder(@NonNull View itemView) {
            super(itemView);
            ivThumbnails = itemView.findViewById(R.id.iv_thumbnail_video);
            tvTitleVideo = itemView.findViewById(R.id.tv_title_video_channel);
            tvViewCount = itemView.findViewById(R.id.tv_view_count_video_channel);
            tvTimeDiff = itemView.findViewById(R.id.tv_time_diff_date);
            llOpenVideo = itemView.findViewById(R.id.ll_open_video_from_video_channel);
        }
    }
    class LoadingPageViewHolder extends RecyclerView.ViewHolder {
        ProgressBar pbLoading;
        public LoadingPageViewHolder(@NonNull View itemView) {
            super(itemView);
            pbLoading = itemView.findViewById(R.id.pb_loading);
        }
    }

    public void addFooterLoading() {
            isLoadingAdd = true;
            listItems.add(new VideoChannelItem("", "", "", "", ""));
    }

    public void removeFooterLoading() {
        isLoadingAdd = false;

        int pos = listItems.size() - 1;
        VideoChannelItem item = listItems.get(pos);
        if (item != null) {
            listItems.remove(pos);
            notifyItemRemoved(pos);
        }

    }

    private void callApiDetailVideo(String idVideo) {
        ApiServicePlayList.apiServicePlayList.detailVideo(
                "snippet",
                "statistics",
                "contentDetails",
                idVideo,
                Util.API_KEY
        ).enqueue(new Callback<DetailVideo>() {
            @Override
            public void onResponse(Call<DetailVideo> call, Response<DetailVideo> response) {
                String urlThumbnailVideo = "", titleVideo = "", titleChannel = "",
                        publishAt = "", viewCountVideo = "", commentCount = "",
                        idVideo = "", likeCountVideo = "", descVideo = "",
                        idChannel = "", urlLogoChannel = "", duration = "";

                DetailVideo detailVideo = response.body();
                if (detailVideo != null) {
                    ArrayList<ItemVideo> listItem = detailVideo.getItems();

                        urlThumbnailVideo = listItem.get(0)
                                .getSnippet().getThumbnails()
                                .getHigh().getUrl();

                    viewCountVideo = listItem.get(0).getStatistics().getViewCount();
                    titleVideo = listItem.get(0).getSnippet().getTitle();
                    titleChannel = listItem.get(0).getSnippet().getChannelTitle();
                    idChannel = listItem.get(0).getSnippet().getChannelId();

                    idVideo = listItem.get(0).getId();
                    urlLogoChannel = "";
                    publishAt = listItem.get(0).getSnippet().getPublishedAt();
//                        String dateDayDiff = Util.getTime(timeVideo);

                    viewCountVideo = listItem.get(0).getStatistics().getViewCount();
                    double viewCount = Double.parseDouble(viewCountVideo);
                    viewCountVideo = Util.convertViewCount(viewCount);


                    likeCountVideo = listItem.get(0).getStatistics().getLikeCount();
                    descVideo = listItem.get(0).getSnippet().getDescription();
                    commentCount = listItem.get(0).getStatistics().getCommentCount();
                    duration = listItem.get(0).getContentDetails().getDuration();

                    itemVideo = new VideoItem(urlThumbnailVideo, urlLogoChannel, titleVideo, publishAt,
                            titleChannel, viewCountVideo, idVideo, likeCountVideo, descVideo,
                            idChannel, commentCount, duration);
                }
                onClickVideoListener.OnClickItemVideo(itemVideo);
            }

            @Override
            public void onFailure(Call<DetailVideo> call, Throwable t) {

            }
        });
    }
}
