package com.unicom.waslak_client.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.unicom.waslak_client.R;
import com.unicom.waslak_client.databinding.RecyclerViewOrderRowBinding;
import com.unicom.waslak_client.model.user.ProductsStoreModel;
import com.unicom.waslak_client.utils.PriceFormatter;

import java.util.Locale;

import javax.inject.Inject;

public class OrdersAdapter extends ListAdapter<ProductsStoreModel.InnerDatum, OrdersAdapter.MenuViewHolder> {

    private ClickListener clickListener;
    LayoutInflater layoutInflater;
    @Inject
    PriceFormatter priceFormatter;

    @Inject
    protected OrdersAdapter(@NonNull DiffUtil.ItemCallback<ProductsStoreModel.InnerDatum> diffCallback, ClickListener clickListener) {
        super(diffCallback);
        this.clickListener = clickListener;
    }


    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        RecyclerViewOrderRowBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.recycler_view_order_row, parent, false);
        return new MenuViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        ProductsStoreModel.InnerDatum product = getItem(position);
        holder.binding.setProduct(product);
        holder.binding.setView(holder.itemView);
        holder.binding.setPriceFormater(priceFormatter);
        holder.binding.executePendingBindings();

    }

    class MenuViewHolder extends RecyclerView.ViewHolder {
        private final RecyclerViewOrderRowBinding binding;

        public MenuViewHolder(final RecyclerViewOrderRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.btnRemoveCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.clickRemoveCart(getAdapterPosition(), Integer.parseInt(binding.qtyNumber.getText().toString()));
                }
            });

            binding.qtyPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int number = Integer.parseInt(binding.qtyNumber.getText().toString());
                    if(number >= getItem(getAdapterPosition()).getQuantity()){
                        return;
                    }
                    binding.qtyNumber.setText(String.format("%d" , ++number));
                    double price = (getItem(getAdapterPosition()).getPriceAfterDiscount()) * number;
                    double price_before = (getItem(getAdapterPosition()).getSellingPrice()) * number;
                    binding.menuPrice.setText(priceFormatter.toDecimalString(price));
                    binding.menuPriceBefore.setText(priceFormatter.toDecimalString(price_before));
                    clickListener.clickPlusQty(getAdapterPosition(), Integer.parseInt(binding.qtyNumber.getText().toString()));
                }
            });

            binding.qtyMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int number = Integer.parseInt(binding.qtyNumber.getText().toString());
                    if (number == 1) {
                        return;
                        //nothing
                    } else {
                        double price = ((getItem(getAdapterPosition()).getPriceAfterDiscount()) * number) - (getItem(getAdapterPosition()).getPriceAfterDiscount());
                        double price_before = ((getItem(getAdapterPosition()).getSellingPrice()) * number) - (getItem(getAdapterPosition()).getSellingPrice());
                        binding.qtyNumber.setText(String.format("%d" ,--number));
                        binding.menuPrice.setText(priceFormatter.toDecimalString(price));
                        binding.menuPriceBefore.setText(priceFormatter.toDecimalString(price_before));
                        clickListener.clickMinusQty(getAdapterPosition(), Integer.parseInt(binding.qtyNumber.getText().toString()));
                    }
                }
            });
        }
    }

    public interface ClickListener {
        void clickRemoveCart(int position, int qty);

        void clickPlusQty(int position, int qty);

        void clickMinusQty(int position, int qty);
    }
}
