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
import com.unicom.waslak_client.databinding.RecyclerViewOrderHistoryRowBinding;
import com.unicom.waslak_client.databinding.RecyclerViewOrderRowBinding;
import com.unicom.waslak_client.model.user.OrdersHistoryModel;

import javax.inject.Inject;

public class OrdersHistoryAdapter extends ListAdapter<OrdersHistoryModel.InnerDatum, OrdersHistoryAdapter.OrderHistoryViewHolder> {
    LayoutInflater layoutInflater;
    private ClickListener clickListener;

    @Inject
    protected OrdersHistoryAdapter(@NonNull DiffUtil.ItemCallback<OrdersHistoryModel.InnerDatum> diffCallback, ClickListener clickListener) {
        super(diffCallback);
        this.clickListener = clickListener;
    }


    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        RecyclerViewOrderHistoryRowBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.recycler_view_order_history_row, parent, false);
        return new OrderHistoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, int position) {
        OrdersHistoryModel.InnerDatum order = getItem(position);
        holder.binding.setOrderHistory(order);
        holder.binding.executePendingBindings();
    }

    class OrderHistoryViewHolder extends RecyclerView.ViewHolder {
        private final RecyclerViewOrderHistoryRowBinding binding;

        public OrderHistoryViewHolder(final RecyclerViewOrderHistoryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.constraint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.clickOrder(getAdapterPosition());
                }
            });
        }
    }

    public interface ClickListener {
        void clickOrder(int position);
    }
}
