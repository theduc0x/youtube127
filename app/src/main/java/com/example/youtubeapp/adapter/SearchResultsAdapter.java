package com.example.youtubeapp.adapter;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtubeapp.R;
import com.example.youtubeapp.model.itemrecycleview.SearchItem;
import com.example.youtubeapp.my_interface.IItemOnClickChannelListener;
import com.example.youtubeapp.my_interface.IItemOnClickPlayListSearchListener;
import com.example.youtubeapp.my_interface.IItemOnClickVideoSearchListener;
import com.example.youtubeapp.utiliti.Util;
import com.squareup.picasso.Picasso;

import java.time.Duration;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchResultsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static int
            VIEW_TYPE_ITEM_CHANNEL = 0,
            VIEW_TYPE_ITEM_PLAYLIST = 1,
            VIEW_TYPE_ITEM_VIDEO = 2,
            VIEW_TYPE_LOADING = 3;
    private boolean isLoadingAdd;
    IItemOnClickChannelListener onClickChannelListener;
    IItemOnClickPlayListSearchListener onCLickItemPlayListS;
    IItemOnClickVideoSearchListener onClickVideoSearchListener;

    ArrayList<SearchItem> listSearch;

    public SearchResultsAdapter(IItemOnClickChannelListener onClickChannelListener,
                                IItemOnClickPlayListSearchListener onCLickItemPlayListS,
                                IItemOnClickVideoSearchListener onClickVideoSearchListener) {
        this.onClickChannelListener = onClickChannelListener;
        this.onCLickItemPlayListS = onCLickItemPlayListS;
        this.onClickVideoSearchListener = onClickVideoSearchListener;
    }

    public void setData(ArrayList<SearchItem> listSearch) {
        this.listSearch = listSearch;
    }

    @Override
    public int getItemViewType(int position) {
        if (listSearch != null && position == listSearch.size() - 1 && isLoadingAdd) {
            return VIEW_TYPE_LOADING;
        } else if (listSearch.get(position).getKindType().equals("youtube#channel")) {
            return VIEW_TYPE_ITEM_CHANNEL;
        } else if (listSearch.get(position).getKindType().equals("youtube#playlist")) {
            return VIEW_TYPE_ITEM_PLAYLIST;
        }
        return VIEW_TYPE_ITEM_VIDEO;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (VIEW_TYPE_ITEM_VIDEO == viewType) {
            View view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.item_video_home, parent, false);
            return new ItemVideoViewHolder(view);
        } else if (VIEW_TYPE_ITEM_PLAYLIST == viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_playlist_search_results, parent, false);
            return new ItemPlayListViewHolder(view);
        } else if (VIEW_TYPE_ITEM_CHANNEL == viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_channels_list, parent, false);
            return new ItemChannelViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_loading, parent, false);
            return new LoadingPageSearchViewHolder(view);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_ITEM_CHANNEL) {
            SearchItem item = listSearch.get(position);
            if (item == null) {
                return;
            }
            ItemChannelViewHolder viewHolder = (ItemChannelViewHolder) holder;
            viewHolder.setData(item);
        } else if (holder.getItemViewType() == VIEW_TYPE_ITEM_VIDEO) {
            SearchItem item = listSearch.get(position);
            if (item == null) {
                return;
            }
            ItemVideoViewHolder viewHolder = (ItemVideoViewHolder) holder;
            viewHolder.setData(item);
        } else if (holder.getItemViewType() == VIEW_TYPE_ITEM_PLAYLIST) {
            SearchItem item = listSearch.get(position);
            if (item == null) {
                return;
            }
            ItemPlayListViewHolder viewHolder = (ItemPlayListViewHolder) holder;
            viewHolder.setData(item);
        }
    }

    @Override
    public int getItemCount() {
        if (listSearch != null) {
            return listSearch.size();
        }
        return 0;
    }

    // Loading Page Progress Bar
    class LoadingPageSearchViewHolder extends RecyclerView.ViewHolder {
        ProgressBar pbLoading;

        public LoadingPageSearchViewHolder(@NonNull View itemView) {
            super(itemView);
            pbLoading = itemView.findViewById(R.id.pb_loading);
        }
    }

    // ViewHolder của Channel
    class ItemChannelViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView civLogoChannel;
        private TextView tvTitleChannel, tvSubCount, tvVideoCount, tvSub;
        private LinearLayout llOpenChannel;

        public ItemChannelViewHolder(@NonNull View itemView) {
            super(itemView);
            civLogoChannel = itemView.findViewById(R.id.civ_logo_channel_list);
            tvTitleChannel = itemView.findViewById(R.id.tv_title_channel_list);
            tvSubCount = itemView.findViewById(R.id.tv_sub_count_channel_list);
            tvVideoCount = itemView.findViewById(R.id.tv_video_count_channel_list);
            tvSub = itemView.findViewById(R.id.tv_subscribe_channel_list);
            llOpenChannel = itemView.findViewById(R.id.ll_open_channel_list);
        }

        public void setData(SearchItem item) {
            String urlLogoChannel = item.getUrlLogoChannel();
            String titleChannel = item.getTitleChannel();
            String idChannel = item.getIdChannel();
//            callApiChannelFull(idChannel, item, pos);

            String videoCount = item.getVideoCount();
            if (!item.getSubCount().equals("")) {
                tvSubCount.setText(Util.convertViewCount(Double.parseDouble(item.getSubCount())));
            }
            Picasso.get().load(urlLogoChannel).into(civLogoChannel);
            tvTitleChannel.setText(titleChannel);
            if (!videoCount.equals("")) {
                tvVideoCount.setText(Util.convertViewCount(Double.parseDouble(videoCount)));
            }
            llOpenChannel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickChannelListener.onClickOpenChannel(idChannel, item.getTitleChannel());
                }
            });
        }
    }

    // ViewHolder của Video
    class ItemVideoViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivItemVideo, ivSettingVideo;
        CircleImageView civLogoChannel;
        TextView tvTitleVideo, tvTitleChannel, tvViewCountVideo, tvTimeVideo, tvDuration;
        ConstraintLayout clItemClick;


        public ItemVideoViewHolder(@NonNull View itemView) {
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

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void setData(SearchItem video) {
            String urlThumbnailVideo = video.getUrlThumbnailsVideo();
            String titleVideo = video.getTvTitleVideo();
            String titleChannel = video.getTitleChannel();

            String timeVideo = video.getPublishAt();
            // tính khoảng cách từ ngày public video đến nay
            String dateDayDiff = Util.getTime(timeVideo);
            String viewCountVideo = video.getViewCountVideo();
            String likeCount = video.getLikeCountVideo();
            String descVideo = video.getDescVideo();
            String idChannel = video.getIdChannel();
            String idVideo = video.getIdVideo();
            String duration = video.getDuration();

            Log.d("idVideo", idVideo);
            Log.d("idChannel", idChannel);
            String urlLogoChannel;
            if (video.getUrlLogoChannel().equals("")) {
                urlLogoChannel = "https://st.quantrimang.com/photos/image/2020/07/30/Hinh-Nen-Trang-10.jpg";
            } else {
                urlLogoChannel = video.getUrlLogoChannel();
            }
            if (!duration.equals("")) {
                Duration d = Duration.parse(duration);
                long s = d.getSeconds();
                String durationI = Util.changeDuration(s);
                tvDuration.setText(durationI);
            }
            Picasso.get().load(urlThumbnailVideo).into(ivItemVideo);
            tvTitleVideo.setText(titleVideo);
            tvTitleChannel.setText(titleChannel);
            Picasso.get().load(urlLogoChannel).into(civLogoChannel);

            tvTimeVideo.setText(dateDayDiff);
            if (!viewCountVideo.equals("")) {
                viewCountVideo = Util.convertViewCount(Double.parseDouble(viewCountVideo));
                tvViewCountVideo.setText("• " + viewCountVideo + " views • ");
            }


            clItemClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickVideoSearchListener.OnClickItemVideoS(video);
                }
            });

            civLogoChannel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickChannelListener.onClickOpenChannel(idChannel, titleChannel);
                }
            });
        }
    }

    // ViewHolder của playlist
    class ItemPlayListViewHolder extends RecyclerView.ViewHolder {
        ImageView ivThumbnails;
        TextView tvTitleVideo, tvVideoCount, tvVideoCountI, tvTitleChannel;
        LinearLayout llOpenPlayList;

        public ItemPlayListViewHolder(@NonNull View itemView) {
            super(itemView);
            ivThumbnails = itemView.findViewById(R.id.iv_item_video_playlist_search);
            tvTitleVideo = itemView.findViewById(R.id.tv_title_playlist_search);
            tvTitleChannel = itemView.findViewById(R.id.tv_title_channel_in_playlist_search);
            tvVideoCount = itemView.findViewById(R.id.tv_video_count_playlist_search);
            tvVideoCountI = itemView.findViewById(R.id.tv_item_count_playlist_search);
            llOpenPlayList = itemView.findViewById(R.id.ll_item_open_playlist_search);
        }

        public void setData(SearchItem item) {
            Picasso.get().load(item.getUrlThumbnailsVideo()).into(ivThumbnails);
            tvVideoCount.setText(item.getVideoCountPlayList() + " video");
            tvVideoCountI.setText(item.getVideoCountPlayList());
            tvTitleChannel.setText(item.getTitleChannel());
            tvTitleVideo.setText(item.getTvTitleVideo());
            String idPlayList = item.getIdPlayList();
            llOpenPlayList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCLickItemPlayListS.onCLickItemPlayListS(
                            item
                    );
                }
            });
        }
    }

    public void addFooterLoading() {
        isLoadingAdd = true;
        listSearch.add(new SearchItem("", "", "",
                "", "", "", "", "",
                "", "", "", "", "",
                "", "", "", ""));
    }

    public void removeFooterLoading() {
        isLoadingAdd = false;

        int pos = listSearch.size() - 1;
        SearchItem item = listSearch.get(pos);
        if (item != null) {
            listSearch.remove(pos);
            notifyItemRemoved(pos);
        }
    }

//    // Api thông tin channel;
//    public void callApiChannelFull(String idChannel1, SearchItem itemCh, int pos) {
//        ApiServicePlayList.apiServicePlayList.infoChannelFull(
//                "contentDetails",
//                "snippet",
//                "statistics",
//                "topicDetails",
//                "brandingSettings",
//                idChannel1,
//                Util.API_KEY
//        ).enqueue(new Callback<Channel>() {
//            @Override
//            public void onResponse(Call<Channel> call, Response<Channel> response) {
//                String urlBanner = "", titleChannel = "", subCount = "",
//                        videoCount = "", urlLogoChannel = "", introduce = "", idChannelList = "";
//                boolean isCheckHideSub = false;
//                Itemss item = null;
//                Channel channel = response.body();
//                if (channel != null) {
//                    ArrayList<Itemss> listItem = channel.getItems();
//                    item = listItem.get(0);
//                    titleChannel = item.getSnippet().getTitle();
//                    isCheckHideSub = item.getStatistics().isHiddenSubscriberCount();
//                    if (isCheckHideSub) {
//                        subCount = "";
//                    } else {
//                        subCount = item.getStatistics().getSubscriberCount();
//                    }
//                    videoCount = item.getStatistics().getVideoCount();
//                    urlLogoChannel = item.getSnippet().getThumbnails().getHigh().getUrl();
//                    introduce = item.getSnippet().getDescription();
//                    idChannelList = item.getId();
//                    Log.d("duc3", titleChannel);
//
//                    itemCh.setSubCount(subCount);
//                    itemCh.setVideoCount(videoCount);
//
//                    notifyItemChanged(pos);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Channel> call, Throwable t) {
//                Log.d("ducak", t.toString());
//            }
//        });
//    }

//    private void callApiDetailVideo(String idVideo) {
//        ApiServicePlayList.apiServicePlayList.detailVideo(
//                "snippet",
//                "statistics",
//                idVideo,
//                Util.API_KEY
//        ).enqueue(new Callback<DetailVideo>() {
//            @Override
//            public void onResponse(Call<DetailVideo> call, Response<DetailVideo> response) {
//                String urlThumbnailVideo = "", titleVideo = "", titleChannel = "",
//                        publishAt = "", viewCountVideo = "", commentCount = "",
//                        idVideo = "", likeCountVideo = "", descVideo = "",
//                        idChannel = "", urlLogoChannel = "";
//
//                DetailVideo detailVideo = response.body();
//                if (detailVideo != null) {
//                    ArrayList<ItemVideo> listItem = detailVideo.getItems();
//
//                    urlThumbnailVideo = listItem.get(0)
//                            .getSnippet().getThumbnails()
//                            .getHigh().getUrl();
//
//                    viewCountVideo = listItem.get(0).getStatistics().getViewCount();
//                    titleVideo = listItem.get(0).getSnippet().getTitle();
//                    titleChannel = listItem.get(0).getSnippet().getChannelTitle();
//                    idChannel = listItem.get(0).getSnippet().getChannelId();
//
//                    idVideo = listItem.get(0).getId();
//                    urlLogoChannel = "";
//                    publishAt = listItem.get(0).getSnippet().getPublishedAt();
////                        String dateDayDiff = Util.getTime(timeVideo);
//
//                    viewCountVideo = listItem.get(0).getStatistics().getViewCount();
//                    double viewCount = Double.parseDouble(viewCountVideo);
//                    viewCountVideo = Util.convertViewCount(viewCount);
//
//
//                    likeCountVideo = listItem.get(0).getStatistics().getLikeCount();
//                    descVideo = listItem.get(0).getSnippet().getDescription();
//                    commentCount = listItem.get(0).getStatistics().getCommentCount();
//
////                    listSearch = new SearchItem(urlThumbnailVideo, urlLogoChannel, titleVideo, publishAt,
////                            titleChannel, viewCountVideo, idVideo, likeCountVideo, descVideo,
////                            idChannel, commentCount);
//                }
////                onClickVideoListener.OnClickItemVideo(itemVideo);
//            }
//
//            @Override
//            public void onFailure(Call<DetailVideo> call, Throwable t) {
//
//            }
//        });
//    }
}
