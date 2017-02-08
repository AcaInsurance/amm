package com.aca.dal;

import android.app.Activity;
import android.database.Cursor;
import android.database.SQLException;
import android.test.ActivityTestCase;
import android.text.TextUtils;

import com.aca.database.DBA_MASTER_PRODUCT_SETTING;
import com.aca.database.DBA_PRODUCT_MAIN;
import com.aca.database.DBA_PRODUCT_OTOMATE;
import com.aca.dbflow.SppaMain;
import com.aca.dbflow.SubProduct;
import com.aca.dbflow.SubProductPlanAdd;
import com.aca.dbflow.SubProductPlanBasic;
import com.aca.dbflow.SubProduct_Table;
import com.aca.dbflow.VersionAndroid;
import com.aca.holder.PremiHolder;
import com.aca.util.UtilDate;
import com.aca.util.Var;
import com.raizlabs.android.dbflow.sql.language.Select;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.w3c.dom.Text;

import static android.text.TextUtils.isEmpty;

/**
 * Created by marsel on 30/9/2016.
 */
public class Scalar {

    public static String getProdukName(Activity activity, String kodeProduk) {
        try {
            Cursor c;
            DBA_MASTER_PRODUCT_SETTING dba = new DBA_MASTER_PRODUCT_SETTING(activity);
            dba.open();
            c = dba.getRow(kodeProduk);
            if (!c.moveToFirst()) return "";

            String produkName = c.getString(c.getColumnIndex(DBA_MASTER_PRODUCT_SETTING.PRODUCT_NAME));
            return produkName;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String getProdukName(Activity activity, String productName, long sppaId) {
        try {
            if (productName.equalsIgnoreCase("OTOMATE")) {
                DBA_PRODUCT_OTOMATE dbOtomate = new DBA_PRODUCT_OTOMATE(activity);
                dbOtomate.open();
                Cursor cOtomate = dbOtomate.getRow(sppaId);
                if (!cOtomate.moveToFirst()) {
                    return productName;
                }
                String kodeOtomate = cOtomate.getString(cOtomate.getColumnIndex(DBA_PRODUCT_OTOMATE.KODE_PRODUK));
                String tempProductName = getProdukName(activity, kodeOtomate);
                productName = tempProductName.equals("") ? productName : tempProductName;

                dbOtomate.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return productName;
        }
    }

    public static String getProdukKode(Activity activity, long sppaId) {
        String kodeOtomate = "03";
        try {
            DBA_PRODUCT_OTOMATE dbOtomate = new DBA_PRODUCT_OTOMATE(activity);
            dbOtomate.open();
            Cursor cOtomate = dbOtomate.getRow(sppaId);
            if (!cOtomate.moveToFirst()) {
                return kodeOtomate;
            }
            kodeOtomate = cOtomate.getString(cOtomate.getColumnIndex(DBA_PRODUCT_OTOMATE.KODE_PRODUK));
            kodeOtomate = isEmpty(kodeOtomate) ? Var.OTOMATE : kodeOtomate;


            dbOtomate.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return kodeOtomate;
        }
    }

    public static boolean isOtomateSppa(Activity activity, long sppaId) {
        DBA_PRODUCT_MAIN dbMain = null;
        try {
            dbMain = new DBA_PRODUCT_MAIN(activity);

            dbMain.open();

            Cursor cMain = dbMain.getRow(sppaId);

            if (!cMain.moveToFirst()) return false;

            String productName = cMain.getString(cMain.getColumnIndex(DBA_PRODUCT_MAIN.PRODUCT_NAME));
            if (productName.equalsIgnoreCase("OTOMATE") || productName.equalsIgnoreCase("OTOMATESYARIAH"))
                return true;
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbMain.close();
        }

        return false;
    }
    public static boolean isNewSppa(Activity activity, long sppaId) {
        DBA_PRODUCT_OTOMATE dbOtomate = null;

        try {
            dbOtomate = new DBA_PRODUCT_OTOMATE(activity);
            dbOtomate.open();
            Cursor cMain = dbOtomate.getRow(sppaId);
            if (!cMain.moveToFirst()) return false;


//            String entry = cMain.getString(cMain.getColumnIndex(DBA_PRODUCT_MAIN.ENTRY_DATE));
//            LocalDate entryDate = UtilDate.toDate(entry, UtilDate.STANDARD_DATE);

            VersionAndroid versionAndroid = new Select().from(VersionAndroid.class).querySingle();
            if (versionAndroid.Version >= 39) {
                String isNewSppa = cMain.getString(cMain.getColumnIndex(DBA_PRODUCT_OTOMATE.IS_NEW_SPPA));
                boolean isNew = TextUtils.isEmpty(isNewSppa) ? false : true;
                return isNew;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbOtomate.close();
        }
        return false;
    }

    public static boolean isCompleteSppa(Activity activity, long SppaId) {
        DBA_PRODUCT_MAIN dbMain = new DBA_PRODUCT_MAIN(activity);
        try {
            dbMain.open();
            Cursor cMain = dbMain.getRow(SppaId);
            if (!cMain.moveToFirst())
                return false;

            String isSppaNo = cMain.getString(cMain.getColumnIndex(DBA_PRODUCT_MAIN.I_SPPA_NO));
            if (TextUtils.isEmpty(isSppaNo))
                return false;

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            dbMain.close();
        }

        return false;
    }


    public static String getExpiredDate() {
        try {
            SppaMain sppaMain = new Select().from(SppaMain.class).querySingle();
            String expDate = sppaMain.ExpireDate;

            return UtilDate.format(expDate, UtilDate.STANDARD_DATE, UtilDate.BASIC_MON_DATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public static String getBeginDate() {
        try {
            SppaMain sppaMain = new Select().from(SppaMain.class).querySingle();
            String expDate = sppaMain.EffectiveDate;

            return UtilDate.format(expDate, UtilDate.ISO_DATE, UtilDate.BASIC_MON_DATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static int getAge(String expiredDate, String dateOfBirth) {
        try {
            LocalDate now = UtilDate.toDate(expiredDate);
            LocalDate dob = UtilDate.toDate(dateOfBirth);
            int age = UtilDate.yearDiffInPeriode(dob, now);
            return age;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int getAge(String expiredDate, String dateOfBirth, String pattern) {
        try {
            LocalDate now = UtilDate.toDate(expiredDate, pattern);
            LocalDate dob = UtilDate.toDate(dateOfBirth, pattern);
            int age = UtilDate.yearDiffInPeriode(dob, now);
            return age;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


    public static int getDaysPeriode() {
        try {
            SppaMain sppaMain = new Select().from(SppaMain.class).querySingle();
            if (sppaMain == null)
                return 0;

            if (TextUtils.isEmpty(sppaMain.EffectiveDate) || TextUtils.isEmpty(sppaMain.ExpireDate))
                return 0;

            LocalDate effDate = LocalDate.parse(sppaMain.EffectiveDate, DateTimeFormat.forPattern(UtilDate.STANDARD_DATE));
            LocalDate expDate = LocalDate.parse(sppaMain.ExpireDate, DateTimeFormat.forPattern(UtilDate.STANDARD_DATE));

            int dayDiff = UtilDate.dayDiff(effDate, expDate) + 1;

            return dayDiff;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static String getDurationCode(PremiHolder premiHolder) {
        SppaMain sppaMain;
        String durationCode = null;
        try {
            sppaMain = SppaMain.get();

            String subProductCode = sppaMain.SubProductCode;
            String planCode = sppaMain.PlanCode;
            String zoneId = sppaMain.ZoneId;

            if (premiHolder.premiAdd != 0.0) {
                SubProductPlanAdd subProductPlanAdd = SubProductPlanAdd.get(subProductCode, planCode, zoneId);
                durationCode = subProductPlanAdd.DurationCodeAs400;
            } else {
                SubProductPlanBasic subProductPlanBasic = SubProductPlanBasic.get(subProductCode, planCode, zoneId, Scalar.getDaysPeriode());
                durationCode = subProductPlanBasic.DurationCodeAs400;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return durationCode;
    }


    /***
     * @return true = maxday
     * false = -1
     **/
    public static int getMaxDayTravel(String subProductCode) {
        SubProduct subProduct = new Select().from(SubProduct.class)
                .where(SubProduct_Table.SubProductCode.eq(subProductCode))
                .querySingle();

        if (subProduct != null)
            return Integer.parseInt(subProduct.MaxDayTravel);

        return -1;
    }

    public static String getSubProductCode() {
        SppaMain sppaMain;
        try {
            sppaMain = new Select().from(SppaMain.class).querySingle();

            if (sppaMain == null) {
                return "";
            }

            return sppaMain.SubProductCode;

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
