package com.aca.amm;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;

import com.aca.database.DBA_MASTER_AGENT;
import com.aca.database.DBA_MASTER_DISC_COMM;
import com.aca.dbflow.GeneralSetting;
import com.aca.util.Var;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.File;

import static android.R.attr.id;
import static android.provider.ContactsContract.CommonDataKinds.Identity.NAMESPACE;

/**
 * Created by Marsel on 16-Dec-16.
 */

public class RetrieveAccount extends AsyncTask<String, Void, Void> {
    private static String NAMESPACE = "http://tempuri.org/";
    private static String URL = Utility.getURL();

    private static String SOAP_ACTION = "http://tempuri.org/ValidateLoginAgentNew";
    private static String METHOD_NAME = "ValidateLoginAgentNew";

    private static String SOAP_ACTION_DETAIL = "http://tempuri.org/GetAgentInfo";
    private static String METHOD_NAME_DETAIL = "GetAgentInfo";

    private static String SOAP_ACTION_DISC_COMM = "http://tempuri.org/GetDiscCommAll";
    private static String METHOD_NAME_DISC_COMM= "GetDiscCommAll";

    private static String SOAP_ACTION_EMAIL_AGENT = "http://tempuri.org/GetEmailAgent";
    private static String METHOD_NAME_EMAIL_AGENT= "GetEmailAgent";


    private ProgressDialog progressBar;

    private PropertyInfo id, pass;

    private HttpTransportSE androidHttpTransport = null;

    private SoapObject requestretrive = null;
    private SoapSerializationEnvelope envelope = null;

    private SoapObject requestretrive2 = null;
    private SoapSerializationEnvelope envelope2 = null;

    private SoapObject requestretrive3 = null;
    private SoapSerializationEnvelope envelope3 = null;

    private SoapObject requestretrive4 = null;
    private SoapSerializationEnvelope envelope4 = null;


    private boolean error;
    private String errorMessage = "";
    private boolean removeOldID = false;

    private Activity activity;
    String userName;
    String password;

    InterfaceLogin interfaceLogin;

    public RetrieveAccount(Activity activity, String userName, String password) {
        this.activity = activity;
        this.userName = userName;
        this.password = password;
    }


    @Override
    protected void onPreExecute() {

        super.onPreExecute();

        progressBar = new ProgressDialog(activity);
        progressBar.setCancelable(false);
        progressBar.setMessage("Please wait ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.show();

        requestretrive = new SoapObject(NAMESPACE, METHOD_NAME);
        requestretrive2 = new SoapObject(NAMESPACE, METHOD_NAME_DETAIL);
        requestretrive3 = new SoapObject(NAMESPACE, METHOD_NAME_DISC_COMM);

        id = new PropertyInfo();
        pass = new PropertyInfo();

        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.implicitTypes = true;
        envelope.dotNet = true;	//used only if we use the webservice from a dot net file (asmx)

        envelope2 = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope2.implicitTypes = true;
        envelope2.dotNet = true;	//used only if we use the webservice from a dot net file (asmx)

        envelope3 = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope3.implicitTypes = true;
        envelope3.dotNet = true;	//used only if we use the webservice from a dot net file (asmx)


        androidHttpTransport = new HttpTransportSE(URL);
    }

    @Override
    protected Void doInBackground(String... params) {


        DBA_MASTER_AGENT dba = null;
        DBA_MASTER_DISC_COMM dbaDC = null;
        Cursor c = null;

        error = false;
        removeOldID = false;

        try{

            id.setName("AgentCode");
            id.setValue(userName.trim());
            id.setType(String.class);
            requestretrive.addProperty(id);

            pass.setName("Password");
            pass.setValue(password);
            pass.setType(String.class);
            requestretrive.addProperty(pass);

            envelope.setOutputSoapObject(requestretrive);
            envelope.bodyOut = requestretrive;
            androidHttpTransport.call(SOAP_ACTION, envelope);

            SoapObject result = (SoapObject)envelope.bodyIn;

            String loginResult = result.getPropertySafelyAsString("ValidateLoginAgentNewResult");

            if (loginResult.toLowerCase().contains("anytype"))
                loginResult = "";

            if(!TextUtils.isEmpty(loginResult)) {
                error = true;
                errorMessage = loginResult;

                return null;
            }
            else
            {

                dba = new DBA_MASTER_AGENT(activity);
                dba.open();


                SoapObject table = null;                        // Contains table of dataset that returned through SoapObject
                SoapObject tableRow = null;                     // Contains row of table
                SoapObject responseBody = null;					// Contains XML content of dataset

                requestretrive2.addProperty(id);
                envelope2.setOutputSoapObject(requestretrive2);
                envelope2.bodyOut = requestretrive2;
                androidHttpTransport.call(SOAP_ACTION_DETAIL, envelope2);

                responseBody = (SoapObject) envelope2.getResponse();
                responseBody = (SoapObject) responseBody.getProperty(1);

                if (responseBody.getPropertyCount() == 0) {
                    error = true;
                    errorMessage = "Data agent kosong";
                    return null;
                }

                table = (SoapObject) responseBody.getProperty(0);
                tableRow = (SoapObject) table.getProperty(0);

                if (!error)
                {

                    c = dba.getRow();
                    c.moveToFirst();

                    //cek apakah sudah ada kode agent di DB

                    if (!GeneralSetting.getParameterValue(Var.GN_LOCK_LOGIN).equalsIgnoreCase(tableRow.getPropertySafelyAsString("U_ID"))
                    && !GeneralSetting.getParameterValue(Var.GN_LOCK_LOGIN).isEmpty()) {
                        error = true;
                        removeOldID = true;
                        return null;
                    }

                   /* if (c.getCount() > 0)
                    {
                        c.moveToFirst();
                        //jika sudah ada, apakah sama?, jika tidak sama maka flag error
                        //1 device harus 1 agent code, tidak bisa di-sharing
                        if (!c.getString(1).equalsIgnoreCase(tableRow.getPropertySafelyAsString("U_ID")))
                        {
                            error = true;
                            removeOldID = true;
                            return null;
                        }
                    }
                    else
                    {
                        File fileOrDirectory = new File(Environment.getExternalStorageDirectory() + "/LoadImg/");
                        Utility.DeleteRecursive(fileOrDirectory);
                    }
*/


                    id.setValue(tableRow.getPropertySafelyAsString("U_ID").toString());

                    requestretrive3.addProperty(id);
                    envelope3.setOutputSoapObject(requestretrive3);
                    envelope3.bodyOut = requestretrive3;
                    androidHttpTransport.call(SOAP_ACTION_DISC_COMM, envelope3);

                    responseBody = (SoapObject) envelope3.getResponse();
                    responseBody = (SoapObject) responseBody.getProperty(1);

                    if (responseBody.getPropertyCount() == 0) {
                        error = true;
                        errorMessage = "Diskon komisi belum ada";
                        return null;

                    }

                    dba.deleteAll();
                    dba.insert(
                            tableRow.getPropertySafelyAsString("BRANCH_ID").toString()
                            , tableRow.getPropertySafelyAsString("U_ID").toString()
                            , tableRow.getPropertySafelyAsString("SIGN_PLACE").toString()
                            , tableRow.getPropertySafelyAsString("MKT_CODE").toString()
                            , tableRow.getPropertySafelyAsString("OFFICE_ID").toString()
                            , tableRow.getPropertySafelyAsString("MAX_ROW").toString()

                            , tableRow.getPropertySafelyAsString("STATUS_USER").toString()
                            , tableRow.getPropertySafelyAsString("USER_EXP_DATE").toString()
                            , tableRow.getPropertySafelyAsString("EMAIL_ADDRESS").toString()
                            , tableRow.getPropertySafelyAsString("PHONE_NO").toString()
                            , tableRow.getPropertySafelyAsString("USER_NAME").toString()
                            , tableRow.getPropertySafelyAsString("U_NAME").toString()
//                            , Utility.md5(tableRow.getPropertySafelyAsString("U_PASS").toString())
                            , (tableRow.getPropertySafelyAsString("U_PASS").toString())
                    );

                    GeneralSetting.insert(Var.GN_LOCK_LOGIN, tableRow.getPropertySafelyAsString("U_ID").toString());

//
                    table = (SoapObject) responseBody.getProperty(0);


                    dbaDC = new DBA_MASTER_DISC_COMM(activity);
                    dbaDC.open();
                    dbaDC.deleteAll();

                    int iTotalDataFromWebService = table.getPropertyCount();

                    for(int i = 0; i < iTotalDataFromWebService; i++)
                    {
                        tableRow = (SoapObject) table.getProperty(i);
                        dbaDC.insert(tableRow.getPropertySafelyAsString("AGENT_CODE").toString()
                                , tableRow.getPropertySafelyAsString("LOB").toString()
                                , tableRow.getPropertySafelyAsString("BRANCH_CODE").toString()
                                , Double.parseDouble(tableRow.getPropertySafelyAsString("DISC_COMM").toString())
                                , Double.parseDouble(tableRow.getPropertySafelyAsString("DISCOUNT").toString())
                                , Double.parseDouble(tableRow.getPropertySafelyAsString("COMMISION").toString()));
                    }
                    error = false;

                }

            }}
        catch (Exception e) {
            error = true;
            e.printStackTrace();
            errorMessage = new MasterExceptionClass(e).getException();
        }
        finally{
            if(dba != null)
                dba.close();

            if(dbaDC != null)
                dbaDC.close();
        }

        return null;
    }

    @Override

    protected void onPostExecute(Void result) {

        super.onPostExecute(result);

        progressBar.hide();
        progressBar.dismiss();

        try{
            if (removeOldID) {
                Utility.showCustomDialogInformation(activity, "Informasi",
                        "User ID lain telah terdaftar pada device ini");
//					ShowDialog(R.layout.dialog_remove_userid, "Konfirmasi", R.id.dlgOK, R.id.dlgCancel);
            }
            else {
                if(error){
                    Utility.showCustomDialogInformation(activity, "Informasi", errorMessage);
                }
                else{
                    SharedPreferences sharedPreferences = activity.getSharedPreferences("com.aca.amm", Context.MODE_PRIVATE);
                    sharedPreferences
                            .edit()
                            .putLong("SESSION", System.currentTimeMillis())
                            .apply();

                    interfaceLogin.loginUser(true);
                }

            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        finally {
            removeOldID = false;
            error = false;
            errorMessage = "";

        }
    }
}
