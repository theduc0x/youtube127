package com.example.youtubeapp.adapter;

import android.app.Activity;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtubeapp.R;
import com.example.youtubeapp.model.itemrecycleview.CommentItem;
import com.example.youtubeapp.model.itemrecycleview.VideoChannelItem;
import com.example.youtubeapp.utiliti.Util;
import com.example.youtubeapp.model.itemrecycleview.RepliesCommentItem;
import com.example.youtubeapp.my_interface.ILoadMore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

// View Holder khi loading
class LoadingViewHolder extends RecyclerView.ViewHolder {
    ProgressBar progressBar;

    public LoadingViewHolder(@NonNull View itemView) {
        super(itemView);
        progressBar = itemView.findViewById(R.id.pb_loading);
    }
}

// View Holder tiêu đề của view
class ItemInReplies extends RecyclerView.ViewHolder {
    TextView tvNameReceive, tvDateDiffReceive, tvEditor;
    TextView tvContentReceive, likeCountReceive, repliesCountReceive;
    CircleImageView civAuthorLogo;

    public ItemInReplies(@NonNull View itemView) {
        super(itemView);
        tvNameReceive = itemView.findViewById(R.id.tv_author_name_replies);
        tvDateDiffReceive = itemView.findViewById(R.id.tv_date_diff_replies);
        tvContentReceive = itemView.findViewById(R.id.tv_cmt_content_replies);
        likeCountReceive = itemView.findViewById(R.id.tv_like_count_cmt_replies);
        repliesCountReceive = itemView.findViewById(R.id.tv_replies_count_replies);
        tvEditor = itemView.findViewById(R.id.tv_editor_replies);
        civAuthorLogo = itemView.findViewById(R.id.civ_logo_author_replies);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setData(CommentItem itemR) {
        String publishAt = itemR.getPublishedAt();
        String updateAt = itemR.getUpdateAt();
        if (!publishAt.equals(updateAt)) {
            tvEditor.setVisibility(View.VISIBLE);
        } else {
            tvEditor.setVisibility(View.GONE);
        }
        Picasso.get().load(itemR.getAuthorLogoUrl()).into(civAuthorLogo);
        tvNameReceive.setText(itemR.getAuthorName());
        tvContentReceive.setText(itemR.getTextDisplay());
        tvDateDiffReceive.setText(" • " + Util.getTime(itemR.getPublishedAt()));
        String likeCount = itemR.getLikeCount();
        String totalReplies = itemR.getTotalReplyCount();
        if (likeCount.equals("0")) {
            likeCountReceive.setVisibility(View.GONE);
        } else {
            likeCountReceive.setVisibility(View.VISIBLE);
            likeCountReceive.setText(Util.convertViewCount(Double.parseDouble(likeCount)));
        }
        if (totalReplies.equals("0")) {
            repliesCountReceive.setVisibility(View.GONE);
        } else {
            repliesCountReceive.setVisibility(View.VISIBLE);
            repliesCountReceive.setText(Util.convertViewCount(Double.parseDouble(itemR.getTotalReplyCount())));
        }
    }
}


//View Holder khi loading xong
class RepliesViewHolder extends RecyclerView.ViewHolder {
    CircleImageView civLogoAuthor;
    TextView tvAuthorName, tvDateDiff, tvCommentContent,
            tvLikeCountCmt, tvEditor;
    ImageView ivMoreSelect;


    public RepliesViewHolder(@NonNull View itemView) {
        super(itemView);
        civLogoAuthor = itemView.findViewById(R.id.civ_logo_author_replies_item);
        tvAuthorName = itemView.findViewById(R.id.tv_author_name_replies_item);
        tvDateDiff = itemView.findViewById(R.id.tv_date_diff_replies_item);
        tvCommentContent = itemView.findViewById(R.id.tv_cmt_content_replies_item);
        tvLikeCountCmt = itemView.findViewById(R.id.tv_like_count_cmt_replies_item);
        tvEditor = itemView.findViewById(R.id.tv_editor_replies_item);
        ivMoreSelect = itemView.findViewById(R.id.iv_more_select_replies_item);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setData(RepliesCommentItem replies) {
        String dateDiff = "";
        if (replies == null) {
            return;
        }

        String authorLogoUrl = replies.getAuthorLogoUrl();
        String authorName = replies.getAuthorName();
        String publishedAt = replies.getPublishedAt();
        String updateAt = replies.getUpdateAt();
        if (publishedAt != null) {
            dateDiff  = Util.getTime(publishedAt);
        }
        String commentContent = replies.getTextDisplay();
        String likeCountCmt = replies.getLikeCount();

        // Đưa dữ đổ vào view
        Picasso.get().load(authorLogoUrl).into(civLogoAuthor);
        tvAuthorName.setText(authorName);
        tvCommentContent.setText(commentContent);
        tvDateDiff.setText(" • " + dateDiff);
        if (likeCountCmt.equals("0")) {
            tvLikeCountCmt.setVisibility(View.GONE);
        } else {
            tvLikeCountCmt.setVisibility(View.VISIBLE);
            tvLikeCountCmt.setText(Util.convertViewCount(Double.parseDouble(likeCountCmt)));
        }
        if (!publishedAt.equals(updateAt)) {
            tvEditor.setVisibility(View.VISIBLE);
        } else {
            tvEditor.setVisibility(View.GONE);
        }
    }
}

public class RepliesCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<RepliesCommentItem> listReplies;
    private final static int VIEW_TYPE_ITEM = 0,
            VIEW_TYPE_LOADING = 1,
            VIEW_TYPE_DESC = 2;
    ;
    boolean isLoadingAdd;
    private CommentItem items;

    public RepliesCommentAdapter(CommentItem items) {
        this.items = items;
    }

    public void setData(ArrayList<RepliesCommentItem> listReplies) {
        this.listReplies = listReplies;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_replies_comment_video, parent, false);
            return new RepliesViewHolder(view);
        } else if (viewType == VIEW_TYPE_DESC) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_in_bottom_sheet_replies, parent, false);
            return new ItemInReplies(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_ITEM) {
            RepliesCommentItem item = listReplies.get(position);

            RepliesViewHolder viewHolder = (RepliesViewHolder) holder;
            viewHolder.setData(item);
        } else if (holder.getItemViewType() == VIEW_TYPE_DESC) {
            ItemInReplies viewHolder = (ItemInReplies) holder;
            viewHolder.setData(items);
        } else {
            LoadingViewHolder viewHolder = (LoadingViewHolder) holder;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (listReplies != null && position == listReplies.size() - 1 && isLoadingAdd) {
            return VIEW_TYPE_LOADING;
        } else if (position == 0) {
            return VIEW_TYPE_DESC;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        if (listReplies != null) {
            return listReplies.size();
        }
        return 0;
    }

    public void addFooterLoading() {
        isLoadingAdd = true;
        listReplies.add(new RepliesCommentItem(""));
    }

    public void removeFooterLoading() {
        isLoadingAdd = false;

        int pos = listReplies.size() - 1;
        RepliesCommentItem item = listReplies.get(pos);
        if (item != null) {
            listReplies.remove(pos);
            notifyItemRemoved(pos);
        }
    }

}
