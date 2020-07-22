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
import com.unicom.waslak_client.databinding.RecyclerViewMenuRowBinding;
import com.unicom.waslak_client.model.user.ProductsStoreModel;
import com.unicom.waslak_client.utils.PriceFormatter;

import javax.inject.Inject;

public class MenuAdapter extends ListAdapter<ProductsStoreModel.InnerDatum, MenuAdapter.MenuViewHolder> {

    private ClickListener clickListener;
    LayoutInflater layoutInflater;
    @Inject
    PriceFormatter priceFormatter;

    @Inject
    protected MenuAdapter(@NonNull DiffUtil.ItemCallback<ProductsStoreModel.InnerDatum> diffCallback, ClickListener clickListener) {
        super(diffCallback);
        this.clickListener = clickListener;
    }


    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        RecyclerViewMenuRowBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.recycler_view_menu_row, parent, false);
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
        private final RecyclerViewMenuRowBinding binding;

        public MenuViewHolder(final RecyclerViewMenuRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.btnAddCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    binding.btnAddCart.setVisibility(View.INVISIBLE);
                    binding.btnRemoveCart.setVisibility(View.VISIBLE);
                    clickListener.clickAddCart(getAdapterPosition());
                }
            });

            binding.btnRemoveCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    binding.btnAddCart.setVisibility(View.VISIBLE);
                    binding.btnRemoveCart.setVisibility(View.INVISIBLE);
                    clickListener.clickRemoveCart(getAdapterPosition());
                }
            });
        }
    }

    public interface ClickListener {
        void clickAddCart(int position);
        void clickRemoveCart(int position);
    }
}
