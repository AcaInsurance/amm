package com.aca.dbflow;

import android.content.ContentValues;
import android.database.Cursor;
import com.raizlabs.android.dbflow.config.DatabaseHolder;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.Method;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.sql.language.property.BaseProperty;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseStatement;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import java.lang.Class;
import java.lang.Number;
import java.lang.Override;
import java.lang.String;

public final class StandardField_Adapter extends ModelAdapter<StandardField> {
  public StandardField_Adapter(DatabaseHolder holder) {
  }

  @Override
  public final Class<StandardField> getModelClass() {
    return StandardField.class;
  }

  public final String getTableName() {
    return "`StandardField`";
  }

  public final void updateAutoIncrement(StandardField model, Number id) {
    model.id = id.intValue();
  }

  @Override
  public final Number getAutoIncrementingId(StandardField model) {
    return model.id;
  }

  @Override
  public final String getAutoIncrementingColumnName() {
    return "id";
  }

  @Override
  public final IProperty[] getAllColumnProperties() {
    return StandardField_Table.getAllColumnProperties();
  }

  @Override
  public final void bindToInsertValues(ContentValues values, StandardField model) {
    if (model.FieldCode != null) {
      values.put(StandardField_Table.FieldCode.getCursorKey(), model.FieldCode);
    } else {
      values.putNull(StandardField_Table.FieldCode.getCursorKey());
    }
    if (model.FieldCodeDt != null) {
      values.put(StandardField_Table.FieldCodeDt.getCursorKey(), model.FieldCodeDt);
    } else {
      values.putNull(StandardField_Table.FieldCodeDt.getCursorKey());
    }
    if (model.FieldNameDt != null) {
      values.put(StandardField_Table.FieldNameDt.getCursorKey(), model.FieldNameDt);
    } else {
      values.putNull(StandardField_Table.FieldNameDt.getCursorKey());
    }
    if (model.Value != null) {
      values.put(StandardField_Table.Value.getCursorKey(), model.Value);
    } else {
      values.putNull(StandardField_Table.Value.getCursorKey());
    }
    if (model.Description != null) {
      values.put(StandardField_Table.Description.getCursorKey(), model.Description);
    } else {
      values.putNull(StandardField_Table.Description.getCursorKey());
    }
    if (model.CreateBy != null) {
      values.put(StandardField_Table.CreateBy.getCursorKey(), model.CreateBy);
    } else {
      values.putNull(StandardField_Table.CreateBy.getCursorKey());
    }
    if (model.CreateDate != null) {
      values.put(StandardField_Table.CreateDate.getCursorKey(), model.CreateDate);
    } else {
      values.putNull(StandardField_Table.CreateDate.getCursorKey());
    }
    if (model.ModifyBy != null) {
      values.put(StandardField_Table.ModifyBy.getCursorKey(), model.ModifyBy);
    } else {
      values.putNull(StandardField_Table.ModifyBy.getCursorKey());
    }
    if (model.ModifyDate != null) {
      values.put(StandardField_Table.ModifyDate.getCursorKey(), model.ModifyDate);
    } else {
      values.putNull(StandardField_Table.ModifyDate.getCursorKey());
    }
    if (model.IsActive != null) {
      values.put(StandardField_Table.IsActive.getCursorKey(), model.IsActive);
    } else {
      values.putNull(StandardField_Table.IsActive.getCursorKey());
    }
  }

  @Override
  public final void bindToContentValues(ContentValues values, StandardField model) {
    values.put(StandardField_Table.id.getCursorKey(), model.id);
    bindToInsertValues(values, model);
  }

  @Override
  public final void bindToInsertStatement(DatabaseStatement statement, StandardField model, int start) {
    if (model.FieldCode != null) {
      statement.bindString(1 + start, model.FieldCode);
    } else {
      statement.bindNull(1 + start);
    }
    if (model.FieldCodeDt != null) {
      statement.bindString(2 + start, model.FieldCodeDt);
    } else {
      statement.bindNull(2 + start);
    }
    if (model.FieldNameDt != null) {
      statement.bindString(3 + start, model.FieldNameDt);
    } else {
      statement.bindNull(3 + start);
    }
    if (model.Value != null) {
      statement.bindString(4 + start, model.Value);
    } else {
      statement.bindNull(4 + start);
    }
    if (model.Description != null) {
      statement.bindString(5 + start, model.Description);
    } else {
      statement.bindNull(5 + start);
    }
    if (model.CreateBy != null) {
      statement.bindString(6 + start, model.CreateBy);
    } else {
      statement.bindNull(6 + start);
    }
    if (model.CreateDate != null) {
      statement.bindString(7 + start, model.CreateDate);
    } else {
      statement.bindNull(7 + start);
    }
    if (model.ModifyBy != null) {
      statement.bindString(8 + start, model.ModifyBy);
    } else {
      statement.bindNull(8 + start);
    }
    if (model.ModifyDate != null) {
      statement.bindString(9 + start, model.ModifyDate);
    } else {
      statement.bindNull(9 + start);
    }
    if (model.IsActive != null) {
      statement.bindString(10 + start, model.IsActive);
    } else {
      statement.bindNull(10 + start);
    }
  }

  @Override
  public final void bindToStatement(DatabaseStatement statement, StandardField model) {
    statement.bindLong(1, model.id);
    bindToInsertStatement(statement, model, 1);
  }

  @Override
  public final String getInsertStatementQuery() {
    return "INSERT INTO `StandardField`(`FieldCode`,`FieldCodeDt`,`FieldNameDt`,`Value`,`Description`,`CreateBy`,`CreateDate`,`ModifyBy`,`ModifyDate`,`IsActive`) VALUES (?,?,?,?,?,?,?,?,?,?)";
  }

  @Override
  public final String getCompiledStatementQuery() {
    return "INSERT INTO `StandardField`(`id`,`FieldCode`,`FieldCodeDt`,`FieldNameDt`,`Value`,`Description`,`CreateBy`,`CreateDate`,`ModifyBy`,`ModifyDate`,`IsActive`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
  }

  @Override
  public final String getCreationQuery() {
    return "CREATE TABLE IF NOT EXISTS `StandardField`(`id` INTEGER PRIMARY KEY AUTOINCREMENT,`FieldCode` TEXT,`FieldCodeDt` TEXT,`FieldNameDt` TEXT,`Value` TEXT,`Description` TEXT,`CreateBy` TEXT,`CreateDate` TEXT,`ModifyBy` TEXT,`ModifyDate` TEXT,`IsActive` TEXT" + ");";
  }

  @Override
  public final void loadFromCursor(Cursor cursor, StandardField model) {
    int indexid = cursor.getColumnIndex("id");
    if (indexid != -1 && !cursor.isNull(indexid)) {
      model.id = cursor.getInt(indexid);
    } else {
      model.id = 0;
    }
    int indexFieldCode = cursor.getColumnIndex("FieldCode");
    if (indexFieldCode != -1 && !cursor.isNull(indexFieldCode)) {
      model.FieldCode = cursor.getString(indexFieldCode);
    } else {
      model.FieldCode = null;
    }
    int indexFieldCodeDt = cursor.getColumnIndex("FieldCodeDt");
    if (indexFieldCodeDt != -1 && !cursor.isNull(indexFieldCodeDt)) {
      model.FieldCodeDt = cursor.getString(indexFieldCodeDt);
    } else {
      model.FieldCodeDt = null;
    }
    int indexFieldNameDt = cursor.getColumnIndex("FieldNameDt");
    if (indexFieldNameDt != -1 && !cursor.isNull(indexFieldNameDt)) {
      model.FieldNameDt = cursor.getString(indexFieldNameDt);
    } else {
      model.FieldNameDt = null;
    }
    int indexValue = cursor.getColumnIndex("Value");
    if (indexValue != -1 && !cursor.isNull(indexValue)) {
      model.Value = cursor.getString(indexValue);
    } else {
      model.Value = null;
    }
    int indexDescription = cursor.getColumnIndex("Description");
    if (indexDescription != -1 && !cursor.isNull(indexDescription)) {
      model.Description = cursor.getString(indexDescription);
    } else {
      model.Description = null;
    }
    int indexCreateBy = cursor.getColumnIndex("CreateBy");
    if (indexCreateBy != -1 && !cursor.isNull(indexCreateBy)) {
      model.CreateBy = cursor.getString(indexCreateBy);
    } else {
      model.CreateBy = null;
    }
    int indexCreateDate = cursor.getColumnIndex("CreateDate");
    if (indexCreateDate != -1 && !cursor.isNull(indexCreateDate)) {
      model.CreateDate = cursor.getString(indexCreateDate);
    } else {
      model.CreateDate = null;
    }
    int indexModifyBy = cursor.getColumnIndex("ModifyBy");
    if (indexModifyBy != -1 && !cursor.isNull(indexModifyBy)) {
      model.ModifyBy = cursor.getString(indexModifyBy);
    } else {
      model.ModifyBy = null;
    }
    int indexModifyDate = cursor.getColumnIndex("ModifyDate");
    if (indexModifyDate != -1 && !cursor.isNull(indexModifyDate)) {
      model.ModifyDate = cursor.getString(indexModifyDate);
    } else {
      model.ModifyDate = null;
    }
    int indexIsActive = cursor.getColumnIndex("IsActive");
    if (indexIsActive != -1 && !cursor.isNull(indexIsActive)) {
      model.IsActive = cursor.getString(indexIsActive);
    } else {
      model.IsActive = null;
    }
  }

  @Override
  public final boolean exists(StandardField model, DatabaseWrapper wrapper) {
    return model.id > 0 && new Select(Method.count()).from(StandardField.class).where(getPrimaryConditionClause(model)).count(wrapper) > 0;
  }

  @Override
  public final ConditionGroup getPrimaryConditionClause(StandardField model) {
    ConditionGroup clause = ConditionGroup.clause();
    clause.and(StandardField_Table.id.eq(model.id));return clause;
  }

  @Override
  public final StandardField newInstance() {
    return new StandardField();
  }

  @Override
  public final BaseProperty getProperty(String name) {
    return StandardField_Table.getProperty(name);
  }
}
