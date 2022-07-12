package com.example.youtubeapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtubeapp.R;
import com.example.youtubeapp.model.itemrecycleview.CategoryItem;
import com.example.youtubeapp.my_interface.IItemOnClickCategoryListener;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CateViewHolder> {
    IItemOnClickCategoryListener onClickCategoryListener;
    ArrayList<CategoryItem> listCate;
    Context context;
    private int previousPosition = 0;

    public CategoryAdapter(Context context, IItemOnClickCategoryListener onClickCategoryListener) {
        this.context = context;
        this.onClickCategoryListener = onClickCategoryListener;
    }

    public void setData(ArrayList<CategoryItem> listCate) {
        this.listCate = listCate;
    }


    public void setPosition(int pos) {
        this.previousPosition = pos;
    }
    @NonNull
    @Override
    public CateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new CateViewHolder(view);
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "ResourceType"})
    @Override
    public void onBindViewHolder(@NonNull CateViewHolder holder, int position) {
        CategoryItem cate = listCate.get(position);
        if (cate == null) {
            return;
        }
        holder.setData(cate);

        String idCate = cate.getIdCategory();
        holder.btCate.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                previousPosition = holder.getAdapterPosition() ;
                notifyDataSetChanged();
                onClickCategoryListener.onClickCategory(idCate);
            }
        });
        if (previousPosition == position) {
//            holder.btCate.setText(R.color.white);
            holder.btCate.setBackground(context.getResources().getDrawable(
                    R.drawable.custom_click_category
            ));
        } else {
            holder.btCate.setBackground(context.getResources().getDrawable(
                    R.drawable.custom_not_click_category));
        }

    }

    @Override
    public int getItemCount() {
        if (listCate != null) {
            return listCate.size();
        }
        return 0;
    }

    class CateViewHolder extends RecyclerView.ViewHolder {
        private AppCompatButton btCate;
        public CateViewHolder(@NonNull View itemView) {
            super(itemView);
            btCate = itemView.findViewById(R.id.bt_category);
        }
        @SuppressLint("ResourceAsColor")
        public void setData(CategoryItem cate) {
            btCate.setText(cate.getTitleCate());
        }
    }

}
