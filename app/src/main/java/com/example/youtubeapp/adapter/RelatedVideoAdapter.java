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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtubeapp.R;
import com.example.youtubeapp.utiliti.Util;
import com.example.youtubeapp.api.ApiServicePlayList;
import com.example.youtubeapp.model.detailvideo.DetailVideo;
import com.example.youtubeapp.model.detailvideo.ItemVideo;
import com.example.youtubeapp.model.itemrecycleview.VideoItem;
import com.example.youtubeapp.my_interface.IItemOnClickVideoListener;
import com.example.youtubeapp.my_interface.ILoadMore;
import com.squareup.picasso.Picasso;

import java.time.Duration;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class Loading2ViewHolder extends RecyclerView.ViewHolder {
    public ProgressBar progressBar;
    public Loading2ViewHolder(@NonNull View itemView) {
        super(itemView);
        progressBar = itemView.findViewById(R.id.pb_loading);
    }
}

class ItemViewHolder extends RecyclerView.ViewHolder {
    ImageView ivItemVideo, ivSettingVideo;
    CircleImageView civLogoChannel;
    TextView tvTitleVideo, tvTitleChannel, tvViewCountVideo, tvTimeVideo;
    ConstraintLayout clItemClick;
    TextView tvDuration;

    public ItemViewHolder(@NonNull View itemView) {
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

public class RelatedVideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0, VIEW_TYPE_LOADING = 1;
    ILoadMore loadMore;
    boolean isLoading;
    ArrayList<VideoItem> items;
    int visibleThreshold = 5;
    int lastVisibleItem, totalItemCount;
    private IItemOnClickVideoListener itemOnClickVideoListener;

    public RelatedVideoAdapter(RecyclerView recyclerView, ArrayList<VideoItem> items, IItemOnClickVideoListener itemOnClickVideoListener) {
        this.items = items;
        this.itemOnClickVideoListener = itemOnClickVideoListener;

        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (loadMore != null) {
                        loadMore.onLoadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void setLoadMore(ILoadMore loadMore) {
        this.loadMore = loadMore;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_video_home, parent, false);
            return new ItemViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_loading, parent, false);
            return new Loading2ViewHolder(view);
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            VideoItem item = items.get(position);
            if (item == null) {
                return;
            }
            ItemViewHolder viewHolder = (ItemViewHolder) holder;

            String urlThumbnailVideo = item.getUrlImageItemVideo();
            String titleVideo = item.getTvTitleVideo();
            String titleChannel = item.getTvTitleChannel();

            String timeVideo = item.getPublishAt();
            // tính khoảng cách từ ngày public video đến nay
            String dateDayDiff = Util.getTime(timeVideo);
            String viewCountVideo = item.getViewCountVideo();
            String likeCount = item.getLikeCountVideo();
            String descVideo = item.getDescVideo();
            String idChannel = item.getIdChannel();
            String idVideo = item.getIdVideo();
            String urlLogoChannel;
            if (item.getUrlLogoChannel().equals("")) {
                urlLogoChannel = "https://st.quantrimang.com/photos/image/2020/07/30/Hinh-Nen-Trang-10.jpg";
            } else {
                urlLogoChannel = item.getUrlLogoChannel();
            }
            callApiDetailVideo(idVideo, viewHolder, item);

            Picasso.get().load(urlThumbnailVideo).into(viewHolder.ivItemVideo);
            viewHolder.tvTitleVideo.setText(titleVideo);
            viewHolder.tvTitleChannel.setText(titleChannel);
            Picasso.get().load(urlLogoChannel).into(viewHolder.civLogoChannel);
            viewHolder.tvTimeVideo.setText(dateDayDiff);

            viewHolder.clItemClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemOnClickVideoListener.OnClickItemVideo(item);
                }
            });


        } else if (holder instanceof Loading2ViewHolder) {
            Loading2ViewHolder loading2ViewHolder = (Loading2ViewHolder) holder;
            loading2ViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setLoaded() {
        isLoading = false;
    }

    private void callApiDetailVideo(String idVideo, ItemViewHolder holder, VideoItem item) {
        ApiServicePlayList.apiServicePlayList.detailVideo(
                "snippet",
                "statistics",
                "contentDetails",
                idVideo,
                Util.API_KEY
        ).enqueue(new Callback<DetailVideo>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<DetailVideo> call, Response<DetailVideo> response) {
                String duration = "",
                        timeVideo = "", viewCountVideo = "", commentCount = "", likeCountVideo = "";

                DetailVideo detailVideo = response.body();
                if (detailVideo != null) {
                    ArrayList<ItemVideo> listItem = detailVideo.getItems();

                    viewCountVideo = listItem.get(0).getStatistics().getViewCount();
                    duration = listItem.get(0).getContentDetails().getDuration();
                    item.setDuration(duration);
                    Duration d = Duration.parse(duration);
                    String durationI = Util.changeDuration(d.getSeconds());
                    holder.tvDuration.setText(durationI);

                    item.setViewCountVideo(Util.convertViewCount(Double.parseDouble(viewCountVideo)));
                    holder.tvViewCountVideo
                            .setText("• " + Util.convertViewCount(Double.parseDouble(viewCountVideo)) + " views •");
                    commentCount = listItem.get(0).getStatistics().getCommentCount();
                    item.setCommentCount(commentCount);

                    likeCountVideo = listItem.get(0).getStatistics().getLikeCount();
                    item.setLikeCountVideo(likeCountVideo);

                }
            }

            @Override
            public void onFailure(Call<DetailVideo> call, Throwable t) {

            }
        });

    }
}
