package com.tharuka_lakshan.simplenewsapp.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tharuka_lakshan.simplenewsapp.R;
import com.tharuka_lakshan.simplenewsapp.models.PostRetrofit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReadNews extends AppCompatActivity {

    PostRetrofit post;
    private TextView title, description, date;
    private ImageView image;
    private Button readMore, share;

    private String titles, content, dates, images, url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_news);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // getActionBar().setTitle("Post");
        getSupportActionBar().setTitle("Post Read");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            titles = extras.getString("title");
            images = extras.getString("image");
            content = extras.getString("content");
            url = extras.getString("url");
            dates = extras.getString("date");
        }

        title = findViewById(R.id.card_title_item_read);
        description = findViewById(R.id.card_description_item_read);
        date = findViewById(R.id.card_date_item_read);
        image = findViewById(R.id.image_grid_read);

        share = findViewById(R.id.share_read);
        readMore = findViewById(R.id.readmore_read);


        title.setText(titles);

        description.setText(content);
        Picasso.get().load(images).into(image);
        date.setText(dates);

        readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out this link: "+url);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case android.R.id.home:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
