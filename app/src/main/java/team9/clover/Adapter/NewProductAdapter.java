package team9.clover.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import team9.clover.Fragment.ProductDetailFragment;
import team9.clover.Model.ProductModel;
import team9.clover.R;

public class NewProductAdapter extends RecyclerView.Adapter<NewProductAdapter.ViewHolder> {

    List<ProductModel> productModelList;

    public NewProductAdapter(List<ProductModel> productModelList) {
        this.productModelList = productModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.set(productModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView mInage;
        MaterialTextView mTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mInage = itemView.findViewById(R.id.rivImage);
            mTitle = itemView.findViewById(R.id.mtvProductTitle);
        }

        private void set(ProductModel productModel) {
            Glide.with(itemView.getContext()).load(productModel.getImage().get(0)).into(mInage);
            mTitle.setText(productModel.getTitle());

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
