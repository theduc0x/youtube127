package com.example.youtubeapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.youtubeapp.R;
import com.example.youtubeapp.adapter.ViewPagerChannelAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ChannelDetailFragment extends Fragment {
    TabLayout tlChannel;
    ViewPager2 vp2Content;
    ViewPagerChannelAdapter adapter;
    String idChannel = "", titleChannel = "";
    TextView tvTitleChannel;
    ImageButton ibBack;
    BottomNavigationView bnvChannel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel_detail, container, false);
        initView(view);
        setTabLayout();
        getIdChannelAndTransHomeChannel();
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
//        setBnvChannel();
        return view;
    }

    private void initView(View view) {
        tlChannel = view.findViewById(R.id.tl_channel);
        vp2Content = view.findViewById(R.id.vp2_content);
        tvTitleChannel = view.findViewById(R.id.tv_title_channel_nav);
        ibBack = view.findViewById(R.id.ib_back_home_channel);
    }

    private void setTabLayout() {
        adapter = new ViewPagerChannelAdapter(getActivity());
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
        Bundle bundleRe = getArguments();
        idChannel = bundleRe.getString("ID_CHANNEL");
        titleChannel = bundleRe.getString("TITLE_CHANNEL");
        tvTitleChannel.setText(titleChannel);
        adapter.setData(idChannel);
    }

//    private void setBnvChannel() {
//        bnvChannel.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
//            @SuppressLint("NonConstantResourceId")
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.mn_home:
////                        getParentFragmentManager().popBackStack("alo123", 0);
//                        break;
//                    case R.id.mn_explore:
//
//                        break;
//                    case R.id.mn_subcription:
//
//                        break;
//                    case R.id.mn_notification:
//
//                        break;
//                    case R.id.mn_library:
//
//                        break;
//                }
//                return false;
//
//            }
//        });
//    }
}