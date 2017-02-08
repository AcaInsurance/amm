package com.aca.dbflow;

import com.raizlabs.android.dbflow.runtime.BaseContentProvider.PropertyConverter;
import com.raizlabs.android.dbflow.sql.QueryBuilder;
import com.raizlabs.android.dbflow.sql.language.property.BaseProperty;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.sql.language.property.IntProperty;
import com.raizlabs.android.dbflow.sql.language.property.Property;
import java.lang.IllegalArgumentException;
import java.lang.String;

/**
 * This is generated code. Please do not modify */
public final class GeneralSetting_Table {
  public static final PropertyConverter PROPERTY_CONVERTER = new PropertyConverter(){ 
  public IProperty fromName(String columnName) {
  return com.aca.dbflow.GeneralSetting_Table.getProperty(columnName); 
  }
  };

  public static final IntProperty id = new IntProperty(GeneralSetting.class, "id");

  public static final Property<String> ParameterCode = new Property<String>(GeneralSetting.class, "ParameterCode");

  public static final Property<String> ParameterValue = new Property<String>(GeneralSetting.class, "ParameterValue");

  public static final IProperty[] getAllColumnProperties() {
    return new IProperty[]{id,ParameterCode,ParameterValue};
  }

  public static BaseProperty getProperty(String columnName) {
    columnName = QueryBuilder.quoteIfNeeded(columnName);
    switch (columnName)  {
      case "`id`":  {
        return id;
      }
      case "`ParameterCode`":  {
        return ParameterCode;
      }
      case "`ParameterValue`":  {
        return ParameterValue;
      }
      default:  {
        throw new IllegalArgumentException("Invalid column name passed. Ensure you are calling the correct table's column");
      }
    }
  }
}
