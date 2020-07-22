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
import com.unicom.waslak_client.databinding.RecyclerViewOrderDetailRowBinding;
import com.unicom.waslak_client.databinding.RecyclerViewOrderRowBinding;
import com.unicom.waslak_client.model.user.OrderDetailModel;
import com.unicom.waslak_client.model.user.ProductsStoreModel;
import com.unicom.waslak_client.utils.PriceFormatter;

import javax.inject.Inject;

public class OrdersDetailAdapter extends ListAdapter<OrderDetailModel.OrderItem, OrdersDetailAdapter.OrdersDetailViewHolder> {
    LayoutInflater layoutInflater;
    @Inject
    PriceFormatter priceFormatter;
    @Inject
    protected OrdersDetailAdapter(@NonNull DiffUtil.ItemCallback<OrderDetailModel.OrderItem> diffCallback) {
        super(diffCallback);
    }


    @NonNull
    @Override
    public OrdersDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        RecyclerViewOrderDetailRowBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.recycler_view_order_detail_row, parent, false);
        return new OrdersDetailViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersDetailViewHolder holder, int position) {
        OrderDetailModel.OrderItem product = getItem(position);
        holder.binding.setProduct(product);
        holder.binding.setPriceFormater(priceFormatter);
        holder.binding.executePendingBindings();
    }

    class OrdersDetailViewHolder extends RecyclerView.ViewHolder {
        private final RecyclerViewOrderDetailRowBinding binding;

        public OrdersDetailViewHolder(final RecyclerViewOrderDetailRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
