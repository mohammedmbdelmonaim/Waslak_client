package com.unicom.waslak_client.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.unicom.waslak_client.R;
import com.unicom.waslak_client.databinding.RecyclerViewOpenTimeRowBinding;
import com.unicom.waslak_client.model.user.StoresActivityModel;

import javax.inject.Inject;

public class OpenTimesAdapter extends ListAdapter<StoresActivityModel.StoresOpeningTime, OpenTimesAdapter.OpenTimesViewHolder> {
    LayoutInflater layoutInflater;

    @Inject
    protected OpenTimesAdapter(@NonNull DiffUtil.ItemCallback<StoresActivityModel.StoresOpeningTime> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public OpenTimesAdapter.OpenTimesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        RecyclerViewOpenTimeRowBinding binding = DataBindingUtil.inflate(layoutInflater , R.layout.recycler_view_open_time_row , parent , false);
        return new OpenTimesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OpenTimesAdapter.OpenTimesViewHolder holder, int position) {
        StoresActivityModel.StoresOpeningTime openTime = getItem(position);
        holder.binding.setOpenTime(openTime);
        holder.binding.executePendingBindings();
    }

    class OpenTimesViewHolder extends RecyclerView.ViewHolder{
        private final RecyclerViewOpenTimeRowBinding binding;
        public OpenTimesViewHolder(final RecyclerViewOpenTimeRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }
}
