package com.example.youtubeapp.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.youtubeapp.R;
import com.example.youtubeapp.activitys.VideoPlayActivity;
import com.example.youtubeapp.adapter.CommentYoutubeAdapter;
import com.example.youtubeapp.adapter.RepliesCommentAdapter;
import com.example.youtubeapp.model.itemrecycleview.CommentItem;
import com.example.youtubeapp.model.itemrecycleview.RepliesCommentItem;
import com.example.youtubeapp.model.itemrecycleview.SearchItem;
import com.example.youtubeapp.model.listcomment.RepliesComment;
import com.example.youtubeapp.model.listreplies.ItemsR;
import com.example.youtubeapp.model.listreplies.Replies;
import com.example.youtubeapp.my_interface.IItemOnClickCommentListener;
import com.example.youtubeapp.my_interface.PaginationScrollListener;
import com.example.youtubeapp.utiliti.Util;
import com.example.youtubeapp.activitys.ChannelActivity;
import com.example.youtubeapp.api.ApiServicePlayList;
import com.example.youtubeapp.model.infochannel.Channel;
import com.example.youtubeapp.model.infochannel.Itemss;
import com.example.youtubeapp.model.itemrecycleview.VideoItem;
import com.example.youtubeapp.model.listcomment.Comment;
import com.example.youtubeapp.model.listcomment.ItemsComment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoContainDataFragment extends Fragment {

    RelativeLayout rlGroup, rlOpenChannel;
    LinearLayout llDisplayDesc, llCommentGroup;
    TextView tvTitleVideoPlay, tvViewVideoPlay, tvTimeVideoPlay, tvTurnOffComment;
    BottomNavigationView bnvOption;
    CircleImageView civLogoChannel, civLogoUser;
    TextView tvTitleChannelVideo, tvSubscription, tvCommentCount, tvCmtContent;
    AppCompatButton btSubscribe;
    // Biến dùng chung
    String idVideo, titleVideo, titleChannel;
    String viewCount;
    String timePublic;
    String likeCount, commentCount;
    String descVideo, idChannel,dateDayDiff;
    VideoItem itemVideo;
    SearchItem itemVideoS;
    // Bottom Sheet Desc
    ConstraintLayout clBSDesc;
    BottomSheetBehavior sheetBehaviorDesc;
    TextView tvTitleVideoDesc, tvViewCountVideoDesc, tvPublishAtDesc, tvDateYearVideoDesc;
    CircleImageView civLogoChannelDesc;
    TextView tvTitleChannelVideoDesc, tvLikeCountVideo, tvDesc;
    Toolbar tbCancel;
    // Bottom Sheet Comment
    ConstraintLayout clBSCmt;
    BottomSheetBehavior sheetBehaviorCmt;
    private TextView tvTotalCmtCount;
    private RecyclerView rvListComment;
    CommentYoutubeAdapter adapterCmt;
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

    // Bottom Sheet Replies
    ConstraintLayout clBSReplies;
    BottomSheetBehavior sheetBehaviorRep;
    ArrayList<RepliesCommentItem> listReplies;
    ArrayList<RepliesCommentItem> listAddR;
    ArrayList<RepliesCommentItem> listAddSR = new ArrayList<>();
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



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_contain_data, container, false);
        initView(view);
        setData();
        addFragmentRelatedVideo();
        // Mở thông tin channel
        rlOpenChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChannel();
            }
        });
        // Ib Back replies
        ibBackCmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetBehaviorRep.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        tbCommentVideo.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mn_close_comment:
                        sheetBehaviorCmt.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        break;
                    case R.id.mn_sort_comment:
                        popupMenu();
                        break;
                }
                return false;
            }
        });
        tbReplies.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mn_close_replies:
                        sheetBehaviorCmt.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        sheetBehaviorRep.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        break;
                }
                return false;
            }
        });


        return view;
    }
    // Api lấy thông tin channel, ở đây chỉ lấy ảnh của channel
    private void callApiChannel(String id) {
        ApiServicePlayList.apiServicePlayList.infoChannel(
                "snippet",
                "contentDetails",
                "statistics",
                id,
                Util.API_KEY
        ).enqueue(new Callback<Channel>() {
            @Override
            public void onResponse(Call<Channel> call, Response<Channel> response) {
//                Toast.makeText(VideoPlayActivity.this, "Call api", Toast.LENGTH_SHORT).show();
                Channel channel = response.body();
                String sub = "";
                if (channel != null) {
                    ArrayList<Itemss> listItem = channel.getItems();
                    Picasso.get().load(listItem.get(0).getSnippet().getThumbnails().getMedium().getUrl())
                            .into(civLogoChannel);
                    Picasso.get().load(listItem.get(0).getSnippet().getThumbnails().getMedium().getUrl())
                            .into(civLogoChannelDesc);
                    if (listItem.get(0).getStatistics().getSubscriberCount() == null) {
                        sub = "";
                        tvSubscription.setVisibility(View.INVISIBLE);
                        rlGroup.setVerticalGravity(Gravity.CENTER_VERTICAL);
                    } else {
                        sub = listItem.get(0).getStatistics().getSubscriberCount();
                        double subC = Double.parseDouble(sub);
                        sub = Util.convertViewCount(subC);
                        tvSubscription.setText(sub + " subcribers");
                    }
                }
            }

            @Override
            public void onFailure(Call<Channel> call, Throwable t) {
//                Toast.makeText(VideoPlayActivity.this,
//                        "Call Api Error", Toast.LENGTH_SHORT).show();
                Log.d("ab", t.toString());
            }
        });
    }
    // Gọi api lấy comment đầu tiên, nổi bật nhất của video
    private void callApiComment(String id) {
        ApiServicePlayList.apiServicePlayList.comment(
                "",
                "snippet",
                "replies",
                "relevance",
                "id",
                "plainText",
                id,
                Util.API_KEY,
                "100"
        ).enqueue(new Callback<Comment>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                String authorLogoUrl = "",
                        displayContentCmt = "";

                Comment comment = response.body();
                if (comment != null) {
                    ArrayList<ItemsComment> listItem = comment.getItems();
                    if (listItem.get(0) != null) {
                        authorLogoUrl = listItem.get(0).getSnippet()
                                .getTopLevelComment().getSnippet()
                                .getAuthorProfileImageUrl();

                        displayContentCmt = listItem.get(0).getSnippet()
                                .getTopLevelComment().getSnippet()
                                .getTextDisplay();

                        Picasso.get().load(authorLogoUrl).into(civLogoUser);
                        tvCmtContent.setText(displayContentCmt);
                    }
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
            }
        });
    }

    private void initView(View view) {
        tvTitleVideoPlay = view.findViewById(R.id.tv_title_video_play);
        tvViewVideoPlay =  view.findViewById(R.id.tv_view_video_play);
        tvTimeVideoPlay =  view.findViewById(R.id.tv_time_video_play);
        bnvOption =  view.findViewById(R.id.bnv_play_video_select);
        civLogoChannel =  view.findViewById(R.id.civ_image_logo_channel);
        tvTitleChannelVideo =  view.findViewById(R.id.tv_title_channel_video);
        tvSubscription =  view.findViewById(R.id.tv_subscription);
        btSubscribe =  view.findViewById(R.id.bt_subscribe);
        rlGroup =  view.findViewById(R.id.rl_group_title_and_sub);
        llDisplayDesc =  view.findViewById(R.id.ll_display_desc);
        llCommentGroup = view.findViewById(R.id.ll_comment_group);
        tvCommentCount = view.findViewById(R.id.tv_comment_count_video);
        civLogoUser = view.findViewById(R.id.civ_logo_channel_user);
        tvCmtContent = view.findViewById(R.id.tv_comment_video);
        rlOpenChannel = view.findViewById(R.id.rl_channel_click);
        tvTurnOffComment = view.findViewById(R.id.tv_comment_off);

        // BottomSheet Desc
        clBSDesc = view.findViewById(R.id.cl_bottom_sheet_desc);
        sheetBehaviorDesc = BottomSheetBehavior.from(clBSDesc);
        civLogoChannelDesc = view.findViewById(R.id.civ_logo_channel_desc);
        tvTitleVideoDesc = view.findViewById(R.id.tv_title_video_desc);
        tvTitleChannelVideoDesc = view.findViewById(R.id.tv_title_channel_desc);
        tvViewCountVideoDesc = view.findViewById(R.id.tv_view_video_desc);
        tvLikeCountVideo = view.findViewById(R.id.tv_like_video_desc);
        tvPublishAtDesc = view.findViewById(R.id.tv_date_dm_desc);
        tvDateYearVideoDesc = view.findViewById(R.id.tv_date_year_desc);
        tvDesc = view.findViewById(R.id.tv_display_video_desc);
        tbCancel = view.findViewById(R.id.tb_desc_video);

        // Bottom Sheet Comment
        clBSCmt = view.findViewById(R.id.cl_bottom_sheet_comment);
        sheetBehaviorCmt = BottomSheetBehavior.from(clBSCmt);
        tvTotalCmtCount = view.findViewById(R.id.tv_comment_count_video_main);
        rvListComment = view.findViewById(R.id.rv_list_comment);
        listCmtItem = new ArrayList<>();
        tbCommentVideo = view.findViewById(R.id.tb_comment_video);
        videoPlayActivity = (VideoPlayActivity) getActivity();
        listAddS.add(new CommentItem(""));

        // Bottom Sheet Replies
        clBSReplies = view.findViewById(R.id.fl_bottom_sheet_replies);
        sheetBehaviorRep = BottomSheetBehavior.from(clBSReplies);
        sheetBehaviorRep.setDraggable(false);
        listReplies = new ArrayList<>();
        rvListReplies = view.findViewById(R.id.rv_item_replies);
        tbReplies = view.findViewById(R.id.tb_replies_video);
        rlOpenKeyboard = view.findViewById(R.id.rl_open_replies_keyboard);
        ibBackCmt = view.findViewById(R.id.ib_back_comment);
        listAddSR.add(new RepliesCommentItem());
    }

    public void getTime(String dateOne) {
        String startDate = dateOne.substring(0, 10);

        String days = startDate.substring(8,10);
        String month = startDate.substring(5,7);
        String year = startDate.substring(0,4);

        tvPublishAtDesc.setText(days + " thg " + month);
        tvDateYearVideoDesc.setText(year);
    }

    private void setDataBSDesc() {
        tvTitleVideoDesc.setText(titleVideo);
        tvViewCountVideoDesc.setText(viewCount);
        tvLikeCountVideo.setText(likeCount);
        getTime(timePublic);
        tvTitleChannelVideoDesc.setText(titleChannel);
        tvDesc.setText(descVideo);

        tbCancel.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_close_desc) {
                    sheetBehaviorDesc.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                return false;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setData() {
        // Nhận dữ liệu bundle và set dữ liệu lên các view
        Bundle bundleReceive = getArguments();
        if (bundleReceive != null) {
            String key = bundleReceive.getString(Util.EXTRA_KEY_ITEM_VIDEO);
            if (key.equals("Search")) {
                itemVideoS =
                        (SearchItem) bundleReceive.getSerializable(Util.BUNDLE_EXTRA_OBJECT_ITEM_VIDEO);
                idVideo = itemVideoS.getIdVideo();
                titleVideo = itemVideoS.getTvTitleVideo();
                titleChannel = itemVideoS.getTitleChannel();
                viewCount = itemVideoS.getViewCountVideo();
                timePublic = itemVideoS.getPublishAt();
                dateDayDiff = Util.getTime(timePublic);
                likeCount = itemVideoS.getLikeCountVideo();
                descVideo = itemVideoS.getDescVideo();
                idChannel = itemVideoS.getIdChannel();
                commentCount = itemVideoS.getCommentCount();
            } else {
                itemVideo =
                        (VideoItem) bundleReceive.getSerializable(Util.BUNDLE_EXTRA_OBJECT_ITEM_VIDEO);
                idVideo = itemVideo.getIdVideo();
                titleVideo = itemVideo.getTvTitleVideo();
                titleChannel = itemVideo.getTvTitleChannel();
                viewCount = itemVideo.getViewCountVideo();
                timePublic = itemVideo.getPublishAt();
                dateDayDiff = Util.getTime(timePublic);
                likeCount = itemVideo.getLikeCountVideo();
                descVideo = itemVideo.getDescVideo();
                idChannel = itemVideo.getIdChannel();
                commentCount = itemVideo.getCommentCount();
            }

        }
        // call api lấy logo channel
        callApiChannel(idChannel);
        callApiComment(idVideo);
        // set dữ liệu lên description
        setDataBSDesc();
        // Set dữ liệu lên view
        tvTitleVideoPlay.setText(titleVideo);
        tvTitleChannelVideo.setText(titleChannel);
        tvViewVideoPlay.setText(viewCount + " views • ");
        tvTimeVideoPlay.setText(dateDayDiff);
//        tvCommentCount.setText(Util.convertViewCount(Double.parseDouble(commentCount)));
        tvCommentCount.setText(Util.convertViewCount(Double.parseDouble(commentCount)));
        if (likeCount == null) {
            likeCount = "";
            bnvOption.getMenu().findItem(R.id.mn_like).setTitle("Like");
        } else {
            likeCount = Util.convertViewCount(Double.parseDouble(likeCount));
            bnvOption.getMenu().findItem(R.id.mn_like).setTitle(likeCount);
        }

        // Hiển thi dialog Fragment của Description
        llDisplayDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sheetBehaviorDesc.getState() != BottomSheetBehavior.STATE_EXPANDED){
                    sheetBehaviorDesc.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    sheetBehaviorDesc.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

        });
        if (commentCount.equals("0")) {
            llCommentGroup.setVisibility(View.GONE);
            tvTurnOffComment.setVisibility(View.VISIBLE);
        } else {
            llCommentGroup.setVisibility(View.VISIBLE);
            tvTurnOffComment.setVisibility(View.GONE);

            llCommentGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(sheetBehaviorCmt.getState() != BottomSheetBehavior.STATE_EXPANDED){
                        resetData();
                        setDataComment();
                        LoadPage = 1;
                        sheetBehaviorCmt.setState(BottomSheetBehavior.STATE_EXPANDED);
                    } else {
                        sheetBehaviorCmt.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                }

            });
        }
    }

    // Add thêm phần video liên quan
    private void addFragmentRelatedVideo() {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        RelatedToVideoFragment relatedToVideoFragment = new RelatedToVideoFragment();
        Bundle bundleReceive = getArguments();
        relatedToVideoFragment.setArguments(bundleReceive);

        transaction.add(R.id.fl_related_video, relatedToVideoFragment);
        transaction.commit();
    }
    private void openChannel() {

        Intent openToChannel = new Intent(getActivity(), ChannelActivity.class);
        openToChannel.putExtra(Util.EXTRA_ID_CHANNEL_TO_CHANNEL, idChannel);
        openToChannel.putExtra(Util.EXTRA_TITLE_CHANNEL_TO_CHANNEL, titleChannel);
        startActivity(openToChannel);
    }
//  Bottom Sheet Comment
    private void setDataComment() {
        int cmtCountD = Integer.valueOf(commentCount);
        if (cmtCountD % 10 != 0) {
            totalPage = (cmtCountD / 10) + 1;
        } else {
            totalPage = (cmtCountD / 10);
        }

        tvTotalCmtCount.setText(Util.convertViewCount(Double.parseDouble(commentCount)));
        if (idVideo == null) {
            return;
        }

        // Sự kiện click hiển thị list replies
        adapterCmt = new CommentYoutubeAdapter( new IItemOnClickCommentListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClickItemComment(CommentItem commentItem) {
                if(sheetBehaviorRep.getState() != BottomSheetBehavior.STATE_EXPANDED){
                    resetDataR();
                    setDataRep(commentItem);
                    sheetBehaviorRep.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    sheetBehaviorRep.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

        });

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        RecyclerView.ItemDecoration decoration =
                new DividerItemDecoration(getActivity(), RecyclerView.VERTICAL);
        rvListComment.setLayoutManager(linearLayoutManager);
        rvListComment.addItemDecoration(decoration);
        rvListComment.setNestedScrollingEnabled(false);
        rvListComment.setAdapter(adapterCmt);

        setFirstDataR();

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

    private void setFirstDataR() {
        listCmtItem = null;
        callApiComment(idVideo, pageToken, "relevance", "10");
    }


    // Set propress bar load data
    private void setProgressBar() {
        if (currenPage < totalPage) {
            adapterCmt.addFooterLoading();
        } else {
            isLastPage = true;
        }
    }
    // Load dữ liệu của page tiếp theo
    private void loadNextPage() {
                if (LoadPage == 1) {
                    callApiComment(idVideo, pageToken, "relevance", "10");
                    isLoading = false;
                } else if (LoadPage == 2) {
                    callApiComment(idVideo, pageToken, "time", "10");
                    isLoading = false;
                }
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
                        adapterCmt.setData(listCmtItem);
                    } else {
                        adapterCmt.removeFooterLoading();
                        listCmtItem.addAll(listAdd);
                        adapterCmt.notifyDataSetChanged();
                    }
                    setProgressBar();

                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
            }
        });
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
                        callApiComment(idVideo, "", "relevance", "10");
//                        adapter.setData(listCmtItem);
                        break;
                    case R.id.mn_new_first:
                        resetData();
                        LoadPage = 2;
                        callApiComment(idVideo, "", "time", "10");
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
        pageToken = "";
        listAddS = new ArrayList<>();
        listAddS.add(new CommentItem(""));
    }

    // Bottom Sheet Replies

    private void resetDataR() {
        currentPageR = 1;
        listReplies = null;
        pageTokenR = "";
        listAddSR = new ArrayList<>();
        listAddSR.add(new RepliesCommentItem());
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setDataRep(CommentItem itemR) {
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
                loadNextPageR();
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
    private void loadNextPageR() {
                callApiReplies(pageTokenR, parentId, "10");
                isLoadingR = false;
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
                        Log.d("asfdafsf", likeCount);
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