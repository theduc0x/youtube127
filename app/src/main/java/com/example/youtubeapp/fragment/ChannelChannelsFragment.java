package com.example.youtubeapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.youtubeapp.R;
import com.example.youtubeapp.activitys.ChannelActivity;
import com.example.youtubeapp.adapter.ListChannelAdapter;
import com.example.youtubeapp.api.ApiServicePlayList;
import com.example.youtubeapp.model.infochannel.Channel;
import com.example.youtubeapp.model.infochannel.Itemss;
import com.example.youtubeapp.model.itemrecycleview.ChannelItem;
import com.example.youtubeapp.model.listchannelsfromchannel.ChannelsList;
import com.example.youtubeapp.model.listchannelsfromchannel.Items;
import com.example.youtubeapp.my_interface.IItemOnClickChannelListener;
import com.example.youtubeapp.utiliti.Util;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChannelChannelsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    String idChannel;
    RecyclerView rvChannelList;
    ListChannelAdapter adapter;
    ArrayList<ChannelItem> listItems;
    SwipeRefreshLayout srlLoad;
    TextView tvNotFeature;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel_channels, container, false);
        getBundle();
        tvNotFeature = view.findViewById(R.id.tv_not_channels);
        srlLoad = view.findViewById(R.id.srl_reload_data_channel);
        srlLoad.setOnRefreshListener(this);
        rvChannelList = view.findViewById(R.id.rv_item_channel_list);
        listItems = new ArrayList<>();
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvChannelList.setLayoutManager(linearLayoutManager);
        adapter = new ListChannelAdapter(new IItemOnClickChannelListener() {
            @Override
            public void onClickOpenChannel(String idChannel, String titleChannel) {
                Intent openToChannel = new Intent(getActivity(), ChannelActivity.class);
                openToChannel.putExtra(Util.EXTRA_ID_CHANNEL_TO_CHANNEL, idChannel);
                openToChannel.putExtra(Util.EXTRA_TITLE_CHANNEL_TO_CHANNEL, titleChannel);
                startActivity(openToChannel);
            }
        });
        rvChannelList.setAdapter(adapter);
        callApiGetListChannel(idChannel);

        adapter.setData(listItems);

        return view;
    }

    private void getBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            idChannel = bundle.getString(Util.EXTRA_ID_CHANNEL_TO_CHANNEL_FROM_ADAPTER);
        }
    }

    private void callApiGetListChannel(String idChannel) {
        Log.d("duc1", idChannel);
        ApiServicePlayList.apiServicePlayList.channelsList(
                "contentDetails,id,snippet",
                idChannel,
                Util.API_KEY
        ).enqueue(new Callback<ChannelsList>() {
            @Override
            public void onResponse(Call<ChannelsList> call, Response<ChannelsList> response) {
                Log.d("duc2", response.toString());
                String urlLogoChannel = "", titleChannel = "", subCount = "", videoCount = "",
                        idChannel = "";
                String type = "";
                ChannelsList list = response.body();
                if (list != null) {
                    ArrayList<Items> listItem = list.getItems();
                    for (int i = 0; i < listItem.size(); i++) {
                        type = listItem.get(i).getSnippet().getType();

                        if (type.equals("multiplechannels")) {
                            tvNotFeature.setVisibility(View.GONE);
                            ArrayList<String> listIdChannel =
                                    listItem.get(i).getContentDetails().getChannels();
                            for (int j = 0; j < listIdChannel.size(); j++) {
                                String id = listIdChannel.get(j);
                                callApiChannelFull(id);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ChannelsList> call, Throwable t) {

            }
        });
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
                        videoCount = "", urlLogoChannel, introduce, idChannelList;
                boolean isCheckHideSub = false;
                Itemss item = null;
                Channel channel = response.body();
                if (channel != null) {
                    ArrayList<Itemss> listItem = channel.getItems();
                    item = listItem.get(0);
                    titleChannel = item.getSnippet().getTitle();
                    isCheckHideSub = item.getStatistics().isHiddenSubscriberCount();
                    if (isCheckHideSub) {
                        subCount = "";
                    } else {
                        subCount = item.getStatistics().getSubscriberCount();
                    }
                    videoCount = item.getStatistics().getVideoCount();
                    urlLogoChannel = item.getSnippet().getThumbnails().getHigh().getUrl();
                    introduce = item.getSnippet().getDescription();
                    idChannelList = item.getId();
                    Log.d("duc3", titleChannel);
                    listItems.add(new ChannelItem(idChannelList, titleChannel, subCount, videoCount,
                            urlLogoChannel));
                    adapter.notifyItemInserted(listItems.size() - 1);
                }
            }

            @Override
            public void onFailure(Call<Channel> call, Throwable t) {
                Log.d("ducak", t.toString());
            }
        });
    }

    @Override
    public void onRefresh() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                srlLoad.setRefreshing(false);
                listItems = new ArrayList<>();
                callApiGetListChannel(idChannel);
                adapter.setData(listItems);
            }
        }, 1000);
    }
}