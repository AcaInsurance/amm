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

public final class SubProductPlan_Adapter extends ModelAdapter<SubProductPlan> {
  public SubProductPlan_Adapter(DatabaseHolder holder) {
  }

  @Override
  public final Class<SubProductPlan> getModelClass() {
    return SubProductPlan.class;
  }

  public final String getTableName() {
    return "`SubProductPlan`";
  }

  @Override
  public final IProperty[] getAllColumnProperties() {
    return SubProductPlan_Table.getAllColumnProperties();
  }

  @Override
  public final void bindToInsertValues(ContentValues values, SubProductPlan model) {
    if (model.PlanCode != null) {
      values.put(SubProductPlan_Table.PlanCode.getCursorKey(), model.PlanCode);
    } else {
      values.putNull(SubProductPlan_Table.PlanCode.getCursorKey());
    }
    if (model.SubProductCode != null) {
      values.put(SubProductPlan_Table.SubProductCode.getCursorKey(), model.SubProductCode);
    } else {
      values.putNull(SubProductPlan_Table.SubProductCode.getCursorKey());
    }
    if (model.Description != null) {
      values.put(SubProductPlan_Table.Description.getCursorKey(), model.Description);
    } else {
      values.putNull(SubProductPlan_Table.Description.getCursorKey());
    }
    if (model.OrderNo != null) {
      values.put(SubProductPlan_Table.OrderNo.getCursorKey(), model.OrderNo);
    } else {
      values.putNull(SubProductPlan_Table.OrderNo.getCursorKey());
    }
    if (model.Benefit != null) {
      values.put(SubProductPlan_Table.Benefit.getCursorKey(), model.Benefit);
    } else {
      values.putNull(SubProductPlan_Table.Benefit.getCursorKey());
    }
    if (model.BenefitImage != null) {
      values.put(SubProductPlan_Table.BenefitImage.getCursorKey(), model.BenefitImage);
    } else {
      values.putNull(SubProductPlan_Table.BenefitImage.getCursorKey());
    }
    if (model.CoverageCodeAs400 != null) {
      values.put(SubProductPlan_Table.CoverageCodeAs400.getCursorKey(), model.CoverageCodeAs400);
    } else {
      values.putNull(SubProductPlan_Table.CoverageCodeAs400.getCursorKey());
    }
    if (model.IsActive != null) {
      values.put(SubProductPlan_Table.IsActive.getCursorKey(), model.IsActive);
    } else {
      values.putNull(SubProductPlan_Table.IsActive.getCursorKey());
    }
  }

  @Override
  public final void bindToContentValues(ContentValues values, SubProductPlan model) {
    bindToInsertValues(values, model);
  }

  @Override
  public final void bindToInsertStatement(DatabaseStatement statement, SubProductPlan model, int start) {
    if (model.PlanCode != null) {
      statement.bindString(1 + start, model.PlanCode);
    } else {
      statement.bindNull(1 + start);
    }
    if (model.SubProductCode != null) {
      statement.bindString(2 + start, model.SubProductCode);
    } else {
      statement.bindNull(2 + start);
    }
    if (model.Description != null) {
      statement.bindString(3 + start, model.Description);
    } else {
      statement.bindNull(3 + start);
    }
    if (model.OrderNo != null) {
      statement.bindString(4 + start, model.OrderNo);
    } else {
      statement.bindNull(4 + start);
    }
    if (model.Benefit != null) {
      statement.bindString(5 + start, model.Benefit);
    } else {
      statement.bindNull(5 + start);
    }
    if (model.BenefitImage != null) {
      statement.bindString(6 + start, model.BenefitImage);
    } else {
      statement.bindNull(6 + start);
    }
    if (model.CoverageCodeAs400 != null) {
      statement.bindString(7 + start, model.CoverageCodeAs400);
    } else {
      statement.bindNull(7 + start);
    }
    if (model.IsActive != null) {
      statement.bindString(8 + start, model.IsActive);
    } else {
      statement.bindNull(8 + start);
    }
  }

  @Override
  public final void bindToStatement(DatabaseStatement statement, SubProductPlan model) {
    bindToInsertStatement(statement, model, 0);
  }

  @Override
  public final String getInsertStatementQuery() {
    return "INSERT INTO `SubProductPlan`(`PlanCode`,`SubProductCode`,`Description`,`OrderNo`,`Benefit`,`BenefitImage`,`CoverageCodeAs400`,`IsActive`) VALUES (?,?,?,?,?,?,?,?)";
  }

  @Override
  public final String getCompiledStatementQuery() {
    return "INSERT INTO `SubProductPlan`(`PlanCode`,`SubProductCode`,`Description`,`OrderNo`,`Benefit`,`BenefitImage`,`CoverageCodeAs400`,`IsActive`) VALUES (?,?,?,?,?,?,?,?)";
  }

  @Override
  public final String getCreationQuery() {
    return "CREATE TABLE IF NOT EXISTS `SubProductPlan`(`PlanCode` TEXT,`SubProductCode` TEXT,`Description` TEXT,`OrderNo` TEXT,`Benefit` TEXT,`BenefitImage` TEXT,`CoverageCodeAs400` TEXT,`IsActive` TEXT, PRIMARY KEY(`PlanCode`)" + ");";
  }

  @Override
  public final void loadFromCursor(Cursor cursor, SubProductPlan model) {
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
    int indexDescription = cursor.getColumnIndex("Description");
    if (indexDescription != -1 && !cursor.isNull(indexDescription)) {
      model.Description = cursor.getString(indexDescription);
    } else {
      model.Description = null;
    }
    int indexOrderNo = cursor.getColumnIndex("OrderNo");
    if (indexOrderNo != -1 && !cursor.isNull(indexOrderNo)) {
      model.OrderNo = cursor.getString(indexOrderNo);
    } else {
      model.OrderNo = null;
    }
    int indexBenefit = cursor.getColumnIndex("Benefit");
    if (indexBenefit != -1 && !cursor.isNull(indexBenefit)) {
      model.Benefit = cursor.getString(indexBenefit);
    } else {
      model.Benefit = null;
    }
    int indexBenefitImage = cursor.getColumnIndex("BenefitImage");
    if (indexBenefitImage != -1 && !cursor.isNull(indexBenefitImage)) {
      model.BenefitImage = cursor.getString(indexBenefitImage);
    } else {
      model.BenefitImage = null;
    }
    int indexCoverageCodeAs400 = cursor.getColumnIndex("CoverageCodeAs400");
    if (indexCoverageCodeAs400 != -1 && !cursor.isNull(indexCoverageCodeAs400)) {
      model.CoverageCodeAs400 = cursor.getString(indexCoverageCodeAs400);
    } else {
      model.CoverageCodeAs400 = null;
    }
    int indexIsActive = cursor.getColumnIndex("IsActive");
    if (indexIsActive != -1 && !cursor.isNull(indexIsActive)) {
      model.IsActive = cursor.getString(indexIsActive);
    } else {
      model.IsActive = null;
    }
  }

  @Override
  public final boolean exists(SubProductPlan model, DatabaseWrapper wrapper) {
    return new Select(Method.count()).from(SubProductPlan.class).where(getPrimaryConditionClause(model)).count(wrapper) > 0;
  }

  @Override
  public final ConditionGroup getPrimaryConditionClause(SubProductPlan model) {
    ConditionGroup clause = ConditionGroup.clause();
    clause.and(SubProductPlan_Table.PlanCode.eq(model.PlanCode));return clause;
  }

  @Override
  public final SubProductPlan newInstance() {
    return new SubProductPlan();
  }

  @Override
  public final BaseProperty getProperty(String name) {
    return SubProductPlan_Table.getProperty(name);
  }
}
