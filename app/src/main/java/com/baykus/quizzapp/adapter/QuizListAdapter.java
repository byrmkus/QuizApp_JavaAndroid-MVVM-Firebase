package com.baykus.quizzapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baykus.quizzapp.databinding.EachQuizBinding;
import com.baykus.quizzapp.model.QuizListModel;
import com.bumptech.glide.Glide;

import java.util.List;

public class QuizListAdapter extends RecyclerView.Adapter<QuizListAdapter.QuizListViewHolder> {

    private List<QuizListModel> quizListModels;
    private OnItemClickedListner onItemClickedListner;

    public void setQuizListModels(List<QuizListModel> quizListModels) {
        this.quizListModels = quizListModels;
    }

    public QuizListAdapter(OnItemClickedListner onItemClickedListner) {
        this.onItemClickedListner = onItemClickedListner;
    }

    @NonNull
    @Override
    public QuizListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        EachQuizBinding binding = EachQuizBinding.inflate(inflater, parent, false);


        return new QuizListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizListAdapter.QuizListViewHolder holder, int position) {

        QuizListModel model = quizListModels.get(position);
        holder.binding.quizTitleList.setText(model.getTitle());
        Glide.with(holder.itemView)
                .load(model.getImage())
                .into(holder.binding.quizImageList);


    }

    @Override
    public int getItemCount() {

        if (quizListModels == null)
            return 0;
        else
            return quizListModels.size();
    }

    public class QuizListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        EachQuizBinding binding;

        public QuizListViewHolder(EachQuizBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.constrainLayout.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            onItemClickedListner.onItemClick(getAdapterPosition());
        }
    }

    public interface OnItemClickedListner{
        void onItemClick(int position);
    }
}
