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
import com.unicom.waslak_client.databinding.RecyclerViewNotificationHistoryRowBinding;
import com.unicom.waslak_client.databinding.RecyclerViewOrderHistoryRowBinding;
import com.unicom.waslak_client.model.user.NotificationHistoryModel;
import com.unicom.waslak_client.model.user.OrdersHistoryModel;

import javax.inject.Inject;

public class NotificationsHistoryAdapter extends ListAdapter<NotificationHistoryModel.InnerDatum, NotificationsHistoryAdapter.NotificationHistoryViewHolder> {
    LayoutInflater layoutInflater;
    private ClickListener clickListener;
    @Inject
    protected NotificationsHistoryAdapter(@NonNull DiffUtil.ItemCallback<NotificationHistoryModel.InnerDatum> diffCallback, ClickListener clickListener) {
        super(diffCallback);
        this.clickListener = clickListener;
    }


    @NonNull
    @Override
    public NotificationHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        RecyclerViewNotificationHistoryRowBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.recycler_view_notification_history_row, parent, false);
        return new NotificationHistoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationHistoryViewHolder holder, int position) {
        NotificationHistoryModel.InnerDatum notification = getItem(position);
        holder.binding.setNotification(notification);
        holder.binding.executePendingBindings();
    }

    class NotificationHistoryViewHolder extends RecyclerView.ViewHolder {
        private final RecyclerViewNotificationHistoryRowBinding binding;

        public NotificationHistoryViewHolder(final RecyclerViewNotificationHistoryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.constraint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.clickNotification(getAdapterPosition());
                }
            });
        }
    }

    public interface ClickListener {
        void clickNotification(int position);
    }
}
