package com.baykus.quizzapp.repository;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.baykus.quizzapp.model.QuestionModel;
import com.baykus.quizzapp.model.QuizListModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;

public class QuestionRepository {


    private FirebaseFirestore db;
    private String quizId;
    private HashMap<String, Long> resultHashMap = new HashMap<>();
    private OnQuestionLoad onQuestionLoad;
    private OnResultAdded onResultAdded;
    private String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private OnResultLoad onResultLoad;

    public QuestionRepository(OnQuestionLoad onQuestionLoad, OnResultAdded onResultAdded,OnResultLoad onResultLoad) {

        db = FirebaseFirestore.getInstance();
        this.onQuestionLoad = onQuestionLoad;
        this.onResultAdded = onResultAdded;
        this.onResultLoad = onResultLoad;


    }

    public void addResults(HashMap<String, Object> resultMap) {
        db.collection("Quiz").document(quizId)
                .collection("results").document(currentUserId)
                .set(resultMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    onResultAdded.onSubmit();
                } else {
                    onResultAdded.onError(task.getException());
                }
            }
        });
    }

    public void getResults() {
        db.collection("Quiz").document(quizId)
                .collection("results").document(currentUserId)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    resultHashMap.put("correct", task.getResult().getLong("correct"));
                    resultHashMap.put("wrong", task.getResult().getLong("wrong"));
                    resultHashMap.put("notAnswered", task.getResult().getLong("notAnswered"));

                    onResultLoad.onResultLoad(resultHashMap);
                } else {

                    onResultLoad.onError(task.getException());

                }
            }
        });


    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public void getQuestions() {

        db.collection("Quiz").document(quizId)
                .collection("qustions").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    onQuestionLoad.onLoad(task.getResult().toObjects(QuestionModel.class));

                } else {
                    onQuestionLoad.onError(task.getException());
                }


            }
        });

    }

    public interface OnResultLoad {
        void onResultLoad(HashMap<String, Long> resultHashMap);
        void onError(Exception e);
    }

    public interface OnQuestionLoad {
        void onLoad(List<QuestionModel> questionModels);

        void onError(Exception e);
    }

    public interface OnResultAdded {

        boolean onSubmit();

        void onError(Exception e);
    }
}
