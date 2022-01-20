package team9.clover.Fragment;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import team9.clover.Adapter.OrderDetailAdapter;
import team9.clover.MainActivity;
import team9.clover.Model.CartItemModel;
import team9.clover.Model.DatabaseModel;
import team9.clover.Model.OrderModel;
import team9.clover.Module.Reuse;
import team9.clover.R;

public class OrderDetailFragment extends Fragment {

    public final static String NAME = "OrderDetail";
    public final static int ID = 9;

    OrderModel orderModel;

    ActionBar actionBar;
    RecyclerView mContainer;
    MaterialTextView mCode, mTotal, mNoProducts, mFullName, mPhone, mAddress;
    MaterialTextView mOrder, mOrderDate, mDelivery, mDeliveryDate, mDone, mDoneDate;
    ImageView mImage1, mImage2, mImage3;
    ProgressBar mBar1, mBar2;


    public OrderDetailFragment(ActionBar actionBar, OrderModel orderModel) {
        this.actionBar = actionBar;
        this.orderModel = orderModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_detail, container, false);

        refer(view);
        setData();

        return view;
    }

    private void refer(View view) {
        mContainer = view.findViewById(R.id.rvContainer);
        mCode = view.findViewById(R.id.mtvCode);
        mTotal = view.findViewById(R.id.mtvTotal);
        mNoProducts = view.findViewById(R.id.mtvNoProduct);
        mFullName = view.findViewById(R.id.mtvFullName);
        mPhone = view.findViewById(R.id.mtvPhone);
        mAddress = view.findViewById(R.id.mtvAddress);
        mOrder = view.findViewById(R.id.mtvOrder);
        mOrderDate = view.findViewById(R.id.mtvOrderDate);
        mDelivery = view.findViewById(R.id.mtvDelivery);
        mDeliveryDate = view.findViewById(R.id.mtvDeliveryDate);
        mDone = view.findViewById(R.id.mtvDone);
        mDoneDate = view.findViewById(R.id.mtvDoneDate);
        mImage1 = view.findViewById(R.id.ordered_indicator);
        mImage2 = view.findViewById(R.id.packed_indicator);
        mImage3 = view.findViewById(R.id.shipping_indicator);
        mBar1 = view.findViewById(R.id.pbOne);
        mBar2 = view.findViewById(R.id.pbTwo);
    }

    private void setData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mContainer.setLayoutManager(layoutManager);

        DatabaseModel.loadOrderDetail(orderModel.getId()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<CartItemModel> carts = new ArrayList<>();
                for (QueryDocumentSnapshot snapshot : task.getResult())
                    carts.add(snapshot.toObject(CartItemModel.class));
                mContainer.setAdapter(new OrderDetailAdapter(carts));
            }
        });

        int dateSize = orderModel.getDate().size();
        if (dateSize > 1) {
            mOrder.setText("Đặt hàng thành công");
            mOrder.setTextColor(getContext().getColor(R.color.black));
            mOrderDate.setText(orderModel.getDate().get(0));
            mBar1.setProgressTintList(ColorStateList.valueOf(getContext().getColor(R.color.black)));

            mDelivery.setText("Đang vận chuyển");
            mDelivery.setTextColor(getContext().getColor(R.color.black));
            mDeliveryDate.setText(orderModel.getDate().get(1));
            mImage2.setImageTintList(ColorStateList.valueOf(getContext().getColor(R.color.black)));

            if (dateSize > 2) {
                mDelivery.setText("Hoàn thành");
                mDone.setText("Giao hàng thành công");
                mDone.setTextColor(getContext().getColor(R.color.black));
                mDoneDate.setText(orderModel.getDate().get(2));
                mBar2.setProgressTintList(ColorStateList.valueOf(getContext().getColor(R.color.black)));
                mImage3.setImageTintList(ColorStateList.valueOf(getContext().getColor(R.color.black)));
            }
        }

        mFullName.setText(orderModel.getFullName());
        mTotal.setText(Reuse.vietnameseCurrency(orderModel.getTotal()));
        mNoProducts.setText(String.format("(x%d sản phẩm)", orderModel.getNoProducts()));
        mCode.setText("#"+orderModel.takeId());
        mPhone.setText(orderModel.getPhone());
        mAddress.setText(orderModel.getAddress());
    }

    @Override
    public void onResume() {
        super.onResume();
        setActionBar();
    }

    private void setActionBar() {
        actionBar.setTitle("Chi tiết đơn hàng");
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        MainActivity.toggle.setDrawerIndicatorEnabled(false);
        MainActivity.displayActionBarMenu(false);
        MainActivity.displayCategory(false);
    }
}