package com.baykus.quizzapp.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baykus.quizzapp.R;
import com.baykus.quizzapp.databinding.FragmentQuizBinding;
import com.baykus.quizzapp.databinding.FragmentResultBinding;
import com.baykus.quizzapp.viewmodel.QuestionViewModel;

import java.util.HashMap;


public class ResultFragment extends Fragment {

    private FragmentResultBinding binding;
    private NavController navController;
    private QuestionViewModel viewModel;
    private String quizId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication())).get(QuestionViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);


        binding.btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_resultFragment_to_listFragment2);
            }
        });

        quizId = ResultFragmentArgs.fromBundle(getArguments()).getQuizId();
        viewModel.setQuizId(quizId);
        viewModel.getResults();
        viewModel.getResultMutableLiveData().observe(getViewLifecycleOwner(), new Observer<HashMap<String, Long>>() {
            @Override
            public void onChanged(HashMap<String, Long> stringLongHashMap) {
                Long corrent = stringLongHashMap.get("correct");
                Long wrong = stringLongHashMap.get("wrong");
                Long noAnswer = stringLongHashMap.get("notAnswered");

                binding.correctAnswersTv.setText(corrent.toString());
                binding.wrongAnswerTv.setText(wrong.toString());
                binding.notAnswerenQuestionsTv.setText(noAnswer.toString());

                Long total = corrent + wrong + noAnswer;
                Long persent = (corrent*100)/total;
                binding.txtCountTimeQuiz.setText(persent.toString());
                binding.progressBarResultCount.setProgress(persent.intValue());
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentResultBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}