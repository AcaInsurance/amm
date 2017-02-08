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

public final class GeneralSetting_Adapter extends ModelAdapter<GeneralSetting> {
  public GeneralSetting_Adapter(DatabaseHolder holder) {
  }

  @Override
  public final Class<GeneralSetting> getModelClass() {
    return GeneralSetting.class;
  }

  public final String getTableName() {
    return "`GeneralSetting`";
  }

  public final void updateAutoIncrement(GeneralSetting model, Number id) {
    model.id = id.intValue();
  }

  @Override
  public final Number getAutoIncrementingId(GeneralSetting model) {
    return model.id;
  }

  @Override
  public final String getAutoIncrementingColumnName() {
    return "id";
  }

  @Override
  public final IProperty[] getAllColumnProperties() {
    return GeneralSetting_Table.getAllColumnProperties();
  }

  @Override
  public final void bindToInsertValues(ContentValues values, GeneralSetting model) {
    if (model.ParameterCode != null) {
      values.put(GeneralSetting_Table.ParameterCode.getCursorKey(), model.ParameterCode);
    } else {
      values.putNull(GeneralSetting_Table.ParameterCode.getCursorKey());
    }
    if (model.ParameterValue != null) {
      values.put(GeneralSetting_Table.ParameterValue.getCursorKey(), model.ParameterValue);
    } else {
      values.putNull(GeneralSetting_Table.ParameterValue.getCursorKey());
    }
  }

  @Override
  public final void bindToContentValues(ContentValues values, GeneralSetting model) {
    values.put(GeneralSetting_Table.id.getCursorKey(), model.id);
    bindToInsertValues(values, model);
  }

  @Override
  public final void bindToInsertStatement(DatabaseStatement statement, GeneralSetting model, int start) {
    if (model.ParameterCode != null) {
      statement.bindString(1 + start, model.ParameterCode);
    } else {
      statement.bindNull(1 + start);
    }
    if (model.ParameterValue != null) {
      statement.bindString(2 + start, model.ParameterValue);
    } else {
      statement.bindNull(2 + start);
    }
  }

  @Override
  public final void bindToStatement(DatabaseStatement statement, GeneralSetting model) {
    statement.bindLong(1, model.id);
    bindToInsertStatement(statement, model, 1);
  }

  @Override
  public final String getInsertStatementQuery() {
    return "INSERT INTO `GeneralSetting`(`ParameterCode`,`ParameterValue`) VALUES (?,?)";
  }

  @Override
  public final String getCompiledStatementQuery() {
    return "INSERT INTO `GeneralSetting`(`id`,`ParameterCode`,`ParameterValue`) VALUES (?,?,?)";
  }

  @Override
  public final String getCreationQuery() {
    return "CREATE TABLE IF NOT EXISTS `GeneralSetting`(`id` INTEGER PRIMARY KEY AUTOINCREMENT,`ParameterCode` TEXT,`ParameterValue` TEXT" + ");";
  }

  @Override
  public final void loadFromCursor(Cursor cursor, GeneralSetting model) {
    int indexid = cursor.getColumnIndex("id");
    if (indexid != -1 && !cursor.isNull(indexid)) {
      model.id = cursor.getInt(indexid);
    } else {
      model.id = 0;
    }
    int indexParameterCode = cursor.getColumnIndex("ParameterCode");
    if (indexParameterCode != -1 && !cursor.isNull(indexParameterCode)) {
      model.ParameterCode = cursor.getString(indexParameterCode);
    } else {
      model.ParameterCode = null;
    }
    int indexParameterValue = cursor.getColumnIndex("ParameterValue");
    if (indexParameterValue != -1 && !cursor.isNull(indexParameterValue)) {
      model.ParameterValue = cursor.getString(indexParameterValue);
    } else {
      model.ParameterValue = null;
    }
  }

  @Override
  public final boolean exists(GeneralSetting model, DatabaseWrapper wrapper) {
    return model.id > 0 && new Select(Method.count()).from(GeneralSetting.class).where(getPrimaryConditionClause(model)).count(wrapper) > 0;
  }

  @Override
  public final ConditionGroup getPrimaryConditionClause(GeneralSetting model) {
    ConditionGroup clause = ConditionGroup.clause();
    clause.and(GeneralSetting_Table.id.eq(model.id));return clause;
  }

  @Override
  public final GeneralSetting newInstance() {
    return new GeneralSetting();
  }

  @Override
  public final BaseProperty getProperty(String name) {
    return GeneralSetting_Table.getProperty(name);
  }
}
