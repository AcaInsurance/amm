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

public final class PerluasanPremi_Adapter extends ModelAdapter<PerluasanPremi> {
  public PerluasanPremi_Adapter(DatabaseHolder holder) {
  }

  @Override
  public final Class<PerluasanPremi> getModelClass() {
    return PerluasanPremi.class;
  }

  public final String getTableName() {
    return "`PerluasanPremi`";
  }

  public final void updateAutoIncrement(PerluasanPremi model, Number id) {
    model.id = id.intValue();
  }

  @Override
  public final Number getAutoIncrementingId(PerluasanPremi model) {
    return model.id;
  }

  @Override
  public final String getAutoIncrementingColumnName() {
    return "id";
  }

  @Override
  public final IProperty[] getAllColumnProperties() {
    return PerluasanPremi_Table.getAllColumnProperties();
  }

  @Override
  public final void bindToInsertValues(ContentValues values, PerluasanPremi model) {
    if (model.Tipe != null) {
      values.put(PerluasanPremi_Table.Tipe.getCursorKey(), model.Tipe);
    } else {
      values.putNull(PerluasanPremi_Table.Tipe.getCursorKey());
    }
    if (model.Amount_Text != null) {
      values.put(PerluasanPremi_Table.Amount_Text.getCursorKey(), model.Amount_Text);
    } else {
      values.putNull(PerluasanPremi_Table.Amount_Text.getCursorKey());
    }
    if (model.Kode_Produk != null) {
      values.put(PerluasanPremi_Table.Kode_Produk.getCursorKey(), model.Kode_Produk);
    } else {
      values.putNull(PerluasanPremi_Table.Kode_Produk.getCursorKey());
    }
    values.put(PerluasanPremi_Table.Amount.getCursorKey(), model.Amount);
    values.put(PerluasanPremi_Table.Premi.getCursorKey(), model.Premi);
  }

  @Override
  public final void bindToContentValues(ContentValues values, PerluasanPremi model) {
    values.put(PerluasanPremi_Table.id.getCursorKey(), model.id);
    bindToInsertValues(values, model);
  }

  @Override
  public final void bindToInsertStatement(DatabaseStatement statement, PerluasanPremi model, int start) {
    if (model.Tipe != null) {
      statement.bindString(1 + start, model.Tipe);
    } else {
      statement.bindNull(1 + start);
    }
    if (model.Amount_Text != null) {
      statement.bindString(2 + start, model.Amount_Text);
    } else {
      statement.bindNull(2 + start);
    }
    if (model.Kode_Produk != null) {
      statement.bindString(3 + start, model.Kode_Produk);
    } else {
      statement.bindNull(3 + start);
    }
    statement.bindDouble(4 + start, model.Amount);
    statement.bindDouble(5 + start, model.Premi);
  }

  @Override
  public final void bindToStatement(DatabaseStatement statement, PerluasanPremi model) {
    statement.bindLong(1, model.id);
    bindToInsertStatement(statement, model, 1);
  }

  @Override
  public final String getInsertStatementQuery() {
    return "INSERT INTO `PerluasanPremi`(`Tipe`,`Amount_Text`,`Kode_Produk`,`Amount`,`Premi`) VALUES (?,?,?,?,?)";
  }

  @Override
  public final String getCompiledStatementQuery() {
    return "INSERT INTO `PerluasanPremi`(`id`,`Tipe`,`Amount_Text`,`Kode_Produk`,`Amount`,`Premi`) VALUES (?,?,?,?,?,?)";
  }

  @Override
  public final String getCreationQuery() {
    return "CREATE TABLE IF NOT EXISTS `PerluasanPremi`(`id` INTEGER PRIMARY KEY AUTOINCREMENT,`Tipe` TEXT,`Amount_Text` TEXT,`Kode_Produk` TEXT,`Amount` REAL,`Premi` REAL" + ");";
  }

  @Override
  public final void loadFromCursor(Cursor cursor, PerluasanPremi model) {
    int indexid = cursor.getColumnIndex("id");
    if (indexid != -1 && !cursor.isNull(indexid)) {
      model.id = cursor.getInt(indexid);
    } else {
      model.id = 0;
    }
    int indexTipe = cursor.getColumnIndex("Tipe");
    if (indexTipe != -1 && !cursor.isNull(indexTipe)) {
      model.Tipe = cursor.getString(indexTipe);
    } else {
      model.Tipe = null;
    }
    int indexAmount_Text = cursor.getColumnIndex("Amount_Text");
    if (indexAmount_Text != -1 && !cursor.isNull(indexAmount_Text)) {
      model.Amount_Text = cursor.getString(indexAmount_Text);
    } else {
      model.Amount_Text = null;
    }
    int indexKode_Produk = cursor.getColumnIndex("Kode_Produk");
    if (indexKode_Produk != -1 && !cursor.isNull(indexKode_Produk)) {
      model.Kode_Produk = cursor.getString(indexKode_Produk);
    } else {
      model.Kode_Produk = null;
    }
    int indexAmount = cursor.getColumnIndex("Amount");
    if (indexAmount != -1 && !cursor.isNull(indexAmount)) {
      model.Amount = cursor.getDouble(indexAmount);
    } else {
      model.Amount = 0;
    }
    int indexPremi = cursor.getColumnIndex("Premi");
    if (indexPremi != -1 && !cursor.isNull(indexPremi)) {
      model.Premi = cursor.getDouble(indexPremi);
    } else {
      model.Premi = 0;
    }
  }

  @Override
  public final boolean exists(PerluasanPremi model, DatabaseWrapper wrapper) {
    return model.id > 0 && new Select(Method.count()).from(PerluasanPremi.class).where(getPrimaryConditionClause(model)).count(wrapper) > 0;
  }

  @Override
  public final ConditionGroup getPrimaryConditionClause(PerluasanPremi model) {
    ConditionGroup clause = ConditionGroup.clause();
    clause.and(PerluasanPremi_Table.id.eq(model.id));return clause;
  }

  @Override
  public final PerluasanPremi newInstance() {
    return new PerluasanPremi();
  }

  @Override
  public final BaseProperty getProperty(String name) {
    return PerluasanPremi_Table.getProperty(name);
  }
}
