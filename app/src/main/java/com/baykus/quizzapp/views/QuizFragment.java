package com.baykus.quizzapp.views;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.baykus.quizzapp.R;
import com.baykus.quizzapp.databinding.FragmentQuizBinding;
import com.baykus.quizzapp.model.QuestionModel;
import com.baykus.quizzapp.viewmodel.QuestionViewModel;

import java.util.HashMap;
import java.util.List;

public class QuizFragment extends Fragment implements View.OnClickListener {

    private FragmentQuizBinding binding;
    private QuestionViewModel viewModel;
    private NavController navController;
    private String quizId;
    private long totalQuestion;
    private int currentQueNo = 0;
    private boolean canAnswer = false;
    private long timer;
    private CountDownTimer countDownTimer;
    private int notAnswer = 0;
    private int correctAnswer = 0;
    private int wrongAnswer = 0;

    private String answer = "";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentQuizBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication())).get(QuestionViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        quizId = (QuizFragmentArgs.fromBundle(getArguments()).getQuizId());
        totalQuestion = QuizFragmentArgs.fromBundle(getArguments()).getTotalQueCount();

        viewModel.setQuizId(quizId);
        viewModel.getQuestion();

        binding.btnOptionA.setOnClickListener(this);
        binding.btnOptionB.setOnClickListener(this);
        binding.btnOptionC.setOnClickListener(this);
        binding.btnNextQuestion.setOnClickListener(this);

        Log.e("QuizId :", quizId);




        binding.btnCloseQuize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                navController.navigate(R.id.action_quizFragment_to_listFragment);

            }
        });


        loadData();
    }

    private void loadData() {
        enableOptions();
        loadQuestion(1);
    }

    private void enableOptions() {

        binding.btnOptionA.setVisibility(View.VISIBLE);
        binding.btnOptionB.setVisibility(View.VISIBLE);
        binding.btnOptionC.setVisibility(View.VISIBLE);

        // enable buttons,hide feedback tv,hide nextQuiz btn
        binding.btnOptionA.setEnabled(true);
        binding.btnOptionB.setEnabled(true);
        binding.btnOptionC.setEnabled(true);


        binding.txtAnsFeedbackTv.setVisibility(View.INVISIBLE);
        binding.btnNextQuestion.setVisibility(View.INVISIBLE);

    }

    private void loadQuestion(int i) {

        currentQueNo = i;
        viewModel.getQuestionMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<QuestionModel>>() {
            @Override
            public void onChanged(List<QuestionModel> questionModels) {
                binding.txtQuizQuestionTv.setText(String.valueOf(currentQueNo)+ ") "+questionModels.get(i - 1).getQuestion());
                binding.btnOptionA.setText(questionModels.get(i - 1).getOption_a());
                binding.btnOptionB.setText(questionModels.get(i - 1).getOption_b());
                binding.btnOptionC.setText(questionModels.get(i - 1).getOption_c());
                binding.txtAnsFeedbackTv.setText(questionModels.get(i - 1).getAnswer());
                timer = questionModels.get(i - 1).getTimer();
                answer = questionModels.get(i - 1).getAnswer();

                binding.txtQuestionNumberTv.setText(String.valueOf(currentQueNo));
                startTimer();
            }
        });


        canAnswer = true;
    }

    //progressbar ın zamanlayıcısını ayarlıyouruz
    private void startTimer() {


        binding.txtCountTimeQuiz.setText(String.valueOf(timer));
        binding.progressBarQuizCount.setVisibility(View.VISIBLE);
        countDownTimer = new CountDownTimer(timer * 1000, 1000) {

            @Override
            public void onTick(long l) {
                binding.txtCountTimeQuiz.setText(l / 1000 + "");

                Long persent = l / (timer * 10);  //progressbar ilerlemesinin yüzdesel ifadesi
                binding.progressBarQuizCount.setProgress(persent.intValue());


            }

            @Override
            public void onFinish() {

                canAnswer = false;
                binding.txtAnsFeedbackTv.setText("Times Up!!No answer selected");
                notAnswer++;
                showNextBtn();

            }
        }.start();
    }

    private void showNextBtn() {
        if (currentQueNo == totalQuestion) {
            binding.btnNextQuestion.setText("Submit");
            binding.btnNextQuestion.setVisibility(View.VISIBLE);
            binding.btnNextQuestion.setEnabled(true);

        } else {
            binding.btnNextQuestion.setVisibility(View.VISIBLE);
            binding.btnNextQuestion.setEnabled(true);
            binding.txtAnsFeedbackTv.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_option_a:
                verifyAnswer(binding.btnOptionA);
                break;
            case R.id.btn_option_b:
                verifyAnswer(binding.btnOptionB);
                break;
            case R.id.btn_option_c:
                verifyAnswer(binding.btnOptionC);
                break;
            case R.id.btn_nextQuestion:
                if (currentQueNo == totalQuestion) {
                    submitResults();
                } else {
                    currentQueNo++;
                    loadQuestion(currentQueNo);
                    resetOptions();
                }

                break;
        }

    }

    private void resetOptions() {

        binding.txtAnsFeedbackTv.setVisibility(View.INVISIBLE);
        binding.btnNextQuestion.setVisibility(View.INVISIBLE);
        binding.btnNextQuestion.setEnabled(false);
        binding.btnOptionA.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.edit_pg));
        binding.btnOptionB.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.edit_pg));
        binding.btnOptionC.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.edit_pg));
    }

    private void submitResults() {

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("correct", correctAnswer);
        resultMap.put("wrong", wrongAnswer);
        resultMap.put("notAnswered", notAnswer);

        viewModel.addResults(resultMap);

        QuizFragmentDirections.ActionQuizFragmentToResultFragment action =
                QuizFragmentDirections.actionQuizFragmentToResultFragment();
        action.setQuizId(quizId);
        navController.navigate(action);

    }

    private void verifyAnswer(Button btn) {

        if (canAnswer) {
            if (answer.contains(btn.getText())) {
                btn.setBackground(ContextCompat.getDrawable(getContext(), R.color.green));
                correctAnswer++;
                binding.txtAnsFeedbackTv.setText("Correct Answer");
            } else {
                btn.setBackground(ContextCompat.getDrawable(getContext(), R.color.red));
                wrongAnswer++;
                binding.txtAnsFeedbackTv.setText("Wrong Answer \n Correct Answer : " + answer);
            }
        }
        canAnswer = false;
        countDownTimer.cancel();
        showNextBtn();

    }
}