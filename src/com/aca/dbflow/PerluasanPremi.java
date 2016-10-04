package com.aca.dbflow;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;


/**
 * Created by Marsel on 6/4/2016.
 */
@Table(database = DBMaster.class)
public class PerluasanPremi extends BaseModel {

    @Column
    @PrimaryKey (autoincrement = true)
    public int id;

    @Column
    public String
                Tipe
                ,Amount_Text
                ,Kode_Produk;

    @Column
    public double
                Amount,
                Premi;

    public PerluasanPremi() {
    }
}
