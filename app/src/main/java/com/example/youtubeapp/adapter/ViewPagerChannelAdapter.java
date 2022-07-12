package com.example.youtubeapp.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.youtubeapp.utiliti.Util;
import com.example.youtubeapp.fragment.ChannelAboutFragment;
import com.example.youtubeapp.fragment.ChannelChannelsFragment;
import com.example.youtubeapp.fragment.ChannelCommunityFragment;
import com.example.youtubeapp.fragment.ChannelHomeFragment;
import com.example.youtubeapp.fragment.ChannelPlayListFragment;
import com.example.youtubeapp.fragment.ChannelVideoFragment;

public class ViewPagerChannelAdapter extends FragmentStateAdapter {
    String idChannel = "";
    public ViewPagerChannelAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public void setData(String idChannel) {
        this.idChannel = idChannel;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Bundle bundle = new Bundle();
        bundle.putString(Util.EXTRA_ID_CHANNEL_TO_CHANNEL_FROM_ADAPTER, idChannel);

        switch (position) {
            case 0 :
                ChannelHomeFragment channelHomeFragment = new ChannelHomeFragment();
                channelHomeFragment.setArguments(bundle);

                return channelHomeFragment;
            case 1 :
                ChannelVideoFragment channelVideoFragment = new ChannelVideoFragment();
                channelVideoFragment.setArguments(bundle);

                return channelVideoFragment;
            case 2 :
                ChannelPlayListFragment channelPlayListFragment = new ChannelPlayListFragment();
                channelPlayListFragment.setArguments(bundle);
                return channelPlayListFragment;
            case 3 :
                return new ChannelCommunityFragment();
            case 4 :
                ChannelChannelsFragment channelChannelsFragment = new ChannelChannelsFragment();
                channelChannelsFragment.setArguments(bundle);
                return channelChannelsFragment;
            case 5 :
                ChannelAboutFragment channelAboutFragment = new ChannelAboutFragment();
                channelAboutFragment.setArguments(bundle);
                return channelAboutFragment;
            default:
                return new ChannelHomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 6;
    }
}
