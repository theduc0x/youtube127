package com.example.youtubeapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.youtubeapp.R;
import com.example.youtubeapp.activitys.ChannelActivity;
import com.example.youtubeapp.activitys.MainActivity;
import com.example.youtubeapp.adapter.CategoryAdapter;
import com.example.youtubeapp.model.itemrecycleview.CategoryItem;
import com.example.youtubeapp.model.itemrecycleview.CommentItem;
import com.example.youtubeapp.model.itemrecycleview.SearchItem;
import com.example.youtubeapp.model.listcategory.Category;
import com.example.youtubeapp.my_interface.IItemOnClickCategoryListener;
import com.example.youtubeapp.my_interface.IItemOnClickChannelListener;
import com.example.youtubeapp.my_interface.PaginationScrollListener;
import com.example.youtubeapp.utiliti.Util;
import com.example.youtubeapp.model.infochannel.Channel;
import com.example.youtubeapp.model.infochannel.Itemss;
import com.example.youtubeapp.model.itemrecycleview.VideoItem;
import com.example.youtubeapp.activitys.VideoPlayActivity;
import com.example.youtubeapp.adapter.VideoYoutubeAdapter;
import com.example.youtubeapp.api.ApiServicePlayList;
import com.example.youtubeapp.model.listvideohome.Items;
import com.example.youtubeapp.model.listvideohome.ListVideo;
import com.example.youtubeapp.my_interface.IItemOnClickVideoListener;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    public static final String TAG = HomeFragment.class.getName();
    public RecyclerView rvItemVideo;;
    public static VideoYoutubeAdapter adapter;
    LinearLayout llCategory;
    SwipeRefreshLayout srlReloadData;
    RecyclerView rvListCate;
    ArrayList<CategoryItem> listCategory;
    CategoryAdapter adapterCate;
    MainActivity mainActivity;
    public int a = 1;
    ArrayList<VideoItem> listVideoItem;
    ArrayList<VideoItem> listAdd;
    private String pageToken = "";
    private boolean isLoading;
    private boolean isLastPage;
    private int totalPage = 5;
    private int currenPage = 1;
    private String categoryId = "0";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        listVideoItem = new ArrayList<>();
        rvItemVideo = view.findViewById(R.id.rv_item_video);
        srlReloadData = view.findViewById(R.id.srl_reload);
        srlReloadData.setOnRefreshListener(this);
        mainActivity = (MainActivity) getActivity();
        llCategory = view.findViewById(R.id.ll_category);
        callCategory("vn");
        listCategory = new ArrayList<>();
        listCategory.add(new CategoryItem("0", "Most Popular", true));
        rvListCate = view.findViewById(R.id.rv_list_category);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                getActivity(), LinearLayoutManager.HORIZONTAL, false);
        adapterCate = new CategoryAdapter(getActivity(), new IItemOnClickCategoryListener() {
            @Override
            public void onClickCategory(String idCategory) {
                categoryId = idCategory;
                reloadData();
            }
        });
        rvListCate.setLayoutManager(linearLayoutManager);
        rvListCate.setAdapter(adapterCate);
        adapterCate.setData(listCategory);

        // Khi click vào các item video sẽ mở activity play để chạy video
        adapter = new VideoYoutubeAdapter(new IItemOnClickVideoListener() {
            @Override
            public void OnClickItemVideo(VideoItem item) {
                Intent toPlayVideo = new Intent(getActivity(), VideoPlayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Util.BUNDLE_EXTRA_OBJECT_ITEM_VIDEO, item);
                bundle.putString(Util.EXTRA_KEY_ITEM_VIDEO, "Video");
                toPlayVideo.putExtras(bundle);
                startActivity(toPlayVideo);
            }
        }, new IItemOnClickChannelListener() {
            @Override
            public void onClickOpenChannel(String idChannel, String titleChannel) {
                Intent openToChannel = new Intent(getActivity(), ChannelActivity.class);
                openToChannel.putExtra(Util.EXTRA_ID_CHANNEL_TO_CHANNEL, idChannel);
                openToChannel.putExtra(Util.EXTRA_TITLE_CHANNEL_TO_CHANNEL, titleChannel);
                startActivity(openToChannel);
//                    mainActivity.addFragmentChannel(idChannel, titleChannel);
            }
        });

        LinearLayoutManager linearLayoutManagerr =
                new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        RecyclerView.ItemDecoration decoration =
                new DividerItemDecoration(getActivity(), RecyclerView.VERTICAL);
        rvItemVideo.setLayoutManager(linearLayoutManagerr);
        rvItemVideo.addItemDecoration(decoration);
        rvItemVideo.setAdapter(adapter);

        setFirstData();
        rvItemVideo.smoothScrollToPosition(0);

        rvItemVideo.addOnScrollListener(new PaginationScrollListener(linearLayoutManagerr) {
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
        return view;
    }

    private void setFirstData() {
        listVideoItem = null;
        callApiPlaylist(pageToken, "10", categoryId);
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
                Toast.makeText(getContext(), "Load Page" + currenPage, Toast.LENGTH_SHORT).show();
                callApiPlaylist(pageToken, "10", categoryId);
                isLoading = false;
    }

    // Get dữ liệu về
    private void callApiPlaylist(String nextPageToken, String maxResults, String idCategory) {
        ApiServicePlayList.apiServicePlayList.listVideoNext(
                nextPageToken,
                "snippet",
                "statistics",
                "contentDetails",
                "mostPopular",
                "vn",
                "vn",
                idCategory,
                Util.API_KEY,
                maxResults
        ).enqueue(new Callback<ListVideo>() {
            @Override
            public void onResponse(Call<ListVideo> call, Response<ListVideo> response) {
                listAdd = new ArrayList<>();
//                Toast.makeText(getActivity(), "Call Api Success", Toast.LENGTH_SHORT).show();
//                Log.d("abc", response.body().toString());
                String urlThumbnailVideo = "", titleVideo = "", titleChannel = "",
                         timeVideo = "", viewCountVideo = "", commentCount = "",
                        idVideo = "", likeCountVideo = "", descVideo = "",
                        idChannel = "", urlLogoChannel = "", duration = "";


                ListVideo listVideo = response.body();
                // Nếu json không rỗng thì ta sẽ add vào list
                if (listVideo != null) {
                    int totalPlayList =  listVideo.getPageInfo().getTotalResults();
                    if (totalPlayList % 10 != 0) {
                        totalPage = (totalPlayList / 10) + 1;
                    }
                    else {
                        totalPage = (totalPlayList / 10);
                    }
                    pageToken = listVideo.getNextPageToken();
                    ArrayList<Items> listItem = listVideo.getItems();
                    for (int i = 0; i < listItem.size(); i++) {
                        if (listItem.get(0) == null) adapter.notifyDataSetChanged();

                        if (listItem.get(i).getSnippet().getThumbnails().getMaxres() != null) {
                            urlThumbnailVideo = listItem.get(i)
                                    .getSnippet().getThumbnails()
                                    .getMaxres().getUrl();
                        }else if (listItem.get(i).getSnippet().getThumbnails().getStandard() != null) {
                            urlThumbnailVideo = listItem.get(i)
                                    .getSnippet().getThumbnails()
                                    .getStandard().getUrl();
                        } else {
                            urlThumbnailVideo = listItem.get(i)
                                    .getSnippet().getThumbnails()
                                    .getHigh().getUrl();
                        }
                        titleVideo = listItem.get(i).getSnippet().getTitle();
                        titleChannel = listItem.get(i).getSnippet().getChannelTitle();
                        idChannel = listItem.get(i).getSnippet().getChannelId();

                        idVideo = listItem.get(i).getId();

                        timeVideo = listItem.get(i).getSnippet().getPublishedAt();
//                        String dateDayDiff = Util.getTime(timeVideo);

                        viewCountVideo = listItem.get(i).getStatistics().getViewCount();
                        double viewCount = Double.parseDouble(viewCountVideo);
                        viewCountVideo = Util.convertViewCount(viewCount);

                        likeCountVideo = listItem.get(i).getStatistics().getLikeCount();
                        descVideo = listItem.get(i).getSnippet().getDescription();
                        commentCount = listItem.get(i).getStatistics().getCommentCount();
                        duration = listItem.get(i).getContentDetails().getDuration();

                        // Lấy ảnh logo channel vì json ko có
                        callApiChannel(idChannel ,listAdd, i);

                        listAdd.add(new VideoItem(urlThumbnailVideo,
                                urlLogoChannel, titleVideo, timeVideo,
                                titleChannel, viewCountVideo, idVideo,
                                likeCountVideo, descVideo, idChannel, commentCount, duration));
//                        adapter.notifyItemInserted(i);
                    }
                    // load xong dữ liệu thì sẽ tắt pb
                    srlReloadData.setRefreshing(false);
//                    Collections.shuffle(Util.listVideoItem);
                    if (listVideoItem == null) {
                        listVideoItem = listAdd;
                        adapter.setData(listVideoItem);
                        setProgressBar();
                    } else {
                        adapter.removeFooterLoading();
                        listVideoItem.addAll(listAdd);
                        setProgressBar();
                    }

                }
            }

            // Nếu lỗi sẽ thông báo lỗi
            @Override
            public void onFailure(Call<ListVideo> call, Throwable t) {
                Toast.makeText(getActivity(), "Call Api Error", Toast.LENGTH_SHORT).show();
                Log.d("ab", t.toString());
            }
        });
    }

    // Lên đầu trang home
    public void topRecycleView() {

        rvItemVideo.smoothScrollToPosition(0);
    }
    public void topRecycleViewFast() {
        rvItemVideo.scrollToPosition(0);
    }

// Lấy ảnh logo Channel
private void callApiChannel(String id, ArrayList<VideoItem> video, int pos) {
    ApiServicePlayList.apiServicePlayList.infoChannel(
            "snippet",
            "contentDetails",
            "statistics",
            id,
            Util.API_KEY
    ).enqueue(new Callback<Channel>() {
        @Override
        public void onResponse(Call<Channel> call, Response<Channel> response) {
            String urlLogooo = "";
            Channel channel = response.body();
            if (channel != null) {
                ArrayList<Itemss> item = channel.getItems();
                urlLogooo = item.get(0).getSnippet().getThumbnails().getHigh().getUrl();
                video.get(pos).setUrlLogoChannel(urlLogooo);
                adapter.notifyDataSetChanged();
            }
        }
        @Override
        public void onFailure(Call<Channel> call, Throwable t) {
            Log.d("ab", t.toString());
        }
    });
}

    @Override
    public void onRefresh() {
        reloadCategory();
        categoryId = "0";
        reloadData();;
    }

    private void reloadData() {
        currenPage = 1;
        totalPage = 5;
        isLastPage = false;
        listVideoItem = null;
        listAdd = new ArrayList<>();
        listAdd.add(new VideoItem());
        topRecycleViewFast();
        callApiPlaylist("", "10", categoryId);
    }

    private void callCategory(String regionCode) {
        ApiServicePlayList.apiServicePlayList.listCategory(
                "snippet",
                regionCode,
                Util.API_KEY
        ).enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                boolean check = false;
                String titleCate = "", idCate = "";
                Category category = response.body();
                if (category != null) {
                    ArrayList<com.example.youtubeapp.model.listcategory.Items> listItem = category.getItems();
                    for (int i = 0; i < listItem.size(); i++) {
                        check = listItem.get(i).getSnippet().isAssignable();
                        if (check) {
                            titleCate  = listItem.get(i).getSnippet().getTitle();
                            idCate = listItem.get(i).getId();
                            listCategory.add(new CategoryItem(idCate, titleCate, check));
                            adapterCate.notifyItemChanged(i);
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<Category> call, Throwable t) {

            }
        });
    }

    private void reloadCategory() {
        listCategory.clear();
        listCategory.add(new CategoryItem("0", "Most Popular", true));
        adapterCate.setPosition(0);
        callCategory("vn");
        adapterCate.notifyDataSetChanged();
    }
}