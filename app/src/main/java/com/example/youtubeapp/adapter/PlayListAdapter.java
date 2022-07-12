package com.example.youtubeapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtubeapp.R;
import com.example.youtubeapp.model.itemrecycleview.PlayListItem;
import com.example.youtubeapp.model.itemrecycleview.VideoChannelItem;
import com.example.youtubeapp.my_interface.IItemOnClickPlayListListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PlayListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int VIEW_TYPE_ITEM = 0,
            VIEW_TYPE_LOADING = 1;
    private ArrayList<PlayListItem> listItems;
    private boolean isLoadingAdd;
    IItemOnClickPlayListListener playListListener;

    public PlayListAdapter(IItemOnClickPlayListListener playListListener) {
        this.playListListener = playListListener;
    }

    public void setData(ArrayList<PlayListItem> listItems) {
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
                    .inflate(R.layout.item_playlist_channel, parent, false);
            return new ItemPlayListViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_loading, parent, false);
            return new LoadingPagePlayListViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_ITEM) {
            PlayListItem item = listItems.get(position);
            if (item == null) {
                return;
            }
            ItemPlayListViewHolder viewHolder = (ItemPlayListViewHolder) holder;
            Picasso.get().load(item.getUrlThumbnailsVideo()).into(viewHolder.ivThumbnails);
            viewHolder.tvVideoCount.setText(item.getVideoCount() + " video");
            viewHolder.tvVideoCountI.setText(item.getVideoCount());
            viewHolder.tvTitleChannel.setText(item.getTitleChannel());
            viewHolder.tvTitleVideo.setText(item.getTitleVideo());
            String idPlayList = item.getIdPlayList();
            viewHolder.llOpenPlayList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playListListener.onCLickItemPlayList(item);
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

    class ItemPlayListViewHolder extends RecyclerView.ViewHolder {
        ImageView ivThumbnails;
        TextView tvTitleVideo, tvVideoCount, tvVideoCountI, tvTitleChannel;
        LinearLayout llOpenPlayList;
        public ItemPlayListViewHolder(@NonNull View itemView) {
            super(itemView);
            ivThumbnails = itemView.findViewById(R.id.iv_thumbnail_playlist);
            tvTitleVideo = itemView.findViewById(R.id.tv_title_playlist_channel);
            tvTitleChannel = itemView.findViewById(R.id.tv_title_channel_in_playlist);
            tvVideoCount = itemView.findViewById(R.id.tv_video_count_playlist);
            tvVideoCountI = itemView.findViewById(R.id.tv_item_count_playlist);
            llOpenPlayList = itemView.findViewById(R.id.ll_item_open_playlist);
        }
    }
    class LoadingPagePlayListViewHolder extends RecyclerView.ViewHolder {
        ProgressBar pbLoading;
        public LoadingPagePlayListViewHolder(@NonNull View itemView) {
            super(itemView);
            pbLoading = itemView.findViewById(R.id.pb_loading);
        }
    }

    public void addFooterLoading() {
        isLoadingAdd = true;
        listItems.add(new PlayListItem("", "", "", "", ""));
    }

    public void removeFooterLoading() {
        isLoadingAdd = false;

        int pos = listItems.size() - 1;
        PlayListItem item = listItems.get(pos);
        if (item != null) {
            listItems.remove(pos);
            notifyItemRemoved(pos);
        }

    }
}
