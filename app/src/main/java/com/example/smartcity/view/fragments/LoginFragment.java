package com.example.smartcity.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.smartcity.MainActivity;
import com.example.smartcity.R;
import com.example.smartcity.data.model.User;
import com.example.smartcity.viewmodel.LoginViewModel;

import java.util.Objects;

public class LoginFragment extends Fragment {
    private LoginViewModel loginViewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(R.string.login);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        final EditText editTextEmailAddress = view.findViewById(R.id.editTextEmailAddress);
        final EditText editTextPassword = view.findViewById(R.id.editTextPassword);
        final Button loginButton = view.findViewById(R.id.loginButton);
        final Button registerButton = view.findViewById(R.id.registerButton);
        loginButton.setEnabled(false);

        loginViewModel.getLoginFormState().observe(getViewLifecycleOwner(), loginFormState -> {
            if (loginFormState == null){
                return;
            }
            loginButton.setEnabled(loginFormState.isDataValid());
            if (loginFormState.getEmailError() != null){
                editTextEmailAddress.setError(getString(loginFormState.getEmailError()));
            }
            if (loginFormState.getPasswordError() != null){
                editTextPassword.setError(getString(loginFormState.getPasswordError()));
            }
        });

        loginViewModel.getStatutCode().observe(getViewLifecycleOwner(), error -> {
            if(error == 500){
                Toast.makeText(requireContext(), R.string.bad_credentials, Toast.LENGTH_LONG).show();
            }
        });

        loginViewModel.getLoginResult().observe(getViewLifecycleOwner(), loginResult -> {
            if(loginResult != null){
                updateUiWithUser(loginResult);
                requireActivity().finish();
            }
        });

        TextWatcher afterTextChagedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Voluntary ignored
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Voluntary ignored
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(editTextEmailAddress.getText().toString(), editTextPassword.getText().toString());
            }
        };

        editTextEmailAddress.addTextChangedListener(afterTextChagedListener);
        editTextPassword.addTextChangedListener(afterTextChagedListener);

        loginButton.setOnClickListener(v -> {
            loginViewModel.login(editTextEmailAddress.getText().toString(), editTextPassword.getText().toString());
        });

        registerButton.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment));
    }

    private void updateUiWithUser(User user){
        Toast.makeText(requireContext(), getString(R.string.welcome) + user.getFirstname(), Toast.LENGTH_LONG).show();

        SharedPreferences.Editor sharedPref = requireActivity().getSharedPreferences("JSONWEBTOKEN", Context.MODE_PRIVATE).edit();

        sharedPref.putString("JSONWEBTOKEN", loginViewModel.getToken());
        sharedPref.apply();

        startActivity(new Intent(requireActivity(), MainActivity.class));
    }
}
