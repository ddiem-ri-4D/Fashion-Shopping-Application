package team9.clover.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
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
import team9.clover.Model.HomePageModel;
import team9.clover.Model.ProductModel;
import team9.clover.R;


public class ViewMoreFragment extends Fragment {

    public final static String NAME = "ViewMore";
    public final static int ID = 5;

    int screen;
    GridView mContainer;
    SpecificProductAdapter adapter = null;
    ActionBar actionBar;

    List<ProductModel> productList;

    public ViewMoreFragment(ActionBar actionBar, int screen) {
        this.actionBar = actionBar;
        this.screen = screen;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_more, container, false);

        refer(view);
        setData();

        return view;
    }


    private void refer(View view) {
        mContainer = view.findViewById(R.id.gvContainer);
    }

    private void setData() {
        DatabaseModel.loadProduct("screen", (long) screen)
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
        actionBar.setTitle(screen == HomePageModel.SLIDER_PRODUCT_VIEW_TYPE ? "Bán nhiều nhất" : "Khuyến mãi");
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        MainActivity.displayActionBarMenu(true);
        MainActivity.displayCategory(false);
    }
}