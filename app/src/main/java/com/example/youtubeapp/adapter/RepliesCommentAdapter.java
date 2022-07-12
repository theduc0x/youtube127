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
        if (replies == null) {
            return;
        }

        String authorLogoUrl = replies.getAuthorLogoUrl();
        String authorName = replies.getAuthorName();
        String publishedAt = replies.getPublishedAt();
        String updateAt = replies.getUpdateAt();
        String dateDiff = Util.getTime(publishedAt);
        String commentContent = replies.getTextDisplay();
        String likeCountCmt = replies.getLikeCount();

        // Đưa dữ đổ vào view
        Picasso.get().load(authorLogoUrl).into(civLogoAuthor);
        tvAuthorName.setText(authorName);
        tvCommentContent.setText(commentContent);
        tvDateDiff.setText(" • " + dateDiff);
        tvLikeCountCmt.setText(Util.convertViewCount(Double.parseDouble(likeCountCmt)));
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
            VIEW_TYPE_DESC = 2;;
    boolean isLoadingAdd;

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
        } else  {
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
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (listReplies != null && position == listReplies.size() - 1 && isLoadingAdd) {
            return VIEW_TYPE_LOADING;
        }
        return VIEW_TYPE_ITEM;
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
