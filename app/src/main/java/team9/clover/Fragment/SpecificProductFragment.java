package team9.clover.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import team9.clover.Adapter.SpecificProductAdapter;
import team9.clover.MainActivity;
import team9.clover.Model.DatabaseModel;
import team9.clover.Model.ProductModel;
import team9.clover.R;

public class SpecificProductFragment extends Fragment {

    public static final String NAME = "SpecificProduct";
    public static final int ID = 1;


    int category;
    ActionBar actionBar;
    GridView mContainer;
    SpecificProductAdapter adapter = null;

    List<ProductModel> productList;

    public SpecificProductFragment(ActionBar actionBar, int category) {
        this.actionBar = actionBar;
        this.category = category;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_specific_product, container, false);

        refer(view);
        setData();

        return view;
    }


    private void refer(View view) {
        mContainer = view.findViewById(R.id.gvContainer);
    }

    private void setData() {
        DatabaseModel.loadProduct("category", (long) category)
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            productList = new ArrayList<>();
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                productList.add(snapshot.toObject(ProductModel.class));
                            }

                            if (productList.size() % 2 == 1) {
                                productList.add(new ProductModel());
                            }

                            adapter = new SpecificProductAdapter(productList);
                            mContainer.setAdapter(adapter);
                        }
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        setActionBar();
    }

    private void setActionBar() {
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayUseLogoEnabled(true);
        MainActivity.toggle.setDrawerIndicatorEnabled(true); // hiển thị hamburger
        MainActivity.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        MainActivity.displayActionBarMenu(true);
        MainActivity.displayCategory(true);
    }
}