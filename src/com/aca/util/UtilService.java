package com.aca.util;

import com.aca.amm.BuildConfig;

import org.ksoap2.serialization.PropertyInfo;

/**
 * Created by Marsel on 8/13/2015.
 */
public class UtilService {
    private static final String namespace = "http://tempuri.org/";
    private static final String soapAction = "http://tempuri.org/";
    private static final String urlLive = "http://182.23.65.68/watravelsafe/api/";
    private static final String urlTest = "http://172.16.88.31/waTravelsafe/api/";
    private static final String loadingMessage = "Please wait ...";

    public static String getNamespace() {
        return namespace;
    }


    public static String getWsURL() {
        if (BuildConfig.DEBUG)
            return urlTest;
        else
            return urlLive;
    }


    public static String getLoadingMessage() {
        return loadingMessage;
    }


    public static PropertyInfo setPropertyInfo( String namaProperty, String valueProperty, Object typeProperty ) {
        PropertyInfo pi = new PropertyInfo();

        pi.setName(namaProperty);
        pi.setValue(valueProperty);
        pi.setType(typeProperty);


        return pi;
    }


}
