package team9.clover.Adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.List;

import team9.clover.Model.CarouselModel;
import team9.clover.R;

public class CarouselAdapter extends PagerAdapter {

    List<CarouselModel> carouselModelList;

    public CarouselAdapter(List<CarouselModel> carouselModelList) {
        this.carouselModelList = carouselModelList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        CarouselModel carouselModel = carouselModelList.get(position);
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_carousel, container, false);
        ImageView mImage = view.findViewById(R.id.ivImage);
        ConstraintLayout mContainer = view.findViewById(R.id.clContainer);
        mContainer.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(carouselModel.getPadding())));
        Glide.with(container.getContext()).load(carouselModel.getImage()).into(mImage);
        container.addView(view, 0);
        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return carouselModelList.size();
    }
}
