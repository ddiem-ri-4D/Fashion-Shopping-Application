package team9.clover.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import team9.clover.Fragment.ProductDetailMoreFragment;
import team9.clover.Model.ProductModel;

public class ProductDetailAdapter extends FragmentStateAdapter{

    int noTabs;
    ProductModel product;

    public ProductDetailAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, int noTabs, ProductModel product) {
        super(fragmentManager, lifecycle);
        this.noTabs = noTabs;
        this.product = product;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ProductDetailMoreFragment(product.getDescription());
            case 1:
                return new ProductDetailMoreFragment(product.getInfo());
            case 2:
                return new ProductDetailMoreFragment(product.getBodyName(), product.getSize(), product.getMeasure());

            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return noTabs;
    }
}