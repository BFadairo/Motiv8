package com.onramp.android.takehome.data.Retrofit;

import com.onramp.android.takehome.model.Quote;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface GetQuoteData {

    @Headers({
            "X-RapidAPI-key: 4bRCbQOwQRmshxciCluWsNOO0g9rp1jaDLQjsnwQOsCyeQQAm5"
    })
    @GET("quotes/")
    Call<List<Quote>> getQuote(@Query(value = "t", encoded = true) String category, @Query(value = "maxR") int maxResult,
            @Query(value = "size") String size);
}
