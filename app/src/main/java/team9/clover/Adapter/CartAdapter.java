package team9.clover.Adapter;

import static team9.clover.Model.DatabaseModel.masterOrder;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import team9.clover.Fragment.CartFragment;
import team9.clover.Fragment.ProductDetailFragment;
import team9.clover.Model.CartItemModel;
import team9.clover.Model.DatabaseModel;
import team9.clover.Model.ProductModel;
import team9.clover.Module.Reuse;
import team9.clover.R;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    List<CartItemModel> productList;
    MaterialTextView mTotalCart;

    public CartAdapter(List<CartItemModel> productList, MaterialTextView mTotalCart) {
        this.productList = productList;
        this.mTotalCart = mTotalCart;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        holder.set(productList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView mImage;
        MaterialTextView mTitle, mSize, mPrice, mTotal;
        ImageButton mDelete, mEdit;

        String selectedSize;
        long quantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.rivImage);
            mTitle = itemView.findViewById(R.id.mtvTitle);
            mSize = itemView.findViewById(R.id.mtvSize);
            mTotal = itemView.findViewById(R.id.mtvTotal);
            mPrice = itemView.findViewById(R.id.mtvPrice);
            mDelete = itemView.findViewById(R.id.ibDelete);
            mEdit = itemView.findViewById(R.id.ibEdit);
        }

        public void set(CartItemModel cart, int position) {
            Glide.with(itemView.getContext()).load(cart.getImage()).into(mImage);
            Pair pair = cart.takeSize();
            mTitle.setText(cart.getTitle());
            mSize.setText(pair.first.toString());
            mTotal.setText(Reuse.vietnameseCurrency(cart.getTotal()));
            mPrice.setText(String.format("(x%d) ", pair.second)+ Reuse.vietnameseCurrency(cart.getPrice()));

            mDelete.setOnClickListener(v -> {
                new MaterialAlertDialogBuilder(itemView.getContext(), R.style.ThemeOverlay_App_MaterialAlertDialog)
                        .setMessage("Bạn thực sự muốn xóa sản phẩm này?")
                        .setCancelable(false)
                        .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DatabaseModel.removeMasterCard(cart.getId())
                                        .addOnCompleteListener(task -> {
                                           if (task.isSuccessful()) {
                                               productList.remove(position);
                                               notifyDataSetChanged();
                                               DatabaseModel.refreshMasterOrder();
                                               mTotalCart.setText(Reuse.vietnameseCurrency(masterOrder.getTotal()));
                                           }
                                        });
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("Từ chối", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
            });

            mEdit.setOnClickListener(v -> {
                showChooseForm(position);
            });

            mImage.setOnClickListener(v -> {
                DatabaseModel.loadSingleProduct(cart.getId()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent("broadcast");
                        intent.putExtra(ProductDetailFragment.NAME, task.getResult().toObject(ProductModel.class));
                        LocalBroadcastManager.getInstance(itemView.getContext()).sendBroadcast(intent);
                    }
                });
            });
        }

        private void showChooseForm(int position) {
            final Dialog dialog = new Dialog(itemView.getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.dialog_add_cart);

            final Spinner spinner = dialog.findViewById(R.id.spSize);
            final EditText editText = dialog.findViewById(R.id.etQuantity);
            final MaterialButton button = dialog.findViewById(R.id.mbConfirm);

            CartItemModel cart = productList.get(position);

            editText.setEnabled(false);
            editText.setText("");

            List<String> sizes = new ArrayList<>();
            sizes.add("Chọn...");
            sizes.addAll(productList.get(position).getChoice().keySet());

            ArrayAdapter<String> adapter = new ArrayAdapter<>(itemView.getContext(),
                    android.R.layout.simple_spinner_item, sizes);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (i != 0) {
                        selectedSize = spinner.getSelectedItem().toString();
                        editText.setEnabled(true);
                        editText.setText(Long.toString(cart.getChoice().get(selectedSize)));
                    } else {
                        editText.setEnabled(false);
                        editText.setText("");
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) { }
            });

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (editText.getText().toString().equals("")) return;
                    CartFragment.isChanged = true;
                    dialog.dismiss();
                    quantity = Integer.parseInt(editText.getText().toString());
                    DatabaseModel.updateMasterCart(cart.getId(), selectedSize, (-1*cart.getChoice().get(selectedSize) + quantity), null);
                    DatabaseModel.refreshMasterOrder();
                    notifyDataSetChanged();
                    Toast.makeText(itemView.getContext(), "Cập nhật thành công.", Toast.LENGTH_LONG).show();
                    mTotalCart.setText(Reuse.vietnameseCurrency(masterOrder.getTotal()));
                }
            });

            dialog.show();
        }
    }
}
