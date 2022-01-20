package team9.clover.Fragment;

import static team9.clover.Model.DatabaseModel.masterUser;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import team9.clover.Adapter.ProductDetailAdapter;
import team9.clover.Adapter.ProductImageAdapter;
import team9.clover.Adapter.SliderProductAdapter;
import team9.clover.MainActivity;
import team9.clover.Model.CartItemModel;
import team9.clover.Model.DatabaseModel;
import team9.clover.Model.ProductModel;
import team9.clover.Module.Reuse;
import team9.clover.R;

public class ProductDetailFragment extends Fragment {

    public final static String NAME = "ProductDetail";
    public final static int ID = 3;

    ViewPager mImageViewPager;
    ViewPager2 mMoreViewPager;
    TabLayout mIndicator, mMore;
    FloatingActionButton mFavourite;
    MaterialTextView mTitle, mSize, mPrice, mCutPrice;
    MaterialButton mAddCart;
    RecyclerView mMoreProductContainer;
    ActionBar actionBar;

    String selectedSize;
    int quantity;
    boolean isChanged = false;

    ProductModel productModel;

    public ProductDetailFragment(ActionBar actionBar, ProductModel productModel) {
        this.actionBar = actionBar;
        this.productModel = productModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);

        refer(view);
        setView1();
        setView2(view);
        setEvent();

        return view;
    }

    private void refer(View view) {
        mImageViewPager = view.findViewById(R.id.vpImagesContainer);
        mIndicator = view.findViewById(R.id.tlIndicator);
        mTitle = view.findViewById(R.id.mtvProductTitle);
        mSize = view.findViewById(R.id.mtvSize);
        mPrice = view.findViewById(R.id.mtvTotal);
        mCutPrice = view.findViewById(R.id.cutBar);
        mFavourite = view.findViewById(R.id.fabFavorite);
        mMore = view.findViewById(R.id.tlMore);
        mMoreViewPager = view.findViewById(R.id.vpMore);
        mAddCart = view.findViewById(R.id.mbAddCart);
        mMoreProductContainer = view.findViewById(R.id.rvContainer);
    }

    private void setView1() {
        ProductImageAdapter adapter = new ProductImageAdapter(productModel.getImage());
        mImageViewPager.setAdapter(adapter);
        mIndicator.setupWithViewPager(mImageViewPager, true);

        mTitle.setText(productModel.getTitle());
        mSize.setText(String.join("  ", productModel.getSize()));
        mPrice.setText(Reuse.vietnameseCurrency(productModel.getPrice()));
        mFavourite.setTag(0);

        mMoreViewPager.setAdapter(new ProductDetailAdapter(getChildFragmentManager(), getLifecycle(), mMore.getTabCount(), productModel));

        if (!productModel.getCutPrice().isEmpty()) {
            mCutPrice.setText(productModel.getCutPrice());
        } else {
            mCutPrice.setVisibility(View.GONE);
        }

        if (masterUser != null && masterUser.getFavorite().contains((String) productModel.getId())) {
            mFavourite.setImageResource(R.drawable.icon_filled_heart);
            mFavourite.setTag(1);
        }
    }

    private void setView2(View view) {
        MaterialTextView title = view.findViewById(R.id.mtvTitle);
        title.setText("Sản phẩm tương tự");
        view.findViewById(R.id.mbViewAll).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.mbViewAll).setClickable(false);

        DatabaseModel.loadProduct("category", productModel.getCategory())
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<ProductModel> products = new ArrayList<>();
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                ProductModel product = snapshot.toObject(ProductModel.class);
                                if (product.getId().equals(productModel.getId())) continue;
                                products.add(snapshot.toObject(ProductModel.class));
                            }

                            SliderProductAdapter adapter = new SliderProductAdapter(products);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                            mMoreProductContainer.setLayoutManager(layoutManager);
                            mMoreProductContainer.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void setEvent() {
        mFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DatabaseModel.masterUid.isEmpty()) {
                    Toast.makeText(getContext(), "Bạn chưa đăng nhập.", Toast.LENGTH_SHORT).show();
                } else {
                    if ((int) mFavourite.getTag() == 0) {
                        mFavourite.setTag(1);
                        mFavourite.setImageResource(R.drawable.icon_filled_heart);
                    } else {
                        mFavourite.setTag(0);
                        mFavourite.setImageResource(R.drawable.icon_empty_heart);
                    }
                }
            }
        });

        mMore.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mMoreViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        mMoreViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                mMore.selectTab(mMore.getTabAt(position));
            }
        });

        mAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DatabaseModel.masterUid.isEmpty()) {
                    Toast.makeText(getContext(), "Bạn chưa đăng nhập.", Toast.LENGTH_SHORT).show();
                } else {
                    showChooseForm();
                }
            }
        });
    }

    private void showChooseForm() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_add_cart);

        final Spinner spinner = dialog.findViewById(R.id.spSize);
        final EditText editText = dialog.findViewById(R.id.etQuantity);
        final MaterialButton button = dialog.findViewById(R.id.mbConfirm);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, productModel.getSize());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedSize = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity = Integer.parseInt(editText.getText().toString());
                if (quantity > 0) {
                    dialog.dismiss();
                    Toast.makeText(getContext(), "Đã thêm sản phẩm vào giỏ hàng", Toast.LENGTH_LONG).show();
                    isChanged = true;

                    if (!DatabaseModel.updateMasterCart(productModel.getId(), selectedSize, quantity, null)) {
                        CartItemModel newCart = new CartItemModel(
                                productModel.getId(),
                                productModel.getTitle(),
                                productModel.getImage().get(0),
                                productModel.getPrice(), selectedSize, quantity);

                        DatabaseModel.updateMasterCart(productModel.getId(), selectedSize, quantity, newCart);
                    }
                } else {
                    editText.setError("> 0");
                }
            }
        });

        dialog.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!DatabaseModel.masterUid.isEmpty()) {
            if ((int) mFavourite.getTag() == 1 && !masterUser.getFavorite().contains(productModel.getId())) {
                masterUser.addFavorite(productModel.getId());
                DatabaseModel.updateMasterUser();
            } else if ((int) mFavourite.getTag() == 0 && masterUser.getFavorite().contains(productModel.getId())) {
                masterUser.removeFavorite(productModel.getId());
                DatabaseModel.updateMasterUser();
            }

            if (isChanged) {
                DatabaseModel.updateMasterCart();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setActionBar();
        isChanged = false;
    }

    private void setActionBar() {
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        MainActivity.toggle.setDrawerIndicatorEnabled(false);
        MainActivity.displayActionBarMenu(true);
        MainActivity.displayCategory(false);

        MainActivity.keyboard.hideSoftInputFromWindow(MainActivity.frameLayout.getWindowToken(), 0);
    }
}