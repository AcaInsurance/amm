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
public final class PaketOtomate_Table {
  public static final PropertyConverter PROPERTY_CONVERTER = new PropertyConverter(){ 
  public IProperty fromName(String columnName) {
  return com.aca.dbflow.PaketOtomate_Table.getProperty(columnName); 
  }
  };

  public static final IntProperty ID = new IntProperty(PaketOtomate.class, "ID");

  public static final Property<String> KodeProduct = new Property<String>(PaketOtomate.class, "KodeProduct");

  public static final Property<String> Flood = new Property<String>(PaketOtomate.class, "Flood");

  public static final Property<String> Eq = new Property<String>(PaketOtomate.class, "Eq");

  public static final IProperty[] getAllColumnProperties() {
    return new IProperty[]{ID,KodeProduct,Flood,Eq};
  }

  public static BaseProperty getProperty(String columnName) {
    columnName = QueryBuilder.quoteIfNeeded(columnName);
    switch (columnName)  {
      case "`ID`":  {
        return ID;
      }
      case "`KodeProduct`":  {
        return KodeProduct;
      }
      case "`Flood`":  {
        return Flood;
      }
      case "`Eq`":  {
        return Eq;
      }
      default:  {
        throw new IllegalArgumentException("Invalid column name passed. Ensure you are calling the correct table's column");
      }
    }
  }
}
