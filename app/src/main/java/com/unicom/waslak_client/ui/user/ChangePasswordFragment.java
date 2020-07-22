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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.unicom.waslak_client.MainApplication;
import com.unicom.waslak_client.R;
import com.unicom.waslak_client.databinding.FragmentChangePasswordBinding;
import com.unicom.waslak_client.databinding.FragmentMyAccountBinding;
import com.unicom.waslak_client.di.component.ApplicationComponent;
import com.unicom.waslak_client.di.component.ChangePasswordFragmentComponent;
import com.unicom.waslak_client.di.qualifier.ActivityContext;
import com.unicom.waslak_client.model.user.ChangePassModel;
import com.unicom.waslak_client.model.user.ChangePassUser;
import com.unicom.waslak_client.viewmodel.ViewModelFactory;
import com.unicom.waslak_client.viewmodel.user.ChangePasswordViewModel;

import javax.inject.Inject;


public class ChangePasswordFragment extends Fragment {
    private FragmentChangePasswordBinding binding;
    private NavController navController;
    @ActivityContext
    @Inject
    Context context;
    @Inject
    ViewModelFactory viewModelFactory;
    private ChangePasswordViewModel changePasswordViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_change_password, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (navController == null) {
            navController = Navigation.findNavController(view);
        }
        binding.changePassToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigateUp();
            }
        });
        getActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navController.navigateUp();
            }
        });
        // dagger
        ApplicationComponent applicationComponent = MainApplication.get(getActivity()).getApplicationComponent();
        ChangePasswordFragmentComponent changePasswordFragmentComponent = applicationComponent.changePasswordFragmentComponentBuilder().getContext(getActivity()).build();
        changePasswordFragmentComponent.inject(this);

        //data binding to view
        changePasswordViewModel = new ViewModelProvider(this, viewModelFactory).get(ChangePasswordViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setViewModel(changePasswordViewModel);
        binding.setFragment(this);

        // get user data and validate then call network
        changePasswordViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<ChangePassUser>() {
            @Override
            public void onChanged(ChangePassUser changePassUser) {
                binding.txtLayoutConfirmPassword.setError(null);
                binding.txtLayoutNewPassword.setError(null);
                binding.txtLayoutPassword.setError(null);
                if (changePassUser.getOldPassword() == null || changePassUser.getOldPassword().isEmpty()) {
                    binding.txtLayoutPassword.setError(getResources().getString(R.string.password_mandatory));
                    changePasswordViewModel.enableButton.setValue(true);
                } else if (changePassUser.getNewPassword() == null || changePassUser.getNewPassword().isEmpty()) {
                    binding.txtLayoutNewPassword.setError(getResources().getString(R.string.password_mandatory));
                    changePasswordViewModel.enableButton.setValue(true);
                }else if (changePasswordViewModel.passwordConfirm.getValue() == null || changePasswordViewModel.passwordConfirm.getValue().isEmpty()){
                    binding.txtLayoutConfirmPassword.setError(getResources().getString(R.string.password_mandatory));
                    changePasswordViewModel.enableButton.setValue(true);
                }else if (changePassUser.isPasswordValid()) {
                    binding.txtLayoutNewPassword.setError(getResources().getString(R.string.password_error));
                    changePasswordViewModel.enableButton.setValue(true);
                } else if (!changePassUser.getNewPassword().equals(changePasswordViewModel.passwordConfirm.getValue())) {
                    binding.txtLayoutConfirmPassword.setError(getString(R.string.password_not_match));
                    changePasswordViewModel.enableButton.setValue(true);
                } else {
                    changePasswordViewModel.isLoading.setValue(true);
                    changePasswordViewModel.publishSubject.onNext("null");
                }
            }
        });


        //get login network data
        changePasswordViewModel.getLChangePassNetworkResponse().observe(getViewLifecycleOwner(), new Observer<ChangePassModel>() {
            @Override
            public void onChanged(ChangePassModel changePassModel) {
                Snackbar snackbar = Snackbar
                        .make(binding.constraint, R.string.sucess , Snackbar.LENGTH_LONG);
                snackbar.setTextColor(Color.GREEN);
                snackbar.show();
                navController.navigateUp();
            }
        });
        //handle error response from server
        changePasswordViewModel.getError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Snackbar snackbar = Snackbar
                        .make(binding.constraint, s, Snackbar.LENGTH_LONG)
                        .setAction(R.string.retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                                changePasswordViewModel.changeNetwork();
                            }
                        });
                snackbar.setActionTextColor(Color.GREEN);
                snackbar.setTextColor(Color.RED);
                snackbar.show();

            }
        });
    }
}