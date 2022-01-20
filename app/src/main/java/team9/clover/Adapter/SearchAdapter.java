package team9.clover.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import team9.clover.Fragment.ProductDetailFragment;
import team9.clover.Model.DatabaseModel;
import team9.clover.Model.ProductModel;
import team9.clover.Module.Reuse;
import team9.clover.R;

public class SearchAdapter extends BaseAdapter implements Filterable {

    boolean canSearch = true;
    List<ProductModel> productList = new ArrayList<>();

    public SearchAdapter() { }

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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String keyword = charSequence.toString().trim();

                if (canSearch && !keyword.isEmpty()) {
                    canSearch = false;
                    List<String> keywords = Arrays.asList(keyword.split(" "));

                    DatabaseModel.searchProduct(keywords).addOnCompleteListener(task -> {
                        canSearch = true;
                        List<ProductModel> products = new ArrayList<>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                ProductModel productModel = snapshot.toObject(ProductModel.class);
                                products.add(productModel);
                            }
                        } else {
                            Log.v("db", task.toString());
                        }

                        productList = products;
                    });
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = productList;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                productList = (List<ProductModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
