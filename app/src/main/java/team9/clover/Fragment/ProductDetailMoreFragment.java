package team9.clover.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

import team9.clover.Adapter.ProductDetailMoreAdapter;
import team9.clover.R;

public class ProductDetailMoreFragment extends Fragment {

    public ProductDetailMoreFragment() { }

    int tab;

    // tab mô tả
    String description;

    // tab thông tin
    List<String> infoList;

    // tab kích thước
    List<String> bodyNameList, sizeList;
    List<Long> measureList;

    // tab  mô tả
    public ProductDetailMoreFragment(String description) {
        this.description = description;
        this.tab = 0;
    }

    // tab thông tin
    public ProductDetailMoreFragment(List<String> infoList) {
        this.infoList = infoList;
        this.tab = 1;
    }

    // tab kich thước
    public ProductDetailMoreFragment(List<String> bodyNameList, List<String> sizeList, List<Long> measureList) {
        this.bodyNameList = bodyNameList;
        this.sizeList = sizeList;
        this.measureList = measureList;
        this.tab = 2;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_detail_more, container, false);

        if (tab == 0) {
            MaterialTextView mDescription = view.findViewById(R.id.mtvDescription);
            view.findViewById(R.id.nsvContainer).setVisibility(View.VISIBLE);
            mDescription.setText(description);
        } else {
            GridView mContainer = view.findViewById(R.id.gvContainer);
            mContainer.setVisibility(View.VISIBLE);

            if (tab == 1) {
                mContainer.setNumColumns(1);
                setInformationTab(mContainer);
            } else {
                mContainer.setNumColumns(sizeList.size() + 1);
                setSizeTab(mContainer);
            }
        }

        return view;
    }

    private void setInformationTab(GridView mContainer) {
        List<String> infoData = new ArrayList<>(infoList);
        ProductDetailMoreAdapter adapter = new ProductDetailMoreAdapter(getContext(), R.layout.item_product_detail_more, 1, infoData, 0);
        mContainer.setAdapter(adapter);
    }

    private void setSizeTab(GridView mContainer) {
        int n = sizeList.size();
        List<String> sizeData = new ArrayList<>();
        sizeData.add("(centimet)");
        sizeData.addAll(sizeList);

        for (int i = 0; i < bodyNameList.size(); ++i) {
            sizeData.add(bodyNameList.get(i));
            for (int j = 0; j < n; ++j) {
                sizeData.add(Long.toString(measureList.get(n*i + j)));
            }
        }
        ProductDetailMoreAdapter adapter = new ProductDetailMoreAdapter(getContext(), R.layout.item_product_detail_more, 2, sizeData, n + 1);
        mContainer.setAdapter(adapter);
    }
}