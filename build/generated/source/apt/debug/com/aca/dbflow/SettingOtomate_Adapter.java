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

public final class SettingOtomate_Adapter extends ModelAdapter<SettingOtomate> {
  public SettingOtomate_Adapter(DatabaseHolder holder) {
  }

  @Override
  public final Class<SettingOtomate> getModelClass() {
    return SettingOtomate.class;
  }

  public final String getTableName() {
    return "`SettingOtomate`";
  }

  public final void updateAutoIncrement(SettingOtomate model, Number id) {
    model.id = id.intValue();
  }

  @Override
  public final Number getAutoIncrementingId(SettingOtomate model) {
    return model.id;
  }

  @Override
  public final String getAutoIncrementingColumnName() {
    return "id";
  }

  @Override
  public final IProperty[] getAllColumnProperties() {
    return SettingOtomate_Table.getAllColumnProperties();
  }

  @Override
  public final void bindToInsertValues(ContentValues values, SettingOtomate model) {
    if (model.KodeProduct != null) {
      values.put(SettingOtomate_Table.KodeProduct.getCursorKey(), model.KodeProduct);
    } else {
      values.putNull(SettingOtomate_Table.KodeProduct.getCursorKey());
    }
    if (model.FloodDefault != null) {
      values.put(SettingOtomate_Table.FloodDefault.getCursorKey(), model.FloodDefault);
    } else {
      values.putNull(SettingOtomate_Table.FloodDefault.getCursorKey());
    }
    if (model.EqDefault != null) {
      values.put(SettingOtomate_Table.EqDefault.getCursorKey(), model.EqDefault);
    } else {
      values.putNull(SettingOtomate_Table.EqDefault.getCursorKey());
    }
    if (model.IsPaket != null) {
      values.put(SettingOtomate_Table.IsPaket.getCursorKey(), model.IsPaket);
    } else {
      values.putNull(SettingOtomate_Table.IsPaket.getCursorKey());
    }
    if (model.SRCCDefault != null) {
      values.put(SettingOtomate_Table.SRCCDefault.getCursorKey(), model.SRCCDefault);
    } else {
      values.putNull(SettingOtomate_Table.SRCCDefault.getCursorKey());
    }
    if (model.TSDefault != null) {
      values.put(SettingOtomate_Table.TSDefault.getCursorKey(), model.TSDefault);
    } else {
      values.putNull(SettingOtomate_Table.TSDefault.getCursorKey());
    }
    if (model.IsChangeable != null) {
      values.put(SettingOtomate_Table.IsChangeable.getCursorKey(), model.IsChangeable);
    } else {
      values.putNull(SettingOtomate_Table.IsChangeable.getCursorKey());
    }
    if (model.BengkelDefault != null) {
      values.put(SettingOtomate_Table.BengkelDefault.getCursorKey(), model.BengkelDefault);
    } else {
      values.putNull(SettingOtomate_Table.BengkelDefault.getCursorKey());
    }
    if (model.IsChangeableBengkel != null) {
      values.put(SettingOtomate_Table.IsChangeableBengkel.getCursorKey(), model.IsChangeableBengkel);
    } else {
      values.putNull(SettingOtomate_Table.IsChangeableBengkel.getCursorKey());
    }
    values.put(SettingOtomate_Table.LimitTPL.getCursorKey(), model.LimitTPL);
    values.put(SettingOtomate_Table.LimitPA.getCursorKey(), model.LimitPA);
  }

  @Override
  public final void bindToContentValues(ContentValues values, SettingOtomate model) {
    values.put(SettingOtomate_Table.id.getCursorKey(), model.id);
    bindToInsertValues(values, model);
  }

  @Override
  public final void bindToInsertStatement(DatabaseStatement statement, SettingOtomate model, int start) {
    if (model.KodeProduct != null) {
      statement.bindString(1 + start, model.KodeProduct);
    } else {
      statement.bindNull(1 + start);
    }
    if (model.FloodDefault != null) {
      statement.bindString(2 + start, model.FloodDefault);
    } else {
      statement.bindNull(2 + start);
    }
    if (model.EqDefault != null) {
      statement.bindString(3 + start, model.EqDefault);
    } else {
      statement.bindNull(3 + start);
    }
    if (model.IsPaket != null) {
      statement.bindString(4 + start, model.IsPaket);
    } else {
      statement.bindNull(4 + start);
    }
    if (model.SRCCDefault != null) {
      statement.bindString(5 + start, model.SRCCDefault);
    } else {
      statement.bindNull(5 + start);
    }
    if (model.TSDefault != null) {
      statement.bindString(6 + start, model.TSDefault);
    } else {
      statement.bindNull(6 + start);
    }
    if (model.IsChangeable != null) {
      statement.bindString(7 + start, model.IsChangeable);
    } else {
      statement.bindNull(7 + start);
    }
    if (model.BengkelDefault != null) {
      statement.bindString(8 + start, model.BengkelDefault);
    } else {
      statement.bindNull(8 + start);
    }
    if (model.IsChangeableBengkel != null) {
      statement.bindString(9 + start, model.IsChangeableBengkel);
    } else {
      statement.bindNull(9 + start);
    }
    statement.bindDouble(10 + start, model.LimitTPL);
    statement.bindDouble(11 + start, model.LimitPA);
  }

  @Override
  public final void bindToStatement(DatabaseStatement statement, SettingOtomate model) {
    statement.bindLong(1, model.id);
    bindToInsertStatement(statement, model, 1);
  }

  @Override
  public final String getInsertStatementQuery() {
    return "INSERT INTO `SettingOtomate`(`KodeProduct`,`FloodDefault`,`EqDefault`,`IsPaket`,`SRCCDefault`,`TSDefault`,`IsChangeable`,`BengkelDefault`,`IsChangeableBengkel`,`LimitTPL`,`LimitPA`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
  }

  @Override
  public final String getCompiledStatementQuery() {
    return "INSERT INTO `SettingOtomate`(`id`,`KodeProduct`,`FloodDefault`,`EqDefault`,`IsPaket`,`SRCCDefault`,`TSDefault`,`IsChangeable`,`BengkelDefault`,`IsChangeableBengkel`,`LimitTPL`,`LimitPA`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
  }

  @Override
  public final String getCreationQuery() {
    return "CREATE TABLE IF NOT EXISTS `SettingOtomate`(`id` INTEGER PRIMARY KEY AUTOINCREMENT,`KodeProduct` TEXT,`FloodDefault` TEXT,`EqDefault` TEXT,`IsPaket` TEXT,`SRCCDefault` TEXT,`TSDefault` TEXT,`IsChangeable` TEXT,`BengkelDefault` TEXT,`IsChangeableBengkel` TEXT,`LimitTPL` REAL,`LimitPA` REAL" + ");";
  }

  @Override
  public final void loadFromCursor(Cursor cursor, SettingOtomate model) {
    int indexid = cursor.getColumnIndex("id");
    if (indexid != -1 && !cursor.isNull(indexid)) {
      model.id = cursor.getInt(indexid);
    } else {
      model.id = 0;
    }
    int indexKodeProduct = cursor.getColumnIndex("KodeProduct");
    if (indexKodeProduct != -1 && !cursor.isNull(indexKodeProduct)) {
      model.KodeProduct = cursor.getString(indexKodeProduct);
    } else {
      model.KodeProduct = null;
    }
    int indexFloodDefault = cursor.getColumnIndex("FloodDefault");
    if (indexFloodDefault != -1 && !cursor.isNull(indexFloodDefault)) {
      model.FloodDefault = cursor.getString(indexFloodDefault);
    } else {
      model.FloodDefault = null;
    }
    int indexEqDefault = cursor.getColumnIndex("EqDefault");
    if (indexEqDefault != -1 && !cursor.isNull(indexEqDefault)) {
      model.EqDefault = cursor.getString(indexEqDefault);
    } else {
      model.EqDefault = null;
    }
    int indexIsPaket = cursor.getColumnIndex("IsPaket");
    if (indexIsPaket != -1 && !cursor.isNull(indexIsPaket)) {
      model.IsPaket = cursor.getString(indexIsPaket);
    } else {
      model.IsPaket = null;
    }
    int indexSRCCDefault = cursor.getColumnIndex("SRCCDefault");
    if (indexSRCCDefault != -1 && !cursor.isNull(indexSRCCDefault)) {
      model.SRCCDefault = cursor.getString(indexSRCCDefault);
    } else {
      model.SRCCDefault = null;
    }
    int indexTSDefault = cursor.getColumnIndex("TSDefault");
    if (indexTSDefault != -1 && !cursor.isNull(indexTSDefault)) {
      model.TSDefault = cursor.getString(indexTSDefault);
    } else {
      model.TSDefault = null;
    }
    int indexIsChangeable = cursor.getColumnIndex("IsChangeable");
    if (indexIsChangeable != -1 && !cursor.isNull(indexIsChangeable)) {
      model.IsChangeable = cursor.getString(indexIsChangeable);
    } else {
      model.IsChangeable = null;
    }
    int indexBengkelDefault = cursor.getColumnIndex("BengkelDefault");
    if (indexBengkelDefault != -1 && !cursor.isNull(indexBengkelDefault)) {
      model.BengkelDefault = cursor.getString(indexBengkelDefault);
    } else {
      model.BengkelDefault = null;
    }
    int indexIsChangeableBengkel = cursor.getColumnIndex("IsChangeableBengkel");
    if (indexIsChangeableBengkel != -1 && !cursor.isNull(indexIsChangeableBengkel)) {
      model.IsChangeableBengkel = cursor.getString(indexIsChangeableBengkel);
    } else {
      model.IsChangeableBengkel = null;
    }
    int indexLimitTPL = cursor.getColumnIndex("LimitTPL");
    if (indexLimitTPL != -1 && !cursor.isNull(indexLimitTPL)) {
      model.LimitTPL = cursor.getDouble(indexLimitTPL);
    } else {
      model.LimitTPL = 0;
    }
    int indexLimitPA = cursor.getColumnIndex("LimitPA");
    if (indexLimitPA != -1 && !cursor.isNull(indexLimitPA)) {
      model.LimitPA = cursor.getDouble(indexLimitPA);
    } else {
      model.LimitPA = 0;
    }
  }

  @Override
  public final boolean exists(SettingOtomate model, DatabaseWrapper wrapper) {
    return model.id > 0 && new Select(Method.count()).from(SettingOtomate.class).where(getPrimaryConditionClause(model)).count(wrapper) > 0;
  }

  @Override
  public final ConditionGroup getPrimaryConditionClause(SettingOtomate model) {
    ConditionGroup clause = ConditionGroup.clause();
    clause.and(SettingOtomate_Table.id.eq(model.id));return clause;
  }

  @Override
  public final SettingOtomate newInstance() {
    return new SettingOtomate();
  }

  @Override
  public final BaseProperty getProperty(String name) {
    return SettingOtomate_Table.getProperty(name);
  }
}
