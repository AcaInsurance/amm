package com.aca.amm;

import java.util.List;

import com.aca.amm.R;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class SpinnerGenericAdapter extends BaseAdapter implements SpinnerAdapter {

	private final List<SpinnerGenericItem> content;
	private final Activity activity;
	
	public SpinnerGenericAdapter(List<SpinnerGenericItem> content, Activity activity) {
		super();
		this.content = content;
		this.activity = activity;
	}
	
	public Activity getActivity()
	{
		return activity;
	}
	
	public int getCount() {
		return content.size();
	}

	public SpinnerGenericItem getItem(int position) {
		return content.get(position);
	}

	public long getItemId(int position) {
		return position;
	}
	
	public int getItemId(String code) {
		for(int i=0; i<content.size(); i++)
		{
			if (content.get(i).getCode().equals(code))
			{
				return i;
			}
		}
		
		return 0;	
	}
	
	public int getItemIdByDesc(String desc) {
		for(int i=0; i<content.size(); i++)
		{
			if (content.get(i).getDesc().equals(desc))
			{
				return i;
			}
		}
		
		return 0;	
	}
	
	public int getItemIdByDesc2(String desc) {
		for(int i=0; i<content.size(); i++)
		{
			if (content.get(i).getDesc2().equals(desc))
			{
				return i;
			}
		}
		
		return 0;	
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final LayoutInflater inflater = activity.getLayoutInflater();	
		final View spinnerItem = inflater.inflate(R.layout.spinner_item_generic, null);				
		
		final TextView code = (TextView)spinnerItem.findViewById(R.id.spinnerGenericCode);
		final TextView desc = (TextView)spinnerItem.findViewById(R.id.spinnerGenericDesc);
		
		final SpinnerGenericItem currentEntry = content.get(position);	
		code.setText(currentEntry.getCode());
		desc.setText(currentEntry.getDesc());
		
		return spinnerItem;
	}
	
}
