package com.example.youtubeapp.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youtubeapp.R;
import com.example.youtubeapp.my_interface.IItemOnClickSortListener;
import com.example.youtubeapp.utiliti.Util;
import com.example.youtubeapp.activitys.VideoPlayActivity;
import com.example.youtubeapp.adapter.VideoChannelAdapter;
import com.example.youtubeapp.api.ApiServicePlayList;
import com.example.youtubeapp.model.detailvideo.DetailVideo;
import com.example.youtubeapp.model.detailvideo.ItemVideo;
import com.example.youtubeapp.model.itemrecycleview.VideoChannelItem;
import com.example.youtubeapp.model.itemrecycleview.VideoItem;
import com.example.youtubeapp.model.searchyoutube.ItemsSearch;
import com.example.youtubeapp.model.searchyoutube.Search;
import com.example.youtubeapp.my_interface.IItemOnClickVideoListener;
import com.example.youtubeapp.my_interface.PaginationScrollListener;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChannelVideoFragment extends Fragment implements IItemOnClickSortListener {
    RecyclerView rvListVideo;
    LinearLayout llOpenSort;
    ArrayList<VideoChannelItem> listItems;
    TextView tvClickName;
    ArrayList<VideoChannelItem> list;
    VideoChannelAdapter adapter;
    String idChannel;
    private String pageToken = "";
    private boolean isLoading;
    private boolean isLastPage;
    private int totalPage = 5;
    private int currentPage = 1;
    private int loadPage = 1;

    Search video;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel_video, container, false);
        rvListVideo = view.findViewById(R.id.rv_list_video_channel);
        tvClickName = view.findViewById(R.id.tv_sort_video_channel);
        llOpenSort = view.findViewById(R.id.ll_sort_video_channel);
        listItems = new ArrayList<>();
        // lấy idChannel
        getBundle();

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvListVideo.setLayoutManager(linearLayoutManager);
        adapter = new VideoChannelAdapter(new IItemOnClickVideoListener() {
            @Override
            public void OnClickItemVideo(VideoItem item) {
                Intent toPlayVideo = new Intent(getActivity(), VideoPlayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Util.BUNDLE_EXTRA_OBJECT_ITEM_VIDEO, item);
                bundle.putString(Util.EXTRA_KEY_ITEM_VIDEO, "Video");
                toPlayVideo.putExtras(bundle);
                startActivity(toPlayVideo);
            }
        });
//        rvListVideo.setNestedScrollingEnabled(false);
        rvListVideo.setAdapter(adapter);
        // Gọi page đầu tiên trong recycleview
        setFirstData();
        // Sự kiện load data khi cuộn đến cuối page
        rvListVideo.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            public void loadMoreItem() {
                isLoading = true;
                currentPage += 1;
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

        llOpenSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOpenBottomSheetDialogFragment();
            }
        });

        return view;
    }
//     Load data page one
    private void setFirstData() {
        listItems = null;
        callApiVideoChannel(pageToken, idChannel, "10", "date");
    }
    // Set propress bar load data
    private void setProgressBar() {
        if (currentPage < totalPage) {
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
                Toast.makeText(getContext(), "Load Page" + currentPage, Toast.LENGTH_SHORT).show();

                isLoading = false;

                if (loadPage == 1) {
                    callApiVideoChannel(pageToken, idChannel, "10", "date");
                    isLoading = false;
                } else if (loadPage == 2) {
                    callApiVideoChannel(pageToken, idChannel, "10", "viewCount");
                    isLoading = false;
                }
            }
        },1000);
    }

    private void callApiVideoChannel(String nextPageToken, String channelId, String maxResults, String order) {
        list = new ArrayList<>();
        ApiServicePlayList.apiServicePlayList.videoUpdateNews(
                nextPageToken,
                "snippet",
                "id",
                channelId,
                order,
                null,
                "video",
                null,
                Util.API_KEY,
                maxResults
        ).enqueue(new Callback<Search>() {
            @Override
            public void onResponse(Call<Search> call, Response<Search> response) {
                String urlThumbnails = "", titleVideo = "", publishAt = "",
                        viewCount = "", idVideo = "";

                video = response.body();
                if (video != null) {
                    totalPage = (int) video.getPageInfo().getTotalResults() / 10;
                    Log.d("page", String.valueOf(totalPage));
                    Log.d("page", String.valueOf(video.getPageInfo().getTotalResults()));
                    pageToken = video.getNextPageToken();
                    ArrayList<ItemsSearch> listItem = video.getItems();

                    for (int i = 0;i <listItem.size(); i++) {
                        idVideo = listItem.get(i).getId().getVideoId();
//                        Log.d("duckaka", idVideo);
                        if (listItem.get(i).getSnippet().getThumbnails().getMaxres() != null) {
                            urlThumbnails = listItem.get(i).getSnippet().getThumbnails().getMaxres().getUrl();
                        } else if (listItem.get(i).getSnippet().getThumbnails().getStandard() != null) {
                            urlThumbnails = listItem.get(i).getSnippet().getThumbnails().getStandard().getUrl();
                        }else {
                            urlThumbnails = listItem.get(i).getSnippet().getThumbnails().getHigh().getUrl();
                        }
                        publishAt = listItem.get(i).getSnippet().getPublishedAt();
                        Log.d("duckakaka", titleVideo);
                        Log.d("duckakaka", publishAt);
                        titleVideo = listItem.get(i).getSnippet().getTitle();

                        callApiViewCountVideo(idVideo, list, i);

                        list.add(new VideoChannelItem(urlThumbnails, titleVideo,
                                publishAt, "", idVideo));
//                        Log.d("duckaka1", list.get(0).getIdVideo());
                    }
                    if (listItems == null) {
                        listItems = list;
                        adapter.setData(listItems);
                        setProgressBar();
                    } else {
                        adapter.removeFooterLoading();
                        listItems.addAll(list);
                        adapter.notifyDataSetChanged();
                        setProgressBar();
                    }

                }
            }
            @Override
            public void onFailure(Call<Search> call, Throwable t) {
                        Log.d("error", t.toString());
            }
        });
    }

    private void callApiViewCountVideo(String idVideo, ArrayList<VideoChannelItem> listItemV,
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
                String viewCount = "";
                DetailVideo video = response.body();
                if (video != null) {
                    ArrayList<ItemVideo> listItem = video.getItems();
                    for (int i = 0; i <listItem.size(); i++ ) {
                        viewCount = listItem.get(0).getStatistics().getViewCount();
                        listItemV.get(pos).setViewCount(viewCount);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailVideo> call, Throwable t) {

            }
        });
    }

    private void clickOpenBottomSheetDialogFragment() {
        BottomSheetDiaLogSortVideoFragment bottomSheetDiaLogSortVideoFragment =
                BottomSheetDiaLogSortVideoFragment.newInstance(this);
        bottomSheetDiaLogSortVideoFragment.show(getChildFragmentManager(),
                bottomSheetDiaLogSortVideoFragment.getTag());

    }

    private void getBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            idChannel = bundle.getString(Util.EXTRA_ID_CHANNEL_TO_CHANNEL_FROM_ADAPTER);
        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClickSortDate() {
         currentPage = 1;
         loadPage = 1;
         listItems = null;
         callApiVideoChannel("", idChannel, "10", "date");
         tvClickName.setText("SORT BY");
        tvClickName.setTextColor(R.color.black);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClickSortMostPopular() {
        currentPage = 1;
        loadPage = 2;
        listItems = null;
        callApiVideoChannel("", idChannel, "10", "viewCount");
        tvClickName.setText("MOST POPULAR");
        tvClickName.setTextColor(R.color.white);
    }
}