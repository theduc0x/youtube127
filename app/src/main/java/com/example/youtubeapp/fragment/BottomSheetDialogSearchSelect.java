package com.example.youtubeapp.fragment;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.example.youtubeapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetDialogSearchSelect extends BottomSheetDialogFragment {
    LinearLayout llOpenFilter, llFeedBack, llCancelFilter;
    public static SearchResultsFragment fragmentS;
    public static BottomSheetDialogSearchSelect newInstance(SearchResultsFragment fragment) {
        BottomSheetDialogSearchSelect searchFilterFragment =
                new BottomSheetDialogSearchSelect();
        fragmentS = fragment;
        return searchFilterFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog =
                (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(
                R.layout.fragment_bottom_sheet_dialog_search_select, null);
        bottomSheetDialog.setContentView(view);
        initView(view);

        llCancelFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        llOpenFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.hide();
                openBottomSheetFilterList();
            }
        });

        return bottomSheetDialog;
    }

    private void initView(View view) {
        llOpenFilter = view.findViewById(R.id.ll_open_filter_search);
        llFeedBack = view.findViewById(R.id.ll_feedBack);
        llCancelFilter = view.findViewById(R.id.ll_cancel_filter);
    }
    private void openBottomSheetFilterList() {

        BottomSheetDialogFilterSearchFragment searchFragment =
                BottomSheetDialogFilterSearchFragment.newInstance(fragmentS);
        searchFragment.show(getChildFragmentManager(), searchFragment.getTag());
    }
}