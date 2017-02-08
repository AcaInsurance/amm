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

public final class SubProductPlanBDA_Adapter extends ModelAdapter<SubProductPlanBDA> {
  public SubProductPlanBDA_Adapter(DatabaseHolder holder) {
  }

  @Override
  public final Class<SubProductPlanBDA> getModelClass() {
    return SubProductPlanBDA.class;
  }

  public final String getTableName() {
    return "`SubProductPlanBDA`";
  }

  @Override
  public final IProperty[] getAllColumnProperties() {
    return SubProductPlanBDA_Table.getAllColumnProperties();
  }

  @Override
  public final void bindToInsertValues(ContentValues values, SubProductPlanBDA model) {
    if (model.SubPlanBdaId != null) {
      values.put(SubProductPlanBDA_Table.SubPlanBdaId.getCursorKey(), model.SubPlanBdaId);
    } else {
      values.putNull(SubProductPlanBDA_Table.SubPlanBdaId.getCursorKey());
    }
    if (model.PlanCode != null) {
      values.put(SubProductPlanBDA_Table.PlanCode.getCursorKey(), model.PlanCode);
    } else {
      values.putNull(SubProductPlanBDA_Table.PlanCode.getCursorKey());
    }
    if (model.SubProductCode != null) {
      values.put(SubProductPlanBDA_Table.SubProductCode.getCursorKey(), model.SubProductCode);
    } else {
      values.putNull(SubProductPlanBDA_Table.SubProductCode.getCursorKey());
    }
    if (model.PremiumAmount != null) {
      values.put(SubProductPlanBDA_Table.PremiumAmount.getCursorKey(), model.PremiumAmount);
    } else {
      values.putNull(SubProductPlanBDA_Table.PremiumAmount.getCursorKey());
    }
    if (model.ZoneId != null) {
      values.put(SubProductPlanBDA_Table.ZoneId.getCursorKey(), model.ZoneId);
    } else {
      values.putNull(SubProductPlanBDA_Table.ZoneId.getCursorKey());
    }
    if (model.IsActive != null) {
      values.put(SubProductPlanBDA_Table.IsActive.getCursorKey(), model.IsActive);
    } else {
      values.putNull(SubProductPlanBDA_Table.IsActive.getCursorKey());
    }
  }

  @Override
  public final void bindToContentValues(ContentValues values, SubProductPlanBDA model) {
    bindToInsertValues(values, model);
  }

  @Override
  public final void bindToInsertStatement(DatabaseStatement statement, SubProductPlanBDA model, int start) {
    if (model.SubPlanBdaId != null) {
      statement.bindString(1 + start, model.SubPlanBdaId);
    } else {
      statement.bindNull(1 + start);
    }
    if (model.PlanCode != null) {
      statement.bindString(2 + start, model.PlanCode);
    } else {
      statement.bindNull(2 + start);
    }
    if (model.SubProductCode != null) {
      statement.bindString(3 + start, model.SubProductCode);
    } else {
      statement.bindNull(3 + start);
    }
    if (model.PremiumAmount != null) {
      statement.bindString(4 + start, model.PremiumAmount);
    } else {
      statement.bindNull(4 + start);
    }
    if (model.ZoneId != null) {
      statement.bindString(5 + start, model.ZoneId);
    } else {
      statement.bindNull(5 + start);
    }
    if (model.IsActive != null) {
      statement.bindString(6 + start, model.IsActive);
    } else {
      statement.bindNull(6 + start);
    }
  }

  @Override
  public final void bindToStatement(DatabaseStatement statement, SubProductPlanBDA model) {
    bindToInsertStatement(statement, model, 0);
  }

  @Override
  public final String getInsertStatementQuery() {
    return "INSERT INTO `SubProductPlanBDA`(`SubPlanBdaId`,`PlanCode`,`SubProductCode`,`PremiumAmount`,`ZoneId`,`IsActive`) VALUES (?,?,?,?,?,?)";
  }

  @Override
  public final String getCompiledStatementQuery() {
    return "INSERT INTO `SubProductPlanBDA`(`SubPlanBdaId`,`PlanCode`,`SubProductCode`,`PremiumAmount`,`ZoneId`,`IsActive`) VALUES (?,?,?,?,?,?)";
  }

  @Override
  public final String getCreationQuery() {
    return "CREATE TABLE IF NOT EXISTS `SubProductPlanBDA`(`SubPlanBdaId` TEXT,`PlanCode` TEXT,`SubProductCode` TEXT,`PremiumAmount` TEXT,`ZoneId` TEXT,`IsActive` TEXT, PRIMARY KEY(`SubPlanBdaId`)" + ");";
  }

  @Override
  public final void loadFromCursor(Cursor cursor, SubProductPlanBDA model) {
    int indexSubPlanBdaId = cursor.getColumnIndex("SubPlanBdaId");
    if (indexSubPlanBdaId != -1 && !cursor.isNull(indexSubPlanBdaId)) {
      model.SubPlanBdaId = cursor.getString(indexSubPlanBdaId);
    } else {
      model.SubPlanBdaId = null;
    }
    int indexPlanCode = cursor.getColumnIndex("PlanCode");
    if (indexPlanCode != -1 && !cursor.isNull(indexPlanCode)) {
      model.PlanCode = cursor.getString(indexPlanCode);
    } else {
      model.PlanCode = null;
    }
    int indexSubProductCode = cursor.getColumnIndex("SubProductCode");
    if (indexSubProductCode != -1 && !cursor.isNull(indexSubProductCode)) {
      model.SubProductCode = cursor.getString(indexSubProductCode);
    } else {
      model.SubProductCode = null;
    }
    int indexPremiumAmount = cursor.getColumnIndex("PremiumAmount");
    if (indexPremiumAmount != -1 && !cursor.isNull(indexPremiumAmount)) {
      model.PremiumAmount = cursor.getString(indexPremiumAmount);
    } else {
      model.PremiumAmount = null;
    }
    int indexZoneId = cursor.getColumnIndex("ZoneId");
    if (indexZoneId != -1 && !cursor.isNull(indexZoneId)) {
      model.ZoneId = cursor.getString(indexZoneId);
    } else {
      model.ZoneId = null;
    }
    int indexIsActive = cursor.getColumnIndex("IsActive");
    if (indexIsActive != -1 && !cursor.isNull(indexIsActive)) {
      model.IsActive = cursor.getString(indexIsActive);
    } else {
      model.IsActive = null;
    }
  }

  @Override
  public final boolean exists(SubProductPlanBDA model, DatabaseWrapper wrapper) {
    return new Select(Method.count()).from(SubProductPlanBDA.class).where(getPrimaryConditionClause(model)).count(wrapper) > 0;
  }

  @Override
  public final ConditionGroup getPrimaryConditionClause(SubProductPlanBDA model) {
    ConditionGroup clause = ConditionGroup.clause();
    clause.and(SubProductPlanBDA_Table.SubPlanBdaId.eq(model.SubPlanBdaId));return clause;
  }

  @Override
  public final SubProductPlanBDA newInstance() {
    return new SubProductPlanBDA();
  }

  @Override
  public final BaseProperty getProperty(String name) {
    return SubProductPlanBDA_Table.getProperty(name);
  }
}
