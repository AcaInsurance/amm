package com.aca.Retrofit;

import com.aca.dbflow.StandardField;
import com.aca.dbflow.SubProduct;
import com.aca.dbflow.SubProductPlan;
import com.aca.dbflow.SubProductPlanAdd;
import com.aca.dbflow.SubProductPlanBDA;
import com.aca.dbflow.SubProductPlanBasic;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import rx.Observable;

/**
 * Created by Marsel on 6/4/2016.
 */
public interface MasterAPI {


    @GET("SubProduct")
    @Headers({
            "Content-Type: application/json;charset=utf-8",
            "Accept: application/json"
    })
    Observable<List<SubProduct>> SubProduct();

    @GET("SubProductPlan")
    @Headers({
            "Content-Type: application/json;charset=utf-8",
            "Accept: application/json"
    })
    Observable<List<SubProductPlan>> SubProductPlan();

    @GET("SubProductPlanAdd")
    @Headers({
            "Content-Type: application/json;charset=utf-8",
            "Accept: application/json"
    })
    Observable<List<SubProductPlanAdd>> SubProductPlanAdd();

    @GET("SubProductPlanBasic")
    @Headers({
            "Content-Type: application/json;charset=utf-8",
            "Accept: application/json"
    })
    Observable<List<SubProductPlanBasic>> SubProductPlanBasic();

    @GET("SubProductPlanBDA")
    @Headers({
            "Content-Type: application/json;charset=utf-8",
            "Accept: application/json"
    })
    Observable<List<SubProductPlanBDA>> SubProductPlanBDA();


}
