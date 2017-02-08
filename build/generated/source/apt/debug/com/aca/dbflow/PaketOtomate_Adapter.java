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

public final class PaketOtomate_Adapter extends ModelAdapter<PaketOtomate> {
  public PaketOtomate_Adapter(DatabaseHolder holder) {
  }

  @Override
  public final Class<PaketOtomate> getModelClass() {
    return PaketOtomate.class;
  }

  public final String getTableName() {
    return "`PaketOtomate`";
  }

  public final void updateAutoIncrement(PaketOtomate model, Number id) {
    model.ID = id.intValue();
  }

  @Override
  public final Number getAutoIncrementingId(PaketOtomate model) {
    return model.ID;
  }

  @Override
  public final String getAutoIncrementingColumnName() {
    return "ID";
  }

  @Override
  public final IProperty[] getAllColumnProperties() {
    return PaketOtomate_Table.getAllColumnProperties();
  }

  @Override
  public final void bindToInsertValues(ContentValues values, PaketOtomate model) {
    if (model.KodeProduct != null) {
      values.put(PaketOtomate_Table.KodeProduct.getCursorKey(), model.KodeProduct);
    } else {
      values.putNull(PaketOtomate_Table.KodeProduct.getCursorKey());
    }
    if (model.Flood != null) {
      values.put(PaketOtomate_Table.Flood.getCursorKey(), model.Flood);
    } else {
      values.putNull(PaketOtomate_Table.Flood.getCursorKey());
    }
    if (model.Eq != null) {
      values.put(PaketOtomate_Table.Eq.getCursorKey(), model.Eq);
    } else {
      values.putNull(PaketOtomate_Table.Eq.getCursorKey());
    }
  }

  @Override
  public final void bindToContentValues(ContentValues values, PaketOtomate model) {
    values.put(PaketOtomate_Table.ID.getCursorKey(), model.ID);
    bindToInsertValues(values, model);
  }

  @Override
  public final void bindToInsertStatement(DatabaseStatement statement, PaketOtomate model, int start) {
    if (model.KodeProduct != null) {
      statement.bindString(1 + start, model.KodeProduct);
    } else {
      statement.bindNull(1 + start);
    }
    if (model.Flood != null) {
      statement.bindString(2 + start, model.Flood);
    } else {
      statement.bindNull(2 + start);
    }
    if (model.Eq != null) {
      statement.bindString(3 + start, model.Eq);
    } else {
      statement.bindNull(3 + start);
    }
  }

  @Override
  public final void bindToStatement(DatabaseStatement statement, PaketOtomate model) {
    statement.bindLong(1, model.ID);
    bindToInsertStatement(statement, model, 1);
  }

  @Override
  public final String getInsertStatementQuery() {
    return "INSERT INTO `PaketOtomate`(`KodeProduct`,`Flood`,`Eq`) VALUES (?,?,?)";
  }

  @Override
  public final String getCompiledStatementQuery() {
    return "INSERT INTO `PaketOtomate`(`ID`,`KodeProduct`,`Flood`,`Eq`) VALUES (?,?,?,?)";
  }

  @Override
  public final String getCreationQuery() {
    return "CREATE TABLE IF NOT EXISTS `PaketOtomate`(`ID` INTEGER PRIMARY KEY AUTOINCREMENT,`KodeProduct` TEXT,`Flood` TEXT,`Eq` TEXT" + ");";
  }

  @Override
  public final void loadFromCursor(Cursor cursor, PaketOtomate model) {
    int indexID = cursor.getColumnIndex("ID");
    if (indexID != -1 && !cursor.isNull(indexID)) {
      model.ID = cursor.getInt(indexID);
    } else {
      model.ID = 0;
    }
    int indexKodeProduct = cursor.getColumnIndex("KodeProduct");
    if (indexKodeProduct != -1 && !cursor.isNull(indexKodeProduct)) {
      model.KodeProduct = cursor.getString(indexKodeProduct);
    } else {
      model.KodeProduct = null;
    }
    int indexFlood = cursor.getColumnIndex("Flood");
    if (indexFlood != -1 && !cursor.isNull(indexFlood)) {
      model.Flood = cursor.getString(indexFlood);
    } else {
      model.Flood = null;
    }
    int indexEq = cursor.getColumnIndex("Eq");
    if (indexEq != -1 && !cursor.isNull(indexEq)) {
      model.Eq = cursor.getString(indexEq);
    } else {
      model.Eq = null;
    }
  }

  @Override
  public final boolean exists(PaketOtomate model, DatabaseWrapper wrapper) {
    return model.ID > 0 && new Select(Method.count()).from(PaketOtomate.class).where(getPrimaryConditionClause(model)).count(wrapper) > 0;
  }

  @Override
  public final ConditionGroup getPrimaryConditionClause(PaketOtomate model) {
    ConditionGroup clause = ConditionGroup.clause();
    clause.and(PaketOtomate_Table.ID.eq(model.ID));return clause;
  }

  @Override
  public final PaketOtomate newInstance() {
    return new PaketOtomate();
  }

  @Override
  public final BaseProperty getProperty(String name) {
    return PaketOtomate_Table.getProperty(name);
  }
}
