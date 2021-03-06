package com.aca.holder;

/**
 * Created by Marsel on 29/4/2016.
 */
public class PremiHolder {
    public double
            premiBasic,
            premiAdd,
            premiBDA,
            premiBDADefault,
            premiAllocation,
            premiLoading,
            loadingPercentage,
            charge,
            stamp,
            potongan,
            diskon,
            exchangeRate,
            maxBenefit;

    public boolean loadingUmur;
    public String currency;
    public int
            dayDiff,
            daysPeriode,
            weeksPeriode;

    public String
            ccod,
            dcod,
            acod;

    public PremiHolder() {
        premiBasic = 0.0;
        premiAdd = 0.0;
        premiBDA = 0.0;
        premiBDADefault = 0.0;
        premiAllocation = 0.0;
        premiLoading = 0.0;
        loadingPercentage = 0.0;
        charge = 0.0;
        stamp = 0.0;
        potongan = 1;
        diskon = 0;
        exchangeRate = 1;
        maxBenefit = 0.0;

        loadingUmur = false;
        currency = "";

        dayDiff = 0;
        daysPeriode = 0;
        weeksPeriode = 0;

        ccod = "";
        dcod = "";
        acod = "";

    }

    public void setPotongan(double persenPotong) {
        potongan = (100 - persenPotong) / 100;
        diskon = persenPotong;
    }


    public void resetPotongan() {
        potongan = 1;
        diskon = 0;
    }
    public double getTotalPremi() {
        return premiBasic + getAddPremi() + premiLoading;
    }

    public double getTotal() {
        return getTotalPremi() * potongan +  + getTotalCharge();
    }

    public double getTotalInIdr() {
        return Math.round(getTotal() * exchangeRate);
    }

    public double getPremiAlokasiUmroh() {
        return getTotal() - premiBDA - premiBDADefault;
    }

    public double getDiskonAmount() {
        return  getTotalPremi() * (1-potongan);
    }

    public double getDiskon(){
        return diskon;
    }

    public double getAddPremi() {
        return premiAdd + premiBDA;
    }

    public double getTotalCharge() {
        return stamp + charge;
    }
}
