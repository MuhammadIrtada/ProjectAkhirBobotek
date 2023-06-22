package com.example.projectakhir_bobotek;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectakhir_bobotek.databinding.ItemNewsBinding;
import com.example.projectakhir_bobotek.model.News;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.VH>{
    private final List<News> newsList; // Membuat array list News
    public NewsAdapter(List<News> news) {
        this.newsList = news;
    } // Membuat constructor

    @NonNull
    @Override
    public NewsAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNewsBinding binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new NewsAdapter.VH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.VH holder, int position) {
        holder.bind(newsList.get(position));
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class VH extends RecyclerView.ViewHolder{
        final ItemNewsBinding binding;
        public VH(ItemNewsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bind(News news) {
            binding.newsTvTitle.setText(news.getTitle());
            binding.newsTvAuthors.setText(news.getAuthor());
            binding.newsTvAtPublished.setText(news.getPublishedAt());
            binding.newsCv.setOnClickListener(v -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(news.getUrl()));
                Intent browserChooserIntent = Intent.createChooser(browserIntent , "Choose browser of your choice");
                v.getContext().startActivity(browserChooserIntent );
            });
        }
    }
}
