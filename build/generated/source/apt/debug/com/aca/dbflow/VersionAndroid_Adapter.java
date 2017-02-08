package com.aca.dbflow;

import android.content.ContentValues;
import android.database.Cursor;
import com.raizlabs.android.dbflow.config.DatabaseHolder;
import com.raizlabs.android.dbflow.converter.DateConverter;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.Method;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.sql.language.property.BaseProperty;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseStatement;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import java.lang.Class;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.util.Date;

public final class VersionAndroid_Adapter extends ModelAdapter<VersionAndroid> {
  private final DateConverter global_typeConverterDateConverter;

  public VersionAndroid_Adapter(DatabaseHolder holder) {
    global_typeConverterDateConverter = (DateConverter) holder.getTypeConverterForClass(Date.class);
  }

  @Override
  public final Class<VersionAndroid> getModelClass() {
    return VersionAndroid.class;
  }

  public final String getTableName() {
    return "`VersionAndroid`";
  }

  @Override
  public final IProperty[] getAllColumnProperties() {
    return VersionAndroid_Table.getAllColumnProperties();
  }

  @Override
  public final void bindToInsertValues(ContentValues values, VersionAndroid model) {
    values.put(VersionAndroid_Table.Version.getCursorKey(), model.Version);
    Long refDateTime = model.DateTime != null ? global_typeConverterDateConverter.getDBValue((java.util.Date) model.DateTime) : null;
    if (refDateTime != null) {
      values.put(VersionAndroid_Table.DateTime.getCursorKey(), refDateTime);
    } else {
      values.putNull(VersionAndroid_Table.DateTime.getCursorKey());
    }
    values.put(VersionAndroid_Table.Maintenance.getCursorKey(), model.Maintenance ? 1 : 0);
  }

  @Override
  public final void bindToContentValues(ContentValues values, VersionAndroid model) {
    bindToInsertValues(values, model);
  }

  @Override
  public final void bindToInsertStatement(DatabaseStatement statement, VersionAndroid model, int start) {
    statement.bindLong(1 + start, model.Version);
    Long refDateTime = model.DateTime != null ? global_typeConverterDateConverter.getDBValue((java.util.Date) model.DateTime) : null;
    if (refDateTime != null) {
      statement.bindLong(2 + start, refDateTime);
    } else {
      statement.bindNull(2 + start);
    }
    statement.bindLong(3 + start, model.Maintenance ? 1 : 0);
  }

  @Override
  public final void bindToStatement(DatabaseStatement statement, VersionAndroid model) {
    bindToInsertStatement(statement, model, 0);
  }

  @Override
  public final String getInsertStatementQuery() {
    return "INSERT INTO `VersionAndroid`(`Version`,`DateTime`,`Maintenance`) VALUES (?,?,?)";
  }

  @Override
  public final String getCompiledStatementQuery() {
    return "INSERT INTO `VersionAndroid`(`Version`,`DateTime`,`Maintenance`) VALUES (?,?,?)";
  }

  @Override
  public final String getCreationQuery() {
    return "CREATE TABLE IF NOT EXISTS `VersionAndroid`(`Version` INTEGER,`DateTime` INTEGER,`Maintenance` INTEGER, PRIMARY KEY(`Version`)" + ");";
  }

  @Override
  public final void loadFromCursor(Cursor cursor, VersionAndroid model) {
    int indexVersion = cursor.getColumnIndex("Version");
    if (indexVersion != -1 && !cursor.isNull(indexVersion)) {
      model.Version = cursor.getInt(indexVersion);
    } else {
      model.Version = 0;
    }
    int indexDateTime = cursor.getColumnIndex("DateTime");
    if (indexDateTime != -1 && !cursor.isNull(indexDateTime)) {
      model.DateTime = global_typeConverterDateConverter.getModelValue(cursor.getLong(indexDateTime));
    } else {
      model.DateTime = null;
    }
    int indexMaintenance = cursor.getColumnIndex("Maintenance");
    if (indexMaintenance != -1 && !cursor.isNull(indexMaintenance)) {
      model.Maintenance = cursor.getInt(indexMaintenance) == 1 ? true : false;
    } else {
      model.Maintenance = false;
    }
  }

  @Override
  public final boolean exists(VersionAndroid model, DatabaseWrapper wrapper) {
    return new Select(Method.count()).from(VersionAndroid.class).where(getPrimaryConditionClause(model)).count(wrapper) > 0;
  }

  @Override
  public final ConditionGroup getPrimaryConditionClause(VersionAndroid model) {
    ConditionGroup clause = ConditionGroup.clause();
    clause.and(VersionAndroid_Table.Version.eq(model.Version));return clause;
  }

  @Override
  public final VersionAndroid newInstance() {
    return new VersionAndroid();
  }

  @Override
  public final BaseProperty getProperty(String name) {
    return VersionAndroid_Table.getProperty(name);
  }
}
