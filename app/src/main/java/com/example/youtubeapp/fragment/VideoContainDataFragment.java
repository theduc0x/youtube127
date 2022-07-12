package com.example.youtubeapp.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.youtubeapp.R;
import com.example.youtubeapp.model.itemrecycleview.SearchItem;
import com.example.youtubeapp.utiliti.Util;
import com.example.youtubeapp.activitys.ChannelActivity;
import com.example.youtubeapp.api.ApiServicePlayList;
import com.example.youtubeapp.model.infochannel.Channel;
import com.example.youtubeapp.model.infochannel.Itemss;
import com.example.youtubeapp.model.itemrecycleview.VideoItem;
import com.example.youtubeapp.model.listcomment.Comment;
import com.example.youtubeapp.model.listcomment.ItemsComment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
    String idVideo, titleVideo, titleChannel;
    String viewCount;
    String timePublic;
    String likeCount, commentCount;
    String descVideo, idChannel,dateDayDiff;
    VideoItem itemVideo;
    SearchItem itemVideoS;
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
                clickOpenBottomSheetDialogFragment();
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
                    clickOpenCommentDialogFragment();
                }

            });
        }

    }
    // Mở số comment
    private void clickOpenCommentDialogFragment() {
        BottomSheetDialogCommentFragment dialog =
                BottomSheetDialogCommentFragment.newInstance(idVideo, commentCount);
        dialog.show(getChildFragmentManager(), dialog.getTag());

    }

    // Click open desc
    private void clickOpenBottomSheetDialogFragment() {
        BottomSheetDialogDescFragment bottomSheetDialogDescFragment =
                BottomSheetDialogDescFragment.newInstance(itemVideo, itemVideoS);
        bottomSheetDialogDescFragment.show(getChildFragmentManager(),
                bottomSheetDialogDescFragment.getTag());

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
}