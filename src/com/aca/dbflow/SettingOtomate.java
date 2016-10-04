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
public class SettingOtomate extends BaseModel {

    @Column
    @PrimaryKey (autoincrement = true)
    public int id;

    @Column
    public String
            KodeProduct
            ,FloodDefault
            ,EqDefault
            ,IsPaket
            ,SRCCDefault
            ,TSDefault
            ,IsChangeable
            ,BengkelDefault
            ,IsChangeableBengkel;

    @Column
    public double
             LimitTPL
            ,LimitPA;

    public SettingOtomate() {
    }

    public static SettingOtomate get(String kodeProduct) {
        try {
            SettingOtomate settingOtomate = new Select()
                    .from(SettingOtomate.class)
                    .where(SettingOtomate_Table.KodeProduct.eq(kodeProduct))
                    .querySingle();

            if (settingOtomate == null)
                return null;

            return settingOtomate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
