package team9.clover.Fragment;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import team9.clover.Adapter.SearchAdapter;
import team9.clover.MainActivity;
import team9.clover.R;

public class SearchFragment extends Fragment {

    public static final int ID = 11;
    public static final String NAME = "Search";

    ActionBar actionBar;
    GridView mContainer;
    SearchAdapter adapter;

    public SearchFragment(ActionBar actionBar, SearchAdapter adapter) {
        this.actionBar = actionBar;
        this.adapter = adapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        mContainer = view.findViewById(R.id.gvContainer);
        mContainer.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setActionBar();
    }

    private void setActionBar() {
        actionBar.setDisplayHomeAsUpEnabled(true);
        MainActivity.displayCategory(false);
    }
}