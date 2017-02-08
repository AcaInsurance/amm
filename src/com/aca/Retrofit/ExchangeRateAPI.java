package com.aca.Retrofit;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by Marsel on 6/4/2016.
 */
public interface ExchangeRateAPI {
    @GET("exchangeRate")
    Observable<Double> get();
}
