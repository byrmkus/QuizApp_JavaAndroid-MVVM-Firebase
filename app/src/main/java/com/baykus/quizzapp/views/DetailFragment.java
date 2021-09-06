package com.baykus.quizzapp.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baykus.quizzapp.R;
import com.baykus.quizzapp.databinding.FragmentDetailBinding;
import com.baykus.quizzapp.model.QuizListModel;
import com.baykus.quizzapp.viewmodel.QuizListViewModel;
import com.bumptech.glide.Glide;

import java.util.List;


public class DetailFragment extends Fragment {

    private FragmentDetailBinding binding;
    private NavController navController;
    private int position;
    private QuizListViewModel viewModel;
    private String quizId;
    private long totalQusCount;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        position = DetailFragmentArgs.fromBundle(getArguments()).getPosition();

        viewModel.getQuizListLiveData().observe(getViewLifecycleOwner(), new Observer<List<QuizListModel>>() {
            @Override
            public void onChanged(List<QuizListModel> quizListModels) {
                QuizListModel quiz = quizListModels.get(position);
                binding.txtDetailFragmentTitle.setText(quiz.getTitle());
                Glide.with(view)
                        .load(quiz.getImage())
                        .into(binding.imageDetailFragment);
                binding.txtDetailFragmentDifficulty.setText(quiz.getDifficulty());
                binding.txtDetailsFragmentQuestions.setText(String.valueOf(quiz.getQuestions()));

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.progressBarDetail.setVisibility(View.GONE);
                    }
                }, 2000);

                totalQusCount = quiz.getQuestions();
                quizId = quiz.getQuizId();
                Log.e("quizId ",quizId);
            }
        });

        binding.btnStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailFragmentDirections.ActionDetailFragmentToQuizFragment action =
                        DetailFragmentDirections.actionDetailFragmentToQuizFragment();
                action.setQuizId(quizId);
                action.setTotalQueCount(totalQusCount);
                navController.navigate(action);
            }
        });


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication())).get(QuizListViewModel.class);
    }


}