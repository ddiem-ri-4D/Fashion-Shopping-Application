package team9.clover.Adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class ProductImageAdapter extends PagerAdapter {
    
    List<String> productImageList;
    
    public ProductImageAdapter(List<String> productImageList) {
        this.productImageList = productImageList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        RoundedImageView productImage = new RoundedImageView(container.getContext());
        productImage.setCornerRadius((float) 30);
        Glide.with(container.getContext()).load(productImageList.get(position)).into(productImage);
        container.addView(productImage, 0);
        return productImage;
    }

    @Override
    public int getCount() {
        return productImageList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RoundedImageView) object);
    }
}
