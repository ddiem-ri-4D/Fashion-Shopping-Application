package team9.clover.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.android.material.textview.MaterialTextView;

import java.util.List;

import team9.clover.R;

public class ProductDetailMoreAdapter extends ArrayAdapter<String> {

    List<String> itemData;
    Context context;
    int tab, n;

    public ProductDetailMoreAdapter(Context context, int itemId, int tab, List<String> itemData, int n) {
        super(context, itemId, itemData);
        this.context = context;
        this.itemData = itemData;
        this.tab = tab;
        this.n = n;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        MaterialTextView mItem = (MaterialTextView) inflater.inflate(R.layout.item_product_detail_more,parent,false);
        setData(position, mItem);
        return mItem;
    }

    private void setData(int position, MaterialTextView mItem) {
        String itemText = itemData.get(position);
        mItem.setText(itemText);
        mItem.setBackgroundColor(context.getColor(R.color.white));

        if (tab == 1) {
            mItem.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        } else {
            if (position < n || position % n == 0) {
                mItem.setTypeface(null, Typeface.BOLD);
                mItem.setTextColor(getContext().getColor(R.color.black));
                mItem.setTextAlignment(position == 0 ? View.TEXT_ALIGNMENT_TEXT_START : View.TEXT_ALIGNMENT_CENTER);
            } else {
                mItem.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
        }
    }
}