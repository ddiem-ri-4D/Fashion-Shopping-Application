package team9.clover.Adapter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.gridlayout.widget.GridLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import team9.clover.Fragment.ProductDetailFragment;
import team9.clover.Fragment.ViewMoreFragment;
import team9.clover.Model.BannerModel;
import team9.clover.Model.CarouselModel;
import team9.clover.Model.HomePageModel;
import team9.clover.Model.ProductModel;
import team9.clover.Module.Reuse;
import team9.clover.R;

public class HomePageAdapter extends RecyclerView.Adapter {

    List<HomePageModel> homePageModelList;
    RecyclerView.RecycledViewPool recycledViewPool;
    RecyclerView.RecycledViewPool recycledViewPool2;


    public HomePageAdapter(List<HomePageModel> homePageModelList) {
        this.homePageModelList = homePageModelList;
        recycledViewPool = new RecyclerView.RecycledViewPool();
        recycledViewPool2 = new RecyclerView.RecycledViewPool();
        this.homePageModelList.sort(Comparator.comparing(HomePageModel::getType));
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case HomePageModel.CAROUSEL_VIEW_TYPE:
                return new CarouselViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.container_carousel, parent, false));

            case HomePageModel.BANNER_VIEW_TYPE:
                return new BannerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.container_banner, parent, false));

            case HomePageModel.NEW_PRODUCT_VIEW_TYPE:
                return new NewProductViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.container_new_product, parent, false));

            case HomePageModel.SLIDER_PRODUCT_VIEW_TYPE:
                return new SliderProductViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.container_slider_product, parent, false));

            case HomePageModel.GRID_PRODUCT_VIEW_TYPE:
                return new GridProductViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.container_grid_product, parent, false));

            case HomePageModel.FOOTER_VIEW_TYPE:
                return new FooterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_footer, parent, false));

            default:
                return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (homePageModelList.get(position).getType()) {
            case HomePageModel.CAROUSEL_VIEW_TYPE:
                return HomePageModel.CAROUSEL_VIEW_TYPE;

            case HomePageModel.BANNER_VIEW_TYPE:
                return HomePageModel.BANNER_VIEW_TYPE;

            case HomePageModel.NEW_PRODUCT_VIEW_TYPE:
                return HomePageModel.NEW_PRODUCT_VIEW_TYPE;

            case HomePageModel.SLIDER_PRODUCT_VIEW_TYPE:
                return HomePageModel.SLIDER_PRODUCT_VIEW_TYPE;

            case HomePageModel.GRID_PRODUCT_VIEW_TYPE:
                return HomePageModel.GRID_PRODUCT_VIEW_TYPE;

            case HomePageModel.FOOTER_VIEW_TYPE:
                return HomePageModel.FOOTER_VIEW_TYPE;

            default:
                return -1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (homePageModelList.get(position).getType()) {
            case HomePageModel.CAROUSEL_VIEW_TYPE:
                ((CarouselViewHolder) holder).set(homePageModelList.get(position).getCarouselModelList());
                break;

            case HomePageModel.BANNER_VIEW_TYPE:
                BannerModel bannerModel = homePageModelList.get(position).getBanner();
                ((BannerViewHolder) holder).set(bannerModel.getImage(), bannerModel.getPadding());
                break;

            case HomePageModel.NEW_PRODUCT_VIEW_TYPE:
                ((NewProductViewHolder) holder).set(homePageModelList.get(position).getProductModelList());
                break;

            case HomePageModel.SLIDER_PRODUCT_VIEW_TYPE:
                HomePageModel sliderProduct = homePageModelList.get(position);
                ((SliderProductViewHolder) holder).set(
                        sliderProduct.getProductModelList(),
                        sliderProduct.getIcon(),
                        sliderProduct.getTitle());
                break;

            case HomePageModel.GRID_PRODUCT_VIEW_TYPE:
                HomePageModel gridProduct = homePageModelList.get(position);
                ((GridProductViewHolder) holder).set(
                        gridProduct.getProductModelList(),
                        gridProduct.getIcon(),
                        gridProduct.getTitle());
                break;

            default:
                return;
        }
    }

    @Override
    public int getItemCount() {
        return homePageModelList.size();
    }

    public class CarouselViewHolder extends RecyclerView.ViewHolder {

        ViewPager mContainer;
        Timer timer;
        int currentPage;
        List<CarouselModel> carouselModelList;

        public CarouselViewHolder(@NonNull View itemView) {
            super(itemView);
            mContainer = itemView.findViewById(R.id.vpContainer);

        }

        private void set(final List<CarouselModel> carouselModels) {
            currentPage = 2;
            if (timer != null) {
                timer.cancel();
            }

            carouselModelList = new ArrayList<>();
            for (int x = 0; x < carouselModels.size(); ++x) {
                carouselModelList.add(x, carouselModels.get(x));
            }

            carouselModelList.add(0, carouselModels.get(carouselModels.size() - 2));
            carouselModelList.add(1, carouselModels.get(carouselModels.size() - 1));
            carouselModelList.add(carouselModels.get(0));
            carouselModelList.add(carouselModels.get(1));


            CarouselAdapter adapter = new CarouselAdapter(carouselModelList);
            mContainer.setAdapter(adapter);
            mContainer.setClipToPadding(false);
            mContainer.setPageMargin(20);
            mContainer.setCurrentItem(currentPage);
            ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    currentPage = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if (state == ViewPager.SCROLL_STATE_IDLE) {
                        pageLooper(carouselModelList);
                    }
                }
            };
            mContainer.addOnPageChangeListener(onPageChangeListener);
            startCarousel(carouselModelList);

            mContainer.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    pageLooper(carouselModelList);
                    stopCarousel();
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) startCarousel(carouselModelList);
                    return false;
                }
            });
        }

        private void pageLooper(List<CarouselModel> carouselModelList) {
            if (currentPage == carouselModelList.size() - 2) {
                currentPage = 2;
                mContainer.setCurrentItem(currentPage, false);
            }

            if (currentPage == 1) {
                currentPage = carouselModelList.size() - 3;
                mContainer.setCurrentItem(currentPage, false);
            }
        }

        private void startCarousel(List<CarouselModel> carouselModelList) {
            Handler handler = new Handler();
            Runnable update = new Runnable() {
                @Override
                public void run() {
                    if (currentPage >= carouselModelList.size()) currentPage = 1;
                    mContainer.setCurrentItem(currentPage++, true);
                }
            };

            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(update);
                }
            }, 3000, 3000);
        }

        private void stopCarousel() {
            timer.cancel();
        }
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder {

        ImageView mImage;
        ConstraintLayout mContainer;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.ivImage);
            mContainer = itemView.findViewById(R.id.clContainer);
        }

        private void set(String imageUrl, String color) {
            Glide.with(itemView.getContext()).load(imageUrl).into(mImage);
            mContainer.setBackgroundColor(Color.parseColor(color));
        }
    }

    public class NewProductViewHolder extends RecyclerView.ViewHolder {

        RecyclerView mContainer;

        public NewProductViewHolder(@NonNull View itemView) {
            super(itemView);
            mContainer = itemView.findViewById(R.id.rvContainer);
            mContainer.setRecycledViewPool(recycledViewPool2);
        }

        private void set(List<ProductModel> productModelList) {
            NewProductAdapter adapter = new NewProductAdapter(productModelList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext());
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            mContainer.setLayoutManager(layoutManager);
            mContainer.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    public class SliderProductViewHolder extends RecyclerView.ViewHolder {

        MaterialTextView mTitle;
        MaterialButton mViewAll;
        RecyclerView mContainer;

        public SliderProductViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.mtvTitle);
            mViewAll = itemView.findViewById(R.id.mbViewAll);
            mContainer = itemView.findViewById(R.id.rvContainer);
            mContainer.setRecycledViewPool(recycledViewPool);
        }

        private void set(List<ProductModel> productModelList, int icon, String title) {
            mTitle.setText(title);
            mTitle.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0);

            SliderProductAdapter adapter = new SliderProductAdapter(productModelList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext());
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            mContainer.setLayoutManager(layoutManager);
            mContainer.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            mViewAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent("broadcast");
                    intent.putExtra(ViewMoreFragment.NAME, HomePageModel.SLIDER_PRODUCT_VIEW_TYPE);
                    LocalBroadcastManager.getInstance(itemView.getContext()).sendBroadcast(intent);
                }
            });
        }
    }

    public class GridProductViewHolder extends RecyclerView.ViewHolder {

        MaterialTextView mTitle;
        MaterialButton mViewAll;
        GridLayout mContainer;

        public GridProductViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.mtvProductTitle);
            mViewAll = itemView.findViewById(R.id.mbViewAll);
            mContainer = itemView.findViewById(R.id.glContainer);
        }

        public void set(List<ProductModel> productModelList, int icon, String title) {
            mTitle.setText(title);
            mTitle.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0);

            for (int i = 0; i < 4; ++i) {
                View mChild = mContainer.getChildAt(i);
                ImageView mImageItem = mChild.findViewById(R.id.ivImage);
                MaterialTextView mTitleItem = mChild.findViewById(R.id.mtvProductTitle),
                        mSizeItem = mChild.findViewById(R.id.mtvSize),
                        mPriceItem = mChild.findViewById(R.id.mtvTotal);

                ProductModel productModel = productModelList.get(i);
                Glide.with(itemView.getContext()).load(productModel.getImage().get(0)).into(mImageItem);
                mTitleItem.setText(productModel.getTitle());
                mSizeItem.setText(String.join("  ", productModel.getSize()));
                mPriceItem.setText(Reuse.vietnameseCurrency(productModel.getPrice()));

                /*
                * Sự kiện khi user nhấn vào sản phẩm tương ứng sẽ đưa user đến trang chi tiết của sản phẩm đó
                * */
                mChild.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent("broadcast");
                        intent.putExtra(ProductDetailFragment.NAME, productModel);
                        LocalBroadcastManager.getInstance(itemView.getContext()).sendBroadcast(intent);
                    }
                });
            }

            /*
            * Sự kiện khi user nhấn vào button xem thêm
            * */
            mViewAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent("broadcast");
                    intent.putExtra(ViewMoreFragment.NAME, HomePageModel.GRID_PRODUCT_VIEW_TYPE);
                    LocalBroadcastManager.getInstance(itemView.getContext()).sendBroadcast(intent);
                }
            });
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}