package com.aca.dbflow;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;


/**
 * Created by Marsel on 6/4/2016.
 */
@Table(database = DBMaster.class)
public class PaketOtomate extends BaseModel {

    @Column
    @PrimaryKey (autoincrement = true)
    public int ID;

    @Column
    public String
            KodeProduct,
            Flood,
            Eq;

    public PaketOtomate() {
    }
}