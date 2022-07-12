package com.example.youtubeapp.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.youtubeapp.R;
import com.example.youtubeapp.fragment.HomeFragment;
import com.example.youtubeapp.utiliti.Util;
import com.example.youtubeapp.adapter.ViewPagerChannelAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ChannelActivity extends AppCompatActivity {
    TabLayout tlChannel;
    ViewPager2 vp2Content;
    ViewPagerChannelAdapter adapter;
    String idChannel = "", titleChannel = "";
    TextView tvTitleChannel;
    ImageButton ibBack;
    BottomNavigationView bnvChannel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        initView();
        setTabLayout();
        getIdChannelAndTransHomeChannel();
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setBnvChannel();
    }

    private void initView() {
        tlChannel = findViewById(R.id.tl_channel);
        vp2Content = findViewById(R.id.vp2_content);
        tvTitleChannel = findViewById(R.id.tv_title_channel_nav);
        ibBack = findViewById(R.id.ib_back_home_channel);
        bnvChannel = findViewById(R.id.bnv_fragment_channel);
    }

    private void setTabLayout() {
        adapter = new ViewPagerChannelAdapter(this);
        vp2Content.setAdapter(adapter);
        new TabLayoutMediator(tlChannel, vp2Content, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0 :
                        tab.setText("HOME");
                        break;
                    case 1 :
                        tab.setText("VIDEOS");
                        break;
                    case 2 :
                        tab.setText("PLAYLISTS");
                        break;
                    case 3 :
                        tab.setText("COMMUNITY");
                        break;
                    case 4 :
                        tab.setText("CHANNELS");
                        break;
                    case 5 :
                        tab.setText("ABOUT");
                        break;
                }
            }
        }).attach();
    }
    private void getIdChannelAndTransHomeChannel() {
        Intent getData = getIntent();
        idChannel = getData.getStringExtra(Util.EXTRA_ID_CHANNEL_TO_CHANNEL);
        titleChannel = getData.getStringExtra(Util.EXTRA_TITLE_CHANNEL_TO_CHANNEL);
        tvTitleChannel.setText(titleChannel);
        adapter.setData(idChannel);
    }

    private void setBnvChannel() {
        bnvChannel.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mn_home_channel:
                        getSupportFragmentManager().popBackStack(HomeFragment.TAG, 0);
                        break;
                    case R.id.mn_explore:

                        break;
                    case R.id.mn_subcription:

                        break;
                    case R.id.mn_notification:

                        break;
                    case R.id.mn_library:

                        break;
                }
                return false;

            }
        });
    }
}