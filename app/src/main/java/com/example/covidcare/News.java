package com.example.covidcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.covidcare.NewsPackage.newsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.util.ArrayList;
import java.util.List;

public class News extends AppCompatActivity {

    RecyclerView recyclerView;
    newsAdapter adapter;
    FirebaseAuth auth;
    private List<Article> articleList;
    private int per_page = 20;
    Toolbar toolbar;
    TextView tvBack;
    private String API_KEY = "49ceaaca160941f5872b9a2993f0ff16";
    ProgressDialog progressDialog;


    //final String getEverythingUrl = "http://newsapi.org/v2/everything?q=tesla&from=2021-02-20&sortBy=publishedAt&apiKey=49ceaaca160941f5872b9a2993f0ff16" + API_KEY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        auth = FirebaseAuth.getInstance();
        articleList = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        toolbar = (Toolbar) findViewById(R.id.Toolbar);
        tvBack = (TextView) findViewById(R.id.back_tv);
        recyclerView = (RecyclerView) findViewById(R.id.news_recycleView_1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);


        recyclerView.setLayoutManager(linearLayoutManager);



        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);


        progressDialog.setTitle("               Connecting...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        NewsApi newsApi = new NewsApi();
        newsApi.execute();

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(News.this, dashboard.class);
                startActivity(intent);
            }
        });
    }

    private class NewsApi extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {


            NewsApiClient newsApiClient = new NewsApiClient(API_KEY);

            newsApiClient.getEverything(
                    new EverythingRequest.Builder()
                            .q("Covid")
                            .build(),
                    new NewsApiClient.ArticlesResponseCallback() {

                        @Override
                        public void onSuccess(ArticleResponse response) {
                            articleList.clear();
                            for (int i = 0; i < response.getArticles().size(); i++) {
                                articleList.add(response.getArticles().get(i));

                            }
                            adapter = new newsAdapter(News.this, articleList);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                            progressDialog.dismiss();
                        }
                        @Override
                        public void onFailure(Throwable throwable) {
                            progressDialog.dismiss();
                            System.out.println(throwable.getMessage());
                        }
                    }
            );
            return null;
        }


    }
}


