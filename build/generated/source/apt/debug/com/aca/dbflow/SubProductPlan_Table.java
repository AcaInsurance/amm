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
public final class SubProductPlan_Table {
  public static final PropertyConverter PROPERTY_CONVERTER = new PropertyConverter(){ 
  public IProperty fromName(String columnName) {
  return com.aca.dbflow.SubProductPlan_Table.getProperty(columnName); 
  }
  };

  public static final Property<String> PlanCode = new Property<String>(SubProductPlan.class, "PlanCode");

  public static final Property<String> SubProductCode = new Property<String>(SubProductPlan.class, "SubProductCode");

  public static final Property<String> Description = new Property<String>(SubProductPlan.class, "Description");

  public static final Property<String> OrderNo = new Property<String>(SubProductPlan.class, "OrderNo");

  public static final Property<String> Benefit = new Property<String>(SubProductPlan.class, "Benefit");

  public static final Property<String> BenefitImage = new Property<String>(SubProductPlan.class, "BenefitImage");

  public static final Property<String> CoverageCodeAs400 = new Property<String>(SubProductPlan.class, "CoverageCodeAs400");

  public static final Property<String> IsActive = new Property<String>(SubProductPlan.class, "IsActive");

  public static final IProperty[] getAllColumnProperties() {
    return new IProperty[]{PlanCode,SubProductCode,Description,OrderNo,Benefit,BenefitImage,CoverageCodeAs400,IsActive};
  }

  public static BaseProperty getProperty(String columnName) {
    columnName = QueryBuilder.quoteIfNeeded(columnName);
    switch (columnName)  {
      case "`PlanCode`":  {
        return PlanCode;
      }
      case "`SubProductCode`":  {
        return SubProductCode;
      }
      case "`Description`":  {
        return Description;
      }
      case "`OrderNo`":  {
        return OrderNo;
      }
      case "`Benefit`":  {
        return Benefit;
      }
      case "`BenefitImage`":  {
        return BenefitImage;
      }
      case "`CoverageCodeAs400`":  {
        return CoverageCodeAs400;
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
