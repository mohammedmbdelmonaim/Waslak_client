package com.unicom.waslak_client.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.unicom.waslak_client.R;
import com.unicom.waslak_client.databinding.RecyclerViewStoreDetailRowBinding;
import com.unicom.waslak_client.databinding.RecyclerViewStoreRowBinding;
import com.unicom.waslak_client.di.scope.FragmentScope;
import com.unicom.waslak_client.model.user.StoresActivityModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@FragmentScope
public class DetailStoresAdapter extends ListAdapter<StoresActivityModel.InnerDatum , DetailStoresAdapter.DetailStoresViewHolder> {

    private ClickListener clickListener;
    LayoutInflater layoutInflater;
    public MutableLiveData<List<StoresActivityModel.InnerDatum>> filteredStores = new MutableLiveData<>();
    public List<StoresActivityModel.InnerDatum> allStores;
    int selectedPos = -1;


    @Inject
    protected DetailStoresAdapter(@NonNull DiffUtil.ItemCallback<StoresActivityModel.InnerDatum> diffCallback , ClickListener clickListener) {
        super(diffCallback);
        this.clickListener = clickListener;
    }


    @NonNull
    @Override
    public DetailStoresViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        RecyclerViewStoreDetailRowBinding binding = DataBindingUtil.inflate(layoutInflater , R.layout.recycler_view_store_detail_row , parent , false);
        return new DetailStoresViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailStoresViewHolder holder, int position) {
        StoresActivityModel.InnerDatum store = getItem(position);
        holder.binding.setStore(store);
        holder.binding.executePendingBindings();
        if (selectedPos == position) {
            holder.binding.constraint.setSelected(true);
        }else{
            holder.binding.constraint.setSelected(false);
        }
    }

    class DetailStoresViewHolder extends RecyclerView.ViewHolder{
        private final RecyclerViewStoreDetailRowBinding binding;
        public DetailStoresViewHolder(final  RecyclerViewStoreDetailRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.constraint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyItemChanged(selectedPos);
                    selectedPos = getAdapterPosition();
                    notifyItemChanged(selectedPos);
                    clickListener.clickStoreOrder(getAdapterPosition());
                }
            });
        }

    }

    public interface ClickListener{
        void clickStoreOrder(int position);
    }
}
