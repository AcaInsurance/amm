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
public final class SubProductPlanAdd_Table {
  public static final PropertyConverter PROPERTY_CONVERTER = new PropertyConverter(){ 
  public IProperty fromName(String columnName) {
  return com.aca.dbflow.SubProductPlanAdd_Table.getProperty(columnName); 
  }
  };

  public static final Property<String> SubPlanAddId = new Property<String>(SubProductPlanAdd.class, "SubPlanAddId");

  public static final Property<String> PlanCode = new Property<String>(SubProductPlanAdd.class, "PlanCode");

  public static final Property<String> SubProductCode = new Property<String>(SubProductPlanAdd.class, "SubProductCode");

  public static final Property<String> PremiumAmount = new Property<String>(SubProductPlanAdd.class, "PremiumAmount");

  public static final Property<String> AllocationAmount = new Property<String>(SubProductPlanAdd.class, "AllocationAmount");

  public static final Property<String> ZoneId = new Property<String>(SubProductPlanAdd.class, "ZoneId");

  public static final Property<String> DurationCodeAs400 = new Property<String>(SubProductPlanAdd.class, "DurationCodeAs400");

  public static final Property<String> IsActive = new Property<String>(SubProductPlanAdd.class, "IsActive");

  public static final IProperty[] getAllColumnProperties() {
    return new IProperty[]{SubPlanAddId,PlanCode,SubProductCode,PremiumAmount,AllocationAmount,ZoneId,DurationCodeAs400,IsActive};
  }

  public static BaseProperty getProperty(String columnName) {
    columnName = QueryBuilder.quoteIfNeeded(columnName);
    switch (columnName)  {
      case "`SubPlanAddId`":  {
        return SubPlanAddId;
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
      case "`AllocationAmount`":  {
        return AllocationAmount;
      }
      case "`ZoneId`":  {
        return ZoneId;
      }
      case "`DurationCodeAs400`":  {
        return DurationCodeAs400;
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
