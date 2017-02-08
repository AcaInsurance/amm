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

public final class SubProductPlanBasic_Adapter extends ModelAdapter<SubProductPlanBasic> {
  public SubProductPlanBasic_Adapter(DatabaseHolder holder) {
  }

  @Override
  public final Class<SubProductPlanBasic> getModelClass() {
    return SubProductPlanBasic.class;
  }

  public final String getTableName() {
    return "`SubProductPlanBasic`";
  }

  @Override
  public final IProperty[] getAllColumnProperties() {
    return SubProductPlanBasic_Table.getAllColumnProperties();
  }

  @Override
  public final void bindToInsertValues(ContentValues values, SubProductPlanBasic model) {
    if (model.SubPlanBasicId != null) {
      values.put(SubProductPlanBasic_Table.SubPlanBasicId.getCursorKey(), model.SubPlanBasicId);
    } else {
      values.putNull(SubProductPlanBasic_Table.SubPlanBasicId.getCursorKey());
    }
    if (model.PlanCode != null) {
      values.put(SubProductPlanBasic_Table.PlanCode.getCursorKey(), model.PlanCode);
    } else {
      values.putNull(SubProductPlanBasic_Table.PlanCode.getCursorKey());
    }
    if (model.SubProductCode != null) {
      values.put(SubProductPlanBasic_Table.SubProductCode.getCursorKey(), model.SubProductCode);
    } else {
      values.putNull(SubProductPlanBasic_Table.SubProductCode.getCursorKey());
    }
    if (model.PremiumAmount != null) {
      values.put(SubProductPlanBasic_Table.PremiumAmount.getCursorKey(), model.PremiumAmount);
    } else {
      values.putNull(SubProductPlanBasic_Table.PremiumAmount.getCursorKey());
    }
    if (model.AllocationAmount != null) {
      values.put(SubProductPlanBasic_Table.AllocationAmount.getCursorKey(), model.AllocationAmount);
    } else {
      values.putNull(SubProductPlanBasic_Table.AllocationAmount.getCursorKey());
    }
    if (model.ZoneId != null) {
      values.put(SubProductPlanBasic_Table.ZoneId.getCursorKey(), model.ZoneId);
    } else {
      values.putNull(SubProductPlanBasic_Table.ZoneId.getCursorKey());
    }
    if (model.DurationCodeAs400 != null) {
      values.put(SubProductPlanBasic_Table.DurationCodeAs400.getCursorKey(), model.DurationCodeAs400);
    } else {
      values.putNull(SubProductPlanBasic_Table.DurationCodeAs400.getCursorKey());
    }
    if (model.IsActive != null) {
      values.put(SubProductPlanBasic_Table.IsActive.getCursorKey(), model.IsActive);
    } else {
      values.putNull(SubProductPlanBasic_Table.IsActive.getCursorKey());
    }
    values.put(SubProductPlanBasic_Table.DayFrom.getCursorKey(), model.DayFrom);
    values.put(SubProductPlanBasic_Table.DayTo.getCursorKey(), model.DayTo);
  }

  @Override
  public final void bindToContentValues(ContentValues values, SubProductPlanBasic model) {
    bindToInsertValues(values, model);
  }

  @Override
  public final void bindToInsertStatement(DatabaseStatement statement, SubProductPlanBasic model, int start) {
    if (model.SubPlanBasicId != null) {
      statement.bindString(1 + start, model.SubPlanBasicId);
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
    if (model.AllocationAmount != null) {
      statement.bindString(5 + start, model.AllocationAmount);
    } else {
      statement.bindNull(5 + start);
    }
    if (model.ZoneId != null) {
      statement.bindString(6 + start, model.ZoneId);
    } else {
      statement.bindNull(6 + start);
    }
    if (model.DurationCodeAs400 != null) {
      statement.bindString(7 + start, model.DurationCodeAs400);
    } else {
      statement.bindNull(7 + start);
    }
    if (model.IsActive != null) {
      statement.bindString(8 + start, model.IsActive);
    } else {
      statement.bindNull(8 + start);
    }
    statement.bindLong(9 + start, model.DayFrom);
    statement.bindLong(10 + start, model.DayTo);
  }

  @Override
  public final void bindToStatement(DatabaseStatement statement, SubProductPlanBasic model) {
    bindToInsertStatement(statement, model, 0);
  }

  @Override
  public final String getInsertStatementQuery() {
    return "INSERT INTO `SubProductPlanBasic`(`SubPlanBasicId`,`PlanCode`,`SubProductCode`,`PremiumAmount`,`AllocationAmount`,`ZoneId`,`DurationCodeAs400`,`IsActive`,`DayFrom`,`DayTo`) VALUES (?,?,?,?,?,?,?,?,?,?)";
  }

  @Override
  public final String getCompiledStatementQuery() {
    return "INSERT INTO `SubProductPlanBasic`(`SubPlanBasicId`,`PlanCode`,`SubProductCode`,`PremiumAmount`,`AllocationAmount`,`ZoneId`,`DurationCodeAs400`,`IsActive`,`DayFrom`,`DayTo`) VALUES (?,?,?,?,?,?,?,?,?,?)";
  }

  @Override
  public final String getCreationQuery() {
    return "CREATE TABLE IF NOT EXISTS `SubProductPlanBasic`(`SubPlanBasicId` TEXT,`PlanCode` TEXT,`SubProductCode` TEXT,`PremiumAmount` TEXT,`AllocationAmount` TEXT,`ZoneId` TEXT,`DurationCodeAs400` TEXT,`IsActive` TEXT,`DayFrom` INTEGER,`DayTo` INTEGER, PRIMARY KEY(`SubPlanBasicId`)" + ");";
  }

  @Override
  public final void loadFromCursor(Cursor cursor, SubProductPlanBasic model) {
    int indexSubPlanBasicId = cursor.getColumnIndex("SubPlanBasicId");
    if (indexSubPlanBasicId != -1 && !cursor.isNull(indexSubPlanBasicId)) {
      model.SubPlanBasicId = cursor.getString(indexSubPlanBasicId);
    } else {
      model.SubPlanBasicId = null;
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
    int indexAllocationAmount = cursor.getColumnIndex("AllocationAmount");
    if (indexAllocationAmount != -1 && !cursor.isNull(indexAllocationAmount)) {
      model.AllocationAmount = cursor.getString(indexAllocationAmount);
    } else {
      model.AllocationAmount = null;
    }
    int indexZoneId = cursor.getColumnIndex("ZoneId");
    if (indexZoneId != -1 && !cursor.isNull(indexZoneId)) {
      model.ZoneId = cursor.getString(indexZoneId);
    } else {
      model.ZoneId = null;
    }
    int indexDurationCodeAs400 = cursor.getColumnIndex("DurationCodeAs400");
    if (indexDurationCodeAs400 != -1 && !cursor.isNull(indexDurationCodeAs400)) {
      model.DurationCodeAs400 = cursor.getString(indexDurationCodeAs400);
    } else {
      model.DurationCodeAs400 = null;
    }
    int indexIsActive = cursor.getColumnIndex("IsActive");
    if (indexIsActive != -1 && !cursor.isNull(indexIsActive)) {
      model.IsActive = cursor.getString(indexIsActive);
    } else {
      model.IsActive = null;
    }
    int indexDayFrom = cursor.getColumnIndex("DayFrom");
    if (indexDayFrom != -1 && !cursor.isNull(indexDayFrom)) {
      model.DayFrom = cursor.getInt(indexDayFrom);
    } else {
      model.DayFrom = 0;
    }
    int indexDayTo = cursor.getColumnIndex("DayTo");
    if (indexDayTo != -1 && !cursor.isNull(indexDayTo)) {
      model.DayTo = cursor.getInt(indexDayTo);
    } else {
      model.DayTo = 0;
    }
  }

  @Override
  public final boolean exists(SubProductPlanBasic model, DatabaseWrapper wrapper) {
    return new Select(Method.count()).from(SubProductPlanBasic.class).where(getPrimaryConditionClause(model)).count(wrapper) > 0;
  }

  @Override
  public final ConditionGroup getPrimaryConditionClause(SubProductPlanBasic model) {
    ConditionGroup clause = ConditionGroup.clause();
    clause.and(SubProductPlanBasic_Table.SubPlanBasicId.eq(model.SubPlanBasicId));return clause;
  }

  @Override
  public final SubProductPlanBasic newInstance() {
    return new SubProductPlanBasic();
  }

  @Override
  public final BaseProperty getProperty(String name) {
    return SubProductPlanBasic_Table.getProperty(name);
  }
}
