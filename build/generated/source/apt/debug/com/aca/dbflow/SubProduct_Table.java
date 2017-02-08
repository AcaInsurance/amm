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
public final class SubProduct_Table {
  public static final PropertyConverter PROPERTY_CONVERTER = new PropertyConverter(){ 
  public IProperty fromName(String columnName) {
  return com.aca.dbflow.SubProduct_Table.getProperty(columnName); 
  }
  };

  public static final Property<String> SubProductCode = new Property<String>(SubProduct.class, "SubProductCode");

  public static final Property<String> Description = new Property<String>(SubProduct.class, "Description");

  public static final Property<String> ProductCode = new Property<String>(SubProduct.class, "ProductCode");

  public static final Property<String> OrderNo = new Property<String>(SubProduct.class, "OrderNo");

  public static final Property<String> IsConventional = new Property<String>(SubProduct.class, "IsConventional");

  public static final Property<String> Url = new Property<String>(SubProduct.class, "Url");

  public static final Property<String> ScheduleName = new Property<String>(SubProduct.class, "ScheduleName");

  public static final Property<String> AttachBenefit = new Property<String>(SubProduct.class, "AttachBenefit");

  public static final Property<String> AttachClaim = new Property<String>(SubProduct.class, "AttachClaim");

  public static final Property<String> AttachOthers = new Property<String>(SubProduct.class, "AttachOthers");

  public static final Property<String> WsUrl = new Property<String>(SubProduct.class, "WsUrl");

  public static final Property<String> IdOrFa = new Property<String>(SubProduct.class, "IdOrFa");

  public static final Property<String> AnnOrReg = new Property<String>(SubProduct.class, "AnnOrReg");

  public static final Property<String> IsNeedFlight = new Property<String>(SubProduct.class, "IsNeedFlight");

  public static final Property<String> MaxAgeFrom = new Property<String>(SubProduct.class, "MaxAgeFrom");

  public static final Property<String> MaxAgeTo = new Property<String>(SubProduct.class, "MaxAgeTo");

  public static final Property<String> MinAge = new Property<String>(SubProduct.class, "MinAge");

  public static final Property<String> MaxDay = new Property<String>(SubProduct.class, "MaxDay");

  public static final Property<String> MaxDayBda = new Property<String>(SubProduct.class, "MaxDayBda");

  public static final Property<String> LoadingPct = new Property<String>(SubProduct.class, "LoadingPct");

  public static final Property<String> Charges = new Property<String>(SubProduct.class, "Charges");

  public static final Property<String> BdaAmount = new Property<String>(SubProduct.class, "BdaAmount");

  public static final Property<String> StampAmount = new Property<String>(SubProduct.class, "StampAmount");

  public static final Property<String> MaxDayTravel = new Property<String>(SubProduct.class, "MaxDayTravel");

  public static final Property<String> IsConvertToIdr = new Property<String>(SubProduct.class, "IsConvertToIdr");

  public static final Property<String> MinMember = new Property<String>(SubProduct.class, "MinMember");

  public static final Property<String> IsMobile = new Property<String>(SubProduct.class, "IsMobile");

  public static final Property<String> IsActive = new Property<String>(SubProduct.class, "IsActive");

  public static final IProperty[] getAllColumnProperties() {
    return new IProperty[]{SubProductCode,Description,ProductCode,OrderNo,IsConventional,Url,ScheduleName,AttachBenefit,AttachClaim,AttachOthers,WsUrl,IdOrFa,AnnOrReg,IsNeedFlight,MaxAgeFrom,MaxAgeTo,MinAge,MaxDay,MaxDayBda,LoadingPct,Charges,BdaAmount,StampAmount,MaxDayTravel,IsConvertToIdr,MinMember,IsMobile,IsActive};
  }

  public static BaseProperty getProperty(String columnName) {
    columnName = QueryBuilder.quoteIfNeeded(columnName);
    switch (columnName)  {
      case "`SubProductCode`":  {
        return SubProductCode;
      }
      case "`Description`":  {
        return Description;
      }
      case "`ProductCode`":  {
        return ProductCode;
      }
      case "`OrderNo`":  {
        return OrderNo;
      }
      case "`IsConventional`":  {
        return IsConventional;
      }
      case "`Url`":  {
        return Url;
      }
      case "`ScheduleName`":  {
        return ScheduleName;
      }
      case "`AttachBenefit`":  {
        return AttachBenefit;
      }
      case "`AttachClaim`":  {
        return AttachClaim;
      }
      case "`AttachOthers`":  {
        return AttachOthers;
      }
      case "`WsUrl`":  {
        return WsUrl;
      }
      case "`IdOrFa`":  {
        return IdOrFa;
      }
      case "`AnnOrReg`":  {
        return AnnOrReg;
      }
      case "`IsNeedFlight`":  {
        return IsNeedFlight;
      }
      case "`MaxAgeFrom`":  {
        return MaxAgeFrom;
      }
      case "`MaxAgeTo`":  {
        return MaxAgeTo;
      }
      case "`MinAge`":  {
        return MinAge;
      }
      case "`MaxDay`":  {
        return MaxDay;
      }
      case "`MaxDayBda`":  {
        return MaxDayBda;
      }
      case "`LoadingPct`":  {
        return LoadingPct;
      }
      case "`Charges`":  {
        return Charges;
      }
      case "`BdaAmount`":  {
        return BdaAmount;
      }
      case "`StampAmount`":  {
        return StampAmount;
      }
      case "`MaxDayTravel`":  {
        return MaxDayTravel;
      }
      case "`IsConvertToIdr`":  {
        return IsConvertToIdr;
      }
      case "`MinMember`":  {
        return MinMember;
      }
      case "`IsMobile`":  {
        return IsMobile;
      }
      case "`IsActive`":  {
        return IsActive;
      }
      default:  {
        throw new IllegalArgumentException("Invalid column name passed. Ensure you are calling the correct table's column");
      }
    }
  }
}
