package com.baykus.quizzapp.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baykus.quizzapp.R;
import com.baykus.quizzapp.viewmodel.AuthViewModel;
import com.google.firebase.FirebaseApp;


public class SprashFragment extends Fragment {

    private AuthViewModel viewModel;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sprash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull  View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication())). get(AuthViewModel.class);

        navController = Navigation.findNavController(view);

    }

    @Override
    public void onCreate(@Nullable  Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Handler handler =new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (viewModel.getCurrentUser()!=null){
                    navController.navigate(R.id.action_sprashFragment_to_signUpFragment);
                }else {
                    navController.navigate(R.id.action_sprashFragment_to_signInFragment);

                }
            }
        },2000);
    }
}