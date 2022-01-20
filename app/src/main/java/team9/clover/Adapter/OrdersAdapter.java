package team9.clover.Adapter;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import java.util.List;

import team9.clover.Fragment.OrderDetailFragment;
import team9.clover.Model.OrderModel;
import team9.clover.Module.Reuse;
import team9.clover.R;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {

    List<OrderModel> orderList;

    public OrdersAdapter(List<OrderModel> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrdersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.set(orderList.get(position));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        MaterialTextView mCode, mNoProducts, mTotal, mStatus, mDate;
        ImageView mDot;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mDate = itemView.findViewById(R.id.mtvDate);
            mCode = itemView.findViewById(R.id.mtvCode);
            mNoProducts = itemView.findViewById(R.id.mtvNoProducts);
            mTotal = itemView.findViewById(R.id.mtvTotal);
            mStatus = itemView.findViewById(R.id.mtvStatus);
            mDot = itemView.findViewById(R.id.ivDot);
        }

        private void set(OrderModel order) {
            mDate.setText("đặt hàng\n" + order.getDate().get(0));
            mCode.setText("#" + order.takeId());
            mNoProducts.setText(""+order.getNoProducts());
            mTotal.setText(Reuse.vietnameseCurrency(order.getTotal()));
            mStatus.setText(order.takeStatus());

            if (order.takeSize() == 3) mDot.setImageTintList(ColorStateList.valueOf(itemView.getContext().getColor(R.color.black)));
            else if (order.takeSize() == 4) mDot.setImageTintList(ColorStateList.valueOf(itemView.getContext().getColor(R.color.red)));

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent("broadcast");
                intent.putExtra(OrderDetailFragment.NAME, order);
                LocalBroadcastManager.getInstance(itemView.getContext()).sendBroadcast(intent);
            });
        }
    }
}
