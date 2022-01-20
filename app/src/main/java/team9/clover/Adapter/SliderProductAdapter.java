package team9.clover.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

import team9.clover.Fragment.ProductDetailFragment;
import team9.clover.Model.ProductModel;
import team9.clover.Module.Reuse;
import team9.clover.R;

public class SliderProductAdapter extends RecyclerView.Adapter<SliderProductAdapter.ViewHolder> {

    List<ProductModel> productModelList;

    public SliderProductAdapter(List<ProductModel> productModelList) {
        this.productModelList = productModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slider_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position < productModelList.size())
            holder.set(productModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return 8;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImage;
        MaterialTextView mTitle, mSize, mPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.ivImage);
            mTitle = itemView.findViewById(R.id.mtvProductTitle);
            mSize = itemView.findViewById(R.id.mtvSize);
            mPrice = itemView.findViewById(R.id.mtvTotal);
        }

        private void set(ProductModel productModel) {
            Glide.with(itemView.getContext()).load(productModel.getImage().get(0)).into(mImage);
            mTitle.setText(productModel.getTitle());
            mSize.setText(String.join("  ", productModel.getSize()));
            mPrice.setText(Reuse.vietnameseCurrency(productModel.getPrice()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent("broadcast");
                    intent.putExtra(ProductDetailFragment.NAME, productModel);
                    LocalBroadcastManager.getInstance(itemView.getContext()).sendBroadcast(intent);
                }
            });
        }
    }
}
