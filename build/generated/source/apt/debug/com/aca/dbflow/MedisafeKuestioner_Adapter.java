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
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;

public final class MedisafeKuestioner_Adapter extends ModelAdapter<MedisafeKuestioner> {
  public MedisafeKuestioner_Adapter(DatabaseHolder holder) {
  }

  @Override
  public final Class<MedisafeKuestioner> getModelClass() {
    return MedisafeKuestioner.class;
  }

  public final String getTableName() {
    return "`MedisafeKuestioner`";
  }

  @Override
  public final IProperty[] getAllColumnProperties() {
    return MedisafeKuestioner_Table.getAllColumnProperties();
  }

  @Override
  public final void bindToInsertValues(ContentValues values, MedisafeKuestioner model) {
    if (model.SppaNo != null) {
      values.put(MedisafeKuestioner_Table.SppaNo.getCursorKey(), model.SppaNo);
    } else {
      values.putNull(MedisafeKuestioner_Table.SppaNo.getCursorKey());
    }
    Integer refIsYesA1 = model.IsYesA1 != null ? (java.lang.Integer) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getDBValue((java.lang.Boolean) model.IsYesA1) : null;
    if (refIsYesA1 != null) {
      values.put(MedisafeKuestioner_Table.IsYesA1.getCursorKey(), refIsYesA1);
    } else {
      values.putNull(MedisafeKuestioner_Table.IsYesA1.getCursorKey());
    }
    Integer refIsYesA2 = model.IsYesA2 != null ? (java.lang.Integer) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getDBValue((java.lang.Boolean) model.IsYesA2) : null;
    if (refIsYesA2 != null) {
      values.put(MedisafeKuestioner_Table.IsYesA2.getCursorKey(), refIsYesA2);
    } else {
      values.putNull(MedisafeKuestioner_Table.IsYesA2.getCursorKey());
    }
    Integer refIsYesA3 = model.IsYesA3 != null ? (java.lang.Integer) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getDBValue((java.lang.Boolean) model.IsYesA3) : null;
    if (refIsYesA3 != null) {
      values.put(MedisafeKuestioner_Table.IsYesA3.getCursorKey(), refIsYesA3);
    } else {
      values.putNull(MedisafeKuestioner_Table.IsYesA3.getCursorKey());
    }
    Integer refIsYesA4 = model.IsYesA4 != null ? (java.lang.Integer) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getDBValue((java.lang.Boolean) model.IsYesA4) : null;
    if (refIsYesA4 != null) {
      values.put(MedisafeKuestioner_Table.IsYesA4.getCursorKey(), refIsYesA4);
    } else {
      values.putNull(MedisafeKuestioner_Table.IsYesA4.getCursorKey());
    }
    Integer refIsYesB1 = model.IsYesB1 != null ? (java.lang.Integer) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getDBValue((java.lang.Boolean) model.IsYesB1) : null;
    if (refIsYesB1 != null) {
      values.put(MedisafeKuestioner_Table.IsYesB1.getCursorKey(), refIsYesB1);
    } else {
      values.putNull(MedisafeKuestioner_Table.IsYesB1.getCursorKey());
    }
    Integer refIsYesB2i = model.IsYesB2i != null ? (java.lang.Integer) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getDBValue((java.lang.Boolean) model.IsYesB2i) : null;
    if (refIsYesB2i != null) {
      values.put(MedisafeKuestioner_Table.IsYesB2i.getCursorKey(), refIsYesB2i);
    } else {
      values.putNull(MedisafeKuestioner_Table.IsYesB2i.getCursorKey());
    }
    Integer refIsYesB2ii = model.IsYesB2ii != null ? (java.lang.Integer) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getDBValue((java.lang.Boolean) model.IsYesB2ii) : null;
    if (refIsYesB2ii != null) {
      values.put(MedisafeKuestioner_Table.IsYesB2ii.getCursorKey(), refIsYesB2ii);
    } else {
      values.putNull(MedisafeKuestioner_Table.IsYesB2ii.getCursorKey());
    }
    Integer refIsYesB2iii = model.IsYesB2iii != null ? (java.lang.Integer) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getDBValue((java.lang.Boolean) model.IsYesB2iii) : null;
    if (refIsYesB2iii != null) {
      values.put(MedisafeKuestioner_Table.IsYesB2iii.getCursorKey(), refIsYesB2iii);
    } else {
      values.putNull(MedisafeKuestioner_Table.IsYesB2iii.getCursorKey());
    }
    Integer refIsYesB2iv = model.IsYesB2iv != null ? (java.lang.Integer) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getDBValue((java.lang.Boolean) model.IsYesB2iv) : null;
    if (refIsYesB2iv != null) {
      values.put(MedisafeKuestioner_Table.IsYesB2iv.getCursorKey(), refIsYesB2iv);
    } else {
      values.putNull(MedisafeKuestioner_Table.IsYesB2iv.getCursorKey());
    }
    Integer refIsYesB2v = model.IsYesB2v != null ? (java.lang.Integer) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getDBValue((java.lang.Boolean) model.IsYesB2v) : null;
    if (refIsYesB2v != null) {
      values.put(MedisafeKuestioner_Table.IsYesB2v.getCursorKey(), refIsYesB2v);
    } else {
      values.putNull(MedisafeKuestioner_Table.IsYesB2v.getCursorKey());
    }
    Integer refIsYesB2vi = model.IsYesB2vi != null ? (java.lang.Integer) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getDBValue((java.lang.Boolean) model.IsYesB2vi) : null;
    if (refIsYesB2vi != null) {
      values.put(MedisafeKuestioner_Table.IsYesB2vi.getCursorKey(), refIsYesB2vi);
    } else {
      values.putNull(MedisafeKuestioner_Table.IsYesB2vi.getCursorKey());
    }
    Integer refIsYesB3 = model.IsYesB3 != null ? (java.lang.Integer) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getDBValue((java.lang.Boolean) model.IsYesB3) : null;
    if (refIsYesB3 != null) {
      values.put(MedisafeKuestioner_Table.IsYesB3.getCursorKey(), refIsYesB3);
    } else {
      values.putNull(MedisafeKuestioner_Table.IsYesB3.getCursorKey());
    }
    Integer refIsYesB4 = model.IsYesB4 != null ? (java.lang.Integer) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getDBValue((java.lang.Boolean) model.IsYesB4) : null;
    if (refIsYesB4 != null) {
      values.put(MedisafeKuestioner_Table.IsYesB4.getCursorKey(), refIsYesB4);
    } else {
      values.putNull(MedisafeKuestioner_Table.IsYesB4.getCursorKey());
    }
    Integer refIsYesB5i = model.IsYesB5i != null ? (java.lang.Integer) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getDBValue((java.lang.Boolean) model.IsYesB5i) : null;
    if (refIsYesB5i != null) {
      values.put(MedisafeKuestioner_Table.IsYesB5i.getCursorKey(), refIsYesB5i);
    } else {
      values.putNull(MedisafeKuestioner_Table.IsYesB5i.getCursorKey());
    }
    Integer refIsYesB5ii = model.IsYesB5ii != null ? (java.lang.Integer) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getDBValue((java.lang.Boolean) model.IsYesB5ii) : null;
    if (refIsYesB5ii != null) {
      values.put(MedisafeKuestioner_Table.IsYesB5ii.getCursorKey(), refIsYesB5ii);
    } else {
      values.putNull(MedisafeKuestioner_Table.IsYesB5ii.getCursorKey());
    }
    Integer refIsAgreed = model.IsAgreed != null ? (java.lang.Integer) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getDBValue((java.lang.Boolean) model.IsAgreed) : null;
    if (refIsAgreed != null) {
      values.put(MedisafeKuestioner_Table.IsAgreed.getCursorKey(), refIsAgreed);
    } else {
      values.putNull(MedisafeKuestioner_Table.IsAgreed.getCursorKey());
    }
    if (model.NamaPerusahaan1 != null) {
      values.put(MedisafeKuestioner_Table.NamaPerusahaan1.getCursorKey(), model.NamaPerusahaan1);
    } else {
      values.putNull(MedisafeKuestioner_Table.NamaPerusahaan1.getCursorKey());
    }
    if (model.NamaPerusahaan2 != null) {
      values.put(MedisafeKuestioner_Table.NamaPerusahaan2.getCursorKey(), model.NamaPerusahaan2);
    } else {
      values.putNull(MedisafeKuestioner_Table.NamaPerusahaan2.getCursorKey());
    }
    if (model.NamaPerusahaan3 != null) {
      values.put(MedisafeKuestioner_Table.NamaPerusahaan3.getCursorKey(), model.NamaPerusahaan3);
    } else {
      values.putNull(MedisafeKuestioner_Table.NamaPerusahaan3.getCursorKey());
    }
    if (model.NamaPerusahaan4 != null) {
      values.put(MedisafeKuestioner_Table.NamaPerusahaan4.getCursorKey(), model.NamaPerusahaan4);
    } else {
      values.putNull(MedisafeKuestioner_Table.NamaPerusahaan4.getCursorKey());
    }
    if (model.NoPolis1 != null) {
      values.put(MedisafeKuestioner_Table.NoPolis1.getCursorKey(), model.NoPolis1);
    } else {
      values.putNull(MedisafeKuestioner_Table.NoPolis1.getCursorKey());
    }
    if (model.NoPolis2 != null) {
      values.put(MedisafeKuestioner_Table.NoPolis2.getCursorKey(), model.NoPolis2);
    } else {
      values.putNull(MedisafeKuestioner_Table.NoPolis2.getCursorKey());
    }
    if (model.NoPolis3 != null) {
      values.put(MedisafeKuestioner_Table.NoPolis3.getCursorKey(), model.NoPolis3);
    } else {
      values.putNull(MedisafeKuestioner_Table.NoPolis3.getCursorKey());
    }
    if (model.NoPolis4 != null) {
      values.put(MedisafeKuestioner_Table.NoPolis4.getCursorKey(), model.NoPolis4);
    } else {
      values.putNull(MedisafeKuestioner_Table.NoPolis4.getCursorKey());
    }
    if (model.KeteranganA2 != null) {
      values.put(MedisafeKuestioner_Table.KeteranganA2.getCursorKey(), model.KeteranganA2);
    } else {
      values.putNull(MedisafeKuestioner_Table.KeteranganA2.getCursorKey());
    }
    if (model.KeteranganA3 != null) {
      values.put(MedisafeKuestioner_Table.KeteranganA3.getCursorKey(), model.KeteranganA3);
    } else {
      values.putNull(MedisafeKuestioner_Table.KeteranganA3.getCursorKey());
    }
    if (model.KeteranganA4 != null) {
      values.put(MedisafeKuestioner_Table.KeteranganA4.getCursorKey(), model.KeteranganA4);
    } else {
      values.putNull(MedisafeKuestioner_Table.KeteranganA4.getCursorKey());
    }
    if (model.KeteranganB1 != null) {
      values.put(MedisafeKuestioner_Table.KeteranganB1.getCursorKey(), model.KeteranganB1);
    } else {
      values.putNull(MedisafeKuestioner_Table.KeteranganB1.getCursorKey());
    }
    if (model.KeteranganB2i != null) {
      values.put(MedisafeKuestioner_Table.KeteranganB2i.getCursorKey(), model.KeteranganB2i);
    } else {
      values.putNull(MedisafeKuestioner_Table.KeteranganB2i.getCursorKey());
    }
    if (model.KeteranganB2ii != null) {
      values.put(MedisafeKuestioner_Table.KeteranganB2ii.getCursorKey(), model.KeteranganB2ii);
    } else {
      values.putNull(MedisafeKuestioner_Table.KeteranganB2ii.getCursorKey());
    }
    if (model.KeteranganB2iii != null) {
      values.put(MedisafeKuestioner_Table.KeteranganB2iii.getCursorKey(), model.KeteranganB2iii);
    } else {
      values.putNull(MedisafeKuestioner_Table.KeteranganB2iii.getCursorKey());
    }
    if (model.KeteranganB2iv != null) {
      values.put(MedisafeKuestioner_Table.KeteranganB2iv.getCursorKey(), model.KeteranganB2iv);
    } else {
      values.putNull(MedisafeKuestioner_Table.KeteranganB2iv.getCursorKey());
    }
    if (model.KeteranganB2v != null) {
      values.put(MedisafeKuestioner_Table.KeteranganB2v.getCursorKey(), model.KeteranganB2v);
    } else {
      values.putNull(MedisafeKuestioner_Table.KeteranganB2v.getCursorKey());
    }
    if (model.KeteranganB2vi != null) {
      values.put(MedisafeKuestioner_Table.KeteranganB2vi.getCursorKey(), model.KeteranganB2vi);
    } else {
      values.putNull(MedisafeKuestioner_Table.KeteranganB2vi.getCursorKey());
    }
    if (model.KeteranganB31 != null) {
      values.put(MedisafeKuestioner_Table.KeteranganB31.getCursorKey(), model.KeteranganB31);
    } else {
      values.putNull(MedisafeKuestioner_Table.KeteranganB31.getCursorKey());
    }
    if (model.KeteranganB32 != null) {
      values.put(MedisafeKuestioner_Table.KeteranganB32.getCursorKey(), model.KeteranganB32);
    } else {
      values.putNull(MedisafeKuestioner_Table.KeteranganB32.getCursorKey());
    }
    if (model.KeteranganB33 != null) {
      values.put(MedisafeKuestioner_Table.KeteranganB33.getCursorKey(), model.KeteranganB33);
    } else {
      values.putNull(MedisafeKuestioner_Table.KeteranganB33.getCursorKey());
    }
    if (model.KeteranganB34 != null) {
      values.put(MedisafeKuestioner_Table.KeteranganB34.getCursorKey(), model.KeteranganB34);
    } else {
      values.putNull(MedisafeKuestioner_Table.KeteranganB34.getCursorKey());
    }
    if (model.KeteranganB4 != null) {
      values.put(MedisafeKuestioner_Table.KeteranganB4.getCursorKey(), model.KeteranganB4);
    } else {
      values.putNull(MedisafeKuestioner_Table.KeteranganB4.getCursorKey());
    }
    if (model.KeteranganB5i != null) {
      values.put(MedisafeKuestioner_Table.KeteranganB5i.getCursorKey(), model.KeteranganB5i);
    } else {
      values.putNull(MedisafeKuestioner_Table.KeteranganB5i.getCursorKey());
    }
    if (model.KeteranganB5ii != null) {
      values.put(MedisafeKuestioner_Table.KeteranganB5ii.getCursorKey(), model.KeteranganB5ii);
    } else {
      values.putNull(MedisafeKuestioner_Table.KeteranganB5ii.getCursorKey());
    }
  }

  @Override
  public final void bindToContentValues(ContentValues values, MedisafeKuestioner model) {
    bindToInsertValues(values, model);
  }

  @Override
  public final void bindToInsertStatement(DatabaseStatement statement, MedisafeKuestioner model, int start) {
    if (model.SppaNo != null) {
      statement.bindString(1 + start, model.SppaNo);
    } else {
      statement.bindNull(1 + start);
    }
    Integer refIsYesA1 = model.IsYesA1 != null ? (java.lang.Integer) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getDBValue((java.lang.Boolean) model.IsYesA1) : null;
    if (refIsYesA1 != null) {
      statement.bindLong(2 + start, refIsYesA1);
    } else {
      statement.bindNull(2 + start);
    }
    Integer refIsYesA2 = model.IsYesA2 != null ? (java.lang.Integer) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getDBValue((java.lang.Boolean) model.IsYesA2) : null;
    if (refIsYesA2 != null) {
      statement.bindLong(3 + start, refIsYesA2);
    } else {
      statement.bindNull(3 + start);
    }
    Integer refIsYesA3 = model.IsYesA3 != null ? (java.lang.Integer) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getDBValue((java.lang.Boolean) model.IsYesA3) : null;
    if (refIsYesA3 != null) {
      statement.bindLong(4 + start, refIsYesA3);
    } else {
      statement.bindNull(4 + start);
    }
    Integer refIsYesA4 = model.IsYesA4 != null ? (java.lang.Integer) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getDBValue((java.lang.Boolean) model.IsYesA4) : null;
    if (refIsYesA4 != null) {
      statement.bindLong(5 + start, refIsYesA4);
    } else {
      statement.bindNull(5 + start);
    }
    Integer refIsYesB1 = model.IsYesB1 != null ? (java.lang.Integer) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getDBValue((java.lang.Boolean) model.IsYesB1) : null;
    if (refIsYesB1 != null) {
      statement.bindLong(6 + start, refIsYesB1);
    } else {
      statement.bindNull(6 + start);
    }
    Integer refIsYesB2i = model.IsYesB2i != null ? (java.lang.Integer) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getDBValue((java.lang.Boolean) model.IsYesB2i) : null;
    if (refIsYesB2i != null) {
      statement.bindLong(7 + start, refIsYesB2i);
    } else {
      statement.bindNull(7 + start);
    }
    Integer refIsYesB2ii = model.IsYesB2ii != null ? (java.lang.Integer) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getDBValue((java.lang.Boolean) model.IsYesB2ii) : null;
    if (refIsYesB2ii != null) {
      statement.bindLong(8 + start, refIsYesB2ii);
    } else {
      statement.bindNull(8 + start);
    }
    Integer refIsYesB2iii = model.IsYesB2iii != null ? (java.lang.Integer) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getDBValue((java.lang.Boolean) model.IsYesB2iii) : null;
    if (refIsYesB2iii != null) {
      statement.bindLong(9 + start, refIsYesB2iii);
    } else {
      statement.bindNull(9 + start);
    }
    Integer refIsYesB2iv = model.IsYesB2iv != null ? (java.lang.Integer) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getDBValue((java.lang.Boolean) model.IsYesB2iv) : null;
    if (refIsYesB2iv != null) {
      statement.bindLong(10 + start, refIsYesB2iv);
    } else {
      statement.bindNull(10 + start);
    }
    Integer refIsYesB2v = model.IsYesB2v != null ? (java.lang.Integer) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getDBValue((java.lang.Boolean) model.IsYesB2v) : null;
    if (refIsYesB2v != null) {
      statement.bindLong(11 + start, refIsYesB2v);
    } else {
      statement.bindNull(11 + start);
    }
    Integer refIsYesB2vi = model.IsYesB2vi != null ? (java.lang.Integer) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getDBValue((java.lang.Boolean) model.IsYesB2vi) : null;
    if (refIsYesB2vi != null) {
      statement.bindLong(12 + start, refIsYesB2vi);
    } else {
      statement.bindNull(12 + start);
    }
    Integer refIsYesB3 = model.IsYesB3 != null ? (java.lang.Integer) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getDBValue((java.lang.Boolean) model.IsYesB3) : null;
    if (refIsYesB3 != null) {
      statement.bindLong(13 + start, refIsYesB3);
    } else {
      statement.bindNull(13 + start);
    }
    Integer refIsYesB4 = model.IsYesB4 != null ? (java.lang.Integer) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getDBValue((java.lang.Boolean) model.IsYesB4) : null;
    if (refIsYesB4 != null) {
      statement.bindLong(14 + start, refIsYesB4);
    } else {
      statement.bindNull(14 + start);
    }
    Integer refIsYesB5i = model.IsYesB5i != null ? (java.lang.Integer) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getDBValue((java.lang.Boolean) model.IsYesB5i) : null;
    if (refIsYesB5i != null) {
      statement.bindLong(15 + start, refIsYesB5i);
    } else {
      statement.bindNull(15 + start);
    }
    Integer refIsYesB5ii = model.IsYesB5ii != null ? (java.lang.Integer) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getDBValue((java.lang.Boolean) model.IsYesB5ii) : null;
    if (refIsYesB5ii != null) {
      statement.bindLong(16 + start, refIsYesB5ii);
    } else {
      statement.bindNull(16 + start);
    }
    Integer refIsAgreed = model.IsAgreed != null ? (java.lang.Integer) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getDBValue((java.lang.Boolean) model.IsAgreed) : null;
    if (refIsAgreed != null) {
      statement.bindLong(17 + start, refIsAgreed);
    } else {
      statement.bindNull(17 + start);
    }
    if (model.NamaPerusahaan1 != null) {
      statement.bindString(18 + start, model.NamaPerusahaan1);
    } else {
      statement.bindNull(18 + start);
    }
    if (model.NamaPerusahaan2 != null) {
      statement.bindString(19 + start, model.NamaPerusahaan2);
    } else {
      statement.bindNull(19 + start);
    }
    if (model.NamaPerusahaan3 != null) {
      statement.bindString(20 + start, model.NamaPerusahaan3);
    } else {
      statement.bindNull(20 + start);
    }
    if (model.NamaPerusahaan4 != null) {
      statement.bindString(21 + start, model.NamaPerusahaan4);
    } else {
      statement.bindNull(21 + start);
    }
    if (model.NoPolis1 != null) {
      statement.bindString(22 + start, model.NoPolis1);
    } else {
      statement.bindNull(22 + start);
    }
    if (model.NoPolis2 != null) {
      statement.bindString(23 + start, model.NoPolis2);
    } else {
      statement.bindNull(23 + start);
    }
    if (model.NoPolis3 != null) {
      statement.bindString(24 + start, model.NoPolis3);
    } else {
      statement.bindNull(24 + start);
    }
    if (model.NoPolis4 != null) {
      statement.bindString(25 + start, model.NoPolis4);
    } else {
      statement.bindNull(25 + start);
    }
    if (model.KeteranganA2 != null) {
      statement.bindString(26 + start, model.KeteranganA2);
    } else {
      statement.bindNull(26 + start);
    }
    if (model.KeteranganA3 != null) {
      statement.bindString(27 + start, model.KeteranganA3);
    } else {
      statement.bindNull(27 + start);
    }
    if (model.KeteranganA4 != null) {
      statement.bindString(28 + start, model.KeteranganA4);
    } else {
      statement.bindNull(28 + start);
    }
    if (model.KeteranganB1 != null) {
      statement.bindString(29 + start, model.KeteranganB1);
    } else {
      statement.bindNull(29 + start);
    }
    if (model.KeteranganB2i != null) {
      statement.bindString(30 + start, model.KeteranganB2i);
    } else {
      statement.bindNull(30 + start);
    }
    if (model.KeteranganB2ii != null) {
      statement.bindString(31 + start, model.KeteranganB2ii);
    } else {
      statement.bindNull(31 + start);
    }
    if (model.KeteranganB2iii != null) {
      statement.bindString(32 + start, model.KeteranganB2iii);
    } else {
      statement.bindNull(32 + start);
    }
    if (model.KeteranganB2iv != null) {
      statement.bindString(33 + start, model.KeteranganB2iv);
    } else {
      statement.bindNull(33 + start);
    }
    if (model.KeteranganB2v != null) {
      statement.bindString(34 + start, model.KeteranganB2v);
    } else {
      statement.bindNull(34 + start);
    }
    if (model.KeteranganB2vi != null) {
      statement.bindString(35 + start, model.KeteranganB2vi);
    } else {
      statement.bindNull(35 + start);
    }
    if (model.KeteranganB31 != null) {
      statement.bindString(36 + start, model.KeteranganB31);
    } else {
      statement.bindNull(36 + start);
    }
    if (model.KeteranganB32 != null) {
      statement.bindString(37 + start, model.KeteranganB32);
    } else {
      statement.bindNull(37 + start);
    }
    if (model.KeteranganB33 != null) {
      statement.bindString(38 + start, model.KeteranganB33);
    } else {
      statement.bindNull(38 + start);
    }
    if (model.KeteranganB34 != null) {
      statement.bindString(39 + start, model.KeteranganB34);
    } else {
      statement.bindNull(39 + start);
    }
    if (model.KeteranganB4 != null) {
      statement.bindString(40 + start, model.KeteranganB4);
    } else {
      statement.bindNull(40 + start);
    }
    if (model.KeteranganB5i != null) {
      statement.bindString(41 + start, model.KeteranganB5i);
    } else {
      statement.bindNull(41 + start);
    }
    if (model.KeteranganB5ii != null) {
      statement.bindString(42 + start, model.KeteranganB5ii);
    } else {
      statement.bindNull(42 + start);
    }
  }

  @Override
  public final void bindToStatement(DatabaseStatement statement, MedisafeKuestioner model) {
    bindToInsertStatement(statement, model, 0);
  }

  @Override
  public final String getInsertStatementQuery() {
    return "INSERT INTO `MedisafeKuestioner`(`SppaNo`,`IsYesA1`,`IsYesA2`,`IsYesA3`,`IsYesA4`,`IsYesB1`,`IsYesB2i`,`IsYesB2ii`,`IsYesB2iii`,`IsYesB2iv`,`IsYesB2v`,`IsYesB2vi`,`IsYesB3`,`IsYesB4`,`IsYesB5i`,`IsYesB5ii`,`IsAgreed`,`NamaPerusahaan1`,`NamaPerusahaan2`,`NamaPerusahaan3`,`NamaPerusahaan4`,`NoPolis1`,`NoPolis2`,`NoPolis3`,`NoPolis4`,`KeteranganA2`,`KeteranganA3`,`KeteranganA4`,`KeteranganB1`,`KeteranganB2i`,`KeteranganB2ii`,`KeteranganB2iii`,`KeteranganB2iv`,`KeteranganB2v`,`KeteranganB2vi`,`KeteranganB31`,`KeteranganB32`,`KeteranganB33`,`KeteranganB34`,`KeteranganB4`,`KeteranganB5i`,`KeteranganB5ii`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
  }

  @Override
  public final String getCompiledStatementQuery() {
    return "INSERT INTO `MedisafeKuestioner`(`SppaNo`,`IsYesA1`,`IsYesA2`,`IsYesA3`,`IsYesA4`,`IsYesB1`,`IsYesB2i`,`IsYesB2ii`,`IsYesB2iii`,`IsYesB2iv`,`IsYesB2v`,`IsYesB2vi`,`IsYesB3`,`IsYesB4`,`IsYesB5i`,`IsYesB5ii`,`IsAgreed`,`NamaPerusahaan1`,`NamaPerusahaan2`,`NamaPerusahaan3`,`NamaPerusahaan4`,`NoPolis1`,`NoPolis2`,`NoPolis3`,`NoPolis4`,`KeteranganA2`,`KeteranganA3`,`KeteranganA4`,`KeteranganB1`,`KeteranganB2i`,`KeteranganB2ii`,`KeteranganB2iii`,`KeteranganB2iv`,`KeteranganB2v`,`KeteranganB2vi`,`KeteranganB31`,`KeteranganB32`,`KeteranganB33`,`KeteranganB34`,`KeteranganB4`,`KeteranganB5i`,`KeteranganB5ii`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
  }

  @Override
  public final String getCreationQuery() {
    return "CREATE TABLE IF NOT EXISTS `MedisafeKuestioner`(`SppaNo` TEXT,`IsYesA1` INTEGER,`IsYesA2` INTEGER,`IsYesA3` INTEGER,`IsYesA4` INTEGER,`IsYesB1` INTEGER,`IsYesB2i` INTEGER,`IsYesB2ii` INTEGER,`IsYesB2iii` INTEGER,`IsYesB2iv` INTEGER,`IsYesB2v` INTEGER,`IsYesB2vi` INTEGER,`IsYesB3` INTEGER,`IsYesB4` INTEGER,`IsYesB5i` INTEGER,`IsYesB5ii` INTEGER,`IsAgreed` INTEGER,`NamaPerusahaan1` TEXT,`NamaPerusahaan2` TEXT,`NamaPerusahaan3` TEXT,`NamaPerusahaan4` TEXT,`NoPolis1` TEXT,`NoPolis2` TEXT,`NoPolis3` TEXT,`NoPolis4` TEXT,`KeteranganA2` TEXT,`KeteranganA3` TEXT,`KeteranganA4` TEXT,`KeteranganB1` TEXT,`KeteranganB2i` TEXT,`KeteranganB2ii` TEXT,`KeteranganB2iii` TEXT,`KeteranganB2iv` TEXT,`KeteranganB2v` TEXT,`KeteranganB2vi` TEXT,`KeteranganB31` TEXT,`KeteranganB32` TEXT,`KeteranganB33` TEXT,`KeteranganB34` TEXT,`KeteranganB4` TEXT,`KeteranganB5i` TEXT,`KeteranganB5ii` TEXT, PRIMARY KEY(`SppaNo`)" + ");";
  }

  @Override
  public final void loadFromCursor(Cursor cursor, MedisafeKuestioner model) {
    int indexSppaNo = cursor.getColumnIndex("SppaNo");
    if (indexSppaNo != -1 && !cursor.isNull(indexSppaNo)) {
      model.SppaNo = cursor.getString(indexSppaNo);
    } else {
      model.SppaNo = null;
    }
    int indexIsYesA1 = cursor.getColumnIndex("IsYesA1");
    if (indexIsYesA1 != -1 && !cursor.isNull(indexIsYesA1)) {
      model.IsYesA1 = (java.lang.Boolean) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getModelValue(cursor.getInt(indexIsYesA1));
    } else {
      model.IsYesA1 = null;
    }
    int indexIsYesA2 = cursor.getColumnIndex("IsYesA2");
    if (indexIsYesA2 != -1 && !cursor.isNull(indexIsYesA2)) {
      model.IsYesA2 = (java.lang.Boolean) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getModelValue(cursor.getInt(indexIsYesA2));
    } else {
      model.IsYesA2 = null;
    }
    int indexIsYesA3 = cursor.getColumnIndex("IsYesA3");
    if (indexIsYesA3 != -1 && !cursor.isNull(indexIsYesA3)) {
      model.IsYesA3 = (java.lang.Boolean) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getModelValue(cursor.getInt(indexIsYesA3));
    } else {
      model.IsYesA3 = null;
    }
    int indexIsYesA4 = cursor.getColumnIndex("IsYesA4");
    if (indexIsYesA4 != -1 && !cursor.isNull(indexIsYesA4)) {
      model.IsYesA4 = (java.lang.Boolean) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getModelValue(cursor.getInt(indexIsYesA4));
    } else {
      model.IsYesA4 = null;
    }
    int indexIsYesB1 = cursor.getColumnIndex("IsYesB1");
    if (indexIsYesB1 != -1 && !cursor.isNull(indexIsYesB1)) {
      model.IsYesB1 = (java.lang.Boolean) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getModelValue(cursor.getInt(indexIsYesB1));
    } else {
      model.IsYesB1 = null;
    }
    int indexIsYesB2i = cursor.getColumnIndex("IsYesB2i");
    if (indexIsYesB2i != -1 && !cursor.isNull(indexIsYesB2i)) {
      model.IsYesB2i = (java.lang.Boolean) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getModelValue(cursor.getInt(indexIsYesB2i));
    } else {
      model.IsYesB2i = null;
    }
    int indexIsYesB2ii = cursor.getColumnIndex("IsYesB2ii");
    if (indexIsYesB2ii != -1 && !cursor.isNull(indexIsYesB2ii)) {
      model.IsYesB2ii = (java.lang.Boolean) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getModelValue(cursor.getInt(indexIsYesB2ii));
    } else {
      model.IsYesB2ii = null;
    }
    int indexIsYesB2iii = cursor.getColumnIndex("IsYesB2iii");
    if (indexIsYesB2iii != -1 && !cursor.isNull(indexIsYesB2iii)) {
      model.IsYesB2iii = (java.lang.Boolean) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getModelValue(cursor.getInt(indexIsYesB2iii));
    } else {
      model.IsYesB2iii = null;
    }
    int indexIsYesB2iv = cursor.getColumnIndex("IsYesB2iv");
    if (indexIsYesB2iv != -1 && !cursor.isNull(indexIsYesB2iv)) {
      model.IsYesB2iv = (java.lang.Boolean) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getModelValue(cursor.getInt(indexIsYesB2iv));
    } else {
      model.IsYesB2iv = null;
    }
    int indexIsYesB2v = cursor.getColumnIndex("IsYesB2v");
    if (indexIsYesB2v != -1 && !cursor.isNull(indexIsYesB2v)) {
      model.IsYesB2v = (java.lang.Boolean) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getModelValue(cursor.getInt(indexIsYesB2v));
    } else {
      model.IsYesB2v = null;
    }
    int indexIsYesB2vi = cursor.getColumnIndex("IsYesB2vi");
    if (indexIsYesB2vi != -1 && !cursor.isNull(indexIsYesB2vi)) {
      model.IsYesB2vi = (java.lang.Boolean) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getModelValue(cursor.getInt(indexIsYesB2vi));
    } else {
      model.IsYesB2vi = null;
    }
    int indexIsYesB3 = cursor.getColumnIndex("IsYesB3");
    if (indexIsYesB3 != -1 && !cursor.isNull(indexIsYesB3)) {
      model.IsYesB3 = (java.lang.Boolean) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getModelValue(cursor.getInt(indexIsYesB3));
    } else {
      model.IsYesB3 = null;
    }
    int indexIsYesB4 = cursor.getColumnIndex("IsYesB4");
    if (indexIsYesB4 != -1 && !cursor.isNull(indexIsYesB4)) {
      model.IsYesB4 = (java.lang.Boolean) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getModelValue(cursor.getInt(indexIsYesB4));
    } else {
      model.IsYesB4 = null;
    }
    int indexIsYesB5i = cursor.getColumnIndex("IsYesB5i");
    if (indexIsYesB5i != -1 && !cursor.isNull(indexIsYesB5i)) {
      model.IsYesB5i = (java.lang.Boolean) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getModelValue(cursor.getInt(indexIsYesB5i));
    } else {
      model.IsYesB5i = null;
    }
    int indexIsYesB5ii = cursor.getColumnIndex("IsYesB5ii");
    if (indexIsYesB5ii != -1 && !cursor.isNull(indexIsYesB5ii)) {
      model.IsYesB5ii = (java.lang.Boolean) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getModelValue(cursor.getInt(indexIsYesB5ii));
    } else {
      model.IsYesB5ii = null;
    }
    int indexIsAgreed = cursor.getColumnIndex("IsAgreed");
    if (indexIsAgreed != -1 && !cursor.isNull(indexIsAgreed)) {
      model.IsAgreed = (java.lang.Boolean) com.raizlabs.android.dbflow.config.FlowManager.getTypeConverterForClass(java.lang.Boolean.class).getModelValue(cursor.getInt(indexIsAgreed));
    } else {
      model.IsAgreed = null;
    }
    int indexNamaPerusahaan1 = cursor.getColumnIndex("NamaPerusahaan1");
    if (indexNamaPerusahaan1 != -1 && !cursor.isNull(indexNamaPerusahaan1)) {
      model.NamaPerusahaan1 = cursor.getString(indexNamaPerusahaan1);
    } else {
      model.NamaPerusahaan1 = null;
    }
    int indexNamaPerusahaan2 = cursor.getColumnIndex("NamaPerusahaan2");
    if (indexNamaPerusahaan2 != -1 && !cursor.isNull(indexNamaPerusahaan2)) {
      model.NamaPerusahaan2 = cursor.getString(indexNamaPerusahaan2);
    } else {
      model.NamaPerusahaan2 = null;
    }
    int indexNamaPerusahaan3 = cursor.getColumnIndex("NamaPerusahaan3");
    if (indexNamaPerusahaan3 != -1 && !cursor.isNull(indexNamaPerusahaan3)) {
      model.NamaPerusahaan3 = cursor.getString(indexNamaPerusahaan3);
    } else {
      model.NamaPerusahaan3 = null;
    }
    int indexNamaPerusahaan4 = cursor.getColumnIndex("NamaPerusahaan4");
    if (indexNamaPerusahaan4 != -1 && !cursor.isNull(indexNamaPerusahaan4)) {
      model.NamaPerusahaan4 = cursor.getString(indexNamaPerusahaan4);
    } else {
      model.NamaPerusahaan4 = null;
    }
    int indexNoPolis1 = cursor.getColumnIndex("NoPolis1");
    if (indexNoPolis1 != -1 && !cursor.isNull(indexNoPolis1)) {
      model.NoPolis1 = cursor.getString(indexNoPolis1);
    } else {
      model.NoPolis1 = null;
    }
    int indexNoPolis2 = cursor.getColumnIndex("NoPolis2");
    if (indexNoPolis2 != -1 && !cursor.isNull(indexNoPolis2)) {
      model.NoPolis2 = cursor.getString(indexNoPolis2);
    } else {
      model.NoPolis2 = null;
    }
    int indexNoPolis3 = cursor.getColumnIndex("NoPolis3");
    if (indexNoPolis3 != -1 && !cursor.isNull(indexNoPolis3)) {
      model.NoPolis3 = cursor.getString(indexNoPolis3);
    } else {
      model.NoPolis3 = null;
    }
    int indexNoPolis4 = cursor.getColumnIndex("NoPolis4");
    if (indexNoPolis4 != -1 && !cursor.isNull(indexNoPolis4)) {
      model.NoPolis4 = cursor.getString(indexNoPolis4);
    } else {
      model.NoPolis4 = null;
    }
    int indexKeteranganA2 = cursor.getColumnIndex("KeteranganA2");
    if (indexKeteranganA2 != -1 && !cursor.isNull(indexKeteranganA2)) {
      model.KeteranganA2 = cursor.getString(indexKeteranganA2);
    } else {
      model.KeteranganA2 = null;
    }
    int indexKeteranganA3 = cursor.getColumnIndex("KeteranganA3");
    if (indexKeteranganA3 != -1 && !cursor.isNull(indexKeteranganA3)) {
      model.KeteranganA3 = cursor.getString(indexKeteranganA3);
    } else {
      model.KeteranganA3 = null;
    }
    int indexKeteranganA4 = cursor.getColumnIndex("KeteranganA4");
    if (indexKeteranganA4 != -1 && !cursor.isNull(indexKeteranganA4)) {
      model.KeteranganA4 = cursor.getString(indexKeteranganA4);
    } else {
      model.KeteranganA4 = null;
    }
    int indexKeteranganB1 = cursor.getColumnIndex("KeteranganB1");
    if (indexKeteranganB1 != -1 && !cursor.isNull(indexKeteranganB1)) {
      model.KeteranganB1 = cursor.getString(indexKeteranganB1);
    } else {
      model.KeteranganB1 = null;
    }
    int indexKeteranganB2i = cursor.getColumnIndex("KeteranganB2i");
    if (indexKeteranganB2i != -1 && !cursor.isNull(indexKeteranganB2i)) {
      model.KeteranganB2i = cursor.getString(indexKeteranganB2i);
    } else {
      model.KeteranganB2i = null;
    }
    int indexKeteranganB2ii = cursor.getColumnIndex("KeteranganB2ii");
    if (indexKeteranganB2ii != -1 && !cursor.isNull(indexKeteranganB2ii)) {
      model.KeteranganB2ii = cursor.getString(indexKeteranganB2ii);
    } else {
      model.KeteranganB2ii = null;
    }
    int indexKeteranganB2iii = cursor.getColumnIndex("KeteranganB2iii");
    if (indexKeteranganB2iii != -1 && !cursor.isNull(indexKeteranganB2iii)) {
      model.KeteranganB2iii = cursor.getString(indexKeteranganB2iii);
    } else {
      model.KeteranganB2iii = null;
    }
    int indexKeteranganB2iv = cursor.getColumnIndex("KeteranganB2iv");
    if (indexKeteranganB2iv != -1 && !cursor.isNull(indexKeteranganB2iv)) {
      model.KeteranganB2iv = cursor.getString(indexKeteranganB2iv);
    } else {
      model.KeteranganB2iv = null;
    }
    int indexKeteranganB2v = cursor.getColumnIndex("KeteranganB2v");
    if (indexKeteranganB2v != -1 && !cursor.isNull(indexKeteranganB2v)) {
      model.KeteranganB2v = cursor.getString(indexKeteranganB2v);
    } else {
      model.KeteranganB2v = null;
    }
    int indexKeteranganB2vi = cursor.getColumnIndex("KeteranganB2vi");
    if (indexKeteranganB2vi != -1 && !cursor.isNull(indexKeteranganB2vi)) {
      model.KeteranganB2vi = cursor.getString(indexKeteranganB2vi);
    } else {
      model.KeteranganB2vi = null;
    }
    int indexKeteranganB31 = cursor.getColumnIndex("KeteranganB31");
    if (indexKeteranganB31 != -1 && !cursor.isNull(indexKeteranganB31)) {
      model.KeteranganB31 = cursor.getString(indexKeteranganB31);
    } else {
      model.KeteranganB31 = null;
    }
    int indexKeteranganB32 = cursor.getColumnIndex("KeteranganB32");
    if (indexKeteranganB32 != -1 && !cursor.isNull(indexKeteranganB32)) {
      model.KeteranganB32 = cursor.getString(indexKeteranganB32);
    } else {
      model.KeteranganB32 = null;
    }
    int indexKeteranganB33 = cursor.getColumnIndex("KeteranganB33");
    if (indexKeteranganB33 != -1 && !cursor.isNull(indexKeteranganB33)) {
      model.KeteranganB33 = cursor.getString(indexKeteranganB33);
    } else {
      model.KeteranganB33 = null;
    }
    int indexKeteranganB34 = cursor.getColumnIndex("KeteranganB34");
    if (indexKeteranganB34 != -1 && !cursor.isNull(indexKeteranganB34)) {
      model.KeteranganB34 = cursor.getString(indexKeteranganB34);
    } else {
      model.KeteranganB34 = null;
    }
    int indexKeteranganB4 = cursor.getColumnIndex("KeteranganB4");
    if (indexKeteranganB4 != -1 && !cursor.isNull(indexKeteranganB4)) {
      model.KeteranganB4 = cursor.getString(indexKeteranganB4);
    } else {
      model.KeteranganB4 = null;
    }
    int indexKeteranganB5i = cursor.getColumnIndex("KeteranganB5i");
    if (indexKeteranganB5i != -1 && !cursor.isNull(indexKeteranganB5i)) {
      model.KeteranganB5i = cursor.getString(indexKeteranganB5i);
    } else {
      model.KeteranganB5i = null;
    }
    int indexKeteranganB5ii = cursor.getColumnIndex("KeteranganB5ii");
    if (indexKeteranganB5ii != -1 && !cursor.isNull(indexKeteranganB5ii)) {
      model.KeteranganB5ii = cursor.getString(indexKeteranganB5ii);
    } else {
      model.KeteranganB5ii = null;
    }
  }

  @Override
  public final boolean exists(MedisafeKuestioner model, DatabaseWrapper wrapper) {
    return new Select(Method.count()).from(MedisafeKuestioner.class).where(getPrimaryConditionClause(model)).count(wrapper) > 0;
  }

  @Override
  public final ConditionGroup getPrimaryConditionClause(MedisafeKuestioner model) {
    ConditionGroup clause = ConditionGroup.clause();
    clause.and(MedisafeKuestioner_Table.SppaNo.eq(model.SppaNo));return clause;
  }

  @Override
  public final MedisafeKuestioner newInstance() {
    return new MedisafeKuestioner();
  }

  @Override
  public final BaseProperty getProperty(String name) {
    return MedisafeKuestioner_Table.getProperty(name);
  }
}
