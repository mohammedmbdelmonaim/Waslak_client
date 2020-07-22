package com.unicom.waslak_client.ui.guest.forget;

import android.content.Context;
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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.unicom.waslak_client.MainApplication;
import com.unicom.waslak_client.R;
import com.unicom.waslak_client.databinding.FragmentForgetPasswordBinding;
import com.unicom.waslak_client.di.component.ApplicationComponent;
import com.unicom.waslak_client.di.component.ForgetPassFragmentComponent;
import com.unicom.waslak_client.model.guest.ForgetPassUSer;
import com.unicom.waslak_client.model.guest.ForgetPasswordModel;
import com.unicom.waslak_client.viewmodel.ViewModelFactory;
import com.unicom.waslak_client.viewmodel.guest.ForgetPasswordViewModel;

import javax.inject.Inject;

public class ForgetPasswordFragment extends Fragment {
    private NavController navController;
    private FragmentForgetPasswordBinding binding;
    private ForgetPasswordViewModel forgetPasswordViewModel;
    @Inject
    ViewModelFactory viewModelFactory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_forget_password , container , false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (navController == null) {
            navController = Navigation.findNavController(view);
        }

        // dagger
        ApplicationComponent applicationComponent = MainApplication.get(getActivity()).getApplicationComponent();
        ForgetPassFragmentComponent forgetPassFragmentComponent = applicationComponent.forgetPassFragmentComponentBuilder().getContext(getActivity()).build();
        forgetPassFragmentComponent.inject(this);

        //data binding to view
        forgetPasswordViewModel = new ViewModelProvider(this, viewModelFactory).get(ForgetPasswordViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setForgetPassViewModel(forgetPasswordViewModel);
        binding.setFragment(this);

        // get user data and validate then call network
        forgetPasswordViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<ForgetPassUSer>() {
            @Override
            public void onChanged(ForgetPassUSer forgetPassUSer) {
                binding.txtLayoutEmail.setError(null);
                if (forgetPassUSer.getStrEmailAddress() == null || forgetPassUSer.getStrEmailAddress().isEmpty()) {
                    binding.txtLayoutEmail.setError(getResources().getString(R.string.email_mandatory));
                    forgetPasswordViewModel.enableButton.setValue(true);
                }else if (!forgetPassUSer.isEmailValid()) {
                    binding.txtLayoutEmail.setError(getResources().getString(R.string.email_error));
                    forgetPasswordViewModel.enableButton.setValue(true);
                }else{
                    forgetPasswordViewModel.isLoading.setValue(true);
                    forgetPasswordViewModel.publishSubject.onNext("null");
                }
            }
        });

        //get login network data
        forgetPasswordViewModel.getForgetNetworkResponse().observe(getViewLifecycleOwner(), new Observer<ForgetPasswordModel>() {
            @Override
            public void onChanged(ForgetPasswordModel forgetPasswordModel) {
                Snackbar snackbar = Snackbar
                        .make(binding.constraintLayout, R.string.sucess , Snackbar.LENGTH_LONG);
                snackbar.setTextColor(Color.GREEN);
                snackbar.show();
                navController.navigateUp();
            }
        });
        //handle error response from server
        forgetPasswordViewModel.getError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Snackbar snackbar = Snackbar
                        .make(binding.loginConstraint, s , Snackbar.LENGTH_LONG)
                        .setAction(R.string.retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                                forgetPasswordViewModel.forgetNetwork();
                            }
                        });
                snackbar.setActionTextColor(Color.GREEN);
                snackbar.setTextColor(Color.RED);
                snackbar.show();
            }
        });
    }

    public void onClickLogin(){
        // return to login fragment
        navController.navigateUp();

        // go to fragment and destroy back stack
//        NavOptions options = new NavOptions.Builder().setPopUpTo(R.id.loginFragment, true).build();
//        navController.navigate(R.id.action_forgetPasswordFragment_to_loginFragment, null, options);
    }
}