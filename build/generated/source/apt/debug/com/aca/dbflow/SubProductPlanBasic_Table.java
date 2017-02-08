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
public final class SubProductPlanBasic_Table {
  public static final PropertyConverter PROPERTY_CONVERTER = new PropertyConverter(){ 
  public IProperty fromName(String columnName) {
  return com.aca.dbflow.SubProductPlanBasic_Table.getProperty(columnName); 
  }
  };

  public static final Property<String> SubPlanBasicId = new Property<String>(SubProductPlanBasic.class, "SubPlanBasicId");

  public static final Property<String> PlanCode = new Property<String>(SubProductPlanBasic.class, "PlanCode");

  public static final Property<String> SubProductCode = new Property<String>(SubProductPlanBasic.class, "SubProductCode");

  public static final Property<String> PremiumAmount = new Property<String>(SubProductPlanBasic.class, "PremiumAmount");

  public static final Property<String> AllocationAmount = new Property<String>(SubProductPlanBasic.class, "AllocationAmount");

  public static final Property<String> ZoneId = new Property<String>(SubProductPlanBasic.class, "ZoneId");

  public static final Property<String> DurationCodeAs400 = new Property<String>(SubProductPlanBasic.class, "DurationCodeAs400");

  public static final Property<String> IsActive = new Property<String>(SubProductPlanBasic.class, "IsActive");

  public static final IntProperty DayFrom = new IntProperty(SubProductPlanBasic.class, "DayFrom");

  public static final IntProperty DayTo = new IntProperty(SubProductPlanBasic.class, "DayTo");

  public static final IProperty[] getAllColumnProperties() {
    return new IProperty[]{SubPlanBasicId,PlanCode,SubProductCode,PremiumAmount,AllocationAmount,ZoneId,DurationCodeAs400,IsActive,DayFrom,DayTo};
  }

  public static BaseProperty getProperty(String columnName) {
    columnName = QueryBuilder.quoteIfNeeded(columnName);
    switch (columnName)  {
      case "`SubPlanBasicId`":  {
        return SubPlanBasicId;
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
      case "`DayFrom`":  {
        return DayFrom;
      }
      case "`DayTo`":  {
        return DayTo;
      }
      default:  {
        throw new IllegalArgumentException("Invalid column name passed. Ensure you are calling the correct table's column");
      }
    }
  }
}
