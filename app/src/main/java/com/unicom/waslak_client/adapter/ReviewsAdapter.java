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
import com.unicom.waslak_client.databinding.RecyclerViewReviewRowBinding;
import com.unicom.waslak_client.databinding.RecyclerViewStoreRowBinding;
import com.unicom.waslak_client.di.scope.FragmentScope;
import com.unicom.waslak_client.model.user.RateModel;
import com.unicom.waslak_client.model.user.StoresActivityModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@FragmentScope
public class ReviewsAdapter extends ListAdapter<RateModel.CustomersRate , ReviewsAdapter.ReviewsViewHolder>{
    LayoutInflater layoutInflater;

    @Inject
    protected ReviewsAdapter(@NonNull DiffUtil.ItemCallback<RateModel.CustomersRate> diffCallback) {
        super(diffCallback);
    }


    @NonNull
    @Override
    public ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        RecyclerViewReviewRowBinding binding = DataBindingUtil.inflate(layoutInflater , R.layout.recycler_view_review_row , parent , false);
        return new ReviewsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsViewHolder holder, int position) {
        RateModel.CustomersRate customersRate = getItem(position);
        holder.binding.setCustomerRate(customersRate);
        holder.binding.executePendingBindings();
    }



    class ReviewsViewHolder extends RecyclerView.ViewHolder{
        private final RecyclerViewReviewRowBinding binding;
        public ReviewsViewHolder(final  RecyclerViewReviewRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }
}
