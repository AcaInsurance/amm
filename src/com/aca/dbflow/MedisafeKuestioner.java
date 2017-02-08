package com.aca.dbflow;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Date;

import static com.aca.dbflow.StandardField_Table.IsActive;


/**
 * Created by Marsel on 6/4/2016.
 */
@Table(database = DBMaster.class)
public class MedisafeKuestioner extends BaseModel {

    @Column
    @PrimaryKey
    public String SppaNo;

    @Column
    public Boolean IsYesA1
                ,IsYesA2
                ,IsYesA3
                ,IsYesA4
                ,IsYesB1
                ,IsYesB2i
                ,IsYesB2ii
                ,IsYesB2iii
                ,IsYesB2iv
                ,IsYesB2v
                ,IsYesB2vi
                ,IsYesB3
                ,IsYesB4
                ,IsYesB5i
                ,IsYesB5ii,
                 IsAgreed;
    @Column
    public String NamaPerusahaan1
                ,NamaPerusahaan2
                ,NamaPerusahaan3
                ,NamaPerusahaan4
                ,NoPolis1
                ,NoPolis2
                ,NoPolis3
                ,NoPolis4
                ,KeteranganA2
                ,KeteranganA3
                ,KeteranganA4
                ,KeteranganB1
                ,KeteranganB2i
                ,KeteranganB2ii
                ,KeteranganB2iii
                ,KeteranganB2iv
                ,KeteranganB2v
                ,KeteranganB2vi
                ,KeteranganB31
                ,KeteranganB32
                ,KeteranganB33
                ,KeteranganB34
                ,KeteranganB4
                ,KeteranganB5i
                ,KeteranganB5ii;

    public MedisafeKuestioner() {
        IsYesA1 = false;
        IsYesA2 = false;
        IsYesA3 = false;
        IsYesA4 = false;
        IsYesB1 = false;
        IsYesB2i = false;
        IsYesB2ii = false;
        IsYesB2iii = false;
        IsYesB2iv = false;
        IsYesB2v = false;
        IsYesB2vi = false;
        IsYesB3 = false;
        IsYesB4 = false;
        IsYesB5i = false;
        IsYesB5ii = false;
        IsAgreed = false;
    }

    @Override
    public void save() {
        super.save();
    }



}
