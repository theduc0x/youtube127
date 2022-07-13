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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
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

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomSheetDialogRepliesFragment extends BottomSheetDialogFragment {
    ArrayList<RepliesCommentItem> listReplies;
    ArrayList<RepliesCommentItem> listAddR;
    ArrayList<RepliesCommentItem> listAddSR = new ArrayList<>();
    CommentItem itemR;
    RelativeLayout rlOpenKeyboard;
    RecyclerView rvListReplies;
    RepliesCommentAdapter adapterReplies;
    Toolbar tbReplies;
    String parentId;
    ImageButton ibBackCmt;

    private String pageTokenR = "";
    private boolean isLoadingR;
    private boolean isLastPageR;
    private int totalPageR = 5;
    private int currentPageR = 1;


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
        setDataRep();

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
        rvListReplies = view.findViewById(R.id.rv_item_replies);
        tbReplies = view.findViewById(R.id.tb_replies_video);
        rlOpenKeyboard = view.findViewById(R.id.rl_open_replies_keyboard);
        ibBackCmt = view.findViewById(R.id.ib_back_comment);
        listAddSR.add(new RepliesCommentItem());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setDataRep() {
        if (itemR == null) {
            return;
        }
        parentId = itemR.getIdComment();

        int cmtCountD = Integer.valueOf(itemR.getTotalReplyCount());
        if (cmtCountD % 10 != 0) {
            totalPageR = (cmtCountD / 10) + 1;
        } else {
            totalPageR = (cmtCountD / 10);
        }

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvListReplies.setLayoutManager(linearLayoutManager);
        adapterReplies = new RepliesCommentAdapter(itemR);
        rvListReplies.setAdapter(adapterReplies);
        rvListReplies.setNestedScrollingEnabled(false);

        setFirstDataPo();

        rvListReplies.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            public void loadMoreItem() {
                isLoadingR = true;
                currentPageR += 1;
                loadNextPage();
            }
            @Override
            public boolean isLoading() {
                return isLoadingR;
            }
            @Override
            public boolean isLastPage() {
                return isLastPageR;
            }
        });

    }

    private void setFirstDataPo() {
        listReplies = null;
        callApiReplies(pageTokenR, parentId, "10");
    }


    // Set propress bar load data
    private void setProgressBarR() {
        if (currentPageR < totalPageR) {
            adapterReplies.addFooterLoading();
        } else {
            isLastPageR = true;
        }
    }
    // Load dữ liệu của page tiếp theo
    private void loadNextPage() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    callApiReplies(pageTokenR, parentId, "10");
                    isLoadingR = false;
            }
        },1000);
    }


    public void callApiReplies(String nextPageToken, String parentId, String maxResults) {
        listAddR = new ArrayList<>();
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
                    pageTokenR = replies.getNextPageToken();
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

                        listAddR.add(new RepliesCommentItem(
                                displayContentCmt, authorName, authorLogoUrl,
                                authorIdChannel, likeCount, publishAt, updateAt
                        ));

                        }
                    if (listReplies == null) {
                        listReplies = listAddSR;
                        listReplies.addAll(listAddR);
                        adapterReplies.setData(listReplies);
                    } else {
                        adapterReplies.removeFooterLoading();
                        listReplies.addAll(listAddR);
                        adapterReplies.notifyDataSetChanged();
                    }
                    setProgressBarR();
                }

            }

            @Override
            public void onFailure(Call<Replies> call, Throwable t) {

            }
        });
    }

}
