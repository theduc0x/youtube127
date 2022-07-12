package com.example.youtubeapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.youtubeapp.R;
import com.example.youtubeapp.utiliti.Util;
import com.example.youtubeapp.api.ApiServicePlayList;
import com.example.youtubeapp.model.infochannel.Channel;
import com.example.youtubeapp.model.infochannel.Itemss;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChannelHomeFragment extends Fragment {
    TextView tvTitleChannel, tvSubCount, tvVideoCount,
            tvIntroduce, tvNameSub;
    ImageView ivBannerChannel;
    CircleImageView civLogoChannel;
    String idChannel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_channel_home, container, false);
        initView(view);
        Bundle bundleRe = getArguments();
        if (bundleRe != null) {
            idChannel = bundleRe.getString(Util.EXTRA_ID_CHANNEL_TO_CHANNEL_FROM_ADAPTER);
        }
        callApiChannelFull(idChannel);
        return view;
    }

    private void initView(View view) {
        tvTitleChannel = view.findViewById(R.id.tv_title_channel_home);
        tvSubCount = view.findViewById(R.id.tv_sub_count_channel_home);
        tvVideoCount = view.findViewById(R.id.tv_video_count_channel_home);
        tvIntroduce = view.findViewById(R.id.tv_introduce_content);
        ivBannerChannel = view.findViewById(R.id.iv_banner_channel);
        civLogoChannel = view.findViewById(R.id.civ_logo_Channel_home);
        tvNameSub = view.findViewById(R.id.tv_name_sub);
    }

    public void callApiChannelFull(String idChannel1) {
        ApiServicePlayList.apiServicePlayList.infoChannelFull(
                "contentDetails",
                "snippet",
                "statistics",
                "topicDetails",
                "brandingSettings",
                idChannel1,
                Util.API_KEY
        ).enqueue(new Callback<Channel>() {
            @Override
            public void onResponse(Call<Channel> call, Response<Channel> response) {
                String urlBanner = "", titleChannel = "", subCount = "",
                        videoCount = "", urlLogoChannel, introduce;
                boolean isCheckHideSub = false;
                Itemss item = null;
                Channel channel = response.body();
                if (channel != null) {
                    ArrayList<Itemss> listItem = channel.getItems();
                    item = listItem.get(0);
                    if (item == null) {
                        return;
                    }
                        if (item.getBrandingSettings().getImage() == null) {
                            urlBanner =
                                    "https://st.quantrimang.com/photos/image/2020/07/30/Hinh-Nen-Trang-10.jpg";
                        } else {
                            urlBanner = item.getBrandingSettings().getImage().getBannerExternalUrl();
                        }

                        titleChannel = item.getSnippet().getTitle();
                        isCheckHideSub = item.getStatistics().isHiddenSubscriberCount();
                        if (isCheckHideSub) {
                            subCount = "";
                            tvSubCount.setVisibility(View.GONE);
                            tvNameSub.setVisibility(View.GONE);
                        } else {
                            subCount = item.getStatistics().getSubscriberCount();
                            tvSubCount.setText(Util.convertViewCount(Double.parseDouble(subCount)));
                        }
                        videoCount = item.getStatistics().getVideoCount();
                        urlLogoChannel = item.getSnippet().getThumbnails().getHigh().getUrl();
                        introduce = item.getSnippet().getDescription();
                        tvTitleChannel.setText(titleChannel);
                        tvVideoCount.setText(Util.convertViewCount(Double.parseDouble(videoCount)));
                        tvIntroduce.setText(introduce);
                        Picasso.get().load(urlLogoChannel).into(civLogoChannel);
                        Picasso.get().load(urlBanner).into(ivBannerChannel);
                    }
            }

            @Override
            public void onFailure(Call<Channel> call, Throwable t) {
                Log.d("ducak", t.toString());
            }
        });
    }

}