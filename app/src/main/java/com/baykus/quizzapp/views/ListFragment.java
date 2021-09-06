package com.baykus.quizzapp.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.baykus.quizzapp.adapter.QuizListAdapter;
import com.baykus.quizzapp.databinding.FragmentListBinding;
import com.baykus.quizzapp.model.QuizListModel;
import com.baykus.quizzapp.viewmodel.QuizListViewModel;

import java.util.List;


public class ListFragment extends Fragment implements QuizListAdapter.OnItemClickedListner {

    private FragmentListBinding binding;
    private NavController navController;
    private QuizListViewModel viewModel;
    private QuizListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        binding.recyclerListQuiz.setHasFixedSize(true);
        binding.recyclerListQuiz.setLayoutManager(new GridLayoutManager(getContext(), 2));

        adapter = new QuizListAdapter(this);
        binding.recyclerListQuiz.setAdapter(adapter);

        viewModel.getQuizListLiveData().observe(getViewLifecycleOwner(), new Observer<List<QuizListModel>>() {
            @Override
            public void onChanged(List<QuizListModel> quizListModels) {
                binding.progressBarQuizList.setVisibility(View.GONE);
                adapter.setQuizListModels(quizListModels);
                adapter.notifyDataSetChanged();

            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication())).get(QuizListViewModel.class);
    }

    @Override
    public void onItemClick(int position) {

        ListFragmentDirections.ActionListFragmentToDetailFragment action =
                ListFragmentDirections.actionListFragmentToDetailFragment();

        action.setPosition(position);
        navController.navigate(action);
    }
}