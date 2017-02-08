package com.aca.dbflow;

import com.raizlabs.android.dbflow.runtime.BaseContentProvider.PropertyConverter;
import com.raizlabs.android.dbflow.sql.QueryBuilder;
import com.raizlabs.android.dbflow.sql.language.property.BaseProperty;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.sql.language.property.Property;
import java.lang.Boolean;
import java.lang.IllegalArgumentException;
import java.lang.String;

/**
 * This is generated code. Please do not modify */
public final class MedisafeKuestioner_Table {
  public static final PropertyConverter PROPERTY_CONVERTER = new PropertyConverter(){ 
  public IProperty fromName(String columnName) {
  return com.aca.dbflow.MedisafeKuestioner_Table.getProperty(columnName); 
  }
  };

  public static final Property<String> SppaNo = new Property<String>(MedisafeKuestioner.class, "SppaNo");

  public static final Property<Boolean> IsYesA1 = new Property<Boolean>(MedisafeKuestioner.class, "IsYesA1");

  public static final Property<Boolean> IsYesA2 = new Property<Boolean>(MedisafeKuestioner.class, "IsYesA2");

  public static final Property<Boolean> IsYesA3 = new Property<Boolean>(MedisafeKuestioner.class, "IsYesA3");

  public static final Property<Boolean> IsYesA4 = new Property<Boolean>(MedisafeKuestioner.class, "IsYesA4");

  public static final Property<Boolean> IsYesB1 = new Property<Boolean>(MedisafeKuestioner.class, "IsYesB1");

  public static final Property<Boolean> IsYesB2i = new Property<Boolean>(MedisafeKuestioner.class, "IsYesB2i");

  public static final Property<Boolean> IsYesB2ii = new Property<Boolean>(MedisafeKuestioner.class, "IsYesB2ii");

  public static final Property<Boolean> IsYesB2iii = new Property<Boolean>(MedisafeKuestioner.class, "IsYesB2iii");

  public static final Property<Boolean> IsYesB2iv = new Property<Boolean>(MedisafeKuestioner.class, "IsYesB2iv");

  public static final Property<Boolean> IsYesB2v = new Property<Boolean>(MedisafeKuestioner.class, "IsYesB2v");

  public static final Property<Boolean> IsYesB2vi = new Property<Boolean>(MedisafeKuestioner.class, "IsYesB2vi");

  public static final Property<Boolean> IsYesB3 = new Property<Boolean>(MedisafeKuestioner.class, "IsYesB3");

  public static final Property<Boolean> IsYesB4 = new Property<Boolean>(MedisafeKuestioner.class, "IsYesB4");

  public static final Property<Boolean> IsYesB5i = new Property<Boolean>(MedisafeKuestioner.class, "IsYesB5i");

  public static final Property<Boolean> IsYesB5ii = new Property<Boolean>(MedisafeKuestioner.class, "IsYesB5ii");

  public static final Property<Boolean> IsAgreed = new Property<Boolean>(MedisafeKuestioner.class, "IsAgreed");

  public static final Property<String> NamaPerusahaan1 = new Property<String>(MedisafeKuestioner.class, "NamaPerusahaan1");

  public static final Property<String> NamaPerusahaan2 = new Property<String>(MedisafeKuestioner.class, "NamaPerusahaan2");

  public static final Property<String> NamaPerusahaan3 = new Property<String>(MedisafeKuestioner.class, "NamaPerusahaan3");

  public static final Property<String> NamaPerusahaan4 = new Property<String>(MedisafeKuestioner.class, "NamaPerusahaan4");

  public static final Property<String> NoPolis1 = new Property<String>(MedisafeKuestioner.class, "NoPolis1");

  public static final Property<String> NoPolis2 = new Property<String>(MedisafeKuestioner.class, "NoPolis2");

  public static final Property<String> NoPolis3 = new Property<String>(MedisafeKuestioner.class, "NoPolis3");

  public static final Property<String> NoPolis4 = new Property<String>(MedisafeKuestioner.class, "NoPolis4");

  public static final Property<String> KeteranganA2 = new Property<String>(MedisafeKuestioner.class, "KeteranganA2");

  public static final Property<String> KeteranganA3 = new Property<String>(MedisafeKuestioner.class, "KeteranganA3");

  public static final Property<String> KeteranganA4 = new Property<String>(MedisafeKuestioner.class, "KeteranganA4");

  public static final Property<String> KeteranganB1 = new Property<String>(MedisafeKuestioner.class, "KeteranganB1");

  public static final Property<String> KeteranganB2i = new Property<String>(MedisafeKuestioner.class, "KeteranganB2i");

  public static final Property<String> KeteranganB2ii = new Property<String>(MedisafeKuestioner.class, "KeteranganB2ii");

  public static final Property<String> KeteranganB2iii = new Property<String>(MedisafeKuestioner.class, "KeteranganB2iii");

  public static final Property<String> KeteranganB2iv = new Property<String>(MedisafeKuestioner.class, "KeteranganB2iv");

  public static final Property<String> KeteranganB2v = new Property<String>(MedisafeKuestioner.class, "KeteranganB2v");

  public static final Property<String> KeteranganB2vi = new Property<String>(MedisafeKuestioner.class, "KeteranganB2vi");

  public static final Property<String> KeteranganB31 = new Property<String>(MedisafeKuestioner.class, "KeteranganB31");

  public static final Property<String> KeteranganB32 = new Property<String>(MedisafeKuestioner.class, "KeteranganB32");

  public static final Property<String> KeteranganB33 = new Property<String>(MedisafeKuestioner.class, "KeteranganB33");

  public static final Property<String> KeteranganB34 = new Property<String>(MedisafeKuestioner.class, "KeteranganB34");

  public static final Property<String> KeteranganB4 = new Property<String>(MedisafeKuestioner.class, "KeteranganB4");

  public static final Property<String> KeteranganB5i = new Property<String>(MedisafeKuestioner.class, "KeteranganB5i");

  public static final Property<String> KeteranganB5ii = new Property<String>(MedisafeKuestioner.class, "KeteranganB5ii");

  public static final IProperty[] getAllColumnProperties() {
    return new IProperty[]{SppaNo,IsYesA1,IsYesA2,IsYesA3,IsYesA4,IsYesB1,IsYesB2i,IsYesB2ii,IsYesB2iii,IsYesB2iv,IsYesB2v,IsYesB2vi,IsYesB3,IsYesB4,IsYesB5i,IsYesB5ii,IsAgreed,NamaPerusahaan1,NamaPerusahaan2,NamaPerusahaan3,NamaPerusahaan4,NoPolis1,NoPolis2,NoPolis3,NoPolis4,KeteranganA2,KeteranganA3,KeteranganA4,KeteranganB1,KeteranganB2i,KeteranganB2ii,KeteranganB2iii,KeteranganB2iv,KeteranganB2v,KeteranganB2vi,KeteranganB31,KeteranganB32,KeteranganB33,KeteranganB34,KeteranganB4,KeteranganB5i,KeteranganB5ii};
  }

  public static BaseProperty getProperty(String columnName) {
    columnName = QueryBuilder.quoteIfNeeded(columnName);
    switch (columnName)  {
      case "`SppaNo`":  {
        return SppaNo;
      }
      case "`IsYesA1`":  {
        return IsYesA1;
      }
      case "`IsYesA2`":  {
        return IsYesA2;
      }
      case "`IsYesA3`":  {
        return IsYesA3;
      }
      case "`IsYesA4`":  {
        return IsYesA4;
      }
      case "`IsYesB1`":  {
        return IsYesB1;
      }
      case "`IsYesB2i`":  {
        return IsYesB2i;
      }
      case "`IsYesB2ii`":  {
        return IsYesB2ii;
      }
      case "`IsYesB2iii`":  {
        return IsYesB2iii;
      }
      case "`IsYesB2iv`":  {
        return IsYesB2iv;
      }
      case "`IsYesB2v`":  {
        return IsYesB2v;
      }
      case "`IsYesB2vi`":  {
        return IsYesB2vi;
      }
      case "`IsYesB3`":  {
        return IsYesB3;
      }
      case "`IsYesB4`":  {
        return IsYesB4;
      }
      case "`IsYesB5i`":  {
        return IsYesB5i;
      }
      case "`IsYesB5ii`":  {
        return IsYesB5ii;
      }
      case "`IsAgreed`":  {
        return IsAgreed;
      }
      case "`NamaPerusahaan1`":  {
        return NamaPerusahaan1;
      }
      case "`NamaPerusahaan2`":  {
        return NamaPerusahaan2;
      }
      case "`NamaPerusahaan3`":  {
        return NamaPerusahaan3;
      }
      case "`NamaPerusahaan4`":  {
        return NamaPerusahaan4;
      }
      case "`NoPolis1`":  {
        return NoPolis1;
      }
      case "`NoPolis2`":  {
        return NoPolis2;
      }
      case "`NoPolis3`":  {
        return NoPolis3;
      }
      case "`NoPolis4`":  {
        return NoPolis4;
      }
      case "`KeteranganA2`":  {
        return KeteranganA2;
      }
      case "`KeteranganA3`":  {
        return KeteranganA3;
      }
      case "`KeteranganA4`":  {
        return KeteranganA4;
      }
      case "`KeteranganB1`":  {
        return KeteranganB1;
      }
      case "`KeteranganB2i`":  {
        return KeteranganB2i;
      }
      case "`KeteranganB2ii`":  {
        return KeteranganB2ii;
      }
      case "`KeteranganB2iii`":  {
        return KeteranganB2iii;
      }
      case "`KeteranganB2iv`":  {
        return KeteranganB2iv;
      }
      case "`KeteranganB2v`":  {
        return KeteranganB2v;
      }
      case "`KeteranganB2vi`":  {
        return KeteranganB2vi;
      }
      case "`KeteranganB31`":  {
        return KeteranganB31;
      }
      case "`KeteranganB32`":  {
        return KeteranganB32;
      }
      case "`KeteranganB33`":  {
        return KeteranganB33;
      }
      case "`KeteranganB34`":  {
        return KeteranganB34;
      }
      case "`KeteranganB4`":  {
        return KeteranganB4;
      }
      case "`KeteranganB5i`":  {
        return KeteranganB5i;
      }
      case "`KeteranganB5ii`":  {
        return KeteranganB5ii;
      }
      default:  {
        throw new IllegalArgumentException("Invalid column name passed. Ensure you are calling the correct table's column");
      }
    }
  }
}
