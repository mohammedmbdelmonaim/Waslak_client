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
import com.unicom.waslak_client.databinding.RecyclerViewActivityRowBinding;
import com.unicom.waslak_client.model.user.ActivitiesModel;

import javax.inject.Inject;

public class ActivitiesAdapter  extends ListAdapter<ActivitiesModel.InnerDatum , ActivitiesAdapter.ActivitiesViewHolder> {

    private ClickListener clickListener;
    LayoutInflater layoutInflater;
    int selectedPos = 0;
    @Inject
    protected ActivitiesAdapter(@NonNull DiffUtil.ItemCallback<ActivitiesModel.InnerDatum> diffCallback , ClickListener clickListener) {
        super(diffCallback);
        this.clickListener = clickListener;
    }


    @NonNull
    @Override
    public ActivitiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        RecyclerViewActivityRowBinding binding = DataBindingUtil.inflate(layoutInflater , R.layout.recycler_view_activity_row , parent , false);
        return new ActivitiesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivitiesViewHolder holder, int position) {
        ActivitiesModel.InnerDatum activity = getItem(position);
        holder.binding.setActivity(activity);
        holder.binding.executePendingBindings();
        if (selectedPos == position) {
            holder.binding.activityBtn.setSelected(true);
        }else{
            holder.binding.activityBtn.setSelected(false);
        }
    }

    class ActivitiesViewHolder extends RecyclerView.ViewHolder{
        private final RecyclerViewActivityRowBinding binding;
        public ActivitiesViewHolder(final  RecyclerViewActivityRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.activityBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyItemChanged(selectedPos);
                    selectedPos = getAdapterPosition();
                    notifyItemChanged(selectedPos);
                    clickListener.clickActivity( getAdapterPosition());
                }
            });
        }

    }

    public interface ClickListener{
        void clickActivity( int position);
    }
}
