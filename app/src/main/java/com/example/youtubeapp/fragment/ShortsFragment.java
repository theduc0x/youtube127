package com.example.youtubeapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.youtubeapp.R;
import com.example.youtubeapp.adapter.ShortsVideoAdapter;
import com.example.youtubeapp.api.ApiServicePlayList;
import com.example.youtubeapp.model.detailvideo.DetailVideo;
import com.example.youtubeapp.model.detailvideo.ItemVideo;
import com.example.youtubeapp.model.itemrecycleview.VideoChannelItem;
import com.example.youtubeapp.model.itemrecycleview.VideoItem;
import com.example.youtubeapp.model.searchyoutube.ItemsSearch;
import com.example.youtubeapp.model.searchyoutube.Search;
import com.example.youtubeapp.utiliti.Util;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShortsFragment extends Fragment {
    YouTubePlayerView ypvVideo;
    ArrayList<VideoItem> listItems;
    ShortsVideoAdapter adapter;
    String pageToken = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shorts, container, false);
        final ViewPager2 vp2Video = view.findViewById(R.id.vp2_shorts_video);
        ypvVideo = view.findViewById(R.id.ypv_shorts);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        listItems = new ArrayList<>();
        adapter = new ShortsVideoAdapter(getContext());
        calLApiVideoShortRandom(pageToken, "50", null);
        vp2Video.setAdapter(adapter);
        adapter.setData(listItems);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void calLApiVideoShortRandom(String nextPageToken, String maxResults, String order) {
        ApiServicePlayList.apiServicePlayList.videoUpdateNews(
                nextPageToken,
                "snippet",
                "id",
                null,
                order,
                "shorts",
                "video",
                "vn",
                Util.API_KEY,
                maxResults
        ).enqueue(new Callback<Search>() {
            @Override
            public void onResponse(Call<Search> call, Response<Search> response) {
                String urlThumbnails = "", titleVideo = "", publishAt = "",
                        viewCount = "", idVideo = "", likeCount = "", commentCount = "",
                        idChannel = "", urlLogoChannel = "", titleChannel = "";
                Search video = response.body();
                if (video != null) {
                    List<ItemsSearch> listItem = video.getItems();
                    for (int i = 0; i < listItem.size(); i++) {
                        if (listItem.get(i).getSnippet().getThumbnails().getMaxres() != null) {
                            urlThumbnails = listItem.get(i).getSnippet().getThumbnails().getMaxres().getUrl();
                        } else if (listItem.get(i).getSnippet().getThumbnails().getStandard() != null) {
                            urlThumbnails = listItem.get(i).getSnippet().getThumbnails().getStandard().getUrl();
                        }else {
                            urlThumbnails = listItem.get(i).getSnippet().getThumbnails().getHigh().getUrl();
                        }
                        titleVideo = listItem.get(i).getSnippet().getTitle();
                        publishAt = listItem.get(i).getSnippet().getPublishedAt();
                        idVideo = listItem.get(i).getId().getVideoId();
                        idChannel = listItem.get(i).getSnippet().getChannelId();
                        titleChannel = listItem.get(i).getSnippet().getChannelTitle();
                        callApiViewCountVideo(idVideo, listItems, i);

                        listItems.add(new VideoItem(urlThumbnails, urlThumbnails, titleVideo,
                                publishAt, titleChannel, "", idVideo, likeCount,
                                "", idChannel, "", ""));
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Search> call, Throwable t) {

            }
        });
    }

    private void callApiViewCountVideo(String idVideo, List<VideoItem> listItemV,
                                       int pos) {
        ApiServicePlayList.apiServicePlayList.detailVideo(
                "snippet",
                "statistics",
                null,
                idVideo,
                Util.API_KEY
        ).enqueue(new Callback<DetailVideo>() {
            @Override
            public void onResponse(Call<DetailVideo> call, Response<DetailVideo> response) {
                String viewCount = "", cmtCount = "", likeCount = "";
                DetailVideo video = response.body();
                if (video != null) {
                    ArrayList<ItemVideo> listItem = video.getItems();
                    for (int i = 0; i <listItem.size(); i++ ) {
                        viewCount = listItem.get(0).getStatistics().getViewCount();
                        cmtCount = listItem.get(0).getStatistics().getCommentCount();
                        likeCount = listItem.get(0).getStatistics().likeCount;
                        listItemV.get(pos).setViewCountVideo(viewCount);
                        listItemV.get(pos).setLikeCountVideo(likeCount);
                        listItemV.get(pos).setCommentCount(cmtCount);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailVideo> call, Throwable t) {

            }
        });
    }
}