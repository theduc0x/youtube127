package com.example.youtubeapp.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.youtubeapp.R;
import com.example.youtubeapp.adapter.CategoryAdapter;
import com.example.youtubeapp.api.ApiServicePlayList;
import com.example.youtubeapp.fragment.ShortsFragment;
import com.example.youtubeapp.fragment.HomeFragment;
import com.example.youtubeapp.fragment.LibraryFragment;
import com.example.youtubeapp.fragment.NotificationFragment;
import com.example.youtubeapp.fragment.SearchFragment;
import com.example.youtubeapp.fragment.SearchResultsFragment;
import com.example.youtubeapp.fragment.SubcriptionFragment;
import com.example.youtubeapp.model.itemrecycleview.CategoryItem;
import com.example.youtubeapp.model.listcategory.Category;
import com.example.youtubeapp.model.listcategory.Items;
import com.example.youtubeapp.my_interface.IItemOnClickCategoryListener;
import com.example.youtubeapp.utiliti.Util;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    Toolbar tbNav;
    BottomNavigationView bnvFragment;
    FrameLayout flContent;
    HomeFragment homeFragment;
    AppBarLayout ablHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tbNav = findViewById(R.id.tb_nav);
        tbNav.setVisibility(View.VISIBLE);
        bnvFragment = findViewById(R.id.bnv_fragment);
        bnvFragment.setVisibility(View.VISIBLE);
        flContent = findViewById(R.id.fl_content);
        ablHome = findViewById(R.id.abl_nav);

        homeFragment = new HomeFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fl_content, homeFragment, "fragHome");
        fragmentTransaction.addToBackStack(HomeFragment.TAG);
        fragmentTransaction.commit();
        bnvFragment.getMenu().findItem(R.id.mn_home).setChecked(true);
        // Sự kiện click thanh menu bottom
        bnvFragment.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mn_home:
                        setToolBarMainVisible();
                        if (item.isChecked()) {
                            homeFragment = (HomeFragment) getSupportFragmentManager()
                                    .findFragmentByTag("fragHome");
                            if (homeFragment.rvItemVideo.getAdapter().getItemCount() > 21) {
                                homeFragment.topRecycleViewFast();
                            } else {
                                homeFragment.topRecycleView();
                            }

                        } else {
                            item.setChecked(true);
                            getSupportFragmentManager().popBackStack(HomeFragment.TAG, 0);
                        }
                        break;
                    case R.id.mn_explore:
                        setToolBarMainInvisible();
                        ShortsFragment exploreFragment = new ShortsFragment();
                        item.setChecked(true);
                        selectFragment(exploreFragment);
                        break;
                    case R.id.mn_subcription:
                        setToolBarMainVisible();
                        SubcriptionFragment subcriptionFragment = new SubcriptionFragment();
                        item.setChecked(true);
                        selectFragment(subcriptionFragment);
                        break;
                    case R.id.mn_notification:
                        setToolBarMainVisible();
                        NotificationFragment notificationFragment = new NotificationFragment();
                        item.setChecked(true);
                        selectFragment(notificationFragment);
                        break;
                    case R.id.mn_library:
                        setToolBarMainVisible();
                        LibraryFragment libraryFragment = new LibraryFragment();
                        item.setChecked(true);
                        selectFragment(libraryFragment);
                        break;
                }
                return false;
            }
        });

        tbNav.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mn_search:
                        addFragmentSearch("");

                        break;

                }
                return false;
            }
        });

    }


    // Phương thức chọn fragment khi click menu
    private void selectFragment(Fragment fragment) {
        Random random = new Random();
        float floatNumber = random.nextFloat();
        int intNumber = random.nextInt();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_content, fragment);
        fragmentTransaction.addToBackStack(floatNumber + intNumber + "9");
        fragmentTransaction.commit();
    }

    // add phần tìm kiếm
    public void addFragmentSearch(String s) {
        setToolBarMainInvisible();
        bnvFragment.setVisibility(View.GONE);
        SearchFragment searchFragment = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Util.BUNDLE_EXTRA_TEXT_EDITTEXT, s);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        searchFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fl_content, searchFragment, "fragSearch");
        fragmentTransaction.addToBackStack("SearchFragment");
        fragmentTransaction.commit();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        HomeFragment fragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("fragHome");
        if (fragment != null && fragment.isVisible()) {
            setToolBarMainVisible();
        }

    }
    // Add thêm kết quả của việc search vào main
    public void addFragmentSearchResults(String q) {
        setToolBarMainInvisible();
        bnvFragment.setVisibility(View.VISIBLE);

//        removeFragmentSearch();
        SearchResultsFragment searchResultsFragment = new SearchResultsFragment();
        getSupportFragmentManager().popBackStack();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString(Util.BUNDLE_EXTRA_Q, q);
        searchResultsFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fl_content, searchResultsFragment, "fragSearchRe");
        fragmentTransaction.addToBackStack("SearchFragmentRe");
        fragmentTransaction.commit();
    }
    // hiển thị toolbar và bnvbar
    public void setToolBarMainVisible() {
        // hiển thị sau khi cuộn trang
        if (tbNav.getParent() instanceof AppBarLayout){
            ((AppBarLayout)tbNav.getParent()).setExpanded(true,true);
        }
        bnvFragment.setVisibility(View.VISIBLE);
        tbNav.setVisibility(View.VISIBLE);
        Log.d("fjdsal","success");
    }
    public void setToolBarMainInvisible() {
        tbNav.setVisibility(View.GONE);
    }

}