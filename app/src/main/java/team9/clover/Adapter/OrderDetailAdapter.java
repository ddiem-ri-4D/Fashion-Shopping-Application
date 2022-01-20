package team9.clover.Adapter;

import android.content.Intent;
import android.util.Pair;
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
import team9.clover.Model.CartItemModel;
import team9.clover.Model.DatabaseModel;
import team9.clover.Model.ProductModel;
import team9.clover.Module.Reuse;
import team9.clover.R;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {

    List<CartItemModel> cartList;

    public OrderDetailAdapter(List<CartItemModel> cartList) {
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new OrderDetailAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.set(cartList.get(position));
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView mImage;
        MaterialTextView mTitle, mSize, mTotal, mPrice;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.findViewById(R.id.ibEdit).setVisibility(View.GONE);
            itemView.findViewById(R.id.ibDelete).setVisibility(View.GONE);

            mImage = itemView.findViewById(R.id.rivImage);
            mTitle = itemView.findViewById(R.id.mtvTitle);
            mSize = itemView.findViewById(R.id.mtvSize);
            mTotal = itemView.findViewById(R.id.mtvTotal);
            mPrice = itemView.findViewById(R.id.mtvPrice);
        }

        private void set(CartItemModel cart) {
            Glide.with(itemView.getContext()).load(cart.getImage()).into(mImage);
            Pair pair = cart.takeSize();
            mTitle.setText(cart.getTitle());
            mSize.setText(pair.first.toString());
            mTotal.setText(Reuse.vietnameseCurrency(cart.getTotal()));
            mPrice.setText(String.format("(x%d) ", pair.second)+ Reuse.vietnameseCurrency(cart.getPrice()));

            mImage.setOnClickListener(v -> {
                DatabaseModel.loadSingleProduct(cart.getId()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent("broadcast");
                        intent.putExtra(ProductDetailFragment.NAME, task.getResult().toObject(ProductModel.class));
                        LocalBroadcastManager.getInstance(itemView.getContext()).sendBroadcast(intent);
                    }
                });
            });
        }
    }
}
