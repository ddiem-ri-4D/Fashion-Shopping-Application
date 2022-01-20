package team9.clover;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textview.MaterialTextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Stack;

import team9.clover.Adapter.CategoryAdapter;
import team9.clover.Adapter.SearchAdapter;
import team9.clover.Fragment.AccountFragment;
import team9.clover.Fragment.CartFragment;
import team9.clover.Fragment.DeliveryFragment;
import team9.clover.Fragment.FavoriteFragment;
import team9.clover.Fragment.HomeFragment;
import team9.clover.Fragment.OrderDetailFragment;
import team9.clover.Fragment.OrdersFragment;
import team9.clover.Fragment.ProductDetailFragment;
import team9.clover.Fragment.SearchFragment;
import team9.clover.Fragment.SpecificProductFragment;
import team9.clover.Fragment.ViewMoreFragment;
import team9.clover.Model.DatabaseModel;
import team9.clover.Model.OrderModel;
import team9.clover.Model.ProductModel;
import team9.clover.Module.Reuse;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static NavigationView navigationView;
    public static FrameLayout frameLayout;
    public static ActionBarDrawerToggle toggle;
    public static DrawerLayout drawerLayout;
    public static RecyclerView mCategory;
    public static MenuItem mSearch, mBell, mBag;
    public static Toolbar toolbar;
    public static DrawerArrowDrawable arrowDrawable;
    public static SearchView searchView;
    public static SearchAdapter searchAdapter = new SearchAdapter();
    public static InputMethodManager keyboard;

    CategoryAdapter categoryAdapter;
    HomeFragment homeFragment;

    private static int previousNavigation = 0;
    private static boolean quitApp = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        quitApp = false;

        refer();
        setToolbar();
        setCategory();
        setNavigationView();
        setBroadcast();
        setFirstFragment();
    }

    /*============================================================================================== THIẾT LẬP CÁC FRAGMENT SẼ HIỂN THỊ TRÊN MÀN HÌNH
    *
    * */
    private void setFragment(int fragmentId, Fragment fragment) {
        quitApp = false;
        if (fragmentId == ProductDetailFragment.ID) {
            activeBackButton();
            setFragment(fragment, ProductDetailFragment.NAME, 0);
        } else if (fragmentId == SpecificProductFragment.ID) {
            setFragment(fragment, SpecificProductFragment.NAME, 1);
        } else if (fragmentId == ViewMoreFragment.ID) {
            activeBackButton();
            setFragment(fragment, ViewMoreFragment.NAME, 0);
        } else if (fragmentId == FavoriteFragment.ID) {
            setFragment(fragment, FavoriteFragment.NAME, 0);
        } else if (fragmentId == CartFragment.ID) {
            setFragment(fragment, CartFragment.NAME, 0);
        } else if (fragmentId == DeliveryFragment.ID) {
            activeBackButton();
            setFragment(fragment, DeliveryFragment.NAME, 0);
        } else if (fragmentId == OrdersFragment.ID) {
            setFragment(fragment, OrdersFragment.NAME, 0);
        } else if (fragmentId == OrderDetailFragment.ID) {
            activeBackButton();
            setFragment(fragment, OrderDetailFragment.NAME, 0);
        } else if (fragmentId == AccountFragment.ID) {
            setFragment(fragment, AccountFragment.NAME, 0);
        } else if (fragmentId == SearchFragment.ID) {
            activeBackButton();
            setFragment(fragment, SearchFragment.NAME, 0);
        }
    }

    /*
    * These events occur when users click on top-right menu items on action bar
    * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.abBag) {
            if (DatabaseModel.masterUid.isEmpty()) {
                Toast.makeText(MainActivity.this, "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
                return true;
            }

            previousNavigation = R.id.nvCart;
            setFragment(CartFragment.ID, new CartFragment(getSupportActionBar()));
        }

        return super.onOptionsItemSelected(item);
    }

    /*============================================================================================== THIẾT LẬP ITEM CLICKING TRÊN NAVIGATION VIEW
    *
    * */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        drawerLayout.closeDrawers();

        if (itemId != previousNavigation) {
            if (itemId == R.id.nvMall) {
                clearBackStack();
                CategoryAdapter.currentTab = 0;
                categoryAdapter.notifyDataSetChanged();
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                toggle.setDrawerIndicatorEnabled(true); // hiển thị hamburger
                previousNavigation = itemId;
            } else if (itemId == R.id.nvFavorite) {
                if (DatabaseModel.masterUid.isEmpty()) {
                    navigationView.setCheckedItem(R.id.nvMall);
                    Toast.makeText(MainActivity.this, "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    previousNavigation = itemId;
                    setFragment(FavoriteFragment.ID, new FavoriteFragment(getSupportActionBar()));
                }
            } else if (itemId == R.id.nvCart) {
                if (DatabaseModel.masterUid.isEmpty()) {
                    navigationView.setCheckedItem(R.id.nvMall);
                    Toast.makeText(MainActivity.this, "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    previousNavigation = R.id.nvCart;
                    setFragment(CartFragment.ID, new CartFragment(getSupportActionBar()));
                }
            } else if (itemId == R.id.nvOrder) {
                if (DatabaseModel.masterUid.isEmpty()) {
                    navigationView.setCheckedItem(R.id.nvMall);
                    Toast.makeText(MainActivity.this, "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    previousNavigation = R.id.nvOrder;
                    setFragment(OrdersFragment.ID, new OrdersFragment(getSupportActionBar()));
                }
            } else if (itemId == R.id.nvProfile) {
                if (DatabaseModel.masterUid.isEmpty()) {
                    new MaterialAlertDialogBuilder(MainActivity.this, R.style.ThemeOverlay_App_MaterialAlertDialog)
                            .setMessage("Bạn có muốn đăng nhập không?")
                            .setCancelable(true)
                            .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    finish();
                                    startActivity(new Intent(MainActivity.this, LogInActivity.class));
                                    Reuse.startActivity(MainActivity.this);
                                }
                            }).setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).create().show();

                    navigationView.setCheckedItem(R.id.nvMall);
                    return false;
                } else {
                    previousNavigation = R.id.nvProfile;
                    setFragment(AccountFragment.ID, new AccountFragment(getSupportActionBar()));
                }
            }
        }

        return true;
    }

    /*============================================================================================== THIẾT LẬP CATEGORY RECYCLER VIEW
     * Thiết lập cho thanh category
     * */
    private void setCategory() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mCategory.setLayoutManager(layoutManager);

        categoryAdapter = new CategoryAdapter(DatabaseModel.categoryModelList);
        mCategory.setAdapter(categoryAdapter);
        categoryAdapter.notifyDataSetChanged();
    }

    /*============================================================================================== KHI USER NHẤN BUTTON BACK
    *
    * */
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBackPressed() {
        if (quitApp) {
            quitApp = false;
            finishAffinity();
            System.exit(0);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                quitApp = true;
                Toast.makeText(this, getString(R.string.on_back_press), Toast.LENGTH_SHORT).show();
            } else {
                String lastFragmentName = Reuse.getLastFragmentName(getSupportFragmentManager());
                if (lastFragmentName.equals(ProductDetailFragment.NAME) ||
                        lastFragmentName.equals(ViewMoreFragment.NAME) ||
                        lastFragmentName.equals(DeliveryFragment.NAME) ||
                        lastFragmentName.equals(OrderDetailFragment.NAME) ||

                        lastFragmentName.equals(AccountFragment.NAME) ||
                        lastFragmentName.equals(OrdersFragment.NAME) ||
                        lastFragmentName.equals(FavoriteFragment.NAME) ||
                        lastFragmentName.equals(CartFragment.NAME)) {
                    getSupportFragmentManager().popBackStack();
                } else if (lastFragmentName.equals(SpecificProductFragment.NAME)) {
                    getSupportFragmentManager().popBackStack(SpecificProductFragment.NAME, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    CategoryAdapter.currentTab = 0;
                    categoryAdapter.notifyDataSetChanged();
                } else if (lastFragmentName.equals(SearchFragment.NAME)) {
                    getSupportFragmentManager().popBackStack();
                    searchView.setIconified(true); // tắt search view đi
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        quitApp = false;
        previousNavigation = R.id.nvMall;
    }

    //============================================================================================== THAM CHIẾU ĐẾN CÁC COMPONENT CỦA ACTIVITY
    private void refer() {
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        frameLayout = findViewById(R.id.main_framelayout);
        mCategory = findViewById(R.id.rvCategory);

        keyboard = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    //============================================================================================== THIẾT LẬP CHO TOOLBAR
    private void setToolbar() {
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_arrow_left); // thiết lập icon trở về
        getSupportActionBar().setLogo(R.drawable.actionbar_logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        arrowDrawable = toggle.getDrawerArrowDrawable();
        arrowDrawable.setColor(getColor(R.color.black));
        toggle.setDrawerArrowDrawable(arrowDrawable);
        toggle.syncState();
        drawerLayout.addDrawerListener(toggle);
    }

    /*============================================================================================== THIẾT LẬP NAVIGATION VIEW
     * Hàm này giúp hamburger có animation, đồng thời chỉnh background cho selected item view
     * */
    private void setNavigationView() {
        View view =  navigationView.getHeaderView(0);
        MaterialTextView fullName = view.findViewById(R.id.mtvFullName),
                email = view.findViewById(R.id.mtvEmail);
        DatabaseModel.loadMasterUser(fullName, email);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    public void activeBackButton() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        toggle.setDrawerIndicatorEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setFirstFragment() {
        homeFragment = new HomeFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_framelayout, homeFragment, null);
        transaction.commit();
    }

    public static void displayActionBarMenu(boolean show) {
        if (show) {
            mSearch.setVisible(true);
            mBell.setVisible(true);
            mBag.setVisible(true);
        } else {
            mSearch.setVisible(false);
            mBell.setVisible(false);
            mBag.setVisible(false);
        }
    }

    public static void displayCategory(boolean show) {
        if (show)
            mCategory.setVisibility(View.VISIBLE);
        else
            mCategory.setVisibility(View.GONE);
    }

    /*============================================================================================== THIẾT LẬP MENU TRÊN ACTION BAR
     *
     * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        // tham chiếu đến các item trên menu của action bar
        mSearch = menu.getItem(0);
        mBell = menu.getItem(1);
        mBag = menu.getItem(2);
        homeFragment.setActionBar(getSupportActionBar(), categoryAdapter);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.abSearch).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnSearchClickListener(view -> {
            setFragment(SearchFragment.ID, new SearchFragment(getSupportActionBar(), searchAdapter));
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;

    }

    //============================================================================================== THIẾT LẬP BROADCAST
    private void setBroadcast() {
        BroadcastReceiver broadcast = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                int screen = intent.getIntExtra(ViewMoreFragment.NAME, -1);
                if (screen != -1) {
                    setFragment(ViewMoreFragment.ID, new ViewMoreFragment(getSupportActionBar(), (int) screen));
                }

                int category = intent.getIntExtra(SpecificProductFragment.NAME, -1);
                if (category != -1) {
                    if (category == 0) {
                        clearBackStack();
                        navigationView.getMenu().findItem(R.id.nvMall).setChecked(true);
                        previousNavigation = R.id.nvMall;
                    } else {
                        setFragment(SpecificProductFragment.ID, new SpecificProductFragment(getSupportActionBar(), (int) category));
                    }
                }

                ProductModel productBroadcast = (ProductModel) intent.getSerializableExtra(ProductDetailFragment.NAME);
                if (productBroadcast != null) {
                    setFragment(ProductDetailFragment.ID, new ProductDetailFragment(getSupportActionBar(), productBroadcast));
                }

                int fragmentId = intent.getIntExtra("Fragment", -1);
                if (fragmentId == DeliveryFragment.ID) {
                    setFragment(DeliveryFragment.ID, new DeliveryFragment(getSupportActionBar()));
                }

                OrderModel orderModel = (OrderModel) intent.getSerializableExtra(OrderDetailFragment.NAME);
                if (orderModel != null) {
                    setFragment(OrderDetailFragment.ID, new OrderDetailFragment(getSupportActionBar(), orderModel));
                }
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcast, new IntentFilter("broadcast"));
    }

    private void clearBackStack() {
        getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public void setFragment(Fragment fragment, String name, int animStyle) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (!fragmentManager.isDestroyed()) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_framelayout, fragment);
            transaction.addToBackStack(name);

            if (animStyle == 0) transaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left, R.anim.slide_from_left, R.anim.slide_to_right);
            else transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);

            transaction.commit();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        DatabaseModel.updateMasterUser();
        DatabaseModel.updateMasterOrder();
        DatabaseModel.updateMasterCart();

        if (DatabaseModel.firebaseUser == null) {
            DatabaseModel.masterUid = "";
        }
    }
}