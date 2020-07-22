package com.unicom.waslak_client.ui.user;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.unicom.waslak_client.MainApplication;
import com.unicom.waslak_client.R;
import com.unicom.waslak_client.databinding.FragmentMyAccountBinding;
import com.unicom.waslak_client.di.component.ApplicationComponent;
import com.unicom.waslak_client.di.component.MyAccountFragmentComponent;
import com.unicom.waslak_client.di.qualifier.ActivityContext;
import com.unicom.waslak_client.model.user.LogoutModel;
import com.unicom.waslak_client.ui.guest.GuestActivity;
import com.unicom.waslak_client.utils.ChangeLang;
import com.unicom.waslak_client.utils.PreferenceUtils;
import com.unicom.waslak_client.viewmodel.ViewModelFactory;
import com.unicom.waslak_client.viewmodel.user.MyAccountViewModel;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class MyAccountFragment extends Fragment {
    private FragmentMyAccountBinding binding;
    private NavController navController;
    @ActivityContext
    @Inject
    Context context;
    @Inject
    ViewModelFactory viewModelFactory;
    private MyAccountViewModel myAccountViewModel;
    @Inject
    public PreferenceUtils preference;
    private PublishSubject<Boolean> backButtonClickSource = PublishSubject.create();
    private static final long EXIT_TIMEOUT = 2000;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // dagger
        ApplicationComponent applicationComponent = MainApplication.get(getActivity()).getApplicationComponent();
        MyAccountFragmentComponent myAccountFragmentComponent = applicationComponent.myAccountFragmentComponentBuilder().getContext(getActivity()).build();
        myAccountFragmentComponent.inject(this);

        //data binding to view
        myAccountViewModel = new ViewModelProvider(this, viewModelFactory).get(MyAccountViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_account, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (navController == null) {
            navController = Navigation.findNavController(view);
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
        binding.setLifecycleOwner(this);
        binding.setFragment(this);
        binding.setViewModel(myAccountViewModel);
        checkSwitchChange();
    }


    @Override
    public void onResume() {
        super.onResume();
        checkSwitchChange();
    }

    private boolean isCheck;

    private void checkSwitchChange() {
        binding.switchNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCheck = preference.getNotification();
                if (isChecked == isCheck) {
                    return;
                }
                myAccountViewModel.notification.setValue(isChecked);
                myAccountViewModel.publishSubject.onNext("null");

            }
        });
    }

    public void onClickLogout() {
        new MaterialAlertDialogBuilder(context)
                .setTitle("")
                .setMessage(R.string.sure_logout)
                .setNegativeButton(R.string.logout, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myAccountViewModel.getLogoutMutableLiveData().observe(getViewLifecycleOwner(), new Observer<LogoutModel>() {
                            @Override
                            public void onChanged(LogoutModel logoutModel) {
                                preference.saveTokenUser("");
                                startActivity(new Intent(context, GuestActivity.class));
                                ((UserActivity) context).overridePendingTransition(R.anim.animate_diagonal_right_enter,
                                        R.anim.animate_diagonal_right_exit);
                                ((UserActivity) context).finish();
                            }
                        });
                    }
                })
                .setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    public void clickChangePass() {
        navController.navigate(R.id.action_myAccountFragment_to_changePasswordFragment);
    }


    public void onClickLanguage() {
        new MaterialAlertDialogBuilder(context)
                .setTitle("")
                .setMessage(R.string.choose_lang)
                .setNegativeButton(R.string.arabic, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ChangeLang.setNewLocale(context, "ar");
                        startActivity(new Intent(context, UserActivity.class));
                        ((Activity) context).overridePendingTransition(R.anim.animate_zoom_enter,
                                R.anim.animate_zoom_exit);
                        ((UserActivity) context).finish();
                    }
                })
                .setPositiveButton(R.string.english, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ChangeLang.setNewLocale(context, "en");
                        startActivity(new Intent(context, UserActivity.class));
                        ((Activity) context).overridePendingTransition(R.anim.animate_zoom_enter,
                                R.anim.animate_zoom_exit);
                        ((UserActivity) context).finish();
                    }
                })
                .show();
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