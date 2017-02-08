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
import java.lang.Override;
import java.lang.String;

public final class AATemplate_Adapter extends ModelAdapter<AATemplate> {
  public AATemplate_Adapter(DatabaseHolder holder) {
  }

  @Override
  public final Class<AATemplate> getModelClass() {
    return AATemplate.class;
  }

  public final String getTableName() {
    return "`AATemplate`";
  }

  @Override
  public final IProperty[] getAllColumnProperties() {
    return AATemplate_Table.getAllColumnProperties();
  }

  @Override
  public final void bindToInsertValues(ContentValues values, AATemplate model) {
    if (model.CountryId != null) {
      values.put(AATemplate_Table.CountryId.getCursorKey(), model.CountryId);
    } else {
      values.putNull(AATemplate_Table.CountryId.getCursorKey());
    }
    if (model.CountryName != null) {
      values.put(AATemplate_Table.CountryName.getCursorKey(), model.CountryName);
    } else {
      values.putNull(AATemplate_Table.CountryName.getCursorKey());
    }
  }

  @Override
  public final void bindToContentValues(ContentValues values, AATemplate model) {
    bindToInsertValues(values, model);
  }

  @Override
  public final void bindToInsertStatement(DatabaseStatement statement, AATemplate model, int start) {
    if (model.CountryId != null) {
      statement.bindString(1 + start, model.CountryId);
    } else {
      statement.bindNull(1 + start);
    }
    if (model.CountryName != null) {
      statement.bindString(2 + start, model.CountryName);
    } else {
      statement.bindNull(2 + start);
    }
  }

  @Override
  public final void bindToStatement(DatabaseStatement statement, AATemplate model) {
    bindToInsertStatement(statement, model, 0);
  }

  @Override
  public final String getInsertStatementQuery() {
    return "INSERT INTO `AATemplate`(`CountryId`,`CountryName`) VALUES (?,?)";
  }

  @Override
  public final String getCompiledStatementQuery() {
    return "INSERT INTO `AATemplate`(`CountryId`,`CountryName`) VALUES (?,?)";
  }

  @Override
  public final String getCreationQuery() {
    return "CREATE TABLE IF NOT EXISTS `AATemplate`(`CountryId` TEXT,`CountryName` TEXT, PRIMARY KEY(`CountryId`)" + ");";
  }

  @Override
  public final void loadFromCursor(Cursor cursor, AATemplate model) {
    int indexCountryId = cursor.getColumnIndex("CountryId");
    if (indexCountryId != -1 && !cursor.isNull(indexCountryId)) {
      model.CountryId = cursor.getString(indexCountryId);
    } else {
      model.CountryId = null;
    }
    int indexCountryName = cursor.getColumnIndex("CountryName");
    if (indexCountryName != -1 && !cursor.isNull(indexCountryName)) {
      model.CountryName = cursor.getString(indexCountryName);
    } else {
      model.CountryName = null;
    }
  }

  @Override
  public final boolean exists(AATemplate model, DatabaseWrapper wrapper) {
    return new Select(Method.count()).from(AATemplate.class).where(getPrimaryConditionClause(model)).count(wrapper) > 0;
  }

  @Override
  public final ConditionGroup getPrimaryConditionClause(AATemplate model) {
    ConditionGroup clause = ConditionGroup.clause();
    clause.and(AATemplate_Table.CountryId.eq(model.CountryId));return clause;
  }

  @Override
  public final AATemplate newInstance() {
    return new AATemplate();
  }

  @Override
  public final BaseProperty getProperty(String name) {
    return AATemplate_Table.getProperty(name);
  }
}
