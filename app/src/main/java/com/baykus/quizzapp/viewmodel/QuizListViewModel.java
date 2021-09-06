package com.baykus.quizzapp.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.baykus.quizzapp.model.QuizListModel;
import com.baykus.quizzapp.repository.QuizListRepository;

import java.util.List;

public class QuizListViewModel extends ViewModel implements QuizListRepository.onFirestoreTaskComplate {

    private MutableLiveData<List<QuizListModel>> quizListLiveData = new MutableLiveData<>();

    private QuizListRepository repository = new QuizListRepository(this);


    public QuizListViewModel() {
        repository.getQuizData();
    }


    public MutableLiveData<List<QuizListModel>> getQuizListLiveData() {
        return quizListLiveData;
    }

    @Override
    public void quizDataLoaded(List<QuizListModel> quizListModels) {

        quizListLiveData.setValue(quizListModels);

    }

    @Override
    public void onError(Exception e) {

        Log.e("QuizERROR","onError: "+e.getMessage());

    }
}
