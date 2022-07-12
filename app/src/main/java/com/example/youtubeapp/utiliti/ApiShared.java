package com.example.youtubeapp.utiliti;

import android.util.Log;
import android.view.View;

import com.example.youtubeapp.api.ApiServicePlayList;
import com.example.youtubeapp.model.infochannel.Channel;
import com.example.youtubeapp.model.infochannel.Itemss;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiShared {
//    public void callApiChannelFull(String idChannel1) {
//        ApiServicePlayList.apiServicePlayList.infoChannelFull(
//                "contentDetails",
//                "snippet",
//                "statistics",
//                "topicDetails",
//                "brandingSettings",
//                idChannel1,
//                Util.API_KEY
//        ).enqueue(new Callback<Channel>() {
//            @Override
//            public void onResponse(Call<Channel> call, Response<Channel> response) {
//                String urlBanner = "", titleChannel = "", subCount = "",
//                        videoCount = "", urlLogoChannel, introduce;
//                boolean isCheckHideSub = false;
//                Itemss item = null;
//                Channel channel = response.body();
//                if (channel != null) {
//                    ArrayList<Itemss> listItem = channel.getItems();
//                    item = listItem.get(0);
//                    if (item.getBrandingSettings().getImage().getBannerExternalUrl() == null) {
//                        urlBanner = "https://i.ytimg.com/vi/0UDkE-PrV7s/hqdefault.jpg";
//                    } else {
//                        urlBanner = item.getBrandingSettings().getImage().getBannerExternalUrl();
//                    }
//
//                    titleChannel = item.getSnippet().getTitle();
//                    isCheckHideSub = item.getStatistics().isHiddenSubscriberCount();
//                    if (isCheckHideSub) {
//                        subCount = "";
//                        tvSubCount.setVisibility(View.GONE);
//                        tvNameSub.setVisibility(View.GONE);
//                    } else {
//                        subCount = item.getStatistics().getSubscriberCount();
//                        tvSubCount.setText(Util.convertViewCount(Double.parseDouble(subCount)));
//                    }
//                    videoCount = item.getStatistics().getVideoCount();
//                    urlLogoChannel = item.getSnippet().getThumbnails().getHigh().getUrl();
//                    introduce = item.getSnippet().getDescription();
//                    tvTitleChannel.setText(titleChannel);
//                    tvVideoCount.setText(Util.convertViewCount(Double.parseDouble(videoCount)));
//                    tvIntroduce.setText(introduce);
//                    Picasso.get().load(urlLogoChannel).into(civLogoChannel);
//                    Picasso.get().load(urlBanner).into(ivBannerChannel);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Channel> call, Throwable t) {
//                Log.d("ducak", t.toString());
//            }
//        });
//    }
}
