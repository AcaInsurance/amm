package com.raizlabs.android.dbflow.config;

import com.aca.dbflow.AATemplate;
import com.aca.dbflow.AATemplate_Adapter;
import com.aca.dbflow.GeneralSetting;
import com.aca.dbflow.GeneralSetting_Adapter;
import com.aca.dbflow.MedisafeKuestioner;
import com.aca.dbflow.MedisafeKuestioner_Adapter;
import com.aca.dbflow.PaketOtomate;
import com.aca.dbflow.PaketOtomate_Adapter;
import com.aca.dbflow.PerluasanPremi;
import com.aca.dbflow.PerluasanPremi_Adapter;
import com.aca.dbflow.SettingOtomate;
import com.aca.dbflow.SettingOtomate_Adapter;
import com.aca.dbflow.SppaMain;
import com.aca.dbflow.SppaMain_Adapter;
import com.aca.dbflow.StandardField;
import com.aca.dbflow.StandardField_Adapter;
import com.aca.dbflow.SubProduct;
import com.aca.dbflow.SubProductPlan;
import com.aca.dbflow.SubProductPlanAdd;
import com.aca.dbflow.SubProductPlanAdd_Adapter;
import com.aca.dbflow.SubProductPlanBDA;
import com.aca.dbflow.SubProductPlanBDA_Adapter;
import com.aca.dbflow.SubProductPlanBasic;
import com.aca.dbflow.SubProductPlanBasic_Adapter;
import com.aca.dbflow.SubProductPlan_Adapter;
import com.aca.dbflow.SubProduct_Adapter;
import com.aca.dbflow.VersionAndroid;
import com.aca.dbflow.VersionAndroid_Adapter;
import com.raizlabs.android.dbflow.structure.database.FlowSQLiteOpenHelper;
import com.raizlabs.android.dbflow.structure.database.OpenHelper;
import java.lang.Override;
import java.lang.String;

/**
 * This is generated code. Please do not modify */
public final class DBMasterMM_Database extends BaseDatabaseDefinition {
  public DBMasterMM_Database(DatabaseHolder holder) {
    holder.putDatabaseForTable(SubProduct.class, this);
    holder.putDatabaseForTable(SppaMain.class, this);
    holder.putDatabaseForTable(VersionAndroid.class, this);
    holder.putDatabaseForTable(MedisafeKuestioner.class, this);
    holder.putDatabaseForTable(SubProductPlanBasic.class, this);
    holder.putDatabaseForTable(GeneralSetting.class, this);
    holder.putDatabaseForTable(SettingOtomate.class, this);
    holder.putDatabaseForTable(SubProductPlanAdd.class, this);
    holder.putDatabaseForTable(AATemplate.class, this);
    holder.putDatabaseForTable(SubProductPlanBDA.class, this);
    holder.putDatabaseForTable(StandardField.class, this);
    holder.putDatabaseForTable(PerluasanPremi.class, this);
    holder.putDatabaseForTable(SubProductPlan.class, this);
    holder.putDatabaseForTable(PaketOtomate.class, this);
    models.add(SubProduct.class);
    modelTableNames.put("SubProduct", SubProduct.class);
    modelAdapters.put(SubProduct.class, new SubProduct_Adapter(holder));
    models.add(SppaMain.class);
    modelTableNames.put("SppaMain", SppaMain.class);
    modelAdapters.put(SppaMain.class, new SppaMain_Adapter(holder));
    models.add(VersionAndroid.class);
    modelTableNames.put("VersionAndroid", VersionAndroid.class);
    modelAdapters.put(VersionAndroid.class, new VersionAndroid_Adapter(holder));
    models.add(MedisafeKuestioner.class);
    modelTableNames.put("MedisafeKuestioner", MedisafeKuestioner.class);
    modelAdapters.put(MedisafeKuestioner.class, new MedisafeKuestioner_Adapter(holder));
    models.add(SubProductPlanBasic.class);
    modelTableNames.put("SubProductPlanBasic", SubProductPlanBasic.class);
    modelAdapters.put(SubProductPlanBasic.class, new SubProductPlanBasic_Adapter(holder));
    models.add(GeneralSetting.class);
    modelTableNames.put("GeneralSetting", GeneralSetting.class);
    modelAdapters.put(GeneralSetting.class, new GeneralSetting_Adapter(holder));
    models.add(SettingOtomate.class);
    modelTableNames.put("SettingOtomate", SettingOtomate.class);
    modelAdapters.put(SettingOtomate.class, new SettingOtomate_Adapter(holder));
    models.add(SubProductPlanAdd.class);
    modelTableNames.put("SubProductPlanAdd", SubProductPlanAdd.class);
    modelAdapters.put(SubProductPlanAdd.class, new SubProductPlanAdd_Adapter(holder));
    models.add(AATemplate.class);
    modelTableNames.put("AATemplate", AATemplate.class);
    modelAdapters.put(AATemplate.class, new AATemplate_Adapter(holder));
    models.add(SubProductPlanBDA.class);
    modelTableNames.put("SubProductPlanBDA", SubProductPlanBDA.class);
    modelAdapters.put(SubProductPlanBDA.class, new SubProductPlanBDA_Adapter(holder));
    models.add(StandardField.class);
    modelTableNames.put("StandardField", StandardField.class);
    modelAdapters.put(StandardField.class, new StandardField_Adapter(holder));
    models.add(PerluasanPremi.class);
    modelTableNames.put("PerluasanPremi", PerluasanPremi.class);
    modelAdapters.put(PerluasanPremi.class, new PerluasanPremi_Adapter(holder));
    models.add(SubProductPlan.class);
    modelTableNames.put("SubProductPlan", SubProductPlan.class);
    modelAdapters.put(SubProductPlan.class, new SubProductPlan_Adapter(holder));
    models.add(PaketOtomate.class);
    modelTableNames.put("PaketOtomate", PaketOtomate.class);
    modelAdapters.put(PaketOtomate.class, new PaketOtomate_Adapter(holder));
  }

  @Override
  public final OpenHelper createHelper() {
    return new FlowSQLiteOpenHelper(this, internalHelperListener);
  }

  @Override
  public final boolean isForeignKeysSupported() {
    return false;
  }

  @Override
  public final boolean isInMemory() {
    return false;
  }

  @Override
  public final boolean backupEnabled() {
    return false;
  }

  @Override
  public final boolean areConsistencyChecksEnabled() {
    return false;
  }

  @Override
  public final int getDatabaseVersion() {
    return 3;
  }

  @Override
  public final String getDatabaseName() {
    return "MM";
  }
}
