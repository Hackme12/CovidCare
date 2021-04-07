package com.example.covidcare.NewsPackage;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covidcare.NewsInDetail;
import com.example.covidcare.R;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Source;
import com.squareup.picasso.Picasso;

import java.util.List;

public class newsAdapter extends RecyclerView.Adapter<newsAdapter.myViewHolder> {


   List<com.kwabenaberko.newsapilib.models.Article>articleList;
    Context context;



    public newsAdapter(Context context, List<com.kwabenaberko.newsapilib.models.Article> articles){
        this.context = context;
        this.articleList = articles;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_layout,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.newsTitle.setText(articleList.get(position).getTitle());

        holder.news_url.setText(articleList.get(position).getSource().getName());
        String date = articleList.get(position).getPublishedAt();
        String formatted_date = date_format(date);
        holder.publishedDate.setText(formatted_date);
        String imageUrl = articleList.get(position).getUrlToImage();
        Picasso.get().load(imageUrl).into(holder.imageView);




        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(view.getContext(), NewsInDetail.class);
                intent.putExtra("URL", articleList.get(position).getUrl());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }



    public class myViewHolder extends RecyclerView.ViewHolder{

        TextView newsTitle;
        TextView publishedDate;
        CardView cardView;
        ImageView imageView;
        TextView news_url;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            newsTitle = itemView.findViewById(R.id.news_title);
            publishedDate = itemView.findViewById(R.id.news_published_date);
            imageView = itemView.findViewById(R.id.imageView);
            news_url = itemView.findViewById(R.id.news_url);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }

    public String  date_format(String date){

        String subString = date.substring(0,10);
        return subString;



    }




}
