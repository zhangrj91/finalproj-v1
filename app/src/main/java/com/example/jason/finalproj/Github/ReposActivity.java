package com.example.jason.finalproj.Github;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.jason.finalproj.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by jason on 2017/12/19.
 */

public class ReposActivity extends AppCompatActivity {
    private ListView mlistView;
    private List<Repos> myListItems = new ArrayList<>();
    private Reposadapter adapter1 = new Reposadapter(ReposActivity.this, R.layout.item,myListItems);
    private GithubService service;
    private ProgressBar progressbar;
    private Toolbar toolbar;
    public static final String baseUrl = "https://api.github.com/";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        //toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar();

        progressbar = (ProgressBar) findViewById(R.id.progress2);
        progressbar.setVisibility(View.VISIBLE);

        String user = getIntent().getStringExtra("user");
        Log.i("TAG", user);
        mlistView = (ListView) findViewById(R.id.list_item);
        Retrofit retrofit = createRetrofit(baseUrl);
        GithubService githubService = retrofit.create(GithubService.class);
        githubService.getRepository(user)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Repos>>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("完成传输");
                        progressbar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Github-Demo",e.getMessage());
                    }

                    @Override
                    public void onNext(List<Repos> repositories) {
                        myListItems.addAll(repositories);
                        adapter1.notifyDataSetChanged();
                        Log.d("Github-Demo","成功");
                    }
                });
        mlistView.setAdapter(adapter1);
    }

    private static OkHttpClient createOkHttp(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30,TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .build();
        return okHttpClient;
    }

    private static Retrofit createRetrofit(String baseUrl){
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(createOkHttp())
                .build();
    }


    //toolbar设置函数
    public void setToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.previous);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
