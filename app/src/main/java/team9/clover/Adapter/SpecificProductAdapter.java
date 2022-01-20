package team9.clover.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

import team9.clover.Fragment.ProductDetailFragment;
import team9.clover.Model.ProductModel;
import team9.clover.Module.Reuse;
import team9.clover.R;

public class SpecificProductAdapter extends BaseAdapter {

    List<ProductModel> productList;

    public SpecificProductAdapter(List<ProductModel> productList) {
        this.productList = productList;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View contentView, ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_grid_product, null);
        ImageView mImage = view.findViewById(R.id.ivImage);
        MaterialTextView mTitle = view.findViewById(R.id.mtvProductTitle),
                mSize = view.findViewById(R.id.mtvSize),
                mPrice = view.findViewById(R.id.mtvTotal);

        ProductModel product = productList.get(position);
        if (product.getImage() != null) {
            Glide.with(view.getContext()).load(product.getImage().get(0)).into(mImage);
            mTitle.setText(product.getTitle());
            mPrice.setText(Reuse.vietnameseCurrency(product.getPrice()));
            mSize.setText(String.join("  ", product.getSize()));

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent("broadcast");
                    intent.putExtra(ProductDetailFragment.NAME, product);
                    LocalBroadcastManager.getInstance(view.getContext()).sendBroadcast(intent);
                }
            });
        } else {
            view.setClickable(false);
        }

        return view;
    }
}
