package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private ImageView imgid;
    private TextView titleid,descriptionid;
    private Button prebtn,morebtn,nextbtn;
    private int count = 0;
    final private int max = 69;
    private String newsurl;
    private String imgurl;

    NewsApiClient newsApiClient = new NewsApiClient("d4ea897a6ac245d38377c25e591e0beb");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgid = findViewById(R.id.img);
        titleid = findViewById(R.id.title);
        prebtn = findViewById(R.id.pre);
        morebtn = findViewById(R.id.more);
        nextbtn = findViewById(R.id.next);
        descriptionid = findViewById(R.id.description);
        prebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count==0){
                    Toast toast = new Toast(MainActivity.this);
                    toast.makeText(MainActivity.this,"First News can't go previous",Toast.LENGTH_LONG).show();
                }
                else{
                    count--;
                    GetNews();
                }
            }
        });
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count==max){
                    Toast toast = new Toast(MainActivity.this);
                    toast.makeText(MainActivity.this,"Last News can't go next",Toast.LENGTH_LONG).show();
                }
                else{
                    count++;
                    GetNews();
                }
            }
        });
        morebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(newsurl));
                startActivity(urlIntent);
            }
        });
        GetNews();
    }
    private void GetNews(){
        newsApiClient.getEverything(
                new EverythingRequest.Builder()
                        .q("technology")
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        String  title = response.getArticles().get(count).getTitle();
                        titleid.setText(title);

                        String description = response.getArticles().get(count).getDescription();
                        descriptionid.setText(description);

                        newsurl = response.getArticles().get(count).getUrl();
                        imgurl = response.getArticles().get(count).getUrlToImage();

                        Picasso.get()
                                .load(imgurl)
                                .into(imgid);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        String msg = throwable.getMessage();
                        Toast toast = new Toast(MainActivity.this);
                        toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

}