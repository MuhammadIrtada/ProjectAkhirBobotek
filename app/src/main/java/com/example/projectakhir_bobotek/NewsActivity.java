package com.example.projectakhir_bobotek;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.projectakhir_bobotek.api.ApiConfig;
import com.example.projectakhir_bobotek.databinding.ActivityNewsBinding;
import com.example.projectakhir_bobotek.model.News;
import com.example.projectakhir_bobotek.model.NewsRespopnse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsActivity extends AppCompatActivity {

    ActivityNewsBinding binding;

    NewsAdapter adapterNews;

    List<News> newsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        LinearLayoutManager mLayout = new LinearLayoutManager(this);
        binding.newsRv.setLayoutManager(mLayout);

        getAllNews();
    }

    public void getAllNews() {
        Call<NewsRespopnse> req = ApiConfig.getApiService().getNews();
        req.enqueue(new Callback<NewsRespopnse>() {
            @Override
            public void onResponse(Call<NewsRespopnse> call, Response<NewsRespopnse> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        newsList = response.body().getArticles();
                        System.out.println("BERHASILL");
                        adapterNews = new NewsAdapter(newsList);
                        binding.newsRv.setAdapter(adapterNews);
                    }
                }
            }
            @Override
            public void onFailure(Call<NewsRespopnse> call, Throwable t) {
                System.out.println("GAGALL");
                System.out.println(call);
            }
        });
    }
}