package team9.clover.Fragment;

import static team9.clover.Model.DatabaseModel.masterOrder;

import androidx.appcompat.app.ActionBar;

import android.content.Intent;
import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import team9.clover.Adapter.CartAdapter;
import team9.clover.MainActivity;
import team9.clover.Model.DatabaseModel;
import team9.clover.Module.Reuse;
import team9.clover.R;


public class CartFragment extends Fragment {
    
    public static final String NAME = "Cart";
    public static final int ID = 6;

    RecyclerView mContainer;
    MaterialButton mContinue;
    MaterialTextView mTotalCart;

    ActionBar actionBar;

    public static boolean isChanged = false;
    
    public CartFragment(ActionBar actionBar) {
        this.actionBar = actionBar;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        refer(view);
        setEvent();
        setData();
        
        return view;
    }

    private void setData() {
        DatabaseModel.refreshMasterOrder();
        mTotalCart.setText(Reuse.vietnameseCurrency(masterOrder.getTotal()));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        CartAdapter adapter = new CartAdapter(DatabaseModel.masterCart, mTotalCart);
        mContainer.setLayoutManager(layoutManager);
        mContainer.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isChanged) {
            DatabaseModel.updateMasterCart();
        }
    }

    private void refer(View view) {
        mContainer = view.findViewById(R.id.rvContainer);
        mContinue = view.findViewById(R.id.mbCheck);
        mTotalCart = view.findViewById(R.id.mtvTotalCart);
    }

    private void setEvent() {
        mContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!DatabaseModel.masterUid.isEmpty()) {
                    if (masterOrder.getTotal() == 0) {
                        Toast.makeText(getContext(), "Giỏ hàng trống", Toast.LENGTH_LONG).show();
                        return;
                    }

                    Intent intent = new Intent("broadcast");
                    intent.putExtra("Fragment", DeliveryFragment.ID);
                    LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
                }
            }
        });
    }

    private void setActionBar() {
        actionBar.setTitle("Giỏ hàng");
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        MainActivity.toggle.setDrawerIndicatorEnabled(true); // hiển thị hamburger
        MainActivity.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        MainActivity.displayActionBarMenu(false);
        MainActivity.displayCategory(false);
        MainActivity.navigationView.getMenu().findItem(R.id.nvCart).setChecked(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        setActionBar();
        isChanged = false;
        DatabaseModel.refreshMasterOrder();
        mTotalCart.setText(Reuse.vietnameseCurrency(masterOrder.getTotal()));
    }
}