package com.unicom.waslak_client.ui.user;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.unicom.waslak_client.MainApplication;
import com.unicom.waslak_client.R;
import com.unicom.waslak_client.adapter.NotificationsHistoryAdapter;
import com.unicom.waslak_client.adapter.OrdersHistoryAdapter;
import com.unicom.waslak_client.databinding.FragmentNotificationBinding;
import com.unicom.waslak_client.databinding.FragmentOrderHistoryBinding;
import com.unicom.waslak_client.di.component.ApplicationComponent;
import com.unicom.waslak_client.di.component.NotificationsHistoryFragmentComponent;
import com.unicom.waslak_client.di.component.OrderHistoryFragmentComponent;
import com.unicom.waslak_client.di.qualifier.ActivityContext;
import com.unicom.waslak_client.model.user.NotificationHistoryModel;
import com.unicom.waslak_client.model.user.OrdersHistoryModel;
import com.unicom.waslak_client.model.user.ProductsStoreModel;
import com.unicom.waslak_client.viewmodel.ViewModelFactory;
import com.unicom.waslak_client.viewmodel.user.NotificationsHistoryViewModel;
import com.unicom.waslak_client.viewmodel.user.OrdersHistoryViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class NotificationFragment extends Fragment implements NotificationsHistoryAdapter.ClickListener {
    private FragmentNotificationBinding binding;
    private NavController navController;
    @Inject
    NotificationsHistoryAdapter historyAdapter;
    @ActivityContext
    @Inject
    Context context;
    @Inject
    ViewModelFactory viewModelFactory;
    private NotificationsHistoryViewModel historyViewModel;
    private PublishSubject<Boolean> backButtonClickSource = PublishSubject.create();
    private static final long EXIT_TIMEOUT = 2000;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // dagger
        ApplicationComponent applicationComponent = MainApplication.get(getActivity()).getApplicationComponent();
        NotificationsHistoryFragmentComponent notificationsHistoryFragmentComponent = applicationComponent.notificationComponentComponentBuilder().getClickListener(this).getContext(getActivity()).build();
        notificationsHistoryFragmentComponent.inject(this);
        //data binding to view
        historyViewModel = new ViewModelProvider(this, viewModelFactory).get(NotificationsHistoryViewModel.class);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_notification, container , false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (navController == null) {
            navController = Navigation.findNavController(view);
        }

        //data binding to view
        binding.setLifecycleOwner(this);
        binding.setViewModel(historyViewModel);
        binding.setFragment(this);


        if (UserActivity.id != -1) {
            NotificationFragmentDirections.ActionNotificationFragmentToOrderDetailFragment actionNotificationFragmentToOrderDetailFragment = NotificationFragmentDirections.actionNotificationFragmentToOrderDetailFragment();
            actionNotificationFragmentToOrderDetailFragment.setOrderId(UserActivity.id);
            navController.navigate(actionNotificationFragmentToOrderDetailFragment);
            UserActivity.id = -1;
        }
        ((UserActivity) context).binding.bottomNavigation.setVisibility(View.VISIBLE);

        //handle back button
        observeBackButton();

        getActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                backButtonClickSource.onNext(true);
            }
        });
        initializeRecyclerNotifications();
    }

    private void initializeRecyclerNotifications() {
        binding.notificationHistoryRecycler.setLayoutManager(new LinearLayoutManager(context));
        binding.notificationHistoryRecycler.setAdapter(historyAdapter);
        historyViewModel.getOrderHistoryMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<NotificationHistoryModel.InnerDatum>>() {
            @Override
            public void onChanged(List<NotificationHistoryModel.InnerDatum> notifications) {
                historyAdapter.submitList(notifications);
            }
        });
    }

    @Override
    public void clickNotification(int position) {
        List<NotificationHistoryModel.InnerDatum> notifications = new ArrayList<>(historyAdapter.getCurrentList());
        NotificationHistoryModel.InnerDatum notification = notifications.get(position);
        NotificationFragmentDirections.ActionNotificationFragmentToOrderDetailFragment actionNotificationFragmentToOrderDetailFragment = NotificationFragmentDirections.actionNotificationFragmentToOrderDetailFragment();
        actionNotificationFragmentToOrderDetailFragment.setOrderId(notification.getRecordId());
        navController.navigate(actionNotificationFragmentToOrderDetailFragment);
    }
    private void setSnackBar() {
        Snackbar snackbar = Snackbar
                .make(binding.coordinate, R.string.once_again_to_exit, Snackbar.LENGTH_LONG);
        snackbar.setTextColor(Color.GREEN);
        snackbar.show();
    }

    private Disposable observeBackButton() {
        return backButtonClickSource
                .debounce(100, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(__ -> setSnackBar())
                .timeInterval(TimeUnit.MILLISECONDS)
                .skip(1)
                .filter(interval -> interval.time() < EXIT_TIMEOUT)
                .subscribe(__ -> getActivity().finish());
    }
}