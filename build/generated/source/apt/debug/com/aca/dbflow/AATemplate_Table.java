package com.aca.dbflow;

import com.raizlabs.android.dbflow.runtime.BaseContentProvider.PropertyConverter;
import com.raizlabs.android.dbflow.sql.QueryBuilder;
import com.raizlabs.android.dbflow.sql.language.property.BaseProperty;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.sql.language.property.Property;
import java.lang.IllegalArgumentException;
import java.lang.String;

/**
 * This is generated code. Please do not modify */
public final class AATemplate_Table {
  public static final PropertyConverter PROPERTY_CONVERTER = new PropertyConverter(){ 
  public IProperty fromName(String columnName) {
  return com.aca.dbflow.AATemplate_Table.getProperty(columnName); 
  }
  };

  public static final Property<String> CountryId = new Property<String>(AATemplate.class, "CountryId");

  public static final Property<String> CountryName = new Property<String>(AATemplate.class, "CountryName");

  public static final IProperty[] getAllColumnProperties() {
    return new IProperty[]{CountryId,CountryName};
  }

  public static BaseProperty getProperty(String columnName) {
    columnName = QueryBuilder.quoteIfNeeded(columnName);
    switch (columnName)  {
      case "`CountryId`":  {
        return CountryId;
      }
      case "`CountryName`":  {
        return CountryName;
      }
      default:  {
        throw new IllegalArgumentException("Invalid column name passed. Ensure you are calling the correct table's column");
      }
    }
  }
}
