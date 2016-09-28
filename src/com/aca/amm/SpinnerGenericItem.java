package com.aca.amm;

public class SpinnerGenericItem {
	private final String Code;
	private final String Desc;
	private final String Desc2;

	public SpinnerGenericItem(String code, String desc, String desc2)
	{
		this.Code = code;
		this.Desc = desc;
		this.Desc2 = desc2;
	}
	
	public SpinnerGenericItem(String code, String desc)
	{
		this.Code = code;
		this.Desc = desc;
		this.Desc2 = "";
	}

	public String getCode() {
		return Code;
	}

	public String getDesc() {
		return Desc;
	}

	public String getDesc2() {
		return Desc2;
	}
}
