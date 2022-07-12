package com.example.youtubeapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtubeapp.R;
import com.example.youtubeapp.my_interface.IItemOnClickHintListener;
import com.example.youtubeapp.my_interface.IItemOnClickSearchListener;

import java.util.ArrayList;

public class HintAdapter extends RecyclerView.Adapter<HintAdapter.HintViewHolder> {
    ArrayList<String> listHint;
    IItemOnClickHintListener onClickHintListener;
    IItemOnClickSearchListener onClickSearchListener;

    public HintAdapter(IItemOnClickHintListener onClickHintListener,
                       IItemOnClickSearchListener onClickSearchListener) {
        this.onClickHintListener = onClickHintListener;
        this.onClickSearchListener = onClickSearchListener;
    }

    public void setData(ArrayList<String> listHint) {
        this.listHint = listHint;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public HintViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_hint, parent, false);
        return new HintViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HintViewHolder holder, int position) {
        String hint = listHint.get(position);
        if (hint == null) {
            return;
        }

        holder.tvHintSearch.setText(hint);
        holder.ibGetHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickHintListener.onClickListener(hint);
            }
        });

        holder.llOpenSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSearchListener.onClickSearchListener(hint);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listHint != null) {
            return listHint.size();
        }
        return 0;
    }

    class HintViewHolder extends RecyclerView.ViewHolder {
        TextView tvHintSearch;
        ImageView ibGetHint;
        LinearLayout llOpenSearch;
        public HintViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHintSearch = itemView.findViewById(R.id.tv_hint_search);
            ibGetHint = itemView.findViewById(R.id.ib_get_hint);
            llOpenSearch = itemView.findViewById(R.id.ll_open_search);
        }
    }
}
