package com.aca.Retrofit;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Marsel on 6/4/2016.
 */
public interface PolisAPI {

    @GET("sendpolis.aspx?")
    Observable<Void> send( @Query("id") String sppaNo, @Query("uid") String userEmail );

}
