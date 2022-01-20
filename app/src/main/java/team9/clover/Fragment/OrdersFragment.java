package team9.clover.Fragment;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import team9.clover.Adapter.OrdersAdapter;
import team9.clover.MainActivity;
import team9.clover.Model.DatabaseModel;
import team9.clover.Model.OrderModel;
import team9.clover.R;

public class OrdersFragment extends Fragment {

    public static final int ID = 8;
    public static final String NAME = "Orders";

    ActionBar actionBar;
    RecyclerView mContainer;

    public OrdersFragment(ActionBar actionBar) {
        this.actionBar = actionBar;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_orders, container, false);

        mContainer = view.findViewById(R.id.rvContainer);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mContainer.setLayoutManager(layoutManager);

        DatabaseModel.loadOrders().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<OrderModel> orderList = new ArrayList<>();
                for (QueryDocumentSnapshot snapshot : task.getResult()) {
                    OrderModel order = snapshot.toObject(OrderModel.class);
                    if (order == null || order.getId().isEmpty()) continue;
                    orderList.add(order);
                }
                mContainer.setAdapter(new OrdersAdapter(orderList));
            }
        });

        return view;
    }

    private void setActionBar() {
        actionBar.setTitle("Đơn hàng");
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        MainActivity.toggle.setDrawerIndicatorEnabled(true); // hiển thị hamburger
        MainActivity.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        MainActivity.displayActionBarMenu(false);
        MainActivity.displayCategory(false);
        MainActivity.navigationView.getMenu().findItem(R.id.nvOrder).setChecked(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        setActionBar();
    }
}