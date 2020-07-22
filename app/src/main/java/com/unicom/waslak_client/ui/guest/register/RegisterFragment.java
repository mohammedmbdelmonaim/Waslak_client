package com.unicom.waslak_client.ui.guest.register;

import android.graphics.Color;
import android.os.Bundle;

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
import com.unicom.waslak_client.databinding.FragmentRegisterBinding;
import com.unicom.waslak_client.di.component.ApplicationComponent;
import com.unicom.waslak_client.di.component.RegisterFragmentComponent;
import com.unicom.waslak_client.model.guest.RegisterModel;
import com.unicom.waslak_client.model.guest.RegisterUser;
import com.unicom.waslak_client.viewmodel.ViewModelFactory;
import com.unicom.waslak_client.viewmodel.guest.RegisterViewModel;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {
    private NavController navController;
    private FragmentRegisterBinding binding;
    private RegisterViewModel registerViewModel;
    @Inject
    ViewModelFactory viewModelFactory;

    private static final String TAG = "RegisterFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (navController == null) {
            navController = Navigation.findNavController(view);
        }

        //dagger
        ApplicationComponent applicationComponent = MainApplication.get(getActivity()).getApplicationComponent();
        RegisterFragmentComponent fragmentComponent = applicationComponent.registerFragmentComponentBuilder().getContext(getActivity()).build();
        fragmentComponent.inject(this);

        //data binding to view
        registerViewModel = new ViewModelProvider(this, viewModelFactory).get(RegisterViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setRegisterViewModel(registerViewModel);
        binding.setFragment(this);

        // get user data and validate then call network
        registerViewModel.getUserMutableLiveData().observe(getViewLifecycleOwner(), new Observer<RegisterUser>() {
            @Override
            public void onChanged(RegisterUser registerUser) {
                binding.txtLayoutEmail.setError(null);
                binding.txtLayoutPassword.setError(null);
                binding.txtLayoutName.setError(null);
                binding.txtLayoutLastname.setError(null);
                binding.txtLayoutPhone.setError(null);
                if (registerUser.getStrEmailAddress() == null || registerUser.getStrEmailAddress().isEmpty()) {
                    binding.txtLayoutEmail.setError(getResources().getString(R.string.email_mandatory));
                    registerViewModel.enableButton.setValue(true);
                } else if (registerUser.getStrPassword() == null || registerUser.getStrPassword().isEmpty()) {
                    binding.txtLayoutPassword.setError(getResources().getString(R.string.password_mandatory));
                    registerViewModel.enableButton.setValue(true);
                } else if (registerUser.getStrFirstName() == null || registerUser.getStrFirstName().isEmpty()) {
                    binding.txtLayoutName.setError(getResources().getString(R.string.first_name_required));
                    registerViewModel.enableButton.setValue(true);
                } else if (registerUser.getStrLastName() == null || registerUser.getStrLastName().isEmpty()) {
                    binding.txtLayoutLastname.setError(getResources().getString(R.string.last_name_required));
                    registerViewModel.enableButton.setValue(true);
                } else if (registerUser.getStrMobile() == null || registerUser.getStrMobile().isEmpty()) {
                    binding.txtLayoutPhone.setError(getResources().getString(R.string.phone_required));
                    registerViewModel.enableButton.setValue(true);
                } else if (!registerUser.isEmailValid()) {
                    binding.txtLayoutEmail.setError(getResources().getString(R.string.email_error));
                    registerViewModel.enableButton.setValue(true);
                } else if (registerUser.isPasswordValid()) {
                    binding.txtLayoutPassword.setError(getResources().getString(R.string.password_error));
                    registerViewModel.enableButton.setValue(true);
                } else if (!registerUser.isMobileValid()) {
                    binding.txtLayoutPhone.setError(getResources().getString(R.string.phone_error));
                    registerViewModel.enableButton.setValue(true);
                } else {
                    registerViewModel.isLoading.setValue(true);
                    registerViewModel.publishSubject.onNext("null");
                }
            }
        });

        //get register network data
        registerViewModel.getRegisterMutableLiveData().observe(getViewLifecycleOwner(), new Observer<RegisterModel>() {
            @Override
            public void onChanged(RegisterModel registerModel) {
                navController.navigateUp();
                Snackbar snackbar = Snackbar
                        .make(binding.constraintLayout, R.string.sucess , Snackbar.LENGTH_LONG);
                snackbar.setTextColor(Color.GREEN);
                snackbar.show();
            }
        });

        //handle error response from server
        registerViewModel.getError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Snackbar snackbar = Snackbar
                        .make(binding.registerConstraint, s, Snackbar.LENGTH_LONG)
                        .setAction(R.string.retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                                registerViewModel.registerNetwork();
                            }
                        });
                snackbar.setActionTextColor(Color.GREEN);
                snackbar.setTextColor(Color.RED);
                snackbar.show();
            }
        });

    }

    public void onClickLogin(){
        // go to fragment and destroy back stack
        navController.navigateUp();
//        NavOptions options = new NavOptions.Builder().setPopUpTo(R.id.loginFragment, true).build();
//        navController.navigate(R.id.action_registerFragment_to_loginFragment, null, options);
    }
}
