package com.noelon.edussier.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.noelon.edussier.FileViewModel;
import com.noelon.edussier.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import es.dmoral.toasty.Toasty;


public class SignInFragment extends Fragment {
    EditText emailEt, passwordEt;
    Button signIn;
    FileViewModel viewModel;
    TextView signUp;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        emailEt = view.findViewById(R.id.loginEmailET);
        passwordEt = view.findViewById(R.id.loginPasswordET);
        signIn = view.findViewById(R.id.signInbut);
        signUp = view.findViewById(R.id.signuptext);
        viewModel = ViewModelProviders.of(getActivity()).get(FileViewModel.class);
        progressBar = view.findViewById(R.id.signInprog);
        progressBar.setVisibility(View.INVISIBLE);


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_signInFragment_to_signUpFragment);
            }
        });


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                progressBar.setVisibility(View.VISIBLE);
                if (!(TextUtils.isEmpty(emailEt.getText()) || TextUtils.isEmpty(passwordEt.getText().toString()))) {
                    String logEmail = emailEt.getText().toString().trim();
                    String logPass = passwordEt.getText().toString().trim();
                    viewModel.loginUser(logEmail, logPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                Navigation.findNavController(view).navigate(R.id.action_signInFragment_to_levelFragment);
                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toasty.warning(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toasty.warning(getContext(), "Fill all the boxes please!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }
}
