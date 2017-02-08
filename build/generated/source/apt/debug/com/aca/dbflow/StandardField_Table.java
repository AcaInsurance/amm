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
public final class StandardField_Table {
  public static final PropertyConverter PROPERTY_CONVERTER = new PropertyConverter(){ 
  public IProperty fromName(String columnName) {
  return com.aca.dbflow.StandardField_Table.getProperty(columnName); 
  }
  };

  public static final IntProperty id = new IntProperty(StandardField.class, "id");

  public static final Property<String> FieldCode = new Property<String>(StandardField.class, "FieldCode");

  public static final Property<String> FieldCodeDt = new Property<String>(StandardField.class, "FieldCodeDt");

  public static final Property<String> FieldNameDt = new Property<String>(StandardField.class, "FieldNameDt");

  public static final Property<String> Value = new Property<String>(StandardField.class, "Value");

  public static final Property<String> Description = new Property<String>(StandardField.class, "Description");

  public static final Property<String> CreateBy = new Property<String>(StandardField.class, "CreateBy");

  public static final Property<String> CreateDate = new Property<String>(StandardField.class, "CreateDate");

  public static final Property<String> ModifyBy = new Property<String>(StandardField.class, "ModifyBy");

  public static final Property<String> ModifyDate = new Property<String>(StandardField.class, "ModifyDate");

  public static final Property<String> IsActive = new Property<String>(StandardField.class, "IsActive");

  public static final IProperty[] getAllColumnProperties() {
    return new IProperty[]{id,FieldCode,FieldCodeDt,FieldNameDt,Value,Description,CreateBy,CreateDate,ModifyBy,ModifyDate,IsActive};
  }

  public static BaseProperty getProperty(String columnName) {
    columnName = QueryBuilder.quoteIfNeeded(columnName);
    switch (columnName)  {
      case "`id`":  {
        return id;
      }
      case "`FieldCode`":  {
        return FieldCode;
      }
      case "`FieldCodeDt`":  {
        return FieldCodeDt;
      }
      case "`FieldNameDt`":  {
        return FieldNameDt;
      }
      case "`Value`":  {
        return Value;
      }
      case "`Description`":  {
        return Description;
      }
      case "`CreateBy`":  {
        return CreateBy;
      }
      case "`CreateDate`":  {
        return CreateDate;
      }
      case "`ModifyBy`":  {
        return ModifyBy;
      }
      case "`ModifyDate`":  {
        return ModifyDate;
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
