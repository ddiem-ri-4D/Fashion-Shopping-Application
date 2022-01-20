package team9.clover.Fragment;

import static team9.clover.MainActivity.displayActionBarMenu;
import static team9.clover.MainActivity.drawerLayout;
import static team9.clover.MainActivity.mCategory;
import static team9.clover.MainActivity.toggle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import team9.clover.Adapter.CategoryAdapter;
import team9.clover.MainActivity;
import team9.clover.Model.DatabaseModel;
import team9.clover.Adapter.HomePageAdapter;
import team9.clover.R;

public class HomeFragment extends Fragment {

    public final static String NAME = "Home";
    public final static int ID = 0;

    RecyclerView mHomePage;
    HomePageAdapter homePageAdapter;
    ActionBar actionBar = null;
    CategoryAdapter categoryAdapter = null;


    public HomeFragment() { }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        refer(view);
        setView(view);
//        DatabaseModel.addProduct();

        return view;
    }

    private void refer(View view) {
        mHomePage = view.findViewById(R.id.rvHomePage);
    }

    private void setView(View view) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mHomePage.setLayoutManager(layoutManager);

        homePageAdapter = new HomePageAdapter(DatabaseModel.homePageModelList);
        mHomePage.setAdapter(homePageAdapter);
        homePageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        setActionBar();
    }

    private void setActionBar() {
        if (actionBar != null) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            actionBar.setDisplayHomeAsUpEnabled(false); // xóa button go-back
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayUseLogoEnabled(true);
            toggle.setDrawerIndicatorEnabled(true); // hiển thị hamburger
            toggle.setToolbarNavigationClickListener(null);
            mCategory.setVisibility(View.VISIBLE); // hiển thị lại thanh category navigation view

            MainActivity.navigationView.getMenu().findItem(R.id.nvMall).setChecked(true);
            if (categoryAdapter != null) {
                CategoryAdapter.currentTab = 0;
                categoryAdapter.notifyDataSetChanged();
            }


            displayActionBarMenu(true);
        }
    }

    public void setActionBar(ActionBar actionBar, CategoryAdapter categoryAdapter) {
        this.actionBar = actionBar;
        this.categoryAdapter = categoryAdapter;
    }
}













//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/*
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//        binding = FragmentHomeBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
//        noInternetConnection = root.findViewById(R.id.no_internet_connection);
//
//        // check internet connection is possible
//        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//
//        if (networkInfo != null && networkInfo.isConnected()) {
//            noInternetConnection.setVisibility(View.GONE);
//            referWidgets(root);
//            setViewCategory();
//            setViewRemaining(root);
//        } else {
//            Glide.with(this).load(R.drawable.no_internet).into(noInternetConnection);
//            noInternetConnection.setVisibility(View.VISIBLE);
//        }
//
//        return root;

        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        refer(view);
        setCategory();
        setView(view);

        return view;
    }

    private void refer(View view) {
        mCategory = view.findViewById(R.id.rvCategory);
        mHomePage = view.findViewById(R.id.rvHomePage);
    }
* */