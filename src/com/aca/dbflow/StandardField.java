package com.aca.dbflow;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;


/**
 * Created by Marsel on 6/4/2016.
 */
@Table(database = DBMaster.class)
public class StandardField extends BaseModel {

    @Column
    @PrimaryKey (autoincrement = true)
    public int id;

    @Column
    public String
                FieldCode
                ,FieldCodeDt
                ,FieldNameDt
                ,Value
                ,Description
                ,CreateBy
                ,CreateDate
                ,ModifyBy
                ,ModifyDate
                ,IsActive;

    public StandardField() {
    }

    public static StandardField getStandardField (String fieldNameDt) {
        StandardField standardField = new Select()
                .from(StandardField.class)
                .where(StandardField_Table.FieldNameDt.eq(fieldNameDt))
                    .and(StandardField_Table.IsActive.eq(String.valueOf(true)))
                .querySingle();
        return standardField;
    }

    public static StandardField getStandardFieldByDescription (String description) {
        StandardField standardField = new Select()
                .from(StandardField.class)
                .where(StandardField_Table.Description.eq(description))
                .and(StandardField_Table.IsActive.eq(String.valueOf(true)))
                .querySingle();
        return standardField;
    }

}
