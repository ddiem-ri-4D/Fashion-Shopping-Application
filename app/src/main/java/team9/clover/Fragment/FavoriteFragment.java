package team9.clover.Fragment;

import static team9.clover.Model.DatabaseModel.masterUser;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import team9.clover.Adapter.FavoriteAdapter;
import team9.clover.MainActivity;
import team9.clover.Model.DatabaseModel;
import team9.clover.Model.ProductModel;
import team9.clover.R;

public class FavoriteFragment extends Fragment {

    public static final String NAME = "Favorite";
    public static final int ID = 4;
    public static boolean isChanged;

    RecyclerView mContainer;
    ActionBar actionBar;

    public FavoriteFragment(ActionBar actionBar) {
        this.actionBar = actionBar;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_favorite, container, false);

        isChanged = false;
        mContainer = view.findViewById(R.id.rvContainer);
        setData();

        return view;
    }

    public void setData() {
        if (masterUser.getFavorite() != null && masterUser.getFavorite().size() > 0) {
            DatabaseModel.loadProduct("id", DatabaseModel.masterUser.getFavorite())
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                List<ProductModel> products = new ArrayList<>();
                                for (QueryDocumentSnapshot snapshot : task.getResult())
                                    products.add(snapshot.toObject(ProductModel.class));

                                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                FavoriteAdapter adapter = new FavoriteAdapter(products);
                                mContainer.setLayoutManager(layoutManager);
                                mContainer.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isChanged)
            DatabaseModel.updateMasterUser();
    }

    @Override
    public void onResume() {
        super.onResume();
        setActionBar();
    }

    private void setActionBar() {
        actionBar.setTitle("Yêu thích");
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        MainActivity.toggle.setDrawerIndicatorEnabled(true); // hiển thị hamburger
        MainActivity.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        MainActivity.displayActionBarMenu(false);
        MainActivity.displayCategory(false);
        MainActivity.navigationView.getMenu().findItem(R.id.nvFavorite).setChecked(true);
    }
}