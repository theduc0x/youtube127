package com.example.youtubeapp.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtubeapp.R;
import com.example.youtubeapp.my_interface.PaginationScrollListener;
import com.example.youtubeapp.utiliti.Util;
import com.example.youtubeapp.activitys.VideoPlayActivity;
import com.example.youtubeapp.adapter.CommentYoutubeAdapter;
import com.example.youtubeapp.api.ApiServicePlayList;
import com.example.youtubeapp.model.itemrecycleview.CommentItem;
import com.example.youtubeapp.model.listcomment.Comment;
import com.example.youtubeapp.model.listcomment.ItemsComment;
import com.example.youtubeapp.model.listcomment.RepliesComment;
import com.example.youtubeapp.my_interface.IItemOnClickCommentListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomSheetDialogCommentFragment extends BottomSheetDialogFragment {
    public static String idVideoM = "", cmtCount = "";
    private TextView tvTotalCmtCount;
    private RecyclerView rvListComment;
    CommentYoutubeAdapter adapter;
    Toolbar tbCommentVideo;
    ProgressDialog progressDialog;
    VideoPlayActivity videoPlayActivity;


    ArrayList<CommentItem> listCmtItem;
    ArrayList<CommentItem> listAdd;
    ArrayList<CommentItem> listAddS = new ArrayList<>();
    private int LoadPage = 1;
    private String pageToken = "";
    private boolean isLoading;
    private boolean isLastPage;
    private int totalPage = 5;
    private int currenPage = 1;


    // Khởi tạo fragment dialog với dữ liệu truyền vào là 1 CommentItem
    public static BottomSheetDialogCommentFragment newInstance(String idVideo, String cmtCounts) {
        BottomSheetDialogCommentFragment bsdCommentFragment =
                new BottomSheetDialogCommentFragment();

//            idVideoM = idVideo;
//            cmtCount = cmtCounts;
        // Khởi tạo dialog fragment và gửi dữ liệu đi tới bước sau bằng bundle
        Bundle bundle = new Bundle();
        bundle.putString(Util.BUNDLE_EXTRA_ID_VIDEO, idVideo);
        bundle.putString(Util.BUNDLE_EXTRA_CMT_COUNT_VIDEO, cmtCounts);
        bsdCommentFragment.setArguments(bundle);
        return bsdCommentFragment;
    }

    // Khi khởi tạo thì bắt đầu nhận
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundleReceive = getArguments();
        if (bundleReceive != null) {
            idVideoM = bundleReceive.getString(Util.BUNDLE_EXTRA_ID_VIDEO);
            cmtCount = bundleReceive.getString(Util.BUNDLE_EXTRA_CMT_COUNT_VIDEO);
        }
    }

    // Chuyển fagment thành dialog
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog =
                (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View viewDialog = LayoutInflater.from(getContext()).inflate(
                R.layout.fragment_bottom_sheet_comment, null);
        bottomSheetDialog.setContentView(viewDialog);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
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
        setDataComment();

        bottomSheetDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        bottomSheetDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        tbCommentVideo.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mn_close_comment:
                        bottomSheetDialog.dismiss();
                        break;
                    case R.id.mn_sort_comment:
                        popupMenu();
                        break;
                }
                return false;
            }
        });
        return bottomSheetDialog;
    }

    private void intMain(View view) {
        tvTotalCmtCount = view.findViewById(R.id.tv_comment_count_video_main);
        rvListComment = view.findViewById(R.id.rv_list_comment);
        listCmtItem = new ArrayList<>();
        tbCommentVideo = view.findViewById(R.id.tb_comment_video);
        videoPlayActivity = (VideoPlayActivity) getActivity();
        listAddS.add(new CommentItem(""));
    }

    private void setDataComment() {
        int cmtCountD = Integer.valueOf(cmtCount);
        if (cmtCountD % 10 != 0) {
            totalPage = (cmtCountD / 10) + 1;
        } else {
            totalPage = (cmtCountD / 10);
        }

        tvTotalCmtCount.setText(Util.convertViewCount(Double.parseDouble(cmtCount)));
        if (idVideoM == null) {
            return;
        }

        // Sự kiện click hiển thị list replies
        adapter = new CommentYoutubeAdapter( new IItemOnClickCommentListener() {
            @Override
            public void onClickItemComment(CommentItem commentItem) {
//                videoPlayActivity.goToDetailRepliesFragment(commentItem);
                clickOpenReplies(commentItem);
            }

        });

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        RecyclerView.ItemDecoration decoration =
                new DividerItemDecoration(getActivity(), RecyclerView.VERTICAL);
        rvListComment.setLayoutManager(linearLayoutManager);
        rvListComment.addItemDecoration(decoration);
        rvListComment.setNestedScrollingEnabled(false);
        rvListComment.setAdapter(adapter);

        setFirstDataPo();

        rvListComment.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
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
        listCmtItem = null;
        callApiComment(idVideoM, pageToken, "relevance", "10");
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
                if (LoadPage == 1) {
                    callApiComment(idVideoM, pageToken, "relevance", "10");
                    isLoading = false;
                } else if (LoadPage == 2) {
                    callApiComment(idVideoM, pageToken, "time", "10");
                    isLoading = false;
                }
            }
        },1000);
    }

    private void callApiComment(String id, String nextPageToken, String order, String maxResults) {
        listAdd = new ArrayList<>();
        ApiServicePlayList.apiServicePlayList.comment(
                nextPageToken,
                "snippet",
                "replies",
                order,
                "id",
                "plainText",
                id,
                Util.API_KEY,
                maxResults
        ).enqueue(new Callback<Comment>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                String authorLogoUrl = "", authorName = "", publishAt = "", updateAt = "",
                        displayContentCmt = "", dateDiff = "", authorIdChannel = "", idComment = "";
                int likeCount = 0, totalRepliesCount = 0;
                RepliesComment repliesComment;

                Comment comment = response.body();
                if (comment != null) {

                    pageToken = comment.getNextPageToken();
                    ArrayList<ItemsComment> listItem = comment.getItems();
                    for (int i = 0; i < listItem.size(); i++) {
                        idComment = listItem.get(i).getId();
                        authorLogoUrl = listItem.get(i).getSnippet()
                                .getTopLevelComment().getSnippet()
                                .getAuthorProfileImageUrl();
                        authorName = listItem.get(i).getSnippet()
                                .getTopLevelComment().getSnippet()
                                .getAuthorDisplayName();
                        publishAt = listItem.get(i).getSnippet()
                                .getTopLevelComment().getSnippet()
                                .getPublishedAt();
                        updateAt = listItem.get(i).getSnippet()
                                .getTopLevelComment().getSnippet()
                                .getUpdatedAt();
                        if (listItem.get(i).getSnippet()
                                .getTopLevelComment().getSnippet()
                                .getAuthorChannelId() == null) {
                            authorIdChannel = "";
                        } else {
                            authorIdChannel = listItem.get(i).getSnippet()
                                    .getTopLevelComment().getSnippet()
                                    .getAuthorChannelId().getValue();
                        }
                        displayContentCmt = listItem.get(i).getSnippet()
                                .getTopLevelComment().getSnippet()
                                .getTextDisplay();
                        likeCount = listItem.get(i).getSnippet()
                                .getTopLevelComment().getSnippet()
                                .getLikeCount();
                        totalRepliesCount = listItem.get(i).getSnippet().getTotalReplyCount();
                        repliesComment = listItem.get(i).getReplies();


                        // Thêm vào list

                        listAdd.add(new CommentItem(
                                idComment, displayContentCmt, authorName, authorLogoUrl,
                                authorIdChannel, String.valueOf(likeCount), publishAt,
                                updateAt, String.valueOf(totalRepliesCount), repliesComment));
                    }
                    if (listCmtItem == null) {
                        listCmtItem = listAddS;
//                        listCmtItem = listAdd;
                        listCmtItem.addAll(listAdd);
                        adapter.setData(listCmtItem);
                    } else {
                        adapter.removeFooterLoading();
                        listCmtItem.addAll(listAdd);
                        adapter.notifyDataSetChanged();
                    }
                    setProgressBar();

                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
            }
        });
    }

    private void clickOpenReplies(CommentItem commentItem) {
        BottomSheetDialogRepliesFragment dialog =
                BottomSheetDialogRepliesFragment.newInstance(commentItem);
        dialog.show(getChildFragmentManager(), dialog.getTag());
    }

    private void popupMenu() {
        PopupMenu popupMenu = new PopupMenu(getActivity(), tbCommentVideo, Gravity.RIGHT);
        popupMenu.getMenuInflater().inflate(R.menu.menu_sort_comment,
                popupMenu.getMenu());
        // Gọi lại 1 hàm setData trong adapter
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()) {

                    case R.id.mn_top_cmt:
                        resetData();
                        LoadPage = 1;
                        callApiComment(idVideoM, "", "relevance", "10");
//                        adapter.setData(listCmtItem);
                        break;
                    case R.id.mn_new_first:
                        resetData();
                        LoadPage = 2;
                        callApiComment(idVideoM, "", "time", "10");
//                        adapter.setData(listCmtItem);
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }
    private void resetData() {
        currenPage = 1;
        listCmtItem = null;
        listAddS = new ArrayList<>();
        listAddS.add(new CommentItem(""));
    }

}
