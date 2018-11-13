package com.example.jason.finalproj.Github;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by jason on 2018/1/2.
 */

public interface GithubService{
    @GET("/users/{user}/repos")
    rx.Observable<List<Repos>> getRepository(@Path("user") String user);
}
