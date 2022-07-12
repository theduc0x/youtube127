package com.example.youtubeapp.fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.youtubeapp.R;
import com.example.youtubeapp.api.ApiServicePlayList;
import com.example.youtubeapp.model.infochannel.Channel;
import com.example.youtubeapp.model.infochannel.Itemss;
import com.example.youtubeapp.utiliti.Util;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChannelAboutFragment extends Fragment
implements SwipeRefreshLayout.OnRefreshListener {
    String idChannel;
    TextView tvDesc, tvViewCount, tvCountry, tvPublished, tvLinkYoutube;
    SwipeRefreshLayout srlData;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel_about, container, false);
        getBundle();
        initView(view);
        srlData.setOnRefreshListener(this);
        callApiChannelFull(idChannel);
        return view;
    }


    private void getBundle() {
            Bundle bundle = getArguments();
            if (bundle != null) {
                idChannel = bundle.getString(Util.EXTRA_ID_CHANNEL_TO_CHANNEL_FROM_ADAPTER);
            }
    }

    private void initView(View view) {
        tvDesc = view.findViewById(R.id.tv_content_desc_about);
        tvViewCount = view.findViewById(R.id.tv_total_view_count);
        tvCountry = view.findViewById(R.id.tv_location);
        tvPublished = view.findViewById(R.id.tv_published_channel);
        tvLinkYoutube = view.findViewById(R.id.tv_link_youtube_detail);
        srlData = view.findViewById(R.id.srl_reload_data);
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
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Channel> call, Response<Channel> response) {
                String  viewCount = "", publishAt = "",
                        customUrl = "", country = "", description = "";
                Itemss item = null;
                Channel channel = response.body();
                if (channel != null) {
                    ArrayList<Itemss> listItem = channel.getItems();
                    item = listItem.get(0);

                    viewCount = item.getStatistics().getViewCount();
                    publishAt = item.getSnippet().getPublishedAt();
                    if (item.getSnippet().getCustomUrl() == null) {
                        customUrl = "";
                    } else {
                        customUrl = item.getSnippet().getCustomUrl();
                    }
                    if (item.getSnippet().getCountry() == null) {
                        country = "";
                        tvCountry.setVisibility(View.GONE);
                    } else {
                        country = item.getSnippet().getCountry();
                        Locale l = new Locale("", country);
                        String countryD = l.getDisplayCountry();
                        tvCountry.setText(countryD);
                    }
                    description = item.getSnippet().getDescription();

                    // chuyển đổi tên viết tắt sang tên đầy đủ


                    if (customUrl.equals("")) {
                        tvLinkYoutube.setText(getString(R.string.tv_link_url_youtube_1) + idChannel1);
                    } else {
                        tvLinkYoutube.setText(getString(R.string.tv_link_url_youtube_2) + customUrl);
                    }
                    String str1 = convertView(viewCount);
                    tvViewCount.setText(str1 + " " +  getString(R.string.tv_title_view));
                    tvDesc.setText(description);
                    String pub = publishAt.substring(0, 10);

                    tvPublished.setText(pub);

                }
            }

            @Override
            public void onFailure(Call<Channel> call, Throwable t) {
                Log.d("ducak", t.toString());
            }
        });
    }




    private String convertView(String view) {
        Locale localeEN = new Locale("en", "EN");
        NumberFormat en = NumberFormat.getInstance(localeEN);
        long s = Long.parseLong(view);
        String str1 = en.format(s);
        return str1;
    }

    @Override
    public void onRefresh() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                srlData.setRefreshing(false);
                callApiChannelFull(idChannel);
            }
        }, 1000);
    }
}