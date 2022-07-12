package com.example.youtubeapp.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.example.youtubeapp.R;
import com.example.youtubeapp.model.itemrecycleview.SearchItem;
import com.example.youtubeapp.utiliti.Util;
import com.example.youtubeapp.api.ApiServicePlayList;
import com.example.youtubeapp.model.infochannel.Channel;
import com.example.youtubeapp.model.infochannel.Itemss;
import com.example.youtubeapp.model.itemrecycleview.VideoItem;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomSheetDialogDescFragment extends BottomSheetDialogFragment {
    private VideoItem itemV;
    private SearchItem itemS;
    TextView tvTitleVideoPlay, tvViewCountVideo, tvDatePublicVideo, tvDateYearVideo;
    CircleImageView civLogoChannel;
    TextView tvTitleChannelVideo, tvLikeCountVideo, tvDesc;
    Toolbar tbCancel;

    public static BottomSheetDialogDescFragment newInstance(VideoItem item, SearchItem itemS) {
        BottomSheetDialogDescFragment bottomSheetDialogDescFragment =
                new BottomSheetDialogDescFragment();

        Bundle bundle = new Bundle();
            bundle.putSerializable(Util.BUNDLE_EXTRA_ITEM_VIDEO_SEARCH, itemS);
            bundle.putSerializable(Util.BUNDLE_EXTRA_ITEM_VIDEO, item);

        bottomSheetDialogDescFragment.setArguments(bundle);

        return bottomSheetDialogDescFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundleReceive = getArguments();
        if (bundleReceive != null) {
            itemV = (VideoItem) bundleReceive.getSerializable(Util.BUNDLE_EXTRA_ITEM_VIDEO);
            itemS = (SearchItem) bundleReceive.getSerializable(Util.BUNDLE_EXTRA_ITEM_VIDEO_SEARCH);
        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        BottomSheetDialog bottomSheetDialog =
                (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View viewDialog = LayoutInflater.from(getContext()).inflate(
                R.layout.fragment_bottom_sheet_desc, null);
        bottomSheetDialog.setContentView(viewDialog);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        // lấy chiều cao màn hình activity chứa
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        int maxHeight = (int) (height*0.65);

        BottomSheetBehavior bottomSheetBehavior =
                BottomSheetBehavior.from(((View) viewDialog.getParent()));
        bottomSheetBehavior.setMaxHeight(maxHeight);
        bottomSheetBehavior.setPeekHeight(maxHeight);

        intMain(viewDialog);
        setDataDesc();
        bottomSheetDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        bottomSheetDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        tbCancel.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_close_desc) {
                    bottomSheetDialog.dismiss();
                }
                return false;
            }
        });

        return bottomSheetDialog;
    }
    // call api lấy ảnh icon channel
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
                if (channel != null) {
                    ArrayList<Itemss> listItem = channel.getItems();
                    Picasso.get().load(listItem.get(0).getSnippet().getThumbnails().getDefaults().getUrl())
                            .into(civLogoChannel);
                }
            }

            @Override
            public void onFailure(Call<Channel> call, Throwable t) {
                Log.d("ab", t.toString());
            }
        });
    }
    public void getTime(String dateOne) {
            String startDate = dateOne.substring(0, 10);

            String days = startDate.substring(8,10);
            String month = startDate.substring(5,7);
            String year = startDate.substring(0,4);

        tvDatePublicVideo.setText(days + " thg " + month);
        tvDateYearVideo.setText(year);
    }

    private void intMain(View view) {
        civLogoChannel = view.findViewById(R.id.civ_logo_channel_desc);
        tvTitleVideoPlay = view.findViewById(R.id.tv_title_video_desc);
        tvTitleChannelVideo = view.findViewById(R.id.tv_title_channel_desc);
        tvViewCountVideo = view.findViewById(R.id.tv_view_video_desc);
        tvLikeCountVideo = view.findViewById(R.id.tv_like_video_desc);
        tvDatePublicVideo = view.findViewById(R.id.tv_date_dm_desc);
        tvDateYearVideo = view.findViewById(R.id.tv_date_year_desc);
        tvDesc = view.findViewById(R.id.tv_display_video_desc);
        tbCancel = view.findViewById(R.id.tb_desc_video);
    }
    private void setDataDesc() {
        if (itemV == null && itemS == null) {
            return;
        } else if (itemS != null) {
            callApiChannel(itemS.getIdChannel());
            tvTitleVideoPlay.setText(itemS.getTvTitleVideo());
            tvTitleChannelVideo.setText(itemS.getTitleChannel());
            tvViewCountVideo.setText(itemS.getViewCountVideo());
            tvLikeCountVideo.setText(itemS.getLikeCountVideo());
            getTime(itemS.getPublishAt());
            tvDesc.setText(itemS.getDescVideo());
        } else {
            callApiChannel(itemV.getIdChannel());
            tvTitleVideoPlay.setText(itemV.getTvTitleVideo());
            tvTitleChannelVideo.setText(itemV.getTvTitleChannel());
            tvViewCountVideo.setText(itemV.getViewCountVideo());
            tvLikeCountVideo.setText(itemV.getLikeCountVideo());
            getTime(itemV.getPublishAt());
            tvDesc.setText(itemV.getDescVideo());
        }
    }

}
