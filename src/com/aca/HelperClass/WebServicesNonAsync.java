package com.aca.HelperClass;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.aca.amm.Utility;
import com.aca.util.UtilException;
import com.aca.util.UtilService;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Marsel on 16/11/2015.
 */

public class WebServicesNonAsync  {
    private String NAMESPACE = Utility.getNamespace();
    private String URL = Utility.getURL();
    private String SOAP_ACTION = Utility.getSoapAction();
    private String METHOD_NAME = "";

    private SoapObject requestretrive = null;
    private SoapSerializationEnvelope envelope = null;
    private ProgressDialog progressBar;
    private HttpTransportSE androidHttpTransport = null;

    private Context context;
    private boolean error = true;
    private String errorMessage;
    private String stackTrace;

    private ArrayList<HashMap<String, String>> responseList = null;
    private String[] propertyName = null;
    private HashMap<String, String> propertyParam = null;
    private String[] responseArray;
    private boolean loadingDialog;
    private String loadingMessage;

    private static final String TAG = "WebServices";


    public WebServicesNonAsync(@NonNull Context context
            , @NonNull String methodName
            , @NonNull HashMap<String, String> propertyParam
            , @NonNull String[] propertyName
            , @NonNull String[] responseArray) {
        this.context = context;
        this.SOAP_ACTION += methodName;
        this.METHOD_NAME = methodName;
        this.propertyParam = propertyParam;
        this.propertyName = propertyName;
        this.responseArray = responseArray;
    }



    public WebServicesNonAsync(@NonNull Context context
            , @NonNull String methodName
            , @NonNull HashMap<String, String> propertyParam
            , @NonNull String[] propertyName
            , @NonNull String[] responseArray
            , boolean loadingDialog) {
        this.context = context;
        this.SOAP_ACTION += methodName;
        this.METHOD_NAME = methodName;
        this.propertyParam = propertyParam;
        this.propertyName = propertyName;
        this.responseArray = responseArray;
        this.loadingDialog = loadingDialog;
        this.loadingMessage = UtilService.getLoadingMessage();

    }

    public WebServicesNonAsync(@NonNull Context context
            , @NonNull String methodName
            , @NonNull HashMap<String, String> propertyParam
            , @NonNull String[] propertyName
            , @NonNull String[] responseArray
            , boolean loadingDialog
            , String loadingMessage) {
        this.context = context;
        this.SOAP_ACTION += methodName;
        this.METHOD_NAME = methodName;
        this.propertyParam = propertyParam;
        this.propertyName = propertyName;
        this.responseArray = responseArray;
        this.loadingDialog = loadingDialog;
        this.loadingMessage = loadingMessage;
    }


    public WebServicesNonAsync(@NonNull Context context
            , @NonNull String methodName
            , @NonNull HashMap<String, String> propertyParam
            , @NonNull String[] propertyName
            , @NonNull String[] responseArray
            , boolean loadingDialog
            , int ws) {
        this.context = context;
        this.SOAP_ACTION += methodName;
        this.METHOD_NAME = methodName;
        this.propertyParam = propertyParam;
        this.propertyName = propertyName;
        this.responseArray = responseArray;
        this.loadingDialog = loadingDialog;
        this.loadingMessage = loadingMessage;

    }


    public void onPreExecute() {
        if (loadingDialog) {
            if (context != null) {
                progressBar = new ProgressDialog(context);
                progressBar.setCancelable(false);
                progressBar.setMessage(loadingMessage);
                progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressBar.show();
            }
        }

        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.implicitTypes = true;
        envelope.dotNet = true;    //used only if we use the webservice from a dot net file (asmx)
        requestretrive = new SoapObject(NAMESPACE, METHOD_NAME);
        androidHttpTransport = new HttpTransportSE(URL);
    }


    public  ArrayList<HashMap<String, String>> doInBackground() {

        SoapObject table;                        // Contains table of dataset that returned through SoapObject
        SoapObject tableRow;                     // Contains row of table
        SoapObject responseBody;                    // Contains XML content of dataset
        try {
            String namaParam;

            for (int i = 0; i < propertyParam.size(); i++) {
                namaParam = propertyName[i];

                Log.d(TAG, namaParam);
                Log.d(TAG, propertyParam.get(namaParam));

                requestretrive.addProperty(UtilService.setPropertyInfo(namaParam, propertyParam.get(namaParam), String.class));
            }


            envelope.setOutputSoapObject(requestretrive);
            envelope.bodyOut = requestretrive;
            androidHttpTransport.call(SOAP_ACTION, envelope);


            responseBody = (SoapObject) envelope.getResponse();
            responseBody = (SoapObject) responseBody.getProperty(1);

            if (responseBody.getPropertyCount() != 0) {

                responseList = new ArrayList<>();
                table = (SoapObject) responseBody.getProperty(0);

                int iTotalDataFromWebService = table.getPropertyCount();
                HashMap<String, String> hashMap;


                for (int i = 0; i < iTotalDataFromWebService; i++) {
                    tableRow = (SoapObject) table.getProperty(i);
                    hashMap = new HashMap<String, String>();

                    for (int j = 0; j < responseArray.length; j++) {
                        hashMap.put(responseArray[j], (tableRow.getPropertySafelyAsString(responseArray[j])));
                    }
                    responseList.add(hashMap);
                }
            }
            error = false;

        } catch (Exception ex) {
            UtilException exClass =  new UtilException();

            ex.printStackTrace();
            error = true;
            errorMessage = exClass.getException(ex);
            responseList = null;
            stackTrace = ex.getStackTrace().toString();
        }

        return responseList;
    }

    protected void onPostExecute() {

        if (loadingDialog) {
            if (context != null) {
                progressBar.dismiss();
                progressBar.hide();
            }
        }

//        if (error)
//            onFailed(errorMessage);
//        else
//            onSuccess(responseList);

    }

//
//    protected abstract void onSuccess(@NonNull ArrayList<HashMap<String, String>> arrList);
//    protected abstract void onFailed(String message);
//    protected abstract void onCancel();



}