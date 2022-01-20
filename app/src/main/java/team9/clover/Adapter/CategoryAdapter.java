package team9.clover.Adapter;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

import team9.clover.Fragment.SpecificProductFragment;
import team9.clover.Model.CategoryModel;
import team9.clover.R;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    List<CategoryModel> categoryList;
    public static int currentTab = 0;

    public CategoryAdapter(List<CategoryModel> categoryList) {
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        CategoryModel item = categoryList.get(position);
        holder.set(categoryList.get(position), position);

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImage;
        MaterialTextView mTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.ivImage);
            mTitle = itemView.findViewById(R.id.mtvProductTitle);
        }

        private void set(CategoryModel category, int position) {
            Glide.with(itemView.getContext())
                    .load(category.getImage())
                    .into(mImage);

            mTitle.setText(category.getTitle());

            itemView.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onClick(View view) {
                    if (currentTab != position) {
                        currentTab = position;
                        Intent intent = new Intent("broadcast");
                        intent.putExtra(SpecificProductFragment.NAME, position);
                        LocalBroadcastManager.getInstance(itemView.getContext()).sendBroadcast(intent);
                        notifyDataSetChanged();
                    }
                }
            });

            ConstraintLayout layout = itemView.findViewById(R.id.clContainer);
            if (position == currentTab) {
                layout.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.shape_border_transparent));
            } else {
                layout.setBackgroundColor(Color.WHITE);
            }
        }
    }
}
