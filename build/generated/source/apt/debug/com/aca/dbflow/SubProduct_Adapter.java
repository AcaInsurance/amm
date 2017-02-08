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

public final class SubProduct_Adapter extends ModelAdapter<SubProduct> {
  public SubProduct_Adapter(DatabaseHolder holder) {
  }

  @Override
  public final Class<SubProduct> getModelClass() {
    return SubProduct.class;
  }

  public final String getTableName() {
    return "`SubProduct`";
  }

  @Override
  public final IProperty[] getAllColumnProperties() {
    return SubProduct_Table.getAllColumnProperties();
  }

  @Override
  public final void bindToInsertValues(ContentValues values, SubProduct model) {
    if (model.SubProductCode != null) {
      values.put(SubProduct_Table.SubProductCode.getCursorKey(), model.SubProductCode);
    } else {
      values.putNull(SubProduct_Table.SubProductCode.getCursorKey());
    }
    if (model.Description != null) {
      values.put(SubProduct_Table.Description.getCursorKey(), model.Description);
    } else {
      values.putNull(SubProduct_Table.Description.getCursorKey());
    }
    if (model.ProductCode != null) {
      values.put(SubProduct_Table.ProductCode.getCursorKey(), model.ProductCode);
    } else {
      values.putNull(SubProduct_Table.ProductCode.getCursorKey());
    }
    if (model.OrderNo != null) {
      values.put(SubProduct_Table.OrderNo.getCursorKey(), model.OrderNo);
    } else {
      values.putNull(SubProduct_Table.OrderNo.getCursorKey());
    }
    if (model.IsConventional != null) {
      values.put(SubProduct_Table.IsConventional.getCursorKey(), model.IsConventional);
    } else {
      values.putNull(SubProduct_Table.IsConventional.getCursorKey());
    }
    if (model.Url != null) {
      values.put(SubProduct_Table.Url.getCursorKey(), model.Url);
    } else {
      values.putNull(SubProduct_Table.Url.getCursorKey());
    }
    if (model.ScheduleName != null) {
      values.put(SubProduct_Table.ScheduleName.getCursorKey(), model.ScheduleName);
    } else {
      values.putNull(SubProduct_Table.ScheduleName.getCursorKey());
    }
    if (model.AttachBenefit != null) {
      values.put(SubProduct_Table.AttachBenefit.getCursorKey(), model.AttachBenefit);
    } else {
      values.putNull(SubProduct_Table.AttachBenefit.getCursorKey());
    }
    if (model.AttachClaim != null) {
      values.put(SubProduct_Table.AttachClaim.getCursorKey(), model.AttachClaim);
    } else {
      values.putNull(SubProduct_Table.AttachClaim.getCursorKey());
    }
    if (model.AttachOthers != null) {
      values.put(SubProduct_Table.AttachOthers.getCursorKey(), model.AttachOthers);
    } else {
      values.putNull(SubProduct_Table.AttachOthers.getCursorKey());
    }
    if (model.WsUrl != null) {
      values.put(SubProduct_Table.WsUrl.getCursorKey(), model.WsUrl);
    } else {
      values.putNull(SubProduct_Table.WsUrl.getCursorKey());
    }
    if (model.IdOrFa != null) {
      values.put(SubProduct_Table.IdOrFa.getCursorKey(), model.IdOrFa);
    } else {
      values.putNull(SubProduct_Table.IdOrFa.getCursorKey());
    }
    if (model.AnnOrReg != null) {
      values.put(SubProduct_Table.AnnOrReg.getCursorKey(), model.AnnOrReg);
    } else {
      values.putNull(SubProduct_Table.AnnOrReg.getCursorKey());
    }
    if (model.IsNeedFlight != null) {
      values.put(SubProduct_Table.IsNeedFlight.getCursorKey(), model.IsNeedFlight);
    } else {
      values.putNull(SubProduct_Table.IsNeedFlight.getCursorKey());
    }
    if (model.MaxAgeFrom != null) {
      values.put(SubProduct_Table.MaxAgeFrom.getCursorKey(), model.MaxAgeFrom);
    } else {
      values.putNull(SubProduct_Table.MaxAgeFrom.getCursorKey());
    }
    if (model.MaxAgeTo != null) {
      values.put(SubProduct_Table.MaxAgeTo.getCursorKey(), model.MaxAgeTo);
    } else {
      values.putNull(SubProduct_Table.MaxAgeTo.getCursorKey());
    }
    if (model.MinAge != null) {
      values.put(SubProduct_Table.MinAge.getCursorKey(), model.MinAge);
    } else {
      values.putNull(SubProduct_Table.MinAge.getCursorKey());
    }
    if (model.MaxDay != null) {
      values.put(SubProduct_Table.MaxDay.getCursorKey(), model.MaxDay);
    } else {
      values.putNull(SubProduct_Table.MaxDay.getCursorKey());
    }
    if (model.MaxDayBda != null) {
      values.put(SubProduct_Table.MaxDayBda.getCursorKey(), model.MaxDayBda);
    } else {
      values.putNull(SubProduct_Table.MaxDayBda.getCursorKey());
    }
    if (model.LoadingPct != null) {
      values.put(SubProduct_Table.LoadingPct.getCursorKey(), model.LoadingPct);
    } else {
      values.putNull(SubProduct_Table.LoadingPct.getCursorKey());
    }
    if (model.Charges != null) {
      values.put(SubProduct_Table.Charges.getCursorKey(), model.Charges);
    } else {
      values.putNull(SubProduct_Table.Charges.getCursorKey());
    }
    if (model.BdaAmount != null) {
      values.put(SubProduct_Table.BdaAmount.getCursorKey(), model.BdaAmount);
    } else {
      values.putNull(SubProduct_Table.BdaAmount.getCursorKey());
    }
    if (model.StampAmount != null) {
      values.put(SubProduct_Table.StampAmount.getCursorKey(), model.StampAmount);
    } else {
      values.putNull(SubProduct_Table.StampAmount.getCursorKey());
    }
    if (model.MaxDayTravel != null) {
      values.put(SubProduct_Table.MaxDayTravel.getCursorKey(), model.MaxDayTravel);
    } else {
      values.putNull(SubProduct_Table.MaxDayTravel.getCursorKey());
    }
    if (model.IsConvertToIdr != null) {
      values.put(SubProduct_Table.IsConvertToIdr.getCursorKey(), model.IsConvertToIdr);
    } else {
      values.putNull(SubProduct_Table.IsConvertToIdr.getCursorKey());
    }
    if (model.MinMember != null) {
      values.put(SubProduct_Table.MinMember.getCursorKey(), model.MinMember);
    } else {
      values.putNull(SubProduct_Table.MinMember.getCursorKey());
    }
    if (model.IsMobile != null) {
      values.put(SubProduct_Table.IsMobile.getCursorKey(), model.IsMobile);
    } else {
      values.putNull(SubProduct_Table.IsMobile.getCursorKey());
    }
    if (model.IsActive != null) {
      values.put(SubProduct_Table.IsActive.getCursorKey(), model.IsActive);
    } else {
      values.putNull(SubProduct_Table.IsActive.getCursorKey());
    }
  }

  @Override
  public final void bindToContentValues(ContentValues values, SubProduct model) {
    bindToInsertValues(values, model);
  }

  @Override
  public final void bindToInsertStatement(DatabaseStatement statement, SubProduct model, int start) {
    if (model.SubProductCode != null) {
      statement.bindString(1 + start, model.SubProductCode);
    } else {
      statement.bindNull(1 + start);
    }
    if (model.Description != null) {
      statement.bindString(2 + start, model.Description);
    } else {
      statement.bindNull(2 + start);
    }
    if (model.ProductCode != null) {
      statement.bindString(3 + start, model.ProductCode);
    } else {
      statement.bindNull(3 + start);
    }
    if (model.OrderNo != null) {
      statement.bindString(4 + start, model.OrderNo);
    } else {
      statement.bindNull(4 + start);
    }
    if (model.IsConventional != null) {
      statement.bindString(5 + start, model.IsConventional);
    } else {
      statement.bindNull(5 + start);
    }
    if (model.Url != null) {
      statement.bindString(6 + start, model.Url);
    } else {
      statement.bindNull(6 + start);
    }
    if (model.ScheduleName != null) {
      statement.bindString(7 + start, model.ScheduleName);
    } else {
      statement.bindNull(7 + start);
    }
    if (model.AttachBenefit != null) {
      statement.bindString(8 + start, model.AttachBenefit);
    } else {
      statement.bindNull(8 + start);
    }
    if (model.AttachClaim != null) {
      statement.bindString(9 + start, model.AttachClaim);
    } else {
      statement.bindNull(9 + start);
    }
    if (model.AttachOthers != null) {
      statement.bindString(10 + start, model.AttachOthers);
    } else {
      statement.bindNull(10 + start);
    }
    if (model.WsUrl != null) {
      statement.bindString(11 + start, model.WsUrl);
    } else {
      statement.bindNull(11 + start);
    }
    if (model.IdOrFa != null) {
      statement.bindString(12 + start, model.IdOrFa);
    } else {
      statement.bindNull(12 + start);
    }
    if (model.AnnOrReg != null) {
      statement.bindString(13 + start, model.AnnOrReg);
    } else {
      statement.bindNull(13 + start);
    }
    if (model.IsNeedFlight != null) {
      statement.bindString(14 + start, model.IsNeedFlight);
    } else {
      statement.bindNull(14 + start);
    }
    if (model.MaxAgeFrom != null) {
      statement.bindString(15 + start, model.MaxAgeFrom);
    } else {
      statement.bindNull(15 + start);
    }
    if (model.MaxAgeTo != null) {
      statement.bindString(16 + start, model.MaxAgeTo);
    } else {
      statement.bindNull(16 + start);
    }
    if (model.MinAge != null) {
      statement.bindString(17 + start, model.MinAge);
    } else {
      statement.bindNull(17 + start);
    }
    if (model.MaxDay != null) {
      statement.bindString(18 + start, model.MaxDay);
    } else {
      statement.bindNull(18 + start);
    }
    if (model.MaxDayBda != null) {
      statement.bindString(19 + start, model.MaxDayBda);
    } else {
      statement.bindNull(19 + start);
    }
    if (model.LoadingPct != null) {
      statement.bindString(20 + start, model.LoadingPct);
    } else {
      statement.bindNull(20 + start);
    }
    if (model.Charges != null) {
      statement.bindString(21 + start, model.Charges);
    } else {
      statement.bindNull(21 + start);
    }
    if (model.BdaAmount != null) {
      statement.bindString(22 + start, model.BdaAmount);
    } else {
      statement.bindNull(22 + start);
    }
    if (model.StampAmount != null) {
      statement.bindString(23 + start, model.StampAmount);
    } else {
      statement.bindNull(23 + start);
    }
    if (model.MaxDayTravel != null) {
      statement.bindString(24 + start, model.MaxDayTravel);
    } else {
      statement.bindNull(24 + start);
    }
    if (model.IsConvertToIdr != null) {
      statement.bindString(25 + start, model.IsConvertToIdr);
    } else {
      statement.bindNull(25 + start);
    }
    if (model.MinMember != null) {
      statement.bindString(26 + start, model.MinMember);
    } else {
      statement.bindNull(26 + start);
    }
    if (model.IsMobile != null) {
      statement.bindString(27 + start, model.IsMobile);
    } else {
      statement.bindNull(27 + start);
    }
    if (model.IsActive != null) {
      statement.bindString(28 + start, model.IsActive);
    } else {
      statement.bindNull(28 + start);
    }
  }

  @Override
  public final void bindToStatement(DatabaseStatement statement, SubProduct model) {
    bindToInsertStatement(statement, model, 0);
  }

  @Override
  public final String getInsertStatementQuery() {
    return "INSERT INTO `SubProduct`(`SubProductCode`,`Description`,`ProductCode`,`OrderNo`,`IsConventional`,`Url`,`ScheduleName`,`AttachBenefit`,`AttachClaim`,`AttachOthers`,`WsUrl`,`IdOrFa`,`AnnOrReg`,`IsNeedFlight`,`MaxAgeFrom`,`MaxAgeTo`,`MinAge`,`MaxDay`,`MaxDayBda`,`LoadingPct`,`Charges`,`BdaAmount`,`StampAmount`,`MaxDayTravel`,`IsConvertToIdr`,`MinMember`,`IsMobile`,`IsActive`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
  }

  @Override
  public final String getCompiledStatementQuery() {
    return "INSERT INTO `SubProduct`(`SubProductCode`,`Description`,`ProductCode`,`OrderNo`,`IsConventional`,`Url`,`ScheduleName`,`AttachBenefit`,`AttachClaim`,`AttachOthers`,`WsUrl`,`IdOrFa`,`AnnOrReg`,`IsNeedFlight`,`MaxAgeFrom`,`MaxAgeTo`,`MinAge`,`MaxDay`,`MaxDayBda`,`LoadingPct`,`Charges`,`BdaAmount`,`StampAmount`,`MaxDayTravel`,`IsConvertToIdr`,`MinMember`,`IsMobile`,`IsActive`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
  }

  @Override
  public final String getCreationQuery() {
    return "CREATE TABLE IF NOT EXISTS `SubProduct`(`SubProductCode` TEXT,`Description` TEXT,`ProductCode` TEXT,`OrderNo` TEXT,`IsConventional` TEXT,`Url` TEXT,`ScheduleName` TEXT,`AttachBenefit` TEXT,`AttachClaim` TEXT,`AttachOthers` TEXT,`WsUrl` TEXT,`IdOrFa` TEXT,`AnnOrReg` TEXT,`IsNeedFlight` TEXT,`MaxAgeFrom` TEXT,`MaxAgeTo` TEXT,`MinAge` TEXT,`MaxDay` TEXT,`MaxDayBda` TEXT,`LoadingPct` TEXT,`Charges` TEXT,`BdaAmount` TEXT,`StampAmount` TEXT,`MaxDayTravel` TEXT,`IsConvertToIdr` TEXT,`MinMember` TEXT,`IsMobile` TEXT,`IsActive` TEXT, PRIMARY KEY(`SubProductCode`)" + ");";
  }

  @Override
  public final void loadFromCursor(Cursor cursor, SubProduct model) {
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
    int indexProductCode = cursor.getColumnIndex("ProductCode");
    if (indexProductCode != -1 && !cursor.isNull(indexProductCode)) {
      model.ProductCode = cursor.getString(indexProductCode);
    } else {
      model.ProductCode = null;
    }
    int indexOrderNo = cursor.getColumnIndex("OrderNo");
    if (indexOrderNo != -1 && !cursor.isNull(indexOrderNo)) {
      model.OrderNo = cursor.getString(indexOrderNo);
    } else {
      model.OrderNo = null;
    }
    int indexIsConventional = cursor.getColumnIndex("IsConventional");
    if (indexIsConventional != -1 && !cursor.isNull(indexIsConventional)) {
      model.IsConventional = cursor.getString(indexIsConventional);
    } else {
      model.IsConventional = null;
    }
    int indexUrl = cursor.getColumnIndex("Url");
    if (indexUrl != -1 && !cursor.isNull(indexUrl)) {
      model.Url = cursor.getString(indexUrl);
    } else {
      model.Url = null;
    }
    int indexScheduleName = cursor.getColumnIndex("ScheduleName");
    if (indexScheduleName != -1 && !cursor.isNull(indexScheduleName)) {
      model.ScheduleName = cursor.getString(indexScheduleName);
    } else {
      model.ScheduleName = null;
    }
    int indexAttachBenefit = cursor.getColumnIndex("AttachBenefit");
    if (indexAttachBenefit != -1 && !cursor.isNull(indexAttachBenefit)) {
      model.AttachBenefit = cursor.getString(indexAttachBenefit);
    } else {
      model.AttachBenefit = null;
    }
    int indexAttachClaim = cursor.getColumnIndex("AttachClaim");
    if (indexAttachClaim != -1 && !cursor.isNull(indexAttachClaim)) {
      model.AttachClaim = cursor.getString(indexAttachClaim);
    } else {
      model.AttachClaim = null;
    }
    int indexAttachOthers = cursor.getColumnIndex("AttachOthers");
    if (indexAttachOthers != -1 && !cursor.isNull(indexAttachOthers)) {
      model.AttachOthers = cursor.getString(indexAttachOthers);
    } else {
      model.AttachOthers = null;
    }
    int indexWsUrl = cursor.getColumnIndex("WsUrl");
    if (indexWsUrl != -1 && !cursor.isNull(indexWsUrl)) {
      model.WsUrl = cursor.getString(indexWsUrl);
    } else {
      model.WsUrl = null;
    }
    int indexIdOrFa = cursor.getColumnIndex("IdOrFa");
    if (indexIdOrFa != -1 && !cursor.isNull(indexIdOrFa)) {
      model.IdOrFa = cursor.getString(indexIdOrFa);
    } else {
      model.IdOrFa = null;
    }
    int indexAnnOrReg = cursor.getColumnIndex("AnnOrReg");
    if (indexAnnOrReg != -1 && !cursor.isNull(indexAnnOrReg)) {
      model.AnnOrReg = cursor.getString(indexAnnOrReg);
    } else {
      model.AnnOrReg = null;
    }
    int indexIsNeedFlight = cursor.getColumnIndex("IsNeedFlight");
    if (indexIsNeedFlight != -1 && !cursor.isNull(indexIsNeedFlight)) {
      model.IsNeedFlight = cursor.getString(indexIsNeedFlight);
    } else {
      model.IsNeedFlight = null;
    }
    int indexMaxAgeFrom = cursor.getColumnIndex("MaxAgeFrom");
    if (indexMaxAgeFrom != -1 && !cursor.isNull(indexMaxAgeFrom)) {
      model.MaxAgeFrom = cursor.getString(indexMaxAgeFrom);
    } else {
      model.MaxAgeFrom = null;
    }
    int indexMaxAgeTo = cursor.getColumnIndex("MaxAgeTo");
    if (indexMaxAgeTo != -1 && !cursor.isNull(indexMaxAgeTo)) {
      model.MaxAgeTo = cursor.getString(indexMaxAgeTo);
    } else {
      model.MaxAgeTo = null;
    }
    int indexMinAge = cursor.getColumnIndex("MinAge");
    if (indexMinAge != -1 && !cursor.isNull(indexMinAge)) {
      model.MinAge = cursor.getString(indexMinAge);
    } else {
      model.MinAge = null;
    }
    int indexMaxDay = cursor.getColumnIndex("MaxDay");
    if (indexMaxDay != -1 && !cursor.isNull(indexMaxDay)) {
      model.MaxDay = cursor.getString(indexMaxDay);
    } else {
      model.MaxDay = null;
    }
    int indexMaxDayBda = cursor.getColumnIndex("MaxDayBda");
    if (indexMaxDayBda != -1 && !cursor.isNull(indexMaxDayBda)) {
      model.MaxDayBda = cursor.getString(indexMaxDayBda);
    } else {
      model.MaxDayBda = null;
    }
    int indexLoadingPct = cursor.getColumnIndex("LoadingPct");
    if (indexLoadingPct != -1 && !cursor.isNull(indexLoadingPct)) {
      model.LoadingPct = cursor.getString(indexLoadingPct);
    } else {
      model.LoadingPct = null;
    }
    int indexCharges = cursor.getColumnIndex("Charges");
    if (indexCharges != -1 && !cursor.isNull(indexCharges)) {
      model.Charges = cursor.getString(indexCharges);
    } else {
      model.Charges = null;
    }
    int indexBdaAmount = cursor.getColumnIndex("BdaAmount");
    if (indexBdaAmount != -1 && !cursor.isNull(indexBdaAmount)) {
      model.BdaAmount = cursor.getString(indexBdaAmount);
    } else {
      model.BdaAmount = null;
    }
    int indexStampAmount = cursor.getColumnIndex("StampAmount");
    if (indexStampAmount != -1 && !cursor.isNull(indexStampAmount)) {
      model.StampAmount = cursor.getString(indexStampAmount);
    } else {
      model.StampAmount = null;
    }
    int indexMaxDayTravel = cursor.getColumnIndex("MaxDayTravel");
    if (indexMaxDayTravel != -1 && !cursor.isNull(indexMaxDayTravel)) {
      model.MaxDayTravel = cursor.getString(indexMaxDayTravel);
    } else {
      model.MaxDayTravel = null;
    }
    int indexIsConvertToIdr = cursor.getColumnIndex("IsConvertToIdr");
    if (indexIsConvertToIdr != -1 && !cursor.isNull(indexIsConvertToIdr)) {
      model.IsConvertToIdr = cursor.getString(indexIsConvertToIdr);
    } else {
      model.IsConvertToIdr = null;
    }
    int indexMinMember = cursor.getColumnIndex("MinMember");
    if (indexMinMember != -1 && !cursor.isNull(indexMinMember)) {
      model.MinMember = cursor.getString(indexMinMember);
    } else {
      model.MinMember = null;
    }
    int indexIsMobile = cursor.getColumnIndex("IsMobile");
    if (indexIsMobile != -1 && !cursor.isNull(indexIsMobile)) {
      model.IsMobile = cursor.getString(indexIsMobile);
    } else {
      model.IsMobile = null;
    }
    int indexIsActive = cursor.getColumnIndex("IsActive");
    if (indexIsActive != -1 && !cursor.isNull(indexIsActive)) {
      model.IsActive = cursor.getString(indexIsActive);
    } else {
      model.IsActive = null;
    }
  }

  @Override
  public final boolean exists(SubProduct model, DatabaseWrapper wrapper) {
    return new Select(Method.count()).from(SubProduct.class).where(getPrimaryConditionClause(model)).count(wrapper) > 0;
  }

  @Override
  public final ConditionGroup getPrimaryConditionClause(SubProduct model) {
    ConditionGroup clause = ConditionGroup.clause();
    clause.and(SubProduct_Table.SubProductCode.eq(model.SubProductCode));return clause;
  }

  @Override
  public final SubProduct newInstance() {
    return new SubProduct();
  }

  @Override
  public final BaseProperty getProperty(String name) {
    return SubProduct_Table.getProperty(name);
  }
}
