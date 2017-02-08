package com.aca.dbflow;

import com.raizlabs.android.dbflow.runtime.BaseContentProvider.PropertyConverter;
import com.raizlabs.android.dbflow.sql.QueryBuilder;
import com.raizlabs.android.dbflow.sql.language.property.BaseProperty;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.sql.language.property.IntProperty;
import com.raizlabs.android.dbflow.sql.language.property.Property;
import java.lang.IllegalArgumentException;
import java.lang.String;

/**
 * This is generated code. Please do not modify */
public final class SppaMain_Table {
  public static final PropertyConverter PROPERTY_CONVERTER = new PropertyConverter(){ 
  public IProperty fromName(String columnName) {
  return com.aca.dbflow.SppaMain_Table.getProperty(columnName); 
  }
  };

  public static final IntProperty id = new IntProperty(SppaMain.class, "id");

  public static final Property<String> SppaNo = new Property<String>(SppaMain.class, "SppaNo");

  public static final Property<String> SppaDate = new Property<String>(SppaMain.class, "SppaDate");

  public static final Property<String> SppaStatus = new Property<String>(SppaMain.class, "SppaStatus");

  public static final Property<String> ProductCode = new Property<String>(SppaMain.class, "ProductCode");

  public static final Property<String> SubProductCode = new Property<String>(SppaMain.class, "SubProductCode");

  public static final Property<String> PlanCode = new Property<String>(SppaMain.class, "PlanCode");

  public static final Property<String> ZoneId = new Property<String>(SppaMain.class, "ZoneId");

  public static final Property<String> Name = new Property<String>(SppaMain.class, "Name");

  public static final Property<String> CurrencyCode = new Property<String>(SppaMain.class, "CurrencyCode");

  public static final Property<String> PremiumAmount = new Property<String>(SppaMain.class, "PremiumAmount");

  public static final Property<String> PremiumAdditionalAmount = new Property<String>(SppaMain.class, "PremiumAdditionalAmount");

  public static final Property<String> PremiumLoadingAmount = new Property<String>(SppaMain.class, "PremiumLoadingAmount");

  public static final Property<String> PremiumBdaAmount = new Property<String>(SppaMain.class, "PremiumBdaAmount");

  public static final Property<String> ChargeAmount = new Property<String>(SppaMain.class, "ChargeAmount");

  public static final Property<String> StampAmount = new Property<String>(SppaMain.class, "StampAmount");

  public static final Property<String> DiscRate = new Property<String>(SppaMain.class, "DiscRate");

  public static final Property<String> DiscAmount = new Property<String>(SppaMain.class, "DiscAmount");

  public static final Property<String> TotalPremiumAmount = new Property<String>(SppaMain.class, "TotalPremiumAmount");

  public static final Property<String> PromoCode = new Property<String>(SppaMain.class, "PromoCode");

  public static final Property<String> EffectiveDate = new Property<String>(SppaMain.class, "EffectiveDate");

  public static final Property<String> ExpireDate = new Property<String>(SppaMain.class, "ExpireDate");

  public static final Property<String> PolicyNo = new Property<String>(SppaMain.class, "PolicyNo");

  public static final Property<String> AgentCode = new Property<String>(SppaMain.class, "AgentCode");

  public static final Property<String> AgentUserCode = new Property<String>(SppaMain.class, "AgentUserCode");

  public static final Property<String> SppaSubmitDate = new Property<String>(SppaMain.class, "SppaSubmitDate");

  public static final Property<String> SppaSubmitBy = new Property<String>(SppaMain.class, "SppaSubmitBy");

  public static final Property<String> BranchCode = new Property<String>(SppaMain.class, "BranchCode");

  public static final Property<String> SubBranchCode = new Property<String>(SppaMain.class, "SubBranchCode");

  public static final Property<String> TypingBranch = new Property<String>(SppaMain.class, "TypingBranch");

  public static final Property<String> AgeUse = new Property<String>(SppaMain.class, "AgeUse");

  public static final Property<String> TotalDayTravel = new Property<String>(SppaMain.class, "TotalDayTravel");

  public static final Property<String> BasicDayTravel = new Property<String>(SppaMain.class, "BasicDayTravel");

  public static final Property<String> AddDay = new Property<String>(SppaMain.class, "AddDay");

  public static final Property<String> AddWeek = new Property<String>(SppaMain.class, "AddWeek");

  public static final Property<String> AddWeekFactor = new Property<String>(SppaMain.class, "AddWeekFactor");

  public static final Property<String> BdaDayMax = new Property<String>(SppaMain.class, "BdaDayMax");

  public static final Property<String> BdaDay = new Property<String>(SppaMain.class, "BdaDay");

  public static final Property<String> BdaWeek = new Property<String>(SppaMain.class, "BdaWeek");

  public static final Property<String> BdaWeekFactor = new Property<String>(SppaMain.class, "BdaWeekFactor");

  public static final Property<String> AgeLoadingFactor = new Property<String>(SppaMain.class, "AgeLoadingFactor");

  public static final Property<String> NoOfPersonFactor = new Property<String>(SppaMain.class, "NoOfPersonFactor");

  public static final Property<String> ExchangeRate = new Property<String>(SppaMain.class, "ExchangeRate");

  public static final Property<String> IsEndors = new Property<String>(SppaMain.class, "IsEndors");

  public static final Property<String> EndorsementTypeId = new Property<String>(SppaMain.class, "EndorsementTypeId");

  public static final Property<String> EndorsBy = new Property<String>(SppaMain.class, "EndorsBy");

  public static final Property<String> EndorsDate = new Property<String>(SppaMain.class, "EndorsDate");

  public static final Property<String> IsFromEndorsment = new Property<String>(SppaMain.class, "IsFromEndorsment");

  public static final Property<String> KdPaymentStatus = new Property<String>(SppaMain.class, "KdPaymentStatus");

  public static final Property<String> PaymentDate = new Property<String>(SppaMain.class, "PaymentDate");

  public static final Property<String> IsTransferAS400 = new Property<String>(SppaMain.class, "IsTransferAS400");

  public static final Property<String> TransferDate = new Property<String>(SppaMain.class, "TransferDate");

  public static final Property<String> CommisionRate = new Property<String>(SppaMain.class, "CommisionRate");

  public static final Property<String> Commision = new Property<String>(SppaMain.class, "Commision");

  public static final Property<String> AreaCodeAs400 = new Property<String>(SppaMain.class, "AreaCodeAs400");

  public static final Property<String> CoverageCodeAs400 = new Property<String>(SppaMain.class, "CoverageCodeAs400");

  public static final Property<String> DurationCodeAs400 = new Property<String>(SppaMain.class, "DurationCodeAs400");

  public static final Property<String> AllocationAmount = new Property<String>(SppaMain.class, "AllocationAmount");

  public static final Property<String> Age = new Property<String>(SppaMain.class, "Age");

  public static final IProperty[] getAllColumnProperties() {
    return new IProperty[]{id,SppaNo,SppaDate,SppaStatus,ProductCode,SubProductCode,PlanCode,ZoneId,Name,CurrencyCode,PremiumAmount,PremiumAdditionalAmount,PremiumLoadingAmount,PremiumBdaAmount,ChargeAmount,StampAmount,DiscRate,DiscAmount,TotalPremiumAmount,PromoCode,EffectiveDate,ExpireDate,PolicyNo,AgentCode,AgentUserCode,SppaSubmitDate,SppaSubmitBy,BranchCode,SubBranchCode,TypingBranch,AgeUse,TotalDayTravel,BasicDayTravel,AddDay,AddWeek,AddWeekFactor,BdaDayMax,BdaDay,BdaWeek,BdaWeekFactor,AgeLoadingFactor,NoOfPersonFactor,ExchangeRate,IsEndors,EndorsementTypeId,EndorsBy,EndorsDate,IsFromEndorsment,KdPaymentStatus,PaymentDate,IsTransferAS400,TransferDate,CommisionRate,Commision,AreaCodeAs400,CoverageCodeAs400,DurationCodeAs400,AllocationAmount,Age};
  }

  public static BaseProperty getProperty(String columnName) {
    columnName = QueryBuilder.quoteIfNeeded(columnName);
    switch (columnName)  {
      case "`id`":  {
        return id;
      }
      case "`SppaNo`":  {
        return SppaNo;
      }
      case "`SppaDate`":  {
        return SppaDate;
      }
      case "`SppaStatus`":  {
        return SppaStatus;
      }
      case "`ProductCode`":  {
        return ProductCode;
      }
      case "`SubProductCode`":  {
        return SubProductCode;
      }
      case "`PlanCode`":  {
        return PlanCode;
      }
      case "`ZoneId`":  {
        return ZoneId;
      }
      case "`Name`":  {
        return Name;
      }
      case "`CurrencyCode`":  {
        return CurrencyCode;
      }
      case "`PremiumAmount`":  {
        return PremiumAmount;
      }
      case "`PremiumAdditionalAmount`":  {
        return PremiumAdditionalAmount;
      }
      case "`PremiumLoadingAmount`":  {
        return PremiumLoadingAmount;
      }
      case "`PremiumBdaAmount`":  {
        return PremiumBdaAmount;
      }
      case "`ChargeAmount`":  {
        return ChargeAmount;
      }
      case "`StampAmount`":  {
        return StampAmount;
      }
      case "`DiscRate`":  {
        return DiscRate;
      }
      case "`DiscAmount`":  {
        return DiscAmount;
      }
      case "`TotalPremiumAmount`":  {
        return TotalPremiumAmount;
      }
      case "`PromoCode`":  {
        return PromoCode;
      }
      case "`EffectiveDate`":  {
        return EffectiveDate;
      }
      case "`ExpireDate`":  {
        return ExpireDate;
      }
      case "`PolicyNo`":  {
        return PolicyNo;
      }
      case "`AgentCode`":  {
        return AgentCode;
      }
      case "`AgentUserCode`":  {
        return AgentUserCode;
      }
      case "`SppaSubmitDate`":  {
        return SppaSubmitDate;
      }
      case "`SppaSubmitBy`":  {
        return SppaSubmitBy;
      }
      case "`BranchCode`":  {
        return BranchCode;
      }
      case "`SubBranchCode`":  {
        return SubBranchCode;
      }
      case "`TypingBranch`":  {
        return TypingBranch;
      }
      case "`AgeUse`":  {
        return AgeUse;
      }
      case "`TotalDayTravel`":  {
        return TotalDayTravel;
      }
      case "`BasicDayTravel`":  {
        return BasicDayTravel;
      }
      case "`AddDay`":  {
        return AddDay;
      }
      case "`AddWeek`":  {
        return AddWeek;
      }
      case "`AddWeekFactor`":  {
        return AddWeekFactor;
      }
      case "`BdaDayMax`":  {
        return BdaDayMax;
      }
      case "`BdaDay`":  {
        return BdaDay;
      }
      case "`BdaWeek`":  {
        return BdaWeek;
      }
      case "`BdaWeekFactor`":  {
        return BdaWeekFactor;
      }
      case "`AgeLoadingFactor`":  {
        return AgeLoadingFactor;
      }
      case "`NoOfPersonFactor`":  {
        return NoOfPersonFactor;
      }
      case "`ExchangeRate`":  {
        return ExchangeRate;
      }
      case "`IsEndors`":  {
        return IsEndors;
      }
      case "`EndorsementTypeId`":  {
        return EndorsementTypeId;
      }
      case "`EndorsBy`":  {
        return EndorsBy;
      }
      case "`EndorsDate`":  {
        return EndorsDate;
      }
      case "`IsFromEndorsment`":  {
        return IsFromEndorsment;
      }
      case "`KdPaymentStatus`":  {
        return KdPaymentStatus;
      }
      case "`PaymentDate`":  {
        return PaymentDate;
      }
      case "`IsTransferAS400`":  {
        return IsTransferAS400;
      }
      case "`TransferDate`":  {
        return TransferDate;
      }
      case "`CommisionRate`":  {
        return CommisionRate;
      }
      case "`Commision`":  {
        return Commision;
      }
      case "`AreaCodeAs400`":  {
        return AreaCodeAs400;
      }
      case "`CoverageCodeAs400`":  {
        return CoverageCodeAs400;
      }
      case "`DurationCodeAs400`":  {
        return DurationCodeAs400;
      }
      case "`AllocationAmount`":  {
        return AllocationAmount;
      }
      case "`Age`":  {
        return Age;
      }
      default:  {
        throw new IllegalArgumentException("Invalid column name passed. Ensure you are calling the correct table's column");
      }
    }
  }
}
