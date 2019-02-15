package com.tharuka_lakshan.simplenewsapp.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tharuka_lakshan.simplenewsapp.R;
import com.tharuka_lakshan.simplenewsapp.models.MainResponceRetrofit;
import com.tharuka_lakshan.simplenewsapp.models.PostRetrofit;
import com.tharuka_lakshan.simplenewsapp.util.GetDataService;
import com.tharuka_lakshan.simplenewsapp.util.OnBottomReachedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView myRecycleView;
    List<PostRetrofit> post_list;
    int page = 1;
    private boolean isLoading = false;
    private SwipeRefreshLayout swipeRefreshLayout;

    CustomePostAdapter postAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        post_list = new ArrayList<>();
        swipeRefreshLayout = findViewById(R.id.refresh_news);
        myRecycleView = findViewById(R.id.new_recycleView);
        myRecycleView.setLayoutManager(new LinearLayoutManager(this));
        postAdapter = new CustomePostAdapter(this);
        myRecycleView.setAdapter(postAdapter);

        postAdapter.setOnBottomReachedListener(new OnBottomReachedListener() {
            @Override
            public void onBottomReached(int position) {

                if(!isLoading)
                {
                    fetchBlogPosts(page);
                }

            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                post_list.clear();
                fetchBlogPosts(page);
            }
        });

        fetchBlogPosts(page);
    }



    class CustomePostAdapter extends RecyclerView.Adapter<CustomePostAdapter.MyViewHolder>
    {
        OnBottomReachedListener onBottomReachedListener;
        Context mContext;
        class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView title, description, date;
            private ImageView image;
            private CardView post;

            MyViewHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.card_title_item);
                description = itemView.findViewById(R.id.card_description_item);
                date = itemView.findViewById(R.id.card_date_item);
                image = itemView.findViewById(R.id.image_grid);
                post = itemView.findViewById(R.id.post_card);
            }
        }

        void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener){

            this.onBottomReachedListener = onBottomReachedListener;
        }

        CustomePostAdapter(Context context)
        {
            mContext = context;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(mContext)
                    .inflate(R.layout.blog_post_item, viewGroup, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {

            if (i == (post_list.size()-1)) {
                onBottomReachedListener.onBottomReached(i);
            }

            /* Format Date in Nicer way */
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            formatter.setLenient(false);
            Date date = null;
            try {
                date = formatter.parse(post_list.get(i).getDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            formatter = new SimpleDateFormat("MMM dd, HH:mm");
            final String format = formatter.format(date);

            myViewHolder.date.setText(format);
            myViewHolder.title.setText(post_list.get(i).getTitle());
            myViewHolder.description.setText(post_list.get(i).getDescription());
            Picasso.get().load(post_list.get(i).getImage()).into(myViewHolder.image);

            myViewHolder.post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ReadNews.class);
                    intent.putExtra("image", post_list.get(i).getImage());
                    intent.putExtra("date", format);
                    intent.putExtra("content", post_list.get(i).getContent());
                    intent.putExtra("title", post_list.get(i).getTitle());
                    intent.putExtra("url", post_list.get(i).getUrl());
                    startActivity(intent);
                }
            });

        }


        @Override
        public int getItemCount() {
            return post_list.size();
        }
    }

    private void fetchBlogPosts(int page)
    {

        isLoading = true;
        swipeRefreshLayout.setRefreshing(true);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://newsapi.org/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetDataService apiService =
                retrofit.create(GetDataService.class);

        Call<MainResponceRetrofit> call = apiService.getPost("2ea10b840e694cbeb8643d7d6ee7a1c8", 10, page, "technology");
        call.enqueue(new Callback<MainResponceRetrofit>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<MainResponceRetrofit> call, Response<MainResponceRetrofit> response) {

                List<PostRetrofit> items = null;
                try {
                    items = response.body().getPosts();
                    //itemsList.clear();
                    assert items != null;
                    post_list.addAll(items);
                    postAdapter.notifyDataSetChanged();

                    swipeRefreshLayout.setRefreshing(false);
                }catch (Exception e){
                    swipeRefreshLayout.setRefreshing(false);
                }

                isLoading = false;
            }

            @Override
            public void onFailure(Call<MainResponceRetrofit> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        this.page++;
    }

}
