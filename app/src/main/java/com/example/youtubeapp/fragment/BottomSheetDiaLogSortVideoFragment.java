package com.example.youtubeapp.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.youtubeapp.R;
import com.example.youtubeapp.my_interface.IItemOnClickSortListener;
import com.example.youtubeapp.utiliti.Util;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetDiaLogSortVideoFragment extends BottomSheetDialogFragment {
    LinearLayout llSortNew, llSortPopular, llCancel;
    IItemOnClickSortListener onClickSortListener;
    ImageView ivSortDate, ivSortMost;
    public static ChannelVideoFragment fragmentC;

    public static BottomSheetDiaLogSortVideoFragment newInstance(ChannelVideoFragment fragment) {
        BottomSheetDiaLogSortVideoFragment bottomSheetDiaLogSortVideoFragment =
                new BottomSheetDiaLogSortVideoFragment();
        // lấy được fragment call dialog này
        fragmentC = fragment;

        return bottomSheetDiaLogSortVideoFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog =
                (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View viewDialog = LayoutInflater.from(getContext()).inflate(
                R.layout.fragment_botton_sheet_dia_log_sort_video, null);
        bottomSheetDialog.setContentView(viewDialog);

        initView(viewDialog);
        if (Util.REQUEST_CODE_SORT_VIDEO == 111) {
            ivSortDate.setVisibility(View.VISIBLE);
            ivSortMost.setVisibility(View.INVISIBLE);
        } else {
            ivSortDate.setVisibility(View.INVISIBLE);
            ivSortMost.setVisibility(View.VISIBLE);
        }
//        bottomSheetDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
//                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
//        bottomSheetDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        llCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        llSortNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSortListener.onClickSortDate();
                Util.REQUEST_CODE_SORT_VIDEO = 111;
                bottomSheetDialog.dismiss();
            }
        });

        llSortPopular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSortListener.onClickSortMostPopular();
                Util.REQUEST_CODE_SORT_VIDEO = 112;
                bottomSheetDialog.dismiss();
            }
        });


        return bottomSheetDialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Kiểm tra xem fragmentC này có thuộc kiểu Interface này hay không, nếu thuộc thì cho interface có kiểu đó
        if (fragmentC instanceof IItemOnClickSortListener) {
            onClickSortListener =  fragmentC;
        } else {
            throw new RuntimeException("you must implement IItemOnClickSortListener");
        }
//        onClickSortListener = new ChannelVideoFragment();
    }

    private void initView(View view) {
        llSortNew = view.findViewById(R.id.ll_sort_date_newest);
        llSortPopular = view.findViewById(R.id.ll_sort_most_popular);
        llCancel = view.findViewById(R.id.ll_cancel_sort);
        ivSortDate = view.findViewById(R.id.iv_check_sort_date);
        ivSortMost = view.findViewById(R.id.iv_check_sort_most);
    }
}