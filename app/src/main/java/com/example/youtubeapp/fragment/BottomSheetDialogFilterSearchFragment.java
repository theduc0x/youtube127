package com.example.youtubeapp.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.youtubeapp.R;
import com.example.youtubeapp.my_interface.IItemClickFilterSearch;
import com.example.youtubeapp.utiliti.Util;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetDialogFilterSearchFragment extends BottomSheetDialogFragment {
    ImageView ivBack;
    IItemClickFilterSearch onFilterSearch;
    LinearLayout llSBRelevance, llSBDate, llSBViewCount, llSBRating,
            llTypeAll, llTypeVideo, llTypeChannel, llTypePlayList, llTypeMovie,
            llUDAnytime, llUDLastHour, llUDToday, llUDThisWeek, llUDThisMonth, llUDThisYear,
            llDAny, llD4Minutes, llD420Minutes, llDOver20Minutes;
    ImageView ivCRelevance, ivCDate, ivCViewCount, ivCRating,
            ivTypeAll, ivTypeVideo, ivTypeChannel, ivTypePlayList, ivTypeMovie,
            ivUDAnytime, ivUDLastHour, ivUDToday, ivUDThisWeek, ivUDThisMonth, ivUDThisYear,
            ivDAny, ivD4Minutes, ivD420Minutes, ivDOver20Minutes;
    public static SearchResultsFragment fragmentS;

    String sortBy = "relevance", type = null, uploadDate = null, duration = "any",
                    videoType = null;

    public static BottomSheetDialogFilterSearchFragment newInstance(SearchResultsFragment fragment) {
        BottomSheetDialogFilterSearchFragment searchFragment =
                new BottomSheetDialogFilterSearchFragment();
        fragmentS = fragment;
        return searchFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog =
                (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(
                R.layout.fragment_bottom_sheet_dialog_filter_search, null);
        bottomSheetDialog.setContentView(view);


        // chiều cao của màn hình activiy chứa fragment
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        BottomSheetBehavior bottomSheetBehavior =
                BottomSheetBehavior.from(((View) view.getParent()));
        bottomSheetBehavior.setDraggable(false);
        bottomSheetBehavior.setPeekHeight(height);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        initView(view);
        onClickFilter();
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFilterSearch.onClickSearchFilter(sortBy, type, uploadDate, duration, videoType);
                bottomSheetDialog.dismiss();
            }
        });


        return bottomSheetDialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Kiểm tra xem fragmentC này có thuộc kiểu Interface này hay không, nếu thuộc thì cho interface có kiểu đó
        if (fragmentS instanceof IItemClickFilterSearch) {
            onFilterSearch = fragmentS;
        } else {
            throw new RuntimeException("you must implement IItemClickFilterSearch");
        }
    }

    private void checkSortBy(ImageView ivSortBy) {
        ivCRelevance.setVisibility(View.INVISIBLE);
        ivCDate.setVisibility(View.INVISIBLE);
        ivCViewCount.setVisibility(View.INVISIBLE);
        ivCRating.setVisibility(View.INVISIBLE);


        ivSortBy.setVisibility(View.VISIBLE);
    }

    private void checkType(ImageView ivType) {
        ivTypeAll.setVisibility(View.INVISIBLE);
        ivTypeVideo.setVisibility(View.INVISIBLE);
        ivTypeChannel.setVisibility(View.INVISIBLE);
        ivTypePlayList.setVisibility(View.INVISIBLE);
        ivTypeMovie.setVisibility(View.INVISIBLE);

        ivType.setVisibility(View.VISIBLE);
    }

    private void checkUploadDate(ImageView ivUploadDate) {
        ivUDAnytime.setVisibility(View.INVISIBLE);
        ivUDLastHour.setVisibility(View.INVISIBLE);
        ivUDToday.setVisibility(View.INVISIBLE);
        ivUDThisWeek.setVisibility(View.INVISIBLE);
        ivUDThisMonth.setVisibility(View.INVISIBLE);
        ivUDThisYear.setVisibility(View.INVISIBLE);

        ivUploadDate.setVisibility(View.VISIBLE);
    }

    private void checkDuration(ImageView ivDuration) {
        ivDAny.setVisibility(View.INVISIBLE);
        ivD4Minutes.setVisibility(View.INVISIBLE);
        ivD420Minutes.setVisibility(View.INVISIBLE);
        ivDOver20Minutes.setVisibility(View.INVISIBLE);

        ivDuration.setVisibility(View.VISIBLE);
    }

    private void onClickFilter() {
        // Sort By
        llSBRelevance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortBy = "relevance";
                checkSortBy(ivCRelevance);
            }
        });
        llSBDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortBy = "date";
                checkSortBy(ivCDate);
            }
        });
        llSBViewCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortBy = "viewCount";
                checkSortBy(ivCViewCount);
            }
        });
        llSBRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortBy = "rating";
                checkSortBy(ivCRating);
            }
        });
        // Type
        llTypeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoType = null;
                type = null;
                checkType(ivTypeAll);
            }
        });
        llTypeVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoType = null;
                type = "video";
                checkType(ivTypeVideo);
            }
        });
        llTypeChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoType = null;
                type = "channel";
                checkType(ivTypeChannel);
            }
        });
        llTypePlayList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoType = null;
                type = "playlist";
                checkType(ivTypePlayList);
            }
        });
        llTypeMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoType = "movie";
                type = "video";
                checkType(ivTypeMovie);
            }
        });
        // Upload Date
        llUDAnytime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadDate = null;
                checkUploadDate(ivUDAnytime);
            }
        });
        llUDLastHour.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                uploadDate = Util.getTimeFilter(Util.SECONDS_IN_1_HOUR);
                checkUploadDate(ivUDLastHour);
            }
        });
        llUDToday.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                uploadDate = Util.getTimeFilter(Util.SECONDS_IN_1_DAY);
                checkUploadDate(ivUDToday);
            }
        });
        llUDThisWeek.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                uploadDate = Util.getTimeFilter(Util.SECONDS_IN_1_WEEK);
                checkUploadDate(ivUDThisWeek);
            }
        });
        llUDThisMonth.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                uploadDate = Util.getTimeFilter(Util.SECONDS_IN_1_MONTH);
                checkUploadDate(ivUDThisMonth);
            }
        });
        llUDThisYear.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                uploadDate = Util.getTimeFilter(Util.SECONDS_IN_1_YEAR);
                checkUploadDate(ivUDThisYear);
            }
        });
//      Duration
        llDAny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                duration = "any";
                checkDuration(ivDAny);
            }
        });
        llD4Minutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "video";
                duration = "short";
                checkDuration(ivD4Minutes);
            }
        });
        llD420Minutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "video";
                duration = "medium";
                checkDuration(ivD420Minutes);
            }
        });
        llDOver20Minutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "video";
                duration = "long";
                checkDuration(ivDOver20Minutes);
            }
        });
    }




    private void initView(View view) {
        ivBack = view.findViewById(R.id.iv_back_filter);

        llSBRelevance = view.findViewById(R.id.ll_filter_relevance);
        llSBDate = view.findViewById(R.id.ll_filter_date);
        llSBViewCount = view.findViewById(R.id.ll_filter_view_count);
        llSBRating = view.findViewById(R.id.ll_filter_rating);

        llTypeAll = view.findViewById(R.id.ll_filter_all);
        llTypeVideo = view.findViewById(R.id.ll_filter_video);
        llTypeChannel = view.findViewById(R.id.ll_filter_channel);
        llTypePlayList = view.findViewById(R.id.ll_filter_playlist);
        llTypeMovie = view.findViewById(R.id.ll_filter_movie);

        llUDAnytime = view.findViewById(R.id.ll_filter_anytime);
        llUDLastHour = view.findViewById(R.id.ll_filter_last_hour);
        llUDToday = view.findViewById(R.id.ll_filtel_today);
        llUDThisWeek = view.findViewById(R.id.ll_filter_this_week);
        llUDThisMonth = view.findViewById(R.id.ll_filter_this_month);
        llUDThisYear = view.findViewById(R.id.ll_filter_this_year);

        llDAny = view.findViewById(R.id.ll_filter_any_duration);
        llD4Minutes = view.findViewById(R.id.ll_filter_4_m);
        llD420Minutes = view.findViewById(R.id.ll_filter_4_20_m);
        llDOver20Minutes = view.findViewById(R.id.ll_filter_over_20_m);

        ivCRelevance = view.findViewById(R.id.iv_check_relevance);
        ivCDate = view.findViewById(R.id.iv_check_date);
        ivCViewCount = view.findViewById(R.id.iv_check_view_count);
        ivCRating = view.findViewById(R.id.iv_check_rating);

        ivTypeAll = view.findViewById(R.id.iv_check_all);
        ivTypeVideo = view.findViewById(R.id.iv_check_video);
        ivTypeChannel = view.findViewById(R.id.iv_check_channel);
        ivTypePlayList = view.findViewById(R.id.iv_check_playlist);
        ivTypeMovie = view.findViewById(R.id.iv_check_movie);

        ivUDAnytime = view.findViewById(R.id.iv_check_anytime);
        ivUDLastHour = view.findViewById(R.id.iv_check_last_hour);
        ivUDToday = view.findViewById(R.id.iv_check_today);
        ivUDThisWeek = view.findViewById(R.id.iv_check_this_week);
        ivUDThisMonth = view.findViewById(R.id.iv_check_this_month);
        ivUDThisYear = view.findViewById(R.id.iv_check_this_year);

        ivDAny = view.findViewById(R.id.iv_check_any_duration);
        ivD4Minutes = view.findViewById(R.id.iv_check_4_m);
        ivD420Minutes = view.findViewById(R.id.iv_check_4_20_m);
        ivDOver20Minutes = view.findViewById(R.id.iv_check_over_20_m);
    }
}