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
public final class PerluasanPremi_Table {
  public static final PropertyConverter PROPERTY_CONVERTER = new PropertyConverter(){ 
  public IProperty fromName(String columnName) {
  return com.aca.dbflow.PerluasanPremi_Table.getProperty(columnName); 
  }
  };

  public static final IntProperty id = new IntProperty(PerluasanPremi.class, "id");

  public static final Property<String> Tipe = new Property<String>(PerluasanPremi.class, "Tipe");

  public static final Property<String> Amount_Text = new Property<String>(PerluasanPremi.class, "Amount_Text");

  public static final Property<String> Kode_Produk = new Property<String>(PerluasanPremi.class, "Kode_Produk");

  public static final DoubleProperty Amount = new DoubleProperty(PerluasanPremi.class, "Amount");

  public static final DoubleProperty Premi = new DoubleProperty(PerluasanPremi.class, "Premi");

  public static final IProperty[] getAllColumnProperties() {
    return new IProperty[]{id,Tipe,Amount_Text,Kode_Produk,Amount,Premi};
  }

  public static BaseProperty getProperty(String columnName) {
    columnName = QueryBuilder.quoteIfNeeded(columnName);
    switch (columnName)  {
      case "`id`":  {
        return id;
      }
      case "`Tipe`":  {
        return Tipe;
      }
      case "`Amount_Text`":  {
        return Amount_Text;
      }
      case "`Kode_Produk`":  {
        return Kode_Produk;
      }
      case "`Amount`":  {
        return Amount;
      }
      case "`Premi`":  {
        return Premi;
      }
      default:  {
        throw new IllegalArgumentException("Invalid column name passed. Ensure you are calling the correct table's column");
      }
    }
  }
}
