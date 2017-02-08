package com.aca.dbflow;

import com.raizlabs.android.dbflow.runtime.BaseContentProvider.PropertyConverter;
import com.raizlabs.android.dbflow.sql.QueryBuilder;
import com.raizlabs.android.dbflow.sql.language.property.BaseProperty;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.sql.language.property.IntProperty;
import com.raizlabs.android.dbflow.sql.language.property.Property;
import java.lang.Boolean;
import java.lang.IllegalArgumentException;
import java.lang.String;
import java.util.Date;

/**
 * This is generated code. Please do not modify */
public final class VersionAndroid_Table {
  public static final PropertyConverter PROPERTY_CONVERTER = new PropertyConverter(){ 
  public IProperty fromName(String columnName) {
  return com.aca.dbflow.VersionAndroid_Table.getProperty(columnName); 
  }
  };

  public static final IntProperty Version = new IntProperty(VersionAndroid.class, "Version");

  public static final Property<Date> DateTime = new Property<Date>(VersionAndroid.class, "DateTime");

  public static final Property<Boolean> Maintenance = new Property<Boolean>(VersionAndroid.class, "Maintenance");

  public static final IProperty[] getAllColumnProperties() {
    return new IProperty[]{Version,DateTime,Maintenance};
  }

  public static BaseProperty getProperty(String columnName) {
    columnName = QueryBuilder.quoteIfNeeded(columnName);
    switch (columnName)  {
      case "`Version`":  {
        return Version;
      }
      case "`DateTime`":  {
        return DateTime;
      }
      case "`Maintenance`":  {
        return Maintenance;
      }
      default:  {
        throw new IllegalArgumentException("Invalid column name passed. Ensure you are calling the correct table's column");
      }
    }
  }
}
