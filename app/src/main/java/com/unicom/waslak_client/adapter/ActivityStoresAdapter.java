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
import com.unicom.waslak_client.databinding.RecyclerViewStoreRowBinding;
import com.unicom.waslak_client.di.scope.FragmentScope;
import com.unicom.waslak_client.model.user.StoresActivityModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@FragmentScope
public class ActivityStoresAdapter extends ListAdapter<StoresActivityModel.InnerDatum , ActivityStoresAdapter.ActivityStoresViewHolder> implements Filterable {

    private ClickListener clickListener;
    LayoutInflater layoutInflater;
    public MutableLiveData<List<StoresActivityModel.InnerDatum>> filteredStores = new MutableLiveData<>();
    public List<StoresActivityModel.InnerDatum> allStores;

    @Inject
    protected ActivityStoresAdapter(@NonNull DiffUtil.ItemCallback<StoresActivityModel.InnerDatum> diffCallback , ClickListener clickListener) {
        super(diffCallback);
        this.clickListener = clickListener;
    }


    @NonNull
    @Override
    public ActivityStoresViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        RecyclerViewStoreRowBinding binding = DataBindingUtil.inflate(layoutInflater , R.layout.recycler_view_store_row , parent , false);
        return new ActivityStoresViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityStoresViewHolder holder, int position) {
        StoresActivityModel.InnerDatum store = getItem(position);
        holder.binding.setStore(store);
        holder.binding.executePendingBindings();
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<StoresActivityModel.InnerDatum> filteredStores = new ArrayList<>();
            if (constraint.toString().isEmpty()){
                filteredStores.addAll(allStores);
            }else{
                for (StoresActivityModel.InnerDatum store : allStores){
                    if (store.getDescription().toLowerCase().contains(constraint.toString().toLowerCase())){
                        filteredStores.add(store);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredStores;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
//            submitList((List<StoresActivityModel.InnerDatum>) results.values);
            filteredStores.setValue((List<StoresActivityModel.InnerDatum>) results.values);
        }
    };
    @Override
    public Filter getFilter() {
        return filter;
    }

    class ActivityStoresViewHolder extends RecyclerView.ViewHolder{
        private final RecyclerViewStoreRowBinding binding;
        public ActivityStoresViewHolder(final  RecyclerViewStoreRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.orderBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.clickStoreOrder(getAdapterPosition());
                }
            });
        }

    }

    public interface ClickListener{
        void clickStoreOrder(int position);
    }
}
