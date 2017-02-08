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
public final class SubProductPlanBDA_Table {
  public static final PropertyConverter PROPERTY_CONVERTER = new PropertyConverter(){ 
  public IProperty fromName(String columnName) {
  return com.aca.dbflow.SubProductPlanBDA_Table.getProperty(columnName); 
  }
  };

  public static final Property<String> SubPlanBdaId = new Property<String>(SubProductPlanBDA.class, "SubPlanBdaId");

  public static final Property<String> PlanCode = new Property<String>(SubProductPlanBDA.class, "PlanCode");

  public static final Property<String> SubProductCode = new Property<String>(SubProductPlanBDA.class, "SubProductCode");

  public static final Property<String> PremiumAmount = new Property<String>(SubProductPlanBDA.class, "PremiumAmount");

  public static final Property<String> ZoneId = new Property<String>(SubProductPlanBDA.class, "ZoneId");

  public static final Property<String> IsActive = new Property<String>(SubProductPlanBDA.class, "IsActive");

  public static final IProperty[] getAllColumnProperties() {
    return new IProperty[]{SubPlanBdaId,PlanCode,SubProductCode,PremiumAmount,ZoneId,IsActive};
  }

  public static BaseProperty getProperty(String columnName) {
    columnName = QueryBuilder.quoteIfNeeded(columnName);
    switch (columnName)  {
      case "`SubPlanBdaId`":  {
        return SubPlanBdaId;
      }
      case "`PlanCode`":  {
        return PlanCode;
      }
      case "`SubProductCode`":  {
        return SubProductCode;
      }
      case "`PremiumAmount`":  {
        return PremiumAmount;
      }
      case "`ZoneId`":  {
        return ZoneId;
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
