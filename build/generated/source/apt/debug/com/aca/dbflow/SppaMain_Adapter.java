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

public final class SppaMain_Adapter extends ModelAdapter<SppaMain> {
  public SppaMain_Adapter(DatabaseHolder holder) {
  }

  @Override
  public final Class<SppaMain> getModelClass() {
    return SppaMain.class;
  }

  public final String getTableName() {
    return "`SppaMain`";
  }

  public final void updateAutoIncrement(SppaMain model, Number id) {
    model.id = id.intValue();
  }

  @Override
  public final Number getAutoIncrementingId(SppaMain model) {
    return model.id;
  }

  @Override
  public final String getAutoIncrementingColumnName() {
    return "id";
  }

  @Override
  public final IProperty[] getAllColumnProperties() {
    return SppaMain_Table.getAllColumnProperties();
  }

  @Override
  public final void bindToInsertValues(ContentValues values, SppaMain model) {
    if (model.SppaNo != null) {
      values.put(SppaMain_Table.SppaNo.getCursorKey(), model.SppaNo);
    } else {
      values.putNull(SppaMain_Table.SppaNo.getCursorKey());
    }
    if (model.SppaDate != null) {
      values.put(SppaMain_Table.SppaDate.getCursorKey(), model.SppaDate);
    } else {
      values.putNull(SppaMain_Table.SppaDate.getCursorKey());
    }
    if (model.SppaStatus != null) {
      values.put(SppaMain_Table.SppaStatus.getCursorKey(), model.SppaStatus);
    } else {
      values.putNull(SppaMain_Table.SppaStatus.getCursorKey());
    }
    if (model.ProductCode != null) {
      values.put(SppaMain_Table.ProductCode.getCursorKey(), model.ProductCode);
    } else {
      values.putNull(SppaMain_Table.ProductCode.getCursorKey());
    }
    if (model.SubProductCode != null) {
      values.put(SppaMain_Table.SubProductCode.getCursorKey(), model.SubProductCode);
    } else {
      values.putNull(SppaMain_Table.SubProductCode.getCursorKey());
    }
    if (model.PlanCode != null) {
      values.put(SppaMain_Table.PlanCode.getCursorKey(), model.PlanCode);
    } else {
      values.putNull(SppaMain_Table.PlanCode.getCursorKey());
    }
    if (model.ZoneId != null) {
      values.put(SppaMain_Table.ZoneId.getCursorKey(), model.ZoneId);
    } else {
      values.putNull(SppaMain_Table.ZoneId.getCursorKey());
    }
    if (model.Name != null) {
      values.put(SppaMain_Table.Name.getCursorKey(), model.Name);
    } else {
      values.putNull(SppaMain_Table.Name.getCursorKey());
    }
    if (model.CurrencyCode != null) {
      values.put(SppaMain_Table.CurrencyCode.getCursorKey(), model.CurrencyCode);
    } else {
      values.putNull(SppaMain_Table.CurrencyCode.getCursorKey());
    }
    if (model.PremiumAmount != null) {
      values.put(SppaMain_Table.PremiumAmount.getCursorKey(), model.PremiumAmount);
    } else {
      values.putNull(SppaMain_Table.PremiumAmount.getCursorKey());
    }
    if (model.PremiumAdditionalAmount != null) {
      values.put(SppaMain_Table.PremiumAdditionalAmount.getCursorKey(), model.PremiumAdditionalAmount);
    } else {
      values.putNull(SppaMain_Table.PremiumAdditionalAmount.getCursorKey());
    }
    if (model.PremiumLoadingAmount != null) {
      values.put(SppaMain_Table.PremiumLoadingAmount.getCursorKey(), model.PremiumLoadingAmount);
    } else {
      values.putNull(SppaMain_Table.PremiumLoadingAmount.getCursorKey());
    }
    if (model.PremiumBdaAmount != null) {
      values.put(SppaMain_Table.PremiumBdaAmount.getCursorKey(), model.PremiumBdaAmount);
    } else {
      values.putNull(SppaMain_Table.PremiumBdaAmount.getCursorKey());
    }
    if (model.ChargeAmount != null) {
      values.put(SppaMain_Table.ChargeAmount.getCursorKey(), model.ChargeAmount);
    } else {
      values.putNull(SppaMain_Table.ChargeAmount.getCursorKey());
    }
    if (model.StampAmount != null) {
      values.put(SppaMain_Table.StampAmount.getCursorKey(), model.StampAmount);
    } else {
      values.putNull(SppaMain_Table.StampAmount.getCursorKey());
    }
    if (model.DiscRate != null) {
      values.put(SppaMain_Table.DiscRate.getCursorKey(), model.DiscRate);
    } else {
      values.putNull(SppaMain_Table.DiscRate.getCursorKey());
    }
    if (model.DiscAmount != null) {
      values.put(SppaMain_Table.DiscAmount.getCursorKey(), model.DiscAmount);
    } else {
      values.putNull(SppaMain_Table.DiscAmount.getCursorKey());
    }
    if (model.TotalPremiumAmount != null) {
      values.put(SppaMain_Table.TotalPremiumAmount.getCursorKey(), model.TotalPremiumAmount);
    } else {
      values.putNull(SppaMain_Table.TotalPremiumAmount.getCursorKey());
    }
    if (model.PromoCode != null) {
      values.put(SppaMain_Table.PromoCode.getCursorKey(), model.PromoCode);
    } else {
      values.putNull(SppaMain_Table.PromoCode.getCursorKey());
    }
    if (model.EffectiveDate != null) {
      values.put(SppaMain_Table.EffectiveDate.getCursorKey(), model.EffectiveDate);
    } else {
      values.putNull(SppaMain_Table.EffectiveDate.getCursorKey());
    }
    if (model.ExpireDate != null) {
      values.put(SppaMain_Table.ExpireDate.getCursorKey(), model.ExpireDate);
    } else {
      values.putNull(SppaMain_Table.ExpireDate.getCursorKey());
    }
    if (model.PolicyNo != null) {
      values.put(SppaMain_Table.PolicyNo.getCursorKey(), model.PolicyNo);
    } else {
      values.putNull(SppaMain_Table.PolicyNo.getCursorKey());
    }
    if (model.AgentCode != null) {
      values.put(SppaMain_Table.AgentCode.getCursorKey(), model.AgentCode);
    } else {
      values.putNull(SppaMain_Table.AgentCode.getCursorKey());
    }
    if (model.AgentUserCode != null) {
      values.put(SppaMain_Table.AgentUserCode.getCursorKey(), model.AgentUserCode);
    } else {
      values.putNull(SppaMain_Table.AgentUserCode.getCursorKey());
    }
    if (model.SppaSubmitDate != null) {
      values.put(SppaMain_Table.SppaSubmitDate.getCursorKey(), model.SppaSubmitDate);
    } else {
      values.putNull(SppaMain_Table.SppaSubmitDate.getCursorKey());
    }
    if (model.SppaSubmitBy != null) {
      values.put(SppaMain_Table.SppaSubmitBy.getCursorKey(), model.SppaSubmitBy);
    } else {
      values.putNull(SppaMain_Table.SppaSubmitBy.getCursorKey());
    }
    if (model.BranchCode != null) {
      values.put(SppaMain_Table.BranchCode.getCursorKey(), model.BranchCode);
    } else {
      values.putNull(SppaMain_Table.BranchCode.getCursorKey());
    }
    if (model.SubBranchCode != null) {
      values.put(SppaMain_Table.SubBranchCode.getCursorKey(), model.SubBranchCode);
    } else {
      values.putNull(SppaMain_Table.SubBranchCode.getCursorKey());
    }
    if (model.TypingBranch != null) {
      values.put(SppaMain_Table.TypingBranch.getCursorKey(), model.TypingBranch);
    } else {
      values.putNull(SppaMain_Table.TypingBranch.getCursorKey());
    }
    if (model.AgeUse != null) {
      values.put(SppaMain_Table.AgeUse.getCursorKey(), model.AgeUse);
    } else {
      values.putNull(SppaMain_Table.AgeUse.getCursorKey());
    }
    if (model.TotalDayTravel != null) {
      values.put(SppaMain_Table.TotalDayTravel.getCursorKey(), model.TotalDayTravel);
    } else {
      values.putNull(SppaMain_Table.TotalDayTravel.getCursorKey());
    }
    if (model.BasicDayTravel != null) {
      values.put(SppaMain_Table.BasicDayTravel.getCursorKey(), model.BasicDayTravel);
    } else {
      values.putNull(SppaMain_Table.BasicDayTravel.getCursorKey());
    }
    if (model.AddDay != null) {
      values.put(SppaMain_Table.AddDay.getCursorKey(), model.AddDay);
    } else {
      values.putNull(SppaMain_Table.AddDay.getCursorKey());
    }
    if (model.AddWeek != null) {
      values.put(SppaMain_Table.AddWeek.getCursorKey(), model.AddWeek);
    } else {
      values.putNull(SppaMain_Table.AddWeek.getCursorKey());
    }
    if (model.AddWeekFactor != null) {
      values.put(SppaMain_Table.AddWeekFactor.getCursorKey(), model.AddWeekFactor);
    } else {
      values.putNull(SppaMain_Table.AddWeekFactor.getCursorKey());
    }
    if (model.BdaDayMax != null) {
      values.put(SppaMain_Table.BdaDayMax.getCursorKey(), model.BdaDayMax);
    } else {
      values.putNull(SppaMain_Table.BdaDayMax.getCursorKey());
    }
    if (model.BdaDay != null) {
      values.put(SppaMain_Table.BdaDay.getCursorKey(), model.BdaDay);
    } else {
      values.putNull(SppaMain_Table.BdaDay.getCursorKey());
    }
    if (model.BdaWeek != null) {
      values.put(SppaMain_Table.BdaWeek.getCursorKey(), model.BdaWeek);
    } else {
      values.putNull(SppaMain_Table.BdaWeek.getCursorKey());
    }
    if (model.BdaWeekFactor != null) {
      values.put(SppaMain_Table.BdaWeekFactor.getCursorKey(), model.BdaWeekFactor);
    } else {
      values.putNull(SppaMain_Table.BdaWeekFactor.getCursorKey());
    }
    if (model.AgeLoadingFactor != null) {
      values.put(SppaMain_Table.AgeLoadingFactor.getCursorKey(), model.AgeLoadingFactor);
    } else {
      values.putNull(SppaMain_Table.AgeLoadingFactor.getCursorKey());
    }
    if (model.NoOfPersonFactor != null) {
      values.put(SppaMain_Table.NoOfPersonFactor.getCursorKey(), model.NoOfPersonFactor);
    } else {
      values.putNull(SppaMain_Table.NoOfPersonFactor.getCursorKey());
    }
    if (model.ExchangeRate != null) {
      values.put(SppaMain_Table.ExchangeRate.getCursorKey(), model.ExchangeRate);
    } else {
      values.putNull(SppaMain_Table.ExchangeRate.getCursorKey());
    }
    if (model.IsEndors != null) {
      values.put(SppaMain_Table.IsEndors.getCursorKey(), model.IsEndors);
    } else {
      values.putNull(SppaMain_Table.IsEndors.getCursorKey());
    }
    if (model.EndorsementTypeId != null) {
      values.put(SppaMain_Table.EndorsementTypeId.getCursorKey(), model.EndorsementTypeId);
    } else {
      values.putNull(SppaMain_Table.EndorsementTypeId.getCursorKey());
    }
    if (model.EndorsBy != null) {
      values.put(SppaMain_Table.EndorsBy.getCursorKey(), model.EndorsBy);
    } else {
      values.putNull(SppaMain_Table.EndorsBy.getCursorKey());
    }
    if (model.EndorsDate != null) {
      values.put(SppaMain_Table.EndorsDate.getCursorKey(), model.EndorsDate);
    } else {
      values.putNull(SppaMain_Table.EndorsDate.getCursorKey());
    }
    if (model.IsFromEndorsment != null) {
      values.put(SppaMain_Table.IsFromEndorsment.getCursorKey(), model.IsFromEndorsment);
    } else {
      values.putNull(SppaMain_Table.IsFromEndorsment.getCursorKey());
    }
    if (model.KdPaymentStatus != null) {
      values.put(SppaMain_Table.KdPaymentStatus.getCursorKey(), model.KdPaymentStatus);
    } else {
      values.putNull(SppaMain_Table.KdPaymentStatus.getCursorKey());
    }
    if (model.PaymentDate != null) {
      values.put(SppaMain_Table.PaymentDate.getCursorKey(), model.PaymentDate);
    } else {
      values.putNull(SppaMain_Table.PaymentDate.getCursorKey());
    }
    if (model.IsTransferAS400 != null) {
      values.put(SppaMain_Table.IsTransferAS400.getCursorKey(), model.IsTransferAS400);
    } else {
      values.putNull(SppaMain_Table.IsTransferAS400.getCursorKey());
    }
    if (model.TransferDate != null) {
      values.put(SppaMain_Table.TransferDate.getCursorKey(), model.TransferDate);
    } else {
      values.putNull(SppaMain_Table.TransferDate.getCursorKey());
    }
    if (model.CommisionRate != null) {
      values.put(SppaMain_Table.CommisionRate.getCursorKey(), model.CommisionRate);
    } else {
      values.putNull(SppaMain_Table.CommisionRate.getCursorKey());
    }
    if (model.Commision != null) {
      values.put(SppaMain_Table.Commision.getCursorKey(), model.Commision);
    } else {
      values.putNull(SppaMain_Table.Commision.getCursorKey());
    }
    if (model.AreaCodeAs400 != null) {
      values.put(SppaMain_Table.AreaCodeAs400.getCursorKey(), model.AreaCodeAs400);
    } else {
      values.putNull(SppaMain_Table.AreaCodeAs400.getCursorKey());
    }
    if (model.CoverageCodeAs400 != null) {
      values.put(SppaMain_Table.CoverageCodeAs400.getCursorKey(), model.CoverageCodeAs400);
    } else {
      values.putNull(SppaMain_Table.CoverageCodeAs400.getCursorKey());
    }
    if (model.DurationCodeAs400 != null) {
      values.put(SppaMain_Table.DurationCodeAs400.getCursorKey(), model.DurationCodeAs400);
    } else {
      values.putNull(SppaMain_Table.DurationCodeAs400.getCursorKey());
    }
    if (model.AllocationAmount != null) {
      values.put(SppaMain_Table.AllocationAmount.getCursorKey(), model.AllocationAmount);
    } else {
      values.putNull(SppaMain_Table.AllocationAmount.getCursorKey());
    }
    if (model.Age != null) {
      values.put(SppaMain_Table.Age.getCursorKey(), model.Age);
    } else {
      values.putNull(SppaMain_Table.Age.getCursorKey());
    }
  }

  @Override
  public final void bindToContentValues(ContentValues values, SppaMain model) {
    values.put(SppaMain_Table.id.getCursorKey(), model.id);
    bindToInsertValues(values, model);
  }

  @Override
  public final void bindToInsertStatement(DatabaseStatement statement, SppaMain model, int start) {
    if (model.SppaNo != null) {
      statement.bindString(1 + start, model.SppaNo);
    } else {
      statement.bindNull(1 + start);
    }
    if (model.SppaDate != null) {
      statement.bindString(2 + start, model.SppaDate);
    } else {
      statement.bindNull(2 + start);
    }
    if (model.SppaStatus != null) {
      statement.bindString(3 + start, model.SppaStatus);
    } else {
      statement.bindNull(3 + start);
    }
    if (model.ProductCode != null) {
      statement.bindString(4 + start, model.ProductCode);
    } else {
      statement.bindNull(4 + start);
    }
    if (model.SubProductCode != null) {
      statement.bindString(5 + start, model.SubProductCode);
    } else {
      statement.bindNull(5 + start);
    }
    if (model.PlanCode != null) {
      statement.bindString(6 + start, model.PlanCode);
    } else {
      statement.bindNull(6 + start);
    }
    if (model.ZoneId != null) {
      statement.bindString(7 + start, model.ZoneId);
    } else {
      statement.bindNull(7 + start);
    }
    if (model.Name != null) {
      statement.bindString(8 + start, model.Name);
    } else {
      statement.bindNull(8 + start);
    }
    if (model.CurrencyCode != null) {
      statement.bindString(9 + start, model.CurrencyCode);
    } else {
      statement.bindNull(9 + start);
    }
    if (model.PremiumAmount != null) {
      statement.bindString(10 + start, model.PremiumAmount);
    } else {
      statement.bindNull(10 + start);
    }
    if (model.PremiumAdditionalAmount != null) {
      statement.bindString(11 + start, model.PremiumAdditionalAmount);
    } else {
      statement.bindNull(11 + start);
    }
    if (model.PremiumLoadingAmount != null) {
      statement.bindString(12 + start, model.PremiumLoadingAmount);
    } else {
      statement.bindNull(12 + start);
    }
    if (model.PremiumBdaAmount != null) {
      statement.bindString(13 + start, model.PremiumBdaAmount);
    } else {
      statement.bindNull(13 + start);
    }
    if (model.ChargeAmount != null) {
      statement.bindString(14 + start, model.ChargeAmount);
    } else {
      statement.bindNull(14 + start);
    }
    if (model.StampAmount != null) {
      statement.bindString(15 + start, model.StampAmount);
    } else {
      statement.bindNull(15 + start);
    }
    if (model.DiscRate != null) {
      statement.bindString(16 + start, model.DiscRate);
    } else {
      statement.bindNull(16 + start);
    }
    if (model.DiscAmount != null) {
      statement.bindString(17 + start, model.DiscAmount);
    } else {
      statement.bindNull(17 + start);
    }
    if (model.TotalPremiumAmount != null) {
      statement.bindString(18 + start, model.TotalPremiumAmount);
    } else {
      statement.bindNull(18 + start);
    }
    if (model.PromoCode != null) {
      statement.bindString(19 + start, model.PromoCode);
    } else {
      statement.bindNull(19 + start);
    }
    if (model.EffectiveDate != null) {
      statement.bindString(20 + start, model.EffectiveDate);
    } else {
      statement.bindNull(20 + start);
    }
    if (model.ExpireDate != null) {
      statement.bindString(21 + start, model.ExpireDate);
    } else {
      statement.bindNull(21 + start);
    }
    if (model.PolicyNo != null) {
      statement.bindString(22 + start, model.PolicyNo);
    } else {
      statement.bindNull(22 + start);
    }
    if (model.AgentCode != null) {
      statement.bindString(23 + start, model.AgentCode);
    } else {
      statement.bindNull(23 + start);
    }
    if (model.AgentUserCode != null) {
      statement.bindString(24 + start, model.AgentUserCode);
    } else {
      statement.bindNull(24 + start);
    }
    if (model.SppaSubmitDate != null) {
      statement.bindString(25 + start, model.SppaSubmitDate);
    } else {
      statement.bindNull(25 + start);
    }
    if (model.SppaSubmitBy != null) {
      statement.bindString(26 + start, model.SppaSubmitBy);
    } else {
      statement.bindNull(26 + start);
    }
    if (model.BranchCode != null) {
      statement.bindString(27 + start, model.BranchCode);
    } else {
      statement.bindNull(27 + start);
    }
    if (model.SubBranchCode != null) {
      statement.bindString(28 + start, model.SubBranchCode);
    } else {
      statement.bindNull(28 + start);
    }
    if (model.TypingBranch != null) {
      statement.bindString(29 + start, model.TypingBranch);
    } else {
      statement.bindNull(29 + start);
    }
    if (model.AgeUse != null) {
      statement.bindString(30 + start, model.AgeUse);
    } else {
      statement.bindNull(30 + start);
    }
    if (model.TotalDayTravel != null) {
      statement.bindString(31 + start, model.TotalDayTravel);
    } else {
      statement.bindNull(31 + start);
    }
    if (model.BasicDayTravel != null) {
      statement.bindString(32 + start, model.BasicDayTravel);
    } else {
      statement.bindNull(32 + start);
    }
    if (model.AddDay != null) {
      statement.bindString(33 + start, model.AddDay);
    } else {
      statement.bindNull(33 + start);
    }
    if (model.AddWeek != null) {
      statement.bindString(34 + start, model.AddWeek);
    } else {
      statement.bindNull(34 + start);
    }
    if (model.AddWeekFactor != null) {
      statement.bindString(35 + start, model.AddWeekFactor);
    } else {
      statement.bindNull(35 + start);
    }
    if (model.BdaDayMax != null) {
      statement.bindString(36 + start, model.BdaDayMax);
    } else {
      statement.bindNull(36 + start);
    }
    if (model.BdaDay != null) {
      statement.bindString(37 + start, model.BdaDay);
    } else {
      statement.bindNull(37 + start);
    }
    if (model.BdaWeek != null) {
      statement.bindString(38 + start, model.BdaWeek);
    } else {
      statement.bindNull(38 + start);
    }
    if (model.BdaWeekFactor != null) {
      statement.bindString(39 + start, model.BdaWeekFactor);
    } else {
      statement.bindNull(39 + start);
    }
    if (model.AgeLoadingFactor != null) {
      statement.bindString(40 + start, model.AgeLoadingFactor);
    } else {
      statement.bindNull(40 + start);
    }
    if (model.NoOfPersonFactor != null) {
      statement.bindString(41 + start, model.NoOfPersonFactor);
    } else {
      statement.bindNull(41 + start);
    }
    if (model.ExchangeRate != null) {
      statement.bindString(42 + start, model.ExchangeRate);
    } else {
      statement.bindNull(42 + start);
    }
    if (model.IsEndors != null) {
      statement.bindString(43 + start, model.IsEndors);
    } else {
      statement.bindNull(43 + start);
    }
    if (model.EndorsementTypeId != null) {
      statement.bindString(44 + start, model.EndorsementTypeId);
    } else {
      statement.bindNull(44 + start);
    }
    if (model.EndorsBy != null) {
      statement.bindString(45 + start, model.EndorsBy);
    } else {
      statement.bindNull(45 + start);
    }
    if (model.EndorsDate != null) {
      statement.bindString(46 + start, model.EndorsDate);
    } else {
      statement.bindNull(46 + start);
    }
    if (model.IsFromEndorsment != null) {
      statement.bindString(47 + start, model.IsFromEndorsment);
    } else {
      statement.bindNull(47 + start);
    }
    if (model.KdPaymentStatus != null) {
      statement.bindString(48 + start, model.KdPaymentStatus);
    } else {
      statement.bindNull(48 + start);
    }
    if (model.PaymentDate != null) {
      statement.bindString(49 + start, model.PaymentDate);
    } else {
      statement.bindNull(49 + start);
    }
    if (model.IsTransferAS400 != null) {
      statement.bindString(50 + start, model.IsTransferAS400);
    } else {
      statement.bindNull(50 + start);
    }
    if (model.TransferDate != null) {
      statement.bindString(51 + start, model.TransferDate);
    } else {
      statement.bindNull(51 + start);
    }
    if (model.CommisionRate != null) {
      statement.bindString(52 + start, model.CommisionRate);
    } else {
      statement.bindNull(52 + start);
    }
    if (model.Commision != null) {
      statement.bindString(53 + start, model.Commision);
    } else {
      statement.bindNull(53 + start);
    }
    if (model.AreaCodeAs400 != null) {
      statement.bindString(54 + start, model.AreaCodeAs400);
    } else {
      statement.bindNull(54 + start);
    }
    if (model.CoverageCodeAs400 != null) {
      statement.bindString(55 + start, model.CoverageCodeAs400);
    } else {
      statement.bindNull(55 + start);
    }
    if (model.DurationCodeAs400 != null) {
      statement.bindString(56 + start, model.DurationCodeAs400);
    } else {
      statement.bindNull(56 + start);
    }
    if (model.AllocationAmount != null) {
      statement.bindString(57 + start, model.AllocationAmount);
    } else {
      statement.bindNull(57 + start);
    }
    if (model.Age != null) {
      statement.bindString(58 + start, model.Age);
    } else {
      statement.bindNull(58 + start);
    }
  }

  @Override
  public final void bindToStatement(DatabaseStatement statement, SppaMain model) {
    statement.bindLong(1, model.id);
    bindToInsertStatement(statement, model, 1);
  }

  @Override
  public final String getInsertStatementQuery() {
    return "INSERT INTO `SppaMain`(`SppaNo`,`SppaDate`,`SppaStatus`,`ProductCode`,`SubProductCode`,`PlanCode`,`ZoneId`,`Name`,`CurrencyCode`,`PremiumAmount`,`PremiumAdditionalAmount`,`PremiumLoadingAmount`,`PremiumBdaAmount`,`ChargeAmount`,`StampAmount`,`DiscRate`,`DiscAmount`,`TotalPremiumAmount`,`PromoCode`,`EffectiveDate`,`ExpireDate`,`PolicyNo`,`AgentCode`,`AgentUserCode`,`SppaSubmitDate`,`SppaSubmitBy`,`BranchCode`,`SubBranchCode`,`TypingBranch`,`AgeUse`,`TotalDayTravel`,`BasicDayTravel`,`AddDay`,`AddWeek`,`AddWeekFactor`,`BdaDayMax`,`BdaDay`,`BdaWeek`,`BdaWeekFactor`,`AgeLoadingFactor`,`NoOfPersonFactor`,`ExchangeRate`,`IsEndors`,`EndorsementTypeId`,`EndorsBy`,`EndorsDate`,`IsFromEndorsment`,`KdPaymentStatus`,`PaymentDate`,`IsTransferAS400`,`TransferDate`,`CommisionRate`,`Commision`,`AreaCodeAs400`,`CoverageCodeAs400`,`DurationCodeAs400`,`AllocationAmount`,`Age`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
  }

  @Override
  public final String getCompiledStatementQuery() {
    return "INSERT INTO `SppaMain`(`id`,`SppaNo`,`SppaDate`,`SppaStatus`,`ProductCode`,`SubProductCode`,`PlanCode`,`ZoneId`,`Name`,`CurrencyCode`,`PremiumAmount`,`PremiumAdditionalAmount`,`PremiumLoadingAmount`,`PremiumBdaAmount`,`ChargeAmount`,`StampAmount`,`DiscRate`,`DiscAmount`,`TotalPremiumAmount`,`PromoCode`,`EffectiveDate`,`ExpireDate`,`PolicyNo`,`AgentCode`,`AgentUserCode`,`SppaSubmitDate`,`SppaSubmitBy`,`BranchCode`,`SubBranchCode`,`TypingBranch`,`AgeUse`,`TotalDayTravel`,`BasicDayTravel`,`AddDay`,`AddWeek`,`AddWeekFactor`,`BdaDayMax`,`BdaDay`,`BdaWeek`,`BdaWeekFactor`,`AgeLoadingFactor`,`NoOfPersonFactor`,`ExchangeRate`,`IsEndors`,`EndorsementTypeId`,`EndorsBy`,`EndorsDate`,`IsFromEndorsment`,`KdPaymentStatus`,`PaymentDate`,`IsTransferAS400`,`TransferDate`,`CommisionRate`,`Commision`,`AreaCodeAs400`,`CoverageCodeAs400`,`DurationCodeAs400`,`AllocationAmount`,`Age`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
  }

  @Override
  public final String getCreationQuery() {
    return "CREATE TABLE IF NOT EXISTS `SppaMain`(`id` INTEGER PRIMARY KEY AUTOINCREMENT,`SppaNo` TEXT,`SppaDate` TEXT,`SppaStatus` TEXT,`ProductCode` TEXT,`SubProductCode` TEXT,`PlanCode` TEXT,`ZoneId` TEXT,`Name` TEXT,`CurrencyCode` TEXT,`PremiumAmount` TEXT,`PremiumAdditionalAmount` TEXT,`PremiumLoadingAmount` TEXT,`PremiumBdaAmount` TEXT,`ChargeAmount` TEXT,`StampAmount` TEXT,`DiscRate` TEXT,`DiscAmount` TEXT,`TotalPremiumAmount` TEXT,`PromoCode` TEXT,`EffectiveDate` TEXT,`ExpireDate` TEXT,`PolicyNo` TEXT,`AgentCode` TEXT,`AgentUserCode` TEXT,`SppaSubmitDate` TEXT,`SppaSubmitBy` TEXT,`BranchCode` TEXT,`SubBranchCode` TEXT,`TypingBranch` TEXT,`AgeUse` TEXT,`TotalDayTravel` TEXT,`BasicDayTravel` TEXT,`AddDay` TEXT,`AddWeek` TEXT,`AddWeekFactor` TEXT,`BdaDayMax` TEXT,`BdaDay` TEXT,`BdaWeek` TEXT,`BdaWeekFactor` TEXT,`AgeLoadingFactor` TEXT,`NoOfPersonFactor` TEXT,`ExchangeRate` TEXT,`IsEndors` TEXT,`EndorsementTypeId` TEXT,`EndorsBy` TEXT,`EndorsDate` TEXT,`IsFromEndorsment` TEXT,`KdPaymentStatus` TEXT,`PaymentDate` TEXT,`IsTransferAS400` TEXT,`TransferDate` TEXT,`CommisionRate` TEXT,`Commision` TEXT,`AreaCodeAs400` TEXT,`CoverageCodeAs400` TEXT,`DurationCodeAs400` TEXT,`AllocationAmount` TEXT,`Age` TEXT" + ");";
  }

  @Override
  public final void loadFromCursor(Cursor cursor, SppaMain model) {
    int indexid = cursor.getColumnIndex("id");
    if (indexid != -1 && !cursor.isNull(indexid)) {
      model.id = cursor.getInt(indexid);
    } else {
      model.id = 0;
    }
    int indexSppaNo = cursor.getColumnIndex("SppaNo");
    if (indexSppaNo != -1 && !cursor.isNull(indexSppaNo)) {
      model.SppaNo = cursor.getString(indexSppaNo);
    } else {
      model.SppaNo = null;
    }
    int indexSppaDate = cursor.getColumnIndex("SppaDate");
    if (indexSppaDate != -1 && !cursor.isNull(indexSppaDate)) {
      model.SppaDate = cursor.getString(indexSppaDate);
    } else {
      model.SppaDate = null;
    }
    int indexSppaStatus = cursor.getColumnIndex("SppaStatus");
    if (indexSppaStatus != -1 && !cursor.isNull(indexSppaStatus)) {
      model.SppaStatus = cursor.getString(indexSppaStatus);
    } else {
      model.SppaStatus = null;
    }
    int indexProductCode = cursor.getColumnIndex("ProductCode");
    if (indexProductCode != -1 && !cursor.isNull(indexProductCode)) {
      model.ProductCode = cursor.getString(indexProductCode);
    } else {
      model.ProductCode = null;
    }
    int indexSubProductCode = cursor.getColumnIndex("SubProductCode");
    if (indexSubProductCode != -1 && !cursor.isNull(indexSubProductCode)) {
      model.SubProductCode = cursor.getString(indexSubProductCode);
    } else {
      model.SubProductCode = null;
    }
    int indexPlanCode = cursor.getColumnIndex("PlanCode");
    if (indexPlanCode != -1 && !cursor.isNull(indexPlanCode)) {
      model.PlanCode = cursor.getString(indexPlanCode);
    } else {
      model.PlanCode = null;
    }
    int indexZoneId = cursor.getColumnIndex("ZoneId");
    if (indexZoneId != -1 && !cursor.isNull(indexZoneId)) {
      model.ZoneId = cursor.getString(indexZoneId);
    } else {
      model.ZoneId = null;
    }
    int indexName = cursor.getColumnIndex("Name");
    if (indexName != -1 && !cursor.isNull(indexName)) {
      model.Name = cursor.getString(indexName);
    } else {
      model.Name = null;
    }
    int indexCurrencyCode = cursor.getColumnIndex("CurrencyCode");
    if (indexCurrencyCode != -1 && !cursor.isNull(indexCurrencyCode)) {
      model.CurrencyCode = cursor.getString(indexCurrencyCode);
    } else {
      model.CurrencyCode = null;
    }
    int indexPremiumAmount = cursor.getColumnIndex("PremiumAmount");
    if (indexPremiumAmount != -1 && !cursor.isNull(indexPremiumAmount)) {
      model.PremiumAmount = cursor.getString(indexPremiumAmount);
    } else {
      model.PremiumAmount = null;
    }
    int indexPremiumAdditionalAmount = cursor.getColumnIndex("PremiumAdditionalAmount");
    if (indexPremiumAdditionalAmount != -1 && !cursor.isNull(indexPremiumAdditionalAmount)) {
      model.PremiumAdditionalAmount = cursor.getString(indexPremiumAdditionalAmount);
    } else {
      model.PremiumAdditionalAmount = null;
    }
    int indexPremiumLoadingAmount = cursor.getColumnIndex("PremiumLoadingAmount");
    if (indexPremiumLoadingAmount != -1 && !cursor.isNull(indexPremiumLoadingAmount)) {
      model.PremiumLoadingAmount = cursor.getString(indexPremiumLoadingAmount);
    } else {
      model.PremiumLoadingAmount = null;
    }
    int indexPremiumBdaAmount = cursor.getColumnIndex("PremiumBdaAmount");
    if (indexPremiumBdaAmount != -1 && !cursor.isNull(indexPremiumBdaAmount)) {
      model.PremiumBdaAmount = cursor.getString(indexPremiumBdaAmount);
    } else {
      model.PremiumBdaAmount = null;
    }
    int indexChargeAmount = cursor.getColumnIndex("ChargeAmount");
    if (indexChargeAmount != -1 && !cursor.isNull(indexChargeAmount)) {
      model.ChargeAmount = cursor.getString(indexChargeAmount);
    } else {
      model.ChargeAmount = null;
    }
    int indexStampAmount = cursor.getColumnIndex("StampAmount");
    if (indexStampAmount != -1 && !cursor.isNull(indexStampAmount)) {
      model.StampAmount = cursor.getString(indexStampAmount);
    } else {
      model.StampAmount = null;
    }
    int indexDiscRate = cursor.getColumnIndex("DiscRate");
    if (indexDiscRate != -1 && !cursor.isNull(indexDiscRate)) {
      model.DiscRate = cursor.getString(indexDiscRate);
    } else {
      model.DiscRate = null;
    }
    int indexDiscAmount = cursor.getColumnIndex("DiscAmount");
    if (indexDiscAmount != -1 && !cursor.isNull(indexDiscAmount)) {
      model.DiscAmount = cursor.getString(indexDiscAmount);
    } else {
      model.DiscAmount = null;
    }
    int indexTotalPremiumAmount = cursor.getColumnIndex("TotalPremiumAmount");
    if (indexTotalPremiumAmount != -1 && !cursor.isNull(indexTotalPremiumAmount)) {
      model.TotalPremiumAmount = cursor.getString(indexTotalPremiumAmount);
    } else {
      model.TotalPremiumAmount = null;
    }
    int indexPromoCode = cursor.getColumnIndex("PromoCode");
    if (indexPromoCode != -1 && !cursor.isNull(indexPromoCode)) {
      model.PromoCode = cursor.getString(indexPromoCode);
    } else {
      model.PromoCode = null;
    }
    int indexEffectiveDate = cursor.getColumnIndex("EffectiveDate");
    if (indexEffectiveDate != -1 && !cursor.isNull(indexEffectiveDate)) {
      model.EffectiveDate = cursor.getString(indexEffectiveDate);
    } else {
      model.EffectiveDate = null;
    }
    int indexExpireDate = cursor.getColumnIndex("ExpireDate");
    if (indexExpireDate != -1 && !cursor.isNull(indexExpireDate)) {
      model.ExpireDate = cursor.getString(indexExpireDate);
    } else {
      model.ExpireDate = null;
    }
    int indexPolicyNo = cursor.getColumnIndex("PolicyNo");
    if (indexPolicyNo != -1 && !cursor.isNull(indexPolicyNo)) {
      model.PolicyNo = cursor.getString(indexPolicyNo);
    } else {
      model.PolicyNo = null;
    }
    int indexAgentCode = cursor.getColumnIndex("AgentCode");
    if (indexAgentCode != -1 && !cursor.isNull(indexAgentCode)) {
      model.AgentCode = cursor.getString(indexAgentCode);
    } else {
      model.AgentCode = null;
    }
    int indexAgentUserCode = cursor.getColumnIndex("AgentUserCode");
    if (indexAgentUserCode != -1 && !cursor.isNull(indexAgentUserCode)) {
      model.AgentUserCode = cursor.getString(indexAgentUserCode);
    } else {
      model.AgentUserCode = null;
    }
    int indexSppaSubmitDate = cursor.getColumnIndex("SppaSubmitDate");
    if (indexSppaSubmitDate != -1 && !cursor.isNull(indexSppaSubmitDate)) {
      model.SppaSubmitDate = cursor.getString(indexSppaSubmitDate);
    } else {
      model.SppaSubmitDate = null;
    }
    int indexSppaSubmitBy = cursor.getColumnIndex("SppaSubmitBy");
    if (indexSppaSubmitBy != -1 && !cursor.isNull(indexSppaSubmitBy)) {
      model.SppaSubmitBy = cursor.getString(indexSppaSubmitBy);
    } else {
      model.SppaSubmitBy = null;
    }
    int indexBranchCode = cursor.getColumnIndex("BranchCode");
    if (indexBranchCode != -1 && !cursor.isNull(indexBranchCode)) {
      model.BranchCode = cursor.getString(indexBranchCode);
    } else {
      model.BranchCode = null;
    }
    int indexSubBranchCode = cursor.getColumnIndex("SubBranchCode");
    if (indexSubBranchCode != -1 && !cursor.isNull(indexSubBranchCode)) {
      model.SubBranchCode = cursor.getString(indexSubBranchCode);
    } else {
      model.SubBranchCode = null;
    }
    int indexTypingBranch = cursor.getColumnIndex("TypingBranch");
    if (indexTypingBranch != -1 && !cursor.isNull(indexTypingBranch)) {
      model.TypingBranch = cursor.getString(indexTypingBranch);
    } else {
      model.TypingBranch = null;
    }
    int indexAgeUse = cursor.getColumnIndex("AgeUse");
    if (indexAgeUse != -1 && !cursor.isNull(indexAgeUse)) {
      model.AgeUse = cursor.getString(indexAgeUse);
    } else {
      model.AgeUse = null;
    }
    int indexTotalDayTravel = cursor.getColumnIndex("TotalDayTravel");
    if (indexTotalDayTravel != -1 && !cursor.isNull(indexTotalDayTravel)) {
      model.TotalDayTravel = cursor.getString(indexTotalDayTravel);
    } else {
      model.TotalDayTravel = null;
    }
    int indexBasicDayTravel = cursor.getColumnIndex("BasicDayTravel");
    if (indexBasicDayTravel != -1 && !cursor.isNull(indexBasicDayTravel)) {
      model.BasicDayTravel = cursor.getString(indexBasicDayTravel);
    } else {
      model.BasicDayTravel = null;
    }
    int indexAddDay = cursor.getColumnIndex("AddDay");
    if (indexAddDay != -1 && !cursor.isNull(indexAddDay)) {
      model.AddDay = cursor.getString(indexAddDay);
    } else {
      model.AddDay = null;
    }
    int indexAddWeek = cursor.getColumnIndex("AddWeek");
    if (indexAddWeek != -1 && !cursor.isNull(indexAddWeek)) {
      model.AddWeek = cursor.getString(indexAddWeek);
    } else {
      model.AddWeek = null;
    }
    int indexAddWeekFactor = cursor.getColumnIndex("AddWeekFactor");
    if (indexAddWeekFactor != -1 && !cursor.isNull(indexAddWeekFactor)) {
      model.AddWeekFactor = cursor.getString(indexAddWeekFactor);
    } else {
      model.AddWeekFactor = null;
    }
    int indexBdaDayMax = cursor.getColumnIndex("BdaDayMax");
    if (indexBdaDayMax != -1 && !cursor.isNull(indexBdaDayMax)) {
      model.BdaDayMax = cursor.getString(indexBdaDayMax);
    } else {
      model.BdaDayMax = null;
    }
    int indexBdaDay = cursor.getColumnIndex("BdaDay");
    if (indexBdaDay != -1 && !cursor.isNull(indexBdaDay)) {
      model.BdaDay = cursor.getString(indexBdaDay);
    } else {
      model.BdaDay = null;
    }
    int indexBdaWeek = cursor.getColumnIndex("BdaWeek");
    if (indexBdaWeek != -1 && !cursor.isNull(indexBdaWeek)) {
      model.BdaWeek = cursor.getString(indexBdaWeek);
    } else {
      model.BdaWeek = null;
    }
    int indexBdaWeekFactor = cursor.getColumnIndex("BdaWeekFactor");
    if (indexBdaWeekFactor != -1 && !cursor.isNull(indexBdaWeekFactor)) {
      model.BdaWeekFactor = cursor.getString(indexBdaWeekFactor);
    } else {
      model.BdaWeekFactor = null;
    }
    int indexAgeLoadingFactor = cursor.getColumnIndex("AgeLoadingFactor");
    if (indexAgeLoadingFactor != -1 && !cursor.isNull(indexAgeLoadingFactor)) {
      model.AgeLoadingFactor = cursor.getString(indexAgeLoadingFactor);
    } else {
      model.AgeLoadingFactor = null;
    }
    int indexNoOfPersonFactor = cursor.getColumnIndex("NoOfPersonFactor");
    if (indexNoOfPersonFactor != -1 && !cursor.isNull(indexNoOfPersonFactor)) {
      model.NoOfPersonFactor = cursor.getString(indexNoOfPersonFactor);
    } else {
      model.NoOfPersonFactor = null;
    }
    int indexExchangeRate = cursor.getColumnIndex("ExchangeRate");
    if (indexExchangeRate != -1 && !cursor.isNull(indexExchangeRate)) {
      model.ExchangeRate = cursor.getString(indexExchangeRate);
    } else {
      model.ExchangeRate = null;
    }
    int indexIsEndors = cursor.getColumnIndex("IsEndors");
    if (indexIsEndors != -1 && !cursor.isNull(indexIsEndors)) {
      model.IsEndors = cursor.getString(indexIsEndors);
    } else {
      model.IsEndors = null;
    }
    int indexEndorsementTypeId = cursor.getColumnIndex("EndorsementTypeId");
    if (indexEndorsementTypeId != -1 && !cursor.isNull(indexEndorsementTypeId)) {
      model.EndorsementTypeId = cursor.getString(indexEndorsementTypeId);
    } else {
      model.EndorsementTypeId = null;
    }
    int indexEndorsBy = cursor.getColumnIndex("EndorsBy");
    if (indexEndorsBy != -1 && !cursor.isNull(indexEndorsBy)) {
      model.EndorsBy = cursor.getString(indexEndorsBy);
    } else {
      model.EndorsBy = null;
    }
    int indexEndorsDate = cursor.getColumnIndex("EndorsDate");
    if (indexEndorsDate != -1 && !cursor.isNull(indexEndorsDate)) {
      model.EndorsDate = cursor.getString(indexEndorsDate);
    } else {
      model.EndorsDate = null;
    }
    int indexIsFromEndorsment = cursor.getColumnIndex("IsFromEndorsment");
    if (indexIsFromEndorsment != -1 && !cursor.isNull(indexIsFromEndorsment)) {
      model.IsFromEndorsment = cursor.getString(indexIsFromEndorsment);
    } else {
      model.IsFromEndorsment = null;
    }
    int indexKdPaymentStatus = cursor.getColumnIndex("KdPaymentStatus");
    if (indexKdPaymentStatus != -1 && !cursor.isNull(indexKdPaymentStatus)) {
      model.KdPaymentStatus = cursor.getString(indexKdPaymentStatus);
    } else {
      model.KdPaymentStatus = null;
    }
    int indexPaymentDate = cursor.getColumnIndex("PaymentDate");
    if (indexPaymentDate != -1 && !cursor.isNull(indexPaymentDate)) {
      model.PaymentDate = cursor.getString(indexPaymentDate);
    } else {
      model.PaymentDate = null;
    }
    int indexIsTransferAS400 = cursor.getColumnIndex("IsTransferAS400");
    if (indexIsTransferAS400 != -1 && !cursor.isNull(indexIsTransferAS400)) {
      model.IsTransferAS400 = cursor.getString(indexIsTransferAS400);
    } else {
      model.IsTransferAS400 = null;
    }
    int indexTransferDate = cursor.getColumnIndex("TransferDate");
    if (indexTransferDate != -1 && !cursor.isNull(indexTransferDate)) {
      model.TransferDate = cursor.getString(indexTransferDate);
    } else {
      model.TransferDate = null;
    }
    int indexCommisionRate = cursor.getColumnIndex("CommisionRate");
    if (indexCommisionRate != -1 && !cursor.isNull(indexCommisionRate)) {
      model.CommisionRate = cursor.getString(indexCommisionRate);
    } else {
      model.CommisionRate = null;
    }
    int indexCommision = cursor.getColumnIndex("Commision");
    if (indexCommision != -1 && !cursor.isNull(indexCommision)) {
      model.Commision = cursor.getString(indexCommision);
    } else {
      model.Commision = null;
    }
    int indexAreaCodeAs400 = cursor.getColumnIndex("AreaCodeAs400");
    if (indexAreaCodeAs400 != -1 && !cursor.isNull(indexAreaCodeAs400)) {
      model.AreaCodeAs400 = cursor.getString(indexAreaCodeAs400);
    } else {
      model.AreaCodeAs400 = null;
    }
    int indexCoverageCodeAs400 = cursor.getColumnIndex("CoverageCodeAs400");
    if (indexCoverageCodeAs400 != -1 && !cursor.isNull(indexCoverageCodeAs400)) {
      model.CoverageCodeAs400 = cursor.getString(indexCoverageCodeAs400);
    } else {
      model.CoverageCodeAs400 = null;
    }
    int indexDurationCodeAs400 = cursor.getColumnIndex("DurationCodeAs400");
    if (indexDurationCodeAs400 != -1 && !cursor.isNull(indexDurationCodeAs400)) {
      model.DurationCodeAs400 = cursor.getString(indexDurationCodeAs400);
    } else {
      model.DurationCodeAs400 = null;
    }
    int indexAllocationAmount = cursor.getColumnIndex("AllocationAmount");
    if (indexAllocationAmount != -1 && !cursor.isNull(indexAllocationAmount)) {
      model.AllocationAmount = cursor.getString(indexAllocationAmount);
    } else {
      model.AllocationAmount = null;
    }
    int indexAge = cursor.getColumnIndex("Age");
    if (indexAge != -1 && !cursor.isNull(indexAge)) {
      model.Age = cursor.getString(indexAge);
    } else {
      model.Age = null;
    }
  }

  @Override
  public final boolean exists(SppaMain model, DatabaseWrapper wrapper) {
    return model.id > 0 && new Select(Method.count()).from(SppaMain.class).where(getPrimaryConditionClause(model)).count(wrapper) > 0;
  }

  @Override
  public final ConditionGroup getPrimaryConditionClause(SppaMain model) {
    ConditionGroup clause = ConditionGroup.clause();
    clause.and(SppaMain_Table.id.eq(model.id));return clause;
  }

  @Override
  public final SppaMain newInstance() {
    return new SppaMain();
  }

  @Override
  public final BaseProperty getProperty(String name) {
    return SppaMain_Table.getProperty(name);
  }
}
