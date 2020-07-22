package com.unicom.waslak_client.ui.guest.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.unicom.waslak_client.databinding.FragmentLoginBinding;
import com.unicom.waslak_client.di.component.ApplicationComponent;
import com.unicom.waslak_client.di.component.LoginFragmentComponent;
import com.unicom.waslak_client.di.qualifier.ActivityContext;
import com.unicom.waslak_client.model.guest.LoginModel;
import com.unicom.waslak_client.model.guest.LoginUser;
import com.unicom.waslak_client.ui.guest.GuestActivity;
import com.unicom.waslak_client.ui.user.UserActivity;
import com.unicom.waslak_client.utils.ChangeLang;
import com.unicom.waslak_client.viewmodel.ViewModelFactory;
import com.unicom.waslak_client.viewmodel.guest.LoginViewModel;

import java.util.Random;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    private NavController navController;
    private FragmentLoginBinding binding;
    private LoginViewModel loginViewModel;
    @Inject
    ViewModelFactory viewModelFactory;

    @Inject
    @ActivityContext
    Context context;


    private static final String TAG = "LoginFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "mohammed onCreateView: ");
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "mohammed onViewCreated: ");
        if (navController == null) {
            navController = Navigation.findNavController(view);
        }


        // dagger
        ApplicationComponent applicationComponent = MainApplication.get(getActivity()).getApplicationComponent();
        LoginFragmentComponent loginFragmentComponent = applicationComponent.loginFragmentComponentBuilder().getContext(getActivity()).build();
        loginFragmentComponent.inject(this);

        //data binding to view
        loginViewModel = new ViewModelProvider(this, viewModelFactory).get(LoginViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setLoginViewModel(loginViewModel);
        binding.setFragment(this);

        // get user data and validate then call network
        loginViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<LoginUser>() {
            @Override
            public void onChanged(LoginUser loginUser) {
                binding.txtLayoutEmail.setError(null);
                binding.txtLayoutPassword.setError(null);
                if (loginUser.getStrEmailAddress() == null || loginUser.getStrEmailAddress().isEmpty()) {
                    binding.txtLayoutEmail.setError(getResources().getString(R.string.email_mandatory));
                    loginViewModel.enableButton.setValue(true);
                } else if (loginUser.getStrPassword() == null || loginUser.getStrPassword().isEmpty()) {
                    binding.txtLayoutPassword.setError(getResources().getString(R.string.password_mandatory));
                    loginViewModel.enableButton.setValue(true);
                } else if (!loginUser.isEmailValid()) {
                    binding.txtLayoutEmail.setError(getResources().getString(R.string.email_error));
                    loginViewModel.enableButton.setValue(true);
                } else if (loginUser.isPasswordValid()) {
                    binding.txtLayoutPassword.setError(getResources().getString(R.string.password_error));
                    loginViewModel.enableButton.setValue(true);
                } else {
                    loginViewModel.isLoading.setValue(true);
                    loginViewModel.publishSubject.onNext(new Random().nextInt(258 * 15));
                }
            }
        });

        //get login network data
        loginViewModel.getLoginNetworkResponse().observe(getViewLifecycleOwner(), new Observer<LoginModel>() {
            @Override
            public void onChanged(LoginModel loginModel) {
                Snackbar snackbar = Snackbar
                        .make(binding.constraintLayout, R.string.sucess , Snackbar.LENGTH_LONG);
                snackbar.setTextColor(Color.GREEN);
                snackbar.show();
                startActivity(new Intent(context , UserActivity.class));
                ((GuestActivity) context).overridePendingTransition(R.anim.animate_slide_in_left,
                        R.anim.animate_slide_out_right);
                ((GuestActivity) context).finish();
            }
        });
        //handle error response from server
        loginViewModel.getError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Snackbar snackbar = Snackbar
                        .make(binding.loginConstraint, s, Snackbar.LENGTH_LONG)
                        .setAction(R.string.retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                                loginViewModel.loginNetwork();
                            }
                        });
                snackbar.setActionTextColor(Color.GREEN);
                snackbar.setTextColor(Color.RED);
                snackbar.show();

            }
        });
    }

    public void onClickForget(){
        navController.navigate(R.id.action_loginFragment_to_forgetPasswordFragment);
    }

    public void onClickRegister(){
        navController.navigate(R.id.action_loginFragment_to_registerFragment);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "mohammed onStart: ");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "mohammed onAttach: ");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "mohammed onCreate: ");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "mohammed onActivityCreated: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "mohammed onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "mohammed onPause: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "mohammed onStop: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "mohammed onDestroyView: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "mohammed onDestroy: ");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "mohammed onDetach: ");
    }
}