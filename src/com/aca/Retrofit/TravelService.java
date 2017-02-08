package com.aca.Retrofit;

import com.aca.util.DBFlowExclusionStrategy;
import com.aca.util.UtilDate;
import com.aca.util.UtilService;
import com.google.gson.ExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;

public class TravelService {

    private TravelService() {
    }

    public static Retrofit.Builder createService( final RequestBody body ) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .setExclusionStrategies(new ExclusionStrategy[]{new DBFlowExclusionStrategy()})
                .setDateFormat(UtilDate.UTC_DATE_TIME)
                .create();

        Retrofit.Builder builder = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(UtilService.getWsURL());

        if (body == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept( Chain chain ) throws IOException {
                            Request request = chain.request();
                            Request newReq = request.newBuilder().build();
                            return chain.proceed(newReq);
                        }
                    })
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build();

            builder.client(client);
        }
        return builder;
    }

    public static MasterAPI createMasterService( final RequestBody body ) {
        Retrofit.Builder builder = createService(body);
        return builder.build().create(MasterAPI.class);
    }


}
