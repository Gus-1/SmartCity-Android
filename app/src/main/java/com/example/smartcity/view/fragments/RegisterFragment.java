package com.example.smartcity.view.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.smartcity.R;
import com.example.smartcity.viewmodel.RegisterViewModel;

import java.util.Objects;

public class RegisterFragment extends Fragment {
    private EditText firstName, lastName, password, email;
    private DatePicker birthDate;
    private Button registerButton, cancelButton;
    private RegisterViewModel registerViewModel;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_register, container, false);

        firstName = root.findViewById(R.id.registerFirstName);
        lastName = root.findViewById(R.id.registerLastName);
        email = root.findViewById(R.id.registerEmail);
        password = root.findViewById(R.id.registerPassWord);
        birthDate = root.findViewById(R.id.registerBirthDate);
        registerButton = root.findViewById(R.id.registerButtonForm);
        cancelButton = root.findViewById(R.id.cancelButtonForm);
        registerButton.setEnabled(false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("Inscription");

        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);


        cancelButton.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_loginFragment));

        registerViewModel.getRegisterFormState().observe(getViewLifecycleOwner(), registerFormState -> {
            if (registerFormState == null){
                return;
            }
            registerButton.setEnabled(registerFormState.isDataValid());
            if (registerFormState.getFirstNameError() != null){
                firstName.setError(getString(registerFormState.getFirstNameError()));
            }
            if (registerFormState.getLastNameError() != null){
                lastName.setError(getString(registerFormState.getLastNameError()));
            }
            if (registerFormState.getEmailError() != null){
                email.setError(getString(registerFormState.getEmailError()));
            }
            if (registerFormState.getPasswordError() != null){
                password.setError(getString(registerFormState.getPasswordError()));
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
                registerViewModel.registerDataChanged(firstName.getText().toString(),
                        lastName.getText().toString(), null,
                        email.getText().toString(), password.getText().toString());
            }
        };

        firstName.addTextChangedListener(afterTextChagedListener);
        lastName.addTextChangedListener(afterTextChagedListener);
        email.addTextChangedListener(afterTextChagedListener);
        password.addTextChangedListener(afterTextChagedListener);


        registerButton.setOnClickListener(v -> {
            registerViewModel.addUser(
                    firstName.getText().toString(),
                    lastName.getText().toString(),
                    email.getText().toString(),
                    password.getText().toString(),
                    birthDate.getYear() + "-" + (birthDate.getMonth() + 1) + "-" + birthDate.getDayOfMonth()
            );
            Toast.makeText(requireContext(), R.string.account_created, Toast.LENGTH_LONG).show();
            Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_loginFragment);
        });
    }
}
