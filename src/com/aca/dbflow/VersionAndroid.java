package com.aca.dbflow;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Date;


/**
 * Created by Marsel on 6/4/2016.
 */
@Table(database = DBMaster.class)
public class VersionAndroid extends BaseModel {

    @Column
    @PrimaryKey
    public int Version;

    @Column
    public Date DateTime;

    @Column
    public boolean Maintenance;

    public VersionAndroid() {
    }

    public static VersionAndroid get() {
        VersionAndroid versionAndroid = new Select().from(VersionAndroid.class).querySingle();
        return versionAndroid;
    }

}
