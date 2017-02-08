package com.aca.dbflow;

import com.raizlabs.android.dbflow.runtime.BaseContentProvider.PropertyConverter;
import com.raizlabs.android.dbflow.sql.QueryBuilder;
import com.raizlabs.android.dbflow.sql.language.property.BaseProperty;
import com.raizlabs.android.dbflow.sql.language.property.DoubleProperty;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.sql.language.property.IntProperty;
import com.raizlabs.android.dbflow.sql.language.property.Property;
import java.lang.IllegalArgumentException;
import java.lang.String;

/**
 * This is generated code. Please do not modify */
public final class SettingOtomate_Table {
  public static final PropertyConverter PROPERTY_CONVERTER = new PropertyConverter(){ 
  public IProperty fromName(String columnName) {
  return com.aca.dbflow.SettingOtomate_Table.getProperty(columnName); 
  }
  };

  public static final IntProperty id = new IntProperty(SettingOtomate.class, "id");

  public static final Property<String> KodeProduct = new Property<String>(SettingOtomate.class, "KodeProduct");

  public static final Property<String> FloodDefault = new Property<String>(SettingOtomate.class, "FloodDefault");

  public static final Property<String> EqDefault = new Property<String>(SettingOtomate.class, "EqDefault");

  public static final Property<String> IsPaket = new Property<String>(SettingOtomate.class, "IsPaket");

  public static final Property<String> SRCCDefault = new Property<String>(SettingOtomate.class, "SRCCDefault");

  public static final Property<String> TSDefault = new Property<String>(SettingOtomate.class, "TSDefault");

  public static final Property<String> IsChangeable = new Property<String>(SettingOtomate.class, "IsChangeable");

  public static final Property<String> BengkelDefault = new Property<String>(SettingOtomate.class, "BengkelDefault");

  public static final Property<String> IsChangeableBengkel = new Property<String>(SettingOtomate.class, "IsChangeableBengkel");

  public static final DoubleProperty LimitTPL = new DoubleProperty(SettingOtomate.class, "LimitTPL");

  public static final DoubleProperty LimitPA = new DoubleProperty(SettingOtomate.class, "LimitPA");

  public static final IProperty[] getAllColumnProperties() {
    return new IProperty[]{id,KodeProduct,FloodDefault,EqDefault,IsPaket,SRCCDefault,TSDefault,IsChangeable,BengkelDefault,IsChangeableBengkel,LimitTPL,LimitPA};
  }

  public static BaseProperty getProperty(String columnName) {
    columnName = QueryBuilder.quoteIfNeeded(columnName);
    switch (columnName)  {
      case "`id`":  {
        return id;
      }
      case "`KodeProduct`":  {
        return KodeProduct;
      }
      case "`FloodDefault`":  {
        return FloodDefault;
      }
      case "`EqDefault`":  {
        return EqDefault;
      }
      case "`IsPaket`":  {
        return IsPaket;
      }
      case "`SRCCDefault`":  {
        return SRCCDefault;
      }
      case "`TSDefault`":  {
        return TSDefault;
      }
      case "`IsChangeable`":  {
        return IsChangeable;
      }
      case "`BengkelDefault`":  {
        return BengkelDefault;
      }
      case "`IsChangeableBengkel`":  {
        return IsChangeableBengkel;
      }
      case "`LimitTPL`":  {
        return LimitTPL;
      }
      case "`LimitPA`":  {
        return LimitPA;
      }
      default:  {
        throw new IllegalArgumentException("Invalid column name passed. Ensure you are calling the correct table's column");
      }
    }
  }
}
