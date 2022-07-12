package com.example.youtubeapp.fragment;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtubeapp.R;
import com.example.youtubeapp.my_interface.PaginationScrollListener;
import com.example.youtubeapp.utiliti.Util;
import com.example.youtubeapp.adapter.RepliesCommentAdapter;
import com.example.youtubeapp.api.ApiServicePlayList;
import com.example.youtubeapp.model.itemrecycleview.CommentItem;
import com.example.youtubeapp.model.itemrecycleview.RepliesCommentItem;
import com.example.youtubeapp.model.listreplies.ItemsR;
import com.example.youtubeapp.model.listreplies.Replies;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomSheetDialogRepliesFragment extends BottomSheetDialogFragment {
    public static ArrayList<RepliesCommentItem> listReplies;
    public static ArrayList<RepliesCommentItem> listAdd;
    CommentItem itemR;
    RelativeLayout rlOpenKeyboard;
    CircleImageView civReceive;
    TextView tvNameReceive, tvDateDiffReceive, tvEditor;
    TextView tvContentReceive, likeCountReceive, repliesCountReceive;
    RecyclerView rvListReplies;
    RepliesCommentAdapter adapter;
    Toolbar tbReplies;
    String parentId;
    ImageButton ibBackCmt;


    private String pageToken = "";
    private boolean isLoading;
    private boolean isLastPage;
    private int totalPage = 5;
    private int currenPage = 1;


    // Khởi tạo fragment dialog với dữ liệu truyền vào là 1 CommentItem
    public static BottomSheetDialogRepliesFragment newInstance(CommentItem item) {
        BottomSheetDialogRepliesFragment bsdRepliesFragment =
                new BottomSheetDialogRepliesFragment();

        // Khởi tạo dialog fragment và gửi dữ liệu đi tới bước sau bằng bundle
        Bundle bundle = new Bundle();
        bundle.putSerializable(Util.BUNDLE_EXTRA_ITEM_VIDEO_TO_REPLIES_INSIDE, item);
        bsdRepliesFragment.setArguments(bundle);
        return bsdRepliesFragment;
    }

    // Khi khởi tạo thì bắt đầu nhận
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundleReceive = getArguments();
        if (bundleReceive != null) {
            itemR = (CommentItem) bundleReceive
                    .getSerializable(Util.BUNDLE_EXTRA_ITEM_VIDEO_TO_REPLIES_INSIDE);
        }
    }

    // Chuyển fagment thành dialog
    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog =
                (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View viewDialog = LayoutInflater.from(getContext()).inflate(
                R.layout.fragment_bottom_sheet_detail_replies, null);
        bottomSheetDialog.setContentView(viewDialog);
//        bottomSheetDialog.setCanceledOnTouchOutside(false);
        // Xoay khi đang chờ load comment
//        progressDialog = new ProgressDialog(getContext());
//        progressDialog.setMessage("Loading...");

        // chiều cao của màn hình activiy chứa fragment
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        int maxHeight = (int) (height * 0.65);

        BottomSheetBehavior bottomSheetBehavior =
                BottomSheetBehavior.from(((View) viewDialog.getParent()));
        bottomSheetBehavior.setMaxHeight(maxHeight);
        bottomSheetBehavior.setPeekHeight(maxHeight);

        // Ánh xạ view và chạy RecycleVIew
        intMain(viewDialog);
        setData();

        ibBackCmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        bottomSheetDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        tbReplies.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mn_close_replies:
                        bottomSheetDialog.dismiss();

                        break;
                }
                return false;
            }
        });
        return bottomSheetDialog;
    }

    private void intMain(View view) {
        listReplies = new ArrayList<>();
        civReceive = view.findViewById(R.id.civ_logo_author_replies);
        tvNameReceive = view.findViewById(R.id.tv_author_name_replies);
        tvDateDiffReceive = view.findViewById(R.id.tv_date_diff_replies);
        tvContentReceive = view.findViewById(R.id.tv_cmt_content_replies);
        likeCountReceive = view.findViewById(R.id.tv_like_count_cmt_replies);
        repliesCountReceive = view.findViewById(R.id.tv_replies_count_replies);
        rvListReplies = view.findViewById(R.id.rv_item_replies);
        tbReplies = view.findViewById(R.id.tb_replies_video);
        rlOpenKeyboard = view.findViewById(R.id.rl_open_replies_keyboard);
        tvEditor = view.findViewById(R.id.tv_editor_replies);
        ibBackCmt = view.findViewById(R.id.ib_back_comment);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setData() {
        if (itemR == null) {
            return;
        }
        String publishAt = itemR.getPublishedAt();
        String updateAt = itemR.getUpdateAt();
        parentId = itemR.getIdComment();

        Picasso.get().load(itemR.getAuthorLogoUrl()).into(civReceive);
        tvNameReceive.setText(itemR.getAuthorName());
        tvContentReceive.setText(itemR.getTextDisplay());
        tvDateDiffReceive.setText(" • " + Util.getTime(itemR.getPublishedAt()));
        likeCountReceive.setText(Util.convertViewCount(Double.parseDouble(itemR.getLikeCount())));
        repliesCountReceive.setText(Util.convertViewCount(Double.parseDouble(itemR.getTotalReplyCount())));

        int cmtCountD = Integer.valueOf(itemR.getTotalReplyCount());
        if (cmtCountD % 10 != 0) {
            totalPage = (cmtCountD / 10) + 1;
        } else {
            totalPage = (cmtCountD / 10);
        }

        if (publishAt.equals(updateAt)) {
            tvEditor.setVisibility(View.VISIBLE);
        }
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        RecyclerView.ItemDecoration decoration =
                new DividerItemDecoration(getActivity(), RecyclerView.VERTICAL);

        rvListReplies.setLayoutManager(linearLayoutManager);

        adapter = new RepliesCommentAdapter();
        rvListReplies.addItemDecoration(decoration);
        rvListReplies.setAdapter(adapter);

        setFirstDataPo();

        rvListReplies.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            public void loadMoreItem() {
                isLoading = true;
                currenPage += 1;
                loadNextPage();
            }
            @Override
            public boolean isLoading() {
                return isLoading;
            }
            @Override
            public boolean isLastPage() {
                return isLastPage;
            }
        });

    }

    private void setFirstDataPo() {
        listReplies = null;
        callApiReplies( pageToken, parentId, "10");
    }


    // Set propress bar load data
    private void setProgressBar() {
        if (currenPage < totalPage) {
            adapter.addFooterLoading();
        } else {
            isLastPage = true;
        }
    }
    // Load dữ liệu của page tiếp theo
    private void loadNextPage() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    callApiReplies( pageToken, parentId, "10");
                    isLoading = false;
            }
        },1000);
    }


    public void callApiReplies(String nextPageToken, String parentId, String maxResults) {
        listAdd = new ArrayList<>();
        ApiServicePlayList.apiServicePlayList.replies(
                nextPageToken,
                "snippet",
                maxResults,
                parentId,
                "plainText",
                Util.API_KEY
        ).enqueue(new Callback<Replies>() {

            @Override
            public void onResponse(Call<Replies> call, Response<Replies> response) {
                Replies replies = null;
                String authorLogoUrl = "", authorName = "", publishAt = "", updateAt = "",
                        displayContentCmt = "", authorIdChannel = "";
                String likeCount = "";
                replies = response.body();
                if (replies != null) {
                    pageToken = replies.getNextPageToken();
                    ArrayList<ItemsR> listItem = replies.getItems();
                    for (int i = 0; i < listItem.size(); i++) {
                        authorLogoUrl = listItem.get(i).getSnippet().getAuthorProfileImageUrl();
                        authorName = listItem.get(i).getSnippet()
                                .getAuthorDisplayName();
                        publishAt = listItem.get(i).getSnippet()
                                .getPublishedAt();
                        updateAt = listItem.get(i).getSnippet()
                                .getUpdatedAt();
                        authorIdChannel = listItem.get(i).getSnippet()
                                .getAuthorChannelId().getValue();
                        displayContentCmt = listItem.get(i).getSnippet()
                                .getTextDisplay();
                        likeCount = String.valueOf(listItem.get(i).getSnippet()
                                .getLikeCount());

                        listAdd.add(new RepliesCommentItem(
                                displayContentCmt, authorName, authorLogoUrl,
                                authorIdChannel, likeCount, publishAt, updateAt
                        ));

                        }
                    if (listReplies == null) {
                        listReplies = listAdd;
                        adapter.setData(listReplies);
                    } else {
                        adapter.removeFooterLoading();
                        listReplies.addAll(listAdd);
                        adapter.notifyDataSetChanged();
                    }
                    setProgressBar();
                }

            }

            @Override
            public void onFailure(Call<Replies> call, Throwable t) {

            }
        });
    }

}
