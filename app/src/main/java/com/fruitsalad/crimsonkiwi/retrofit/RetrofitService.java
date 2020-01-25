package com.fruitsalad.crimsonkiwi.retrofit;

import com.fruitsalad.crimsonkiwi.model.Quote;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitService {

    @GET("/all")
    List<Quote> getAllQuotes();

    @GET()
    List<Quote> getCategorizedQuotes(@Query("category") String category);

}
